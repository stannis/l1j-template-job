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
package l1j.plugin;

import java.util.Calendar;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import l1j.server.Config;
import l1j.server.server.Account;
import l1j.server.server.model.gametime.L1GameTime;
import l1j.server.server.model.gametime.L1GameTimeListener;
import l1j.server.server.ClientThread;// 重開斷線 use eric1300460 code
import l1j.server.server.GameServer;
import l1j.server.server.GeneralThreadPool;
// 新年倒數 idea by missu0524  
import l1j.plugin.GetNowTime;
import l1j.server.server.serverpackets.S_SkillSound;
// 新年倒數 優化 by terry0412  

//elfooxx
import java.util.Collection;
import java.io.IOException;
import java.io.InputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.L1World;
import l1j.server.server.serverpackets.S_SystemMessage;
import l1j.server.server.serverpackets.S_Message_YN;

//elfooxx

public class L1GameRestart {
	private static Logger _log = Logger
			.getLogger(L1GameRestart.class.getName());

	private static L1GameRestart _instance;
	private volatile L1GameTime _currentTime = L1GameTime
			.fromSystemCurrentTime();
	private L1GameTime _previousTime = null;

	private List<L1GameTimeListener> _listeners = new CopyOnWriteArrayList<L1GameTimeListener>();

	public int _remnant; /* elfooxx */

	private class TimeUpdaterRestar implements Runnable /* elfooxx */{
		public void run() {
			while (true) {
				_previousTime = _currentTime;
				_currentTime = L1GameTime.fromSystemCurrentTime();
				notifyChanged();
				int remnant = GetRestartTime() * 60;
				System.out.println("》》正在載入 自動重開 設定...完成! " + GetRestartTime()
						+ "分鐘後");
				while (remnant > 0) {
					for (int i = remnant; i >= 0; i--) {
						SetRemnant(i);

						// 定時報時
						if ((GetNowTime.GetNowHour() == 6
								&& GetNowTime.GetNowMinute() == 0 && GetNowTime
								.GetNowSecond() == 0) || // 6:00
								(GetNowTime.GetNowHour() == 12
										&& GetNowTime.GetNowMinute() == 0 && GetNowTime
										.GetNowSecond() == 0) || // 12:00
								(GetNowTime.GetNowHour() == 18
										&& GetNowTime.GetNowMinute() == 0 && GetNowTime
										.GetNowSecond() == 0) || // 18:00
								(GetNowTime.GetNowHour() == 21
										&& GetNowTime.GetNowMinute() == 0 && GetNowTime
										.GetNowSecond() == 0) // 21:00
						) {
							BroadCastToAll("\\fR現在時間是 "
									+ GetNowTime.GetNowHour() + " 點 "
									+ GetNowTime.GetNowMinute()
									+ " 分，快站起來動一動別坐太久喔！");
							RessToAll();
						}
						// end
						// 新年倒數 idea by missu0524
						int NextYear = GetNowTime.GetNowYear() + 1;
						int countdown = 10;
						if (GetNowTime.GetNowMonth() == 12
								&& GetNowTime.GetNowDay() == 31
								&& GetNowTime.GetNowHour() == 23
								&& GetNowTime.GetNowMinute() == 50
								&& GetNowTime.GetNowSecond() == 0) {
							while (countdown >= 0) {
								for (int i2 = 60; i2 >= 0; i2--) { // 執行61次
									if (countdown != 0 && i2 % 60 == 0
											&& i2 != 0) {
										BroadCastToAll("\\fU迎向 " + NextYear
												+ " 新年倒數前 " + countdown
												+ " 分鐘。");
										System.out.println("迎向 " + NextYear
												+ " 新年倒數前 " + countdown
												+ " 分鐘。");
										countdown--;
									} else if (countdown == 0 && i2 == 30
											&& i2 != 0) {
										BroadCastToAll("\\fU迎向 " + NextYear
												+ " 新年倒數前 " + i2 + " 秒。");
										System.out.println("迎向 " + NextYear
												+ " 新年倒數前 " + i2 + " 秒。");
									} else if (countdown == 0 && i2 <= 10
											&& i2 != 0) {
										BroadCastToAll("\\fU迎向 " + NextYear
												+ " 新年倒數前 " + i2 + " 秒。");
										System.out.println("迎向 " + NextYear
												+ " 新年倒數前 " + i2 + " 秒。");
									} else if (countdown == 0 && i2 == 0) {
										BroadCastToAll("\\fW＊＊＊＊＊ " + NextYear
												+ " 新 年 快 樂 ＊＊＊＊＊");
										System.out.println("＊＊＊＊＊ " + NextYear
												+ " 新 年 快 樂 ＊＊＊＊＊");
										countdown--;
										/*
										 * for (L1PcInstance pc : L1World
										 * .getInstance().getAllPlayers()) {
										 * pc.sendPackets(new S_SkillSound(pc
										 * .getId(), 2048));
										 * pc.broadcastPacket(new S_SkillSound(
										 * pc.getId(), 2048));
										 * pc.getInventory().storeItem(60194,
										 * 1); pc.sendPackets(new
										 * S_SystemMessage( "祝伺服器全體人員【" +
										 * NextYear + "年】新年快樂！贈金元寶1個")); }
										 */
									}
									try {
										Thread.sleep(1000);
									} catch (Exception e) {
										_log.log(Level.SEVERE,
												e.getLocalizedMessage(), e);
									}
								}
							}
						}
						// 新年倒數 優化 by terry0412

						if (i % 60 == 0 && i <= 300 && i != 0) {
							BroadCastToAll("伺服器將於 " + i / 60
									+ " 分鐘後自動爆炸,請至安全區域準備登出。");
							System.out
									.println("》》伺服器將於 " + i / 60 + " 分鐘後重新啟動");
						} // if (五分鐘內 一分鐘一次)
						else if (i <= 30 && i != 0) {
							BroadCastToAll("伺服器將於 " + i + "秒後自動爆炸,請立即下線！");
							System.out.println("》》伺服器將於 " + i + " 秒後重新啟動");
						} // else if (30秒內 一秒一次)
						else if (i == 0) {
							BroadCastToAll("伺服器自動爆炸。");
							System.out.println("》》伺服器重新啟動。");
							disconnectAllCharacters(); // 重開斷線 by eric1300460
							//add GUI by Eric
							if(Config.GUI)
								l1j.gui.Eric_J_Main.getInstance().saveChatData(false);//自動儲存訊息
							//end
							GameServer.getInstance().shutdown(); // 修正自動重開角色資料會回溯之問題
						} // if 1秒
						try {
							Thread.sleep(1000);
						} catch (InterruptedException e) {
							_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
						} // try and catch

					} // for
				} // while ( remnant > 0 )
			} // while true
		} // run()
	} // class TimeUpdaterRestar /*elfooxx*/

