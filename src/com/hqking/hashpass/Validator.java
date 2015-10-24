package com.hqking.hashpass;

class Validator {
	private enum CharType {
		NUMBER, UPPER, LOWER, SPECIAL, INVALID
	}

	private CharType asciiCat[] = { CharType.INVALID, // 0
			CharType.INVALID, // 1
			CharType.INVALID, // 2
			CharType.INVALID, // 3
			CharType.INVALID, // 4
			CharType.INVALID, // 5
			CharType.INVALID, // 6
			CharType.INVALID, // 7
			CharType.INVALID, // 8
			CharType.INVALID, // 9
			CharType.INVALID, // 10
			CharType.INVALID, // 11
			CharType.INVALID, // 12
			CharType.INVALID, // 13
			CharType.INVALID, // 14
			CharType.INVALID, // 15
			CharType.INVALID, // 16
			CharType.INVALID, // 17
			CharType.INVALID, // 18
			CharType.INVALID, // 19
			CharType.INVALID, // 20
			CharType.INVALID, // 21
			CharType.INVALID, // 22
			CharType.INVALID, // 23
			CharType.INVALID, // 24
			CharType.INVALID, // 25
			CharType.INVALID, // 26
			CharType.INVALID, // 27
			CharType.INVALID, // 28
			CharType.INVALID, // 29
			CharType.INVALID, // 30
			CharType.INVALID, // 31
			CharType.INVALID, // 32 Space
			CharType.SPECIAL, // 33 !
			CharType.SPECIAL, // 34 "
			CharType.SPECIAL, // 35 #
			CharType.SPECIAL, // 36 $
			CharType.SPECIAL, // 37 %
			CharType.SPECIAL, // 38 &
			CharType.SPECIAL, // 39 '
			CharType.SPECIAL, // 40 (
			CharType.SPECIAL, // 41 )
			CharType.SPECIAL, // 42 *
			CharType.SPECIAL, // 43 +
			CharType.SPECIAL, // 44 ,
			CharType.SPECIAL, // 45 -
			CharType.SPECIAL, // 46 .
			CharType.SPECIAL, // 47 /
			CharType.NUMBER, // 48 0
			CharType.NUMBER, // 49 1
			CharType.NUMBER, // 50 2
			CharType.NUMBER, // 51 3
			CharType.NUMBER, // 52 4
			CharType.NUMBER, // 53 5
			CharType.NUMBER, // 54 6
			CharType.NUMBER, // 55 7
			CharType.NUMBER, // 56 8
			CharType.NUMBER, // 57 9
			CharType.SPECIAL, // 58 :
			CharType.SPECIAL, // 59 ;
			CharType.SPECIAL, // 60 <
			CharType.SPECIAL, // 61 =
			CharType.SPECIAL, // 62 >
			CharType.SPECIAL, // 63 ?
			CharType.SPECIAL, // 64 @
			CharType.UPPER, // 65 A
			CharType.UPPER, // 66 B
			CharType.UPPER, // 67 C
			CharType.UPPER, // 68 D
			CharType.UPPER, // 69 E
			CharType.UPPER, // 70 F
			CharType.UPPER, // 71 G
			CharType.UPPER, // 72 H
			CharType.UPPER, // 73 I
			CharType.UPPER, // 74 J
			CharType.UPPER, // 75 K
			CharType.UPPER, // 76 L
			CharType.UPPER, // 77 M
			CharType.UPPER, // 78 N
			CharType.UPPER, // 79 O
			CharType.UPPER, // 80 P
			CharType.UPPER, // 81 Q
			CharType.UPPER, // 82 R
			CharType.UPPER, // 83 S
			CharType.UPPER, // 84 T
			CharType.UPPER, // 85 U
			CharType.UPPER, // 86 V
			CharType.UPPER, // 87 W
			CharType.UPPER, // 88 X
			CharType.UPPER, // 89 Y
			CharType.UPPER, // 90 Z
			CharType.SPECIAL, // 91 [
			CharType.SPECIAL, // 92 \
			CharType.SPECIAL, // 93 ]
			CharType.SPECIAL, // 94 ^
			CharType.SPECIAL, // 95 _
			CharType.SPECIAL, // 96 `
			CharType.LOWER, // 97
			CharType.LOWER, // 98
			CharType.LOWER, // 99
			CharType.LOWER, // 100
			CharType.LOWER, // 101
			CharType.LOWER, // 102
			CharType.LOWER, // 103
			CharType.LOWER, // 104
			CharType.LOWER, // 105
			CharType.LOWER, // 106
			CharType.LOWER, // 107
			CharType.LOWER, // 108
			CharType.LOWER, // 109
			CharType.LOWER, // 110
			CharType.LOWER, // 111
			CharType.LOWER, // 112
			CharType.LOWER, // 113
			CharType.LOWER, // 114
			CharType.LOWER, // 115
			CharType.LOWER, // 116
			CharType.LOWER, // 117
			CharType.LOWER, // 118
			CharType.LOWER, // 119
			CharType.LOWER, // 120
			CharType.LOWER, // 121
			CharType.LOWER, // 122
			CharType.SPECIAL, // 123 {
			CharType.SPECIAL, // 124 |
			CharType.SPECIAL, // 125 }
			CharType.SPECIAL, // 126 ~
	};

