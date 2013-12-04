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
package l1j.server.server.datatables;

import java.sql.Connection;
import java.util.Random;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import l1j.server.L1DatabaseFactory;
import l1j.server.server.model.L1EquipmentSlot;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_NPCTalkReturn;
import l1j.server.server.templates.L1Pet;
import l1j.server.server.templates.L1WeaponSoul;
import l1j.server.server.utils.SQLUtil;
import l1j.server.server.utils.collections.Maps;

public class WeaponSoulTable {

	private static Logger _log = Logger.getLogger(WeaponSoulTable.class.getName());

	private static WeaponSoulTable _instance;

	private final Map<Integer, L1WeaponSoul> _WeaponSouls = Maps.newMap();
	

	public static WeaponSoulTable getInstance() {
		if (_instance == null) {
			_instance = new WeaponSoulTable();
		}
		return _instance;
	}
	//取得最新資料表
	public static WeaponSoulTable getNewInstance() {	
		_instance = new WeaponSoulTable();
		return _instance;
	}

	private WeaponSoulTable() {
		load();
	}

	private void load() {
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("SELECT * FROM weapon_Soul");

			rs = pstm.executeQuery();
			while (rs.next()) {
				L1WeaponSoul WeaponSoul = new L1WeaponSoul();
				int itemobjid = rs.getInt(1);
				WeaponSoul.set_itemobjid(itemobjid);
				WeaponSoul.set_itemid(rs.getInt(2));
				WeaponSoul.set_type(rs.getInt(3));
				WeaponSoul.set_level(rs.getInt(4));
				WeaponSoul.set_lSkill1(rs.getInt(5));
				WeaponSoul.set_baselskill1(rs.getInt(6));
				WeaponSoul.set_lSkill2(rs.getInt(7));
				WeaponSoul.set_baselskill2(rs.getInt(8));
				WeaponSoul.set_mSkill1(rs.getInt(9));
				WeaponSoul.set_basemSkill1(rs.getInt(10));
				WeaponSoul.set_mSkill2(rs.getInt(11));
				WeaponSoul.set_basemSkill2(rs.getInt(12));
				WeaponSoul.set_mSkill3(rs.getInt(13));
				WeaponSoul.set_basemSkill3(rs.getInt(14));
				WeaponSoul.set_mSkill4(rs.getInt(15));
				WeaponSoul.set_basemSkill4(rs.getInt(16));
				WeaponSoul.set_hSkill1(rs.getInt(17));
				WeaponSoul.set_basehSkill1(rs.getInt(18));
				WeaponSoul.set_hSkill2(rs.getInt(19));
				WeaponSoul.set_basehSkill2(rs.getInt(20));
				WeaponSoul.set_hSkill3(rs.getInt(21));
				WeaponSoul.set_basehSkill3(rs.getInt(22));
				WeaponSoul.set_hSkill4(rs.getInt(23));
				WeaponSoul.set_basehSkill4(rs.getInt(24));
				WeaponSoul.set_lvpoint(rs.getInt(25));
				WeaponSoul.set_exp(rs.getInt(26));
				WeaponSoul.set_remainlvPoint(rs.getInt(27));
				WeaponSoul.set_owner(rs.getInt(28));

				_WeaponSouls.put(new Integer(itemobjid), WeaponSoul);
			}
		} catch (SQLException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(rs);
			SQLUtil.close(pstm);
			SQLUtil.close(con);

		}
	}
	
	public void storeNewWeaponSoul(L1PcInstance pc, int itemid, int itemobjid) {
		L1WeaponSoul l1WeaponSoul = new L1WeaponSoul();
		l1WeaponSoul.set_itemobjid(itemobjid);
		l1WeaponSoul.set_itemid(itemid);
		l1WeaponSoul.set_lSkill1(0);
		l1WeaponSoul.set_lSkill2(0);
		l1WeaponSoul.set_mSkill1(0);
		l1WeaponSoul.set_mSkill2(0);
		l1WeaponSoul.set_mSkill3(0);
		l1WeaponSoul.set_mSkill4(0);
		l1WeaponSoul.set_hSkill1(0);
		l1WeaponSoul.set_hSkill2(0);
		l1WeaponSoul.set_hSkill3(0);
		l1WeaponSoul.set_hSkill4(0);
		l1WeaponSoul.set_exp(0);
		l1WeaponSoul.set_owner(pc.getId());
		if (itemid == 300001){//判定為幼年
			l1WeaponSoul.set_type(0);//設定為幼年武魂石
			l1WeaponSoul.set_level(1);
			lporion(l1WeaponSoul, 1);
			mporion(l1WeaponSoul, 2, 0);
			hporion(l1WeaponSoul, 2, 0);
			l1WeaponSoul.set_lvpoint(3);
			l1WeaponSoul.set_remainlvPoint(3);
		}
		else if (itemid == 300002 || itemid == 300003){
			l1WeaponSoul.set_type(1);//設定為幼年武魂石
			l1WeaponSoul.set_level(1);
			l1WeaponSoul.set_lvpoint(4);
			l1WeaponSoul.set_remainlvPoint(4);
			if (itemid == 300002){
				lporion(l1WeaponSoul, 2);
				mporion(l1WeaponSoul, 3, 0);
				hporion(l1WeaponSoul, 3, 0);
			}	
			else{
				lporion(l1WeaponSoul, 2);
				mporion(l1WeaponSoul, 3, 1);
				hporion(l1WeaponSoul, 3, 1);
			}
		}
		else if (itemid == 300004 || itemid == 300005 || itemid == 300006){
			l1WeaponSoul.set_type(2);//設定為幼年武魂石
			l1WeaponSoul.set_level(1);
			l1WeaponSoul.set_lvpoint(5);
			l1WeaponSoul.set_remainlvPoint(5);
			if (itemid == 300004){
				lporion(l1WeaponSoul, 3);
				mporion(l1WeaponSoul, 4, 0);
				hporion(l1WeaponSoul, 4, 0);
			}	
			else if (itemid == 300005){
				lporion(l1WeaponSoul, 3);
				mporion(l1WeaponSoul, 4, 1);
				hporion(l1WeaponSoul, 4, 1);
			}
			else if (itemid == 300006){
				lporion(l1WeaponSoul, 3);
				mporion(l1WeaponSoul, 4, 2);
				hporion(l1WeaponSoul, 4, 2);
			}
		}
		_WeaponSouls.put(new Integer(itemid), l1WeaponSoul);
		Connection con = null;
		PreparedStatement pstm = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con
					.prepareStatement("INSERT INTO weapon_Soul SET item_obj_id=?,itemid=?,type=?,lv=?,lSkill1_Lv=?,base_lSkill1_Lv=?,lSkill2_Lv=?,base_lSkill2_Lv=?,mSkill1_Lv=?,base_mSkill1_Lv=?,mSkill2_Lv=?,base_mSkill2_Lv=?,mSkill3_Lv=?,base_mSkill3_Lv=?,mSkill4_Lv=?,base_mSkill4_Lv=?,hSkill1_Lv=?,base_hSkill1_Lv=?,hSkill2_Lv=?,base_hSkill2_Lv=?,hSkill3_Lv=?,base_hSkill3_Lv=?,hSkill4_Lv=?,base_hSkill4_Lv=?,lvPoints=?,exp=?,remainlvPoint=?,owner=?");
			pstm.setInt(1, l1WeaponSoul.get_itemobjid());
			pstm.setInt(2, l1WeaponSoul.get_itemid());
			pstm.setInt(3, l1WeaponSoul.get_type());
			pstm.setInt(4, l1WeaponSoul.get_level());
			pstm.setInt(5, l1WeaponSoul.get_lSkill1());
			pstm.setInt(6, l1WeaponSoul.get_baselskill1());
			pstm.setInt(7, l1WeaponSoul.get_lSkill2());
			pstm.setInt(8, l1WeaponSoul.get_baselskill2());
			pstm.setInt(9, l1WeaponSoul.get_mSkill1());
			pstm.setInt(10, l1WeaponSoul.get_basemSkill1());
			pstm.setInt(11, l1WeaponSoul.get_mSkill2());
			pstm.setInt(12, l1WeaponSoul.get_basemSkill2());
			pstm.setInt(13, l1WeaponSoul.get_mSkill3());
			pstm.setInt(14, l1WeaponSoul.get_basemSkill3());
			pstm.setInt(15, l1WeaponSoul.get_mSkill4());
			pstm.setInt(16, l1WeaponSoul.get_basemSkill4());
			pstm.setInt(17, l1WeaponSoul.get_hSkill1());
			pstm.setInt(18, l1WeaponSoul.get_basehSkill1());
			pstm.setInt(19, l1WeaponSoul.get_hSkill2());
			pstm.setInt(20, l1WeaponSoul.get_basehSkill2());
			pstm.setInt(21, l1WeaponSoul.get_hSkill3());
			pstm.setInt(22, l1WeaponSoul.get_basehSkill3());
			pstm.setInt(23, l1WeaponSoul.get_hSkill4());
			pstm.setInt(24, l1WeaponSoul.get_basehSkill4());
			pstm.setInt(25, l1WeaponSoul.get_lvpoint());
			pstm.setInt(26, l1WeaponSoul.get_exp());
			pstm.setInt(27, l1WeaponSoul.get_remainlvPoint());
			pstm.setInt(28, l1WeaponSoul.get_owner());
			pstm.execute();
		} catch (Exception e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);

		} finally {
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
	}
	
	//public L1pstm getTemplate(int itemobjid){//未完成
		
	//}
	
	//remainpoints=基礎總點數-技能項目
	//幼年期中階技能配點總數為3、少年期中階技能配點總數為4、成年期中階技能配點總數為5
	private void lporion(L1WeaponSoul l1WeaponSoul, int remainpoints){
		int lbaseskilllv1=1,lbaseskilllv2=1;
		int choice;
		while (remainpoints > 0){
			choice = (int) (Math.random()*2+1);
			switch(choice){
			case 1:
				if(lbaseskilllv1 < 3){
					lbaseskilllv1++;
					remainpoints--;
				}
				break;
			case 2:
				if(lbaseskilllv2 < 3){
					lbaseskilllv2++;
					remainpoints--;
				}
				break;
			}
		}
		l1WeaponSoul.set_baselskill1(lbaseskilllv1);
		l1WeaponSoul.set_baselskill2(lbaseskilllv2);
	}
	
	//remainpoints=基礎總點數-技能項目
	//幼年期中階技能配點總數為6、少年期中階技能配點總數為7、成年期中階技能配點總數為8
	private void mporion(L1WeaponSoul l1WeaponSoul, int remainpoints, int hightskills){
		Random random = new Random();
		random.setSeed(random.nextInt(515));
		int mbaseskilllv1=1,mbaseskilllv2=1,mbaseskilllv3=1,mbaseskilllv4=1,phightskills=0;
		int premainpoints = remainpoints;
		int choice;
		boolean flv1=false,flv2=false,flv3=false,flv4=false,chk=false;
		while (!chk){
		while (premainpoints > 0){
			choice = random.nextInt(3)+1;
			switch(choice){
			case 1:
				if(mbaseskilllv1 < 3){
					mbaseskilllv1++;
					premainpoints--;
					if ((flv1==false) && (mbaseskilllv1 == 3)){
						phightskills++;
						flv1=true;
					}
				}
				break;
			case 2:
				if(mbaseskilllv2 < 3){
					mbaseskilllv2++;
					premainpoints--;
					if ((flv2==false) && (mbaseskilllv2 == 3)){
						phightskills++;
						flv2=true;
					}
				}
				break;
			case 3:
				if(mbaseskilllv3 < 3){
					mbaseskilllv3++;
					premainpoints--;
					if ((flv3==false) && (mbaseskilllv3 == 3)){
						phightskills++;
						flv3=true;
					}
				}		
				break;
			case 4:
				if(mbaseskilllv4 < 3){
					mbaseskilllv4++;
					premainpoints--;
					if ((flv4==false) && (mbaseskilllv4 == 3)){
						phightskills++;
						flv4=true;
					}				
				}
				break;
		}			
		}
		if (phightskills != hightskills){//不滿足條件就重跑
			flv1=false;flv2=false;flv3=false;flv4=false;
			mbaseskilllv1=1;mbaseskilllv2=1;mbaseskilllv3=1;mbaseskilllv4=1;phightskills=0;
			premainpoints = remainpoints;
		}
		else{
			chk=true;
		}
		}

		l1WeaponSoul.set_basemSkill1(mbaseskilllv1);
		l1WeaponSoul.set_basemSkill2(mbaseskilllv2);
		l1WeaponSoul.set_basemSkill3(mbaseskilllv3);
		l1WeaponSoul.set_basemSkill4(mbaseskilllv4);
	}

	//remainpoints=基礎總點數-技能項目
	//幼年期高階技能配點總數為6、少年期高階技能配點總數為7、成年期高階技能配點總數為8
	private void hporion(L1WeaponSoul l1WeaponSoul, int remainpoints, int hightskills){
		Random random = new Random();
		random.setSeed(random.nextInt(254));
		int hbaseskilllv1=1,hbaseskilllv2=1,hbaseskilllv3=1,hbaseskilllv4=1,phightskills=0;
		int premainpoints;
		int choice;
		boolean flv1=false,flv2=false,flv3=false,flv4=false,chk=false;
		premainpoints = remainpoints;
		while (!chk){
		while (premainpoints > 0){
			choice = random.nextInt(3)+1;
			switch(choice){
			case 1:
				if(hbaseskilllv1 < 3){
					hbaseskilllv1++;
					premainpoints--;
					if ((flv1==false) && (hbaseskilllv1 == 3)){
						phightskills++;
						flv1=true;
					}
				}
				break;
			case 2:
				if(hbaseskilllv2 < 3){
					hbaseskilllv2++;
					premainpoints--;
					if ((flv2==false) && (hbaseskilllv2 == 3)){
						phightskills++;
						flv2=true;
					}
				}
				break;
			case 3:
				if(hbaseskilllv3 < 3){
					hbaseskilllv3++;
					premainpoints--;
					if ((flv3==false) && (hbaseskilllv3 == 3)){
						phightskills++;
						flv3=true;
					}
				}
				break;
			case 4:
				if(hbaseskilllv4 < 3){
					hbaseskilllv4++;
					premainpoints--;
					if ((flv4==false) && (hbaseskilllv4 == 3)){
						phightskills++;
						flv4=true;
					}
				}
				break;
			}			
		}
		if (phightskills != hightskills){//不滿足條件就重跑
			flv1=false;flv2=false;flv3=false;flv4=false;
			hbaseskilllv1=1;hbaseskilllv2=1;hbaseskilllv3=1;hbaseskilllv4=1;phightskills=0;
			premainpoints = remainpoints;
		}
		else{
			chk=true;
		}
		}
		l1WeaponSoul.set_basehSkill1(hbaseskilllv1);
		l1WeaponSoul.set_basehSkill2(hbaseskilllv2);
		l1WeaponSoul.set_basehSkill3(hbaseskilllv3);
		l1WeaponSoul.set_basehSkill4(hbaseskilllv4);
	}
	
	public void updateOwner(L1WeaponSoul WeaponSoul, int objid) {
		Connection con = null;
		PreparedStatement pstm = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con
					.prepareStatement("UPDATE Weapon_Soul SET owner=? WHERE item_obj_id=?");
			pstm.setInt(1, objid);
			pstm.setInt(2, WeaponSoul.get_itemobjid());
			pstm.execute();
		} catch (Exception e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
	}
	
	public L1WeaponSoul getTemplate(int itemobjid) {
		return _WeaponSouls.get(new Integer(itemobjid));
	}

	public Map<Integer, L1WeaponSoul> getWeaponSoullList() {
		return _WeaponSouls;
	}
	
	public void deleteWeaponSoul(int itemobjid) {
		Connection con = null;
		PreparedStatement pstm = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("DELETE FROM Weapon_Soul WHERE item_obj_id=?");
			pstm.setInt(1, itemobjid);
			pstm.execute();
		} catch (SQLException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);

		} finally {
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
		_WeaponSouls.remove(itemobjid);
	}
	
	public void getStr1 (L1PcInstance pc, int itemobjid){
		L1WeaponSoul ws = new L1WeaponSoul();
		ws = WeaponSoulTable.getNewInstance().getTemplate(itemobjid);
		int lex = 0, mex = 0, hex = 0;
		String msg0 = "";
		String msg1 = "";
		String msg2 = "";
		String msg3 = "";
		String msg4 = "";
		String msg5 = "";
		String msg6 = "";
		msg0 = pc.getName();
		msg1 = Integer.toString(ws.get_level());
		msg1 +=  " "+"級";
		msg2 = Integer.toString(ws.get_exp());
		msg3 = Integer.toString(ws.get_remainlvPoint());
		if (ws.get_lSkill1() == 9) {
			lex++;
		}
		if (ws.get_lSkill2() == 9) {
			lex++;
		}
		if (ws.get_mSkill1() == 9) {
			mex++;
		}
		if (ws.get_mSkill2() == 9) {
			mex++;
		}
		if (ws.get_mSkill3() == 9) {
			mex++;
		}
		if (ws.get_mSkill4() == 9) {
			mex++;
		}
		if (ws.get_hSkill1() == 9) {
			hex++;
		}
		if (ws.get_hSkill2() == 9) {
			hex++;
		}
		if (ws.get_hSkill3() == 9) {
			hex++;
		}
		if (ws.get_hSkill4() == 9) {
			hex++;
		}
		msg4 = Integer.toString(lex);
		msg5 = Integer.toString(mex);
		msg6 = Integer.toString(hex);
		String msg[] = { msg0, msg1, msg2, msg3, msg4, msg5, msg6 };
		pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "ws_stat", msg));
	}
	
}