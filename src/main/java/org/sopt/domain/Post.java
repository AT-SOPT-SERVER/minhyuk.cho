package org.sopt.domain;

import org.sopt.utils.IDGenUtil;

public class Post {
	private Long id;
	private String title;

	public Post(String title){
		this.id = IDGenUtil.generateId();
		this.title = title;
	}

	public void setId(long id){
		this.id = id;
	}

	public Long getId(){
		return this.id;
	}

	public String getTitle(){
		return this.title;
	}

	public void setTitle(String newTitle){
		this.title = newTitle;
	}

}
