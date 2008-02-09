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
public class storyEven
{
	private int evenPoint;//�]�w�����m(�̬G�ƶ���)
	private int evenSchedule[];//�x�s�ƥ�Ѱg�i��
	private String evenBuffer;//�Ȧs�ƥ�i��
	private RoleDataValue mainValue;//�D�����U�ت��A��
	private map mapImfor;//�a�ϸ�T������
	private wordLayerCreate makeWord;
	private mapCanvars myMapCanvars;
	
	private int talkAbout = 1;
	private boolean stop[];
	private boolean talkLevel = false;
	
	public storyEven(mapCanvars myMapCanvars, RoleDataValue mainValue, map mapImfor, wordLayerCreate makeWord, int evenPoint)
	{
		this.mainValue = mainValue;
		this.mapImfor = mapImfor;
		this.makeWord = makeWord;
		this.myMapCanvars = myMapCanvars;
		setEvenPoint(evenPoint);
		
		//stop = new boolean[4];//���o�O�_������
		//for(int i=0; i<4; i++) stop[i] = mapImfor.getDBStop(i ,this.evenPoint);
	}
	
	public void doEven(int npcNum)
	{
		System.out.println("this.evenPoint = " + this.evenPoint);
		switch (this.evenPoint)
		{
				//��0�ӫ���
				case 0:
						{
									evenBuffer = RoleDataValue.nowEvenSchedule[0];//Ū���ثe�ƥ�i��(��Ʈw)
									System.out.println("RoleDataValue.nowEvenSchedule[0] = "+RoleDataValue.nowEvenSchedule[0]);
									if(! evenBuffer.equals("")) evenSchedule = split(evenBuffer, ",");
				
							switch(npcNum)
							{
								//�ƥ�@
								case 0:
										{	
											if(checkEvenSchedul(npcNum) == 2)
											{
												setTalkValue(4);
												
												System.out.println("even  "+npcNum+" : "+evenBuffer);
												
												break;
											}
											
											if(checkEvenSchedul(npcNum) == 0)
											{
												setTalkValue(1);
												addEvenBuffer(0, npcNum);
												//��evenBuffer�[�J���a���(��Ʈw)
												System.out.println("even  "+npcNum+" : "+evenBuffer);
												
												break;
											}
											
											int npcCount = 0;
											for(int i=0; i<mapImfor.getNpcAmount(this.evenPoint); i++)
											{
												if(checkEvenSchedul(mapImfor.getNpcKind(this.evenPoint, i)) == 1)
													npcCount++;
											}
											
											if(checkEvenSchedul(npcNum) == 1)
											{
												if(npcCount == 6)
												{
													setTalkValue(4);
													addEvenBuffer(0, npcNum);
													makeWord.setAnimated(-4, mapImfor.getStartImage(this.evenPoint));
													//�]�w��Ʈw�����ɪ��A
													mapImfor.setDBStop(3, this.evenPoint, false);
													RoleDataValue.evenFinished[0] = "1";
													
													//Write to DataBase
													ConnectionServer.updateGameEvent(1);
													
													try{ Thread.sleep(500); } catch(Exception e){}
													
													ConnectionServer.updateGameMap(3);
													
												}
												else
													setTalkValue(1);
											}
											System.out.println("even  "+npcNum+" : "+evenBuffer);
											
											
											
											break;
										}
										
								case 2:
										{
											if(checkEvenSchedul(npcNum) == 1)
											{
												setTalkValue(0);
												System.out.println("even  "+npcNum+" : "+evenBuffer);
											}else setTalkValue(0);
											
											if((checkEvenSchedul(0) == 1) & (checkEvenSchedul(npcNum) == 0))
											{
												setTalkValue(1);
												
												//��evenBuffer�[�J���a���(��Ʈw)
												addEvenBuffer(0, npcNum);
												
												//��ұo�����~�[�J���a���(��Ʈw)
												mainValue.addItem("J11", false, 1);
												//ConnectionServer.updateGameInf();
												
												System.out.println("even  "+npcNum+" : "+evenBuffer);
											}
												
											if((RoleDataValue.evenFinished[2].equals("1")))
											{
												evenBuffer = RoleDataValue.nowEvenSchedule[3];//Ū���ثe�ƥ�i��(��Ʈw)
												if(! evenBuffer.equals("")) evenSchedule = split(evenBuffer, ",");
												
												if((RoleDataValue.evenFinished[3].equals("0")) & (checkEvenSchedul(11) == 1))
												{
													if(checkItem("I1", "20"))
													{
														setTalkValue(2);
														//�o�쳽����@
														//�����G���������� 20��
														mainValue.addItem("E1", false, 1);
														mainValue.deleteItemNum("I1", 20);
														ConnectionServer.updateGameInf();
														
														try{ Thread.sleep(500); } catch(Exception e){}
														
														addEvenBuffer(3, npcNum);
													}
												}
											}
											
											//�ƥ�Q�T
											evenBuffer = RoleDataValue.nowEvenSchedule[11];//Ū���ثe�ƥ�i��(��Ʈw)
											if(! evenBuffer.equals("")) evenSchedule = split(evenBuffer, ",");
											
											if((RoleDataValue.evenFinished[12].equals("0")) & checkEvenSchedul(14) == 1)
											{
												if(checkItem("I38", "30") & checkItem("I10", "10"))
												{
													setTalkValue(4);
													RoleDataValue.evenFinished[12] = "1";
													//�o��빡�ۥH�Τ볽������
													mainValue.addItem("E7", false, 1);
													mainValue.deleteItemNum("I38", 30);
													mainValue.deleteItemNum("I10", 10);
													ConnectionServer.updateGameInf();
												}
											}
											
											//�ƥ�Q��
											evenBuffer = RoleDataValue.nowEvenSchedule[13];//Ū���ثe�ƥ�i��(��Ʈw)
											if(! evenBuffer.equals("")) evenSchedule = split(evenBuffer, ",");
											
											if((RoleDataValue.evenFinished[14].equals("0")) & checkEvenSchedul(21) == 1)
											{
												if(checkItem("I13", "20") & checkItem("I14", "25"))
												{
													setTalkValue(3);
													RoleDataValue.evenFinished[14] = "1";
													//�o��B���ǵP�q�u���A�����������M�����B
													mainValue.addItem("E8", false, 1);
													mainValue.deleteItemNum("I13", 20);
													mainValue.deleteItemNum("I14", 25);
													ConnectionServer.updateGameInf();
												}
											}
											
											break;
										}
										
								case 4:
										{
											if(checkEvenSchedul(0) != 1)
											{
												setTalkValue(-1);
												break;
											}
											
											if(checkEvenSchedul(npcNum) == 1)
											{
												setTalkValue(0);
												System.out.println("even  "+npcNum+" : "+evenBuffer);
												break;
											}else setTalkValue(0);
											
											if(checkEvenSchedul(0) == 1)
											{
												setTalkValue(1);
												
												//��evenBuffer�[�J���a���(��Ʈw)
												addEvenBuffer(0, npcNum);
												
												//��ұo�����~�[�J���a���(��Ʈw)
												mainValue.addItem("I11", false, 10);
												ConnectionServer.updateGameInf();
												
												System.out.println("even  "+npcNum+" : "+evenBuffer);
											}
											
											break;
										}
										
								case 5:
										{
											if(checkEvenSchedul(npcNum) == 1)
											{
												setTalkValue(0);
												System.out.println("even  "+npcNum+" : "+evenBuffer);
												break;
											}else setTalkValue(0);
											
											
											if(checkEvenSchedul(0) == 1)
											{
												setTalkValue(1);
												
												//��evenBuffer�[�J���a���(��Ʈw)
												addEvenBuffer(0, npcNum);
												
												//��ұo�����~�[�J���a���(��Ʈw)
												mainValue.addItem("G1", false, 1);
												ConnectionServer.updateGameInf();
												
												System.out.println("even  "+npcNum+" : "+evenBuffer);
											}
											break;
										}
										
								case 8:
										{
											if(checkEvenSchedul(npcNum) == 1)
											{
												setTalkValue(0);
												System.out.println("even  "+npcNum+" : "+evenBuffer);
											}else setTalkValue(0);
											
											if((checkEvenSchedul(0) == 1) & (checkEvenSchedul(npcNum) == 0))
											{
												setTalkValue(1);
												
												//��evenBuffer�[�J���a���(��Ʈw)
												addEvenBuffer(0, npcNum);
												
												//��ұo�����~�[�J���a���(��Ʈw)
												mainValue.addItem("P1", false, 1);
												ConnectionServer.updateGameInf();
												
												System.out.println("even  "+npcNum+" : "+evenBuffer);
											}
											
											//�ƥ�G�Q�@�A�P�_�ƥ�Q�K�O�_��������L��
											evenBuffer = RoleDataValue.nowEvenSchedule[17];//Ū���ثe�ƥ�i��(��Ʈw)
											if(! evenBuffer.equals("")) evenSchedule = split(evenBuffer, ",");
											
											if((RoleDataValue.evenFinished[20].equals("0")) & (checkEvenSchedul(25) == 1))
											{System.out.println("YES");
												evenBuffer = RoleDataValue.nowEvenSchedule[20];//Ū���ثe�ƥ�i��(��Ʈw)
												if(! evenBuffer.equals("")) evenSchedule = split(evenBuffer, ",");
												
												if(checkEvenSchedul(9) == 1)
												{
													setTalkValue(4);
													//�o��q�q��
													mainValue.addItem("E13", false, 1);
													ConnectionServer.updateGameInf();
													
													try{ Thread.sleep(500); } catch(Exception e){}
													
													RoleDataValue.evenFinished[20] = "1";
													ConnectionServer.updateGameEvent(1);
												}
												
												if(checkEvenSchedul(npcNum) == 1)
												{
													if(checkItem("I7", "20") & checkItem("I8", "10"))
													{
														setTalkValue(3);
														addEvenBuffer(20, npcNum);
													}
												}
												
												if(checkEvenSchedul(npcNum) == 0)
												{
													setTalkValue(2);
													addEvenBuffer(20, npcNum);
												}
											}
											
											break;
										}
											
								case 9:
										{
											if(checkEvenSchedul(npcNum) == 1)
											{
												setTalkValue(0);
												System.out.println("even  "+npcNum+" : "+evenBuffer);
											}else setTalkValue(0);
											
											if((checkEvenSchedul(0) == 1) & (checkEvenSchedul(npcNum) == 0))
											{
												setTalkValue(1);
												
												//��evenBuffer�[�J���a���(��Ʈw)
												addEvenBuffer(0, npcNum);
												
												//��ұo�����~�[�J���a���(��Ʈw)
												mainValue.addItem("B1", false, 5);
												ConnectionServer.updateGameInf();
											}
												
											//�~�h�_���OŢ
											if(RoleDataValue.evenFinished[6].equals("1"))
											{
												evenBuffer = RoleDataValue.nowEvenSchedule[7];//Ū���ثe�ƥ�i��(��Ʈw)
												if(! evenBuffer.equals("")) evenSchedule = split(evenBuffer, ",");
											
												if((checkEvenSchedul(13) == 1) & (checkEvenSchedul(npcNum) == 0))
												{
													if(checkItem("I36","3") & checkItem("I3","20"))
													{
														//�o����t�OŢ
														mainValue.addItem("E4", false, 1);
														mainValue.deleteItemNum("I3", 20);
														mainValue.deleteItemNum("I36", 3);
														ConnectionServer.updateGameInf();
														
														try{ Thread.sleep(500); } catch(Exception e){}
														
														setTalkValue(3);
														addEvenBuffer(7, npcNum);
													}
												}
											}
											
											//�~�h�_�����K�Q�g��
											if(RoleDataValue.evenFinished[7].equals("1"))
											{
												evenBuffer = RoleDataValue.nowEvenSchedule[8];//Ū���ثe�ƥ�i��(��Ʈw)
												if(! evenBuffer.equals("")) evenSchedule = split(evenBuffer, ",");
											
												if(checkEvenSchedul(18) == 1)
												{
													if(checkItem("I2","40") & checkItem("I36","1") & checkItem("B2","1"))
													{
														//�o����K�Q�g��
														mainValue.addItem("E5", false, 1);
														mainValue.deleteItemNum("I2", 40);
														mainValue.deleteItemNum("I36", 1);
														mainValue.deleteItemNum("B2", 1);
														ConnectionServer.updateGameInf();
														
														setTalkValue(3);
													}
												}
											}
											
											//�ƥ�G�Q�@�A�s�@����
											evenBuffer = RoleDataValue.nowEvenSchedule[20];//Ū���ثe�ƥ�i��(��Ʈw)
											if(! evenBuffer.equals("")) evenSchedule = split(evenBuffer, ",");
											
											if((checkEvenSchedul(8) == 2) & (checkEvenSchedul(npcNum) == 0))
											{
												//�o�����
												//mainValue.addItem("E5", false, 1);
												mainValue.deleteItemNum("I7", 20);
												mainValue.deleteItemNum("I8", 10);
												ConnectionServer.updateGameInf();
											
												try{ Thread.sleep(500); } catch(Exception e){}
											
												setTalkValue(2);
												addEvenBuffer(20, npcNum);
											}											
											
											break;
										}
										
								case 23:
										{											
											if(RoleDataValue.evenFinished[1].equals("1"))//Ū���ƥ󧹦���(��Ʈw)
											{
												evenBuffer = RoleDataValue.nowEvenSchedule[2];//Ū���ثe�ƥ�i��(��Ʈw)
												if(! evenBuffer.equals("")) evenSchedule = split(evenBuffer, ",");
											}
											else
											{
												setTalkValue(-1);
												break;
											}
											
											if((checkEvenSchedul(7) == 1) & checkItem("B7", "15") 
																	& RoleDataValue.evenFinished[2].equals("0"))
											{
												setTalkValue(1);
												addEvenBuffer(2, npcNum);
												
												//�������r�N
												mainValue.deleteItemNum("B7", 15);
												ConnectionServer.updateGameInf();
												
												break;
											}
											else
											{
												setTalkValue(0);
												
												break;
											}
											
											
										}
										
										
								default :
										{
											break;
										}
							}
							
					
							break;
					}
						
				//��4�ӫ���
				case 4:
						{	
							//�ƥ�G
								evenBuffer = RoleDataValue.nowEvenSchedule[1];//Ū���ثe�ƥ�i��(��Ʈw)
								if(! evenBuffer.equals("")) evenSchedule = split(evenBuffer, ",");
								
							switch(npcNum)
							{
								case 6:
									{
										if(checkEvenSchedul(npcNum) == 2)
										{
											setTalkValue(1);
											
											break;
										}
										
										if((checkEvenSchedul(npcNum) == 1) &
												(RoleDataValue.killMonster >= 5) & (RoleDataValue.level > 10))
										{
											setTalkValue(4);
											addEvenBuffer(1, npcNum);
											makeWord.setAnimated(-3, mapImfor.getStartImage(this.evenPoint));
											mapImfor.setDBStop(3, this.evenPoint, false);
											RoleDataValue.evenFinished[1] = "1";
											
											//Write to DataBase
											ConnectionServer.updateGameEvent(1);
											
											try{ Thread.sleep(500); } catch(Exception e){}
											
											ConnectionServer.updateGameMap(3);
											break;
										}
										else if(checkEvenSchedul(npcNum) == 1)
										{
											setTalkValue(3);
											break;
										}
										
										if(checkEvenSchedul(npcNum) == 0)
										{
											setTalkValue(2);
											addEvenBuffer(1, npcNum);
											break;
										}
									}
									
								case 1:
									{//�ƥ�(���o����)
										if(RoleDataValue.evenFinished[5].equals("1"))
										{
												setTalkValue(1);
												break;
										}
									
										evenBuffer = RoleDataValue.nowEvenSchedule[4];//Ū���ثe�ƥ�i��(��Ʈw)
										if(! evenBuffer.equals("")) evenSchedule = split(evenBuffer, ",");
										
										if((checkEvenSchedul(17) == 1) & 
											(RoleDataValue.evenFinished[5].equals("0")))
										{
												evenBuffer = RoleDataValue.nowEvenSchedule[5];//Ū���ثe�ƥ�i��(��Ʈw)
												if(! evenBuffer.equals("")) evenSchedule = split(evenBuffer, ",");
												
												if((checkEvenSchedul(npcNum) == 1) & 
													checkItem("I36", "6") &
													checkItem("I37", "1"))
												{
													setTalkValue(4);
													RoleDataValue.evenFinished[5] = "1";
													//Write Even To Data Base
													ConnectionServer.updateGameEvent(1);
													
													//�R�q������
													//���o�����A�åB�O�d�����P�ʹq��
													mainValue.addItem("E2", false, 1);
													mainValue.deleteItemNum("I37", 1);
													ConnectionServer.updateGameInf();
													
												}else if(checkEvenSchedul(npcNum) == 1)
												{
													setTalkValue(3);
												}
												
												if(checkEvenSchedul(npcNum) == 0)
												{
													setTalkValue(2);
													addEvenBuffer(5, npcNum);
												}
										}else
										{
												setTalkValue(1);	
										}
										
										break;
									}
																		
								default	:
									{
											break;
									}
							}
							
							break;
						}
						
				//��6�ӫ���
				case 6:
						{
								//�ƥ�T
								evenBuffer = RoleDataValue.nowEvenSchedule[2];//Ū���ثe�ƥ�i��(��Ʈw)
								if(! evenBuffer.equals("")) evenSchedule = split(evenBuffer, ",");
							
							switch(npcNum)
							{
								case 7:
									{
										if(RoleDataValue.evenFinished[2].equals("1"))
										{
											setTalkValue(1);
											break;
										}
										
										if(checkEvenSchedul(npcNum) == 1)
										{
											setTalkValue(3);
											if(checkEvenSchedul(23) == 1)
											{
												setTalkValue(4);
												makeWord.setAnimated(-2, mapImfor.getStartImage(this.evenPoint));//�]�w��Ʈw�����ɪ��A
												mapImfor.setDBStop(1, this.evenPoint, false);
												RoleDataValue.evenFinished[2] = "1";
												
											//Write to DataBase
											ConnectionServer.updateGameEvent(1);
											
											try{ Thread.sleep(500); } catch(Exception e){}
											
											ConnectionServer.updateGameMap(1);
											}
											break;
										}
										
										if((RoleDataValue.level >= 20) & (checkEvenSchedul(npcNum) == 0))
										{
											setTalkValue(2);
											addEvenBuffer(2, npcNum);
										}
										break;
									}
								default :
									{
										break;
									}
							}
							
							break;
						}
						
				//��10�ӫ���
				case 10:
						{
							//�ƥ�|
								evenBuffer = RoleDataValue.nowEvenSchedule[3];//Ū���ثe�ƥ�i��(��Ʈw)
								if(! evenBuffer.equals("")) evenSchedule = split(evenBuffer, ",");
							
							switch(npcNum)
							{
								case 11:
									{
										if(RoleDataValue.evenFinished[3].equals("1"))
										{
											setTalkValue(1);
											break;
										}
										
										if(RoleDataValue.level < 25)
										{
											setTalkValue(0);
											break;
										}
										
										if((RoleDataValue.level >= 25) & (checkEvenSchedul(npcNum) == 0))
										{
											setTalkValue(2);
											addEvenBuffer(3, npcNum);
											break;
										}
										
										if((checkEvenSchedul(npcNum) == 1) &
														(checkEvenSchedul(2) == 1))
										{
											if(checkItem("E1", "1"))
											{
												setTalkValue(4);
												makeWord.setAnimated(-3, mapImfor.getStartImage(this.evenPoint));//�]�w��Ʈw�����ɪ��A
												mapImfor.setDBStop(2, this.evenPoint, false);
												RoleDataValue.evenFinished[3] = "1";
												
											//Write to DataBase
											ConnectionServer.updateGameEvent(1);
											
											try{ Thread.sleep(500); } catch(Exception e){}
											
											ConnectionServer.updateGameMap(2);
											}
											break;
										}
										else if(checkEvenSchedul(npcNum) == 1)
										{
											setTalkValue(3);
											break;
										}
										
									}
								default :
									{
										break;
									}
							}
							
							break;
						}
						
				//��8�ӫ���
				case 8:
						{
							evenBuffer = RoleDataValue.nowEvenSchedule[4];//Ū���ثe�ƥ�i��(��Ʈw)
							if(! evenBuffer.equals("")) evenSchedule = split(evenBuffer, ",");
							
							//�ƥ󤭡A�F�z�ǰe�v
							switch(npcNum)
							{
								case 17:
									{
										if(RoleDataValue.evenFinished[4].equals("1"))
										{
											setTalkValue(1);
										}
										
										if((checkEvenSchedul(npcNum) == 1) &
											checkItem("E2", "1"))
										{
											setTalkValue(4);
											addEvenBuffer(4, npcNum);
											makeWord.setAnimated(-4, mapImfor.getStartImage(this.evenPoint));//�]�w��Ʈw�����ɪ��A
											mapImfor.setDBStop(3, this.evenPoint, false);
											RoleDataValue.evenFinished[4] = "1";
											
											//Write to DataBase
											ConnectionServer.updateGameEvent(1);
											
											try{ Thread.sleep(500); } catch(Exception e){}
											
											ConnectionServer.updateGameMap(3);
											
											try{ Thread.sleep(500); } catch(Exception e){}
											
											//��������
											mainValue.deleteItemNum("E2", 1);
											ConnectionServer.updateGameInf();
											
										}else if(checkEvenSchedul(npcNum) == 1)
										{
											setTalkValue(3);
										}
										
										if((RoleDataValue.level >= 30) & (checkEvenSchedul(npcNum) == 0))// &
											//(! RoleDataValue.evenFinished[4]))
										{
											System.out.println("npcNum = "+checkEvenSchedul(npcNum));
											setTalkValue(2);
											addEvenBuffer(4, npcNum);
										}
										
										break;
									}
								default :
									{
										break;
									}
							}
							
							break;
						}
				
				//��27�ӫ���
				case 27:
						{
							//�ƥ�C
							//�P�ݪ����԰�
							switch(npcNum)
							{
								case 12:
									{
										if((RoleDataValue.level >= 35) )//& (checkEvenSchedul(npcNum) == 0))
										{
											//�԰��ӧQ�Υ���
											if((talkLevel == true) & (BattleFrame.battleVictory == true))
											{
												talkLevel = false;
												
												if(RoleDataValue.evenFinished[6].equals("1")) 
												{System.out.println("point 1");												
													setTalkValue(1);
													makeWord.remove(makeWord.getLayerAt(1));
												}
												
												if(RoleDataValue.evenFinished[6].equals("0"))
												{System.out.println("point 2");
													setTalkValue(4);
													RoleDataValue.evenFinished[6] = "1";
											
													//Write to DataBase
													ConnectionServer.updateGameEvent(1);
													
													makeWord.remove(makeWord.getLayerAt(2));
												}
											}else if(talkLevel == true)
											{System.out.println("point 3");
												talkLevel = false;
												setTalkValue(3);
												if(RoleDataValue.evenFinished[5].equals("1")) setTalkValue(1);
											}
											
											
											if((talkLevel == false) & (RoleDataValue.evenFinished[6].equals("0"))) 
											{	System.out.println("point 4");
												talkLevel = true;
												setTalkValue(2);	
											}
											else if(talkLevel == false)
											{System.out.println("point 5");
												talkLevel = true;
												setTalkValue(1);
											}
											
											//myMapCanvars.callBattle("M49");
											
										}else	System.out.println("point 6");//setTalkValue(1);
										
										break;
									}
								default :
									{
										break;
									}
							}
							
							break;
						}
						
				//��30�ӫ���
				case 30:
						{
							//�ƥ�K
							evenBuffer = RoleDataValue.nowEvenSchedule[7];//Ū���ثe�ƥ�i��(��Ʈw)
							if(! evenBuffer.equals("")) evenSchedule = split(evenBuffer, ",");
							
							switch(npcNum)
							{
								case 13:
									{
										if(checkEvenSchedul(npcNum) == 1)
										{
											if(checkItem("E4","1"))
											{
												setTalkValue(4);
												makeWord.setAnimated(-2, mapImfor.getStartImage(this.evenPoint));//�]�w��Ʈw�����ɪ��A
												mapImfor.setDBStop(1, this.evenPoint, false);
												RoleDataValue.evenFinished[7] = "1";
												
											//Write to DataBase
											ConnectionServer.updateGameEvent(1);
											
											try{ Thread.sleep(500); } catch(Exception e){}
											
											ConnectionServer.updateGameMap(1);
												
											}else if(RoleDataValue.evenFinished[7].equals("0"))
											{
												setTalkValue(3);
											}
										}
										
										if((RoleDataValue.level >= 50) & (checkEvenSchedul(npcNum) == 0))
										{
											setTalkValue(2);
											addEvenBuffer(7, npcNum);
										}
										
										break;
									}
								default :
									{
										break;
									}
							}
							
							break;
						}
						
				//��33�ӫ���
				case 33:
						{
							//�ƥ�E
							evenBuffer = RoleDataValue.nowEvenSchedule[8];//Ū���ثe�ƥ�i��(��Ʈw)
							if(! evenBuffer.equals("")) evenSchedule = split(evenBuffer, ",");
							
							switch(npcNum)
							{
								case 18:
									{
										if(checkEvenSchedul(npcNum) == 1)
										{
											if(checkItem("E5","1"))
											{
												setTalkValue(4);
												makeWord.setAnimated(-3, mapImfor.getStartImage(this.evenPoint));//�]�w��Ʈw�����ɪ��A
												mapImfor.setDBStop(2, this.evenPoint, false);
												RoleDataValue.evenFinished[8] = "1";
												
											//Write to DataBase
											ConnectionServer.updateGameEvent(1);
											
											try{ Thread.sleep(500); } catch(Exception e){}
											
											ConnectionServer.updateGameMap(2);
												
											}else if(RoleDataValue.evenFinished[8].equals("0"))
											{
												setTalkValue(3);
											}
										}
										
										if((RoleDataValue.level >= 60) & (checkEvenSchedul(npcNum) == 0))
										{
											setTalkValue(2);
											addEvenBuffer(8, npcNum);
										}
										
										break;
									}
								default :
									{
										break;
									}
							}
							
							break;
						}
						
				//��38�ӫ���
				case 38:
						{
							//�ƥ�Q
							evenBuffer = RoleDataValue.nowEvenSchedule[9];//Ū���ثe�ƥ�i��(��Ʈw)
							if(! evenBuffer.equals("")) evenSchedule = split(evenBuffer, ",");
							
							switch(npcNum)
							{
								case 19:
									{
										if(RoleDataValue.evenFinished[9].equals("1"))
										{
											setTalkValue(1);
										}
										
										if((RoleDataValue.evenFinished[10].equals("0")) & checkEvenSchedul(npcNum) == 1)
										{
											if(checkItem("I2","20"))
											{
												//�������P�P
												mainValue.deleteItemNum("I2", 20);
												ConnectionServer.updateGameInf();
												
												try{ Thread.sleep(500); } catch(Exception e){}
												
												setTalkValue(4);
												makeWord.setAnimated(-3, mapImfor.getStartImage(this.evenPoint));//�]�w��Ʈw�����ɪ��A
												mapImfor.setDBStop(2, this.evenPoint, false);
												RoleDataValue.evenFinished[9] = "1";
												
												//Write to DataBase
												ConnectionServer.updateGameEvent(1);
												
												try{ Thread.sleep(500); } catch(Exception e){}
												
												ConnectionServer.updateGameMap(2);
												
											}else
											{
												setTalkValue(3);
											}
										}
										
										if((RoleDataValue.level >= 62) & (checkEvenSchedul(npcNum) == 0))
										{
											setTalkValue(2);
											addEvenBuffer(9, npcNum);
										}
										
										break;
									}
								default :
									{
										break;
									}
							}
							
							break;
						}
						
				//��11�ӫ���
				case 41:
						{
							//�ƥ�Q�@
							evenBuffer = RoleDataValue.nowEvenSchedule[10];//Ū���ثe�ƥ�i��(��Ʈw)
							if(! evenBuffer.equals("")) evenSchedule = split(evenBuffer, ",");
							
							switch(npcNum)
							{
								case 20:
									{
										if(RoleDataValue.evenFinished[10].equals("1"))
										{
											setTalkValue(1);
										}
										
										if((RoleDataValue.evenFinished[10].equals("0")) & checkEvenSchedul(npcNum) == 1)
										{
											if(checkItem("I4", "20") & checkItem("I5", "10"))
											{
												//�o��B�����H�Τ��P�P
												mainValue.addItem("I4", false, 20);
												mainValue.addItem("I5", false, 10);
												ConnectionServer.updateGameInf();
												
												try{ Thread.sleep(500); } catch(Exception e){}
												
												setTalkValue(4);
												makeWord.setAnimated(-4, mapImfor.getStartImage(this.evenPoint));//�]�w��Ʈw�����ɪ��A
												mapImfor.setDBStop(3, this.evenPoint, false);
												RoleDataValue.evenFinished[10] = "1";
												
												//Write to DataBase
												ConnectionServer.updateGameEvent(1);
												
												try{ Thread.sleep(500); } catch(Exception e){}
												
												ConnectionServer.updateGameMap(3);
											}
											else
											{
												setTalkValue(3);
											}
										}
										
										if((RoleDataValue.level >= 68) & (checkEvenSchedul(npcNum) == 0))
										{
											setTalkValue(2);
											addEvenBuffer(10, npcNum);
										}
										
										break;
									}
								default :
									{
										break;
									}
							}
							
							break;
						}
						
				//��43�ӫ���
				case 43:
						{
							//�ƥ�Q�G
							evenBuffer = RoleDataValue.nowEvenSchedule[11];//Ū���ثe�ƥ�i��(��Ʈw)
							if(! evenBuffer.equals("")) evenSchedule = split(evenBuffer, ",");
							
							switch(npcNum)
							{
								case 14:
									{
										if(RoleDataValue.level < 70)
										{
											setTalkValue(1);
											break;
										}
										
										if(RoleDataValue.evenFinished[11].equals("1"))
										{
											setTalkValue(1);
										}
										
										if((RoleDataValue.evenFinished[11].equals("0")) & checkEvenSchedul(npcNum) == 1)
										{
											if(checkItem("E7", "1") & checkItem("I6", "40"))
											{
												//�����빡�ۡA�H�γ�������
												mainValue.deleteItemNum("E7", 1);
												mainValue.deleteItemNum("I6", 40);
												ConnectionServer.updateGameInf();
												
												try{ Thread.sleep(500); } catch(Exception e){}
												
												setTalkValue(4);
												RoleDataValue.evenFinished[10] = "1";
												ConnectionServer.updateGameEvent(1);
											}
											else
											{
												setTalkValue(3);
											}
										}
										
										if((RoleDataValue.level >= 70) & (checkEvenSchedul(npcNum) == 0))
										{
											setTalkValue(2);
											addEvenBuffer(11, npcNum);
										}
										
										break;
									}
								default :
									{
										break;
									}
							}
							
							break;
						}
						
				//��45�ӫ���
				case 45:
						{
							//�ƥ�Q�|
							evenBuffer = RoleDataValue.nowEvenSchedule[13];//Ū���ثe�ƥ�i��(��Ʈw)
							if(! evenBuffer.equals("")) evenSchedule = split(evenBuffer, ",");
							
							switch(npcNum)
							{
								case 21:
									{
										if(RoleDataValue.evenFinished[13].equals("1"))
										{
											setTalkValue(0);
											break;
										}
										
										if((RoleDataValue.evenFinished[13].equals("0")) & checkEvenSchedul(npcNum) == 1)
										{
											if(checkItem("E8", "1") & checkItem("I12", "1"))
											{
												//�����B���ǵP�q�u���H�Τ��j
												mainValue.deleteItemNum("E8", 1);
												mainValue.deleteItemNum("I12", 1);
												ConnectionServer.updateGameInf();
												
												try{ Thread.sleep(500); } catch(Exception e){}
												
												setTalkValue(4);
												makeWord.setAnimated(-3, mapImfor.getStartImage(this.evenPoint));//�]�w��Ʈw�����ɪ��A
												mapImfor.setDBStop(2, this.evenPoint, false);
												RoleDataValue.evenFinished[13] = "1";
												
												//Write to DataBase
												ConnectionServer.updateGameEvent(1);
												
												try{ Thread.sleep(500); } catch(Exception e){}
												
												ConnectionServer.updateGameMap(2);
											}
											else
											{
												setTalkValue(3);
											}
										}
										
										if((RoleDataValue.level >= 73) & checkEvenSchedul(npcNum) == 0)
										{
											setTalkValue(2);
											addEvenBuffer(13, npcNum);
										}else if(RoleDataValue.level < 73)	setTalkValue(1);
										
										break;
									}
								default :
									{
										break;
									}
							}
							
							break;
						}
						
				//��7�ӫ���
				case 7:
						{
							//�ƥ�Q��
							evenBuffer = RoleDataValue.nowEvenSchedule[15];//Ū���ثe�ƥ�i��(��Ʈw)
							if(! evenBuffer.equals("")) evenSchedule = split(evenBuffer, ",");
							
							switch(npcNum)
							{
								case 22://���~�����u��
									{
										if(RoleDataValue.evenFinished[15].equals("1")) setTalkValue(1);
										
										if((RoleDataValue.evenFinished[15].equals("0")) & (checkEvenSchedul(npcNum) == 1))
										{
											if(checkItem("E9", "1"))
											{
												setTalkValue(4);
												makeWord.setAnimated(-2, mapImfor.getStartImage(this.evenPoint));//�]�w��Ʈw�����ɪ��A
												mapImfor.setDBStop(1, this.evenPoint, false);
												RoleDataValue.evenFinished[15] = "1";
												
												//Write to DataBase
												ConnectionServer.updateGameEvent(1);
												
												try{ Thread.sleep(500); } catch(Exception e){}
												
												ConnectionServer.updateGameMap(1);
											}
											else	setTalkValue(3);
										}
										
										if((RoleDataValue.level >= 75) & (checkEvenSchedul(npcNum) == 0))
										{
											setTalkValue(2);
											addEvenBuffer(15, npcNum);
										}else if((RoleDataValue.level < 75) & (checkEvenSchedul(npcNum) == 0))
										{
											setTalkValue(0);
										}
										
										break;
									}
								default :
									{
										break;
									}
							}
							
							break;
						}
						
				//��17�ӫ���
				case 17:
						{
							//�ƥ�Q��
							evenBuffer = RoleDataValue.nowEvenSchedule[15];//Ū���ثe�ƥ�i��(��Ʈw)
							if(! evenBuffer.equals("")) evenSchedule = split(evenBuffer, ",");
							
							switch(npcNum)
							{
								case 15:
									{
										if(checkEvenSchedul(npcNum) == 1) setTalkValue(1);
										
										if((checkEvenSchedul(npcNum) == 0) & (checkEvenSchedul(22) == 1))
										{
											setTalkValue(4);
											addEvenBuffer(15, npcNum);
											//�o�쯫���u�@�H���ҩ���
											mainValue.addItem("E9", false, 1);
											ConnectionServer.updateGameInf();
											
										}else if((RoleDataValue.level >= 75) & (checkEvenSchedul(npcNum) == 0))
										{
											setTalkValue(0);
										}
										
										break;
									}
								default :
									{
										break;
									}
							}
							
							break;
						}
						
				//��50�ӫ���
				case 50:
						{
							//�ƥ�Q�C
							evenBuffer = RoleDataValue.nowEvenSchedule[16];//Ū���ثe�ƥ�i��(��Ʈw)
							if(! evenBuffer.equals("")) evenSchedule = split(evenBuffer, ",");
							
							switch(npcNum)
							{
								case 26:
									{
										if(RoleDataValue.evenFinished[15].equals("1")) setTalkValue(0);
										
										if((RoleDataValue.level >= 79) & (checkEvenSchedul(npcNum) == 0))
										{
											setTalkValue(0);
											makeWord.setAnimated(-2, mapImfor.getStartImage(this.evenPoint));//�]�w��Ʈw�����ɪ��A
											mapImfor.setDBStop(1, this.evenPoint, false);
											RoleDataValue.evenFinished[15] = "1";
											addEvenBuffer(16, npcNum);
											//�o����˪��a��
											mainValue.addItem("E10", false, 1);
											ConnectionServer.updateGameInf();
											
										}else if((RoleDataValue.level < 79) & (checkEvenSchedul(npcNum) == 0))
										{
											setTalkValue(0);
										}
										
										break;
									}
								default :
									{
										break;
									}
							}
							
							break;
						}
					
				//��31�ӫ���
				case 31:
						{
							//�ƥ�Q�K
							evenBuffer = RoleDataValue.nowEvenSchedule[17];//Ū���ثe�ƥ�i��(��Ʈw)
							if(! evenBuffer.equals("")) evenSchedule = split(evenBuffer, ",");
							
							switch(npcNum)
							{
								case 27:
									{
										if(RoleDataValue.evenFinished[17].equals("1")) setTalkValue(4);
										
										if((RoleDataValue.evenFinished[17].equals("0")) & (checkEvenSchedul(npcNum) == 1))
										{
											if(checkItem("E11", "1"))
											{
												setTalkValue(4);
												makeWord.setAnimated(-1, mapImfor.getStartImage(this.evenPoint));//�]�w��Ʈw�����ɪ��A
												mapImfor.setDBStop(0, this.evenPoint, false);
												RoleDataValue.evenFinished[17] = "1";
												
												//Write to DataBase
												ConnectionServer.updateGameEvent(1);
												
												try{ Thread.sleep(500); } catch(Exception e){}
												
												ConnectionServer.updateGameMap(0);
											}
											else 
											{
												setTalkValue(0);
											}
										}
										
										if((RoleDataValue.level >= 80) & (checkEvenSchedul(npcNum) == 0))
										{
											setTalkValue(0);
											addEvenBuffer(17, npcNum);
										}
										
										break;
									}
									
								case 28:
									{
										if(RoleDataValue.evenFinished[17].equals("1") &
													(checkItem("E15", "1"))) setTalkValue(4);
										
										if((RoleDataValue.evenFinished[17].equals("1")) & (checkEvenSchedul(npcNum) == 1))
										{
											if(checkItem("E11", "1"))
											{
												setTalkValue(4);
												//�o��^�������H���Y
												mainValue.addItem("E15", false, 1);
												ConnectionServer.updateGameInf();
											}
										}
										
										if((RoleDataValue.level >= 80) & (checkEvenSchedul(npcNum) == 0))
										{
											setTalkValue(0);
											addEvenBuffer(17, npcNum);
										}
										
										break;
									}
								default :
									{
										break;
									}
							}
							
							break;
						}
						
				//��53�ӫ���
				case 53:
						{
							//�ƥ�Q�E�A�P�񯫾԰�
							evenBuffer = RoleDataValue.nowEvenSchedule[18];//Ū���ثe�ƥ�i��(��Ʈw)
							if(! evenBuffer.equals("")) evenSchedule = split(evenBuffer, ",");
							
							switch(npcNum)
							{
								case 16:
									{
										if(RoleDataValue.evenFinished[18].equals("1")) setTalkValue(4);
										
										if((RoleDataValue.evenFinished[17].equals("0")) & (checkEvenSchedul(npcNum) == 1))
										{
											if(checkItem("E11", "1"))
											{
												setTalkValue(4);
												
												//�P�񯫾԰�
												
												//Ĺ�F�񯫱o���(���~)�A�H�Χ������ƥ�
												//mainValue.addItem("P31", false, 1);
												ConnectionServer.updateGameInf();
												
												try{ Thread.sleep(500); } catch(Exception e){}
												
												RoleDataValue.evenFinished[18] = "1";
												ConnectionServer.updateGameEvent(1);
											}
											else 
											{
												setTalkValue(0);
											}
										}
										
										if((RoleDataValue.level < 79) & (checkEvenSchedul(npcNum) == 0))
										{
											setTalkValue(0);
											addEvenBuffer(17, npcNum);
										}
										
										break;
									}
								default :
									{
										break;
									}
							}
							
							break;
						}
						
				//��63�ӫ���
				case 63:
						{
							//�ƥ�G�Q
							evenBuffer = RoleDataValue.nowEvenSchedule[19];//Ū���ثe�ƥ�i��(��Ʈw)
							if(! evenBuffer.equals("")) evenSchedule = split(evenBuffer, ",");
							
							switch(npcNum)
							{
								case 25:
									{
										if(RoleDataValue.evenFinished[19].equals("1")) setTalkValue(4);
										
										if((RoleDataValue.level >= 88) & (RoleDataValue.evenFinished[19].equals("0"))
																						& (checkEvenSchedul(npcNum) == 1))
										{
											if(checkItem("E13", "1"))
											{
												//�o��s�W�������Y
												mainValue.addItem("E12", false, 1);
												ConnectionServer.updateGameInf();
												
												try{ Thread.sleep(500); } catch(Exception e){}
												
												setTalkValue(1);
												makeWord.setAnimated(-4, mapImfor.getStartImage(this.evenPoint));//�]�w��Ʈw�����ɪ��A
												mapImfor.setDBStop(3, this.evenPoint, false);
												RoleDataValue.evenFinished[19] = "1";
												
												//Write to DataBase
												ConnectionServer.updateGameEvent(1);
												
												try{ Thread.sleep(500); } catch(Exception e){}
												
												ConnectionServer.updateGameMap(3);
											}
											else 
											{
												setTalkValue(0);
											}
										}
										
										if(checkEvenSchedul(npcNum) == 0)
										{
											setTalkValue(0);
											addEvenBuffer(19, npcNum);
										}
										
										break;
									}
								default :
									{
										break;
									}
							}
							
							break;
						}
						
				//��45�ӫ���
				case 67:
						{
							//�ƥ�G�Q�G
							evenBuffer = RoleDataValue.nowEvenSchedule[21];//Ū���ثe�ƥ�i��(��Ʈw)
							if(! evenBuffer.equals("")) evenSchedule = split(evenBuffer, ",");
							
							switch(npcNum)
							{
								case 29:
									{
										
										if(RoleDataValue.evenFinished[21].equals("1"))
										{
											setTalkValue(0);
										}
										
										if(RoleDataValue.evenFinished[21].equals("0"))
										{
											if(checkItem("E12", "1") & checkItem("E10", "1") & checkItem("E15", "1")
											 & checkItem("I34", "1") & checkItem("I9", "1") & checkItem("E9", "1")
											 & checkItem("E6", "1") & checkItem("E14", "1"))
											 {
											 	setTalkValue(4);
											 	makeWord.setAnimated(-1, mapImfor.getStartImage(this.evenPoint));//�]�w��Ʈw�����ɪ��A
												mapImfor.setDBStop(0, this.evenPoint, false);
												RoleDataValue.evenFinished[19] = "1";
											 }else
											 {
											 	setTalkValue(1);
											 }
										}
										
										break;
									}
								default :
									{
										break;
									}
							}
							
							break;
						}
										
				default :
						{
							setTalkValue(-1);
							break;
						}
						
		}
	}
	
