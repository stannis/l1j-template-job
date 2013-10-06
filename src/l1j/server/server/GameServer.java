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
package l1j.server.server;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Collection;
import java.util.logging.Logger;

import l1j.server.Config;
import l1j.server.L1Message;
import l1j.server.console.ConsoleProcess;
import l1j.server.server.datatables.CastleTable;
import l1j.server.server.datatables.CharacterTable;
import l1j.server.server.datatables.ChatLogTable;
import l1j.server.server.datatables.ClanTable;
import l1j.server.server.datatables.DoorTable;
import l1j.server.server.datatables.DropTable;
import l1j.server.server.datatables.DropItemTable;
import l1j.server.server.datatables.FurnitureItemTable;
import l1j.server.server.datatables.FurnitureSpawnTable;
import l1j.server.server.datatables.GetBackRestartTable;
import l1j.server.server.datatables.InnTable;
import l1j.server.server.datatables.IpTable;
import l1j.server.server.datatables.ItemTable;
import l1j.server.server.datatables.MagicDollTable;
import l1j.server.server.datatables.MailTable;
import l1j.server.server.datatables.MapsTable;
import l1j.server.server.datatables.MobGroupTable;
import l1j.server.server.datatables.NpcActionTable;
import l1j.server.server.datatables.NpcChatTable;
import l1j.server.server.datatables.NpcSpawnTable;
import l1j.server.server.datatables.NpcTable;
import l1j.server.server.datatables.NPCTalkDataTable;
import l1j.server.server.datatables.PetTable;
import l1j.server.server.datatables.PetTypeTable;
import l1j.server.server.datatables.PolyTable;
import l1j.server.server.datatables.RaceTicketTable;
import l1j.server.server.datatables.ResolventTable;
import l1j.server.server.datatables.ShopTable;
import l1j.server.server.datatables.SkillsTable;
import l1j.server.server.datatables.SpawnTable;
import l1j.server.server.datatables.SprTable;
import l1j.server.server.datatables.UBSpawnTable;
import l1j.server.server.datatables.WeaponSkillTable;
import l1j.server.server.model.Dungeon;
import l1j.server.server.model.ElementalStoneGenerator;
import l1j.server.server.model.Getback;
import l1j.server.server.model.L1BossCycle;
import l1j.server.server.model.L1CastleLocation;
import l1j.server.server.model.L1DeleteItemOnGround;
import l1j.server.server.model.L1NpcRegenerationTimer;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.game.L1BugBearRace;
import l1j.server.server.model.gametime.L1GameTimeClock;
import l1j.server.server.model.item.L1TreasureBox;
import l1j.server.server.model.map.L1WorldMap;
import l1j.server.server.model.npc.action.L1NpcDefaultAction;
import l1j.server.server.model.trap.L1WorldTraps;
import l1j.server.server.storage.mysql.MysqlAutoBackup;
import l1j.server.server.utils.MysqlAutoBackupTimer;
import l1j.server.server.utils.SystemUtil;
import l1j.plugin.L1GameRestart; // 自動重開系統
import java.util.ArrayList;// 連線線程
import l1j.plugin.L1IpCount;// 連線線程
import l1j.william.TeleportScroll; // DB道具傳送卷軸(符) by 丫傑
import l1j.william.SystemMessage; // WilliamSystemMessage


// Referenced classes of package l1j.server.server:
// ClientThread, Logins, RateTable, IdFactory,
// LoginController, GameTimeController, Announcements,
// MobTable, SpawnTable, SkillsTable, PolyTable,
// TeleportLocations, ShopTable, NPCTalkDataTable, NpcSpawnTable,
// IpTable, Shutdown, NpcTable, MobGroupTable, NpcShoutTable

public class GameServer extends Thread {
	// private ServerSocket _serverSocket; 連線線程
	private static Logger _log = Logger.getLogger(GameServer.class.getName());
	private static int YesNoCount = 0;
	// private int _port; 連線線程
	// private Logins _logins;
	private LoginController _loginController;
	private int chatlvl;
	// 連線線程
	private GameServerThread GST1;
	private GameServerThread GST2;
	private GameServerThread GST3;
	private GameServerThread GST4;
	private GameServerThread GST5;
	private GameServerThread GST6;
	private GameServerThread GST7;
	private GameServerThread GST8;
	private static ArrayList<L1IpCount> iplist = new ArrayList<L1IpCount>();
	private static GameServer _instance;

