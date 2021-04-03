const ResponseData = require("../models/Response/ResponseData");
const ResponseCode = require("../models/Response/ResponseCode");
const DataObject = require("../models/DataObject");
const { login, createAccount, addLocation } = require("../methods/User");
const jwt = require("jsonwebtoken");
const { hashData } = require("../security/methods");
const { validateEmail } = require("../utils/pattern");
const { retrieveUserData } = require("../methods/User");
const { authenticateModule } = require("../security/middleware");
const { retrieveAllUserNear } = require("../methods/Location");

module.exports.includeAllUserRoutes = (app) => {
    createAccountRoute(app);
    loginRoute(app);
    addLocationToUser(app);
    generateAccessTokenRoute(app);

    retrieveUserDataRoute(app);
    retrieveAllUserNearRoute(app);
}

const createAccountRoute = (app) => {
    app.post("/api/:version/user", (req, res) => {
        const email = req.body.email || "";
        const pwd = req.body.password || "";
        const surname = req.body.surname || "";
        const firstName = req.body.first_name || "";
        const age = req.body.age || 0;

        if (email.length <= 100 && (pwd.length >= 6 && pwd.length <= 300) && surname.length <= 100 && firstName.length <= 100 && age >= 15) {
            if (validateEmail(email)) {
                createAccount(email, hashData(pwd), surname, firstName, age)
                    .then((data) => {
                        data["token"] = jwt.sign({ID: data.ID}, process.env.NEARYOU_SECURITY_KEY);

                        const responseData = new ResponseData("Account created with success !", ResponseCode.S_Success, data);
                        res.status(200).json(responseData)
                    })
                    .catch((error) => {
                        if (error.code != null && error.code === "ER_DUP_ENTRY") {
                            const responseData = new ResponseData("Email already used by an account !", ResponseCode.E_EmailKnown, new DataObject());
                            res.status(403).json(responseData)
                        } else if (error.code != null && error.code === "ER_DATA_TOO_LONG") {
                            const responseData = new ResponseData("A data is too long !", ResponseCode.E_DatabaseDataTooLong, new DataObject());
                            res.status(403).json(responseData)
                        } else {
                            const responseData = new ResponseData("An unknown error occurred !", ResponseCode.E_UnknownError, new DataObject());
                            res.status(500).json(responseData)
                        }
                    })
            } else {
                const responseData = new ResponseData("Wrong email format !", ResponseCode.E_BadEmailFormat, new DataObject());
                res.status(403).json(responseData)
            }
        } else {
            if (age < 15) {
                const responseData = new ResponseData("Too young !", ResponseCode.E_AgeTooYoung, new DataObject());
                res.status(403).json(responseData)
            } else if (email.length > 100) {
                const responseData = new ResponseData("Email is too long !", ResponseCode.E_EmailTooLong, new DataObject());
                res.status(403).json(responseData)
            } else if (pwd.length < 6) {
                const responseData = new ResponseData("Password is too short !", ResponseCode.E_PasswordTooShort, new DataObject());
                res.status(403).json(responseData)
            } else if (pwd.length > 300) {
                const responseData = new ResponseData("Password is too long !", ResponseCode.E_PasswordTooLong, new DataObject());
                res.status(403).json(responseData)
            } else if (surname.length > 100) {
                const responseData = new ResponseData("Surname is too long !", ResponseCode.E_SurnameTooLong, new DataObject());
                res.status(403).json(responseData)
            } else if (firstName.length > 100) {
                const responseData = new ResponseData("First name is too long !", ResponseCode.E_FirstNameTooLong, new DataObject());
                res.status(403).json(responseData)
            } else {
                const responseData = new ResponseData("A data is too long !", ResponseCode.E_DatabaseDataTooLong, new DataObject());
                res.status(403).json(responseData)
            }
        }
    });
}
const loginRoute = (app) => {
    app.post("/api/:version/user/login", (req, res) => {
        const email = req.body.email || "";
        const password = req.body.password || "";

        login(email, hashData(password))
            .then((data) => {
                if (!!data) {
                    data["token"] = jwt.sign({ID: data.ID}, process.env.NEARYOU_SECURITY_KEY);

                    const responseData = new ResponseData("Login success !", ResponseCode.S_Success, data);
                    res.status(200).json(responseData)
                } else {
                    const responseData = new ResponseData("Login failed !", ResponseCode.E_WrongCredentials, new DataObject());
                    res.status(403).json(responseData)
                }
            })
            .catch(() => {
                const responseData = new ResponseData("An unknown error occurred !", ResponseCode.E_UnknownError, new DataObject());
                res.status(500).json(responseData)
            })
    });
}
const addLocationToUser = (app) => {
    app.post("/api/:version/user/:id/location", authenticateModule, (req, res) => {
        addLocation(req.params.id, req.body.latitude, req.body.longitude)
            .then(() => {
                const responseData = new ResponseData("Location added with success !", ResponseCode.S_Success, { });
                res.status(200).json(responseData)
            })
            .catch(() => {
                const responseData = new ResponseData("An unknown error occurred !", ResponseCode.E_UnknownError, new DataObject());
                res.status(500).json(responseData)
            })
    });
}

const retrieveAllUserNearRoute = (app) => {
    app.get("/api/:version/user/:id/near", authenticateModule, (req, res) => {
        retrieveAllUserNear(req.params.id)
            .then((data) => {
                if (!!data) {
                    Promise.all(data)
                        .then((users) => {
                            const responseData = new ResponseData("Successful recovery of users close to you !", ResponseCode.S_Success, users);
                            res.status(200).json(responseData)
                        })
                        .catch(() => {
                            const responseData = new ResponseData("An unknown error occurred !", ResponseCode.E_UnknownError, new DataObject());
                            res.status(500).json(responseData)
                        })
                } else {
                    const responseData = new ResponseData("An unknown error occurred !", ResponseCode.E_UnknownError, new DataObject());
                    res.status(500).json(responseData)
                }
            })
            .catch(() => {
                const responseData = new ResponseData("An unknown error occurred !", ResponseCode.E_UnknownError, new DataObject());
                res.status(500).json(responseData)
            })
    });
}

const generateAccessTokenRoute = (app) => {
    app.get("/api/:version/user/:id/access", authenticateModule, (req, res) => {
        const data = {
            token : jwt.sign({ID: req.params.id}, process.env.NEARYOU_SECURITY_KEY, { expiresIn: "1m"})
        }

        const responseData = new ResponseData("Token access generated !", ResponseCode.S_Success, data);
        res.status(200).json(responseData)
    });
}

const retrieveUserDataRoute = (app) => {
    app.get("/api/:version/user/:id", authenticateModule, (req, res) => {
        retrieveUserData(req.params.id)
            .then((data) => {
                if (!!data) {
                    const responseData = new ResponseData("Successful recovery of user data !", ResponseCode.S_Success, data);
                    res.status(200).json(responseData)
                } else {
                    const responseData = new ResponseData("An unknown error occurred !", ResponseCode.E_NoResource, new DataObject());
                    res.status(500).json(responseData)
                }
            })
            .catch(() => {
                const responseData = new ResponseData("An unknown error occurred !", ResponseCode.E_UnknownError, new DataObject());
                res.status(500).json(responseData)
            })
    });
}
