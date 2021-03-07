const { connection } = require("../database/config");
const Link = require("../models/DataObject/Link");

module.exports = {
    retrieveAllUserLinks(id_user) {
        return connection.promise().execute(
            "SELECT ID, LINK, DATE_CREATED, DATE_EDITED FROM `link` WHERE ID_USER = ?",
            [id_user])
            .then(([rows]) => {
                return rows.map(data => {
                    return new Link(data['ID'], data['LINK'], data['DATE_CREATED'], data['DATE_EDITED']);
                });
            });
    },
}
