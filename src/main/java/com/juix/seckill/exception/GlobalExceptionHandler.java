package com.juix.seckill.exception;

import com.juix.seckill.common.Result;
import com.juix.seckill.enums.ServerEnums;
import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @param: none
 * @description:
 * @author: KingJ
 * @create: 2019-07-21 17:28
 **/
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = Exception.class)
    public Result<String> exceptionHandler(HttpServletRequest request, Exception e) {
        e.printStackTrace();

        if (e instanceof GlobalException) {
            GlobalException ex = (GlobalException) e;
            return Result.errorException(((GlobalException) e).getStatus().getMsg());
        } else if (e instanceof BindException) {
            BindException ex = (BindException) e;
            List<ObjectError> errors = ex.getAllErrors();
            ObjectError error = errors.get(0);
            String msg = ServerEnums.SERVER_PARAMETER_ERROR.fillParameter(ServerEnums.SERVER_PARAMETER_ERROR.getMsg(), error.getDefaultMessage());
            return Result.errorException(msg);
        } else {
            return Result.errorMsg();
        }
    }
}
