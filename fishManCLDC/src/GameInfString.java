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
*      郭宗穎     負責工作：
* 					地圖程式設計
*			   		故事流程程式設計
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
public class GameInfString {
	public static String gameInfStr[][] = {
									  /*操作說明*/
									  {"各位玩家您好~~",
									   "以下是釣魚達人的基本操作指",
									   "令，要熟記指令才能更快對遊",
									   "戲上手喔!!^^",
									   "",
									   "按下手機上的 \" 1 \" 鍵是",
									   "「決定指令」。",
									   "按下手機上的 \" 2 \" 鍵是",
									   "「取消指令」。",
									   "按下手機上的 \" 3 \" 鍵是",
									   "「人物狀態指令」。",
									   "按下手機上的 ↑ 鍵是控制主",
									   "角往上走動。",
									   "按下手機上的 ↓ 鍵是控制主",
									   "角往下走動。",
									   "按下手機上的 ← 鍵是控制主",
									   "角往左走動。",
									   "按下手機上的 → 鍵是控制主",
									   "角往右走動。"},
									   
									  /*故事介紹*/
									  { "       原本平和無憂的魚國嘎利",
									   	"基大陸，突然發生了怪事，原",
										"本在海裡的魚突然爬上岸開始",
										"生活，並且具有攻擊性，開始",
										"自製各種武器，攻擊人類，而",
										"且部分突變的魚類也學會說簡",
										"單的人類語言，並成立自己",
										"的軍團，名為考機腿軍團。嘎",
										"利基大陸的人們為了保護自己",
										"，成立專門對付這些怪魚的軍",
										"隊、釣竿以及特殊的餌，並也",
										"開使著手調查這些魚突變的原",
										"因。"},										
									   
									   /*戰鬥介紹*/
									   {"◎ATB(紅色)：",
									    " ATB值滿時可使用指令選單。",
									    "◎XPB(黃色)：",
									    " 當XPB滿時，指令選單才可以",
										" 使用必殺技發動。",
									    "◎HP(綠色)：",
										" 顯示玩家目前血量。",
									    "◎怪物血量(藍色)：",
									    " 顯示怪物的目前的血量。",
									    "◎釣竿：",
										" 顯示主角所裝備的釣竿",
									    "◎釣餌：",
										" 顯示主角所裝備的釣餌",
									    "◎Combo：",
										" 顯示目前可以使用的必殺次數",
									    "◎使用物品：",
									    " 用於使用補品，可以讓玩家補",
										" 充HP值。",
									    "◎逃跑：",
										" 沒事可以逃跑",
									    "◎戰勝：",
										" 當怪物的血量為零時，就是戰",
									    " 勝了。",
									    "◎怪物對話：",
									    " 怪物的對話，可以增加遊戲樂",
										" 趣。",
									    "◎主角受到傷害：",
									    " 當玩家受到傷害時，畫面會閃",
										" 爍紅色。",
									    "◎怪物受到傷害：",
									    " 當怪物被ㄧ般攻擊砍到，會噴",
										" 出閃爍綠色"},
									   
									   /*其他說明*/
									   {"◎駐免前-旅館店：",
										" 提供玩家休息補充HP的地方。",
										"◎攪卡好-釣竿店：",
									    " 提供玩家最優質的釣竿和怪物",
										" 對抗喔。",
										"◎其爬貓-釣餌店：",
										" 提供的釣餌是種類最多最豐富",
										" 的。",
										"◎洗多寶-防具店：",
			 							" 提供玩家相當好品質的謢具。",
										"◎斯斯樂-特殊道具店：",
								        " 為玩家提供很多不ㄧ樣的東西",
									    " 喔。",
										"◎衣服：",
									    " 主要關係著玩家的防禦力值",
									    "◎手套：",
										" 手套是可以增強玩家的防禦",
									    "◎力值",
									    "◎下褲：",
										" 主要關係著玩家的防禦力值",
									    "◎鞋子：",
										" 鞋子是可以增強玩家的防禦",
									    "◎力值",
									    "◎飾品：",
										" 飾品是可以讓玩家增加附屬",
									    " 的功能",
									    "◎帽子：",
										" 有頂好帽子出去才不會被太",
									    " 陽曬",
										"◎釣竿：",
										" 遊戲裡面的特色，將釣竿當",
										" 作武器來使用",
										"◎釣餌：",
										" 遊戲裡面扮演施展必殺技次",
										" 數的重要物品",
										"◎一般道具：",
									    " 在遊戲中最容易看得到的物品",
										"。",
									    "◎補品道具：",
									    " 可以讓玩家在戰鬥時使用的救",
										" 命仙丹。",
									    "◎卷軸道具：",
									    " 在跟相關的ＮＰＣ人物對話後",
										" 得到的物品。",
									    "◎事件道具：",
									    " 玩家必須要去蒐集的東西。"},
										
										/*系統介紹*/
										{"          Version：1.0.1",
									     "◎製作團隊：",
									     "南台科技大學 資訊傳播系",
										 "樹德科技大學 資訊工程系",
										 "共同製作完成。",
										 "◎隊長：劉士達",
										 "  - 遊戲架構規劃",
										 "  - 場景設計",
										 "  - 美術設計",
										 "  - 系統整合",
										 "◎隊員：鍾易峻",
										 " - 遊戲介面程式設計",
										 " - 資料庫設計",
										 " - Server端程式",
										 " - 程式系統整合",
										 "◎隊員：郭宗穎",
										 " - 地圖程式設計",
										 " - 故事流程程式設計",
										 "◎隊員：黃肅純",
										 " - 美術設計",
										 " - 網路設計建構",
										 " - 周邊產品設計",
										 "◎隊員：蔡佳靜",
										 " - 美術製作",
										 " - 場景製作",
										 " - 周邊產品設計製作",
										 "◎隊員：劉宗學",
										 " - 遊戲流程設計",
										 " - 遊戲故事設計",
										 " - NPC設計",
										 " - 攻略本製作",
										 "◎隊員：莊耿瑜",
										 " - 行銷規劃",
										 " - 音效製作",
										 " - 遊戲場景製作除錯"},
										 /*必殺技*/
										 {"◎甩甩攻擊",
										  "1+2+3",
										  "◎劉連斬",
										  "3+2+1",
										  "◎媽媽的怒氣",
										  "2+2+3+1",
										  "◎爸爸的不爽",
										  "3+3+2+1",
										  "◎哇哇哇",
										  "1+2+3+2+1",
										  "◎勒勒勒",
										  "1+2+3+3+2",
										  "◎殺殺殺",
										  "1+2+3+1+3",
										  "◎火神斬",
										  "3+2+1+1+2+3",
										  "◎怒髮衝冠",
										  "1+2+3+3+2+1",
										  "◎密發動技",
										  "1+2+2+3+3+1+3",
										  "◎暴氣狂熱",
										  "2+2+3+3+1+1+3",
										  "◎女神之憐",
										  "3+3+2+2+1+1+3",
										  "◎千刀萬剮",
										  "1+2+1+1+1+1+3",
										  "◎神斬",
										  "3+2+1+3+2+1+3+3",
										  "◎無言",
										  "1+2+3+2+2+3+3+3",
										  "◎無可避免",
										  "2+2+2+2+1+1+1+1",
										  "◎電電樂",
										  "1+2+1+2+1+2+3+3+3",
										  "◎夜光棒",
										  "3+3+3+2+2+2+1+1+1",
										  "◎鬼殺擊",
										  "1+2+3+3+3+3+3+1+1+1",
										  "修羅技",
										  "2+2+1+2+3+1+2+1+3+1",
										  "無",
										  "1+1+1+1+1+1+1+1+1+1+1",
										  "超究連擊",
										  "3+2+3+2+1+1+3+2+1+2+3+2",
										  "密技特別發動",
										  "1+2+3+2+1+3+2+1+2+3+2+1+3",
										  "歐拉帝斯特",
										  "3+2+1+1+2+2+2+3+2+1+2+3+2+1"}};
	
	public GameInfString(){
		
	}
}
