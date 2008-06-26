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

import javax.microedition.media.Manager;
import javax.microedition.media.*;
import javax.microedition.media.MediaException;

class BackupMusic{
	private static Player backmusic = null;
	private static Player backsound = null ;
	private static Player selectsound = null ;
	private static Player pushsound = null ;
	private static Player battleMusic = null ;
	private static Player atksound = null ;
	private static Player monstersound = null ;
	private static Player upLevelsound = null ;
	private static Player getItemsound = null ;
	private static Player monsterdiesound = null ;
	private static Player runawaysound = null ;
	private static Player buysound = null ;
	private static Player monsterAtkSound = null ;
	private static Player comboSound = null ;
	private static String musicKind = "x-wav";
	private static String musicSecName = "wav";
	private boolean stopMusic = false;
	private boolean useMusic = true;
	private boolean useSound = true;
	
	public BackupMusic(){
		try{
			if(useMusic){
				battleMusic = Manager.createPlayer(getClass().getResourceAsStream("/music/music/music13."+musicSecName),"audio/"+musicKind);
				battleMusic.prefetch();
			}
			if(useSound){
				selectsound = Manager.createPlayer(getClass().getResourceAsStream("/music/sound/Sound_A01."+musicSecName),"audio/"+musicKind);
				selectsound.prefetch();
				pushsound = Manager.createPlayer(getClass().getResourceAsStream("/music/sound/Sound_A02."+musicSecName),"audio/"+musicKind);
				pushsound.prefetch();
				atksound = Manager.createPlayer(getClass().getResourceAsStream("/music/sound/Sound_C01."+musicSecName),"audio/"+musicKind);
				atksound.prefetch();
				monstersound = Manager.createPlayer(getClass().getResourceAsStream("/music/sound/Sound_C12."+musicSecName),"audio/"+musicKind);
				monstersound.prefetch();
				upLevelsound = Manager.createPlayer(getClass().getResourceAsStream("/music/sound/Sound_C14."+musicSecName),"audio/"+musicKind);
				upLevelsound.prefetch();
				getItemsound = Manager.createPlayer(getClass().getResourceAsStream("/music/sound/Sound_B01."+musicSecName),"audio/"+musicKind);
				getItemsound.prefetch();
				monsterdiesound = Manager.createPlayer(getClass().getResourceAsStream("/music/sound/Sound_C13."+musicSecName),"audio/"+musicKind);
				monsterdiesound.prefetch();
				runawaysound = Manager.createPlayer(getClass().getResourceAsStream("/music/sound/Sound_C11."+musicSecName),"audio/"+musicKind);
				runawaysound.prefetch();
				buysound  = Manager.createPlayer(getClass().getResourceAsStream("/music/sound/Sound_E04."+musicSecName),"audio/"+musicKind);
				buysound.prefetch();
			}
		}catch(Exception e){}
	}
	
	//設定地圖時的音樂
	public void musicStart(int map){
		if(useMusic){
			System.out.println("music loading..");
			map = map+1;
			String musicName = "";
			//System.out.println("map:"+map);
			try{
				backmusic.stop();
				backmusic.close();
				backmusic = null;
				Thread.sleep(500);
			}catch(Exception e){}
			try {
				if(map==1)
					musicName = "2";
				else if(map==37 || map==56)
					musicName = "3";
				else if(map==2 || map==3 || map==55 || map==4 || map==6 || map==7 || map==16)
					musicName = "4";
				else if(map==5 || map==18)
					musicName = "5";
				else if(map==14 || map==19 || map==5)
					musicName = "6";
				else if(map==26 || map==27)
					musicName = "7";
				else if(map>=20 && map<=25)
					musicName = "8";
				else if(map==42 || map==43)
					musicName = "9";
				else if(map==9 || map==8 || map==50 || map==51)
					musicName = "10";
				else if(map==10 || map==11 || map==12 || map==13 || map==44 || map==54)
					musicName = "11";
				else if(map>=45 && map<=50)
					musicName = "12";
				else if(map==17 || (map>=57 && map<=63))
					musicName = "18";
				else if(map==65 || map==66)
					musicName = "14";
				else if(map==40 || map==41)
					musicName = "15";
				else if(map==34 || map==35 || map==36 || map==38)
					musicName = "16";
				else if(map==32 || map==52)
					musicName = "17";
				else
					musicName = "6";
				
				backmusic = Manager.createPlayer(getClass().getResourceAsStream("/music/music/music"+musicName+"."+musicSecName),"audio/"+musicKind);
				
				backmusic.setLoopCount(-1);
				backmusic.start();
				System.out.println("music fin");
			} catch (MediaException pe) {
				pe.printStackTrace();
			} catch (Exception ioe) {
				ioe.printStackTrace();
			}
		}
	}
	
