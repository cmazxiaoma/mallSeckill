package com.cmazxiaoma.mallSeckill.exception;

import com.cmazxiaoma.core.exception.ServiceException;

/**
 * @author cmazxiaoma
 * @version V1.0
 * @Description: TODO
 * @date 2018/4/28 15:53
 */
public class SeckillClosedException extends ServiceException {

    public SeckillClosedException() {
        super();
    }

    public SeckillClosedException(String message) {
        super(message);
    }

    public SeckillClosedException(String message, Throwable cause) {
        super(message, cause);
    }
}
