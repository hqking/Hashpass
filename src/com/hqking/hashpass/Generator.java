package com.hqking.hashpass;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.zip.CRC32;


class Generator {
	private long seed;
	private char map[];

	private static MessageDigest digest;
	private static final CRC32 crc32 = new CRC32();
	
	static final char tablePrintableAscii[] = {
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
	
	static final char tableAlphaNumeric[] = {
		'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
		'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H',	'I', 'J', 
		'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R',	'S', 'T', 
		'U', 'V', 'W', 'X', 'Y', 'Z',
		'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j',	
		'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't',
		'u', 'v', 'w', 'x', 'y', 'z',
	};
	
	static final char tableNumeric[] = {
		'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
	};
		
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
	
	private int random(int mod) {
		seed = lcg(seed);
		
		return Math.abs((int)seed) % mod;
	}
	
	private void passGenerate(char pass[]) {
		for (int i = 0; i < pass.length; i++) {
			int index = random(map.length);
			pass[i] = map[index];
		}
	}
	
	Generator(char map[]) throws NoSuchAlgorithmException {
		digest = java.security.MessageDigest.getInstance("SHA-1");
		this.map = map;
	}
	
	String password(Site site, String key) {		
		byte hash[] = hashedWord(key + site.description);
		seed = bytes2long(hash);
		
		char pass[] = new char[site.length];
		for (int i = 0; i <= site.bump; i++)
			passGenerate(pass);
		
		return new String(pass);
	}
	
	int entropy(char pass[]) {
		double H; // entropy per symbol
		
		H = Math.log(map.length) / Math.log(2);
		
		return (int)(H * pass.length);
	}
}
