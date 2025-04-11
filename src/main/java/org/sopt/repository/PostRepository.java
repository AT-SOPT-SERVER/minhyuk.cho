package org.sopt.repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.sopt.domain.Post;
import org.sopt.utils.IDGenUtil;

public class PostRepository {

	Map<Long,Post> postMap = new HashMap<>();

	public void save(Post post) {
		postMap.put(post.getId(),post);
	}

	public Map<Long,Post> findAll(){
		return postMap;
	}

	public Post findById(long id) {
		return postMap.get(id);
	}

	public List<Post> findByKeyword(String keyword){
		List<Post> newList = new ArrayList<>();
		for(Map.Entry<Long,Post> post : postMap.entrySet()){
			if(post.getValue().getTitle().contains(keyword)){
				newList.add(post.getValue());
			}
		}
		return newList;
	}

	public Boolean deleteById(long id) {
		Post post = postMap.remove(id);
		return post != null;
	}
}
