package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class JDBC {
	private Connection conn;
	public static final String mysqlAddress = "aalc2fcujr130q.czqnjok2u2ed.us-west-2.rds.amazonaws.com";
	public static final String dbUser = "lei";
	public static final String dbPassword = "11111111";
	public static final String dbName = "do_me_a_favor";

	public Connection getConn() throws ClassNotFoundException, SQLException {
		if (null != conn) {
			return conn;
		} else {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection("jdbc:mysql://" + mysqlAddress + ":3306/" + dbName, dbUser, dbPassword);
			System.out.println("connect success");
			return conn;
		}
	}
}