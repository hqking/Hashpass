package com.hqking.hashpass;

import java.security.NoSuchAlgorithmException;

public class Hashpass {
	/**
	 * @param args
	 * @throws NoSuchAlgorithmException 
	 */
	public static void main(String[] args) throws NoSuchAlgorithmException {
		Generator.init("hqking");
		
		
		Site site = new Site("printableAscii", 12);
		site.bump = 0;
		
		String pass = site.password();
		System.out.println(pass);
		System.out.println(site.entropy());
		
		Validator validator = new Validator(pass);
		System.out.printf("number: %d\n", validator.numericCount());
		System.out.printf("lower letter: %d\n", validator.lowerLetterCount());
		System.out.printf("upper letter: %d\n", validator.upperLetterCount());
		System.out.printf("special char: %d\n", validator.specialCharCount());
		System.out.printf("invalid char: %d\n", validator.invalidCount());
		System.out.printf("score: %d\n", validator.score());
		
		StorageSqlite db = new StorageSqlite("test.db");
		
		Site ccb = db.find("ccb");
		if (ccb != null) {
			System.out.println(ccb.password());
			System.out.println(ccb.entropy());
		}
	}

}
