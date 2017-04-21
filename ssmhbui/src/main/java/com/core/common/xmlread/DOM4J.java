/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.core.common.xmlread;

import com.core.bean.VO.TmplVo;
import com.core.utils.CheckUtil;
import java.util.Objects;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.net.URL;
import org.apache.commons.io.IOUtils;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.QName;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

/**
 * xml 文件解析
 *
 * @since dom4j xml 文件解析
 * @author xiaosun
 * @since 1.0
 * @version v1.0
 */
public class DOM4J {

    private static final Logger logger = LoggerFactory.getLogger(DOM4J.class);
    private static ResourceLoader resourceLoader = new DefaultResourceLoader();

    /**
     * 获取根节点
     *
     * @param doc
     * @return
     */
    public static Element getRootElement(Document doc) {
        if (Objects.isNull(doc)) {
            return null;
        }
        return doc.getRootElement();
    }

    /**
     * 获取节点eleName下的文本值，若eleName不存在则返回默认值defaultValue
     *
     * @param eleName
     * @param defaultValue
     * @return
     */
    public static String getElementValue(Element eleName, String defaultValue) {
        if (Objects.isNull(eleName)) {
            return defaultValue == null ? "" : defaultValue;
        } else {
            return eleName.getTextTrim();
        }
    }

