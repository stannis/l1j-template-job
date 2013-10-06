#----------------------------
# Table structure for armor_set
#----------------------------
CREATE TABLE `armor_set` (
  `id` int(10) NOT NULL auto_increment,
  `note` varchar(45) default NULL,
  `sets` varchar(1000) NOT NULL,
  `polyid` int(10) NOT NULL default '0',
  `ac` int(2) NOT NULL default '0',
  `hp` int(5) NOT NULL default '0',
  `mp` int(5) NOT NULL default '0',
  `hpr` int(5) NOT NULL default '0',
  `mpr` int(5) NOT NULL default '0',
  `mr` int(5) NOT NULL default '0',
  `str` int(2) NOT NULL default '0',
  `dex` int(2) NOT NULL default '0',
  `con` int(2) NOT NULL default '0',
  `wis` int(2) NOT NULL default '0',
  `cha` int(2) NOT NULL default '0',
  `intl` int(2) NOT NULL default '0',
  `defense_water` int(2) NOT NULL default '0',
  `defense_wind` int(2) NOT NULL default '0',
  `defense_fire` int(2) NOT NULL default '0',
  `defense_earth` int(2) NOT NULL default '0',
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COMMENT='MyISAM free: 10240 kB';

#----------------------------
# Records for table armor_set
#----------------------------
insert  into armor_set values 
(1, 'デーモンセット', '20009,20099,20165,20197', 3889, -2, 0, 0, 5, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
(2, 'デスナイトセット', '20010,20100,20166,20198', 6137, -4, 0, 0, -7, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
(3, '反王セット', '20024,20118,20170,20203', 3903, -3, 0, 0, 12, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
(4, 'カーツセット', '20041,20150,20184,20214', 3101, -4, 0, 0, -7, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
(5, 'ケレニスセット', '20042,20151,20185,20215', 3902, -2, 0, 100, 0, 12, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
(6, 'パンプキン', '20047', 2501, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
(7, 'ヴァンパイア', '20079', 3952, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
(8, 'デス', '20342', 2388, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
(9, 'ラビット', '20343', 4767, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
(10, 'キャロットラビット', '20344', 4769, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
(11, 'スケルトン', '20278', 2374, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
(12, 'オークファイター', '20277', 3864, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
(13, 'ウェアウルフ', '20250', 3865, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
(14, 'ハイ コリー', '20345', 4928, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
(15, 'ハイ ラクーン', '20346', 4929, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
(16, '袴', '20347', 4227, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
(17, '晴着', '20348', 3750, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
(18, 'コリー', '20349', 938, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
(19, 'スノーマン', '20350', 2064, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
(20, 'スノーマンセット', '20351,20352', 2064, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
(21, 'まねきねこ', '20420', 5719, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
(22, '赤いオーク', '20382', 6010, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
(23, 'ドレイク船長', '20452', 6089, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
(24, 'アイリス', '20453', 4001, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
(25, 'ナイトバルド', '20454', 4000, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
(26, 'サキュバス クイーン', '20455', 4004, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
(27, 'レッド ユニフォーム', '20456', 5184, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
(28, 'ブルー ユニフォーム', '20457', 5186, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
(29, '赤いオーク(赤いオークのマスク)', '20458', 6010, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
(30, '騎馬用ヘルム', '20383', 6080, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
(31, 'ハロウィン祝福ハット', '20380', 6400, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
(32, 'ハイ ベアーのチョーカー', '20419', 5976, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
(33, 'レザーセット', '20001,20090,20193,20219', -1, -3, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
(34, 'オークセット', '20034,20072,20135,20237', -1, -3, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
(35, 'ドワーフセット', '20007,20052,20223', -1, -1, 5, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
(36, 'スタデッドレザーセット', '20038,20148,20241,20212', -1, -3, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
(37, 'ボーンセット', '20045,20124,20221', -1, -2, 10, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
(38, '遠征隊員の遺品セット', '20389,20393,20401,20409,20406', -1, -2, 15, 10, 0, 0, 10, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
(39, 'ウィザードセット', '20012,20111', -1, 0, 0, 50, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
(40, 'アイアンセット', '20003,20091,20163,20194,20220', -1, -3, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
(41, 'パイレーツセット', '20044,20155,20188,20217', -1, -1, 10, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0),
(42, 'ヤヒセット', '20031,20069,20083,20131,20179,20209,20290,20261', -1, -88, 100, 100, 15, 15, 0, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0),
(43, '軍王セット', '20057,20109,20178,20200', -1, 0, 30, 30, 10, 10, 0, 0, 0, 0, 0, 3, 0, 0, 0, 0, 0),
(44, '真冥王セット', '20390,20395,20402,20410,20408', -1, -20, 100, 20, 10, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
(45, '濡れた装備セット', '21051,21052,21053,21054,21055,21056', -1, -10, 100, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 10, 0),
(46, '希望セット', '20413,20428', -1, 0, 0, 5, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
(47, '幸運セット', '20414,20430', -1, 0, 0, 10, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
(48, '情熱セット', '20415,20429', -1, 0, 10, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
(49, '真実セット', '20416,20431', -1, 0, 15, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
(50, '奇跡セット', '20417,20432', -1, 0, 15, 10, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
(51, '慈愛・勇気セット', '20418,20433', -1, 0, 0, 0, 2, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
(52, '赤呪のアミュレット・浄化のイアリング', '20423,21019', -1, 0, 0, 0, 0, 0, 0, 2, 0, -2, 0, 0, 0, 0, 0, 0, 0),
(53, '青呪のアミュレット・浄化のイアリング', '20424,21019', -1, 0, 0, 0, 0, 0, 0, 0, 0, 0, -2, 0, 2, 0, 0, 0, 0),
(54, '緑呪のアミュレット・浄化のイアリング', '20425,21019', -1, 0, 0, 0, 0, 0, 0, 0, 2, 0, 0, -2, 0, 0, 0, 0, 0),
(55, '男幽霊仮装セット', 21061, 5484, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
(56, '女幽霊仮装セット', 21062, 5412, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
(57, '男性水着：ブリーフ', 21063, 6746, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
(58, '男性水着：トランクス', 21064, 6747, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
(59, '女性水着：ワンピース', 21065, 6749, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
(60, '女性水着：ビキニ', 21066, 6750, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
