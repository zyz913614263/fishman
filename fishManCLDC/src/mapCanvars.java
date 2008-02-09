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
import javax.microedition.lcdui.*;
import javax.microedition.lcdui.game.*;

public class mapCanvars extends GameCanvas implements Runnable
{
	static map mapImfor;//儲存地圖的資訊，陣列長度、行數、列數、-1值、起始座標
	static wordLayerCreate makeWord;//wordLayerCreate繼承LayerManager並實作了TiledLayer and Sprite
	static npcGemSprite makeNpcGemSprite;
	static storyEven createStory;//故事事件的流程
	//*******主角資料*******//
	static RoleDataValue mainValue = null;
	
	//*******viewWindow的位置 X'Y*******//
	private int viewTargetX = 0;
	private int viewTargetY = 0;
	
	//*******mainSprite的位置 X'Y*******//
	private int mainTargetX = 0;
	private int mainTargetY = 0;
	
	//*******mainSprite的位置 X'Y*******//
	private int lastKeyState = -1;
	
	//*******Sprite指標********//
	private Sprite mainSprite;
	private Sprite checkSprite;
	private Sprite npcSprite[];
	private Sprite gemSprite[];
	private TiledLayer cantMoveLayer;
	
	//*******NPC Image指標*******//
	private Image npcImage[];
	
	//********人物走的步幅********//
	private final int step = 16;
	
	//*******按鍵鎖定*******//
	private boolean luckState = true;
	
	//*******view狀態*******//
	private boolean viewState = false;
	
	//*******控制步伐*******//
	private int tick = 0;
	
	//*******重複呼叫用的Timer*******//
	//private Timer gameTimer = new Timer();
	
	//*******設定城鎮位置(依故事順序)*******//
	private int evenPoint;
	
	//*******產生怪物亂數*******//
	private monsterTurnUp runMonsterTurnUp;
	
	//*******產生怪物的數量*******//
	final int monsterAmount = 3;//更改這個數字，改變每個地圖出現怪物的數量
	private int monsterCounter = 50;//控制怪物的時間的計算者
	final int RESET_MONSTER = 50;
	
	//*******儲存NPC種類*******//
	private int npcKind = -1;
	
	//MIDlet Display
	private Display display = null;
	
	//戰鬥是否結束
	public boolean battleFinish = false;
	
	//是否有不能行走區域碰撞
	public boolean collision = false;
	
	public static Thread thread = null;
	
	private static RoleStateFrame roleState = null;
	
	//地圖計數器,用來顯示地圖Title用
	private static int mapTilteValue = 0;
	
	//*******無引數建構值*******//
	public mapCanvars(Display display,RoleDataValue roleData,GameMain midlet_)
	{
		super(false);
		
		this.display = display;
		mainValue = roleData;
		//主角資料(模擬用，正式版使用資料庫)
		//RoleDataValue mainValue = new RoleDataValue();

		//怪物戰鬥初使
		battle = new BattleFrame(mainValue,this);
		roleState = new RoleStateFrame(mainValue,this,midlet_);
		try{
			setStoreValue();//設定商店物品
		}catch(Exception e){e.printStackTrace();}
		//*******畫初始位置*******//
		drawWord();
		//Thread
		thread = new Thread(this);
		thread.start();
	}
	
	//********畫地圖、人物...等會出現在地圖上之圖片(無引數)********//
	private void drawWord()
	{
		mapTilteValue = 0;
		
		setEvenPoint(0);

		makeWord = new wordLayerCreate(this.mainValue, this.evenPoint);//呼叫無引數的建構值
		mapImfor = makeWord.getMapClass();
		createStory = new storyEven(this, this.mainValue, this.mapImfor,  this.makeWord, this.evenPoint);
		checkSprite = makeWord.getCheckSprite();
		cantMoveLayer = makeWord.getCantMoveTiled();
		mainSprite = makeWord.getMainSprite();
		makeNpcGemSprite = new npcGemSprite(this.makeWord, this.mapImfor, this.evenPoint);
		npcSprite = makeNpcGemSprite.getNpcSprite();
		gemSprite = makeNpcGemSprite.getGemSprite();
		
		//*******創造NPC至地圖上*******//
		if(mapImfor.getNpc(this.evenPoint))
		{	
			makeNpcGemSprite.makeNpcSprite();
		}
		
		//*******創造寶箱至地圖上*******//
		if(mapImfor.getGem(this.evenPoint).equals("1"))
		{	
			makeNpcGemSprite.makeGemSprite();
		}
		
		//******new() 怪物亂數的Class
		runMonsterTurnUp = new monsterTurnUp(mapImfor, this.evenPoint);
		
		//*******起始Main Sprite座標********//
		mainTargetX = 299;
		mainTargetY = 267;
		
		//***********人物起始座標**************//
		makeWord.setMainPosition(mainTargetX, mainTargetY);//第一次初始遊戲的固定起始座標 304, 272
		makeWord.setCheckPosition(mainTargetX-3, mainTargetY-3);//碰撞圖片初始座標
		
		//*******起始View Window座標********//
		viewTargetX = (mainTargetX-this.getWidth()/2);
		viewTargetY = (mainTargetY-this.getHeight()/2);
		
		checkViewWindow();
		
		//makeWord.setView(viewTargetX, viewTargetY, this.getWidth(), this.getHeight());
		
		if(RoleDataValue.roleMap != 0) jumpWord(RoleDataValue.roleMap, RoleDataValue.roleX, RoleDataValue.roleY);
		
		render();
		
		//music
		if(RoleDataValue.roleMap == 0)
			GameMain.music.musicStart(this.evenPoint);
	}
	
	//*******重新繪製新的地圖*******//
	public void resetWord(int deliver, int evenPoint)
	{
		mapTilteValue = 0;
		
		stopNpcGem();//移出NPC
		setEvenPoint(mapImfor.getLink(deliver, evenPoint));
		
		runMonsterTurnUp.setEvenPoint(this.evenPoint);//把城鎮事件點傳給亂數怪物的class
		makeNpcGemSprite.setEvenPoint(this.evenPoint);
		createStory.setEvenPoint(this.evenPoint);
		
		
		this.mainTargetX = mapImfor.getStartX(deliver, evenPoint);
		this.mainTargetY = mapImfor.getStartY(deliver, evenPoint);
		
		//*******初始NPC的圖片*******//
		makeNpcGemSprite.setEvenPoint(this.evenPoint);
		if(mapImfor.getNpc(this.evenPoint))
		{
			makeNpcGemSprite.makeNpcSprite();
		}
		
		//*******創造寶箱至地圖上*******//
		if(mapImfor.getGem(this.evenPoint).equals("1"))
		{	
			makeNpcGemSprite.makeGemSprite();
		}
		
		//*******設定背景及碰撞圖片初始點*******//
		makeWord.setMainPosition(mainTargetX , mainTargetY);
		makeWord.setCheckPosition(mainTargetX-3, mainTargetY-3);
		
		//*******起始View Window座標********//
		if(mainTargetX>((mapImfor.getColumn(this.evenPoint)*16)-(this.getWidth()/2)))
					viewTargetX = (mapImfor.getColumn(this.evenPoint)*16)-this.getWidth();
		else if(mainTargetX>this.getWidth()/2) viewTargetX = (mainTargetX-this.getWidth()/2);
		else viewTargetX=0;
		
		if(mainTargetY>((mapImfor.getRow(this.evenPoint)*16)-(this.getHeight()/2)))
					viewTargetY = (mapImfor.getRow(this.evenPoint)*16)-this.getHeight();
		else if(mainTargetY>this.getHeight()/2) viewTargetY = (mainTargetY-this.getHeight()/2);
		else viewTargetY=0;
		
		checkViewWindow();
		
		//makeWord.setView(viewTargetX, viewTargetY, this.getWidth(), this.getHeight());
		
		//*****把畫面重新設定為下個地圖*****//
		makeWord.resetMap(deliver, evenPoint);	
		render();
		
		//music
		GameMain.music.musicStart(this.evenPoint);
	}
	
