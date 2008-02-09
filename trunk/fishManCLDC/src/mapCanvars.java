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
import javax.microedition.lcdui.*;
import javax.microedition.lcdui.game.*;

public class mapCanvars extends GameCanvas implements Runnable
{
	static map mapImfor;//�x�s�a�Ϫ���T�A�}�C���סB��ơB�C�ơB-1�ȡB�_�l�y��
	static wordLayerCreate makeWord;//wordLayerCreate�~��LayerManager�ù�@�FTiledLayer and Sprite
	static npcGemSprite makeNpcGemSprite;
	static storyEven createStory;//�G�ƨƥ󪺬y�{
	//*******�D�����*******//
	static RoleDataValue mainValue = null;
	
	//*******viewWindow����m X'Y*******//
	private int viewTargetX = 0;
	private int viewTargetY = 0;
	
	//*******mainSprite����m X'Y*******//
	private int mainTargetX = 0;
	private int mainTargetY = 0;
	
	//*******mainSprite����m X'Y*******//
	private int lastKeyState = -1;
	
	//*******Sprite����********//
	private Sprite mainSprite;
	private Sprite checkSprite;
	private Sprite npcSprite[];
	private Sprite gemSprite[];
	private TiledLayer cantMoveLayer;
	
	//*******NPC Image����*******//
	private Image npcImage[];
	
	//********�H�������B�T********//
	private final int step = 16;
	
	//*******������w*******//
	private boolean luckState = true;
	
	//*******view���A*******//
	private boolean viewState = false;
	
	//*******����B��*******//
	private int tick = 0;
	
	//*******���ƩI�s�Ϊ�Timer*******//
	//private Timer gameTimer = new Timer();
	
	//*******�]�w�����m(�̬G�ƶ���)*******//
	private int evenPoint;
	
	//*******���ͩǪ��ü�*******//
	private monsterTurnUp runMonsterTurnUp;
	
	//*******���ͩǪ����ƶq*******//
	final int monsterAmount = 3;//���o�ӼƦr�A���ܨC�Ӧa�ϥX�{�Ǫ����ƶq
	private int monsterCounter = 50;//����Ǫ����ɶ����p���
	final int RESET_MONSTER = 50;
	
	//*******�x�sNPC����*******//
	private int npcKind = -1;
	
	//MIDlet Display
	private Display display = null;
	
	//�԰��O�_����
	public boolean battleFinish = false;
	
	//�O�_������樫�ϰ�I��
	public boolean collision = false;
	
	public static Thread thread = null;
	
	private static RoleStateFrame roleState = null;
	
	//�a�ϭp�ƾ�,�Ψ���ܦa��Title��
	private static int mapTilteValue = 0;
	
	//*******�L�޼ƫغc��*******//
	public mapCanvars(Display display,RoleDataValue roleData,GameMain midlet_)
	{
		super(false);
		
		this.display = display;
		mainValue = roleData;
		//�D�����(�����ΡA�������ϥθ�Ʈw)
		//RoleDataValue mainValue = new RoleDataValue();

		//�Ǫ��԰����
		battle = new BattleFrame(mainValue,this);
		roleState = new RoleStateFrame(mainValue,this,midlet_);
		try{
			setStoreValue();//�]�w�ө����~
		}catch(Exception e){e.printStackTrace();}
		//*******�e��l��m*******//
		drawWord();
		//Thread
		thread = new Thread(this);
		thread.start();
	}
	
	//********�e�a�ϡB�H��...���|�X�{�b�a�ϤW���Ϥ�(�L�޼�)********//
	private void drawWord()
	{
		mapTilteValue = 0;
		
		setEvenPoint(0);

		makeWord = new wordLayerCreate(this.mainValue, this.evenPoint);//�I�s�L�޼ƪ��غc��
		mapImfor = makeWord.getMapClass();
		createStory = new storyEven(this, this.mainValue, this.mapImfor,  this.makeWord, this.evenPoint);
		checkSprite = makeWord.getCheckSprite();
		cantMoveLayer = makeWord.getCantMoveTiled();
		mainSprite = makeWord.getMainSprite();
		makeNpcGemSprite = new npcGemSprite(this.makeWord, this.mapImfor, this.evenPoint);
		npcSprite = makeNpcGemSprite.getNpcSprite();
		gemSprite = makeNpcGemSprite.getGemSprite();
		
		//*******�гyNPC�ܦa�ϤW*******//
		if(mapImfor.getNpc(this.evenPoint))
		{	
			makeNpcGemSprite.makeNpcSprite();
		}
		
		//*******�гy�_�c�ܦa�ϤW*******//
		if(mapImfor.getGem(this.evenPoint).equals("1"))
		{	
			makeNpcGemSprite.makeGemSprite();
		}
		
		//******new() �Ǫ��üƪ�Class
		runMonsterTurnUp = new monsterTurnUp(mapImfor, this.evenPoint);
		
		//*******�_�lMain Sprite�y��********//
		mainTargetX = 299;
		mainTargetY = 267;
		
		//***********�H���_�l�y��**************//
		makeWord.setMainPosition(mainTargetX, mainTargetY);//�Ĥ@����l�C�����T�w�_�l�y�� 304, 272
		makeWord.setCheckPosition(mainTargetX-3, mainTargetY-3);//�I���Ϥ���l�y��
		
		//*******�_�lView Window�y��********//
		viewTargetX = (mainTargetX-this.getWidth()/2);
		viewTargetY = (mainTargetY-this.getHeight()/2);
		
		checkViewWindow();
		
		//makeWord.setView(viewTargetX, viewTargetY, this.getWidth(), this.getHeight());
		
		if(RoleDataValue.roleMap != 0) jumpWord(RoleDataValue.roleMap, RoleDataValue.roleX, RoleDataValue.roleY);
		
		render();
		
		//music
		if(RoleDataValue.roleMap == 0)
			GameMain.music.musicStart(this.evenPoint);
	}
	
	//*******���sø�s�s���a��*******//
	public void resetWord(int deliver, int evenPoint)
	{
		mapTilteValue = 0;
		
		stopNpcGem();//���XNPC
		setEvenPoint(mapImfor.getLink(deliver, evenPoint));
		
		runMonsterTurnUp.setEvenPoint(this.evenPoint);//�⫰��ƥ��I�ǵ��üƩǪ���class
		makeNpcGemSprite.setEvenPoint(this.evenPoint);
		createStory.setEvenPoint(this.evenPoint);
		
		
		this.mainTargetX = mapImfor.getStartX(deliver, evenPoint);
		this.mainTargetY = mapImfor.getStartY(deliver, evenPoint);
		
		//*******��lNPC���Ϥ�*******//
		makeNpcGemSprite.setEvenPoint(this.evenPoint);
		if(mapImfor.getNpc(this.evenPoint))
		{
			makeNpcGemSprite.makeNpcSprite();
		}
		
		//*******�гy�_�c�ܦa�ϤW*******//
		if(mapImfor.getGem(this.evenPoint).equals("1"))
		{	
			makeNpcGemSprite.makeGemSprite();
		}
		
		//*******�]�w�I���θI���Ϥ���l�I*******//
		makeWord.setMainPosition(mainTargetX , mainTargetY);
		makeWord.setCheckPosition(mainTargetX-3, mainTargetY-3);
		
		//*******�_�lView Window�y��********//
		if(mainTargetX>((mapImfor.getColumn(this.evenPoint)*16)-(this.getWidth()/2)))
					viewTargetX = (mapImfor.getColumn(this.evenPoint)*16)-this.getWidth();
		else if(mainTargetX>this.getWidth()/2) viewTargetX = (mainTargetX-this.getWidth()/2);
		else viewTargetX=0;
		
		if(mainTargetY>((mapImfor.getRow(this.evenPoint)*16)-(this.getHeight()/2)))
					viewTargetY = (mapImfor.getRow(this.evenPoint)*16)-this.getHeight();
		else if(mainTargetY>this.getHeight()/2) viewTargetY = (mainTargetY-this.getHeight()/2);
		else viewTargetY=0;
		
		checkViewWindow();
		
		//makeWord.setView(viewTargetX, viewTargetY, this.getWidth(), this.getHeight());
		
		//*****��e�����s�]�w���U�Ӧa��*****//
		makeWord.resetMap(deliver, evenPoint);	
		render();
		
		//music
		GameMain.music.musicStart(this.evenPoint);
	}
	
