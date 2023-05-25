package com.fintech.creditprocessing.domain.exception;


import com.fintech.creditprocessing.constant.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CommonException extends RuntimeException {
    private ErrorCode code;
    private String message;
}
