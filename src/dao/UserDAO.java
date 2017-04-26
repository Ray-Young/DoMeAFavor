package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

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
			data.setId(result.getInt("id"));
			data.setName(result.getString("name"));
			datas.add(data);
		}
		stmt.close();
		return datas;
	}

	public ArrayList<User> getUsers(String name) throws Exception {
		conn = new JDBC().getConn();
		String sql = "select * from user where name = '" + name + "';";
		PreparedStatement stmt = conn.prepareStatement(sql);
		ResultSet result = stmt.executeQuery();
		ArrayList<User> datas = new ArrayList<User>();
		while (result.next()) {

			User data = new User();
			// data.setId(result.getInt("id"));
			data.setName(result.getString("name"));
			datas.add(data);
		}
		stmt.close();
		return datas;
	}

	public void createUser(String name) {
		try {
			conn = new JDBC().getConn();
			String sql = "insert into user (name) values(?)";
			PreparedStatement stmt = conn.prepareStatement(sql);
			System.out.println("aa");
			stmt.setString(1, name);
			stmt.execute();
			stmt.close();
		} catch (Exception e) {
			System.err.println("ERROR: Exception in create User");

		}
	}
}
