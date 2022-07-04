package com.hurui.service;

import com.hurui.entity.Comment;
import com.hurui.entity.Post;
import com.hurui.envers.RevisionInfo;
import com.hurui.repository.CommentRepository;
import com.hurui.repository.PostRepository;
import io.smallrye.mutiny.Uni;
import io.vertx.core.json.JsonArray;
import io.vertx.mutiny.core.Vertx;
import org.hibernate.envers.AuditReaderFactory;
import org.hibernate.envers.RevisionType;
import org.hibernate.envers.query.AuditQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.Dependent;
import javax.persistence.EntityManagerFactory;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.time.temporal.ChronoUnit.DAYS;
import static org.hibernate.envers.query.AuditEntity.id;
import static org.hibernate.envers.query.AuditEntity.revisionProperty;

@Dependent
public class CommentServiceImpl implements CommentService {

    private static final Logger logger = LoggerFactory.getLogger(PostServiceImpl.class);
    private static final Integer DEFAULT_DAYS_TO_FETCH_CHANGELOG = 30;

    private Vertx vertx;
    private final EntityManagerFactory entityManagerFactory;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;

    public CommentServiceImpl(Vertx vertx, EntityManagerFactory entityManagerFactory, PostRepository postRepository, CommentRepository commentRepository) {
        this.vertx = vertx;
        this.entityManagerFactory = entityManagerFactory;
        this.postRepository = postRepository;
        this.commentRepository = commentRepository;
    }

    @Override
    public Uni<List<Comment>> listComments(Integer limit) {
        return Uni.createFrom().<List<Comment>>emitter(uniEmitter -> {
            try {
                Optional<Integer> limitOptional = Optional.ofNullable(limit);
                if(limitOptional.isPresent()) {
                    uniEmitter.complete(
                            this.commentRepository.findAll()
                                    .stream()
                                    .limit(limitOptional.get())
                                    .collect(Collectors.toList())
                    );
                } else {
                    uniEmitter.complete(
                            this.commentRepository.findAll()
                                    .stream()
                                    .collect(Collectors.toList())
                    );
                }
            } catch (Exception ex) {
                uniEmitter.fail(ex);
            }
        });
    }

    @Override
    public Uni<List<Comment>> listCommentsByPostId(Long postId) {
        return Uni.createFrom().emitter(uniEmitter -> {
            try {
                Optional<Long> postIdOptional = Optional.ofNullable(postId);
                if(postIdOptional.isPresent()) {
                    this.postRepository.findByIdOptional(postIdOptional.get())
                            .ifPresentOrElse(
                                    post -> uniEmitter.complete(post.getComments()),
                                    () -> uniEmitter.complete(new ArrayList<>())
                            );
                    uniEmitter.fail(new RuntimeException("postId cannot be null."));
                }
            } catch (Exception ex) {
                uniEmitter.fail(ex);
            }
        });
    }

    @Override
    public Uni<Comment> createComment(Long postId, Comment comment) {
        return Uni.createFrom().emitter(uniEmitter -> {
            try {
                this.postRepository.findByIdOptional(postId)
                        .ifPresentOrElse(post -> {
                            comment.setPost(post);
                            this.commentRepository.persist(comment);
                            uniEmitter.complete(comment);
                        }, () -> {
                            uniEmitter.fail(new RuntimeException("postId is not found"));
                        });
            } catch (Exception ex) {
                uniEmitter.fail(ex);
            }
        });
    }

    @Override
    public Uni<Void> deleteComment(Long id) {
        return Uni.createFrom().emitter(uniEmitter -> {
            try {
                this.commentRepository.deleteById(id);
                uniEmitter.complete(Uni.createFrom().voidItem().await().indefinitely());
            } catch (Exception ex) {
                uniEmitter.fail(ex);
            }
        });
    }

    @Override
    public Uni<JsonArray> getCommentChangelog(Long id) {
        return Uni.createFrom().emitter(uniEmitter -> {
            try {
                uniEmitter.complete(getCommentChangelogInternal(id));
            } catch (Exception ex) {
                uniEmitter.fail(ex);
            }
        });
    }

    private JsonArray getCommentChangelogInternal(Long id) {
        AuditQuery auditQuery = AuditReaderFactory.get(this.entityManagerFactory.createEntityManager())
                .createQuery()
                .forRevisionsOfEntityWithChanges(Comment.class, Boolean.TRUE)
                .add(id().eq(id));
        auditQuery.add(revisionProperty("timestamp").ge(Instant.now().minus(DEFAULT_DAYS_TO_FETCH_CHANGELOG, DAYS).toEpochMilli()));
        List<Object[]> result = auditQuery.getResultList();
        result.forEach(auditResult -> {
            Post post = (Post) auditResult[0];
            logger.info("Audit query: {}", post.toJson().encode());
            RevisionInfo revisionInfo = (RevisionInfo) auditResult[1];
            RevisionType revisionType = (RevisionType) auditResult[2];
            Set<String> modifiedFields = (Set<String>) auditResult[3];
            logger.info("Modified Fields: {}", new JsonArray(Arrays.asList(modifiedFields.toArray())).encode());
        });
        return new JsonArray(result);
    }
}