	//*******重新進入遊戲時跳關用*******//
	public void jumpWord(int evenPoint, int x, int y)
	{
		mapTilteValue = 0;
		
		stopNpcGem();//移出NPC
		
		setEvenPoint(evenPoint);
		runMonsterTurnUp.setEvenPoint(this.evenPoint);//把城鎮事件點傳給亂數怪物的class
		makeNpcGemSprite.setEvenPoint(this.evenPoint);
		createStory.setEvenPoint(this.evenPoint);
		
		//music
		GameMain.music.musicStart(this.evenPoint);
		
		this.mainTargetX = x;
		this.mainTargetY = y;
		
		//*******初始NPC的圖片*******//
		makeNpcGemSprite.setEvenPoint(this.evenPoint);
		if(mapImfor.getNpc(this.evenPoint))
		{
			makeNpcGemSprite.makeNpcSprite();
		}
		
		//*******創造寶箱至地圖上*******//
		if(mapImfor.getGem(this.evenPoint).equals("1"))
		{	System.out.println("call makeGemSprite : " + mapImfor.getGem(this.evenPoint));
			makeNpcGemSprite.makeGemSprite();
		}
		
		//*******設定背景及碰撞圖片初始點*******//
		makeWord.setMainPosition(mainTargetX , mainTargetY);
		makeWord.setCheckPosition(mainTargetX-3, mainTargetY-3);
		
		//*******起始View Window座標********//
		if(mainTargetX>((mapImfor.getColumn(this.evenPoint)*16)-(this.getWidth()/2)))
					viewTargetX = (mapImfor.getColumn(this.evenPoint)*16)-this.getWidth();
		else if(mainTargetX>this.getWidth()/2) viewTargetX = (mainTargetX-this.getWidth()/2);
		else viewTargetX=0;
		
		if(mainTargetY>((mapImfor.getRow(this.evenPoint)*16)-(this.getHeight()/2)))
					viewTargetY = (mapImfor.getRow(this.evenPoint)*16)-this.getHeight();
		else if(mainTargetY>this.getHeight()/2) viewTargetY = (mainTargetY-this.getHeight()/2);
		else viewTargetY=0;
		
		checkViewWindow();
		
		//makeWord.setView(viewTargetX, viewTargetY, this.getWidth(), this.getHeight());
		
		//*****把畫面重新設定為下個地圖*****//
		makeWord.jumpMap(this.evenPoint);		
		render();
	}
	
	//*******將NPC從LayerManager中移除(如果沒有吃寶箱，會順便移除寶箱)*******//
	public void stopNpcGem()
	{
		if(mapImfor.getGem(this.evenPoint).equals("1")) makeWord.remove(makeWord.getLayerAt(1));
		for(int i=0; i<mapImfor.getNpcAmount(this.evenPoint); i++) makeWord.remove(makeWord.getLayerAt(1));
	}
	
	private void render()
	{
		if(!storeEvent){
		    final Graphics g = this.getGraphics();
		    g.setColor(0, 0, 0);
		    g.fillRect(0, 0, this.getWidth(), this.getHeight());
		    makeWord.paint(g, 0, 0);
		    if(mapTilteValue <= 10){
		    	g.drawImage(mapBar,0+getWidth()-100,0,0);
		    	g.drawString(db.readDBField(4,this.evenPoint+1,2),0+getWidth()-94,5,0);
		    }
		    flushGraphics();
		}
    }
  		
