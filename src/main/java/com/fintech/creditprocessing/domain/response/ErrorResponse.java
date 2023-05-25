package com.fintech.creditprocessing.domain.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fintech.creditprocessing.domain.exception.ErrorReport;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ErrorResponse(ErrorReport error) implements Response {

}