	//*******���s�i�J�C���ɸ�����*******//
	public void jumpWord(int evenPoint, int x, int y)
	{
		mapTilteValue = 0;
		
		stopNpcGem();//���XNPC
		
		setEvenPoint(evenPoint);
		runMonsterTurnUp.setEvenPoint(this.evenPoint);//�⫰��ƥ��I�ǵ��üƩǪ���class
		makeNpcGemSprite.setEvenPoint(this.evenPoint);
		createStory.setEvenPoint(this.evenPoint);
		
		//music
		GameMain.music.musicStart(this.evenPoint);
		
		this.mainTargetX = x;
		this.mainTargetY = y;
		
		//*******��lNPC���Ϥ�*******//
		makeNpcGemSprite.setEvenPoint(this.evenPoint);
		if(mapImfor.getNpc(this.evenPoint))
		{
			makeNpcGemSprite.makeNpcSprite();
		}
		
		//*******�гy�_�c�ܦa�ϤW*******//
		if(mapImfor.getGem(this.evenPoint).equals("1"))
		{	System.out.println("call makeGemSprite : " + mapImfor.getGem(this.evenPoint));
			makeNpcGemSprite.makeGemSprite();
		}
		
		//*******�]�w�I���θI���Ϥ���l�I*******//
		makeWord.setMainPosition(mainTargetX , mainTargetY);
		makeWord.setCheckPosition(mainTargetX-3, mainTargetY-3);
		
		//*******�_�lView Window�y��********//
		if(mainTargetX>((mapImfor.getColumn(this.evenPoint)*16)-(this.getWidth()/2)))
					viewTargetX = (mapImfor.getColumn(this.evenPoint)*16)-this.getWidth();
		else if(mainTargetX>this.getWidth()/2) viewTargetX = (mainTargetX-this.getWidth()/2);
		else viewTargetX=0;
		
		if(mainTargetY>((mapImfor.getRow(this.evenPoint)*16)-(this.getHeight()/2)))
					viewTargetY = (mapImfor.getRow(this.evenPoint)*16)-this.getHeight();
		else if(mainTargetY>this.getHeight()/2) viewTargetY = (mainTargetY-this.getHeight()/2);
		else viewTargetY=0;
		
		checkViewWindow();
		
		//makeWord.setView(viewTargetX, viewTargetY, this.getWidth(), this.getHeight());
		
		//*****��e�����s�]�w���U�Ӧa��*****//
		makeWord.jumpMap(this.evenPoint);		
		render();
	}
	
	//*******�NNPC�qLayerManager������(�p�G�S���Y�_�c�A�|���K�����_�c)*******//
	public void stopNpcGem()
	{
		if(mapImfor.getGem(this.evenPoint).equals("1")) makeWord.remove(makeWord.getLayerAt(1));
		for(int i=0; i<mapImfor.getNpcAmount(this.evenPoint); i++) makeWord.remove(makeWord.getLayerAt(1));
	}
	
	private void render()
	{
		if(!storeEvent){
		    final Graphics g = this.getGraphics();
		    g.setColor(0, 0, 0);
		    g.fillRect(0, 0, this.getWidth(), this.getHeight());
		    makeWord.paint(g, 0, 0);
		    if(mapTilteValue <= 10){
		    	g.drawImage(mapBar,0+getWidth()-100,0,0);
		    	g.drawString(db.readDBField(4,this.evenPoint+1,2),0+getWidth()-94,5,0);
		    }
		    flushGraphics();
		}
    }
  		
