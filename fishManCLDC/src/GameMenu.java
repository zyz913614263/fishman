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
import javax.microedition.lcdui.game.*;
import javax.microedition.lcdui.*;
import javax.microedition.lcdui.Display;

public class GameMenu extends GameCanvas implements Runnable{

	private static Graphics g;
	private static Image img;
	private int menustate = 0;//目前選取到的選項
	private int menuNum = 3;//共有幾個選項
	public boolean menunow = true;//是否在主選單畫面
	private static Thread thread;
	private static Display display;
	private static GameMain midlet;
	private int page=-1;//畫面頁數
	private static LayerManager layMa = null;
	private static String username = "" ;
	private static String pass = "" ;
	private static String nickname = "" ;
	private int bgWidth = 176;//遊戲畫面大小
	private int bgHeight = 208;//遊戲畫面大小
	private int disWidth = 0;//LayManager實際位置
	private int disHeight = 0;//LayManager實際位置
	
	//遊戲選單格式------------------------------------------------
	private String loginStr[] = {"帳號","密碼"," 選單","   登入"};
	private String gameInfo[] = {"上一頁", "下一頁"};
	private String gameNameInp[] = {"名稱","上一頁", "下一頁"};
	private String showGameUser[] = {"上一頁", "   開始"};
	private String nowString[] = new String[4];
	//-----------------------------------------------------------
	//遊戲說明
	private String gameUserInf[] = {"各位玩家您好:","以下是釣魚達人的基本操","作指令,要熟記指令才能更",
			"快對遊戲上手喔!!^^","續...","遊戲操作指令:","按下 \" 1 \" 鍵是決定指令",
			"按下 \" 2 \" 鍵是取消指令","按下 \" 3 \" 鍵是\"人物狀態\"","續...",
			"您在遊戲中可以任意的按","下 \" 3 \" 鍵進入人物狀態","選擇\" 遊戲說明 \"來查看",
			"更詳細的遊戲說明","完"};
	private int gameUserInfCount=0;//遊戲說明顯示行數計數

	private Sprite spInterface;
	private Sprite spName;
	private int gameUserInfPage=2;
	private boolean init = true;
	private boolean connSucess = false;//叛斷是否登入成
	private boolean connFin = true;//叛斷是否Server已回傳資訊
	private String requestStr = "";//
	private static RoleDataValue roleData = null;
	//private static GameInfString gaminf = null;
	public static BackupMusic music = null;
	
	public GameMenu(GameMain midlet_/*,ConnectionServer conn_,RoleDataValue roleData_*/) {
		//true->使用GameAction(boolean suppressKeyEvents) , false->不使用GameAction(boolean suppressKeyEvents) 
		super(false);
		//使用全螢幕
		setFullScreenMode(true);
		midlet = midlet_;
		//music = music_;
		//roleData = roleData_;
		display = midlet.getDisplay();
		layMa = new LayerManager();
		spInterface = new Sprite(createImage("/image/gameInterface/inface.png"),bgWidth,bgHeight);
		spName = new Sprite(createImage("/image/gameInterface/Title.png"),84,15);
		menustate = 0;
		menuNum = 3;
		//setFullScreenMode(true);
		g = getGraphics();
		/*if(!(getWidth()<176) || !(getHeight()<208)){
			disWidth = (getWidth()-176)/2;//抓取螢幕的中心點
			disHeight = (getHeight()-208)/2;//抓取螢幕的中心點
		}*/
		//設定Sprite位置
		spName.setPosition(80, 20);
		spInterface.setPosition(0,0);
		//加入到LayerManager
		layMa.append(spName);
		spName.setVisible(false);
		layMa.append(spInterface);
		spInterface.setFrame(0);
		System.out.println("start");
		start();
	}
	
	//建立圖片
	public Image createImage(String pathImg){
		img = null;
		try{
			img = Image.createImage(pathImg);
		}
		catch(Exception e){e.printStackTrace();}
		return img;
	}
	
	//顯示畫面
	public void drawDisplay(){
		g = getGraphics();
		g.setColor(0,0,0);
		g.drawRect(disWidth-1,disHeight-1,176+1,208+1);
		layMa.paint(g,disWidth,disHeight);
		g.drawString(getWidth()+"|"+getHeight(),0,0,0);
		flushGraphics();
	}
	
