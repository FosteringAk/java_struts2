# mySQL scripts 

DROP DATABASE IF EXISTS cz3002_auth_db;
DROP USER IF EXISTS 'sqladmin'@'localhost';

CREATE DATABASE cz3002_auth_db;
CREATE USER 'sqladmin'@'localhost' IDENTIFIED BY 'sqladmin_password';
GRANT ALL ON cz3002_auth_db.* To 'sqladmin'@'localhost' ;

USE cz3002_auth_db;
CREATE TABLE users (
  id int(11) NOT NULL AUTO_INCREMENT,
  full_name varchar(45) NOT NULL,
  username varchar(45) NOT NULL,
  password varchar(45) NOT NULL,
  PRIMARY KEY (id)
);

USE cz3002_auth_db;
TRUNCATE users;
INSERT INTO users (full_name, username, password) VALUES("Anusha Datta", "anushadatta", "test123");

USE cz3002_auth_db;
SELECT * FROM users;


