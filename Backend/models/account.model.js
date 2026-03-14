const db = require("./db");

const accSchema = new db.mongoose.Schema(
  {
    firebase_uid: { type: String, required: true, unique: true },
    name: { type: String },
    email: {
      type: String,
      required: true,
      unique: true,
      lowercase: true,
      trim: true,
    },
    role: {
      type: String,
      enum: ["superAdmin", "admin", "user"],
      default: "user",
    },
    image: { type: String, default: null },
    is_active: { type: Boolean, default: true },
    deleted_at: { type: Date, default: null },
  },
  { collection: "accounts", timestamps: true },
);

let accModel = db.mongoose.model("accModel", accSchema);
module.exports = { accModel };