		//********���䰻��********//
		private void processInput()//********��evenPoint�v�T��Method()********//
		{	
	        final int keyState = this.getKeyStates();
	        
	           //���ө�Ĳ�o��-----------------------------------------------------------------------------
		       if(storeEvent){
		       		if( (storeEvent==true) & (keyState & FIRE_PRESSED) !=0 ){
		       			if(talkAbout){
		       				try{
								eventTalk();
							}catch(Exception e){e.printStackTrace();}
		       			}else
						switch (menustate) {//�P�_�e������b���ӭ���
							case 0://���
								//*******�s���G�Ƭy�{*******//
								createStory.doEven(npcKind);
								usualTalk = false;
								if(!talkAbout){
									setTalkAbout(createStory.getTalkValue());
									talkAbout = true;
									whoTalkAbout = false;
								}
								try{
									eventTalk();
								}catch(Exception e){e.printStackTrace();}
								break;
							case 1://�R���~��
								try{
								page=1;
								if(storeEvent=true && page==1){
					        		itemEvent = false;
					        		itemBuy = false;
								}
								changPage();
								}catch(Exception e){e.printStackTrace();}
								break;
							case 2:
								if(page==0 & (npcKind==2 || npcKind==5 || npcKind==8 || npcKind==9)){//��F��
									page=2;
									if(storeEvent=true && page==2){
						        		itemEvent = false;
						        		itemBuy = false;
									}
									changPage();
								}								
								else if(page==1 & (npcKind==2 || npcKind==5 || npcKind==8 || npcKind==9)){//�T�w�R
									itemEvent=true;
									itemBuy = !itemBuy;
									if(itemBuy==false){//�T�w�n�R���˪��~��
										itemEvent = false;
										mainValue.addItem(itemBuff[storeIndex+itemSelect][0],true,1);
										ConnectionServer.updateGameInf();
										System.out.println("buy:"+(storeIndex+itemSelect+1));
									}
									page=1;
									changPage();
								}//�T�w��
								else if(page==2 & (npcKind==2 || npcKind==5 || npcKind==8 || npcKind==9)){
									itemEvent=true;
									itemBuy = !itemBuy;
									if(itemBuy==false){//�T�w�n�榹�˪��~��
										itemEvent = false;
										mainValue.useItem(itemBuff[storeIndex+itemSelect][0],true);
										if(mainValue.getItemAmount(itemBuff[storeIndex+itemSelect][0]).equals("0")){
											itemSelect=0;
											storeIndex=0;
										}
										ConnectionServer.updateGameInf();
										System.out.println("sell:"+(storeIndex+itemSelect+1));
									}
									page=2;
									changPage();
								}else{//��ܤG
									//*******�s���G�Ƭy�{*******//
									//createStory.doEven(npcKind);
									//usualTalk = false;
									if(npcKind!=4 || (npcKind==4 && RoleDataValue.nowEvenSchedule[0].indexOf("4")==-1)){//�p�G���]���ƥ��,�~�|Ĳ�o
										createStory.doEven(npcKind);
										usualTalk = false;
										if(!talkAbout & !otherEventFinish){
											setTalkAbout(createStory.getTalkValue());
											talkAbout = true;
											whoTalkAbout = false;
										}//�p�G�D�����ۨ��ۻy��
										if(otherEventFinish){
											if(!otherEvent)
												roleOwnEvent(1);
											else{
												try{
													eventTalk();
												}catch(Exception e){e.printStackTrace();}
											}
										}else
											eventTalk();
									}else if(npcKind==4 && RoleDataValue.nowEvenSchedule[0].indexOf("4")!=-1){//��
										RoleDataValue.nowHP = RoleDataValue.maxHP;
										//System.out.println("sleep");
									}
								}
								break;
							case 3:
								if(page==1 ||page==2){
									itemSelect=0;
									storeIndex=0;
									drawItemMenu = false;
									itemEvent = true;
									storeG.fillRect(0, 0, this.getWidth(), this.getHeight());
									makeWord.paint(storeG, 0, 0);
									flushGraphics();
									page=0;
									changPage();
								}
								else{
									storeEvent=false;
									npcKind = -1;
									otherEvent = false;
									otherEventFinish = false;
									//gameTimer = new Timer();
									//gameTimer.scheduleAtFixedRate(new gameTask(), 0, 10);
								}
								break;
						}
		       		}//�W
					else if( !talkAbout & ((keyState & UP_PRESSED) != 0)){
						//System.out.println("up");
						if(itemEvent && (page==1 || page==2)){
							if(menustate <=2 )
								menustate=4;
							menustate--;
						}
						else if(!itemEvent && (page==1 || page==2)){//������~��
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
						}//���O�ө�npc��
						else if(!(npcKind==2 || npcKind==5 || npcKind==8 || npcKind==9)){
							if(menustate <=2 )
								menustate=4;
							menustate--;
						}
						else {//�@��Menu����
							if (menustate <= 0)
								menustate = menuNum;
							menustate--;
						}
					}//�U
					else if( !talkAbout & ((keyState & DOWN_PRESSED) != 0)){
						//System.out.println("down");
						if(itemEvent && (page==1 || page==2)){
							if(menustate >=3 )
								menustate=1;
							menustate++;
						}
						else if(!itemEvent && (page==1 || page==2)){
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
						}//���O�ө�npc��
						else if(!(npcKind==2 || npcKind==5 || npcKind==8 || npcKind==9)){
							if(menustate >=3 )
								menustate=1;
							menustate++;
						}
						else {//�@��Menu����
							if (menustate >= menuNum - 1)
								menustate = -1;
							menustate++;
						}
					}
					try{
						changeMenu();
					}catch(Exception e){e.printStackTrace();}
					
		    }//-------------------------------------------------------------------------------------------------
			else{//�a�Ϩ��ʮ�
				mapTilteValue++;
		        //*********************RIGHT ACTIVE*****************************//
		        if ((luckState == true) & ((keyState & RIGHT_PRESSED) != 0)) {
		        	if(lastKeyState != keyState) makeWord.setImage(3);
		        	makeWord.setCollisionArea(34, 31, 1, 1);
		        	luckState = false;
		        	
		            mainTargetX += step;
		            
		            if(mainTargetX > getWidth()/2){
			        		viewTargetX = mainTargetX-(this.getWidth()/2);
			        		viewState = true;
			        		
			        		if(viewTargetX+this.getWidth() > makeWord.getCellWidth()*mapImfor.getColumn(evenPoint))
			        		{
			        				viewTargetX = (makeWord.getCellWidth()*mapImfor.getColumn(evenPoint)) - this.getWidth();
			        				viewState = false;
			        		}
		        	}
		            for(int i=0; i<mapImfor.getNpcAmount(this.evenPoint); i++) checkNpcCollision(keyState, i);
					checkCollision(keyState);
					if(mapImfor.getStop(3, this.evenPoint) & (mapImfor.getDBStop(3, this.evenPoint).equals("1")))
						checkStopCollision(keyState, 3);
						
					
					for(int j=0; j<mapImfor.getGemAmount(this.evenPoint); j++)
						if(mapImfor.getGem(this.evenPoint).equals("1")) checkGemCollision(j);
					if(mapImfor.getMonster(this.evenPoint)) callMonster();
					
					lastKeyState = keyState;
		        	//*********************RIGHT ACTIVE END*************************//
		        	
		        	//*********************Print out*******************//
		        	//System.out.println("RIGHT");
	            	//System.out.println("mainTargetX = " + mainTargetX);
					//System.out.println("mainTargetY = " + mainTargetY);
					//System.out.println("mainSprite.getRefPixelX() = " + mainSprite.getRefPixelX());
					//System.out.println("mainSprite.getRefPixelY() = " + mainSprite.getRefPixelY());
					
					//*********************LEFT ACTIVE *****************************// 
		        } else if ((luckState == true) & ((keyState & LEFT_PRESSED) != 0)) {
		        	if(lastKeyState != keyState) makeWord.setImage(2);;
		        	makeWord.setCollisionArea(2, 31, 1, 1);
		        	luckState = false;
	
		            mainTargetX -= step;
		            
		            if(mainTargetX > ((makeWord.getCellWidth()*mapImfor.getColumn(evenPoint)) - this.getWidth()/2))	;
					else if(mainTargetX > getWidth()/2){
		        		
			        		viewTargetX = mainTargetX-((this.getWidth()/2)+8);
			        		viewState = true;
		        	}
					for(int i=0; i<mapImfor.getNpcAmount(this.evenPoint); i++) checkNpcCollision(keyState, i);
					checkCollision(keyState);
					if(mapImfor.getStop(2, this.evenPoint) & (mapImfor.getDBStop(2, this.evenPoint).equals("1")))
						checkStopCollision(keyState, 2);
					
					for(int j=0; j<mapImfor.getGemAmount(this.evenPoint); j++)
						if(mapImfor.getGem(this.evenPoint).equals("1")) checkGemCollision(j);
					if(mapImfor.getMonster(this.evenPoint)) callMonster();
					lastKeyState = keyState;
		        	//*********************LEFT ACTIVE END**************************//
		        	
		        	//*********************Print out*******************//
		        	//System.out.println("LEFT");
	            	//System.out.println("mainTargetX = " + mainTargetX);
					//System.out.println("mainTargetY = " + mainTargetY);
					//System.out.println("mainSprite.getRefPixelX() = " + mainSprite.getRefPixelX());
					//System.out.println("mainSprite.getRefPixelY() = " + mainSprite.getRefPixelY());
		        	
		        	//**********************DOWN ACTIVE*****************************//
		        } else if((luckState == true) & ((keyState & DOWN_PRESSED) != 0)){
		        	if(lastKeyState != keyState) makeWord.setImage(1);
		        	makeWord.setCollisionArea(12, 50, 3, 1);
		        	luckState = false;
		        	
		            mainTargetY += step;
		            
		            if(mainTargetY > this.getHeight()/2){
		        		
			        			viewTargetY = mainTargetY-(this.getHeight()/2);
			        			viewState = true;
			        			
			        			if(viewTargetY+this.getHeight() > makeWord.getCellHeight()*mapImfor.getRow(evenPoint))
			        			{
			        					viewTargetY = (makeWord.getCellHeight()*mapImfor.getRow(evenPoint)) - this.getHeight();
			        					viewState = false;
			        			}
			        			
		        	}
		            for(int i=0; i<mapImfor.getNpcAmount(this.evenPoint); i++) checkNpcCollision(keyState, i);
					checkCollision(keyState);
					if(mapImfor.getStop(1, this.evenPoint) & (mapImfor.getDBStop(1, this.evenPoint).equals("1")))
						checkStopCollision(keyState, 1);
					
					for(int j=0; j<mapImfor.getGemAmount(this.evenPoint); j++)
						if(mapImfor.getGem(this.evenPoint).equals("1")) checkGemCollision(j);
					if(mapImfor.getMonster(this.evenPoint)) callMonster();
					lastKeyState = keyState;
		            //**********************DOWN ACTIVE END*******************************//
		            
		            //*********************Print out*******************//
		            //System.out.println("DOWN");
	            	//System.out.println("mainTargetX = " + mainTargetX);
					//System.out.println("mainTargetY = " + mainTargetY);
					//System.out.println("mainSprite.getRefPixelX() = " + mainSprite.getRefPixelX());
					//System.out.println("mainSprite.getRefPixelY() = " + mainSprite.getRefPixelY());
					
					//**********************UP ACTIVE*************************************//
		        } else if((luckState == true) & ((keyState & UP_PRESSED) != 0)){
		        	if(lastKeyState != keyState) makeWord.setImage(0);
		        	makeWord.setCollisionArea(11, 16, 8, 3);
		        	luckState = false;
					
		            mainTargetY -= step;
	
					if(mainTargetY > ((makeWord.getCellHeight()*mapImfor.getRow(evenPoint)) - this.getHeight()/2))	;
					else if(mainTargetY > this.getHeight()/2){
			        			viewTargetY = mainTargetY-((this.getHeight()/2)+16);
			        			viewState = true;
		        	}
					for(int i=0; i<mapImfor.getNpcAmount(this.evenPoint); i++) checkNpcCollision(keyState, i);
					checkCollision(keyState);
					if(mapImfor.getStop(0, this.evenPoint) & (mapImfor.getDBStop(0, this.evenPoint).equals("1")))
						checkStopCollision(keyState, 0);
					
					for(int j=0; j<mapImfor.getGemAmount(this.evenPoint); j++)
						if(mapImfor.getGem(this.evenPoint).equals("1")) checkGemCollision(j);
					if(mapImfor.getMonster(this.evenPoint)) callMonster();
					lastKeyState = keyState;
	            //**********************UP ACTIVE END*********************************//
	            
	            	//*********************Print out*******************//
	            	//System.out.println("UP");
	            	//System.out.println("mainTargetX = " + mainTargetX);
					//System.out.println("mainTargetY = " + mainTargetY);
					//System.out.println("mainSprite.getRefPixelX() = " + mainSprite.getRefPixelX());
					//System.out.println("mainSprite.getRefPixelY() = " + mainSprite.getRefPixelY());
		        }
		        //����U�������ɡA�_�ʹ�ܤ��----------------------------------------
		        else if( (npcKind!=-1) & (luckState == true) & (storeEvent==false) & (keyState & FIRE_PRESSED) !=0 ){
		        	storeEvent=true;
		        	page=0;
		        	//System.out.println(npcKind);
		        	changPage();
		        }else if((keyState & GAME_A_PRESSED)!=0 ){
		        	RoleDataValue.roleMap = this.evenPoint;
		        	RoleDataValue.roleX = this.mainTargetX;
		        	RoleDataValue.roleY = this.mainTargetY;
		        	ConnectionServer.updateGameInf();
		        	battleFinish = true;
		        	roleState.roleStateStart();
		        	display.setCurrent(roleState);
		        }
		        //-------------------------------------------------------------------
			}
	    }
	    
