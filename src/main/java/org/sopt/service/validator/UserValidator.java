package org.sopt.service.validator;

import org.sopt.dto.request.UserRequest;
import org.sopt.global.exception.ErrorCodes.CustomException;
import org.sopt.global.exception.ErrorCodes.ErrorCode;
import org.sopt.global.exception.ErrorCodes.NoNameException;

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
