package org.sopt.service.validator;

import org.sopt.dto.request.UserRequest;
import org.sopt.global.exception.CustomException;
import org.sopt.global.exception.ErrorCode;
import org.sopt.global.exception.NoNameException;

public class UserValidator {

	public static void checkUser(UserRequest userRequest){
		String name = userRequest.name();
		if(name.isEmpty()){
			throw new NoNameException();
		}
		if(name.length() > 10){
			throw new CustomException(ErrorCode.LONG_NAME);
		}
	}
}
