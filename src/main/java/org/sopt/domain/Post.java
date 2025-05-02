package org.sopt.domain;

import org.springframework.lang.NonNull;

import jakarta.annotation.Nonnull;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class Post {
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

	public Post(String title){
		this.title = title;
	}

	public Long getId() {
		return id;
	}


	public String getTitle() {
		return this.title;
	}

	public void changeTitle(String newTitle){
		this.title = newTitle;
	}
}