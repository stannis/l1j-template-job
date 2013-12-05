/**
 *                            License
 * THE WORK (AS DEFINED BELOW) IS PROVIDED UNDER THE TERMS OF THIS  
 * CREATIVE COMMONS PUBLIC LICENSE ("CCPL" OR "LICENSE"). 
 * THE WORK IS PROTECTED BY COPYRIGHT AND/OR OTHER APPLICABLE LAW.  
 * ANY USE OF THE WORK OTHER THAN AS AUTHORIZED UNDER THIS LICENSE OR  
 * COPYRIGHT LAW IS PROHIBITED.
 * 
 * BY EXERCISING ANY RIGHTS TO THE WORK PROVIDED HERE, YOU ACCEPT AND  
 * AGREE TO BE BOUND BY THE TERMS OF THIS LICENSE. TO THE EXTENT THIS LICENSE  
 * MAY BE CONSIDERED TO BE A CONTRACT, THE LICENSOR GRANTS YOU THE RIGHTS CONTAINED 
 * HERE IN CONSIDERATION OF YOUR ACCEPTANCE OF SUCH TERMS AND CONDITIONS.
 * 
 */
package l1j.server.server.model.item.action;

import l1j.server.server.ClientThread;
import l1j.server.server.datatables.DropItemTable;
import l1j.server.server.datatables.ItemTable;
import l1j.server.server.model.L1Inventory;
import l1j.server.server.model.L1PcInventory;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_ServerMessage;
import l1j.server.server.serverpackets.S_NPCTalkReturn;
import l1j.server.server.templates.L1Item;
import l1j.server.server.utils.random.Random;

public class FilterItem {
	
	public static void useFilterItem(L1PcInstance pc, L1ItemInstance f_item,
			L1ItemInstance u_item, ClientThread client) {
		if(u_item.getItemId() == 61018){ // 升級石擷取器by testt
			int stonebasenum = 61000;
			int goldcoinid = 60000;//商城幣編號
			L1Item goldcoin = ItemTable.getInstance().getTemplate(goldcoinid);
			if (f_item.getItemLevel() > 0) {//判斷武器等級				
				//武器大於3級擷取升級石需要消耗gold幣
				if (!pc.getInventory().checkItem(goldcoinid, 300) && 
						f_item.getItemLevel() > 3) {
					pc.sendPackets(new S_ServerMessage(337, goldcoin.getName()
							+ "("
							+ (300 - pc.getInventory().countItems(
									goldcoinid)) + ")"));
					return;
				}
				L1ItemInstance item = ItemTable.getInstance().createItem(
						stonebasenum + f_item.getItemLevel());
				item.setCount(1);
				if (item != null) {
					if (pc.getInventory().checkAddItem(item, 1) == L1Inventory.OK) {
						pc.getInventory().storeItem(item);
					} else { // 持てない場合は地面に落とす 處理のキャンセルはしない（不正防止）
						L1World.getInstance()
						.getInventory(pc.getX(), pc.getY(),
								pc.getMapId()).storeItem(item);
					}
					pc.sendPackets(new S_ServerMessage(166, "你的 " + f_item.getLogName() + " 黯淡下來，裝備等級還原到最初狀態"));
					pc.sendPackets(new S_ServerMessage(403, item.getLogName())); // 獲得XXX
					//如果武器等級大於3則消耗gold幣
					if (f_item.getItemLevel() > 3)
						pc.getInventory().consumeItem(goldcoinid, 300);
						f_item.setItemLevel(0);
						//更新客戶端物品資料
						client.getActiveChar().getInventory().updateItem(f_item, L1PcInventory.COL_ITEM_ENCHANT_LEVEL);
						//DB儲存物品資料
						client.getActiveChar().getInventory().saveItem(f_item, L1PcInventory.COL_ITEM_ENCHANT_LEVEL);	
				}
			}
			else {
				pc.sendPackets(new S_ServerMessage(79)); // \f1沒有任何事情發生。
			}
		}else if(u_item.getItemId() == 61019) { // 煉金術by testt
			int count = 0;
			int itemid = 60000;
			switch (f_item.getItemId()) {
			case 61001:
				count = 10;
				break;
			case 61002:
				count = 40;
				break;
			case 61003:
				count = 160;
				break;
			case 61004:
				count = 640;
				break;
			case 61005:
				count = 2048;
				break;
			case 61006:
				count = 6554;
				break;
			case 61007:
				count = 20972;
				break;
			case 61008:
				count = 67109;
				break;
			case 61009:
				count = 214748;
				break;
			default:				
				if (f_item.getItem().getMaterial() == 9
				|| DropItemTable.getInstance().checkDropItem(f_item.getItemId())
				|| f_item.getStepLevel() != 0) { //骨製品及部分雜物可以煉金，若物品有等級則無法煉金by testt
					count = 2000;
					itemid = 40308;
				}
				else {
					pc.sendPackets(new S_ServerMessage(79));
					return;
				}
				break;
			}
			L1ItemInstance item = ItemTable.getInstance().createItem(
					itemid);
			int rnd = Random.nextInt(count) + 1;
			item.setCount(rnd);
			if (item != null) {
				if (pc.getInventory().checkAddItem(item, rnd) == L1Inventory.OK) {
					pc.getInventory().storeItem(item);
				} else { // 持てない場合は地面に落とす 處理のキャンセルはしない（不正防止）
					L1World.getInstance()
					.getInventory(pc.getX(), pc.getY(),
							pc.getMapId()).storeItem(item);
				}
				pc.sendPackets(new S_ServerMessage(166, "你的 " + f_item.getLogName() + " 放出一道光芒，變成了 " + item.getLogName()));
				pc.sendPackets(new S_ServerMessage(403, item.getLogName())); // 獲得XXX
				pc.getInventory().consumeItem(f_item.getItemId(), 1);
			}
		}
		else {
			pc.sendPackets(new S_ServerMessage(79)); // \f1沒有任何事情發生。
		}				
	}

	public static void useFilterItem(L1PcInstance pc, L1ItemInstance f_item,
			L1ItemInstance u_item) {
		if ((f_item == null) || (u_item == null)) {
			pc.sendPackets(new S_ServerMessage(79)); // \f1沒有任何事情發生。
			return;
		}
		if(u_item.getItemId() == 60014){ // 物品過濾器
			if(pc.checkFilterList(f_item.getItemId())){
				pc.deleteFilterList(f_item.getItemId());
				pc.sendPackets(new S_ServerMessage(166, f_item.getItem().getName()+" 已從物品過濾清單刪除"));	
				String msg[] = { pc.getFilterList() };
				pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "filter_list", msg));
				return;
			}
			pc.addFilterList(f_item.getItemId());
			pc.sendPackets(new S_ServerMessage(166, f_item.getItem().getName()+" 已從物品過濾清單加入"));	
			String msg[] = { pc.getFilterList() };
			pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "filter_list", msg));
		}
	}
	
	public static void useFilterItemCleaner(L1PcInstance pc, L1ItemInstance u_item) {
		if (u_item == null) {
			pc.sendPackets(new S_ServerMessage(79)); // \f1沒有任何事情發生。
			return;
		}
		if(u_item.getItemId() == 60015){ // 物品過濾清單
			String msg[] = { pc.getFilterList() };
			pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "filter_list", msg));
		}
		if (u_item.getItemId() == 60023){// C級武器兌換卷 by testt
			pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "getstep0"));
		}
		if (u_item.getItemId() == 43002) {// 轉生卷軸 by testt
			pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "rebirth_get"));
		}
	}

}
