package com.hurui.web;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.api.service.ServiceRequest;
import io.vertx.ext.web.api.service.ServiceResponse;
import io.vertx.mutiny.core.Vertx;

import javax.enterprise.context.Dependent;

@Dependent
public class TestWebApiServiceImpl implements TestWebApiService {

    private final Vertx vertx;

    public TestWebApiServiceImpl(Vertx vertx) {
        this.vertx = vertx;
    }

    @Override
    public TestWebApiService listPets(Integer limit, ServiceRequest request, Handler<AsyncResult<ServiceResponse>> resultHandler) {
        this.vertx.setTimer(1000L, handler -> {
            System.out.println("Limit is " + limit);
            resultHandler.handle(Future.succeededFuture(ServiceResponse.completedWithJson(new JsonObject())));
        });
        return this;
    }

}
