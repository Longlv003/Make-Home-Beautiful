const db = require("./db");

const categorySchema = new db.mongoose.Schema(
  {
    category_code: { type: String, required: true, unique: true },
    category_name: { type: String, required: true },
    icon_category: { type: String },
    delete_at: { type: Date, default: null },
  },
  { collection: "categories", timestamps: true },
);

const categoryModel = db.mongoose.model("categoryModel", categorySchema);
module.exports = { categoryModel };
