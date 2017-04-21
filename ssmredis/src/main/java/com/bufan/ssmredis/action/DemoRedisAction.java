/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bufan.ssmredis.action;

import com.alibaba.fastjson.JSONObject;
import com.bufan.ssmredis.bean.DemoTest;
import com.bufan.ssmredis.common.AjaxSupportAction;
import com.bufan.ssmredis.service.DemoTestService;
import com.bufan.ssmredis.util.Constants;
import com.bufan.ssmredis.util.PageUtil;

/**
 *
 * @author xiaosun
 */
public class DemoRedisAction extends AjaxSupportAction {

    private DemoTestService demotestservice;

    /**
     * http://localhost/ssmredis/demotest/add
     *
     * @return
     */
    public String add() {
        JSONObject result = new JSONObject();
        boolean success = false;
        String msg = "测试1";
        try {
            DemoTest demoteset = new DemoTest();
            //得到key
            String key = demotestservice.getPrimaryKey(demoteset);
            demoteset.setId(key);
            demoteset.setName("用户名" + key);
            success = demotestservice.add(key, demoteset);
            generateAndSend(result, msg, success);
            success = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * http://localhost:9090/ssmredis/demotest/query?id=3
     *
     * @return
     */
    public String query() {
        JSONObject result = new JSONObject();
        boolean success = false;
        String msg = "测试2";
        try {
            String key = request.getParameter("id");
            Object demoteset = demotestservice.getObj("user2");
            DemoTest demotest2 = demotestservice.queryByKey(DemoTest.class.getSimpleName().toLowerCase()
                    + Constants.KEY_REDIS_SPLIT + key);
            result.put("data", demoteset);
            result.put("data2", demotest2);
        } catch (Exception e) {
            e.printStackTrace();
        }
        generateAndSend(result, msg, success);
        return null;
    }

    /**
     * http://localhost:9090/ssmredis/demotest/update?id=3
     *
     * @return
     */
    public String update() {
        JSONObject result = new JSONObject();
        boolean success = false;
        String msg = "修改测试2";
        try {
            String key = request.getParameter("id");
            DemoTest demoteset = new DemoTest();
            demoteset.setId(key);
            demoteset.setName("修改----");
            demoteset.setPassword("修改密码");
            demoteset.setRemark("修改备注");
            boolean flog = demotestservice.update(DemoTest.class.getSimpleName().toLowerCase()
                    + Constants.KEY_REDIS_SPLIT + key, demoteset);
            result.put("data2", flog);
        } catch (Exception e) {
            e.printStackTrace();
        }
        generateAndSend(result, msg, success);
        return null;
    }

    /**
     * 集合
     * <p>
     * http://localhost:9090/ssmredis/demotest/addlist
     *
     * @return
     */
    public String addlist() {
        JSONObject result = new JSONObject();
        boolean success = false;
        String msg = "修改测试2";
        try {
            for (int i = 0; i < 5; i++) {
                DemoTest demoteset = new DemoTest();
                //得到key
                String key = demotestservice.getPrimaryKey(demoteset);
                demoteset.setId(key);
                demoteset.setName("用户名" + key);
                success = demotestservice.add(key, demoteset);
                //集合
                boolean addlist = demotestservice.addRedisBeanList(key);
                result.put("add" + i, success + "---" + addlist);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        generateAndSend(result, msg, success);
        return null;
    }

    /**
     *
     * http://localhost:9090/ssmredis/demotest/queryPage
     *
     * @return
     */
    public String queryPage() {
        JSONObject result = new JSONObject();
        boolean success = false;
        String msg = "查询";
        try {
            PageUtil pageutil = new PageUtil();
            pageutil = demotestservice.queryPageRedisList("0", "20");
            result.put("data", pageutil);
        } catch (Exception e) {
            e.printStackTrace();
        }
        generateAndSend(result, msg, success);
        return null;
    }

    public DemoTestService getDemotestservice() {
        return demotestservice;
    }

    public void setDemotestservice(DemoTestService demotestservice) {
        this.demotestservice = demotestservice;
    }

}
