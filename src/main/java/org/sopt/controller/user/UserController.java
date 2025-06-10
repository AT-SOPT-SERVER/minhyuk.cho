package org.sopt.controller.user;

import org.sopt.dto.request.UserRequest;
import org.sopt.global.response.ResponseCode;
import org.sopt.service.user.UserService;
import org.sopt.service.validator.UserValidator;
import org.sopt.utils.ResponseUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/user")
public class UserController {

	private final UserService userService;

	public UserController(UserService userService){
		this.userService = userService;
	}

	@PostMapping
	public ResponseEntity<?> createUser(@RequestBody UserRequest userRequest){
		UserValidator.checkUser(userRequest);
		return ResponseUtil.success(ResponseCode.USER_CREATED,userService.createUser(userRequest));
	}
}
