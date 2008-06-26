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
	
	//�]�w�a�Ϯɪ�����
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
	
	//�]�w�Ұʮɪ�����
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
	
	//�����~��
	public void musicRun(){
		if(useMusic){
			try {
				backmusic.start();
			}catch (Exception ioe) {
				ioe.printStackTrace();
			}
		}
	}
	
	//���ּȰ�
	public void musicPause(){
		if(useMusic){
			try {
				backmusic.stop();
			}catch (Exception ioe) {
				ioe.printStackTrace();
			}
		}
	}
	
	//�԰����ֱҰ�
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
	
	//	�԰����ּȰ�
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
				if(n==0)//���U
					pushsound.start();
				else if(n==1)//���
					selectsound.start();
				else if(n==2)
					atksound.start();//�D������
				else if(n==3)
					monstersound.start();//�Ǫ��Q�ˮ`
				else if(n==4)
					upLevelsound.start();//�ɯ�
				else if(n==5)
					getItemsound.start();//���o���~
				else if(n==6)
					monsterdiesound.start();//�Ǫ����`
				else if(n==7)
					runawaysound.start();//�k�]
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
	
	//�]�wcombo�ޭ���
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
	
	//�]�w�Ǫ�����
	public void soundMonster(){
		if(useSound){
			msuicSound("D0"+MonsterDataValue.monsterKind,1);
		}
	}
	
	//���񭵮�,0��COMBO,1���Ǫ�atk����
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