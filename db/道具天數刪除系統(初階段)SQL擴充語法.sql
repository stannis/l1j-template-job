#於 beginner資料表 bless欄位 之後新增 DeleteDay欄位
ALTER TABLE `beginner` Add `DeleteDay` int(10) NOT NULL default '0' AFTER `bless`;

#於 character_items資料表 m_def欄位 之後新增 DeleteDate欄位
ALTER TABLE `character_items` Add `DeleteDate` datetime default NULL AFTER `m_def`;
