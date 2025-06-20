package org.sopt.domain;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Builder;
import lombok.Getter;

@Getter
@Entity
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String name;

	private String email;

	@OneToMany(mappedBy = "user")
	private List<Post> contents = new ArrayList<>();

	@OneToMany(mappedBy = "user")
	private List<Comment> comments = new ArrayList<>();


	public User(){

	}
	public User(String name, String email){
		this.name = name;
		this.email = email;
	}

	@Builder
	public User(Long id, String name, String email) {
		this.id = id;
		this.name = name;
		this.email = email;
	}



	public String getName() {
		return name;
	}
}
