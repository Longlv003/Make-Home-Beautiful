const db = require("./db");

const productSchema = new db.mongoose.Schema(
  {
    category_id: {
      type: db.mongoose.Schema.Types.ObjectId,
      required: true,
      ref: "categoryModel",
    },
    product_name: { type: String, required: true },
    description: { type: String },
    quantity: { type: Number, default: 0, min: 0 },
    price: { type: Number, default: 0, min: 0 },
    image: { type: String },
    delete_at: { type: Date, default: null },
  },
  { collection: "products", timestamps: true },
);

const productModel = db.mongoose.model("productModel", productSchema);
module.exports = { productModel };
