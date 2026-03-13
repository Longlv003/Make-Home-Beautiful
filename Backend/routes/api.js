var express = require("express");
var router = express.Router();
var accCtrl = require("../controllers/account.controller");
var categoryCtrl = require("../controllers/category.controller");

// Acc
router.post("/verifyFirebaseUser", accCtrl.verifyFirebaseUser);

// Category
router.get("/category/getlist-instock", categoryCtrl.GetListCategoryInStock);

module.exports = router;
