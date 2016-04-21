package org.vsw.nlp.rest.resources;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

@Path("/topic")
public class TopicResource {
	@GET 
	@Produces("text/plain")
	public String query(@QueryParam("q") String q) {
		return "Hello World";
	}
	
	@GET 
	@Path("/md5:{id}")
	@Produces({"application/json"})
	public JSONObject get(@PathParam("id") String id) {
		JSONObject myObject = new JSONObject();
		try {
			myObject.put("name", "Agamemnon");
			myObject.put("age", 32);
			myObject.put("md5Id", id);
		} catch (JSONException ex) {
			ex.printStackTrace() ;
		}
		return myObject;
	}
	
	@PUT 
	@Produces({"application/json"})
	@Consumes({"application/json"})
	public JSONObject save(JSONObject jsobject) {
		System.out.println("JSONObject: " + jsobject.toString());
		return jsobject;
	}
	
	@PUT 
	@Produces({"application/json"})
	@Consumes({"text/plain"})
	public JSONObject save(String jsobject) {
		System.out.println("String JSONObject: " + jsobject.toString());
		return null ;
	}
}