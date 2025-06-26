package org.sopt.repository.impl;

import java.util.List;
import java.util.Optional;

import org.sopt.domain.Post;
import org.sopt.domain.QPost;
import org.sopt.repository.PostRepositoryCustom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class PostRepositoryImpl implements PostRepositoryCustom {

	private final JPAQueryFactory queryFactory;
	QPost post = QPost.post;

	@Override
	public Page<Post> findAllWithPageable(Pageable pageable){
		List<Post> content = queryFactory
			.selectFrom(post)
			.orderBy(post.createdAt.desc())
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.fetch();

		// 이게 약간 레거시 코드 같은 느낌인데 좀 더 직관적이라 비교해볼까 하고 남겨뒀습니다!
		// Long count = queryFactory
		// 	.select(post.count())
		// 	.from(post)
		// 	.fetchOne();
		//
		// long total = count != null ? count : 0L;

		long total = Optional.ofNullable(
			queryFactory.select(post.count()).from(post).fetchOne()
		).orElse(0L);

		return new PageImpl<>(content,pageable,total);
	}

	@Override
	public Boolean existsByTitle(String title){

		return queryFactory
			.selectOne()
			.from(post)
			.where(
				post.title.eq(title)
			)
			.fetchFirst() !=null;
	}

	@Override
	public Page<Post> findAllByTitleContaining(Pageable pageable,String keyword){

		List<Post> content = queryFactory
			.selectFrom(post)
			.where(
				post.title.containsIgnoreCase(keyword)
			)
			.orderBy(post.createdAt.desc())
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.fetch();

		Long total = Optional.ofNullable(
			queryFactory
				.select(post.count())
				.from(post)
				.where(post.title.containsIgnoreCase(keyword))
				.fetchOne())
			.orElse(0L);

		return new PageImpl<>(content,pageable,total);
	}

}