	public void run() {
		try {
			if(init){
				Thread.sleep(500);
				disWidth = (getWidth()-176)/2;//抓取螢幕的中心點
				disHeight = (getHeight()-208)/2;//抓取螢幕的中心點
				drawDisplay();
				
				systemShowInf("建立資料庫...");
				new DataBaseWrite();
				systemShowInf("設定音樂...");
				GameMain.music = new BackupMusic();
				music = GameMain.music;
				systemShowInf("設定遊戲環境...");
				GameMain.roleData = new RoleDataValue();
				roleData = GameMain.roleData;
				GameMain.gaminf = new GameInfString();
				GameMain.music.musicStart("1");
				spInterface.setFrame(1);
				drawDisplay();
				changeMeun();
				init = false;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void systemShowInf(String infStr){
		g.setColor(128,255,255);
		g.fillRect(disWidth+50,disHeight+166,80,15);
		g.setColor(0,0,0);
		g.drawString(infStr,disWidth+55,disHeight+166,0);
		flushGraphics();
	}

	//程式執行
	public void start() {
		thread = new Thread(this);
		thread.start();
	}
	
	public void setFont(int size){
		Font font = Font.getFont(Font.FACE_SYSTEM, 0,
				size);
		g.setFont(font);
	}
	
	//鍵盤事件
	protected void keyPressed(int keyCode){
		if(!init & page!=-2){
			switch( keyCode ){
				case -5://按下FIRE
					GameMain.music.msuicSound(0);
					try{
						changeMeunStart();
					}catch(Exception e){e.printStackTrace();}
					break;
				case -1://按下UP
					GameMain.music.msuicSound(1);
					if(page==0 || page==-1){
						if(menustate <=0 )
							menustate=menuNum;
						menustate--;
					}
					else if(page==2){
						if(menustate <=1 )
							menustate=menuNum;
						menustate--;
					}
					else{
						if(menustate <=2 )
							menustate=menuNum;
						menustate--;
					}
					break;
				case -2://按下DOWN
					GameMain.music.msuicSound(1);
					if(page==0 || page==-1){
						if(menustate >=menuNum-1 )
							menustate=-1;
						menustate++;
					}
					else if(page==2){
						if(menustate >=3 )
							menustate=0;
						menustate++;
					}
					else{
						if(menustate >=3 )
							menustate=1;
						menustate++;
					}
					break;
				case -4://按下RIGHT
					GameMain.music.msuicSound(1);
					if(menustate==2)
						menustate=3;
					else if(menustate==3)
						menustate=2;
					break;
				case -3://按下LEFT
					GameMain.music.msuicSound(1);
					if(menustate==2)
						menustate=3;
					else if(menustate==3)
						menustate=2;
					break;
			}
			changeMeun();
		}
	}
	
	//畫面選單改變
	public void changePage(){
		try{
			switch(page){
				case -1:
					spName.setVisible(false);
					spInterface.setFrame(1);
					drawDisplay();
					break;
				case 0://遊戲帳號輸入選單
					menustate=0;
					spName.setVisible(true);
					spInterface.setFrame(2);
					spName.setFrame(0);
					drawDisplay();
					for(int i=0;i<loginStr.length;i++)
						nowString[i] = loginStr[i];
					menuNum = loginStr.length;//MENU 數量
					//System.out.println(page);
					break;
				case 1://遊戲說明選單
					spInterface.setFrame(2);
					spName.setFrame(1);
					drawDisplay();
					for(int i=2;i<nowString.length;i++)
						nowString[i] = gameInfo[i-2];
					menuNum = loginStr.length;
					menustate = 2;
					break;
				case 2://遊戲姓名輸入選單
					spInterface.setFrame(2);
					spName.setFrame(2);
					spName.setVisible(true);
					drawDisplay();
					for(int i=1;i<nowString.length;i++)
						nowString[i] = gameNameInp[i-1];
					menuNum = loginStr.length;
					menustate = 1;
					break;
				case 3://遊戲角色建立成功選單
					spName.setVisible(false);
					spInterface.setFrame(3);
					drawDisplay();
					for(int i=2;i<nowString.length;i++)
						nowString[i] = showGameUser[i-2];
					menuNum = loginStr.length;
					menustate = 2;
					break;
			}
			drawMenu();
		}
		catch(Exception e){e.printStackTrace();}
	}
	
	public void drawMenu(){
		try{
			if(page==3){
				setFont(16);
				g.drawString(nickname, disWidth+80, disHeight+15, 0);
			}
			//判斷在遊戲說明時,防止文字超出螢幕,當超出螢幕時放到下一頁面
			if(page==1){
				setFont(0);
				g.setColor(255,255,255);
				for(int i=0;gameUserInfCount<gameUserInf.length;gameUserInfCount++,i+=17){
					if(gameUserInfCount%5!=4)//遊戲說明
						g.drawString(gameUserInf[gameUserInfCount], disWidth+10, disHeight+70+i, 0);
					else{//顯示還有下一頁的文字
						g.drawString(gameUserInf[gameUserInfCount], disWidth+135, disHeight+70+i, 0);
						gameUserInfCount++;
						break;
					}
				}
				flushGraphics();
			}
			changeMeun();
			flushGraphics();
		}
		catch(Exception e){e.printStackTrace();}
	}

	//選單切換
	private void changeMeun() {
		if(page==-1){
			setFont(16);
			switch (menustate) {
				case 0:
					//Select----------------------------
					g.setColor(255, 255, 255);
					g.drawString("開始遊戲", disWidth+60, disHeight+120, 0);
					//----------------------------------
					g.setColor(69, 118, 181);
					g.drawString("繼續遊戲", disWidth+60, disHeight+140, 0);
					g.drawString("結束遊戲", disWidth+60, disHeight+160, 0);
					break;
				case 1:
					g.setColor(69, 118, 181);
					g.drawString("開始遊戲", disWidth+60, disHeight+120, 0);
					//Select----------------------------
					g.setColor(255, 255, 255);
					g.drawString("繼續遊戲", disWidth+60, disHeight+140, 0);
					//----------------------------------
					g.setColor(69, 118, 181);
					g.drawString("結束遊戲", disWidth+60, disHeight+160, 0);
					break;
				case 2:
					g.setColor(69, 118, 181);
					g.drawString("開始遊戲", disWidth+60, disHeight+120, 0);
					g.drawString("繼續遊戲", disWidth+60, disHeight+140, 0);
					//Select----------------------------
					g.setColor(255, 255, 255);
					g.drawString("結束遊戲", disWidth+60, disHeight+160, 0);
					//----------------------------------
					break;
			}
		}
		else{
			setFont(0);
			switch(menustate){
				case 0:
					//Select------------------------
					g.setColor(255, 255, 255);
					g.drawRoundRect(disWidth+52, disHeight+93, 95, 15, 15, 15);
					g.drawString(nowString[0], disWidth+22, disHeight+90, 0);
					//------------------------------
					g.setColor(69,118,181);
					g.drawRoundRect(disWidth+52, disHeight+113, 95, 15, 15, 15);
					g.drawString(nowString[1], disWidth+22, disHeight+110, 0);
					
					g.drawString(nowString[2], disWidth+88, disHeight+165, 0);
					g.drawString(nowString[3], disWidth+128, disHeight+165, 0);
					break;
				case 1:
					if(page==0){//當為輸入帳號時顯示
						g.setColor(69,118,181);
						g.drawRoundRect(disWidth+52, disHeight+93, 95, 15, 15, 15);
						g.drawString(nowString[0], disWidth+22, disHeight+90, 0);
					}
					if(page==0 || page==2){//當為輸入帳號與遊戲名稱時顯示
						//Select------------------------
						g.setColor(255, 255, 255);
						g.drawRoundRect(disWidth+52, disHeight+113, 95, 15, 15, 15);
						g.drawString(nowString[1], disWidth+22, disHeight+110, 0);
						//------------------------------
					}
					g.setColor(69,118,181);
					g.drawString(nowString[2], disWidth+88, disHeight+165, 0);
					g.drawString(nowString[3], disWidth+128, disHeight+165, 0);
					//System.out.println(menustate);
					break;
				case 3:
					if(page==0){//當為輸入帳號時顯示
						g.setColor(69,118,181);
						g.drawRoundRect(disWidth+52, disHeight+93, 95, 15, 15, 15);
						g.drawString(nowString[0],disWidth+22, disHeight+90, 0);
					}
					if(page==0 || page==2){//當為輸入帳號與遊戲名稱時顯示
						g.setColor(69,118,181);
						g.drawRoundRect(disWidth+52, disHeight+113, 95, 15, 15, 15);
						g.drawString(nowString[1], disWidth+22, disHeight+110, 0);
					}
					//Select--------------------------
					g.setColor(255, 255, 255);
					g.drawString(nowString[2], disWidth+88, disHeight+165, 0);
					//--------------------------------
					g.setColor(69,118,181);
					g.drawString(nowString[3], disWidth+128, disHeight+165, 0);
					break;
				case 2:
					if(page==0){
						g.setColor(69,118,181);
						g.drawRoundRect(disWidth+52, disHeight+93, 95, 15, 15, 15);
						g.drawString(nowString[0], disWidth+22, disHeight+90, 0);
					}
					if(page==0 || page==2){
						g.setColor(69,118,181);
						g.drawRoundRect(disWidth+52, disHeight+113, 95, 15, 15, 15);
						g.drawString(nowString[1], disWidth+22, disHeight+110, 0);
					}
					g.setColor(69,118,181);
					g.drawString(nowString[2], disWidth+88, disHeight+165, 0);
					//Select-------------------------
					g.setColor(255, 255, 255);
					g.drawString(nowString[3], disWidth+128, disHeight+165, 0);
					//-------------------------------
					break;
			}
		}
		flushGraphics();
	}

	//判斷選取的選單選項
	public void changeMeunStart() {
		try {
			if(page==-1){
				switch (menustate) {
				case 0:
					page=0;
					spName.setVisible(true);
					changePage();
					break;
				case 1:
					page=0;
					spName.setVisible(true);
					changePage();
					break;
				case 2:
					//spWelcome.setVisible(false);
					spName.setVisible(false);
					spInterface.setFrame(4);
					drawDisplay();
					GameMain.music.stop();
					exit();
					break;
				}
			}
			else{
				switch(menustate){
					case 0:
						try{
								new InputText(nowString[0]);
						}
						catch(Exception e){e.printStackTrace();}
						break;
					case 1:
						new InputText(nowString[1]);
						break;
					case 2://登入
						if(page==0){//當在輸入帳號畫面時
							//未輸入帳號
							if( username.equals("") || pass.equals("") ){
								g.drawString("「請 輸入帳號密碼」", disWidth+75, disHeight+140, 0);
								flushGraphics();
							}
							else if( username.equals("xlinx") && pass.equals("x") )
							{
								runGame();
								g.drawString("進入遊戲...", disWidth+15, disHeight+129, 0);
								flushGraphics();
							}
							else
							{//連到Server
								try{
									GameMain.conn.setLoginAccess(username,pass);//送出帳號密碼
								}catch(Exception e){e.printStackTrace();}
								g.setColor(255,255,255);
								g.drawString("「登入中」", disWidth+95, disHeight+140, 0);
								flushGraphics();
								while(!ConnectionServer.connFin);//Server是否回傳完資訊
								page=0;
								drawDisplay();
								g.setColor(255,255,255);
								gameUserInfCount=0;
								//成功登入將繼續到下一頁面(第一次進入遊戲)
								if(GameMain.conn.getConnSucess()==1){
									g.drawString("「登入成功」", disWidth+95, disHeight+140, 0);
									flushGraphics();
									RoleDataValue.userpass = pass;
									page=1;
									changePage();
								}else if(GameMain.conn.getConnSucess()==0){//登入,不是第一次遊戲
									try{
										page=-2;
										g.drawString("「登入成功」", disWidth+95, disHeight+140, 0);
										flushGraphics();
										RoleDataValue.userpass = pass;
										g.drawString("讀取使用者資料....", disWidth+15, disHeight+69, 0);
										flushGraphics();
										GameMain.conn.setGameName("");
										while(!ConnectionServer.connFin);//Server是否回傳完資訊
										g.drawString("人物狀態初始化.....", disWidth+15, disHeight+89, 0);
										flushGraphics();
										roleData.getFirstRoleStateForInternet(GameMain.conn.getRequestState());
										roleData.loginSuccessStart();
										g.drawString("地圖初始化..........", disWidth+15, disHeight+109, 0);
										flushGraphics();
									}catch(Exception e){e.printStackTrace();}
									runGame();
									g.drawString("進入遊戲...", disWidth+15, disHeight+129, 0);
									flushGraphics();									
								}else{
									g.drawString("「登入失敗」", disWidth+95, disHeight+140, 0);
									flushGraphics();
								}
							}
						}
						else if(page==1){//當畫面在遊戲說明時
							if(gameUserInfCount<gameUserInf.length)//如果遊戲說明未結速時
								changePage();
							else{//遊戲說明完成,畫面改變為輸入遊戲名稱
								page=2;
								gameUserInfCount=20;
								changePage();
							}
						}
						else if(page==2){//輸入名稱完成,畫面改變為角色建立完成
							if(nickname.equals(""))
								g.drawString("「請輸入名稱」",disWidth+90,disHeight+140,0);
							else{
								page=3;
								changePage();								
							}
						}
						else if(page==3){//第一進入遊戲,完成輸入名稱
							page=-2;
							GameMain.conn.setGameName(nickname);
							while(!ConnectionServer.connFin);//Server是否回傳完資訊
							try{
								roleData.getFirstRoleStateForInternet(GameMain.conn.getRequestState());
								roleData.loginSuccessStart();
							}catch(Exception e){e.printStackTrace();}
							runGame();
						}
						break;
					case 3://回主選單,上一頁
						if(page==0){
							page=-1;
							menustate=0;
							menuNum = 3;
						}
						//判斷當為GameInformation時,要回輸入帳號密碼
						if(page==1 && gameUserInfCount==5)
							page--;
						else if(page==2 || page==3)//判斷當不是GameInformation時
							page--;
						//切換GameInformaion的頁面
						if(page==1){
							gameUserInfCount-=5*gameUserInfPage;
							gameUserInfPage++;
							if(gameUserInfPage>=3)
								gameUserInfPage=2;
							changePage();
						}//回到輸入姓名部份
						else if(page==2){
							//rolesprite=true;
							Thread.sleep(300);
							gameUserInfPage=2;
							changePage();
						}
						else
							changePage();
						break;
				}
			showBufferString();
			}
		} catch (Exception e) {}
	}
	
	//	畫出所輸入的字
	public void showBufferString(){
		if(page==0){
			drawUser_pass();
		}
		else if(page==2){
			g.setColor(255,255,255);
			g.drawString(nickname, disWidth+60, disHeight+115, 0);
		}
	}
	
	//判斷是否為密碼
	public void drawUser_pass(){
		g.setColor(255,255,255);
		g.drawString(username, disWidth+60, disHeight+95, 0);
		if(pass.equals(""))
			g.drawString("", disWidth+60, disHeight+115, 0);
		else
			g.drawString("*******", disWidth+60, disHeight+115, 0);
	}

	//關閉程式
	public void exit(){
		drawDisplay();
		try{
			Thread.sleep(2000);
		}catch(Exception e){}
		//backupmusic.stop();
		//backupmusic = null;
		midlet.exit();
	}
	
	public void runGame(){
		try{
			GameMain.music.stop();
			display.setCurrent(new mapCanvars(display,roleData,midlet));
		}catch(Exception e){e.printStackTrace();}
	}
	
	//設定為此display
	public void setDisplay(){
		display.setCurrent(this);
		drawDisplay();
		changeMeun();
		showBufferString();
	}
	
	//	帳號密碼輸入
	class InputText implements CommandListener{
		private Command okCommand;
		private TextField txt;
		private String strtxt;
		
		public InputText(String str){
			Form form = new Form("請輸入"+str);
			if(page==0)
				txt=new TextField(str, "", 10, TextField.ANY);
			else if(page==2)
				txt=new TextField(str, "", 5, TextField.ANY);
			okCommand = new Command("確定", Command.OK, 1);
			form.append(txt);
			form.addCommand(okCommand);
			form.setCommandListener(this);
		    display.setCurrent(form);
		}
		
		public void commandAction(Command c, Displayable s){
			if(c==okCommand){
				if(menustate==0 && page==0)
					username = txt.getString();
				else if(menustate==1 && page==0)
					pass = txt.getString();
				else if(page==2){
					nickname = txt.getString();
				}
				setDisplay();
			}
		}
	}
	
}