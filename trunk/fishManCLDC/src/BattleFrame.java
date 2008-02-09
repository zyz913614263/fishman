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
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.*;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import javax.microedition.lcdui.game.*;

public class BattleFrame extends GameCanvas implements Runnable{

	private static int disWidth = 0;//LayManager��ڦ�m

	private static int disHeight = 0;//LayManager��ڦ�m
	
	private static int menustate = 0;//�ثe����쪺�ﶵ
	
	private static int itemSelect = 0;
	private static int storeIndex=0;//���~����
	private static int itemTotal = 0;
	private static int itemMenuSelect = 0;

	private static int menuNum = 4;//�@���X�ӿﶵ

	private static String[] menuStr = { "�@�����", " ������ ", "���~�ϥ�", "  �k�]"};
	private static String[] itemUseStr = {"�ϥ�","����","���}"};
	private static String[] itemNameArray = {"Item","","Jewelry","","Battle","Protect","	Event",
			"Monster","Pole","Bait","Tonic","Talk",""};
	private static int dataFieldNum[] = {6,4,11,1,4,10,4,11,12,6,7,4,9};//�C�@��Ʈw����
	private static String itemBuff[][] = new String[30][];
	
	private static DataBaseReader db = new DataBaseReader();

	private Graphics g = null;
	//private static Image monsterImg = null;
	//private static Image battleFrame = null;
	private static Image battleMenu = createImage("/image/gameInterface/BattleMenu.png");//�԰����
	private static Image BattleFrameDown = createImage("/image/gameInterface/BattleFrameDown.png");//
	private static Image battleConver = createImage("/image/gameInterface/BattleFrameUp.png");//��ܤ��
	private static Image comboDis = createImage("/image/gameInterface/combo.png");//COMBO����ܤ��
	private static Image battleWin = createImage("/image/gameInterface/BattleWin.png");//�԰��ӧQ���
	private static Image battleItem = createImage("/image/gameInterface/BattleItem.png");//�԰��ϥΪ��~
	private static Image battleItemMenu = createImage("/image/gameInterface/buyMenu.png");//�԰��ϥΪ��~���
	private static Image battlePeople = createImage("/image/gameInterface/battlePeople.png");//�԰����D��
	private static Image atkImg = createImage("/image/gameInterface/atk.png");//�@������Ϥ�
	private static Image monsterDied = createImage("/image/gameInterface/monsterDied.png");//�Ǫ����`
	private static Image roleDied = createImage("/image/gameInterface/gameover.png");//�Ǫ����`
	private static Image comboImg = null;//combo��
	private static Image monsterAtk = createImage("/image/gameInterface/monsterAtk.png");//�Ǫ��Q����
	private static Image titleImg = null;
	private static Image img2[] = null;
	
	private static boolean drawItemMenu = false;//�O�_���n�e���~
	private static boolean storeEvent = false;//�O�_���ϥΪ��~
	private static boolean itemEvent = true;//true�ɪ����ܪ��~�b�M�椤
	private static boolean itemBuy = false;//��false����٨S�T�w�n�ϥ�,true��ܽT�w�n�ϥ�
	private static boolean atbFill = false;//ATB�O�_���F
	private static boolean xpbFill = false;//XPB�O�_���F
	public static boolean battleFinish = false;//�԰��O�_����
	public static boolean battleVictory = false;//�԰��ӧQ�Υ���
	private static boolean useCombo = false;
	private static boolean init = false;
	private static int storeNum = 0;//
	private static int atbValue = 0;//ATB�W�[����
	private static int xpbValue = 0;//XPB�W�[����
	private static int atbSpeed = 100;//ATB���t��
	
	private static MonsterDataValue monsterData = new MonsterDataValue();
	private static Timer atbTimer = new Timer();
	private static Timer xpbTimer = new Timer();
	private static Timer comboTimer = new Timer();
	
	private static int page = 0;//����
	
	private static Random atkRandom = null;
	private static String comboBuff = "";
	
	private static RoleDataValue roleData  = null;
	
	private static Thread monsterThread = null;
	
	private static mapCanvars mapca = null;
	
	private static Image img = null;
	
	private static String itemAmount[] = new String[99];
	

	public BattleFrame(/*int map, */RoleDataValue roleData_,mapCanvars mapca) {
		super(false);
		setFullScreenMode(true);
		atkRandom = new Random();
		BattleFrame.mapca = mapca;
		roleData = roleData_;
		monsterThread = new Thread(this);
		monsterThread.start();
	}
	
