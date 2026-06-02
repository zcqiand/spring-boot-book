package com.xrtech.api.exception;

import com.xrtech.api.response.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;
import java.util.stream.Collectors;

/**
 * 全局异常处理器
 * 统一处理所有未捕获的异常，返回标准化错误响应
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * 处理业务异常
     */
    @ExceptionHandler(BusinessException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ApiResponse<Void>> handleBusinessException(
            BusinessException<?> ex,
            HttpServletRequest request) {

        log.warn("业务异常: code={}, message={}, path={}",
                ex.getErrorCode(), ex.getMessage(), request.getRequestURI());

        ApiResponse<Void> response = ApiResponse.fail(
                extractCode(ex.getErrorCode()),
                ex.getMessage(),
                request.getRequestURI()
        );

        return ResponseEntity.status(ex.getHttpStatus()).body(response);
    }

    /**
     * 处理资源未找到异常
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<ApiResponse<Void>> handleResourceNotFoundException(
            ResourceNotFoundException<?> ex,
            HttpServletRequest request) {

        log.warn("资源未找到: code={}, message={}, path={}",
                ex.getErrorCode(), ex.getMessage(), request.getRequestURI());

        ApiResponse<Void> response = ApiResponse.fail(
                extractCode(ex.getErrorCode()),
                ex.getMessage(),
                request.getRequestURI()
        );

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    /**
     * 处理方法参数验证异常（@Valid + @RequestBody）
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ApiResponse<Void>> handleValidationException(
            MethodArgumentNotValidException ex,
            HttpServletRequest request) {

        Map<String, String> fieldErrors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .collect(Collectors.toMap(
                        FieldError::getField,
                        error -> error.getDefaultMessage() != null
                            ? error.getDefaultMessage()
                            : "无效的值",
                        (existing, replacement) -> existing
                ));

        log.debug("参数验证失败: errors={}, path={}", fieldErrors, request.getRequestURI());

        ApiResponse<Void> response = new ApiResponse<>(
                false, 400, "参数验证失败", null, fieldErrors,
                java.time.Instant.now(), request.getRequestURI()
        );

        return ResponseEntity.badRequest().body(response);
    }

    /**
     * 处理约束违反异常（@Validated + 方法参数）
     */
    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ApiResponse<Void>> handleConstraintViolationException(
            ConstraintViolationException ex,
            HttpServletRequest request) {

        Map<String, String> violations = ex.getConstraintViolations()
                .stream()
                .collect(Collectors.toMap(
                        violation -> extractParameterName(violation),
                        ConstraintViolation::getMessage,
                        (existing, replacement) -> existing
                ));

        log.debug("约束违反: violations={}, path={}", violations, request.getRequestURI());

        ApiResponse<Void> response = new ApiResponse<>(
                false, 400, "参数超出有效范围", null, violations,
                java.time.Instant.now(), request.getRequestURI()
        );

        return ResponseEntity.badRequest().body(response);
    }

    /**
     * 处理所有未捕获的异常（兜底）
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<ApiResponse<Void>> handleGenericException(
            Exception ex,
            HttpServletRequest request) {

        log.error("未处理异常: message={}, path={}", ex.getMessage(), request.getRequestURI(), ex);

        ApiResponse<Void> response = ApiResponse.fail(
                500,
                "服务器内部错误，请稍后重试或联系管理员",
                request.getRequestURI()
        );

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

    /**
     * 从错误码中提取 int 值
     */
    private int extractCode(Object errorCode) {
        if (errorCode instanceof Integer) {
            return (Integer) errorCode;
        }
        return errorCode.hashCode();
    }

    /**
     * 从约束违反中提取参数名
     */
    private String extractParameterName(ConstraintViolation<?> violation) {
        String path = violation.getPropertyPath().toString();
        int lastDot = path.lastIndexOf('.');
        return lastDot > 0 ? path.substring(lastDot + 1) : path;
    }
}