#於 armor資料表 itemdesc_id欄位 之後新增 itemdesc_extra欄位
ALTER TABLE `armor` Add `itemdesc_extra` varchar(1000) default NULL AFTER `itemdesc_id`;

#於 etcitem資料表 itemdesc_id欄位 之後新增 itemdesc_extra欄位
ALTER TABLE `etcitem` Add `itemdesc_extra` varchar(1000) default NULL AFTER `itemdesc_id`;

#於 weapon資料表 itemdesc_id欄位 之後新增 itemdesc_extra欄位
ALTER TABLE `weapon` Add `itemdesc_extra` varchar(1000) default NULL AFTER `itemdesc_id`;
