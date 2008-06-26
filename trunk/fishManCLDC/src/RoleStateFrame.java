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
import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.game.GameCanvas;

public class RoleStateFrame extends GameCanvas{
	
	private static int menustate = 0;//�ثe����쪺�ﶵ
	private static int menuNum = 6;//�@���X�ӿﶵ
	private static int page = 0;//����
	
	private static int itemSelect = 0;
	private static int storeIndex=0;//���~����
	private static int itemTotal = 0;
	private static int itemMenuSelect = 0;
	private static boolean equipmentEvent = false;//���ܨϥθ˳�
	private static boolean itemEvent = false;//true�ɪ����ܪ��~�b�M�椤
	private static boolean itemBuy = false;//��false����٨S�T�w�n�ϥ�,true��ܽT�w�n�ϥ�
	private static boolean drawItemMenu = false;//�O�_���n�e���~
	private static boolean itemStart = false;//�O�_�n���˳�
	private static boolean gameInfStart = false;
	
	private static int disWidth = 0;//LayManager��ڦ�m

	private static int disHeight = 0;//LayManager��ڦ�m
	
	private static int equipmentNum = 0;//�ثe�˳ƽs��
	
	private static int lineNum = 0;//���
	
	private static Graphics g = null;
	private static Image img = null;
	private static Image itemMenu = createImage("/image/gameInterface/BattleItem.png");
	private static Image pageIndex = createImage("/image/gameInterface/page_index.png");
	private static Image pageIndex_2 = createImage("/image/gameInterface/s_page2.png");
	private static Image roleStateMenu = createImage("/image/gameInterface/roleStateMenu.png");
	private static Image buyMenu = createImage("/image/gameInterface/buyMenu.png");
	private static Image description = createImage("/image/gameInterface/description.png");
	private static Image downInf = createImage("/image/gameInterface/downInf.png");
	private static Image img2[] = null;
	private static Image titleImg = null;
	
	private static String itemBuff[][] = new String[99][0];
	private static String itemAmount[] = new String[99];
	
	private static DataBaseReader db = null;
	
	private static RoleDataValue roleData = null;
	
	private static String[] menuStr = new String[6];
	private static String[] menuStr1 = { "�˳Ƴ]�w", "�C������", "�^��C��", "���~���", " ������", "���}�C��"};
	private static String[] menuStr2 = {"�ɫ~�D��","�@��D��","�ƥ�D��","���b�D��","�^�D���"};
	private static String[] menuStr3 = {"�ާ@����","�G�Ƥ���","�԰�����","��L����","�t�Τ���","�^�D���"};
	private static String[] itemUseStr = {"�ϥ�","����","���}"};
	private static String[] equipmentStr = new String[8];
	
	private static String[] itemNameArray = {"Item","","Jewelry","","Battle","Protect","Event",
			"Monster","Pole","Bait","Tonic","Talk",""};
	
	private static mapCanvars mapca = null;
	private static GameMain midlet;
	
	public RoleStateFrame(RoleDataValue roleData_,mapCanvars mapca_,GameMain midlet_){
		super(false);
		setFullScreenMode(true);
		roleData = roleData_;
		mapca = mapca_;
		midlet = midlet_;
		//�˳Ƴ]�w
		db = new DataBaseReader();
		roleStateStart();
	}
	
	public void roleStateStart(){
		disWidth = (getWidth() - 176) / 2;//����ù��������I
		disHeight = (getHeight() - 208) / 2;//����ù��������I
		for(int i=0;i<itemBuff.length;i++)
			itemBuff[i] = null;
		page=0;
		changPage();
		drawDisplay();
	}
	
	//�]�w�ثe�˳�
	public void setNowEquipment(){
		if(RoleDataValue.pole!=0){
			equipmentStr[0] = db.readDBField(8,RoleDataValue.pole,2);
		}else{
			equipmentStr[0] = "�L";
		}
		
		if(RoleDataValue.proBody!=0){
			equipmentStr[1] = db.readDBField(5,RoleDataValue.proBody,2);
		}else{
			equipmentStr[1] = "�L";
		}
		
		if(RoleDataValue.proHand!=0){
			equipmentStr[2] = db.readDBField(5,RoleDataValue.proHand,2);
		}else{
			equipmentStr[2] = "�L";
		}
		
		if(RoleDataValue.proWaist!=0){
			equipmentStr[3] = db.readDBField(5,RoleDataValue.proWaist,2);
		}else{
			equipmentStr[3] = "�L";
		}
		
		if(RoleDataValue.proFoot!=0){
			equipmentStr[4] = db.readDBField(5,RoleDataValue.proFoot,2);
		}else{
			equipmentStr[4] = "�L";
		}
		
		if(RoleDataValue.jewelry!=0){
			equipmentStr[5] = db.readDBField(2,RoleDataValue.jewelry,2);
		}else{
			equipmentStr[5] = "�L";
		}
		
		if(RoleDataValue.proHead!=0){
			equipmentStr[6] = db.readDBField(5,RoleDataValue.proHead,2);
		}else{
			equipmentStr[6] = "�L";
		}
		
		if(RoleDataValue.bait!=0){
			equipmentStr[7] = db.readDBField(9,RoleDataValue.bait,2);
		}else{
			equipmentStr[7] = "�L";
		}
	}
	
	public void drawDisplay() {
		g = getGraphics();
		switch (page) {
			case 0://�@�몬�A
				g.drawImage(pageIndex,disWidth+0,disHeight+0,0);
				g.drawImage(roleStateMenu,disWidth+0,disHeight+144,0);
				g.drawString(RoleDataValue.name,disWidth+126,disHeight+190,0);
				break;
			case 1://��ܸ˳Ƴ]�w
				setNowEquipment();//�]�w�ثe�˳�
				if(!itemStart){
					g.drawImage(pageIndex_2,disWidth+0,disHeight+0,0);
					g.drawImage(downInf,disWidth+0,disHeight+144,0);
				}
				if(itemStart){//��n�˳ƪ��~��
					if(!itemBuy){
						titleImg = createImage("/image/gameInterface/text1.png");
						g.drawImage(itemMenu,disWidth+3,disHeight+0,0);
						g.drawImage(titleImg,disWidth+117,disHeight+8,0);
					}
					if(itemBuy)
						g.drawImage(buyMenu,disWidth+121,disHeight+77,0);
				}
				break;
			case 2://��ܹC������,�G�Ƥ���
				if(!itemStart){
					g.drawImage(pageIndex,disWidth+0,disHeight+0,0);
					showRoleStateInf();
				}
				g.drawImage(roleStateMenu,disWidth+0,disHeight+144,0);
				if(itemStart){	
					g.drawImage(description,disWidth+3,disHeight+0,0);
					g.setColor(128,255,0);
					setFont(16);
					g.drawString(menuStr[menustate],disWidth+55,disHeight+5,0);
					showGameStr();//��ܤ���
				}
				break;
			case 3://��ܦ^��C��
				
				break;
			case 4://��ܪ��~�ϥ�
				if(!itemStart){
					g.drawImage(pageIndex,disWidth+0,disHeight+0,0);
					g.drawImage(roleStateMenu,disWidth+0,disHeight+144,0);
					g.drawString(RoleDataValue.name,disWidth+126,disHeight+191,0);
					showRoleStateInf();
				}
				//g.drawImage(roleStateMenu,disWidth+0,disHeight+144,0);
				//g.drawString(RoleDataValue.name,disWidth+126,disHeight+191,0);
				if(itemStart){//��n�ϥΪ��~��
					if(!itemBuy){
						titleImg = createImage("/image/gameInterface/text2.png");
						g.drawImage(itemMenu,disWidth+3,disHeight+0,0);
						g.drawImage(titleImg,disWidth+117,disHeight+8,0);
						titleImg = createImage("/image/gameInterface/text3.png");
						g.drawImage(titleImg,disWidth+117,disHeight+42,0);
					}
					if(itemBuy)
						g.drawImage(buyMenu,disWidth+121,disHeight+77,0);
				}
				break;
			case 5://��ܥ�����
				g.drawImage(description,disWidth+3,disHeight+0,0);
				g.setColor(128,255,0);
				setFont(16);
				g.drawString(" ������",disWidth+55,disHeight+5,0);
				showGameStr();//��ܤ���
				break;
			case 6://������}�C��
				break;
		}
		flushGraphics();
		changeMenu();
	}
	
