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
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.DataInputStream;
import javax.microedition.io.Connector;
import javax.microedition.io.HttpConnection;

public class ConnectionServer implements Runnable {
	public static boolean connFin = false;//Server�O�_�^�Ǧ��\
	private static InputStream in = null;
	private static DataInputStream dos = null;
	private static HttpConnection connServ = null;
	private static Thread thread = null;
	private static String requestStr = "";
	private static String connStr = "";
	private static boolean connection = false;//�O�_�n�PServer�s�u
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
	
	//�n�J
	public void setLoginAccess(String username,String pass){
		connKind = 0;
		connStr = "?username="+username+"&userpass="+pass;
		connFin = false;
		connection = true;
	}
	
	//�Ĥ@������C���ǤJname
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
	
	//��s�D�����
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
	
	//��s�C���ƥ���,0��s�_�c���,1��s�ƥ�O�_����,2��s�ثe�ƥ��I������
	public static void updateGameEvent(int kind){
		tmp = "";
		if(kind==0){//��s�_�c���
			for(int i=0;i<67;i++)
				tmp+=map.gem[i]+",";
			tmp+=map.gem[67];
		}else if(kind==1){//��s�ƥ�O�_����
			for(int i=0;i<22;i++)
				tmp+=RoleDataValue.evenFinished[i]+",";
			tmp+=RoleDataValue.evenFinished[22];
		}else if(kind==2){//��s�ثe�ƥ��I������
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
	
	//��s�a�ϵ���,map_dir����V-0��north ,1��south ,2��west ,3��east
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
	
	//�^��Server��T
	public String getRequestState(){
		return requestStr;
	}
	
	//�^��Server�O�_�ǰe����
	/*public boolean getFinishState(){
		return this.connFin;
	}*/
	
	//�^�Ǳb���K�X�O�_���T
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
	
	//�]�w�n��^�Ȫ�����
	public void setConnKind(int kind){
		connKind = kind;
	}
	
	//�r�����
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
				if(connKind==0){//�n�J
					connServ = (HttpConnection)Connector.open(httpStr + loginAccessjsp +connStr);
				}else if(connKind==1){//�n�J���\,����ثe�D�����A
					connServ = (HttpConnection)Connector.open(httpStr + getGameInfjsp +connStr);
				}else if(connKind==2){//��s�D�����
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
						//���Server�^�Ǫ���
						
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
