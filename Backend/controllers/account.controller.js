const admin = require("../config/firebaseAdmin");
const { accModel } = require("../models/account.model");

exports.doReg = async (req, res, next) => {
  try {
    const { firebase_uid, name, email } = req.body;

    if (!firebase_uid || !email) {
      return res
        .status(400)
        .json({ dataRes: { msg: "Missing required fields", data: null } });
    }

    const existed = await accModel.findOne({ firebase_uid }).lean();
    if (existed) {
      return res
        .status(400)
        .json({ dataRes: { msg: "Account already exists", data: null } });
    }

    const newUser = new accModel({
      firebase_uid,
      name: name || null,
      email,
    });

    await newUser.save();

    return res.status(201).json({
      dataRes: {
        msg: "Register successful",
        data: newUser,
      },
    });
  } catch (err) {
    console.error(err);
    return res
      .status(500)
      .json({ dataRes: { msg: "Server error", data: null } });
  }
};

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

    const { uid, email, name, picture } = decodedToken;

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
