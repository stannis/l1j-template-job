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

import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;

import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_SkillSound; //TODO HPR効果
import l1j.server.server.serverpackets.S_SystemMessage;
import l1j.server.server.templates.L1MagicDoll;

public class HpRegenerationByDoll extends TimerTask {
	private static Logger _log = Logger.getLogger(HpRegenerationByDoll.class
			.getName());

	private final L1PcInstance _pc;

	public HpRegenerationByDoll(L1PcInstance pc) {
		_pc = pc;
	}

	@Override
	public void run() {
		try {
			if (_pc.isDead()) {
				return;
			}
			regenHp();
		} catch (Throwable e) {
			_log.log(Level.WARNING, e.getLocalizedMessage(), e);
		}
	}

	public void regenHp() {
		int newHp = _pc.getCurrentHp() + L1MagicDoll.getHpByDoll(_pc);
		if (newHp < 0) {
			newHp = 0;
			return;
		}
		//_pc.sendPackets(new S_SkillSound(_pc.getId(), 744));
		//_pc.broadcastPacket(new S_SkillSound(_pc.getId(), 744));
		//_pc.setCurrentHp(newHp);
		
		// 增加扣金幣判斷 by testt
		if (_pc.getInventory().checkItem(40308, L1MagicDoll.getHpByDoll(_pc) * 20)) {
			_pc.getInventory().consumeItem(40308, L1MagicDoll.getHpByDoll(_pc) * 20);
			_pc.sendPackets(new S_SkillSound(_pc.getId(), 744));
			_pc.broadcastPacket(new S_SkillSound(_pc.getId(), 744));
			_pc.setCurrentHp(newHp);
		} else {
			_pc.sendPackets(new S_SystemMessage("金幣不足，停止娃娃回血功能"));
			_pc.stopHpRegenerationByDoll();
		}
		// END
	}
}