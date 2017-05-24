/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bufan.ssmredis.common.BaseDao.imp;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bufan.ssmredis.bean.DemoTest;
import com.bufan.ssmredis.common.BaseDao.AgentBean;
import com.bufan.ssmredis.common.BaseDao.BaseField;
import com.bufan.ssmredis.common.BaseDao.Parm;
import com.bufan.ssmredis.common.BaseDao.RedisDao;
import com.bufan.ssmredis.util.Constants;
import com.bufan.ssmredis.util.PageUtil;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import org.apache.log4j.Logger;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.util.CollectionUtils;

/**
 * redis模板
 *
 * @author xiaosun
 * @param <T>
 */
public class RedisDaoImp<T extends AgentBean> implements RedisDao<T> {

    private Class<T> clazz;//类
    private StringRedisTemplate stringRedisTemplate;//redis操作模板  
    private RedisTemplate<String, Object> redisTemplate;
    //集中键值存储方式的容器
    private ValueOperations<String, Object> opsValue;
    private ListOperations<String, Object> listOps;
    private SetOperations<String, Object> setOps;
    private ZSetOperations<String, Object> zSetOps;
    private HashOperations<String, Object, Object> hashOps;

    private Lock lock = new ReentrantLock();//基于底层IO阻塞考虑  
    // 日志信息
    protected static Logger logger = Logger.getLogger(RedisDaoImp.class);

    //该实体类的方法
    private List<Parm> redisParm;

    public Class getClazz() {
        return clazz;
    }

    public void setClazz(Class clazz) {
        this.clazz = clazz;
    }

    public StringRedisTemplate getStringRedisTemplate() {
        return stringRedisTemplate;
    }

    public void setStringRedisTemplate(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }

    public RedisTemplate<String, Object> getRedisTemplate() {
        return redisTemplate;
    }

