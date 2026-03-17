const admin = require("../configs/firebase.config");
const { accModel } = require("../models/account.model");

exports.verifyFirebaseUser = async (req, res, next) => {
  try {
    const authHeader = req.headers.authorization;

    if (!authHeader) {
      return res.status(401).json({
        dataRes: { msg: "Missing Authorization header", data: null },
      });
    }

    const token = authHeader.split("Bearer ")[1];

    const decodedToken = await admin.auth().verifyIdToken(token);

    const { uid, email, picture } = decodedToken;
    const { name } = req.body;

    let user = await accModel.findOne({ firebase_uid: uid });

    if (!user) {
      user = new accModel({
        firebase_uid: uid,
        email,
        name: name || null,
        image: picture || null,
      });

      await user.save();
    }

    return res.status(200).json({
      dataRes: {
        msg: "Verify success",
        data: user,
      },
    });
  } catch (err) {
    console.error(err);
    return res.status(401).json({
      dataRes: { msg: "Invalid Firebase token", data: null },
    });
  }
};
