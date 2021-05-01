package com.ManifestTeswTancis.dtos;

public class ErrorClass {
	String code;
	String description;
	
	
	public ErrorClass() {
		
	}
	
	
	public ErrorClass(String code, String description) {
		super();
		this.code = code;
		this.description = description;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}


	@Override
	public String toString() {
		return "ErrorClass [code=" + code + ", description=" + description + "]";
	}
	
	
}