	//設定啟動時的音樂
	public void musicStart(String musicName){
		if(useMusic){
			try {
				backmusic = Manager.createPlayer(getClass().getResourceAsStream("/music/music/music"+musicName+"."+musicSecName),"audio/"+musicKind);
				//backmusic.realize();
				//backmusic.prefetch();
				//VolumeControl vc = (VolumeControl) backmusic.getControl("VolumeControl");
				//vc.setLevel(100);
				backmusic.setLoopCount(-1);
				backmusic.start();
			}catch (Exception ioe) {
				ioe.printStackTrace();
			}
		}
	}
	
	//音樂繼續
	public void musicRun(){
		if(useMusic){
			try {
				backmusic.start();
			}catch (Exception ioe) {
				ioe.printStackTrace();
			}
		}
	}
	
	//音樂暫停
	public void musicPause(){
		if(useMusic){
			try {
				backmusic.stop();
			}catch (Exception ioe) {
				ioe.printStackTrace();
			}
		}
	}
	
	//戰鬥音樂啟動
	public void battleMusicStart(){
		if(useMusic){
			try {
				battleMusic.setLoopCount(-1);
				battleMusic.start();
			}catch (Exception ioe) {
				ioe.printStackTrace();
			}
		}
	}
	
	//	戰鬥音樂暫停
	public void battleMusicStop(){
		if(useMusic){
			try {
				battleMusic.stop();
			}catch (Exception ioe) {
				ioe.printStackTrace();
			}
		}
	}
	
	public void msuicSound(int n){
		if(useSound){
			try {
				if(n==0)//按下
					pushsound.start();
				else if(n==1)//選擇
					selectsound.start();
				else if(n==2)
					atksound.start();//主角攻擊
				else if(n==3)
					monstersound.start();//怪物被傷害
				else if(n==4)
					upLevelsound.start();//升級
				else if(n==5)
					getItemsound.start();//取得物品
				else if(n==6)
					monsterdiesound.start();//怪物死亡
				else if(n==7)
					runawaysound.start();//逃跑
			}catch (Exception ioe) {
				//ioe.printStackTrace();
			}
		}
	}
	
	public void msuicSound(String soundName,int n){
		if(useSound){
			try {
				try{
					if(n==0)
						comboSound.close();
					else if(n==1)
						monsterAtkSound.close();
				}catch(Exception e){}
				
				if(n==0){
					comboSound = Manager.createPlayer(getClass().getResourceAsStream("/music/sound/Sound_"+soundName+"."+musicSecName),"audio/"+musicKind);
					comboSound.prefetch();
				}
				else if(n==1){
					monsterAtkSound = Manager.createPlayer(getClass().getResourceAsStream("/music/sound/Sound_"+soundName+"."+musicSecName),"audio/"+musicKind);
					monsterAtkSound.prefetch();
				}
				//backsound.realize();
				
				/*VolumeControl vc = (VolumeControl) backmusic.getControl("VolumeControl");
				vc.setLevel(100);*/
				//backsound.start();
			} catch (MediaException pe) {
				pe.printStackTrace();
			} catch (Exception ioe) {
				ioe.printStackTrace();
			}
		}
	}
	
	//設定combo技音效
	public void soundCom(){
		if(useSound){
			if(RoleDataValue.comboValue>=1 && RoleDataValue.comboValue<=3)
				msuicSound("C02",0);
			else if(RoleDataValue.comboValue==4)
				msuicSound("C03",0);
			else if(RoleDataValue.comboValue==5)
				msuicSound("C04",0);
			else if(RoleDataValue.comboValue>=6 && RoleDataValue.comboValue<=8)
				msuicSound("C05",0);
			else if(RoleDataValue.comboValue==9)
				msuicSound("C06",0);
			else if(RoleDataValue.comboValue==10)
				msuicSound("C07",0);
			else if(RoleDataValue.comboValue==11 || RoleDataValue.comboValue==12)
				msuicSound("C08",0);
		}
	}
	
	//設定怪物音效
	public void soundMonster(){
		if(useSound){
			msuicSound("D0"+MonsterDataValue.monsterKind,1);
		}
	}
	
	//播放音效,0為COMBO,1為怪物atk音效
	public void soundStart(int n){
		try{
			if(n==0)
				comboSound.start();
			else if(n==1)
				monsterAtkSound.start();
		}catch(Exception e){e.printStackTrace();}
	}
	
	public void stop(){
		try{
			if(useMusic){
				backmusic.stop();
				backmusic.close();
			}
		}catch (MediaException pe) {
			pe.printStackTrace();
		} 
	}
}