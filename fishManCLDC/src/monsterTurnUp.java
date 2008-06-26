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
import java.util.Random;
import java.lang.Math;

public class monsterTurnUp
{
	private Random monsterRandom;//�ü�
	private map mapImfor;//��map������
	
	private int randomValueX;//�x�s�üƮy��X
	private int randomValueY;//�x�s�üƮy��Y
	private int evenPoint;//�����y�{�s��
	
	private boolean monsterState;
	
	//***�L�޼ƫغc��***//
	public monsterTurnUp(map mapImfor, int evenPoint)
	{
		this.mapImfor = mapImfor;
		this.evenPoint = evenPoint;
		
		monsterRandom = new Random();
		
		monsterState = false;
	}
	
	//***�üƤ@�ӷs���y��***//
	public void runRandom()
	{
			randomValueX = (Math.abs(monsterRandom.nextInt()) % (mapImfor.getColumn(evenPoint)*16));
			randomValueY = (Math.abs(monsterRandom.nextInt()) % (mapImfor.getRow(evenPoint)*16));
	}
	
	//***�����Ǫ��O�_��H���y���I��(�p�G�üƮy�Цb�H�����϶������ܡA�|�^��true)***//
	public boolean monsterCollision(int mainX, int mainY)
	{
		if((randomValueX>mainX) & (randomValueX<(mainX+24))) this.monsterState = true;
		else if((randomValueY>mainY) & (randomValueY<(mainY+38))) this.monsterState = true;
		
		return this.monsterState;
	}
	
	//***�]�w����s��(�̨ƥ�y�{)***//
	public void setEvenPoint(int evenPoint)
	{
		this.evenPoint=evenPoint;
	}
}