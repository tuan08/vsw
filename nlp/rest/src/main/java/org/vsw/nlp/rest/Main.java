package org.vsw.nlp.rest;

import com.sun.grizzly.http.SelectorThread;
import com.sun.jersey.api.container.grizzly.GrizzlyWebContainerFactory;
import com.sun.jersey.api.json.JSONConfiguration;

import java.io.IOException;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import javax.ws.rs.core.UriBuilder;

public class Main {
	private static int getPort(int defaultPort) {
		String port = System.getenv("JERSEY_HTTP_PORT");
		if (null != port) {
			try {
				return Integer.parseInt(port);
			} catch (NumberFormatException e) {
			}
		}
		return defaultPort;        
	} 

	private static URI getBaseURI() {
		return UriBuilder.fromUri("http://localhost/").port(getPort(9998)).build();
	}

	public static final URI BASE_URI = getBaseURI();

	public static SelectorThread startServer() throws IOException {
		final Map<String, String> params = new HashMap<String, String>();
		params.put("com.sun.jersey.config.property.packages", "org.headvances.nlp.rest.resources");
		params.put(JSONConfiguration.FEATURE_POJO_MAPPING, "true") ;
		JSONConfiguration.DEFAULT.natural().build();
		System.out.println("Starting grizzly...");
		SelectorThread threadSelector = GrizzlyWebContainerFactory.create(BASE_URI, params);     
		return threadSelector;
	}

	public static void main(String[] args) throws IOException {
		SelectorThread threadSelector = startServer();
		System.out.println(String.format("Jersey app started with WADL available at "
				+ "%sapplication.wadl\nTry out %shelloworld\nHit enter to stop it...",
				BASE_URI, BASE_URI));
		System.in.read();
		threadSelector.stopEndpoint();
	}    
}