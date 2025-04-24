package org.sopt.service;


import java.util.List;

import org.sopt.domain.Post;
import org.sopt.dto.PostDTO;
import org.sopt.dto.PostListDTO;
import org.sopt.dto.PostRequest;
import org.sopt.dto.PostResponseDTO;
import org.sopt.dto.PostUpdateDTO;
import org.sopt.global.exception.DuplicateTitleException;
import org.sopt.global.CheckTime;
import org.sopt.global.exception.InvalidIdException;
import org.sopt.global.exception.NoListException;
import org.sopt.global.exception.PostNotFoundException;
import org.sopt.repository.PostRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class PostService {

	private final PostRepository postRepository;

	public PostService(PostRepository postRepository) {
		this.postRepository = postRepository;
	}

	@Transactional
	public PostResponseDTO createPost(PostRequest postRequest){
		String title = postRequest.title();
		if(!postRepository.existsByTitle(title)){
			Post post = new Post(title);
			postRepository.save(post);
			CheckTime.setTimestamp();
			return new PostResponseDTO(post.getId());
		}else {
			throw new DuplicateTitleException();
		}
	}

	@Transactional(readOnly = true)
	public PostListDTO getAllPosts(){
		List<Post> postList = postRepository.findAll();
		if(postList.isEmpty()){
			throw new NoListException();
		}
		return new PostListDTO(postList);
	}

	@Transactional(readOnly = true)
	public PostDTO getPostById(Long id){
		Post post = postRepository.findById(id)
			.orElseThrow(PostNotFoundException::new);

		return new PostDTO(post.getId(), post.getTitle());
	}

	@Transactional
	public PostDTO updatePostById(PostUpdateDTO postUpdateDTO){
		Long id = postUpdateDTO.id();
		String newTitle = postUpdateDTO.postRequest().title();
		if(!postRepository.existsById(id)){
			throw new InvalidIdException();
		}else if(postRepository.existsByTitle(newTitle)){
			throw new DuplicateTitleException();
		}

		Post post = postRepository.findById(id)
			.orElseThrow(InvalidIdException::new);

		post.changeTitle(newTitle);

		return new PostDTO(post.getId(),post.getTitle());
	}

	@Transactional
	public void deletePostById(Long id){
		if(!postRepository.existsById(id)) {
			throw new InvalidIdException();
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
