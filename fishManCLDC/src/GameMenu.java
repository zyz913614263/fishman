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
import javax.microedition.lcdui.game.*;
import javax.microedition.lcdui.*;
import javax.microedition.lcdui.Display;

public class GameMenu extends GameCanvas implements Runnable{

	private static Graphics g;
	private static Image img;
	private int menustate = 0;//�ثe����쪺�ﶵ
	private int menuNum = 3;//�@���X�ӿﶵ
	public boolean menunow = true;//�O�_�b�D���e��
	private static Thread thread;
	private static Display display;
	private static GameMain midlet;
	private int page=-1;//�e������
	private static LayerManager layMa = null;
	private static String username = "" ;
	private static String pass = "" ;
	private static String nickname = "" ;
	private int bgWidth = 176;//�C���e���j�p
	private int bgHeight = 208;//�C���e���j�p
	private int disWidth = 0;//LayManager��ڦ�m
	private int disHeight = 0;//LayManager��ڦ�m
	
	//�C�����榡------------------------------------------------
	private String loginStr[] = {"�b��","�K�X"," ���","   �n�J"};
	private String gameInfo[] = {"�W�@��", "�U�@��"};
	private String gameNameInp[] = {"�W��","�W�@��", "�U�@��"};
	private String showGameUser[] = {"�W�@��", "   �}�l"};
	private String nowString[] = new String[4];
	//-----------------------------------------------------------
	//�C������
	private String gameUserInf[] = {"�U�쪱�a�z�n:","�H�U�O�����F�H���򥻾�","�@���O,�n���O���O�~���",
			"�ֹ�C���W���!!^^","��...","�C���ާ@���O:","���U \" 1 \" ��O�M�w���O",
			"���U \" 2 \" ��O�������O","���U \" 3 \" ��O\"�H�����A\"","��...",
			"�z�b�C�����i�H���N����","�U \" 3 \" ��i�J�H�����A","���\" �C������ \"�Ӭd��",
			"��ԲӪ��C������","��"};
	private int gameUserInfCount=0;//�C��������ܦ�ƭp��

	private Sprite spInterface;
	private Sprite spName;
	private int gameUserInfPage=2;
	private boolean init = true;
	private boolean connSucess = false;//�q�_�O�_�n�J��
	private boolean connFin = true;//�q�_�O�_Server�w�^�Ǹ�T
	private String requestStr = "";//
	private static RoleDataValue roleData = null;
	//private static GameInfString gaminf = null;
	public static BackupMusic music = null;
	
