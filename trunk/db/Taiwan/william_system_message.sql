/*
Navicat MySQL Data Transfer

Source Server         : localhost_3306
Source Server Version : 50051
Source Host           : localhost:3306
Source Database       : l1jdb_yorick

Target Server Type    : MYSQL
Target Server Version : 50051
File Encoding         : 65001

Date: 2012-07-01 21:32:33
*/

SET FOREIGN_KEY_CHECKS=0;
-- ----------------------------
-- Table structure for `william_system_message`
-- ----------------------------
DROP TABLE IF EXISTS `william_system_message`;
CREATE TABLE `william_system_message` (
  `id` int(10) unsigned NOT NULL auto_increment,
  `message` varchar(1000) NOT NULL,
  `note` varchar(45) default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=112 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of william_system_message
-- ----------------------------
INSERT INTO `william_system_message` VALUES ('100', '\\f:[聲望LV1]', '聲望LV1');
INSERT INTO `william_system_message` VALUES ('101', '\\fU[聲望LV2]', '聲望LV2');
INSERT INTO `william_system_message` VALUES ('102', '\\fA[聲望LV3]', '聲望LV3');
INSERT INTO `william_system_message` VALUES ('103', '\\fI[聲望LV4]', '聲望LV4');
INSERT INTO `william_system_message` VALUES ('104', '\\fN[聲望LV5]', '聲望LV5');
INSERT INTO `william_system_message` VALUES ('105', '\\fB[聲望LV6]', '聲望LV6');
INSERT INTO `william_system_message` VALUES ('106', '\\fY[聲望LV7]', '聲望LV7');
INSERT INTO `william_system_message` VALUES ('107', '\\f9[聲望LV8]', '聲望LV8');
INSERT INTO `william_system_message` VALUES ('108', '\\fW[聲望LV9]', '聲望LV9');
INSERT INTO `william_system_message` VALUES ('109', '\\f7[聲望LV10]', '聲望LV10');
INSERT INTO `william_system_message` VALUES ('110', '\\f;[聲望LV11]', '聲望LV11');
INSERT INTO `william_system_message` VALUES ('111', '\\fH[聲望LV12]', '聲望LV12');
INSERT INTO `william_system_message` VALUES ('112', '您的聲望值增加1點', '聲望道具');
INSERT INTO `william_system_message` VALUES ('113', '您的聲望值增加5點', '聲望道具');
INSERT INTO `william_system_message` VALUES ('114', '您的聲望值增加10點', '聲望道具');
INSERT INTO `william_system_message` VALUES ('115', '您的聲望值已到了極限', '聲望道具');
INSERT INTO `william_system_message` VALUES ('116', '您不適合使用這種聲望道具', '聲望道具');