		//********按鍵偵測********//
		private void processInput()//********受evenPoint影響的Method()********//
		{	
	        final int keyState = this.getKeyStates();
	        
	           //當有商店觸發時-----------------------------------------------------------------------------
		       if(storeEvent){
		       		if( (storeEvent==true) & (keyState & FIRE_PRESSED) !=0 ){
		       			if(talkAbout){
		       				try{
								eventTalk();
							}catch(Exception e){e.printStackTrace();}
		       			}else
						switch (menustate) {//判斷畫面執行在哪個頁面
							case 0://對話
								//*******瀏覽故事流程*******//
								createStory.doEven(npcKind);
								usualTalk = false;
								if(!talkAbout){
									setTalkAbout(createStory.getTalkValue());
									talkAbout = true;
									whoTalkAbout = false;
								}
								try{
									eventTalk();
								}catch(Exception e){e.printStackTrace();}
								break;
							case 1://買物品時
								try{
								page=1;
								if(storeEvent=true && page==1){
					        		itemEvent = false;
					        		itemBuy = false;
								}
								changPage();
								}catch(Exception e){e.printStackTrace();}
								break;
							case 2:
								if(page==0 & (npcKind==2 || npcKind==5 || npcKind==8 || npcKind==9)){//賣東西
									page=2;
									if(storeEvent=true && page==2){
						        		itemEvent = false;
						        		itemBuy = false;
									}
									changPage();
								}								
								else if(page==1 & (npcKind==2 || npcKind==5 || npcKind==8 || npcKind==9)){//確定買
									itemEvent=true;
									itemBuy = !itemBuy;
									if(itemBuy==false){//確定要買此樣物品時
										itemEvent = false;
										mainValue.addItem(itemBuff[storeIndex+itemSelect][0],true,1);
										ConnectionServer.updateGameInf();
										System.out.println("buy:"+(storeIndex+itemSelect+1));
									}
									page=1;
									changPage();
								}//確定賣
								else if(page==2 & (npcKind==2 || npcKind==5 || npcKind==8 || npcKind==9)){
									itemEvent=true;
									itemBuy = !itemBuy;
									if(itemBuy==false){//確定要賣此樣物品時
										itemEvent = false;
										mainValue.useItem(itemBuff[storeIndex+itemSelect][0],true);
										if(mainValue.getItemAmount(itemBuff[storeIndex+itemSelect][0]).equals("0")){
											itemSelect=0;
											storeIndex=0;
										}
										ConnectionServer.updateGameInf();
										System.out.println("sell:"+(storeIndex+itemSelect+1));
									}
									page=2;
									changPage();
								}else{//對話二
									//*******瀏覽故事流程*******//
									//createStory.doEven(npcKind);
									//usualTalk = false;
									if(npcKind!=4 || (npcKind==4 && RoleDataValue.nowEvenSchedule[0].indexOf("4")==-1)){//如果旅館有事件時,才會觸發
										createStory.doEven(npcKind);
										usualTalk = false;
										if(!talkAbout & !otherEventFinish){
											setTalkAbout(createStory.getTalkValue());
											talkAbout = true;
											whoTalkAbout = false;
										}//如果主角有自言自語時
										if(otherEventFinish){
											if(!otherEvent)
												roleOwnEvent(1);
											else{
												try{
													eventTalk();
												}catch(Exception e){e.printStackTrace();}
											}
										}else
											eventTalk();
									}else if(npcKind==4 && RoleDataValue.nowEvenSchedule[0].indexOf("4")!=-1){//休息
										RoleDataValue.nowHP = RoleDataValue.maxHP;
										//System.out.println("sleep");
									}
								}
								break;
							case 3:
								if(page==1 ||page==2){
									itemSelect=0;
									storeIndex=0;
									drawItemMenu = false;
									itemEvent = true;
									storeG.fillRect(0, 0, this.getWidth(), this.getHeight());
									makeWord.paint(storeG, 0, 0);
									flushGraphics();
									page=0;
									changPage();
								}
								else{
									storeEvent=false;
									npcKind = -1;
									otherEvent = false;
									otherEventFinish = false;
									//gameTimer = new Timer();
									//gameTimer.scheduleAtFixedRate(new gameTask(), 0, 10);
								}
								break;
						}
		       		}//上
					else if( !talkAbout & ((keyState & UP_PRESSED) != 0)){
						//System.out.println("up");
						if(itemEvent && (page==1 || page==2)){
							if(menustate <=2 )
								menustate=4;
							menustate--;
						}
						else if(!itemEvent && (page==1 || page==2)){//選取物品時
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
						}//當不是商店npc時
						else if(!(npcKind==2 || npcKind==5 || npcKind==8 || npcKind==9)){
							if(menustate <=2 )
								menustate=4;
							menustate--;
						}
						else {//一般Menu選單時
							if (menustate <= 0)
								menustate = menuNum;
							menustate--;
						}
					}//下
					else if( !talkAbout & ((keyState & DOWN_PRESSED) != 0)){
						//System.out.println("down");
						if(itemEvent && (page==1 || page==2)){
							if(menustate >=3 )
								menustate=1;
							menustate++;
						}
						else if(!itemEvent && (page==1 || page==2)){
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
						}//當不是商店npc時
						else if(!(npcKind==2 || npcKind==5 || npcKind==8 || npcKind==9)){
							if(menustate >=3 )
								menustate=1;
							menustate++;
						}
						else {//一般Menu選單時
							if (menustate >= menuNum - 1)
								menustate = -1;
							menustate++;
						}
					}
					try{
						changeMenu();
					}catch(Exception e){e.printStackTrace();}
					
		    }//-------------------------------------------------------------------------------------------------
			else{//地圖走動時
				mapTilteValue++;
		        //*********************RIGHT ACTIVE*****************************//
		        if ((luckState == true) & ((keyState & RIGHT_PRESSED) != 0)) {
		        	if(lastKeyState != keyState) makeWord.setImage(3);
		        	makeWord.setCollisionArea(34, 31, 1, 1);
		        	luckState = false;
		        	
		            mainTargetX += step;
		            
		            if(mainTargetX > getWidth()/2){
			        		viewTargetX = mainTargetX-(this.getWidth()/2);
			        		viewState = true;
			        		
			        		if(viewTargetX+this.getWidth() > makeWord.getCellWidth()*mapImfor.getColumn(evenPoint))
			        		{
			        				viewTargetX = (makeWord.getCellWidth()*mapImfor.getColumn(evenPoint)) - this.getWidth();
			        				viewState = false;
			        		}
		        	}
		            for(int i=0; i<mapImfor.getNpcAmount(this.evenPoint); i++) checkNpcCollision(keyState, i);
					checkCollision(keyState);
					if(mapImfor.getStop(3, this.evenPoint) & (mapImfor.getDBStop(3, this.evenPoint).equals("1")))
						checkStopCollision(keyState, 3);
						
					
					for(int j=0; j<mapImfor.getGemAmount(this.evenPoint); j++)
						if(mapImfor.getGem(this.evenPoint).equals("1")) checkGemCollision(j);
					if(mapImfor.getMonster(this.evenPoint)) callMonster();
					
					lastKeyState = keyState;
		        	//*********************RIGHT ACTIVE END*************************//
		        	
		        	//*********************Print out*******************//
		        	//System.out.println("RIGHT");
	            	//System.out.println("mainTargetX = " + mainTargetX);
					//System.out.println("mainTargetY = " + mainTargetY);
					//System.out.println("mainSprite.getRefPixelX() = " + mainSprite.getRefPixelX());
					//System.out.println("mainSprite.getRefPixelY() = " + mainSprite.getRefPixelY());
					
					//*********************LEFT ACTIVE *****************************// 
		        } else if ((luckState == true) & ((keyState & LEFT_PRESSED) != 0)) {
		        	if(lastKeyState != keyState) makeWord.setImage(2);;
		        	makeWord.setCollisionArea(2, 31, 1, 1);
		        	luckState = false;
	
		            mainTargetX -= step;
		            
		            if(mainTargetX > ((makeWord.getCellWidth()*mapImfor.getColumn(evenPoint)) - this.getWidth()/2))	;
					else if(mainTargetX > getWidth()/2){
		        		
			        		viewTargetX = mainTargetX-((this.getWidth()/2)+8);
			        		viewState = true;
		        	}
					for(int i=0; i<mapImfor.getNpcAmount(this.evenPoint); i++) checkNpcCollision(keyState, i);
					checkCollision(keyState);
					if(mapImfor.getStop(2, this.evenPoint) & (mapImfor.getDBStop(2, this.evenPoint).equals("1")))
						checkStopCollision(keyState, 2);
					
					for(int j=0; j<mapImfor.getGemAmount(this.evenPoint); j++)
						if(mapImfor.getGem(this.evenPoint).equals("1")) checkGemCollision(j);
					if(mapImfor.getMonster(this.evenPoint)) callMonster();
					lastKeyState = keyState;
		        	//*********************LEFT ACTIVE END**************************//
		        	
		        	//*********************Print out*******************//
		        	//System.out.println("LEFT");
	            	//System.out.println("mainTargetX = " + mainTargetX);
					//System.out.println("mainTargetY = " + mainTargetY);
					//System.out.println("mainSprite.getRefPixelX() = " + mainSprite.getRefPixelX());
					//System.out.println("mainSprite.getRefPixelY() = " + mainSprite.getRefPixelY());
		        	
		        	//**********************DOWN ACTIVE*****************************//
		        } else if((luckState == true) & ((keyState & DOWN_PRESSED) != 0)){
		        	if(lastKeyState != keyState) makeWord.setImage(1);
		        	makeWord.setCollisionArea(12, 50, 3, 1);
		        	luckState = false;
		        	
		            mainTargetY += step;
		            
		            if(mainTargetY > this.getHeight()/2){
		        		
			        			viewTargetY = mainTargetY-(this.getHeight()/2);
			        			viewState = true;
			        			
			        			if(viewTargetY+this.getHeight() > makeWord.getCellHeight()*mapImfor.getRow(evenPoint))
			        			{
			        					viewTargetY = (makeWord.getCellHeight()*mapImfor.getRow(evenPoint)) - this.getHeight();
			        					viewState = false;
			        			}
			        			
		        	}
		            for(int i=0; i<mapImfor.getNpcAmount(this.evenPoint); i++) checkNpcCollision(keyState, i);
					checkCollision(keyState);
					if(mapImfor.getStop(1, this.evenPoint) & (mapImfor.getDBStop(1, this.evenPoint).equals("1")))
						checkStopCollision(keyState, 1);
					
					for(int j=0; j<mapImfor.getGemAmount(this.evenPoint); j++)
						if(mapImfor.getGem(this.evenPoint).equals("1")) checkGemCollision(j);
					if(mapImfor.getMonster(this.evenPoint)) callMonster();
					lastKeyState = keyState;
		            //**********************DOWN ACTIVE END*******************************//
		            
		            //*********************Print out*******************//
		            //System.out.println("DOWN");
	            	//System.out.println("mainTargetX = " + mainTargetX);
					//System.out.println("mainTargetY = " + mainTargetY);
					//System.out.println("mainSprite.getRefPixelX() = " + mainSprite.getRefPixelX());
					//System.out.println("mainSprite.getRefPixelY() = " + mainSprite.getRefPixelY());
					
					//**********************UP ACTIVE*************************************//
		        } else if((luckState == true) & ((keyState & UP_PRESSED) != 0)){
		        	if(lastKeyState != keyState) makeWord.setImage(0);
		        	makeWord.setCollisionArea(11, 16, 8, 3);
		        	luckState = false;
					
		            mainTargetY -= step;
	
					if(mainTargetY > ((makeWord.getCellHeight()*mapImfor.getRow(evenPoint)) - this.getHeight()/2))	;
					else if(mainTargetY > this.getHeight()/2){
			        			viewTargetY = mainTargetY-((this.getHeight()/2)+16);
			        			viewState = true;
		        	}
					for(int i=0; i<mapImfor.getNpcAmount(this.evenPoint); i++) checkNpcCollision(keyState, i);
					checkCollision(keyState);
					if(mapImfor.getStop(0, this.evenPoint) & (mapImfor.getDBStop(0, this.evenPoint).equals("1")))
						checkStopCollision(keyState, 0);
					
					for(int j=0; j<mapImfor.getGemAmount(this.evenPoint); j++)
						if(mapImfor.getGem(this.evenPoint).equals("1")) checkGemCollision(j);
					if(mapImfor.getMonster(this.evenPoint)) callMonster();
					lastKeyState = keyState;
	            //**********************UP ACTIVE END*********************************//
	            
	            	//*********************Print out*******************//
	            	//System.out.println("UP");
	            	//System.out.println("mainTargetX = " + mainTargetX);
					//System.out.println("mainTargetY = " + mainTargetY);
					//System.out.println("mainSprite.getRefPixelX() = " + mainSprite.getRefPixelX());
					//System.out.println("mainSprite.getRefPixelY() = " + mainSprite.getRefPixelY());
		        }
		        //當按下按鍵執行時，起動對話方塊----------------------------------------
		        else if( (npcKind!=-1) & (luckState == true) & (storeEvent==false) & (keyState & FIRE_PRESSED) !=0 ){
		        	storeEvent=true;
		        	page=0;
		        	//System.out.println(npcKind);
		        	changPage();
		        }else if((keyState & GAME_A_PRESSED)!=0 ){
		        	RoleDataValue.roleMap = this.evenPoint;
		        	RoleDataValue.roleX = this.mainTargetX;
		        	RoleDataValue.roleY = this.mainTargetY;
		        	ConnectionServer.updateGameInf();
		        	battleFinish = true;
		        	roleState.roleStateStart();
		        	display.setCurrent(roleState);
		        }
		        //-------------------------------------------------------------------
			}
	    }
	    
