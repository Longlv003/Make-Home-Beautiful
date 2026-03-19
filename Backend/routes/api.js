var express = require("express");
var router = express.Router();
var accCtrl = require("../controllers/account.controller");
var categoryCtrl = require("../controllers/category.controller");
var productCtrl = require("../controllers/product.controller");
var cartCtrl = require("../controllers/cart.controller");
const jwt = require("../middlewares/auth.middleware");
var orderCtrl = require("../controllers/order.controller");
var vnpayCtrl = require("../controllers/vnpay.controller");
var addressCtrl = require("../controllers/address.controller");

// Acc
router.post("/verifyFirebaseUser", accCtrl.verifyFirebaseUser);

// Address
router.post("/account/address", jwt.verifyToken, addressCtrl.AddAddress);
router.get("/account/address", jwt.verifyToken, addressCtrl.GetAddresses);

// Category
router.get("/category/get-list-in-stock", categoryCtrl.GetListCategoryInStock);

// Product
router.get("/product/get-list-in-stock", productCtrl.GetListProductInStock);

// Cart
router.post("/cart/add", jwt.verifyToken, cartCtrl.AddToCart);
router.get("/cart/get", jwt.verifyToken, cartCtrl.GetCart);

// Checkout
router.post("/checkout", jwt.verifyToken, orderCtrl.createOrder);

// Retry payment
router.get(
  "/order/:orderId/retry-payment",
  jwt.verifyToken,
  orderCtrl.retryPayment,
);

// Cancel order
router.patch("/order/:orderId/cancel", jwt.verifyToken, orderCtrl.cancelOrder);

// Get bill
router.get("/order/my-bill", jwt.verifyToken, orderCtrl.getBill);

// VNPay
router.get("/payment/vnpay-return", vnpayCtrl.vnpayReturn);
router.get("/payment/vnpay-ipn", vnpayCtrl.vnpayIPN);

module.exports = router;
