package com.room7.moneygement.controller;

import com.room7.moneygement.dto.CommentDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.room7.moneygement.service.CommentService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/comments")
public class CommentController {

	private final CommentService commentService;

	@GetMapping
	public ResponseEntity<Page<CommentDTO>> getComments(Pageable pageable) {
		Page<CommentDTO> comments = commentService.getComments(pageable);
		return ResponseEntity.ok(comments);
	}

	@PostMapping
	public ResponseEntity<CommentDTO> createComment(@RequestBody CommentDTO commentDTO) {
		CommentDTO createdComment = commentService.createComment(commentDTO);
		return ResponseEntity.status(HttpStatus.CREATED).body(createdComment);
	}

	@PutMapping("/{commentId}")
	public ResponseEntity<CommentDTO> updateComment(@PathVariable Long commentId, @RequestBody CommentDTO commentDTO) {
		CommentDTO updatedComment = commentService.updateComment(commentId, commentDTO);
		return ResponseEntity.ok(updatedComment);
	}

	@DeleteMapping("/{commentId}")
	public ResponseEntity<Void> deleteComment(@PathVariable Long commentId) {
		commentService.deleteComment(commentId);
		return ResponseEntity.noContent().build();
	}
}