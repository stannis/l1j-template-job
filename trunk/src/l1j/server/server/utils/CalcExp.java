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
package l1j.server.server.utils;

import static l1j.server.server.model.skill.L1SkillId.COOKING_1_7_N;
import static l1j.server.server.model.skill.L1SkillId.COOKING_1_7_S;
import static l1j.server.server.model.skill.L1SkillId.COOKING_2_7_N;
import static l1j.server.server.model.skill.L1SkillId.COOKING_2_7_S;
import static l1j.server.server.model.skill.L1SkillId.COOKING_3_7_N;
import static l1j.server.server.model.skill.L1SkillId.COOKING_3_7_S;
import static l1j.server.server.model.skill.L1SkillId.EFFECT_POTION_OF_EXP_150;
import static l1j.server.server.model.skill.L1SkillId.EFFECT_POTION_OF_EXP_175;
import static l1j.server.server.model.skill.L1SkillId.EFFECT_POTION_OF_EXP_200;
import static l1j.server.server.model.skill.L1SkillId.EFFECT_POTION_OF_EXP_225;
import static l1j.server.server.model.skill.L1SkillId.EFFECT_POTION_OF_EXP_250;
import static l1j.server.server.model.skill.L1SkillId.EFFECT_POTION_OF_BATTLE;

import java.util.List;
import java.util.logging.Logger;
import java.text.NumberFormat;

import l1j.server.Config;
import l1j.server.server.datatables.ExpTable;
import l1j.server.server.datatables.ItemTable;
import l1j.server.server.datatables.PetTable;
import l1j.server.server.model.L1Character;
import l1j.server.server.model.L1Inventory;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.Instance.L1PetInstance;
import l1j.server.server.model.Instance.L1SummonInstance;
import l1j.server.server.serverpackets.S_PetPack;
import l1j.server.server.serverpackets.S_ServerMessage;
import l1j.server.server.templates.L1Pet;
import l1j.server.server.templates.L1MagicDoll; // 經驗加倍魔法娃娃

// Referenced classes of package l1j.server.server.utils:
// CalcStat

public class CalcExp {

	private static final long serialVersionUID = 1L;

	private static Logger _log = Logger.getLogger(CalcExp.class.getName());

	public static final int MAX_EXP = ExpTable.getExpByLevel(100) - 1;

