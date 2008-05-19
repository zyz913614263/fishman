<%@ page import = "java.sql.*,java.io.*" contentType="text/html;charset=BIG5" %>
 <%
   DataOutputStream dos = new DataOutputStream(response.getOutputStream());
   try{
   		//抓取資料
   		String userID = request.getParameter("userID").trim();
   		String pass = request.getParameter("userpass").trim();
   		String gameInf = request.getParameter("gameInf").trim();
		
		//連線到資料庫
    	String url = "jdbc:mysql://localhost:3306/sotdatabase";
   		String username = "root";
   		String password = "";
       	Class.forName("com.mysql.jdbc.Driver");
       	
		Connection conn = DriverManager.getConnection(url,username,password);
		
		Statement state = conn.createStatement();
		
		ResultSet rs = state.executeQuery("SELECT * FROM user_Information WHERE userid='" + userID + "'");
		
		//判斷密碼是否正確
		if(!rs.next())
			dos.writeUTF("NoUser");
		else if(!rs.getString("password").equals(pass))
			dos.writeUTF("PassError");
		else{//更新使用者資料
			//System.out.println(gameInf);
			String tmp[] = gameInf.split(":");
			String comm = "UPDATE user_game_information SET " + 
						  "gameFame='" + tmp[0] + "',"+
						  "level='" + tmp[1] + "',"+
						  "exp='" + tmp[2] + "',"+
						  "expNext='" + tmp[3] + "',"+
						  "atk='" + tmp[4] + "',"+
						  "def='" + tmp[5] + "',"+
						  "agi='" + tmp[6] + "',"+
						  "nowHP='" + tmp[7] + "',"+
						  "nowXP='" + tmp[8] + "',"+
						  "maxHP='" + tmp[9] + "',"+
						  "killMonster='" + tmp[10] + "',"+
						  "equipment='" + tmp[11] + "',"+
						  "money='" + tmp[12] + "',"+
						  "roleX='" + tmp[13] + "',"+
						  "roleY='" + tmp[14] + "',"+
						  "roleMap='" + tmp[15] + "',"+
						  "roleItem='" + tmp[16] + "' "+
						  "WHERE userid=" + userID;
			System.out.println(userID+"-SetGameInf"+gameInf);
			int row = state.executeUpdate(comm);
			dos.writeUTF("");
		}
		dos.flush();
			
		dos.close();
		rs.close();
		state.close();
		conn.close();
	}
	catch(Exception e){
		out.println(e);
	}
 %>