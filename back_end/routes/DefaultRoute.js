const ResponseData = require("../models/ResponseData");
const DataObject = require("../models/DataObject");
const ResponseCode = require("../models/ResponseCode");

module.exports.includeDefaultRoute = (app) => {
    app.use((req, res) => {
        const responseData = new ResponseData("Unknown route !", ResponseCode.E_UnknownRoute, new DataObject());
        res.status(404).json(responseData);
    })
}