	private CalcExp() {
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public static void calcExp(L1PcInstance l1pcinstance, int targetid, List<L1Character> acquisitorList, List<Integer> hateList, int exp) {

		int i = 0;
		double party_level = 0;
		double dist = 0;
		int member_exp = 0;
		int member_lawful = 0;
		L1Object l1object = L1World.getInstance().findObject(targetid);
		L1NpcInstance npc = (L1NpcInstance) l1object;

		// ヘイトの合計を取得
		L1Character acquisitor;
		int hate = 0;
		int acquire_exp = 0;
		int acquire_lawful = 0;
		int party_exp = 0;
		int party_lawful = 0;
		int totalHateExp = 0;
		int totalHateLawful = 0;
		int partyHateExp = 0;
		int partyHateLawful = 0;
		int ownHateExp = 0;
		int coinrate=2;
		
		//修正經驗值設定過高會出錯的問題by testt
		switch(npc.getNpcId()){
		case 152001:
		case 152002:
		case 152003:
		case 152004:
			coinrate=10000;
		}

		if (acquisitorList.size() != hateList.size()) {
			return;
		}
		for (i = hateList.size() - 1; i >= 0; i--) {
			acquisitor = acquisitorList.get(i);
			hate = hateList.get(i);
			
			//if ((acquisitor != null) && !acquisitor.isDead()) {
			//死亡後，仇恨值不歸零  by testt
			if (acquisitor != null) {
				totalHateExp += hate;
				if (acquisitor instanceof L1PcInstance) {
					totalHateLawful += hate;
				}
			}
			else { // nullだったり死んでいたら排除
					acquisitorList.remove(i);
					hateList.remove(i);		
			}
		}
		if (totalHateExp == 0) { // 取得者がいない場合
			return;
		}

		if ((l1object != null) && !(npc instanceof L1PetInstance) && !(npc instanceof L1SummonInstance)) {
			// int exp = npc.get_exp();
			/*if (!L1World.getInstance().isProcessingContributionTotal() && (l1pcinstance.getHomeTownId() > 0)) {
				int contribution = npc.getLevel() / 10;
				l1pcinstance.addContribution(contribution);
			}*/ // 取消由打怪獲得村莊貢獻度，改由製作村莊福利品獲得貢獻度 for 3.3C
			int lawful = npc.getLawful();

			if (l1pcinstance.isInParty()) { // パーティー中
				// パーティーのヘイトの合計を算出
				// パーティーメンバー以外にはそのまま配分
				partyHateExp = 0;
				partyHateLawful = 0;
				for (i = hateList.size() - 1; i >= 0; i--) {
					acquisitor = acquisitorList.get(i);
					hate = hateList.get(i);
					if (acquisitor instanceof L1PcInstance) {
						L1PcInstance pc = (L1PcInstance) acquisitor;
						if (pc == l1pcinstance) {
							partyHateExp += hate;
							partyHateLawful += hate;
						}
						else if (l1pcinstance.getParty().isMember(pc)) {
							partyHateExp += hate;
							partyHateLawful += hate;
						}
						else {
							if (totalHateExp > 0) {
								acquire_exp = (exp * hate / totalHateExp);
							}
							if (totalHateLawful > 0) {
								acquire_lawful = (lawful * hate / totalHateLawful);
							}
							AddExp(pc, acquire_exp, acquire_lawful,coinrate);//修正經驗值設定過高會出錯的問題by testt
						}
					}
					else if (acquisitor instanceof L1PetInstance) {
						L1PetInstance pet = (L1PetInstance) acquisitor;
						L1PcInstance master = (L1PcInstance) pet.getMaster();
						if (master == l1pcinstance) {
							partyHateExp += hate;
						}
						else if (l1pcinstance.getParty().isMember(master)) {
							partyHateExp += hate;
						}
						else {
							if (totalHateExp > 0) {
								acquire_exp = (exp * hate / totalHateExp);
							}
							AddExpPet(pet, acquire_exp);
						}
					}
					else if (acquisitor instanceof L1SummonInstance) {
						L1SummonInstance summon = (L1SummonInstance) acquisitor;
						L1PcInstance master = (L1PcInstance) summon.getMaster();
						if (master == l1pcinstance) {
							partyHateExp += hate;
						}
						else if (l1pcinstance.getParty().isMember(master)) {
							partyHateExp += hate;
						}
						else {}
					}
				}
				if (totalHateExp > 0) {
					party_exp = (exp * partyHateExp / totalHateExp);
				}
				if (totalHateLawful > 0) {
					party_lawful = (lawful * partyHateLawful / totalHateLawful);
				}

				// EXP、ロウフル配分

				// プリボーナス
				double pri_bonus = 0;
				L1PcInstance leader = l1pcinstance.getParty().getLeader();
				if (leader.isCrown() && (l1pcinstance.knownsObject(leader) || l1pcinstance.equals(leader))) {
					pri_bonus = 0.059;
				}

				// PT経験値の計算
				L1PcInstance[] ptMembers = l1pcinstance.getParty().getMembers();
				double pt_bonus = 0;
				for (L1PcInstance each : ptMembers) {
					if (l1pcinstance.knownsObject(each) || l1pcinstance.equals(each)) {
						party_level += each.getLevel() * each.getLevel();
					}
					if (l1pcinstance.knownsObject(each)) {
						pt_bonus += 0.04;
					}
				}

				party_exp = (int) (party_exp * (1 + pt_bonus + pri_bonus));

				// 自キャラクターとそのペット・サモンのヘイトの合計を算出
				if (party_level > 0) {
					dist = ((l1pcinstance.getLevel() * l1pcinstance.getLevel()) / party_level);
				}
				member_exp = (int) (party_exp * dist);
				member_lawful = (int) (party_lawful * dist);

				ownHateExp = 0;
				for (i = hateList.size() - 1; i >= 0; i--) {
					acquisitor = acquisitorList.get(i);
					hate = hateList.get(i);
					if (acquisitor instanceof L1PcInstance) {
						L1PcInstance pc = (L1PcInstance) acquisitor;
						if (pc == l1pcinstance) {
							ownHateExp += hate;
						}
					}
					else if (acquisitor instanceof L1PetInstance) {
						L1PetInstance pet = (L1PetInstance) acquisitor;
						L1PcInstance master = (L1PcInstance) pet.getMaster();
						if (master == l1pcinstance) {
							ownHateExp += hate;
						}
					}
					else if (acquisitor instanceof L1SummonInstance) {
						L1SummonInstance summon = (L1SummonInstance) acquisitor;
						L1PcInstance master = (L1PcInstance) summon.getMaster();
						if (master == l1pcinstance) {
							ownHateExp += hate;
						}
					}
				}
				// 自キャラクターとそのペット・サモンに分配
				if (ownHateExp != 0) { // 攻撃に参加していた
					for (i = hateList.size() - 1; i >= 0; i--) {
						acquisitor = acquisitorList.get(i);
						hate = hateList.get(i);
						if (acquisitor instanceof L1PcInstance) {
							L1PcInstance pc = (L1PcInstance) acquisitor;
							if (pc == l1pcinstance) {
								if (ownHateExp > 0) {
									acquire_exp = (member_exp * hate / ownHateExp);
								}
								AddExp(pc, acquire_exp, member_lawful,coinrate);//修正經驗值設定過高會出錯的問題by testt
							}
						}
						else if (acquisitor instanceof L1PetInstance) {
							L1PetInstance pet = (L1PetInstance) acquisitor;
							L1PcInstance master = (L1PcInstance) pet.getMaster();
							if (master == l1pcinstance) {
								if (ownHateExp > 0) {
									acquire_exp = (member_exp * hate / ownHateExp);
								}
								AddExpPet(pet, acquire_exp);
							}
						}
						else if (acquisitor instanceof L1SummonInstance) {}
					}
				}
				else { // 攻撃に参加していなかった
						// 自キャラクターのみに分配
					AddExp(l1pcinstance, member_exp, member_lawful,coinrate);//修正經驗值設定過高會出錯的問題by testt
				}

				// パーティーメンバーとそのペット・サモンのヘイトの合計を算出
				for (L1PcInstance ptMember : ptMembers) {
					if (l1pcinstance.knownsObject(ptMember)) {
						if (party_level > 0) {
							dist = ((ptMember.getLevel() * ptMember.getLevel()) / party_level);
						}
						member_exp = (int) (party_exp * dist);
						member_lawful = (int) (party_lawful * dist);

						ownHateExp = 0;
						for (i = hateList.size() - 1; i >= 0; i--) {
							acquisitor = acquisitorList.get(i);
							hate = hateList.get(i);
							if (acquisitor instanceof L1PcInstance) {
								L1PcInstance pc = (L1PcInstance) acquisitor;
								if (pc == ptMember) {
									ownHateExp += hate;
								}
							}
							else if (acquisitor instanceof L1PetInstance) {
								L1PetInstance pet = (L1PetInstance) acquisitor;
								L1PcInstance master = (L1PcInstance) pet.getMaster();
								if (master == ptMember) {
									ownHateExp += hate;
								}
							}
							else if (acquisitor instanceof L1SummonInstance) {
								L1SummonInstance summon = (L1SummonInstance) acquisitor;
								L1PcInstance master = (L1PcInstance) summon.getMaster();
								if (master == ptMember) {
									ownHateExp += hate;
								}
							}
						}
						// パーティーメンバーとそのペット・サモンに分配
						if (ownHateExp != 0) { // 攻撃に参加していた
							for (i = hateList.size() - 1; i >= 0; i--) {
								acquisitor = acquisitorList.get(i);
								hate = hateList.get(i);
								if (acquisitor instanceof L1PcInstance) {
									L1PcInstance pc = (L1PcInstance) acquisitor;
									if (pc == ptMember) {
										if (ownHateExp > 0) {
											acquire_exp = (member_exp * hate / ownHateExp);
										}
										AddExp(pc, acquire_exp, member_lawful,coinrate);//修正經驗值設定過高會出錯的問題by testt
									}
								}
								else if (acquisitor instanceof L1PetInstance) {
									L1PetInstance pet = (L1PetInstance) acquisitor;
									L1PcInstance master = (L1PcInstance) pet.getMaster();
									if (master == ptMember) {
										if (ownHateExp > 0) {
											acquire_exp = (member_exp * hate / ownHateExp);
										}
										AddExpPet(pet, acquire_exp);
									}
								}
								else if (acquisitor instanceof L1SummonInstance) {}
							}
						}
						else { // 攻撃に参加していなかった
								// パーティーメンバーのみに分配
							AddExp(ptMember, member_exp, member_lawful,coinrate);//修正經驗值設定過高會出錯的問題by testt
						}
					}
				}
			}
			else { // パーティーを組んでいない
					// EXP、ロウフルの分配
				for (i = hateList.size() - 1; i >= 0; i--) {
					acquisitor = acquisitorList.get(i);
					hate = hateList.get(i);
					acquire_exp = (exp * hate / totalHateExp);
					if (acquisitor instanceof L1PcInstance) {
						if (totalHateLawful > 0) {
							acquire_lawful = (lawful * hate / totalHateLawful);
						}
					}

					if (acquisitor instanceof L1PcInstance) {
						L1PcInstance pc = (L1PcInstance) acquisitor;
						AddExp(pc, acquire_exp, acquire_lawful,coinrate);//修正經驗值設定過高會出錯的問題by testt
					}
					else if (acquisitor instanceof L1PetInstance) {
						L1PetInstance pet = (L1PetInstance) acquisitor;
						AddExpPet(pet, acquire_exp);
					}
					else if (acquisitor instanceof L1SummonInstance) {}
				}
			}
		}
	}

	private static void AddExp(L1PcInstance pc, int exp, int lawful,int coinrate) {

		int add_lawful = (int) (lawful * Config.RATE_LA) * -1;
		pc.addLawful(add_lawful);

		int _nLevel_t = pc.getMaxLvl(); // 經驗值回饋獎勵系統
		short _nLevel_max = (short) L1World.getInstance().getMaxLevel();
		short gaplvl = (short)(pc.getLevel() - pc.getHighLevel());
		double exppenalty = ExpTable.getPenaltyRate(pc.getLevel());
		double foodBonus = 1.0;
		double expBonus = 1.0;
		double LevelBonus = 1.0; // 經驗值回饋獎勵系統

		// 魔法料理經驗加成
		if (pc.hasSkillEffect(COOKING_1_7_N) || pc.hasSkillEffect(COOKING_1_7_S)) {
			foodBonus = 1.1;// fix 增加經驗調整 by testt
		}
		if (pc.hasSkillEffect(COOKING_2_7_N) || pc.hasSkillEffect(COOKING_2_7_S)) {
			foodBonus = 1.2;
		}
		if (pc.hasSkillEffect(COOKING_3_7_N) || pc.hasSkillEffect(COOKING_3_7_S)) {
			foodBonus = 1.3;
		}
		// 戰鬥藥水、神力藥水經驗加成
		if (pc.hasSkillEffect(EFFECT_POTION_OF_BATTLE)) {
			expBonus = 1.2;
		} else if (pc.hasSkillEffect(EFFECT_POTION_OF_EXP_150)) {
			expBonus = 1.5;
		} else if (pc.hasSkillEffect(EFFECT_POTION_OF_EXP_175)) {
			expBonus = 1.75;
		} else if (pc.hasSkillEffect(EFFECT_POTION_OF_EXP_200)) {
			expBonus = 2.0;
		} else if (pc.hasSkillEffect(EFFECT_POTION_OF_EXP_225)) {
			expBonus = 2.25;
		} else if (pc.hasSkillEffect(EFFECT_POTION_OF_EXP_250)) {
			expBonus = 2.5;
		}
		// 經驗值回饋獎勵系統
		//if(Config.LEVEL_BONUS){
		//	if (_nLevel_t >= 49 && _nLevel_t <= 63) {
		//		LevelBonus = 1.64 - (_nLevel_t / 100D);
		//	} else if (_nLevel_t == 64) {
		//		LevelBonus = 1.01;
		//	}
		//}
		// end
		
		// 經驗值回饋獎勵系統
		//NumberFormat nf = NumberFormat.getInstance();
        //nf.setMaximumFractionDigits( 0 );    //無小數點
		
		if(Config.LEVEL_BONUS){
			if (pc.getLevel() >= 49 && _nLevel_t < _nLevel_max) {
				LevelBonus = 1.2 + (_nLevel_max / 5D) - ((_nLevel_t + gaplvl) / 5D);
				if (LevelBonus > 5)// 最大增加500%經驗
					LevelBonus = 5;
				if (pc.isShowMsg())
					pc.sendPackets(new S_ServerMessage(166,"因為與伺服器最大角色等級差距的關係，額外取得了 " + (short) ((LevelBonus -1) * 100) +"% 經驗值"));
			} else if (_nLevel_t == _nLevel_max) {
				LevelBonus = 1.0;
			}
		}
		// end

		// 追憶之島原始經驗改為取得金碧 by testt
		if (pc.getMapId() == 701) {			
			L1ItemInstance item = ItemTable.getInstance().createItem(
					40308);
			
			item.setCount(exp*coinrate);
			if (item != null) {
				if (pc.getInventory().checkAddItem(item, exp*coinrate) == L1Inventory.OK) {
					pc.getInventory().storeItem(item);
				} else { // 持てない場合は地面に落とす 處理のキャンセルはしない（不正防止）
					L1World.getInstance()
					.getInventory(pc.getX(), pc.getY(),
							pc.getMapId()).storeItem(item);
				}
				pc.sendPackets(new S_ServerMessage(403, item
						.getLogName())); // 得到多少金幣
			}
		}
		else {
			// 殷海薩加成條件
			double _nExpRate = exppenalty * foodBonus * expBonus * LevelBonus * L1MagicDoll.getDoubleExpByDoll(pc); // 經驗加倍魔法娃娃

			if (pc.isEinLevel() && pc.getEinPoint() != 0) {
				_nExpRate *= 1.77;
				pc.CalcExpCostEin((int) (exp * _nExpRate));
			}		
			int add_exp = (int) (exp * _nExpRate * Config.RATE_XP);
			// end
			pc.addExp(add_exp);
			// 大小GM顯示取得總經驗值
			if (pc.getAccessLevel()== 200 || pc.getAccessLevel()== 100) {
				pc.sendPackets(new S_ServerMessage(166,"總共取得 " + add_exp +" 經驗值")); //\f1%0%s %4%1%3 %2。
			}
			// end
		}
		// 追憶之島原始經驗改為取得金碧 end by testt

	}

	private static void AddExpPet(L1PetInstance pet, int exp) {
		L1PcInstance pc = (L1PcInstance) pet.getMaster();

		int petItemObjId = pet.getItemObjId();

		int levelBefore = pet.getLevel();
		// 寵物裝備經驗加倍功能
		double exp_w = 1.0;
		double exp_a = 1.0;
		
		if(pet.getExpRateByWeapon() > 100){
			exp_w = ((double)pet.getExpRateByWeapon() / 100);
		}
		if(pet.getExpRateByArmor() > 100){
			exp_a = ((double)pet.getExpRateByArmor() / 100);
		}
		double exp_wa = exp_w * exp_a;
		// end
		int totalExp = (int) (exp * exp_wa * Config.RATE_XP_PET + pet.getExp()); // 寵物經驗倍率
		if (totalExp >= ExpTable.getExpByLevel(Config.PET_MAX_LV + 1)) { // 寵物最高等級
			totalExp = ExpTable.getExpByLevel(Config.PET_MAX_LV + 1) - 1; // 寵物最高等級
		}
		pet.setExp(totalExp);

		pet.setLevel(ExpTable.getLevelByExp(totalExp));

		int expPercentage = ExpTable.getExpPercentage(pet.getLevel(), totalExp);

		int gap = pet.getLevel() - levelBefore;
		for (int i = 1; i <= gap; i++) {
			IntRange hpUpRange = pet.getPetType().getHpUpRange();
			IntRange mpUpRange = pet.getPetType().getMpUpRange();
			pet.addMaxHp(hpUpRange.randomValue());
			pet.addMaxMp(mpUpRange.randomValue());
		}

		pet.setExpPercent(expPercentage);
		pc.sendPackets(new S_PetPack(pet, pc));

		if (gap != 0) { // レベルアップしたらDBに書き込む
			L1Pet petTemplate = PetTable.getInstance().getTemplate(petItemObjId);
			if (petTemplate == null) { // PetTableにない
				_log.warning("L1Pet == null");
				return;
			}
			petTemplate.set_exp(pet.getExp());
			petTemplate.set_level(pet.getLevel());
			petTemplate.set_hp(pet.getMaxHp());
			petTemplate.set_mp(pet.getMaxMp());
			PetTable.getInstance().storePet(petTemplate); // DBに書き込み
			pc.sendPackets(new S_ServerMessage(320, pet.getName())); // \f1%0のレベルが上がりました。
		}
	}
}