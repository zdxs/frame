package com.core.common.sql;

/**
 * 实体类字段约束注解 标有此注解的字段对应数据库中的字段名强制约束为该注解中的name值
 */
public @interface FieldName {

    String name() default PUBVALUE.FIELD_NAME_DEFAUL_VALUE;
}
