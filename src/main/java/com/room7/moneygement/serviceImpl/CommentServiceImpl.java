package com.room7.moneygement.serviceImpl;

import com.room7.moneygement.dto.CommentDTO;
import com.room7.moneygement.model.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.room7.moneygement.repository.CommentRepository;
import com.room7.moneygement.service.CommentService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
	private final CommentRepository commentRepository;

	@Override
	public Page<CommentDTO> getComments(Pageable pageable) {
		Page<Comment> comments = commentRepository.findAll(pageable);
		return comments.map(this::toDTO);
	}

	@Override
	public CommentDTO createComment(CommentDTO commentDTO) {
		Comment comment = toEntity(commentDTO);
		comment = commentRepository.save(comment);
		return toDTO(comment);
	}

	@Override
	public CommentDTO updateComment(Long commentId, CommentDTO commentDTO) {
		Comment comment = commentRepository.findById(commentId)
				.orElseThrow(() -> new IllegalArgumentException("Comment not found"));
		comment.setContent(commentDTO.getContent());
		comment = commentRepository.save(comment);
		return toDTO(comment);
	}

	@Override
	public void deleteComment(Long commentId) {
		commentRepository.deleteById(commentId);
	}

	private CommentDTO toDTO(Comment comment) {
		CommentDTO commentDTO = new CommentDTO();
		commentDTO.setCommentId(comment.getCommentId());
		commentDTO.setUserId(comment.getUserId());
		commentDTO.setContent(comment.getContent());
		commentDTO.setCreatedAt(comment.getCreatedAt());
		commentDTO.setUpdatedAt(comment.getUpdatedAt());
		return commentDTO;
	}

	private Comment toEntity(CommentDTO commentDTO) {
		Comment comment = new Comment();
		comment.setUserId(commentDTO.getUserId());
		comment.setContent(commentDTO.getContent());
		comment.setCreatedAt(commentDTO.getCreatedAt());
		comment.setUpdatedAt(commentDTO.getUpdatedAt());
		return comment;
	}

}