	//���game����
	public void showGameStr(){
		setFont(0);
		if(page==5)
			menustate=5;
		if(GameInfString.gameInfStr[menustate].length<=lineNum){
			//System.out.println("false");
			itemStart = false;
			gameInfStart = false;
			if(page==5){
				equipmentEvent = false;
				page=0;
				menustate = 0;
			}
			lineNum = 0;
			g.drawImage(roleStateMenu,disWidth+0,disHeight+144,0);
			g.drawImage(pageIndex,disWidth+0,disHeight+0,0);
			showRoleStateInf();
		}else{
			for(int i=0,k=0;i<12;i++,k+=14){
				if(GameInfString.gameInfStr[menustate].length<=lineNum){
					g.setColor(0,0,255);
					g.drawString("�Ы����N��^���",disWidth+70,disHeight+186,0);
					break;
				}
				//System.out.println("line"+lineNum);
				g.setColor(0,0,0);
				g.drawString(GameInfString.gameInfStr[menustate][lineNum],disWidth+9,disHeight+26+k,0);
				lineNum++;
			}
			if(!(GameInfString.gameInfStr[menustate].length<=lineNum)){
				g.setColor(0,0,255);
				g.fillTriangle(disWidth+161,disHeight+196,disWidth+168,disHeight+196,disWidth+164,disHeight+200);
			}
		}
	}
	
	//���ܭ����ɡA��Ϥ�
	public void changPage() {
		switch (page) {
			case 0://�@�몬�A
				menustate = 0;
				menuNum = 6;
				menuStr = menuStr1;
				break;
			case 1://��ܸ˳Ƴ]�w
				//menustate = 0;
				menuNum = 9;
				itemUseStr[0] = "�˳�";
				if(itemStart & !drawItemMenu){
					
					System.out.println("search item");
					
					equipmentNum = 0;
					String equipmentKind = "0";
					String itemStr = "";
					switch (menustate) {//��ܨ쪺�˳�
						case 0://��ܳ���
							equipmentNum = 8;
							itemStr = "P";
							break;
						case 1://��ܦ�A
							equipmentNum = 5;
							equipmentKind = "2";
							itemStr = "G";
							break;
						case 2://��ܤ�M
							equipmentNum = 5;
							equipmentKind = "5";
							itemStr = "G";
							break;
						case 3://��ܤU��
							equipmentNum = 5;
							equipmentKind = "3";
							itemStr = "G";
							break;
						case 4://��ܾc�l
							equipmentNum = 5;
							equipmentKind = "4";
							itemStr = "G";
							break;
						case 5://��ܹ��~
							equipmentNum = 2;
							itemStr = "J";
							break;
						case 6://��ܴU�l
							equipmentNum = 5;
							equipmentKind = "1";
							itemStr = "G";
							break;
						case 7://��ܳ���
							equipmentNum = 9;
							itemStr = "B";
							break;
					}
					//�⪫�~�k��
					String tmp_ = "";
					String tmp[] = null;
					int n=0;
					//�j�M�D�����~
					roleData.setHashEnume();
					for(;RoleDataValue.enu.hasMoreElements();){
						tmp = (String[])RoleDataValue.enu.nextElement();
						if(tmp[0].indexOf(itemStr)==-1)
							continue;
						if(equipmentKind.equals("0")){
							//System.out.println("Find:"+tmp[0]);
							itemBuff[n] = db.readDBRecord(equipmentNum,
									Int( tmp[0].substring(1,tmp[0].length()) ));
							n++;
						}else{//�������,�P�_�����@���쪺����					
							tmp_ = db.readDBField(equipmentNum,
									Int(tmp[0].substring(1,tmp[0].length())),8);
							if(tmp_.equals(equipmentKind)){
								//System.out.println("Find:"+tmp[0]);
								itemBuff[n] = db.readDBRecord(equipmentNum,
										Int( tmp[0].substring(1,tmp[0].length()) ));
									n++;
							}
						}
					}
					itemSelect=0;
					storeIndex=0;
					itemTotal = n;
					if(itemTotal==0){
						page=1;
						itemStart = false;
					}else{
						img2 = new Image[itemTotal];
						for(int i=0;i<itemTotal;i++){
							img2[i] = createImage("/image/" + itemNameArray[equipmentNum] +"/"+ itemBuff[i][1] + ".png");
						}
						//System.out.println("Total:"+itemTotal+"|"+page);
						drawItemMenu = true;
					}
				}
				break;
			case 2://��ܹC������,�G�Ƥ���
				if(!itemStart){
					menustate = 0;
					menuNum = 6;
					menuStr = menuStr3;
				}
				break;
			case 3://��ܦ^��C��
				
				break;
			case 4://��ܪ��~�ϥ�
				menuNum = 5;
				//itemUseStr[0] = "�ϥ�";
				if(itemStart & !drawItemMenu){
					equipmentNum = 0;
					String itemStr = "";
					switch (menustate) {//��ܨ쪺�˳�
						case 0://��ܸɫ~�D��
							equipmentNum = 10;
							itemStr = "T";
							itemUseStr[0] = "�ϥ�";
							break;
						case 1://��ܤ@��D��
							equipmentNum = 0;
							itemStr = "I";
							itemUseStr[0] = "�^��";
							break;
						case 2://��ܨƥ�D��
							equipmentNum = 6;
							itemStr = "E";
							itemUseStr[0] = "���";
							break;
						case 3://��ܨ��b�D��
							equipmentNum = 11;
							itemStr = "K";
							itemUseStr[0] = "���";
							break;
					}
					//�⪫�~�k��
					String tmp[] = null;
					int n=0;
					//�j�M�D�����~
					roleData.setHashEnume();
					for(;RoleDataValue.enu.hasMoreElements();){
						tmp = (String[])RoleDataValue.enu.nextElement();
						//�p�G�s���j��n�j�M��Ʈw���ƶq�h���j�M
						if(tmp[0].indexOf(itemStr)==-1)
							continue;
						System.out.println("equipmentNum:"+equipmentNum+"|"+tmp[0]+"|");
						itemBuff[n] = db.readDBRecord(equipmentNum,
								Int( tmp[0].substring(1,tmp[0].length()) ));
						
						//itemAmount[n] = tmp[1];
						n++;
					}
					itemSelect=0;
					storeIndex=0;
					itemTotal = n;//�j�M��ŦX�����~�ƶq
					if(itemTotal==0){
						page=4;
						itemStart = false;
					}else{
						img2 = new Image[itemTotal];
						for(int i=0;i<itemTotal;i++){
							img2[i] = createImage("/image/" + itemNameArray[equipmentNum] +"/"+ itemBuff[i][1] + ".png");
						}
						//System.out.println("Total:"+itemTotal+"|"+page);
						drawItemMenu = true;
					}
				}else//����l
					menustate = 0;
				break;
			case 5://��ܥ�����
				
				break;
			case 6://������}�C��
				
				break;
		}
		drawDisplay();
	}
	
