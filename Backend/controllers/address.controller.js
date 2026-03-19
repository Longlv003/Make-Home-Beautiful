const { addressModel } = require("../models/address.model");

exports.AddAddress = async (req, res, next) => {
  try {
    const { name, phone, address } = req.body;

    if (!name || !phone || !address) {
      return res.status(400).json({
        success: false,
        message: "Vui lòng điền đầy đủ thông tin",
      });
    }

    const newAddress = new addressModel({
      account_id: req.user.id,
      name,
      phone,
      address,
    });

    const saved = await newAddress.save();

    return res.status(201).json({
      success: true,
      message: "Thêm địa chỉ thành công",
      data: saved,
    });
  } catch (error) {
    return res.status(500).json({
      success: false,
      message: "Lỗi server",
      error: error.message,
    });
  }
};

exports.GetAddresses = async (req, res, next) => {
  try {
    const addresses = await addressModel.find({
      account_id: req.user.id,
      deleted_at: null,
    });

    return res.status(200).json({
      success: true,
      data: addresses,
    });
  } catch (error) {
    return res.status(500).json({
      success: false,
      message: "Lỗi server",
      error: error.message,
    });
  }
};
