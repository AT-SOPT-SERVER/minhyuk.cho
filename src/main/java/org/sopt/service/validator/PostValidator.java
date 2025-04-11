package org.sopt.service.validator;

import java.util.List;

import org.sopt.domain.Post;

public class PostValidator {

	public static Boolean findDuplicateTitle(String newTitle, List<Post> postList){
		for(Post posting : postList){
			if(posting.getTitle().equals(newTitle)){
				return true;
			}
		}
		return false;
	}
}
