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
import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.game.GameCanvas;

public class RoleStateFrame extends GameCanvas{
	
	private static int menustate = 0;//目前選取到的選項
	private static int menuNum = 6;//共有幾個選項
	private static int page = 0;//頁數
	
	private static int itemSelect = 0;
	private static int storeIndex=0;//物品索引
	private static int itemTotal = 0;
	private static int itemMenuSelect = 0;
	private static boolean equipmentEvent = false;//當選擇使用裝備
	private static boolean itemEvent = false;//true時表示顯示物品在清單中
	private static boolean itemBuy = false;//當為false表示還沒確定要使用,true表示確定要使用
	private static boolean drawItemMenu = false;//是否為要畫物品
	private static boolean itemStart = false;//是否要換裝備
	private static boolean gameInfStart = false;
	
	private static int disWidth = 0;//LayManager實際位置

	private static int disHeight = 0;//LayManager實際位置
	
	private static int equipmentNum = 0;//目前裝備編號
	
	private static int lineNum = 0;//行數
	
	private static Graphics g = null;
	private static Image img = null;
	private static Image itemMenu = createImage("/image/gameInterface/BattleItem.png");
	private static Image pageIndex = createImage("/image/gameInterface/page_index.png");
	private static Image pageIndex_2 = createImage("/image/gameInterface/s_page2.png");
	private static Image roleStateMenu = createImage("/image/gameInterface/roleStateMenu.png");
	private static Image buyMenu = createImage("/image/gameInterface/buyMenu.png");
	private static Image description = createImage("/image/gameInterface/description.png");
	private static Image downInf = createImage("/image/gameInterface/downInf.png");
	private static Image img2[] = null;
	private static Image titleImg = null;
	
	private static String itemBuff[][] = new String[99][0];
	private static String itemAmount[] = new String[99];
	
	private static DataBaseReader db = null;
	
	private static RoleDataValue roleData = null;
	
	private static String[] menuStr = new String[6];
	private static String[] menuStr1 = { "裝備設定", "遊戲說明", "回到遊戲", "物品選單", " 必殺技", "離開遊戲"};
	private static String[] menuStr2 = {"補品道具","一般道具","事件道具","卷軸道具","回主選單"};
	private static String[] menuStr3 = {"操作說明","故事介紹","戰鬥介紹","其他說明","系統介紹","回主選單"};
	private static String[] itemUseStr = {"使用","取消","離開"};
	private static String[] equipmentStr = new String[8];
	
	private static String[] itemNameArray = {"Item","","Jewelry","","Battle","Protect","Event",
			"Monster","Pole","Bait","Tonic","Talk",""};
	
	private static mapCanvars mapca = null;
	private static GameMain midlet;
	
	public RoleStateFrame(RoleDataValue roleData_,mapCanvars mapca_,GameMain midlet_){
		super(false);
		setFullScreenMode(true);
		roleData = roleData_;
		mapca = mapca_;
		midlet = midlet_;
		//裝備設定
		db = new DataBaseReader();
		roleStateStart();
	}
	
	public void roleStateStart(){
		disWidth = (getWidth() - 176) / 2;//抓取螢幕的中心點
		disHeight = (getHeight() - 208) / 2;//抓取螢幕的中心點
		for(int i=0;i<itemBuff.length;i++)
			itemBuff[i] = null;
		page=0;
		changPage();
		drawDisplay();
	}
	
	//設定目前裝備
	public void setNowEquipment(){
		if(RoleDataValue.pole!=0){
			equipmentStr[0] = db.readDBField(8,RoleDataValue.pole,2);
		}else{
			equipmentStr[0] = "無";
		}
		
		if(RoleDataValue.proBody!=0){
			equipmentStr[1] = db.readDBField(5,RoleDataValue.proBody,2);
		}else{
			equipmentStr[1] = "無";
		}
		
		if(RoleDataValue.proHand!=0){
			equipmentStr[2] = db.readDBField(5,RoleDataValue.proHand,2);
		}else{
			equipmentStr[2] = "無";
		}
		
		if(RoleDataValue.proWaist!=0){
			equipmentStr[3] = db.readDBField(5,RoleDataValue.proWaist,2);
		}else{
			equipmentStr[3] = "無";
		}
		
		if(RoleDataValue.proFoot!=0){
			equipmentStr[4] = db.readDBField(5,RoleDataValue.proFoot,2);
		}else{
			equipmentStr[4] = "無";
		}
		
		if(RoleDataValue.jewelry!=0){
			equipmentStr[5] = db.readDBField(2,RoleDataValue.jewelry,2);
		}else{
			equipmentStr[5] = "無";
		}
		
		if(RoleDataValue.proHead!=0){
			equipmentStr[6] = db.readDBField(5,RoleDataValue.proHead,2);
		}else{
			equipmentStr[6] = "無";
		}
		
		if(RoleDataValue.bait!=0){
			equipmentStr[7] = db.readDBField(9,RoleDataValue.bait,2);
		}else{
			equipmentStr[7] = "無";
		}
	}
	