	public void changeMenu(){
		setFont(0);
		if(page==0 || (page==2 && !itemStart) ){//�@�몬�A,�C������
			switch (menustate) {
				case 0:
					//Select----------------------------
					setColor(true);
					g.drawString(menuStr[0], disWidth + 8, disHeight + 153, 0);
					//----------------------------------
					setColor(false);
					g.drawString(menuStr[1], disWidth + 8, disHeight + 169, 0);
					g.drawString(menuStr[2], disWidth + 8, disHeight + 185, 0);
					g.drawString(menuStr[3], disWidth + 62, disHeight + 153, 0);
					g.drawString(menuStr[4], disWidth + 62, disHeight + 169, 0);
					g.drawString(menuStr[5], disWidth + 62, disHeight + 185, 0);
					break;
				case 1:
					setColor(false);
					g.drawString(menuStr[0], disWidth + 8, disHeight + 153, 0);
					//Select----------------------------
					setColor(true);
					g.drawString(menuStr[1], disWidth + 8, disHeight + 169, 0);
					//----------------------------------
					setColor(false);
					g.drawString(menuStr[2], disWidth + 8, disHeight + 185, 0);
					g.drawString(menuStr[3], disWidth + 62, disHeight + 153, 0);
					g.drawString(menuStr[4], disWidth + 62, disHeight + 169, 0);
					g.drawString(menuStr[5], disWidth + 62, disHeight + 185, 0);
					break;
				case 2:
					setColor(false);
					g.drawString(menuStr[0], disWidth + 8, disHeight + 153, 0);
					g.drawString(menuStr[1], disWidth + 8, disHeight + 169, 0);
					//Select----------------------------
					setColor(true);
					g.drawString(menuStr[2], disWidth + 8, disHeight + 185, 0);
					//----------------------------------
					setColor(false);
					g.drawString(menuStr[3], disWidth + 62, disHeight + 153, 0);
					g.drawString(menuStr[4], disWidth + 62, disHeight + 169, 0);
					g.drawString(menuStr[5], disWidth + 62, disHeight + 185, 0);
					break;
				case 3:
					setColor(false);
					g.drawString(menuStr[0], disWidth + 8, disHeight + 153, 0);
					g.drawString(menuStr[1], disWidth + 8, disHeight + 169, 0);
					g.drawString(menuStr[2], disWidth + 8, disHeight + 185, 0);
					//Select----------------------------
					setColor(true);
					g.drawString(menuStr[3], disWidth + 62, disHeight + 153, 0);
					//----------------------------------
					setColor(false);
					g.drawString(menuStr[4], disWidth + 62, disHeight + 169, 0);
					g.drawString(menuStr[5], disWidth + 62, disHeight + 185, 0);
					break;
				case 4:
					setColor(false);
					g.drawString(menuStr[0], disWidth + 8, disHeight + 153, 0);
					g.drawString(menuStr[1], disWidth + 8, disHeight + 169, 0);
					g.drawString(menuStr[2], disWidth + 8, disHeight + 185, 0);
					g.drawString(menuStr[3], disWidth + 62, disHeight + 153, 0);
					//Select----------------------------
					setColor(true);
					g.drawString(menuStr[4], disWidth + 62, disHeight + 169, 0);
					//----------------------------------
					setColor(false);
					g.drawString(menuStr[5], disWidth + 62, disHeight + 185, 0);
					break;
				case 5:
					setColor(false);
					g.drawString(menuStr[0], disWidth + 8, disHeight + 153, 0);
					g.drawString(menuStr[1], disWidth + 8, disHeight + 169, 0);
					g.drawString(menuStr[2], disWidth + 8, disHeight + 185, 0);
					g.drawString(menuStr[3], disWidth + 62, disHeight + 153, 0);
					g.drawString(menuStr[4], disWidth + 62, disHeight + 169, 0);
					//Select----------------------------
					setColor(true);
					g.drawString(menuStr[5], disWidth + 62, disHeight + 185, 0);
					//----------------------------------
					break;
			}
			if(page==0)
				showRoleStateInf();
		}
		if(page==1 || page==4){//�˳Ƴ]�w
			if(equipmentEvent==true && itemBuy==true){
				switch (itemMenuSelect) {
					case 0:
						//Select----------------------------
						setColor(true);
						g.drawString(itemUseStr[0], disWidth + 130, disHeight + 85, 0);
						//----------------------------------
						setColor(false);
						g.drawString(itemUseStr[1], disWidth + 130, disHeight + 100, 0);
						g.drawString(itemUseStr[2], disWidth + 130, disHeight + 115, 0);
						break;
					case 1:
						setColor(false);
						g.drawString(itemUseStr[0], disWidth + 130, disHeight + 85, 0);
						//Select----------------------------
						setColor(true);
						g.drawString(itemUseStr[1], disWidth + 130, disHeight + 100, 0);
						//----------------------------------
						setColor(false);
						g.drawString(itemUseStr[2], disWidth + 130, disHeight + 115, 0);
						break;
					case 2:
						setColor(false);
						g.drawString(itemUseStr[0], disWidth + 130, disHeight + 85, 0);
						g.drawString(itemUseStr[1], disWidth + 130, disHeight + 100, 0);
						//Select----------------------------
						setColor(true);
						g.drawString(itemUseStr[2], disWidth + 130, disHeight + 115, 0);
						//----------------------------------
						break;
				}
			}
			else if(itemStart){//�e���~��T�bitemMenu
				setFont(0);
				g.drawImage(itemMenu,disWidth+3,disHeight+0,0);
				titleImg = createImage("/image/gameInterface/text3.png");
				g.drawImage(titleImg,disWidth+117,disHeight+42,0);
				if(page==1){
					titleImg = createImage("/image/gameInterface/text1.png");
					g.drawImage(titleImg,disWidth+117,disHeight+8,0);
				}else if(page==4){
					titleImg = createImage("/image/gameInterface/text2.png");
					g.drawImage(titleImg,disWidth+117,disHeight+8,0);
				}
				switch (itemSelect) {
					case 0:
						//Select----------------------------
						setColor(true);
						g.drawString(itemBuff[0+storeIndex][2], disWidth + 50, disHeight + 15, 0);
						g.drawImage(img2[0+storeIndex],disWidth+10,disHeight+6,0);
						setColor(false);
						showItemInf(0+storeIndex);
						//----------------------------------
						setColor(false);
						if(storeIndex+2<=itemTotal){//��u�ѤG�Ӫ��~�b���ɦL�X
							g.drawString(itemBuff[1+storeIndex][2], disWidth + 50, disHeight + 48, 0);
							g.drawImage(img2[1+storeIndex],disWidth+10,disHeight+39,0);
						}
						if(storeIndex+3<=itemTotal){//��u�ѤT�Ӫ��~�b���ɦL�X
							g.drawString(itemBuff[2+storeIndex][2], disWidth + 50, disHeight + 81, 0);
							g.drawImage(img2[2+storeIndex],disWidth+10,disHeight+72,0);
						}
						if(storeIndex+4<=itemTotal){//�|�Ӫ��~�b���ɦL�X
							g.drawString(itemBuff[3+storeIndex][2], disWidth + 50, disHeight + 114, 0);
							g.drawImage(img2[3+storeIndex],disWidth+10,disHeight+105,0);
						}
						break;
					case 1:
						setColor(false);
						g.drawString(itemBuff[0+storeIndex][2], disWidth + 50, disHeight + 15, 0);
						g.drawImage(img2[0+storeIndex],disWidth+10,disHeight+6,0);
						//Select----------------------------
						setColor(true);
						if(storeIndex+2<=itemTotal){
							g.drawString(itemBuff[1+storeIndex][2],disWidth + 50, disHeight + 48, 0);
							g.drawImage(img2[1+storeIndex],disWidth+10,disHeight+39,0);
							setColor(false);
							showItemInf(1+storeIndex);
						}
						//----------------------------------
						setColor(false);
						if(storeIndex+3<=itemTotal){
							g.drawString(itemBuff[2+storeIndex][2], disWidth + 50, disHeight + 81, 0);
							g.drawImage(img2[2+storeIndex],disWidth+10,disHeight+72,0);
						}
						if(storeIndex+4<=itemTotal){
							g.drawString(itemBuff[3+storeIndex][2], disWidth + 50, disHeight + 114, 0);
							g.drawImage(img2[3+storeIndex],disWidth+10,disHeight+105,0);
						}
						break;
					case 2:
						setColor(false);
						g.drawString(itemBuff[0+storeIndex][2], disWidth + 50, disHeight + 15, 0);
						g.drawImage(img2[0+storeIndex],disWidth+10,disHeight+6,0);
						setColor(false);
						if(storeIndex+2<=itemTotal){
							g.drawString(itemBuff[1+storeIndex][2], disWidth + 50, disHeight + 48, 0);
							g.drawImage(img2[1+storeIndex],disWidth+10,disHeight+39,0);
						}
						//Select----------------------------
						setColor(true);
						if(storeIndex+3<=itemTotal){
							g.drawString(itemBuff[2+storeIndex][2], disWidth + 50, disHeight + 81, 0);
							g.drawImage(img2[2+storeIndex],disWidth+10,disHeight+72,0);
							setColor(false);
							showItemInf(2+storeIndex);
						}
						//----------------------------------
						setColor(false);
						if(storeIndex+4<=itemTotal){
							g.drawString(itemBuff[3+storeIndex][2], disWidth + 50, disHeight + 114, 0);
							g.drawImage(img2[3+storeIndex],disWidth+10,disHeight+105,0);
						}
						break;
					case 3:
						setColor(false);
						g.drawString(itemBuff[0+storeIndex][2], disWidth + 50, disHeight + 15, 0);
						g.drawImage(img2[0+storeIndex],disWidth+10,disHeight+6,0);
						if(storeIndex+2<=itemTotal){
							g.drawString(itemBuff[1+storeIndex][2], disWidth + 50, disHeight + 48, 0);
							g.drawImage(img2[1+storeIndex],disWidth+10,disHeight+39,0);
						}
						if(storeIndex+3<=itemTotal){
							g.drawString(itemBuff[2+storeIndex][2], disWidth + 50, disHeight + 81, 0);
							g.drawImage(img2[2+storeIndex],disWidth+10,disHeight+72,0);
						}
						//Select----------------------------
						setColor(true);
						if(storeIndex+4<=itemTotal){
							g.drawString(itemBuff[3+storeIndex][2], disWidth + 50, disHeight + 114, 0);
							g.drawImage(img2[3+storeIndex],disWidth+10,disHeight+105,0);
							setColor(false);
							showItemInf(3+storeIndex);
						}
						//----------------------------------
						break;
					}
				}else if(page==1){
					switch (menustate) {
						case 0:
							//Select----------------------------
							setColor(true);
							g.drawString(equipmentStr[0], disWidth + 41, disHeight + 30, 0);
							g.setColor(255,255,255);
							g.fillRect(disWidth + 126, disHeight + 33,32,32);
							if(!equipmentStr[0].equals("�L"))
								g.drawImage(RoleDataValue.poleImg,disWidth + 126, disHeight + 33,0);
							//----------------------------------
							setColor(false);
							g.drawString(equipmentStr[1], disWidth + 41, disHeight + 45, 0);
							g.drawString(equipmentStr[2], disWidth + 41, disHeight + 60, 0);
							g.drawString(equipmentStr[3], disWidth + 41, disHeight + 74, 0);
							g.drawString(equipmentStr[4], disWidth + 41, disHeight + 89, 0);
							g.drawString(equipmentStr[5], disWidth + 41, disHeight + 104, 0);
							g.drawString(equipmentStr[6], disWidth + 41, disHeight + 118, 0);
							g.drawString(equipmentStr[7], disWidth + 122, disHeight + 105, 0);
							g.drawString("��    �}", disWidth + 122, disHeight + 120, 0);
							break;
						case 1:
							setColor(false);
							g.drawString(equipmentStr[0], disWidth + 41, disHeight + 30, 0);
							//Select----------------------------
							setColor(true);
							g.drawString(equipmentStr[1], disWidth + 41, disHeight + 45, 0);
							g.setColor(255,255,255);
							g.fillRect(disWidth + 126, disHeight + 33,32,32);
							if(!equipmentStr[1].equals("�L"))
								g.drawImage(RoleDataValue.proBodyImg,disWidth + 126, disHeight + 33,0);
							//----------------------------------
							setColor(false);
							g.drawString(equipmentStr[2], disWidth + 41, disHeight + 60, 0);
							g.drawString(equipmentStr[3], disWidth + 41, disHeight + 74, 0);
							g.drawString(equipmentStr[4], disWidth + 41, disHeight + 89, 0);
							g.drawString(equipmentStr[5], disWidth + 41, disHeight + 104, 0);
							g.drawString(equipmentStr[6], disWidth + 41, disHeight + 118, 0);
							g.drawString(equipmentStr[7], disWidth + 122, disHeight + 105, 0);
							g.drawString("��    �}", disWidth + 122, disHeight + 120, 0);
							break;
						case 2:
							setColor(false);
							g.drawString(equipmentStr[0], disWidth + 41, disHeight + 30, 0);
							g.drawString(equipmentStr[1], disWidth + 41, disHeight + 45, 0);
							//Select----------------------------
							setColor(true);
							g.drawString(equipmentStr[2], disWidth + 41, disHeight + 60, 0);
							g.setColor(255,255,255);
							g.fillRect(disWidth + 126, disHeight + 33,32,32);
							if(!equipmentStr[2].equals("�L"))
								g.drawImage(RoleDataValue.proHandImg,disWidth + 126, disHeight + 33,0);
							//----------------------------------
							setColor(false);
							g.drawString(equipmentStr[3], disWidth + 41, disHeight + 74, 0);
							g.drawString(equipmentStr[4], disWidth + 41, disHeight + 89, 0);
							g.drawString(equipmentStr[5], disWidth + 41, disHeight + 104, 0);
							g.drawString(equipmentStr[6], disWidth + 41, disHeight + 118, 0);
							g.drawString(equipmentStr[7], disWidth + 122, disHeight + 105, 0);
							g.drawString("��    �}", disWidth + 122, disHeight + 120, 0);
							break;
						case 3:
							setColor(false);
							g.drawString(equipmentStr[0], disWidth + 41, disHeight + 30, 0);
							g.drawString(equipmentStr[1], disWidth + 41, disHeight + 45, 0);
							g.drawString(equipmentStr[2], disWidth + 41, disHeight + 60, 0);
							//Select----------------------------
							setColor(true);
							g.drawString(equipmentStr[3], disWidth + 41, disHeight + 74, 0);
							g.setColor(255,255,255);
							g.fillRect(disWidth + 126, disHeight + 33,32,32);
							if(!equipmentStr[3].equals("�L"))
								g.drawImage(RoleDataValue.proWaistImg,disWidth + 126, disHeight + 33,0);
							//----------------------------------
							setColor(false);
							g.drawString(equipmentStr[4], disWidth + 41, disHeight + 89, 0);
							g.drawString(equipmentStr[5], disWidth + 41, disHeight + 104, 0);
							g.drawString(equipmentStr[6], disWidth + 41, disHeight + 118, 0);
							g.drawString(equipmentStr[7], disWidth + 122, disHeight + 105, 0);
							g.drawString("��    �}", disWidth + 122, disHeight + 120, 0);
							break;
						case 4:
							setColor(false);
							g.drawString(equipmentStr[0], disWidth + 41, disHeight + 30, 0);
							g.drawString(equipmentStr[1], disWidth + 41, disHeight + 45, 0);
							g.drawString(equipmentStr[2], disWidth + 41, disHeight + 60, 0);
							g.drawString(equipmentStr[3], disWidth + 41, disHeight + 74, 0);
							//Select----------------------------
							setColor(true);
							g.drawString(equipmentStr[4], disWidth + 41, disHeight + 89, 0);
							g.setColor(255,255,255);
							g.fillRect(disWidth + 126, disHeight + 33,32,32);
							if(!equipmentStr[4].equals("�L"))
								g.drawImage(RoleDataValue.proFootImg,disWidth + 126, disHeight + 33,0);
							//----------------------------------
							setColor(false);
							g.drawString(equipmentStr[5], disWidth + 41, disHeight + 104, 0);
							g.drawString(equipmentStr[6], disWidth + 41, disHeight + 118, 0);
							g.drawString(equipmentStr[7], disWidth + 122, disHeight + 105, 0);
							g.drawString("��    �}", disWidth + 122, disHeight + 120, 0);
							break;
						case 5:
							setColor(false);
							g.drawString(equipmentStr[0], disWidth + 41, disHeight + 30, 0);
							g.drawString(equipmentStr[1], disWidth + 41, disHeight + 45, 0);
							g.drawString(equipmentStr[2], disWidth + 41, disHeight + 60, 0);
							g.drawString(equipmentStr[3], disWidth + 41, disHeight + 74, 0);
							g.drawString(equipmentStr[4], disWidth + 41, disHeight + 89, 0);
							//Select----------------------------
							setColor(true);
							g.drawString(equipmentStr[5], disWidth + 41, disHeight + 104, 0);
							g.setColor(255,255,255);
							g.fillRect(disWidth + 126, disHeight + 33,32,32);
							if(!equipmentStr[5].equals("�L"))
								g.drawImage(RoleDataValue.jewelryImg,disWidth + 126, disHeight + 33,0);
							//----------------------------------
							setColor(false);							
							g.drawString(equipmentStr[6], disWidth + 41, disHeight + 118, 0);
							g.drawString(equipmentStr[7], disWidth + 122, disHeight + 105, 0);
							g.drawString("��    �}", disWidth + 122, disHeight + 120, 0);
							break;
						case 6:
							setColor(false);
							g.drawString(equipmentStr[0], disWidth + 41, disHeight + 30, 0);
							g.drawString(equipmentStr[1], disWidth + 41, disHeight + 45, 0);
							g.drawString(equipmentStr[2], disWidth + 41, disHeight + 60, 0);
							g.drawString(equipmentStr[3], disWidth + 41, disHeight + 74, 0);
							g.drawString(equipmentStr[4], disWidth + 41, disHeight + 89, 0);
							g.drawString(equipmentStr[5], disWidth + 41, disHeight + 104, 0);
							//Select----------------------------
							setColor(true);
							g.drawString(equipmentStr[6], disWidth + 41, disHeight + 118, 0);
							g.setColor(255,255,255);
							g.fillRect(disWidth + 126, disHeight + 33,32,32);
							if(!equipmentStr[6].equals("�L"))
								g.drawImage(RoleDataValue.proHeadImg,disWidth + 126, disHeight + 33,0);
							//----------------------------------
							setColor(false);							
							g.drawString(equipmentStr[7], disWidth + 122, disHeight + 105, 0);
							g.drawString("��    �}", disWidth + 122, disHeight + 120, 0);
							break;
						case 7:
							setColor(false);
							g.drawString(equipmentStr[0], disWidth + 41, disHeight + 30, 0);
							g.drawString(equipmentStr[1], disWidth + 41, disHeight + 45, 0);
							g.drawString(equipmentStr[2], disWidth + 41, disHeight + 60, 0);
							g.drawString(equipmentStr[3], disWidth + 41, disHeight + 74, 0);
							g.drawString(equipmentStr[4], disWidth + 41, disHeight + 89, 0);
							g.drawString(equipmentStr[5], disWidth + 41, disHeight + 104, 0);
							g.drawString(equipmentStr[6], disWidth + 41, disHeight + 118, 0);
							//Select----------------------------
							setColor(true);							
							g.drawString(equipmentStr[7], disWidth + 122, disHeight + 105, 0);
							g.setColor(255,255,255);
							g.fillRect(disWidth + 126, disHeight + 33,32,32);
							if(!equipmentStr[7].equals("�L"))
								g.drawImage(RoleDataValue.baitImg,disWidth + 126, disHeight + 33,0);
							//----------------------------------
							setColor(false);
							g.drawString("��    �}", disWidth + 122, disHeight + 120, 0);
							break;
						case 8:
							setColor(false);
							g.drawString(equipmentStr[0], disWidth + 41, disHeight + 30, 0);
							g.drawString(equipmentStr[1], disWidth + 41, disHeight + 45, 0);
							g.drawString(equipmentStr[2], disWidth + 41, disHeight + 60, 0);
							g.drawString(equipmentStr[3], disWidth + 41, disHeight + 74, 0);
							g.drawString(equipmentStr[4], disWidth + 41, disHeight + 89, 0);
							g.drawString(equipmentStr[5], disWidth + 41, disHeight + 104, 0);
							g.drawString(equipmentStr[6], disWidth + 41, disHeight + 118, 0);							
							g.drawString(equipmentStr[7], disWidth + 122, disHeight + 105, 0);
							//Select----------------------------
							setColor(true);
							g.drawString("��    �}", disWidth + 122, disHeight + 120, 0);
							//----------------------------------
							g.setColor(255,255,255);
							g.fillRect(disWidth + 126, disHeight + 33,32,32);
							break;
					}
				}
			}
			if(page==4 && !itemStart){//���~�ϥ�
				switch (menustate) {
					case 0:
						//Select----------------------------
						setColor(true);
						g.drawString(menuStr2[0], disWidth + 8, disHeight + 153, 0);
						//----------------------------------
						setColor(false);
						g.drawString(menuStr2[1], disWidth + 8, disHeight + 169, 0);
						g.drawString(menuStr2[2], disWidth + 8, disHeight + 185, 0);
						g.drawString(menuStr2[3], disWidth + 62, disHeight + 153, 0);
						g.drawString(menuStr2[4], disWidth + 62, disHeight + 169, 0);
						break;
					case 1:
						setColor(false);
						g.drawString(menuStr2[0], disWidth + 8, disHeight + 153, 0);
						//Select----------------------------
						setColor(true);
						g.drawString(menuStr2[1], disWidth + 8, disHeight + 169, 0);
						//----------------------------------
						setColor(false);
						g.drawString(menuStr2[2], disWidth + 8, disHeight + 185, 0);
						g.drawString(menuStr2[3], disWidth + 62, disHeight + 153, 0);
						g.drawString(menuStr2[4], disWidth + 62, disHeight + 169, 0);
						break;
					case 2:
						setColor(false);
						g.drawString(menuStr2[0], disWidth + 8, disHeight + 153, 0);
						g.drawString(menuStr2[1], disWidth + 8, disHeight + 169, 0);
						//Select----------------------------
						setColor(true);
						g.drawString(menuStr2[2], disWidth + 8, disHeight + 185, 0);
						//----------------------------------
						setColor(false);
						g.drawString(menuStr2[3], disWidth + 62, disHeight + 153, 0);
						g.drawString(menuStr2[4], disWidth + 62, disHeight + 169, 0);
						break;
					case 3:
						setColor(false);
						g.drawString(menuStr2[0], disWidth + 8, disHeight + 153, 0);
						g.drawString(menuStr2[1], disWidth + 8, disHeight + 169, 0);
						g.drawString(menuStr2[2], disWidth + 8, disHeight + 185, 0);
						//Select----------------------------
						setColor(true);
						g.drawString(menuStr2[3], disWidth + 62, disHeight + 153, 0);
						//----------------------------------
						setColor(false);
						g.drawString(menuStr2[4], disWidth + 62, disHeight + 169, 0);
						break;
					case 4:
						setColor(false);
						g.drawString(menuStr2[0], disWidth + 8, disHeight + 153, 0);
						g.drawString(menuStr2[1], disWidth + 8, disHeight + 169, 0);
						g.drawString(menuStr2[2], disWidth + 8, disHeight + 185, 0);
						g.drawString(menuStr2[3], disWidth + 62, disHeight + 153, 0);
						//Select----------------------------
						setColor(true);
						g.drawString(menuStr2[4], disWidth + 62, disHeight + 169, 0);
						//----------------------------------
						break;
				}
			}
		flushGraphics();
	}
	
