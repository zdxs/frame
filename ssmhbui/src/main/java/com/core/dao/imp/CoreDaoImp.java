/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.core.dao.imp;

import com.core.bean.CoreModel;
import com.core.common.sql.FieldName;
import com.core.common.sql.GenericsUtils;
import com.core.common.sql.Method;
import com.core.common.sql.Pram;
import com.core.common.sql.SqlStringZf;
import com.core.common.sql.SqlUtil;
import com.core.common.sql.SqlZf;
import com.core.common.sql.WherePrams;
import com.core.dao.CoreDao;
import com.core.utils.CheckUtil;
import com.core.utils.Pagination;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 持久层核心操作
 *
 * @author xiaosun
 * @param <T>
 * @param <PK>
 */
public class CoreDaoImp<T extends CoreModel, PK extends Serializable> implements CoreDao<T, PK> {

    //全局变量
    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    private SqlSessionTemplate sqlSessionTemplate;

    private Class<T> entityClass;

    private String pkName;                  //主键字段-数据库

    private String idName;                  //对应id名称

    private String tableName;               //数据库表名

    private List<Pram> sqlParms;

    private List<Pram> selectSqlParms;      //字段集合

    private SqlUtil<T> sqlUtil;

    /**
     * 无参构造函数
     */
    public CoreDaoImp() {
        super();

        this.sqlUtil = new SqlUtil<>();

        this.entityClass = (Class<T>) GenericsUtils.getSuperClassGenricType(this.getClass());

        this.sqlParms = this.sqlUtil.getPramList(this.entityClass);

        this.selectSqlParms = this.sqlUtil.getPramListOfSelect(this.entityClass);

        this.tableName = this.sqlUtil.getTableName(this.entityClass);

        this.pkName = this.sqlUtil.getPkName(this.entityClass);//主键字段(数据库)

        this.idName = this.sqlUtil.getIdName(this.entityClass);//实体主键

    }

