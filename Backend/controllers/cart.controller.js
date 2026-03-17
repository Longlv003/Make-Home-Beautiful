const { cartModel } = require("../models/cart.model");

exports.AddToCart = async (req, res) => {
  let dataRes = { msg: "OK" };

  try {
    const { product_id, quantity, price } = req.body;
    const account_id = req.user._id;

    if (!product_id || !quantity || !price) {
      dataRes.msg = "Missing required fields";
      return res.status(400).json(dataRes);
    }

    const existingItem = await cartModel.findOne({ account_id, product_id });

    if (existingItem) {
      existingItem.quantity += quantity;
      existingItem.price = price;
      await existingItem.save();
      dataRes.data = existingItem;
    } else {
      const newItem = await cartModel.create({
        account_id,
        product_id,
        quantity,
        price,
      });
      dataRes.data = newItem;
    }
  } catch (error) {
    dataRes.msg = error.message;
  }

  res.json(dataRes);
};

exports.GetCart = async (req, res) => {
  let dataRes = { msg: "OK" };
  try {
    const account_id = req.user._id;

    const cartItems = await cartModel
      .find({ account_id })
      .populate("product_id")
      .sort({ createdAt: -1 });

    dataRes.data = cartItems.map((item) => ({
      ...item._doc,
      product_id: {
        ...item.product_id._doc,
        image: `${req.protocol}://${req.get("host")}/images/products/${item.product_id.image}`,
      },
    }));
  } catch (error) {
    dataRes.msg = error.message;
  }
  res.json(dataRes);
};
