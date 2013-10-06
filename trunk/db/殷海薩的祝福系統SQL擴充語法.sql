#於 characters資料表 DeleteTime欄位 之後新增 LastActive欄位
ALTER TABLE `characters` ADD `LastActive` datetime default NULL AFTER `DeleteTime`;

#於 characters資料表 LastActive欄位 之後新增 EinhasadPoint欄位
ALTER TABLE `characters` ADD `EinhasadPoint` int(10) default NULL AFTER `LastActive`;