    public void setRedisTemplate(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public RedisDaoImp() {
        super();
        // 得到泛型父类
        Type genType = this.getClass().getGenericSuperclass();

        // 如果没有实现ParameterizedType接口，即不支持泛型，直接返回Object.class
        if (!(genType instanceof ParameterizedType)) {
            this.clazz = null;
        } else {
            ParameterizedType type = (ParameterizedType) genType;
            this.clazz = (Class) type.getActualTypeArguments()[0];

            //赋值redisParm
            this.redisParm = BaseField.getClassParm(this.clazz);
        }
        //Type是 Java 编程语言中所有类型的公共高级接口。它们包括原始类型、参数化类型、数组类型、类型变量和基本类型。  
//        Type types = this.getClass().getGenericSuperclass();
//        ParameterizedType type = (ParameterizedType) this.getClass()
//                .getGenericSuperclass();
//        if (type instanceof ParameterizedType) {
//            this.clazz = (Class) type.getActualTypeArguments()[0];
//        } else {
//            this.clazz = null;
//        }
    }

    /**
     * 将对象转换为json字符串
     * <p>
     * 记得捕获异常
     *
     * @param object
     * @return
     */
    private String ObjectToString(Object object) throws InterruptedException {
        if (null == object) {
            return null;
        }
        String result = "";
        try {
            result = JSON.toJSONString(object, Constants.features);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return result;
    }

    /**
     * str转objcet
     * <p>
     * 记得捕获异常
     *
     * @param objstr
     * @return
     */
    private Object StringToObject(String objstr) throws InterruptedException {
        if (null == objstr) {
            return null;
        }
        Object obj = null;
        try {
            obj = JSON.parseObject(objstr, clazz);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return obj;
    }

    /**
     * 新增
     *
     * @param key 键值--id
     * @param data 对象存储数据
     * @return
     */
    @Override
    public boolean add(final String key, T data) {
        boolean flog = false;
        try {
            ValueOperations<String, Object> opsValue = null;
            if (null != redisTemplate) {
                opsValue = redisTemplate.opsForValue();
                if (null != opsValue) {
                    String keys = clazz.getSimpleName().toLowerCase() + Constants.KEY_REDIS_SPLIT + key;
                    //验证是否存在
                    if (!this.exists(keys)) {
                        //对象存储规则--类名称的小写+:+key
                        opsValue.set(keys, this.ObjectToString(data));
                        flog = true;
                    }
                }
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return flog;
    }

    /**
     * 新增实体对象不需要传入主键
     *
     * @param obj
     * @return
     */
    @Override
    public boolean addObj(T obj) {
        boolean flog = false;
        try {
            //1创建主键id
            String beanPk = this.incr(obj.getClass().getName().toLowerCase());
            if (null != beanPk) {
                //2获取主键key
                String pkName = BaseField.getFieldValue(this.redisParm, Constants.KEY_BEAN_pkField).toString();
                //3为对象设置值
                flog = BaseField.setFileValue(obj, Constants.KEY_BEAN_SET + pkName, beanPk);
                if (flog) {
                    //5得到主键id
                    String pkId = BaseField.getFieldValue(this.redisParm, pkName).toString();
                    //6保存进入数据库
                    flog = this.add(pkId, obj);
                }
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return flog;
    }

    /**
     * 新增一个list集合数据 key=实体类+KEY_REDIS_SPLIT+KEY_REDIS_LIST 值=存储实体id
     * <p>
     * 该方法需要一个对应值 当实体删除的时候可以通过该值删除集合中的数据 以实现更新
     *
     * @param key 存储集合的key
     * @return true|false
     */
    @Override
    public boolean addRedisBeanList(String key) {
        boolean flog = false;
        try {
            ListOperations<String, Object> listOps = null;
            if (null != redisTemplate) {
                listOps = redisTemplate.opsForList();
                if (null != listOps) {
                    //对象存储规则--类名称的小写+:+Constants.KEY_REDIS_LIST
                    String keys = clazz.getSimpleName().toLowerCase()
                            + Constants.KEY_REDIS_SPLIT
                            + Constants.KEY_REDIS_LIST;
                    Long addlist = listOps.rightPush(keys, key);

                    //存储集合位置-- 键值对形式 键的规则 类名称的小写+:+Constants.KEY_REDIS_LIST+:+key
                    // TODO 执行此处的目的为了在删除的时候可以到其位置
                    String key_postion = keys + Constants.KEY_REDIS_SPLIT + key;
                    opsValue = getOpsValue();
                    opsValue.set(key_postion, addlist);
                    flog = true;
                }
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return flog;
    }

    /**
     * 查询长度
     *
     * @param key 键
     * @param type 类型 lsit,hash,set,zset
     * @return
     */
    @Override
    public String queryRedisSize(String key, String type) {
        String result = "0";
        try {
            if (null != key && null != type && null != redisTemplate) {
                switch (type) {
                    case Constants.KEY_REDIS_LIST:
                        ListOperations<String, Object> listOps = null;
                        listOps = redisTemplate.opsForList();
                        result = listOps.size(key).toString();
                        break;
                    case Constants.KEY_REDIS_HASH:
                        HashOperations<String, Object, Object> hashOps = null;
                        hashOps = redisTemplate.opsForHash();
                        result = hashOps.size(key).toString();
                        break;
                    case Constants.KEY_REDIS_SET:
                        SetOperations<String, Object> setOps = null;
                        setOps = redisTemplate.opsForSet();
                        result = setOps.size(key).toString();
                        break;
                    case Constants.KEY_REDIS_ZSET:
                        ZSetOperations<String, Object> zsetOps = null;
                        zsetOps = redisTemplate.opsForZSet();
                        result = zsetOps.size(key).toString();
                        break;
                    default:
                        result = "" + redisTemplate.keys(key + "*").size();
                        break;
                }
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return result;
    }

    /**
     * 分页查询
     *
     * @param start 开始页
     * @param end 结束页
     * @return
     */
    @Override
    public PageUtil queryPageRedisList(String start, String end) {
        if (null == start || null == end) {
            return null;
        }
        Long starts = Long.parseLong(start);
        Long ends = Long.parseLong(end);
        PageUtil page = new PageUtil();
        try {
            if (null != redisTemplate) {
                String key = clazz.getSimpleName().toLowerCase()
                        + Constants.KEY_REDIS_SPLIT
                        + Constants.KEY_REDIS_LIST;
                //总量
                page.setTotalCount(Integer.parseInt(this.queryRedisSize(key, Constants.KEY_REDIS_LIST)));
                ListOperations<String, Object> listOps = null;
                listOps = redisTemplate.opsForList();
                List<Object> listobj = listOps.range(key, starts, ends);
                //将结果转对象
                List<T> list = new ArrayList<>();
                for (Object obj : listobj) {
                    String key_bean = clazz.getSimpleName().toLowerCase()
                            + Constants.KEY_REDIS_SPLIT + obj.toString();
                    list.add(this.queryByKey(key_bean));
                }
                page.setList(list);
                page.setPageNum(Integer.parseInt(start));
                page.setPageSize(Integer.parseInt(end));
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return page;
    }

    /**
     * 存储一个集合list
     *
     * @param key 集合key
     * @param obj 存储对象
     * @return
     */
    @Override
    public boolean addRedisList(String key, Object obj) {
        boolean flog = false;
        try {
            ListOperations<String, Object> listOps = null;
            if (null != redisTemplate) {
                listOps = redisTemplate.opsForList();
                if (null != listOps) {
                    //对象存储规则--类名称的小写+:+key
                    listOps.rightPush(key, obj);
                    flog = true;
                }
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return flog;
    }

    /**
     * 批量删除
     * <br>
     * （该操作会执行模糊查询，请尽量不要使用，以免影响性能或误删）
     *
     * @param pattern
     * @return
     */
    @Override
    public int delBatch(final String... pattern) {
        int i = 0;
        try {
            for (String kp : pattern) {
                redisTemplate.delete(redisTemplate.keys(kp + "*"));
                i++;
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return i;
    }

    /**
     * 批量精确删除
     * <br>
     *
     *
     * @param pattern
     * @return
     */
    @Override
    public int delBatchAccurate(final String... pattern) {
        int i = 0;
//        redisTemplate.delete(Arrays.asList(pattern));  
        try {
            for (String kp : pattern) {
                redisTemplate.delete(kp);
                i++;
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return i;
    }

    /**
     * 根据键获取数据 object类型
     *
     * @param key 此方法传入的键已经拼凑完整
     * @return
     */
    @Override
    public Object getObj(final String key) {
        if (null == key) {
            return null;
        }
        Object object = null;
        try {
            ValueOperations<String, Object> opsValue = null;
            if (null != redisTemplate) {
                opsValue = redisTemplate.opsForValue();
                if (null != opsValue) {
                    object = (Object) opsValue.get(key);
                }
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return object;
    }

    /**
     * 删除缓存<br>
     * 根据key精确匹配删除
     *
     * @param key
     * @return
     */
    public int del(final String... key) {
        int i = 0;
        try {
            if (null != key && key.length > 0) {
                if (key.length == 1) {
                    redisTemplate.delete(key[0]);
                } else {
                    redisTemplate.delete(CollectionUtils.arrayToList(key));
                }
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return i;
    }

    /**
     * 根据key得到对象
     *
     * @param key
     * @return
     */
    @Override
    public T queryByKey(final String key) {
        if (null == key) {
            return null;
        }
        try {
            String objstr = (String) redisTemplate.boundValueOps(key).get();
            Object obj = this.StringToObject(objstr);
            if (null != obj) {
                return (T) obj;
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }

    //reidis自身命令
    /**
     * Redis INCR命令用于将键的整数值递增1。如果键不存在， 则在执行操作之前将其设置为0。
     * 如果键包含错误类型的值或包含无法表示为整数的字符串， 则会返回错误。此操作限于64位有符号整数。
     *
     * @param key
     * @return
     */
    @Override
    public String incr(final String key) {
        String result = null;
        try {
            Long results = stringRedisTemplate.getConnectionFactory().getConnection().incr(key.getBytes());
            result = Long.toString(results);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return result;
    }

    /**
     * 判断键是否存在
     * <p>
     * 如果键存在，返回 true 如果键不存在，返回 false
     *
     * @param key
     * @return true|false
     */
    @Override
    public boolean exits(final String key) {
        boolean flog = false;
        try {
            flog = stringRedisTemplate.getConnectionFactory().getConnection().exists(key.getBytes());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return flog;
    }

    /**
     * 判断是否存在
     *
     * @param key
     * @return
     */
    @Override
    public boolean exists(final String key) {
        boolean flog = false;
        try {
            flog = redisTemplate.hasKey(key);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return flog;
    }

    /**
     * 默认每次自增1
     *
     * @param key redis存储的key
     * @return
     */
    @Override
    public String increment(String key) {
        String result = null;
        try {
            Long results = redisTemplate.opsForValue().increment(key, 1);
            result = Long.toString(results);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return result;
    }

    /**
     * 默认每次自增kb
     *
     * @param key redis存储的key
     * @param kb 自增数量
     * @return
     */
    @Override
    public String increment(String key, Long kb) {
        String result = null;
        try {
            Long results = redisTemplate.opsForValue().increment(key, kb);
            result = Long.toString(results);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return result;
    }

    /**
     * 递减操作
     *
     * @param key
     * @param kb
     * @return
     */
    @Override
    public String decr(String key, Long kb) {
        String result = null;
        try {
            Long results = redisTemplate.opsForValue().increment(key, -kb);
            result = Long.toString(results);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return result;
    }

    /**
     * 修改
     *
     * @param key 键-实体类:id
     * @param obj 对象
     * @return
     */
    @Override
    public boolean update(String key, T obj) {
        boolean flog = false;
        try {
            ValueOperations<String, Object> opsValue = null;
            if (null != redisTemplate) {
                opsValue = redisTemplate.opsForValue();
                if (null != opsValue) {
                    //对象存储规则--类名称的小写+:+key
                    opsValue.set(key, this.ObjectToString(obj));
                    flog = true;
                }
            }
        } catch (InterruptedException e) {
            logger.error(e.getMessage(), e);
        }
        return flog;
    }

    /**
     * 删除list集合中的实体值
     *
     * @param key 实体id
     * @return
     */
    @Override
    public boolean delListByKey(String key) {
        boolean flog = false;
        try {
            //对象存储规则--类名称的小写+:+Constants.KEY_REDIS_LIST
            String keys = clazz.getSimpleName().toLowerCase()
                    + Constants.KEY_REDIS_SPLIT
                    + Constants.KEY_REDIS_LIST;
            //存储集合位置-- 键值对形式 键的规则 类名称的小写+:+Constants.KEY_REDIS_LIST+:+key
            String key_postion = keys + Constants.KEY_REDIS_SPLIT + key;
            //得到该数据在集合中的位置
            opsValue = getOpsValue();
            //得到位置
            Object object = (Object) opsValue.get(key_postion);
            //删除
            getListOps().remove(keys, 0, object);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return flog;
    }

    public ValueOperations<String, Object> getOpsValue() {
        if (null != redisTemplate) {
            opsValue = redisTemplate.opsForValue();
            if (null != opsValue) {
                return opsValue;
            }
        }
        return null;
    }

    public void setOpsValue(ValueOperations<String, Object> opsValue) {
        this.opsValue = opsValue;
    }

    public ListOperations<String, Object> getListOps() {
        if (null != redisTemplate) {
            listOps = redisTemplate.opsForList();
            if (null != listOps) {
                return listOps;
            }
        }
        return null;
    }

    public void setListOps(ListOperations<String, Object> listOps) {
        this.listOps = listOps;
    }

    public SetOperations<String, Object> getSetOps() {
        if (null != redisTemplate) {
            setOps = redisTemplate.opsForSet();
            if (null != setOps) {
                return setOps;
            }
        }
        return null;
    }

    public void setSetOps(SetOperations<String, Object> setOps) {
        this.setOps = setOps;
    }

    public ZSetOperations<String, Object> getzSetOps() {
        if (null != redisTemplate) {
            zSetOps = redisTemplate.opsForZSet();
            if (null != zSetOps) {
                return zSetOps;
            }
        }
        return null;
    }

    public void setzSetOps(ZSetOperations<String, Object> zSetOps) {
        this.zSetOps = zSetOps;
    }

    public HashOperations<String, Object, Object> getHashOps() {
        if (null != redisTemplate) {
            hashOps = redisTemplate.opsForHash();
            if (null != hashOps) {
                return hashOps;
            }
        }
        return null;
    }

    public void setHashOps(HashOperations<String, Object, Object> hashOps) {
        this.hashOps = hashOps;
    }

    public List<Parm> getRedisParm() {
        return redisParm;
    }

    public void setRedisParm(List<Parm> redisParm) {
        this.redisParm = redisParm;
    }

    public static void main(String[] args) {
        DemoTest demotest = new DemoTest();
        demotest.setName("s中文ss");
        demotest.setId("a'\'a");
        String results = JSON.toJSONString(demotest, Constants.features);
        System.out.println(results);
        String demo = "{\"name\":\"sas dasdf\\u4E2D\\u6587ss\",\"password\":\"\",\"remark\":\"\"}";
        JSONObject resultobj = JSON.parseObject(demo);//转换为JSONObject  
        System.out.println(resultobj);
        for (Map.Entry<String, Object> entry : resultobj.entrySet()) {
            String key = entry.getKey();
            String value = (String) entry.getValue();
            System.out.println("old--------" + key + "------------" + value);
            value = value.replaceAll(" ", "");
            entry.setValue(value);
            System.out.println("new--------" + key + "------------" + value);
        }
        for (Map.Entry<String, Object> entry : resultobj.entrySet()) {
            String key = entry.getKey();
            String value = (String) entry.getValue();
            value = value.replaceAll(" ", "");
            entry.setValue(value);
            System.out.println("newobj--------" + key + "------------" + value);
        }
        //json obj转str
        String strnewobj = JSONObject.toJSONString(resultobj);
        System.out.println("去空格之后--------------------" + strnewobj);
        System.out.println("class====================" + DemoTest.class);
        DemoTest demotest2 = JSON.parseObject(demo, DemoTest.class);
        System.out.println(demotest2);
        System.out.println(demotest2.getClass().getName().toLowerCase());
        System.out.println(demotest2.getClass().getSimpleName().toLowerCase());

        Random random = new Random();
        int tmp = random.nextInt(9000) + 1000;
        System.out.println(tmp);
    }

}
