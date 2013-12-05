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

import l1j.server.server.datatables.MagicDollTable;
import l1j.server.server.model.L1Character;
import l1j.server.server.model.Instance.L1DollInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_SystemMessage;
import l1j.server.server.templates.L1MagicDoll;

/**
 * GM指令：讓玩家自行開啟娃娃的補血功能，並且設定回血量及回血秒數 by testt
 */
public class L1DollHpr implements L1CommandExecutor {
	private L1DollHpr () {
	}

	public static L1CommandExecutor getInstance() {
		return new L1DollHpr ();
	}

	public void execute(L1PcInstance pc, String cmdName, String arg) {
		try {
			if (arg.isEmpty())
				arg = "";
			StringTokenizer st = new StringTokenizer(arg);
			String parm = "";
			if (!arg.isEmpty())
				parm = st.nextToken();
			int hpr = 100;
			int period = 5;

			//先判斷是否為start或stop參數
			if (parm.equals("start")) {
				pc.startHpRegenerationByDoll();
				pc.sendPackets(new S_SystemMessage("開始娃娃回血功能"));
				return;
			} else if (parm.equals("stop")) {
				pc.stopHpRegenerationByDoll();
				pc.sendPackets(new S_SystemMessage("暫時停止娃娃回血功能"));
				return;
			}
			
			//若都不是就再次初始化字串重新判斷輸入參數
			st = new StringTokenizer(arg);
			
			if (st.hasMoreTokens()) {
				hpr = Integer.parseInt(st.nextToken());
			} else {
				pc.sendPackets(new S_SystemMessage("未設定回血量及回血週期將\n以預設值回血量100\n週期5秒執行指令"));
				pc.setIntervalByDoll(period);
				L1Character _master = null;
				if (pc instanceof L1Character) {
					pc.sendPackets(new S_SystemMessage("目前娃娃回血設定：\n回血量:" + hpr + "\n週期：" + period));
					_master = (L1Character) pc;
					setDollHpr(_master, hpr);
					pc.stopHpRegenerationByDoll();
					pc.startHpRegenerationByDoll();
				}
				return;
			}

			if (st.hasMoreTokens()) {
				period = Integer.parseInt(st.nextToken());
			} else {
				pc.sendPackets(new S_SystemMessage("未設定回血週期，將以預設值5秒執行指令"));
			}

			if (hpr > 100 || hpr < 1) {
				pc.sendPackets(new S_SystemMessage("請輸入 .dollhpr 回血量[最大100,最小1] 回血週期[最小5秒,最大64秒]"));
				pc.sendPackets(new S_SystemMessage("或者輸入 .dollhpr stop | start 來停止或開始娃娃回血"));
				pc.sendPackets(new S_SystemMessage("注意:每次的補血都會消耗補血量20倍的金幣\n請玩家斟酌使用"));
				return;
			}
			if ( period < 5 || period > 64) {
				pc.sendPackets(new S_SystemMessage("請輸入 .dollhpr 回血量[最大100,最小1] 回血週期[最小5秒,最大64秒]"));
				pc.sendPackets(new S_SystemMessage("或者輸入 .dollhpr stop | start 來停止或開始娃娃回血"));
				pc.sendPackets(new S_SystemMessage("注意:每次的補血都會消耗補血量20倍的金幣\n請玩家斟酌使用"));
				return;
			}
			pc.setIntervalByDoll(period);
			L1Character _master = null;
			if (pc instanceof L1Character) {
				pc.sendPackets(new S_SystemMessage("目前娃娃回血設定：\n回血量:" + hpr + "\n週期：" + period));
				_master = (L1Character) pc;
				setDollHpr(_master, hpr);
				pc.stopHpRegenerationByDoll();
				pc.startHpRegenerationByDoll();
			}
			
		}
		catch (Exception e) {
			pc.sendPackets(new S_SystemMessage("請輸入 .dollhpr 回血量[最大100,最小1] 回血週期[最小5秒,最大64秒]"));
			pc.sendPackets(new S_SystemMessage("或者輸入 .dollhpr stop | start 來停止或開始娃娃回血"));
			pc.sendPackets(new S_SystemMessage("注意:\n每次的補血都會消耗補血量20倍的金幣\n請玩家斟酌使用"));
		}
	}
	
	private void setDollHpr(L1Character _master, int hpr) { // 體力回覆量 (時間固定性)

		for ( L1DollInstance dollIns : _master.getDollList().values()) {
			L1MagicDoll doll = MagicDollTable.getInstance().getTemplate(dollIns.getItemId());
			if (doll != null) {
				if (doll.getHprTime() && doll.getHpr() != 0) {
					doll.setHpr(hpr);
				}
			}
		}

	}

}