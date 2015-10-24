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
		
		Validator validator = new Validator(pass);
		System.out.printf("number: %d\n", validator.numericCount());
		System.out.printf("lower letter: %d\n", validator.lowerLetterCount());
		System.out.printf("upper letter: %d\n", validator.upperLetterCount());
		System.out.printf("special char: %d\n", validator.specialCharCount());
		System.out.printf("invalid char: %d\n", validator.invalidCount());
		System.out.printf("score: %d\n", validator.score());
	}

}
