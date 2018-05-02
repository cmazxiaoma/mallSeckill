package com.cmazxiaoma.mallSeckill.dao;

import com.cmazxiaoma.core.Mapper;
import com.cmazxiaoma.mallSeckill.model.SuccessKilled;
import org.apache.ibatis.annotations.Param;

public interface SuccessKilledMapper extends Mapper<SuccessKilled> {

    /**
     * 查询秒杀成功信息
     * @param seckillId
     * @param userPhone
     * @return
     */
    SuccessKilled queryByIdWithSeckill(@Param("seckillId") Long seckillId,
                                       @Param("userPhone") Long userPhone);
}