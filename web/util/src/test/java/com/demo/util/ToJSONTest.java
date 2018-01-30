/**
 * 
 */
package com.demo.util;

import static org.junit.Assert.*;

import java.sql.Date;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;


import com.mockrunner.mock.jdbc.MockResultSet;

/**
 * @author sasbcl
 *
 */
public class ToJSONTest extends ToJSON {
	
    MockResultSet rs;

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		  rs = new MockResultSet("myMock");
	      rs.addColumn("id", new Integer[]{1});
	      rs.addColumn("NAME", new String[]{"Road Runner"});
	      rs.addColumn("SPECIES", new String[]{"Bird"});
	      Date startDate = Date.valueOf("2011-02-10");
	      rs.addColumn("SRVSTART", new Date[]{startDate});
	      Date lastDate = Date.valueOf("2014-03-11");
	      rs.addColumn("SRVLAST", new Date[]{lastDate});
	      rs.addColumn("ADVNAME", new String[]{"W.E. Coyote"});
	      rs.addColumn("ADVTECH", new String[]{"ACME gadget du jour"});
	}
	
	public boolean isJSONValid(String test) {
	    try {
	        new JSONObject(test);
	    } catch (JSONException ex) {
	     
	        try {
	            new JSONArray(test);
	        } catch (JSONException ex1) {
	            return false;
	        }
	    }
	    return true;
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
		rs.close();
	}

	/**
	 * Test method for {@link com.demo.util.ToJSON#toJSONArray(java.sql.ResultSet)}.
	 */
	@Test
	public final void testToJSONArray() {
		JSONArray json = new JSONArray();
		
		try {
			json = toJSONArray(rs);
		} catch (Exception e) {
			
			e.printStackTrace();
			fail("exception in processing toJSON");
		}
		
		String row1 = null;
		try {
			row1 = json.getJSONObject(0).toString();					
		} catch (JSONException e) {
			e.printStackTrace();
			fail("exception getting first row");
		}
		
		String expected = "{\"id\":1,\"NAME\":\"Road Runner\",\"SPECIES\":\"Bird\",\"SRVSTART\":\"2011-02-10\",\"SRVLAST\":\"2014-03-11\",\"ADVNAME\":\"W.E. Coyote\",\"ADVTECH\":\"ACME gadget du jour\"}";
	   	assertEquals(row1,expected);
		
		
	}

}
