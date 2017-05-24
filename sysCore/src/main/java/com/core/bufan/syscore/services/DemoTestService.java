/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.core.bufan.syscore.services;

import com.core.bufan.syscore.bean.DemoTest;
import com.core.bufan.syscore.util.PageUtil;

/**
 *
 * @author xiaosun
 */
public interface DemoTestService {

    public boolean addObj(DemoTest demotest);

    //分页查询
    public PageUtil queryData(final Integer page,
            final Integer pageSize, final Object... param);
}