	public void battleStart(int map,String monster){
		GameMain.music.musicPause();
		GameMain.music.battleMusicStart();
		disWidth = (getWidth() - 176) / 2;//����ù��������I
		disHeight = (getHeight() - 208) / 2;//����ù��������I
		try{
			if(monster.equals(""))
				monsterData.createMonster(map);
			else
				monsterData.createMonster(monster);
			
			page=0;
			itemSelect = 0;
			storeIndex=0;//���~����
			itemTotal = 0;
			itemMenuSelect = 0;
			init = false;
			battleFinish = true;
			drawDisplay();
			changPage();
		}catch(Exception e){e.printStackTrace();}
	}

	//��Ϥ��e�W
	public void drawDisplay() {
		g = getGraphics();
		if(page==0 ||page==1){//�԰��}�l,�D��atb������
			g.drawImage(MonsterDataValue.battleBackgroundImg,disWidth+0,disHeight+48,0);
			if(!init){
				g.drawImage(battleConver,disWidth+0,disHeight+0,0);
				monsterConvs();
				init=true;
			}
			if(atbFill==false)
				g.drawImage(BattleFrameDown,disWidth+0,disHeight+144,0);
			g.drawImage(MonsterDataValue.monsterImg,disWidth+105,disHeight+70,0);
			g.drawImage(battlePeople,disWidth+12,disHeight+90,0);
			if(RoleDataValue.pole!=0)
				g.drawImage(RoleDataValue.poleImg,disWidth+70,disHeight+168,0);
			if(RoleDataValue.bait!=0){
				g.drawImage(RoleDataValue.baitImg,disWidth+134,disHeight+168,0);
				g.drawString(String.valueOf(roleData.getItemAmount("B"+RoleDataValue.bait)),disWidth+111,disHeight+187,0);
			}
			if(page==1)//��atb����,�i�H��ʮ�
				g.drawImage(battleMenu,disWidth+65,disHeight+150,0);
			showHp();
			showMonsterHp();
		}
		/*if(page==1){//��atb����,�i�H��ʮ�
			g.drawImage(battleMenu,disWidth+65,disHeight+150,0);
		}*/
		if(page==2){//��combo�ޥi�H�ή�
			g.drawImage(comboDis,disWidth+52,disHeight+72,0);
			g.drawString(RoleDataValue.nowComboName,disWidth+57,disHeight+80,0);
		}
		if(page==3){//��n�ϥΪ��~��
			g.drawImage(battleItem,disWidth+3,disHeight+0,0);
			g.drawImage(battleItemMenu,disWidth+121,disHeight+77,0);
			titleImg = createImage("/image/gameInterface/text4.png");
			g.drawImage(titleImg,disWidth+117,disHeight+8,0);
			titleImg = createImage("/image/gameInterface/text3.png");
			g.drawImage(titleImg,disWidth+117,disHeight+42,0);
		}
		if(!battleFinish & battleVictory){//�԰��ӧQ
			g.drawImage(battleWin,disWidth+0,disHeight+48,0);
			showWinInf();
		}else if(!battleFinish & !battleVictory){
			battleLose();
		}
		
		g.setColor(255,255,0);
		g.fillRect(disWidth + 7,disHeight + 170,xpbValue,10);
		flushGraphics();
		changeMenu();
	}
	
	//�k�]
	public void RunAway(){
		GameMain.music.msuicSound(7);
		GameMain.music.battleMusicStop();
		GameMain.music.musicRun();
		atbTimer.cancel();
		atbTimer=null;
		atbTimer = new Timer();
		atbValue=0;
		xpbTimer.cancel();
		xpbTimer=null;
		xpbTimer = new Timer();
		atbFill = false;
		xpbFill = false;
		xpbValue=0;
		comboTimer.cancel();
		comboTimer = null;
		storeEvent = false;
		comboTimer = new Timer();
		useCombo = false;
		battleFinish = false;
		battleVictory = false;
		mapca.threadStart();
	}
	
