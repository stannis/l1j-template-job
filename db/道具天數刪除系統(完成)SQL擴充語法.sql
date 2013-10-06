#於 shop資料表 gash_price欄位 之後新增 delete_day欄位
ALTER TABLE `shop` Add `delete_day` int(10) unsigned NOT NULL default '0' AFTER `gash_price`;
#於 shop資料表 delete_day欄位 之後新增 delete_date欄位
ALTER TABLE `shop` Add `delete_date` datetime default NULL AFTER `delete_day`;
