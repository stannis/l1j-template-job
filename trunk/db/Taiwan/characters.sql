/*
Navicat MySQL Data Transfer

Source Server         : localhost_3306
Source Server Version : 50051
Source Host           : localhost:3306
Source Database       : l1jdb_yorick

Target Server Type    : MYSQL
Target Server Version : 50051
File Encoding         : 65001

Date: 2012-07-01 19:32:11
*/

SET FOREIGN_KEY_CHECKS=0;
-- ----------------------------
-- Table structure for `characters`
-- ----------------------------
DROP TABLE IF EXISTS `characters`;
CREATE TABLE `characters` (
  `account_name` varchar(13) NOT NULL default '0',
  `objid` int(11) unsigned NOT NULL default '0',
  `char_name` varchar(45) NOT NULL default '',
  `FamePoint` int(10) default '0',
  `birthday` datetime default NULL,
  `level` int(11) unsigned NOT NULL default '0',
  `HighLevel` int(11) unsigned NOT NULL default '0',
  `Exp` int(10) unsigned NOT NULL default '0',
  `MaxHp` int(10) unsigned NOT NULL default '0',
  `CurHp` int(10) unsigned NOT NULL default '0',
  `MaxMp` int(10) NOT NULL default '0',
  `CurMp` int(10) NOT NULL default '0',
  `Ac` int(10) NOT NULL default '0',
  `Str` int(3) NOT NULL default '0',
  `Con` int(3) NOT NULL default '0',
  `Dex` int(3) NOT NULL default '0',
  `Cha` int(3) NOT NULL default '0',
  `Intel` int(3) NOT NULL default '0',
  `Wis` int(3) NOT NULL default '0',
  `Status` int(10) unsigned NOT NULL default '0',
  `Class` int(10) unsigned NOT NULL default '0',
  `Sex` int(10) unsigned NOT NULL default '0',
  `Type` int(10) unsigned NOT NULL default '0',
  `Heading` int(10) unsigned NOT NULL default '0',
  `LocX` int(11) unsigned NOT NULL default '0',
  `LocY` int(11) unsigned NOT NULL default '0',
  `MapID` int(10) unsigned NOT NULL default '0',
  `Food` int(10) unsigned NOT NULL default '0',
  `Lawful` int(10) NOT NULL default '0',
  `Title` varchar(35) NOT NULL default '',
  `ClanID` int(10) unsigned NOT NULL default '0',
  `Clanname` varchar(45) NOT NULL default '',
  `ClanRank` int(3) NOT NULL default '0',
  `BonusStatus` int(10) NOT NULL default '0',
  `ElixirStatus` int(10) NOT NULL default '0',
  `ElfAttr` int(10) NOT NULL default '0',
  `PKcount` int(10) NOT NULL default '0',
  `PkCountForElf` int(10) NOT NULL default '0',
  `ExpRes` int(10) NOT NULL default '0',
  `PartnerID` int(10) NOT NULL default '0',
  `AccessLevel` int(10) unsigned NOT NULL default '0',
  `OnlineStatus` int(10) unsigned NOT NULL default '0',
  `HomeTownID` int(10) NOT NULL default '0',
  `Contribution` int(10) NOT NULL default '0',
  `Pay` int(10) NOT NULL default '0',
  `HellTime` int(10) unsigned NOT NULL default '0',
  `Banned` tinyint(1) unsigned NOT NULL default '0',
  `Karma` int(10) NOT NULL default '0',
  `LastPk` datetime default NULL,
  `LastPkForElf` datetime default NULL,
  `DeleteTime` datetime default NULL,
  `LastActive` datetime default NULL,
  `EinhasadPoint` int(10) default NULL,
  `OriginalStr` int(3) NOT NULL default '0',
  `OriginalCon` int(3) NOT NULL default '0',
  `OriginalDex` int(3) NOT NULL default '0',
  `OriginalCha` int(3) NOT NULL default '0',
  `OriginalInt` int(3) NOT NULL default '0',
  `OriginalWis` int(3) NOT NULL default '0',
  PRIMARY KEY  (`objid`),
  KEY `key_id` (`account_name`,`char_name`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of characters
-- ----------------------------
