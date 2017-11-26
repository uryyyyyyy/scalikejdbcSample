DROP TABLE IF EXISTS `city`;
CREATE TABLE `city` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `name` CHAR(35) NOT NULL,
  `country_code` CHAR(3) NOT NULL,
  `created_at_date` date,
  `created_at_timestamp` timestamp not null,
  PRIMARY KEY (`id`),
  KEY `country_code` (`country_code`)
) ENGINE=InnoDB;


DROP TABLE IF EXISTS `country`;
CREATE TABLE `country` (
  `code` CHAR(3) NOT NULL,
  `name` CHAR(52) NOT NULL,
  PRIMARY KEY (`code`)
) ENGINE=InnoDB;


DROP TABLE IF EXISTS `country_language`;
CREATE TABLE `country_language` (
  `country_code` CHAR(3) NOT NULL,
  `language` CHAR(30) NOT NULL,
  PRIMARY KEY (`country_code`,`language`),
  KEY `country_code` (`country_code`)
) ENGINE=InnoDB;

