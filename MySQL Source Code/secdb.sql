DROP DATABASE IF EXISTS Secproj;
CREATE DATABASE IF NOT EXISTS Secproj;
USE Secproj;
SET SQL_SAFE_UPDATES = 0;

DROP TABLE IF EXISTS Users;
CREATE TABLE Users(
id INT NOT NULL AUTO_INCREMENT,
email VARCHAR(255) NOT NULL DEFAULT 'notSet',
passphrase VARCHAR(255) NOT NULL DEFAULT 'notSet',
saltInput VARCHAR(255) NOT NULL DEFAULT 'notSet',
PRIMARY KEY(id,email)
)Engine=InnoDB;

DROP TABLE IF EXISTS HistoryBulk;
CREATE TABLE HistoryBulk(
	aaKey INT(11) NOT NULL AUTO_INCREMENT,
	email VARCHAR(255) NOT NULL,
    dateOfChange TIMESTAMP DEFAULT NOW(),
    entryNew VARCHAR(255) NOT NULL DEFAULT 'deleted',
    entryOld VARCHAR(255) NOT NULL DEFAULT 'deleted',
    typeOfChange VARCHAR(255) NOT NULL DEFAULT 'notSet',
    PRIMARY KEY(aaKey,email) -- ,
    /*CONSTRAINT HistoryToUsers
		FOREIGN KEY (`email`) REFERENCES Users(`email`)
        ON DELETE CASCADE ON UPDATE CASCADE
        select * from HistoryBulk
        */
)Engine=InnoDB;

DROP TABLE IF EXISTS UserCredentials;
CREATE TABLE UserCredentials(
	aakey INT(11) NOT NULL AUTO_INCREMENT,
    email VARCHAR(255) NOT NULL,
    typeOfApp VARCHAR(255) NOT NULL,
    entryCode VARCHAR(255) NOT NULL,
    PRIMARY KEY(aakey,email)
)Engine=InnoDB;

-- select * from Users;

DELIMITER %%
DROP PROCEDURE IF EXISTS InsertWithAes%%
DROP PROCEDURE IF EXISTS InsertWithoutAes%%
DROP PROCEDURE IF EXISTS RetrieveJavaEncryptedUser%%
DROP PROCEDURE IF EXISTS RetrieveJavaEncryptedUserNoReplace%%
DROP PROCEDURE IF EXISTS InsertNewAppPass%%
DROP PROCEDURE IF EXISTS RetrieveEncryptedAppPass%%
DROP TRIGGER IF EXISTS UpdateHistoryBulkOnUpdate%%
DROP TRIGGER IF EXISTS UpdateHistoryBulkOnDelete%%
DROP PROCEDURE IF EXISTS UpdateCredentials%%
DROP PROCEDURE IF EXISTS RetrieveEncryptedHistory%%
-- CALL InsertNewAppPass('diaman','lelele','s+weHm8Xd7LGL8nOZxcKRw==','vYk4tLr/6rn/wFE0moKxcQ==')
-- CALL RetrieveEncryptedAppPass('diaman','lelele','vYk4tLr/6rn/wFE0moKxcQ==')
-- select * from UserCredentials WHERE email LIKE 'diaman';

-- update koumpi
-- CALL UpdateCredentials('diaman','lenukes','HYGFex5Z0VvmUePeY5pAFQ==','vYk4tLr/6rn/wFE0moKxcQ==')

-- CALL RetrieveEncryptedHistory('diaman','hamster','2017-12-26','vYk4tLr/6rn/wFE0moKxcQ==')
-- CALL RetrieveEncryptedHistory('diaman','newApp','2017-12-27','vYk4tLr/6rn/wFE0moKxcQ==')
-- CALL RetrieveEncryptedHistory('diaman','newApp','2017-12-27','vYk4tLr/6rn/wFE0moKxcQ==')
CREATE TRIGGER UpdateHistoryBulkOnUpdate
	AFTER UPDATE ON UserCredentials
    FOR EACH ROW
BEGIN
	INSERT INTO HistoryBulk VALUES
    (NULL,NEW.email,DEFAULT,NEW.entryCode,OLD.entryCode,NEW.typeOfApp);
END%%
CREATE TRIGGER UpdateHistoryBulkOnDelete
	AFTER DELETE ON UserCredentials
    FOR EACH ROW
BEGIN
	INSERT INTO HistoryBulk VALUES
    (NULL,OLD.email,DEFAULT,DEFAULT,OLD.entryCode,OLD.typeOfApp);
END%%

CREATE PROCEDURE RetrieveEncryptedHistory(
	IN var_email VARCHAR(255),
    IN var_type VARCHAR(255),
    IN var_date VARCHAR(255),
    IN var_passJavaCipher VARCHAR(255))
BEGIN
	DECLARE v_salt VARCHAR(255);
    SET v_salt=(SELECT saltInput FROM Users WHERE email LIKE var_email LIMIT 1);
	SELECT
		date_format(dateOfChange, '%Y-%m-%d %H:%i:%s') as DateOfChange, 
		typeOfChange, 
		replace(cast(aes_decrypt(entryOld,var_passJavaCipher) as char(100)),v_salt,''),
		replace(cast(aes_decrypt(entryNew,var_passJavaCipher) as char(100)),v_salt,'')
	FROM HistoryBulk  
	WHERE email LIKE var_email 
		AND typeOfChange LIKE var_type 
		AND dateOfChange LIKE var_date
	ORDER BY DateOfChange DESC 
	LIMIT 1;
