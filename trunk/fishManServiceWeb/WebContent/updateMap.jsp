<%@ page import = "java.sql.*,java.io.*" contentType="text/html;charset=BIG5" %>
 <%
   DataOutputStream dos = new DataOutputStream(response.getOutputStream());
   try{
   		//������
   		String userID = request.getParameter("userID").trim();
   		String pass = request.getParameter("userpass").trim();
   		String map_dir = request.getParameter("map_dir").trim();//�a�Ϥ�V�s��
   		String map_value = request.getParameter("map_value").trim();//�a�ϧ�s����
		
		//�s�u���Ʈw
    	String url = "jdbc:mysql://localhost:3306/sotdatabase";
   		String username = "root";
   		String password = "";
       	Class.forName("com.mysql.jdbc.Driver");
       	
		Connection conn = DriverManager.getConnection(url,username,password);
		
		Statement state = conn.createStatement();
		
		ResultSet rs = state.executeQuery("SELECT * FROM user_Information WHERE userid='" + userID + "'");
		
		//�P�_�K�X�O�_���T
		if(!rs.next())
			dos.writeUTF("NoUser");
		else if(!rs.getString("password").equals(pass))
			dos.writeUTF("PassError");
		else{//��s�a�ϵ���
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