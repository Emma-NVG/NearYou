const mysql = require("mysql2"); // Documentation : https://www.npmjs.com/package/mysql2

module.exports.connection = mysql.createConnection({
    host: process.env.DATABASE_HOST,
    user: process.env.DATABASE_USER,
    password: process.env.DATABASE_PWD,
    database: process.env.DATABASE_NAME
});
