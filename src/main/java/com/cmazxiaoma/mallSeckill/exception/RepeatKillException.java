package com.cmazxiaoma.mallSeckill.exception;

import com.cmazxiaoma.core.exception.ServiceException;

/**
 * @author cmazxiaoma
 * @version V1.0
 * @Description: TODO
 * @date 2018/4/28 15:52
 */
public class RepeatKillException extends ServiceException {

    public RepeatKillException() {
        super();
    }

    public RepeatKillException(String message) {
        super(message);
    }

    public RepeatKillException(String message, Throwable cause) {
        super(message, cause);
    }
}
