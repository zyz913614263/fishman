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
import javax.microedition.lcdui.Image;
import java.io.*;

public class wordLayerCreate extends LayerManager
{
	//*******��@TiledLayer*******//
	private TiledLayer moveBackLayer;//�i�樫���a��
	private TiledLayer cantMoveLayer;//���i�樫���a��
	
	//*******��@Sprite***********//
	private Sprite mainSprite;//�D����Sprite
	private Sprite checkSprite;//�����I����Sprite
	private Sprite npcSprite[];//NPC��ܥ�Sprite
	
	//*******��@map.class********//
	static map mapImfor;
	
	//*******RoleDataValue ����*******//
	private RoleDataValue RoleDataValue;
	
	//*******Ū���Ϥ���*******//
	private Image mapImage;//�a�Ϲ�	������
	private Image mainImage[];//�H���Ϥ�����
	private Image checkImage;//�����I���ιϤ�����
	private Image linkImage[];//�W�U���k���I�I���Ϥ�
	//private Image npcImage;//NPC����ܹϤ�
	
	//*******�a�ϰ}�C*********//
	private int map[];
	private int cantMoveMap[];
	
	//*******view Windows��m*******//
	private int viewTargetX;
	private int viewTargetY;
	
	//*******�a�Ϫ���********//
	private int mapLength;
	
	//*******�]�w�@����m(�̬G�ƶ���)*******//
	private int evenPoint;
	private int oldPoint;
	
	//*******�غc��*******//
	public wordLayerCreate(RoleDataValue RoleDataValue, int evenPoint)
	{
		this.RoleDataValue = RoleDataValue;
		//setEvenPoint(RoleDataValue.roleMap);
		this.evenPoint = evenPoint;
		
		//*************��l��**********//
		mainImage = new Image[4];
		linkImage = new Image[4];
		npcSprite = new Sprite[21];
		
		mapImfor = new map();
		
		//********��l�ư}�C*******//
		map = new int[1287];
		cantMoveMap = new int[1287];
		setArray();
		
		//*************Ū�J�Ϥ�************//
		try	{
		mapImage = Image.createImage("/img/MainMap.png");
		mainImage[0] = Image.createImage("/img/MainUp.png");
		mainImage[1] = Image.createImage("/img/MainDown.png");
		mainImage[2] = Image.createImage("/img/MainLeft.png");
		mainImage[3] = Image.createImage("/img/MainRight.png");
		checkImage = Image.createImage("/img/Collision.png");
		linkImage[0] = Image.createImage("/img/LinkUp.png");
		linkImage[1] = Image.createImage("/img/LinkDown.png");
		linkImage[2] = Image.createImage("/img/LinkLeft.png");
		linkImage[3] = Image.createImage("/img/LinkRight.png");
		//npcImage = Image.createImage("/NPC.png");
		} catch(IOException ex)	{}
		
		//***�e�X�H���έI��***//
		makeMainSprite();
		makeBackView();
		
		//***�e�X�H�����I����***//
		makeCheckSprite();
		
	}
	
	//******��l�I��(����TiledLayer�M��JLayerManager)
	private void makeBackView()//********��evenPoint�v�T��Method()********//
	{	
		setMapLength();//********�p��a�ϰ}�C����
		
	
		//*******��lTiledLayer*******//
		//moveBackLayer = new TiledLayer(mapImfor.getColumn(this.evenPoint), mapImfor.getRow(this.evenPoint), mapImage, 16, 16);
		//cantMoveLayer = new TiledLayer(mapImfor.getColumn(this.evenPoint), mapImfor.getRow(this.evenPoint), mapImage, 16, 16);
		
		moveBackLayer = new TiledLayer(39, 39, mapImage, 16, 16);
		cantMoveLayer = new TiledLayer(39, 39, mapImage, 16, 16);
		
		//*******�J��-1�Ȯɩҹ����쪺�Ϥ�*******//
		//***********���ɹϯ��GNO.125***********//
		
		for(int i=0; i<4; i++)
		{
			if(mapImfor.getStop(i, this.evenPoint))
			{	
				if((mapImfor.getDBStop(i, this.evenPoint).equals("1")))
					moveBackLayer.createAnimatedTile(mapImfor.getStopImage(this.evenPoint));
				else
					moveBackLayer.createAnimatedTile(mapImfor.getStartImage(this.evenPoint));
					
			}
			else
					moveBackLayer.createAnimatedTile(125);					
		}
		
		//moveBackLayer.createAnimatedTile(125);
		//cantMoveLayer.createAnimatedTile(125);
		
		//*******setCell()*******//
		backSetCell();
		
		this.append(cantMoveLayer);
		this.append(moveBackLayer);
	}
	
