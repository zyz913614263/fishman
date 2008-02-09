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
import javax.microedition.lcdui.*;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import javax.microedition.lcdui.game.*;

public class BattleFrame extends GameCanvas implements Runnable{

	private static int disWidth = 0;//LayManager實際位置

	private static int disHeight = 0;//LayManager實際位置
	
	private static int menustate = 0;//目前選取到的選項
	
	private static int itemSelect = 0;
	private static int storeIndex=0;//物品索引
	private static int itemTotal = 0;
	private static int itemMenuSelect = 0;

	private static int menuNum = 4;//共有幾個選項

	private static String[] menuStr = { "一般攻擊", " 必殺技 ", "物品使用", "  逃跑"};
	private static String[] itemUseStr = {"使用","取消","離開"};
	private static String[] itemNameArray = {"Item","","Jewelry","","Battle","Protect","	Event",
			"Monster","Pole","Bait","Tonic","Talk",""};
	private static int dataFieldNum[] = {6,4,11,1,4,10,4,11,12,6,7,4,9};//每一資料庫欄位數
	private static String itemBuff[][] = new String[30][];
	
	private static DataBaseReader db = new DataBaseReader();

	private Graphics g = null;
	//private static Image monsterImg = null;
	//private static Image battleFrame = null;
	private static Image battleMenu = createImage("/image/gameInterface/BattleMenu.png");//戰鬥選單
	private static Image BattleFrameDown = createImage("/image/gameInterface/BattleFrameDown.png");//
	private static Image battleConver = createImage("/image/gameInterface/BattleFrameUp.png");//對話方塊
	private static Image comboDis = createImage("/image/gameInterface/combo.png");//COMBO技顯示方塊
	private static Image battleWin = createImage("/image/gameInterface/BattleWin.png");//戰鬥勝利選單
	private static Image battleItem = createImage("/image/gameInterface/BattleItem.png");//戰鬥使用物品
	private static Image battleItemMenu = createImage("/image/gameInterface/buyMenu.png");//戰鬥使用物品選單
	private static Image battlePeople = createImage("/image/gameInterface/battlePeople.png");//戰鬥中主角
	private static Image atkImg = createImage("/image/gameInterface/atk.png");//一般攻擊圖片
	private static Image monsterDied = createImage("/image/gameInterface/monsterDied.png");//怪物死亡
	private static Image roleDied = createImage("/image/gameInterface/gameover.png");//怪物死亡
	private static Image comboImg = null;//combo技
	private static Image monsterAtk = createImage("/image/gameInterface/monsterAtk.png");//怪物被攻擊
	private static Image titleImg = null;
	private static Image img2[] = null;
	
	private static boolean drawItemMenu = false;//是否為要畫物品
	private static boolean storeEvent = false;//是否有使用物品
	private static boolean itemEvent = true;//true時表示顯示物品在清單中
	private static boolean itemBuy = false;//當為false表示還沒確定要使用,true表示確定要使用
	private static boolean atbFill = false;//ATB是否滿了
	private static boolean xpbFill = false;//XPB是否滿了
	public static boolean battleFinish = false;//戰鬥是否結束
	public static boolean battleVictory = false;//戰鬥勝利或失敗
	private static boolean useCombo = false;
	private static boolean init = false;
	private static int storeNum = 0;//
	private static int atbValue = 0;//ATB增加的值
	private static int xpbValue = 0;//XPB增加的值
	private static int atbSpeed = 100;//ATB的速度
	
	private static MonsterDataValue monsterData = new MonsterDataValue();
	private static Timer atbTimer = new Timer();
	private static Timer xpbTimer = new Timer();
	private static Timer comboTimer = new Timer();
	
	private static int page = 0;//頁數
	
	private static Random atkRandom = null;
	private static String comboBuff = "";
	
	private static RoleDataValue roleData  = null;
	
	private static Thread monsterThread = null;
	
	private static mapCanvars mapca = null;
	
	private static Image img = null;
	
	private static String itemAmount[] = new String[99];
	

	public BattleFrame(/*int map, */RoleDataValue roleData_,mapCanvars mapca) {
		super(false);
		setFullScreenMode(true);
		atkRandom = new Random();
		BattleFrame.mapca = mapca;
		roleData = roleData_;
		monsterThread = new Thread(this);
		monsterThread.start();
	}
	
	public void battleStart(int map,String monster){
		GameMain.music.musicPause();
		GameMain.music.battleMusicStart();
		disWidth = (getWidth() - 176) / 2;//抓取螢幕的中心點
		disHeight = (getHeight() - 208) / 2;//抓取螢幕的中心點
		try{
			if(monster.equals(""))
				monsterData.createMonster(map);
			else
				monsterData.createMonster(monster);
			
			page=0;
			itemSelect = 0;
			storeIndex=0;//物品索引
			itemTotal = 0;
			itemMenuSelect = 0;
			init = false;
			battleFinish = true;
			drawDisplay();
			changPage();
		}catch(Exception e){e.printStackTrace();}
	}

