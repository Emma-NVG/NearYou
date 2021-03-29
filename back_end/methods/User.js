const { connection } = require("../database/config");
const User = require("../models/DataObject/User");
const { retrieveAllUserLinks } = require("./Link");
const async = require("async");

function createAccount(email, pwd, surname, first_name, age) {
    return connection.promise().execute(
        "INSERT INTO user (EMAIL, PASSWORD, SURNAME, FIRST_NAME, AGE) VALUES (?, ?, ?, ?, ?)",
        [email, pwd, surname, first_name, age])
        .then(([rows]) => {
            if (rows.insertId != null) {
                return login(email, pwd);
            } else {
                throw '';
            }
        });
}

function login(email, pwd) {
    return connection.promise().execute(
        "SELECT ID, URL_PROFILE, SURNAME, FIRST_NAME, AGE, CUSTOM_STATUS, IS_PUBLIC, DATE_CREATED, DATE_EDITED FROM `user` WHERE EMAIL = ? AND PASSWORD = ?",
        [email, pwd])
        .then(([rows]) => {
            const resultSQL = rows[0];
            if (resultSQL != null) {
                return async.parallel([
                    function (callback) {
                        retrieveAllUserLinks(resultSQL['ID']).then((data) => {
                            callback(null, data);
                        })
                    }
                ]).then((data) => {
                    const userLinks = data[0];
                    return new User(resultSQL['ID'], resultSQL['URL_PROFILE'], resultSQL['SURNAME'], resultSQL['FIRST_NAME'], resultSQL['AGE'], resultSQL['CUSTOM_STATUS'], resultSQL['IS_PUBLIC'], userLinks, 0, resultSQL['DATE_CREATED'], resultSQL['DATE_EDITED']);
                });
            } else {
                return null;
            }
        });
}

function addLocation(ID, latitude, longitude) {
    return connection.promise().execute(
        "INSERT INTO location (ID_USER, COORDINATE) VALUES (?, Point(?, ?))",
        [ID, latitude, longitude])
        .then(([rows]) => {
            return rows;
        });
}

function retrieveUserData(ID_user) {
    return connection.promise().execute(
        "SELECT ID, URL_PROFILE, SURNAME, FIRST_NAME, AGE, CUSTOM_STATUS, IS_PUBLIC, DATE_CREATED, DATE_EDITED FROM `user` WHERE ID = ?",
        [ID_user])
        .then(([rows]) => {
            const resultSQL = rows[0];
            if (resultSQL != null) {
                return async.parallel([
                    function (callback) {
                        retrieveAllUserLinks(resultSQL['ID']).then((data) => {
                            callback(null, data);
                        })
                    }
                ]).then((data) => {
                    const userLinks = data[0];
                    return new User(resultSQL['ID'], resultSQL['URL_PROFILE'], resultSQL['SURNAME'], resultSQL['FIRST_NAME'], resultSQL['AGE'], resultSQL['CUSTOM_STATUS'], resultSQL['IS_PUBLIC'], userLinks, resultSQL['DATE_CREATED'], resultSQL['DATE_EDITED']);
                });
            } else {
                return null;
            }
        });
}

module.exports = {
    createAccount,
    login,
    addLocation,
    retrieveUserData
}
