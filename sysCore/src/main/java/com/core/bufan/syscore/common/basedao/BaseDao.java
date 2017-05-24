/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.core.bufan.syscore.common.basedao;

import com.core.bufan.syscore.util.PageUtil;
import java.util.List;

/**
 * 底层数据CURID核心
 *
 * @author xiaosun
 */
public interface BaseDao<T> {

    // 新增数据
    public boolean addObj(final T obj);

    //修改或者保存
    public boolean updateObj(final T obj);

    //删除
    public boolean deleteObj(final Integer id);

    //获取
    public T getObj(final Integer id);

    //分页查询
    public PageUtil queryData(final String hql, final Integer page,
            final Integer pageSize, final Object... param);

    //所有数据
    public Integer queryCount(final String hql, final Object... param);

    //查询所有数据
    public List<T> queryList(final String hql, final Object... param);

    ///分页查询sql语句 下面这三个方法会用到vo实体类
    public PageUtil queryDataSql(final String sql, final Integer page,
            final Integer pageSize);

    //所有数据
    public Integer queryCountSql(final String sql);

    //查询所有数据
    public List<T> queryListSql(final String sql);
}
