package org.vsw.extract.entity;

public class Property {
	private String name ;
	private String value ;
	private String reference ;
	
	public Property() {} 
	
	public Property(String name, String value) {
		this.name = name;
		this.value = value ;
	}
	
	public String getName() { return name; }
	public void setName(String name) { this.name = name; }
	
	public String getValue() { return value; }
	public void setValue(String value) { this.value = value; }
	
	public String getReference() { return reference; }
	public void setReference(String reference) { this.reference = reference; }
}
