package com.hurui.web;

import com.hurui.entity.Post;
import io.vertx.codegen.annotations.Fluent;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.ext.web.api.service.ServiceRequest;
import io.vertx.ext.web.api.service.ServiceResponse;
import io.vertx.ext.web.api.service.WebApiServiceGen;

@WebApiServiceGen
public interface PostWebApiService {

    @Fluent
    PostWebApiService listPosts(Integer limit, ServiceRequest request, Handler<AsyncResult<ServiceResponse>> resultHandler);

    @Fluent
    PostWebApiService createPosts(Post body, ServiceRequest request, Handler<AsyncResult<ServiceResponse>> resultHandler);
}
