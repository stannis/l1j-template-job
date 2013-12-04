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
package l1j.server.server.command.executor;

import java.util.StringTokenizer;

import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_ServerMessage;
import l1j.server.server.serverpackets.S_SystemMessage;

/**
 * GM指令：給對象增加GOLD幣並更新贊助狀態 by testt
 */
public class L1AddSupport implements L1CommandExecutor {
	private L1AddSupport () {
	}

	public static L1CommandExecutor getInstance() {
		return new L1AddSupport ();
	}

	public void execute(L1PcInstance pc, String cmdName, String arg) {
		try {
			StringTokenizer st = new StringTokenizer(arg);
			String name = st.nextToken();
			int count = 0;
			int bonus = 0;
			int a = pc.getRewardStep();
			int b = 0;
			if (st.hasMoreTokens()) {
				count = Integer.parseInt(st.nextToken());
			}
			L1PcInstance target = L1World.getInstance().getPlayer(name);

			if (target == null) {
				//pc.sendPackets(new S_ServerMessage(73, name)); // \f1%0はゲームをしていません。
				//測試離線更新角色贊助狀態
				target = L1PcInstance.load(name);
				
				//return;
			}
			if (count == 0) {
				pc.sendPackets(new S_SystemMessage("請輸入 .addsupport 玩家名稱 [贊助的金額]"));
				return;
			} else {
				bonus = count;
				if (count >= 1599 && count < 2599) { 
					b = 1;
					if ((a & b) != 1 && (a & 2) != 2 && (a & 4) != 4) {						
						if (target.getRewardStep() == 0) {
							target.setRewardStep(target.getRewardStep() + 1);
							bonus += 1599 * 10 / 100;
						} else {
							target.setRewardStep(target.getRewardStep() + 1);
						}
					}
				} else if (count >= 2599 && count < 5599) {
					b = 2;
					if ((a & b) != 2 && (a & 4) != 4) {
						if (target.getRewardStep() == 0) {
							target.setRewardStep(target.getRewardStep() + 2);
							bonus += 2599 * 15 / 100;
						}else {
							target.setRewardStep(target.getRewardStep() + 2);
							bonus += 2599 * 5 / 100;
						}
					}
				} else if (count >= 5599) {
					b = 4;
					if ((a & b) != 4) {
						if (target.getRewardStep() == 0) {
							target.setRewardStep(target.getRewardStep() + 4);
							bonus += 5599 * 20 / 100;
						}else {
							target.setRewardStep(target.getRewardStep() + 4);
							bonus += count * 10 / 100;
						}
					}
				}
				
			}
			

			
			pc.sendPackets(new S_ServerMessage(166, "嘗試為" + name + "增加GOLD幣"));
			pc.sendPackets(new S_ServerMessage(166, "增加前的未領取GOLD幣為" + target.getRemainCoin()));
			pc.sendPackets(new S_ServerMessage(166, "增加前的總贊助金額為" + target.getTotalSupport()));
			pc.sendPackets(new S_ServerMessage(166, "增加的GOLD幣(加上獎勵)為" + bonus));
			target.isSpecialLogin(true);
			target.setRemainCoin(bonus+target.getRemainCoin());
			pc.sendPackets(new S_ServerMessage(166, "增加後的未領取GOLD幣為" + target.getRemainCoin()));
			if (pc.isSupport()) {
				target.setTotalSupport(pc.getTotalSupport() + count);
			}else {
				target.setTotalSupport(count);
				target.isSupport(true);
			}
			if (target.getTotalSupport() >= 10000 && target.getRewardStep() < 8) {
				target.setRewardStep(target.getRewardStep() + 8);
			}
			if (target.getTotalSupport() >= 20000 && target.getRewardStep() < 16) {
				target.setRewardStep(target.getRewardStep() + 16);
			}
			if (target.getTotalSupport() >= 50000 && target.getRewardStep() < 32) {
				target.setRewardStep(target.getRewardStep() + 32);
			}
			pc.sendPackets(new S_ServerMessage(166, "增加後的總贊助金額為" + target.getTotalSupport()));
			target.updateSupportState();
			target.isSpecialLogin(false);
		}
		catch (Exception e) {
			pc.sendPackets(new S_SystemMessage(("請輸入 .addsupport 玩家名稱 [贊助的金額]")));
		}
	}

}