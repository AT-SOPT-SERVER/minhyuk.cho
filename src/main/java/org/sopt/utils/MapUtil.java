package org.sopt.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.sopt.domain.Post;

public class MapUtil {
	public static List<Post> mapToList(Map<Long,Post> getMaps){
		List<Post> postList = new ArrayList<>();
		for(Map.Entry<Long,Post> post : getMaps.entrySet()){
			postList.add(post.getValue());
		}
		return postList;
	}
}
