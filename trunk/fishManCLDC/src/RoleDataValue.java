/*���ɦѮv�G
*  		�n�x��Ǩt�B�h�C��C���]�p�t   �i�ث� �Ѯv
* 		��w��ޤj�Ǹ�T�u�{�t		    �i�ʷ� �б�
* 
*�����G�B�h�F     �t�d�u�@�G
*					�C���[�c�W��
*                   �����]�p
*                   ���N�]�p
*                   �t�ξ�X
* 
*�����G
*	   ����m     �t�d�u�@�G
*					�C�������{���]�p
*			   		����ݸ�Ʈw�]�p
*			   		��Ʈw�]�p
*			   		�{���t�ξ�X
*
*      ���v�o�B�L�q��     �t�d�u�@�G
* 					�a�ϵ{���]�p
*			   		�G�Ƭy�{�{���]�p
*		
*
*      ���¯�     �t�d�u�@�G
* 					���N�]�p
*                   �����]�p�غc
*                   �P�䲣�~�]�p
* 
*      �����R     �t�d�u�@�G
* 					���N�s�@
*                   �����s�@
*                   �P�䲣�~�]�p�s�@
* 
*      �B�v��     �t�d�u�@�G
* 					�C���y�{�]�p
*                   �C���G�Ƴ]�p
*                   NPC�]�p
*                   �𲤥��s�@
* 
*      ���շ�     �t�d�u�@�G
* 					��P�W��
*                   ���Ļs�@
*                   �C�������s�@����
*
*/
import javax.microedition.lcdui.Image;
import java.util.Hashtable;
import java.util.Enumeration;

public class RoleDataValue {
	public static String userID = "";//�ϥΪ�ID

	public static String username = "";//�ϥΪ̱b��

	public static String userpass = "";//�ϥΪ̱K�X

	public static String name = "";//�D���W��

	public static int level = 1;//����

	public static int fame = 0;//�W��
	
	public static String fameStr = "";//�W��

	public static int exp = 200;//�ثe�o�쪺�g���

	public static int expNext = 0;//�U���ɯŻݭn���g���

	public static int atk = 500;//�����O,�R��

	public static int def = 500;//���m�O,�j��

	public static int agi = 3;//�ӱ�,�j��

	public static int nowHP = 5000;//�ثeHP

	public static int nowXP = 0;//�ثeXP

	public static int maxHP = 5000;//�̤jHP

	public static int killMonster = 0;//�ثe���Ǽƶq

	public static int money = 9999999;//�ثe����

	public static int pole = 8;//�ثe�Z��

	public static int poleAtk = 0;//�ثe�Z�������O

	public static int proHead = 0;//�ثe����-�Y��

	public static int proBody = 0;//�ثe����-����

	public static int proWaist = 0;//�ثe����-�y��

	public static int proFoot = 0;//�ثe����-�}��

	public static int proHand = 0;//�ثe����-�ⳡ

	public static int jewelry = 0;//�ثe���~

	public static int nowItemDef = 0;//�ثe�Ҧ��˳ƨ��m

	public static int bait = 0;//�ثe����

	public static int baitAmount = 99;//�ثe���窺�ƶq

	public static int roleX = 299;//�ثe�H���y�Э�x

	public static int roleY = 267;//�ثe�H���y�Э�y

	public static int roleMap = 0;//�ثe�H���a�Ͻs��

	public static String[][] roleItem = { { "P1", "1" }, { "T1", "2" },
			{ "E1", "1" }, { "G1", "1" }, { "P2", "1" }, { "P3", "1" },
			{ "P4", "1" }, { "B5", "10" }, { "B6", "50" }, { "G4", "1" },
			{ "G14", "1" }, { "G7", "1" }, { "G12", "1" }, { "G16", "1" },
			{ "T7", "3" }, { "J8", "1" }, { "P29", "1" } };//�D���Ҿ֦��ثe�����~

	//roleItem[�s��][���~]
	//ex:roleItem[0][1] = {"�s��","�W��","�ƶq"};
	public static Image roleImg = null;

	public static int comboValue = 0;//�ثecombo�ެq��

	private static DataBaseReader db = null;

	public static int nowCombo = 0;//�ثe��combo��

	public static String nowComboMethod = "";

	public static String nowComboName = "";//�ثecombo�ު��W��

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

