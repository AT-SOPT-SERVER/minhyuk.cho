package org.sopt.service;


import org.sopt.domain.Post;
import org.sopt.dto.PostDTO;
import org.sopt.dto.PostListDTO;
import org.sopt.dto.PostResponseDTO;
import org.sopt.global.exception.DuplicateTitleException;
import org.sopt.global.CheckTime;
import org.sopt.repository.PostRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityNotFoundException;

@Service
public class PostService {

	private final PostRepository postRepository;

	public PostService(PostRepository postRepository) {
		this.postRepository = postRepository;
	}

	@Transactional
	public PostResponseDTO createPost(String title){
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
		return new PostListDTO(postRepository.findAll());
	}
	@Transactional(readOnly = true)
	public PostDTO getPostById(Long id){
		Post post = postRepository.findById(id)
			.orElseThrow(() -> new EntityNotFoundException("해당 게시글이 존재하지 않습니다."));

		return new PostDTO(post.getId(), post.getTitle());
	}

	@Transactional
	public PostDTO updatePostById(Long id, String newTitle){
		if(!postRepository.existsById(id)){
			throw new IllegalArgumentException("존재하지 않는 ID입니다.");
		}else if(postRepository.existsByTitle(newTitle)){
			throw new DuplicateTitleException();
		}

		Post post = postRepository.findById(id)
			.orElseThrow(() -> new IllegalArgumentException("존재하지 않는 ID입니다."));

		post.changeTitle(newTitle);

		return new PostDTO(post);
	}

	@Transactional
	public void deletePostById(Long id){
		if(!postRepository.existsById(id)) {
			throw new IllegalArgumentException("존재하지 않는 ID입니다.");
		}
		postRepository.deleteById(id);
	}
	@Transactional(readOnly = true)
	public PostListDTO findPostsByKeyword(String keyword){
		return new PostListDTO(postRepository.findAllByTitleContaining(keyword));
	}


}
