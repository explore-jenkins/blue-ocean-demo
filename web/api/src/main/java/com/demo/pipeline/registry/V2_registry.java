package com.demo.pipeline.registry;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.QueryParam;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jettison.json.JSONArray;

import com.demo.dao.SchemaRegistry;

import java.util.logging.Level;
import java.util.logging.Logger;


@Path("v2/registry")
public class V2_registry {

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response returnSpeciesAgents(
		@QueryParam("species") String species)
        throws Exception 
	{
		String returnString = null;
		JSONArray json = new JSONArray();
		Logger logger = Logger.getLogger( V2_registry.class.getName() );		
		
		try {
			
			if (species == null) {
				return Response.status(400).entity("Error: please specify a species for this search.").build();
			}
			
			SchemaRegistry dao = new SchemaRegistry();
			json = dao.queryReturnSpeciesAgents(species);
				
			returnString = json.toString();
		
		}
		catch (Exception e) {
			logger.log(Level.INFO, "server was not able to process request to return agent species", e);
			return Response.status(500).entity("Server was not able to process your request").build();
		}
			
		return Response.ok(returnString).build();
	}
	
	@Path("{species}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response returnSpecies(
		@PathParam("species") String species)
        throws Exception 
	{
		String returnString = null;
		JSONArray json = new JSONArray();
		Logger logger = Logger.getLogger( V2_registry.class.getName() );	
		
		try {
			
					
			SchemaRegistry dao = new SchemaRegistry();
			json = dao.queryReturnSpeciesAgents(species);
				
			returnString = json.toString();
		
		}
		catch (Exception e) {
			logger.log(Level.INFO, "Could not get species list.", e);
			return Response.status(500).entity("Server was not able to process your request").build();
		}
			
		return Response.ok(returnString).build();
	}
	
	@Path("{species}/{name}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response returnSpeciesName(
		@PathParam("species") String species,
		@PathParam("name") String name)
        throws Exception 
	{
		String returnString = null;
		JSONArray json = new JSONArray();
		Logger logger = Logger.getLogger( V2_registry.class.getName() );	
		
		try {
			
					
			SchemaRegistry dao = new SchemaRegistry();
			json = dao.queryReturnSpeciesNameAgents(species, name);
			
		
				
			returnString = json.toString();
		
		}
		catch (Exception e) {
			logger.log(Level.INFO, "Could not get species by name.", e);
			return Response.status(500).entity("Server was not able to process your request").build();
		}
			
		return Response.ok(returnString).build();
	}
	
	@POST
	@Consumes({MediaType.APPLICATION_FORM_URLENCODED,MediaType.APPLICATION_JSON})

	@Produces(MediaType.APPLICATION_JSON)
	public Response addAgent(String incomingData) throws Exception {
		
		String returnString = null;
		
		SchemaRegistry dao = new SchemaRegistry();
		Logger logger = Logger.getLogger( V2_registry.class.getName() );	
		
		try {
			System.out.println("incomingData: " + incomingData);
			
			/*
			 * ObjectMapper is from Jackson Processor framework
			 * http://jackson.codehaus.org/
			 * 
			 * Using the readValue method, you can parse the json from the http request
			 * and data bind it to a Java Class.
			 */
			ObjectMapper mapper = new ObjectMapper();  
			AgentEntry agentEntry = mapper.readValue(incomingData, AgentEntry.class);
			
			int http_code = dao.insertIntoAgents(agentEntry.AGENT_NAME, 
												 agentEntry.AGENT_SPECIES, 
													agentEntry.SERVICE_START, 
													agentEntry.SERVICE_LAST, 
													agentEntry.ADVERSARY_NAME,
													agentEntry.ADVERSARY_TECH);
			
			if( http_code == 200 ) {
				returnString = "Item inserted";
			} else {
				return Response.status(500).entity("Unable to process Item").build();
			}
			
		} catch (Exception e) {
			logger.log(Level.INFO, "Could not add agent.", e);
			return Response.status(500).entity("Server was not able to process your request").build();
		}
		
		return Response.ok(returnString).build();
	}
}

	
	class AgentEntry {
		public String AGENT_NAME;
		public String AGENT_SPECIES;
		public String SERVICE_START;
		public String SERVICE_LAST;
		public String ADVERSARY_NAME;
		public String ADVERSARY_TECH;
	}
	



