package org.sopt.service;

import static org.sopt.service.validator.PostValidator.*;
import static org.sopt.utils.MapUtil.*;


import java.util.List;


import org.sopt.domain.Post;
import org.sopt.dto.PostDTO;
import org.sopt.dto.PostResponseDTO;
import org.sopt.global.exception.DuplicateTitleException;
import org.sopt.global.CheckTime;
import org.sopt.repository.PostRepository;
import org.springframework.stereotype.Service;

@Service
public class PostService {

	private final PostRepository postRepository;

	public PostService(PostRepository postRepository) {
		this.postRepository = postRepository;
	}

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


	public List<Post> getAllPosts(){
		return mapToList(postRepository.findAll());
	}

	public Post getPostById(int id){
		return postRepository.findById(id);
	}

	public void updatePostById(int id, String newTitle){
		Post post = postRepository.findById(id);
		if(post == null){
			throw new IllegalArgumentException("존재하지 않는 ID입니다.");
		}else if(findDuplicateTitle(newTitle,mapToList(postRepository.findAll()))){
			throw new DuplicateTitleException();
		}
		post.setTitle(newTitle);
	}

	public Boolean deletePostById(int id){
		return postRepository.deleteById(id);
	}

	public List<Post> findPostsByKeyword(String keyword){
		return postRepository.findByKeyword(keyword);
	}


}
