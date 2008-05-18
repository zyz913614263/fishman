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
*	   林義翔     負責工作：
*					遊戲介面程式設計
*			   		手機端資料庫設計
*			   		資料庫設計
*			   		程式系統整合

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
import javax.microedition.lcdui.Display;
import javax.microedition.midlet.MIDlet;
import javax.microedition.midlet.MIDletStateChangeException;

public class GameMain extends MIDlet{

	public static Display display;
	private static GameMenu app;
	private boolean init = true;
	public static ConnectionServer conn  = null;
	//private static mapCanvars drawWord = null;
	//private Command exitCommand;
	//private Command enterCommand;
	public static RoleDataValue roleData = null;
	public static GameInfString gaminf = null;
	public static BackupMusic music = null;
	
	public GameMain() {
		super();
		//抓取螢幕
		display = Display.getDisplay(this);
		//roleData = new RoleDataValue();
		conn  = new ConnectionServer();
	}

	//啟動手機
	public void startApp() throws MIDletStateChangeException {
		if(init){
			app = new GameMenu(this/*,conn,roleData*/);
			display.setCurrent(app);
			init = false;
		}
	}

	public void pauseApp(){}

	public void destroyApp(boolean arg0){}
	
	public MIDlet getMIDlet(){
		return this;
	}
	
	//開始遊戲
	/*public void runGame(){
		drawWord = new mapCanvars(display,roleData,this);
		display.setCurrent(drawWord);
		//app = null;
		//ConversationMenu app2 = new ConversationMenu(this,gg);
		//display.setCurrent(app2);
		System.out.println("Run");
	}*/
	
	public Display getDisplay(){
		return display;
	}
	
	//關閉手機
	public void exit(){
		System.out.println("exit");
		try{
			ConnectionServer.connFin = false;
			this.destroyApp(false);
		    this.notifyDestroyed();
		}catch(Exception e){e.printStackTrace();}
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
}
