const DataObject = require("../DataObject");

module.exports = class Link extends DataObject{

    /** @type {number} **/ ID
    /** @type {String} **/ link
    /** @type {Date} **/ created_date
    /** @type {Date} **/ edited_date


    /**
     * @param {number} ID
     * @param {String} link
     * @param {Date} created_date
     * @param {Date} edited_date
     */
    constructor(ID, link, created_date, edited_date) {
        super();

        this.ID = ID;
        this.link = link;
        this.created_date = created_date;
        this.edited_date = edited_date;
    }
};
