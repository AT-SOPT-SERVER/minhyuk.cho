package org.sopt.domain;

import java.util.ArrayList;
import java.util.List;

import org.sopt.global.BaseTtime;
import org.springframework.lang.NonNull;

import jakarta.annotation.Nonnull;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Getter;

@Getter
@Entity
public class Post extends BaseTtime {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String title;

	@Column(nullable = false)
	private String content;

	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;


	protected Post() {

	}

	public Post(String title,String content) {
		this.title = title;
		this.content = content;
	}

	public Post(String title,String content,User user){
		this.title = title;
		this.content = content;
		this.user = user;
	}

	public Long getId() {
		return id;
	}

	public User getUser() {
		return user;
	}

	public String getContent() {
		return content;
	}

	public String getTitle() {
		return this.title;
	}

	public void changeTitleAndContent(String newTitle,String newContent){
		this.title = newTitle;
		this.content = newContent;
	}


}