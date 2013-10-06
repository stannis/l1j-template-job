#於 characters資料表 char_name欄位 之後新增 FamePoint欄位
ALTER TABLE `characters` ADD `FamePoint` int(10) default '0' AFTER `char_name`;