	//�]�w�ƥ��I
	public void setEvenPoint(int evenPoint)
	{
		this.evenPoint = evenPoint;
	}
	
	//�M��O�_�w�����ثeNPC���ƥ�
	public int checkEvenSchedul(int num)
	{
		int evenState = 0;
	
			for(int i=0; i<evenSchedule.length; i++)
			{
					if(evenSchedule[i] == num)
					{
						evenState++;
					}
			}
				
		return evenState;
	}
	
	//�]�wNPC�������
	public void setTalkValue(int talkAbout)
	{
		this.talkAbout = talkAbout;
	}
	
	//���oTalkAbout Value
	public int getTalkValue()
	{
		return talkAbout;
	}
	
	//��i�ץ[�JevenBuffer
	public void addEvenBuffer(int storyNum, int npcNum)
	{
		if(evenBuffer.equals("")) evenBuffer += npcNum;
		else evenBuffer += ","+npcNum;
		
		evenSchedule = split(evenBuffer, ",");
		RoleDataValue.nowEvenSchedule[storyNum] = evenBuffer;
		
		//�g�J�i�ר��Ʈw
		ConnectionServer.updateGameEvent(2);
		try{ Thread.sleep(500); } catch(Exception e){}
		System.out.println("Write Even To DataBase And Thread 400ms");
		
		if(evenSchedule != null) for(int x=0; x<evenSchedule.length; x++) 
					System.out.print("evenSchedule["+x+"]:"+evenSchedule[x]+"  ");
		System.out.println();
	}
	
