/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bufan.ssmredis.dao.imp;

import com.bufan.ssmredis.bean.DemoTest;
import com.bufan.ssmredis.common.BaseDao.imp.RedisDaoImp;
import com.bufan.ssmredis.dao.DemoTestDao;
import com.bufan.ssmredis.util.PageUtil;

/**
 *
 * @author xiaosun
 */
public class DemoTestDaoImp extends RedisDaoImp<DemoTest> implements DemoTestDao {

    /**
     * 新增对象
     *
     * @param key
     * @param obj
     * @return
     */
    @Override
    public boolean add(String key, DemoTest obj) {
        return super.add(key, obj);
    }

    /**
     * 获取对象
     *
     * @param key
     * @return
     */
    @Override
    public Object getObj(String key) {
        return super.getObj(key);
    }

    /**
     * 获取key
     *
     * @param key
     * @return
     */
    @Override
    public DemoTest queryByKey(String key) {
        return super.queryByKey(key);
    }

    /**
     * 获取key
     *
     * @param obj
     * @return
     */
    @Override
    public String getPrimaryKey(DemoTest obj) {
        return super.incr(obj.getClass().getName().toLowerCase());
    }

    /**
     * 修改对象
     *
     * @param key 键
     * @param obj 修改的对象
     * @return
     */
    @Override
    public boolean update(String key, DemoTest obj) {
        return super.update(key, obj);
    }

    /**
     * 新增集合
     *
     * @param key
     * @param obj
     * @return
     */
    @Override
    public boolean addRedisList(String key, Object obj) {
        return super.addRedisList(key, obj);
    }

    /**
     * 分页查询
     *
     * @param start 开始页
     * @param end   结束页
     * @return
     */
    @Override
    public PageUtil queryPageRedisList(String start, String end) {
        return super.queryPageRedisList(start, end);
    }

    /**
     * 新增一个list集合数据
     * key=实体类+KEY_REDIS_SPLIT+KEY_REDIS_LIST 值=存储实体id
     *
     * @param key 存储集合的key
     * @return true|false
     */
    @Override
    public boolean addRedisBeanList(String key) {
        return super.addRedisBeanList(key);
    }
}
