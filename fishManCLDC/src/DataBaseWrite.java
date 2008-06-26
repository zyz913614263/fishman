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
	
	//�إߤ@�ӷs��RMS
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

	//��Ʈw�w�إ�,�p�G�S����Ʈw�h�إ߷s����Ʈw
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

	//�R����Ʈw
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

	//����Ҧ���Ʈw���W��
	public String[] getAllDataName(){
		return allDBName;
	}
	
	//�����Ʈw���ƶq
	public int getDataNum(){
		return allDBName.length;
	}
}