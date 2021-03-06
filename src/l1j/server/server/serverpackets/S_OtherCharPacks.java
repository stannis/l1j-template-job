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
package l1j.server.server.serverpackets;

import static l1j.server.server.model.skill.L1SkillId.STATUS_THIRD_SPEED;

import l1j.server.Config; // sosodemon add 聲望系統 BY.SosoDEmoN
import l1j.server.server.Opcodes;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.william.L1WilliamSystemMessage; // sosodemon add 聲望系統 BY.SosoDEmoN

// Referenced classes of package l1j.server.server.serverpackets:
// ServerBasePacket, S_OtherCharPacks

public class S_OtherCharPacks extends ServerBasePacket {

	private static final String S_OTHER_CHAR_PACKS = "[S] S_OtherCharPacks";

	private static final int STATUS_POISON = 1;

	private static final int STATUS_INVISIBLE = 2;

	private static final int STATUS_PC = 4;

	private byte[] _byte = null;


	public S_OtherCharPacks(L1PcInstance pc, boolean isFindInvis, int clanid) {
		buildPacket(pc, isFindInvis, clanid);
	}
	
	public S_OtherCharPacks(L1PcInstance pc, boolean isFindInvis) {
		buildPacket(pc, isFindInvis, 0);
	}

	public S_OtherCharPacks(L1PcInstance pc) {
		buildPacket(pc, false, 0);
	}

