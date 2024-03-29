CREATE DATABASE IF NOT EXISTS nearyou;

USE nearyou;


CREATE TABLE user (
    ID int PRIMARY KEY AUTO_INCREMENT,
    EMAIL VARCHAR(100) NOT NULL,
    PASSWORD VARCHAR(300) NOT NULL,
    URL_PROFILE VARCHAR(100) DEFAULT NULL DEFAULT '',
    SURNAME VARCHAR(100) NOT NULL,
    FIRST_NAME VARCHAR(100) NOT NULL,
    AGE INT NOT NULL CHECK ( AGE >= 15 ),
    CUSTOM_STATUS VARCHAR(300) NOT NULL DEFAULT '',
    IS_PUBLIC BOOLEAN DEFAULT false,
    DATE_CREATED DATETIME DEFAULT NOW(),
    DATE_EDITED DATETIME DEFAULT NOW()
);

CREATE TABLE location (
    ID_USER int NOT NULL,
    COORDINATE point NOT NULL,
    DATE datetime NOT NULL DEFAULT NOW(),

    FOREIGN KEY (ID_USER) REFERENCES user(ID)
);

CREATE TABLE link (
    ID int PRIMARY KEY AUTO_INCREMENT,
    ID_USER int NOT NULL,
    LINK VARCHAR(100) NOT NULL,
    DATE_CREATED DATETIME DEFAULT NOW(),
    DATE_EDITED DATETIME DEFAULT NOW(),

    FOREIGN KEY (ID_USER) REFERENCES user(ID)
);