	    //****************�H�����ʨB��****************//
	    private void tick()
		{
			if (mainSprite.getRefPixelY() < mainTargetY) {//�V�U����
	            //if((tick % 2) == 0) {
	                mainSprite.move(0, step);
	                checkSprite.move(0, step);
	                
	                if(viewState)
	                {	if(! collision) makeWord.setView(viewTargetX, viewTargetY-(step),
	                									  this.getWidth(), this.getHeight());
	                	viewState = false;	}
	                	else
	                	{	makeWord.setView(viewTargetX, viewTargetY, this.getWidth(), this.getHeight());}
	                
	                checkViewWindow();
	                
	                //mainSprite.setFrameSequence(new int[]{0, 1, 2});
	                mainSprite.nextFrame();
	                mainSprite.nextFrame();
	            //}
	            //this.tick++;
	        }else if (mainSprite.getRefPixelY() > mainTargetY) {//�V�W����
	            //if((tick % 2) == 0) {
	            	makeWord.spriteMove(0, -(step));
	                makeWord.checkSpriteMove(0, -(step));
	                
	                if(viewState)
	                {	if(! collision) makeWord.setView(viewTargetX, viewTargetY+(step),
	                												this.getWidth(), this.getHeight());
	                	viewState = false;	}
	                	else	
	                	{	makeWord.setView(viewTargetX, viewTargetY, this.getWidth(), this.getHeight());	}
	                
	                checkViewWindow();
	                
	                //mainSprite.setFrameSequence(new int[]{0, 1, 2});
	                mainSprite.nextFrame();
	                mainSprite.nextFrame();
	            //}
	            //this.tick++;
	        }else if (mainSprite.getRefPixelX() < mainTargetX) {//�V�k����
	            //if((this.tick % 2) == 0) {
	            	makeWord.spriteMove(step, 0);
	                makeWord.checkSpriteMove(step, 0);
	                
	                if(viewState)
	                {	if(! collision) makeWord.setView(viewTargetX-(step), viewTargetY,
	                												this.getWidth(), this.getHeight());
	                	viewState = false;	}
	                	else	
	                	{	makeWord.setView(viewTargetX, viewTargetY, this.getWidth(), this.getHeight());}
	               
	               checkViewWindow();
	               
	                //mainSprite.setFrameSequence(new int[]{0, 1, 2});	
	                mainSprite.nextFrame();
	                mainSprite.nextFrame();
	            //}
	            //this.tick++;
	        }else if (mainSprite.getRefPixelX() > mainTargetX) {//�V������
	            //if((this.tick % 2) == 0) {
	            	makeWord.spriteMove((-step), 0);
	                makeWord.checkSpriteMove((-step), 0);
	                
	                if(viewState)
	                {	if(! collision) makeWord.setView(viewTargetX+(step), viewTargetY,
	                												this.getWidth(), this.getHeight());
	                	viewState = false;	}
	                	else	
	                	{	makeWord.setView(viewTargetX, viewTargetY, this.getWidth(), this.getHeight());	}
	                
	                checkViewWindow();
	                
	                //mainSprite.setFrameSequence(new int[]{0, 1, 2});
	                mainSprite.nextFrame();
	                mainSprite.nextFrame();
	            //}
	            //this.tick++;
	            }
			else
			{
				//mainSprite.setFrameSequence(new int[]{0, 1, 2});
				//mainSprite.setFrame(0);
				//mainSprite.nextFrame();
				//mainSprite.nextFrame();
				//mainSprite.setFrame(0);
				//tick = 0;
				luckState = true;
			}
		}
		
		
		//********�����I��(�H���樫��a��)*******//
		private void checkCollision(int key)
		{
			//System.out.println("Check�I����......");
			
			if( checkSprite.collidesWith(cantMoveLayer, true) )
			{
				//System.out.println("�I��...");
				collision = true;
				
				if(key == LEFT_PRESSED)
				{
					mainTargetX += step;
					makeWord.spriteMove(step, 0);
					makeWord.checkSpriteMove(step, 0);
					//makeWord.setView(viewTargetX+step, viewTargetY, this.getWidth(), this.getHeight());
				}
               else if(key == RIGHT_PRESSED)
               {
               		mainTargetX -= step;
               		makeWord.spriteMove(-(step), 0);
               		makeWord.checkSpriteMove(-(step), 0);
               		//makeWord.setView(viewTargetX-step, viewTargetY, this.getWidth(), this.getHeight());
               }
               else if(key == UP_PRESSED)
               {
               		mainTargetY += step;
               		makeWord.spriteMove(0, step);
               		makeWord.checkSpriteMove(0, step);
               		//makeWord.setView(viewTargetX, viewTargetY+step, this.getWidth(), this.getHeight());
               }
               else if(key == DOWN_PRESSED)
               {
               		mainTargetY -= step;
               		makeWord.spriteMove(0, -step);
                    makeWord.checkSpriteMove(0, -step);
                    //makeWord.setView(viewTargetX, viewTargetY-step, this.getWidth(), this.getHeight());
               }
             
			}else collision = false;
		}
		
		
		//********����NPC�I��(�H���樫��a��)*******//
		private void checkNpcCollision(int key, int num)
		{
			
			if( checkSprite.collidesWith(npcSprite[num], false) )
			{
				this.npcKind = mapImfor.getNpcKind(this.evenPoint, num);
				
				if(key == LEFT_PRESSED)
				{
					mainTargetX += step;
					makeWord.spriteMove(step, 0);
					makeWord.checkSpriteMove(step, 0);
					makeWord.setView(viewTargetX+step, viewTargetY, this.getWidth(), this.getHeight());		
				}
               else if(key == RIGHT_PRESSED)
               {
               		mainTargetX -= step;
               		makeWord.spriteMove(-(step), 0);
               		makeWord.checkSpriteMove(-(step), 0);
               		makeWord.setView(viewTargetX-step, viewTargetY, this.getWidth(), this.getHeight());
               }
               else if(key == UP_PRESSED)
               {
               		mainTargetY += step;
               		makeWord.spriteMove(0, step);
               		makeWord.checkSpriteMove(0, step);
               		makeWord.setView(viewTargetX, viewTargetY+step, this.getWidth(), this.getHeight());
               }
               else if(key == DOWN_PRESSED)
               {
               		mainTargetY -= step;
               		makeWord.spriteMove(0, -step);
                    makeWord.checkSpriteMove(0, -step);
                    makeWord.setView(viewTargetX, viewTargetY+step, this.getWidth(), this.getHeight());
               }
             
			}
		}
		

		
		//********�����_���I��(�H���樫��a��)*******//
		private void checkGemCollision(int nom)
		{
			
			//*******�_���I������******//
			if( checkSprite.collidesWith(gemSprite[nom], true) )
			{
				//���o�_�c���~
				System.out.println("getGemItem");
				for(int i=0; i<mapImfor.getGemAmount(this.evenPoint); i++)
				{
					mainValue.addItem(mapImfor.getGemContents(this.evenPoint, i), false, 1);
				}
				ConnectionServer.updateGameInf();//��s�D�����~
				
				//�w���o�_�c���~
				mapImfor.setGem(this.evenPoint, "0");
				ConnectionServer.updateGameEvent(0);
				
				//remove gem
				makeWord.remove(makeWord.getLayerAt(1));
				
			}
		}
		
		//******�ǰe�I�����I��******//
		public void checkLinkCollision()
		{
			int weight=mainTargetX+12;
			int height=mainTargetY+19;
			int leftLimitX, leftLimitY;
			int limitX, limitY;
			
			for(int i=0; i<4; i++)
			{
				leftLimitX=mapImfor.getDeliverX(i, this.evenPoint);
				leftLimitY=mapImfor.getDeliverY(i, this.evenPoint);
				
				if(i<2)
				{
					limitX=mapImfor.getDeliverX(i, this.evenPoint)+64;
					limitY=mapImfor.getDeliverY(i, this.evenPoint)+16;
				}else
				{
					limitX=mapImfor.getDeliverX(i, this.evenPoint)+16;
					limitY=mapImfor.getDeliverY(i, this.evenPoint)+64;
				}
				
				if(((weight>leftLimitX) & ((weight)<limitX)) &
							(height>leftLimitY) & ((height)<limitY) ) {	resetWord(i, evenPoint);	}
			}
		}
		
