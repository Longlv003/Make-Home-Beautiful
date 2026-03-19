const { orderModel } = require("../models/order.model");
const { orderDetailModel } = require("../models/order.detail.model");
const { cartModel } = require("../models/cart.model");
const { buildVNPayUrl } = require("./vnpay.controller");

const PAYMENT_METHODS = {
  BANK_TRANSFER: "BANK_TRANSFER",
  CASH: "CASH",
};

exports.createOrder = async (req, res) => {
  const dataRes = { msg: "", data: null };

  try {
    const account_id = req.user._id;
    const { address_id, paymentMethod } = req.body;

    if (!address_id || !paymentMethod) {
      dataRes.msg = "Missing required fields";
      return res.status(400).json(dataRes);
    }

    if (!Object.values(PAYMENT_METHODS).includes(paymentMethod)) {
      dataRes.msg = `Invalid payment method. Must be one of: ${Object.values(PAYMENT_METHODS).join(", ")}`;
      return res.status(400).json(dataRes);
    }

    // Chỉ block nếu đang có order PENDING + UNPAID thật sự (chưa qua VNPay lần nào)
    // Không block nếu order trước đó đã PAYMENT_FAILED — user được phép tạo đơn mới
    const existingOrder = await orderModel.findOne({
      account_id,
      order_status: "PENDING",
      payment_status: "UNPAID",
    });

    if (existingOrder) {
      dataRes.msg = "You have an unpaid order";
      dataRes.data = existingOrder;
      return res.status(400).json(dataRes);
    }

    const cartItems = await cartModel.find({ account_id });

    if (!cartItems.length) {
      dataRes.msg = "Cart is empty";
      return res.status(400).json(dataRes);
    }

    const total_amount = cartItems.reduce(
      (sum, item) => sum + item.price * item.quantity,
      0,
    );

    const order = await orderModel.create({
      account_id,
      address_id,
      total_amount,
      payment_method: paymentMethod,
      payment_status: "UNPAID",
      order_status: "PENDING",
    });

    const orderDetailsData = cartItems.map((item) => ({
      orderId: order._id,
      productId: item.productId,
      quantity: item.quantity,
      price: item.price,
    }));

    await orderDetailModel.insertMany(orderDetailsData);

    if (paymentMethod === PAYMENT_METHODS.CASH) {
      await cartModel.deleteMany({ account_id });
    }

    if (paymentMethod === PAYMENT_METHODS.BANK_TRANSFER) {
      const paymentUrl = await buildVNPayUrl(order, req);
      dataRes.data = { order, paymentUrl };
    } else {
      dataRes.data = { order };
    }

    dataRes.msg = "OK";
    return res.status(201).json(dataRes);
  } catch (error) {
    console.error(error);
    dataRes.msg = "Server error";
    return res.status(500).json(dataRes);
  }
};

exports.cancelOrder = async (req, res) => {
  const dataRes = { msg: "", data: null };

  try {
    const account_id = req.user._id;
    const { orderId } = req.params;

    const order = await orderModel.findOne({ _id: orderId, account_id });

    if (!order) {
      dataRes.msg = "Order not found";
      return res.status(404).json(dataRes);
    }

    // Cho phép cancel cả PENDING lẫn PAYMENT_FAILED
    const cancellableStatuses = ["PENDING", "PAYMENT_FAILED"];
    if (
      !cancellableStatuses.includes(order.order_status) ||
      order.payment_status === "PAID"
    ) {
      dataRes.msg = "Cannot cancel this order";
      return res.status(400).json(dataRes);
    }

    await orderModel.findByIdAndUpdate(orderId, {
      order_status: "CANCELLED",
    });

    dataRes.msg = "Order cancelled";
    return res.status(200).json(dataRes);
  } catch (error) {
    console.error(error);
    dataRes.msg = "Server error";
    return res.status(500).json(dataRes);
  }
};

exports.retryPayment = async (req, res) => {
  const dataRes = { msg: "", data: null };

  try {
    const account_id = req.user._id;
    const { orderId } = req.params;

    const order = await orderModel.findOne({ _id: orderId, account_id });

    if (!order) {
      dataRes.msg = "Order not found";
      return res.status(404).json(dataRes);
    }

    if (order.payment_status === "PAID") {
      dataRes.msg = "Order already paid";
      return res.status(400).json(dataRes);
    }

    if (order.order_status === "CANCELLED") {
      dataRes.msg = "Order has been cancelled";
      return res.status(400).json(dataRes);
    }

    // Chỉ cho retry khi order đang ở trạng thái PAYMENT_FAILED
    if (order.order_status !== "PAYMENT_FAILED") {
      dataRes.msg = "Order cannot be retried";
      return res.status(400).json(dataRes);
    }

    // Reset về PENDING + UNPAID trước khi tạo URL mới
    await orderModel.findByIdAndUpdate(order._id, {
      order_status: "PENDING",
      payment_status: "UNPAID",
    });

    const paymentUrl = await buildVNPayUrl(order, req);

    dataRes.data = { paymentUrl };
    dataRes.msg = "OK";
    return res.status(200).json(dataRes);
  } catch (error) {
    console.error(error);
    dataRes.msg = "Server error";
    return res.status(500).json(dataRes);
  }
};

exports.getBill = async (req, res) => {
  try {
    const account_id = req.user._id;

    const orders = await orderModel
      .find({ account_id })
      .sort({ createdAt: -1 });

    const ordersWithDetails = await Promise.all(
      orders.map(async (order) => {
        const orderDetails = await orderDetailModel
          .find({ orderId: order._id })
          .populate("product_id");

        return {
          ...order.toObject(),
          orderDetails,
        };
      }),
    );

    return res.status(200).json({ msg: "OK", data: ordersWithDetails });
  } catch (error) {
    console.error(error);
    return res.status(500).json({ msg: "Server error", data: null });
  }
};
