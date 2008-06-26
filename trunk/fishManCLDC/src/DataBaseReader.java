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
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import javax.microedition.rms.RecordEnumeration;
import javax.microedition.rms.RecordStore;
//import javax.microedition.rms.RecordStoreException;

public class DataBaseReader{
	private String allDBName[] = {"ItemDB","ComboDB","JewelryDB","ExpDB","MapDB","Protectdb",
			"EventDB","MonsterDB","PoleDB","BaitDB","TonicDB","TalkDB","NpcDB","monsterTalkDB","roleOwnDB"};
	private String dataNmaeEn[] = {"item.dat","combo.dat","jewelry.dat","exp.dat","map.dat","Protect.dat",
			"event.dat","monster.dat","pole.dat","bait.dat","tonic.dat","talk.dat","npc.dat","monsterTalk.dat","roleOwnTalk.dat"};
	private int dataFieldNum[] = {6,4,11,1,4,10,4,11,12,7,7,4,9,6,3};//每一資料庫欄位數
	private RecordStore rs = null;
	private RecordEnumeration re = null;
	private static int travelNum = -1;
	private static String tmp[] = null;
	private static byte[] bytetmp = null;
	private static ByteArrayInputStream bis = null;
	private static DataInputStream dis = null;
	private static ByteArrayOutputStream bos = null;
	private static DataOutputStream dos = null;
	
	public DataBaseReader(){
		
	}
	
	//抓取資料庫的某一記錄，dbNum=資料庫編號，recordNum=每一筆記錄的id
	public String[] readDBRecord(int dbNum,int recordNum){
		tmp = new String[dataFieldNum[dbNum]];
		bytetmp = null;
		try{
			rs = RecordStore.openRecordStore(allDBName[dbNum], true);
			bytetmp = rs.getRecord(recordNum);
			
			bis = new ByteArrayInputStream(bytetmp);
			dis = new DataInputStream(bis);
			
			for(int i=0;i<dataFieldNum[dbNum];i++){
				tmp[i] = dis.readUTF();
				//System.out.println(tmp[i]);
			}
			rs.closeRecordStore();
			dis.close();
			bis.close();
		} catch (Exception e) {e.printStackTrace();}
		return tmp;
	}
	
	//抓取目前資料表紀錄總數
	public int getDBCount(int dbNum){
		int countDB = 0;
		try{
			rs = RecordStore.openRecordStore(allDBName[dbNum], true);
			countDB = rs.getNumRecords();
		} catch (Exception e) {e.printStackTrace();}
		return countDB;
	}
	
	//抓取資料庫的某一欄位，dbNum=資料庫編號，recordNum=每一筆記錄的id，field=記錄的欄位
	public String readDBField(int dbNum,int recordNum,int field){
		String tmp_ = "";
		bytetmp = null;
		try{
			rs = RecordStore.openRecordStore(allDBName[dbNum], true);
			bytetmp = rs.getRecord(recordNum);
			
			bis = new ByteArrayInputStream(bytetmp);
			dis = new DataInputStream(bis);
			
			for(int i=0;i<field+1;i++){
				tmp_ = dis.readUTF();
				//System.out.println(tmp[i]);
			}
			rs.closeRecordStore();
			dis.close();
			bis.close();
		} catch (Exception e) {e.printStackTrace();}
		return tmp_;
	}
	
	public void opentravelDB(int dbNum){
		try{
			rs = RecordStore.openRecordStore(allDBName[dbNum], true);
			re = rs.enumerateRecords(null,null,false);
			re.reset();
			travelNum = dbNum;
		} catch (Exception e) {e.printStackTrace();}
	}
	
	//寫入主角狀態
	public void writeRoleState(){
		
	}
	
	//清除主角物品資料庫,再重建一次
	public void clearRoleItemDB(){
		try{
			rs = RecordStore.openRecordStore("roleItemDB", true);
			for(int i=0;i<rs.getNumRecords();i++)
				rs.deleteRecord(i);
				
		}catch(Exception e){e.printStackTrace();}
	}
	
	//寫入物品到資料庫
	public void writeRoleItem(String itemTmp[]){
		bytetmp = null;
		try{
			//rs = RecordStore.openRecordStore("roleItemDB", true);
			bos = new ByteArrayOutputStream();
			dos = new DataOutputStream(bos);
			for(int i=0;i<itemTmp.length;i++){
				dos.writeUTF(itemTmp[i]);
			}
			bytetmp = bos.toByteArray();
			rs.addRecord(bytetmp,0,bytetmp.length);
			dos.close();
			bos.close();
		}
		catch(Exception e){e.printStackTrace();}
	}
	
