package org.sopt.repository;

import java.util.List;

import org.sopt.domain.Comment;
import org.sopt.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<Comment,Long> {
	List<Comment> findAllByPost(Post post);

}
