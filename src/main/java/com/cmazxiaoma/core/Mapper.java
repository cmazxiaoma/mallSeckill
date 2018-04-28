package com.cmazxiaoma.core;

import tk.mybatis.mapper.common.ConditionMapper;
import tk.mybatis.mapper.common.IdsMapper;
import tk.mybatis.mapper.common.MySqlMapper;

/**
 * 定制版MyBatis Mapper插件接口，如需其他接口参考官方文档自行添加。
 */
public interface Mapper<T> extends ConditionMapper<T>, IdsMapper<T>, MySqlMapper<T>, tk.mybatis.mapper.common.Mapper<T> {
}
