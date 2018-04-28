package com.cmazxiaoma.mallSeckill.exception;

import com.cmazxiaoma.core.exception.ServiceException;

/**
 * @author cmazxiaoma
 * @version V1.0
 * @Description: TODO
 * @date 2018/4/28 19:34
 */
public class SeckillException extends ServiceException {

    public SeckillException() {
        super();
    }

    public SeckillException(String message) {
        super(message);
    }

    public SeckillException(String message, Throwable cause) {
        super(message, cause);
    }
}
