package org.sopt.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Entity
public class Comment {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Setter
	private String content;

	//단방향보다는 양방향이 좋다고 생각했던게, 내가 단 댓글로 가서 해당 게시글을 갈 수도 있을 것 같아서 그럼
	@ManyToOne
	@JoinColumn(name = "post_id")
	private Post post;

	@ManyToOne
	@JoinColumn(name ="user_id")
	private User user;

	@Builder
	private Comment(String content, Post post,User user) {
		this.content = content;
		this.post = post;
		this.user = user;
	}



	public Comment(){

	}

}
