const { connection } = require("../database/config");
const Location = require("../models/DataObject/Location");
const { retrieveUserData } = require("./User");
const { retrieveAllUserLinks } = require("./Link");
const User = require("../models/DataObject/User");
const async = require("async");

module.exports = {
    async retrieveLastUserLocation(id_user) {
        return connection.promise().execute(
            "SELECT COORDINATE, DATE FROM `location` WHERE ID_USER = ? ORDER BY DATE DESC LIMIT 1",
            [id_user])
            .then(([rows]) => {
                if (rows[0] != null) {
                    return new Location(1, 1, rows[0]['DATE']);
                } else {
                    return null;
                }
            });
    },
    retrieveAllUserNear(id_user) {
        return connection.promise().execute(
            "SELECT l2.ID_USER, ST_Distance_Sphere(L1.COORDINATE, L2.COORDINATE) as DISTANCE\n" +
            " FROM location l1\n" +
            "    JOIN location l2 on l2.ID_USER != l1.ID_USER\n" +
            "    JOIN user u on l2.ID_USER = u.ID\n" +
            " WHERE l1.ID_USER = ?\n" +
            "  AND ST_Distance_Sphere(L1.COORDINATE, L2.COORDINATE) < 5000\n" +
            "  AND u.IS_PUBLIC = true\n" +
            "  AND l2.DATE = (\n" +
            "      SELECT l3.DATE\n" +
            "      FROM location l3\n" +
            "      WHERE l3.ID_USER = l2.ID_USER\n" +
            "      ORDER BY l3.DATE DESC\n" +
            "      LIMIT 1);",
            [id_user])
            .then(([rows]) => {
                const resultSQL = rows[0];
                if (resultSQL != null) {
                    return async.parallel([
                        function (callback) {
                            retrieveUserData(resultSQL['ID_USER']).then((data) => {
                                callback(null, data);
                            })
                        },
                    ]).then((data) => {
                        const user = data[0];
                        user.distance = resultSQL['DISTANCE'];
                        return user;
                    });
                } else {
                    return null;
                }
            });
    }
}