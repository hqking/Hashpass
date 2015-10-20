package com.hqking.hashpass;

import java.security.NoSuchAlgorithmException;

public class Hashpass {

	/**
	 * @param args
	 * @throws NoSuchAlgorithmException 
	 */
	public static void main(String[] args) throws NoSuchAlgorithmException {
		Generator generator = new Generator();
		Site site = new Site(generator, 12);
		site.bump = 3;
		
		System.out.println(site.password("hqking"));
	}

}
