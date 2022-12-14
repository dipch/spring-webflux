package com.example.springwebflux.exception;

import org.springframework.boot.autoconfigure.web.WebProperties;
import org.springframework.boot.autoconfigure.web.reactive.error.AbstractErrorWebExceptionHandler;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.reactive.error.ErrorAttributes;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.*;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.Optional;

import static org.springframework.boot.web.error.ErrorAttributeOptions.*;
import static org.springframework.boot.web.error.ErrorAttributeOptions.defaults;
import static org.springframework.boot.web.error.ErrorAttributeOptions.of;

@Component
@Order(-2)
public class GlobalExceptionHandler extends AbstractErrorWebExceptionHandler {

    public GlobalExceptionHandler(ErrorAttributes errorAttributes, WebProperties resources, ApplicationContext applicationContext, ServerCodecConfigurer serverCodecConfigurer) {
        super(errorAttributes, resources.getResources(), applicationContext);
        this.setMessageWriters(serverCodecConfigurer.getWriters());
        this.setMessageReaders(serverCodecConfigurer.getReaders());
    }

    @Override
    protected RouterFunction<ServerResponse> getRoutingFunction(ErrorAttributes errorAttributes) {
        return RouterFunctions.route(RequestPredicates.all(), this::formatErrorResponse);
    }

    private Mono<ServerResponse> formatErrorResponse(ServerRequest request){
        String query = request.uri().getQuery();
        ErrorAttributeOptions errorAttributeOptions = isTraceEnabled(query) ? of(Include.STACK_TRACE) : defaults();
        Map<String, Object> errorAttributesMap = getErrorAttributes(request, errorAttributeOptions);
//        Map<String, Object> errorAttributesMap = getErrorAttributes(request, ErrorAttributeOptions.of(ErrorAttributeOptions.Include.STACK_TRACE));
//        Map<String, Object> errorAttributesMap = getErrorAttributes(request, ErrorAttributeOptions.defaults());
        int status = (int) Optional.ofNullable(errorAttributesMap.get("status")).orElse(500);
        return ServerResponse
                .status(status)
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(errorAttributesMap));
    }

    private boolean isTraceEnabled(String query){
        return !ObjectUtils.isEmpty(query) && query.contains("trace=true");
        // StringUtils.isEmpty() deperecated
    }
}
