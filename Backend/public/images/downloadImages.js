const fs = require("fs");
const https = require("https");

const folder = "./images";

if (!fs.existsSync(folder)) {
  fs.mkdirSync(folder);
}

function download(url, filename) {
  return new Promise((resolve, reject) => {
    https
      .get(url, (res) => {
        if (res.statusCode === 302 || res.statusCode === 301) {
          return download(res.headers.location, filename)
            .then(resolve)
            .catch(reject);
        }

        const file = fs.createWriteStream(filename);

        res.pipe(file);

        file.on("finish", () => {
          file.close(resolve);
        });
      })
      .on("error", reject);
  });
}

async function run() {
  for (let c = 1; c <= 10; c++) {
    for (let i = 1; i <= 10; i++) {
      const code = "cat" + String(c).padStart(3, "0");
      const filename = `${folder}/${code}_${i}.jpg`;

      const url = `https://picsum.photos/400/400`;

      await download(url, filename);

      console.log("Downloaded:", filename);
    }
  }
}

run();
