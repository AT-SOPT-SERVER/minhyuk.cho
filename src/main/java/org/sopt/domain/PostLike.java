package org.sopt.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.Builder;
import lombok.Getter;

@Entity
@Getter
public class PostLike {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	//단방향의 맵핑이면 된다.
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "post_id")
	private Post post;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private User user;

	public PostLike(){

	}

	@Builder
	private PostLike(Post post, User user) {
		this.post = post;
		this.user = user;
	}

	//근데 여기서 중요한 건,, 하나의 댓글이나 댓글에 대해서 좋아요를 누를 수 있는 것은 한 번 뿐이다... 아 테이블에 하는게 아니라 그냥 필드에 넣어야하나?
	//근데 그러면 무결성의 원칙을 벗어나기 때문에 한 명이 여러번 누를 수 있기 때문에 이를 막기 위해서 따로 테이블을 만들어서 관리하는게 더 비용이 적을 듯
	//그리고 또한 다른 테이블로 분리해두면 좋아요를 누른 게시물 같은 것들을 가져올 수 있게 되는 것이다.
	//그러면 양방향으로 해야지 그런 게시글들을 가져올 수 있으니까 양방향으로 해야하는건가?






}