	public void closeDB(){
		try{
			rs.closeRecordStore();
		}catch(Exception e){e.printStackTrace();}
	}
	
	//傳回資料庫的筆數
	public int numRecords(){
		if(travelNum==-1) return 1;
		return re.numRecords();
	}
	
	//傳回下一筆資料
	public String[] nextRecord() throws Exception{
		if(travelNum==-1) return null;
		tmp = new String[dataFieldNum[travelNum]];
		bytetmp = null;
		try{
			bytetmp = re.previousRecord();
			bis = new ByteArrayInputStream(bytetmp);
			dis = new DataInputStream(bis);
			
			for(int i=0;i<dataFieldNum[travelNum];i++)
				tmp[i] = dis.readUTF();
		} catch (Exception e) {}
		return tmp;
	}
	
	//傳回上一筆資料
	public String[] previousRecord()throws Exception{
		if(travelNum==-1) return null;
		tmp = new String[dataFieldNum[travelNum]];
		bytetmp = null;
		try{
			bytetmp = re.nextRecord();
			bis = new ByteArrayInputStream(bytetmp);
			dis = new DataInputStream(bis);
			
			for(int i=0;i<dataFieldNum[travelNum];i++)
				tmp[i] = dis.readUTF();
		} catch (Exception e) {}
		return tmp;
	}
	
	public String tanStringToByte(byte[] bytetmp){
		String tmp = "";
		try{
			bis = new ByteArrayInputStream(bytetmp);
			dis = new DataInputStream(bis);
			tmp = dis.readUTF();
		}catch(Exception e){}
		return tmp;
	}
	
	//抓取Pole在商店的物品
	public String[] getOpenUpStoreItem(int dbNum){
		tmp = new String[dataFieldNum[dbNum]];
		bytetmp = null;
		int n=0;
		try{
			rs = RecordStore.openRecordStore(allDBName[dbNum], true);
			
			for(int i=0;i<rs.getNumRecords();i++){
				if(n==13) break;
				bytetmp = rs.getRecord(i+1);
				bis = new ByteArrayInputStream(bytetmp);
				dis = new DataInputStream(bis);
			
				for(int j=0;j<dataFieldNum[dbNum];j++){
					tmp[j] = dis.readUTF().trim();
				}
			}
			rs.closeRecordStore();
			/*for(int i=0;i<tmp.length;i++)
				System.out.println(tmp[i][0]);*/
		} catch (Exception e) {e.printStackTrace();}
		return tmp;
	}
	
	//抓取要搜尋編號的資料
	public String[] getNumData(String dataNum){
		int dbNum = 0;
		if(dataNum.indexOf("I")!=-1)
			dbNum = 0;
		else if(dataNum.indexOf("C")!=-1)
			dbNum = 1;
		else if(dataNum.indexOf("J")!=-1)
			dbNum = 2;
		else if(dataNum.indexOf("MA")!=-1)
			dbNum = 4;
		else if(dataNum.indexOf("G")!=-1)
			dbNum = 5;
		else if(dataNum.indexOf("E")!=-1)
			dbNum = 6;
		else if(dataNum.indexOf("M")!=-1)
			dbNum = 7;
		else if(dataNum.indexOf("P")!=-1)
			dbNum = 8;
		else if(dataNum.indexOf("B")!=-1)
			dbNum = 9;
		else if(dataNum.indexOf("T")!=-1)
			dbNum = 10;
		else if(dataNum.indexOf("K")!=-1)
			dbNum = 11;
		else if(dataNum.indexOf("S")!=-1)
			dbNum = 12;
		
		tmp = null;
		for(int i=0;i<getDBCount(dbNum);i++){
			if(dbNum==4 ||dbNum==11)
				tmp = readDBRecord( dbNum, 
						Integer.parseInt( dataNum.substring(2,dataNum.length() )) );
			else
				tmp = readDBRecord( dbNum, 
						Integer.parseInt( dataNum.substring(1,dataNum.length() )) );
		}
		return tmp;
	}
	
	public void destroyDB(){
		re.destroy();
	}
}