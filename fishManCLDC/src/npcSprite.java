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

public class npcSprite
{
	private int evenPoint;//事件流程點，故事順序
	private Image npcImage;//NPC的顯示圖片
	private Sprite npcSprite[];//NPC顯示用Sprite
	private wordLayerCreate makeWord;//wordLayerCreate的指標索引
	private map mapImfor;//map的指標索引
	
	//無引數建構值
	public npcSprite(wordLayerCreate makeWord, map mapImfor)
	{
		setEvenPoint(0);
		this.makeWord = makeWord;
		this.mapImfor = mapImfor;
		npcSprite = new Sprite[7];
		
		try{
				npcImage = Image.createImage("/img/NPC.png");
				
			} catch(IOException ex)	{}
			
		makeNpcSprite();
	}
	
	//創造NPC物件
	private void makeNpcSprite()
	{
		//for(int i=0; i<mapImfor.getNpcAmount(this.evenPoint); i++)
		for(int i=0; i<npcSprite.length; i++)
		{
			npcSprite[i] = new Sprite(npcImage, 32, 32);
			npcSprite[i].setFrame(mapImfor.getNpcKind(this.evenPoint, i));
			makeWord.insert(npcSprite[i], 1);
		}
	}
	
	public void resetNpc(int evenPoint)
	{
		setEvenPoint(evenPoint);
		
		for(int i=0; i<mapImfor.getNpcAmount(this.evenPoint); i++)
		{
			//npcSprite[i] = new Sprite(npcImage, 32, 32);
			npcSprite[i].setFrame(mapImfor.getNpcKind(this.evenPoint, i));
			makeWord.insert(npcSprite[i], 1);
		}
		
	}
	
	//設定事件點
	public void setEvenPoint(int evenPoint)
	{
		this.evenPoint = evenPoint;
	}
	
	//***********設定NPC Sprite的位置**********//
	public void setNpcPosition()
	{
		for(int i=0; i<mapImfor.getNpcAmount(this.evenPoint); i++)
			npcSprite[i].setPosition(mapImfor.getNpcX(this.evenPoint, i), mapImfor.getNpcY(this.evenPoint, i));
	}
	
	//***********取得NPC的Sprite物件**********//
	public Sprite[] getNpcSprite()
	{
		return npcSprite;
	}
}