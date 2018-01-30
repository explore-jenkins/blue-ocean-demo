package com.demo.dao;

import java.sql.Connection;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.io.File;

import java.io.PrintStream;
import java.io.PrintWriter;



import javax.sql.*;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;


public class MyDataSource {
	

	
	private static MysqlDataSource mysqlDS = null;


		
    public static DataSource getMySQLDataSource() throws Exception {
    	
    	Logger logger = Logger.getLogger(MyDataSource.class.getName() );		
              
        if (mysqlDS != null) {
			return mysqlDS;	
		}
    
        String strMySQLHost = "";
        String strMySQLPort = "";
        String strMySQLDatabase = "";
          
        try {
        	
           // TO-DO Change to load from a general properties file - once we figure out where that should be
           // is = new URL("http://localhost:8080/static/db.properties").openStream();
           // props.load(is);
            mysqlDS = new MysqlDataSource();

            strMySQLHost = System.getenv("MYSQL_PORT_3306_TCP_ADDR");
            if(strMySQLHost == null || strMySQLHost.isEmpty()) {
            	strMySQLHost = "localhost";
            }
            strMySQLPort = System.getenv("MYSQL_PORT_3306_TCP_PORT");
            if(strMySQLPort == null || strMySQLPort.isEmpty()) {
            	strMySQLPort = "3306";
            }
            strMySQLDatabase = System.getenv("MYSQL_ENV_MYSQL_DATABASE");
            if(strMySQLDatabase == null || strMySQLDatabase.isEmpty()) {
            	strMySQLDatabase = "registry";
            }
            mysqlDS.setURL("jdbc:mysql://"+strMySQLHost+":"+strMySQLPort+"/"+strMySQLDatabase);
            mysqlDS.setUser("admin");
            mysqlDS.setPassword("admin");
            
            logger.log(Level.INFO, "Attempting to use jdbc:mysql://"+strMySQLHost+":"+strMySQLPort+"/"+strMySQLDatabase);
           } catch (Exception e) {
        	   
        	   logger.log(Level.SEVERE, "Could not access database via connect string jdbc:mysql://"+strMySQLHost+":"+strMySQLPort+"/"+strMySQLDatabase,e);
           
           
            
            
        }
        return mysqlDS;
    }
    
    protected static Connection MysqlConnection() {
    	Connection conn = null;
    	try {
    		conn = MyDataSource.getMySQLDataSource().getConnection();
    		return conn;
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    	return conn;
    }
    	
    

}
