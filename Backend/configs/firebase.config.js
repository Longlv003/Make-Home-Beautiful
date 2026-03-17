const admin = require("firebase-admin");
const serviceAccount = require("./kotlin-db34e-firebase-adminsdk-fbsvc-49756c0804.json");

admin.initializeApp({
  credential: admin.credential.cert(serviceAccount),
});

module.exports = admin;