	// 連線線程 end

	// 連線線程
	public GameServer() {
		try {
			Load();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 連線線程
	/*
	 * @Override public void run() { System.out.println(L1Message.memoryUse +
	 * SystemUtil.getUsedMemoryMB() + L1Message.memory);
	 * System.out.println(L1Message.waitingforuser); while (true) { try { Socket
	 * socket = _serverSocket.accept(); System.out.println(L1Message.from +
	 * socket.getInetAddress() + L1Message.attempt); String host =
	 * socket.getInetAddress().getHostAddress(); if
	 * (IpTable.getInstance().isBannedIp(host)) { _log.info("banned IP(" + host
	 * + ")"); } else { ClientThread client = new ClientThread(socket);
	 * GeneralThreadPool.getInstance().execute(client); } } catch (IOException
	 * ioexception) { } } }
	 * 
	 * private static GameServer _instance;
	 * 
	 * private GameServer() { super("GameServer"); }
	 */

	public static GameServer getInstance() {
		if (_instance == null) {
			_instance = new GameServer();
		}
		return _instance;
	}

	// 連線線程 end
	// 連線線程 public void initialize() throws Exception {
	public void Load() throws Exception {
		// String s = Config.GAME_SERVER_HOST_NAME;
		double rateXp = Config.RATE_XP;
		double LA = Config.RATE_LA;
		double rateKarma = Config.RATE_KARMA;
		double rateDropItems = Config.RATE_DROP_ITEMS;
		double rateDropAdena = Config.RATE_DROP_ADENA;

		// Locale 多國語系
		L1Message.getInstance();

		chatlvl = Config.GLOBAL_CHAT_LEVEL;
		/*
		 * 連線線程 _port = Config.GAME_SERVER_PORT; if (!"*".equals(s)) {
		 * InetAddress inetaddress = InetAddress.getByName(s);
		 * inetaddress.getHostAddress(); _serverSocket = new ServerSocket(_port,
		 * 50, inetaddress); System.out.println(L1Message.setporton + _port); }
		 * else { _serverSocket = new ServerSocket(_port);
		 * System.out.println(L1Message.setporton + _port); }連線線程 end
		 */

		System.out.println("┌───────────────────────────────┐");
		System.out.println("│     " + L1Message.ver + "\t" + "\t" + "│");
		System.out.println("└───────────────────────────────┘" + "\n");

		System.out.println(L1Message.settingslist + "\n");
		System.out.println("┌" + L1Message.exp + ": " + (rateXp) + L1Message.x
				+ "\n├" + L1Message.justice + ": " + (LA) + L1Message.x
				+ "\n├" + L1Message.karma + ": " + (rateKarma) + L1Message.x
				+ "\n├" + L1Message.dropitems + ": " + (rateDropItems)+ L1Message.x 
				+ "\n├" + L1Message.dropadena + ": "+ (rateDropAdena) + L1Message.x 
				+ "\n├"+ L1Message.enchantweapon + ": "+ (Config.ENCHANT_CHANCE_WEAPON) + "%" 
				+ "\n├"+ L1Message.enchantarmor + ": " + (Config.ENCHANT_CHANCE_ARMOR)+ "%");
		System.out.println("├" + L1Message.chatlevel + ": " + (chatlvl)
				+ L1Message.level);

		if (Config.ALT_NONPVP) { // Non-PvP設定
			System.out.println("└" + L1Message.nonpvpNo + "\n");
		} else {
			System.out.println("└" + L1Message.nonpvpYes + "\n");
		}

		int maxOnlineUsers = Config.MAX_ONLINE_USERS;
		System.out.println(L1Message.maxplayer + (maxOnlineUsers)
				+ L1Message.player);

		System.out.println("┌───────────────────────────────┐");
		System.out.println("│     " + L1Message.ver + "\t" + "\t" + "│");
		System.out.println("└───────────────────────────────┘" + "\n");

		IdFactory.getInstance();
		L1WorldMap.getInstance();
		_loginController = LoginController.getInstance();
		_loginController.setMaxAllowedOnlinePlayers(maxOnlineUsers);

		// 讀取所有角色名稱
		CharacterTable.getInstance().loadAllCharName();

		// 初始化角色的上線狀態
		CharacterTable.clearOnlineStatus();

		// 初始化遊戲時間
		L1GameTimeClock.init();

		// 自動重開系統
		L1GameRestart.init();

		// 初始化無限大戰
		UbTimeController ubTimeContoroller = UbTimeController.getInstance();
		GeneralThreadPool.getInstance().execute(ubTimeContoroller);

		// 初始化攻城
		WarTimeController warTimeController = WarTimeController.getInstance();
		GeneralThreadPool.getInstance().execute(warTimeController);

		// 設定精靈石的產生
		if (Config.ELEMENTAL_STONE_AMOUNT > 0) {
			ElementalStoneGenerator elementalStoneGenerator = ElementalStoneGenerator.getInstance();
			GeneralThreadPool.getInstance().execute(elementalStoneGenerator);
		}

		// 初始化 HomeTown 時間
		HomeTownTimeController.getInstance();

		// 初始化盟屋拍賣
		AuctionTimeController auctionTimeController = AuctionTimeController.getInstance();
		GeneralThreadPool.getInstance().execute(auctionTimeController);

		// 初始化盟屋的稅金
		HouseTaxTimeController houseTaxTimeController = HouseTaxTimeController.getInstance();
		GeneralThreadPool.getInstance().execute(houseTaxTimeController);

		// 初始化釣魚
		FishingTimeController fishingTimeController = FishingTimeController.getInstance();
		GeneralThreadPool.getInstance().execute(fishingTimeController);

		// 初始化 NPC 聊天
		NpcChatTimeController npcChatTimeController = NpcChatTimeController.getInstance();
		GeneralThreadPool.getInstance().execute(npcChatTimeController);

		// 初始化 Light
		LightTimeController lightTimeController = LightTimeController.getInstance();
		GeneralThreadPool.getInstance().execute(lightTimeController);

		// 初始化遊戲公告
		Announcements.getInstance();
		
		// 初始化遊戲循環公告
	    AnnouncementsCycle.getInstance();

		// 初始化MySQL自動備份程序
		MysqlAutoBackup.getInstance();

		// 開始 MySQL自動備份程序 計時器
		MysqlAutoBackupTimer.TimerStart();
		
		// 初始化帳號使用狀態
		Account.InitialOnlineStatus();

		NpcTable.getInstance();
		L1DeleteItemOnGround deleteitem = new L1DeleteItemOnGround();
		deleteitem.initialize();

		if (!NpcTable.getInstance().isInitialized()) {
			throw new Exception("無法初始化NPC資料表");
		}
		L1NpcDefaultAction.getInstance();
		DoorTable.initialize();
		SpawnTable.getInstance();
		MobGroupTable.getInstance();
		SkillsTable.getInstance();
		PolyTable.getInstance();
		ItemTable.getInstance();
		DropTable.getInstance();
		DropItemTable.getInstance();
		ShopTable.getInstance();
		NPCTalkDataTable.getInstance();
		L1World.getInstance();
		L1WorldTraps.getInstance();
		Dungeon.getInstance();
		NpcSpawnTable.getInstance();
		IpTable.getInstance();
		MapsTable.getInstance();
		UBSpawnTable.getInstance();
		PetTable.getInstance();
		ClanTable.getInstance();
		CastleTable.getInstance();
		L1CastleLocation.setCastleTaxRate(); // 必須在 CastleTable 初始化之後
		GetBackRestartTable.getInstance();
		GeneralThreadPool.getInstance();
		L1NpcRegenerationTimer.getInstance();
		ChatLogTable.getInstance();
		WeaponSkillTable.getInstance();
		NpcActionTable.load();
		GMCommandsConfig.load();
		Getback.loadGetBack();
		PetTypeTable.load();
		L1BossCycle.load();
		L1TreasureBox.load();
		SprTable.getInstance();
		ResolventTable.getInstance();
		FurnitureSpawnTable.getInstance();
		NpcChatTable.getInstance();
		MailTable.getInstance();
		RaceTicketTable.getInstance();
		L1BugBearRace.getInstance();
		TeleportScroll.getInstance(); // DB道具傳送卷軸(符) by 丫傑
		InnTable.getInstance();
		MagicDollTable.getInstance();
		FurnitureItemTable.getInstance();
		SystemMessage.getInstance(); // WilliamSystemMessage

		System.out.println(L1Message.initialfinished);
		Runtime.getRuntime().addShutdownHook(Shutdown.getInstance());
		
		// cmd互動指令
		Thread cp = new ConsoleProcess();
		cp.start();
		
		// 連線線程
		if (Config.GAME_SERVER_PORT > 0 && Config.GAME_SERVER_PORT <= 65535
				&& Config.OpenPort) {
			GST1 = new GameServerThread(1, Config.GAME_SERVER_PORT,
					Config.GAME_SERVER_HOST_NAME);
			if (GST1.get_IsCanStart())
				GST1.start();
		}
		if (Config.GAME_SERVER_PORT1 > 0 && Config.GAME_SERVER_PORT1 <= 65535
				&& Config.OpenPort1) {
			GST2 = new GameServerThread(2, Config.GAME_SERVER_PORT1,
					Config.GAME_SERVER_HOST_NAME);
			if (GST2.get_IsCanStart())
				GST2.start();
		}
		if (Config.GAME_SERVER_PORT2 > 0 && Config.GAME_SERVER_PORT2 <= 65535
				&& Config.OpenPort2) {
			GST3 = new GameServerThread(3, Config.GAME_SERVER_PORT2,
					Config.GAME_SERVER_HOST_NAME);
			if (GST3.get_IsCanStart())
				GST3.start();
		}
		if (Config.GAME_SERVER_PORT3 > 0 && Config.GAME_SERVER_PORT3 <= 65535
				&& Config.OpenPort3) {
			GST4 = new GameServerThread(4, Config.GAME_SERVER_PORT3,
					Config.GAME_SERVER_HOST_NAME);
			if (GST4.get_IsCanStart())
				GST4.start();
		}
		if (Config.GAME_SERVER_PORT4 > 0 && Config.GAME_SERVER_PORT4 <= 65535
				&& Config.OpenPort4) {
			GST5 = new GameServerThread(5, Config.GAME_SERVER_PORT4,
					Config.GAME_SERVER_HOST_NAME);
			if (GST5.get_IsCanStart())
				GST5.start();
		}
		if (Config.GAME_SERVER_PORT5 > 0 && Config.GAME_SERVER_PORT5 <= 65535
				&& Config.OpenPort5) {
			GST6 = new GameServerThread(6, Config.GAME_SERVER_PORT5,
					Config.GAME_SERVER_HOST_NAME);
			if (GST6.get_IsCanStart())
				GST6.start();
		}
		if (Config.GAME_SERVER_PORT6 > 0 && Config.GAME_SERVER_PORT6 <= 65535
				&& Config.OpenPort6) {
			GST7 = new GameServerThread(7, Config.GAME_SERVER_PORT6,
					Config.GAME_SERVER_HOST_NAME);
			if (GST7.get_IsCanStart())
				GST7.start();
		}
		if (Config.GAME_SERVER_PORT7 > 0 && Config.GAME_SERVER_PORT7 <= 65535
				&& Config.OpenPort7) {
			GST8 = new GameServerThread(8, Config.GAME_SERVER_PORT7,
					Config.GAME_SERVER_HOST_NAME);
			if (GST8.get_IsCanStart())
				GST8.start();
		}
		// 連線線程 end

		this.start();
	}

	/**
	 * 踢掉世界地圖中所有的玩家與儲存資料。
	 */
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
	}

	private class ServerShutdownThread extends Thread {
		private final int _secondsCount;

		public ServerShutdownThread(int secondsCount) {
			_secondsCount = secondsCount;
		}

		@Override
		public void run() {
			L1World world = L1World.getInstance();
			try {
				int secondsCount = _secondsCount;
				world.broadcastServerMessage("伺服器即將爆炸。");
				System.out.println("伺服器即將關閉。");
				world.broadcastServerMessage("請玩家移動到安全區域準備逃生。");
				while (0 < secondsCount) {
					if (secondsCount <= 30) {
						world.broadcastServerMessage("伺服器將在" + secondsCount
								+ "秒後爆炸，請玩家移動到安全區域準備逃生。");
						System.out.println("伺服器將在" + secondsCount
								+ "秒後關閉。");
					} else {
						if (secondsCount % 60 == 0) {
							world.broadcastServerMessage("伺服器將在" + secondsCount
									/ 60 + "分鐘後爆炸。");
							System.out.println("伺服器將在" + secondsCount
									/ 60 + "分鐘後關閉。");
						}
					}
					Thread.sleep(1000);
					secondsCount--;
				}
				shutdown();
			} catch (InterruptedException e) {
				world.broadcastServerMessage("爆炸危機已解除，伺服器將會繼續正常運作！");
				System.out.println("取消關機動作，伺服器將會繼續正常運作！");
				return;
			}
		}
	}

	// 連線線程
	public static boolean Ishave(String i) {
		for (L1IpCount j : iplist) {
			if (i.equalsIgnoreCase(j.get_IP())) {
				return true;
			}
		}
		return false;
	}

	public static void update(String i) {
		for (L1IpCount j : iplist) {
			if (i.equalsIgnoreCase(j.get_IP())) {
				j.set_TIMES(System.currentTimeMillis());
			}
		}
	}

	public static long get(String i) {
		for (L1IpCount j : iplist) {
			if (i.equalsIgnoreCase(j.get_IP())) {
				return j.get_TIMES();
			}
		}
		return 0;
	}

	public static void Insert(String i) {
		L1IpCount ipc = new L1IpCount(i, System.currentTimeMillis());
		iplist.add(ipc);
	}

	public class GameServerThread extends Thread {
		private int _id = 0;
		private int _port = 0;
		private boolean IsCanStart = false;
		private ServerSocket _serverSocket;

		public boolean get_IsCanStart() {
			return IsCanStart;
		}

		public GameServerThread(int id, int port, String s) {
			_id = id;
			_port = port;
			try {
				if (!"*".equals(s)) {
					InetAddress inetaddress;
					inetaddress = InetAddress.getByName(s);
					inetaddress.getHostAddress();
					if (_port != 0)
						_serverSocket = new ServerSocket(_port, 50, inetaddress);
				} else {
					_serverSocket = new ServerSocket(_port);
				}
				IsCanStart = true;
			} catch (Exception e) {
				IsCanStart = false;
				e.printStackTrace();
			}
		}

		public void run() {
			System.out
					.println("★連線線程:【" + _id + "】" + L1Message.waitingforuser);
			System.out.println(L1Message.memoryUse
					+ SystemUtil.getUsedMemoryMB() + L1Message.memory);
			while (true) {
				try {
					Socket socket = _serverSocket.accept();
					System.out.println("☆連線線程:【" + _id + "】"
							+ socket.getInetAddress() + L1Message.attempt);
					String host = socket.getInetAddress().getHostAddress();
					if (IpTable.getInstance().isBannedIp(host)) {
						_log.info("◆連線線程:【" + _id + "】 banned IP(" + host + ")");
					} else {
						ClientThread client = new ClientThread(socket);
						GeneralThreadPool.getInstance().execute(client);
					}
				} catch (IOException ioexception) {
				}
			}
		}
	}

	// 連線線程 end

	private ServerShutdownThread _shutdownThread = null;

	public synchronized void shutdownWithCountdown(int secondsCount) {
		if (_shutdownThread != null) {
			// 如果正在關閉
			// TODO 可能要有錯誤通知之類的
			return;
		}
		_shutdownThread = new ServerShutdownThread(secondsCount);
		GeneralThreadPool.getInstance().execute(_shutdownThread);
	}

	public void shutdown() {
		disconnectAllCharacters();
		System.exit(0);
	}

	public synchronized void abortShutdown() {
		if (_shutdownThread == null) {
			// 如果正在關閉
			// TODO 可能要有錯誤通知之類的
			return;
		}

		_shutdownThread.interrupt();
		_shutdownThread = null;
	}

	/**
	 * 取得世界中發送YesNo總次數
	 * @return YesNo總次數
	 */
	public static int getYesNoCount() {
		YesNoCount += 1;
		return YesNoCount;
	}
}
