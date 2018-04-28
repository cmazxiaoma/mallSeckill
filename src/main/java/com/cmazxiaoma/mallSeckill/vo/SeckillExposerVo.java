package com.cmazxiaoma.mallSeckill.vo;

import lombok.Data;

/**
 * @author cmazxiaoma
 * @version V1.0
 * @Description: TODO
 * @date 2018/4/28 18:49
 */
@Data
public class SeckillExposerVo {

    private Boolean exposed;

    private String md5;

    private Long seckillId;

    private Long nowTime;

    private Long startTime;

    private Long endTime;

    public SeckillExposerVo(Boolean exposed, Long seckillId) {
        this.exposed = exposed;
        this.seckillId = seckillId;
    }

    public SeckillExposerVo(Boolean exposed, String md5, Long seckillId) {
        this.exposed = exposed;
        this.md5 = md5;
        this.seckillId = seckillId;
    }

    public SeckillExposerVo(Boolean exposed, Long seckillId, Long nowTime, Long startTime, Long endTime) {
        this.exposed = exposed;
        this.seckillId = seckillId;
        this.nowTime = nowTime;
        this.startTime = startTime;
        this.endTime = endTime;
    }
}