	private void buildPacket(L1PcInstance pc, boolean isFindInvis, int clanid) {
		int status = STATUS_PC;

		//在戰場地圖中所有不同血盟的人都將成為敵人 by testt
		switch (pc.getMapId()) {
		case 725:
		case 726:
			if ((pc.getClanid() != clanid) || pc.getClanid()==0) {
				status = 0;
			}
			break;
		default:
			if (pc.getMapId() >= 5153 && pc.getMapId() <= 5164){
				status = 0;
			}
			break;
		}
		//在戰場地圖中所有不同血盟的人都將成為敵人 end by testt
		
		// sosodemon add 聲望系統 BY.SosoDEmoN
		String FameName = "";
		if (pc.getFamePoint() >= 0 && pc.getFamePoint() < Config.FAME_LV1_POINT) {
			FameName = L1WilliamSystemMessage.ShowMessage(100); //Lv1 0~9
		} else if (pc.getFamePoint() >= Config.FAME_LV1_POINT && pc.getFamePoint() < Config.FAME_LV2_POINT) {
			FameName = L1WilliamSystemMessage.ShowMessage(101); //Lv2 10~29
		} else if (pc.getFamePoint() >= Config.FAME_LV2_POINT && pc.getFamePoint() < Config.FAME_LV3_POINT) {
			FameName = L1WilliamSystemMessage.ShowMessage(102); //Lv3 30~49
		} else if (pc.getFamePoint() >= Config.FAME_LV3_POINT && pc.getFamePoint() < Config.FAME_LV4_POINT) {
			FameName = L1WilliamSystemMessage.ShowMessage(103); //Lv4 50~69
		} else if (pc.getFamePoint() >= Config.FAME_LV4_POINT && pc.getFamePoint() < Config.FAME_LV5_POINT) {
			FameName = L1WilliamSystemMessage.ShowMessage(104); //Lv5 70~89
		} else if (pc.getFamePoint() >= Config.FAME_LV5_POINT && pc.getFamePoint() < Config.FAME_LV6_POINT) {
			FameName = L1WilliamSystemMessage.ShowMessage(105); //Lv6 90~109
		} else if (pc.getFamePoint() >= Config.FAME_LV6_POINT && pc.getFamePoint() < Config.FAME_LV7_POINT) {
			FameName = L1WilliamSystemMessage.ShowMessage(106); //Lv7 110~129
		} else if (pc.getFamePoint() >= Config.FAME_LV7_POINT && pc.getFamePoint() < Config.FAME_LV8_POINT) {
			FameName = L1WilliamSystemMessage.ShowMessage(107); //Lv8 130~149
		} else if (pc.getFamePoint() >= Config.FAME_LV8_POINT && pc.getFamePoint() < Config.FAME_LV9_POINT) {
			FameName = L1WilliamSystemMessage.ShowMessage(108); //Lv9 150~169
		} else if (pc.getFamePoint() >= Config.FAME_LV9_POINT && pc.getFamePoint() < Config.FAME_LV10_POINT) {
			FameName = L1WilliamSystemMessage.ShowMessage(109); //Lv10 170~189
		} else if (pc.getFamePoint() >= Config.FAME_LV10_POINT && pc.getFamePoint() < Config.FAME_LV11_POINT) {
			FameName = L1WilliamSystemMessage.ShowMessage(110); //Lv11 190~199
		} else if (pc.getFamePoint() >= Config.FAME_LV11_POINT) {
			FameName = L1WilliamSystemMessage.ShowMessage(111); //Lv12 200以上
		}
		// sosodemon end 聲望系統 BY.SosoDEmoN
		
		if (pc.getPoison() != null) { // 毒状態
			if (pc.getPoison().getEffectId() == 1) {
				status |= STATUS_POISON;
			}
		}
		if (pc.isInvisble() && !isFindInvis) {
			status |= STATUS_INVISIBLE;
		}
		if (pc.getBraveSpeed() != 0) { // 2段加速效果
			status |= pc.getBraveSpeed() * 16;
		}

		// int addbyte = 0;
		// int addbyte1 = 1;

		writeC(Opcodes.S_OPCODE_CHARPACK);
		writeH(pc.getX());
		writeH(pc.getY());
		writeD(pc.getId());
		if (pc.isDead()) {
			writeH(pc.getTempCharGfxAtDead());
		}
		else {
			//在盟戰地圖中所有不同血盟的人都將成為敵人 by testt
			int polyId = 0;
			switch (pc.getMapId()) {
			case 725:
			case 726:
				if ((pc.get_sex() == 0) && pc.isCrown()) { // 夏納的變身卷軸(等級70)
					polyId = 6882;
				} else if ((pc.get_sex() == 1) && pc.isCrown()) {
					polyId = 6883;
				} else if ((pc.get_sex() == 0) && pc.isKnight()) {
					polyId = 6884;
				} else if ((pc.get_sex() == 1) && pc.isKnight()) {
					polyId = 6885;
				} else if ((pc.get_sex() == 0) && pc.isElf()) {
					polyId = 6886;
				} else if ((pc.get_sex() == 1) && pc.isElf()) {
					polyId = 6887;
				} else if ((pc.get_sex() == 0) && pc.isWizard()) {
					polyId = 6888;
				} else if ((pc.get_sex() == 1) && pc.isWizard()) {
					polyId = 6889;
				} else if ((pc.get_sex() == 0) && pc.isDarkelf()) {
					polyId = 6890;
				} else if ((pc.get_sex() == 1) && pc.isDarkelf()) {
					polyId = 6891;
				} else if ((pc.get_sex() == 0)
						&& pc.isDragonKnight()) {
					polyId = 7163;
				} else if ((pc.get_sex() == 1)
						&& pc.isDragonKnight()) {
					polyId = 7164;
				} else if ((pc.get_sex() == 0)
						&& pc.isIllusionist()) {
					polyId = 7165;
				} else if ((pc.get_sex() == 1)
						&& pc.isIllusionist()) {
					polyId = 7166;
				}
				writeH(polyId);//經過測試需要變身方可直接攻擊				
				break;
			default:
				if (pc.getMapId() >= 5153 && pc.getMapId() <= 5164){
					if ((pc.get_sex() == 0) && pc.isCrown()) { // 夏納的變身卷軸(等級70)
						polyId = 6882;
					} else if ((pc.get_sex() == 1) && pc.isCrown()) {
						polyId = 6883;
					} else if ((pc.get_sex() == 0) && pc.isKnight()) {
						polyId = 6884;
					} else if ((pc.get_sex() == 1) && pc.isKnight()) {
						polyId = 6885;
					} else if ((pc.get_sex() == 0) && pc.isElf()) {
						polyId = 6886;
					} else if ((pc.get_sex() == 1) && pc.isElf()) {
						polyId = 6887;
					} else if ((pc.get_sex() == 0) && pc.isWizard()) {
						polyId = 6888;
					} else if ((pc.get_sex() == 1) && pc.isWizard()) {
						polyId = 6889;
					} else if ((pc.get_sex() == 0) && pc.isDarkelf()) {
						polyId = 6890;
					} else if ((pc.get_sex() == 1) && pc.isDarkelf()) {
						polyId = 6891;
					} else if ((pc.get_sex() == 0)
							&& pc.isDragonKnight()) {
						polyId = 7163;
					} else if ((pc.get_sex() == 1)
							&& pc.isDragonKnight()) {
						polyId = 7164;
					} else if ((pc.get_sex() == 0)
							&& pc.isIllusionist()) {
						polyId = 7165;
					} else if ((pc.get_sex() == 1)
							&& pc.isIllusionist()) {
						polyId = 7166;
					}
					writeH(polyId);//經過測試需要變身方可直接攻擊	
				}
				else{
					writeH(pc.getTempCharGfx());
				}
				break;
			}
			//在戰場地圖中所有不同血盟的人都將成為敵人 end by testt
			
			//*writeH(pc.getTempCharGfx());*//原始程式碼
		}
		if (pc.isDead()) {
			writeC(pc.getStatus());
		}
		else {
			writeC(pc.getCurrentWeapon());
		}
		writeC(pc.getHeading());
		// writeC(0); // makes char invis (0x01), cannot move. spells display
		writeC(pc.getChaLightSize());
		writeC(pc.getMoveSpeed());
		writeD(0x0000); // exp
		// writeC(0x00);
		writeH(pc.getLawful());
		// sosodemon add 聲望系統 BY.SosoDEmoN
		if(Config.FAME_IS_ACTIVE){
			writeS(pc.getName() + FameName);
		} else {
			writeS(pc.getName());
		}
		// sosodemon end 聲望系統 BY.SosoDEmoN
		//writeS(pc.getName());
		writeS(pc.getTitle());
		writeC(status);
		writeD(pc.getClanid());
		writeS(pc.getClanname()); // クラン名
		writeS(null); // ペッホチング？
		writeC(0); // ？


		/*
		 * if(pc.is_isInParty()) // パーティー中 { writeC(100 * pc.get_currentHp() /
		 * pc.get_maxHp()); } else { writeC(0xFF); }
		 */

		writeC(0xFF);
		if (pc.hasSkillEffect(STATUS_THIRD_SPEED)) {
			writeC(0x08); // 3段加速
		} else {
			writeC(0);
		}

		writeC(pc.getLevel()); // PC = 0, Mon = Lv

		writeC(0); // ？
		writeC(0xFF);
		writeC(0xFF);
	}

	@Override
	public byte[] getContent() {
		if (_byte == null) {
			_byte = getBytes();
		}
		return _byte;
	}

	@Override
	public String getType() {
		return S_OTHER_CHAR_PACKS;
	}

}