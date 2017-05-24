/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.core.bufan.syscore.services.impl;

import com.core.bufan.syscore.bean.DemoTest;
import com.core.bufan.syscore.dao.DemoTestDao;
import com.core.bufan.syscore.services.DemoTestService;
import com.core.bufan.syscore.util.PageUtil;

/**
 *
 * @author xiaosun
 */
public class DemoTestServiceImp implements DemoTestService {

    private DemoTestDao demoTestDao;

    @Override
    public boolean addObj(DemoTest demotest) {
        return demoTestDao.addObj(demotest);
    }

    @Override
    public PageUtil queryData(Integer page, Integer pageSize, Object... param) {
        String hql = "from DemoTest demotest";
        return demoTestDao.queryData(hql, page, pageSize, param);
    }

    public DemoTestDao getDemoTestDao() {
        return demoTestDao;
    }

    public void setDemoTestDao(DemoTestDao demoTestDao) {
        this.demoTestDao = demoTestDao;
    }

    
}