	public void drawDisplay() {
		g = getGraphics();
		switch (page) {
			case 0://一般狀態
				g.drawImage(pageIndex,disWidth+0,disHeight+0,0);
				g.drawImage(roleStateMenu,disWidth+0,disHeight+144,0);
				g.drawString(RoleDataValue.name,disWidth+126,disHeight+190,0);
				break;
			case 1://選擇裝備設定
				setNowEquipment();//設定目前裝備
				if(!itemStart){
					g.drawImage(pageIndex_2,disWidth+0,disHeight+0,0);
					g.drawImage(downInf,disWidth+0,disHeight+144,0);
				}
				if(itemStart){//當要裝備物品時
					if(!itemBuy){
						titleImg = createImage("/image/gameInterface/text1.png");
						g.drawImage(itemMenu,disWidth+3,disHeight+0,0);
						g.drawImage(titleImg,disWidth+117,disHeight+8,0);
					}
					if(itemBuy)
						g.drawImage(buyMenu,disWidth+121,disHeight+77,0);
				}
				break;
			case 2://選擇遊戲說明,故事介紹
				if(!itemStart){
					g.drawImage(pageIndex,disWidth+0,disHeight+0,0);
					showRoleStateInf();
				}
				g.drawImage(roleStateMenu,disWidth+0,disHeight+144,0);
				if(itemStart){	
					g.drawImage(description,disWidth+3,disHeight+0,0);
					g.setColor(128,255,0);
					setFont(16);
					g.drawString(menuStr[menustate],disWidth+55,disHeight+5,0);
					showGameStr();//顯示介紹
				}
				break;
			case 3://選擇回到遊戲
				
				break;
			case 4://選擇物品使用
				if(!itemStart){
					g.drawImage(pageIndex,disWidth+0,disHeight+0,0);
					g.drawImage(roleStateMenu,disWidth+0,disHeight+144,0);
					g.drawString(RoleDataValue.name,disWidth+126,disHeight+191,0);
					showRoleStateInf();
				}
				//g.drawImage(roleStateMenu,disWidth+0,disHeight+144,0);
				//g.drawString(RoleDataValue.name,disWidth+126,disHeight+191,0);
				if(itemStart){//當要使用物品時
					if(!itemBuy){
						titleImg = createImage("/image/gameInterface/text2.png");
						g.drawImage(itemMenu,disWidth+3,disHeight+0,0);
						g.drawImage(titleImg,disWidth+117,disHeight+8,0);
						titleImg = createImage("/image/gameInterface/text3.png");
						g.drawImage(titleImg,disWidth+117,disHeight+42,0);
					}
					if(itemBuy)
						g.drawImage(buyMenu,disWidth+121,disHeight+77,0);
				}
				break;
			case 5://選擇必殺技
				g.drawImage(description,disWidth+3,disHeight+0,0);
				g.setColor(128,255,0);
				setFont(16);
				g.drawString(" 必殺技",disWidth+55,disHeight+5,0);
				showGameStr();//顯示介紹
				break;
			case 6://選擇離開遊戲
				break;
		}
		flushGraphics();
		changeMenu();
	}
	
	//顯示game介紹
	public void showGameStr(){
		setFont(0);
		if(page==5)
			menustate=5;
		if(GameInfString.gameInfStr[menustate].length<=lineNum){
			//System.out.println("false");
			itemStart = false;
			gameInfStart = false;
			if(page==5){
				equipmentEvent = false;
				page=0;
				menustate = 0;
			}
			lineNum = 0;
			g.drawImage(roleStateMenu,disWidth+0,disHeight+144,0);
			g.drawImage(pageIndex,disWidth+0,disHeight+0,0);
			showRoleStateInf();
		}else{
			for(int i=0,k=0;i<12;i++,k+=14){
				if(GameInfString.gameInfStr[menustate].length<=lineNum){
					g.setColor(0,0,255);
					g.drawString("請按任意鍵回選單",disWidth+70,disHeight+186,0);
					break;
				}
				//System.out.println("line"+lineNum);
				g.setColor(0,0,0);
				g.drawString(GameInfString.gameInfStr[menustate][lineNum],disWidth+9,disHeight+26+k,0);
				lineNum++;
			}
			if(!(GameInfString.gameInfStr[menustate].length<=lineNum)){
				g.setColor(0,0,255);
				g.fillTriangle(disWidth+161,disHeight+196,disWidth+168,disHeight+196,disWidth+164,disHeight+200);
			}
		}
	}
	
	//改變頁面時，初使化
	public void changPage() {
		switch (page) {
			case 0://一般狀態
				menustate = 0;
				menuNum = 6;
				menuStr = menuStr1;
				break;
			case 1://選擇裝備設定
				//menustate = 0;
				menuNum = 9;
				itemUseStr[0] = "裝備";
				if(itemStart & !drawItemMenu){
					
					System.out.println("search item");
					
					equipmentNum = 0;
					String equipmentKind = "0";
					String itemStr = "";
					switch (menustate) {//選擇到的裝備
						case 0://選擇釣竿
							equipmentNum = 8;
							itemStr = "P";
							break;
						case 1://選擇衣服
							equipmentNum = 5;
							equipmentKind = "2";
							itemStr = "G";
							break;
						case 2://選擇手套
							equipmentNum = 5;
							equipmentKind = "5";
							itemStr = "G";
							break;
						case 3://選擇下褲
							equipmentNum = 5;
							equipmentKind = "3";
							itemStr = "G";
							break;
						case 4://選擇鞋子
							equipmentNum = 5;
							equipmentKind = "4";
							itemStr = "G";
							break;
						case 5://選擇飾品
							equipmentNum = 2;
							itemStr = "J";
							break;
						case 6://選擇帽子
							equipmentNum = 5;
							equipmentKind = "1";
							itemStr = "G";
							break;
						case 7://選擇釣餌
							equipmentNum = 9;
							itemStr = "B";
							break;
					}
					//把物品歸類
					String tmp_ = "";
					String tmp[] = null;
					int n=0;
					//搜尋主角物品
					roleData.setHashEnume();
					for(;RoleDataValue.enu.hasMoreElements();){
						tmp = (String[])RoleDataValue.enu.nextElement();
						if(tmp[0].indexOf(itemStr)==-1)
							continue;
						if(equipmentKind.equals("0")){
							//System.out.println("Find:"+tmp[0]);
							itemBuff[n] = db.readDBRecord(equipmentNum,
									Int( tmp[0].substring(1,tmp[0].length()) ));
							n++;
						}else{//為防具時,判斷為哪一部位的防具					
							tmp_ = db.readDBField(equipmentNum,
									Int(tmp[0].substring(1,tmp[0].length())),8);
							if(tmp_.equals(equipmentKind)){
								//System.out.println("Find:"+tmp[0]);
								itemBuff[n] = db.readDBRecord(equipmentNum,
										Int( tmp[0].substring(1,tmp[0].length()) ));
									n++;
							}
						}
					}
					itemSelect=0;
					storeIndex=0;
					itemTotal = n;
					if(itemTotal==0){
						page=1;
						itemStart = false;
					}else{
						img2 = new Image[itemTotal];
						for(int i=0;i<itemTotal;i++){
							img2[i] = createImage("/image/" + itemNameArray[equipmentNum] +"/"+ itemBuff[i][1] + ".png");
						}
						//System.out.println("Total:"+itemTotal+"|"+page);
						drawItemMenu = true;
					}
				}
				break;
			case 2://選擇遊戲介紹,故事介紹
				if(!itemStart){
					menustate = 0;
					menuNum = 6;
					menuStr = menuStr3;
				}
				break;
			case 3://選擇回到遊戲
				
				break;
			case 4://選擇物品使用
				menuNum = 5;
				//itemUseStr[0] = "使用";
				if(itemStart & !drawItemMenu){
					equipmentNum = 0;
					String itemStr = "";
					switch (menustate) {//選擇到的裝備
						case 0://選擇補品道具
							equipmentNum = 10;
							itemStr = "T";
							itemUseStr[0] = "使用";
							break;
						case 1://選擇一般道具
							equipmentNum = 0;
							itemStr = "I";
							itemUseStr[0] = "回收";
							break;
						case 2://選擇事件道具
							equipmentNum = 6;
							itemStr = "E";
							itemUseStr[0] = "丟棄";
							break;
						case 3://選擇卷軸道具
							equipmentNum = 11;
							itemStr = "K";
							itemUseStr[0] = "丟棄";
							break;
					}
					//把物品歸類
					String tmp[] = null;
					int n=0;
					//搜尋主角物品
					roleData.setHashEnume();
					for(;RoleDataValue.enu.hasMoreElements();){
						tmp = (String[])RoleDataValue.enu.nextElement();
						//如果編號大於要搜尋資料庫的數量則不搜尋
						if(tmp[0].indexOf(itemStr)==-1)
							continue;
						System.out.println("equipmentNum:"+equipmentNum+"|"+tmp[0]+"|");
						itemBuff[n] = db.readDBRecord(equipmentNum,
								Int( tmp[0].substring(1,tmp[0].length()) ));
						
						//itemAmount[n] = tmp[1];
						n++;
					}
					itemSelect=0;
					storeIndex=0;
					itemTotal = n;//搜尋到符合的物品數量
					if(itemTotal==0){
						page=4;
						itemStart = false;
					}else{
						img2 = new Image[itemTotal];
						for(int i=0;i<itemTotal;i++){
							img2[i] = createImage("/image/" + itemNameArray[equipmentNum] +"/"+ itemBuff[i][1] + ".png");
						}
						//System.out.println("Total:"+itemTotal+"|"+page);
						drawItemMenu = true;
					}
				}else//選單初始
					menustate = 0;
				break;
			case 5://選擇必殺技
				
				break;
			case 6://選擇離開遊戲
				
				break;
		}
		drawDisplay();
	}
	
