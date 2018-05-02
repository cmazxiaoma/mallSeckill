package com.cmazxiaoma.mallSeckill.service.impl;

import com.cmazxiaoma.mallSeckill.dao.SuccessKilledMapper;
import com.cmazxiaoma.mallSeckill.model.SuccessKilled;
import com.cmazxiaoma.mallSeckill.service.ISuccessKilledService;
import com.cmazxiaoma.core.AbstractService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;


/**
 * Created by cmazxiaoma on 2018/04/28.
 */
@Service
@Transactional
public class SuccessKilledServiceImpl extends AbstractService<SuccessKilled> implements ISuccessKilledService {

    @Resource
    private SuccessKilledMapper successKilledMapper;

    @Override
    public SuccessKilled queryByIdWithSeckill(Long seckillId, Long userPhone) {
        return successKilledMapper.queryByIdWithSeckill(seckillId, userPhone);
    }
}
