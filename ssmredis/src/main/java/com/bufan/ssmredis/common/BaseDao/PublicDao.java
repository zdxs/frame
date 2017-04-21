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
public interface PublicDao<T> {

    //新增对象的时候得到key
    public String getPrimaryKey(T obj);

    //新增对象
    public boolean add(String key, T obj);

    //获取对象
    public Object getObj(String key);

    //根据key获取单个对象
    public T queryByKey(String key);

    //修改值根据对象 需要key
    public boolean update(String key, T obj);

    //新增一个list集合
    public boolean addRedisList(String key, Object obj);

    //新增一个list集合
    public boolean addRedisBeanList(String key);

    //针对于list集合的分页查询
    public PageUtil queryPageRedisList(String start, String end);
}
