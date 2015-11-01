package com.hqking.hashpass;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.zip.CRC32;


class Generator {
	static final String TABLE_PRINTALBE_ASCII = "printableAscii";
	static final String TABLE_ALPHA_NUMERIC = "alphanumeric";
	static final String TABLE_NUMBERS_ONLY = "numeric";
	
	private static String masterKey;
	private static MessageDigest digest;
	private static final CRC32 crc32 = new CRC32();
	private static long seed;
	
	private static final char tablePrintableAscii[] = {
		'!', '"', '#', '$', '%', '&', '\'', '(', ')', '*',
		'+', ',', '-', '.', '/', '0', '1', '2', '3', '4',
		'5', '6', '7', '8', '9', ':', ';', '<', '=', '>',
		'?', '@', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H',
		'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R',
		'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', '[', '\\',
		']', '^', '_', '`', 'a', 'b', 'c', 'd', 'e', 'f', 
		'g', 'h', 'i', 'j',	'k', 'l', 'm', 'n', 'o', 'p', 
		'q', 'r', 's', 't',	'u', 'v', 'w', 'x', 'y', 'z',
		'{', '|', '}', '~',
	};
	
	private static final char tableAlphaNumeric[] = {
		'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
		'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H',	'I', 'J', 
		'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R',	'S', 'T', 
		'U', 'V', 'W', 'X', 'Y', 'Z',
		'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j',	
		'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't',
		'u', 'v', 'w', 'x', 'y', 'z',
	};
	
	private static final char tableNumeric[] = {
		'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
	};
	
	private static char[] findMap(String type) {
		if (type.compareTo(TABLE_NUMBERS_ONLY) == 0) {
			return tableNumeric;
		} else if (type.compareTo(TABLE_ALPHA_NUMERIC) == 0) {
			return tableAlphaNumeric;
		} else if (type.compareTo(TABLE_PRINTALBE_ASCII) == 0) {
			return tablePrintableAscii;
		} else {
			return tablePrintableAscii;
		}
	}
		
	private static long lcg(long x0) {
		return (1103515245 * x0 + 12345);
	}
	
	private static long bytes2long(byte hash[]) {
		crc32.reset();
		crc32.update(hash);
		
		return crc32.getValue();
	}
	
	private static byte[] hashedWord(String input) {
		digest.reset();
		digest.update(input.getBytes());
		
		return digest.digest();
	}
	
	private static int random(int mod) {
		seed = lcg(seed);
		
		return Math.abs((int)seed) % mod;
	}
	
	private static void passGenerate(char pass[], char map[]) {
		for (int i = 0; i < pass.length; i++) {
			int index = random(map.length);
			pass[i] = map[index];
		}
	}
	
	static void init(String key) throws NoSuchAlgorithmException {
		digest = java.security.MessageDigest.getInstance("SHA-1");
		masterKey = key;
	}
	
	static void setKey(String key) {
		masterKey = key;
	}
	
	static String password(Site site) {		
		byte hash[] = hashedWord(masterKey + site.description);
		seed = bytes2long(hash);
		
		char map[] = findMap(site.type);
		
		char pass[] = new char[site.length];
		for (int i = 0; i <= site.bump; i++)
			passGenerate(pass, map);
		
		return new String(pass);
	}
	
	static int entropy(String pass, String type) {
		char map[] = findMap(type);
		
		double H; // entropy per symbol
		
		H = Math.log(map.length) / Math.log(2);
		
		return (int)(H * pass.length());
	}
}
