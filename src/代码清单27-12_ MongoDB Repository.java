package com.example.dal.repository.mongo;

import com.example.dal.document.OperationLog;
import com.example.dal.document.Comment;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface OperationLogRepository extends MongoRepository<OperationLog, String> {

    List<OperationLog> findByUserId(Long userId);

    List<OperationLog> findByTargetTypeAndTargetId(String targetType, String targetId);

    List<OperationLog> findByOperationType(String operationType);
}

@Repository
public interface CommentRepository extends MongoRepository<Comment, String> {

    List<Comment> findByTargetTypeAndTargetId(String targetType, String targetId);

    List<Comment> findByUserId(Long userId);

    List<Comment> findByParentId(String parentId);

    List<Comment> findByTargetTypeAndTargetIdAndStatus(
        String targetType, String targetId, Integer status);
}