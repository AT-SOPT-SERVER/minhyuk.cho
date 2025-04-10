package org.sopt.service;

import java.util.List;

import org.sopt.domain.Post;
import org.sopt.exception.DuplicateTitleException;
import org.sopt.global.CheckTime;
import org.sopt.repository.PostRepository;

public class PostService {

	private final PostRepository postRepository = new PostRepository();

	public Boolean createPost(Post post){
		if(!findDuplicateTitle(post.getTitle())){
			postRepository.save(post);
			CheckTime.setTimestamp();
			return true;
		}
		return false;
	}

	public List<Post> getAllPosts(){
		return postRepository.findAll();
	}

	public Post getPostById(int id){
		Post post = postRepository.findById(id);
		if(post == null){
			return null;
		}
		return postRepository.findById(id);
	}

	public void updatePostById(int id, String newTitle){
		Post post = postRepository.findById(id);
		if(post == null){
			throw new IllegalArgumentException("존재하지 않는 ID입니다.");
		}else if(findDuplicateTitle(newTitle)){
			throw new DuplicateTitleException();
		}
		post.setTitle(newTitle);
	}

	public Boolean deletePostById(int id){
		return postRepository.deleteById(id);
	}

	private Boolean findDuplicateTitle(String newTitle){
		for(Post posting : postRepository.findAll()){
			if(posting.getTitle().equals(newTitle)){
				return true;
			}
		}
		return false;
	}
}
