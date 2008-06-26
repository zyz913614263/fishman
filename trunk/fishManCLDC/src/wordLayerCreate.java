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
import javax.microedition.lcdui.game.*;
import javax.microedition.lcdui.Image;
import java.io.*;

public class wordLayerCreate extends LayerManager
{
	//*******實作TiledLayer*******//
	private TiledLayer moveBackLayer;//可行走之地圖
	private TiledLayer cantMoveLayer;//不可行走之地圖
	
	//*******實作Sprite***********//
	private Sprite mainSprite;//主角的Sprite
	private Sprite checkSprite;//偵測碰撞用Sprite
	private Sprite npcSprite[];//NPC顯示用Sprite
	
	//*******實作map.class********//
	static map mapImfor;
	
	//*******RoleDataValue 指標*******//
	private RoleDataValue RoleDataValue;
	
	//*******讀取圖片檔*******//
	private Image mapImage;//地圖圖	片元素
	private Image mainImage[];//人物圖片元素
	private Image checkImage;//偵測碰撞用圖片元素
	private Image linkImage[];//上下左右傳點碰撞圖片
	//private Image npcImage;//NPC的顯示圖片
	
	//*******地圖陣列*********//
	private int map[];
	private int cantMoveMap[];
	
	//*******view Windows位置*******//
	private int viewTargetX;
	private int viewTargetY;
	
	//*******地圖長度********//
	private int mapLength;
	
	//*******設定劇情位置(依故事順序)*******//
	private int evenPoint;
	private int oldPoint;
	
	//*******建構值*******//
	public wordLayerCreate(RoleDataValue RoleDataValue, int evenPoint)
	{
		this.RoleDataValue = RoleDataValue;
		//setEvenPoint(RoleDataValue.roleMap);
		this.evenPoint = evenPoint;
		
		//*************初始化**********//
		mainImage = new Image[4];
		linkImage = new Image[4];
		npcSprite = new Sprite[21];
		
		mapImfor = new map();
		
		//********初始化陣列*******//
		map = new int[1287];
		cantMoveMap = new int[1287];
		setArray();
		
		//*************讀入圖片************//
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
		
		//***畫出人物及背景***//
		makeMainSprite();
		makeBackView();
		
		//***畫出人物的碰撞圖***//
		makeCheckSprite();
		
	}
	
