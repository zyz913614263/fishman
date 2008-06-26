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
import java.io.IOException;

/*************************************************************
** 	NPC 的Sprite 以及 寶箱的Sprite 以及 事件觸發的Sprite	**
*************************************************************/

public class npcGemSprite
{
	private int evenPoint;//事件流程點，故事順序
	private Image npcImage;//NPC的顯示圖片
	private Image gemImage;//寶箱的顯示圖片
	private Sprite npcSprite[];//NPC顯示用Sprite
	private Sprite gemSprite[];//顯示寶箱的Sprite
	private wordLayerCreate makeWord;//wordLayerCreate的指標索引
	private map mapImfor;//map的指標索引
	
	//無引數建構值
	public npcGemSprite(wordLayerCreate makeWord, map mapImfor, int evenPoint)
	{
		setEvenPoint(evenPoint);
		this.makeWord = makeWord;
		this.mapImfor = mapImfor;
		npcSprite = new Sprite[7];
		gemSprite = new Sprite[3];
		
		try{
				npcImage = Image.createImage("/img/NPC.png");
				gemImage = Image.createImage("/img/GEM.png");
				
			} catch(IOException ex)	{}
			
		for(int i=0; i<npcSprite.length; i++) npcSprite[i] = new Sprite(npcImage, 32, 32);
		for(int j=0; j<gemSprite.length; j++) gemSprite[j] = new Sprite(gemImage, 16, 16);
			
		//makeNpcSprite();
	}
	
	//創造NPC物件
	public void makeNpcSprite()
	{
		for(int i=0; i<mapImfor.getNpcAmount(this.evenPoint); i++)
		{
			npcSprite[i].setFrame(mapImfor.getNpcKind(this.evenPoint, i));
			npcSprite[i].setPosition(mapImfor.getNpcX(this.evenPoint, i), mapImfor.getNpcY(this.evenPoint, i));
			makeWord.insert(npcSprite[i], 1);
		}
	}
	
	//設定寶箱物件的座標以及插入物件至LayerManager
	public void makeGemSprite()
	{
			gemSprite[0].setPosition(mapImfor.getGemX(this.evenPoint, 0), mapImfor.getGemY(this.evenPoint, 0));
			makeWord.insert(gemSprite[0], 1);
	}
	
	//設定事件點
	public void setEvenPoint(int evenPoint)
	{
		this.evenPoint = evenPoint;
	}
	
	//***********取得NPC的Sprite物件**********//
	public Sprite[] getNpcSprite()
	{
		return npcSprite;
	}
	
	//***********取得寶箱的Sprite物件**********//
	public Sprite[] getGemSprite()
	{
		return gemSprite;
	}
}