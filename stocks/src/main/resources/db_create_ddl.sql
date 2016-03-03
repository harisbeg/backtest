-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL,ALLOW_INVALID_DATES';

-- -----------------------------------------------------
-- Schema mydb
-- -----------------------------------------------------
-- -----------------------------------------------------
-- Schema backtest
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema backtest
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `backtest` ;
USE `backtest` ;

-- -----------------------------------------------------
-- Table `backtest`.`stock`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `backtest`.`stock` (
  `id` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `ticker` VARCHAR(10) NOT NULL,
  `name` VARCHAR(45) NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `ticker_UNIQUE` (`ticker` ASC))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `backtest`.`transaction`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `backtest`.`transaction` (
  `id` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `stock_id` INT UNSIGNED NULL,
  `date` DATE NULL,
  `1weekreturn` DECIMAL(5,2) NULL,
  `2weekreturn` DECIMAL(5,2) NULL,
  `openinggap` DECIMAL(5,2) NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_transaction_stock_idx` (`stock_id` ASC),
  CONSTRAINT `fk_transaction_stock`
    FOREIGN KEY (`stock_id`)
    REFERENCES `backtest`.`stock` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `backtest`.`price_history`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `backtest`.`price_history` (
  `id` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `stock_id` INT UNSIGNED NULL,
  `date` DATE NULL,
  `open` DECIMAL(10,5) NULL,
  `high` DECIMAL(10,5) NULL,
  `low` DECIMAL(10,5) NULL,
  `close` DECIMAL(10,5) NULL,
  `volume` INT UNSIGNED NULL,
  `adj_close` DECIMAL(10,5) NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_price_history_stock1_idx` (`stock_id` ASC),
  CONSTRAINT `fk_price_history_stock1`
    FOREIGN KEY (`stock_id`)
    REFERENCES `backtest`.`stock` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
