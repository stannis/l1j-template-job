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
package l1j.william;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import l1j.server.L1DatabaseFactory;
import l1j.server.server.utils.SQLUtil;
import l1j.william.L1WilliamTeleportScroll;

public class TeleportScroll {
	private static Logger _log = Logger.getLogger(TeleportScroll.class
			.getName());

	private static TeleportScroll _instance;

	private final HashMap<Integer, L1WilliamTeleportScroll> _itemIdIndex = new HashMap<Integer, L1WilliamTeleportScroll>();

	public static TeleportScroll getInstance() {
		if (_instance == null) {
			_instance = new TeleportScroll();
		}
		return _instance;
	}

	private TeleportScroll() {
		loadTeleportScroll();
	}

	private void loadTeleportScroll() {
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {

			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con
					.prepareStatement("SELECT * FROM william_teleport_scroll");
			rs = pstm.executeQuery();
			fillTeleportScroll(rs);
		} catch (SQLException e) {
			_log.log(Level.SEVERE,
					"error while creating william_teleport_scroll table", e);
		} finally {
			SQLUtil.close(rs);
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
	}

	private void fillTeleportScroll(ResultSet rs) throws SQLException {
		while (rs.next()) {
			int itemId = rs.getInt("item_id");
			int tpLocX = rs.getInt("tpLocX");
			int tpLocY = rs.getInt("tpLocY");
			short tpMapId = rs.getShort("tpMapId");
			int check_minLocX = rs.getInt("check_minLocX");
			int check_minLocY = rs.getInt("check_minLocY");
			int check_maxLocX = rs.getInt("check_maxLocX");
			int check_maxLocY = rs.getInt("check_maxLocY");
			short check_MapId = rs.getShort("check_MapId");
			int removeItem = rs.getInt("removeItem");

			L1WilliamTeleportScroll teleport_scroll = new L1WilliamTeleportScroll(
					itemId, tpLocX, tpLocY, tpMapId, check_minLocX,
					check_minLocY, check_maxLocX, check_maxLocY, check_MapId,
					removeItem);
			_itemIdIndex.put(itemId, teleport_scroll);
		}
	}

	public L1WilliamTeleportScroll getTemplate(int itemId) {
		return _itemIdIndex.get(itemId);
	}
}
