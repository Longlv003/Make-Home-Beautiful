var express = require("express");
var router = express.Router();
var accCtrl = require("../controllers/account.controller");

router.post("/verifyFirebaseUser", accCtrl.verifyFirebaseUser);

module.exports = router;
