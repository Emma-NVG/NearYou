const ResponseData = require("../models/Response/ResponseData");
const ResponseCode = require("../models/Response/ResponseCode");
const DataObject = require("../models/DataObject");

const jwt = require("jsonwebtoken");

module.exports = {
    authenticateModule(req, res, next) {
        const authorizationHeader = req.headers.authorization;

        if (!authorizationHeader) {
            const responseData = new ResponseData("Authentication token not included !", ResponseCode.E_NoToken, new DataObject());
            return res.status(400).json(responseData);
        } else {
            const token = authorizationHeader.split(" ")[1];
            jwt.verify(token, process.env.NEARYOU_SECURITY_KEY, (error, decodedToken) => {
                if (error) {
                    const responseData = new ResponseData("You are not authorized to make this request !", ResponseCode.E_Unauthorized, new DataObject());
                    return res.status(401).json(responseData);
                } else {
                    const userID = decodedToken.ID;

                    if (req.params.ID && req.params.ID !== userID) {
                        const responseData = new ResponseData("Token invalid !", ResponseCode.E_Unauthorized, new DataObject());
                        return res.status(401).json(responseData);
                    } else {
                        return next();
                    }
                }
            });
        }
    }
}
