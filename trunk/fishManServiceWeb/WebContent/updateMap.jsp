<%@ page import = "java.sql.*,java.io.*" contentType="text/html;charset=BIG5" %>
 <%
   DataOutputStream dos = new DataOutputStream(response.getOutputStream());
   try{
   		//抓取資料
   		String userID = request.getParameter("userID").trim();
   		String pass = request.getParameter("userpass").trim();
   		String map_dir = request.getParameter("map_dir").trim();//地圖方向編號
   		String map_value = request.getParameter("map_value").trim();//地圖更新的值
		
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
		else{//更新地圖結界
			String comm = "";
			if(map_dir.equals("0")){
				comm = "UPDATE gameevent SET " + 
							  "map_north='" + map_value + "' " +
							  "WHERE userid=" + userID;
			}else if(map_dir.equals("1")){
				comm = "UPDATE gameevent SET " + 
							  "map_south='" + map_value + "' " +
							  "WHERE userid=" + userID;
			}else if(map_dir.equals("2")){
				comm = "UPDATE gameevent SET " + 
							  "map_west='" + map_value + "' " +
							  "WHERE userid=" + userID;
			}else if(map_dir.equals("3")){
				comm = "UPDATE gameevent SET " + 
							  "map_east='" + map_value + "' " +
							  "WHERE userid=" + userID;
			}
			System.out.println(userID+"-updateMap:" + map_value);
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