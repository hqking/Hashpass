package com.hqking.hashpass;

import java.time.LocalTime;

class Site {
	String description;
	int bump;
	int length;
	final LocalTime createTime;
	private Generator generator;
	
	private static final int DEFAULT_PASSWORD_LENGTH = 8;
	
	Site(Generator generator, int length) {
		createTime = LocalTime.now();
		this.generator = generator;
		this.length = length;
		bump = 0;
	}
	
	Site(Generator generator) {
		this(generator, DEFAULT_PASSWORD_LENGTH);
	}
	
	String password(String key) {
		return generator.password(this, key);
	}
}
