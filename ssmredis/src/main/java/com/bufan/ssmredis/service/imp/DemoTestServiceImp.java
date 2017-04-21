/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bufan.ssmredis.service.imp;

import com.bufan.ssmredis.bean.DemoTest;
import com.bufan.ssmredis.dao.DemoTestDao;
import com.bufan.ssmredis.service.DemoTestService;
import com.bufan.ssmredis.util.PageUtil;

/**
 *
 * @author xiaosun
 */
public class DemoTestServiceImp implements DemoTestService {

    private DemoTestDao demotestdao;

    @Override
    public boolean add(String key, DemoTest obj) {
        return demotestdao.add(key, obj);
    }

    @Override
    public Object getObj(String key) {
        return demotestdao.getObj(key);
    }

    @Override
    public String getPrimaryKey(DemoTest obj) {
        return demotestdao.getPrimaryKey(obj);
    }

    @Override
    public DemoTest queryByKey(String key) {
        return demotestdao.queryByKey(key);
    }

    public DemoTestDao getDemotestdao() {
        return demotestdao;
    }

    public void setDemotestdao(DemoTestDao demotestdao) {
        this.demotestdao = demotestdao;
    }

    @Override
    public boolean update(String key, DemoTest obj) {
        return demotestdao.update(key, obj);
    }

    @Override
    public boolean addRedisList(String key, Object obj) {
        return demotestdao.addRedisList(key, obj);
    }

    @Override
    public PageUtil queryPageRedisList(String start, String end) {
        return demotestdao.queryPageRedisList(start, end);
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
        return demotestdao.addRedisBeanList(key);
    }

}