	    //****************人物移動步伐****************//
	    private void tick()
		{
			if (mainSprite.getRefPixelY() < mainTargetY) {//向下移動
	            //if((tick % 2) == 0) {
	                mainSprite.move(0, step);
	                checkSprite.move(0, step);
	                
	                if(viewState)
	                {	if(! collision) makeWord.setView(viewTargetX, viewTargetY-(step),
	                									  this.getWidth(), this.getHeight());
	                	viewState = false;	}
	                	else
	                	{	makeWord.setView(viewTargetX, viewTargetY, this.getWidth(), this.getHeight());}
	                
	                checkViewWindow();
	                
	                //mainSprite.setFrameSequence(new int[]{0, 1, 2});
	                mainSprite.nextFrame();
	                mainSprite.nextFrame();
	            //}
	            //this.tick++;
	        }else if (mainSprite.getRefPixelY() > mainTargetY) {//向上移動
	            //if((tick % 2) == 0) {
	            	makeWord.spriteMove(0, -(step));
	                makeWord.checkSpriteMove(0, -(step));
	                
	                if(viewState)
	                {	if(! collision) makeWord.setView(viewTargetX, viewTargetY+(step),
	                												this.getWidth(), this.getHeight());
	                	viewState = false;	}
	                	else	
	                	{	makeWord.setView(viewTargetX, viewTargetY, this.getWidth(), this.getHeight());	}
	                
	                checkViewWindow();
	                
	                //mainSprite.setFrameSequence(new int[]{0, 1, 2});
	                mainSprite.nextFrame();
	                mainSprite.nextFrame();
	            //}
	            //this.tick++;
	        }else if (mainSprite.getRefPixelX() < mainTargetX) {//向右移動
	            //if((this.tick % 2) == 0) {
	            	makeWord.spriteMove(step, 0);
	                makeWord.checkSpriteMove(step, 0);
	                
	                if(viewState)
	                {	if(! collision) makeWord.setView(viewTargetX-(step), viewTargetY,
	                												this.getWidth(), this.getHeight());
	                	viewState = false;	}
	                	else	
	                	{	makeWord.setView(viewTargetX, viewTargetY, this.getWidth(), this.getHeight());}
	               
	               checkViewWindow();
	               
	                //mainSprite.setFrameSequence(new int[]{0, 1, 2});	
	                mainSprite.nextFrame();
	                mainSprite.nextFrame();
	            //}
	            //this.tick++;
	        }else if (mainSprite.getRefPixelX() > mainTargetX) {//向左移動
	            //if((this.tick % 2) == 0) {
	            	makeWord.spriteMove((-step), 0);
	                makeWord.checkSpriteMove((-step), 0);
	                
	                if(viewState)
	                {	if(! collision) makeWord.setView(viewTargetX+(step), viewTargetY,
	                												this.getWidth(), this.getHeight());
	                	viewState = false;	}
	                	else	
	                	{	makeWord.setView(viewTargetX, viewTargetY, this.getWidth(), this.getHeight());	}
	                
	                checkViewWindow();
	                
	                //mainSprite.setFrameSequence(new int[]{0, 1, 2});
	                mainSprite.nextFrame();
	                mainSprite.nextFrame();
	            //}
	            //this.tick++;
	            }
			else
			{
				//mainSprite.setFrameSequence(new int[]{0, 1, 2});
				//mainSprite.setFrame(0);
				//mainSprite.nextFrame();
				//mainSprite.nextFrame();
				//mainSprite.setFrame(0);
				//tick = 0;
				luckState = true;
			}
		}
		
		
		//********偵測碰撞(人物行走於地圖)*******//
		private void checkCollision(int key)
		{
			//System.out.println("Check碰撞中......");
			
			if( checkSprite.collidesWith(cantMoveLayer, true) )
			{
				//System.out.println("碰撞...");
				collision = true;
				
				if(key == LEFT_PRESSED)
				{
					mainTargetX += step;
					makeWord.spriteMove(step, 0);
					makeWord.checkSpriteMove(step, 0);
					//makeWord.setView(viewTargetX+step, viewTargetY, this.getWidth(), this.getHeight());
				}
               else if(key == RIGHT_PRESSED)
               {
               		mainTargetX -= step;
               		makeWord.spriteMove(-(step), 0);
               		makeWord.checkSpriteMove(-(step), 0);
               		//makeWord.setView(viewTargetX-step, viewTargetY, this.getWidth(), this.getHeight());
               }
               else if(key == UP_PRESSED)
               {
               		mainTargetY += step;
               		makeWord.spriteMove(0, step);
               		makeWord.checkSpriteMove(0, step);
               		//makeWord.setView(viewTargetX, viewTargetY+step, this.getWidth(), this.getHeight());
               }
               else if(key == DOWN_PRESSED)
               {
               		mainTargetY -= step;
               		makeWord.spriteMove(0, -step);
                    makeWord.checkSpriteMove(0, -step);
                    //makeWord.setView(viewTargetX, viewTargetY-step, this.getWidth(), this.getHeight());
               }
             
			}else collision = false;
		}
		
		
		//********偵測NPC碰撞(人物行走於地圖)*******//
		private void checkNpcCollision(int key, int num)
		{
			
			if( checkSprite.collidesWith(npcSprite[num], false) )
			{
				this.npcKind = mapImfor.getNpcKind(this.evenPoint, num);
				
				if(key == LEFT_PRESSED)
				{
					mainTargetX += step;
					makeWord.spriteMove(step, 0);
					makeWord.checkSpriteMove(step, 0);
					makeWord.setView(viewTargetX+step, viewTargetY, this.getWidth(), this.getHeight());		
				}
               else if(key == RIGHT_PRESSED)
               {
               		mainTargetX -= step;
               		makeWord.spriteMove(-(step), 0);
               		makeWord.checkSpriteMove(-(step), 0);
               		makeWord.setView(viewTargetX-step, viewTargetY, this.getWidth(), this.getHeight());
               }
               else if(key == UP_PRESSED)
               {
               		mainTargetY += step;
               		makeWord.spriteMove(0, step);
               		makeWord.checkSpriteMove(0, step);
               		makeWord.setView(viewTargetX, viewTargetY+step, this.getWidth(), this.getHeight());
               }
               else if(key == DOWN_PRESSED)
               {
               		mainTargetY -= step;
               		makeWord.spriteMove(0, -step);
                    makeWord.checkSpriteMove(0, -step);
                    makeWord.setView(viewTargetX, viewTargetY+step, this.getWidth(), this.getHeight());
               }
             
			}
		}
		

		
		//********偵測寶物碰撞(人物行走於地圖)*******//
		private void checkGemCollision(int nom)
		{
			
			//*******寶物碰撞偵測******//
			if( checkSprite.collidesWith(gemSprite[nom], true) )
			{
				//取得寶箱物品
				System.out.println("getGemItem");
				for(int i=0; i<mapImfor.getGemAmount(this.evenPoint); i++)
				{
					mainValue.addItem(mapImfor.getGemContents(this.evenPoint, i), false, 1);
				}
				ConnectionServer.updateGameInf();//更新主角物品
				
				//已取得寶箱物品
				mapImfor.setGem(this.evenPoint, "0");
				ConnectionServer.updateGameEvent(0);
				
				//remove gem
				makeWord.remove(makeWord.getLayerAt(1));
				
			}
		}
		
		//******傳送點偵測碰撞******//
		public void checkLinkCollision()
		{
			int weight=mainTargetX+12;
			int height=mainTargetY+19;
			int leftLimitX, leftLimitY;
			int limitX, limitY;
			
			for(int i=0; i<4; i++)
			{
				leftLimitX=mapImfor.getDeliverX(i, this.evenPoint);
				leftLimitY=mapImfor.getDeliverY(i, this.evenPoint);
				
				if(i<2)
				{
					limitX=mapImfor.getDeliverX(i, this.evenPoint)+64;
					limitY=mapImfor.getDeliverY(i, this.evenPoint)+16;
				}else
				{
					limitX=mapImfor.getDeliverX(i, this.evenPoint)+16;
					limitY=mapImfor.getDeliverY(i, this.evenPoint)+64;
				}
				
				if(((weight>leftLimitX) & ((weight)<limitX)) &
							(height>leftLimitY) & ((height)<limitY) ) {	resetWord(i, evenPoint);	}
			}
		}
		
		//*******偵測結界的碰撞*********//
		private void checkStopCollision(int key, int num)
		{
			if(key == LEFT_PRESSED)
				{
					if(((mainTargetY+19)>mapImfor.getStopY(num, this.evenPoint)) & 
							((mainTargetY+19)<(mapImfor.getStopY(num, this.evenPoint)+64)))
					{
						if(((mainTargetX-16)<mapImfor.getStopX(num, this.evenPoint)))
						{
							mainTargetX += step;
							makeWord.spriteMove(step, 0);
							makeWord.checkSpriteMove(step, 0);
							makeWord.setView(viewTargetX+step, viewTargetY, this.getWidth(), this.getHeight());
						}
					}
				}
               else if(key == RIGHT_PRESSED)
               {
               		if(((mainTargetY+19)>mapImfor.getStopY(num, this.evenPoint)) & 
							((mainTargetY+19)<(mapImfor.getStopY(num, this.evenPoint)+64)))
					{
						if(((mainTargetX+8)>(mapImfor.getStopX(num, this.evenPoint))))
						{
		               		mainTargetX -= step;
		               		makeWord.spriteMove(-(step), 0);
		               		makeWord.checkSpriteMove(-(step), 0);
		               		makeWord.setView(viewTargetX-step, viewTargetY, this.getWidth(), this.getHeight());
		       			}
		       		}
		       }
               else if(key == UP_PRESSED)
               {
               		if(((mainTargetX+12)>mapImfor.getStopX(num, this.evenPoint)) & 
							((mainTargetX+12)<(mapImfor.getStopX(num, this.evenPoint)+64)))
					{
						if(((mainTargetY+3)<(mapImfor.getStopY(num, this.evenPoint))))
						{
		               		mainTargetY += step;
		               		makeWord.spriteMove(0, step);
		               		makeWord.checkSpriteMove(0, step);
		               		makeWord.setView(viewTargetX, viewTargetY+step, this.getWidth(), this.getHeight());
		    			}	
		    		}
		       }
               else if(key == DOWN_PRESSED)
               {
               		if(((mainTargetX+12)>mapImfor.getStopX(num, this.evenPoint)) & 
							((mainTargetX+12)<(mapImfor.getStopX(num, this.evenPoint)+64)))
					{
						if(((mainTargetY+22)>(mapImfor.getStopY(num, this.evenPoint))))
						{
		               		mainTargetY -= step;
		               		makeWord.spriteMove(0, -step);
		                    makeWord.checkSpriteMove(0, -step);
		                    makeWord.setView(viewTargetX, viewTargetY+step, this.getWidth(), this.getHeight());
		    			}
		    		}
		       }
		}
		
