package com.fintech.creditprocessing.domain.response;


import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record SuccessResponse<T>(T data) implements Response {

}
