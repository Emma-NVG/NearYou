const SHA256 = require("crypto-js/sha256");
const BaseHEX = require("crypto-js/enc-hex");

function hashData(data) {
    return BaseHEX.stringify(SHA256(data))
}

module.exports = {
    hashData
}
