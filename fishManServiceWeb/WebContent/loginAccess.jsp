<%@ page import = "java.sql.*,java.io.*" contentType="text/html;charset=BIG5" %>
 <%
   DataOutputStream dos = new DataOutputStream(response.getOutputStream());
   try{
   		//������
   		String name = request.getParameter("username").trim();
   		String pass = request.getParameter("userpass").trim();
   		
   		//�s�u���Ʈw
    	String url = "jdbc:mysql://localhost:3306/sotdatabase";
   		String username = "root";
   		String password = "";
       	Class.forName("com.mysql.jdbc.Driver");
       	
		Connection conn = DriverManager.getConnection(url,username,password);
		
		Statement state = conn.createStatement();
		
		ResultSet rs = state.executeQuery("SELECT * FROM user_Information WHERE username='" + name + "'");
		
		//�P�_�O�_�����ϥΪ�
		if(!rs.next()){
			dos.writeUTF("NoUser");
		}//�K�X���~
		else if(!rs.getString("password").equals(pass)){
			dos.writeUTF("PassError|0|0");
		}//�n�J���\
		else{
			dos.writeUTF("LoginSucess|"+rs.getString(1)+"|"+rs.getString(4));
			System.out.println(name+"-Sucess");
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