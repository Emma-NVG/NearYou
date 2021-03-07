module.exports = class ResponseData {

    /** @type {String} **/ message
    /** @type {String} **/ code
    /** @type {DataObject} **/ data


    /**
     * @param {String} message
     * @param {String} code
     * @param {DataObject} data
     */
    constructor(message, code, data) {
        this.message = message;
        this.code = code;
        this.data = data;
    }
};
