const { categoryModel } = require("../models/category.model");
const { productModel } = require("../models/product.model");

exports.GetListCategoryInStock = async (req, res, next) => {
  let dataRes = { msg: "OK" };

  try {
    // Lấy category_id từ product còn hàng
    const categoryIds = await productModel.distinct("category_id", {
      quantity: { $gt: 0 },
      delete_at: null,
    });

    if (!categoryIds.length) {
      dataRes.msg = "No categories found";
      dataRes.data = [];
    } else {
      // Lấy category chưa bị xoá
      const categories = await categoryModel
        .find({
          _id: { $in: categoryIds },
          delete_at: null,
        })
        .sort({ createdAt: -1 });

      // dataRes.data = categories;

      dataRes.data = categories.map((cat) => ({
        ...cat._doc,
        icon_category: `${req.protocol}://${req.get("host")}/images/categories/${cat.icon_category}`,
      }));
    }
  } catch (error) {
    dataRes.msg = error.message;
  }

  res.json(dataRes);
};
