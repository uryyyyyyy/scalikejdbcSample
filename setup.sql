CREATE SCHEMA `scalike` ;
use `scalike`;

CREATE TABLE `article` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `title` varchar(255) NOT NULL,
  `content` TEXT NOT NULL,
  `created_datetime` datetime NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `tag` (
  `name` varchar(100) NOT NULL,
  PRIMARY KEY (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `relation_article_tag` (
  `article_id` int(11) NOT NULL,
  `tag_name` varchar(100) NOT NULL,
  PRIMARY KEY (`article_id`, `tag_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `article_comment` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `article_id` int(11) NOT NULL,
  `content` TEXT NOT NULL,
  `username` varchar(100),
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;