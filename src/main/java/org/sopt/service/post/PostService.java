package org.sopt.service.post;


import java.util.List;
import java.util.stream.Collectors;

import org.sopt.domain.Comment;
import org.sopt.domain.Post;
import org.sopt.domain.PostLike;
import org.sopt.domain.User;
import org.sopt.dto.CommentResponse;
import org.sopt.dto.PostDTO;
import org.sopt.dto.LikeDTO;
import org.sopt.dto.PostListDTO;
import org.sopt.dto.request.PostRequest;
import org.sopt.dto.response.PostListResponse;
import org.sopt.dto.response.PostResponseDTO;
import org.sopt.dto.PostUpdateDTO;
import org.sopt.global.exception.ErrorCodes.CustomException;
import org.sopt.global.exception.ErrorCodes.DuplicateTitleException;
import org.sopt.global.CheckTime;
import org.sopt.global.exception.ErrorCodes.ErrorCode;
import org.sopt.global.exception.ErrorCodes.InvalidIdException;
import org.sopt.global.exception.ErrorCodes.NoListException;
import org.sopt.global.exception.ErrorCodes.PostNotFoundException;
import org.sopt.repository.CommentRepository;
import org.sopt.repository.PostLikeRepository;
import org.sopt.repository.PostRepository;
import org.sopt.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class PostService {

	private final PostRepository postRepository;
	private final UserRepository userRepository;
	private final PostLikeRepository postLikeRepository;
	private final CommentRepository commentRepository;

	public PostService(PostRepository postRepository,UserRepository userRepository,PostLikeRepository postLikeRepository,CommentRepository commentRepository) {
		this.postRepository = postRepository;
		this.userRepository = userRepository;
		this.postLikeRepository = postLikeRepository;
		this.commentRepository = commentRepository;
	}

	@Transactional
	public PostResponseDTO createPost(Long userId,PostRequest postRequest){
		String title = postRequest.title();
		if(!postRepository.existsByTitle(title)){
			User user = userRepository.findById(userId)
				.orElseThrow(()-> new CustomException(ErrorCode.NO_USER));
			Post post = new Post(title,postRequest.content(),user);
			postRepository.save(post);
			CheckTime.setTimestamp();
			return new PostResponseDTO(post.getId());
		}else {
			throw new DuplicateTitleException();
		}
	}

	@Transactional(readOnly = true)
	public PostListResponse getAllPosts(Pageable pageable){
		Page<Post> postList = postRepository.findAllWithPageable(pageable);
		if(postList.isEmpty()){
			throw new NoListException();
		}
		return new PostListResponse(postList);
	}

	@Transactional(readOnly = true)
	public PostDTO getPostById(Long id){
		Post post = postRepository.findById(id)
			.orElseThrow(PostNotFoundException::new);

		Long likeCount = postLikeRepository.countByPost(post);
		List<CommentResponse> comments = commentRepository.findAllByPost(post).stream()
			.map(CommentResponse::from)
			.collect(Collectors.toList());

		return new PostDTO(post.getTitle(), post.getContent(),post.getUser().getName(),likeCount,comments);
	}

	@Transactional
	public PostDTO updatePostById(Long userId,PostUpdateDTO postUpdateDTO){
		Long id = postUpdateDTO.id();
		String newTitle = postUpdateDTO.postRequest().title();
		if(!postRepository.existsById(id)){
			throw new InvalidIdException();
		}else if(postRepository.existsByTitle(newTitle)){
			throw new DuplicateTitleException();
		}
		Post post = postRepository.findById(id)
			.orElseThrow(InvalidIdException::new);
		User user = userRepository.findById(userId)
			.orElseThrow(()->new CustomException(ErrorCode.NO_USER));

		if(post.getUser() != user){
			throw new IllegalArgumentException("글을 작성한 유저가 아닙니다.");
		}
		post.changeTitleAndContent(newTitle,postUpdateDTO.postRequest().content());

		Long likeCount = postLikeRepository.countByPost(post);
		List<CommentResponse> comments = commentRepository.findAllByPost(post).stream()
			.map(CommentResponse::from)
			.collect(Collectors.toList());
		return new PostDTO(post.getTitle(),post.getContent(),post.getUser().getName(),likeCount,comments);
	}

	@Transactional
	public void deletePostById(Long userId, Long id){

		User user = userRepository.findById(userId)
			.orElseThrow(()->new CustomException(ErrorCode.NO_USER));

		Post post = postRepository.findById(id)
			.orElseThrow(()->new CustomException(ErrorCode.NO_POST));

		if(post.getUser() != user){
			throw new IllegalArgumentException("글을 작성한 유저가 아닙니다.");
		}
		postRepository.deleteById(id);
	}

	@Transactional(readOnly = true)
	public PostListResponse findPostsByKeyword(Pageable pageable, String keyword){
		Page<Post> postList = postRepository.findAllByTitleContaining(pageable, keyword);
		if(postList.isEmpty()){
			throw new NoListException();
		}
		return new PostListResponse(postList);
	}

	@Transactional
	public LikeDTO createPostLike(Long userId, Long postId){
		Post post = postRepository.findById(postId).orElseThrow(()->new CustomException(ErrorCode.NO_POST));
		User user = userRepository.findById(userId).orElseThrow(()->new CustomException(ErrorCode.NO_USER));

		boolean isAlready = postLikeRepository.existsByUserAndPost(user,post);
		if(isAlready){
			//여기서 비용을 고려해봐야하는데 필드에 boolean 둬서 하는 방법 or 그냥 delete 하는 방법
			//일단은 delete 하는 방식으로 구현하고, 후에 수정할 예정
			PostLike postLike = postLikeRepository.findByUserAndPost(user,post);
			postLikeRepository.deleteById(postLike.getId());
			return new LikeDTO("게시글에 대한 좋아요가 해제되었습니다.");
		}

		PostLike postLike = PostLike.builder()
			.post(post)
			.user(user)
			.build();
		postLikeRepository.save(postLike);
		return new LikeDTO("게시글에 대한 좋아요가 추가되었습니다.");
	}




}
