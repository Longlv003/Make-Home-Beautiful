const db = require("./db");

const orderSchema = new db.mongoose.Schema(
  {
    account_id: {
      type: db.mongoose.Schema.Types.ObjectId,
      ref: "accModel",
      required: true,
    },
    address_id: {
      type: db.mongoose.Schema.Types.ObjectId,
      ref: "addressModel",
      required: true,
    },
    total_amount: {
      type: Number,
      required: true,
      default: 0,
    },
    shipping: { type: Number, required: true },
    finalAmount: { type: Number, required: true }, // số tiền thanh toán
    payment_method: {
      type: String,
      enum: ["CASH", "BANK_TRANSFER"],
      default: "CASH",
      required: true,
    },
    payment_status: {
      type: String,
      enum: [
        "UNPAID", // Chưa thanh toán
        "PAID", // Đã thanh toán
        "FAILED", // Thanh toán thất bại
        "REFUNDED", //  Đã hoàn tiền
      ],
      default: "UNPAID",
      required: true,
    },
    order_status: {
      type: String,
      enum: [
        "PENDING", // Vừa tạo đơn
        "CONFIRMED", // Đã xác nhận
        "SHIPPING", // Đang giao
        "DELIVERED", // Giao thành công
        "CANCELLED", // Hủy đơn
        "PAYMENT_FAILED", // Thanh toán thất bại
      ],
      default: "PENDING",
      required: true,
    },
  },
  { collection: "orders", timestamps: true },
);

const orderModel = db.mongoose.model("orderModel", orderSchema);
module.exports = { orderModel };
