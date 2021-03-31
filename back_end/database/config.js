const mysql = require("mysql2"); // Documentation : https://www.npmjs.com/package/mysql2

module.exports.connection = mysql.createConnection({
    host: process.env.NEARYOU_DATABASE_HOST,
    user: process.env.NEARYOU_DATABASE_USER,
    password: process.env.NEARYOU_DATABASE_PWD,
    database: process.env.NEARYOU_DATABASE_NAME
});
