package com.cmazxiaoma.mallSeckill.service;
import com.cmazxiaoma.core.IService;
import com.cmazxiaoma.mallSeckill.model.SuccessKilled;
import org.apache.ibatis.annotations.Param;


/**
 * Created by cmazxiaoma on 2018/04/28.
 */
public interface ISuccessKilledService extends IService<SuccessKilled> {

    /**
     * 查询秒杀成功信息
     * @param seckillId
     * @param userPhone
     * @return
     */
    SuccessKilled queryByIdWithSeckill(@Param("seckillId") Long seckillId,
                                       @Param("userPhone") Long userPhone);
}
