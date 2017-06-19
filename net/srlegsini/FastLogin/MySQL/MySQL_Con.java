package net.srlegsini.FastLogin.MySQL;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.concurrent.TimeUnit;

import net.md_5.bungee.BungeeCord;
import net.srlegsini.FastLogin.MClass;

public class MySQL_Con {
	
	public static Connection con;
	public static String host, port, db, user, pass;
	public static Statement stmt;
	
	public static void openConnection() throws SQLException, ClassNotFoundException {
	    if (con != null && !con.isClosed()) {
	        return;
	    }

	    synchronized (MClass.class) {
	        if (con != null && !con.isClosed()) {
	            return;
	        }
	        Class.forName("com.mysql.jdbc.Driver");
	        con = (Connection) DriverManager.getConnection("jdbc:mysql://" + host+ ":" + port + "/" + db, user, pass);
	    }
	    
	    initialize();
	}
	
	private static void initialize() {
		try {
			Statement stmt = con.createStatement();
			stmt.executeUpdate(MClass.sqlCreateMainTable);
			stmt.executeUpdate(MClass.sqlMainTableAddition);
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static void antiClose() {
		BungeeCord.getInstance().getScheduler().schedule(MClass.plugin, new Runnable() {
			@Override
			public void run() {
				try {
					stmt.executeUpdate("update `Users` set PlayerName = 'SrLegsini' where PlayerName = 'SrLegsini'");
				} catch (SQLException e) {
				}
			}
		}, 1, 1, TimeUnit.HOURS);
	}
}
