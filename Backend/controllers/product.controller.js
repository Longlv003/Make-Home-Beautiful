const { productModel } = require("../models/product.model");
const { getImageUrl } = require("../helpers/imageHelper");

exports.GetListProductInStock = async (req, res, next) => {
  let dataRes = { msg: "OK" };

  try {
    let filter = {
      quantity: { $gt: 0 },
      delete_at: null,
    };

    if (req.query.category_id) {
      filter.category_id = req.query.category_id;
    }

    const products = await productModel
      .find(filter)
      // .populate("category_id")
      .sort({ createdAt: -1 });

    if (!products.length) {
      dataRes.msg = "No products found";
      dataRes.data = [];
    } else {
      // dataRes.data = products;

      dataRes.data = products.map((pro) => ({
        ...pro._doc,
        image: `${req.protocol}://${req.get("host")}/images/products/${pro.image}`,
      }));
    }
  } catch (error) {
    dataRes.msg = error.message;
  }

  res.json(dataRes);
};
