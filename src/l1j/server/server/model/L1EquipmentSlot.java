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
package l1j.server.server.model;

import static l1j.server.server.model.skill.L1SkillId.BLIND_HIDING;
import static l1j.server.server.model.skill.L1SkillId.COUNTER_BARRIER;
import static l1j.server.server.model.skill.L1SkillId.DETECTION;
import static l1j.server.server.model.skill.L1SkillId.ENCHANT_WEAPON;
import static l1j.server.server.model.skill.L1SkillId.EXTRA_HEAL;
import static l1j.server.server.model.skill.L1SkillId.GREATER_HASTE;
import static l1j.server.server.model.skill.L1SkillId.HASTE;
import static l1j.server.server.model.skill.L1SkillId.HEAL;
import static l1j.server.server.model.skill.L1SkillId.INVISIBILITY;
import static l1j.server.server.model.skill.L1SkillId.PHYSICAL_ENCHANT_DEX;
import static l1j.server.server.model.skill.L1SkillId.PHYSICAL_ENCHANT_STR;
import static l1j.server.server.model.skill.L1SkillId.STATUS_BRAVE;

import java.lang.Integer;
import java.util.List;

import l1j.server.server.datatables.SkillsTable;
import l1j.server.server.datatables.WeaponSoulTable;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.item.action.Effect;
import l1j.server.server.serverpackets.S_Ability;
import l1j.server.server.serverpackets.S_AddSkill;
import l1j.server.server.serverpackets.S_DelSkill;
import l1j.server.server.serverpackets.S_Invis;
import l1j.server.server.serverpackets.S_RemoveObject;
import l1j.server.server.serverpackets.S_SPMR;
import l1j.server.server.serverpackets.S_ServerMessage;
import l1j.server.server.serverpackets.S_SkillBrave;
import l1j.server.server.serverpackets.S_SkillHaste;
import l1j.server.server.serverpackets.S_SkillSound;
import l1j.server.server.templates.L1Item;
import l1j.server.server.templates.L1WeaponSoul;
import l1j.server.server.utils.collections.Lists;

public class L1EquipmentSlot {
	private L1PcInstance _owner;

	/**
	 * 効果中のセットアイテム
	 */
	private List<L1ArmorSet> _currentArmorSet;

	private L1ItemInstance _weapon;

	private List<L1ItemInstance> _armors;

	public L1EquipmentSlot(L1PcInstance owner) {
		_owner = owner;

		_armors = Lists.newList();
		_currentArmorSet = Lists.newList();
	}

	private void setWeapon(L1ItemInstance weapon) {
		_owner.setWeapon(weapon);
		_owner.setCurrentWeapon(weapon.getItem().getType1());
		weapon.startEquipmentTimer(_owner);
		_weapon = weapon;
	}

	public L1ItemInstance getWeapon() {
		return _weapon;
	}

	private void setArmor(L1ItemInstance armor) {
		L1Item item = armor.getItem();
		int itemId = armor.getItem().getItemId();
		// 飾品不加防判斷
		if (armor.getItem().getType2() == 2 && armor.getItem().getType() >= 8
				&& armor.getItem().getType() <= 12) {
			_owner.addAc(item.get_ac() - armor.getAcByMagic());
		} else {
			_owner.addAc(item.get_ac() - armor.getEnchantLevel()
					- armor.getAcByMagic());
		}
		// add 防具階級近戰修正 by testt
		if (armor.getItem().getType() < 8) {
			_owner.addShortStepFix(armor.getStepLevel() * armor.getItemLevel());
		}
		// end
		_owner.addDamageReductionByArmor(item.getDamageReduction());
		_owner.addWeightReduction(item.getWeightReduction());
		_owner.addHitModifierByArmor(item.getHitModifierByArmor());
		_owner.addDmgModifierByArmor(item.getDmgModifierByArmor());
		_owner.addBowHitModifierByArmor(item.getBowHitModifierByArmor());
		_owner.addBowDmgModifierByArmor(item.getBowDmgModifierByArmor());
		_owner.addRegistStun(item.get_regist_stun());
		_owner.addRegistStone(item.get_regist_stone());
		_owner.addRegistSleep(item.get_regist_sleep());
		_owner.add_regist_freeze(item.get_regist_freeze());
		_owner.addRegistSustain(item.get_regist_sustain());
		_owner.addRegistBlind(item.get_regist_blind());
		// 飾品強化 Scroll of Enchant Accessory
		_owner.addEarth(item.get_defense_earth() + armor.getEarthMr());
		_owner.addWind(item.get_defense_wind() + armor.getWindMr());
		_owner.addWater(item.get_defense_water() + armor.getWaterMr());
		_owner.addFire(item.get_defense_fire() + armor.getFireMr());

		_armors.add(armor);

		for (L1ArmorSet armorSet : L1ArmorSet.getAllSet()) {
			if (armorSet.isPartOfSet(itemId) && armorSet.isValid(_owner)) {
				if ((armor.getItem().getType2() == 2)
						&& (armor.getItem().getType() == 9)) { // ring
					if (!armorSet.isEquippedRingOfArmorSet(_owner)) {
						armorSet.giveEffect(_owner);
						_currentArmorSet.add(armorSet);
					}
				} else {
					armorSet.giveEffect(_owner);
					_currentArmorSet.add(armorSet);
				}
			}
		}

		if ((itemId == 20077) || (itemId == 20062) || (itemId == 120077)) {
			if (!_owner.hasSkillEffect(INVISIBILITY)) {
				_owner.killSkillEffectTimer(BLIND_HIDING);
				_owner.setSkillEffect(INVISIBILITY, 0);
				_owner.sendPackets(new S_Invis(_owner.getId(), 1));
				_owner.broadcastPacketForFindInvis(new S_RemoveObject(_owner),
						false);
				// _owner.broadcastPacket(new S_RemoveObject(_owner));
			}
		}
		if (itemId == 20288) { // ROTC
			_owner.sendPackets(new S_Ability(1, true));
		}
		if (itemId == 20383) { // 騎馬用ヘルム
			if (armor.getChargeCount() != 0) {
				armor.setChargeCount(armor.getChargeCount() - 1);
				_owner.getInventory().updateItem(armor,
						L1PcInventory.COL_CHARGE_COUNT);
			}
		}
		armor.startEquipmentTimer(_owner);
	}

