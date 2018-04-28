package com.cmazxiaoma.mallSeckill.vo;

import com.cmazxiaoma.mallSeckill.enums.SeckillStateEnum;
import com.cmazxiaoma.mallSeckill.model.SuccessKilled;
import lombok.Data;

/**
 * @author cmazxiaoma
 * @version V1.0
 * @Description: TODO
 * @date 2018/4/28 18:50
 */
@Data
public class SeckillExecutionVo {

    /**
     * 秒杀商品ID
     */
    private Long seckillId;

    /**
     * 秒杀状态码
     */
    private Integer state;

    /**
     * 秒杀结果信息
     */
    private String stateInfo;

    /**
     * 进行秒杀的用户
     */
    private SuccessKilled successKilled;

    /**
     * 秒杀成功
     * @param seckillId
     * @param successKilled
     * @param seckillStateEnum
     */
    public SeckillExecutionVo(Long seckillId, SuccessKilled successKilled, SeckillStateEnum seckillStateEnum) {
        this.seckillId = seckillId;
        this.successKilled = successKilled;
        this.state = seckillStateEnum.getCode();
        this.stateInfo = seckillStateEnum.getStateMsg();
    }

    /**
     * 秒杀失败
     * @param seckillId
     * @param seckillStateEnum
     */
    public SeckillExecutionVo(Long seckillId, SeckillStateEnum seckillStateEnum) {
        this.seckillId = seckillId;
        this.state = seckillStateEnum.getCode();
        this.stateInfo = seckillStateEnum.getStateMsg();
    }
}
