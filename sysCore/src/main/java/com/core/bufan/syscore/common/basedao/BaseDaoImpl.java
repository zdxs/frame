/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.core.bufan.syscore.common.basedao;

import com.core.bufan.syscore.util.PageUtil;
import com.core.bufan.syscore.util.RemoveArrayNullUtil;
import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.orm.hibernate5.HibernateCallback;
import org.springframework.orm.hibernate5.HibernateTemplate;

/**
 * 底层CURID操作
 *
 * @author xiaosun
 * @param <T>
 */
public class BaseDaoImpl<T extends Serializable> implements BaseDao<T> {

    protected Logger logger = LoggerFactory.getLogger(this.getClass());
    private Class<T> BaseDaoImplClass;
    private HibernateTemplate hibernateTemplate;

    /**
     * 新增数据
     *
     * @param obj 实体类
     * @return
     */
    @Override
    public boolean addObj(final T obj) {
        boolean flog = false;
        try {
            hibernateTemplate.save(obj);
            flog = true;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            e.printStackTrace();
        }
        return flog;
    }

    /**
     * 修改对象数据
     *
     * @param obj
     * @return
     */
    @Override
    public boolean updateObj(final T obj) {
        boolean flog = false;
        try {
            hibernateTemplate.update(obj);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            e.printStackTrace();
        }
        return flog;
    }

    /**
     * 删除对象
     *
     * @param id
     * @return
     */
    @Override
    public boolean deleteObj(final Integer id) {
        boolean flog = false;
        try {
            hibernateTemplate.delete(hibernateTemplate.get(BaseDaoImplClass, id));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            e.printStackTrace();
        }
        return flog;
    }

    /**
     * 获取对象
     *
     * @param id
     * @return
     */
    @Override
    public T getObj(final Integer id) {
        T result = null;
        try {
            result = (T) hibernateTemplate.get(BaseDaoImplClass, id);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 普通分页查询
     *
     * @param hql
     * @param page
     * @param pageSize
     * @param param
     * @return
     */
    @Override
    public PageUtil queryData(final String hql, final Integer page, final Integer pageSize,
            final Object... param) {
        int totalCount = 0;
        totalCount = queryCount(hql, param);
        final PageUtil resultPage = new PageUtil(page, totalCount, pageSize);
        try {
            hibernateTemplate.execute(new HibernateCallback() {
                @Override
                public Object doInHibernate(Session sn) throws HibernateException {
                    Query query = sn.createQuery(hql);
                    Object[] args = RemoveArrayNullUtil.removeNull(param);
                    if (args != null) {
                        for (int i = 0; i < args.length; i++) {
                            query.setParameter(i, args[i]);
                        }
                    }
                    query.setFirstResult(resultPage.getStartRowNum() - 1);
                    query.setMaxResults(pageSize);
                    resultPage.setList(query.list());
                    return resultPage;
                }

            });
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            e.printStackTrace();
        } 
        return resultPage;
    }

    /**
     * 得到指定hql语句中的数量
     *
     * @param hql
     * @param param
     * @return
     */
    @Override
    public Integer queryCount(final String hql, final Object... param) {
        List<T> list = new ArrayList<T>();
        Integer result = 0;
        try {
            result = this.queryList(hql, param).size();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 查询指定hql 语句中的所有数据
     *
     * @param hql
     * @param param
     * @return
     */
    @Override
    public List<T> queryList(final String hql, final Object... param) {
        List<T> result = new ArrayList<T>();
        try {
            result = (List<T>) hibernateTemplate.find(hql, RemoveArrayNullUtil.removeNull(param));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            e.printStackTrace();
        }
        return result;
    }

    /**
     * sql 语句分页查询
     *
     * @param sql
     * @param page
     * @param pageSize
     * @return
     */
    @Override
    public PageUtil queryDataSql(final String sql, final Integer page, final Integer pageSize) {
        int totalCount = 0;
        totalCount = queryCountSql(sql);
        final PageUtil resultPage = new PageUtil(page, totalCount, pageSize);
        try {
            hibernateTemplate.execute(new HibernateCallback() {
                @Override
                public Object doInHibernate(Session sn) throws HibernateException {
                    // TODO Auto-generated method stub  
                    Query query = sn.createSQLQuery(sql)
                            .setResultTransformer(
                                    Transformers
                                            .aliasToBean(BaseDaoImplClass));

                    query.setFirstResult(resultPage.getStartRowNum() - 1);
                    query.setMaxResults(pageSize);
                    resultPage.setList(query.list());
                    return resultPage;
                }
            });
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            e.printStackTrace();
        }
        return resultPage;
    }

    /**
     * sql 语句查询查询 需要vo实体类
     *
     * @param sql
     * @return
     */
    @Override
    public Integer queryCountSql(final String sql) {
        Integer resultCount = 0;
        try {
            resultCount = this.queryListSql(sql) == null ? 0 : this.queryListSql(sql).size();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            e.printStackTrace();
        }
        return resultCount;
    }

    /**
     * sql 语句查询查询 需要vo实体类
     *
     * @param sql
     * @return
     */
    @Override
    public List<T> queryListSql(final String sql) {
        List<T> result = new ArrayList<T>();
        try {
            result = (List<T>) this.hibernateTemplate
                    .execute(new HibernateCallback() {
                        @Override
                        public Object doInHibernate(Session sn) throws HibernateException {
                            // TODO Auto-generated method stub  
                            Query query = sn.createSQLQuery(sql)
                                    .setResultTransformer(
                                            Transformers
                                                    .aliasToBean(BaseDaoImplClass));
                            return query.list();
                        }
                    });
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 构造方法
     */
    public BaseDaoImpl() {
        ParameterizedType type = (ParameterizedType) this.getClass()
                .getGenericSuperclass();
        if (type instanceof ParameterizedType) {
            this.BaseDaoImplClass = (Class) type.getActualTypeArguments()[0];
        } else {
            this.BaseDaoImplClass = null;
        }
    }

    public Class<T> getBaseDaoImplClass() {
        return BaseDaoImplClass;
    }

    public void setBaseDaoImplClass(Class<T> BaseDaoImplClass) {
        this.BaseDaoImplClass = BaseDaoImplClass;
    }

    public HibernateTemplate getHibernateTemplate() {
        return hibernateTemplate;
    }

    public void setHibernateTemplate(HibernateTemplate hibernateTemplate) {
        this.hibernateTemplate = hibernateTemplate;
    }

}
