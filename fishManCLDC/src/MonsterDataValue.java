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
import java.util.Random;
import java.lang.Math;

public class MonsterDataValue {
	private static DataBaseReader dataR = new DataBaseReader();

	public static String name = "";//�Ǫ��W��

	public static int level = 0;//�Ǫ�����

	public static int exp = 0;//�Ǫ��g���

	public static int atk = 0;//�Ǫ������O

	public static int def = 0;//�Ǫ����m�O

	public static int nowHP = 0;//�ثeHP

	public static int maxHP = 0;//�̤jHP

	public static int nowXP = 0;//�ثeXP

	public static int money = 0;//��������

	public static int atb = 100;

	public static String[][] item = null;

	public static Image battleBackgroundImg = null;

	public static Image monsterImg = null;

	private static Random monsterRandom = null;

	private static int monsterRandomValue = 0;

	public static int monsterID = 0;
	
	public static int monsterKind = 0;

	public MonsterDataValue() {
		monsterRandom = new Random();
	}
	
	//�������w�Ǫ�
	public void createMonster(String monster){
		try {
			String monsterDataTmp[] = dataR.readDBRecord(7, Integer.parseInt(monster.substring(1,monster.length())));
			if(monster.equals("M47") || monster.equals("M48")){
				battleBackgroundImg = Image.createImage("/image/Battle/Battle_013.png");//�԰��e���I��
			}else if(monster.equals("M49")){
				battleBackgroundImg = Image.createImage("/image/Battle/Battle_003.png");//�԰��e���I��
			}else if(monster.equals("M50")){
				battleBackgroundImg = Image.createImage("/image/Battle/Battle_009.png");//�԰��e���I��
			}
			
			monsterImg = Image.createImage("/image/Monster/"
					+ monsterDataTmp[1] + ".png");//�Ǫ�
			monsterID = Integer.parseInt(monster.substring(1,monster.length()));
			name = monsterDataTmp[2];
			level = Integer.parseInt(monsterDataTmp[3]);
			monsterKind = Integer.parseInt(monsterDataTmp[4]);
			exp = Integer.parseInt(monsterDataTmp[6]);
			atk = Integer.parseInt(monsterDataTmp[7]);
			def = Integer.parseInt(monsterDataTmp[8]);
			nowHP = Integer.parseInt(monsterDataTmp[5]);
			maxHP = Integer.parseInt(monsterDataTmp[5]);
			money = Integer.parseInt(monsterDataTmp[9]);
			setAtbValue();
			//try{
			setItem(split(monsterDataTmp[10], ","));//�]�w�������~
			GameMain.music.soundMonster();//�]�w�Ǫ�����
			//}catch(Exception e){e.printStackTrace();}
			//System.out.println(monsterRandomValue);
		} catch (Exception e) {e.printStackTrace();}
	}
	
	//�üƲ��ͩǪ�
	public void createMonster(int mapNum) {
		try {
			String maptmp[] = dataR.readDBRecord(4, mapNum);
			String monsterRandTmp[] = split(maptmp[3], ",");
			monsterRandomValue = (Math.abs(monsterRandom.nextInt()))
					% monsterRandTmp.length;
			monsterID = Integer.parseInt(monsterRandTmp[monsterRandomValue]);
			String monsterDataTmp[] = dataR.readDBRecord(7, monsterID);
			battleBackgroundImg = Image.createImage("/image/Battle/"
					+ maptmp[1] + ".png");//�԰��e���I��
			monsterImg = Image.createImage("/image/Monster/"
					+ monsterDataTmp[1] + ".png");//�Ǫ�

			name = monsterDataTmp[2];
			level = Integer.parseInt(monsterDataTmp[3]);
			monsterKind = Integer.parseInt(monsterDataTmp[4]);
			exp = Integer.parseInt(monsterDataTmp[6]);
			atk = Integer.parseInt(monsterDataTmp[7]);
			def = Integer.parseInt(monsterDataTmp[8]);
			nowHP = Integer.parseInt(monsterDataTmp[5]);
			maxHP = Integer.parseInt(monsterDataTmp[5]);
			money = Integer.parseInt(monsterDataTmp[9]);
			setAtbValue();
			//try{
			setItem(split(monsterDataTmp[10], ","));//�]�w�������~
			GameMain.music.soundMonster();//�]�w�Ǫ�����
			//}catch(Exception e){e.printStackTrace();}
			//System.out.println(monsterRandomValue);
		} catch (Exception e) {}
	}