	//把圖片畫上
	public void drawDisplay() {
		g = getGraphics();
		if(page==0 ||page==1){//戰鬥開始,主角atb未滿時
			g.drawImage(MonsterDataValue.battleBackgroundImg,disWidth+0,disHeight+48,0);
			if(!init){
				g.drawImage(battleConver,disWidth+0,disHeight+0,0);
				monsterConvs();
				init=true;
			}
			if(atbFill==false)
				g.drawImage(BattleFrameDown,disWidth+0,disHeight+144,0);
			g.drawImage(MonsterDataValue.monsterImg,disWidth+105,disHeight+70,0);
			g.drawImage(battlePeople,disWidth+12,disHeight+90,0);
			if(RoleDataValue.pole!=0)
				g.drawImage(RoleDataValue.poleImg,disWidth+70,disHeight+168,0);
			if(RoleDataValue.bait!=0){
				g.drawImage(RoleDataValue.baitImg,disWidth+134,disHeight+168,0);
				g.drawString(String.valueOf(roleData.getItemAmount("B"+RoleDataValue.bait)),disWidth+111,disHeight+187,0);
			}
			if(page==1)//當atb滿時,可以行動時
				g.drawImage(battleMenu,disWidth+65,disHeight+150,0);
			showHp();
			showMonsterHp();
		}
		/*if(page==1){//當atb滿時,可以行動時
			g.drawImage(battleMenu,disWidth+65,disHeight+150,0);
		}*/
		if(page==2){//當combo技可以用時
			g.drawImage(comboDis,disWidth+52,disHeight+72,0);
			g.drawString(RoleDataValue.nowComboName,disWidth+57,disHeight+80,0);
		}
		if(page==3){//當要使用物品時
			g.drawImage(battleItem,disWidth+3,disHeight+0,0);
			g.drawImage(battleItemMenu,disWidth+121,disHeight+77,0);
			titleImg = createImage("/image/gameInterface/text4.png");
			g.drawImage(titleImg,disWidth+117,disHeight+8,0);
			titleImg = createImage("/image/gameInterface/text3.png");
			g.drawImage(titleImg,disWidth+117,disHeight+42,0);
		}
		if(!battleFinish & battleVictory){//戰鬥勝利
			g.drawImage(battleWin,disWidth+0,disHeight+48,0);
			showWinInf();
		}else if(!battleFinish & !battleVictory){
			battleLose();
		}
		
		g.setColor(255,255,0);
		g.fillRect(disWidth + 7,disHeight + 170,xpbValue,10);
		flushGraphics();
		changeMenu();
	}
	
	//逃跑
	public void RunAway(){
		GameMain.music.msuicSound(7);
		GameMain.music.battleMusicStop();
		GameMain.music.musicRun();
		atbTimer.cancel();
		atbTimer=null;
		atbTimer = new Timer();
		atbValue=0;
		xpbTimer.cancel();
		xpbTimer=null;
		xpbTimer = new Timer();
		atbFill = false;
		xpbFill = false;
		xpbValue=0;
		comboTimer.cancel();
		comboTimer = null;
		storeEvent = false;
		comboTimer = new Timer();
		useCombo = false;
		battleFinish = false;
		battleVictory = false;
		mapca.threadStart();
	}
	
	//顯示戰鬥勝利後的資訊
	public void showWinInf(){
		System.out.println("win");
		battleVictory = true;
		String[][] item = MonsterDataValue.item;
		setFont(0);
		setColor(false);
		if(item.length==0){
			g.drawString("無掉落物品",disWidth + 68,disHeight + 80 ,0);
			RoleDataValue.money+=MonsterDataValue.money;
		}else{
			GameMain.music.msuicSound(5);
			for(int i=0,k=0;i<item.length;i++,k+=15){
				g.drawString(item[i][2],disWidth + 68,disHeight + 80 + k,0);
				roleData.addItem(item[i][0],false,1);
			}
			RoleDataValue.money+=MonsterDataValue.money;
		}
		setFont(0);
		g.drawString("金錢取得",disWidth + 11,disHeight + 80,0);
		g.drawString("$"+String.valueOf(MonsterDataValue.money),disWidth + 11,disHeight + 94,0);
		
		g.drawString("經驗取得",disWidth + 11,disHeight + 108,0);
		g.drawString(String.valueOf(MonsterDataValue.exp),disWidth + 11,disHeight + 122,0);
		
		if(RoleDataValue.exp+MonsterDataValue.exp>=RoleDataValue.expNext){//升級
			GameMain.music.msuicSound(4);
			RoleDataValue.exp = (RoleDataValue.exp+MonsterDataValue.exp) - RoleDataValue.expNext;
			RoleDataValue.level++;
			RoleDataValue.roleUpLevel();
			g.drawImage(battleConver,disWidth+0,disHeight+0,0);
			g.drawString("[升級]",disWidth+5,disHeight+4,0);
			g.drawString(" Level " + (RoleDataValue.level-1) + "->Level " + RoleDataValue.level,disWidth+5,disHeight+17,0);
		}else
			RoleDataValue.exp+=MonsterDataValue.exp;
		
		RoleDataValue.killMonster++;
		checkFame();
		flushGraphics();
		ConnectionServer.updateGameInf();
		try{
			Thread.sleep(3000);
		}catch(Exception e){}
		
		GameMain.music.battleMusicStop();
		GameMain.music.musicRun();
		mapca.threadStart();
	}
	
	//戰鬥失敗
	public void battleLose(){
		GameMain.music.battleMusicStop();
		mapca.jumpWord(0,299,267);
		mapca.threadStart();
	}
	
	//判斷名望是否上升
	public void checkFame(){
		int value = RoleDataValue.killMonster;
		if(value==100 || value==250 || value==500 || value==1000 || value==2000 
				|| value==3000 || value==5000 || value==8000 || value==10000 || value==14000 || value==20000 
				|| value==25000 || value==32000 || value==50000){
			RoleDataValue.fame++;
			roleData.setFame(RoleDataValue.fame);
		}
	}

