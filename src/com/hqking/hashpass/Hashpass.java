package com.hqking.hashpass;

import java.security.NoSuchAlgorithmException;

public class Hashpass {
	private static Generator printableAsciiGen;
	private static Generator alphaNumericGen;
	private static Generator numericGen;
	
	public static Generator findGenerator(String table) {
		if (table.compareTo("numeric") == 0) {
			return numericGen;
		} else if (table.compareTo("alphaNumeric") == 0) {
			return alphaNumericGen;
		} else if (table.compareTo("printableAscii") == 0) {
			return printableAsciiGen;
		} else {
			return printableAsciiGen;
		}
	}
	
	/**
	 * @param args
	 * @throws NoSuchAlgorithmException 
	 */
	public static void main(String[] args) throws NoSuchAlgorithmException {
		Generator.setMasterKey("hqking");
		
		printableAsciiGen = new Generator(Generator.tablePrintableAscii);
		alphaNumericGen = new Generator(Generator.tableAlphaNumeric);
		numericGen = new Generator(Generator.tableNumeric);
		
		Site site = new Site(printableAsciiGen, 12);
		site.bump = 0;
		
		String pass = site.password();
		System.out.println(pass);
		System.out.println(printableAsciiGen.entropy(pass.toCharArray()));
		
		Validator validator = new Validator(pass);
		System.out.printf("number: %d\n", validator.numericCount());
		System.out.printf("lower letter: %d\n", validator.lowerLetterCount());
		System.out.printf("upper letter: %d\n", validator.upperLetterCount());
		System.out.printf("special char: %d\n", validator.specialCharCount());
		System.out.printf("invalid char: %d\n", validator.invalidCount());
		System.out.printf("score: %d\n", validator.score());
		
		StorageSqlite db = new StorageSqlite("test.db");
		
		Site ccb = db.find("ccb");
		if (ccb != null)
			System.out.println(ccb.password());
	}

}
