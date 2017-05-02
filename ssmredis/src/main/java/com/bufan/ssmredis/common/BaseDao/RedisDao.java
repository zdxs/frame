/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bufan.ssmredis.common.BaseDao;

import com.bufan.ssmredis.util.PageUtil;

/**
 * redis基础操作接口
 *
 * @author xiaosun
 * @param <T>
 */
public interface RedisDao<T> {

    //新增对象
    public boolean add(String key, T obj);

    //新增对象
    public boolean addObj(T obj);

    //新增一个list集合
    public boolean addRedisBeanList(String key);

    //新增一个list集合
    public boolean addRedisList(String key, Object obj);

    //针对于list集合的分页查询
    public PageUtil queryPageRedisList(String start, String end);

    //查询集合的大小
    public String queryRedisSize(String key, String type);

    //获取对象
    public Object getObj(String key);

    //批量删除 进行了模糊删除慎用
    public int delBatch(String... pattern);

    //批量删除 精确删除
    public int delBatchAccurate(String... pattern);

    //根据key获取单个对象
    public T queryByKey(String key);

    //当插入相同的键返回一个整数
    public String incr(String key);

    public String increment(String key);

    public String increment(String key, Long kb);

    //递减
    public String decr(String key, Long kb);

    //判断存在
    public boolean exits(String key);

    //修改值根据对象 需要key
    public boolean exists(final String key);

    //修改数据
    public boolean update(String key, T obj);

    //通过key删除实体在list集合中的数据
    public boolean delListByKey(String key);

}
