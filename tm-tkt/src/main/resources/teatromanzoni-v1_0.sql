SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL,ALLOW_INVALID_DATES';

DROP SCHEMA IF EXISTS `teatromanzoni` ;
CREATE SCHEMA IF NOT EXISTS `teatromanzoni` DEFAULT CHARACTER SET latin1 ;
USE `teatromanzoni` ;

-- -----------------------------------------------------
-- Table `teatromanzoni`.`USER_CATEGORY`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `teatromanzoni`.`USER_CATEGORY` ;

CREATE TABLE IF NOT EXISTS `teatromanzoni`.`USER_CATEGORY` (
  `USER_CATEGORY_id` SMALLINT NOT NULL,
  `DESCRIPTION` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`USER_CATEGORY_id`),
  UNIQUE INDEX `DESCRIPTION_UNIQUE` (`DESCRIPTION` ASC))
ENGINE = InnoDB
DEFAULT CHARACTER SET = latin1
COMMENT = 'Category like Adult, Retired, Student, ...';


-- -----------------------------------------------------
-- Table `teatromanzoni`.`USER`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `teatromanzoni`.`USER` ;

CREATE TABLE IF NOT EXISTS `teatromanzoni`.`USER` (
  `EMAIL` VARCHAR(90) NOT NULL,
  `FIRST_NAME` VARCHAR(45) NOT NULL,
  `LAST_NAME` VARCHAR(45) NOT NULL,
  `MIDDLE_NAME` VARCHAR(45) NULL DEFAULT NULL,
  `PASSWORD` VARCHAR(45) NULL,
  `USER_CATEGORY_id` SMALLINT NULL,
  PRIMARY KEY (`EMAIL`),
  INDEX `fk_USER_USER_CATEGORY1_idx` (`USER_CATEGORY_id` ASC),
  CONSTRAINT `fk_USER_USER_CATEGORY1`
    FOREIGN KEY (`USER_CATEGORY_id`)
    REFERENCES `teatromanzoni`.`USER_CATEGORY` (`USER_CATEGORY_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = latin1;


-- -----------------------------------------------------
-- Table `teatromanzoni`.`SECTOR`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `teatromanzoni`.`SECTOR` ;

CREATE TABLE IF NOT EXISTS `teatromanzoni`.`SECTOR` (
  `SECTOR_id` SMALLINT NOT NULL,
  `SECTOR_NAME` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`SECTOR_id`),
  UNIQUE INDEX `SECTOR_NAME_UNIQUE` (`SECTOR_NAME` ASC))
ENGINE = InnoDB
DEFAULT CHARACTER SET = latin1;


-- -----------------------------------------------------
-- Table `teatromanzoni`.`SEAT_CATEGORY`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `teatromanzoni`.`SEAT_CATEGORY` ;

CREATE TABLE IF NOT EXISTS `teatromanzoni`.`SEAT_CATEGORY` (
  `SEAT_CATEGORY_id` INT NOT NULL,
  `CATEGORY_NAME` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`SEAT_CATEGORY_id`),
  UNIQUE INDEX `CATEGORY_NAME_UNIQUE` (`CATEGORY_NAME` ASC))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `teatromanzoni`.`ROW`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `teatromanzoni`.`ROW` ;

CREATE TABLE IF NOT EXISTS `teatromanzoni`.`ROW` (
  `ROW_id` INT NOT NULL,
  `ROW_NAME` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`ROW_id`),
  UNIQUE INDEX `ROW_NAME_UNIQUE` (`ROW_NAME` ASC))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `teatromanzoni`.`SEAT`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `teatromanzoni`.`SEAT` ;

CREATE TABLE IF NOT EXISTS `teatromanzoni`.`SEAT` (
  `SEAT_id` SMALLINT NOT NULL,
  `ROW_id` INT NOT NULL,
  `SECTOR_id` SMALLINT NOT NULL,
  `SEAT_CATEGORY_id` INT NOT NULL,
  PRIMARY KEY (`SEAT_id`),
  INDEX `fk_SEAT_SECTOR1_idx` (`SECTOR_id` ASC),
  INDEX `fk_SEAT_SEAT_CATEGORY1_idx` (`SEAT_CATEGORY_id` ASC),
  INDEX `fk_SEAT_ROW1_idx` (`ROW_id` ASC),
  CONSTRAINT `fk_SEAT_SECTOR1`
    FOREIGN KEY (`SECTOR_id`)
    REFERENCES `teatromanzoni`.`SECTOR` (`SECTOR_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_SEAT_SEAT_CATEGORY1`
    FOREIGN KEY (`SEAT_CATEGORY_id`)
    REFERENCES `teatromanzoni`.`SEAT_CATEGORY` (`SEAT_CATEGORY_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_SEAT_ROW1`
    FOREIGN KEY (`ROW_id`)
    REFERENCES `teatromanzoni`.`ROW` (`ROW_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = latin1;


-- -----------------------------------------------------
-- Table `teatromanzoni`.`GENRE`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `teatromanzoni`.`GENRE` ;

CREATE TABLE IF NOT EXISTS `teatromanzoni`.`GENRE` (
  `GENRE_id` SMALLINT NOT NULL AUTO_INCREMENT,
  `NAME` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`GENRE_id`),
  UNIQUE INDEX `NAME_UNIQUE` (`NAME` ASC))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `teatromanzoni`.`PLAY`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `teatromanzoni`.`PLAY` ;

CREATE TABLE IF NOT EXISTS `teatromanzoni`.`PLAY` (
  `SHOW_id` SMALLINT NOT NULL AUTO_INCREMENT,
  `TITLE` VARCHAR(80) NOT NULL,
  `AUTHOR_NAME` VARCHAR(80) NULL,
  `DIRECTOR_NAME` VARCHAR(80) NULL,
  `DESCRIPTION` VARCHAR(4096) NULL,
  `GENRE_id` SMALLINT NOT NULL,
  `FIRST_EVENT_DATE` DATE NULL,
  `LAST_EVENT_DATE` DATE NULL,
  PRIMARY KEY (`SHOW_id`),
  INDEX `fk_SHOW_GENRE1_idx` (`GENRE_id` ASC),
  CONSTRAINT `fk_SHOW_GENRE1`
    FOREIGN KEY (`GENRE_id`)
    REFERENCES `teatromanzoni`.`GENRE` (`GENRE_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `teatromanzoni`.`EVENT`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `teatromanzoni`.`EVENT` ;

CREATE TABLE IF NOT EXISTS `teatromanzoni`.`EVENT` (
  `EVENT_id` INT NOT NULL AUTO_INCREMENT,
  `DATETIME` DATETIME NOT NULL,
  `SHOW_id` SMALLINT NOT NULL,
  PRIMARY KEY (`EVENT_id`),
  UNIQUE INDEX `DATETIME_UNIQUE` (`DATETIME` ASC),
  INDEX `fk_EVENT_SHOW1_idx` (`SHOW_id` ASC),
  CONSTRAINT `fk_EVENT_SHOW1`
    FOREIGN KEY (`SHOW_id`)
    REFERENCES `teatromanzoni`.`PLAY` (`SHOW_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = latin1;


-- -----------------------------------------------------
-- Table `teatromanzoni`.`SEASON`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `teatromanzoni`.`SEASON` ;

CREATE TABLE IF NOT EXISTS `teatromanzoni`.`SEASON` (
  `SEASON_id` SMALLINT NOT NULL AUTO_INCREMENT,
  `START_DATE` DATE NULL,
  `END_DATE` VARCHAR(45) NULL,
  PRIMARY KEY (`SEASON_id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `teatromanzoni`.`DAYOFWEEK`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `teatromanzoni`.`DAYOFWEEK` ;

CREATE TABLE IF NOT EXISTS `teatromanzoni`.`DAYOFWEEK` (
  `DAYOFWEEK_id` SMALLINT NOT NULL AUTO_INCREMENT,
  `DAY` VARCHAR(45) NULL,
  PRIMARY KEY (`DAYOFWEEK_id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `teatromanzoni`.`TIMEOFDAY`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `teatromanzoni`.`TIMEOFDAY` ;

CREATE TABLE IF NOT EXISTS `teatromanzoni`.`TIMEOFDAY` (
  `TIMEOFDAY_id` SMALLINT NOT NULL AUTO_INCREMENT,
  `START_TIME` TIME NULL,
  `END_TIME` VARCHAR(45) NULL,
  PRIMARY KEY (`TIMEOFDAY_id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `teatromanzoni`.`PRICE_SCHEME`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `teatromanzoni`.`PRICE_SCHEME` ;

CREATE TABLE IF NOT EXISTS `teatromanzoni`.`PRICE_SCHEME` (
  `PRICE_SCHEME_id` SMALLINT NOT NULL AUTO_INCREMENT,
  `PRICE` DECIMAL(10,0) NOT NULL,
  `PRICE_SCHEME_NAME` VARCHAR(45) NULL,
  `GENRE_id` SMALLINT NULL,
  `SEASON_id` SMALLINT NULL,
  `DAYOFWEEK_id` SMALLINT NULL,
  `TIMEOFDAY_id` SMALLINT NULL,
  `SECTOR_id` SMALLINT NULL,
  `USER_CATEGORY_id` SMALLINT NULL,
  `SALE_TYPE` CHAR NULL,
  `EVENT_id` INT NULL,
  `SHOW_id` SMALLINT NULL,
  PRIMARY KEY (`PRICE_SCHEME_id`),
  INDEX `fk_PRICE_SCHEME_GENRE1_idx` (`GENRE_id` ASC),
  INDEX `fk_PRICE_SCHEME_SEASON1_idx` (`SEASON_id` ASC),
  INDEX `fk_PRICE_SCHEME_DAYOFWEEK1_idx` (`DAYOFWEEK_id` ASC),
  INDEX `fk_PRICE_SCHEME_TIMEOFDAY1_idx` (`TIMEOFDAY_id` ASC),
  INDEX `fk_PRICE_SCHEME_SECTOR1_idx` (`SECTOR_id` ASC),
  UNIQUE INDEX `PRICE_SCHEME_NAME_UNIQUE` (`PRICE_SCHEME_NAME` ASC),
  INDEX `fk_PRICE_SCHEME_USER_CATEGORY1_idx` (`USER_CATEGORY_id` ASC),
  INDEX `fk_PRICE_SCHEME_EVENT1_idx` (`EVENT_id` ASC),
  INDEX `fk_PRICE_SCHEME_PLAY1_idx` (`SHOW_id` ASC),
  CONSTRAINT `fk_PRICE_SCHEME_GENRE1`
    FOREIGN KEY (`GENRE_id`)
    REFERENCES `teatromanzoni`.`GENRE` (`GENRE_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_PRICE_SCHEME_SEASON1`
    FOREIGN KEY (`SEASON_id`)
    REFERENCES `teatromanzoni`.`SEASON` (`SEASON_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_PRICE_SCHEME_DAYOFWEEK1`
    FOREIGN KEY (`DAYOFWEEK_id`)
    REFERENCES `teatromanzoni`.`DAYOFWEEK` (`DAYOFWEEK_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_PRICE_SCHEME_TIMEOFDAY1`
    FOREIGN KEY (`TIMEOFDAY_id`)
    REFERENCES `teatromanzoni`.`TIMEOFDAY` (`TIMEOFDAY_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_PRICE_SCHEME_SECTOR1`
    FOREIGN KEY (`SECTOR_id`)
    REFERENCES `teatromanzoni`.`SECTOR` (`SECTOR_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_PRICE_SCHEME_USER_CATEGORY1`
    FOREIGN KEY (`USER_CATEGORY_id`)
    REFERENCES `teatromanzoni`.`USER_CATEGORY` (`USER_CATEGORY_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_PRICE_SCHEME_EVENT1`
    FOREIGN KEY (`EVENT_id`)
    REFERENCES `teatromanzoni`.`EVENT` (`EVENT_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_PRICE_SCHEME_PLAY1`
    FOREIGN KEY (`SHOW_id`)
    REFERENCES `teatromanzoni`.`PLAY` (`SHOW_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `teatromanzoni`.`PAYMENT_METHOD`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `teatromanzoni`.`PAYMENT_METHOD` ;

CREATE TABLE IF NOT EXISTS `teatromanzoni`.`PAYMENT_METHOD` (
  `PAYMENT_METHOD_id` SMALLINT NOT NULL,
  `PAYMENT_NAME` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`PAYMENT_METHOD_id`),
  UNIQUE INDEX `PAYMENT_NAME_UNIQUE` (`PAYMENT_NAME` ASC))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `teatromanzoni`.`PAYMENT`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `teatromanzoni`.`PAYMENT` ;

CREATE TABLE IF NOT EXISTS `teatromanzoni`.`PAYMENT` (
  `PAYMENT_id` INT NOT NULL AUTO_INCREMENT,
  `TRANSACTION_id` VARCHAR(45) NOT NULL,
  `INVOICE_NUMBER` VARCHAR(45) NULL,
  `PAYMENT_METHOD_id` SMALLINT NOT NULL,
  PRIMARY KEY (`PAYMENT_id`),
  UNIQUE INDEX `TRANSACTION_id_UNIQUE` (`TRANSACTION_id` ASC),
  UNIQUE INDEX `INVOICE_NUMBER_UNIQUE` (`INVOICE_NUMBER` ASC),
  INDEX `fk_PAYMENT_PAYMENT_METHOD1_idx` (`PAYMENT_METHOD_id` ASC),
  CONSTRAINT `fk_PAYMENT_PAYMENT_METHOD1`
    FOREIGN KEY (`PAYMENT_METHOD_id`)
    REFERENCES `teatromanzoni`.`PAYMENT_METHOD` (`PAYMENT_METHOD_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `teatromanzoni`.`SALE`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `teatromanzoni`.`SALE` ;

CREATE TABLE IF NOT EXISTS `teatromanzoni`.`SALE` (
  `SALE_id` INT NOT NULL AUTO_INCREMENT,
  `PAYMENT_id` INT NOT NULL,
  `PRICE_SCHEME_id` SMALLINT NOT NULL,
  `USER_EMAIL` VARCHAR(45) NOT NULL,
  `SALE_TYPE` CHAR NOT NULL,
  PRIMARY KEY (`SALE_id`),
  INDEX `fk_SALE_PAYMENT1_idx` (`PAYMENT_id` ASC),
  INDEX `fk_SALE_PRICE_SCHEME1_idx` (`PRICE_SCHEME_id` ASC),
  INDEX `fk_SALE_USER1_idx` (`USER_EMAIL` ASC),
  CONSTRAINT `fk_SALE_PAYMENT1`
    FOREIGN KEY (`PAYMENT_id`)
    REFERENCES `teatromanzoni`.`PAYMENT` (`PAYMENT_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_SALE_PRICE_SCHEME1`
    FOREIGN KEY (`PRICE_SCHEME_id`)
    REFERENCES `teatromanzoni`.`PRICE_SCHEME` (`PRICE_SCHEME_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_SALE_USER1`
    FOREIGN KEY (`USER_EMAIL`)
    REFERENCES `teatromanzoni`.`USER` (`EMAIL`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `teatromanzoni`.`TICKET`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `teatromanzoni`.`TICKET` ;

CREATE TABLE IF NOT EXISTS `teatromanzoni`.`TICKET` (
  `SALE_id` INT NOT NULL,
  `TICKET_id` INT NOT NULL,
  INDEX `fk_TICKET_SALE1_idx` (`SALE_id` ASC),
  UNIQUE INDEX `TICKET_id_UNIQUE` (`TICKET_id` ASC),
  PRIMARY KEY (`SALE_id`),
  CONSTRAINT `fk_TICKET_SALE1`
    FOREIGN KEY (`SALE_id`)
    REFERENCES `teatromanzoni`.`SALE` (`SALE_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `teatromanzoni`.`SUBSCRIPTION_TYPE`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `teatromanzoni`.`SUBSCRIPTION_TYPE` ;

CREATE TABLE IF NOT EXISTS `teatromanzoni`.`SUBSCRIPTION_TYPE` (
  `SUBSCRIPTION_TYPE_id` SMALLINT NOT NULL,
  `NAME` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`SUBSCRIPTION_TYPE_id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `teatromanzoni`.`SUBSCRIPTION`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `teatromanzoni`.`SUBSCRIPTION` ;

CREATE TABLE IF NOT EXISTS `teatromanzoni`.`SUBSCRIPTION` (
  `SALE_id` INT NOT NULL,
  `SUBSCRIPTION_id` INT NOT NULL,
  `SUBSCRIPTION_TYPE_id` SMALLINT NOT NULL,
  `SEAT_id` SMALLINT NULL,
  `GENRE_id` SMALLINT NOT NULL,
  `PREPAID_TICKETS` SMALLINT NULL,
  INDEX `fk_SUBSCRIPTION_SALE1_idx` (`SALE_id` ASC),
  INDEX `fk_SUBSCRIPTION_SUBSCRIPTION_TYPE1_idx` (`SUBSCRIPTION_TYPE_id` ASC),
  INDEX `fk_SUBSCRIPTION_GENRE1_idx` (`GENRE_id` ASC),
  INDEX `fk_SUBSCRIPTION_SEAT1_idx` (`SEAT_id` ASC),
  UNIQUE INDEX `SUBSCRIPTION_id_UNIQUE` (`SUBSCRIPTION_id` ASC),
  PRIMARY KEY (`SALE_id`),
  CONSTRAINT `fk_SUBSCRIPTION_SALE1`
    FOREIGN KEY (`SALE_id`)
    REFERENCES `teatromanzoni`.`SALE` (`SALE_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_SUBSCRIPTION_SUBSCRIPTION_TYPE1`
    FOREIGN KEY (`SUBSCRIPTION_TYPE_id`)
    REFERENCES `teatromanzoni`.`SUBSCRIPTION_TYPE` (`SUBSCRIPTION_TYPE_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_SUBSCRIPTION_GENRE1`
    FOREIGN KEY (`GENRE_id`)
    REFERENCES `teatromanzoni`.`GENRE` (`GENRE_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_SUBSCRIPTION_SEAT1`
    FOREIGN KEY (`SEAT_id`)
    REFERENCES `teatromanzoni`.`SEAT` (`SEAT_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `teatromanzoni`.`BOOKING`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `teatromanzoni`.`BOOKING` ;

CREATE TABLE IF NOT EXISTS `teatromanzoni`.`BOOKING` (
  `EVENT_id` INT NOT NULL,
  `SEAT_id` SMALLINT NOT NULL,
  `SALE_id` INT NOT NULL,
  PRIMARY KEY (`EVENT_id`, `SEAT_id`),
  INDEX `fk_EVENT_has_SEAT_SEAT1_idx` (`SEAT_id` ASC),
  INDEX `fk_EVENT_has_SEAT_EVENT1_idx` (`EVENT_id` ASC),
  INDEX `fk_BOOKING_SALE1_idx` (`SALE_id` ASC),
  CONSTRAINT `fk_EVENT_has_SEAT_EVENT1`
    FOREIGN KEY (`EVENT_id`)
    REFERENCES `teatromanzoni`.`EVENT` (`EVENT_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_EVENT_has_SEAT_SEAT1`
    FOREIGN KEY (`SEAT_id`)
    REFERENCES `teatromanzoni`.`SEAT` (`SEAT_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_BOOKING_SALE1`
    FOREIGN KEY (`SALE_id`)
    REFERENCES `teatromanzoni`.`SALE` (`SALE_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = latin1;


-- -----------------------------------------------------
-- Table `teatromanzoni`.`USER_ROLES`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `teatromanzoni`.`USER_ROLES` ;

CREATE TABLE IF NOT EXISTS `teatromanzoni`.`USER_ROLES` (
  `EMAIL` VARCHAR(90) NOT NULL,
  `ROLE` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`EMAIL`, `ROLE`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `teatromanzoni`.`SEAT_SUBSCRIPTION`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `teatromanzoni`.`SEAT_SUBSCRIPTION` ;

CREATE TABLE IF NOT EXISTS `teatromanzoni`.`SEAT_SUBSCRIPTION` (
  `SEAT_id` SMALLINT NOT NULL,
  `GENRE_id` SMALLINT NOT NULL,
  PRIMARY KEY (`SEAT_id`, `GENRE_id`),
  INDEX `fk_SEAT_has_GENRE_GENRE1_idx` (`GENRE_id` ASC),
  INDEX `fk_SEAT_has_GENRE_SEAT1_idx` (`SEAT_id` ASC),
  CONSTRAINT `fk_SEAT_has_GENRE_SEAT1`
    FOREIGN KEY (`SEAT_id`)
    REFERENCES `teatromanzoni`.`SEAT` (`SEAT_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_SEAT_has_GENRE_GENRE1`
    FOREIGN KEY (`GENRE_id`)
    REFERENCES `teatromanzoni`.`GENRE` (`GENRE_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = latin1;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;

-- -----------------------------------------------------
-- Data for table `teatromanzoni`.`USER_CATEGORY`
-- -----------------------------------------------------
START TRANSACTION;
USE `teatromanzoni`;
INSERT INTO `teatromanzoni`.`USER_CATEGORY` (`USER_CATEGORY_id`, `DESCRIPTION`) VALUES (1, 'Adult');
INSERT INTO `teatromanzoni`.`USER_CATEGORY` (`USER_CATEGORY_id`, `DESCRIPTION`) VALUES (2, 'Junior');
INSERT INTO `teatromanzoni`.`USER_CATEGORY` (`USER_CATEGORY_id`, `DESCRIPTION`) VALUES (3, 'Senior');
INSERT INTO `teatromanzoni`.`USER_CATEGORY` (`USER_CATEGORY_id`, `DESCRIPTION`) VALUES (4, 'Student');
INSERT INTO `teatromanzoni`.`USER_CATEGORY` (`USER_CATEGORY_id`, `DESCRIPTION`) VALUES (5, 'Teacher');
INSERT INTO `teatromanzoni`.`USER_CATEGORY` (`USER_CATEGORY_id`, `DESCRIPTION`) VALUES (6, 'Disabled');
INSERT INTO `teatromanzoni`.`USER_CATEGORY` (`USER_CATEGORY_id`, `DESCRIPTION`) VALUES (7, 'Staff');
INSERT INTO `teatromanzoni`.`USER_CATEGORY` (`USER_CATEGORY_id`, `DESCRIPTION`) VALUES (0, 'Admin');

COMMIT;


-- -----------------------------------------------------
-- Data for table `teatromanzoni`.`USER`
-- -----------------------------------------------------
START TRANSACTION;
USE `teatromanzoni`;
INSERT INTO `teatromanzoni`.`USER` (`EMAIL`, `FIRST_NAME`, `LAST_NAME`, `MIDDLE_NAME`, `PASSWORD`, `USER_CATEGORY_id`) VALUES ('glgerard@gmail.com', 'Gianluca', 'Gerard', '', 'gianluca', 0);
INSERT INTO `teatromanzoni`.`USER` (`EMAIL`, `FIRST_NAME`, `LAST_NAME`, `MIDDLE_NAME`, `PASSWORD`, `USER_CATEGORY_id`) VALUES ('isiar@dmst.aueb.gr', 'Ioannis', 'Siarapis', NULL, 'ioannis', 1);
INSERT INTO `teatromanzoni`.`USER` (`EMAIL`, `FIRST_NAME`, `LAST_NAME`, `MIDDLE_NAME`, `PASSWORD`, `USER_CATEGORY_id`) VALUES ('oguzmelihpamuk@gmail.com', 'Oguz', 'Pamuk', 'Melih', '', 2);
INSERT INTO `teatromanzoni`.`USER` (`EMAIL`, `FIRST_NAME`, `LAST_NAME`, `MIDDLE_NAME`, `PASSWORD`, `USER_CATEGORY_id`) VALUES ('steffen_hermann@gmx.de', 'Steffen', 'Hermann', NULL, 'steffen', 3);

COMMIT;


-- -----------------------------------------------------
-- Data for table `teatromanzoni`.`SECTOR`
-- -----------------------------------------------------
START TRANSACTION;
USE `teatromanzoni`;
INSERT INTO `teatromanzoni`.`SECTOR` (`SECTOR_id`, `SECTOR_NAME`) VALUES (1, 'Poltronissima');
INSERT INTO `teatromanzoni`.`SECTOR` (`SECTOR_id`, `SECTOR_NAME`) VALUES (2, 'Poltrona');
INSERT INTO `teatromanzoni`.`SECTOR` (`SECTOR_id`, `SECTOR_NAME`) VALUES (3, 'Palco-A');
INSERT INTO `teatromanzoni`.`SECTOR` (`SECTOR_id`, `SECTOR_NAME`) VALUES (4, 'Palco-B');
INSERT INTO `teatromanzoni`.`SECTOR` (`SECTOR_id`, `SECTOR_NAME`) VALUES (5, 'Sala-A');
INSERT INTO `teatromanzoni`.`SECTOR` (`SECTOR_id`, `SECTOR_NAME`) VALUES (6, 'Sala-B');

COMMIT;


-- -----------------------------------------------------
-- Data for table `teatromanzoni`.`SEAT_CATEGORY`
-- -----------------------------------------------------
START TRANSACTION;
USE `teatromanzoni`;
INSERT INTO `teatromanzoni`.`SEAT_CATEGORY` (`SEAT_CATEGORY_id`, `CATEGORY_NAME`) VALUES (1, 'Standard');
INSERT INTO `teatromanzoni`.`SEAT_CATEGORY` (`SEAT_CATEGORY_id`, `CATEGORY_NAME`) VALUES (2, 'Unavailable');
INSERT INTO `teatromanzoni`.`SEAT_CATEGORY` (`SEAT_CATEGORY_id`, `CATEGORY_NAME`) VALUES (3, 'Reserved');
INSERT INTO `teatromanzoni`.`SEAT_CATEGORY` (`SEAT_CATEGORY_id`, `CATEGORY_NAME`) VALUES (4, 'Reduced');
INSERT INTO `teatromanzoni`.`SEAT_CATEGORY` (`SEAT_CATEGORY_id`, `CATEGORY_NAME`) VALUES (5, 'Disabled');

COMMIT;


-- -----------------------------------------------------
-- Data for table `teatromanzoni`.`ROW`
-- -----------------------------------------------------
START TRANSACTION;
USE `teatromanzoni`;
INSERT INTO `teatromanzoni`.`ROW` (`ROW_id`, `ROW_NAME`) VALUES (1, '1');
INSERT INTO `teatromanzoni`.`ROW` (`ROW_id`, `ROW_NAME`) VALUES (2, '2');
INSERT INTO `teatromanzoni`.`ROW` (`ROW_id`, `ROW_NAME`) VALUES (3, '3');
INSERT INTO `teatromanzoni`.`ROW` (`ROW_id`, `ROW_NAME`) VALUES (4, '4');
INSERT INTO `teatromanzoni`.`ROW` (`ROW_id`, `ROW_NAME`) VALUES (5, '5');
INSERT INTO `teatromanzoni`.`ROW` (`ROW_id`, `ROW_NAME`) VALUES (6, '6');
INSERT INTO `teatromanzoni`.`ROW` (`ROW_id`, `ROW_NAME`) VALUES (7, '7');
INSERT INTO `teatromanzoni`.`ROW` (`ROW_id`, `ROW_NAME`) VALUES (8, '8');
INSERT INTO `teatromanzoni`.`ROW` (`ROW_id`, `ROW_NAME`) VALUES (9, '9');
INSERT INTO `teatromanzoni`.`ROW` (`ROW_id`, `ROW_NAME`) VALUES (10, 'A');
INSERT INTO `teatromanzoni`.`ROW` (`ROW_id`, `ROW_NAME`) VALUES (11, 'B');
INSERT INTO `teatromanzoni`.`ROW` (`ROW_id`, `ROW_NAME`) VALUES (12, 'C');
INSERT INTO `teatromanzoni`.`ROW` (`ROW_id`, `ROW_NAME`) VALUES (13, 'D');
INSERT INTO `teatromanzoni`.`ROW` (`ROW_id`, `ROW_NAME`) VALUES (14, 'E');
INSERT INTO `teatromanzoni`.`ROW` (`ROW_id`, `ROW_NAME`) VALUES (15, 'F');
INSERT INTO `teatromanzoni`.`ROW` (`ROW_id`, `ROW_NAME`) VALUES (16, 'G');
INSERT INTO `teatromanzoni`.`ROW` (`ROW_id`, `ROW_NAME`) VALUES (17, 'H');
INSERT INTO `teatromanzoni`.`ROW` (`ROW_id`, `ROW_NAME`) VALUES (18, 'I');
INSERT INTO `teatromanzoni`.`ROW` (`ROW_id`, `ROW_NAME`) VALUES (19, 'L');
INSERT INTO `teatromanzoni`.`ROW` (`ROW_id`, `ROW_NAME`) VALUES (20, 'M');
INSERT INTO `teatromanzoni`.`ROW` (`ROW_id`, `ROW_NAME`) VALUES (21, 'N');
INSERT INTO `teatromanzoni`.`ROW` (`ROW_id`, `ROW_NAME`) VALUES (22, 'O');
INSERT INTO `teatromanzoni`.`ROW` (`ROW_id`, `ROW_NAME`) VALUES (23, 'P');

COMMIT;


-- -----------------------------------------------------
-- Data for table `teatromanzoni`.`SEAT`
-- -----------------------------------------------------
START TRANSACTION;
USE `teatromanzoni`;
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (1, 1, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (2, 1, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (3, 1, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (4, 1, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (5, 1, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (6, 1, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (7, 1, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (8, 1, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (9, 1, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (10, 1, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (11, 1, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (12, 1, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (13, 1, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (14, 1, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (15, 1, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (16, 2, 5, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (17, 2, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (18, 2, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (19, 2, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (20, 2, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (21, 2, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (22, 2, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (23, 2, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (24, 2, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (25, 2, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (26, 2, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (27, 2, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (28, 2, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (29, 2, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (30, 2, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (31, 2, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (32, 2, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (33, 2, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (34, 2, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (35, 2, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (36, 2, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (37, 2, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (38, 2, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (39, 2, 6, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (40, 3, 1, 2);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (41, 3, 5, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (42, 3, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (43, 3, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (44, 3, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (45, 3, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (46, 3, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (47, 3, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (48, 3, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (49, 3, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (50, 3, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (51, 3, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (52, 3, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (53, 3, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (54, 3, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (55, 3, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (56, 3, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (57, 3, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (58, 3, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (59, 3, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (60, 3, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (61, 3, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (62, 3, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (63, 3, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (64, 3, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (65, 3, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (66, 3, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (67, 3, 6, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (68, 4, 5, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (69, 4, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (70, 4, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (71, 4, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (72, 4, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (73, 4, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (74, 4, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (75, 4, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (76, 4, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (77, 4, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (78, 4, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (79, 4, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (80, 4, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (81, 4, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (82, 4, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (83, 4, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (84, 4, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (85, 4, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (86, 4, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (87, 4, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (88, 4, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (89, 4, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (90, 4, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (91, 4, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (92, 4, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (93, 4, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (94, 4, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (95, 4, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (96, 4, 6, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (97, 5, 5, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (98, 5, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (99, 5, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (100, 5, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (101, 5, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (102, 5, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (103, 5, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (104, 5, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (105, 5, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (106, 5, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (107, 5, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (108, 5, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (109, 5, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (110, 5, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (111, 5, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (112, 5, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (113, 5, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (114, 5, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (115, 5, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (116, 5, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (117, 5, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (118, 5, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (119, 5, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (120, 5, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (121, 5, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (122, 5, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (123, 5, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (124, 5, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (125, 5, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (126, 5, 6, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (127, 6, 1, 2);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (128, 6, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (129, 6, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (130, 6, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (131, 6, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (132, 6, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (133, 6, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (134, 6, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (135, 6, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (136, 6, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (137, 6, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (138, 6, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (139, 6, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (140, 6, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (141, 6, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (142, 6, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (143, 6, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (144, 6, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (145, 6, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (146, 6, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (147, 6, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (148, 6, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (149, 6, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (150, 6, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (151, 6, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (152, 6, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (153, 6, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (154, 6, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (155, 6, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (156, 6, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (157, 6, 1, 2);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (158, 7, 5, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (159, 7, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (160, 7, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (161, 7, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (162, 7, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (163, 7, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (164, 7, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (165, 7, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (166, 7, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (167, 7, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (168, 7, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (169, 7, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (170, 7, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (171, 7, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (172, 7, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (173, 7, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (174, 7, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (175, 7, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (176, 7, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (177, 7, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (178, 7, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (179, 7, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (180, 7, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (181, 7, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (182, 7, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (183, 7, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (184, 7, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (185, 7, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (186, 7, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (187, 7, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (188, 7, 6, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (189, 8, 5, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (190, 8, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (191, 8, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (192, 8, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (193, 8, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (194, 8, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (195, 8, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (196, 8, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (197, 8, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (198, 8, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (199, 8, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (200, 8, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (201, 8, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (202, 8, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (203, 8, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (204, 8, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (205, 8, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (206, 8, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (207, 8, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (208, 8, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (209, 8, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (210, 8, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (211, 8, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (212, 8, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (213, 8, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (214, 8, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (215, 8, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (216, 8, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (217, 8, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (218, 8, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (219, 8, 6, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (220, 9, 1, 2);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (221, 9, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (222, 9, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (223, 9, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (224, 9, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (225, 9, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (226, 9, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (227, 9, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (228, 9, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (229, 9, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (230, 9, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (231, 9, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (232, 9, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (233, 9, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (234, 9, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (235, 9, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (236, 9, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (237, 9, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (238, 9, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (239, 9, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (240, 9, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (241, 9, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (242, 9, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (243, 9, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (244, 9, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (245, 9, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (246, 9, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (247, 9, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (248, 9, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (249, 9, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (250, 9, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (251, 9, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (252, 9, 1, 2);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (253, 10, 5, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (254, 10, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (255, 10, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (256, 10, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (257, 10, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (258, 10, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (259, 10, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (260, 10, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (261, 10, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (262, 10, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (263, 10, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (264, 10, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (265, 10, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (266, 10, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (267, 10, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (268, 10, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (269, 10, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (270, 10, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (271, 10, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (272, 10, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (273, 10, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (274, 10, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (275, 10, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (276, 10, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (277, 10, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (278, 10, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (279, 10, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (280, 10, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (281, 10, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (282, 10, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (283, 10, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (284, 10, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (285, 10, 6, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (286, 11, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (287, 11, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (288, 11, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (289, 11, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (290, 11, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (291, 11, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (292, 11, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (293, 11, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (294, 11, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (295, 11, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (296, 11, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (297, 11, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (298, 11, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (299, 11, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (300, 11, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (301, 11, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (302, 11, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (303, 11, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (304, 11, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (305, 11, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (306, 11, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (307, 11, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (308, 11, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (309, 11, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (310, 11, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (311, 11, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (312, 11, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (313, 11, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (314, 11, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (315, 11, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (316, 11, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (317, 11, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (318, 11, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (319, 12, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (320, 12, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (321, 12, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (322, 12, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (323, 12, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (324, 12, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (325, 12, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (326, 12, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (327, 12, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (328, 12, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (329, 12, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (330, 12, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (331, 12, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (332, 12, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (333, 12, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (334, 12, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (335, 12, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (336, 12, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (337, 12, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (338, 12, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (339, 12, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (340, 12, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (341, 12, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (342, 12, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (343, 12, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (344, 12, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (345, 12, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (346, 12, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (347, 12, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (348, 12, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (349, 12, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (350, 12, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (351, 12, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (352, 13, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (353, 13, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (354, 13, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (355, 13, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (356, 13, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (357, 13, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (358, 13, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (359, 13, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (360, 13, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (361, 13, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (362, 13, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (363, 13, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (364, 13, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (365, 13, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (366, 13, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (367, 13, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (368, 13, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (369, 13, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (370, 13, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (371, 13, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (372, 13, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (373, 13, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (374, 13, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (375, 13, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (376, 13, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (377, 13, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (378, 13, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (379, 13, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (380, 13, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (381, 13, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (382, 13, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (383, 13, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (384, 13, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (385, 13, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (386, 14, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (387, 14, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (388, 14, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (389, 14, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (390, 14, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (391, 14, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (392, 14, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (393, 14, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (394, 14, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (395, 14, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (396, 14, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (397, 14, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (398, 14, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (399, 14, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (400, 14, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (401, 14, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (402, 14, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (403, 14, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (404, 14, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (405, 14, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (406, 14, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (407, 14, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (408, 14, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (409, 14, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (410, 14, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (411, 14, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (412, 14, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (413, 14, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (414, 14, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (415, 14, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (416, 14, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (417, 14, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (418, 14, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (419, 14, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (420, 14, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (421, 15, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (422, 15, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (423, 15, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (424, 15, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (425, 15, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (426, 15, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (427, 15, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (428, 15, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (429, 15, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (430, 15, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (431, 15, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (432, 15, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (433, 15, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (434, 15, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (435, 15, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (436, 15, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (437, 15, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (438, 15, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (439, 15, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (440, 15, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (441, 15, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (442, 15, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (443, 15, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (444, 15, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (445, 15, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (446, 15, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (447, 15, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (448, 15, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (449, 15, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (450, 15, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (451, 15, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (452, 15, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (453, 15, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (454, 15, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (455, 15, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (456, 16, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (457, 16, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (458, 16, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (459, 16, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (460, 16, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (461, 16, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (462, 16, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (463, 16, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (464, 16, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (465, 16, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (466, 16, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (467, 16, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (468, 16, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (469, 16, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (470, 16, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (471, 16, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (472, 16, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (473, 16, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (474, 16, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (475, 16, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (476, 16, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (477, 16, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (478, 16, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (479, 16, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (480, 16, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (481, 16, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (482, 16, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (483, 16, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (484, 16, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (485, 16, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (486, 16, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (487, 16, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (488, 16, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (489, 16, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (490, 16, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (491, 17, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (492, 17, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (493, 17, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (494, 17, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (495, 17, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (496, 17, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (497, 17, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (498, 17, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (499, 17, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (500, 17, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (501, 17, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (502, 17, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (503, 17, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (504, 17, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (505, 17, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (506, 17, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (507, 17, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (508, 17, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (509, 17, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (510, 17, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (511, 17, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (512, 17, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (513, 17, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (514, 17, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (515, 17, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (516, 17, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (517, 17, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (518, 17, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (519, 17, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (520, 17, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (521, 17, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (522, 17, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (523, 17, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (524, 17, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (525, 17, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (526, 18, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (527, 18, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (528, 18, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (529, 18, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (530, 18, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (531, 18, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (532, 18, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (533, 18, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (534, 18, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (535, 18, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (536, 18, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (537, 18, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (538, 18, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (539, 18, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (540, 18, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (541, 18, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (542, 18, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (543, 18, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (544, 18, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (545, 18, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (546, 18, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (547, 18, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (548, 18, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (549, 18, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (550, 18, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (551, 18, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (552, 18, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (553, 18, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (554, 18, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (555, 18, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (556, 18, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (557, 18, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (558, 18, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (559, 18, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (560, 18, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (561, 18, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (562, 19, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (563, 19, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (564, 19, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (565, 19, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (566, 19, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (567, 19, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (568, 19, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (569, 19, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (570, 19, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (571, 19, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (572, 19, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (573, 19, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (574, 19, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (575, 19, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (576, 19, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (577, 19, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (578, 19, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (579, 19, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (580, 19, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (581, 19, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (582, 19, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (583, 19, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (584, 19, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (585, 19, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (586, 19, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (587, 19, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (588, 19, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (589, 19, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (590, 19, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (591, 19, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (592, 19, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (593, 19, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (594, 19, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (595, 19, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (596, 19, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (597, 19, 1, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (598, 20, 2, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (599, 20, 2, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (600, 20, 2, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (601, 20, 2, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (602, 20, 2, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (603, 20, 2, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (604, 20, 2, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (605, 20, 2, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (606, 20, 2, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (607, 20, 2, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (608, 20, 2, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (609, 20, 2, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (610, 20, 2, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (611, 20, 2, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (612, 20, 2, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (613, 20, 2, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (614, 20, 2, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (615, 20, 2, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (616, 20, 2, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (617, 20, 2, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (618, 20, 2, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (619, 20, 2, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (620, 20, 2, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (621, 20, 2, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (622, 20, 2, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (623, 20, 2, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (624, 20, 2, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (625, 20, 2, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (626, 20, 2, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (627, 20, 2, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (628, 20, 2, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (629, 20, 2, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (630, 20, 2, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (631, 20, 2, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (632, 20, 2, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (633, 20, 2, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (634, 20, 2, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (635, 21, 2, 2);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (636, 21, 2, 2);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (637, 21, 2, 2);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (638, 21, 2, 2);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (639, 21, 2, 2);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (640, 21, 2, 2);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (641, 21, 2, 2);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (642, 21, 2, 2);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (643, 21, 2, 2);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (644, 21, 2, 2);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (645, 21, 2, 2);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (646, 21, 2, 2);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (647, 21, 2, 2);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (648, 21, 2, 2);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (649, 21, 2, 2);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (650, 21, 2, 2);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (651, 21, 2, 2);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (652, 21, 2, 2);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (653, 21, 2, 2);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (654, 21, 2, 2);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (655, 21, 2, 2);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (656, 21, 2, 2);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (657, 21, 2, 2);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (658, 21, 2, 2);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (659, 21, 2, 2);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (660, 21, 2, 2);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (661, 21, 2, 2);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (662, 21, 2, 2);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (663, 21, 2, 2);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (664, 21, 2, 2);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (665, 21, 2, 2);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (666, 21, 2, 2);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (667, 21, 2, 2);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (668, 21, 2, 2);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (669, 21, 2, 2);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (670, 21, 2, 2);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (671, 21, 2, 2);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (672, 22, 2, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (673, 22, 2, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (674, 22, 2, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (675, 22, 2, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (676, 22, 2, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (677, 22, 2, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (678, 22, 2, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (679, 22, 2, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (680, 22, 2, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (681, 22, 2, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (682, 22, 2, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (683, 22, 2, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (684, 22, 2, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (685, 22, 2, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (686, 22, 2, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (687, 22, 2, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (688, 22, 2, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (689, 22, 2, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (690, 22, 2, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (691, 22, 2, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (692, 22, 2, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (693, 22, 2, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (694, 22, 2, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (695, 22, 2, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (696, 22, 2, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (697, 22, 2, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (698, 22, 2, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (699, 22, 2, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (700, 22, 2, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (701, 22, 2, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (702, 22, 2, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (703, 22, 2, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (704, 22, 2, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (705, 22, 2, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (706, 22, 2, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (707, 22, 2, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (708, 22, 2, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (709, 23, 3, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (710, 23, 3, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (711, 23, 3, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (712, 23, 3, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (713, 23, 3, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (714, 23, 3, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (715, 23, 3, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (716, 23, 3, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (717, 23, 3, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (718, 23, 3, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (719, 23, 3, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (720, 23, 3, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (721, 23, 3, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (722, 23, 3, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (723, 23, 3, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (724, 23, 3, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (725, 23, 3, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (726, 23, 3, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (727, 23, 3, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (728, 23, 3, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (729, 23, 3, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (730, 23, 3, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (731, 23, 3, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (732, 23, 3, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (733, 23, 3, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (734, 23, 3, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (735, 23, 3, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (736, 23, 3, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (737, 23, 3, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (738, 23, 3, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (739, 23, 3, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (740, 23, 4, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (741, 23, 4, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (742, 23, 4, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (743, 23, 4, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (744, 23, 4, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (745, 23, 4, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (746, 23, 4, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (747, 23, 4, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (748, 23, 4, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (749, 23, 4, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (750, 23, 4, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (751, 23, 4, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (752, 23, 4, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (753, 23, 4, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (754, 23, 4, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (755, 23, 4, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (756, 23, 4, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (757, 23, 4, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (758, 23, 4, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (759, 23, 4, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (760, 23, 4, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (761, 23, 4, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (762, 23, 4, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (763, 23, 4, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (764, 23, 4, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (765, 23, 4, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (766, 23, 4, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (767, 23, 4, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (768, 23, 4, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (769, 23, 4, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (770, 23, 4, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (771, 23, 4, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (772, 23, 4, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (773, 23, 4, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (774, 23, 4, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (775, 23, 4, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (776, 23, 4, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (777, 23, 4, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (778, 23, 4, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (779, 23, 4, 1);
INSERT INTO `teatromanzoni`.`SEAT` (`SEAT_id`, `ROW_id`, `SECTOR_id`, `SEAT_CATEGORY_id`) VALUES (780, 23, 4, 1);

COMMIT;


-- -----------------------------------------------------
-- Data for table `teatromanzoni`.`GENRE`
-- -----------------------------------------------------
START TRANSACTION;
USE `teatromanzoni`;
INSERT INTO `teatromanzoni`.`GENRE` (`GENRE_id`, `NAME`) VALUES (NULL, 'Tragedy');
INSERT INTO `teatromanzoni`.`GENRE` (`GENRE_id`, `NAME`) VALUES (NULL, 'Music');
INSERT INTO `teatromanzoni`.`GENRE` (`GENRE_id`, `NAME`) VALUES (NULL, 'Comedy');
INSERT INTO `teatromanzoni`.`GENRE` (`GENRE_id`, `NAME`) VALUES (NULL, 'Opera');
INSERT INTO `teatromanzoni`.`GENRE` (`GENRE_id`, `NAME`) VALUES (NULL, 'Ballet');
INSERT INTO `teatromanzoni`.`GENRE` (`GENRE_id`, `NAME`) VALUES (NULL, 'Drama');
INSERT INTO `teatromanzoni`.`GENRE` (`GENRE_id`, `NAME`) VALUES (NULL, 'Musical');

COMMIT;


-- -----------------------------------------------------
-- Data for table `teatromanzoni`.`PLAY`
-- -----------------------------------------------------
START TRANSACTION;
USE `teatromanzoni`;
INSERT INTO `teatromanzoni`.`PLAY` (`SHOW_id`, `TITLE`, `AUTHOR_NAME`, `DIRECTOR_NAME`, `DESCRIPTION`, `GENRE_id`, `FIRST_EVENT_DATE`, `LAST_EVENT_DATE`) VALUES (NULL, 'Madama Butterfly', 'Giacomo Puccini', NULL, '\nMake way for Puccini\'s opera in three acts and wear waterproof mascara for the dramatic tearjerker of the season. Let the voice of renowned soprano Rosino Storchio and tenor Giovanni Zenatello envelop you under the stars while you sob quietly into your handkerchief! Wine and hard liquor will be available during intermission and after the show for those seeking to drown their sorrows.', 4, '2013-12-20', '2013-12-27');
INSERT INTO `teatromanzoni`.`PLAY` (`SHOW_id`, `TITLE`, `AUTHOR_NAME`, `DIRECTOR_NAME`, `DESCRIPTION`, `GENRE_id`, `FIRST_EVENT_DATE`, `LAST_EVENT_DATE`) VALUES (NULL, 'Hamlet', 'William Shakespeare', NULL, 'Set in the Kingdom of Denmark, the play dramatizes the revenge Prince Hamlet exacts on his uncle Claudius for murdering King Hamlet, Claudius\'s brother and Prince Hamlet\'s father, and then succeeding to the throne and taking as his wife Gertrude, the old king\'s widow and Prince Hamlet\'s mother. The play vividly portrays both true and feigned madness—from overwhelming grief to seething rage—and explores themes of treachery, revenge, incest, and moral corruption.', 1, '2013-12-21', '2013-12-21');
INSERT INTO `teatromanzoni`.`PLAY` (`SHOW_id`, `TITLE`, `AUTHOR_NAME`, `DIRECTOR_NAME`, `DESCRIPTION`, `GENRE_id`, `FIRST_EVENT_DATE`, `LAST_EVENT_DATE`) VALUES (NULL, 'Tutu Tchai', NULL, NULL, '\nJoin your fellow ballet enthusiasts for the National Ballet Company\'s presentation of Tutu Tchai, a tribute to Pyotr Tchaikovsky\'s and the expressive intensity revealed in his three most famous ballets: The Nutcracker, Swan Lake, and The Sleeping Beauty.', 5, '2013-12-22', '2013-12-28');
INSERT INTO `teatromanzoni`.`PLAY` (`SHOW_id`, `TITLE`, `AUTHOR_NAME`, `DIRECTOR_NAME`, `DESCRIPTION`, `GENRE_id`, `FIRST_EVENT_DATE`, `LAST_EVENT_DATE`) VALUES (NULL, 'Soweto Gospel Choir', NULL, NULL, 'Soweto Gospel Choir was formed to celebrate the unique and inspirational power of African Gospel music.\nThe 52-strong choir, under the direction of Beverly Bryer, draws on the best talent from the many churches in and around Soweto.\nThe choir is dedicated to sharing the joy of faith through music with audiences around the world.', 2, NULL, NULL);

COMMIT;


-- -----------------------------------------------------
-- Data for table `teatromanzoni`.`EVENT`
-- -----------------------------------------------------
START TRANSACTION;
USE `teatromanzoni`;
INSERT INTO `teatromanzoni`.`EVENT` (`EVENT_id`, `DATETIME`, `SHOW_id`) VALUES (1, '2013-12-20 20:15:00', 1);
INSERT INTO `teatromanzoni`.`EVENT` (`EVENT_id`, `DATETIME`, `SHOW_id`) VALUES (2, '2013-12-21 20:30:00', 2);
INSERT INTO `teatromanzoni`.`EVENT` (`EVENT_id`, `DATETIME`, `SHOW_id`) VALUES (3, '2013-12-22 20:15:00', 3);
INSERT INTO `teatromanzoni`.`EVENT` (`EVENT_id`, `DATETIME`, `SHOW_id`) VALUES (4, '2013-12-27 20:15:00', 1);
INSERT INTO `teatromanzoni`.`EVENT` (`EVENT_id`, `DATETIME`, `SHOW_id`) VALUES (5, '2013-12-28 20:30:00', 3);

COMMIT;


-- -----------------------------------------------------
-- Data for table `teatromanzoni`.`PRICE_SCHEME`
-- -----------------------------------------------------
START TRANSACTION;
USE `teatromanzoni`;
INSERT INTO `teatromanzoni`.`PRICE_SCHEME` (`PRICE_SCHEME_id`, `PRICE`, `PRICE_SCHEME_NAME`, `GENRE_id`, `SEASON_id`, `DAYOFWEEK_id`, `TIMEOFDAY_id`, `SECTOR_id`, `USER_CATEGORY_id`, `SALE_TYPE`, `EVENT_id`, `SHOW_id`) VALUES (NULL, 40, 'Tragedy Std Tkt', 1, NULL, NULL, NULL, NULL, NULL, 'T', NULL, NULL);
INSERT INTO `teatromanzoni`.`PRICE_SCHEME` (`PRICE_SCHEME_id`, `PRICE`, `PRICE_SCHEME_NAME`, `GENRE_id`, `SEASON_id`, `DAYOFWEEK_id`, `TIMEOFDAY_id`, `SECTOR_id`, `USER_CATEGORY_id`, `SALE_TYPE`, `EVENT_id`, `SHOW_id`) VALUES (NULL, 35, 'Music Std Tkt', 2, NULL, NULL, NULL, NULL, NULL, 'T', NULL, NULL);
INSERT INTO `teatromanzoni`.`PRICE_SCHEME` (`PRICE_SCHEME_id`, `PRICE`, `PRICE_SCHEME_NAME`, `GENRE_id`, `SEASON_id`, `DAYOFWEEK_id`, `TIMEOFDAY_id`, `SECTOR_id`, `USER_CATEGORY_id`, `SALE_TYPE`, `EVENT_id`, `SHOW_id`) VALUES (NULL, 40, 'Opera Std Tkt', 4, NULL, NULL, NULL, NULL, NULL, 'T', NULL, NULL);
INSERT INTO `teatromanzoni`.`PRICE_SCHEME` (`PRICE_SCHEME_id`, `PRICE`, `PRICE_SCHEME_NAME`, `GENRE_id`, `SEASON_id`, `DAYOFWEEK_id`, `TIMEOFDAY_id`, `SECTOR_id`, `USER_CATEGORY_id`, `SALE_TYPE`, `EVENT_id`, `SHOW_id`) VALUES (NULL, 35, 'Ballet Std Tkt', 5, NULL, NULL, NULL, NULL, NULL, 'T', NULL, NULL);
INSERT INTO `teatromanzoni`.`PRICE_SCHEME` (`PRICE_SCHEME_id`, `PRICE`, `PRICE_SCHEME_NAME`, `GENRE_id`, `SEASON_id`, `DAYOFWEEK_id`, `TIMEOFDAY_id`, `SECTOR_id`, `USER_CATEGORY_id`, `SALE_TYPE`, `EVENT_id`, `SHOW_id`) VALUES (NULL, 35, 'Tragedy Std Sub', 1, NULL, NULL, NULL, NULL, NULL, 'S', NULL, NULL);
INSERT INTO `teatromanzoni`.`PRICE_SCHEME` (`PRICE_SCHEME_id`, `PRICE`, `PRICE_SCHEME_NAME`, `GENRE_id`, `SEASON_id`, `DAYOFWEEK_id`, `TIMEOFDAY_id`, `SECTOR_id`, `USER_CATEGORY_id`, `SALE_TYPE`, `EVENT_id`, `SHOW_id`) VALUES (NULL, 30, 'Music Std Sub', 2, NULL, NULL, NULL, NULL, NULL, 'S', NULL, NULL);
INSERT INTO `teatromanzoni`.`PRICE_SCHEME` (`PRICE_SCHEME_id`, `PRICE`, `PRICE_SCHEME_NAME`, `GENRE_id`, `SEASON_id`, `DAYOFWEEK_id`, `TIMEOFDAY_id`, `SECTOR_id`, `USER_CATEGORY_id`, `SALE_TYPE`, `EVENT_id`, `SHOW_id`) VALUES (NULL, 35, 'Opera Std Sub', 4, NULL, NULL, NULL, NULL, NULL, 'S', NULL, NULL);
INSERT INTO `teatromanzoni`.`PRICE_SCHEME` (`PRICE_SCHEME_id`, `PRICE`, `PRICE_SCHEME_NAME`, `GENRE_id`, `SEASON_id`, `DAYOFWEEK_id`, `TIMEOFDAY_id`, `SECTOR_id`, `USER_CATEGORY_id`, `SALE_TYPE`, `EVENT_id`, `SHOW_id`) VALUES (NULL, 30, 'Ballet Std Sub', 5, NULL, NULL, NULL, NULL, NULL, 'S', NULL, NULL);

COMMIT;


-- -----------------------------------------------------
-- Data for table `teatromanzoni`.`PAYMENT_METHOD`
-- -----------------------------------------------------
START TRANSACTION;
USE `teatromanzoni`;
INSERT INTO `teatromanzoni`.`PAYMENT_METHOD` (`PAYMENT_METHOD_id`, `PAYMENT_NAME`) VALUES (1, 'Visa/MasterCard/AMEX');
INSERT INTO `teatromanzoni`.`PAYMENT_METHOD` (`PAYMENT_METHOD_id`, `PAYMENT_NAME`) VALUES (2, 'PostePay');
INSERT INTO `teatromanzoni`.`PAYMENT_METHOD` (`PAYMENT_METHOD_id`, `PAYMENT_NAME`) VALUES (3, 'PayPal');

COMMIT;


-- -----------------------------------------------------
-- Data for table `teatromanzoni`.`SUBSCRIPTION_TYPE`
-- -----------------------------------------------------
START TRANSACTION;
USE `teatromanzoni`;
INSERT INTO `teatromanzoni`.`SUBSCRIPTION_TYPE` (`SUBSCRIPTION_TYPE_id`, `NAME`) VALUES (1, 'Assigned Seat');
INSERT INTO `teatromanzoni`.`SUBSCRIPTION_TYPE` (`SUBSCRIPTION_TYPE_id`, `NAME`) VALUES (2, 'Free Seat');
INSERT INTO `teatromanzoni`.`SUBSCRIPTION_TYPE` (`SUBSCRIPTION_TYPE_id`, `NAME`) VALUES (3, 'Carnet');

COMMIT;


-- -----------------------------------------------------
-- Data for table `teatromanzoni`.`USER_ROLES`
-- -----------------------------------------------------
START TRANSACTION;
USE `teatromanzoni`;
INSERT INTO `teatromanzoni`.`USER_ROLES` (`EMAIL`, `ROLE`) VALUES ('glgerard@gmail.com', 'Admin');
INSERT INTO `teatromanzoni`.`USER_ROLES` (`EMAIL`, `ROLE`) VALUES ('steffen_hermann@gmx.de', 'Customer');
INSERT INTO `teatromanzoni`.`USER_ROLES` (`EMAIL`, `ROLE`) VALUES ('isiar@dmst.aueb.gr', 'Subscriber');
INSERT INTO `teatromanzoni`.`USER_ROLES` (`EMAIL`, `ROLE`) VALUES ('oguzmelih.pamuk@gmail.com', 'Customer');

COMMIT;

