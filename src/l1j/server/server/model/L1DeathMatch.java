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

import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;

import l1j.server.server.datatables.ItemTable;
import l1j.server.server.datatables.NpcTable;
import l1j.server.server.datatables.PetTable;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.Instance.L1PetInstance;
import l1j.server.server.serverpackets.S_CharVisualUpdate;
import l1j.server.server.serverpackets.S_Resurrection;
import l1j.server.server.serverpackets.S_ServerMessage;
import l1j.server.server.serverpackets.S_SkillSound;
import l1j.server.server.templates.L1Npc;
import l1j.server.server.templates.L1Pet;
import l1j.server.server.utils.Random;

public class L1DeathMatch {
	public static final int STATUS_NONE = 0;

	public static final int STATUS_READY1 = 1;

	public static final int STATUS_READY2 = 2;

	public static final int STATUS_PLAYING = 3;

	public static final int MAX_DEATH_MATCH = 12;

	private static final short[] DEATH_MATCH_MAPID =
	{ 5153, 5154, 5155, 5156, 5157, 5158, 5159, 5160, 5161, 5162, 5163, 5164 };

	private String[] _pc1Name = new String[MAX_DEATH_MATCH];

	private String[] _pc2Name = new String[MAX_DEATH_MATCH];

	private L1PcInstance[] _pc1 = new L1PcInstance[MAX_DEATH_MATCH];

	private L1PcInstance[] _pc2 = new L1PcInstance[MAX_DEATH_MATCH];

	private static L1DeathMatch _instance;

	public static L1DeathMatch getInstance() {
		if (_instance == null) {
			_instance = new L1DeathMatch();
		}
		return _instance;
	}

	public int setDeathMatchPc(int deathMatchNo, L1PcInstance pc) {
		int status = getDeathMatchStatus(deathMatchNo);
		if (status == STATUS_NONE) {
			_pc1Name[deathMatchNo] = pc.getName();
			_pc1[deathMatchNo] = pc;
			return STATUS_READY1;
		}
		else if (status == STATUS_READY1) {
			_pc2Name[deathMatchNo] = pc.getName();
			_pc2[deathMatchNo] = pc;
			return STATUS_PLAYING;
		}
		else if (status == STATUS_READY2) {
			_pc1Name[deathMatchNo] = pc.getName();
			_pc1[deathMatchNo] = pc;
			return STATUS_PLAYING;
		}
		return STATUS_NONE;
	}

	private synchronized int getDeathMatchStatus(int deathMatchNo) {
		L1PcInstance pc1 = null;
		if (_pc1Name[deathMatchNo] != null) {
			pc1 = L1World.getInstance().getPlayer(_pc1Name[deathMatchNo]);
		}
		L1PcInstance pc2 = null;
		if (_pc2Name[deathMatchNo] != null) {
			pc2 = L1World.getInstance().getPlayer(_pc2Name[deathMatchNo]);
		}

		if ((pc1 == null) && (pc2 == null)) {
			return STATUS_NONE;
		}
		if ((pc1 == null) && (pc2 != null)) {
			if (pc2.getMapId() == DEATH_MATCH_MAPID[deathMatchNo]) {
				return STATUS_READY2;
			}
			else {
				_pc2Name[deathMatchNo] = null;
				_pc2[deathMatchNo] = null;
				return STATUS_NONE;
			}
		}
		if ((pc1 != null) && (pc2 == null)) {
			if (pc1.getMapId() == DEATH_MATCH_MAPID[deathMatchNo]) {
				return STATUS_READY1;
			}
			else {
				_pc1Name[deathMatchNo] = null;
				_pc1[deathMatchNo] = null;
				return STATUS_NONE;
			}
		}

		// PCが試合場に2人いる場合
		if ((pc1.getMapId() == DEATH_MATCH_MAPID[deathMatchNo]) && (pc2.getMapId() == DEATH_MATCH_MAPID[deathMatchNo])) {
			return STATUS_PLAYING;
		}

		// PCが試合場に1人いる場合
		if (pc1.getMapId() == DEATH_MATCH_MAPID[deathMatchNo]) {
			_pc2Name[deathMatchNo] = null;
			_pc2[deathMatchNo] = null;
			return STATUS_READY1;
		}
		if (pc2.getMapId() == DEATH_MATCH_MAPID[deathMatchNo]) {
			_pc1Name[deathMatchNo] = null;
			_pc1[deathMatchNo] = null;
			return STATUS_READY2;
		}
		return STATUS_NONE;
	}