	//�ƥ�O�_����
	public static String[] evenFinished = new String[23];

	//�ƥ�i��
	public static String[] nowEvenSchedule = new String[23];
	
	//combo�ެO�_�i�H�ϥ�,�O�_���f�t�A������
	public static boolean comboState = false;

	//�Z��,����-�Y��,����-����,����-�y��,����-�}��,����-�ⳡ,���~,����,����
	//private String itemData = "1,0,0,0,0,0,0,0,1";
	
	//��Ϥ�
	public RoleDataValue() {
		try {
			db = new DataBaseReader();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	//�n�J���\�D����ƪ�Ϥ�
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
	
	//�D���ɯ�,���ܥثe���A
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

	//�n�J���\��qServer�^�Ǫ��D�����
	public void getFirstRoleStateForInternet(String request) {
		tmp = split(request, "|");
		//tmp[0]���^��OK
		//System.out.println(request);
		tmp_ = split(tmp[2], "/");//��r��(byte)�ഫ��UTF
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
		setAllItemNum(tmp[14]);//�]�w�˳ƽs��
		money = tansInt(tmp[15]);
		roleX = tansInt(tmp[16]);
		roleY = tansInt(tmp[17]);
		roleMap = tansInt(tmp[18]);
		//�a�ϵ��ɪ�l
		for(int i=0;i<4;i++){
			tmp_ = split(tmp[20+i], ",");
			for(int j=0;j<68;j++)
				map.dBstop[i][j] = tmp_[j];
		}
		//�_�c��l��
		tmp_ = split(tmp[24], ",");
		for(int i=0;i<68;i++){
			map.gem[i] = tmp_[i];
		}
		//�ƥ�O�_������l
		tmp_ = split(tmp[25], ",");
		for(int i=0;i<23;i++)
			RoleDataValue.evenFinished[i] = tmp_[i];
		//�ƥ�i��
		tmp_ = split(tmp[26], ":");
		for(int i=0;i<23;i++){
			System.out.println(tmp_[i]);
			RoleDataValue.nowEvenSchedule[i] = tmp_[i];
		}
		addHash(tmp[19]);
	}
	
	//�]�w�W���
	public void setFame(int va){
		switch(va){
			case 0:
				fameStr = "��Ǫ�";
				break;
			case 1:
				fameStr = "�@�몺";
				break;
			case 2:
				fameStr = "�V�O��";
				break;
			case 3:
				fameStr = "���q��";
				break;
			case 4:
				fameStr = "���O��";
				break;
			case 5:
				fameStr = "�M�~��";
				break;
			case 6:
				fameStr = "�վǪ�";
				break;
			case 7:
				fameStr = "���W��";
				break;
			case 8:
				fameStr = "������";
				break;
			case 9:
				fameStr = "�}�_��";
				break;
			case 10:
				fameStr = "�F���_��";
				break;
			case 11:
				fameStr = "�ǻ���";
				break;
			case 12:
				fameStr = "�L�Ī�";
				break;
			case 13:
				fameStr = "��ù��";
				break;
			case 14:
				fameStr = "�F�H";
				break;
		}
	}

	//�⪫�~��ƥ[�J��Hash
	public void addHash(String str) {
		tmp = split(str, ",");
		//System.out.println(str);
		for (int i = 0; i < tmp.length; i++) {
			tmp_ = split(tmp[i], "-");
			itemHash.put(tmp_[0], tmp_);
		}
	}

	//�]�wHash Enumeration
	public void setHashEnume() {
		enu = itemHash.elements();
	}

	//��D�����~�g�Jdb
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

	//����item�O�_���bHash�̭�
	public boolean testItem(String str) {
		tmp = (String[]) itemHash.get(str);
		if (tmp == null)
			return true;
		return false;
	}

	//�[�J���~��Hash
	public void addItem(String str, boolean buy,int sum) {
		tmp = null;
		if (testItem(str)) {//�j�MHash�O�_��item
			tmp_ = new String[2];
			tmp_[0] = str;
			tmp_[1] = String.valueOf(sum);
			if (buy) {
				if (!setMoney(tmp_[0], true))//true���R�ɴ�֪���
					itemHash.put(tmp_[0], tmp_);//�s�W���~
			} else{
				itemHash.put(tmp_[0], tmp_);//�s�W���~
			}
		}else {//Hash�̭�����M��item�N��item+1
			tmp = null;
			tmp = (String[]) itemHash.get(str);
			int value = tansInt(tmp[1]) + sum;
			
			if (value != 100) {				
				if (buy){
					if (!setMoney(tmp[0], true)){//true���R�ɴ�֪���
						tmp[1] = String.valueOf(value);
						removeItem(tmp[0]);//�R�����
						itemHash.put(tmp[0], tmp);//�s�W���~
					}
				}else{
					tmp[1] = String.valueOf(value);
					removeItem(tmp[0]);//�R�����
					itemHash.put(tmp[0], tmp);
				}
			}
		}
	}

	//����ثe���~�ƶq,�S�������~�^��0
	public String getItemAmount(String str) {
		tmp = (String[]) itemHash.get(str);
		if (tmp == null)
			return "0";
		return tmp[1];
	}
	
	//��s�D����ƪZ���˳�,���p���氣���~�Υ�󪫫~��
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
		
		RoleDataValue.setAtkDefValue();//���s�]�watk�Pdef
		setNowComboValue();//�]�wcombo��
	}

	//�ϥΪ��~
	public void useItem(String tmp_, boolean sell) {
		tmp = (String[]) itemHash.get(tmp_);
		int value = tansInt(tmp[1]) - 1;
		if (value == 0) {//���~�ϥΧ�
			if (sell)
				setMoney(tmp_, false);//false����ɼW�[����
			removeItem(tmp[0]);//�R�����
			updateRoleDataValue(tmp[0]);
		} else {
			tmp[1] = String.valueOf(value);
			if (sell)
				setMoney(tmp_, false);//false����ɼW�[����
			removeItem(tmp[0]);//�R�����
			itemHash.put(tmp[0], tmp);
		}
	}

	//���~�n�R���,�p�⪫�~����
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

	//�R�����~,�R���bHash�̪����
	public void removeItem(String str) {
		itemHash.remove(str);
	}
	
	//�R�����~,sum���R���ƶq
	public void deleteItemNum(String str,int sum){
		tmp = (String[]) itemHash.get(str);
		int value = tansInt(tmp[1]) - sum;
		if(value<=0)
			itemHash.remove(str);
		else{
			tmp[1] = String.valueOf(value);
			removeItem(tmp[0]);//�R�����
			itemHash.put(tmp[0], tmp);
		}
	}

	//����`atk��
	public static int getAtkMaxValue() {
		return ((99 + 4) * 99) / 5;
	}

	//����`def��
	public static int getDefMaxValue() {
		return ((99 + 4) * 99) / 15 + 1;
	}

	//�إ߹Ϥ�
	public static Image createImage(String pathImg) {
		Image img = null;
		try {
			img = Image.createImage(pathImg);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return img;
	}

	//�Ǧ^�ثe�`def
	public static int getNowDef() {
		return def + nowItemDef;
	}

	//�Ǧ^�ثe�`atk
	public static int getNowAtk() {
		return atk + poleAtk;
	}

	//�]�w�˳ƽs��
	public void setAllItemNum(String str) {
		tmp_ = split(str, ",");
		pole = tansInt(tmp_[0]);//�ثe�Z��
		proHead = tansInt(tmp_[1]);//�ثe����-�Y��
		proBody = tansInt(tmp_[2]);//�ثe����-����
		proWaist = tansInt(tmp_[3]);//�ثe����-�y��
		proFoot = tansInt(tmp_[4]);//�ثe����-�}��
		proHand = tansInt(tmp_[5]);//�ثe����-�ⳡ
		jewelry = tansInt(tmp_[6]);//�ثe���~
		bait = tansInt(tmp_[7]);//�ثe����
		//level = tansInt(tmp_[8]);//�ثe����
	}

	//�]�w�ثe�˳ƨ��m&����&�����Ϥ�
	public static void setAtkDefValue() {
		nowItemDef = 0;
		poleAtk = 0;
		if (pole != 0) {
			nowItemDef += tansInt(db.readDBField(8, pole, 5));//����
			poleAtk += tansInt(db.readDBField(8, pole, 3));//�����
			poleAtk += tansInt(db.readDBField(8, pole, 6));//�����
			poleImg = createImage("/image/Pole/" + db.readDBField(8, pole, 1)
					+ ".png");
		}

		if (proHead != 0) {
			nowItemDef += tansInt(db.readDBField(5, proHead, 3));//�Y����
			nowItemDef += tansInt(db.readDBField(5, proHead, 4));//�Y����
			poleAtk += tansInt(db.readDBField(5, proHead, 5));//�Y����
			proHeadImg = createImage("/image/Protect/"
					+ db.readDBField(5, proHead, 1) + ".png");
		}

		if (proBody != 0) {
			nowItemDef += tansInt(db.readDBField(5, proBody, 3));//���騾
			nowItemDef += tansInt(db.readDBField(5, proBody, 4));//���騾
			poleAtk += tansInt(db.readDBField(5, proBody, 5));//�����
			proBodyImg = createImage("/image/Protect/"
					+ db.readDBField(5, proBody, 1) + ".png");
		}

		if (proWaist != 0) {
			nowItemDef += tansInt(db.readDBField(5, proWaist, 3));//�y����
			nowItemDef += tansInt(db.readDBField(5, proWaist, 4));//�y����
			poleAtk += tansInt(db.readDBField(5, proWaist, 5));//�y����
			proWaistImg = createImage("/image/Protect/"
					+ db.readDBField(5, proWaist, 1) + ".png");
		}

		if (proFoot != 0) {
			nowItemDef += tansInt(db.readDBField(5, proFoot, 3));//�}����
			nowItemDef += tansInt(db.readDBField(5, proFoot, 4));//�}����
			poleAtk += tansInt(db.readDBField(5, proFoot, 5));//�}����
			proFootImg = createImage("/image/Protect/"
					+ db.readDBField(5, proFoot, 1) + ".png");
		}

		if (proHand != 0) {
			nowItemDef += tansInt(db.readDBField(5, proHand, 3));//�ⳡ��
			nowItemDef += tansInt(db.readDBField(5, proHand, 4));//�ⳡ��
			poleAtk += tansInt(db.readDBField(5, proHand, 5));//�ⳡ��
			proHandImg = createImage("/image/Protect/"
					+ db.readDBField(5, proHand, 1) + ".png");
		}
		if (jewelry != 0) {
			nowItemDef += tansInt(db.readDBField(2, jewelry, 5));//���~��
			nowItemDef += tansInt(db.readDBField(2, jewelry, 6));//���~��
			poleAtk += tansInt(db.readDBField(2, jewelry, 3));//���~��
			poleAtk += tansInt(db.readDBField(2, jewelry, 7));//���~��
			jewelryImg = createImage("/image/Jewelry/"
					+ db.readDBField(2, jewelry, 1) + ".png");
		}
		if (bait != 0) {
			baitImg = createImage("/image/Bait/" + db.readDBField(9, bait, 1)
					+ ".png");
		}
	}

	//�]�w�ثe�����ƻPcombo�ު����k
	public void setNowComboValue() {
		try{
			if(pole!=0){
				tmp = db.readDBRecord(8, pole);
				nowCombo = tansInt(tmp[8].substring(1, tmp[8].length()));//COMBO��
				if (nowCombo != 0) {
					String tmp_[] = db.readDBRecord(1, nowCombo);
					nowComboMethod = tmp_[2];
					nowComboName = tmp_[1];
					comboValue = tansInt(tmp_[3]);
					if(RoleDataValue.bait==tansInt(tmp[7].substring(1,tmp[7].length()))){
						comboState = true;
						GameMain.music.soundCom();//�]�wcombo����
					}
					else
						comboState = false;
					System.out.println(nowComboMethod+"|"+comboState);
				}
			}
		}catch(Exception e){e.printStackTrace();}
	}

	//���item����,�ഫ���e�XServer���榡
	public static String getItemStr() {
		String str = "";
		enu = itemHash.elements();
		for (; enu.hasMoreElements();) {
			tmp = (String[]) enu.nextElement();
			str += tmp[0] + "-" + tmp[1] + ",";
		}
		return str.substring(0, str.length() - 1);
	}

	//����ثe�˳ƽs��
	public static String getEquipment() {
		//�Z��,����-�Y��,����-����,����-�y��,����-�}��,����-�ⳡ,���~,����
		return pole + "," + proHead + "," + proBody + "," + proWaist + ","
				+ proFoot + "," + proHand + "," + jewelry + "," + bait;
	}

	//�r�����
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

	//�ഫ�����
	public static int tansInt(String data) {
		return Integer.parseInt(data);
	}
}