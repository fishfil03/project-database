package com.fintech.creditprocessing.domain.exception;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fintech.creditprocessing.constant.ErrorCode;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ErrorReport(ErrorCode code, String message) {}