    public static String getElementValue(String eleName, Element parentElement) {
        if (Objects.isNull(parentElement)) {
            return null;
        } else {
            Element element = parentElement.element(eleName);
            if (Objects.isNull(element)) {
                return element.getTextTrim();
            } else {
                try {
                    throw new Exception("找不到节点" + eleName);
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }
        }
    }

    /**
     * 获取节点eleName下的文本值
     *
     * @param eleName
     * @return
     */
    public static String getElementValue(Element eleName) {
        return getElementValue(eleName, null);
    }

    public static Document read(File file) {
        return read(file, null);
    }

    /**
     * 字符转xml
     *
     * @param doc
     * @param path
     * @return
     */
    public static Document findCDATA(Document doc, String path) {
        return DOM4J.stringToXml(DOM4J.getElementValue(path, doc.getRootElement()));
    }

    /**
     * 读取
     *
     * @param file
     * @param charset
     * @return
     */
    public static Document read(File file, String charset) {
        if (Objects.isNull(file)) {
            return null;
        }
        SAXReader reader = new SAXReader();
        // TODO 从测试
        if (Objects.isNull(charset)) {
            reader.setEncoding(charset);
        }
        Document document = null;
        try {
            document = reader.read(file);
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        return document;
    }

    public static Document read(URL url) {
        return read(url, null);
    }

    /**
     * 读取
     *
     * @param url 文件路径
     * @param charset 编码 utf-8
     * @return
     */
    public static Document read(URL url, String charset) {
        if (Objects.isNull(url)) {
            return null;
        }
        SAXReader reader = new SAXReader();
        if (Objects.isNull(charset)) {
            reader.setEncoding(charset);
        }
        Document document = null;
        try {
            document = reader.read(url);
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        return document;
    }

    /**
     * 将文档树转换成字符串
     *
     * @param doc
     * @return
     */
    public static String xmltoString(Document doc) {
        return xmltoString(doc, null);
    }

    /**
     * xml文档转字符
     *
     * @param doc
     * @param charset
     * @return
     */
    public static String xmltoString(Document doc, String charset) {
        if (Objects.isNull(doc)) {
            return "";
        }
        if (Objects.isNull(charset)) {
            return doc.asXML();
        }
        OutputFormat format = OutputFormat.createPrettyPrint();
        format.setEncoding(charset);
        StringWriter strWriter = new StringWriter();
        XMLWriter xmlWriter = new XMLWriter(strWriter, format);
        try {
            xmlWriter.write(doc);
            xmlWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return strWriter.toString();
    }

    /**
     * 持久化Document
     *
     * @param doc
     * @param charset
     * @param file
     * @throws Exception
     * @throws IOException
     */
    public static void xmltoFile(Document doc, File file, String charset)
            throws Exception {
        if (Objects.isNull(doc)) {
            throw new NullPointerException("doc cant not null");
        }
        if (Objects.isNull(charset)) {
            throw new NullPointerException("charset cant not null");
        }
        OutputFormat format = OutputFormat.createPrettyPrint();
        format.setEncoding(charset);
        FileOutputStream os = new FileOutputStream(file);
        OutputStreamWriter osw = new OutputStreamWriter(os, charset);
        XMLWriter xmlWriter = new XMLWriter(osw, format);
        try {
            xmlWriter.write(doc);
            xmlWriter.close();
            if (osw != null) {
                osw.close();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     *
     * @param doc
     * @param charset
     * @param filePath
     * @throws Exception
     * @throws IOException
     */
    public static void xmltoFile(Document doc, String filePath, String charset)
            throws Exception {
        xmltoFile(doc, new File(filePath), charset);
    }

    /**
     *
     * @param doc
     * @param filePath
     * @param charset
     * @throws Exception
     */
    public static void writDocumentToFile(Document doc, String filePath, String charset)
            throws Exception {
        xmltoFile(doc, new File(filePath), charset);
    }

    public static Document stringToXml(String text) {
        try {
            return DocumentHelper.parseText(text);
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Document createDocument() {
        return DocumentHelper.createDocument();
    }

    /**
     * xml字符串转换成bean对象
     *
     * @param xmlStr xml字符串
     * @param clazz 待转换的class
     * @return 转换后的对象
     */
    public static Object xmlStrToBean(String xmlStr, Class clazz) {
        Object obj = null;
        try {
            // 将xml格式的数据转换成Map对象  
            Map<String, Object> map = xmlStrToMap(xmlStr);
            //将map对象的数据转换成Bean对象  
            obj = mapToBean(map, clazz);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return obj;
    }

    /**
     * 将xml格式的字符串转换成Map对象
     *
     * @param xmlStr xml格式的字符串
     * @return Map对象
     */
    public static Map<String, Object> xmlStrToMap(String xmlStr) {
        if (CheckUtil.isEmpty(xmlStr)) {
            return null;
        }
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            //将xml格式的字符串转换成Document对象  
            Document doc = DocumentHelper.parseText(xmlStr);
            //获取根节点  
            Element root = doc.getRootElement();
            //获取根节点下的所有元素  
            List children = root.elements();
            //循环所有子元素  
            if (children != null && children.size() > 0) {
                for (int i = 0; i < children.size(); i++) {
                    Element child = (Element) children.get(i);
                    map.put(child.getName(), child.getTextTrim());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }

    /**
     * 将Map对象通过反射机制转换成Bean对象
     *
     * @param map 存放数据的map对象
     * @param clazz 待转换的class
     * @return 转换后的Bean对象
     */
    public static Object mapToBean(Map<String, Object> map, Class clazz) {
        Object obj = null;
        try {
            obj = clazz.newInstance();
            if (map != null && map.size() > 0) {
                for (Map.Entry<String, Object> entry : map.entrySet()) {
                    String propertyName = entry.getKey();
                    Object value = entry.getValue();
                    String setMethodName = "set"
                            + propertyName.substring(0, 1).toUpperCase()
                            + propertyName.substring(1);
                    Field field = getClassField(clazz, propertyName);
                    Class fieldTypeClass = field.getType();
                    value = convertValType(value, fieldTypeClass);
                    clazz.getMethod(setMethodName, field.getType()).invoke(obj, value);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return obj;
    }

    /**
     * 将Object类型的值，转换成bean对象属性里对应的类型值
     *
     * @param value Object对象值
     * @param fieldTypeClass 属性的类型
     * @return 转换后的值
     */
    private static Object convertValType(Object value, Class fieldTypeClass) {
        Object retVal = null;
        if (Long.class.getName().equals(fieldTypeClass.getName())
                || long.class.getName().equals(fieldTypeClass.getName())) {
            retVal = Long.parseLong(value.toString());
        } else if (Integer.class.getName().equals(fieldTypeClass.getName())
                || int.class.getName().equals(fieldTypeClass.getName())) {
            retVal = Integer.parseInt(value.toString());
        } else if (Float.class.getName().equals(fieldTypeClass.getName())
                || float.class.getName().equals(fieldTypeClass.getName())) {
            retVal = Float.parseFloat(value.toString());
        } else if (Double.class.getName().equals(fieldTypeClass.getName())
                || double.class.getName().equals(fieldTypeClass.getName())) {
            retVal = Double.parseDouble(value.toString());
        } else {
            retVal = value;
        }
        return retVal;
    }

    /**
     * 获取指定字段名称查找在class中的对应的Field对象(包括查找父类)
     *
     * @param clazz 指定的class
     * @param fieldName 字段名称
     * @return Field对象
     */
    private static Field getClassField(Class clazz, String fieldName) {
        if (Object.class.getName().equals(clazz.getName())) {
            return null;
        }
        Field[] declaredFields = clazz.getDeclaredFields();
        for (Field field : declaredFields) {
            if (field.getName().equals(fieldName)) {
                return field;
            }
        }

        Class superClass = clazz.getSuperclass();
        if (superClass != null) {// 简单的递归一下  
            return getClassField(superClass, fieldName);
        }
        return null;
    }

    /**
     * 根据路径读取文件
     *
     * @param filepath 路径
     *
     * 如tmpl/EmailTmpl.xml
     * @return
     */
    public static File getFile(String filepath) {
        if (null == filepath) {
            return null;
        }
        File files = null;
        try {
            Resource resource = resourceLoader.getResource(filepath);
            files = resource.getFile();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return files;
    }

    
    
    public static void main(String[] args) {
        String filepath = "tmpl/EmailTmpl.xml";
        File file = new File("resources/tmpl/EmailTmpl.xml");
        InputStream is = null;
        try {
            Resource resource = resourceLoader.getResource(filepath);
            is = resource.getInputStream();
            Document doc = DOM4J.read(resource.getFile());
            // 获取根元素
            Element root = doc.getRootElement();
            // 获取所有子元素
            List<Element> childList = root.elements();
            //定义并初始化一个数组 
            for (Element e : childList) {
                //取得该节点下所有子节点
                List<Element> liste = e.elements();
                String types = e.elementText("type");
                System.out.println(e); //逐个输出数组元素的值 
            }
            System.out.println("childList:" + childList.toString());

            String dostr = xmltoString(doc, "UTF-8");
            System.out.println("dostr:" + dostr);
            //mpa to bean
            //获取根节点
            Element ele = getRootElement(doc);
            System.out.println("ele:" + ele);
            //根节点名字
            String rootname = ele.getName();
            System.out.println("rootname:" + rootname);
            //子节点
            Document doc2 = ele.getDocument();
            System.out.println("doc2:" + doc2);
            String docstr = doc2.getText();
            System.out.println("docstr:" + docstr);
            Object obj = ele.getData();
            System.out.println("obj:" + obj);
        } catch (IOException ex) {
            logger.info("Could not load properties from path:" + filepath + ", " + ex.getMessage());
        } finally {
            IOUtils.closeQuietly(is);
        }
//        Document doc = DOM4J.read(file);
//        System.out.println(doc);
    }
}
