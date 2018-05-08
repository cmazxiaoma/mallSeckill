package com.cmazxiaoma.mallSeckill.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;
import javax.persistence.*;

@Table(name = "success_killed")
@Data
public class SuccessKilled {
    /**
     * 秒杀商品id
     */
    @Id
    @Column(name = "seckill_id")
    private Long seckillId;

    /**
     * 用户手机号
     */
    @Column(name = "user_phone")
    private Long userPhone;

    /**
     * 状态标识：-1 无效 0 成功 1 已付款
     */
    private Integer state;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private Date createTime;

    @Transient
    private Seckill seckill;

    /**
     * 获取秒杀商品id
     *
     * @return seckill_id - 秒杀商品id
     */
    public Long getSeckillId() {
        return seckillId;
    }

    /**
     * 设置秒杀商品id
     *
     * @param seckillId 秒杀商品id
     */
    public void setSeckillId(Long seckillId) {
        this.seckillId = seckillId;
    }

    /**
     * 获取用户手机号
     *
     * @return user_phone - 用户手机号
     */
    public Long getUserPhone() {
        return userPhone;
    }

    /**
     * 设置用户手机号
     *
     * @param userPhone 用户手机号
     */
    public void setUserPhone(Long userPhone) {
        this.userPhone = userPhone;
    }

    /**
     * 获取状态标识：-1 无效 0 成功 1 已付款
     *
     * @return state - 状态标识：-1 无效 0 成功 1 已付款
     */
    public Integer getState() {
        return state;
    }

    /**
     * 设置状态标识：-1 无效 0 成功 1 已付款
     *
     * @param state 状态标识：-1 无效 0 成功 1 已付款
     */
    public void setState(Integer state) {
        this.state = state;
    }

    /**
     * 获取创建时间
     *
     * @return create_time - 创建时间
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * 设置创建时间
     *
     * @param createTime 创建时间
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}