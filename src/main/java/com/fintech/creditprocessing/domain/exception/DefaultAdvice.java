package com.fintech.creditprocessing.domain.exception;


import com.fintech.creditprocessing.constant.ErrorCode;
import com.fintech.creditprocessing.domain.response.ErrorResponse;
import com.fintech.creditprocessing.domain.response.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Slf4j
public class DefaultAdvice {

    @ExceptionHandler(CommonException.class)
    public ResponseEntity<Response> handleException(CommonException exp) {
        log.error(exp.toString());
        return new ResponseEntity<>(new ErrorResponse(new ErrorReport(exp.getCode(), exp.getMessage())), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Response> handleUnexpectedException(Exception exp) {

        exp.printStackTrace();
        log.error("Internal server error: {}", exp.toString());
        return new ResponseEntity<>(new ErrorResponse(new ErrorReport(ErrorCode.INTERNAL_SERVER_ERROR,
                "Внутренняя ошибка сервера")), HttpStatus.INTERNAL_SERVER_ERROR);

    }
}
