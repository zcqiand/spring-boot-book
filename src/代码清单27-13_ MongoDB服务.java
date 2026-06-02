package com.example.dal.service;

import com.example.dal.document.OperationLog;
import com.example.dal.document.Comment;
import com.example.dal.repository.mongo.OperationLogRepository;
import com.example.dal.repository.mongo.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
public class MongoService {

    @Autowired
    private OperationLogRepository operationLogRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private MongoTemplate mongoTemplate;

    public OperationLog saveOperationLog(OperationLog log) {
        log.setCreatedAt(LocalDateTime.now());
        return operationLogRepository.save(log);
    }

    public List<OperationLog> getUserOperationLogs(Long userId) {
        return operationLogRepository.findByUserId(userId);
    }

    public Comment saveComment(Comment comment) {
        comment.setCreatedAt(LocalDateTime.now());
        comment.setUpdatedAt(LocalDateTime.now());
        return commentRepository.save(comment);
    }

    public List<Comment> getTargetComments(String targetType, String targetId) {
        return commentRepository.findByTargetTypeAndTargetId(targetType, targetId);
    }

    public List<Comment> getUserComments(Long userId) {
        return commentRepository.findByUserId(userId);
    }

    public void deleteComment(String commentId) {
        commentRepository.deleteById(commentId);
    }

    public Long countTargetComments(String targetType, String targetId) {
        Query query = new Query(Criteria.where("targetType").is(targetType)
            .and("targetId").is(targetId));
        return mongoTemplate.count(query, Comment.class);
    }

    public List<Comment> getRecentComments(int limit) {
        Query query = new Query().with(Sort.by(Sort.Direction.DESC, "createdAt")).limit(limit);
        return mongoTemplate.find(query, Comment.class);
    }
}