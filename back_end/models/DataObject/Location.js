const DataObject = require("../DataObject");

module.exports = class Location extends DataObject{

    /** @type {number} **/ latitude
    /** @type {number} **/ longitude
    /** @type {Date} **/ date


    /**
     * @param {number} latitude
     * @param {number} longitude
     * @param {Date} date
     */
    constructor(latitude, longitude, date) {
        super();

        this.latitude = latitude;
        this.longitude = longitude;
        this.date = date;
    }
};
