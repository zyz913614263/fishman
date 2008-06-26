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
import java.util.Random;
import java.lang.Math;

public class monsterTurnUp
{
	private Random monsterRandom;//亂數
	private map mapImfor;//接map的指標
	
	private int randomValueX;//儲存亂數座標X
	private int randomValueY;//儲存亂數座標Y
	private int evenPoint;//城鎮的流程編號
	
	private boolean monsterState;
	
	//***無引數建構值***//
	public monsterTurnUp(map mapImfor, int evenPoint)
	{
		this.mapImfor = mapImfor;
		this.evenPoint = evenPoint;
		
		monsterRandom = new Random();
		
		monsterState = false;
	}
	
	//***亂數一個新的座標***//
	public void runRandom()
	{
			randomValueX = (Math.abs(monsterRandom.nextInt()) % (mapImfor.getColumn(evenPoint)*16));
			randomValueY = (Math.abs(monsterRandom.nextInt()) % (mapImfor.getRow(evenPoint)*16));
	}
	
	//***偵測怪物是否跟人物造成碰撞(如果亂數座標在人物的區塊內的話，會回傳true)***//
	public boolean monsterCollision(int mainX, int mainY)
	{
		if((randomValueX>mainX) & (randomValueX<(mainX+24))) this.monsterState = true;
		else if((randomValueY>mainY) & (randomValueY<(mainY+38))) this.monsterState = true;
		
		return this.monsterState;
	}
	
	//***設定城鎮編號(依事件流程)***//
	public void setEvenPoint(int evenPoint)
	{
		this.evenPoint=evenPoint;
	}
}