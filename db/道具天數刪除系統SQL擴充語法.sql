#於 beginner資料表 bless欄位 之後新增 DeleteDay欄位
ALTER TABLE `beginner` Add `DeleteDay` int(10) NOT NULL default '0' AFTER `bless`;

#於 character_items資料表 m_def欄位 之後新增 DeleteDate欄位
ALTER TABLE `character_items` Add `DeleteDate` datetime default NULL AFTER `m_def`;

#於 armor資料表 grade欄位 之後新增 delete_day欄位
ALTER TABLE `armor` Add `delete_day` int(10) unsigned NOT NULL default '0' AFTER `grade`;
#於 armor資料表 delete_day欄位 之後新增 delete_date欄位
ALTER TABLE `armor` Add `delete_date` datetime default NULL AFTER `delete_day`;

#於 etcitem資料表 save_at_once欄位 之後新增 delete_day欄位
ALTER TABLE `etcitem` Add `delete_day` int(10) unsigned NOT NULL default '0' AFTER `save_at_once`;
#於 etcitem資料表 delete_day欄位 之後新增 delete_date欄位
ALTER TABLE `etcitem` Add `delete_date` datetime default NULL AFTER `delete_day`;

#於 weapon資料表 max_use_time欄位 之後新增 delete_day欄位
ALTER TABLE `weapon` Add `delete_day` int(10) unsigned NOT NULL default '0' AFTER `max_use_time`;
#於 weapon資料表 delete_day欄位 之後新增 delete_date欄位
ALTER TABLE `weapon` Add `delete_date` datetime default NULL AFTER `delete_day`;

#於 shop資料表 gash_price欄位 之後新增 delete_day欄位
ALTER TABLE `shop` Add `delete_day` int(10) unsigned NOT NULL default '0' AFTER `gash_price`;
#於 shop資料表 delete_day欄位 之後新增 delete_date欄位
ALTER TABLE `shop` Add `delete_date` datetime default NULL AFTER `delete_day`;
