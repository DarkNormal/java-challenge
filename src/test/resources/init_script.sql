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
  `driver_id` int(11) NOT NULL UNIQUE,
  `oper_class` varchar(1),
  `carrier_id` int(11) NOT NULL,
  `location_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  foreign key (carrier_id) references carriers(id),
  foreign key (location_id) references locations(id)
);