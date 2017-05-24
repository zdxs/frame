/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.core.bufan.syscore.dao.impl;

import com.core.bufan.syscore.bean.DemoTest;
import com.core.bufan.syscore.common.basedao.BaseDaoImpl;
import com.core.bufan.syscore.dao.DemoTestDao;
import com.core.bufan.syscore.util.PageUtil;

/**
 *
 * @author xiaosun
 */
public class DemoTestDaoImpl extends BaseDaoImpl<DemoTest> implements DemoTestDao {

    @Override
    public boolean addObj(DemoTest demotest) {
        return super.addObj(demotest);
    }

    @Override
    public PageUtil queryData(String hql, Integer page, Integer pageSize, Object... param) {
        return super.queryData(hql, page, pageSize, param);
    }

}
