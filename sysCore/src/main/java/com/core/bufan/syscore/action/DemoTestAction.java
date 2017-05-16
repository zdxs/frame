/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.core.bufan.syscore.action;

import com.core.bufan.syscore.util.AjaxSupportAction;

/**
 *
 * @author xiaosun
 */
public class DemoTestAction extends AjaxSupportAction {

    /**
     *
     * @return
     */
    public String demotest() {
        System.out.println("com.core.bufan.syscore.action.DemoTestAction.demotest()");
        return SUCCESS;
    }
}
