/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.core.bufan.syscore.util;

/**
 *
 * @author xiaosun
 */
public class RemoveArrayNullUtil {

    /**
     * 去除数组中的空值
     *
     * @param args
     * @return
     */
    public static Object[] removeNull(Object[] args) {
        /**
         * 计算Object 数组中 不为空的个数
         */
        if (args != null) {
            int i = 0;
            int j = 0;
            while (i < args.length) {
                if (args[i] != null) {
                    j++;
                }
                i++;
            }
            Object[] temp = new Object[j];
            int init = 0;
            int target = 0;
            while (init < args.length) {
                if (args[init] != null) {
                    temp[target] = args[init];
                    target++;
                }
                init++;
            }
            return temp;
        } else {
            return args;
        }

    }
}
