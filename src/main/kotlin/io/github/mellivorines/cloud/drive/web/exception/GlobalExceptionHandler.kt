package io.github.mellivorines.cloud.drive.web.exception

import io.github.mellivorines.cloud.drive.web.model.ResultModel
import io.github.mellivorines.cloud.drive.web.model.fail
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import java.sql.SQLException


/**
 * @Description:全局异常处理
 *
 * @author lilinxi
 * @version 1.0.0
 * @since 2023/6/27
 */
@RestControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler
    fun handlerException(exception: Exception): ResultModel {
        val message = exception.message
        return fail(message)
    }

    @ExceptionHandler(value = [HttpMessageNotReadableException::class])
    fun handlerHttpMessageNotReadableException(exception: Exception): ResultModel {
        val message = exception.message
        return fail(message)
    }

    @ExceptionHandler(value = [SQLException::class])
    fun msgMySQLExecuteError(exception: java.lang.Exception): ResultModel {
        exception.printStackTrace()
        val message = exception.message
        return fail(message)
    }

    @ExceptionHandler(value = [NullPointerException::class])
    fun findNullPointerException(e: NullPointerException): ResultModel {
        val message = e.message
        return fail("检查业务逻辑是否合理：$message")
    }

}