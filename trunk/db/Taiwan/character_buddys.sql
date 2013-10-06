/*
Navicat MySQL Data Transfer

Source Server         : localhost_3306
Source Server Version : 50051
Source Host           : localhost:3306
Source Database       : l1jdb_taiwan

Target Server Type    : MYSQL
Target Server Version : 50051
File Encoding         : 65001

Date: 2012-06-30 19:30:13
*/

SET FOREIGN_KEY_CHECKS=0;
-- ----------------------------
-- Table structure for `character_buddys`
-- ----------------------------
DROP TABLE IF EXISTS `character_buddys`;
CREATE TABLE `character_buddys` (
  `id` int(10) unsigned NOT NULL auto_increment,
  `char_id` int(10) NOT NULL default '0',
  `buddy_id` int(10) unsigned NOT NULL default '0',
  `buddy_name` varchar(45) NOT NULL,
  PRIMARY KEY  (`char_id`,`buddy_id`),
  KEY `key_id` (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of character_buddys
-- ----------------------------
