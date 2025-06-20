package org.sopt.service.post;


import java.util.List;

import org.sopt.domain.Post;
import org.sopt.domain.PostLike;
import org.sopt.domain.User;
import org.sopt.dto.PostDTO;
import org.sopt.dto.LikeDTO;
import org.sopt.dto.PostListDTO;
import org.sopt.dto.request.PostRequest;
import org.sopt.dto.response.PostResponseDTO;
import org.sopt.dto.PostUpdateDTO;
import org.sopt.global.exception.ErrorCodes.CustomException;
import org.sopt.global.exception.ErrorCodes.DuplicateTitleException;
import org.sopt.global.CheckTime;
import org.sopt.global.exception.ErrorCodes.ErrorCode;
import org.sopt.global.exception.ErrorCodes.InvalidIdException;
import org.sopt.global.exception.ErrorCodes.NoListException;
import org.sopt.global.exception.ErrorCodes.PostNotFoundException;
import org.sopt.repository.PostLikeRepository;
import org.sopt.repository.PostRepository;
import org.sopt.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class PostService {

	private final PostRepository postRepository;
	private final UserRepository userRepository;
	private final PostLikeRepository postLikeRepository;

	public PostService(PostRepository postRepository,UserRepository userRepository,PostLikeRepository postLikeRepository) {
		this.postRepository = postRepository;
		this.userRepository = userRepository;
		this.postLikeRepository = postLikeRepository;
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
	public PostListDTO getAllPosts(){
		List<Post> postList = postRepository.findAllByOrderByCreatedAtDesc();
		if(postList.isEmpty()){
			throw new NoListException();
		}
		return new PostListDTO(postList);
	}

	@Transactional(readOnly = true)
	public PostDTO getPostById(Long id){
		Post post = postRepository.findById(id)
			.orElseThrow(PostNotFoundException::new);
		return new PostDTO(post.getTitle(), post.getContent(),post.getUser().getName());
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
		return new PostDTO(post.getTitle(),post.getContent(),post.getUser().getName());
	}

	@Transactional
	public void deletePostById(Long userId, Long id){
		if(!postRepository.existsById(id)) {
			throw new InvalidIdException();
		}
		User user = userRepository.findById(userId)
			.orElseThrow(()->new CustomException(ErrorCode.NO_USER));

		if(postRepository.findById(id).get().getUser() != user){
			throw new IllegalArgumentException("글을 작성한 유저가 아닙니다.");
		}
		postRepository.deleteById(id);
	}

	@Transactional(readOnly = true)
	public PostListDTO findPostsByKeyword(String keyword){
		List<Post> postList = postRepository.findAllByTitleContaining(keyword);
		if(postList.isEmpty()){
			throw new NoListException();
		}
		return new PostListDTO(postList);
	}

	@Transactional
	public LikeDTO createPostLike(Long userId, Long postId){
		Post post = postRepository.findById(postId).orElseThrow(()->new CustomException(ErrorCode.NO_POST));
		User user = userRepository.findById(userId).orElseThrow(()->new CustomException(ErrorCode.NO_USER));

		boolean isAlready = postLikeRepository.existsByUserAndPost(user,post);
		if(!isAlready){
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
