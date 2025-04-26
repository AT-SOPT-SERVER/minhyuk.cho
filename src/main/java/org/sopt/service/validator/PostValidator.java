package org.sopt.service.validator;

import org.sopt.dto.PostRequest;
import org.sopt.global.exception.InvalidLongTitleException;
import org.sopt.global.exception.InvalidNoTitleException;
import org.sopt.utils.EmojiUtil;

public class PostValidator {

	public static void validateTitle(PostRequest postRequest) {
		String title = postRequest.title();
		if (title == null || title.trim().isEmpty()) {
			throw new InvalidNoTitleException();
		}
		if ( EmojiUtil.getEmojiLength(title) > 30) {
			throw new InvalidLongTitleException();
		}
	}
}