		//********亂數產生怪物，並且偵測碰撞*******//
		private static BattleFrame battle = null;
		private void callMonster()
		{			
			for(int i=0; i<monsterAmount; i++)
			{
				monsterCounter--;
				
				//產生亂數座標
				runMonsterTurnUp.runRandom();
				
				//判斷是否有碰撞到怪物
				if(runMonsterTurnUp.monsterCollision(mainTargetX, mainTargetY) & (monsterCounter == 0))
				{
					monsterCounter = RESET_MONSTER;
					
					//***畫出戰鬥畫面***//
					/*RoleDataValue.roleMap = this.evenPoint;
		        	RoleDataValue.roleX = this.mainTargetX;
		        	RoleDataValue.roleY = this.mainTargetY;
		        	ConnectionServer.updateGameInf();*/
					//callBattle("M50");
					
					final Graphics g = this.getGraphics();
					g.drawImage(bar,disWidth+40,disHeight+80,0);
					g.drawString("  遇  到  怪  物",disWidth+44,disHeight+84,0);
					flushGraphics();
					try{ 
        			 	Thread.sleep(200);
        			 }
		        	 catch(Exception e) {}
					battle.battleStart(this.evenPoint+1,"");
					battleFinish = true;
					display.setCurrent(battle);
						//System.out.println("戰鬥..........");
					
					break;
				}
			}
		}
		
		//呼叫戰鬥,非亂數
		public void callBattle(String monster){
			System.out.println(monster);
			storeEvent=false;
			npcKind = -1;
			otherEvent = false;
			otherEventFinish = false;
			final Graphics g = this.getGraphics();
			g.drawImage(bar,disWidth+40,disHeight+80,0);
			g.drawString("  進  入  戰  鬥",disWidth+44,disHeight+84,0);
			flushGraphics();
			try{ 
			 	Thread.sleep(200);
			 }
        	 catch(Exception e) {}
			battle.battleStart(this.evenPoint+1,monster);
			battleFinish = true;
			display.setCurrent(battle);
		}
		
		public void checkViewWindow()
		{
			if(this.getWidth() > (mapImfor.getColumn(this.evenPoint)*16))
			{
				viewTargetX = 0;
			}
			
			if(this.getHeight() > (mapImfor.getRow(this.evenPoint)*16))
			{
				viewTargetY = 0;
			}
			
			if(! collision) makeWord.setView(viewTargetX, viewTargetY, this.getWidth(), this.getHeight());
		}
		
		//喚醒Thread
		public void threadStart(){
			//if(RoleDataValue.name.equals("看門餘"))
				//makeWord.remove(makeWord.getLayerAt(2));
			battleFinish = false;
			display.setCurrent(this);
			//thread = new Thread(this);
			//thread.start();
		}
		
		//********實作TimerTask********//
		//private class gameTask extends TimerTask
		//{
	        public void run()
	        {
	        	while(true)
	        	{
	        		if(battleFinish){//如果在戰鬥,停止地圖thread
	        			if(!BattleFrame.battleFinish)
	        				battleFinish=false;
	        			 try{ 
	        			 	Thread.sleep(200); 
	        			 	continue;
	        			 }
			        	 catch(Exception e) {}
	        		}
	        		
			        try{ Thread.sleep(100); }
			        catch(Exception e) {}
			        	
			        processInput();
			        
			        if(!storeEvent)	tick();
			        	
			        //if(mapImfor.getMonster(this.evenPoint)) callMonster();
			        	
			        render();
			        	
			        if(!storeEvent)	checkLinkCollision();
            	}
	        }
		//}
		
		//********設定故事點********//
		public void setEvenPoint(int evenPoint)
		{
			this.evenPoint = evenPoint;
		}
		
		//********取得wordLayerCreate的class********//
		public wordLayerCreate getMakeWordClass()
		{
			return makeWord;
		}
		
//		-----------------------------商店對話選單---------------------------------------
		private Graphics storeG;
		
		private static Image img = null;
		
		private Image imgBack = null;

		private int menustate = 0;//目前選取到的選項
		private int itemSelect = 0;

		private int menuNum = 4;//共有幾個選項

		private int disWidth = (getWidth()-176)/2;//抓取螢幕的中心點

		private int disHeight = (getHeight()-208)/2;//抓取螢幕的中心點
		
		private String[] menuStr = {"  對話 ","  購買 ","  販賣 ","  離開 "};
		private String[] menuBuyStr = {"    買","  離開"};
		private String[] menuSellStr = {"    賣","  離開"};
		private String[] nowString = new String[4];
		private int moveY = 0;//偏移位置
		
		//private int moveItemX = 0;//物品名稱偏移量80
		//private int moveItemY = 0;//物品名稱偏移量64
		
		private int page = 0;//頁數
		private boolean drawItemMenu = false;//是否為要畫物品
		
		//-----------是否有商店觸發-----------
		private boolean storeEvent = false;//是否有商店
		private boolean itemEvent = true;//true時表示顯示商店的物品在清單中
		private boolean itemBuy = false;//當為false表示還沒確定要買,true表示確定要買
		private boolean talkAbout = false;//為true時,表示在對話
		private boolean whoTalkAbout = false;//目前是誰在說話false為npc,true為主角
		private boolean talkFinish = false;//是否已說完話
		//--------------DataBase-------------
		private static DataBaseReader db = new DataBaseReader();
		private String itemBuff[][] = new String[40][];//商店物品資訊暫存
		//private String itemPole[][] = new String[13][];//釣竿物品
		//private String itemGe[][] = new String[12][];//防具物品
		private String itemOther[][] = new String[40][];//釣餌跟補品
		//private String itemJew[][] = new String[4][];//飾品
		private int itemTotal = 0;//釣具店物品總數
		private int storeIndex=0;//商店物品索引
		//0守衛  1阿婆   *2特    3指路    4旅    *5防  6傳(布丁)  7天神  
		//*8竿  *9餌    10守衛(活) 11 12看門餘  13神父  14千年老樹
		//15欠人打 16竿神  17水井  18小雞  19守護者  20魚達人

		//private int storeNum = -1;//商店種類,釣竿店設8,防具設5,釣餌補品設9,特殊設2
		private String[] itemNameArray = {"Item","","Jewelry","","Battle","Protect","	Event",
				"Monster","Pole","Bait","Tonic","Talk",""};
		private int dataFieldNum[] = {6,4,11,1,4,10,4,11,12,7,7,4,9,6,3};//每一資料庫欄位數
		private Image img2[] = new Image[40];//商店物品圖片Buffer
		private Image imgOther[] = new Image[40];
		private Image buyItemImg = createImage("/image/gameInterface/buyItem.png");//買物品時的選單圖片
		private Image nowPeoImg = null;//對話時人物介面圖示
		private Image converImg = createImage("/image/gameInterface/Conversation.png");//對話方塊
		private Image buyMenuImg = createImage("/image/gameInterface/buyMenu.png");//買物品時的選單
		private Image mapBar = createImage("/image/gameInterface/mapBar.png");//地圖bar
		private Image bar = createImage("/image/gameInterface/bar.png");//遇怪物或得到物品
		
		//private static String itemAmount[] = new String[99];
		
		private boolean otherEventFinish = false;//主角自言自語是否完成
		private boolean otherEvent = false;//主角自言自語
		
		//private RoleDataValue roleData = new RoleDataValue(" ");

