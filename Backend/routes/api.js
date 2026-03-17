var express = require("express");
var router = express.Router();
var accCtrl = require("../controllers/account.controller");
var categoryCtrl = require("../controllers/category.controller");
var productCtrl = require("../controllers/product.controller");

// Acc
router.post("/verifyFirebaseUser", accCtrl.verifyFirebaseUser);

// Category
router.get("/category/get-list-in-stock", categoryCtrl.GetListCategoryInStock);

// Product
router.get("/product/get-list-in-stock", productCtrl.GetListProductInStock)

module.exports = router;
