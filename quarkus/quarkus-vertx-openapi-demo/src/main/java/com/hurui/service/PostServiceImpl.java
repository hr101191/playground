package com.hurui.service;

import com.hurui.entity.Post;
import com.hurui.envers.RevisionInfo;
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
import javax.persistence.LockModeType;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static java.time.temporal.ChronoUnit.DAYS;
import static org.hibernate.envers.query.AuditEntity.id;
import static org.hibernate.envers.query.AuditEntity.revisionProperty;

@Dependent
public class PostServiceImpl implements PostService {

    private static final Logger logger = LoggerFactory.getLogger(PostServiceImpl.class);
    private static final Integer DEFAULT_DAYS_TO_FETCH_CHANGELOG = 30;

    private Vertx vertx;
    private final PostRepository postRepository;
    private final EntityManagerFactory entityManagerFactory;

    public PostServiceImpl(Vertx vertx, PostRepository postRepository, EntityManagerFactory entityManagerFactory) {
        this.vertx = vertx;
        this.postRepository = postRepository;
        this.entityManagerFactory = entityManagerFactory;
    }

    @Override
    public Uni<List<Post>> listPosts(Integer limit) {
        return Uni.createFrom().<List<Post>>emitter(uniEmitter -> {
                    try {
                        Optional<Integer> limitOptional = Optional.ofNullable(limit);
                        if(limitOptional.isPresent()) {
                            uniEmitter.complete(
                                    this.postRepository.findAll()
                                            .stream()
                                            .limit(limitOptional.get())
                                            .collect(Collectors.toList())
                            );
                        } else {
                            uniEmitter.complete(
                                    this.postRepository.findAll()
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
    public Uni<Post> createPost(Post post) {
        return Uni.createFrom().emitter(uniEmitter -> {
            try {
                if(post.getId() == null) {
                    this.postRepository.persist(post);
                    uniEmitter.complete(post);
                } else {
                    uniEmitter.fail(new RuntimeException("ID cannot be set for post creation."));
                }
            } catch (Exception ex) {
                uniEmitter.fail(ex);
            }
        });
    }

    @Override
    public Uni<Post> updatePost(Post post) {
        return Uni.createFrom().emitter(uniEmitter -> {
            try {
                if(post.getId() != null) {
                    Optional<Post> entityOptional = this.postRepository.findByIdOptional(post.getId());
                    entityOptional.ifPresentOrElse(entity -> {
                        entity.setTitle(post.getTitle());
                        entity.setText(post.getText());
                        entity.setDisplayName(post.getDisplayName());
                        entity.setLikes(post.getLikes());
                        entity.setDislikes(post.getDislikes());
                        entity.setComments(post.getComments());
                        this.postRepository.persist(entity);
                        uniEmitter.complete(entity);
                    }, () -> {
                        uniEmitter.fail(new RuntimeException("ID " + post.getId() + " is not found."));
                    });
                } else {
                    uniEmitter.fail(new RuntimeException("ID must be set for post update."));
                }
            } catch (Exception ex) {
                uniEmitter.fail(ex);
            }
        });
    }

    @Override
    public Uni<Void> deletePost(Long id) {
        return Uni.createFrom().emitter(uniEmitter -> {
            try {
                this.postRepository.deleteById(id);
                uniEmitter.complete(Uni.createFrom().voidItem().await().indefinitely());
            } catch (Exception ex) {
                uniEmitter.fail(ex);
            }
        });
    }

    @Override
    public Uni<JsonArray> getPostChangelog(Long id) {
        return Uni.createFrom().emitter(uniEmitter -> {
            try {
                uniEmitter.complete(getPostChangelogInternal(id));
            } catch (Exception ex) {
                uniEmitter.fail(ex);
            }
        });
    }

    private JsonArray getPostChangelogInternal(Long id) {
        AuditQuery auditQuery = AuditReaderFactory.get(this.entityManagerFactory.createEntityManager())
                .createQuery()
                .forRevisionsOfEntityWithChanges(Post.class, Boolean.TRUE)
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
