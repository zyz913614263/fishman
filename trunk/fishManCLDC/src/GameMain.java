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
*	   �L�q��     �t�d�u�@�G
*					�C�������{���]�p
*			   		����ݸ�Ʈw�]�p
*			   		��Ʈw�]�p
*			   		�{���t�ξ�X

*	   ����m     �t�d�u�@�G
*					�C�������{���]�p
*			   		����ݸ�Ʈw�]�p
*			   		��Ʈw�]�p
*			   		�{���t�ξ�X
*
*      ���v�o     �t�d�u�@�G
* 					�a�ϵ{���]�p
*			   		�G�Ƭy�{�{���]�p
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
		//����ù�
		display = Display.getDisplay(this);
		//roleData = new RoleDataValue();
		conn  = new ConnectionServer();
	}

	//�Ұʤ��
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
	
	//�}�l�C��
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
	
	//�������
	public void exit(){
		System.out.println("exit");
		try{
			ConnectionServer.connFin = false;
			this.destroyApp(false);
		    this.notifyDestroyed();
		}catch(Exception e){e.printStackTrace();}
	}
	
	//�r�����
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
