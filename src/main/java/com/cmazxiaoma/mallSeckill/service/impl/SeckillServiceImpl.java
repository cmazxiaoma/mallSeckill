package com.cmazxiaoma.mallSeckill.service.impl;

import com.cmazxiaoma.core.cache.CacheManager;
import com.cmazxiaoma.core.exception.ServiceException;
import com.cmazxiaoma.mallSeckill.dao.SeckillMapper;
import com.cmazxiaoma.mallSeckill.dao.SuccessKilledMapper;
import com.cmazxiaoma.mallSeckill.enums.SeckillStateEnum;
import com.cmazxiaoma.mallSeckill.exception.RepeatKillException;
import com.cmazxiaoma.mallSeckill.exception.SeckillClosedException;
import com.cmazxiaoma.mallSeckill.exception.SeckillException;
import com.cmazxiaoma.mallSeckill.model.Seckill;
import com.cmazxiaoma.mallSeckill.model.SuccessKilled;
import com.cmazxiaoma.mallSeckill.service.ISeckillService;
import com.cmazxiaoma.core.AbstractService;
import com.cmazxiaoma.mallSeckill.vo.SeckillExecutionVo;
import com.cmazxiaoma.mallSeckill.vo.SeckillExposerVo;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.MapUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.entity.Condition;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.Date;
import java.util.Map;
import java.util.Objects;


/**
 * Created by cmazxiaoma on 2018/04/28.
 */
@Service
@Transactional
@Slf4j
public class SeckillServiceImpl extends AbstractService<Seckill> implements ISeckillService {

    @Resource
    private SeckillMapper seckillMapper;

    @Resource
    private SuccessKilledMapper successKilledMapper;

    @Resource
    private CacheManager cacheManager;

    private final String salt = "cmazxiaoma";

    @Override
    public SeckillExposerVo exportSeckillUrl(Long seckillId) {
        Seckill seckill = cacheManager.getSeckill(seckillId);

        if (Objects.isNull(seckill)) {
            seckill = seckillMapper.selectByPrimaryKey(seckillId);
            if (Objects.isNull(seckill)) {
                return new SeckillExposerVo(false, seckillId);
            } else {
                cacheManager.putSeckill(seckill);
            }
        }
        Date startTime = seckill.getStartTime();
        Date endTime = seckill.getEndTime();
        Date nowTime = new Date();

        if (nowTime.getTime() < startTime.getTime() || nowTime.getTime() > endTime.getTime()) {
            return new SeckillExposerVo(false, seckillId, nowTime.getTime(), startTime.getTime(), endTime.getTime());
        }

        String md5 = getMD5(seckillId);
        return new SeckillExposerVo(true, md5, seckillId);
    }

    private String getMD5(Long seckillId) {
        String base = seckillId + "/" + salt;
        String md5 = DigestUtils.md5DigestAsHex(base.getBytes());
        return seckillId + md5;
    }

    @Override
    public SeckillExecutionVo executeSeckill(Long seckillId, Long userPhone, String md5) {
        if (StringUtils.isEmpty(md5) || !Objects.equals(md5, getMD5(seckillId))) {
            throw new SeckillException("seckill data rewrite");
        }
        Date nowTime = new Date();

        try {
            int insertCount = successKilledMapper.insert(new SuccessKilled(seckillId, userPhone));

            if (insertCount <= 0) {
                throw new RepeatKillException("seckill repeated");
            } else {
                int updateCount = seckillMapper.reduceNumber(seckillId, nowTime);

                if (updateCount <= 0) {
                    throw new SeckillClosedException("seckill closed");
                } else {
                    SuccessKilled successKilled = successKilledMapper.queryByIdWithSeckill(seckillId, userPhone);
                    return new SeckillExecutionVo(seckillId, successKilled, SeckillStateEnum.SUCCESS);
                }
            }

        } catch (SeckillClosedException e) {
            log.error(e.getMessage());
            throw new SeckillClosedException(e.getMessage());
        } catch (RepeatKillException e) {
            log.error(e.getMessage());
            throw new RepeatKillException(e.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new ServiceException(e.getMessage());
        }
    }

    @Override
    public SeckillExecutionVo executeSeckillProcedure(Long seckillId, Long userPhone, String md5) {
        if (StringUtils.isEmpty(md5) || !Objects.equals(md5, getMD5(seckillId))) {
            return new SeckillExecutionVo(seckillId, SeckillStateEnum.DATA_REWRITE);
        }
        Date killTime = new Date();
        Map<String, Object> map = Maps.newHashMap();
        map.put("seckillId", seckillId);
        map.put("phone", userPhone);
        map.put("killTime", killTime);
        map.put("result", null);

        try {
            seckillMapper.killByProcedure(map);
            int resullt = MapUtils.getInteger(map, "-2");

            if (resullt == 1) {
                SuccessKilled successKilled = successKilledMapper.queryByIdWithSeckill(seckillId, userPhone);
                return new SeckillExecutionVo(seckillId, successKilled, SeckillStateEnum.SUCCESS);
            } else {
                return new SeckillExecutionVo(seckillId, SeckillStateEnum.stateOf(resullt));
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            return new SeckillExecutionVo(seckillId, SeckillStateEnum.INNER_ERROR);
        }
    }
}