		//*******�������ɪ��I��*********//
		private void checkStopCollision(int key, int num)
		{
			if(key == LEFT_PRESSED)
				{
					if(((mainTargetY+19)>mapImfor.getStopY(num, this.evenPoint)) & 
							((mainTargetY+19)<(mapImfor.getStopY(num, this.evenPoint)+64)))
					{
						if(((mainTargetX-16)<mapImfor.getStopX(num, this.evenPoint)))
						{
							mainTargetX += step;
							makeWord.spriteMove(step, 0);
							makeWord.checkSpriteMove(step, 0);
							makeWord.setView(viewTargetX+step, viewTargetY, this.getWidth(), this.getHeight());
						}
					}
				}
               else if(key == RIGHT_PRESSED)
               {
               		if(((mainTargetY+19)>mapImfor.getStopY(num, this.evenPoint)) & 
							((mainTargetY+19)<(mapImfor.getStopY(num, this.evenPoint)+64)))
					{
						if(((mainTargetX+8)>(mapImfor.getStopX(num, this.evenPoint))))
						{
		               		mainTargetX -= step;
		               		makeWord.spriteMove(-(step), 0);
		               		makeWord.checkSpriteMove(-(step), 0);
		               		makeWord.setView(viewTargetX-step, viewTargetY, this.getWidth(), this.getHeight());
		       			}
		       		}
		       }
               else if(key == UP_PRESSED)
               {
               		if(((mainTargetX+12)>mapImfor.getStopX(num, this.evenPoint)) & 
							((mainTargetX+12)<(mapImfor.getStopX(num, this.evenPoint)+64)))
					{
						if(((mainTargetY+3)<(mapImfor.getStopY(num, this.evenPoint))))
						{
		               		mainTargetY += step;
		               		makeWord.spriteMove(0, step);
		               		makeWord.checkSpriteMove(0, step);
		               		makeWord.setView(viewTargetX, viewTargetY+step, this.getWidth(), this.getHeight());
		    			}	
		    		}
		       }
               else if(key == DOWN_PRESSED)
               {
               		if(((mainTargetX+12)>mapImfor.getStopX(num, this.evenPoint)) & 
							((mainTargetX+12)<(mapImfor.getStopX(num, this.evenPoint)+64)))
					{
						if(((mainTargetY+22)>(mapImfor.getStopY(num, this.evenPoint))))
						{
		               		mainTargetY -= step;
		               		makeWord.spriteMove(0, -step);
		                    makeWord.checkSpriteMove(0, -step);
		                    makeWord.setView(viewTargetX, viewTargetY+step, this.getWidth(), this.getHeight());
		    			}
		    		}
		       }
		}
		
		//********�üƲ��ͩǪ��A�åB�����I��*******//
		private static BattleFrame battle = null;
		private void callMonster()
		{			
			for(int i=0; i<monsterAmount; i++)
			{
				monsterCounter--;
				
				//���ͶüƮy��
				runMonsterTurnUp.runRandom();
				
				//�P�_�O�_���I����Ǫ�
				if(runMonsterTurnUp.monsterCollision(mainTargetX, mainTargetY) & (monsterCounter == 0))
				{
					monsterCounter = RESET_MONSTER;
					
					//***�e�X�԰��e��***//
					/*RoleDataValue.roleMap = this.evenPoint;
		        	RoleDataValue.roleX = this.mainTargetX;
		        	RoleDataValue.roleY = this.mainTargetY;
		        	ConnectionServer.updateGameInf();*/
					//callBattle("M50");
					
					final Graphics g = this.getGraphics();
					g.drawImage(bar,disWidth+40,disHeight+80,0);
					g.drawString("  �J  ��  ��  ��",disWidth+44,disHeight+84,0);
					flushGraphics();
					try{ 
        			 	Thread.sleep(200);
        			 }
		        	 catch(Exception e) {}
					battle.battleStart(this.evenPoint+1,"");
					battleFinish = true;
					display.setCurrent(battle);
						//System.out.println("�԰�..........");
					
					break;
				}
			}
		}
		
		//�I�s�԰�,�D�ü�
		public void callBattle(String monster){
			System.out.println(monster);
			storeEvent=false;
			npcKind = -1;
			otherEvent = false;
			otherEventFinish = false;
			final Graphics g = this.getGraphics();
			g.drawImage(bar,disWidth+40,disHeight+80,0);
			g.drawString("  �i  �J  ��  ��",disWidth+44,disHeight+84,0);
			flushGraphics();
			try{ 
			 	Thread.sleep(200);
			 }
        	 catch(Exception e) {}
			battle.battleStart(this.evenPoint+1,monster);
			battleFinish = true;
			display.setCurrent(battle);
		}
		
		public void checkViewWindow()
		{
			if(this.getWidth() > (mapImfor.getColumn(this.evenPoint)*16))
			{
				viewTargetX = 0;
			}
			
			if(this.getHeight() > (mapImfor.getRow(this.evenPoint)*16))
			{
				viewTargetY = 0;
			}
			
			if(! collision) makeWord.setView(viewTargetX, viewTargetY, this.getWidth(), this.getHeight());
		}
		
		//���Thread
		public void threadStart(){
			//if(RoleDataValue.name.equals("�ݪ��l"))
				//makeWord.remove(makeWord.getLayerAt(2));
			battleFinish = false;
			display.setCurrent(this);
			//thread = new Thread(this);
			//thread.start();
		}
		
		//********��@TimerTask********//
		//private class gameTask extends TimerTask
		//{
	        public void run()
	        {
	        	while(true)
	        	{
	        		if(battleFinish){//�p�G�b�԰�,����a��thread
	        			if(!BattleFrame.battleFinish)
	        				battleFinish=false;
	        			 try{ 
	        			 	Thread.sleep(200); 
	        			 	continue;
	        			 }
			        	 catch(Exception e) {}
	        		}
	        		
			        try{ Thread.sleep(100); }
			        catch(Exception e) {}
			        	
			        processInput();
			        
			        if(!storeEvent)	tick();
			        	
			        //if(mapImfor.getMonster(this.evenPoint)) callMonster();
			        	
			        render();
			        	
			        if(!storeEvent)	checkLinkCollision();
            	}
	        }
		//}
		
		//********�]�w�G���I********//
		public void setEvenPoint(int evenPoint)
		{
			this.evenPoint = evenPoint;
		}
		
		//********���owordLayerCreate��class********//
		public wordLayerCreate getMakeWordClass()
		{
			return makeWord;
		}
		
//		-----------------------------�ө���ܿ��---------------------------------------
		private Graphics storeG;
		
		private static Image img = null;
		
		private Image imgBack = null;

		private int menustate = 0;//�ثe����쪺�ﶵ
		private int itemSelect = 0;

		private int menuNum = 4;//�@���X�ӿﶵ

		private int disWidth = (getWidth()-176)/2;//����ù��������I

		private int disHeight = (getHeight()-208)/2;//����ù��������I
		
		private String[] menuStr = {"  ��� ","  �ʶR ","  �c�� ","  ���} "};
		private String[] menuBuyStr = {"    �R","  ���}"};
		private String[] menuSellStr = {"    ��","  ���}"};
		private String[] nowString = new String[4];
		private int moveY = 0;//������m
		
		//private int moveItemX = 0;//���~�W�ٰ����q80
		//private int moveItemY = 0;//���~�W�ٰ����q64
		
		private int page = 0;//����
		private boolean drawItemMenu = false;//�O�_���n�e���~
		
		//-----------�O�_���ө�Ĳ�o-----------
		private boolean storeEvent = false;//�O�_���ө�
		private boolean itemEvent = true;//true�ɪ����ܰө������~�b�M�椤
		private boolean itemBuy = false;//��false����٨S�T�w�n�R,true��ܽT�w�n�R
		private boolean talkAbout = false;//��true��,��ܦb���
		private boolean whoTalkAbout = false;//�ثe�O�֦b����false��npc,true���D��
		private boolean talkFinish = false;//�O�_�w������
		//--------------DataBase-------------
		private static DataBaseReader db = new DataBaseReader();
		private String itemBuff[][] = new String[40][];//�ө����~��T�Ȧs
		//private String itemPole[][] = new String[13][];//���񪫫~
		//private String itemGe[][] = new String[12][];//���㪫�~
		private String itemOther[][] = new String[40][];//�����ɫ~
		//private String itemJew[][] = new String[4][];//���~
		private int itemTotal = 0;//���㩱���~�`��
		private int storeIndex=0;//�ө����~����
		//0�u��  1���C   *2�S    3����    4��    *5��  6��(���B)  7�ѯ�  
		//*8��  *9��    10�u��(��) 11 12�ݪ��l  13����  14�d�~�Ѿ�
		//15��H�� 16��  17����  18�p��  19�u�@��  20���F�H

		//private int storeNum = -1;//�ө�����,���񩱳]8,����]5,����ɫ~�]9,�S��]2
		private String[] itemNameArray = {"Item","","Jewelry","","Battle","Protect","	Event",
				"Monster","Pole","Bait","Tonic","Talk",""};
		private int dataFieldNum[] = {6,4,11,1,4,10,4,11,12,7,7,4,9,6,3};//�C�@��Ʈw����
		private Image img2[] = new Image[40];//�ө����~�Ϥ�Buffer
		private Image imgOther[] = new Image[40];
		private Image buyItemImg = createImage("/image/gameInterface/buyItem.png");//�R���~�ɪ����Ϥ�
		private Image nowPeoImg = null;//��ܮɤH�������ϥ�
		private Image converImg = createImage("/image/gameInterface/Conversation.png");//��ܤ��
		private Image buyMenuImg = createImage("/image/gameInterface/buyMenu.png");//�R���~�ɪ����
		private Image mapBar = createImage("/image/gameInterface/mapBar.png");//�a��bar
		private Image bar = createImage("/image/gameInterface/bar.png");//�J�Ǫ��αo�쪫�~
		
