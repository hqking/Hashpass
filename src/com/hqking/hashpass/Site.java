package com.hqking.hashpass;

import java.time.LocalTime;

class Site {
	String description;
	int bump;
	int length;
	final LocalTime createTime;
	String type;
	
	private String pass;
	
	private static final int DEFAULT_PASSWORD_LENGTH = 8;
	
	Site(String type, int length) {
		createTime = LocalTime.now();
		this.length = length;
		this.type = type;
		bump = 0;
	}
	
	Site(String type) {
		this(type, DEFAULT_PASSWORD_LENGTH);
	}
	
	String password() {
		if (pass == null)
			pass = Generator.password(this);
		
		return pass;
	}
	
	int entropy() {
		if (pass == null)
			pass = Generator.password(this);
		
		return Generator.entropy(pass, type);
	}
}
