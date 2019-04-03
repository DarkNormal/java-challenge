DROP TABLE IF EXISTS `drivers`;

DROP TABLE IF EXISTS `carriers`;

DROP TABLE IF EXISTS `locations`;

CREATE TABLE `locations` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `version` int(11) NOT NULL,
  `location_name` varchar(45) NOT NULL,
  `location_id` int(11) NOT NULL UNIQUE,
  PRIMARY KEY (`id`)
);

CREATE TABLE `carriers` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `version` int(11) NOT NULL,
  `carrier_name` varchar(45) NOT NULL UNIQUE,
  PRIMARY KEY (`id`)
);

CREATE TABLE `drivers` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `version` int(11) NOT NULL,
  `first_name` varchar(45) NOT NULL,
  `middle_init` varchar(1),
  `last_name` varchar(45) NOT NULL,
  `oper_class` varchar(1),
  `driver_id` int(11) NOT NULL UNIQUE,
  `carrier_id` int(11) NOT NULL,
  `location_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `carrier_fk` (`carrier_id`),
  KEY `location_fk` (`location_id`),
  CONSTRAINT `carrier_fk` FOREIGN KEY (`carrier_id`) REFERENCES `carriers` (`id`),
  CONSTRAINT `location_fk` FOREIGN KEY (`location_id`) REFERENCES `locations` (`id`)
);