	public void changeMenu(){
		setFont(0);
		if(page==0 || (page==2 && !itemStart) ){//一般狀態,遊戲說明
			switch (menustate) {
				case 0:
					//Select----------------------------
					setColor(true);
					g.drawString(menuStr[0], disWidth + 8, disHeight + 153, 0);
					//----------------------------------
					setColor(false);
					g.drawString(menuStr[1], disWidth + 8, disHeight + 169, 0);
					g.drawString(menuStr[2], disWidth + 8, disHeight + 185, 0);
					g.drawString(menuStr[3], disWidth + 62, disHeight + 153, 0);
					g.drawString(menuStr[4], disWidth + 62, disHeight + 169, 0);
					g.drawString(menuStr[5], disWidth + 62, disHeight + 185, 0);
					break;
				case 1:
					setColor(false);
					g.drawString(menuStr[0], disWidth + 8, disHeight + 153, 0);
					//Select----------------------------
					setColor(true);
					g.drawString(menuStr[1], disWidth + 8, disHeight + 169, 0);
					//----------------------------------
					setColor(false);
					g.drawString(menuStr[2], disWidth + 8, disHeight + 185, 0);
					g.drawString(menuStr[3], disWidth + 62, disHeight + 153, 0);
					g.drawString(menuStr[4], disWidth + 62, disHeight + 169, 0);
					g.drawString(menuStr[5], disWidth + 62, disHeight + 185, 0);
					break;
				case 2:
					setColor(false);
					g.drawString(menuStr[0], disWidth + 8, disHeight + 153, 0);
					g.drawString(menuStr[1], disWidth + 8, disHeight + 169, 0);
					//Select----------------------------
					setColor(true);
					g.drawString(menuStr[2], disWidth + 8, disHeight + 185, 0);
					//----------------------------------
					setColor(false);
					g.drawString(menuStr[3], disWidth + 62, disHeight + 153, 0);
					g.drawString(menuStr[4], disWidth + 62, disHeight + 169, 0);
					g.drawString(menuStr[5], disWidth + 62, disHeight + 185, 0);
					break;
				case 3:
					setColor(false);
					g.drawString(menuStr[0], disWidth + 8, disHeight + 153, 0);
					g.drawString(menuStr[1], disWidth + 8, disHeight + 169, 0);
					g.drawString(menuStr[2], disWidth + 8, disHeight + 185, 0);
					//Select----------------------------
					setColor(true);
					g.drawString(menuStr[3], disWidth + 62, disHeight + 153, 0);
					//----------------------------------
					setColor(false);
					g.drawString(menuStr[4], disWidth + 62, disHeight + 169, 0);
					g.drawString(menuStr[5], disWidth + 62, disHeight + 185, 0);
					break;
				case 4:
					setColor(false);
					g.drawString(menuStr[0], disWidth + 8, disHeight + 153, 0);
					g.drawString(menuStr[1], disWidth + 8, disHeight + 169, 0);
					g.drawString(menuStr[2], disWidth + 8, disHeight + 185, 0);
					g.drawString(menuStr[3], disWidth + 62, disHeight + 153, 0);
					//Select----------------------------
					setColor(true);
					g.drawString(menuStr[4], disWidth + 62, disHeight + 169, 0);
					//----------------------------------
					setColor(false);
					g.drawString(menuStr[5], disWidth + 62, disHeight + 185, 0);
					break;
				case 5:
					setColor(false);
					g.drawString(menuStr[0], disWidth + 8, disHeight + 153, 0);
					g.drawString(menuStr[1], disWidth + 8, disHeight + 169, 0);
					g.drawString(menuStr[2], disWidth + 8, disHeight + 185, 0);
					g.drawString(menuStr[3], disWidth + 62, disHeight + 153, 0);
					g.drawString(menuStr[4], disWidth + 62, disHeight + 169, 0);
					//Select----------------------------
					setColor(true);
					g.drawString(menuStr[5], disWidth + 62, disHeight + 185, 0);
					//----------------------------------
					break;
			}
			if(page==0)
				showRoleStateInf();
		}
		if(page==1 || page==4){//裝備設定
			if(equipmentEvent==true && itemBuy==true){
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
			else if(itemStart){//畫物品資訊在itemMenu
				setFont(0);
				g.drawImage(itemMenu,disWidth+3,disHeight+0,0);
				titleImg = createImage("/image/gameInterface/text3.png");
				g.drawImage(titleImg,disWidth+117,disHeight+42,0);
				if(page==1){
					titleImg = createImage("/image/gameInterface/text1.png");
					g.drawImage(titleImg,disWidth+117,disHeight+8,0);
				}else if(page==4){
					titleImg = createImage("/image/gameInterface/text2.png");
					g.drawImage(titleImg,disWidth+117,disHeight+8,0);
				}
				switch (itemSelect) {
					case 0:
						//Select----------------------------
						setColor(true);
						g.drawString(itemBuff[0+storeIndex][2], disWidth + 50, disHeight + 15, 0);
						g.drawImage(img2[0+storeIndex],disWidth+10,disHeight+6,0);
						setColor(false);
						showItemInf(0+storeIndex);
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
							setColor(false);
							showItemInf(1+storeIndex);
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
							setColor(false);
							showItemInf(2+storeIndex);
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
							setColor(false);
							showItemInf(3+storeIndex);
						}
						//----------------------------------
						break;
					}
				}else if(page==1){
					switch (menustate) {
						case 0:
							//Select----------------------------
							setColor(true);
							g.drawString(equipmentStr[0], disWidth + 41, disHeight + 30, 0);
							g.setColor(255,255,255);
							g.fillRect(disWidth + 126, disHeight + 33,32,32);
							if(!equipmentStr[0].equals("無"))
								g.drawImage(RoleDataValue.poleImg,disWidth + 126, disHeight + 33,0);
							//----------------------------------
							setColor(false);
							g.drawString(equipmentStr[1], disWidth + 41, disHeight + 45, 0);
							g.drawString(equipmentStr[2], disWidth + 41, disHeight + 60, 0);
							g.drawString(equipmentStr[3], disWidth + 41, disHeight + 74, 0);
							g.drawString(equipmentStr[4], disWidth + 41, disHeight + 89, 0);
							g.drawString(equipmentStr[5], disWidth + 41, disHeight + 104, 0);
							g.drawString(equipmentStr[6], disWidth + 41, disHeight + 118, 0);
							g.drawString(equipmentStr[7], disWidth + 122, disHeight + 105, 0);
							g.drawString("離    開", disWidth + 122, disHeight + 120, 0);
							break;
						case 1:
							setColor(false);
							g.drawString(equipmentStr[0], disWidth + 41, disHeight + 30, 0);
							//Select----------------------------
							setColor(true);
							g.drawString(equipmentStr[1], disWidth + 41, disHeight + 45, 0);
							g.setColor(255,255,255);
							g.fillRect(disWidth + 126, disHeight + 33,32,32);
							if(!equipmentStr[1].equals("無"))
								g.drawImage(RoleDataValue.proBodyImg,disWidth + 126, disHeight + 33,0);
							//----------------------------------
							setColor(false);
							g.drawString(equipmentStr[2], disWidth + 41, disHeight + 60, 0);
							g.drawString(equipmentStr[3], disWidth + 41, disHeight + 74, 0);
							g.drawString(equipmentStr[4], disWidth + 41, disHeight + 89, 0);
							g.drawString(equipmentStr[5], disWidth + 41, disHeight + 104, 0);
							g.drawString(equipmentStr[6], disWidth + 41, disHeight + 118, 0);
							g.drawString(equipmentStr[7], disWidth + 122, disHeight + 105, 0);
							g.drawString("離    開", disWidth + 122, disHeight + 120, 0);
							break;
						case 2:
							setColor(false);
							g.drawString(equipmentStr[0], disWidth + 41, disHeight + 30, 0);
							g.drawString(equipmentStr[1], disWidth + 41, disHeight + 45, 0);
							//Select----------------------------
							setColor(true);
							g.drawString(equipmentStr[2], disWidth + 41, disHeight + 60, 0);
							g.setColor(255,255,255);
							g.fillRect(disWidth + 126, disHeight + 33,32,32);
							if(!equipmentStr[2].equals("無"))
								g.drawImage(RoleDataValue.proHandImg,disWidth + 126, disHeight + 33,0);
							//----------------------------------
							setColor(false);
							g.drawString(equipmentStr[3], disWidth + 41, disHeight + 74, 0);
							g.drawString(equipmentStr[4], disWidth + 41, disHeight + 89, 0);
							g.drawString(equipmentStr[5], disWidth + 41, disHeight + 104, 0);
							g.drawString(equipmentStr[6], disWidth + 41, disHeight + 118, 0);
							g.drawString(equipmentStr[7], disWidth + 122, disHeight + 105, 0);
							g.drawString("離    開", disWidth + 122, disHeight + 120, 0);
							break;
						case 3:
							setColor(false);
							g.drawString(equipmentStr[0], disWidth + 41, disHeight + 30, 0);
							g.drawString(equipmentStr[1], disWidth + 41, disHeight + 45, 0);
							g.drawString(equipmentStr[2], disWidth + 41, disHeight + 60, 0);
							//Select----------------------------
							setColor(true);
							g.drawString(equipmentStr[3], disWidth + 41, disHeight + 74, 0);
							g.setColor(255,255,255);
							g.fillRect(disWidth + 126, disHeight + 33,32,32);
							if(!equipmentStr[3].equals("無"))
								g.drawImage(RoleDataValue.proWaistImg,disWidth + 126, disHeight + 33,0);
							//----------------------------------
							setColor(false);
							g.drawString(equipmentStr[4], disWidth + 41, disHeight + 89, 0);
							g.drawString(equipmentStr[5], disWidth + 41, disHeight + 104, 0);
							g.drawString(equipmentStr[6], disWidth + 41, disHeight + 118, 0);
							g.drawString(equipmentStr[7], disWidth + 122, disHeight + 105, 0);
							g.drawString("離    開", disWidth + 122, disHeight + 120, 0);
							break;
						case 4:
							setColor(false);
							g.drawString(equipmentStr[0], disWidth + 41, disHeight + 30, 0);
							g.drawString(equipmentStr[1], disWidth + 41, disHeight + 45, 0);
							g.drawString(equipmentStr[2], disWidth + 41, disHeight + 60, 0);
							g.drawString(equipmentStr[3], disWidth + 41, disHeight + 74, 0);
							//Select----------------------------
							setColor(true);
							g.drawString(equipmentStr[4], disWidth + 41, disHeight + 89, 0);
							g.setColor(255,255,255);
							g.fillRect(disWidth + 126, disHeight + 33,32,32);
							if(!equipmentStr[4].equals("無"))
								g.drawImage(RoleDataValue.proFootImg,disWidth + 126, disHeight + 33,0);
							//----------------------------------
							setColor(false);
							g.drawString(equipmentStr[5], disWidth + 41, disHeight + 104, 0);
							g.drawString(equipmentStr[6], disWidth + 41, disHeight + 118, 0);
							g.drawString(equipmentStr[7], disWidth + 122, disHeight + 105, 0);
							g.drawString("離    開", disWidth + 122, disHeight + 120, 0);
							break;
						case 5:
							setColor(false);
							g.drawString(equipmentStr[0], disWidth + 41, disHeight + 30, 0);
							g.drawString(equipmentStr[1], disWidth + 41, disHeight + 45, 0);
							g.drawString(equipmentStr[2], disWidth + 41, disHeight + 60, 0);
							g.drawString(equipmentStr[3], disWidth + 41, disHeight + 74, 0);
							g.drawString(equipmentStr[4], disWidth + 41, disHeight + 89, 0);
							//Select----------------------------
							setColor(true);
							g.drawString(equipmentStr[5], disWidth + 41, disHeight + 104, 0);
							g.setColor(255,255,255);
							g.fillRect(disWidth + 126, disHeight + 33,32,32);
							if(!equipmentStr[5].equals("無"))
								g.drawImage(RoleDataValue.jewelryImg,disWidth + 126, disHeight + 33,0);
							//----------------------------------
							setColor(false);							
							g.drawString(equipmentStr[6], disWidth + 41, disHeight + 118, 0);
							g.drawString(equipmentStr[7], disWidth + 122, disHeight + 105, 0);
							g.drawString("離    開", disWidth + 122, disHeight + 120, 0);
							break;
						case 6:
							setColor(false);
							g.drawString(equipmentStr[0], disWidth + 41, disHeight + 30, 0);
							g.drawString(equipmentStr[1], disWidth + 41, disHeight + 45, 0);
							g.drawString(equipmentStr[2], disWidth + 41, disHeight + 60, 0);
							g.drawString(equipmentStr[3], disWidth + 41, disHeight + 74, 0);
							g.drawString(equipmentStr[4], disWidth + 41, disHeight + 89, 0);
							g.drawString(equipmentStr[5], disWidth + 41, disHeight + 104, 0);
							//Select----------------------------
							setColor(true);
							g.drawString(equipmentStr[6], disWidth + 41, disHeight + 118, 0);
							g.setColor(255,255,255);
							g.fillRect(disWidth + 126, disHeight + 33,32,32);
							if(!equipmentStr[6].equals("無"))
								g.drawImage(RoleDataValue.proHeadImg,disWidth + 126, disHeight + 33,0);
							//----------------------------------
							setColor(false);							
							g.drawString(equipmentStr[7], disWidth + 122, disHeight + 105, 0);
							g.drawString("離    開", disWidth + 122, disHeight + 120, 0);
							break;
						case 7:
							setColor(false);
							g.drawString(equipmentStr[0], disWidth + 41, disHeight + 30, 0);
							g.drawString(equipmentStr[1], disWidth + 41, disHeight + 45, 0);
							g.drawString(equipmentStr[2], disWidth + 41, disHeight + 60, 0);
							g.drawString(equipmentStr[3], disWidth + 41, disHeight + 74, 0);
							g.drawString(equipmentStr[4], disWidth + 41, disHeight + 89, 0);
							g.drawString(equipmentStr[5], disWidth + 41, disHeight + 104, 0);
							g.drawString(equipmentStr[6], disWidth + 41, disHeight + 118, 0);
							//Select----------------------------
							setColor(true);							
							g.drawString(equipmentStr[7], disWidth + 122, disHeight + 105, 0);
							g.setColor(255,255,255);
							g.fillRect(disWidth + 126, disHeight + 33,32,32);
							if(!equipmentStr[7].equals("無"))
								g.drawImage(RoleDataValue.baitImg,disWidth + 126, disHeight + 33,0);
							//----------------------------------
							setColor(false);
							g.drawString("離    開", disWidth + 122, disHeight + 120, 0);
							break;
						case 8:
							setColor(false);
							g.drawString(equipmentStr[0], disWidth + 41, disHeight + 30, 0);
							g.drawString(equipmentStr[1], disWidth + 41, disHeight + 45, 0);
							g.drawString(equipmentStr[2], disWidth + 41, disHeight + 60, 0);
							g.drawString(equipmentStr[3], disWidth + 41, disHeight + 74, 0);
							g.drawString(equipmentStr[4], disWidth + 41, disHeight + 89, 0);
							g.drawString(equipmentStr[5], disWidth + 41, disHeight + 104, 0);
							g.drawString(equipmentStr[6], disWidth + 41, disHeight + 118, 0);							
							g.drawString(equipmentStr[7], disWidth + 122, disHeight + 105, 0);
							//Select----------------------------
							setColor(true);
							g.drawString("離    開", disWidth + 122, disHeight + 120, 0);
							//----------------------------------
							g.setColor(255,255,255);
							g.fillRect(disWidth + 126, disHeight + 33,32,32);
							break;
					}
				}
			}
			if(page==4 && !itemStart){//物品使用
				switch (menustate) {
					case 0:
						//Select----------------------------
						setColor(true);
						g.drawString(menuStr2[0], disWidth + 8, disHeight + 153, 0);
						//----------------------------------
						setColor(false);
						g.drawString(menuStr2[1], disWidth + 8, disHeight + 169, 0);
						g.drawString(menuStr2[2], disWidth + 8, disHeight + 185, 0);
						g.drawString(menuStr2[3], disWidth + 62, disHeight + 153, 0);
						g.drawString(menuStr2[4], disWidth + 62, disHeight + 169, 0);
						break;
					case 1:
						setColor(false);
						g.drawString(menuStr2[0], disWidth + 8, disHeight + 153, 0);
						//Select----------------------------
						setColor(true);
						g.drawString(menuStr2[1], disWidth + 8, disHeight + 169, 0);
						//----------------------------------
						setColor(false);
						g.drawString(menuStr2[2], disWidth + 8, disHeight + 185, 0);
						g.drawString(menuStr2[3], disWidth + 62, disHeight + 153, 0);
						g.drawString(menuStr2[4], disWidth + 62, disHeight + 169, 0);
						break;
					case 2:
						setColor(false);
						g.drawString(menuStr2[0], disWidth + 8, disHeight + 153, 0);
						g.drawString(menuStr2[1], disWidth + 8, disHeight + 169, 0);
						//Select----------------------------
						setColor(true);
						g.drawString(menuStr2[2], disWidth + 8, disHeight + 185, 0);
						//----------------------------------
						setColor(false);
						g.drawString(menuStr2[3], disWidth + 62, disHeight + 153, 0);
						g.drawString(menuStr2[4], disWidth + 62, disHeight + 169, 0);
						break;
					case 3:
						setColor(false);
						g.drawString(menuStr2[0], disWidth + 8, disHeight + 153, 0);
						g.drawString(menuStr2[1], disWidth + 8, disHeight + 169, 0);
						g.drawString(menuStr2[2], disWidth + 8, disHeight + 185, 0);
						//Select----------------------------
						setColor(true);
						g.drawString(menuStr2[3], disWidth + 62, disHeight + 153, 0);
						//----------------------------------
						setColor(false);
						g.drawString(menuStr2[4], disWidth + 62, disHeight + 169, 0);
						break;
					case 4:
						setColor(false);
						g.drawString(menuStr2[0], disWidth + 8, disHeight + 153, 0);
						g.drawString(menuStr2[1], disWidth + 8, disHeight + 169, 0);
						g.drawString(menuStr2[2], disWidth + 8, disHeight + 185, 0);
						g.drawString(menuStr2[3], disWidth + 62, disHeight + 153, 0);
						//Select----------------------------
						setColor(true);
						g.drawString(menuStr2[4], disWidth + 62, disHeight + 169, 0);
						//----------------------------------
						break;
				}
			}
		flushGraphics();
	}
	
