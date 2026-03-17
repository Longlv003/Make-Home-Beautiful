var express = require("express");
var router = express.Router();
var accCtrl = require("../controllers/account.controller");
var categoryCtrl = require("../controllers/category.controller");
var productCtrl = require("../controllers/product.controller");
var cartCtrl = require("../controllers/cart.controller");
const jwt = require("../middlewares/auth.middleware");

// Acc
router.post("/verifyFirebaseUser", accCtrl.verifyFirebaseUser);

// Category
router.get("/category/get-list-in-stock", categoryCtrl.GetListCategoryInStock);

// Product
router.get("/product/get-list-in-stock", productCtrl.GetListProductInStock);

// Cart
router.post("/cart/add", jwt.verifyToken, cartCtrl.AddToCart);
router.get("/cart/get", jwt.verifyToken, cartCtrl.GetCart);

module.exports = router;