	private int decideDeathMatchNo() {
		// 相手が待機中の試合を探す
		for (int i = 0; i < MAX_DEATH_MATCH; i++) {
			int status = getDeathMatchStatus(i);
			if ((status == STATUS_READY1) || (status == STATUS_READY2)) {
				return i;
			}
		}
		// 待機中の試合がなければ空いている試合を探す
		for (int i = 0; i < MAX_DEATH_MATCH; i++) {
			int status = getDeathMatchStatus(i);
			if (status == STATUS_NONE) {
				return i;
			}
		}
		return -1;
	}

	public synchronized boolean enterDeathMatch(L1PcInstance pc) {
		int deathMatchNo = decideDeathMatchNo();
		if (deathMatchNo == -1) {
			return false;
		}

		//L1PcInstance pc = withdrawPc(pc, amuletId);
		L1Teleport.teleport(pc, 32657, 32898, DEATH_MATCH_MAPID[deathMatchNo], 0, true);

		L1DeathMatchReadyTimer timer = new L1DeathMatchReadyTimer(deathMatchNo, pc);
		timer.begin();
		return true;
	}

	//private L1PcInstance withdrawPc(L1PcInstance pc, int amuletId) {
	//	L1Pet l1pet = PetTable.getInstance().getTemplate(amuletId);
	//	if (l1pet == null) {
	//		return null;
	//	}
	//	L1Npc npcTemp = NpcTable.getInstance().getTemplate(l1pet.get_npcid());
	//	L1PetInstance pet = new L1PetInstance(npcTemp, pc, l1pet);
	//	pet.setPetcost(6);
	//	return pc;
	//}

	public void startDeathMatch(int deathMatchNo) {
		//_pc1[pcMatchNo].setCurrentPetStatus(1);
		//_pc1[pcMatchNo].setTarget(_pet2[petMatchNo]);

		//_pet2[petMatchNo].setCurrentPetStatus(1);
		//_pet2[petMatchNo].setTarget(_pet1[petMatchNo]);
		L1Teleport.teleport(_pc1[deathMatchNo], 32632 + Random.nextInt(10), 32892 + Random.nextInt(10), DEATH_MATCH_MAPID[deathMatchNo], 0, true);
		L1Teleport.teleport(_pc2[deathMatchNo], 32632 + Random.nextInt(10), 32892 + Random.nextInt(10), DEATH_MATCH_MAPID[deathMatchNo], 0, true);
		L1DeathMatchTimer timer = new L1DeathMatchTimer(_pc1[deathMatchNo], _pc2[deathMatchNo], deathMatchNo);
		timer.begin();
	}

	public void endDeathMatch(int deathMatchNo, int winNo) throws InterruptedException {
		L1PcInstance pc1 = L1World.getInstance().getPlayer(_pc1Name[deathMatchNo]);
		L1PcInstance pc2 = L1World.getInstance().getPlayer(_pc2Name[deathMatchNo]);
		if (winNo == 1) {
			giveMedal(pc1, deathMatchNo, true);
			giveMedal(pc2, deathMatchNo, false);
		}
		else if (winNo == 2) {
			giveMedal(pc1, deathMatchNo, false);
			giveMedal(pc2, deathMatchNo, true);
		}
		else if (winNo == 3) { // 引き分け
			giveMedal(pc1, deathMatchNo, false);
			giveMedal(pc2, deathMatchNo, false);
		}
		qiutDeathMatch(deathMatchNo);
	}

	private void giveMedal(L1PcInstance pc, int deathMatchNo, boolean isWin) {
		if (pc == null) {
			return;
		}
		if (pc.getMapId() != DEATH_MATCH_MAPID[deathMatchNo]) {
			return;
		}
		if (isWin) {
			pc.sendPackets(new S_ServerMessage(166, "恭喜 " + pc.getName() + " 在死亡戰場勝出！")); // %0%sペットマッチで勝利を収めました。
			L1ItemInstance item = ItemTable.getInstance().createItem(41402);
			int count = 3;
			if (item != null) {
				if (pc.getInventory().checkAddItem(item, count) == L1Inventory.OK) {
					item.setCount(count);
					pc.getInventory().storeItem(item);
					pc.sendPackets(new S_ServerMessage(403, item.getLogName())); // %0を手に入れました。
				}
			}
		}
		else {
			pc.sendPackets(new S_ServerMessage(166, "恭喜 " + pc.getName() + " 在死亡戰場勝出！")); // %0%sペットマッチで勝利を収めました。
			L1ItemInstance item = ItemTable.getInstance().createItem(41402);
			int count = 1;
			if (item != null) {
				if (pc.getInventory().checkAddItem(item, count) == L1Inventory.OK) {
					item.setCount(count);
					pc.getInventory().storeItem(item);
					pc.sendPackets(new S_ServerMessage(403, item.getLogName())); // %0を手に入れました。
				}
			}
		}
	}