END%%

CREATE PROCEDURE UpdateCredentials(
	IN var_email VARCHAR(255),
    IN var_typeOfApp VARCHAR(255),
	IN var_app_pass VARCHAR(255),
    IN var_passJavaCipher VARCHAR(255))
BEGIN
	DECLARE v_salt VARCHAR(255);
    SET v_salt=(SELECT saltInput FROM Users WHERE email LIKE var_email LIMIT 1);
	UPDATE UserCredentials
		SET entryCode=aes_encrypt(concat(var_app_pass,v_salt),var_passJavaCipher)
        WHERE email LIKE var_email
			AND typeOfApp LIKE var_typeOfApp;
END%%

CREATE PROCEDURE RetrieveEncryptedAppPass(
	IN var_email VARCHAR(255),
    IN var_typeOfApp VARCHAR(255),
	IN var_passJavaCipher VARCHAR(255))
BEGIN
	DECLARE v_salt VARCHAR(255);
    SET v_salt=(SELECT saltInput FROM Users WHERE email LIKE var_email LIMIT 1);
	SELECT 
		typeOfApp, 
		replace(cast(aes_decrypt(entryCode,var_passJavaCipher) as char(100)),v_salt,'')
	FROM UserCredentials
	WHERE email LIKE var_email
		AND typeOfApp LIKE var_typeOfApp;
END%%

CREATE PROCEDURE InsertNewAppPass(
	IN var_user VARCHAR(255),
	IN var_app_type VARCHAR(255),
    IN var_app_code VARCHAR(255),
    IN var_pass VARCHAR(255))
BEGIN
	DECLARE v_salt VARCHAR(255);
    SET v_salt=(SELECT saltInput FROM Users WHERE email LIKE var_user LIMIT 1);
	INSERT INTO UserCredentials VALUES 
    (null,
    var_user,
    var_app_type,
    aes_encrypt(concat(var_app_code,v_salt),var_pass)
    );
END%%

CREATE PROCEDURE InsertWithAes
	(IN emailInput VARCHAR(255),
    IN passphraseInput VARCHAR(255),
    IN saltInput VARCHAR(255))
BEGIN
	INSERT INTO Users VALUES
    (NULL,emailInput,aes_encrypt(concat(passphraseInput,saltInput),passphraseInput),saltInput);
END%%

CREATE PROCEDURE InsertWithoutAes
	(IN emailInput VARCHAR(255),
    IN passphraseInput VARCHAR(255),
    IN saltInput VARCHAR(255))
BEGIN
	INSERT INTO Users VALUES
    (NULL,emailInput,passphraseInput,saltInput);
END%%

CREATE PROCEDURE RetrieveJavaEncryptedUser
	(IN emailIn VARCHAR(255),
    IN passIn VARCHAR(255))
BEGIN
	SELECT 
		email, 
        replace(cast(aes_decrypt(passphrase,passIn) as char(100)),saltInput,''),
        saltInput 
	FROM Users
    WHERE email=emailIn;
END%%

CREATE PROCEDURE RetrieveJavaEncryptedUserNoReplace
	(IN emailIn VARCHAR(255),
    IN passIn VARCHAR(255))
BEGIN
	SELECT 
		email, 
        (cast(aes_decrypt(passphrase,passIn) as char(100))),
        saltInput 
	FROM Users
    WHERE email=emailIn;
END%%

DELIMITER ;

CALL InsertWithAes('sarri@ceid.gr','kK2DELcUM4JGxxJAfqaWrQ==','/VHtJOX0VS8t/jp9N+vEP9meMSHgfymeo6FRL16q8lc=');
CALL InsertNewAppPass('sarri@ceid.gr','facebook','PnN2GGf+P7sf3LozT3IiXw==','kK2DELcUM4JGxxJAfqaWrQ==');
CALL InsertNewAppPass('sarri@ceid.gr','youtube','/0m1nInmAwEObSx/idi3Yw==','kK2DELcUM4JGxxJAfqaWrQ==');
CALL InsertNewAppPass('sarri@ceid.gr','gmail','EwMmVdZbbuRcRiSouUksFw==','kK2DELcUM4JGxxJAfqaWrQ==');
CALL InsertNewAppPass('sarri@ceid.gr','pinterest','HeiEeg+vqorWPc08PLXVJQ==','kK2DELcUM4JGxxJAfqaWrQ==');
CALL UpdateCredentials('sarri@ceid.gr','facebook','WJ/cVNcP/x62CgEwzshV/w==','kK2DELcUM4JGxxJAfqaWrQ==');
CALL UpdateCredentials('sarri@ceid.gr','pinterest','7QnIIBEgzruXxLJOhQFtew==','kK2DELcUM4JGxxJAfqaWrQ==');
DELETE FROM UserCredentials WHERE email LIKE 'sarri@ceid.gr' AND typeOfApp LIKE 'pinterest';
CALL UpdateCredentials('sarri@ceid.gr','gmail','PriIJNzBh9g4/JGiryYuOQ==','kK2DELcUM4JGxxJAfqaWrQ==');


