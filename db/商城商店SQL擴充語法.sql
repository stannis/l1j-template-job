#於 shop資料表 purchasing_price欄位 之後新增 gash_price欄位
ALTER TABLE `shop` ADD `gash_price` int(10) NOT NULL default '-1' AFTER `purchasing_price`;
