const db = require("./db");

const orderDetailSchema = new db.mongoose.Schema(
  {
    order_id: {
      type: db.mongoose.Schema.Types.ObjectId,
      ref: "orderModel",
      required: true,
    },
    product_id: {
      type: db.mongoose.Schema.Types.ObjectId,
      ref: "productModel",
      required: true,
    },
    quantity: { type: Number, default: 1 },
    price: { type: Number, default: 0 },
  },
  { collection: "order_details", timestamps: true },
);

const orderDetailModel = db.mongoose.model(
  "orderDetailModel",
  orderDetailSchema,
);

module.exports = { orderDetailModel };