	public boolean checkItem(String name, String amount)
	{
		boolean itemState = false;
		
		if(! mainValue.testItem(name))
			{ 	if(Integer.parseInt(mainValue.getItemAmount(name))
							>= Integer.parseInt(amount)) itemState = true;	}
		
		return itemState;
	}
	/*	boolean itemState = false;
		
		if(RoleDataValue.roleItem != null)
		{
			for(int i=0; i<RoleDataValue.roleItem.length; i++)
			{
				if((name == RoleDataValue.roleItem[i][0]) & //(amount == RoleDataValue.roleItem[i][2]))
					(Integer.parseInt(amount) <= Integer.parseInt(RoleDataValue.roleItem[i][2])))
				{
					itemState = true;
				}
			}
		}
		
		return itemState;
	}*/
	
	//�r�����
	public int[] split(String str, String regex)
	{
		int count=0;
		int index=0;
		
		
		
		for(int i=0;i<str.length();i++){
			if( str.indexOf(regex, index)!=-1 ){
				count++;
				index=str.indexOf(regex, index) + 1;
			}
		}
		
		if(str.length()!=index) count++;
		index=0;
		
		int strsplit[] = new int[count];
		int indexarray[] = new int[count];
		
		for(int i=0;i<count;i++){
			if( str.indexOf(regex, index)!=-1 ){
				index=str.indexOf(regex, index) + 1;
				indexarray[i]=index;
			}
		}
		
		index=0;
		
		for(int i=0;i<count-1;i++){
			strsplit[i] = Integer.parseInt(str.substring(index, indexarray[i]-1));
			index = indexarray[i];
		}
		
		strsplit[count-1] = Integer.parseInt(str.substring(index, str.length()));
		
		return strsplit;
	}
}