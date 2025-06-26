package org.sopt.service.user;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.sopt.domain.User;
import org.sopt.dto.request.UserRequest;
import org.sopt.repository.UserRepository;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;


@ExtendWith(MockitoExtension.class)
class UserServiceTest {


	@Mock
	private UserRepository userRepository;

	@InjectMocks
	private UserService userService;

	@DisplayName("유저를 만드는 테스트")
	@Test
	void makeUser(){
	    //given
		User user = User.builder()
			.name("테스트")
			.email("123213@naver.com")
			.build();

		when(userRepository.save(any(User.class))).thenReturn(user);
	    //when

		User savedUser = userService.createUser(new UserRequest("테스트","123213@naver.com"));

	    //then

		assertThat(savedUser.getName()).isEqualTo("테스트");
		assertThat(savedUser.getEmail()).isEqualTo("123213@naver.com");

		verify(userRepository,times(1)).save(any(User.class));
	 }


}