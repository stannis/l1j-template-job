/*
Navicat MySQL Data Transfer

Source Server         : localhost_3306
Source Server Version : 50051
Source Host           : localhost:3306
Source Database       : l1jdb_taiwan

Target Server Type    : MYSQL
Target Server Version : 50051
File Encoding         : 65001

Date: 2012-06-30 19:33:50
*/

SET FOREIGN_KEY_CHECKS=0;
-- ----------------------------
-- Table structure for `weapon_skill`
-- ----------------------------
DROP TABLE IF EXISTS `weapon_skill`;
CREATE TABLE `weapon_skill` (
  `weapon_id` int(11) unsigned NOT NULL auto_increment,
  `note` varchar(255) default NULL,
  `probability` int(11) unsigned NOT NULL default '0',
  `fix_damage` int(11) unsigned NOT NULL default '0',
  `random_damage` int(11) unsigned NOT NULL default '0',
  `area` int(11) NOT NULL default '0',
  `skill_id` int(11) unsigned NOT NULL default '0',
  `skill_time` int(11) unsigned NOT NULL default '0',
  `effect_id` int(11) unsigned NOT NULL default '0',
  `effect_target` int(11) unsigned NOT NULL default '0',
  `arrow_type` int(11) unsigned NOT NULL default '0',
  `attr` int(11) unsigned NOT NULL default '0',
  `gfx_id` int(11) unsigned NOT NULL default '0',
  `gfx_id_target` int(1) unsigned NOT NULL default '1',
  PRIMARY KEY  (`weapon_id`)
) ENGINE=MyISAM AUTO_INCREMENT=316 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of weapon_skill
-- ----------------------------
INSERT INTO `weapon_skill` VALUES ('47', '沉默之劍', '2', '0', '0', '0', '64', '16', '2177', '0', '0', '0', '0', '1');
INSERT INTO `weapon_skill` VALUES ('54', '克特之劍', '15', '35', '25', '0', '0', '0', '10', '0', '0', '8', '0', '1');
INSERT INTO `weapon_skill` VALUES ('58', '死亡騎士烈炎之劍', '7', '75', '15', '0', '0', '0', '1811', '0', '0', '2', '0', '1');
INSERT INTO `weapon_skill` VALUES ('76', '倫得雙刀', '15', '35', '25', '0', '0', '0', '1805', '0', '0', '1', '0', '1');
INSERT INTO `weapon_skill` VALUES ('121', '冰之女王魔杖', '25', '95', '55', '0', '0', '0', '1810', '0', '0', '4', '0', '1');
INSERT INTO `weapon_skill` VALUES ('203', '炎魔的雙手劍', '15', '90', '90', '2', '0', '0', '762', '0', '0', '2', '0', '1');
INSERT INTO `weapon_skill` VALUES ('205', '熾炎天使弓', '5', '8', '0', '0', '0', '0', '6288', '0', '1', '0', '0', '1');
INSERT INTO `weapon_skill` VALUES ('256', '萬聖節南瓜長劍', '8', '35', '25', '0', '0', '0', '2750', '0', '0', '1', '0', '1');
INSERT INTO `weapon_skill` VALUES ('257', '萬聖節長劍', '8', '35', '25', '0', '0', '0', '2750', '0', '0', '1', '0', '1');
INSERT INTO `weapon_skill` VALUES ('258', '終極萬聖節南瓜長劍', '8', '35', '25', '0', '0', '0', '2750', '0', '0', '1', '0', '1');
INSERT INTO `weapon_skill` VALUES ('289', '耀武短劍', '8', '65', '30', '0', '0', '0', '129', '0', '0', '0', '0', '1');
INSERT INTO `weapon_skill` VALUES ('290', '耀武雙手劍', '8', '65', '30', '0', '0', '0', '129', '0', '0', '0', '0', '1');
INSERT INTO `weapon_skill` VALUES ('291', '耀武雙刀', '8', '65', '30', '0', '0', '0', '129', '0', '0', '0', '0', '1');
INSERT INTO `weapon_skill` VALUES ('292', '耀武之弩', '8', '65', '30', '0', '0', '0', '129', '0', '1', '0', '0', '1');
INSERT INTO `weapon_skill` VALUES ('293', '耀武魔杖', '8', '65', '30', '0', '0', '0', '129', '0', '0', '0', '0', '1');
INSERT INTO `weapon_skill` VALUES ('294', '特製的耀武短劍', '8', '65', '30', '0', '0', '0', '129', '0', '0', '0', '0', '1');
INSERT INTO `weapon_skill` VALUES ('295', '特製的耀武雙手劍', '8', '65', '30', '0', '0', '0', '129', '0', '0', '0', '0', '1');
INSERT INTO `weapon_skill` VALUES ('296', '特製的耀武雙刀', '8', '65', '30', '0', '0', '0', '129', '0', '0', '0', '0', '1');
INSERT INTO `weapon_skill` VALUES ('297', '特製的耀武之弩', '8', '65', '30', '0', '0', '0', '129', '0', '1', '0', '0', '1');
INSERT INTO `weapon_skill` VALUES ('298', '特製的耀武魔杖', '8', '65', '30', '0', '0', '0', '129', '0', '0', '0', '0', '1');
INSERT INTO `weapon_skill` VALUES ('299', '超特製的耀武短劍', '8', '65', '30', '0', '0', '0', '129', '0', '0', '0', '0', '1');
INSERT INTO `weapon_skill` VALUES ('300', '超特製的耀武雙手劍', '8', '65', '30', '0', '0', '0', '129', '0', '0', '0', '0', '1');
INSERT INTO `weapon_skill` VALUES ('301', '超特製的耀武雙刀', '8', '65', '30', '0', '0', '0', '129', '0', '0', '0', '0', '1');
INSERT INTO `weapon_skill` VALUES ('302', '超特製的耀武之弩', '8', '65', '30', '0', '0', '0', '129', '0', '1', '0', '0', '1');
INSERT INTO `weapon_skill` VALUES ('303', '超特製的耀武魔杖', '8', '65', '30', '0', '0', '0', '129', '0', '0', '0', '0', '1');
