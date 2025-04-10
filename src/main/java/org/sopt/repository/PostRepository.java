package org.sopt.repository;

import java.util.ArrayList;
import java.util.List;

import org.sopt.domain.Post;

public class PostRepository {

	List<Post> postList = new ArrayList<>();

	public void save(Post post) {
		postList.add(post);
	}

	public List<Post> findAll(){
		return postList;
	}

	public Post findById(int id) {
		for (Post post : postList) {
			if (post.getId() == id) {
				return post;
			}
		}
		return null;
	}

	public Boolean deleteById(int id) {
		for (Post post : postList) {
			if (post.getId() == id) {
				postList.remove(post);
				return true;
			}
		}
		return false;
	}
}
