package cn.lazyking.power.ex.handler;

import cn.lazyking.constants.BusinessStatus;
import cn.lazyking.model.Result;
import cn.lazyking.power.ex.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.nio.file.AccessDeniedException;

/**
 *  全局异常处理
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandle {

    @ExceptionHandler(RuntimeException.class)
    public Result<?> runtimeException(RuntimeException e) {
        log.error("运行时异常: {}", e.getMessage());
        return Result.fail(BusinessStatus.SERVER_INNER_ERROR);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public Result<?> accessDeniedException(AccessDeniedException e) throws AccessDeniedException {
        log.error("权限不足: {}", e.getMessage());
        throw e;
    }

    @ExceptionHandler(BusinessException.class)
    public Result<?> businessException(BusinessException e) {
        log.error("业务异常: {}", e.getMessage());
        return Result.fail(BusinessStatus.OPERATION_FAIL.getCode(), e.getMessage());
    }

}
