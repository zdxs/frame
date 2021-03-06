package com.core.utils;

import com.core.common.KeyUtil;
import java.io.IOException;
import java.io.InputStream;
import java.util.NoSuchElementException;
import java.util.Properties;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

/**
 *
 * @Title: PropertiesLoader.java
 * @Package com.dst.util
 * @Description: Properties文件载入工具类. 可载入多个properties文件,
 * 相同的属性在最后载入的文件中的值将会覆盖之前的值，但以System的Property优先.
 * @author lq
 * @date 2016-7-14 上午11:10:08
 * @version V1.0
 *
 */
public class PropertiesLoader {

    private static Logger logger = LoggerFactory.getLogger(PropertiesLoader.class);

    private static ResourceLoader resourceLoader = new DefaultResourceLoader();

    private final Properties properties;

    public PropertiesLoader(String... resourcesPaths) {
        properties = loadProperties(resourcesPaths);
    }

    public PropertiesLoader() {
        properties = loadProperties(KeyUtil.URL.DEFAULT_PROPERTIES_PATH.toString());
    }

    public Properties getProperties() {
        return properties;
    }

    /**
     * 取出Property，但以System的Property优先,取不到返回空字符串.
     */
    private String getValue(String key) {
        try {
            String systemProperty = System.getProperty(key);
            if (null != systemProperty) {
                return systemProperty;
            }
            if (null != properties) {
                if (properties.containsKey(key)) {
                    return properties.getProperty(key);
                }
            }
        } catch (Exception e) {
            LoggerUtil.exceptionLog(logger, e, this.getClass());
        }
        return "";
    }

    /**
     * 取出String类型的Property，但以System的Property优先,如果都为Null则抛出异常.
     *
     * @param key
     * @return
     */
    public String getProperty(String key) {
        String value = getValue(key);
        if (null == value) {
            throw new NoSuchElementException();
        }
        return value;
    }

    /**
     * 取出String类型的Property，但以System的Property优先.如果都为Null则返回Default值.
     *
     * @param key
     * @param defaultValue
     * @return
     */
    public String getProperty(String key, String defaultValue) {
        String value = null;
        try {
            value = getValue(key);
        } catch (Exception e) {
            LoggerUtil.exceptionLog(logger, e, this.getClass());
        }
        return null != value ? value : defaultValue;
    }

    /**
     * 取出Integer类型的Property，但以System的Property优先.如果都为Null或内容错误则抛出异常.
     *
     * @param key
     * @return
     */
    public Integer getInteger(String key) {
        String value = getValue(key);
        if (null == value) {
            throw new NoSuchElementException();
        }
        return Integer.valueOf(value);
    }

    /**
     * 取出Integer类型的Property，但以System的Property优先.如果都为Null则返回Default值，如果内容错误则抛出异常
     *
     * @param key
     * @param defaultValue
     * @return
     */
    public Integer getInteger(String key, Integer defaultValue) {
        String value = null;
        try {
            value = getValue(key);
        } catch (Exception e) {
            LoggerUtil.exceptionLog(logger, e, this.getClass());
        }
        return null != value ? Integer.valueOf(value) : defaultValue;
    }

    /**
     * 取出Double类型的Property，但以System的Property优先.如果都为Null或内容错误则抛出异常.
     *
     * @param key
     * @return
     */
    public Double getDouble(String key) {
        String value = getValue(key);
        if (null == value) {
            throw new NoSuchElementException();
        }
        return Double.valueOf(value);
    }

    /**
     * 取出Double类型的Property，但以System的Property优先.如果都为Null则返回Default值，如果内容错误则抛出异常
     *
     * @param key
     * @param defaultValue
     * @return
     */
    public Double getDouble(String key, Integer defaultValue) {
        String value = null;
        try {
            value = getValue(key);
        } catch (Exception e) {
            LoggerUtil.exceptionLog(logger, e, this.getClass());
        }
        return null != value ? Double.valueOf(value) : defaultValue;
    }

    /**
     * 取出Boolean类型的Property，但以System的Property优先.如果都为Null抛出异常,如果内容不是true/false则返回false.
     *
     * @param key
     * @return
     */
    public Boolean getBoolean(String key) {
        String value = getValue(key);
        if (null == value) {
            throw new NoSuchElementException();
        }
        return Boolean.valueOf(value);
    }

    /**
     * 取出Boolean类型的Property，但以System的Property优先.如果都为Null则返回Default值,如果内容不为true/false则返回false.
     *
     * @param key
     * @param defaultValue
     * @return
     */
    public Boolean getBoolean(String key, boolean defaultValue) {
        String value = null;
        try {
            value = getValue(key);
        } catch (Exception e) {
            LoggerUtil.exceptionLog(logger, e, this.getClass());
        }
        return null != value ? Boolean.valueOf(value) : defaultValue;
    }

    /**
     * 载入多个文件, 文件路径使用Spring Resource格式.
     *
     * @param resourcesPaths
     * @return
     */
    private Properties loadProperties(String... resourcesPaths) {
        Properties props = new Properties();
        if (null != resourcesPaths) {
            for (String location : resourcesPaths) {
                InputStream is = null;
                try {
                    Resource resource = resourceLoader.getResource(location);
                    is = resource.getInputStream();
                    props.load(is);
                } catch (IOException ex) {
                    logger.info("Could not load properties from path:" + location + ", " + ex.getMessage());
                } finally {
                    IOUtils.closeQuietly(is);
                }
            }
        }
        return props;
    }
}
