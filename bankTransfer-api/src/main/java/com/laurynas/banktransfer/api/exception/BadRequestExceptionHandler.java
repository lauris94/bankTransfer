package com.laurynas.banktransfer.api.exception;

import com.laurynas.banktransfer.api.model.ErrorResponse;
import io.micronaut.context.annotation.Requires;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.server.exceptions.ExceptionHandler;

import javax.inject.Singleton;

@Singleton
@Requires(classes = {BadRequestException.class, ExceptionHandler.class})
public class BadRequestExceptionHandler implements ExceptionHandler<BadRequestException, HttpResponse> {

    @Override
    public HttpResponse handle(HttpRequest request, BadRequestException exception) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setMessage(exception.getMessage());
        return HttpResponse.badRequest(errorResponse);
    }
}
