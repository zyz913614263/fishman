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
public class storyEven
{
	private int evenPoint;//設定城鎮位置(依故事順序)
	private int evenSchedule[];//儲存事件解迷進度
	private String evenBuffer;//暫存事件進度
	private RoleDataValue mainValue;//主角的各種狀態值
	private map mapImfor;//地圖資訊的指標
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
		
		//stop = new boolean[4];//取得是否有結界
		//for(int i=0; i<4; i++) stop[i] = mapImfor.getDBStop(i ,this.evenPoint);
	}
	
	public void doEven(int npcNum)
	{
		System.out.println("this.evenPoint = " + this.evenPoint);
		switch (this.evenPoint)
		{
				//第0個城鎮
				case 0:
						{
									evenBuffer = RoleDataValue.nowEvenSchedule[0];//讀取目前事件進度(資料庫)
									System.out.println("RoleDataValue.nowEvenSchedule[0] = "+RoleDataValue.nowEvenSchedule[0]);
									if(! evenBuffer.equals("")) evenSchedule = split(evenBuffer, ",");
				
							switch(npcNum)
							{
								//事件一
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
												//把evenBuffer加入玩家資料(資料庫)
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
													//設定資料庫的結界狀態
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
												
												//把evenBuffer加入玩家資料(資料庫)
												addEvenBuffer(0, npcNum);
												
												//把所得的物品加入玩家資料(資料庫)
												mainValue.addItem("J11", false, 1);
												//ConnectionServer.updateGameInf();
												
												System.out.println("even  "+npcNum+" : "+evenBuffer);
											}
												
											if((RoleDataValue.evenFinished[2].equals("1")))
											{
												evenBuffer = RoleDataValue.nowEvenSchedule[3];//讀取目前事件進度(資料庫)
												if(! evenBuffer.equals("")) evenSchedule = split(evenBuffer, ",");
												
												if((RoleDataValue.evenFinished[3].equals("0")) & (checkEvenSchedul(11) == 1))
												{
													if(checkItem("I1", "20"))
													{
														setTalkValue(2);
														//得到魚鱗皮鞭
														//移除亮晶晶的魚鱗 20個
														mainValue.addItem("E1", false, 1);
														mainValue.deleteItemNum("I1", 20);
														ConnectionServer.updateGameInf();
														
														try{ Thread.sleep(500); } catch(Exception e){}
														
														addEvenBuffer(3, npcNum);
													}
												}
											}
											
											//事件十三
											evenBuffer = RoleDataValue.nowEvenSchedule[11];//讀取目前事件進度(資料庫)
											if(! evenBuffer.equals("")) evenSchedule = split(evenBuffer, ",");
											
											if((RoleDataValue.evenFinished[12].equals("0")) & checkEvenSchedul(14) == 1)
											{
												if(checkItem("I38", "30") & checkItem("I10", "10"))
												{
													setTalkValue(4);
													RoleDataValue.evenFinished[12] = "1";
													//得到月飽石以及月魚骨項鍊
													mainValue.addItem("E7", false, 1);
													mainValue.deleteItemNum("I38", 30);
													mainValue.deleteItemNum("I10", 10);
													ConnectionServer.updateGameInf();
												}
											}
											
											//事件十五
											evenBuffer = RoleDataValue.nowEvenSchedule[13];//讀取目前事件進度(資料庫)
											if(! evenBuffer.equals("")) evenSchedule = split(evenBuffer, ",");
											
											if((RoleDataValue.evenFinished[14].equals("0")) & checkEvenSchedul(21) == 1)
											{
												if(checkItem("I13", "20") & checkItem("I14", "25"))
												{
													setTalkValue(3);
													RoleDataValue.evenFinished[14] = "1";
													//得到冰的勒牌礦泉水，移除機車火和機車冰
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
												
												//把evenBuffer加入玩家資料(資料庫)
												addEvenBuffer(0, npcNum);
												
												//把所得的物品加入玩家資料(資料庫)
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
												
												//把evenBuffer加入玩家資料(資料庫)
												addEvenBuffer(0, npcNum);
												
												//把所得的物品加入玩家資料(資料庫)
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
												
												//把evenBuffer加入玩家資料(資料庫)
												addEvenBuffer(0, npcNum);
												
												//把所得的物品加入玩家資料(資料庫)
												mainValue.addItem("P1", false, 1);
												ConnectionServer.updateGameInf();
												
												System.out.println("even  "+npcNum+" : "+evenBuffer);
											}
											
											//事件二十一，判斷事件十八是否跟水井說過話
											evenBuffer = RoleDataValue.nowEvenSchedule[17];//讀取目前事件進度(資料庫)
											if(! evenBuffer.equals("")) evenSchedule = split(evenBuffer, ",");
											
											if((RoleDataValue.evenFinished[20].equals("0")) & (checkEvenSchedul(25) == 1))
											{System.out.println("YES");
												evenBuffer = RoleDataValue.nowEvenSchedule[20];//讀取目前事件進度(資料庫)
												if(! evenBuffer.equals("")) evenSchedule = split(evenBuffer, ",");
												
												if(checkEvenSchedul(9) == 1)
												{
													setTalkValue(4);
													//得到通通樂
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
												
												//把evenBuffer加入玩家資料(資料庫)
												addEvenBuffer(0, npcNum);
												
												//把所得的物品加入玩家資料(資料庫)
												mainValue.addItem("B1", false, 5);
												ConnectionServer.updateGameInf();
											}
												
											//洗多寶做燈籠
											if(RoleDataValue.evenFinished[6].equals("1"))
											{
												evenBuffer = RoleDataValue.nowEvenSchedule[7];//讀取目前事件進度(資料庫)
												if(! evenBuffer.equals("")) evenSchedule = split(evenBuffer, ",");
											
												if((checkEvenSchedul(13) == 1) & (checkEvenSchedul(npcNum) == 0))
												{
													if(checkItem("I36","3") & checkItem("I3","20"))
													{
														//得到海龜燈籠
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
											
											//洗多寶做火焰噴射器
											if(RoleDataValue.evenFinished[7].equals("1"))
											{
												evenBuffer = RoleDataValue.nowEvenSchedule[8];//讀取目前事件進度(資料庫)
												if(! evenBuffer.equals("")) evenSchedule = split(evenBuffer, ",");
											
												if(checkEvenSchedul(18) == 1)
												{
													if(checkItem("I2","40") & checkItem("I36","1") & checkItem("B2","1"))
													{
														//得到火焰噴射器
														mainValue.addItem("E5", false, 1);
														mainValue.deleteItemNum("I2", 40);
														mainValue.deleteItemNum("I36", 1);
														mainValue.deleteItemNum("B2", 1);
														ConnectionServer.updateGameInf();
														
														setTalkValue(3);
													}
												}
											}
											
											//事件二十一，製作解藥
											evenBuffer = RoleDataValue.nowEvenSchedule[20];//讀取目前事件進度(資料庫)
											if(! evenBuffer.equals("")) evenSchedule = split(evenBuffer, ",");
											
											if((checkEvenSchedul(8) == 2) & (checkEvenSchedul(npcNum) == 0))
											{
												//得到解藥
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
											if(RoleDataValue.evenFinished[1].equals("1"))//讀取事件完成度(資料庫)
											{
												evenBuffer = RoleDataValue.nowEvenSchedule[2];//讀取目前事件進度(資料庫)
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
												
												//移除銅鑼燒
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
						
				//第4個城鎮
				case 4:
						{	
							//事件二
								evenBuffer = RoleDataValue.nowEvenSchedule[1];//讀取目前事件進度(資料庫)
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
									{//事件六(取得金牙)
										if(RoleDataValue.evenFinished[5].equals("1"))
										{
												setTalkValue(1);
												break;
										}
									
										evenBuffer = RoleDataValue.nowEvenSchedule[4];//讀取目前事件進度(資料庫)
										if(! evenBuffer.equals("")) evenSchedule = split(evenBuffer, ",");
										
										if((checkEvenSchedul(17) == 1) & 
											(RoleDataValue.evenFinished[5].equals("0")))
										{
												evenBuffer = RoleDataValue.nowEvenSchedule[5];//讀取目前事件進度(資料庫)
												if(! evenBuffer.equals("")) evenSchedule = split(evenBuffer, ",");
												
												if((checkEvenSchedul(npcNum) == 1) & 
													checkItem("I36", "6") &
													checkItem("I37", "1"))
												{
													setTalkValue(4);
													RoleDataValue.evenFinished[5] = "1";
													//Write Even To Data Base
													ConnectionServer.updateGameEvent(1);
													
													//充電器移除
													//取得金牙，並且保留六顆鹼性電池
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
						
				//第6個城鎮
				case 6:
						{
								//事件三
								evenBuffer = RoleDataValue.nowEvenSchedule[2];//讀取目前事件進度(資料庫)
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
												makeWord.setAnimated(-2, mapImfor.getStartImage(this.evenPoint));//設定資料庫的結界狀態
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
						
				//第10個城鎮
				case 10:
						{
							//事件四
								evenBuffer = RoleDataValue.nowEvenSchedule[3];//讀取目前事件進度(資料庫)
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
												makeWord.setAnimated(-3, mapImfor.getStartImage(this.evenPoint));//設定資料庫的結界狀態
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
						
				//第8個城鎮
				case 8:
						{
							evenBuffer = RoleDataValue.nowEvenSchedule[4];//讀取目前事件進度(資料庫)
							if(! evenBuffer.equals("")) evenSchedule = split(evenBuffer, ",");
							
							//事件五，沙漠傳送師
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
											makeWord.setAnimated(-4, mapImfor.getStartImage(this.evenPoint));//設定資料庫的結界狀態
											mapImfor.setDBStop(3, this.evenPoint, false);
											RoleDataValue.evenFinished[4] = "1";
											
											//Write to DataBase
											ConnectionServer.updateGameEvent(1);
											
											try{ Thread.sleep(500); } catch(Exception e){}
											
											ConnectionServer.updateGameMap(3);
											
											try{ Thread.sleep(500); } catch(Exception e){}
											
											//移除金牙
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
				
				//第27個城鎮
				case 27:
						{
							//事件七
							//與看門魚戰鬥
							switch(npcNum)
							{
								case 12:
									{
										if((RoleDataValue.level >= 35) )//& (checkEvenSchedul(npcNum) == 0))
										{
											//戰鬥勝利或失敗
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
						
				//第30個城鎮
				case 30:
						{
							//事件八
							evenBuffer = RoleDataValue.nowEvenSchedule[7];//讀取目前事件進度(資料庫)
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
												makeWord.setAnimated(-2, mapImfor.getStartImage(this.evenPoint));//設定資料庫的結界狀態
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
						
				//第33個城鎮
				case 33:
						{
							//事件九
							evenBuffer = RoleDataValue.nowEvenSchedule[8];//讀取目前事件進度(資料庫)
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
												makeWord.setAnimated(-3, mapImfor.getStartImage(this.evenPoint));//設定資料庫的結界狀態
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
						
				//第38個城鎮
				case 38:
						{
							//事件十
							evenBuffer = RoleDataValue.nowEvenSchedule[9];//讀取目前事件進度(資料庫)
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
												//移除火星星
												mainValue.deleteItemNum("I2", 20);
												ConnectionServer.updateGameInf();
												
												try{ Thread.sleep(500); } catch(Exception e){}
												
												setTalkValue(4);
												makeWord.setAnimated(-3, mapImfor.getStartImage(this.evenPoint));//設定資料庫的結界狀態
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
						
				//第11個城鎮
				case 41:
						{
							//事件十一
							evenBuffer = RoleDataValue.nowEvenSchedule[10];//讀取目前事件進度(資料庫)
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
												//得到冰晶晶以及水星星
												mainValue.addItem("I4", false, 20);
												mainValue.addItem("I5", false, 10);
												ConnectionServer.updateGameInf();
												
												try{ Thread.sleep(500); } catch(Exception e){}
												
												setTalkValue(4);
												makeWord.setAnimated(-4, mapImfor.getStartImage(this.evenPoint));//設定資料庫的結界狀態
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
						
				//第43個城鎮
				case 43:
						{
							//事件十二
							evenBuffer = RoleDataValue.nowEvenSchedule[11];//讀取目前事件進度(資料庫)
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
												//移除月飽石，以及魚的飄飄
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
						
				//第45個城鎮
				case 45:
						{
							//事件十四
							evenBuffer = RoleDataValue.nowEvenSchedule[13];//讀取目前事件進度(資料庫)
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
												//移除冰的勒牌礦泉水以及水槍
												mainValue.deleteItemNum("E8", 1);
												mainValue.deleteItemNum("I12", 1);
												ConnectionServer.updateGameInf();
												
												try{ Thread.sleep(500); } catch(Exception e){}
												
												setTalkValue(4);
												makeWord.setAnimated(-3, mapImfor.getStartImage(this.evenPoint));//設定資料庫的結界狀態
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
						
				//第7個城鎮
				case 7:
						{
							//事件十六
							evenBuffer = RoleDataValue.nowEvenSchedule[15];//讀取目前事件進度(資料庫)
							if(! evenBuffer.equals("")) evenSchedule = split(evenBuffer, ",");
							
							switch(npcNum)
							{
								case 22://海外神殿守衛
									{
										if(RoleDataValue.evenFinished[15].equals("1")) setTalkValue(1);
										
										if((RoleDataValue.evenFinished[15].equals("0")) & (checkEvenSchedul(npcNum) == 1))
										{
											if(checkItem("E9", "1"))
											{
												setTalkValue(4);
												makeWord.setAnimated(-2, mapImfor.getStartImage(this.evenPoint));//設定資料庫的結界狀態
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
						
				//第17個城鎮
				case 17:
						{
							//事件十六
							evenBuffer = RoleDataValue.nowEvenSchedule[15];//讀取目前事件進度(資料庫)
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
											//得到神殿工作人員證明証
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
						
				//第50個城鎮
				case 50:
						{
							//事件十七
							evenBuffer = RoleDataValue.nowEvenSchedule[16];//讀取目前事件進度(資料庫)
							if(! evenBuffer.equals("")) evenSchedule = split(evenBuffer, ",");
							
							switch(npcNum)
							{
								case 26:
									{
										if(RoleDataValue.evenFinished[15].equals("1")) setTalkValue(0);
										
										if((RoleDataValue.level >= 79) & (checkEvenSchedul(npcNum) == 0))
										{
											setTalkValue(0);
											makeWord.setAnimated(-2, mapImfor.getStartImage(this.evenPoint));//設定資料庫的結界狀態
											mapImfor.setDBStop(1, this.evenPoint, false);
											RoleDataValue.evenFinished[15] = "1";
											addEvenBuffer(16, npcNum);
											//得到父親的家書
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
					
				//第31個城鎮
				case 31:
						{
							//事件十八
							evenBuffer = RoleDataValue.nowEvenSchedule[17];//讀取目前事件進度(資料庫)
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
												makeWord.setAnimated(-1, mapImfor.getStartImage(this.evenPoint));//設定資料庫的結界狀態
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
												//得到英雄的死人骨頭
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
						
				//第53個城鎮
				case 53:
						{
							//事件十九，與竿神戰鬥
							evenBuffer = RoleDataValue.nowEvenSchedule[18];//讀取目前事件進度(資料庫)
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
												
												//與竿神戰鬥
												
												//贏了竿神得到竿神(物品)，以及完成此事件
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
						
				//第63個城鎮
				case 63:
						{
							//事件二十
							evenBuffer = RoleDataValue.nowEvenSchedule[19];//讀取目前事件進度(資料庫)
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
												//得到山上的魚骨頭
												mainValue.addItem("E12", false, 1);
												ConnectionServer.updateGameInf();
												
												try{ Thread.sleep(500); } catch(Exception e){}
												
												setTalkValue(1);
												makeWord.setAnimated(-4, mapImfor.getStartImage(this.evenPoint));//設定資料庫的結界狀態
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
						
				//第45個城鎮
				case 67:
						{
							//事件二十二
							evenBuffer = RoleDataValue.nowEvenSchedule[21];//讀取目前事件進度(資料庫)
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
											 	makeWord.setAnimated(-1, mapImfor.getStartImage(this.evenPoint));//設定資料庫的結界狀態
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
	
	//設定事件點
	public void setEvenPoint(int evenPoint)
	{
		this.evenPoint = evenPoint;
	}
	
	//尋找是否已完成目前NPC之事件
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
	
	//設定NPC說什麼話
	public void setTalkValue(int talkAbout)
	{
		this.talkAbout = talkAbout;
	}
	
	//取得TalkAbout Value
	public int getTalkValue()
	{
		return talkAbout;
	}
	
	//把進度加入evenBuffer
	public void addEvenBuffer(int storyNum, int npcNum)
	{
		if(evenBuffer.equals("")) evenBuffer += npcNum;
		else evenBuffer += ","+npcNum;
		
		evenSchedule = split(evenBuffer, ",");
		RoleDataValue.nowEvenSchedule[storyNum] = evenBuffer;
		
		//寫入進度到資料庫
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
	
	//字串切割
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