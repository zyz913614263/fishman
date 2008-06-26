/*指導老師：
*  		南台資傳系、多媒體遊戲設計系   張華城 老師
* 		樹德科技大學資訊工程系		    張晉源 教授
* 
*隊長：劉士達     負責工作：
*					遊戲架構規劃
*                   場景設計
*                   美術設計
*                   系統整合
* 
*隊員：
*	   鍾易峻     負責工作：
*					遊戲介面程式設計
*			   		手機端資料庫設計
*			   		資料庫設計
*			   		程式系統整合
*
*      郭宗穎、林義翔     負責工作：
* 					地圖程式設計
*			   		故事流程程式設計
*		
*
*      黃肅純     負責工作：
* 					美術設計
*                   網路設計建構
*                   周邊產品設計
* 
*      蔡佳靜     負責工作：
* 					美術製作
*                   場景製作
*                   周邊產品設計製作
* 
*      劉宗學     負責工作：
* 					遊戲流程設計
*                   遊戲故事設計
*                   NPC設計
*                   攻略本製作
* 
*      莊耿瑜     負責工作：
* 					行銷規劃
*                   音效製作
*                   遊戲場景製作除錯
*
*/
import javax.microedition.lcdui.Image;
import java.util.Hashtable;
import java.util.Enumeration;

public class RoleDataValue {
	public static String userID = "";//使用者ID

	public static String username = "";//使用者帳號

	public static String userpass = "";//使用者密碼

	public static String name = "";//主角名稱

	public static int level = 1;//等級

	public static int fame = 0;//名望
	
	public static String fameStr = "";//名望

	public static int exp = 200;//目前得到的經驗值

	public static int expNext = 0;//下次升級需要的經驗值

	public static int atk = 500;//攻擊力,命中

	public static int def = 500;//防禦力,迴避

	public static int agi = 3;//敏捷,迴避

	public static int nowHP = 5000;//目前HP

	public static int nowXP = 0;//目前XP

	public static int maxHP = 5000;//最大HP

	public static int killMonster = 0;//目前殺怪數量

	public static int money = 9999999;//目前金錢

	public static int pole = 8;//目前武器

	public static int poleAtk = 0;//目前武器攻擊力

	public static int proHead = 0;//目前防具-頭部

	public static int proBody = 0;//目前防具-身體

	public static int proWaist = 0;//目前防具-腰部

	public static int proFoot = 0;//目前防具-腳部

	public static int proHand = 0;//目前防具-手部

	public static int jewelry = 0;//目前飾品

	public static int nowItemDef = 0;//目前所有裝備防禦

	public static int bait = 0;//目前釣餌

	public static int baitAmount = 99;//目前釣餌的數量

	public static int roleX = 299;//目前人物座標值x

	public static int roleY = 267;//目前人物座標值y

	public static int roleMap = 0;//目前人物地圖編號

	public static String[][] roleItem = { { "P1", "1" }, { "T1", "2" },
			{ "E1", "1" }, { "G1", "1" }, { "P2", "1" }, { "P3", "1" },
			{ "P4", "1" }, { "B5", "10" }, { "B6", "50" }, { "G4", "1" },
			{ "G14", "1" }, { "G7", "1" }, { "G12", "1" }, { "G16", "1" },
			{ "T7", "3" }, { "J8", "1" }, { "P29", "1" } };//主角所擁有目前的物品

	//roleItem[編號][物品]
	//ex:roleItem[0][1] = {"編號","名稱","數量"};
	public static Image roleImg = null;

	public static int comboValue = 0;//目前combo技段數

	private static DataBaseReader db = null;

	public static int nowCombo = 0;//目前的combo技

	public static String nowComboMethod = "";

	public static String nowComboName = "";//目前combo技的名稱

	public static Image poleImg = null;

	public static Image baitImg = null;

	public static Image proHeadImg = null;

	public static Image proBodyImg = null;

	public static Image proWaistImg = null;

	public static Image proFootImg = null;

	public static Image proHandImg = null;

	public static Image jewelryImg = null;

	public static Hashtable itemHash = new Hashtable();

	public static Enumeration enu = null;

	private static String tmp[];
	private static String tmp_[] = null ;

	private static byte bytetmp[] = null;

	//事件是否完成
	public static String[] evenFinished = new String[23];

	//事件進度
	public static String[] nowEvenSchedule = new String[23];
	
