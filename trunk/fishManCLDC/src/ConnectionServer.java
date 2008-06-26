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
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.DataInputStream;
import javax.microedition.io.Connector;
import javax.microedition.io.HttpConnection;

public class ConnectionServer implements Runnable {
	public static boolean connFin = false;//Server是否回傳成功
	private static InputStream in = null;
	private static DataInputStream dos = null;
	private static HttpConnection connServ = null;
	private static Thread thread = null;
	private static String requestStr = "";
	private static String connStr = "";
	private static boolean connection = false;//是否要與Server連線
	private static String tmp = "";
	private static int connKind = 0;
	private static String httpStr = "http://localhost/fishManWebService";
	public static boolean conn = true;
	
	String loginAccessjsp= "/loginAccess.jsp";
	String getGameInfjsp= "/getGameInf.jsp";
	String setGameInfjsp= "/setGameInf.jsp";
	String updateMapjsp= "/updateMap.jsp";
	String updateEventjsp= "/updateEvent.jsp";
	
	
	public ConnectionServer(){
		try{
			conn = true;
			connServ = (HttpConnection)Connector.open(httpStr);
			thread = new Thread(this);
			thread.start();
		}
		catch(Exception e){}
	}
	
	//登入
	public void setLoginAccess(String username,String pass){
		connKind = 0;
		connStr = "?username="+username+"&userpass="+pass;
		connFin = false;
		connection = true;
	}
	
	//第一次執行遊戲傳入name
	public void setGameName(String name){
		try{
			if(!name.equals("")){			
				ByteArrayOutputStream bos = new ByteArrayOutputStream();
				DataOutputStream dos = new DataOutputStream(bos);
				dos.writeUTF(name);
				byte b[] = bos.toByteArray();
				tmp="";
				for(int i=0;i<b.length-1;i++)
					tmp += b[i]+"/";
				tmp+=b[b.length-1];
					//System.out.println(b[i]);			
			}
		}catch(Exception e){}
		connKind = 1;
		if(!name.equals(""))
			connStr = "?userID="+RoleDataValue.userID+"&userpass="+RoleDataValue.userpass+"&gameName="+tmp;
		else
			connStr = "?userID="+RoleDataValue.userID+"&userpass="+RoleDataValue.userpass+"&gameName=";
		connFin = false;
		connection = true;
	}
	
	//更新主角資料
	public static void updateGameInf(){
		tmp = "";
		tmp += RoleDataValue.fame + ":" + RoleDataValue.level + ":" + RoleDataValue.exp + ":" + RoleDataValue.expNext + ":" +
			   RoleDataValue.getNowAtk() + ":" + RoleDataValue.getNowDef() + ":" + RoleDataValue.agi + ":" + RoleDataValue.nowHP + ":" +
			   RoleDataValue.nowXP + ":" + RoleDataValue.maxHP + ":" + RoleDataValue.killMonster + ":";
		tmp += RoleDataValue.getEquipment() + ":" + RoleDataValue.money + ":" + RoleDataValue.roleX + ":" + 
			   RoleDataValue.roleY + ":" + RoleDataValue.roleMap + ":" + RoleDataValue.getItemStr();
		System.out.println(tmp);
		connKind = 2;
		connStr = "?userID="+RoleDataValue.userID+"&userpass="+RoleDataValue.userpass+"&gameInf="+tmp;
		connFin = false;
		connection = true;
	}
	
	//更新遊戲事件資料,0更新寶箱資料,1更新事件是否完成,2更新目前事件點完成度
	public static void updateGameEvent(int kind){
		tmp = "";
		if(kind==0){//更新寶箱資料
			for(int i=0;i<67;i++)
				tmp+=map.gem[i]+",";
			tmp+=map.gem[67];
		}else if(kind==1){//更新事件是否完成
			for(int i=0;i<22;i++)
				tmp+=RoleDataValue.evenFinished[i]+",";
			tmp+=RoleDataValue.evenFinished[22];
		}else if(kind==2){//更新目前事件點完成度
			for(int i=0;i<22;i++)
				tmp+=RoleDataValue.nowEvenSchedule[i]+":";
			tmp+=RoleDataValue.nowEvenSchedule[22];
		}
		System.out.println(tmp);
		connKind = 4;
		connStr = "?userID="+RoleDataValue.userID+"&userpass="+RoleDataValue.userpass+"&kind=" + kind  + "&event_value="+tmp;
		connFin = false;
		connection = true;
	}
	
	//更新地圖結界,map_dir為方向-0為north ,1為south ,2為west ,3為east
	public static void updateGameMap(int map_dir){
		tmp = "";
		try{
			for(int i=0;i<67;i++)
				tmp += map.dBstop[map_dir][i]+",";
			tmp+=map.dBstop[map_dir][67];
		}catch(Exception e){e.printStackTrace();}
		//System.out.println(tmp);
		connKind = 3;
		connStr = "?userID="+RoleDataValue.userID+"&userpass="+RoleDataValue.userpass+"&map_dir=" + map_dir  + "&map_value="+tmp;
		connFin = false;
		connection = true;
	}
	
	//回傳Server資訊
	public String getRequestState(){
		return requestStr;
	}
	
	//回傳Server是否傳送完成
	/*public boolean getFinishState(){
		return this.connFin;
	}*/
	
	//回傳帳號密碼是否正確
	public int getConnSucess(){
		String tmp[] = split(requestStr.trim(),"|");
		if(tmp[0].equals("LoginSucess")){
			RoleDataValue.userID = tmp[1];
			if(tmp[2].equals("true"))
				return 0;
			else
				return 1;
		}
		return -1;
	}
	
	//設定要抓回值的種類
	public void setConnKind(int kind){
		connKind = kind;
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
	
	//Thread
	public void run(){
		try{
			while(conn){
				if(!connection){
					try{
						Thread.sleep(100);
					}catch(Exception e){e.printStackTrace();}
					continue;
				}else{
					connFin = false;
				}
				if(connKind==0){//登入
					connServ = (HttpConnection)Connector.open(httpStr + loginAccessjsp +connStr);
				}else if(connKind==1){//登入成功,抓取目前主角狀態
					connServ = (HttpConnection)Connector.open(httpStr + getGameInfjsp +connStr);
				}else if(connKind==2){//更新主角資料
					connServ = (HttpConnection)Connector.open(httpStr + setGameInfjsp +connStr);
				}else if(connKind==3){
					connServ = (HttpConnection)Connector.open(httpStr + updateMapjsp +connStr);
				}else if(connKind==4){
					connServ = (HttpConnection)Connector.open(httpStr + updateEventjsp+connStr);
				}
				if(connServ.getResponseCode()==HttpConnection.HTTP_OK){
					tmp = "";
					try{
						in = connServ.openInputStream();
						dos = new DataInputStream(in);
						String requestStr = "";
						//抓取Server回傳的值
						
						while(true){
							tmp = dos.readUTF();
							requestStr = tmp;
						}
					}
					catch(Exception e1){
						requestStr = tmp;
						connFin = true;
						connection = false;
					}
					finally{
						if(in != null){
							dos.close();
							in.close();
						}
						if(connServ != null)
							connServ.close();
					}// end of finally
				}// end of ifFISHMAN
				else
					requestStr = "No Connection";
			}//end of while
		}
		catch(Exception e){e.printStackTrace();}
	}
}
