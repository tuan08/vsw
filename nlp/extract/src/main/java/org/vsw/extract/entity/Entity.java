package org.vsw.extract.entity;

import java.util.HashMap;
import java.util.Map;

public class Entity {
	private String   entityClass  ;
	private String   prefix ;
	private String   name ;
	private String[] variant ;
	private String[] type ;
	private Map<String, Property> properties = new HashMap<String, Property>() ;

	
}
