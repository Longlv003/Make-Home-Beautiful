const admin = require("../configs/firebase.config");
const { accModel } = require("../models/account.model");

const verifyToken = async (req, res, next) => {
  try {
    const authHeader = req.headers.authorization;

    if (!authHeader || !authHeader.startsWith("Bearer ")) {
      return res.status(401).json({ msg: "Unauthorized: No token provided" });
    }

    const token = authHeader.split(" ")[1];
    // console.log("Token nhận được:", token.substring(0, 20) + "...");

    const decodedToken = await admin.auth().verifyIdToken(token);
    // console.log("Token hợp lệ, uid:", decodedToken.uid);

    const account = await accModel.findOne({ firebase_uid: decodedToken.uid });
    // console.log("Account tìm được:", account);

    if (!account) return res.status(401).json({ msg: "Account not found" });

    req.user = account;
    next();
  } catch (error) {
    console.log("Lỗi verify token:", error.message);
    return res.status(401).json({ msg: "Invalid token" });
  }
};

module.exports = { verifyToken };
