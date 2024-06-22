package com.example.sb.camel;

public enum CamelConstantEnum {
	CONST_PREDICATES("predicates"),
	CONST_PREDICATE_NAME("name"),
	CONST_PREDICATE_ARGS("args"),
	CONST_PREDICATE_FACTORY("factory"),
	CONST_UUID("uuid"),
	CONST_FACTS("facts"),
	CONST_QUERY("query"),
	CONST_RESULT("result"),
	CONST_VARIABLES("variables");

	private String headerName;
	
	CamelConstantEnum(String headerName) {
		this.headerName = headerName;
	}
	
	public String toString() {
		return headerName;
	}
}
