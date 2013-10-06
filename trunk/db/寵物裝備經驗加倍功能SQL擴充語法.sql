#於 petitem資料表 m_def欄位 之後新增 exp_rate欄位
ALTER TABLE `petitem` ADD  `exp_rate` int(10) NOT NULL default '100' AFTER `m_def`;