	private int numberCount;
	private int upperCount;
	private int lowerCount;
	private int specialCount;
	private int invalidCount;
	private final String password;
	private final CharType[] pwCat;
	private final int length;

	public Validator(String pass) {
		numberCount = 0;
		upperCount = 0;
		lowerCount = 0;
		specialCount = 0;
		invalidCount = 0;

		password = pass;
		length = pass.length();
		pwCat = new CharType[length];

		int i = 0;
		for (char c : password.toCharArray()) {
			if (c >= asciiCat.length) {
				invalidCount++;
				pwCat[i] = CharType.INVALID;
			} else {
				CharType type = asciiCat[c];
				pwCat[i] = type;
				
				if (type == CharType.NUMBER) {
					numberCount++;
				} else if (type == CharType.LOWER) {
					lowerCount++;
				} else if (type == CharType.UPPER) {
					upperCount++;
				} else if (type == CharType.SPECIAL) {
					specialCount++;
				} else {
					invalidCount++;
				}
			}
			
			i++;
		}
	}

	int consecutiveCount(CharType type) {
		int count = 0;
		
		CharType last = CharType.INVALID;
		for (CharType t: pwCat) {
			if (t != type) {
				last = CharType.INVALID;
				continue;
			}
			
			if (t == last)
				count += 2;
			
			last = t;
		}
		
		return count;
	}
	
	private static final int MIN_SEQ_ALLOW = 2;
	int sequentialCount(String charSeq) {
		int totalMatch = 0;
		
		int minLength = password.length() < charSeq.length() ? password.length() : charSeq.length();
		
		for (int toffset = 0; toffset < password.length() - MIN_SEQ_ALLOW; toffset++) {
			for (int ooffset = 0; ooffset < charSeq.length() - MIN_SEQ_ALLOW; ooffset++) {
				int match = 0;
				
				for (int len = MIN_SEQ_ALLOW + 1; len <= minLength; len++) {
					if (!password.regionMatches(true, toffset, charSeq, ooffset, len))
						break;

					match++;
				}
				
				if (match > 0) {
					totalMatch += match;
					
					toffset += match + MIN_SEQ_ALLOW - 1;
					break;
				}
			}
		}
		
		return totalMatch * 3;
	}
	
	int numericCount() {
		return numberCount;
	}

	int upperLetterCount() {
		return upperCount;
	}

	int lowerLetterCount() {
		return lowerCount;
	}

	int specialCharCount() {
		return specialCount;
	}

	int invalidCount() {
		return invalidCount;
	}

	/**
	 * the algorithm derived from www.passwordmeter.com  
	 */
	public int score() {
		int score = 0;

		// rule 1: length matters
		score += length * 4;
		
		// rule 2: upper case letters
		if (upperCount > 0)
			score += (length - upperCount) * 2;
		
		// rule 3: lower case letters
		if (lowerCount > 0)
			score += (length - lowerCount) * 2;
		
		// rule 4: numbers
		score += numberCount * 4;
		
		// rule 5: punctuation and other characters
		score += specialCount * 6;
		
		// rule 6: numbers and special characters in the middle matters
		score += (numberCount + specialCount) * 2;
		if (asciiCat[password.charAt(0)] == CharType.NUMBER ||
			asciiCat[password.charAt(0)] == CharType.SPECIAL)
			score -= 2;
		if (asciiCat[password.charAt(length - 1)] == CharType.NUMBER ||
			asciiCat[password.charAt(length - 1)] == CharType.SPECIAL)
			score -= 2;
		
		// rule 7: requirements met
		if (length >= 8)
			score += 2;
		if (upperCount > 0)
			score += 2;
		if (lowerCount > 0)
			score += 2;
		if (numberCount > 0)
			score += 2;
		if (specialCount > 0)
			score += 2;
		
		// rule 8: letters only (case insensitive)
		if (length == upperCount + lowerCount)
			score -= length;
		
		// rule 9: number only
		if (length == numberCount)
			score -= length;
		
		// rule 10: repeated characters
		
		// rule 11: consecutive upper case letters
		score -= consecutiveCount(CharType.UPPER);
		
		// rule 12: consecutive lower case letters
		score -= consecutiveCount(CharType.LOWER);
		
		// rule 13: consecutive numbers
		score -= consecutiveCount(CharType.NUMBER);
		
		// rule 14: sequential letters (case insensitive)
		score -= sequentialCount("abcdefghijklmnopqrstuvwxyz");
		score -= sequentialCount("zyxwvutsrqponmlkjihgfedcba");
		
		// rule 15: sequential numbers
		score -= sequentialCount("01234567890");
		score -= sequentialCount("09876543210");
		
		// rule 16: sequential symbols
		score -= sequentialCount("~!@#$%^&*()_+");
		score -= sequentialCount("+_)(*&^%$#@!~");

		if (score > 100)
			score = 100;
		
		if (score < 0)
			score = 0;
		
		return score;
	}
}
