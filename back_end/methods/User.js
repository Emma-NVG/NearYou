const { connection } = require("../database/config");
const User = require("../models/DataObject/User");
const { retrieveAllUserLinks } = require("./Link");
const async = require("async");

module.exports = {
    login(email, pwd) {
        return connection.promise().execute(
            "SELECT ID, EMAIL, PASSWORD, URL_PROFILE, SURNAME, FIRST_NAME, AGE, CUSTOM_STATUS, IS_PUBLIC, DATE_CREATED, DATE_EDITED FROM `user` WHERE EMAIL = ? AND PASSWORD = ?",
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
                        return new User(resultSQL['ID'], resultSQL['EMAIL'], resultSQL['PASSWORD'], resultSQL['URL_PROFILE'], resultSQL['SURNAME'], resultSQL['FIRST_NAME'], resultSQL['AGE'], resultSQL['CUSTOM_STATUS'], resultSQL['IS_PUBLIC'], userLinks, resultSQL['DATE_CREATED'], resultSQL['DATE_EDITED']);
                    });
                } else {
                    return null;
                }
            });
    },
    retrieveUserData(ID_user) {
        return connection.promise().execute(
            "SELECT ID, EMAIL, PASSWORD, URL_PROFILE, SURNAME, FIRST_NAME, AGE, CUSTOM_STATUS, IS_PUBLIC, DATE_CREATED, DATE_EDITED FROM `user` WHERE ID = ?",
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
                        return new User(resultSQL['ID'], resultSQL['EMAIL'], resultSQL['PASSWORD'], resultSQL['URL_PROFILE'], resultSQL['SURNAME'], resultSQL['FIRST_NAME'], resultSQL['AGE'], resultSQL['CUSTOM_STATUS'], resultSQL['IS_PUBLIC'], userLinks, resultSQL['DATE_CREATED'], resultSQL['DATE_EDITED']);
                    });
                } else {
                    return null;
                }
            });
    },
}
