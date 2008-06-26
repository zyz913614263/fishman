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

/*************************************************************
** 	NPC ��Sprite �H�� �_�c��Sprite �H�� �ƥ�Ĳ�o��Sprite	**
*************************************************************/

public class npcGemSprite
{
	private int evenPoint;//�ƥ�y�{�I�A�G�ƶ���
	private Image npcImage;//NPC����ܹϤ�
	private Image gemImage;//�_�c����ܹϤ�
	private Sprite npcSprite[];//NPC��ܥ�Sprite
	private Sprite gemSprite[];//����_�c��Sprite
	private wordLayerCreate makeWord;//wordLayerCreate�����Я���
	private map mapImfor;//map�����Я���
	
	//�L�޼ƫغc��
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
	
	//�гyNPC����
	public void makeNpcSprite()
	{
		for(int i=0; i<mapImfor.getNpcAmount(this.evenPoint); i++)
		{
			npcSprite[i].setFrame(mapImfor.getNpcKind(this.evenPoint, i));
			npcSprite[i].setPosition(mapImfor.getNpcX(this.evenPoint, i), mapImfor.getNpcY(this.evenPoint, i));
			makeWord.insert(npcSprite[i], 1);
		}
	}
	
	//�]�w�_�c���󪺮y�ХH�δ��J�����LayerManager
	public void makeGemSprite()
	{
			gemSprite[0].setPosition(mapImfor.getGemX(this.evenPoint, 0), mapImfor.getGemY(this.evenPoint, 0));
			makeWord.insert(gemSprite[0], 1);
	}
	
	//�]�w�ƥ��I
	public void setEvenPoint(int evenPoint)
	{
		this.evenPoint = evenPoint;
	}
	
	//***********���oNPC��Sprite����**********//
	public Sprite[] getNpcSprite()
	{
		return npcSprite;
	}
	
	//***********���o�_�c��Sprite����**********//
	public Sprite[] getGemSprite()
	{
		return gemSprite;
	}
}