	//�]�w�������~
	public void setItem(String itemTmp[]) {
		//�p��|�����X�Ӫ��~
		int arraySize = itemTmp.length;
		if (arraySize == 0)
			arraySize = 1;
		else if (arraySize > 4)
			arraySize = 4;
		String[] item_ = new String[arraySize];
		System.out.println(arraySize + "|" + monsterID);
		for (int i = 0; i < arraySize; i++)
			item_[i] = "";
		int n = 0;
		int ranV = 0;
		boolean tt = false;
		//��Ǫ��|���������~��ibuff��~������
		while (true) {
			ranV = (Math.abs(monsterRandom.nextInt())) % itemTmp.length;
			for (int k = 0; k < arraySize; k++) {
				if (item_[k].equals(itemTmp[ranV])) {
					tt = true;
					break;
				} else
					tt = false;
			}
			if (tt)
				continue;
			item_[n] = itemTmp[ranV];
			n++;
			if (n == arraySize)
				break;
		}
		//���~�������v
		for (int i = 0; i < item_.length; i++) {
			if (item_[i].indexOf("I") != -1 || item_[i].indexOf("B") != -1
					|| item_[i].indexOf("T") != -1) {
				if (!((Math.abs(monsterRandom.nextInt())) % 2 == 0))
					item_[i] = "";
			}
			if (item_[i].indexOf("J") != -1 || item_[i].indexOf("G") != -1
					|| item_[i].indexOf("P") != -1) {
				if (!((Math.abs(monsterRandom.nextInt())) % 15 == 2))
					item_[i] = "";
			}
		}
		arraySize = 0;
		for (int i = 0; i < item_.length; i++)
			if (!item_[i].equals(""))
				arraySize++;

		try {
			item = new String[arraySize][];
			for (int i = 0, k = 0; i < item_.length; i++)
				if (!item_[i].equals("")) {
					item[k] = dataR.getNumData(item_[i]);
					k++;
				}
		} catch (Exception e) {
			e.printStackTrace();
		}
		/*
		 * for(int i=0;i <arraySize;i++) System.out.println(item[i]);
		 */
	}

	public String[] getMonsterCon() {
		String arrayTmp[] = null;
		try{
			int n = (Math.abs(monsterRandom.nextInt())) % 5 + 1;
			System.out.println(monsterID);
			String tmp = dataR.readDBField(13, monsterID, n);
			int line = tmp.length()/14;
			if(tmp.length()%14>0)
				line++;
			arrayTmp = new String[line];
			
			if(line>1){
				arrayTmp[0] = tmp.substring(0,14);
				arrayTmp[1] = tmp.substring(14,tmp.length());
			}else
				arrayTmp[0] = tmp.substring(0,tmp.length());
		}catch(Exception e){e.printStackTrace();}
		
		return arrayTmp;
	}

	//�]�watb��
	public void setAtbValue() {
		if (level <= 30)
			atb = 6500;
		else if (level >= 31 || level <= 70)
			atb = 5000;
		else if (level >= 71 || level <= 100)
			atb = 4500;
		else if (level >= 101 || level <= 140)
			atb = 3500;
		else if (level >= 141 || level <= 165)
			atb = 3000;
	}

	//�r�����
	public String[] split(String str, String regex) {
		int count = 0;
		int index = 0;

		for (int i = 0; i < str.length(); i++) {
			if (str.indexOf(regex, index) != -1) {
				count++;
				index = str.indexOf(regex, index) + 1;
			}
		}
		if (str.length() != index)
			count++;
		index = 0;
		String strsplit[] = new String[count];
		int indexarray[] = new int[count];
		for (int i = 0; i < count; i++) {
			if (str.indexOf(regex, index) != -1) {
				index = str.indexOf(regex, index) + 1;
				indexarray[i] = index;
			}
		}

		index = 0;
		for (int i = 0; i < count - 1; i++) {
			strsplit[i] = str.substring(index, indexarray[i] - 1);
			index = indexarray[i];
		}
		strsplit[count - 1] = str.substring(index, str.length());

		return strsplit;
	}
}