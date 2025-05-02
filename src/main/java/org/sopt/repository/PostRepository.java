package org.sopt.repository;



import java.util.List;

import org.sopt.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<Post,Long> {

	List<Post> findAllByOrderByCreatedAtDesc();
	Boolean existsByTitle(String title);
	List<Post> findAllByTitleContaining(String keyword);
}
