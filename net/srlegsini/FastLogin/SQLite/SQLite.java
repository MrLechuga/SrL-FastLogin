package net.srlegsini.FastLogin.SQLite;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;

import net.srlegsini.FastLogin.MClass;


public class SQLite {
    static String dbname;
    public static Connection connection;
    public static MClass plugin;

    //SQL creation stuff, You can leave the blow stuff untouched.
    public static Connection getSQLConnection() {
        File dataFolder = new File(MClass.plugin.getDataFolder(), dbname+".db");
        if (!dataFolder.exists()){
            try {
                dataFolder.createNewFile();
            } catch (IOException e) {
                plugin.getLogger().log(Level.SEVERE, "File write error: "+dbname+".db");
            }
        }
        try {
            if(connection!=null&&!connection.isClosed()){
                return connection;
            }
            
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:" + dataFolder);
            return connection;
        } catch (SQLException ex) {
            plugin.getLogger().log(Level.SEVERE,"SQLite exception on initialize", ex);
            ex.printStackTrace();
        } catch (ClassNotFoundException ex) {
            plugin.getLogger().log(Level.SEVERE, "You need the SQLite JBDC library. Google it. Put it in /lib folder.");
            ex.printStackTrace();
        }
        
        return null;
    }

    public static void load() {
        plugin = MClass.plugin;
        dbname = MClass.config.getString("Backend.SQLite.FileName");
    	
        connection = getSQLConnection(); 
        try {
            Statement s = connection.createStatement();
            s.executeUpdate(MClass.sqlCreateMainTable);
            s.executeUpdate(MClass.sqlMainTableAddition);
            s.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}