	//combo技是否可以使用,是否有搭配適當的釣具
	public static boolean comboState = false;

	//武器,防具-頭部,防具-身體,防具-腰部,防具-腳部,防具-手部,飾品,釣餌,等級
	//private String itemData = "1,0,0,0,0,0,0,0,1";
	
	//初使化
	public RoleDataValue() {
		try {
			db = new DataBaseReader();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	//登入成功主角資料初使化
	public void loginSuccessStart() {
		try {
			maxHP = 150 * level * 3 / 5;
			
			if (maxHP < 150)
				maxHP = 150;
			//nowHP = maxHP;

			expNext = Integer.parseInt(db.readDBField(3, level + 1, 0));
			atk = ((level + 4) * level) / 5;
			def = ((level + 4) * level) / 15 + 1;
			
			if(level<=5){
				atk+=10;
				def+=10;
			}

			roleImg = Image
					.createImage("/image/gameInterface/npc/rolePeople.png");
			System.out.println("Img New");
			setNowComboValue();
			setAtkDefValue();
			System.out.println("Now ATK:" + getNowAtk() + " Now DEF"
					+ getNowDef());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//主角升級,改變目前狀態
	public static void roleUpLevel(){
		maxHP = 150 * level * 3 / 5;
		if (maxHP < 150)
			maxHP = 150;
		nowHP = maxHP;

		expNext = Integer.parseInt(db.readDBField(3, level + 1, 0));
		atk = ((level + 4) * level) / 5;
		def = ((level + 4) * level) / 15 + 1;
		setAtkDefValue();
	}

	//登入成功後從Server回傳的主角資料
	public void getFirstRoleStateForInternet(String request) {
		tmp = split(request, "|");
		//tmp[0]為回傳OK
		//System.out.println(request);
		tmp_ = split(tmp[2], "/");//把字串(byte)轉換為UTF
		bytetmp = new byte[tmp_.length];
		for (int i = 0; i < tmp_.length; i++) {
			//System.out.println(tmp_[i]);
			bytetmp[i] = Byte.parseByte(tmp_[i]);
		}
		name = db.tanStringToByte(bytetmp);
		//System.out.println("name:"+name);
		fame = tansInt(tmp[3]);
		setFame(fame);
		level = tansInt(tmp[4]);
		exp = tansInt(tmp[5]);
		nowHP = tansInt(tmp[10]);
		killMonster = tansInt(tmp[13]);
		setAllItemNum(tmp[14]);//設定裝備編號
		money = tansInt(tmp[15]);
		roleX = tansInt(tmp[16]);
		roleY = tansInt(tmp[17]);
		roleMap = tansInt(tmp[18]);
		//地圖結界初始
		for(int i=0;i<4;i++){
			tmp_ = split(tmp[20+i], ",");
			for(int j=0;j<68;j++)
				map.dBstop[i][j] = tmp_[j];
		}
		//寶箱初始化
		tmp_ = split(tmp[24], ",");
		for(int i=0;i<68;i++){
			map.gem[i] = tmp_[i];
		}
		//事件是否完成初始
		tmp_ = split(tmp[25], ",");
		for(int i=0;i<23;i++)
			RoleDataValue.evenFinished[i] = tmp_[i];
		//事件進度
		tmp_ = split(tmp[26], ":");
		for(int i=0;i<23;i++){
			System.out.println(tmp_[i]);
			RoleDataValue.nowEvenSchedule[i] = tmp_[i];
		}
		addHash(tmp[19]);
	}
	
	//設定名望值
	public void setFame(int va){
		switch(va){
			case 0:
				fameStr = "初學的";
				break;
			case 1:
				fameStr = "一般的";
				break;
			case 2:
				fameStr = "努力的";
				break;
			case 3:
				fameStr = "普通的";
				break;
			case 4:
				fameStr = "活力的";
				break;
			case 5:
				fameStr = "專業的";
				break;
			case 6:
				fameStr = "博學的";
				break;
			case 7:
				fameStr = "有名的";
				break;
			case 8:
				fameStr = "高等的";
				break;
			case 9:
				fameStr = "稀奇的";
				break;
			case 10:
				fameStr = "了不起的";
				break;
			case 11:
				fameStr = "傳說的";
				break;
			case 12:
				fameStr = "無敵的";
				break;
			case 13:
				fameStr = "修羅的";
				break;
			case 14:
				fameStr = "達人";
				break;
		}
	}

	//把物品資料加入到Hash
	public void addHash(String str) {
		tmp = split(str, ",");
		//System.out.println(str);
		for (int i = 0; i < tmp.length; i++) {
			tmp_ = split(tmp[i], "-");
			itemHash.put(tmp_[0], tmp_);
		}
	}

	//設定Hash Enumeration
	public void setHashEnume() {
		enu = itemHash.elements();
	}

	//把主角物品寫入db
	public void writeRoleItemToDB() {
		db.clearRoleItemDB();
		enu = itemHash.elements();
		String item_[] = null;
		for (; enu.hasMoreElements();) {
			item_ = (String[]) enu.nextElement();
			db.writeRoleItem(item_);
		}
		db.closeDB();
	}

	//測試item是否有在Hash裡面
	public boolean testItem(String str) {
		tmp = (String[]) itemHash.get(str);
		if (tmp == null)
			return true;
		return false;
	}

	//加入物品到Hash
	public void addItem(String str, boolean buy,int sum) {
		tmp = null;
		if (testItem(str)) {//搜尋Hash是否有item
			tmp_ = new String[2];
			tmp_[0] = str;
			tmp_[1] = String.valueOf(sum);
			if (buy) {
				if (!setMoney(tmp_[0], true))//true為買時減少金錢
					itemHash.put(tmp_[0], tmp_);//新增物品
			} else{
				itemHash.put(tmp_[0], tmp_);//新增物品
			}
		}else {//Hash裡面有找尋到item就把item+1
			tmp = null;
			tmp = (String[]) itemHash.get(str);
			int value = tansInt(tmp[1]) + sum;
			
			if (value != 100) {				
				if (buy){
					if (!setMoney(tmp[0], true)){//true為買時減少金錢
						tmp[1] = String.valueOf(value);
						removeItem(tmp[0]);//刪除資料
						itemHash.put(tmp[0], tmp);//新增物品
					}
				}else{
					tmp[1] = String.valueOf(value);
					removeItem(tmp[0]);//刪除資料
					itemHash.put(tmp[0], tmp);
				}
			}
		}
	}

	//抓取目前物品數量,沒有此物品回傳0
	public String getItemAmount(String str) {
		tmp = (String[]) itemHash.get(str);
		if (tmp == null)
			return "0";
		return tmp[1];
	}
	
	//更新主角資料武器裝備,假如有賣除物品或丟棄物品時
	public void updateRoleDataValue(String itemStr){
		if(itemStr.indexOf("P")!=-1){
			RoleDataValue.pole = 0;
		}else if(itemStr.indexOf("J")!=-1){
			RoleDataValue.jewelry = 0;
		}else if(itemStr.indexOf("G")!=-1){
			itemStr = db.readDBField( 5,tansInt( itemStr.substring(1,itemStr.length()) ),8 );
			if(itemStr.equals("1"))
				RoleDataValue.proHead = 0;
			else if(itemStr.equals("2"))
				RoleDataValue.proBody = 0;
			else if(itemStr.equals("3"))
				RoleDataValue.proWaist = 0;
			else if(itemStr.equals("4"))
				RoleDataValue.proFoot = 0;
			else if(itemStr.equals("5"))
				RoleDataValue.proHand = 0;
		}else if(itemStr.indexOf("B")!=-1){
			RoleDataValue.bait = 0;
		}
		
		RoleDataValue.setAtkDefValue();//重新設定atk與def
		setNowComboValue();//設定combo技
	}

	//使用物品
	public void useItem(String tmp_, boolean sell) {
		tmp = (String[]) itemHash.get(tmp_);
		int value = tansInt(tmp[1]) - 1;
		if (value == 0) {//物品使用完
			if (sell)
				setMoney(tmp_, false);//false為賣時增加金錢
			removeItem(tmp[0]);//刪除資料
			updateRoleDataValue(tmp[0]);
		} else {
			tmp[1] = String.valueOf(value);
			if (sell)
				setMoney(tmp_, false);//false為賣時增加金錢
			removeItem(tmp[0]);//刪除資料
			itemHash.put(tmp[0], tmp);
		}
	}

	//當物品要買賣時,計算物品價格
	public boolean setMoney(String str, boolean kind) {
		String tmp_2 = "";
		if (str.indexOf("I") != -1) {
			tmp_2 = db.readDBField(0, tansInt(str.substring(1, str.length())),
					5);
		} else if (str.indexOf("J") != -1) {
			tmp_2 = db.readDBField(2, tansInt(str.substring(1, str.length())),
					9);
		} else if (str.indexOf("G") != -1) {
			tmp_2 = db.readDBField(5, tansInt(str.substring(1, str.length())),
					7);
		} else if (str.indexOf("P") != -1) {
			tmp_2 = db.readDBField(8, tansInt(str.substring(1, str.length())),
					9);
		} else if (str.indexOf("B") != -1) {
			tmp_2 = db.readDBField(9, tansInt(str.substring(1, str.length())),
					5);
		} else if (str.indexOf("T") != -1) {
			tmp_2 = db.readDBField(10, tansInt(str.substring(1, str.length())),
					5);
		}
		if (kind) {
			if ((money - tansInt(tmp_2)) >= 0) {
				money -= tansInt(tmp_2);
				return false;
			}
		} else
			money += tansInt(tmp_2) / 2;

		return true;
	}

	//刪除物品,刪除在Hash裡的資料
	public void removeItem(String str) {
		itemHash.remove(str);
	}
	
	//刪除物品,sum為刪除數量
	public void deleteItemNum(String str,int sum){
		tmp = (String[]) itemHash.get(str);
		int value = tansInt(tmp[1]) - sum;
		if(value<=0)
			itemHash.remove(str);
		else{
			tmp[1] = String.valueOf(value);
			removeItem(tmp[0]);//刪除資料
			itemHash.put(tmp[0], tmp);
		}
	}

	//抓取總atk值
	public static int getAtkMaxValue() {
		return ((99 + 4) * 99) / 5;
	}

	//抓取總def值
	public static int getDefMaxValue() {
		return ((99 + 4) * 99) / 15 + 1;
	}

	//建立圖片
	public static Image createImage(String pathImg) {
		Image img = null;
		try {
			img = Image.createImage(pathImg);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return img;
	}

	//傳回目前總def
	public static int getNowDef() {
		return def + nowItemDef;
	}

	//傳回目前總atk
	public static int getNowAtk() {
		return atk + poleAtk;
	}

	//設定裝備編號
	public void setAllItemNum(String str) {
		tmp_ = split(str, ",");
		pole = tansInt(tmp_[0]);//目前武器
		proHead = tansInt(tmp_[1]);//目前防具-頭部
		proBody = tansInt(tmp_[2]);//目前防具-身體
		proWaist = tansInt(tmp_[3]);//目前防具-腰部
		proFoot = tansInt(tmp_[4]);//目前防具-腳部
		proHand = tansInt(tmp_[5]);//目前防具-手部
		jewelry = tansInt(tmp_[6]);//目前飾品
		bait = tansInt(tmp_[7]);//目前釣餌
		//level = tansInt(tmp_[8]);//目前等級
	}

	//設定目前裝備防禦&攻擊&對應圖片
	public static void setAtkDefValue() {
		nowItemDef = 0;
		poleAtk = 0;
		if (pole != 0) {
			nowItemDef += tansInt(db.readDBField(8, pole, 5));//釣竿防
			poleAtk += tansInt(db.readDBField(8, pole, 3));//釣竿攻
			poleAtk += tansInt(db.readDBField(8, pole, 6));//釣竿攻
			poleImg = createImage("/image/Pole/" + db.readDBField(8, pole, 1)
					+ ".png");
		}

		if (proHead != 0) {
			nowItemDef += tansInt(db.readDBField(5, proHead, 3));//頭部防
			nowItemDef += tansInt(db.readDBField(5, proHead, 4));//頭部防
			poleAtk += tansInt(db.readDBField(5, proHead, 5));//頭部攻
			proHeadImg = createImage("/image/Protect/"
					+ db.readDBField(5, proHead, 1) + ".png");
		}

		if (proBody != 0) {
			nowItemDef += tansInt(db.readDBField(5, proBody, 3));//身體防
			nowItemDef += tansInt(db.readDBField(5, proBody, 4));//身體防
			poleAtk += tansInt(db.readDBField(5, proBody, 5));//身體攻
			proBodyImg = createImage("/image/Protect/"
					+ db.readDBField(5, proBody, 1) + ".png");
		}

		if (proWaist != 0) {
			nowItemDef += tansInt(db.readDBField(5, proWaist, 3));//腰部防
			nowItemDef += tansInt(db.readDBField(5, proWaist, 4));//腰部防
			poleAtk += tansInt(db.readDBField(5, proWaist, 5));//腰部攻
			proWaistImg = createImage("/image/Protect/"
					+ db.readDBField(5, proWaist, 1) + ".png");
		}

		if (proFoot != 0) {
			nowItemDef += tansInt(db.readDBField(5, proFoot, 3));//腳部防
			nowItemDef += tansInt(db.readDBField(5, proFoot, 4));//腳部防
			poleAtk += tansInt(db.readDBField(5, proFoot, 5));//腳部攻
			proFootImg = createImage("/image/Protect/"
					+ db.readDBField(5, proFoot, 1) + ".png");
		}

		if (proHand != 0) {
			nowItemDef += tansInt(db.readDBField(5, proHand, 3));//手部防
			nowItemDef += tansInt(db.readDBField(5, proHand, 4));//手部防
			poleAtk += tansInt(db.readDBField(5, proHand, 5));//手部攻
			proHandImg = createImage("/image/Protect/"
					+ db.readDBField(5, proHand, 1) + ".png");
		}
		if (jewelry != 0) {
			nowItemDef += tansInt(db.readDBField(2, jewelry, 5));//飾品防
			nowItemDef += tansInt(db.readDBField(2, jewelry, 6));//飾品防
			poleAtk += tansInt(db.readDBField(2, jewelry, 3));//飾品攻
			poleAtk += tansInt(db.readDBField(2, jewelry, 7));//飾品攻
			jewelryImg = createImage("/image/Jewelry/"
					+ db.readDBField(2, jewelry, 1) + ".png");
		}
		if (bait != 0) {
			baitImg = createImage("/image/Bait/" + db.readDBField(9, bait, 1)
					+ ".png");
		}
	}

	//設定目前釣竿資料與combo技的按法
	public void setNowComboValue() {
		try{
			if(pole!=0){
				tmp = db.readDBRecord(8, pole);
				nowCombo = tansInt(tmp[8].substring(1, tmp[8].length()));//COMBO技
				if (nowCombo != 0) {
					String tmp_[] = db.readDBRecord(1, nowCombo);
					nowComboMethod = tmp_[2];
					nowComboName = tmp_[1];
					comboValue = tansInt(tmp_[3]);
					if(RoleDataValue.bait==tansInt(tmp[7].substring(1,tmp[7].length()))){
						comboState = true;
						GameMain.music.soundCom();//設定combo音效
					}
					else
						comboState = false;
					System.out.println(nowComboMethod+"|"+comboState);
				}
			}
		}catch(Exception e){e.printStackTrace();}
	}

	//抓取item的值,轉換為送出Server的格式
	public static String getItemStr() {
		String str = "";
		enu = itemHash.elements();
		for (; enu.hasMoreElements();) {
			tmp = (String[]) enu.nextElement();
			str += tmp[0] + "-" + tmp[1] + ",";
		}
		return str.substring(0, str.length() - 1);
	}

	//抓取目前裝備編號
	public static String getEquipment() {
		//武器,防具-頭部,防具-身體,防具-腰部,防具-腳部,防具-手部,飾品,釣餌
		return pole + "," + proHead + "," + proBody + "," + proWaist + ","
				+ proFoot + "," + proHand + "," + jewelry + "," + bait;
	}

	//字串切割
	public String[] split(String str, String regex) {
		int count = 0;
		int index = 0;

		for (int i = 0; i < str.length(); i++) {
			if (str.indexOf(regex, index) != -1) {
				count++;
				index = str.indexOf(regex, index) + 1;
			}
		}
		if (str.length() != index)
			count++;
		index = 0;
		String strsplit[] = new String[count];
		int indexarray[] = new int[count];
		for (int i = 0; i < count; i++) {
			if (str.indexOf(regex, index) != -1) {
				index = str.indexOf(regex, index) + 1;
				indexarray[i] = index;
			}
		}

		index = 0;
		for (int i = 0; i < count - 1; i++) {
			strsplit[i] = str.substring(index, indexarray[i] - 1);
			index = indexarray[i];
		}
		strsplit[count - 1] = str.substring(index, str.length());

		return strsplit;
	}

	//轉換為整數
	public static int tansInt(String data) {
		return Integer.parseInt(data);
	}
}