package com.turkcell.borrowservice.infrastructure.exception;

import com.turkcell.borrowservice.application.exceptions.BusinessException;
import com.turkcell.borrowservice.application.exceptions.NotFoundException;
import com.turkcell.borrowservice.infrastructure.exception.response.ApiErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Uygulama katmanında fırlatılan iş kuralı ihlallerini yakalar.
     * @return 400 Bad Request
     */
    @ExceptionHandler(BusinessException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST) // 400
    public ApiErrorResponse handleBusinessException(BusinessException ex) {
        return new ApiErrorResponse(ex.getMessage(), "BUSINESS_RULE_VIOLATION", null);
    }

    /**
     * Uygulama katmanında fırlatılan bulunamama (kayıt yok) hatalarını yakalar.
     * @return 404 Not Found
     */
    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND) // 404
    public ApiErrorResponse handleNotFoundException(NotFoundException ex) {
        return new ApiErrorResponse(ex.getMessage(), "RESOURCE_NOT_FOUND", null);
    }

    /**
     * @Valid anotasyonu ile fırlatılan Validation hatalarını yakalar.
     * @return 400 Bad Request
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponse> handleValidationException(MethodArgumentNotValidException ex) {

        // Tüm doğrulama hatalarını alıp tek bir mesajda birleştirme
        String detailedMessage = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining("; "));

        ApiErrorResponse response = new ApiErrorResponse("Validation Error: " + detailedMessage, "VALIDATION_ERROR", null);

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST); // 400
    }

    /**
     * Beklenmeyen genel sistem hatalarını yakalar.
     * @return 500 Internal Server Error
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR) // 500
    public ApiErrorResponse handleGeneralException(Exception ex) {
        System.err.println("Unforeseen Error: " + ex.getMessage());
        return new ApiErrorResponse("An unexpected error occurred.", "UNEXPECTED_ERROR", null);
    }
}