	// 重開斷線 use eric1300460 code
	public void disconnectAllCharacters() {
		Collection<L1PcInstance> players = L1World.getInstance()
				.getAllPlayers();
		for (L1PcInstance pc : players) {
			pc.getNetConnection().setActiveChar(null);
			pc.getNetConnection().kick();
		}
		// 踢除所有在線上的玩家
		for (L1PcInstance pc : players) {
			ClientThread.quitGame(pc);
			L1World.getInstance().removeObject(pc);
			Account account = Account.load(pc.getAccountName());
			Account.online(account, false);
		}
	}// 重開斷線 end

	private int GetRestartTime() {
		Properties properties = new Properties();
		try {
			InputStream input = new FileInputStream(new File(
					"./config/othersettings.properties"));
			properties.load(input);
			input.close();
		} catch (IOException e) {
			e.printStackTrace();
		} // try catch
		return Integer.parseInt(properties.getProperty("RestartTime"));
	} // GetRestartTime()

	private void BroadCastToAll(String string) {
		Collection<L1PcInstance> allpc = L1World.getInstance().getAllPlayers();
		for (L1PcInstance pc : allpc)
			pc.sendPackets(new S_SystemMessage(string));
	} // BroadcastToAll()

	private void RessToAll() {
		for (L1PcInstance tg : L1World.getInstance().getAllPlayers()) {// 修改為全伺服器
			if (tg.getCurrentHp() == 0 && tg.isDead()) {
				tg.broadcastPacket(new S_SkillSound(tg.getId(), 3944));
				tg.sendPackets(new S_SkillSound(tg.getId(), 3944));
				// 祝福された 復活スクロールと同じ効果
				tg.setTempID(tg.getId());
				tg.sendPackets(new S_Message_YN(322, "")); // また復活したいですか？（Y/N）
			} else {
				tg.broadcastPacket(new S_SkillSound(tg.getId(), 832));
				tg.sendPackets(new S_SkillSound(tg.getId(), 832));
				tg.setCurrentHp(tg.getMaxHp());
				tg.setCurrentMp(tg.getMaxMp());
			}
		}
	}

	public void SetRemnant(int remnant) {
		_remnant = remnant;
	}

	public int GetRemnant() {
		return _remnant;
	}

	/* elfooxx */

	private boolean isFieldChanged(int field) {
		return _previousTime.get(field) != _currentTime.get(field);
	}

	private void notifyChanged() {
		if (isFieldChanged(Calendar.MONTH)) {
			for (L1GameTimeListener listener : _listeners) {
				listener.onMonthChanged(_currentTime);
			}
		}
		if (isFieldChanged(Calendar.DAY_OF_MONTH)) {
			for (L1GameTimeListener listener : _listeners) {
				listener.onDayChanged(_currentTime);
			}
		}
		if (isFieldChanged(Calendar.HOUR_OF_DAY)) {
			for (L1GameTimeListener listener : _listeners) {
				listener.onHourChanged(_currentTime);
			}
		}
		if (isFieldChanged(Calendar.MINUTE)) {
			for (L1GameTimeListener listener : _listeners) {
				listener.onMinuteChanged(_currentTime);
			}
		}
	}

	private L1GameRestart() {
		GeneralThreadPool.getInstance().execute(new TimeUpdaterRestar());
	}

	public static void init() {
		_instance = new L1GameRestart();
	}

	public static L1GameRestart getInstance() {
		return _instance;
	}

	public L1GameTime getGameTime() {
		return _currentTime;
	}

	public void addListener(L1GameTimeListener listener) {
		_listeners.add(listener);
	}

	public void removeListener(L1GameTimeListener listener) {
		_listeners.remove(listener);
	}
}
