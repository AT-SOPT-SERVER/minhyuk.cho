package org.sopt.repository;

import java.util.List;

import org.sopt.domain.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PostRepositoryCustom {
	Page<Post> findAllWithPageable(Pageable pageable);
	Boolean existsByTitle(String title);
	Page<Post> findAllByTitleContaining(Pageable pageable,String keyword);
}
