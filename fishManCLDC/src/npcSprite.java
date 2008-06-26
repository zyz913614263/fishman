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
import java.io.IOException;

public class npcSprite
{
	private int evenPoint;//�ƥ�y�{�I�A�G�ƶ���
	private Image npcImage;//NPC����ܹϤ�
	private Sprite npcSprite[];//NPC��ܥ�Sprite
	private wordLayerCreate makeWord;//wordLayerCreate�����Я���
	private map mapImfor;//map�����Я���
	
	//�L�޼ƫغc��
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
	
	//�гyNPC����
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
	
	//�]�w�ƥ��I
	public void setEvenPoint(int evenPoint)
	{
		this.evenPoint = evenPoint;
	}
	
	//***********�]�wNPC Sprite����m**********//
	public void setNpcPosition()
	{
		for(int i=0; i<mapImfor.getNpcAmount(this.evenPoint); i++)
			npcSprite[i].setPosition(mapImfor.getNpcX(this.evenPoint, i), mapImfor.getNpcY(this.evenPoint, i));
	}
	
	//***********���oNPC��Sprite����**********//
	public Sprite[] getNpcSprite()
	{
		return npcSprite;
	}
}