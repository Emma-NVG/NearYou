const ResponseData = require("../models/Response/ResponseData");
const ResponseCode = require("../models/Response/ResponseCode");
const DataObject = require("../models/DataObject");

module.exports.includeDefaultRoute = (app) => {
    app.use((req, res) => {
        const responseData = new ResponseData("Unknown route !", ResponseCode.E_UnknownRoute, new DataObject());
        res.status(404).json(responseData);
    })
}
