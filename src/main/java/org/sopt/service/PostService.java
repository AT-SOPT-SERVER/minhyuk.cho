package org.sopt.service;

import java.util.List;

import org.sopt.domain.Post;
import org.sopt.repository.PostRepository;

public class PostService {

	private final PostRepository postRepository = new PostRepository();

	public void createPost(Post post){
		postRepository.save(post);
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

	public Boolean updatePostById(int id, String newTitle){
		Post post = postRepository.findById(id);
		if(post == null){
			return false;
		}
		post.setTitle(newTitle);
		return true;
	}

	public Boolean deletePostById(int id){
		return postRepository.deleteById(id);
	}


}
