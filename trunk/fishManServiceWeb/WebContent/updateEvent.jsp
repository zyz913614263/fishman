<%@ page import = "java.sql.*,java.io.*" contentType="text/html;charset=BIG5" %>
 <%
   DataOutputStream dos = new DataOutputStream(response.getOutputStream());
   try{
   		String userID = request.getParameter("userID").trim();
   		String pass = request.getParameter("userpass").trim();
   		String kind = request.getParameter("kind").trim();//地圖方向編號
   		String event_value = request.getParameter("event_value").trim();//地圖更新的值
		
		//連線到資料庫
    	String url = "jdbc:mysql://localhost:3306/sotdatabase";
   		String username = "root";
   		String password = "";
       	Class.forName("com.mysql.jdbc.Driver");
       	
		Connection conn = DriverManager.getConnection(url,username,password);
		
		Statement state = conn.createStatement();
		
		ResultSet rs = state.executeQuery("SELECT * FROM user_Information WHERE userid='" + userID + "'");
		
		//判斷帳號密碼是否正確
		if(!rs.next())
			dos.writeUTF("NoUser");
		else if(!rs.getString("password").equals(pass))
			dos.writeUTF("PassError");
		else{//更新事件資料庫
			String comm = "";
			if(kind.equals("0")){
				comm = "UPDATE gameevent SET " + 
							  "treasureEvent='" + event_value + "' " +
							  "WHERE userid=" + userID;
			}else if(kind.equals("1")){
				comm = "UPDATE gameevent SET " + 
							  "event_OK='" + event_value + "' " +
							  "WHERE userid=" + userID;
			}else if(kind.equals("2")){
				comm = "UPDATE gameevent SET " + 
							  "nowEventPoint='" + event_value + "' " +
							  "WHERE userid=" + userID;
			}
			System.out.println(userID+"-updateEvent:"+event_value);
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