    /**
     * 根据实体新增
     *
     * @param po 实体
     * @return 影响的条数
     */
    @Override
    public int addLocal(T po) {
        //影响的行数
        int result = 0;
        String sql = "insert into " + tableName + " (";
        String prams = "";
        String values = "";

        try {
            //存入参数之前必须设置下一个的 设置主键id
            boolean flog = SqlUtil.setFileValue(po, idName, nextId(nextId()));
            if (flog == true) {
                List<Pram> pramList = SqlUtil.getPramListofStatic(po);

                int index = 0;
                for (int i = 0; i < pramList.size(); i++) {
                    if (pramList.get(i).getValue() == null || (pramList.get(i).getValue() + "").equals("0")) {
                        continue;
                    } else {
                        if (index > 0) {
                            prams += SqlStringZf.DEFAULT_SQL_LINK;
                            values += SqlStringZf.DEFAULT_SQL_LINK;
                        }
                        prams += pramList.get(i).getFile();
                        Object value = pramList.get(i).getValue();
                        if (value instanceof byte[]) {
                            values += SqlStringZf.DEFAULT_SQL_COLUMN_LINK + new String((byte[]) value) + SqlStringZf.DEFAULT_SQL_COLUMN_LINK;
                        } else if (value instanceof String) {
                            values += SqlStringZf.DEFAULT_SQL_COLUMN_LINK + value + SqlStringZf.DEFAULT_SQL_COLUMN_LINK;
                        } else {
                            values += value;
                        }

                        index++;
                    }
                }
                sql += prams + ") value (" + values + ");";

                logger.debug(sql);

                result = sqlSessionTemplate.insert("addLocal", sql);
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(SqlStringZf.MSG.SQL_ERROR.toString());
        }
        return result;
    }

    /**
     * 新增数据
     *
     * @param po 实体
     * @return 影响条数
     */
    @Override
    public int add(T po) {
        //影响的行数
        int result = 0;
        String sql = "insert into " + tableName + " (";
        String prams = "";
        String values = "";

        try {
            //存入参数之前必须设置下一个的 设置主键id
            boolean flog = SqlUtil.setFileValue(po, idName, nextId(nextId()));
            if (flog == true) {
                //当前实体
                List<Pram> pramList = SqlUtil.getPramListofStatic(po);
                for (int i = 0; i < pramList.size(); i++) {
                    prams += pramList.get(i).getFile();
                    if (pramList.get(i).getValue() == null) {
                        values += SqlStringZf.DEFAULT_VALUES;
                    } else {
                        values += SqlStringZf.DEFAULT_SQL_COLUMN_LINK + pramList.get(i).getValue() + SqlStringZf.DEFAULT_SQL_COLUMN_LINK;
                    }

                    if (i < pramList.size() - 1) {
                        prams += SqlStringZf.DEFAULT_SQL_LINK;
                        values += SqlStringZf.DEFAULT_SQL_LINK;
                    }
                }
                sql += prams + ") value (" + values + ");";
                logger.debug(sql);

                result = sqlSessionTemplate.insert("add", sql);
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(SqlStringZf.MSG.SQL_ERROR.toString());
        }
        return result;
    }

    @Override
    public T get(PK id) {
        Map<String, Object> resultMap = null;
        // TODO Auto-generated method stub
        String sql = "select ";
        for (int i = 0; i < selectSqlParms.size(); i++) {
            sql += selectSqlParms.get(i).getFile();
            if (i < selectSqlParms.size() - 1) {
                sql += SqlStringZf.DEFAULT_SQL_LINK;
            } else {
                sql += " ";
            }
        }
        sql += " from " + tableName + " where " + pkName + "=" + id;
        try {
            resultMap = sqlSessionTemplate.selectOne("getById", sql);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return handleResult(resultMap, this.entityClass);
    }

    /**
     * 通过id获取实体对象
     *
     * @param id
     * @return
     */
    @Override
    public T getById(PK id) {
        String sql = "select ";
        try {
            for (int i = 0; i < selectSqlParms.size(); i++) {
                sql += selectSqlParms.get(i).getFile();
                if (i < selectSqlParms.size() - 1) {
                    sql += SqlStringZf.DEFAULT_SQL_LINK;
                } else {
                    sql += " ";
                }
            }
            sql += " from " + tableName + " where " + pkName + "=" + id;
            Map<String, Object> resultMap = sqlSessionTemplate.selectOne("getById", sql);

            return handleResult(resultMap, this.entityClass);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     *
     * @param id 主键id
     * @param fileName 实体字段
     * @return
     */
    @Override
    public Serializable getField(PK id, String fileName) {
        String tabField = "";
        try {
            Field f = sqlUtil.getField(this.entityClass, fileName);
            if (null == f) {
                logger.error("查询字段失败(无法找到" + this.entityClass + "中的" + fileName + "字段)");
            }
            FieldName annotation = f.getAnnotation(FieldName.class);
            if (null == annotation) {
                tabField = sqlUtil.toTableString(fileName) + " as " + fileName;
            } else {
                tabField = annotation.name() + " as " + fileName;
            }

            String sql = "select ";
            sql += tabField + " from " + tableName + " where " + pkName + SqlStringZf.DEFAULT_SQL_COLUMN_LINK + id + "';";
            Map<String, Object> resultMap = sqlSessionTemplate.selectOne("getFieldById", sql);
            return (Serializable) resultMap.get(fileName);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 条件查询得到实体
     *
     * @param where
     * @return
     */
    @Override
    public T get(WherePrams where) {
        String sql = "select ";
        for (int i = 0; i < selectSqlParms.size(); i++) {
            sql += selectSqlParms.get(i).getFile();
            if (i < selectSqlParms.size() - 1) {
                sql += SqlStringZf.DEFAULT_SQL_LINK;
            } else {
                sql += " ";
            }
        }
        sql += "from " + tableName + where.getWherePrams() + ";";

        Map<String, Object> resultMap = sqlSessionTemplate.selectOne("getByParm", sql);

        return handleResult(resultMap, this.entityClass);
    }

    @Override
    public Serializable getFile(WherePrams where, String fileName) {
        // TODO Auto-generated method stub
        String field = fileName;
        String tabField = "";
        try {
            Field f = sqlUtil.getField(this.entityClass, fileName);
            if (null == f) {
                logger.error("查询字段失败(无法找到" + this.entityClass + "中的" + fileName + "字段)");
            }
            FieldName annotation = f.getAnnotation(FieldName.class);
            if (null == annotation) {
                tabField = sqlUtil.toTableString(fileName) + " as " + fileName;
            } else {
                tabField = annotation.name() + " as " + fileName;
            }

            String sql = "select ";
            sql += tabField + " from " + tableName + where.getWherePrams() + ";";
            Map<String, Object> resultMap = sqlSessionTemplate.selectOne("getFieldByParm", sql);
            return (Serializable) resultMap.get(fileName);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 分页模型查询所有分页
     *
     * @param where
     * @return
     */
    @Override
    public Pagination<T> queryPaginationList(WherePrams where, int pageIndex, int pageSize) {
        Pagination pagination = null;
        try {
            //sql拼接
            String sql = "select ";
            for (int i = 0; i < selectSqlParms.size(); i++) {
                sql += selectSqlParms.get(i).getFile();
                if (i < selectSqlParms.size() - 1) {
                    sql += SqlStringZf.DEFAULT_SQL_LINK;
                } else {
                    sql += " ";
                }
            }
            sql += "from " + tableName + where.getWherePrams() + ";";
            List<Map<String, Object>> selectList = sqlSessionTemplate.selectList("selectList", sql);
            List<T> list = new ArrayList<>();
            for (Map<String, Object> map : selectList) {
                T t = handleResult(map, this.entityClass);
                list.add(t);
            }
            //总条数
            long count = this.countNoPage(where);
            //存放数据
            pagination = new Pagination(pageIndex, pageSize, count);
            pagination.setItems(list);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return pagination;
    }

    /**
     * 根据字段查询所有数据，用于检测唯一性
     *
     * @param field 数据库字段名
     * @param values 值
     * @return
     */
    public List<T> queryAllByField(String field, String values) {
        //处理字符串
        if (CheckUtil.isEmpty(values) || CheckUtil.isEmpty(field)) {
            return null;
        }
        try {
            String sql = "select ";
            for (int i = 0; i < selectSqlParms.size(); i++) {
                sql += selectSqlParms.get(i).getFile();
                if (i < selectSqlParms.size() - 1) {
                    sql += SqlStringZf.DEFAULT_SQL_LINK;
                } else {
                    sql += " ";
                }
            }
            sql += "from " + tableName + " where " + field + " ='" + values + "';";

            List<Map<String, Object>> selectList = sqlSessionTemplate.selectList("selectList", sql);

            List<T> list = new ArrayList<>();
            for (Map<String, Object> map : selectList) {
                T t = handleResult(map, this.entityClass);
                list.add(t);
            }
            return list;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 查询所有数据：如select * from user where user.id in(1,2,3);
     *
     * @param field 数据库字段名
     * @param strin 1,2,3
     * @return
     */
    public List<T> listIdIn(String field, String strin) {
        //处理字符串
        if (CheckUtil.isEmpty(strin)) {
            return null;
        }
        try {
            strin = SqlStringZf.DEFAULT_SQL_COLUMN_LINK + strin.replaceAll(SqlStringZf.DEFAULT_SQL_LINK, "','") + SqlStringZf.DEFAULT_SQL_COLUMN_LINK;
            String sql = "select ";
            for (int i = 0; i < selectSqlParms.size(); i++) {
                sql += selectSqlParms.get(i).getFile();
                if (i < selectSqlParms.size() - 1) {
                    sql += SqlStringZf.DEFAULT_SQL_LINK;
                } else {
                    sql += " ";
                }
            }
            if (tableName != null && "zhg_word".equalsIgnoreCase(tableName)) {
                tableName = tableName + "_ar_zh";
            }
            sql += "from " + tableName + " where " + field + " in(" + strin + ");";

            List<Map<String, Object>> selectList = sqlSessionTemplate.selectList("selectList", sql);

            List<T> list = new ArrayList<>();
            for (Map<String, Object> map : selectList) {
                T t = handleResult(map, this.entityClass);
                list.add(t);
            }
            return list;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 通过条件获取集合
     *
     * @param where
     * @return
     */
    @Override
    public List<T> list(WherePrams where) {
        String sql = "select ";
        try {
            for (int i = 0; i < selectSqlParms.size(); i++) {
                sql += selectSqlParms.get(i).getFile();
                if (i < selectSqlParms.size() - 1) {
                    sql += SqlStringZf.DEFAULT_SQL_LINK;
                } else {
                    sql += " ";
                }
            }
            if (where != null) {
                sql += "from " + tableName + where.getWherePrams() + ";";
            } else {
                sql += "from " + tableName;
            }

            List<Map<String, Object>> selectList = sqlSessionTemplate.selectList("selectList", sql);

            List<T> list = new ArrayList<>();
            for (Map<String, Object> map : selectList) {
                T t = handleResult(map, this.entityClass);
                list.add(t);
            }
            return list;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 通过执行sql语句，查出所有集合
     *
     * @param sql
     * @return
     */
    @Override
    public List<T> listSql(String sql) {

        List<Map<String, Object>> selectList = sqlSessionTemplate.selectList("selectList", sql);

        List<T> list = new ArrayList<>();
        for (Map<String, Object> map : selectList) {
            T t = handleResult(map, this.entityClass);
            list.add(t);
        }
        return list;
    }

    /**
     * 序列化字段
     *
     * @param where 条件
     * @param fileName 字段
     * @return
     */
    @Override
    public Serializable[] listFile(WherePrams where, String fileName) {
        String tabField = "";
        try {
            Field f = sqlUtil.getField(this.entityClass, fileName);
            if (null == f) {
                logger.error("查询指定字段集失败(无法序列化" + this.entityClass + "中的" + fileName + "字段)");
            }
            FieldName annotation = f.getAnnotation(FieldName.class);
            if (null == annotation) {
                tabField = sqlUtil.toTableString(fileName) + " as " + fileName;
            } else {
                tabField = annotation.name() + " as " + fileName;
            }

            String sql = "select ";
            sql += tabField + " from " + tableName + where.getWherePrams() + ";";
            List<Map<String, Object>> resultMap = sqlSessionTemplate.selectList("selectListField", sql);

            Serializable[] fields = new Serializable[resultMap.size()];

            for (int i = 0; i < resultMap.size(); i++) {
                if (null != resultMap.get(i)) {
                    fields[i] = (Serializable) resultMap.get(i).get(fileName);
                }
            }

            return fields;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Map<String, Serializable>> listFiles(WherePrams where, String[] files) {
        String tabField = "";
        int index = 1;
        try {
            for (String field : files) {
                Field f = sqlUtil.getField(this.entityClass, field);
                if (null == f) {
                    logger.error("查询指定字段集失败(无法序列化" + this.entityClass + "中的" + field + "字段)");
                }
                FieldName annotation = f.getAnnotation(FieldName.class);
                if (null == annotation) {
                    tabField += sqlUtil.toTableString(field) + " as " + field;
                } else {
                    tabField += annotation.name() + " as " + field;
                }
                if (index < files.length) {
                    tabField += SqlStringZf.DEFAULT_SQL_LINK;
                }

                index++;
            }

            String sql = "select ";
            sql += tabField + " from " + tableName + where.getWherePrams() + ";";
            List<Map<String, Serializable>> resultMap = sqlSessionTemplate.selectList("selectListField", sql);

            return resultMap;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 根据实体修改
     *
     * @param po
     * @return 影响行数
     */
    @Override
    public int updateLocal(T po) {
        int result = 0;

        try {
            //获取id
            Serializable id = sqlUtil.getFileValue(po, idName);

            if (null == id) {
                return result;
            }
            String sql = "update " + tableName + " set ";
            List<Pram> prams = sqlUtil.getPramList(po);
            for (int i = 0; i < prams.size(); i++) {
                if (null != prams.get(i).getValue()) {
                    sql += prams.get(i).getFile() + "=";
                    Object value = prams.get(i).getValue();
                    if (value instanceof byte[]) {
                        sql += SqlStringZf.DEFAULT_SQL_COLUMN_LINK + new String((byte[]) value) + SqlStringZf.DEFAULT_SQL_COLUMN_LINK;
                    } else if (value instanceof String) {
                        sql += SqlStringZf.DEFAULT_SQL_COLUMN_LINK + value + SqlStringZf.DEFAULT_SQL_COLUMN_LINK;
                    } else {
                        sql += value;
                    }

//              sql += prams.get(i).getFile() + "='" + prams.get(i).getValue() + "'";
                    if (i < prams.size() - 1) {
                        sql += SqlStringZf.DEFAULT_SQL_LINK;
                    }
                }
            }
            sql += " where " + pkName + SqlStringZf.DEFAULT_SQL_COLUMN_LINK + id + "';";

            result = sqlSessionTemplate.update("updateLocal", sql);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 根据实体修改
     *
     * @param po
     * @return
     */
    @Override
    public int update(T po) {
        int result = 0;
        try {
            Serializable id = sqlUtil.getFileValue(po, idName);

            if (null == id) {
                return result;
            }
            String sql = "update " + tableName + " set ";

            List<Pram> prams = sqlUtil.getPramList(po);

            for (int i = 0; i < prams.size(); i++) {
                if (null != prams.get(i).getValue()) {
                    sql += prams.get(i).getFile() + "=";
                    Object value = prams.get(i).getValue();
                    if (value instanceof byte[]) {
                        sql += SqlStringZf.DEFAULT_SQL_COLUMN_LINK + new String((byte[]) value) + SqlStringZf.DEFAULT_SQL_COLUMN_LINK;
                    } else if (value instanceof String) {
                        sql += SqlStringZf.DEFAULT_SQL_COLUMN_LINK + value + SqlStringZf.DEFAULT_SQL_COLUMN_LINK;
                    } else {
                        sql += value;
                    }
                    if (i < prams.size() - 1) {
                        sql += SqlStringZf.DEFAULT_SQL_LINK;
                    }
                } else {
                    sql += prams.get(i).getFile() + "=null";
                    if (i < prams.size() - 1) {
                        sql += SqlStringZf.DEFAULT_SQL_LINK;
                    }
                }
            }
            sql += " where " + pkName + "='" + id + "';";
            result = sqlSessionTemplate.update("update", sql);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 根据条件修改实体
     *
     * @param po
     * @param where
     * @return
     */
    @Override
    public int updateLocal(T po, WherePrams where) {
        int result = 0;
        try {
            String sql = "update " + tableName + " set ";
            List<Pram> prams = sqlUtil.getPramList(po);
            for (int i = 0; i < prams.size(); i++) {
                if (null != prams.get(i).getValue()) {
                    sql += prams.get(i).getFile() + "=";
                    Object value = prams.get(i).getValue();
                    if (value instanceof byte[]) {
                        sql += SqlStringZf.DEFAULT_SQL_COLUMN_LINK + new String((byte[]) value) + SqlStringZf.DEFAULT_SQL_COLUMN_LINK;
                    } else if (value instanceof String) {
                        sql += SqlStringZf.DEFAULT_SQL_COLUMN_LINK + value + SqlStringZf.DEFAULT_SQL_COLUMN_LINK;
                    } else {
                        sql += value;
                    }
                    if (i < prams.size() - 1) {
                        sql += SqlStringZf.DEFAULT_SQL_LINK;
                    }
                }
            }
            sql += where.getWherePrams() + "';";

            result = sqlSessionTemplate.update("updateLocalByPram", sql);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 条件修改
     *
     * @param po
     * @param where
     * @return
     */
    @Override
    public int update(T po, WherePrams where) {
        int result = 0;
        try {
            String sql = "update " + tableName + " set ";
            Object[] o = new Object[sqlParms.size()];
            for (int i = 0; i < sqlParms.size(); i++) {
                if (null != sqlParms.get(i).getValue()) {
                    sql += sqlParms.get(i).getFile() + "=" + sqlParms.get(i).getValue();
                    if (i < sqlParms.size() - 1) {
                        sql += SqlStringZf.DEFAULT_SQL_LINK;
                    }
                } else {
                    sql += sqlParms.get(i).getFile() + "=null";
                    if (i < sqlParms.size() - 1) {
                        sql += SqlStringZf.DEFAULT_SQL_LINK;
                    }
                }
            }
            sql += where.getWherePrams() + "';";

            result = sqlSessionTemplate.update("updateByPram", sql);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 根据id 删除
     *
     * @param id
     * @return result 影响行数
     */
    @Override
    public int del(PK id) {
        int result = 0;
        try {
            String sql = "delete from " + tableName + " where " + pkName + "=" + id;
            result = sqlSessionTemplate.delete("deleteById", sql);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 条件删除
     *
     * @param where
     * @return
     */
    @Override
    public int del(WherePrams where) {
        int result = 0;
        try {
            String sql = "delete from " + tableName + where.getWherePrams();
            result = sqlSessionTemplate.delete("deleteByparm", sql);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;

    }

    /**
     * 自定义sql语句查询
     *
     * @param sql 传入sql
     * @return
     */
    @Override
    public List<Map<String, Object>> listBySql(String sql) {
        if (null == sql) {
            return null;
        }

        List<Map<String, Object>> selectList = null;
        try {
            selectList = sqlSessionTemplate.selectList("selectBySql", sql);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return selectList;
    }

    /**
     * 修改语句
     *
     * @param sql 修改的sql语句
     * @return 影响的行数
     */
    @Override
    public int excuse(String sql) {
        int result = 0;
        if (null == sql) {
            return result;
        }

        try {
            result = sqlSessionTemplate.update("excuse", sql);
        } catch (Exception e) {
            e.printStackTrace();;
        }
        return result;
    }

    /**
     * 查询所有数量没有分页参数
     *
     * @param where
     * @return
     */
    @Override
    public long countNoPage(WherePrams where) {
        long count = 0;
        if (null == where) {
            return count;
        }

        try {
            String sql = "select count(*) from ";
            sql += tableName + where.getWherePramsNoPage();
            count = sqlSessionTemplate.selectOne("selectCountByParm", sql);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return count;
    }

    /**
     * 查询数量，有时候会有分页参数代入
     *
     * @param where
     * @return
     */
    @Override
    public long count(WherePrams where) {
        long count = 0;
        if (null == where) {
            return count;
        }

        try {
            String sql = "select count(*) from ";

            sql += tableName + where.getWherePrams();

            count = sqlSessionTemplate.selectOne("selectCountByParm", sql);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return count;
    }

    /**
     * 获取整张表的数量
     *
     * @return
     */
    @Override
    public long size() {
        long count = 0;
        try {
            String sql = "select count(*) from " + tableName;
            count = sqlSessionTemplate.selectOne("selectCount", sql);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return count;
    }

    /**
     * 是否存在
     *
     * @param po 传入实体
     * @return
     */
    @Override
    public boolean isExist(T po) {
        boolean flog = false;
        try {
            WherePrams wherePrams = Method.createDefault();
            List<Pram> list = SqlUtil.getPramListofStatic(po);

            for (int i = 0; i < list.size(); i++) {
                if (i == 0) {
                    if (CheckUtil.isEmpty(list.get(i).getValue())) {
                        wherePrams = Method.where(list.get(i).getFile(), SqlZf.EQ, (Serializable) list.get(i).getValue());
                    }else{
                        wherePrams = Method.where("1", SqlZf.EQ, "1");
                    }
                } else {
                    wherePrams.and(list.get(i).getFile(), SqlZf.EQ, (Serializable) list.get(i).getValue());
                }
            }
            flog = count(wherePrams) > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return flog;
    }

    /**
     * 条件查询是否存在
     *
     * @param where
     * @return
     */
    @Override
    public boolean isExist(WherePrams where) {
        boolean flog = false;
        try {
            flog = count(where) > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return flog;
    }

    /**
     * 查询某张表的字段的值在哪个里面
     *
     * @param fileName
     * @param values
     * @return
     */
    @Override
    public List<T> in(String fileName, Serializable[] values) {
        List<T> list = new ArrayList<>();
        if (null == fileName || null == values) {
            return null;
        }
        try {
            String sql = "select ";
            for (int i = 0; i < sqlParms.size(); i++) {
                sql += selectSqlParms.get(i).getFile();
                if (i < selectSqlParms.size() - 1) {
                    sql += SqlStringZf.DEFAULT_SQL_LINK;
                } else {
                    sql += " ";
                }
            }
            sql += "from " + tableName + " where " + fileName + " in ";
            String value = "(";
            for (int i = 0; i < values.length; i++) {
                if (i < values.length - 1) {
                    value += values[i] + SqlStringZf.DEFAULT_SQL_LINK;
                } else {
                    value += values[i] + ");";
                }
            }
            sql += value;

            List<Map<String, Object>> selectList = sqlSessionTemplate.selectList("selectIn", sql);

            for (Map<String, Object> map : selectList) {
                T t = handleResult(map, this.entityClass);
                list.add(t);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    /**
     * 传入查询出的结果集进行实体转换
     *
     * @param resultMap
     * @param tClazz
     * @return
     */
    private T handleResult(Map<String, Object> resultMap, Class<T> tClazz) {
        if (null == resultMap) {
            return null;
        }
        T t = null;
        try {
            t = tClazz.newInstance();
        } catch (InstantiationException e) {
            logger.error("/********************************");
            logger.error("实例化Bean失败(" + this.entityClass + ")!"
                    + e.getMessage());
            logger.error("/********************************");
        } catch (IllegalAccessException e) {
            logger.error("/********************************");
            logger.error("实例化Bean失败(" + this.entityClass + ")!"
                    + e.getMessage());
            logger.error("/********************************");
        }
        for (Map.Entry<String, Object> entry : resultMap.entrySet()) {
            String key = entry.getKey();
            Serializable val = (Serializable) entry.getValue();
            try {
                SqlUtil.setFileValue(t, key, val);
            } catch (Exception e) {
                // TODO: handle exception
                logger.error("/********************************");
                logger.error("/实例化Bean失败(" + this.entityClass + ")不能赋值到字段(" + key + "):"
                        + e.getMessage());
                logger.error("/********************************");
            }
        }
        return t;
    }

    /**
     * 从表数据删除
     *
     * @param field
     * @param values
     * @return
     */
    @Override
    public int deleteTmp(String field, String values) {
        int deletecount = 0;
        try {
            String sql = "delete from " + tableName + " where " + field + "=" + values;
            return sqlSessionTemplate.delete("deleteTmp", sql);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return deletecount;
    }

    /**
     * long 类型id转换成为int类型
     *
     * @param id
     * @return
     */
    public Integer nextId(Long id) {
        Integer primary_key = 0;
        if (null == id) {
            return primary_key;
        }
        primary_key = Integer.parseInt(id.toString());
        return primary_key;
    }

    /**
     * 获取某表的下一个Id
     */
    @Override
    public long nextId() {
        long idval = 0;
        String sql = "SELECT auto_increment FROM information_schema.`TABLES` WHERE 1=1 and TABLE_NAME='" + tableName + SqlStringZf.DEFAULT_SQL_COLUMN_LINK + " ORDER BY auto_increment desc LIMIT 0,1;";
        try {
            Long idvalsql = sqlSessionTemplate.selectOne("fetchSeqNextval", sql);
            if (null == idvalsql) {
                logger.error("/获取id失败，" + tableName + "表异常。请检查是否存在或者配为自增主键");
                return idval;
            }
            idval = idvalsql;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return idval;
    }

    public SqlSessionTemplate getSqlSessionTemplate() {
        return sqlSessionTemplate;
    }

    public void setSqlSessionTemplate(SqlSessionTemplate sqlSessionTemplate) {
        this.sqlSessionTemplate = sqlSessionTemplate;
    }

    public Logger getLogger() {
        return logger;
    }

    public void setLogger(Logger logger) {
        this.logger = logger;
    }

    public Class<T> getEntityClass() {
        return entityClass;
    }

    public void setEntityClass(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    public String getPkName() {
        return pkName;
    }

    public void setPkName(String pkName) {
        this.pkName = pkName;
    }

    public String getIdName() {
        return idName;
    }

    public void setIdName(String idName) {
        this.idName = idName;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public List<Pram> getSqlParms() {
        return sqlParms;
    }

    public void setSqlParms(List<Pram> sqlParms) {
        this.sqlParms = sqlParms;
    }

    public List<Pram> getSelectSqlParms() {
        return selectSqlParms;
    }

    public void setSelectSqlParms(List<Pram> selectSqlParms) {
        this.selectSqlParms = selectSqlParms;
    }

    public SqlUtil<T> getSqlUtil() {
        return sqlUtil;
    }

    public void setSqlUtil(SqlUtil<T> sqlUtil) {
        this.sqlUtil = sqlUtil;
    }

}
