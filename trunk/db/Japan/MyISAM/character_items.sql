-- ----------------------------
-- Table structure for character_items
-- ----------------------------
CREATE TABLE `character_items` (
  `id` int(11) NOT NULL DEFAULT '0',
  `item_id` int(11) DEFAULT NULL,
  `char_id` int(11) DEFAULT NULL,
  `item_name` varchar(255) DEFAULT NULL,
  `count` int(11) DEFAULT NULL,
  `is_equipped` int(11) DEFAULT NULL,
  `enchantlvl` int(11) DEFAULT NULL,
  `is_id` int(11) DEFAULT NULL,
  `durability` int(11) DEFAULT NULL,
  `charge_count` int(11) DEFAULT NULL,
  `remaining_time` int(11) DEFAULT NULL,
  `last_used` datetime DEFAULT NULL,
  `bless` int(11) DEFAULT NULL,
  `attr_enchant_kind` int(11) DEFAULT NULL,
  `attr_enchant_level` int(11) DEFAULT NULL,
  `firemr` int(11) DEFAULT NULL,
  `watermr` int(11) DEFAULT NULL,
  `earthmr` int(11) DEFAULT NULL,
  `windmr` int(11) DEFAULT NULL,
  `addsp` int(11) DEFAULT NULL,
  `addhp` int(11) DEFAULT NULL,
  `addmp` int(11) DEFAULT NULL,
  `hpr` int(11) DEFAULT NULL,
  `mpr` int(11) DEFAULT NULL,
  
  PRIMARY KEY (`id`),
  KEY `key_id` (`char_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records 
-- ----------------------------