	private void qiutDeathMatch(int deathMatchNo) throws InterruptedException {
		L1PcInstance pc1 = L1World.getInstance().getPlayer(_pc1Name[deathMatchNo]);
		if ((pc1 != null)&& (pc1.getMapId() == DEATH_MATCH_MAPID[deathMatchNo])) {
			Thread.sleep(5000);
			if (!pc1.isDead())
			L1Teleport.teleport(pc1, 32760, 32822, (short) 610, 4, true);
		}
		_pc1Name[deathMatchNo] = null;
		_pc1[deathMatchNo] = null;

		L1PcInstance pc2 = L1World.getInstance().getPlayer(_pc2Name[deathMatchNo]);
		if ((pc2 != null) && (pc2.getMapId() == DEATH_MATCH_MAPID[deathMatchNo])) {
			Thread.sleep(5000);
			if (!pc2.isDead())
			L1Teleport.teleport(pc2, 32760, 32822, (short) 610, 4, true);
		}
		_pc2Name[deathMatchNo] = null;
		_pc2[deathMatchNo] = null;
	}

	public class L1DeathMatchReadyTimer extends TimerTask {
		private Logger _log = Logger.getLogger(L1DeathMatchReadyTimer.class.getName());

		private final int _deathMatchNo;

		private final L1PcInstance _pc;

		public L1DeathMatchReadyTimer(int deathMatchNo, L1PcInstance pc) {
			_deathMatchNo = deathMatchNo;
			_pc = pc;
		}

		public void begin() {
			Timer timer = new Timer();
			timer.schedule(this, 3000);
		}

		@Override
		public void run() {
			try {
				for (;;) {
					Thread.sleep(1000);
					if ((_pc == null)) {
						cancel();
						return;
					}

					if (_pc.isTeleport()) {
						continue;
					}
					if (L1DeathMatch.getInstance().setDeathMatchPc(_deathMatchNo, _pc) == L1DeathMatch.STATUS_PLAYING) {
						L1DeathMatch.getInstance().startDeathMatch(_deathMatchNo);
					}
					cancel();
					return;
				}
			}
			catch (Throwable e) {
				_log.log(Level.WARNING, e.getLocalizedMessage(), e);
			}
		}

	}

	public class L1DeathMatchTimer extends TimerTask {
		private Logger _log = Logger.getLogger(L1DeathMatchTimer.class.getName());

		private final L1PcInstance _pc1;

		private final L1PcInstance _pc2;

		private final int _deathMatchNo;

		private int _counter = 0;

		public L1DeathMatchTimer(L1PcInstance pc1, L1PcInstance pc2, int deathMatchNo) {
			_pc1 = pc1;
			_pc2 = pc2;
			_deathMatchNo = deathMatchNo;
		}

		public void begin() {
			Timer timer = new Timer();
			timer.schedule(this, 0);
		}

		@Override
		public void run() {
			try {
				for (;;) {
					Thread.sleep(3000);
					_counter++;
					if ((_pc1 == null) || (_pc2 == null)) {
						cancel();
						return;
					}

					if (_pc1.isDead() || _pc2.isDead()) {
						int winner = 0;
						if (!_pc1.isDead() && _pc2.isDead()) {
							winner = 1;
						}
						else if (_pc1.isDead() && !_pc2.isDead()) {
							winner = 2;
						}
						else {
							winner = 3;
						}
						L1DeathMatch.getInstance().endDeathMatch(_deathMatchNo, winner);
						cancel();
						return;
					}

					if (_counter == 100) { // 5分経っても終わらない場合は引き分け
						L1DeathMatch.getInstance().endDeathMatch(_deathMatchNo, 3);
						cancel();
						return;
					}
				}
			}
			catch (Throwable e) {
				_log.log(Level.WARNING, e.getLocalizedMessage(), e);
			}
		}

	}

}