	//��ܾ԰��ӧQ�᪺��T
	public void showWinInf(){
		System.out.println("win");
		battleVictory = true;
		String[][] item = MonsterDataValue.item;
		setFont(0);
		setColor(false);
		if(item.length==0){
			g.drawString("�L�������~",disWidth + 68,disHeight + 80 ,0);
			RoleDataValue.money+=MonsterDataValue.money;
		}else{
			GameMain.music.msuicSound(5);
			for(int i=0,k=0;i<item.length;i++,k+=15){
				g.drawString(item[i][2],disWidth + 68,disHeight + 80 + k,0);
				roleData.addItem(item[i][0],false,1);
			}
			RoleDataValue.money+=MonsterDataValue.money;
		}
		setFont(0);
		g.drawString("�������o",disWidth + 11,disHeight + 80,0);
		g.drawString("$"+String.valueOf(MonsterDataValue.money),disWidth + 11,disHeight + 94,0);
		
		g.drawString("�g����o",disWidth + 11,disHeight + 108,0);
		g.drawString(String.valueOf(MonsterDataValue.exp),disWidth + 11,disHeight + 122,0);
		
		if(RoleDataValue.exp+MonsterDataValue.exp>=RoleDataValue.expNext){//�ɯ�
			GameMain.music.msuicSound(4);
			RoleDataValue.exp = (RoleDataValue.exp+MonsterDataValue.exp) - RoleDataValue.expNext;
			RoleDataValue.level++;
			RoleDataValue.roleUpLevel();
			g.drawImage(battleConver,disWidth+0,disHeight+0,0);
			g.drawString("[�ɯ�]",disWidth+5,disHeight+4,0);
			g.drawString(" Level " + (RoleDataValue.level-1) + "->Level " + RoleDataValue.level,disWidth+5,disHeight+17,0);
		}else
			RoleDataValue.exp+=MonsterDataValue.exp;
		
		RoleDataValue.killMonster++;
		checkFame();
		flushGraphics();
		ConnectionServer.updateGameInf();
		try{
			Thread.sleep(3000);
		}catch(Exception e){}
		
		GameMain.music.battleMusicStop();
		GameMain.music.musicRun();
		mapca.threadStart();
	}
	
	//�԰�����
	public void battleLose(){
		GameMain.music.battleMusicStop();
		mapca.jumpWord(0,299,267);
		mapca.threadStart();
	}
	
	//�P�_�W��O�_�W��
	public void checkFame(){
		int value = RoleDataValue.killMonster;
		if(value==100 || value==250 || value==500 || value==1000 || value==2000 
				|| value==3000 || value==5000 || value==8000 || value==10000 || value==14000 || value==20000 
				|| value==25000 || value==32000 || value==50000){
			RoleDataValue.fame++;
			roleData.setFame(RoleDataValue.fame);
		}
	}

