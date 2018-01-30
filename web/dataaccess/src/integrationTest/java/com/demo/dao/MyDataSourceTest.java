package com.demo.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.dbunit.Assertion;
import org.dbunit.DatabaseTestCase;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.database.QueryDataSet;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.operation.DatabaseOperation;
import org.apache.commons.io.FileUtils;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.demo.dao.MyDataSource;


public class MyDataSourceTest extends MyDataSource {
	
	private FlatXmlDataSet loadedDataSet;
    Connection jdbcConnection;
	
	
	public IDataSet getExternalDataSet() throws Exception
	{
		return getDataSet();
	}
	
	
    protected IDataSet getDataSet() throws Exception
    {
        loadedDataSet = new FlatXmlDataSetBuilder().build(new FileInputStream("src/integrationTest/resources/registry.xml"));
        return loadedDataSet;
    }

	public static final String TABLE_NAME = "agents";

    

    /**
     * Provide a connection to the database
     */
    protected IDatabaseConnection getConnection() throws Exception
    {
      
        

        jdbcConnection = DriverManager.getConnection("jdbc:mysql://localhost:3306/registry_test", "admin", "admin");
        return new DatabaseConnection(jdbcConnection);
    }

    /**
     * Load the data which will be inserted for the test
     * @throws SQLException 
     */
  //  protected IDataSet getDataSet() throws Exception
  //  {
  //  	FileInputStream fis = new FileInputStream("C:/foo.xml");
  //     return new FlatXmlDataSetBuilder().build(fis);
  //  }
    
  
    
    
    public void testMysqlConnection() throws SQLException{
    
    	jdbcConnection = MysqlConnection();
    	assertNotNull(jdbcConnection);
    	jdbcConnection.close();
    }
    /**
     * Sanity check that the data has been loaded
     */
    public void testCheckDataLoaded() throws Exception
    {
        assertNotNull(loadedDataSet);
        int rowCount = loadedDataSet.getTable(TABLE_NAME).getRowCount();
        System.out.println("rowcount: " + rowCount);
        assertEquals(6, rowCount);
    }

    /**
     * Show how a data set can be extracted and used to compare with the XML representation
     */
    public void testCompareDataSet() throws Exception
    {
        IDataSet createdDataSet = getConnection().createDataSet(new String[]
        {
            TABLE_NAME
        });
        Assertion.assertEquals(loadedDataSet, createdDataSet);
    }

    /**
     * Compare test data with query-generated IDataSet
     */
    public void testCompareQuery() throws Exception
    {
        QueryDataSet queryDataSet = new QueryDataSet(getConnection());
        queryDataSet.addTable(TABLE_NAME, "SELECT * FROM " + TABLE_NAME);
        Assertion.assertEquals(loadedDataSet, queryDataSet);
    }

    /**
     * Test the DbUnit export mechanism
     */
    public void testExportData() throws Exception
    {
        File queryFile = new File("query.xml");
        File loadedFile = new File("loaded.xml");
        IDataSet queryDataSet = getConnection().createDataSet(new String[]
        {
            TABLE_NAME
        });
        FlatXmlDataSet.write(queryDataSet, new FileOutputStream(queryFile));
        FlatXmlDataSet.write(loadedDataSet, new FileOutputStream(loadedFile));

        assertEquals(FileUtils.readFileToString(queryFile, "UTF8"), FileUtils.readFileToString(loadedFile, "UTF8"));

    }
    




	@Before
	public void setUp() throws Exception {
	     // initialize your database connection here

        
        IDatabaseConnection connection = getConnection();
        
        // initialize your dataset here

        getDataSet();



        try {

            DatabaseOperation.CLEAN_INSERT.execute(connection, loadedDataSet);

        } finally {

            connection.close();

        }
	}

	@After
	public void tearDown() throws Exception {
		if (jdbcConnection != null) {
			 jdbcConnection.close();
			
		}
	
	}

	@Test
	public final void test() {
	//	fail("Not yet implemented"); // TODO
	}

}