	public List<L1ItemInstance> getArmors() {
		return _armors;
	}

	private void removeWeapon(L1ItemInstance weapon) {
		_owner.setWeapon(null);
		_owner.setCurrentWeapon(0);
		weapon.stopEquipmentTimer(_owner);
		_weapon = null;
		if (_owner.hasSkillEffect(COUNTER_BARRIER)) {
			_owner.removeSkillEffect(COUNTER_BARRIER);
		}
	}

	private void removeArmor(L1ItemInstance armor) {
		L1Item item = armor.getItem();
		int itemId = armor.getItem().getItemId();
		// 飾品不加防判斷
		if (armor.getItem().getType2() == 2 && armor.getItem().getType() >= 8
				&& armor.getItem().getType() <= 12) {
			_owner.addAc(-(item.get_ac() - armor.getAcByMagic()));
		} else {
			_owner.addAc(-(item.get_ac() - armor.getEnchantLevel() - armor
					.getAcByMagic()));
		}
		// add 防具階級近戰修正 by testt
		if (armor.getItem().getType() < 8) {
			_owner.addShortStepFix(-(armor.getStepLevel() * armor.getItemLevel()));
		}
		// end
		_owner.addDamageReductionByArmor(-item.getDamageReduction());
		_owner.addWeightReduction(-item.getWeightReduction());
		_owner.addHitModifierByArmor(-item.getHitModifierByArmor());
		_owner.addDmgModifierByArmor(-item.getDmgModifierByArmor());
		_owner.addBowHitModifierByArmor(-item.getBowHitModifierByArmor());
		_owner.addBowDmgModifierByArmor(-item.getBowDmgModifierByArmor());
		_owner.addRegistStun(-item.get_regist_stun());
		_owner.addRegistStone(-item.get_regist_stone());
		_owner.addRegistSleep(-item.get_regist_sleep());
		_owner.add_regist_freeze(-item.get_regist_freeze());
		_owner.addRegistSustain(-item.get_regist_sustain());
		_owner.addRegistBlind(-item.get_regist_blind());
		// 飾品強化 Scroll of Enchant Accessory
		_owner.addEarth(-item.get_defense_earth() - armor.getEarthMr());
		_owner.addWind(-item.get_defense_wind() - armor.getWindMr());
		_owner.addWater(-item.get_defense_water() - armor.getWaterMr());
		_owner.addFire(-item.get_defense_fire() - armor.getFireMr());

		for (L1ArmorSet armorSet : L1ArmorSet.getAllSet()) {
			if (armorSet.isPartOfSet(itemId)
					&& _currentArmorSet.contains(armorSet)
					&& !armorSet.isValid(_owner)) {
				armorSet.cancelEffect(_owner);
				_currentArmorSet.remove(armorSet);
			}
		}

		if ((itemId == 20077) || (itemId == 20062) || (itemId == 120077)) {
			_owner.delInvis(); // インビジビリティ状態解除
		}
		if (itemId == 20288) { // ROTC
			_owner.sendPackets(new S_Ability(1, false));
		}
		armor.stopEquipmentTimer(_owner);

		_armors.remove(armor);
	}

