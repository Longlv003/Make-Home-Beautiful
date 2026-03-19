const {
  VNPay,
  ignoreLogger,
  ProductCode,
  VnpLocale,
  dateFormat,
} = require("vnpay");
const { orderModel } = require("../models/order.model");
const { cartModel } = require("../models/cart.model");

require("dotenv").config();

const vnpay = new VNPay({
  tmnCode: process.env.VNP_TMNCODE,
  secureSecret: process.env.VNP_HASH_SECRET,
  vnpayHost: process.env.VNP_HOST,
  testMode: true,
  hashAlgorithm: "SHA512",
  loggerFn: ignoreLogger,
});

exports.buildVNPayUrl = async (order, req) => {
  const tomorrow = new Date();
  tomorrow.setDate(tomorrow.getDate() + 1);

  const paymentUrl = await vnpay.buildPaymentUrl({
    vnp_Amount: order.total_amount,
    vnp_IpAddr:
      req.headers["x-forwarded-for"] || req.socket.remoteAddress || "127.0.0.1",
    vnp_TxnRef: order._id.toString(),
    vnp_OrderInfo: `Payment for order ${order._id}`,
    vnp_OrderType: ProductCode.Other,
    vnp_ReturnUrl: process.env.VNP_RETURN_URL,
    vnp_Locale: VnpLocale.VN,
    vnp_CreateDate: dateFormat(new Date()),
    vnp_ExpireDate: dateFormat(tomorrow),
  });

  return paymentUrl;
};

exports.vnpayReturn = async (req, res) => {
  try {
    const verify = vnpay.verifyReturnUrl(req.query);
    const orderId = req.query.vnp_TxnRef;

    if (!verify.isSuccess) {
      return res.redirect(
        `${process.env.CLIENT_URL}/payment/result?orderId=${orderId}&status=invalid`,
      );
    }

    // Chỉ redirect về, frontend tự gọi GET /orders/:id để lấy trạng thái thật từ DB
    // Không dựa vào vnp_ResponseCode ở đây vì IPN mới là nơi xử lý chính thức
    return res.redirect(
      `${process.env.CLIENT_URL}/payment/result?orderId=${orderId}`,
    );
  } catch (error) {
    return res.redirect(
      `${process.env.CLIENT_URL}/payment/result?status=error`,
    );
  }
};

exports.vnpayIPN = async (req, res) => {
  try {
    console.log("IPN received:", req.query);

    const verify = vnpay.verifyIpnCall(req.query);

    if (!verify.isSuccess) {
      return res.json({ RspCode: "97", Message: "Invalid signature" });
    }

    const orderId = req.query.vnp_TxnRef;
    const responseCode = req.query.vnp_ResponseCode;
    const vnpAmount = Number(req.query.vnp_Amount);

    const order = await orderModel.findById(orderId);

    if (!order) {
      return res.json({ RspCode: "01", Message: "Order not found" });
    }

    // VNPay gửi amount đã nhân 100 (50.000đ → 5.000.000)
    if (vnpAmount !== order.total_amount * 100) {
      return res.json({ RspCode: "04", Message: "Invalid amount" });
    }

    if (order.order_status === "CANCELLED") {
      return res.json({ RspCode: "02", Message: "Order cancelled" });
    }

    if (responseCode === "00") {
      // Atomic update — chỉ update nếu chưa PAID, tránh race condition khi VNPay gọi IPN nhiều lần
      const updated = await orderModel.findOneAndUpdate(
        { _id: orderId, payment_status: { $ne: "PAID" } },
        {
          payment_status: "PAID",
          order_status: "CONFIRMED",
        },
        { new: true },
      );

      // null → đã được update trước đó rồi, vẫn trả success
      if (!updated) {
        return res.json({ RspCode: "00", Message: "Already confirmed" });
      }

      await cartModel.deleteMany({ account_id: order.account_id });
    } else {
      // Atomic update — tránh ghi đè nếu đã PAID (edge case IPN đến muộn)
      await orderModel.findOneAndUpdate(
        { _id: orderId, payment_status: { $ne: "PAID" } },
        {
          payment_status: "FAILED",
          order_status: "PAYMENT_FAILED",
        },
      );
    }

    return res.json({ RspCode: "00", Message: "Confirm Success" });
  } catch (error) {
    console.error("IPN error:", error);
    return res.json({ RspCode: "99", Message: "Server error" });
  }
};