	//鍵盤事件
	protected void keyPressed(int keyCode) {
		switch( keyCode ){
			case -5://按下FIRE
				if(page==0){
					switch (menustate) {//判斷畫面執行在哪個頁面
						case 0://選擇裝備設備
							equipmentEvent = true;
							page=1;
							changPage();
							break;
						case 1://選擇遊戲說明,故事介紹
							equipmentEvent = true;
							page=2;
							changPage();
							break;
						case 2://選擇回到遊戲
							ConnectionServer.updateGameInf();
							page=0;
							mapca.threadStart();
							break;
						case 3://選擇物品使用
							equipmentEvent = true;
							page=4;
							changPage();
							break;
						case 4://選擇必殺技
							equipmentEvent = true;
							page=5;
							changPage();
							break;
						case 5://選擇離開遊戲
							ConnectionServer.updateGameInf();
							try{
								Thread.sleep(2000);
							}catch(Exception e){}
							GameMain.music.stop();
							midlet.exit();
							break;
					}
				}else if(page==1){//裝備設定選項
					if(equipmentEvent & !itemStart){
						itemStart = true;
						itemBuy=false;
					}else if(equipmentEvent & itemStart)
						itemEvent=true;
					
					if(itemEvent){//選擇使用此物品
						itemEvent = false;
						itemBuy = !itemBuy;
						if(itemBuy==false){//裝備
							itemEvent=true;
							if(itemMenuSelect==0){//確定
								itemEvent=false;
								drawItemMenu=false;
								itemStart = false;
								itemBuy = false;
								//equipmentEvent = false;
								//改變裝備
								changeEquipment();
								page=1;
								System.out.println("use:"+(storeIndex+itemSelect+1));
							}else if(itemMenuSelect==1){//取消
								itemBuy = false;
								page=1;
								//changPage();
								System.out.println("cancel");
							}else{//離開
								itemEvent=false;
								drawItemMenu=false;
								itemStart = false;
								itemBuy = false;
								//equipmentEvent = false;
								page=1;
								//changPage();
							}
						}
						itemMenuSelect=0;
					}
					
					if(menustate==8){
						itemEvent=false;
						drawItemMenu=false;
						itemStart = false;
						itemBuy = false;
						equipmentEvent = false;
						page=0;
					}
					changPage();
				}else if(page==2){//選擇遊戲介紹,故事介紹	
					if(equipmentEvent & !itemStart){
						itemStart = true;
						gameInfStart = true;
					}
					
					if(menustate==5){
						itemEvent=false;
						drawItemMenu=false;
						itemStart = false;
						itemBuy = false;
						gameInfStart = false;
						equipmentEvent = false;
						page=0;
					}
					changPage();
				}else if(page==3){//選擇回到遊戲
					
				}else if(page==4){//選擇物品使用
					if(equipmentEvent & !itemStart){
						itemStart = true;
						itemBuy=false;
					}else if(equipmentEvent & itemStart)
						itemEvent=true;
					
					if(itemEvent){//選擇使用此物品
						itemEvent = false;
						itemBuy = !itemBuy;
						if(itemBuy==false){
							itemEvent=true;
							if(itemMenuSelect==0){//確定
								
								if(equipmentNum==10){//物品使用
									RoleDataValue.nowHP+=Integer.parseInt(itemBuff[storeIndex+itemSelect][3]);
									if(RoleDataValue.nowHP>RoleDataValue.maxHP)//血滿
										RoleDataValue.nowHP = RoleDataValue.maxHP;
									itemUserChange(storeIndex+itemSelect);
									page=4;
									itemEvent=false;
									drawItemMenu=false;
									itemStart = false;
									itemBuy = false;
								}else if(equipmentNum==0){//回收一般物品
									roleData.useItem(itemBuff[storeIndex+itemSelect][0],true);
									if(roleData.getItemAmount(itemBuff[storeIndex+itemSelect][0]).equals("0")){
										itemSelect=0;
										storeIndex=0;
									}
									itemBuy = false;
									page=4;
								}else{
									roleData.removeItem(itemBuff[storeIndex+itemSelect][0]);
									page=4;
									itemEvent=false;
									drawItemMenu=false;
									itemStart = false;
									itemBuy = false;
								}
								System.out.println("use:"+(storeIndex+itemSelect+1));
							}else if(itemMenuSelect==1){//取消
								itemBuy = false;
								page=4;
								//changPage();
								System.out.println("cancel");
							}else{//離開
								itemEvent=false;
								drawItemMenu=false;
								itemStart = false;
								itemBuy = false;
								//equipmentEvent = false;
								page=4;
								//changPage();
							}
						}
						itemMenuSelect=0;
						g.drawImage(buyMenu,disWidth+121,disHeight+77,0);
					}
					
					if(menustate==4){
						itemEvent=false;
						drawItemMenu=false;
						itemStart = false;
						itemBuy = false;
						equipmentEvent = false;
						page=0;
					}
					changPage();
				}else if(page==5){//選擇必殺技
					changPage();
				}else if(page==6){//選擇離開遊戲
					
				}
					
				break;
			case -1://按下UP
				if(itemBuy==true && (page==1 || page==4)){//確認是否使用物品時
					if(itemMenuSelect <=0 )
						itemMenuSelect=3;
					itemMenuSelect--;
				}
				else if(itemStart==true && itemBuy==false && (page==1 || page==4)){//選擇要使用的物品
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
				else if(!gameInfStart){
					if(menustate <=0 )
						menustate=menuNum;
					menustate--;
				}				
				break;
			case -2://按下DOWN
				if(itemBuy==true && (page==1 || page==4)){//確認是否使用物品時
					if(itemMenuSelect >=2 )
						itemMenuSelect=-1;
					itemMenuSelect++;
				}
				else if(itemStart==true && itemBuy==false && (page==1 || page==4)){//選擇要使用的物品
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
				}else if(!gameInfStart){
					if(menustate >=menuNum-1 )
						menustate=-1;
					menustate++;
				}
				break;
			case -4://按下RIGHT
				if(page==0 || page==2 || page==4){
					if(menustate==0)
						menustate=3;
					else if(menustate==1)
						menustate=4;
					else if(menustate==2){
						if(page==4)
							menustate=4;
						else
							menustate=5;
					}
					else if(menustate==3)
						menustate=0;
					else if(menustate==4)
						menustate=1;
					else if(menustate==5)
						menustate=2;
				}
				break;
			case -3://按下LEFT
				if(page==0 || page==2 || page==4){
					if(menustate==0)
						menustate=3;
					else if(menustate==1)
						menustate=4;
					else if(menustate==2){
						if(page==4)
							menustate=4;
						else
							menustate=5;
					}
					else if(menustate==3)
						menustate=0;
					else if(menustate==4)
						menustate=1;
					else if(menustate==5)
						menustate=2;
				}
				break;
		}
		changeMenu();
	}
	
	//顯示主角資訊
	public void showRoleStateInf(){
		setColor(false);
		g.drawString(String.valueOf(RoleDataValue.level), disWidth + 84, disHeight + 27, 0);
		showBar(RoleDataValue.exp,RoleDataValue.expNext,70,43,0);//EXP
		showBar(RoleDataValue.nowHP,RoleDataValue.maxHP,70,58,1);//HP
		showBar(RoleDataValue.getNowAtk(),RoleDataValue.getAtkMaxValue(),70,73,2);//ATK
		showBar(RoleDataValue.getNowDef(),RoleDataValue.getDefMaxValue(),70,88,3);//DEF
		g.drawString(RoleDataValue.fameStr, disWidth + 57, disHeight + 103, 0);
		g.drawString(String.valueOf(RoleDataValue.money), disWidth + 53, disHeight + 117, 0);
	}
	
	//當物品使用後,改變數量
	public void itemUserChange(int num){
		String tmp = itemBuff[num][0];
		roleData.useItem(tmp,false);
	}
	
	//抓取裝備累計的攻擊力
	public int getAtk(int num){
		int value = 0;
		if(equipmentNum==8){
			value = Int(itemBuff[num][3])+Int(itemBuff[num][6]);
		}else if(equipmentNum==2){
			value = Int(itemBuff[num][3])+Int(itemBuff[num][7]);
		}else if(equipmentNum==5){
			value = Int(itemBuff[num][5]);
		}
		return value;
	}
	
	//抓取裝備累計的防禦
	public int getDef(int num){
		int value = 0;
		if(equipmentNum==8){
			value = Int(itemBuff[num][5]);
		}else if(equipmentNum==2){
			value = Int(itemBuff[num][5])+Int(itemBuff[num][6]);
		}else if(equipmentNum==5){
			value = Int(itemBuff[num][3])+Int(itemBuff[num][4]);
		}
		return value;
	}
	
	//改變裝備 storeIndex+itemSelect
	public void changeEquipment(){
		String tmp = itemBuff[storeIndex+itemSelect][0];
		if(tmp.indexOf("P")!=-1)
			RoleDataValue.pole = Int(itemBuff[storeIndex+itemSelect][0].substring(1,tmp.length()));
		else if(tmp.indexOf("J")!=-1)
			RoleDataValue.jewelry = Int(itemBuff[storeIndex+itemSelect][0].substring(1,tmp.length()));
		else if(tmp.indexOf("B")!=-1)
			RoleDataValue.bait = Int(itemBuff[storeIndex+itemSelect][0].substring(1,tmp.length()));
		else if(tmp.indexOf("G")!=-1){
			String tmp_ = itemBuff[storeIndex+itemSelect][8];
			if(tmp_.equals("1"))
				RoleDataValue.proHead = Int(itemBuff[storeIndex+itemSelect][0].substring(1,tmp.length()));
			else if(tmp_.equals("2"))
				RoleDataValue.proBody = Int(itemBuff[storeIndex+itemSelect][0].substring(1,tmp.length()));
			else if(tmp_.equals("3"))
				RoleDataValue.proWaist = Int(itemBuff[storeIndex+itemSelect][0].substring(1,tmp.length()));
			else if(tmp_.equals("4"))
				RoleDataValue.proFoot = Int(itemBuff[storeIndex+itemSelect][0].substring(1,tmp.length()));
			else if(tmp_.equals("5"))
				RoleDataValue.proHand = Int(itemBuff[storeIndex+itemSelect][0].substring(1,tmp.length()));
		}
		RoleDataValue.setAtkDefValue();//重新設定atk與def
		roleData.setNowComboValue();//設定combo技
	}
	
	//畫上目前所以顯示的bar在手機上
	public void showBar(int now,int max,int x,int y,int color){
		//g.setColor(111,111,255);
		//g.fillRect(disWidth + x,disHeight + y,40,10);
		switch(color){
			case 0:
				g.setColor(235,205,63);
				break;
			case 1:
				g.setColor(106,255,106);
				break;
			case 2:
				g.setColor(224,47,180);
				break;
			case 3:
				g.setColor(50,112,216);
				break;
		}
		g.fillRect(disWidth + x,disHeight + y,countBar(now,max),10);
		g.setColor(0,0,0);
		setFont(0);
		if(color==0)
			g.drawString(String.valueOf(max-now),disWidth + x + 7,disHeight + y - 1,0);
		else
			g.drawString(String.valueOf(now),disWidth + x + 7,disHeight + y - 1,0);
		flushGraphics();
	}
	
	//顯示物品資料在畫面上
	public void showItemInf(int index){
		g.drawImage(downInf,disWidth+0,disHeight+144,0);
		if(equipmentNum==0){
			g.drawString("用途:",disWidth + 8,disHeight + 150,0);
			g.drawString("回收價格:"+Int(itemBuff[index][5])/2,disWidth + 8,disHeight + 188,0);
			splitText(itemBuff[index][3]);
			for(int i=0,k=0;i<textStr.length;i++,k+=14){
				if(textStr.length==1)
					g.drawString("「"+textStr[i]+"」",disWidth + 1,disHeight + 164+k,0);
				else if(i==0)
					g.drawString("「"+textStr[i],disWidth + 1,disHeight + 164+k,0);
				else if(i==textStr.length-1)
					g.drawString("  "+textStr[i]+"」",disWidth + 1,disHeight + 164+k,0);
				else
					g.drawString(textStr[i],disWidth + 1,disHeight + 164+k,0);
			}
		}else if(equipmentNum==8 || equipmentNum==2 || equipmentNum==5){
			g.drawString("攻擊(ATK)+"+String.valueOf(getAtk(index)),disWidth + 8,disHeight + 150,0);
			g.drawString("防禦(DEF)+"+String.valueOf(getDef(index)),disWidth + 8,disHeight + 164,0);
		}else if(equipmentNum==9){
			g.drawString("搭配釣竿:",disWidth + 8,disHeight + 150,0);
			g.drawString("「"+itemBuff[index][3]+"」",disWidth + 1,disHeight + 164,0);
		}else if(equipmentNum==10){
			g.drawString("增加HP值:",disWidth + 8,disHeight + 150,0);
			g.drawString("「"+itemBuff[index][3]+"」",disWidth + 1,disHeight + 164,0);
		}else if(equipmentNum==6){
			g.drawString("用途:",disWidth + 8,disHeight + 150,0);
			g.drawString("「"+itemBuff[index][3]+"」",disWidth + 1,disHeight + 164,0);
		}else if(equipmentNum==11){
			g.drawString("事件提示:",disWidth + 8,disHeight + 150,0);
			splitText(itemBuff[index][3]);
			for(int i=0,k=0;i<textStr.length;i++,k+=14){
				if(textStr.length==1)
					g.drawString("「"+textStr[i]+"」",disWidth + 1,disHeight + 164+k,0);
				else if(i==0)
					g.drawString("「"+textStr[i],disWidth + 1,disHeight + 164+k,0);
				else if(i==textStr.length-1)
					g.drawString("  "+textStr[i]+"」",disWidth + 1,disHeight + 164+k,0);
				else
					g.drawString(textStr[i],disWidth + 1,disHeight + 164+k,0);
			}
		}
		g.drawString("    "+roleData.getItemAmount(itemBuff[index][0]),disWidth + 123,disHeight + 57,0);
	}
	
	private static String textStr[] = null;
	public void splitText(String str) {
		textStr = null;
		try{
			int line = str.length()/14;
			if(str.length()%14>0)
				line++;
			textStr = new String[line];
			
			if(line>1){
				textStr[0] = str.substring(0,14);
				textStr[1] = str.substring(14,str.length());
			}else
				textStr[0] = str.substring(0,str.length());
		}catch(Exception e){e.printStackTrace();}
	}
	
	//計算目前bar值~~
	public int countBar(int now,int max){
		int poHp = 0;
		poHp = ((now*10)/10*400)/(max*10);
		if(poHp<1)
			poHp = 1;
		return poHp;
	}
	
	//字型設定
	public void setFont(int size) {
		Font font = Font.getFont(Font.FACE_SYSTEM, 0, size);
		g.setFont(font);
	}
	
	//顏色設定
	public void setColor(boolean value){
		if(value)
			g.setColor(255, 0, 0);
		else
			g.setColor(69,118,181);
	}
	
	//轉換為整數
	public int Int(String data){
		return Integer.parseInt(data);
	}
	
	//	建立圖片
	public static Image createImage(String pathImg) {
		img = null;
		try {
			img = Image.createImage(pathImg);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return img;
	}
	
	public void exit(){}
}