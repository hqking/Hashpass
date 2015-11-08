package com.hqking.hashpass;

class Site implements Comparable<Site> {
	String description;
	int bump;
	int length;
	String type;
	
	private String pass;
	
	private static final int DEFAULT_PASSWORD_LENGTH = 8;
	
	Site(String type, int length, int bump) {
		this.length = length;
		this.type = type;
		this.bump = bump;
		description = "";
	}
	
	Site(String type, int length) {
		this(type, length, 0);
	}
	
	Site(String type) {
		this(type, DEFAULT_PASSWORD_LENGTH, 0);
	}
	
	Site copy() {
		Site to = new Site(type, length, bump);
		to.description = description;
		
		return to;
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

	@Override
	public int compareTo(Site o) {
		if (o.description == description &&
				o.bump == bump &&
				o.length == length &&
				o.type == type) {
			return 0;
		} else {
			return -1;
		}
	}
}
