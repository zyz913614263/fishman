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
*      郭宗穎     負責工作：
* 					地圖程式設計
*			   		故事流程程式設計
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
import java.util.Random;
import java.lang.Math;

public class MonsterDataValue {
	private static DataBaseReader dataR = new DataBaseReader();

	public static String name = "";//怪物名稱

	public static int level = 0;//怪物等級

	public static int exp = 0;//怪物經驗值

	public static int atk = 0;//怪物攻擊力

	public static int def = 0;//怪物防禦力

	public static int nowHP = 0;//目前HP

	public static int maxHP = 0;//最大HP

	public static int nowXP = 0;//目前XP

	public static int money = 0;//掉落金錢

	public static int atb = 100;

	public static String[][] item = null;

	public static Image battleBackgroundImg = null;

	public static Image monsterImg = null;

	private static Random monsterRandom = null;

	private static int monsterRandomValue = 0;

	public static int monsterID = 0;
	
	public static int monsterKind = 0;

	public MonsterDataValue() {
		monsterRandom = new Random();
	}
	
	//直接指定怪物
	public void createMonster(String monster){
		try {
			String monsterDataTmp[] = dataR.readDBRecord(7, Integer.parseInt(monster.substring(1,monster.length())));
			if(monster.equals("M47") || monster.equals("M48")){
				battleBackgroundImg = Image.createImage("/image/Battle/Battle_013.png");//戰鬥畫面背景
			}else if(monster.equals("M49")){
				battleBackgroundImg = Image.createImage("/image/Battle/Battle_003.png");//戰鬥畫面背景
			}else if(monster.equals("M50")){
				battleBackgroundImg = Image.createImage("/image/Battle/Battle_009.png");//戰鬥畫面背景
			}
			
			monsterImg = Image.createImage("/image/Monster/"
					+ monsterDataTmp[1] + ".png");//怪物
			monsterID = Integer.parseInt(monster.substring(1,monster.length()));
			name = monsterDataTmp[2];
			level = Integer.parseInt(monsterDataTmp[3]);
			monsterKind = Integer.parseInt(monsterDataTmp[4]);
			exp = Integer.parseInt(monsterDataTmp[6]);
			atk = Integer.parseInt(monsterDataTmp[7]);
			def = Integer.parseInt(monsterDataTmp[8]);
			nowHP = Integer.parseInt(monsterDataTmp[5]);
			maxHP = Integer.parseInt(monsterDataTmp[5]);
			money = Integer.parseInt(monsterDataTmp[9]);
			setAtbValue();
			//try{
			setItem(split(monsterDataTmp[10], ","));//設定掉落物品
			GameMain.music.soundMonster();//設定怪物音效
			//}catch(Exception e){e.printStackTrace();}
			//System.out.println(monsterRandomValue);
		} catch (Exception e) {e.printStackTrace();}
	}
	
	//亂數產生怪物
	public void createMonster(int mapNum) {
		try {
			String maptmp[] = dataR.readDBRecord(4, mapNum);
			String monsterRandTmp[] = split(maptmp[3], ",");
			monsterRandomValue = (Math.abs(monsterRandom.nextInt()))
					% monsterRandTmp.length;
			monsterID = Integer.parseInt(monsterRandTmp[monsterRandomValue]);
			String monsterDataTmp[] = dataR.readDBRecord(7, monsterID);
			battleBackgroundImg = Image.createImage("/image/Battle/"
					+ maptmp[1] + ".png");//戰鬥畫面背景
			monsterImg = Image.createImage("/image/Monster/"
					+ monsterDataTmp[1] + ".png");//怪物

			name = monsterDataTmp[2];
			level = Integer.parseInt(monsterDataTmp[3]);
			monsterKind = Integer.parseInt(monsterDataTmp[4]);
			exp = Integer.parseInt(monsterDataTmp[6]);
			atk = Integer.parseInt(monsterDataTmp[7]);
			def = Integer.parseInt(monsterDataTmp[8]);
			nowHP = Integer.parseInt(monsterDataTmp[5]);
			maxHP = Integer.parseInt(monsterDataTmp[5]);
			money = Integer.parseInt(monsterDataTmp[9]);
			setAtbValue();
			//try{
			setItem(split(monsterDataTmp[10], ","));//設定掉落物品
			GameMain.music.soundMonster();//設定怪物音效
			//}catch(Exception e){e.printStackTrace();}
			//System.out.println(monsterRandomValue);
		} catch (Exception e) {}
	}

	//設定掉落物品
	public void setItem(String itemTmp[]) {
		//計算會掉落幾個物品
		int arraySize = itemTmp.length;
		if (arraySize == 0)
			arraySize = 1;
		else if (arraySize > 4)
			arraySize = 4;
		String[] item_ = new String[arraySize];
		System.out.println(arraySize + "|" + monsterID);
		for (int i = 0; i < arraySize; i++)
			item_[i] = "";
		int n = 0;
		int ranV = 0;
		boolean tt = false;
		//把怪物會掉落的物品放進buff裡~不重覆
		while (true) {
			ranV = (Math.abs(monsterRandom.nextInt())) % itemTmp.length;
			for (int k = 0; k < arraySize; k++) {
				if (item_[k].equals(itemTmp[ranV])) {
					tt = true;
					break;
				} else
					tt = false;
			}
			if (tt)
				continue;
			item_[n] = itemTmp[ranV];
			n++;
			if (n == arraySize)
				break;
		}
		//物品掉落機率
		for (int i = 0; i < item_.length; i++) {
			if (item_[i].indexOf("I") != -1 || item_[i].indexOf("B") != -1
					|| item_[i].indexOf("T") != -1) {
				if (!((Math.abs(monsterRandom.nextInt())) % 2 == 0))
					item_[i] = "";
			}
			if (item_[i].indexOf("J") != -1 || item_[i].indexOf("G") != -1
					|| item_[i].indexOf("P") != -1) {
				if (!((Math.abs(monsterRandom.nextInt())) % 15 == 2))
					item_[i] = "";
			}
		}
		arraySize = 0;
		for (int i = 0; i < item_.length; i++)
			if (!item_[i].equals(""))
				arraySize++;

		try {
			item = new String[arraySize][];
			for (int i = 0, k = 0; i < item_.length; i++)
				if (!item_[i].equals("")) {
					item[k] = dataR.getNumData(item_[i]);
					k++;
				}
		} catch (Exception e) {
			e.printStackTrace();
		}
		/*
		 * for(int i=0;i <arraySize;i++) System.out.println(item[i]);
		 */
	}

	public String[] getMonsterCon() {
		String arrayTmp[] = null;
		try{
			int n = (Math.abs(monsterRandom.nextInt())) % 5 + 1;
			System.out.println(monsterID);
			String tmp = dataR.readDBField(13, monsterID, n);
			int line = tmp.length()/14;
			if(tmp.length()%14>0)
				line++;
			arrayTmp = new String[line];
			
			if(line>1){
				arrayTmp[0] = tmp.substring(0,14);
				arrayTmp[1] = tmp.substring(14,tmp.length());
			}else
				arrayTmp[0] = tmp.substring(0,tmp.length());
		}catch(Exception e){e.printStackTrace();}
		
		return arrayTmp;
	}

	//設定atb表
	public void setAtbValue() {
		if (level <= 30)
			atb = 6500;
		else if (level >= 31 || level <= 70)
			atb = 5000;
		else if (level >= 71 || level <= 100)
			atb = 4500;
		else if (level >= 101 || level <= 140)
			atb = 3500;
		else if (level >= 141 || level <= 165)
			atb = 3000;
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
}