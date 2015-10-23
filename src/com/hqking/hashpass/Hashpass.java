package com.hqking.hashpass;

import java.security.NoSuchAlgorithmException;

public class Hashpass {

	/**
	 * @param args
	 * @throws NoSuchAlgorithmException 
	 */
	public static void main(String[] args) throws NoSuchAlgorithmException {
		Generator.setMasterKey("hqking");
		
		Generator generator = new Generator(Generator.tablePrintableAscii);
		Site site = new Site(generator, 12);
		site.bump = 0;
		
		String pass = site.password();
		System.out.println(pass);
		System.out.println(generator.entropy(pass.toCharArray()));
	}

}