	//建立圖片
	public static Image createImage(String pathImg) {
		img = null;
		try {
			img = Image.createImage(pathImg);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return img;
	}

	//改變頁面時，初使化
	public void changPage() {
		switch (page) {
		case 0://戰鬥開始時
			if(battleFinish){
				if(atbFill==false)
					atbTimer.schedule(new AtbTimerTask(),0,atbSpeed);
				if(xpbFill==false)
					xpbTimer.schedule(new XpbTimerTask(),0,500);
			}
			break;
		case 1://ATB表滿,出現戰鬥選單
			menustate = 0;
			page=1;
			break;
		case 2://當combo技可以用時
			comboTimer.schedule(new ComboTimerTask(),RoleDataValue.comboValue*700,10000);
			break;
		case 3://使用物品
			if(!drawItemMenu){
				itemSelect=0;
				storeIndex=0;
				int n=0;
				String tmp[] = null;
				storeNum = 10;
				//搜尋主角物品
				roleData.setHashEnume();
				for(;RoleDataValue.enu.hasMoreElements();){
					tmp = (String[])RoleDataValue.enu.nextElement();
					//如果編號大於要搜尋資料庫的數量則不搜尋
					if(tmp[0].indexOf("T")==-1)
						continue;
					//System.out.println("Find:"+tmp[0]);
					itemBuff[n] = db.readDBRecord(storeNum,
							Integer.parseInt( tmp[0].substring(1,tmp[0].length()) ));
					itemAmount[n] = tmp[1];
					n++;
				}
				itemSelect=0;
				storeIndex=0;
				itemTotal = n;//搜尋到符合的物品數量
				if(itemTotal==0){
					page=1;
					changPage();
					storeEvent = false;
				}else{
					img2 = new Image[itemTotal];
					for(int i=0;i<itemTotal;i++){
						img2[i] = createImage("/image/" + itemNameArray[storeNum] +"/"+ itemBuff[i][1] + ".png");
					}
					//System.out.println("Total:"+itemTotal+"|"+page);
					drawItemMenu = true;
				}
			}
			break;
		case 4:
			break;
		}
		drawDisplay();
	}

	//選單切換
	private void changeMenu() {
		if(page==0){//戰鬥開始
			
		}
		else if(page==1){//戰鬥選單
			setFont(0);
			switch (menustate) {
				case 0:
					//Select----------------------------
					setColor(true);
					g.drawString(menuStr[0], disWidth + 72, disHeight + 159, 0);
					//----------------------------------
					setColor(false);
					g.drawString(menuStr[1], disWidth + 124, disHeight + 159, 0);
					g.drawString(menuStr[2], disWidth + 72, disHeight + 180, 0);
					g.drawString(menuStr[3], disWidth + 124, disHeight + 180, 0);
					break;
				case 1:
					setColor(false);
					g.drawString(menuStr[0], disWidth + 72, disHeight + 159, 0);
					g.drawString(menuStr[1], disWidth + 124, disHeight + 159, 0);
					//Select----------------------------
					setColor(true);
					g.drawString(menuStr[2], disWidth + 72, disHeight + 180, 0);
					//----------------------------------
					g.setColor(69, 118, 181);
					g.drawString(menuStr[3], disWidth + 124, disHeight + 180, 0);
					break;
				case 2:
					setColor(false);
					g.drawString(menuStr[0], disWidth + 72, disHeight + 159, 0);
					//Select----------------------------
					setColor(true);
					g.drawString(menuStr[1], disWidth + 124, disHeight + 159, 0);
					//----------------------------------
					setColor(false);
					g.drawString(menuStr[2], disWidth + 72, disHeight + 180, 0);
					g.drawString(menuStr[3], disWidth + 124, disHeight + 180, 0);
					break;
				case 3:
					setColor(false);
					g.drawString(menuStr[0], disWidth + 72, disHeight + 159, 0);
					g.drawString(menuStr[1], disWidth + 124, disHeight + 159, 0);
					g.drawString(menuStr[2], disWidth + 72, disHeight + 180, 0);
					//Select----------------------------
					setColor(true);
					g.drawString(menuStr[3], disWidth + 124, disHeight + 180, 0);
					//----------------------------------
					break;
				}
		}
		else if(page==3){//物品使用
			if(drawItemMenu){//畫物品資訊在itemMenu
				setFont(0);
				g.drawImage(battleItem,disWidth+3,disHeight+0,0);
				titleImg = createImage("/image/gameInterface/text4.png");
				g.drawImage(titleImg,disWidth+117,disHeight+8,0);
				titleImg = createImage("/image/gameInterface/text3.png");
				g.drawImage(titleImg,disWidth+117,disHeight+42,0);
				switch (itemSelect) {
					case 0:
						//Select----------------------------
						setColor(true);
						g.drawString(itemBuff[0+storeIndex][2], disWidth + 50, disHeight + 15, 0);
						g.drawImage(img2[0+storeIndex],disWidth+10,disHeight+6,0);
						g.drawString("HP+"+itemBuff[0+storeIndex][3],disWidth + 123,disHeight + 28,0);
						g.drawString("    "+itemAmount[0+storeIndex],disWidth + 123,disHeight + 57,0);
						//----------------------------------
						setColor(false);
						if(storeIndex+2<=itemTotal){//當只剩二個物品在選單時印出
							g.drawString(itemBuff[1+storeIndex][2], disWidth + 50, disHeight + 48, 0);
							g.drawImage(img2[1+storeIndex],disWidth+10,disHeight+39,0);
						}
						if(storeIndex+3<=itemTotal){//當只剩三個物品在選單時印出
							g.drawString(itemBuff[2+storeIndex][2], disWidth + 50, disHeight + 81, 0);
							g.drawImage(img2[2+storeIndex],disWidth+10,disHeight+72,0);
						}
						if(storeIndex+4<=itemTotal){//四個物品在選單時印出
							g.drawString(itemBuff[3+storeIndex][2], disWidth + 50, disHeight + 114, 0);
							g.drawImage(img2[3+storeIndex],disWidth+10,disHeight+105,0);
						}
						break;
					case 1:
						setColor(false);
						g.drawString(itemBuff[0+storeIndex][2], disWidth + 50, disHeight + 15, 0);
						g.drawImage(img2[0+storeIndex],disWidth+10,disHeight+6,0);
						//Select----------------------------
						setColor(true);
						if(storeIndex+2<=itemTotal){
							g.drawString(itemBuff[1+storeIndex][2],disWidth + 50, disHeight + 48, 0);
							g.drawImage(img2[1+storeIndex],disWidth+10,disHeight+39,0);
							g.drawString("HP+"+itemBuff[1+storeIndex][3],disWidth + 123,disHeight + 28,0);
							g.drawString("    "+itemAmount[1+storeIndex],disWidth + 123,disHeight + 57,0);
						}
						//----------------------------------
						setColor(false);
						if(storeIndex+3<=itemTotal){
							g.drawString(itemBuff[2+storeIndex][2], disWidth + 50, disHeight + 81, 0);
							g.drawImage(img2[2+storeIndex],disWidth+10,disHeight+72,0);
						}
						if(storeIndex+4<=itemTotal){
							g.drawString(itemBuff[3+storeIndex][2], disWidth + 50, disHeight + 114, 0);
							g.drawImage(img2[3+storeIndex],disWidth+10,disHeight+105,0);
						}
						break;
					case 2:
						setColor(false);
						g.drawString(itemBuff[0+storeIndex][2], disWidth + 50, disHeight + 15, 0);
						g.drawImage(img2[0+storeIndex],disWidth+10,disHeight+6,0);
						setColor(false);
						if(storeIndex+2<=itemTotal){
							g.drawString(itemBuff[1+storeIndex][2], disWidth + 50, disHeight + 48, 0);
							g.drawImage(img2[1+storeIndex],disWidth+10,disHeight+39,0);
						}
						//Select----------------------------
						setColor(true);
						if(storeIndex+3<=itemTotal){
							g.drawString(itemBuff[2+storeIndex][2], disWidth + 50, disHeight + 81, 0);
							g.drawImage(img2[2+storeIndex],disWidth+10,disHeight+72,0);
							g.drawString("HP+"+itemBuff[2+storeIndex][3],disWidth + 123,disHeight + 28,0);
							g.drawString("    "+itemAmount[2+storeIndex],disWidth + 123,disHeight + 57,0);
						}
						//----------------------------------
						setColor(false);
						if(storeIndex+4<=itemTotal){
							g.drawString(itemBuff[3+storeIndex][2], disWidth + 50, disHeight + 114, 0);
							g.drawImage(img2[3+storeIndex],disWidth+10,disHeight+105,0);
						}
						break;
					case 3:
						setColor(false);
						g.drawString(itemBuff[0+storeIndex][2], disWidth + 50, disHeight + 15, 0);
						g.drawImage(img2[0+storeIndex],disWidth+10,disHeight+6,0);
						if(storeIndex+2<=itemTotal){
							g.drawString(itemBuff[1+storeIndex][2], disWidth + 50, disHeight + 48, 0);
							g.drawImage(img2[1+storeIndex],disWidth+10,disHeight+39,0);
						}
						if(storeIndex+3<=itemTotal){
							g.drawString(itemBuff[2+storeIndex][2], disWidth + 50, disHeight + 81, 0);
							g.drawImage(img2[2+storeIndex],disWidth+10,disHeight+72,0);
						}
						//Select----------------------------
						setColor(true);
						if(storeIndex+4<=itemTotal){
							g.drawString(itemBuff[3+storeIndex][2], disWidth + 50, disHeight + 114, 0);
							g.drawImage(img2[3+storeIndex],disWidth+10,disHeight+105,0);
							g.drawString("HP+"+itemBuff[3+storeIndex][3],disWidth + 123,disHeight + 28,0);
							g.drawString("    "+itemAmount[3+storeIndex],disWidth + 123,disHeight + 57,0);
						}
						//----------------------------------
						break;
					}
				}
				if(storeEvent==true && itemBuy==true){
					switch (itemMenuSelect) {
						case 0:
							//Select----------------------------
							setColor(true);
							g.drawString(itemUseStr[0], disWidth + 130, disHeight + 85, 0);
							//----------------------------------
							setColor(false);
							g.drawString(itemUseStr[1], disWidth + 130, disHeight + 100, 0);
							g.drawString(itemUseStr[2], disWidth + 130, disHeight + 115, 0);
							break;
						case 1:
							setColor(false);
							g.drawString(itemUseStr[0], disWidth + 130, disHeight + 85, 0);
							//Select----------------------------
							setColor(true);
							g.drawString(itemUseStr[1], disWidth + 130, disHeight + 100, 0);
							//----------------------------------
							setColor(false);
							g.drawString(itemUseStr[2], disWidth + 130, disHeight + 115, 0);
							break;
						case 2:
							setColor(false);
							g.drawString(itemUseStr[0], disWidth + 130, disHeight + 85, 0);
							g.drawString(itemUseStr[1], disWidth + 130, disHeight + 100, 0);
							//Select----------------------------
							setColor(true);
							g.drawString(itemUseStr[2], disWidth + 130, disHeight + 115, 0);
							//----------------------------------
							break;
					}
				}
		}
		flushGraphics();
	}

	//字型設定
	public void setFont(int size) {
		Font font = Font.getFont(Font.FACE_SYSTEM, 0, size);
		g.setFont(font);
	}
	//顏色設定
	public void setColor(boolean value){
		if(value)
			g.setColor(0, 0, 0);
		else
			g.setColor(69, 118, 181);
	}
	
	//設定怪物對話
	public void monsterConvs(){
		//怪物對話
		String talkTmp[] = monsterData.getMonsterCon();
		g.drawImage(battleConver,disWidth+0,disHeight+0,0);
		g.setColor(0,0,128);
		g.drawString(MonsterDataValue.name+" 說:",disWidth + 5,disHeight + 4 ,0);
		for(int i=0,k=13;i<talkTmp.length;i++,k+=13)
			g.drawString(talkTmp[i],disWidth + 5,disHeight + 4 + k ,0);
		//System.out.println("TalkL"+talkTmp.length);
	}
	
	//怪物攻擊畫面
	public void monsterAttackFrame(){
		monsterConvs();//顯示怪物對話
		//攻擊
		for(int i=0;i<3;i++){
			g.setColor(247,72,89);
			g.fillRect(disWidth+0,disHeight+48,176,96);
			flushGraphics();
			try{
				Thread.sleep(50);
			}catch(Exception e){}
			g.drawImage(MonsterDataValue.battleBackgroundImg,disWidth+0,disHeight+48,0);
			g.drawImage(battlePeople,disWidth+12,disHeight+90,0);
			g.drawImage(MonsterDataValue.monsterImg,disWidth+105,disHeight+70,0);
			flushGraphics();
		}
		showMonsterHp();
		//怪物攻擊傷害值
		//主角傷害值為怪物atk*(4~8)/10-主角防禦
		int atbRanValue = (MonsterDataValue.atk*(((Math.abs(atkRandom.nextInt()))%5)+4))/10;
		if(atbRanValue-RoleDataValue.getNowDef()>=0){
			RoleDataValue.nowHP -= atbRanValue-RoleDataValue.getNowDef();
			System.out.println("Role loss:"+( atbRanValue-RoleDataValue.getNowDef() ));
		}
		else{
			RoleDataValue.nowHP -= (MonsterDataValue.atk/10+10);
			System.out.println("Role loss-1:"+(MonsterDataValue.atk/10+10) );
		}
		if(RoleDataValue.nowHP<=0){
			RoleDataValue.nowHP = 0;
			showHp();
			battleFinish = false;
			battleVictory = false;
			try{
				Thread.sleep(600);
			}catch(Exception e){}
			
			roleDied();
			//page=4;
			//主角死亡
		}else{
			showHp();
		}
	}
	
	private LayerManager lm = new LayerManager();
	private Sprite sprite = null; 
	//主角攻擊畫面
	public void roleAttackFrame(){
		System.out.println("atk");
		GameMain.music.msuicSound(2);
		g.drawImage(MonsterDataValue.battleBackgroundImg,disWidth+0,disHeight+48,0);
		g.drawImage(battlePeople,disWidth+12,disHeight+90,0);
		g.drawImage(MonsterDataValue.monsterImg,disWidth+105,disHeight+70,0);
		flushGraphics();
		//LayerManager lm = new LayerManager();
		sprite = new Sprite(atkImg,98,51);
		lm.append(sprite);
		for(int i=0;i<sprite.getRawFrameCount();i++){//一般攻擊圖
			lm.paint(g,disWidth+50,disHeight+70);
			flushGraphics();
			try{
				Thread.sleep(50);
			}catch(Exception e){}
			sprite.nextFrame();
		}
		lm.remove(sprite);
		//atkSprite = null;
		sprite = new Sprite(monsterAtk,176,96);
		lm.append(sprite);
		for(int i=0;i<sprite.getRawFrameCount();i++){//怪物被攻擊
			lm.paint(g,disWidth+0,disHeight+48);
			flushGraphics();
			try{
				Thread.sleep(50);
			}catch(Exception e){}
			sprite.nextFrame();
		}
		GameMain.music.msuicSound(3);
		//lm = null;
		lm.remove(sprite);
		//怪物傷害值為主角atk*(4~8)/10-怪物防禦--------
		int atbRanValue = (RoleDataValue.getNowAtk()*(((Math.abs(atkRandom.nextInt()))%5)+4))/10;
		if((atbRanValue-MonsterDataValue.def>=0)){
			MonsterDataValue.nowHP -= atbRanValue-MonsterDataValue.def;
			System.out.println("Monster loss:" + (atbRanValue-MonsterDataValue.def) );
		}
		else{
			MonsterDataValue.nowHP -= (RoleDataValue.getNowAtk()/10+10);
			System.out.println("Monster loss-1:" + (RoleDataValue.getNowAtk()/10+10) );
		}
		if(MonsterDataValue.nowHP<=0){
			MonsterDataValue.nowHP=0;
			showMonsterHp();
			g.drawImage(MonsterDataValue.battleBackgroundImg,disWidth+0,disHeight+48,0);
			g.drawImage(MonsterDataValue.monsterImg,disWidth+105,disHeight+70,0);
			g.drawImage(battlePeople,disWidth+12,disHeight+90,0);
			flushGraphics();
			battleFinish = false;
			try{
				Thread.sleep(600);
			}catch(Exception e){}
			GameMain.music.msuicSound(6);
			monsterDied();
			battleVictory = true;
			page=4;
			//怪物死亡
		}else{
			showMonsterHp();
		}
		//-------------------------------------
		//如果combo技輸入錯誤atb與xpb重新開始
		if(useCombo){
			atbTimer.cancel();
			atbTimer=null;
			atbTimer = new Timer();
			atbValue=0;
			xpbTimer.cancel();
			xpbTimer=null;
			xpbTimer = new Timer();
			atbFill = false;
			xpbFill = false;
			xpbValue=0;
			comboTimer.cancel();
			comboTimer = null;
			storeEvent = false;
			comboTimer = new Timer();
			useCombo = false;
			comboBuff = "";
			page=0;
			changPage();
		}
	}
	
	//怪物死亡畫面
	public void monsterDied(){
		sprite = new Sprite(monsterDied,176,96);
		lm.append(sprite);
		for(int i=0;i<sprite.getRawFrameCount();i++){
			lm.paint(g,disWidth+0,disHeight+48);
			flushGraphics();
			try{
				Thread.sleep(150);
			}catch(Exception e){}
			sprite.nextFrame();
		}
		lm.remove(sprite);
	}
	
	//主角死亡畫面
	public void roleDied(){
		GameMain.music.battleMusicStart();
		sprite = new Sprite(roleDied,176,96);
		lm.append(sprite);
		for(int i=0;i<sprite.getRawFrameCount();i++){
			lm.paint(g,disWidth+0,disHeight+48);
			flushGraphics();
			try{
				Thread.sleep(250);
			}catch(Exception e){}
			sprite.nextFrame();
		}
		
		atbTimer.cancel();
		atbTimer=null;
		atbTimer = new Timer();
		atbValue=0;
		xpbTimer.cancel();
		xpbTimer=null;
		xpbTimer = new Timer();
		atbFill = false;
		xpbFill = false;
		xpbValue=0;
		
		try{
			Thread.sleep(600);
		}catch(Exception e){}
		
		lm.remove(sprite);
		battleLose();
	}
	
	//顯示怪物血在手機畫面上
	public void showMonsterHp(){
		g.setColor(234,234,0);
		g.drawRect(disWidth + 167,disHeight + 49,6,41);
		g.setColor(19,102,215);
		g.fillRect(disWidth + 168,disHeight + 50+(40-countMonsterHp()),5,countMonsterHp());
		flushGraphics();
	}
	
	//計算怪物血量
	public int countMonsterHp(){
		int poHp = 0;
		int maxHp = MonsterDataValue.maxHP;
		int nowHp = MonsterDataValue.nowHP;
		poHp = ((nowHp*10)/10*400)/(maxHp*10);
		//System.out.println("HP"+poHp);
		return poHp;
	}
	
	//combo技攻擊
	public void comboAttack(){
		g.drawImage(MonsterDataValue.battleBackgroundImg,disWidth+0,disHeight+48,0);
		g.drawImage(battlePeople,disWidth+12,disHeight+90,0);
		g.drawImage(MonsterDataValue.monsterImg,disWidth+105,disHeight+70,0);
		flushGraphics();
		//LayerManager lm = new LayerManager();
		Sprite combo = new Sprite(comboImg,176,96);
		lm.append(combo);
		for(int i=0;i<combo.getRawFrameCount();i++){
			lm.paint(g,disWidth+0,disHeight+48);
			flushGraphics();
			try{
				Thread.sleep(100);
			}catch(Exception e){}
			combo.nextFrame();
		}
		lm.remove(combo);
		//怪物傷害值為主角atk*(8~16)/10*段數-怪物防禦
		int atbRanValue = (RoleDataValue.getNowAtk()*(((Math.abs(atkRandom.nextInt()))%8)+8))/10;
		System.out.println("atk:"+atbRanValue);
		if((atbRanValue-MonsterDataValue.def>=0)){
			MonsterDataValue.nowHP -= atbRanValue*RoleDataValue.comboValue-MonsterDataValue.def;
			System.out.println("Monster loss:" + (atbRanValue*RoleDataValue.comboValue-MonsterDataValue.def) );
		}
		else{
			MonsterDataValue.nowHP -= RoleDataValue.getNowAtk()*RoleDataValue.comboValue/10;
			System.out.println("Monster loss:" + (RoleDataValue.getNowAtk()*RoleDataValue.comboValue/10) );
		}
		if(MonsterDataValue.nowHP<=0){
			MonsterDataValue.nowHP=0;
			showMonsterHp();
			g.drawImage(MonsterDataValue.battleBackgroundImg,disWidth+0,disHeight+48,0);
			g.drawImage(MonsterDataValue.monsterImg,disWidth+105,disHeight+70,0);
			g.drawImage(battlePeople,disWidth+12,disHeight+90,0);
			flushGraphics();
			battleFinish = false;
			try{
				Thread.sleep(600);
			}catch(Exception e){}
			monsterDied();
			battleVictory = true;
			//page=4;
			//怪物死亡
		}else{
			showMonsterHp();
		}
		//lm = null;
		atbTimer.cancel();
		atbTimer=null;
		atbTimer = new Timer();
		atbValue=0;
		xpbTimer.cancel();
		xpbTimer=null;
		xpbTimer = new Timer();
		atbFill = false;
		xpbFill = false;
		xpbValue=0;
		comboTimer.cancel();
		comboTimer = null;
		storeEvent = false;
		comboTimer = new Timer();
		useCombo = false;
		comboBuff = "";
		page=0;
		changPage();
	}
	
	//畫上目前主角hp值在手機上
	public void showHp(){
		g.setColor(111,111,255);
		g.fillRect(disWidth + 7,disHeight + 185,40,10);
		g.setColor(106,255,106);
		g.fillRect(disWidth + 7,disHeight + 185,countHp(),10);
		g.setColor(0,0,0);
		setFont(0);
		g.drawString(String.valueOf(RoleDataValue.nowHP),disWidth + 14,disHeight + 184,0);
		flushGraphics();
	}
	
	//計算目前主角hp值~~
	public int countHp(){
		int poHp = 0;
		int maxHp = RoleDataValue.maxHP;
		int nowHp = RoleDataValue.nowHP;
		poHp = ((nowHp*10)/10*400)/(maxHp*10);
		//System.out.println("HP"+poHp);
		return poHp;
	}
	
	//顯示輸入的combo技
	public void drawComboValue(){
		if(RoleDataValue.comboValue*2>comboBuff.length()){
			if(comboBuff.length()<RoleDataValue.comboValue*2-1)
				comboBuff+="+";
			g.setColor(0,0,0);
			Font font = Font.getFont(Font.FACE_SYSTEM, 0, 0);
			g.setFont(font);
			g.drawString(comboBuff,disWidth + 74 - RoleDataValue.comboValue*5+5,disHeight + 100,0);
			g.drawString(comboBuff,disWidth + 75 - RoleDataValue.comboValue*5+5,disHeight + 100,0);
			g.drawString(comboBuff,disWidth + 76 - RoleDataValue.comboValue*5+5,disHeight + 102,0);
			g.drawString(comboBuff,disWidth + 77 - RoleDataValue.comboValue*5+5,disHeight + 102,0);
			g.setColor(255,255,255);
			font = Font.getFont(Font.FACE_SYSTEM, 0, 0);
			g.setFont(font);
			g.drawString(comboBuff,disWidth + 76 - RoleDataValue.comboValue*5+5,disHeight + 101,0);
			flushGraphics();
		}
	}

	//鍵盤事件
	protected void keyPressed(int keyCode) {
		if(useCombo){
			if(comboBuff.length()<RoleDataValue.comboValue*2-1){
				switch(keyCode){
					case 49:
						comboBuff+=(char)keyCode;
						drawComboValue();
						break;
					case 50:
						comboBuff+=(char)keyCode;
						drawComboValue();
						break;
					case 51:
						comboBuff+=(char)keyCode;
						drawComboValue();
						break;
				}
			}
		}
		if(atbFill){
			switch( keyCode ){
				case -5://按下FIRE
					switch (menustate) {//判斷畫面執行在哪個頁面
						case 0://一般攻擊
							try{
								roleAttackFrame();
							}catch(Exception e){e.printStackTrace();}
							atbTimer.cancel();
							atbTimer=null;
							atbTimer = new Timer();
							atbFill = false;
							atbValue=0;
							page=0;
							changPage();
							break;
						case 1://使用物品
							if(itemEvent==true && drawItemMenu==false){//物品清單初使化
								page=3;
								if(storeEvent=true && page==3){
					        		itemEvent = false;
					        		itemBuy = false;
								}
								changPage();
								System.out.println("Item");
							}else{//當選擇好物品時
								if(page==3){//選擇使用此物品
									itemEvent = false;
									itemBuy = !itemBuy;
									if(itemBuy==false){//確定要使用此物品
										itemEvent=true;
										if(itemMenuSelect==0){
											page=3;
											changPage();
											RoleDataValue.nowHP+=Integer.parseInt(itemBuff[storeIndex+itemSelect][3]);
											if(RoleDataValue.nowHP>RoleDataValue.maxHP)//血滿
												RoleDataValue.nowHP = RoleDataValue.maxHP;
											showHp();
											//物品使用
											itemUserChange(storeIndex+itemSelect);
											drawItemMenu = false;
											changPage();
											System.out.println("use:"+(storeIndex+itemSelect+1));
										}else if(itemMenuSelect==1){
											page=3;
											changPage();
											System.out.println("cancel");
										}else{//離開
											monsterConvs();
											itemEvent=true;
											drawItemMenu=false;
											storeEvent=false;
											page=1;
											changPage();
										}
									}
									itemMenuSelect=0;
								}
							}
							break;
						case 2://combo技
							if(xpbFill==true){
								//roleData.setNowComboValue();//設定combo技初始值
								if(RoleDataValue.nowCombo!=0){//如果釣竿支援combo技
									if(RoleDataValue.comboState==true){
										storeEvent = true;
										try{
											Thread.sleep(100);
										}catch(Exception e){}
										useCombo = true;
										page=2;
										changPage();
									}
								}
							}
							break;
						case 3://逃跑,物品選單離開
							if(page==3){
								drawItemMenu = false;
								page=1;
								changPage();
							}else
								RunAway();
							break;
					}
					break;
				case -1://按下UP
					if(itemBuy==true && (page==3)){//確認是否使用物品時
						if(itemMenuSelect <=0 )
							itemMenuSelect=3;
						itemMenuSelect--;
					}
					else if(itemBuy==false && (page==3)){//選擇要使用的物品
						if (itemSelect <= 0){
							if(storeIndex!=0){//選擇物品未到最底時
								storeIndex-=4;
								itemSelect = 4;
							}else{//選擇到最上物品時
								itemSelect = 1;
							}
						}
						itemSelect--;
						//System.out.println("upitemSelect"+itemSelect);
					}
					else{
						if(menustate <=0 )
							menustate=menuNum;
						menustate--;
					}
					
					break;
				case -2://按下DOWN
					if(itemBuy==true && (page==3)){//確認是否使用物品時
						if(itemMenuSelect >=2 )
							itemMenuSelect=-1;
						itemMenuSelect++;
					}
					else if(itemBuy==false && (page==3)){//選擇要使用的物品
						if(storeIndex+4<itemTotal){//先判斷物品還剩多少,是否有超出4個,超出四個就正常顯示
							if (itemSelect >= 4-1){
								storeIndex+=4;
								itemSelect = -1;
							}
						}else{//如果選單物品少餘四個,判斷選項有幾個
							if (itemSelect >= itemTotal-storeIndex-1){
								if(storeIndex+4==itemTotal){
									itemSelect=2;
								}else{
									itemSelect = -1;
									//System.out.println("%");
								}
							}
						}
						itemSelect++;
						System.out.println("Total"+itemTotal+"downitemSelect"+itemSelect+"||"+storeIndex);
					}else{
						if(menustate >=menuNum-1 )
							menustate=-1;
						menustate++;
					}
					break;
				case -4://按下RIGHT
					if(menustate==0)
						menustate=2;
					else if(menustate==1)
						menustate=3;
					else if(menustate==2)
						menustate=0;
					else if(menustate==3)
						menustate=1;
					break;
				case -3://按下LEFT
					if(menustate==0)
						menustate=2;
					else if(menustate==1)
						menustate=3;
					else if(menustate==2)
						menustate=0;
					else if(menustate==3)
						menustate=1;
					break;
			}
			changeMenu();
		}
	}
	
	//當物品使用後,改變數量
	public void itemUserChange(int num){
		String tmp = itemBuff[num][0];
		roleData.useItem(tmp,false);
	}
		
	public void run(){
		while(true){
			try{
				Thread.sleep(MonsterDataValue.atb);
			}catch(Exception e){}
			if(!battleFinish) continue;
			if(!storeEvent){
				GameMain.music.soundStart(1);
				monsterAttackFrame();
			}
		}
	}
	
	//combo技計數器
	class ComboTimerTask extends TimerTask{
		public void run(){
			try{
				if(comboBuff.equals(RoleDataValue.nowComboMethod)){
					comboImg = createImage("/image/combo/C"+RoleDataValue.nowCombo+".png");
					roleData.useItem("B"+RoleDataValue.bait,true);//釣餌減少
					GameMain.music.soundStart(0);
					comboAttack();
				}
				else{
					roleData.useItem("B"+RoleDataValue.bait,true);//釣餌減少
					roleAttackFrame();
				}
			}catch(Exception e){e.printStackTrace();}
		}
	}
	//ATB計數器
	class AtbTimerTask extends TimerTask{
		public void run(){
			if(atbFill==false){//ATB未滿時
				g.setColor(255,0,0);
				g.fillRect(disWidth + 7,disHeight + 155,atbValue,10);
				flushGraphics();
				if(atbValue<40)
					atbValue++;
				if(atbValue==40){
					atbFill=true;
					//if(atbFill==false){
						page=1;
						changPage();
					//}
					//atbFill=true;
				}
			}else{//ATB滿時
				g.setColor(255,0,0);
				g.fillRect(disWidth + 7,disHeight + 155,atbValue,10);
				flushGraphics();
				try{
					Thread.sleep(100);
				}catch(Exception e){}
				g.setColor(0,255,0);
				g.drawRect(disWidth + 7,disHeight + 155,atbValue-1,10-1);
				flushGraphics();
				try{
					Thread.sleep(100);
				}catch(Exception e){}
				
				/*roleAttackFrame();
				atbTimer.cancel();
				atbTimer=null;
				atbTimer = new Timer();
				atbFill = false;
				atbValue=0;
				page=0;
				changPage();*/
			}
		}
	}
	
	//XPB計數器
	class XpbTimerTask extends TimerTask{
		public void run(){
			if(battleFinish){
				if(xpbFill==false){//XPB未滿時
					try{
						Thread.sleep(100);
					}catch(Exception e){}
					g.setColor(255,255,0);
					g.fillRect(disWidth + 7,disHeight + 170,xpbValue,10);
					flushGraphics();
					if(xpbValue<40)
						xpbValue++;
					if(xpbValue==40){
						xpbFill=true;
					}
				}else{//XPB滿時
					g.setColor(255,255,0);
					g.fillRect(disWidth + 7,disHeight + 170,xpbValue,10);
					flushGraphics();
					try{
						Thread.sleep(100);
					}catch(Exception e){}
					g.setColor(0,255,0);
					g.drawRect(disWidth + 7,disHeight + 170,xpbValue-1,10-1);
					flushGraphics();
					try{
						Thread.sleep(100);
					}catch(Exception e){}
				}
			}
		}
	}
}