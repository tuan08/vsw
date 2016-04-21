package org.vsw.nlp.rest.resources;

import com.sun.grizzly.http.SelectorThread;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;

import org.codehaus.jettison.json.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.vsw.nlp.rest.Main;

import static org.junit.Assert.*;

public class TopicResourceUnitTest {
	SelectorThread threadSelector ;
	
	@Before
	public void init() throws Exception {
		threadSelector = Main.startServer();
	}
	
	@After
	public void  destroy() throws Exception {
		threadSelector.stopEndpoint();
	}
	
	@Test
	public void testGetTopic() {
		Client c = Client.create();
		WebResource webResource = c.resource(Main.BASE_URI);
		String responseMsg = webResource.path("/topic").get(String.class);
		assertEquals("Hello World", responseMsg);
	}
	
	@Test
	public void testSaveTopic() throws Exception {
		Client c = Client.create();
		WebResource webResource = c.resource(Main.BASE_URI);
		JSONObject myObject = new JSONObject();
		myObject.put("name", "Agamemnon");
		myObject.put("age", 32);
		webResource.path("/topic").put(myObject) ;
		webResource.path("/topic").put(JSONObject.class, myObject) ;
		webResource.path("/topic").put("{\"name\": \"Tuan Nguyen\"}") ;
	}
}