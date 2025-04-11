package org.sopt.utils;

import java.text.Normalizer;


public class EmojiUtil {

	public static int getEmojiLength(String title){
		String normalized = Normalizer.normalize(title, Normalizer.Form.NFC);
		int[] cps = normalized.codePoints().toArray();

		int count = 0;
		for (int i = 0; i < cps.length; i++) {
			count++;
			while (i + 1 < cps.length && cps[i + 1] == 0x200D) {
				i += 2;
			}
		}
		return count;
	}

}
