package org.sopt.service.comment;

import org.sopt.domain.Comment;
import org.sopt.domain.CommentLike;
import org.sopt.domain.Post;
import org.sopt.domain.User;
import org.sopt.dto.LikeDTO;
import org.sopt.dto.request.CommentRequest;
import org.sopt.dto.request.CommentUpdateRequest;
import org.sopt.dto.CommentResponse;
import org.sopt.global.exception.ErrorCodes.CustomException;
import org.sopt.global.exception.ErrorCodes.ErrorCode;
import org.sopt.repository.CommentLikeRepository;
import org.sopt.repository.CommentRepository;
import org.sopt.repository.PostRepository;
import org.sopt.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CommentService {

	//여기서 userRepository를 가져다가 쓰는 것이 구조적으로 맞을까?
	private final UserRepository userRepository;
	private final CommentRepository commentRepository;
	private final PostRepository postRepository;
	private final CommentLikeRepository commentLikeRepository;

	@Transactional
	public Comment createComment(CommentRequest request, Long userId){
		//여기서 유효성을 검색할게 있을까?
		//유저가 실제로 존재하는 유저인지 확인
		User user = checkUser(userId);
		Post post = checkPostId(request.postId());

		Comment comment = Comment.builder()
			.content(request.comment())
			.post(post)
			.user(user)
			.build();

		return commentRepository.save(comment);
	}

	@Transactional
	public void updateComment(CommentUpdateRequest request, Long userId){
		User user = checkUser(userId);
		checkPostId(request.postId());

		Comment comment = commentRepository.findById(request.commentId()).orElseThrow(()-> new CustomException(
			ErrorCode.NO_COMMENT));
		if(user != comment.getUser()){
			throw new CustomException(ErrorCode.WRONG_USER);
		}

		comment.setContent(request.content());

		commentRepository.save(comment);
	}

	@Transactional(readOnly = true)
	public CommentResponse getComments(Long commentId){
		return CommentResponse.from(checkComment(commentId));
	}

	@Transactional
	public void deleteComment(Long commentId, Long userId){
		Comment comment = checkComment(commentId);
		User user = checkUser(userId);

		if(comment.getUser() != user){
			throw new CustomException(ErrorCode.WRONG_USER);
		}

		commentRepository.delete(comment);
	}

	//comment로 수정
	//추가로 여기에 비동기적으로 동작하게끔 만들어두면 좋겠다.
	//그래야지 현재 몇 명 눌렀는지 바로 반영시킬 수 있게 되니까 ㅇㅋ
	@Transactional
	public LikeDTO createCommentLike(Long userId, Long commentId){
		Comment comment = commentRepository.findById(commentId).orElseThrow(()->new CustomException(ErrorCode.NO_COMMENT));
		User user = userRepository.findById(userId).orElseThrow(()->new CustomException(ErrorCode.NO_USER));

		CommentLike commentLike = commentLikeRepository.findByUserAndComment(user,comment);
		if(commentLike != null){
			//여기서 비용을 고려해봐야하는데 필드에 boolean 둬서 하는 방법 or 그냥 delete 하는 방법
			//일단은 delete 하는 방식으로 구현하고, 후에 수정할 예정
			commentLikeRepository.deleteById(commentLike.getId());
			return new LikeDTO("댓글에 대한 좋아요가 취소되었습니다.");
		}

		CommentLike commentLike1 = CommentLike.builder()
			.comment(comment)
			.user(user)
			.build();
		commentLikeRepository.save(commentLike1);
		return new LikeDTO("댓글에 대한 좋아요가 추가되었습니다.");
	}

	private User checkUser(Long userId){
		//유저가 존재하는지 확인하기 + 추가로 수정하기 위해서는 해당 댓글을 작성한 것이 유저가 맞는지 확인하기
		//orElseThrow는 supplier가 들어와야 해서 람다식으로 제공해주는 것이다.
		return userRepository.findById(userId)
			.orElseThrow(()->new CustomException(ErrorCode.NO_USER));
	}

	private Post checkPostId(Long postId){
		return postRepository.findById(postId)
			.orElseThrow(()->new CustomException(ErrorCode.NO_LIST));
	}

	private Comment checkComment(Long commentId){
		return commentRepository.findById(commentId)
			.orElseThrow(()->new CustomException(ErrorCode.NO_COMMENT));
	}

}
