package com.hqking.hashpass;

import java.security.NoSuchAlgorithmException;
import java.util.List;

public class Hashpass {
	static StorageSqlite db;
	
	static int save(Site site) {
		return db.save(site);
	}
	
	static List<Site> search(String pattern) {
		return db.search(pattern);
	}
	
	static Site find(String desc) {
		return db.find(desc);
	}
	
	static int delete(Site site) {
		return db.delete(site);
	}
	
	static String[] listTag() {
		return db.listTags();
	}
	
	static void addTag(String name) {
		db.addTag(name);
	}

	
	static void exit() {
		Generator.clearKey();
		System.exit(0);
	}
	
	/**
	 * @param args
	 * @throws NoSuchAlgorithmException 
	 */
	public static void main(String[] args) throws NoSuchAlgorithmException {
		Generator.init("");
		
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
		
		db = new StorageSqlite("test.db");
		db.sync();
		
		Site ccb = db.find("ccb");
		if (ccb != null) {
			System.out.println(ccb.password());
			System.out.println(ccb.entropy());
		}
		
		BrowserView.start();
	}

}
