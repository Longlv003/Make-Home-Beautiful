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

exports.UpdateAddress = async (req, res, next) => {
  try {
    const { id } = req.params;
    const { name, phone, address } = req.body;

    if (!name || !phone || !address) {
      return res.status(400).json({
        success: false,
        message: "Vui lòng điền đầy đủ thông tin",
      });
    }

    const updated = await addressModel.findOneAndUpdate(
      { _id: id, account_id: req.user.id, delete_at: null }, // ← chỉ update của chính user
      { name, phone, address },
      { new: true },
    );

    if (!updated) {
      return res.status(404).json({
        success: false,
        message: "Không tìm thấy địa chỉ",
      });
    }

    return res.status(200).json({
      success: true,
      message: "Cập nhật địa chỉ thành công",
      data: updated,
    });
  } catch (error) {
    return res.status(500).json({
      success: false,
      message: "Lỗi server",
      error: error.message,
    });
  }
};

exports.DeleteAddress = async (req, res, next) => {
  try {
    const { id } = req.params;

    const deleted = await addressModel.findOneAndUpdate(
      { _id: id, account_id: req.user.id, delete_at: null }, // ← soft delete, chỉ xóa của chính user
      { delete_at: new Date() },
      { new: true },
    );

    if (!deleted) {
      return res.status(404).json({
        success: false,
        message: "Không tìm thấy địa chỉ",
      });
    }

    return res.status(200).json({
      success: true,
      message: "Xóa địa chỉ thành công",
    });
  } catch (error) {
    return res.status(500).json({
      success: false,
      message: "Lỗi server",
      error: error.message,
    });
  }
};
