/*
Navicat MySQL Data Transfer

Source Server         : L1j-TW
Source Server Version : 60011
Source Host           : localhost:3306
Source Database       : l1jdb

Target Server Type    : MYSQL
Target Server Version : 60011
File Encoding         : 65001

Date: 2013-10-17 17:01:38
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for weapon_soul
-- ----------------------------
DROP TABLE IF EXISTS `weapon_soul`;
CREATE TABLE `weapon_soul` (
  `item_obj_id` int(10) NOT NULL,
  `itemid` int(10) NOT NULL,
  `type` int(10) NOT NULL,
  `lv` int(10) NOT NULL,
  `lSkill1_Lv` int(10) NOT NULL,
  `base_lSkill1_Lv` int(10) NOT NULL,
  `lSkill2_Lv` int(10) NOT NULL,
  `base_lSkill2_Lv` int(10) NOT NULL,
  `mSkill1_Lv` int(10) NOT NULL,
  `base_mSkill1_Lv` int(10) NOT NULL,
  `mSkill2_Lv` int(10) NOT NULL,
  `base_mSkill2_Lv` int(10) NOT NULL,
  `mSkill3_Lv` int(10) NOT NULL,
  `base_mSkill3_Lv` int(10) NOT NULL,
  `mSkill4_Lv` int(10) NOT NULL,
  `base_mSkill4_Lv` int(10) NOT NULL,
  `hSkill1_Lv` int(10) NOT NULL,
  `base_hSkill1_Lv` int(10) NOT NULL,
  `hSkill2_Lv` int(10) NOT NULL,
  `base_hSkill2_Lv` int(10) NOT NULL,
  `hSkill3_Lv` int(10) NOT NULL,
  `base_hSkill3_Lv` int(10) NOT NULL,
  `hSkill4_Lv` int(10) NOT NULL,
  `base_hSkill4_Lv` int(10) NOT NULL,
  `lvPoints` int(10) NOT NULL,
  `exp` int(10) NOT NULL,
  `remainlvPoint` int(10) NOT NULL,
  `owner` int(10) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- ----------------------------
-- Records of weapon_soul
-- ----------------------------
INSERT INTO `weapon_soul` VALUES ('0', '152455', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0');