	//******初始背景(拼湊TiledLayer和放入LayerManager)
	private void makeBackView()//********受evenPoint影響的Method()********//
	{	
		setMapLength();//********計算地圖陣列長度
		
	
		//*******初始TiledLayer*******//
		//moveBackLayer = new TiledLayer(mapImfor.getColumn(this.evenPoint), mapImfor.getRow(this.evenPoint), mapImage, 16, 16);
		//cantMoveLayer = new TiledLayer(mapImfor.getColumn(this.evenPoint), mapImfor.getRow(this.evenPoint), mapImage, 16, 16);
		
		moveBackLayer = new TiledLayer(39, 39, mapImage, 16, 16);
		cantMoveLayer = new TiledLayer(39, 39, mapImage, 16, 16);
		
		//*******遇到-1值時所對應到的圖片*******//
		//***********結界圖素：NO.125***********//
		
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
	
	//**********主角Sprite**********//
	private void makeMainSprite()
	{
		mainSprite = new Sprite(mainImage[1], 24, 38);
		this.append(mainSprite);	
	}
	
	//**********偵測碰撞Sprite*********//
	private void makeCheckSprite()
	{
		checkSprite = new Sprite(checkImage, 36, 53);
		this.append(checkSprite);
	}
	
	//**********NPC用Sprite**********//
	/*private void makeNpcSprite()
	{
		for(int i=0; i<mapImfor.getNpcAmount(this.evenPoint); i++)
		{
			npcSprite[i] = new Sprite(npcImage, 32, 32);
			npcSprite[i].setFrame(mapImfor.getNpcKind(this.evenPoint, i));
			this.append(npcSprite[i]);
		}
	}*/
	
	//*********釋放NPC用Sprite*********//
	public void cancleNpcSprite()
	{
		for(int i=0; i<mapImfor.getNpcAmount(this.evenPoint); i++)
		{
			npcSprite[i]=null;
		}
	}
	
	//*******setCell()*******//
	public void backSetCell()//********受evenPoint影響的Method()********//
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
	
	//*******把背景全部清空*******//
	public void setCellBlack()//********受evenPoint影響的Method()********//
	{
		for(int i=0; i<mapLength; i++)
		{
			int column = i % mapImfor.getColumn(oldPoint);
			int row = (i - column) / mapImfor.getColumn(oldPoint);
			
			moveBackLayer.setCell(column, row, 0);
			cantMoveLayer.setCell(column, row, 0);
			
		}
	}
	
	//********設定故事點********//
	public void setEvenPoint(int evenPoint)
	{
		this.evenPoint = evenPoint;
	}
	
	//**********設定主角Sprite的位置********//
	public void setMainPosition(int x, int y)
	{
		mainSprite.setPosition(x, y);
	}
	
	//**********設定碰撞Sprite的位置*********//
	public void setCheckPosition(int x, int y)
	{
		checkSprite.setPosition(x, y);
	}
	
	//***********設定NPC Sprite的位置**********//
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
	
	//**********設定View Window位置********//
	public void setView(int w, int x, int y, int z)
	{
		this.setViewWindow(w, x, y, z);
	}
	
	//********設定地圖長度********//
	public void setMapLength()//********受evenPoint影響的Method()********//
	{
		mapLength = mapImfor.getColumn(this.evenPoint)*mapImfor.getRow(this.evenPoint);
		//mapLength = mapImfor.getMapLangth(this.evenPoint);
	}
	
	//********設定主角圖片(上、下、左、右的圖片)********//
	public void setImage(int num)
	{
		mainSprite.setImage(mainImage[num], 24, 38);
	}
	
	public void setCheckImage()
	{
		checkSprite.setImage(checkImage, 36, 53);
	}

	//********設定偵測碰撞圖片位置*******//
	public void setCollisionArea(int w, int x, int y, int z)
	{
		checkSprite.defineCollisionRectangle(w, x, y, z);
	}
	
	//********設定負號值的圖片*******//
	public void setAnimated(int x, int y)
	{
		moveBackLayer.setAnimatedTile(x, y);
	}
	
	//*******從 "*.map" 檔取得地圖陣列********//
	private void setArray()//********受evenPoint影響的Method()********//
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
	
	//********回傳map class********//
	public map getMapClass()
	{
		return mapImfor;
	}
	
	//********回傳主角Sprite********//
	public Sprite getMainSprite()
	{
		return mainSprite;
	}
	
	//********回傳碰撞Sprite********//
	public Sprite getCheckSprite()
	{
		return checkSprite;
	}
	
	//********回傳布可行走的TiledLayer*******//
	public TiledLayer getCantMoveTiled()
	{
		return cantMoveLayer;
	}
	
	//********回傳Cell寬********//
	public int getCellWidth()
	{
		return moveBackLayer.getCellWidth();
	}
	
	//********回傳Cell高********//
	public int getCellHeight()
	{
		return moveBackLayer.getCellHeight();
	}
	
	//重畫地圖
	public void resetMap(int deliver, int evenPoint)
	{
		oldPoint = evenPoint;
		setCellBlack();
		
		//***new map***//
		this.evenPoint = mapImfor.getLink(deliver, oldPoint);
		setMapLength();//設定new mapLength
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
	
	//跳躍到下個地圖
	public void jumpMap(int evenPoint)
	{
		setCellBlack();
		
		//***new map***//
		this.evenPoint = evenPoint;
		setMapLength();//設定new mapLength
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