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

import javax.enterprise.context.Dependent;
import java.util.Optional;
import java.util.stream.Collectors;

@Dependent
public class TestWebApiServiceImpl implements TestWebApiService {

    private final Vertx vertx;
    private final PostService postService;

    public TestWebApiServiceImpl(Vertx vertx, PostService postService) {
        this.vertx = vertx;
        this.postService = postService;
    }

    @Override
    public TestWebApiService listPosts(Integer limit, ServiceRequest request, Handler<AsyncResult<ServiceResponse>> resultHandler) {
//        this.vertx.setTimer(1000L, handler -> {
//            System.out.println("Limit is " + limit);
//            resultHandler.handle(Future.succeededFuture(ServiceResponse.completedWithJson(new JsonObject())));
//        });
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

}
