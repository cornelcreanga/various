CREATE TABLE `petition` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(256) NOT NULL,
  `link` varchar(256) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_petition_name` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8;

CREATE TABLE `petition_page` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `petitionId` int(11) NOT NULL,
  `page` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_petition_page_1_idx` (`petitionId`),
  KEY `idx_petition_page_petitionId_page` (`petitionId`,`page`),
  CONSTRAINT `fk_petition_page_1` FOREIGN KEY (`petitionId`) REFERENCES `petition` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=27899 DEFAULT CHARSET=utf8;

CREATE TABLE `petition_signature` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(256) NOT NULL,
  `comment` varchar(16000) DEFAULT NULL,
  `city` varchar(256) DEFAULT NULL,
  `city_cleaned` varchar(256) DEFAULT NULL,
  `signDate` datetime DEFAULT NULL,
  `petitionId` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_petition_signature_1_idx` (`petitionId`),
  CONSTRAINT `fk_petition_signature_1` FOREIGN KEY (`petitionId`) REFERENCES `petition` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=258867 DEFAULT CHARSET=utf8;
