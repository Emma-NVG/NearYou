const DataObject = require("../DataObject");

module.exports = class User extends DataObject {

    /** @type {number} **/ ID
    /** @type {String} **/ email
    /** @type {String} **/ password
    /** @type {String} **/ url_profile
    /** @type {String} **/ surname
    /** @type {String} **/ first_name
    /** @type {number} **/ age
    /** @type {String} **/ custom_status
    /** @type {Boolean} **/ is_public
    /** @type {Array<Link>} **/ links
    /** @type {number} **/ distance
    /** @type {Date} **/ created_date
    /** @type {Date} **/ edited_date


    /**
     * @param {number} ID
     * @param {String} email
     * @param {String} password
     * @param {String} url_profile
     * @param {String} surname
     * @param {String} first_name
     * @param {number} age
     * @param {String} custom_status
     * @param {Boolean} is_public
     * @param {Array<Link>} links
     * @param {number} distance
     * @param {Date} created_date
     * @param {Date} edited_date
     */
    constructor(ID, email, password, url_profile, surname, first_name, age, custom_status, is_public, links, distance = 0, created_date, edited_date) {
        super();

        this.ID = ID;
        this.email = email;
        this.password = password;
        this.url_profile = url_profile;
        this.surname = surname;
        this.first_name = first_name;
        this.age = age;
        this.custom_status = custom_status;
        this.links = links;
        this.distance = distance;
        this.is_public = is_public;
        this.created_date = created_date;
        this.edited_date = edited_date;
    }
};
