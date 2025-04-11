package org.sopt.service;

import static org.sopt.service.validator.PostValidator.*;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.sopt.domain.Post;
import org.sopt.exception.DuplicateTitleException;
import org.sopt.global.CheckTime;
import org.sopt.repository.PostRepository;

public class PostService {

	private final PostRepository postRepository = new PostRepository();


	public void createPost(Post post){
		if(!findDuplicateTitle(post.getTitle(),mapToList())){
			postRepository.save(post);
			CheckTime.setTimestamp();
		}else {
			throw new DuplicateTitleException();
		}
	}

	public List<Post> getAllPosts(){
		return mapToList();
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
		}else if(findDuplicateTitle(newTitle,mapToList())){
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

	public void readFromFile(){
		postRepository.readFromFile();
	}


	public void printToFile(){
		postRepository.writeToFile();
	}



	private List<Post> mapToList(){
		List<Post> postList = new ArrayList<>();
		for(Map.Entry<Long,Post> post : postRepository.findAll().entrySet()){
			postList.add(post.getValue());
		}
		return postList;
	}
}
