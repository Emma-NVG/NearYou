const ResponseData = require("../models/Response/ResponseData");
const ResponseCode = require("../models/Response/ResponseCode");
const DataObject = require("../models/DataObject");
const { login } = require("../methods/User");
const jwt = require("jsonwebtoken");
const { retrieveUserData } = require("../methods/User");
const { authenticateModule } = require("../security/middleware");
const { retrieveAllUserNear } = require("../methods/Location");

module.exports.includeAllUserRoutes = (app) => {
    loginRoute(app);
    generateAccessTokenRoute(app);

    retrieveUserDataRoute(app);
    retrieveAllUserNearRoute(app);
}

const loginRoute = (app) => {
    app.get("/api/:version/user/login", (req, res) => {
        login(req.body.email, req.body.password).then((data) => {
            if (!!data) {
                data["token"] = jwt.sign({ID: data.ID}, process.env.SECURITY_KEY);

                const responseData = new ResponseData("Login success !", ResponseCode.S_Success, data);
                res.status(200).json(responseData)
            } else {
                const responseData = new ResponseData("Login failed !", ResponseCode.E_UnknownError, new DataObject());
                res.status(500).json(responseData)
            }
        })
    });
}

const retrieveAllUserNearRoute = (app) => {
    app.get("/api/:version/user/:id/near", authenticateModule, (req, res) => {
        retrieveAllUserNear(req.params.id).then((data) => {
            if (!!data) {
                Promise.all(data).then((users) => {
                    const responseData = new ResponseData("Successful recovery of users close to you !", ResponseCode.S_Success, users);
                    res.status(200).json(responseData)
                }).catch(() => {
                    const responseData = new ResponseData("An unknown error occurred !", ResponseCode.E_UnknownError, new DataObject());
                    res.status(500).json(responseData)
                })
            } else {
                const responseData = new ResponseData("An unknown error occurred !", ResponseCode.E_UnknownError, new DataObject());
                res.status(500).json(responseData)
            }
        })
    });
}

const generateAccessTokenRoute = (app) => {
    app.get("/api/:version/user/:id/access", authenticateModule, (req, res) => {
        const data = {
            token : jwt.sign({ID: req.params.id}, process.env.SECURITY_KEY, { expiresIn: "1m"})
        }

        const responseData = new ResponseData("Token access generated !", ResponseCode.S_Success, data);
        res.status(200).json(responseData)
    });
}

const retrieveUserDataRoute = (app) => {
    app.get("/api/:version/user/:id", authenticateModule, (req, res) => {
        retrieveUserData(req.params.id).then((data) => {
            if (!!data) {
                const responseData = new ResponseData("Successful recovery of user data !", ResponseCode.S_Success, data);
                res.status(200).json(responseData)
            } else {
                const responseData = new ResponseData("An unknown error occurred !", ResponseCode.E_UnknownError, new DataObject());
                res.status(500).json(responseData)
            }
        })
    });
}
