package org.sopt.utils;

import java.text.Normalizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EmojiUtil {

	private static final Pattern graphemePattern = Pattern.compile("\\X");

	public static int getEmojiLength(String title) {
		if (title == null) return 0;

		String normalized = Normalizer.normalize(title, Normalizer.Form.NFC);
		Matcher matcher = graphemePattern.matcher(normalized);
		int count = 0;

		while (matcher.find()) {
			count++;
		}
		return count;
	}

}
