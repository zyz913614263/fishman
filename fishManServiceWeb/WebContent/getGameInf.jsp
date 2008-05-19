<%@ page import = "java.sql.*,java.io.*" contentType="text/html;charset=BIG5" %>
 <%
   DataOutputStream dos = new DataOutputStream(response.getOutputStream());
   try{
   		//抓取資料
   		String userID = request.getParameter("userID").trim();
   		String pass = request.getParameter("userpass").trim();
   		String gameName = request.getParameter("gameName").trim();
   		
   		//連線資料庫
    	String url = "jdbc:mysql://localhost:3306/sotdatabase";
   		String username = "root";
   		String password = "";
       	Class.forName("com.mysql.jdbc.Driver");
       	
		Connection conn = DriverManager.getConnection(url,username,password);
		
		Statement state = conn.createStatement();
		
		ResultSet rs = state.executeQuery("SELECT * FROM user_Information WHERE userid='" + userID + "'");
		
		//判斷是否密碼正確
		if(!rs.next()){
			dos.writeUTF("error!!");
		}
		else if(!rs.getString("password").equals(pass)){
			dos.writeUTF("PassError");
		}
		else{
			//判斷為第一次遊戲時,建立初始使用者資料
			int row = 0;
			if(rs.getString(4).equals("false")){
				String comm = "UPDATE user_Information SET firstgame='true' WHERE userid=" + userID;
				row = state.executeUpdate(comm);
				comm = "INSERT INTO user_game_information (userid,gameName,gameFame,level,exp,expNext,atk,def,agi,nowHP,nowXP,maxHP,killMonster,equipment,money,roleX,roleY,roleMap,roleItem)" +
					   " VALUES (" + userID + ",'" + gameName + "','1','1','0','718','0','0','0','150','0','150','0','1,0,0,0,0,0,0,0','500','299','267','0','I9-1,P1-1')";
				//System.out.println(comm);
				row = state.executeUpdate(comm);
				comm = "INSERT INTO gameevent (userid,map_north,map_south,map_west,map_east,treasureEvent,event_OK,nowEventPoint)"+
					   " VALUES (" + userID + ",'1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,1,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0',"+//map_north
					   "'1,0,0,0,0,0,1,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0',"+
					   "'1,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,1,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0',"+
					   "'1,0,0,0,1,1,0,0,1,0,0,0,0,0,0,0,0,1,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,1,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0',"+
					   "'0,1,1,0,0,0,0,0,1,1,0,1,0,1,1,0,0,1,0,0,0,0,0,0,1,1,0,1,0,0,0,0,0,1,0,0,0,1,1,0,1,0,0,0,1,0,0,0,0,1,1,0,0,1,1,0,0,0,1,0,0,0,0,1,0,0,0,0',"+ //treasureEvent
					   "'0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0',"+
					   "'-1:-1:-1:-1:-1:-1:-1:-1:-1:-1:-1:-1:-1:-1:-1:-1:-1:-1:-1:-1:-1:-1:-1')";
				row = state.executeUpdate(comm);
			}
			//抓取遊戲狀態,送回手機端
			rs = state.executeQuery("SELECT * FROM user_game_information WHERE userid='" + userID + "'");
			rs.next();
			String str = "";
			for(int i=1;i<=18;i++)
				str += rs.getString(i)+"|";
			str += rs.getString(19)+"|";
			//抓取遊戲map結界
			rs = state.executeQuery("SELECT * FROM gameevent WHERE userid='" + userID + "'");
			rs.next();
			//地圖結界
			str += rs.getString("map_north")+"|"+rs.getString("map_south")+"|"+rs.getString("map_west")+"|"+rs.getString("map_east")+"|";
			str += rs.getString("treasureEvent")+"|"+rs.getString("event_OK")+"|"+rs.getString("nowEventPoint");
			System.out.println(userID+"-getGameInf");
			dos.writeUTF("OK|"+str);
		}
		dos.flush();
			
		dos.close();
		rs.close();
		state.close();
		conn.close();
	}
	catch(Exception e){
		e.printStackTrace();
	}

 %>