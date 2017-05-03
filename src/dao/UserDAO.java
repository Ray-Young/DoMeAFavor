package dao;

import java.io.InputStream;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Base64;

import org.apache.commons.io.IOUtils;

import dto.User;

public class UserDAO {
	private Connection conn;

	public ArrayList<User> getUsers() throws Exception {
		conn = new JDBC().getConn();
		String sql = "select * from user;";
		PreparedStatement stmt = conn.prepareStatement(sql);
		ResultSet result = stmt.executeQuery();
		ArrayList<User> datas = new ArrayList<User>();
		while (result.next()) {

			User data = new User();
			data.setName(result.getString("name"));
			data.setId(result.getInt("userID"));
			System.out.println(data.getId());
			data.setEmail(result.getString("email"));
			data.setCredit(result.getInt("credit"));
			datas.add(data);
		}
		stmt.close();
		conn.close();
		return datas;
	}

	public ArrayList<User> getUsers(String fbToken) throws Exception {
		conn = new JDBC().getConn();
		String sql = "select * from user where fbToken = '" + fbToken + "';";
		PreparedStatement stmt = conn.prepareStatement(sql);
		ResultSet result = stmt.executeQuery();
		ArrayList<User> datas = new ArrayList<User>();
		while (result.next()) {

			User data = new User();
			// data.setId(result.getInt("id"));
			data.setName(result.getString("name"));
			data.setCredit(result.getInt("credit"));
			data.setFBToken(result.getString("fbToken"));
			//data.setId(result.getInt("userID"));
			data.setEmail(result.getString("email"));
			System.out.println("User DAO 1----"+result.getString("fbToken"));
			System.out.println("USer DAO 2------"+data.getFBToken());
			data.setId(result.getInt("userID"));
			System.out.println(data.getId()+"------~~~~----~~~~-----~");
			data.setEmail(result.getString("email"));
			datas.add(data);
			System.out.println(datas);
		}
		stmt.close();
		conn.close();
		return datas;
	}

	public void createUser(String name, String email, String token) {
		try {
			conn = new JDBC().getConn();
			String sql = "insert into user (name,email,fbToken,credit) values(?,?,?,?)";
			System.out.println(sql+"------------");
			PreparedStatement stmt = conn.prepareStatement(sql);
			System.out.println("aa");
			stmt.setString(1, name);
			stmt.setString(2,email);
			stmt.setString(3, token);
			stmt.setInt(4, 0);
			stmt.execute();
			stmt.close();
			conn.close();
		} catch (Exception e) {
			System.err.println("ERROR: Exception in create User");
			e.printStackTrace();

		}
	}
	
	public User getUser2(int userID){
		User user = new User();
		try{
			conn = new JDBC().getConn();
			String sql = "select * from user where userID="+userID;
			PreparedStatement stmt = conn.prepareStatement(sql);
			ResultSet result = stmt.executeQuery();
			result.next();
			user.setName(result.getString("name"));
			user.setId(result.getInt("userID"));
			user.setFBToken(result.getString("fbToken"));
			user.setEmail(result.getString("email"));
			user.setCredit(result.getInt("credit"));
			InputStream is = result.getBinaryStream("fbPhoto");
			byte[] imageBytes = IOUtils.toByteArray(is);
			 
            String base64String = Base64.getEncoder().encodeToString(imageBytes);
            user.setFbPhoto(base64String);
            //System.out.println("here detial");
			stmt.close();
			conn.close();
		}catch(Exception e){
			e.printStackTrace();
		}
		return user;
	}
	
	public String addUser(String name, String email, String fbToken, byte[] imageBytes){
		System.out.println("Here!");
		try{
			System.out.println(fbToken);
			conn = new JDBC().getConn();
			String sql = "select * from user where fbToken="+fbToken;
			PreparedStatement stmt = conn.prepareStatement(sql);
			ResultSet result = stmt.executeQuery();
			System.out.println("Here 1.5!!!");
			if(result.next()){
				stmt.close();
				return "Exist";
			}
			sql = "insert into user (name, credit, email, fbToken,fbPhoto) values(?,?,?,?,?)";
			System.out.println(sql);
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, name);
			stmt.setInt(2, 0);
			stmt.setString(3, email);
			stmt.setString(4, fbToken);
			System.out.println(imageBytes==null);
			Blob blob = new javax.sql.rowset.serial.SerialBlob(imageBytes);
			System.out.println(blob==null);
			stmt.setBlob(5, blob);
			stmt.execute();
			stmt.close();
			System.out.println("Here 2!");
			
			
		}catch(Exception e){
			e.printStackTrace();
		}
		return "Success";
	}
}
