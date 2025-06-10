package org.sopt.service.user;

import org.sopt.domain.User;
import org.sopt.dto.request.UserRequest;
import org.sopt.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

	private final UserRepository userRepository;

	public UserService(UserRepository userRepository){
		this.userRepository = userRepository;
	}
	@Transactional
	public User createUser(UserRequest userRequest){
		User user = new User(userRequest.name(),userRequest.email());
		return userRepository.save(user);
	}

}
