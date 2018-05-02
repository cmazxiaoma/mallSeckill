package com.cmazxiaoma.mallSeckill.dao;

import com.cmazxiaoma.core.Mapper;
import com.cmazxiaoma.mallSeckill.model.Seckill;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.Map;

public interface SeckillMapper extends Mapper<Seckill> {

    /**
     * 减少库存
     * @param seckillId
     * @param killTime
     * @return
     */
    int reduceNumber(@Param("seckillId") Long seckillId,
                     @Param("killTime") Date killTime);

    /**
     * 通过存储过程，进行秒杀
     * @param paramsMap
     */
    void killByProcedure(Map<String, Object> paramsMap);
}