	//**********�D��Sprite**********//
	private void makeMainSprite()
	{
		mainSprite = new Sprite(mainImage[1], 24, 38);
		this.append(mainSprite);	
	}
	
	//**********�����I��Sprite*********//
	private void makeCheckSprite()
	{
		checkSprite = new Sprite(checkImage, 36, 53);
		this.append(checkSprite);
	}
	
	//**********NPC��Sprite**********//
	/*private void makeNpcSprite()
	{
		for(int i=0; i<mapImfor.getNpcAmount(this.evenPoint); i++)
		{
			npcSprite[i] = new Sprite(npcImage, 32, 32);
			npcSprite[i].setFrame(mapImfor.getNpcKind(this.evenPoint, i));
			this.append(npcSprite[i]);
		}
	}*/
	
	//*********����NPC��Sprite*********//
	public void cancleNpcSprite()
	{
		for(int i=0; i<mapImfor.getNpcAmount(this.evenPoint); i++)
		{
			npcSprite[i]=null;
		}
	}
	
	//*******setCell()*******//
	public void backSetCell()//********��evenPoint�v�T��Method()********//
	{
		for(int i=0; i<mapLength; i++)
		{
			int column = i % mapImfor.getColumn(evenPoint);
			int row = (i - column) / mapImfor.getColumn(evenPoint);
			
			//if(this.evenPoint == 43) System.out.println("length = " + i);
			//if(this.evenPoint == 43) System.out.println("mapLength column = " + column);
			//if(this.evenPoint == 43) System.out.println("mapLength row = " + row);
			//if(this.evenPoint == 43) System.out.println("Map = " + map[i]);
			//if(this.evenPoint == 43) System.out.println("CantMoveMap = " + cantMoveMap[i]);
			//if(this.evenPoint == 43) System.out.println();
			
			moveBackLayer.setCell(column, row, map[i]);
			cantMoveLayer.setCell(column, row, cantMoveMap[i]);
			
		}
	}
	
	//*******��I�������M��*******//
	public void setCellBlack()//********��evenPoint�v�T��Method()********//
	{
		for(int i=0; i<mapLength; i++)
		{
			int column = i % mapImfor.getColumn(oldPoint);
			int row = (i - column) / mapImfor.getColumn(oldPoint);
			
			moveBackLayer.setCell(column, row, 0);
			cantMoveLayer.setCell(column, row, 0);
			
		}
	}
	
	//********�]�w�G���I********//
	public void setEvenPoint(int evenPoint)
	{
		this.evenPoint = evenPoint;
	}
	
	//**********�]�w�D��Sprite����m********//
	public void setMainPosition(int x, int y)
	{
		mainSprite.setPosition(x, y);
	}
	
	//**********�]�w�I��Sprite����m*********//
	public void setCheckPosition(int x, int y)
	{
		checkSprite.setPosition(x, y);
	}
	
	//***********�]�wNPC Sprite����m**********//
	//public void setNpcPosition(int num, int x, int y)
	//{
	//	npcSprite[num].setPosition(x, y);
	//}
	
	//**********Set Sprite Move*********//
	public void spriteMove(int x, int y)
	{
		mainSprite.move(x, y);
	}
	
	//**********Set Check Sprite Move*********//
	public void checkSpriteMove(int x, int y)
	{
		checkSprite.move(x, y);
	}
	
	//**********�]�wView Window��m********//
	public void setView(int w, int x, int y, int z)
	{
		this.setViewWindow(w, x, y, z);
	}
	
	//********�]�w�a�Ϫ���********//
	public void setMapLength()//********��evenPoint�v�T��Method()********//
	{
		mapLength = mapImfor.getColumn(this.evenPoint)*mapImfor.getRow(this.evenPoint);
		//mapLength = mapImfor.getMapLangth(this.evenPoint);
	}
	