	public void set(L1ItemInstance equipment) {
		L1Item item = equipment.getItem();
		
		boolean chklist=false;
		L1WeaponSoul ws = new L1WeaponSoul();
//		L1Object objid = equipment.getId();
		if (item.getType2() == 0) {
			return;
		}

		if (item.get_addhp() != 0) {
			_owner.addMaxHp(item.get_addhp());
		}
		if (item.get_addmp() != 0) {
			_owner.addMaxMp(item.get_addmp());
		}
		if (equipment.getaddHp() != 0) {
			_owner.addMaxHp(equipment.getaddHp());
		}
		if (equipment.getaddMp() != 0) {
			_owner.addMaxMp(equipment.getaddMp());
		}
		_owner.addStr(item.get_addstr());
		_owner.addCon(item.get_addcon());
		_owner.addDex(item.get_adddex());
		_owner.addInt(item.get_addint());
		_owner.addWis(item.get_addwis());
		if (item.get_addwis() != 0) {
			_owner.resetBaseMr();
		}
		_owner.addCha(item.get_addcha());

		int addMr = 0;
		addMr += equipment.getMr();
		if ((item.getItemId() == 20236) && _owner.isElf()) {
			addMr += 5;
		}
		if (addMr != 0) {
			_owner.addMr(addMr);
			_owner.sendPackets(new S_SPMR(_owner));
		}
		if (item.get_addsp() != 0 || equipment.getaddSp() != 0) {
			_owner.addSp(item.get_addsp() + equipment.getaddSp());
			_owner.sendPackets(new S_SPMR(_owner));
		}
		if (item.isHasteItem()) {
			if (_owner.getHasteItemByDoll() == 1) { // 判斷是否有加速狀態魔法娃娃
				_owner.setHasteItemByDoll(0); // 移除
			}
			_owner.setHasteItemEquipped(1); // 修正累加錯誤
			_owner.removeHasteSkillEffect();
			if (_owner.getMoveSpeed() != 1 && _owner.getHasteItemEquipped() == 1) {
				_owner.setMoveSpeed(1);
				_owner.sendPackets(new S_SkillHaste(_owner.getId(), 1, -1));
				_owner.broadcastPacket(new S_SkillHaste(_owner.getId(), 1, 0));
			}
		}
		if (item.getItemId() == 20383) { // 騎馬用ヘルム
			if (_owner.hasSkillEffect(STATUS_BRAVE)) {
				_owner.killSkillEffectTimer(STATUS_BRAVE);
				_owner.sendPackets(new S_SkillBrave(_owner.getId(), 0, 0));
				_owner.broadcastPacket(new S_SkillBrave(_owner.getId(), 0, 0));
				_owner.setBraveSpeed(0);
			}
		}
		_owner.getEquipSlot().setMagicHelm(equipment);
		// 增加裝備延遲中斷，避免玩家不斷穿脫裝備增加伺服器負擔 by testt
		try {
			Thread.sleep(10);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		//end
		if (item.getType2() == 1) {
			setWeapon(equipment);
		} else if (item.getType2() == 2) {
			if (item.getType() == 15){//穿上輔助裝備(左)時會有特殊魔法效果
				_owner.setCurrentRune(equipment.getId()); //在pcinstance中加入現在穿的輔助裝備id
				//讀取武魂石的等級
				for (L1WeaponSoul wss : WeaponSoulTable.getNewInstance().getWeaponSoullList().values()) {
					if (wss.get_itemobjid() == equipment.getId()) {
						// 武魂已在清單中
						try {
							Thread.sleep(0);//避免重複占用執行緒
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						chklist=true;
						ws =  wss;
						break;
					}
					//System.out.println(wss.get_itemobjid());
				}					
				
				if (chklist){
					switch (ws.get_type()){
						case 0:
							_owner.sendPackets(new S_ServerMessage(166, "感覺到武魂的氣息"));
							_owner.sendPackets(new S_SkillSound(_owner.getId(), 4229));
							_owner.broadcastPacket(new S_SkillSound(_owner.getId(), 4229));
							break;
						case 1:
							_owner.sendPackets(new S_ServerMessage(166, "感覺到武魂的力量"));
							_owner.sendPackets(new S_SkillSound(_owner.getId(), 5288));
							_owner.broadcastPacket(new S_SkillSound(_owner.getId(), 5288));
							break;
						case 2:
							_owner.sendPackets(new S_ServerMessage(166, "感覺到武魂的強烈力量"));
							_owner.sendPackets(new S_SkillSound(_owner.getId(), 4469));
							_owner.broadcastPacket(new S_SkillSound(_owner.getId(), 4469));
							break;
						}					
				}
				else{
					_owner.sendPackets(new S_ServerMessage(166, "與武魂建立第一次接觸"));
					_owner.sendPackets(new S_SkillSound(_owner.getId(), 4661));
					_owner.broadcastPacket(new S_SkillSound(_owner.getId(), 4661));
					WeaponSoulTable.getInstance().storeNewWeaponSoul(_owner, equipment.getItemId(), equipment.getId());
				}
			} else if (item.getType() == 14) {//輔助裝備(右邊) by testt
				int effectId = item.getItemId() - 296006;
				int gfxId = item.getItemId() - 292706;
				switch (effectId) {
				case 4001:
				case 4002:
				case 4003:
				case 4004:
					Effect.deleteRepeatedSkills(_owner, effectId);
					_owner.setSkillEffect(effectId, 0);
					_owner.sendPackets(new S_ServerMessage(1292));
					_owner.sendPackets(new S_SkillSound(_owner.getId(), gfxId));
					_owner.broadcastPacket(new S_SkillSound(_owner.getId(), gfxId));
					break;
				default:
					break;
				}
			}
				
			setArmor(equipment);
			_owner.sendPackets(new S_SPMR(_owner));
		}
	}



	public void remove(L1ItemInstance equipment) {
		L1Item item = equipment.getItem();
		if (item.getType2() == 0) {
			return;
		}

		if (item.get_addhp() != 0) {
			_owner.addMaxHp(-item.get_addhp());
		}
		if (item.get_addmp() != 0) {
			_owner.addMaxMp(-item.get_addmp());
		}
		if (equipment.getaddHp() != 0) {
			_owner.addMaxHp(-equipment.getaddHp());
		}
		if (equipment.getaddMp() != 0) {
			_owner.addMaxMp(-equipment.getaddMp());
		}
		_owner.addStr((byte) -item.get_addstr());
		_owner.addCon((byte) -item.get_addcon());
		_owner.addDex((byte) -item.get_adddex());
		_owner.addInt((byte) -item.get_addint());
		_owner.addWis((byte) -item.get_addwis());
		if (item.get_addwis() != 0) {
			_owner.resetBaseMr();
		}
		_owner.addCha((byte) -item.get_addcha());

		int addMr = 0;
		addMr -= equipment.getMr();
		if ((item.getItemId() == 20236) && _owner.isElf()) {
			addMr -= 5;
		}
		if (addMr != 0) {
			_owner.addMr(addMr);
			_owner.sendPackets(new S_SPMR(_owner));
		}
		if (item.get_addsp() != 0 || equipment.getaddSp() != 0) {
			_owner.addSp(-(item.get_addsp() + equipment.getaddSp()));
			_owner.sendPackets(new S_SPMR(_owner));
		}
		if (item.isHasteItem()) {
			_owner.setHasteItemEquipped(0); // 修正累加錯誤
			if (_owner.getHasteItemEquipped() == 0) {
				_owner.setMoveSpeed(0);
				_owner.sendPackets(new S_SkillHaste(_owner.getId(), 0, 0));
				_owner.broadcastPacket(new S_SkillHaste(_owner.getId(), 0, 0));
			}
		}
		_owner.getEquipSlot().removeMagicHelm(_owner.getId(), equipment);
		//武魂及經驗符石脫下的處理by testt
		_owner.setCurrentRune(0);
		if (item.getType() == 14) {//輔助裝備(右邊) by testt
			int effectId = item.getItemId() - 296006;
			switch (effectId) {
			case 4001:
			case 4002:
			case 4003:
			case 4004:
				_owner.removeSkillEffect(effectId);
				_owner.sendPackets(new S_ServerMessage(166, "狩獵經驗回復到原始狀態"));
				break;
			default:
				break;
			}
		}
		//end
		
		
		if (item.getType2() == 1) {
			removeWeapon(equipment);
		} else if (item.getType2() == 2) {
			removeArmor(equipment);
		}
	}

	public void setMagicHelm(L1ItemInstance item) {
		switch (item.getItemId()) {
		case 20013:
			_owner.setSkillMastery(PHYSICAL_ENCHANT_DEX);
			_owner.setSkillMastery(HASTE);
			_owner.sendPackets(new S_AddSkill(0, 0, 0, 2, 0, 4, 0, 0, 0, 0, 0,
					0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0));
			break;
		case 20014:
			_owner.setSkillMastery(HEAL);
			_owner.setSkillMastery(EXTRA_HEAL);
			_owner.sendPackets(new S_AddSkill(1, 0, 4, 0, 0, 0, 0, 0, 0, 0, 0,
					0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0));
			break;
		case 20015:
			_owner.setSkillMastery(ENCHANT_WEAPON);
			_owner.setSkillMastery(DETECTION);
			_owner.setSkillMastery(PHYSICAL_ENCHANT_STR);
			_owner.sendPackets(new S_AddSkill(0, 24, 0, 0, 0, 2, 0, 0, 0, 0, 0,
					0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0));
			break;
		case 20008:
			_owner.setSkillMastery(HASTE);
			_owner.sendPackets(new S_AddSkill(0, 0, 0, 0, 0, 4, 0, 0, 0, 0, 0,
					0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0));
			break;
		case 20023:
			_owner.setSkillMastery(HASTE);
			_owner.setSkillMastery(GREATER_HASTE);
			_owner.sendPackets(new S_AddSkill(0, 0, 0, 0, 0, 4, 32, 0, 0, 0, 0,
					0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0));
			break;
		}
	}

	public void removeMagicHelm(int objectId, L1ItemInstance item) {
		switch (item.getItemId()) {
		case 20013: // 敏捷魔法頭盔
			if (!SkillsTable.getInstance().spellCheck(objectId,
					PHYSICAL_ENCHANT_DEX)) {
				_owner.removeSkillMastery(PHYSICAL_ENCHANT_DEX);
				_owner.sendPackets(new S_DelSkill(0, 0, 0, 2, 0, 0, 0, 0, 0, 0,
						0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0));
			}
			if (!SkillsTable.getInstance().spellCheck(objectId, HASTE)) {
				_owner.removeSkillMastery(HASTE);
				_owner.sendPackets(new S_DelSkill(0, 0, 0, 0, 0, 4, 0, 0, 0, 0,
						0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0));
			}
			break;
		case 20014: // 治癒魔法頭盔
			if (!SkillsTable.getInstance().spellCheck(objectId, HEAL)) {
				_owner.removeSkillMastery(HEAL);
				_owner.sendPackets(new S_DelSkill(1, 0, 0, 0, 0, 0, 0, 0, 0, 0,
						0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0));
			}
			if (!SkillsTable.getInstance().spellCheck(objectId, EXTRA_HEAL)) {
				_owner.removeSkillMastery(EXTRA_HEAL);
				_owner.sendPackets(new S_DelSkill(0, 0, 4, 0, 0, 0, 0, 0, 0, 0,
						0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0));
			}
			break;
		case 20015: // 力量魔法頭盔
			if (!SkillsTable.getInstance().spellCheck(objectId, ENCHANT_WEAPON)) {
				_owner.removeSkillMastery(ENCHANT_WEAPON);
				_owner.sendPackets(new S_DelSkill(0, 8, 0, 0, 0, 0, 0, 0, 0, 0,
						0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0));
			}
			if (!SkillsTable.getInstance().spellCheck(objectId, DETECTION)) {
				_owner.removeSkillMastery(DETECTION);
				_owner.sendPackets(new S_DelSkill(0, 16, 0, 0, 0, 0, 0, 0, 0,
						0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0));
			}
			if (!SkillsTable.getInstance().spellCheck(objectId, PHYSICAL_ENCHANT_STR)) {
				_owner.removeSkillMastery(PHYSICAL_ENCHANT_STR);
				_owner.sendPackets(new S_DelSkill(0, 0, 0, 0, 0, 2, 0, 0, 0, 0,
						0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0));
			}
			break;
		case 20008: // 小型風之頭盔
			if (!SkillsTable.getInstance().spellCheck(objectId, HASTE)) {
				_owner.removeSkillMastery(HASTE);
				_owner.sendPackets(new S_DelSkill(0, 0, 0, 0, 0, 4, 0, 0, 0, 0,
						0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0));
			}
			break;
		case 20023: // 風之頭盔
			if (!SkillsTable.getInstance().spellCheck(objectId, HASTE)) {
				_owner.removeSkillMastery(HASTE);
				_owner.sendPackets(new S_DelSkill(0, 0, 0, 0, 0, 4, 0, 0, 0, 0,
						0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0));
			}
			if (!SkillsTable.getInstance().spellCheck(objectId, GREATER_HASTE)) {
				_owner.removeSkillMastery(GREATER_HASTE);
				_owner.sendPackets(new S_DelSkill(0, 0, 0, 0, 0, 0, 32, 0, 0,
						0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0));
			}
			break;
		}
	}

}