	//�إ߹Ϥ�
	public static Image createImage(String pathImg) {
		img = null;
		try {
			img = Image.createImage(pathImg);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return img;
	}

	//���ܭ����ɡA��Ϥ�
	public void changPage() {
		switch (page) {
		case 0://�԰��}�l��
			if(battleFinish){
				if(atbFill==false)
					atbTimer.schedule(new AtbTimerTask(),0,atbSpeed);
				if(xpbFill==false)
					xpbTimer.schedule(new XpbTimerTask(),0,500);
			}
			break;
		case 1://ATB��,�X�{�԰����
			menustate = 0;
			page=1;
			break;
		case 2://��combo�ޥi�H�ή�
			comboTimer.schedule(new ComboTimerTask(),RoleDataValue.comboValue*700,10000);
			break;
		case 3://�ϥΪ��~
			if(!drawItemMenu){
				itemSelect=0;
				storeIndex=0;
				int n=0;
				String tmp[] = null;
				storeNum = 10;
				//�j�M�D�����~
				roleData.setHashEnume();
				for(;RoleDataValue.enu.hasMoreElements();){
					tmp = (String[])RoleDataValue.enu.nextElement();
					//�p�G�s���j��n�j�M��Ʈw���ƶq�h���j�M
					if(tmp[0].indexOf("T")==-1)
						continue;
					//System.out.println("Find:"+tmp[0]);
					itemBuff[n] = db.readDBRecord(storeNum,
							Integer.parseInt( tmp[0].substring(1,tmp[0].length()) ));
					itemAmount[n] = tmp[1];
					n++;
				}
				itemSelect=0;
				storeIndex=0;
				itemTotal = n;//�j�M��ŦX�����~�ƶq
				if(itemTotal==0){
					page=1;
					changPage();
					storeEvent = false;
				}else{
					img2 = new Image[itemTotal];
					for(int i=0;i<itemTotal;i++){
						img2[i] = createImage("/image/" + itemNameArray[storeNum] +"/"+ itemBuff[i][1] + ".png");
					}
					//System.out.println("Total:"+itemTotal+"|"+page);
					drawItemMenu = true;
				}
			}
			break;
		case 4:
			break;
		}
		drawDisplay();
	}

	//������
	private void changeMenu() {
		if(page==0){//�԰��}�l
			
		}
		else if(page==1){//�԰����
			setFont(0);
			switch (menustate) {
				case 0:
					//Select----------------------------
					setColor(true);
					g.drawString(menuStr[0], disWidth + 72, disHeight + 159, 0);
					//----------------------------------
					setColor(false);
					g.drawString(menuStr[1], disWidth + 124, disHeight + 159, 0);
					g.drawString(menuStr[2], disWidth + 72, disHeight + 180, 0);
					g.drawString(menuStr[3], disWidth + 124, disHeight + 180, 0);
					break;
				case 1:
					setColor(false);
					g.drawString(menuStr[0], disWidth + 72, disHeight + 159, 0);
					g.drawString(menuStr[1], disWidth + 124, disHeight + 159, 0);
					//Select----------------------------
					setColor(true);
					g.drawString(menuStr[2], disWidth + 72, disHeight + 180, 0);
					//----------------------------------
					g.setColor(69, 118, 181);
					g.drawString(menuStr[3], disWidth + 124, disHeight + 180, 0);
					break;
				case 2:
					setColor(false);
					g.drawString(menuStr[0], disWidth + 72, disHeight + 159, 0);
					//Select----------------------------
					setColor(true);
					g.drawString(menuStr[1], disWidth + 124, disHeight + 159, 0);
					//----------------------------------
					setColor(false);
					g.drawString(menuStr[2], disWidth + 72, disHeight + 180, 0);
					g.drawString(menuStr[3], disWidth + 124, disHeight + 180, 0);
					break;
				case 3:
					setColor(false);
					g.drawString(menuStr[0], disWidth + 72, disHeight + 159, 0);
					g.drawString(menuStr[1], disWidth + 124, disHeight + 159, 0);
					g.drawString(menuStr[2], disWidth + 72, disHeight + 180, 0);
					//Select----------------------------
					setColor(true);
					g.drawString(menuStr[3], disWidth + 124, disHeight + 180, 0);
					//----------------------------------
					break;
				}
		}
		else if(page==3){//���~�ϥ�
			if(drawItemMenu){//�e���~��T�bitemMenu
				setFont(0);
				g.drawImage(battleItem,disWidth+3,disHeight+0,0);
				titleImg = createImage("/image/gameInterface/text4.png");
				g.drawImage(titleImg,disWidth+117,disHeight+8,0);
				titleImg = createImage("/image/gameInterface/text3.png");
				g.drawImage(titleImg,disWidth+117,disHeight+42,0);
				switch (itemSelect) {
					case 0:
						//Select----------------------------
						setColor(true);
						g.drawString(itemBuff[0+storeIndex][2], disWidth + 50, disHeight + 15, 0);
						g.drawImage(img2[0+storeIndex],disWidth+10,disHeight+6,0);
						g.drawString("HP+"+itemBuff[0+storeIndex][3],disWidth + 123,disHeight + 28,0);
						g.drawString("    "+itemAmount[0+storeIndex],disWidth + 123,disHeight + 57,0);
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
							g.drawString("HP+"+itemBuff[1+storeIndex][3],disWidth + 123,disHeight + 28,0);
							g.drawString("    "+itemAmount[1+storeIndex],disWidth + 123,disHeight + 57,0);
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
							g.drawString("HP+"+itemBuff[2+storeIndex][3],disWidth + 123,disHeight + 28,0);
							g.drawString("    "+itemAmount[2+storeIndex],disWidth + 123,disHeight + 57,0);
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
							g.drawString("HP+"+itemBuff[3+storeIndex][3],disWidth + 123,disHeight + 28,0);
							g.drawString("    "+itemAmount[3+storeIndex],disWidth + 123,disHeight + 57,0);
						}
						//----------------------------------
						break;
					}
				}
				if(storeEvent==true && itemBuy==true){
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
		}
		flushGraphics();
	}

	//�r���]�w
	public void setFont(int size) {
		Font font = Font.getFont(Font.FACE_SYSTEM, 0, size);
		g.setFont(font);
	}
	//�C��]�w
	public void setColor(boolean value){
		if(value)
			g.setColor(0, 0, 0);
		else
			g.setColor(69, 118, 181);
	}
	
	//�]�w�Ǫ����
	public void monsterConvs(){
		//�Ǫ����
		String talkTmp[] = monsterData.getMonsterCon();
		g.drawImage(battleConver,disWidth+0,disHeight+0,0);
		g.setColor(0,0,128);
		g.drawString(MonsterDataValue.name+" ��:",disWidth + 5,disHeight + 4 ,0);
		for(int i=0,k=13;i<talkTmp.length;i++,k+=13)
			g.drawString(talkTmp[i],disWidth + 5,disHeight + 4 + k ,0);
		//System.out.println("TalkL"+talkTmp.length);
	}
	
	//�Ǫ������e��
	public void monsterAttackFrame(){
		monsterConvs();//��ܩǪ����
		//����
		for(int i=0;i<3;i++){
			g.setColor(247,72,89);
			g.fillRect(disWidth+0,disHeight+48,176,96);
			flushGraphics();
			try{
				Thread.sleep(50);
			}catch(Exception e){}
			g.drawImage(MonsterDataValue.battleBackgroundImg,disWidth+0,disHeight+48,0);
			g.drawImage(battlePeople,disWidth+12,disHeight+90,0);
			g.drawImage(MonsterDataValue.monsterImg,disWidth+105,disHeight+70,0);
			flushGraphics();
		}
		showMonsterHp();
		//�Ǫ������ˮ`��
		//�D���ˮ`�Ȭ��Ǫ�atk*(4~8)/10-�D�����m
		int atbRanValue = (MonsterDataValue.atk*(((Math.abs(atkRandom.nextInt()))%5)+4))/10;
		if(atbRanValue-RoleDataValue.getNowDef()>=0){
			RoleDataValue.nowHP -= atbRanValue-RoleDataValue.getNowDef();
			System.out.println("Role loss:"+( atbRanValue-RoleDataValue.getNowDef() ));
		}
		else{
			RoleDataValue.nowHP -= (MonsterDataValue.atk/10+10);
			System.out.println("Role loss-1:"+(MonsterDataValue.atk/10+10) );
		}
		if(RoleDataValue.nowHP<=0){
			RoleDataValue.nowHP = 0;
			showHp();
			battleFinish = false;
			battleVictory = false;
			try{
				Thread.sleep(600);
			}catch(Exception e){}
			
			roleDied();
			//page=4;
			//�D�����`
		}else{
			showHp();
		}
	}
	
	private LayerManager lm = new LayerManager();
	private Sprite sprite = null; 
	//�D�������e��
	public void roleAttackFrame(){
		System.out.println("atk");
		GameMain.music.msuicSound(2);
		g.drawImage(MonsterDataValue.battleBackgroundImg,disWidth+0,disHeight+48,0);
		g.drawImage(battlePeople,disWidth+12,disHeight+90,0);
		g.drawImage(MonsterDataValue.monsterImg,disWidth+105,disHeight+70,0);
		flushGraphics();
		//LayerManager lm = new LayerManager();
		sprite = new Sprite(atkImg,98,51);
		lm.append(sprite);
		for(int i=0;i<sprite.getRawFrameCount();i++){//�@�������
			lm.paint(g,disWidth+50,disHeight+70);
			flushGraphics();
			try{
				Thread.sleep(50);
			}catch(Exception e){}
			sprite.nextFrame();
		}
		lm.remove(sprite);
		//atkSprite = null;
		sprite = new Sprite(monsterAtk,176,96);
		lm.append(sprite);
		for(int i=0;i<sprite.getRawFrameCount();i++){//�Ǫ��Q����
			lm.paint(g,disWidth+0,disHeight+48);
			flushGraphics();
			try{
				Thread.sleep(50);
			}catch(Exception e){}
			sprite.nextFrame();
		}
		GameMain.music.msuicSound(3);
		//lm = null;
		lm.remove(sprite);
		//�Ǫ��ˮ`�Ȭ��D��atk*(4~8)/10-�Ǫ����m--------
		int atbRanValue = (RoleDataValue.getNowAtk()*(((Math.abs(atkRandom.nextInt()))%5)+4))/10;
		if((atbRanValue-MonsterDataValue.def>=0)){
			MonsterDataValue.nowHP -= atbRanValue-MonsterDataValue.def;
			System.out.println("Monster loss:" + (atbRanValue-MonsterDataValue.def) );
		}
		else{
			MonsterDataValue.nowHP -= (RoleDataValue.getNowAtk()/10+10);
			System.out.println("Monster loss-1:" + (RoleDataValue.getNowAtk()/10+10) );
		}
		if(MonsterDataValue.nowHP<=0){
			MonsterDataValue.nowHP=0;
			showMonsterHp();
			g.drawImage(MonsterDataValue.battleBackgroundImg,disWidth+0,disHeight+48,0);
			g.drawImage(MonsterDataValue.monsterImg,disWidth+105,disHeight+70,0);
			g.drawImage(battlePeople,disWidth+12,disHeight+90,0);
			flushGraphics();
			battleFinish = false;
			try{
				Thread.sleep(600);
			}catch(Exception e){}
			GameMain.music.msuicSound(6);
			monsterDied();
			battleVictory = true;
			page=4;
			//�Ǫ����`
		}else{
			showMonsterHp();
		}
		//-------------------------------------
		//�p�Gcombo�޿�J���~atb�Pxpb���s�}�l
		if(useCombo){
			atbTimer.cancel();
			atbTimer=null;
			atbTimer = new Timer();
			atbValue=0;
			xpbTimer.cancel();
			xpbTimer=null;
			xpbTimer = new Timer();
			atbFill = false;
			xpbFill = false;
			xpbValue=0;
			comboTimer.cancel();
			comboTimer = null;
			storeEvent = false;
			comboTimer = new Timer();
			useCombo = false;
			comboBuff = "";
			page=0;
			changPage();
		}
	}
	
	//�Ǫ����`�e��
	public void monsterDied(){
		sprite = new Sprite(monsterDied,176,96);
		lm.append(sprite);
		for(int i=0;i<sprite.getRawFrameCount();i++){
			lm.paint(g,disWidth+0,disHeight+48);
			flushGraphics();
			try{
				Thread.sleep(150);
			}catch(Exception e){}
			sprite.nextFrame();
		}
		lm.remove(sprite);
	}
	
	//�D�����`�e��
	public void roleDied(){
		GameMain.music.battleMusicStart();
		sprite = new Sprite(roleDied,176,96);
		lm.append(sprite);
		for(int i=0;i<sprite.getRawFrameCount();i++){
			lm.paint(g,disWidth+0,disHeight+48);
			flushGraphics();
			try{
				Thread.sleep(250);
			}catch(Exception e){}
			sprite.nextFrame();
		}
		
		atbTimer.cancel();
		atbTimer=null;
		atbTimer = new Timer();
		atbValue=0;
		xpbTimer.cancel();
		xpbTimer=null;
		xpbTimer = new Timer();
		atbFill = false;
		xpbFill = false;
		xpbValue=0;
		
		try{
			Thread.sleep(600);
		}catch(Exception e){}
		
		lm.remove(sprite);
		battleLose();
	}
	
	//��ܩǪ���b����e���W
	public void showMonsterHp(){
		g.setColor(234,234,0);
		g.drawRect(disWidth + 167,disHeight + 49,6,41);
		g.setColor(19,102,215);
		g.fillRect(disWidth + 168,disHeight + 50+(40-countMonsterHp()),5,countMonsterHp());
		flushGraphics();
	}
	
	//�p��Ǫ���q
	public int countMonsterHp(){
		int poHp = 0;
		int maxHp = MonsterDataValue.maxHP;
		int nowHp = MonsterDataValue.nowHP;
		poHp = ((nowHp*10)/10*400)/(maxHp*10);
		//System.out.println("HP"+poHp);
		return poHp;
	}
	
	//combo�ާ���
	public void comboAttack(){
		g.drawImage(MonsterDataValue.battleBackgroundImg,disWidth+0,disHeight+48,0);
		g.drawImage(battlePeople,disWidth+12,disHeight+90,0);
		g.drawImage(MonsterDataValue.monsterImg,disWidth+105,disHeight+70,0);
		flushGraphics();
		//LayerManager lm = new LayerManager();
		Sprite combo = new Sprite(comboImg,176,96);
		lm.append(combo);
		for(int i=0;i<combo.getRawFrameCount();i++){
			lm.paint(g,disWidth+0,disHeight+48);
			flushGraphics();
			try{
				Thread.sleep(100);
			}catch(Exception e){}
			combo.nextFrame();
		}
		lm.remove(combo);
		//�Ǫ��ˮ`�Ȭ��D��atk*(8~16)/10*�q��-�Ǫ����m
		int atbRanValue = (RoleDataValue.getNowAtk()*(((Math.abs(atkRandom.nextInt()))%8)+8))/10;
		System.out.println("atk:"+atbRanValue);
		if((atbRanValue-MonsterDataValue.def>=0)){
			MonsterDataValue.nowHP -= atbRanValue*RoleDataValue.comboValue-MonsterDataValue.def;
			System.out.println("Monster loss:" + (atbRanValue*RoleDataValue.comboValue-MonsterDataValue.def) );
		}
		else{
			MonsterDataValue.nowHP -= RoleDataValue.getNowAtk()*RoleDataValue.comboValue/10;
			System.out.println("Monster loss:" + (RoleDataValue.getNowAtk()*RoleDataValue.comboValue/10) );
		}
		if(MonsterDataValue.nowHP<=0){
			MonsterDataValue.nowHP=0;
			showMonsterHp();
			g.drawImage(MonsterDataValue.battleBackgroundImg,disWidth+0,disHeight+48,0);
			g.drawImage(MonsterDataValue.monsterImg,disWidth+105,disHeight+70,0);
			g.drawImage(battlePeople,disWidth+12,disHeight+90,0);
			flushGraphics();
			battleFinish = false;
			try{
				Thread.sleep(600);
			}catch(Exception e){}
			monsterDied();
			battleVictory = true;
			//page=4;
			//�Ǫ����`
		}else{
			showMonsterHp();
		}
		//lm = null;
		atbTimer.cancel();
		atbTimer=null;
		atbTimer = new Timer();
		atbValue=0;
		xpbTimer.cancel();
		xpbTimer=null;
		xpbTimer = new Timer();
		atbFill = false;
		xpbFill = false;
		xpbValue=0;
		comboTimer.cancel();
		comboTimer = null;
		storeEvent = false;
		comboTimer = new Timer();
		useCombo = false;
		comboBuff = "";
		page=0;
		changPage();
	}
	
	//�e�W�ثe�D��hp�Ȧb����W
	public void showHp(){
		g.setColor(111,111,255);
		g.fillRect(disWidth + 7,disHeight + 185,40,10);
		g.setColor(106,255,106);
		g.fillRect(disWidth + 7,disHeight + 185,countHp(),10);
		g.setColor(0,0,0);
		setFont(0);
		g.drawString(String.valueOf(RoleDataValue.nowHP),disWidth + 14,disHeight + 184,0);
		flushGraphics();
	}
	
	//�p��ثe�D��hp��~~
	public int countHp(){
		int poHp = 0;
		int maxHp = RoleDataValue.maxHP;
		int nowHp = RoleDataValue.nowHP;
		poHp = ((nowHp*10)/10*400)/(maxHp*10);
		//System.out.println("HP"+poHp);
		return poHp;
	}
	
	//��ܿ�J��combo��
	public void drawComboValue(){
		if(RoleDataValue.comboValue*2>comboBuff.length()){
			if(comboBuff.length()<RoleDataValue.comboValue*2-1)
				comboBuff+="+";
			g.setColor(0,0,0);
			Font font = Font.getFont(Font.FACE_SYSTEM, 0, 0);
			g.setFont(font);
			g.drawString(comboBuff,disWidth + 74 - RoleDataValue.comboValue*5+5,disHeight + 100,0);
			g.drawString(comboBuff,disWidth + 75 - RoleDataValue.comboValue*5+5,disHeight + 100,0);
			g.drawString(comboBuff,disWidth + 76 - RoleDataValue.comboValue*5+5,disHeight + 102,0);
			g.drawString(comboBuff,disWidth + 77 - RoleDataValue.comboValue*5+5,disHeight + 102,0);
			g.setColor(255,255,255);
			font = Font.getFont(Font.FACE_SYSTEM, 0, 0);
			g.setFont(font);
			g.drawString(comboBuff,disWidth + 76 - RoleDataValue.comboValue*5+5,disHeight + 101,0);
			flushGraphics();
		}
	}

	//��L�ƥ�
	protected void keyPressed(int keyCode) {
		if(useCombo){
			if(comboBuff.length()<RoleDataValue.comboValue*2-1){
				switch(keyCode){
					case 49:
						comboBuff+=(char)keyCode;
						drawComboValue();
						break;
					case 50:
						comboBuff+=(char)keyCode;
						drawComboValue();
						break;
					case 51:
						comboBuff+=(char)keyCode;
						drawComboValue();
						break;
				}
			}
		}
		if(atbFill){
			switch( keyCode ){
				case -5://���UFIRE
					switch (menustate) {//�P�_�e������b���ӭ���
						case 0://�@�����
							try{
								roleAttackFrame();
							}catch(Exception e){e.printStackTrace();}
							atbTimer.cancel();
							atbTimer=null;
							atbTimer = new Timer();
							atbFill = false;
							atbValue=0;
							page=0;
							changPage();
							break;
						case 1://�ϥΪ��~
							if(itemEvent==true && drawItemMenu==false){//���~�M���Ϥ�
								page=3;
								if(storeEvent=true && page==3){
					        		itemEvent = false;
					        		itemBuy = false;
								}
								changPage();
								System.out.println("Item");
							}else{//���ܦn���~��
								if(page==3){//��ܨϥΦ����~
									itemEvent = false;
									itemBuy = !itemBuy;
									if(itemBuy==false){//�T�w�n�ϥΦ����~
										itemEvent=true;
										if(itemMenuSelect==0){
											page=3;
											changPage();
											RoleDataValue.nowHP+=Integer.parseInt(itemBuff[storeIndex+itemSelect][3]);
											if(RoleDataValue.nowHP>RoleDataValue.maxHP)//�庡
												RoleDataValue.nowHP = RoleDataValue.maxHP;
											showHp();
											//���~�ϥ�
											itemUserChange(storeIndex+itemSelect);
											drawItemMenu = false;
											changPage();
											System.out.println("use:"+(storeIndex+itemSelect+1));
										}else if(itemMenuSelect==1){
											page=3;
											changPage();
											System.out.println("cancel");
										}else{//���}
											monsterConvs();
											itemEvent=true;
											drawItemMenu=false;
											storeEvent=false;
											page=1;
											changPage();
										}
									}
									itemMenuSelect=0;
								}
							}
							break;
						case 2://combo��
							if(xpbFill==true){
								//roleData.setNowComboValue();//�]�wcombo�ު�l��
								if(RoleDataValue.nowCombo!=0){//�p�G����䴩combo��
									if(RoleDataValue.comboState==true){
										storeEvent = true;
										try{
											Thread.sleep(100);
										}catch(Exception e){}
										useCombo = true;
										page=2;
										changPage();
									}
								}
							}
							break;
						case 3://�k�],���~������}
							if(page==3){
								drawItemMenu = false;
								page=1;
								changPage();
							}else
								RunAway();
							break;
					}
					break;
				case -1://���UUP
					if(itemBuy==true && (page==3)){//�T�{�O�_�ϥΪ��~��
						if(itemMenuSelect <=0 )
							itemMenuSelect=3;
						itemMenuSelect--;
					}
					else if(itemBuy==false && (page==3)){//��ܭn�ϥΪ����~
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
					else{
						if(menustate <=0 )
							menustate=menuNum;
						menustate--;
					}
					
					break;
				case -2://���UDOWN
					if(itemBuy==true && (page==3)){//�T�{�O�_�ϥΪ��~��
						if(itemMenuSelect >=2 )
							itemMenuSelect=-1;
						itemMenuSelect++;
					}
					else if(itemBuy==false && (page==3)){//��ܭn�ϥΪ����~
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
					}else{
						if(menustate >=menuNum-1 )
							menustate=-1;
						menustate++;
					}
					break;
				case -4://���URIGHT
					if(menustate==0)
						menustate=2;
					else if(menustate==1)
						menustate=3;
					else if(menustate==2)
						menustate=0;
					else if(menustate==3)
						menustate=1;
					break;
				case -3://���ULEFT
					if(menustate==0)
						menustate=2;
					else if(menustate==1)
						menustate=3;
					else if(menustate==2)
						menustate=0;
					else if(menustate==3)
						menustate=1;
					break;
			}
			changeMenu();
		}
	}
	
	//���~�ϥΫ�,���ܼƶq
	public void itemUserChange(int num){
		String tmp = itemBuff[num][0];
		roleData.useItem(tmp,false);
	}
		
	public void run(){
		while(true){
			try{
				Thread.sleep(MonsterDataValue.atb);
			}catch(Exception e){}
			if(!battleFinish) continue;
			if(!storeEvent){
				GameMain.music.soundStart(1);
				monsterAttackFrame();
			}
		}
	}
	
	//combo�ޭp�ƾ�
	class ComboTimerTask extends TimerTask{
		public void run(){
			try{
				if(comboBuff.equals(RoleDataValue.nowComboMethod)){
					comboImg = createImage("/image/combo/C"+RoleDataValue.nowCombo+".png");
					roleData.useItem("B"+RoleDataValue.bait,true);//������
					GameMain.music.soundStart(0);
					comboAttack();
				}
				else{
					roleData.useItem("B"+RoleDataValue.bait,true);//������
					roleAttackFrame();
				}
			}catch(Exception e){e.printStackTrace();}
		}
	}
	//ATB�p�ƾ�
	class AtbTimerTask extends TimerTask{
		public void run(){
			if(atbFill==false){//ATB������
				g.setColor(255,0,0);
				g.fillRect(disWidth + 7,disHeight + 155,atbValue,10);
				flushGraphics();
				if(atbValue<40)
					atbValue++;
				if(atbValue==40){
					atbFill=true;
					//if(atbFill==false){
						page=1;
						changPage();
					//}
					//atbFill=true;
				}
			}else{//ATB����
				g.setColor(255,0,0);
				g.fillRect(disWidth + 7,disHeight + 155,atbValue,10);
				flushGraphics();
				try{
					Thread.sleep(100);
				}catch(Exception e){}
				g.setColor(0,255,0);
				g.drawRect(disWidth + 7,disHeight + 155,atbValue-1,10-1);
				flushGraphics();
				try{
					Thread.sleep(100);
				}catch(Exception e){}
				
				/*roleAttackFrame();
				atbTimer.cancel();
				atbTimer=null;
				atbTimer = new Timer();
				atbFill = false;
				atbValue=0;
				page=0;
				changPage();*/
			}
		}
	}
	
	//XPB�p�ƾ�
	class XpbTimerTask extends TimerTask{
		public void run(){
			if(battleFinish){
				if(xpbFill==false){//XPB������
					try{
						Thread.sleep(100);
					}catch(Exception e){}
					g.setColor(255,255,0);
					g.fillRect(disWidth + 7,disHeight + 170,xpbValue,10);
					flushGraphics();
					if(xpbValue<40)
						xpbValue++;
					if(xpbValue==40){
						xpbFill=true;
					}
				}else{//XPB����
					g.setColor(255,255,0);
					g.fillRect(disWidth + 7,disHeight + 170,xpbValue,10);
					flushGraphics();
					try{
						Thread.sleep(100);
					}catch(Exception e){}
					g.setColor(0,255,0);
					g.drawRect(disWidth + 7,disHeight + 170,xpbValue-1,10-1);
					flushGraphics();
					try{
						Thread.sleep(100);
					}catch(Exception e){}
				}
			}
		}
	}
}