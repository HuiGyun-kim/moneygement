package com.room7.moneygement.service;

import com.room7.moneygement.dto.CommentDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CommentService {
    Page<CommentDTO> getComments(Pageable pageable);
    CommentDTO createComment(CommentDTO commentDTO);
    CommentDTO updateComment(Long commentId, CommentDTO commentDTO);
    void deleteComment(Long commentId);
}
