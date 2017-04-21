/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 * jdbc数据库连接，与resources中的properties/datasources.properties相结合
 */
package com.core.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;



/**
 * 
 * @author xiaosun
 */
public abstract class JdbcConnectionInterface {
    public abstract Connection getMysqlConnection();
    public abstract boolean closeDB(ResultSet rs, PreparedStatement ps, Connection conn);
}
