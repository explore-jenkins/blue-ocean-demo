package com.demo.dao;

import static org.junit.Assert.*;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.dbunit.DatabaseUnitException;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.database.QueryDataSet;
import org.dbunit.dataset.DataSetException;
import org.dbunit.dataset.xml.FlatXmlDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.operation.DatabaseOperation;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Calendar;
import java.text.SimpleDateFormat;




public class SchemaRegistryTest extends SchemaRegistry {

	private static Connection jdbcConnection = null;
	private static final String TABLE_NAME = "agents";
	private static final String DATE_FORMAT_NOW = "yyyy-MM-dd";

	public static String now() {
	  Calendar cal = Calendar.getInstance();
	  SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_NOW);
	  return sdf.format(cal.getTime());
	}
	
	
	@Before
	public void setUp() throws Exception {
	
    
	}

	@After
	public void tearDown() throws Exception {
		if (jdbcConnection != null) {
			 jdbcConnection.close();
			
		}
	}
	
	 /**
     * Provide a connection to the database
     */
    protected IDatabaseConnection getConnection() throws Exception
    {
 	 

        jdbcConnection = DriverManager.getConnection("jdbc:mysql://localhost:3306/registry_test", "admin", "admin");
        return new DatabaseConnection(jdbcConnection);
    }
    
    

	@Test
	public final void testInsertIntoAgents() throws DatabaseUnitException, SQLException, Exception {
	 insertIntoAgents("Woody Woodpecker","bird","1955-01-20","1995-02-15","Hard trees","bad puns");
         QueryDataSet queryDataSet = new QueryDataSet(getConnection());
         queryDataSet.addTable(TABLE_NAME, "SELECT * FROM " + TABLE_NAME);
         int rowCount = queryDataSet.getTable(TABLE_NAME).getRowCount();
         System.out.println("rowcount: " + rowCount);
         assertEquals(7, rowCount);
	}

	@Test
	public final void testDeleteFromAgents() throws DatabaseUnitException, SQLException, Exception {
	 deleteFromAgents("Woody Woodpecker","bird");	 
         QueryDataSet queryDataSet = new QueryDataSet(getConnection());
         queryDataSet.addTable(TABLE_NAME, "SELECT * FROM " + TABLE_NAME);
         int rowCount = queryDataSet.getTable(TABLE_NAME).getRowCount();
         System.out.println("rowcount: " + rowCount);
         assertEquals(6, rowCount);
	}

	@Test
	public final void testQueryReturnSpeciesAgents() throws DataSetException, SQLException, Exception {
	 queryReturnSpeciesAgents("dog");
	 QueryDataSet queryDataSet = new QueryDataSet(getConnection());
         queryDataSet.addTable(TABLE_NAME, "SELECT * FROM " + TABLE_NAME + " WHERE SPECIES = 'dog'");
         int rowCount = queryDataSet.getTable(TABLE_NAME).getRowCount();
         System.out.println("rowcount: " + rowCount);
         assertEquals(2, rowCount);

	}

	@Test
	public final void testQueryReturnSpeciesNameAgents() throws DataSetException, SQLException, Exception {
	 queryReturnSpeciesNameAgents("dog","Scooby");
	 QueryDataSet queryDataSet = new QueryDataSet(getConnection());
         queryDataSet.addTable(TABLE_NAME, "SELECT * FROM " + TABLE_NAME + " WHERE SPECIES = 'dog' AND NAME = 'Scooby'");
         int rowCount = queryDataSet.getTable(TABLE_NAME).getRowCount();
         System.out.println("rowcount: " + rowCount);
         assertEquals(1, rowCount);
	}

	@Test
	public final void testUpdateAgentLastServiceDate() throws DatabaseUnitException, SQLException, Exception {
	 String strNow = now();
	 updateAgentLastServiceDate("Scooby",strNow);
	 QueryDataSet queryDataSet = new QueryDataSet(getConnection());
         queryDataSet.addTable(TABLE_NAME, "SELECT * FROM " + TABLE_NAME + " WHERE NAME = 'Scooby' AND SRVLAST = '" + strNow + "'");
         int rowCount = queryDataSet.getTable(TABLE_NAME).getRowCount();
         System.out.println("rowcount: " + rowCount + "srvlast: " + strNow);
         assertEquals(1, rowCount);
		
	}

}
