package com.cmazxiaoma.mallSeckill.enums;

import com.cmazxiaoma.core.ResultCode;
import com.cmazxiaoma.mallSeckill.model.Seckill;

import java.util.Objects;

/**
 * @author cmazxiaoma
 * @version V1.0
 * @Description: TODO
 * @date 2018/4/28 16:04
 */
public enum SeckillStateEnum {
    SUCCESS(1, "秒杀成功"),
    END(0, "秒杀结束"),
    REPEAT_KILL(-1, "重复秒杀"),
    INNER_ERROR(-2,"系统异常"),
    DATA_REWRITE(-3,"数据篡改");

    private Integer code;
    private String stateMsg;

    SeckillStateEnum(Integer code, String stateMsg) {
        this.code = code;
        this.stateMsg = stateMsg;
    }

    public Integer getCode() {
        return code;
    }

    public String getStateMsg() {
        return stateMsg;
    }

    public static SeckillStateEnum stateOf(Integer code) {
        for (SeckillStateEnum seckillStateEnum : values()) {
            if (Objects.equals(code, seckillStateEnum.getCode())) {
                return seckillStateEnum;
            }
        }
        return null;
    }
}
