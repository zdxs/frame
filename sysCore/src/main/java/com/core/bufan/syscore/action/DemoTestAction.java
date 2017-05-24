/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.core.bufan.syscore.action;

import com.alibaba.fastjson.JSONObject;
import com.core.bufan.syscore.bean.DemoTest;
import com.core.bufan.syscore.common.constant.SysConstant;
import com.core.bufan.syscore.services.DemoTestService;
import com.core.bufan.syscore.util.AjaxSupportAction;
import com.core.bufan.syscore.util.PageUtil;
import java.util.Date;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author xiaosun
 */
public class DemoTestAction extends AjaxSupportAction {

    protected Logger logger = LoggerFactory.getLogger(this.getClass());
    private DemoTestService demoTestService;

    /**
     *
     * @return
     */
    public String demotestAction() {
        System.out.println("com.core.bufan.syscore.action.DemoTestAction.demotest()");
        return SUCCESS;
    }

    /**
     * 新增测试
     *
     * @return
     */
    public String demoAdd() {
        JSONObject result = new JSONObject();
        boolean success = false;
        try {
            DemoTest demotest = new DemoTest();
            demotest.setName(new Date().toString());
            success = demoTestService.addObj(demotest);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            e.printStackTrace();
        }
        generateAndSend(result, "成功", success);
        return null;
    }

    public String demoQueryPageUtil() {
        JSONObject result = new JSONObject();
        boolean success = false;
        try {
            success = true;
            PageUtil pu = demoTestService.queryData(1, 10, (Object[]) null);
            result.put(SysConstant.KEY_DATA, pu);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        generateAndSend(result, "成功", success);
        return null;
    }

    public DemoTestService getDemoTestService() {
        return demoTestService;
    }

    public void setDemoTestService(DemoTestService demoTestService) {
        this.demoTestService = demoTestService;
    }

}