		//private static String itemAmount[] = new String[99];
		
		private boolean otherEventFinish = false;//�D���ۨ��ۻy�O�_����
		private boolean otherEvent = false;//�D���ۨ��ۻy
		
		//private RoleDataValue roleData = new RoleDataValue(" ");

		//�إ߹Ϥ�
		public Image createImage(String pathImg) {
			img = null;
			try {
				img = Image.createImage(pathImg);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return img;
		}
		
		//�e�ө���menu�b�a�ϤW
		public void drawDisplay(){
			if(storeEvent){
				storeG = getGraphics();
				//��ܹϤ�
				storeG.drawImage(converImg,disWidth+0,disHeight+144,0);
				if(page==0 & !otherEvent){//�@���ܮ�
					render();
					try{
						usualTalk = true;
						setTalkAbout(0);
						talkAbout(0);
						talkAbout = false;
					}catch(Exception e){e.printStackTrace();}
					if(nowPeoImg!=null){
						storeG.drawImage(nowPeoImg,disWidth+0,disHeight+80,0);
					}
				}
				storeG.drawImage(buyMenuImg,disWidth+121,disHeight+77,0);
				if(page==1 || page==2){//�p�G�n�R�F���,��ܪ��~��
					storeG.drawImage(buyItemImg,disWidth+3,disHeight+0,0);
				}
				flushGraphics();
				changeMenu();//���檺�r�e�W�h
			}
		}
		
		//�D���ۨ��ۻy���
		public void roleOwnEvent(int roleOwnNum){
			storeEvent=true;
			otherEvent = true;
			otherEventFinish = true;
        	page=0;
        	changPage();
			talkAbout = true;
			whoTalkAbout = true;
			talkTatol = new String[1];
			talkTatol[0] = db.readDBField(14,roleOwnNum,2);
			eventTalk();
		}
		
		private String talkTatol[] = null;
		private String talkTmp[] = null;
		private String npcName = "";
		private int talkNum = 0;
		//�]�w���,kind=1~5
		public boolean setTalkAbout(int kind){
			if(kind==-1)
				return false;
			String tmp = db.readDBField(12,npcKind+1,4+kind);
			npcName = db.readDBField(12,npcKind+1,2)+" ��:";
			tmp = tmp.substring(3,tmp.length());
			if(tmp.equals("NO"))
				return false;
			talkTatol = split(tmp,"#");
			return true;
		}
		
		//���
		private int talkIndex = -3;
		private int talkEnd = 0;
		private boolean usualTalk = false;
		public void talkAbout(int n){
			if(!talkFinish){
				String tmp = talkTatol[n];
				int line = 0;
				//�P�_��ܦ��X��
				if(tmp.length() % 13==0)
					line = tmp.length() / 13;
				else
					line = (tmp.length() / 13)+1;
				
				char charTmp[] = tmp.toCharArray();
				talkTmp = new String[line];
				for(int i=0,k=0;i<line;i++){
					talkTmp[i]="";
					while(true){
						talkTmp[i]+=charTmp[k];
						k++;
						if(k%13==0 || k>=charTmp.length) break;
					}
					//System.out.println("k"+k);
				}
				talkFinish = true;
				talkIndex = -3;
				talkEnd = 0;
				//talkNum = 0;
				//whoTalkAbout = false;
			}
			if(usualTalk & !otherEvent){
				storeG.setColor(255, 255, 255);
				if(npcKind==25)
					storeG.drawString(RoleDataValue.name + " ��:", disWidth + 7, disHeight + 148, 0);
				else
					storeG.drawString(npcName, disWidth + 7, disHeight + 148, 0);
			}
			if(talkTmp.length<=3){
				talkIndex=0;
				talkEnd = talkTmp.length;
				talkFinish = false;
				if(!usualTalk && createStory.getTalkValue()>0){
					whoTalkAbout = !whoTalkAbout;
					//System.out.println("ppppppp"+createStory.getTalkValue());
				}
			}else{
				talkIndex+=3;
				talkEnd+=3;
				if(talkEnd>=talkTmp.length){
					talkEnd = talkTmp.length;
					talkFinish = false;
					//talkNum = 0;
					whoTalkAbout = !whoTalkAbout;
				}
			}
			try{
				for(int i=talkIndex,k=14;i<talkEnd;i++,k+=14){
					storeG.drawString(talkTmp[i], disWidth + 7, disHeight + 148+k, 0);
				}
			}catch(Exception e){e.printStackTrace();}
		}
		
		//�ƥ���
		public void eventTalk(){
			storeG.setColor(0, 0, 0);
			storeG.fillRect(0, 0, this.getWidth(), this.getHeight());
			makeWord.paint(storeG, 0, 0);
			storeG.drawImage(converImg,disWidth+0,disHeight+144,0);
			storeG.drawImage(buyMenuImg,disWidth+121,disHeight+77,0);
			storeG.setColor(255, 255, 255);
			if(otherEvent)
				whoTalkAbout = true;
			if(npcKind==25 || whoTalkAbout){//���D�����ܮ�
				//System.out.println("11");
				storeG.drawImage(RoleDataValue.roleImg,disWidth+0,disHeight+80,0);
				storeG.drawString(RoleDataValue.name + " ��:", disWidth + 7, disHeight + 148, 0);
				//System.out.println("22");
			}
			else{
				if( !(nowPeoImg==null) )
					storeG.drawImage(nowPeoImg,disWidth+0,disHeight+80,0);
				storeG.drawString(npcName, disWidth + 7, disHeight + 148, 0);
			}

			if(talkAbout){//����ƥ��I
				talkAbout(talkNum);
				if(!talkFinish){
					talkNum++;
					if(talkNum>=talkTatol.length){//��ܵ���
						talkNum = 0;
						talkAbout = false;
						otherEvent = false;
						if(npcKind==12){
							callBattle("M49");
						}else if(npcKind==16){
							callBattle("M50");
						}else if(npcKind==30){
							callBattle("M47");
						}else if(npcKind==31){
							callBattle("M48");
						}
					}
				}
				if(talkAbout)
					storeG.fillTriangle(disWidth+161,disHeight+196,disWidth+168,disHeight+196,disWidth+164,disHeight+200);
			}else{//�p�Gnpc�S���ƥ��,�X�{�@����
				setTalkAbout(0);
				talkAbout(0);
				talkAbout = false;
				whoTalkAbout = false;
			}
				
			
		}
		
		//���ܭ����ɡA��Ϥ�
		public void changPage(){
			switch(page){
				case 0://��ܿ��
					if(!otherEvent){
						System.out.println("npcKind:"+npcKind);
						//��O�@��npc�Ϥ���
						if( npcKind==0 || npcKind==6){//�u��
							nowPeoImg = createImage("/image/gameInterface/npc/npc_0.png");
						}else if( npcKind==7 || npcKind==11 || npcKind==17 || npcKind==18 ){//�ǰe�v
							nowPeoImg = createImage("/image/gameInterface/npc/npc_7.png");
						}else if( npcKind==19 || npcKind==20 || npcKind==21 ){//�ѯ�
							nowPeoImg = createImage("/image/gameInterface/npc/npc_19.png");
						}else{//��npc�Ϥ���
							nowPeoImg = createImage("/image/gameInterface/npc/npc_"+ npcKind +".png");
						}
					}
					
					if(npcKind==2 || npcKind==5 || npcKind==8 || npcKind==9){
						for(int i=0;i<nowString.length;i++)
							nowString[i] = menuStr[i];
						menuNum = menuStr.length;//MENU �ƶq
						menustate = 0;
						moveY = 0;
					}else{
						if(npcKind==4 && RoleDataValue.nowEvenSchedule[0].indexOf("4")!=-1)
							nowString[2] = "��";
						else if(npcKind==0)
							nowString[2] = "���";
						else
							nowString[2] = "���";
						
						nowString[3] = "���}";
						menuNum = 2;
						menustate = 2;
						moveY = -20;
					}
					
					
					break;
				case 1://�R�F��
					for(int i=2;i<nowString.length;i++)
						nowString[i] = menuBuyStr[i-2];
					menuNum = menuBuyStr.length;
					menustate = 2;
					moveY = -20;
					if(!drawItemMenu){
						if(npcKind!=9){
							itemSelect=0;
							storeIndex=0;
							int n=0;
							String tmp[] = null;
							//�ө����~�q��Ʈw��X
							for(int i=1;i<=db.getDBCount(npcKind);i++){
								tmp = db.readDBRecord(npcKind,i);
								if(!tmp[dataFieldNum[npcKind]-1].equals("0")){
									itemBuff[n] = tmp;
									n++;
								}
							}
							itemTotal = n;
							for(int i=0;i<itemTotal;i++){
								img2[i] = createImage("/image/" + itemNameArray[npcKind] +"/"+ itemBuff[i][1] + ".png");
							}
							System.out.println("Total:"+itemTotal);
						}else{
							itemSelect=0;
							storeIndex=0;
							itemTotal=40;
							for(int i=0;i<itemOther.length;i++){
								itemBuff[i] = itemOther[i];
								img2[i] = imgOther[i];
							}
						}
					}
					drawItemMenu = true;
					break;
				case 2://��F��					
					if(!drawItemMenu || itemEvent == false){
						String itemStr = "";
						switch (npcKind) {
							case 2://��S��
								itemStr = "J";
								break;
							case 5://�樾��
								itemStr = "G";
								break;
							case 8://�注��
								itemStr = "P";
								break;
							case 9://��ɫ~
								itemStr = "B";
								break;
						}
						int n=0;
						String tmp[] = null;
						mainValue.setHashEnume();
						for(;RoleDataValue.enu.hasMoreElements();){
							tmp = (String[])RoleDataValue.enu.nextElement();
							//�p�G�s���j��n�j�M��Ʈw���ƶq�h���j�M
							if(tmp[0].indexOf(itemStr)==-1)
								continue;
							System.out.println("Find:"+tmp[0]);
							itemBuff[n] = db.readDBRecord(npcKind,
									Int( tmp[0].substring(1,tmp[0].length()) ));
							//itemAmount[n] = tmp[1];
							n++;
						}
						itemTotal = n;//�j�M��ŦX�����~�ƶq
						//img2 = new Image[itemTotal];
						for(int i=0;i<itemTotal;i++){
							img2[i] = createImage("/image/" + itemNameArray[npcKind] +"/"+ itemBuff[i][1] + ".png");
						}
						System.out.println("Total:"+itemTotal+"|"+page);
						drawItemMenu = true;
					}
					if(itemTotal==0){
						itemSelect=0;
						storeIndex=0;
		        		drawItemMenu = false;
		        		storeG.fillRect(0, 0, this.getWidth(), this.getHeight());
						makeWord.paint(storeG, 0, 0);
						flushGraphics();
						itemEvent = true;
		        		page=0;
		        		changPage();
					}else{
						for(int i=2;i<nowString.length;i++)
							nowString[i] = menuSellStr[i-2];
						menuNum = menuSellStr.length;
						menustate = 2;
						moveY = -20;
					}
					break;
				case 3://��ܪ��~
					break;
			}
			drawDisplay();
		}

		//	������
		private void changeMenu() {
			if(drawItemMenu){//�e���~��T�bitemMenu
				setFont(0);
				storeG.drawImage(buyItemImg,disWidth+3,disHeight+0,0);
				
				storeG.setColor(255,255,255);
				storeG.drawString(String.valueOf(RoleDataValue.money),disWidth+117,disHeight+13,0);//Show Money
				switch (itemSelect) {
				case 0:
					//Select----------------------------
					storeG.setColor(255, 255, 255);
					storeG.drawString(itemBuff[0+storeIndex][2], disWidth + 50, disHeight + 15, 0);
					storeG.drawImage(img2[0+storeIndex],disWidth+10,disHeight+6,0);
					showItemInf(0+storeIndex);
					//----------------------------------
					storeG.setColor(69, 118, 181);
					if(storeIndex+2<=itemTotal){//��u�ѤG�Ӫ��~�b���ɦL�X
						storeG.drawString(itemBuff[1+storeIndex][2], disWidth + 50, disHeight + 48, 0);
						storeG.drawImage(img2[1+storeIndex],disWidth+10,disHeight+39,0);
					}
					if(storeIndex+3<=itemTotal){//��u�ѤT�Ӫ��~�b���ɦL�X
						storeG.drawString(itemBuff[2+storeIndex][2], disWidth + 50, disHeight + 81, 0);
						storeG.drawImage(img2[2+storeIndex],disWidth+10,disHeight+72,0);
					}
					if(storeIndex+4<=itemTotal){//�|�Ӫ��~�b���ɦL�X
						storeG.drawString(itemBuff[3+storeIndex][2], disWidth + 50, disHeight + 114, 0);
						storeG.drawImage(img2[3+storeIndex],disWidth+10,disHeight+105,0);
					}
					break;
				case 1:
					storeG.setColor(69, 118, 181);
					storeG.drawString(itemBuff[0+storeIndex][2], disWidth + 50, disHeight + 15, 0);
					storeG.drawImage(img2[0+storeIndex]/*createImage("/image/Pole/"+itemBuff[0+storeIndex][0]+".png")*/,disWidth+10,disHeight+6,0);
					//Select----------------------------
					storeG.setColor(255, 255, 255);
					if(storeIndex+2<=itemTotal){
						storeG.drawString(itemBuff[1+storeIndex][2],disWidth + 50, disHeight + 48, 0);
						storeG.drawImage(img2[1+storeIndex]/*createImage("/image/Pole/"+itemBuff[1+storeIndex][0]+".png")*/,disWidth+10,disHeight+39,0);
						showItemInf(1+storeIndex);
					}
					//----------------------------------
					storeG.setColor(69, 118, 181);
					if(storeIndex+3<=itemTotal){
						storeG.drawString(itemBuff[2+storeIndex][2], disWidth + 50, disHeight + 81, 0);
						storeG.drawImage(img2[2+storeIndex]/*createImage("/image/Pole/"+itemBuff[2+storeIndex][0]+".png")*/,disWidth+10,disHeight+72,0);
					}
					if(storeIndex+4<=itemTotal){
						storeG.drawString(itemBuff[3+storeIndex][2], disWidth + 50, disHeight + 114, 0);
						storeG.drawImage(img2[3+storeIndex]/*createImage("/image/Pole/"+itemBuff[3+storeIndex][0]+".png")*/,disWidth+10,disHeight+105,0);
					}
					break;
				case 2:
					storeG.setColor(69, 118, 181);
					storeG.drawString(itemBuff[0+storeIndex][2], disWidth + 50, disHeight + 15, 0);
					storeG.drawImage(img2[0+storeIndex]/*createImage("/image/Pole/"+itemBuff[0+storeIndex][0]+".png")*/,disWidth+10,disHeight+6,0);
					storeG.setColor(69, 118, 181);
					if(storeIndex+2<=itemTotal){
						storeG.drawString(itemBuff[1+storeIndex][2], disWidth + 50, disHeight + 48, 0);
						storeG.drawImage(img2[1+storeIndex]/*createImage("/image/Pole/"+itemBuff[1+storeIndex][0]+".png")*/,disWidth+10,disHeight+39,0);
					}
					//Select----------------------------
					storeG.setColor(255, 255, 255);
					if(storeIndex+3<=itemTotal){
						storeG.drawString(itemBuff[2+storeIndex][2], disWidth + 50, disHeight + 81, 0);
						storeG.drawImage(img2[2+storeIndex]/*createImage("/image/Pole/"+itemBuff[2+storeIndex][0]+".png")*/,disWidth+10,disHeight+72,0);
						showItemInf(2+storeIndex);
					}
					//----------------------------------
					storeG.setColor(69, 118, 181);
					if(storeIndex+4<=itemTotal){
						storeG.drawString(itemBuff[3+storeIndex][2], disWidth + 50, disHeight + 114, 0);
						storeG.drawImage(img2[3+storeIndex]/*createImage("/image/Pole/"+itemBuff[3+storeIndex][0]+".png")*/,disWidth+10,disHeight+105,0);
					}
					break;
				case 3:
					storeG.setColor(69, 118, 181);
					storeG.drawString(itemBuff[0+storeIndex][2], disWidth + 50, disHeight + 15, 0);
					storeG.drawImage(img2[0+storeIndex]/*createImage("/image/Pole/"+itemBuff[0+storeIndex][0]+".png")*/,disWidth+10,disHeight+6,0);
					if(storeIndex+2<=itemTotal){
						storeG.drawString(itemBuff[1+storeIndex][2], disWidth + 50, disHeight + 48, 0);
						storeG.drawImage(img2[1+storeIndex]/*createImage("/image/Pole/"+itemBuff[1+storeIndex][0]+".png")*/,disWidth+10,disHeight+39,0);
					}
					if(storeIndex+3<=itemTotal){
						storeG.drawString(itemBuff[2+storeIndex][2], disWidth + 50, disHeight + 81, 0);
						storeG.drawImage(img2[2+storeIndex]/*createImage("/image/Pole/"+itemBuff[2+storeIndex][0]+".png")*/,disWidth+10,disHeight+72,0);
					}
					//Select----------------------------
					storeG.setColor(255, 255, 255);
					if(storeIndex+4<=itemTotal){
						storeG.drawString(itemBuff[3+storeIndex][2], disWidth + 50, disHeight + 114, 0);
						storeG.drawImage(img2[3+storeIndex]/*createImage("/image/Pole/"+itemBuff[3+storeIndex][0]+".png")*/,disWidth+10,disHeight+105,0);
						showItemInf(3+storeIndex);
					}
					//----------------------------------
					break;
				}
			}
			if(storeEvent==true && itemEvent==true){//�R���~�����
				setFont(0);
				switch (menustate) {
					case 0:
						//Select----------------------------
						storeG.setColor(255, 255, 255);
						storeG.drawString(nowString[0], disWidth + 130, disHeight + 79, 0);
						//----------------------------------
						storeG.setColor(69, 118, 181);
						storeG.drawString(nowString[1], disWidth + 130, disHeight + 94, 0);
						storeG.drawString(nowString[2], disWidth + 130, disHeight + 109, 0);
						storeG.drawString(nowString[3], disWidth + 130, disHeight + 124, 0);
						break;
					case 1:
						if(page==0 & (npcKind==2 || npcKind==5 || npcKind==8 || npcKind==9)){
							storeG.setColor(69, 118, 181);
							storeG.drawString(nowString[0], disWidth + 130, disHeight + 79, 0);
							//Select----------------------------
							storeG.setColor(255, 255, 255);
							storeG.drawString(nowString[1], disWidth + 130, disHeight + 94, 0);
							//----------------------------------
						}
						storeG.setColor(69, 118, 181);
						storeG.drawString(nowString[2], disWidth + 130, disHeight + 109, 0);
						storeG.drawString(nowString[3], disWidth + 130, disHeight + 124, 0);
						break;
					case 2:
						storeG.setColor(69, 118, 181);
						if(page==0 & (npcKind==2 || npcKind==5 || npcKind==8 || npcKind==9)){
							storeG.setColor(69, 118, 181);
							storeG.drawString(nowString[0], disWidth + 130, disHeight + 79, 0);
							storeG.setColor(69, 118, 181);
							storeG.drawString(nowString[1], disWidth + 130, disHeight + 94, 0);
						}
						//Select----------------------------
						storeG.setColor(255, 255, 255);
						storeG.drawString(nowString[2], disWidth + 130, disHeight + 109 + moveY, 0);
						//----------------------------------
						storeG.setColor(69, 118, 181);
						storeG.drawString(nowString[3], disWidth + 130, disHeight + 124 + moveY, 0);
						break;
					case 3:
						storeG.setColor(69, 118, 181);
						if(page==0 & (npcKind==2 || npcKind==5 || npcKind==8 || npcKind==9)){
							storeG.setColor(69, 118, 181);
							storeG.drawString(nowString[0], disWidth + 130, disHeight + 79, 0);
							storeG.drawString(nowString[1], disWidth + 130, disHeight + 94, 0);
						}
						storeG.drawString(nowString[2], disWidth + 130, disHeight + 109 + moveY, 0);
						//Select----------------------------
						storeG.setColor(255, 255, 255);
						storeG.drawString(nowString[3], disWidth + 130, disHeight + 124 + moveY, 0);
						//----------------------------------
						break;
					}
			}
			flushGraphics();
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
		
		//�r���]�w
		public void setFont(int size) {
			Font font = Font.getFont(Font.FACE_SYSTEM, 0, size);
			storeG.setFont(font);
		}
		
		public void showItemInf(int index){
			String proStr = "";
			storeG.setColor(255,255,255);
			storeG.fillRect(disWidth + 123, disHeight + 36,32,32);
			if(npcKind==8 && RoleDataValue.pole!=0){
				storeG.drawImage(RoleDataValue.poleImg,disWidth+123,disHeight+36,0);
			}else if(npcKind==2 && RoleDataValue.jewelry!=0){
				storeG.drawImage(RoleDataValue.jewelryImg,disWidth+123,disHeight+36,0);
			}else if(npcKind==9 && RoleDataValue.bait!=0){
				storeG.drawImage(RoleDataValue.baitImg,disWidth+123,disHeight+36,0);
			}else if(npcKind==5){
				if(itemBuff[index][8].equals("1") && RoleDataValue.proHead!=0){
					storeG.drawImage(RoleDataValue.proHeadImg,disWidth+123,disHeight+36,0);
					proStr = "�Y��";
				}
				else if(itemBuff[index][8].equals("2") && RoleDataValue.proBody!=0){
					storeG.drawImage(RoleDataValue.proBodyImg,disWidth+123,disHeight+36,0);
					proStr = "����";
				}
				else if(itemBuff[index][8].equals("3") && RoleDataValue.proWaist!=0){
					storeG.drawImage(RoleDataValue.proWaistImg,disWidth+123,disHeight+36,0);
					proStr = "�y��";
				}
				else if(itemBuff[index][8].equals("4") && RoleDataValue.proFoot!=0){
					storeG.drawImage(RoleDataValue.proFootImg,disWidth+123,disHeight+36,0);
					proStr = "�}��";
				}
				else if(itemBuff[index][8].equals("5") && RoleDataValue.proHand!=0){
					storeG.drawImage(RoleDataValue.proHandImg,disWidth+123,disHeight+36,0);
					proStr = "�ⳡ";
				}
			}
			storeG.drawImage(converImg,disWidth+0,disHeight+144,0);
			if(npcKind==8 || npcKind==2 || npcKind==5){
				storeG.drawString("����(ATK)+"+String.valueOf(getAtk(index)),disWidth + 8,disHeight + 147,0);
				storeG.drawString("���m(DEF)+"+String.valueOf(getDef(index)),disWidth + 8,disHeight + 161,0);
				if(npcKind==8 || npcKind==2)
					storeG.drawString( page==2?"����:"+Int(itemBuff[index][9])/2:"����:"+itemBuff[index][9] ,disWidth + 8,disHeight + 189,0);
				else if(npcKind==5){
					storeG.drawString("�˳Ʀ�m:"+proStr,disWidth + 8,disHeight + 175,0);
					storeG.drawString(page==2?"����:"+Int(itemBuff[index][7])/2:"����:"+itemBuff[index][7],disWidth + 8,disHeight + 189,0);
				}
				storeG.drawString("�ثe�ƶq:"+mainValue.getItemAmount(itemBuff[index][0]),disWidth + 105,disHeight + 189,0);
			}else if(npcKind==9){
				if(index<25)
					storeG.drawString("�f�t����:",disWidth + 8,disHeight + 147,0);
				else
					storeG.drawString("�W�[HP��:",disWidth + 8,disHeight + 147,0);
				storeG.drawString("�u"+itemBuff[index][3]+"�v",disWidth + 1,disHeight + 161,0);
				storeG.drawString(page==2?"����:"+Int(itemBuff[index][5])/2:"����:"+itemBuff[index][5],disWidth + 8,disHeight + 189,0);
				storeG.drawString("�ثe�ƶq:"+mainValue.getItemAmount(itemBuff[index][0]),disWidth + 105,disHeight + 189,0);
			}
		}
		
		//�]�w�ө������~
		public void setStoreValue(){
			String tmp[] = null;
			int n=0;
			//�ө����~�q��Ʈw��X
			for(int i=1;i<=db.getDBCount(9);i++){
				tmp = db.readDBRecord(9,i);
				if(!tmp[dataFieldNum[9]-1].equals("0")){
					itemOther[n] = tmp;
					n++;
				}
			}
			itemTotal = n;
			for(int i=0;i<itemTotal;i++){
				imgOther[i] = createImage("/image/" + itemNameArray[9] +"/"+ itemOther[i][1] + ".png");
			}
			//�⮷�~�D��[�J
			for(int i=1;i<=db.getDBCount(10);i++){
				tmp = db.readDBRecord(10,i);
				if(!tmp[5].equals("0")){
					itemOther[n] = tmp;
					n++;
				}
			}
			for(int i=itemTotal;i<n;i++){
				imgOther[i] = createImage("/image/" + itemNameArray[10] +"/"+ itemOther[i][1] + ".png");
			}
			itemTotal = n;
		}
		
		//����˳Ʋ֭p�������O
		public int getAtk(int num){
			int value = 0;
			if(npcKind==8){
				value = Int(itemBuff[num][3])+Int(itemBuff[num][6]);
			}else if(npcKind==2){
				value = Int(itemBuff[num][3])+Int(itemBuff[num][7]);
			}else if(npcKind==5){
				value = Int(itemBuff[num][5]);
			}
			return value;
		}
		
		//����˳Ʋ֭p�����m
		public int getDef(int num){
			int value = 0;
			if(npcKind==8){
				value = Int(itemBuff[num][5]);
			}else if(npcKind==2){
				value = Int(itemBuff[num][5])+Int(itemBuff[num][6]);
			}else if(npcKind==5){
				value = Int(itemBuff[num][3])+Int(itemBuff[num][4]);
			}
			return value;
		}
		
		//�ഫ�����
		public int Int(String data){
			return Integer.parseInt(data);
		}
		//----------------------------------------------------------------------------------------------
}