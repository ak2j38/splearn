package org.park.tobyspring.splearn.adapter.webapi;

import java.time.LocalDateTime;
import org.park.tobyspring.splearn.domain.member.DuplicateEmailException;
import org.park.tobyspring.splearn.domain.member.DuplicateProfileException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class ApiControllerAdvice extends ResponseEntityExceptionHandler {

  @ExceptionHandler(Exception.class)
  public ProblemDetail exceptionHandler(Exception ex) {
    return getProblemDetail(HttpStatus.INTERNAL_SERVER_ERROR, ex);
  }

  @ExceptionHandler({DuplicateProfileException.class, DuplicateEmailException.class})
  public ProblemDetail emailExceptionHandler(DuplicateEmailException ex) {
    return getProblemDetail(HttpStatus.CONFLICT, ex);
  }

  private static ProblemDetail getProblemDetail(HttpStatus internalServerError, Exception ex) {
    ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(internalServerError, ex.getMessage());

    problemDetail.setProperty("timestamp", LocalDateTime.now());
    problemDetail.setProperty("exception", ex.getClass().getSimpleName());

    return problemDetail;
  }
}
