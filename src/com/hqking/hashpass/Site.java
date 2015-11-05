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
	
	Site(String type, int length, int bump) {
		createTime = LocalTime.now();
		this.length = length;
		this.type = type;
		this.bump = bump;		
	}
	
	Site(String type, int length) {
		this(type, length, 0);
	}
	
	Site(String type) {
		this(type, DEFAULT_PASSWORD_LENGTH, 0);
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