	//��L�ƥ�
	protected void keyPressed(int keyCode) {
		switch( keyCode ){
			case -5://���UFIRE
				if(page==0){
					switch (menustate) {//�P�_�e������b���ӭ���
						case 0://��ܸ˳Ƴ]��
							equipmentEvent = true;
							page=1;
							changPage();
							break;
						case 1://��ܹC������,�G�Ƥ���
							equipmentEvent = true;
							page=2;
							changPage();
							break;
						case 2://��ܦ^��C��
							ConnectionServer.updateGameInf();
							page=0;
							mapca.threadStart();
							break;
						case 3://��ܪ��~�ϥ�
							equipmentEvent = true;
							page=4;
							changPage();
							break;
						case 4://��ܥ�����
							equipmentEvent = true;
							page=5;
							changPage();
							break;
						case 5://������}�C��
							ConnectionServer.updateGameInf();
							try{
								Thread.sleep(2000);
							}catch(Exception e){}
							GameMain.music.stop();
							midlet.exit();
							break;
					}
				}else if(page==1){//�˳Ƴ]�w�ﶵ
					if(equipmentEvent & !itemStart){
						itemStart = true;
						itemBuy=false;
					}else if(equipmentEvent & itemStart)
						itemEvent=true;
					
					if(itemEvent){//��ܨϥΦ����~
						itemEvent = false;
						itemBuy = !itemBuy;
						if(itemBuy==false){//�˳�
							itemEvent=true;
							if(itemMenuSelect==0){//�T�w
								itemEvent=false;
								drawItemMenu=false;
								itemStart = false;
								itemBuy = false;
								//equipmentEvent = false;
								//���ܸ˳�
								changeEquipment();
								page=1;
								System.out.println("use:"+(storeIndex+itemSelect+1));
							}else if(itemMenuSelect==1){//����
								itemBuy = false;
								page=1;
								//changPage();
								System.out.println("cancel");
							}else{//���}
								itemEvent=false;
								drawItemMenu=false;
								itemStart = false;
								itemBuy = false;
								//equipmentEvent = false;
								page=1;
								//changPage();
							}
						}
						itemMenuSelect=0;
					}
					
					if(menustate==8){
						itemEvent=false;
						drawItemMenu=false;
						itemStart = false;
						itemBuy = false;
						equipmentEvent = false;
						page=0;
					}
					changPage();
				}else if(page==2){//��ܹC������,�G�Ƥ���	
					if(equipmentEvent & !itemStart){
						itemStart = true;
						gameInfStart = true;
					}
					
					if(menustate==5){
						itemEvent=false;
						drawItemMenu=false;
						itemStart = false;
						itemBuy = false;
						gameInfStart = false;
						equipmentEvent = false;
						page=0;
					}
					changPage();
				}else if(page==3){//��ܦ^��C��
					
				}else if(page==4){//��ܪ��~�ϥ�
					if(equipmentEvent & !itemStart){
						itemStart = true;
						itemBuy=false;
					}else if(equipmentEvent & itemStart)
						itemEvent=true;
					
					if(itemEvent){//��ܨϥΦ����~
						itemEvent = false;
						itemBuy = !itemBuy;
						if(itemBuy==false){
							itemEvent=true;
							if(itemMenuSelect==0){//�T�w
								
								if(equipmentNum==10){//���~�ϥ�
									RoleDataValue.nowHP+=Integer.parseInt(itemBuff[storeIndex+itemSelect][3]);
									if(RoleDataValue.nowHP>RoleDataValue.maxHP)//�庡
										RoleDataValue.nowHP = RoleDataValue.maxHP;
									itemUserChange(storeIndex+itemSelect);
									page=4;
									itemEvent=false;
									drawItemMenu=false;
									itemStart = false;
									itemBuy = false;
								}else if(equipmentNum==0){//�^���@�몫�~
									roleData.useItem(itemBuff[storeIndex+itemSelect][0],true);
									if(roleData.getItemAmount(itemBuff[storeIndex+itemSelect][0]).equals("0")){
										itemSelect=0;
										storeIndex=0;
									}
									itemBuy = false;
									page=4;
								}else{
									roleData.removeItem(itemBuff[storeIndex+itemSelect][0]);
									page=4;
									itemEvent=false;
									drawItemMenu=false;
									itemStart = false;
									itemBuy = false;
								}
								System.out.println("use:"+(storeIndex+itemSelect+1));
							}else if(itemMenuSelect==1){//����
								itemBuy = false;
								page=4;
								//changPage();
								System.out.println("cancel");
							}else{//���}
								itemEvent=false;
								drawItemMenu=false;
								itemStart = false;
								itemBuy = false;
								//equipmentEvent = false;
								page=4;
								//changPage();
							}
						}
						itemMenuSelect=0;
						g.drawImage(buyMenu,disWidth+121,disHeight+77,0);
					}
					
					if(menustate==4){
						itemEvent=false;
						drawItemMenu=false;
						itemStart = false;
						itemBuy = false;
						equipmentEvent = false;
						page=0;
					}
					changPage();
				}else if(page==5){//��ܥ�����
					changPage();
				}else if(page==6){//������}�C��
					
				}
					
				break;
			case -1://���UUP
				if(itemBuy==true && (page==1 || page==4)){//�T�{�O�_�ϥΪ��~��
					if(itemMenuSelect <=0 )
						itemMenuSelect=3;
					itemMenuSelect--;
				}
				else if(itemStart==true && itemBuy==false && (page==1 || page==4)){//��ܭn�ϥΪ����~
					if (itemSelect <= 0){
						if(storeIndex!=0){//��ܪ��~����̩���
							storeIndex-=4;
							itemSelect = 4;
						}else{//��ܨ�̤W���~��
							itemSelect = 1;
						}
					}
					itemSelect--;
					//System.out.println("upitemSelect"+itemSelect);
				}
				else if(!gameInfStart){
					if(menustate <=0 )
						menustate=menuNum;
					menustate--;
				}				
				break;
			case -2://���UDOWN
				if(itemBuy==true && (page==1 || page==4)){//�T�{�O�_�ϥΪ��~��
					if(itemMenuSelect >=2 )
						itemMenuSelect=-1;
					itemMenuSelect++;
				}
				else if(itemStart==true && itemBuy==false && (page==1 || page==4)){//��ܭn�ϥΪ����~
					if(storeIndex+4<itemTotal){//���P�_���~�ٳѦh��,�O�_���W�X4��,�W�X�|�ӴN���`���
						if (itemSelect >= 4-1){
							storeIndex+=4;
							itemSelect = -1;
						}
					}else{//�p�G��檫�~�־l�|��,�P�_�ﶵ���X��
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
				}else if(!gameInfStart){
					if(menustate >=menuNum-1 )
						menustate=-1;
					menustate++;
				}
				break;
			case -4://���URIGHT
				if(page==0 || page==2 || page==4){
					if(menustate==0)
						menustate=3;
					else if(menustate==1)
						menustate=4;
					else if(menustate==2){
						if(page==4)
							menustate=4;
						else
							menustate=5;
					}
					else if(menustate==3)
						menustate=0;
					else if(menustate==4)
						menustate=1;
					else if(menustate==5)
						menustate=2;
				}
				break;
			case -3://���ULEFT
				if(page==0 || page==2 || page==4){
					if(menustate==0)
						menustate=3;
					else if(menustate==1)
						menustate=4;
					else if(menustate==2){
						if(page==4)
							menustate=4;
						else
							menustate=5;
					}
					else if(menustate==3)
						menustate=0;
					else if(menustate==4)
						menustate=1;
					else if(menustate==5)
						menustate=2;
				}
				break;
		}
		changeMenu();
	}
	
	//��ܥD����T
	public void showRoleStateInf(){
		setColor(false);
		g.drawString(String.valueOf(RoleDataValue.level), disWidth + 84, disHeight + 27, 0);
		showBar(RoleDataValue.exp,RoleDataValue.expNext,70,43,0);//EXP
		showBar(RoleDataValue.nowHP,RoleDataValue.maxHP,70,58,1);//HP
		showBar(RoleDataValue.getNowAtk(),RoleDataValue.getAtkMaxValue(),70,73,2);//ATK
		showBar(RoleDataValue.getNowDef(),RoleDataValue.getDefMaxValue(),70,88,3);//DEF
		g.drawString(RoleDataValue.fameStr, disWidth + 57, disHeight + 103, 0);
		g.drawString(String.valueOf(RoleDataValue.money), disWidth + 53, disHeight + 117, 0);
	}
	
	//���~�ϥΫ�,���ܼƶq
	public void itemUserChange(int num){
		String tmp = itemBuff[num][0];
		roleData.useItem(tmp,false);
	}
	
	//����˳Ʋ֭p�������O
	public int getAtk(int num){
		int value = 0;
		if(equipmentNum==8){
			value = Int(itemBuff[num][3])+Int(itemBuff[num][6]);
		}else if(equipmentNum==2){
			value = Int(itemBuff[num][3])+Int(itemBuff[num][7]);
		}else if(equipmentNum==5){
			value = Int(itemBuff[num][5]);
		}
		return value;
	}
	
	//����˳Ʋ֭p�����m
	public int getDef(int num){
		int value = 0;
		if(equipmentNum==8){
			value = Int(itemBuff[num][5]);
		}else if(equipmentNum==2){
			value = Int(itemBuff[num][5])+Int(itemBuff[num][6]);
		}else if(equipmentNum==5){
			value = Int(itemBuff[num][3])+Int(itemBuff[num][4]);
		}
		return value;
	}
	
	//���ܸ˳� storeIndex+itemSelect
	public void changeEquipment(){
		String tmp = itemBuff[storeIndex+itemSelect][0];
		if(tmp.indexOf("P")!=-1)
			RoleDataValue.pole = Int(itemBuff[storeIndex+itemSelect][0].substring(1,tmp.length()));
		else if(tmp.indexOf("J")!=-1)
			RoleDataValue.jewelry = Int(itemBuff[storeIndex+itemSelect][0].substring(1,tmp.length()));
		else if(tmp.indexOf("B")!=-1)
			RoleDataValue.bait = Int(itemBuff[storeIndex+itemSelect][0].substring(1,tmp.length()));
		else if(tmp.indexOf("G")!=-1){
			String tmp_ = itemBuff[storeIndex+itemSelect][8];
			if(tmp_.equals("1"))
				RoleDataValue.proHead = Int(itemBuff[storeIndex+itemSelect][0].substring(1,tmp.length()));
			else if(tmp_.equals("2"))
				RoleDataValue.proBody = Int(itemBuff[storeIndex+itemSelect][0].substring(1,tmp.length()));
			else if(tmp_.equals("3"))
				RoleDataValue.proWaist = Int(itemBuff[storeIndex+itemSelect][0].substring(1,tmp.length()));
			else if(tmp_.equals("4"))
				RoleDataValue.proFoot = Int(itemBuff[storeIndex+itemSelect][0].substring(1,tmp.length()));
			else if(tmp_.equals("5"))
				RoleDataValue.proHand = Int(itemBuff[storeIndex+itemSelect][0].substring(1,tmp.length()));
		}
		RoleDataValue.setAtkDefValue();//���s�]�watk�Pdef
		roleData.setNowComboValue();//�]�wcombo��
	}
	
	//�e�W�ثe�ҥH��ܪ�bar�b����W
	public void showBar(int now,int max,int x,int y,int color){
		//g.setColor(111,111,255);
		//g.fillRect(disWidth + x,disHeight + y,40,10);
		switch(color){
			case 0:
				g.setColor(235,205,63);
				break;
			case 1:
				g.setColor(106,255,106);
				break;
			case 2:
				g.setColor(224,47,180);
				break;
			case 3:
				g.setColor(50,112,216);
				break;
		}
		g.fillRect(disWidth + x,disHeight + y,countBar(now,max),10);
		g.setColor(0,0,0);
		setFont(0);
		if(color==0)
			g.drawString(String.valueOf(max-now),disWidth + x + 7,disHeight + y - 1,0);
		else
			g.drawString(String.valueOf(now),disWidth + x + 7,disHeight + y - 1,0);
		flushGraphics();
	}
	
	//��ܪ��~��Ʀb�e���W
	public void showItemInf(int index){
		g.drawImage(downInf,disWidth+0,disHeight+144,0);
		if(equipmentNum==0){
			g.drawString("�γ~:",disWidth + 8,disHeight + 150,0);
			g.drawString("�^������:"+Int(itemBuff[index][5])/2,disWidth + 8,disHeight + 188,0);
			splitText(itemBuff[index][3]);
			for(int i=0,k=0;i<textStr.length;i++,k+=14){
				if(textStr.length==1)
					g.drawString("�u"+textStr[i]+"�v",disWidth + 1,disHeight + 164+k,0);
				else if(i==0)
					g.drawString("�u"+textStr[i],disWidth + 1,disHeight + 164+k,0);
				else if(i==textStr.length-1)
					g.drawString("  "+textStr[i]+"�v",disWidth + 1,disHeight + 164+k,0);
				else
					g.drawString(textStr[i],disWidth + 1,disHeight + 164+k,0);
			}
		}else if(equipmentNum==8 || equipmentNum==2 || equipmentNum==5){
			g.drawString("����(ATK)+"+String.valueOf(getAtk(index)),disWidth + 8,disHeight + 150,0);
			g.drawString("���m(DEF)+"+String.valueOf(getDef(index)),disWidth + 8,disHeight + 164,0);
		}else if(equipmentNum==9){
			g.drawString("�f�t����:",disWidth + 8,disHeight + 150,0);
			g.drawString("�u"+itemBuff[index][3]+"�v",disWidth + 1,disHeight + 164,0);
		}else if(equipmentNum==10){
			g.drawString("�W�[HP��:",disWidth + 8,disHeight + 150,0);
			g.drawString("�u"+itemBuff[index][3]+"�v",disWidth + 1,disHeight + 164,0);
		}else if(equipmentNum==6){
			g.drawString("�γ~:",disWidth + 8,disHeight + 150,0);
			g.drawString("�u"+itemBuff[index][3]+"�v",disWidth + 1,disHeight + 164,0);
		}else if(equipmentNum==11){
			g.drawString("�ƥ󴣥�:",disWidth + 8,disHeight + 150,0);
			splitText(itemBuff[index][3]);
			for(int i=0,k=0;i<textStr.length;i++,k+=14){
				if(textStr.length==1)
					g.drawString("�u"+textStr[i]+"�v",disWidth + 1,disHeight + 164+k,0);
				else if(i==0)
					g.drawString("�u"+textStr[i],disWidth + 1,disHeight + 164+k,0);
				else if(i==textStr.length-1)
					g.drawString("  "+textStr[i]+"�v",disWidth + 1,disHeight + 164+k,0);
				else
					g.drawString(textStr[i],disWidth + 1,disHeight + 164+k,0);
			}
		}
		g.drawString("    "+roleData.getItemAmount(itemBuff[index][0]),disWidth + 123,disHeight + 57,0);
	}
	
	private static String textStr[] = null;
	public void splitText(String str) {
		textStr = null;
		try{
			int line = str.length()/14;
			if(str.length()%14>0)
				line++;
			textStr = new String[line];
			
			if(line>1){
				textStr[0] = str.substring(0,14);
				textStr[1] = str.substring(14,str.length());
			}else
				textStr[0] = str.substring(0,str.length());
		}catch(Exception e){e.printStackTrace();}
	}
	
	//�p��ثebar��~~
	public int countBar(int now,int max){
		int poHp = 0;
		poHp = ((now*10)/10*400)/(max*10);
		if(poHp<1)
			poHp = 1;
		return poHp;
	}
	
	//�r���]�w
	public void setFont(int size) {
		Font font = Font.getFont(Font.FACE_SYSTEM, 0, size);
		g.setFont(font);
	}
	
	//�C��]�w
	public void setColor(boolean value){
		if(value)
			g.setColor(255, 0, 0);
		else
			g.setColor(69,118,181);
	}
	
	//�ഫ�����
	public int Int(String data){
		return Integer.parseInt(data);
	}
	
	//	�إ߹Ϥ�
	public static Image createImage(String pathImg) {
		img = null;
		try {
			img = Image.createImage(pathImg);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return img;
	}
	
	public void exit(){}
}