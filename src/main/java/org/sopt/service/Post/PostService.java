package org.sopt.service.Post;


import java.util.List;

import org.sopt.domain.Post;
import org.sopt.domain.User;
import org.sopt.dto.PostDTO;
import org.sopt.dto.PostListDTO;
import org.sopt.dto.PostRequest;
import org.sopt.dto.PostResponseDTO;
import org.sopt.dto.PostUpdateDTO;
import org.sopt.global.exception.CustomException;
import org.sopt.global.exception.DuplicateTitleException;
import org.sopt.global.CheckTime;
import org.sopt.global.exception.ErrorCode;
import org.sopt.global.exception.InvalidIdException;
import org.sopt.global.exception.NoListException;
import org.sopt.global.exception.PostNotFoundException;
import org.sopt.repository.PostRepository;
import org.sopt.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class PostService {

	private final PostRepository postRepository;
	private final UserRepository userRepository;
	public PostService(PostRepository postRepository,UserRepository userRepository) {
		this.postRepository = postRepository;
		this.userRepository = userRepository;
	}

	@Transactional
	public PostResponseDTO createPost(Long userId,PostRequest postRequest){
		String title = postRequest.title();
		if(!postRepository.existsByTitle(title)){
			User user = userRepository.findById(userId)
				.orElseThrow(()-> new CustomException(ErrorCode.NO_USER));
			Post post = new Post(title,postRequest.content(),user);
			postRepository.save(post);
			CheckTime.setTimestamp();
			return new PostResponseDTO(post.getId());
		}else {
			throw new DuplicateTitleException();
		}
	}

	@Transactional(readOnly = true)
	public PostListDTO getAllPosts(){
		List<Post> postList = postRepository.findAllByOrderByCreatedAtDesc();
		if(postList.isEmpty()){
			throw new NoListException();
		}
		return new PostListDTO(postList);
	}

	@Transactional(readOnly = true)
	public PostDTO getPostById(Long id){
		Post post = postRepository.findById(id)
			.orElseThrow(PostNotFoundException::new);
		return new PostDTO(post.getTitle(), post.getContent(),post.getUser().getName());
	}

	@Transactional
	public PostDTO updatePostById(Long userId,PostUpdateDTO postUpdateDTO){
		Long id = postUpdateDTO.id();
		String newTitle = postUpdateDTO.postRequest().title();
		if(!postRepository.existsById(id)){
			throw new InvalidIdException();
		}else if(postRepository.existsByTitle(newTitle)){
			throw new DuplicateTitleException();
		}
		Post post = postRepository.findById(id)
			.orElseThrow(InvalidIdException::new);
		User user = userRepository.findById(userId)
			.orElseThrow(()->new CustomException(ErrorCode.NO_USER));

		if(post.getUser() != user){
			throw new IllegalArgumentException("글을 작성한 유저가 아닙니다.");
		}
		post.changeTitleAndContent(newTitle,postUpdateDTO.postRequest().content());
		return new PostDTO(post.getTitle(),post.getContent(),post.getUser().getName());
	}

	@Transactional
	public void deletePostById(Long userId, Long id){
		if(!postRepository.existsById(id)) {
			throw new InvalidIdException();
		}
		User user = userRepository.findById(userId)
			.orElseThrow(()->new CustomException(ErrorCode.NO_USER));

		if(postRepository.findById(id).get().getUser() != user){
			throw new IllegalArgumentException("글을 작성한 유저가 아닙니다.");
		}
		postRepository.deleteById(id);
	}

	@Transactional(readOnly = true)
	public PostListDTO findPostsByKeyword(String keyword){
		List<Post> postList = postRepository.findAllByTitleContaining(keyword);
		if(postList.isEmpty()){
			throw new NoListException();
		}
		return new PostListDTO(postList);
	}


}
