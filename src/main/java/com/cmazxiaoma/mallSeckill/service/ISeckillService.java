package com.cmazxiaoma.mallSeckill.service;
import com.cmazxiaoma.mallSeckill.model.Seckill;
import com.cmazxiaoma.core.IService;
import com.cmazxiaoma.mallSeckill.vo.SeckillExecutionVo;
import com.cmazxiaoma.mallSeckill.vo.SeckillExposerVo;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.Map;


/**
 * Created by cmazxiaoma on 2018/04/28.
 */
public interface ISeckillService extends IService<Seckill> {

    /**
     * 暴露秒杀地址
     * @param seckillId
     * @return
     */
    SeckillExposerVo exportSeckillUrl(Long seckillId);

    /**
     * 进行秒杀
     *
     * @param seckillId
     * @param userPhone
     * @param md5
     * @return
     */
    SeckillExecutionVo executeSeckill(Long seckillId, Long userPhone, String md5);

    /**
     * 通过存储过程进行秒杀
     * @param seckillId
     * @param userPhone
     * @param md5
     * @return
     */
    SeckillExecutionVo executeSeckillProcedure(Long seckillId, Long userPhone, String md5);
}