		//建立圖片
		public Image createImage(String pathImg) {
			img = null;
			try {
				img = Image.createImage(pathImg);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return img;
		}
		
		//畫商店的menu在地圖上
		public void drawDisplay(){
			if(storeEvent){
				storeG = getGraphics();
				//對話圖片
				storeG.drawImage(converImg,disWidth+0,disHeight+144,0);
				if(page==0 & !otherEvent){//一般對話時
					render();
					try{
						usualTalk = true;
						setTalkAbout(0);
						talkAbout(0);
						talkAbout = false;
					}catch(Exception e){e.printStackTrace();}
					if(nowPeoImg!=null){
						storeG.drawImage(nowPeoImg,disWidth+0,disHeight+80,0);
					}
				}
				storeG.drawImage(buyMenuImg,disWidth+121,disHeight+77,0);
				if(page==1 || page==2){//如果要買東西時,顯示物品欄
					storeG.drawImage(buyItemImg,disWidth+3,disHeight+0,0);
				}
				flushGraphics();
				changeMenu();//把選單的字畫上去
			}
		}
		
		//主角自言自語對話
		public void roleOwnEvent(int roleOwnNum){
			storeEvent=true;
			otherEvent = true;
			otherEventFinish = true;
        	page=0;
        	changPage();
			talkAbout = true;
			whoTalkAbout = true;
			talkTatol = new String[1];
			talkTatol[0] = db.readDBField(14,roleOwnNum,2);
			eventTalk();
		}
		
		private String talkTatol[] = null;
		private String talkTmp[] = null;
		private String npcName = "";
		private int talkNum = 0;
		//設定對話,kind=1~5
		public boolean setTalkAbout(int kind){
			if(kind==-1)
				return false;
			String tmp = db.readDBField(12,npcKind+1,4+kind);
			npcName = db.readDBField(12,npcKind+1,2)+" 說:";
			tmp = tmp.substring(3,tmp.length());
			if(tmp.equals("NO"))
				return false;
			talkTatol = split(tmp,"#");
			return true;
		}
		
		//對話
		private int talkIndex = -3;
		private int talkEnd = 0;
		private boolean usualTalk = false;
		public void talkAbout(int n){
			if(!talkFinish){
				String tmp = talkTatol[n];
				int line = 0;
				//判斷對話有幾行
				if(tmp.length() % 13==0)
					line = tmp.length() / 13;
				else
					line = (tmp.length() / 13)+1;
				
				char charTmp[] = tmp.toCharArray();
				talkTmp = new String[line];
				for(int i=0,k=0;i<line;i++){
					talkTmp[i]="";
					while(true){
						talkTmp[i]+=charTmp[k];
						k++;
						if(k%13==0 || k>=charTmp.length) break;
					}
					//System.out.println("k"+k);
				}
				talkFinish = true;
				talkIndex = -3;
				talkEnd = 0;
				//talkNum = 0;
				//whoTalkAbout = false;
			}
			if(usualTalk & !otherEvent){
				storeG.setColor(255, 255, 255);
				if(npcKind==25)
					storeG.drawString(RoleDataValue.name + " 說:", disWidth + 7, disHeight + 148, 0);
				else
					storeG.drawString(npcName, disWidth + 7, disHeight + 148, 0);
			}
			if(talkTmp.length<=3){
				talkIndex=0;
				talkEnd = talkTmp.length;
				talkFinish = false;
				if(!usualTalk && createStory.getTalkValue()>0){
					whoTalkAbout = !whoTalkAbout;
					//System.out.println("ppppppp"+createStory.getTalkValue());
				}
			}else{
				talkIndex+=3;
				talkEnd+=3;
				if(talkEnd>=talkTmp.length){
					talkEnd = talkTmp.length;
					talkFinish = false;
					//talkNum = 0;
					whoTalkAbout = !whoTalkAbout;
				}
			}
			try{
				for(int i=talkIndex,k=14;i<talkEnd;i++,k+=14){
					storeG.drawString(talkTmp[i], disWidth + 7, disHeight + 148+k, 0);
				}
			}catch(Exception e){e.printStackTrace();}
		}
		
		//事件對話
		public void eventTalk(){
			storeG.setColor(0, 0, 0);
			storeG.fillRect(0, 0, this.getWidth(), this.getHeight());
			makeWord.paint(storeG, 0, 0);
			storeG.drawImage(converImg,disWidth+0,disHeight+144,0);
			storeG.drawImage(buyMenuImg,disWidth+121,disHeight+77,0);
			storeG.setColor(255, 255, 255);
			if(otherEvent)
				whoTalkAbout = true;
			if(npcKind==25 || whoTalkAbout){//換主角說話時
				//System.out.println("11");
				storeG.drawImage(RoleDataValue.roleImg,disWidth+0,disHeight+80,0);
				storeG.drawString(RoleDataValue.name + " 說:", disWidth + 7, disHeight + 148, 0);
				//System.out.println("22");
			}
			else{
				if( !(nowPeoImg==null) )
					storeG.drawImage(nowPeoImg,disWidth+0,disHeight+80,0);
				storeG.drawString(npcName, disWidth + 7, disHeight + 148, 0);
			}

			if(talkAbout){//抓取事件點
				talkAbout(talkNum);
				if(!talkFinish){
					talkNum++;
					if(talkNum>=talkTatol.length){//對話結束
						talkNum = 0;
						talkAbout = false;
						otherEvent = false;
						if(npcKind==12){
							callBattle("M49");
						}else if(npcKind==16){
							callBattle("M50");
						}else if(npcKind==30){
							callBattle("M47");
						}else if(npcKind==31){
							callBattle("M48");
						}
					}
				}
				if(talkAbout)
					storeG.fillTriangle(disWidth+161,disHeight+196,disWidth+168,disHeight+196,disWidth+164,disHeight+200);
			}else{//如果npc沒有事件時,出現一般對話
				setTalkAbout(0);
				talkAbout(0);
				talkAbout = false;
				whoTalkAbout = false;
			}
				
			
		}
		
		//改變頁面時，初使化
		public void changPage(){
			switch(page){
				case 0://對話選單
					if(!otherEvent){
						System.out.println("npcKind:"+npcKind);
						//當是共用npc圖片時
						if( npcKind==0 || npcKind==6){//守衛
							nowPeoImg = createImage("/image/gameInterface/npc/npc_0.png");
						}else if( npcKind==7 || npcKind==11 || npcKind==17 || npcKind==18 ){//傳送師
							nowPeoImg = createImage("/image/gameInterface/npc/npc_7.png");
						}else if( npcKind==19 || npcKind==20 || npcKind==21 ){//天神
							nowPeoImg = createImage("/image/gameInterface/npc/npc_19.png");
						}else{//當有npc圖片時
							nowPeoImg = createImage("/image/gameInterface/npc/npc_"+ npcKind +".png");
						}
					}
					
					if(npcKind==2 || npcKind==5 || npcKind==8 || npcKind==9){
						for(int i=0;i<nowString.length;i++)
							nowString[i] = menuStr[i];
						menuNum = menuStr.length;//MENU 數量
						menustate = 0;
						moveY = 0;
					}else{
						if(npcKind==4 && RoleDataValue.nowEvenSchedule[0].indexOf("4")!=-1)
							nowString[2] = "休息";
						else if(npcKind==0)
							nowString[2] = "對話";
						else
							nowString[2] = "對話";
						
						nowString[3] = "離開";
						menuNum = 2;
						menustate = 2;
						moveY = -20;
					}
					
					
					break;
				case 1://買東西
					for(int i=2;i<nowString.length;i++)
						nowString[i] = menuBuyStr[i-2];
					menuNum = menuBuyStr.length;
					menustate = 2;
					moveY = -20;
					if(!drawItemMenu){
						if(npcKind!=9){
							itemSelect=0;
							storeIndex=0;
							int n=0;
							String tmp[] = null;
							//商店物品從資料庫抓出
							for(int i=1;i<=db.getDBCount(npcKind);i++){
								tmp = db.readDBRecord(npcKind,i);
								if(!tmp[dataFieldNum[npcKind]-1].equals("0")){
									itemBuff[n] = tmp;
									n++;
								}
							}
							itemTotal = n;
							for(int i=0;i<itemTotal;i++){
								img2[i] = createImage("/image/" + itemNameArray[npcKind] +"/"+ itemBuff[i][1] + ".png");
							}
							System.out.println("Total:"+itemTotal);
						}else{
							itemSelect=0;
							storeIndex=0;
							itemTotal=40;
							for(int i=0;i<itemOther.length;i++){
								itemBuff[i] = itemOther[i];
								img2[i] = imgOther[i];
							}
						}
					}
					drawItemMenu = true;
					break;
				case 2://賣東西					
					if(!drawItemMenu || itemEvent == false){
						String itemStr = "";
						switch (npcKind) {
							case 2://賣特殊
								itemStr = "J";
								break;
							case 5://賣防具
								itemStr = "G";
								break;
							case 8://賣釣竿
								itemStr = "P";
								break;
							case 9://賣補品
								itemStr = "B";
								break;
						}
						int n=0;
						String tmp[] = null;
						mainValue.setHashEnume();
						for(;RoleDataValue.enu.hasMoreElements();){
							tmp = (String[])RoleDataValue.enu.nextElement();
							//如果編號大於要搜尋資料庫的數量則不搜尋
							if(tmp[0].indexOf(itemStr)==-1)
								continue;
							System.out.println("Find:"+tmp[0]);
							itemBuff[n] = db.readDBRecord(npcKind,
									Int( tmp[0].substring(1,tmp[0].length()) ));
							//itemAmount[n] = tmp[1];
							n++;
						}
						itemTotal = n;//搜尋到符合的物品數量
						//img2 = new Image[itemTotal];
						for(int i=0;i<itemTotal;i++){
							img2[i] = createImage("/image/" + itemNameArray[npcKind] +"/"+ itemBuff[i][1] + ".png");
						}
						System.out.println("Total:"+itemTotal+"|"+page);
						drawItemMenu = true;
					}
					if(itemTotal==0){
						itemSelect=0;
						storeIndex=0;
		        		drawItemMenu = false;
		        		storeG.fillRect(0, 0, this.getWidth(), this.getHeight());
						makeWord.paint(storeG, 0, 0);
						flushGraphics();
						itemEvent = true;
		        		page=0;
		        		changPage();
					}else{
						for(int i=2;i<nowString.length;i++)
							nowString[i] = menuSellStr[i-2];
						menuNum = menuSellStr.length;
						menustate = 2;
						moveY = -20;
					}
					break;
				case 3://顯示物品
					break;
			}
			drawDisplay();
		}

		//	選單切換
		private void changeMenu() {
			if(drawItemMenu){//畫物品資訊在itemMenu
				setFont(0);
				storeG.drawImage(buyItemImg,disWidth+3,disHeight+0,0);
				
				storeG.setColor(255,255,255);
				storeG.drawString(String.valueOf(RoleDataValue.money),disWidth+117,disHeight+13,0);//Show Money
				switch (itemSelect) {
				case 0:
					//Select----------------------------
					storeG.setColor(255, 255, 255);
					storeG.drawString(itemBuff[0+storeIndex][2], disWidth + 50, disHeight + 15, 0);
					storeG.drawImage(img2[0+storeIndex],disWidth+10,disHeight+6,0);
					showItemInf(0+storeIndex);
					//----------------------------------
					storeG.setColor(69, 118, 181);
					if(storeIndex+2<=itemTotal){//當只剩二個物品在選單時印出
						storeG.drawString(itemBuff[1+storeIndex][2], disWidth + 50, disHeight + 48, 0);
						storeG.drawImage(img2[1+storeIndex],disWidth+10,disHeight+39,0);
					}
					if(storeIndex+3<=itemTotal){//當只剩三個物品在選單時印出
						storeG.drawString(itemBuff[2+storeIndex][2], disWidth + 50, disHeight + 81, 0);
						storeG.drawImage(img2[2+storeIndex],disWidth+10,disHeight+72,0);
					}
					if(storeIndex+4<=itemTotal){//四個物品在選單時印出
						storeG.drawString(itemBuff[3+storeIndex][2], disWidth + 50, disHeight + 114, 0);
						storeG.drawImage(img2[3+storeIndex],disWidth+10,disHeight+105,0);
					}
					break;
				case 1:
					storeG.setColor(69, 118, 181);
					storeG.drawString(itemBuff[0+storeIndex][2], disWidth + 50, disHeight + 15, 0);
					storeG.drawImage(img2[0+storeIndex]/*createImage("/image/Pole/"+itemBuff[0+storeIndex][0]+".png")*/,disWidth+10,disHeight+6,0);
					//Select----------------------------
					storeG.setColor(255, 255, 255);
					if(storeIndex+2<=itemTotal){
						storeG.drawString(itemBuff[1+storeIndex][2],disWidth + 50, disHeight + 48, 0);
						storeG.drawImage(img2[1+storeIndex]/*createImage("/image/Pole/"+itemBuff[1+storeIndex][0]+".png")*/,disWidth+10,disHeight+39,0);
						showItemInf(1+storeIndex);
					}
					//----------------------------------
					storeG.setColor(69, 118, 181);
					if(storeIndex+3<=itemTotal){
						storeG.drawString(itemBuff[2+storeIndex][2], disWidth + 50, disHeight + 81, 0);
						storeG.drawImage(img2[2+storeIndex]/*createImage("/image/Pole/"+itemBuff[2+storeIndex][0]+".png")*/,disWidth+10,disHeight+72,0);
					}
					if(storeIndex+4<=itemTotal){
						storeG.drawString(itemBuff[3+storeIndex][2], disWidth + 50, disHeight + 114, 0);
						storeG.drawImage(img2[3+storeIndex]/*createImage("/image/Pole/"+itemBuff[3+storeIndex][0]+".png")*/,disWidth+10,disHeight+105,0);
					}
					break;
				case 2:
					storeG.setColor(69, 118, 181);
					storeG.drawString(itemBuff[0+storeIndex][2], disWidth + 50, disHeight + 15, 0);
					storeG.drawImage(img2[0+storeIndex]/*createImage("/image/Pole/"+itemBuff[0+storeIndex][0]+".png")*/,disWidth+10,disHeight+6,0);
					storeG.setColor(69, 118, 181);
					if(storeIndex+2<=itemTotal){
						storeG.drawString(itemBuff[1+storeIndex][2], disWidth + 50, disHeight + 48, 0);
						storeG.drawImage(img2[1+storeIndex]/*createImage("/image/Pole/"+itemBuff[1+storeIndex][0]+".png")*/,disWidth+10,disHeight+39,0);
					}
					//Select----------------------------
					storeG.setColor(255, 255, 255);
					if(storeIndex+3<=itemTotal){
						storeG.drawString(itemBuff[2+storeIndex][2], disWidth + 50, disHeight + 81, 0);
						storeG.drawImage(img2[2+storeIndex]/*createImage("/image/Pole/"+itemBuff[2+storeIndex][0]+".png")*/,disWidth+10,disHeight+72,0);
						showItemInf(2+storeIndex);
					}
					//----------------------------------
					storeG.setColor(69, 118, 181);
					if(storeIndex+4<=itemTotal){
						storeG.drawString(itemBuff[3+storeIndex][2], disWidth + 50, disHeight + 114, 0);
						storeG.drawImage(img2[3+storeIndex]/*createImage("/image/Pole/"+itemBuff[3+storeIndex][0]+".png")*/,disWidth+10,disHeight+105,0);
					}
					break;
				case 3:
					storeG.setColor(69, 118, 181);
					storeG.drawString(itemBuff[0+storeIndex][2], disWidth + 50, disHeight + 15, 0);
					storeG.drawImage(img2[0+storeIndex]/*createImage("/image/Pole/"+itemBuff[0+storeIndex][0]+".png")*/,disWidth+10,disHeight+6,0);
					if(storeIndex+2<=itemTotal){
						storeG.drawString(itemBuff[1+storeIndex][2], disWidth + 50, disHeight + 48, 0);
						storeG.drawImage(img2[1+storeIndex]/*createImage("/image/Pole/"+itemBuff[1+storeIndex][0]+".png")*/,disWidth+10,disHeight+39,0);
					}
					if(storeIndex+3<=itemTotal){
						storeG.drawString(itemBuff[2+storeIndex][2], disWidth + 50, disHeight + 81, 0);
						storeG.drawImage(img2[2+storeIndex]/*createImage("/image/Pole/"+itemBuff[2+storeIndex][0]+".png")*/,disWidth+10,disHeight+72,0);
					}
					//Select----------------------------
					storeG.setColor(255, 255, 255);
					if(storeIndex+4<=itemTotal){
						storeG.drawString(itemBuff[3+storeIndex][2], disWidth + 50, disHeight + 114, 0);
						storeG.drawImage(img2[3+storeIndex]/*createImage("/image/Pole/"+itemBuff[3+storeIndex][0]+".png")*/,disWidth+10,disHeight+105,0);
						showItemInf(3+storeIndex);
					}
					//----------------------------------
					break;
				}
			}
			if(storeEvent==true && itemEvent==true){//買物品時顯示
				setFont(0);
				switch (menustate) {
					case 0:
						//Select----------------------------
						storeG.setColor(255, 255, 255);
						storeG.drawString(nowString[0], disWidth + 130, disHeight + 79, 0);
						//----------------------------------
						storeG.setColor(69, 118, 181);
						storeG.drawString(nowString[1], disWidth + 130, disHeight + 94, 0);
						storeG.drawString(nowString[2], disWidth + 130, disHeight + 109, 0);
						storeG.drawString(nowString[3], disWidth + 130, disHeight + 124, 0);
						break;
					case 1:
						if(page==0 & (npcKind==2 || npcKind==5 || npcKind==8 || npcKind==9)){
							storeG.setColor(69, 118, 181);
							storeG.drawString(nowString[0], disWidth + 130, disHeight + 79, 0);
							//Select----------------------------
							storeG.setColor(255, 255, 255);
							storeG.drawString(nowString[1], disWidth + 130, disHeight + 94, 0);
							//----------------------------------
						}
						storeG.setColor(69, 118, 181);
						storeG.drawString(nowString[2], disWidth + 130, disHeight + 109, 0);
						storeG.drawString(nowString[3], disWidth + 130, disHeight + 124, 0);
						break;
					case 2:
						storeG.setColor(69, 118, 181);
						if(page==0 & (npcKind==2 || npcKind==5 || npcKind==8 || npcKind==9)){
							storeG.setColor(69, 118, 181);
							storeG.drawString(nowString[0], disWidth + 130, disHeight + 79, 0);
							storeG.setColor(69, 118, 181);
							storeG.drawString(nowString[1], disWidth + 130, disHeight + 94, 0);
						}
						//Select----------------------------
						storeG.setColor(255, 255, 255);
						storeG.drawString(nowString[2], disWidth + 130, disHeight + 109 + moveY, 0);
						//----------------------------------
						storeG.setColor(69, 118, 181);
						storeG.drawString(nowString[3], disWidth + 130, disHeight + 124 + moveY, 0);
						break;
					case 3:
						storeG.setColor(69, 118, 181);
						if(page==0 & (npcKind==2 || npcKind==5 || npcKind==8 || npcKind==9)){
							storeG.setColor(69, 118, 181);
							storeG.drawString(nowString[0], disWidth + 130, disHeight + 79, 0);
							storeG.drawString(nowString[1], disWidth + 130, disHeight + 94, 0);
						}
						storeG.drawString(nowString[2], disWidth + 130, disHeight + 109 + moveY, 0);
						//Select----------------------------
						storeG.setColor(255, 255, 255);
						storeG.drawString(nowString[3], disWidth + 130, disHeight + 124 + moveY, 0);
						//----------------------------------
						break;
					}
			}
			flushGraphics();
		}
		
		//字串切割
		public String[] split(String str, String regex) {
			int count=0;
			int index=0;

			for(int i=0;i<str.length();i++){
				if( str.indexOf(regex, index)!=-1 ){
					count++;
					index=str.indexOf(regex, index) + 1;
				}
			}
			if(str.length()!=index) count++;
			index=0;
			String strsplit[] = new String[count];
			int indexarray[] = new int[count];
			for(int i=0;i<count;i++){
				if( str.indexOf(regex, index)!=-1 ){
					index=str.indexOf(regex, index) + 1;
					indexarray[i]=index;
				}
			}
			
			index=0;
			for(int i=0;i<count-1;i++){
				strsplit[i] = str.substring(index, indexarray[i]-1);
				index = indexarray[i];
			}
			strsplit[count-1] = str.substring(index, str.length());
			
			return strsplit;
		}
		
		//字型設定
		public void setFont(int size) {
			Font font = Font.getFont(Font.FACE_SYSTEM, 0, size);
			storeG.setFont(font);
		}
		
		public void showItemInf(int index){
			String proStr = "";
			storeG.setColor(255,255,255);
			storeG.fillRect(disWidth + 123, disHeight + 36,32,32);
			if(npcKind==8 && RoleDataValue.pole!=0){
				storeG.drawImage(RoleDataValue.poleImg,disWidth+123,disHeight+36,0);
			}else if(npcKind==2 && RoleDataValue.jewelry!=0){
				storeG.drawImage(RoleDataValue.jewelryImg,disWidth+123,disHeight+36,0);
			}else if(npcKind==9 && RoleDataValue.bait!=0){
				storeG.drawImage(RoleDataValue.baitImg,disWidth+123,disHeight+36,0);
			}else if(npcKind==5){
				if(itemBuff[index][8].equals("1") && RoleDataValue.proHead!=0){
					storeG.drawImage(RoleDataValue.proHeadImg,disWidth+123,disHeight+36,0);
					proStr = "頭部";
				}
				else if(itemBuff[index][8].equals("2") && RoleDataValue.proBody!=0){
					storeG.drawImage(RoleDataValue.proBodyImg,disWidth+123,disHeight+36,0);
					proStr = "身體";
				}
				else if(itemBuff[index][8].equals("3") && RoleDataValue.proWaist!=0){
					storeG.drawImage(RoleDataValue.proWaistImg,disWidth+123,disHeight+36,0);
					proStr = "腰部";
				}
				else if(itemBuff[index][8].equals("4") && RoleDataValue.proFoot!=0){
					storeG.drawImage(RoleDataValue.proFootImg,disWidth+123,disHeight+36,0);
					proStr = "腳部";
				}
				else if(itemBuff[index][8].equals("5") && RoleDataValue.proHand!=0){
					storeG.drawImage(RoleDataValue.proHandImg,disWidth+123,disHeight+36,0);
					proStr = "手部";
				}
			}
			storeG.drawImage(converImg,disWidth+0,disHeight+144,0);
			if(npcKind==8 || npcKind==2 || npcKind==5){
				storeG.drawString("攻擊(ATK)+"+String.valueOf(getAtk(index)),disWidth + 8,disHeight + 147,0);
				storeG.drawString("防禦(DEF)+"+String.valueOf(getDef(index)),disWidth + 8,disHeight + 161,0);
				if(npcKind==8 || npcKind==2)
					storeG.drawString( page==2?"價格:"+Int(itemBuff[index][9])/2:"價格:"+itemBuff[index][9] ,disWidth + 8,disHeight + 189,0);
				else if(npcKind==5){
					storeG.drawString("裝備位置:"+proStr,disWidth + 8,disHeight + 175,0);
					storeG.drawString(page==2?"價格:"+Int(itemBuff[index][7])/2:"價格:"+itemBuff[index][7],disWidth + 8,disHeight + 189,0);
				}
				storeG.drawString("目前數量:"+mainValue.getItemAmount(itemBuff[index][0]),disWidth + 105,disHeight + 189,0);
			}else if(npcKind==9){
				if(index<25)
					storeG.drawString("搭配釣竿:",disWidth + 8,disHeight + 147,0);
				else
					storeG.drawString("增加HP值:",disWidth + 8,disHeight + 147,0);
				storeG.drawString("「"+itemBuff[index][3]+"」",disWidth + 1,disHeight + 161,0);
				storeG.drawString(page==2?"價格:"+Int(itemBuff[index][5])/2:"價格:"+itemBuff[index][5],disWidth + 8,disHeight + 189,0);
				storeG.drawString("目前數量:"+mainValue.getItemAmount(itemBuff[index][0]),disWidth + 105,disHeight + 189,0);
			}
		}
		
		//設定商店的物品
		public void setStoreValue(){
			String tmp[] = null;
			int n=0;
			//商店物品從資料庫抓出
			for(int i=1;i<=db.getDBCount(9);i++){
				tmp = db.readDBRecord(9,i);
				if(!tmp[dataFieldNum[9]-1].equals("0")){
					itemOther[n] = tmp;
					n++;
				}
			}
			itemTotal = n;
			for(int i=0;i<itemTotal;i++){
				imgOther[i] = createImage("/image/" + itemNameArray[9] +"/"+ itemOther[i][1] + ".png");
			}
			//把捕品道具加入
			for(int i=1;i<=db.getDBCount(10);i++){
				tmp = db.readDBRecord(10,i);
				if(!tmp[5].equals("0")){
					itemOther[n] = tmp;
					n++;
				}
			}
			for(int i=itemTotal;i<n;i++){
				imgOther[i] = createImage("/image/" + itemNameArray[10] +"/"+ itemOther[i][1] + ".png");
			}
			itemTotal = n;
		}
		
		//抓取裝備累計的攻擊力
		public int getAtk(int num){
			int value = 0;
			if(npcKind==8){
				value = Int(itemBuff[num][3])+Int(itemBuff[num][6]);
			}else if(npcKind==2){
				value = Int(itemBuff[num][3])+Int(itemBuff[num][7]);
			}else if(npcKind==5){
				value = Int(itemBuff[num][5]);
			}
			return value;
		}
		
		//抓取裝備累計的防禦
		public int getDef(int num){
			int value = 0;
			if(npcKind==8){
				value = Int(itemBuff[num][5]);
			}else if(npcKind==2){
				value = Int(itemBuff[num][5])+Int(itemBuff[num][6]);
			}else if(npcKind==5){
				value = Int(itemBuff[num][3])+Int(itemBuff[num][4]);
			}
			return value;
		}
		
		//轉換為整數
		public int Int(String data){
			return Integer.parseInt(data);
		}
		//----------------------------------------------------------------------------------------------
}