	public GameMenu(GameMain midlet_/*,ConnectionServer conn_,RoleDataValue roleData_*/) {
		//true->�ϥ�GameAction(boolean suppressKeyEvents) , false->���ϥ�GameAction(boolean suppressKeyEvents) 
		super(false);
		//�ϥΥ��ù�
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
			disWidth = (getWidth()-176)/2;//����ù��������I
			disHeight = (getHeight()-208)/2;//����ù��������I
		}*/
		//�]�wSprite��m
		spName.setPosition(80, 20);
		spInterface.setPosition(0,0);
		//�[�J��LayerManager
		layMa.append(spName);
		spName.setVisible(false);
		layMa.append(spInterface);
		spInterface.setFrame(0);
		System.out.println("start");
		start();
	}
	
	//�إ߹Ϥ�
	public Image createImage(String pathImg){
		img = null;
		try{
			img = Image.createImage(pathImg);
		}
		catch(Exception e){e.printStackTrace();}
		return img;
	}
	
	//��ܵe��
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
				disWidth = (getWidth()-176)/2;//����ù��������I
				disHeight = (getHeight()-208)/2;//����ù��������I
				drawDisplay();
				
				systemShowInf("�إ߸�Ʈw...");
				new DataBaseWrite();
				systemShowInf("�]�w����...");
				GameMain.music = new BackupMusic();
				music = GameMain.music;
				systemShowInf("�]�w�C������...");
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

	//�{������
	public void start() {
		thread = new Thread(this);
		thread.start();
	}
	
	public void setFont(int size){
		Font font = Font.getFont(Font.FACE_SYSTEM, 0,
				size);
		g.setFont(font);
	}
	
	//��L�ƥ�
	protected void keyPressed(int keyCode){
		if(!init & page!=-2){
			switch( keyCode ){
				case -5://���UFIRE
					GameMain.music.msuicSound(0);
					try{
						changeMeunStart();
					}catch(Exception e){e.printStackTrace();}
					break;
				case -1://���UUP
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
				case -2://���UDOWN
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
				case -4://���URIGHT
					GameMain.music.msuicSound(1);
					if(menustate==2)
						menustate=3;
					else if(menustate==3)
						menustate=2;
					break;
				case -3://���ULEFT
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
	
	//�e��������
	public void changePage(){
		try{
			switch(page){
				case -1:
					spName.setVisible(false);
					spInterface.setFrame(1);
					drawDisplay();
					break;
				case 0://�C���b����J���
					menustate=0;
					spName.setVisible(true);
					spInterface.setFrame(2);
					spName.setFrame(0);
					drawDisplay();
					for(int i=0;i<loginStr.length;i++)
						nowString[i] = loginStr[i];
					menuNum = loginStr.length;//MENU �ƶq
					//System.out.println(page);
					break;
				case 1://�C���������
					spInterface.setFrame(2);
					spName.setFrame(1);
					drawDisplay();
					for(int i=2;i<nowString.length;i++)
						nowString[i] = gameInfo[i-2];
					menuNum = loginStr.length;
					menustate = 2;
					break;
				case 2://�C���m�W��J���
					spInterface.setFrame(2);
					spName.setFrame(2);
					spName.setVisible(true);
					drawDisplay();
					for(int i=1;i<nowString.length;i++)
						nowString[i] = gameNameInp[i-1];
					menuNum = loginStr.length;
					menustate = 1;
					break;
				case 3://�C������إߦ��\���
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
			//�P�_�b�C��������,�����r�W�X�ù�,��W�X�ù��ɩ��U�@����
			if(page==1){
				setFont(0);
				g.setColor(255,255,255);
				for(int i=0;gameUserInfCount<gameUserInf.length;gameUserInfCount++,i+=17){
					if(gameUserInfCount%5!=4)//�C������
						g.drawString(gameUserInf[gameUserInfCount], disWidth+10, disHeight+70+i, 0);
					else{//����٦��U�@������r
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

	//������
	private void changeMeun() {
		if(page==-1){
			setFont(16);
			switch (menustate) {
				case 0:
					//Select----------------------------
					g.setColor(255, 255, 255);
					g.drawString("�}�l�C��", disWidth+60, disHeight+120, 0);
					//----------------------------------
					g.setColor(69, 118, 181);
					g.drawString("�~��C��", disWidth+60, disHeight+140, 0);
					g.drawString("�����C��", disWidth+60, disHeight+160, 0);
					break;
				case 1:
					g.setColor(69, 118, 181);
					g.drawString("�}�l�C��", disWidth+60, disHeight+120, 0);
					//Select----------------------------
					g.setColor(255, 255, 255);
					g.drawString("�~��C��", disWidth+60, disHeight+140, 0);
					//----------------------------------
					g.setColor(69, 118, 181);
					g.drawString("�����C��", disWidth+60, disHeight+160, 0);
					break;
				case 2:
					g.setColor(69, 118, 181);
					g.drawString("�}�l�C��", disWidth+60, disHeight+120, 0);
					g.drawString("�~��C��", disWidth+60, disHeight+140, 0);
					//Select----------------------------
					g.setColor(255, 255, 255);
					g.drawString("�����C��", disWidth+60, disHeight+160, 0);
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
					if(page==0){//����J�b�������
						g.setColor(69,118,181);
						g.drawRoundRect(disWidth+52, disHeight+93, 95, 15, 15, 15);
						g.drawString(nowString[0], disWidth+22, disHeight+90, 0);
					}
					if(page==0 || page==2){//����J�b���P�C���W�ٮ����
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
					if(page==0){//����J�b�������
						g.setColor(69,118,181);
						g.drawRoundRect(disWidth+52, disHeight+93, 95, 15, 15, 15);
						g.drawString(nowString[0],disWidth+22, disHeight+90, 0);
					}
					if(page==0 || page==2){//����J�b���P�C���W�ٮ����
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

	//�P�_��������ﶵ
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
					case 2://�n�J
						if(page==0){//��b��J�b���e����
							//����J�b��
							if( username.equals("") || pass.equals("") ){
								g.drawString("�u�� ��J�b���K�X�v", disWidth+75, disHeight+140, 0);
								flushGraphics();
							}
							else if( username.equals("xlinx") && pass.equals("x") )
							{
								runGame();
								g.drawString("�i�J�C��...", disWidth+15, disHeight+129, 0);
								flushGraphics();
							}
							else
							{//�s��Server
								try{
									GameMain.conn.setLoginAccess(username,pass);//�e�X�b���K�X
								}catch(Exception e){e.printStackTrace();}
								g.setColor(255,255,255);
								g.drawString("�u�n�J���v", disWidth+95, disHeight+140, 0);
								flushGraphics();
								while(!ConnectionServer.connFin);//Server�O�_�^�ǧ���T
								page=0;
								drawDisplay();
								g.setColor(255,255,255);
								gameUserInfCount=0;
								//���\�n�J�N�~���U�@����(�Ĥ@���i�J�C��)
								if(GameMain.conn.getConnSucess()==1){
									g.drawString("�u�n�J���\�v", disWidth+95, disHeight+140, 0);
									flushGraphics();
									RoleDataValue.userpass = pass;
									page=1;
									changePage();
								}else if(GameMain.conn.getConnSucess()==0){//�n�J,���O�Ĥ@���C��
									try{
										page=-2;
										g.drawString("�u�n�J���\�v", disWidth+95, disHeight+140, 0);
										flushGraphics();
										RoleDataValue.userpass = pass;
										g.drawString("Ū���ϥΪ̸��....", disWidth+15, disHeight+69, 0);
										flushGraphics();
										GameMain.conn.setGameName("");
										while(!ConnectionServer.connFin);//Server�O�_�^�ǧ���T
										g.drawString("�H�����A��l��.....", disWidth+15, disHeight+89, 0);
										flushGraphics();
										roleData.getFirstRoleStateForInternet(GameMain.conn.getRequestState());
										roleData.loginSuccessStart();
										g.drawString("�a�Ϫ�l��..........", disWidth+15, disHeight+109, 0);
										flushGraphics();
									}catch(Exception e){e.printStackTrace();}
									runGame();
									g.drawString("�i�J�C��...", disWidth+15, disHeight+129, 0);
									flushGraphics();									
								}else{
									g.drawString("�u�n�J���ѡv", disWidth+95, disHeight+140, 0);
									flushGraphics();
								}
							}
						}
						else if(page==1){//��e���b�C��������
							if(gameUserInfCount<gameUserInf.length)//�p�G�C�����������t��
								changePage();
							else{//�C����������,�e�����ܬ���J�C���W��
								page=2;
								gameUserInfCount=20;
								changePage();
							}
						}
						else if(page==2){//��J�W�٧���,�e�����ܬ�����إߧ���
							if(nickname.equals(""))
								g.drawString("�u�п�J�W�١v",disWidth+90,disHeight+140,0);
							else{
								page=3;
								changePage();								
							}
						}
						else if(page==3){//�Ĥ@�i�J�C��,������J�W��
							page=-2;
							GameMain.conn.setGameName(nickname);
							while(!ConnectionServer.connFin);//Server�O�_�^�ǧ���T
							try{
								roleData.getFirstRoleStateForInternet(GameMain.conn.getRequestState());
								roleData.loginSuccessStart();
							}catch(Exception e){e.printStackTrace();}
							runGame();
						}
						break;
					case 3://�^�D���,�W�@��
						if(page==0){
							page=-1;
							menustate=0;
							menuNum = 3;
						}
						//�P�_��GameInformation��,�n�^��J�b���K�X
						if(page==1 && gameUserInfCount==5)
							page--;
						else if(page==2 || page==3)//�P�_���OGameInformation��
							page--;
						//����GameInformaion������
						if(page==1){
							gameUserInfCount-=5*gameUserInfPage;
							gameUserInfPage++;
							if(gameUserInfPage>=3)
								gameUserInfPage=2;
							changePage();
						}//�^���J�m�W����
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
	
	//	�e�X�ҿ�J���r
	public void showBufferString(){
		if(page==0){
			drawUser_pass();
		}
		else if(page==2){
			g.setColor(255,255,255);
			g.drawString(nickname, disWidth+60, disHeight+115, 0);
		}
	}
	
	//�P�_�O�_���K�X
	public void drawUser_pass(){
		g.setColor(255,255,255);
		g.drawString(username, disWidth+60, disHeight+95, 0);
		if(pass.equals(""))
			g.drawString("", disWidth+60, disHeight+115, 0);
		else
			g.drawString("*******", disWidth+60, disHeight+115, 0);
	}

	//�����{��
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
	
	//�]�w����display
	public void setDisplay(){
		display.setCurrent(this);
		drawDisplay();
		changeMeun();
		showBufferString();
	}
	
	//	�b���K�X��J
	class InputText implements CommandListener{
		private Command okCommand;
		private TextField txt;
		private String strtxt;
		
		public InputText(String str){
			Form form = new Form("�п�J"+str);
			if(page==0)
				txt=new TextField(str, "", 10, TextField.ANY);
			else if(page==2)
				txt=new TextField(str, "", 5, TextField.ANY);
			okCommand = new Command("�T�w", Command.OK, 1);
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