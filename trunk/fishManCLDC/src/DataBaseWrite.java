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
import java.io.DataInputStream;
import java.io.*;
import java.io.EOFException;

import javax.microedition.rms.*;

public class DataBaseWrite {
	private String allDBName[] = {"ItemDB","ComboDB","JewelryDB","ExpDB","MapDB","Protectdb",
			"EventDB","MonsterDB","PoleDB","BaitDB","TonicDB","TalkDB","NpcDB","monsterTalkDB","roleOwnDB"};
	private String dataNmaeEn[] = {"item.dat","combo.dat","jewelry.dat","exp.dat","map.dat","Protect.dat",
			"event.dat","monster.dat","pole.dat","bait.dat","tonic.dat","talk.dat","npc.dat","monsterTalk.dat","roleOwnTalk.dat"};
	private int dataFieldNum[] = {6,4,11,1,4,10,4,11,12,7,7,4,9,6,3};
	private RecordStore rs = null;
	private String dbState = "";
	private String roleStateDB[] = {"roleStateDB","roleItemDB"};
	
	public DataBaseWrite(){
		for(int i=0;i<getDataNum();i++){
			if( testDataBase(i,true) ){
				createDataBase(i,true);
				writeDB(i);
			}
		}
		for(int i=0;i<roleStateDB.length;i++)
			if( testDataBase(i,false) )
				createDataBase(i,false);
	}
	
	public void writeDB(int dataNum){
		InputStream inp;
		DataInputStream data;
		byte[] result = null;
		String tmp = "";
		try{
    		inp = getClass().getResourceAsStream("/data/"+dataNmaeEn[dataNum]);
    		data = new DataInputStream(inp);
    		
    		while(true){
    			ByteArrayOutputStream bos = new ByteArrayOutputStream();
    			DataOutputStream dos = new DataOutputStream(bos);
    			for(int i=0;i<dataFieldNum[dataNum];i++){
    				dos.writeUTF(data.readUTF());
    			}
    			result = bos.toByteArray();
    			rs.addRecord(result,0,result.length);
    			result = null;
    			dos.close();
    			bos.close();
    		}
    	}
    	catch(EOFException e){}
    	catch(Exception e2){}
	}
	
	//建立一個新的RMS
	public void createDataBase(int dbNum,boolean kind) {
		//RecordStore rs = null;
		try {
			if(kind){
				rs = RecordStore.openRecordStore(allDBName[dbNum], true);
				dbState = allDBName[dbNum]+"Creating...";
			}
			else{
				rs = RecordStore.openRecordStore(roleStateDB[dbNum], true);
				dbState = roleStateDB[dbNum]+"Creating...";
			}
			System.out.println("Creating..."+dbState);
		} catch (Exception e) {}
	}

	//資料庫已建立,如果沒找到資料庫則建立新的資料庫
	public boolean testDataBase(int dbNum,boolean kind){
		//RecordStore rs = null;
		try {
			if(kind){
				rs = RecordStore.openRecordStore(allDBName[dbNum], false);
				dbState = "DataBase Have!"+allDBName[dbNum];
			}
			else{
				rs = RecordStore.openRecordStore(roleStateDB[dbNum], false);
				dbState = "DataBase Have!"+roleStateDB[dbNum];
				RecordStore.deleteRecordStore(roleStateDB[dbNum]);
				return true;
			}
			System.out.println("DataBase Have!"+dbState);
			return false;
		} catch (Exception e) {
			return true;
		}
	}

	//刪除資料庫
	public static boolean deleteDataBase(String rmsname) {
		if (rmsname.length() > 32)
			return false;
		try {
			RecordStore.deleteRecordStore(rmsname);
		} catch (Exception e) {
			return false;
		}
		return true;
	}
	
	public String getDBState(){
		return dbState;
	}

	//抓取所有資料庫的名稱
	public String[] getAllDataName(){
		return allDBName;
	}
	
	//抓取資料庫的數量
	public int getDataNum(){
		return allDBName.length;
	}
}