	//********�]�w�D���Ϥ�(�W�B�U�B���B�k���Ϥ�)********//
	public void setImage(int num)
	{
		mainSprite.setImage(mainImage[num], 24, 38);
	}
	
	public void setCheckImage()
	{
		checkSprite.setImage(checkImage, 36, 53);
	}

	//********�]�w�����I���Ϥ���m*******//
	public void setCollisionArea(int w, int x, int y, int z)
	{
		checkSprite.defineCollisionRectangle(w, x, y, z);
	}
	
	//********�]�w�t���Ȫ��Ϥ�*******//
	public void setAnimated(int x, int y)
	{
		moveBackLayer.setAnimatedTile(x, y);
	}
	
	//*******�q "*.map" �ɨ��o�a�ϰ}�C********//
	private void setArray()//********��evenPoint�v�T��Method()********//
	{
		int point = 0;
		
		try{
    		InputStream inp = getClass().getResourceAsStream("/map/"+ mapImfor.getMapName(evenPoint) +".map");
    		InputStream inp2 = getClass().getResourceAsStream("/map/"+ mapImfor.getCantMoveMapName(evenPoint) +".map");
    		DataInputStream data = new DataInputStream(inp);
    		DataInputStream data2 = new DataInputStream(inp2);
    		
    		while(true){
    			map[point] = data.readInt();
    			cantMoveMap[point] = data2.readInt();
    			point++;
    		}
    	}
    	catch(EOFException e){}
    	catch(Exception e2){e2.printStackTrace();}	
	}
	
	//********�^��map class********//
	public map getMapClass()
	{
		return mapImfor;
	}
	
	//********�^�ǥD��Sprite********//
	public Sprite getMainSprite()
	{
		return mainSprite;
	}
	
	//********�^�ǸI��Sprite********//
	public Sprite getCheckSprite()
	{
		return checkSprite;
	}
	
	//********�^�ǥ��i�樫��TiledLayer*******//
	public TiledLayer getCantMoveTiled()
	{
		return cantMoveLayer;
	}
	
	//********�^��Cell�e********//
	public int getCellWidth()
	{
		return moveBackLayer.getCellWidth();
	}
	
	//********�^��Cell��********//
	public int getCellHeight()
	{
		return moveBackLayer.getCellHeight();
	}
	
	//���e�a��
	public void resetMap(int deliver, int evenPoint)
	{
		oldPoint = evenPoint;
		setCellBlack();
		
		//***new map***//
		this.evenPoint = mapImfor.getLink(deliver, oldPoint);
		setMapLength();//�]�wnew mapLength
		setArray();
		backSetCell();
		
		for(int i=0; i<4; i++)
		{
			if(mapImfor.getStop(i, this.evenPoint))
			{
				if((mapImfor.getDBStop(i, this.evenPoint).equals("1")))
				{
					//if(mapImfor.getDBStop(i, this.evenPoint).equals("1"))
						setAnimated(-(i+1), mapImfor.getStopImage(this.evenPoint));
					//else
						//setAnimated(-(i+1), mapImfor.getStartImage(this.evenPoint));
				}
				else
					setAnimated(-(i+1), mapImfor.getStartImage(this.evenPoint));
					
			}					
		}
	}
	
	//���D��U�Ӧa��
	public void jumpMap(int evenPoint)
	{
		setCellBlack();
		
		//***new map***//
		this.evenPoint = evenPoint;
		setMapLength();//�]�wnew mapLength
		setArray();
		backSetCell();
		
		for(int i=0; i<4; i++)
		{
			if(mapImfor.getStop(i, this.evenPoint))
			{
				if((mapImfor.getDBStop(i, this.evenPoint).equals("1")))
				{
					//if((mapImfor.getDBStop(i, this.evenPoint).equals("1")))
						setAnimated(-(i+1), mapImfor.getStopImage(this.evenPoint));
					//else
						//setAnimated(-(i+1), mapImfor.getStartImage(this.evenPoint));
				}
				else
					setAnimated(-(i+1), mapImfor.getStartImage(this.evenPoint));
					
			}					
		}
	}
	
}