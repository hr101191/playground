package com.hurui.web;

import com.hurui.entity.Post;
import com.hurui.service.PostService;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonArray;
import io.vertx.ext.web.api.service.ServiceRequest;
import io.vertx.ext.web.api.service.ServiceResponse;
import io.vertx.mutiny.core.Vertx;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.Dependent;
import javax.transaction.Transactional;
import java.util.stream.Collectors;

@Dependent
@Transactional
public class PostWebApiServiceImpl implements PostWebApiService {

    private static final Logger logger = LoggerFactory.getLogger(PostWebApiServiceImpl.class);

    private final Vertx vertx;
    private final PostService postService;

    public PostWebApiServiceImpl(Vertx vertx, PostService postService) {
        this.vertx = vertx;
        this.postService = postService;
    }

    @Override
    public PostWebApiService listPosts(Integer limit, ServiceRequest request, Handler<AsyncResult<ServiceResponse>> resultHandler) {
        logger.info("Incoming Http Request - Operation ID: [listPosts] | Service Request: {}", request.toJson().encode());
        this.postService.listPosts(limit)
                .subscribe()
                .with(result -> {
                    resultHandler.handle(
                            Future.succeededFuture(
                                    ServiceResponse.completedWithJson(
                                            new JsonArray(
                                                    result.stream()
                                                            .map(Post::toJson)
                                                            .collect(Collectors.toList())
                                            )
                                    )
                            )
                    );
                }, throwable -> {
                    logger.error("Operation ID: [listPosts] | Failed to retrieve data. Stacktrace: ", throwable);
                    resultHandler.handle(
                            Future.succeededFuture(
                                    new ServiceResponse().setStatusCode(500)
                            )
                    );
                });
        return this;
    }

    @Override
    public PostWebApiService createPosts(Post body, ServiceRequest request, Handler<AsyncResult<ServiceResponse>> resultHandler) {
        logger.info("Incoming Http Request - Operation ID: [createPosts] | Service Request: {}", request.toJson().encode());
        this.postService.createPosts(body)
                .subscribe()
                .with(result -> {
                    resultHandler.handle(
                            Future.succeededFuture(
                                    new ServiceResponse().setStatusCode(204)
                            )
                    );
                }, throwable -> {
                    logger.error("Operation: [createPosts] failed. Stacktrace: ", throwable);
                    resultHandler.handle(
                            Future.succeededFuture(
                                    new ServiceResponse().setStatusCode(500)
                            )
                    );
                });
        return this;
    }
}
