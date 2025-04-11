package org.sopt.domain;

import org.sopt.utils.IDGenUtil;

public class Post {
	private final int id;
	private String title;

	public Post(String title){
		this.id = IDGenUtil.generateId();
		this.title = title;
	}

	public int getId(){
		return this.id;
	}

	public String getTitle(){
		return this.title;
	}

	public void setTitle(String newTitle){
		this.title = newTitle;
	}

}
