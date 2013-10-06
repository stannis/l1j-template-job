#於 magic_doll資料表 effect_chance欄位 之後新增 exp_rate_chance欄位
ALTER TABLE `magic_doll` ADD `exp_rate_chance` tinyint(2) NOT NULL default '0' AFTER `effect_chance`;

#於 magic_doll資料表 exp_rate_chance欄位 之後新增 exp_rate欄位
ALTER TABLE `magic_doll` ADD `exp_rate` int(10) NOT NULL default '100' AFTER `exp_rate_chance`;

#於 magic_doll資料表 exp_rate欄位 之後新增 isHaste欄位
ALTER TABLE `magic_doll` ADD `isHaste` tinyint(2) NOT NULL default '0' AFTER `exp_rate`;

#於 magic_doll資料表 isHaste欄位 之後新增 hp欄位
ALTER TABLE `magic_doll` ADD `hp` int(10) NOT NULL default '0' AFTER `isHaste`;

#於 magic_doll資料表 isHaste欄位 之後新增 mp欄位
ALTER TABLE `magic_doll` ADD `mp` int(10) NOT NULL default '0' AFTER `hp`;

#於 magic_doll資料表 mp欄位 之後新增 hit_modifier欄位
ALTER TABLE `magic_doll` ADD `hit_modifier` int(10) NOT NULL default '0' AFTER `mp`;

#於 magic_doll資料表 hit_modifier欄位 之後新增 dmg_modifier欄位
ALTER TABLE `magic_doll` ADD `dmg_modifier` int(10) NOT NULL default '0' AFTER `hit_modifier`;