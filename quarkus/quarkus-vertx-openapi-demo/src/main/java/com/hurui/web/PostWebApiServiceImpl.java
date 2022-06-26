package com.hurui.web;

import com.hurui.entity.Post;
import com.hurui.service.PostService;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.api.service.ServiceRequest;
import io.vertx.ext.web.api.service.ServiceResponse;
import io.vertx.mutiny.core.Vertx;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.Dependent;
import java.util.Optional;
import java.util.stream.Collectors;

@Dependent
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
        Optional.ofNullable(limit)
                .ifPresentOrElse(searchLimit -> {
                    this.postService.listPosts(searchLimit)
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
                                resultHandler.handle(
                                        Future.succeededFuture(
                                                ServiceResponse.completedWithJson(new JsonObject()).setStatusCode(500)
                                        )
                                );
                            });
                }, () -> {this.postService.listPosts(null)
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
                            resultHandler.handle(
                                    Future.succeededFuture(
                                            ServiceResponse.completedWithJson(new JsonObject()).setStatusCode(500)
                                    )
                            );
                        });

                });
        return this;
    }

    @Override
    public PostWebApiService createPosts(Post body, ServiceRequest request, Handler<AsyncResult<ServiceResponse>> resultHandler) {
        //System.out.println("body: " + body.toJson().encode());
        System.out.println("body: " + request.toJson().encode());
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
