/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.core.utils.excel;

import com.core.utils.CheckUtil;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFPalette;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;

/**
 * poi导出excel(xls后缀)
 *
 * @author LinQiang
 * @date 2016-12-8 10:57:32
 * @version V1.0
 */
public class PoiExport {

    HttpServletResponse response;
    //默认列宽
    private int defTitleSize = 13;
    //默认导出后缀
    private String defSuffix = ".xls";
    // 文件名  
    private String fileName;
    //文件保存路径  
    private String fileDir;
    //sheet名  
    private String sheetName;
    //表头字体  
    private String titleFontType = "Arial Unicode MS";
    //表头背景色  
    private String titleBackColor = "C1FBEE";
    //表头字号  
    private short titleFontSize = 12;
    //添加自动筛选的列 如 A:M  
    private String address = "";
    //正文字体  
    private String contentFontType = "Arial Unicode MS";
    //正文字号  
    private short contentFontSize = 12;
    //Float类型数据小数位  
    private String floatDecimal = ".00";
    //Double类型数据小数位  
    private String doubleDecimal = ".00";
    //设置列的公式  
    private String colFormula[] = null;
    //浮点数格式化
    DecimalFormat floatDecimalFormat = new DecimalFormat(floatDecimal);
    DecimalFormat doubleDecimalFormat = new DecimalFormat(doubleDecimal);

    private HSSFWorkbook workbook = null;

    public PoiExport() {

    }

    public PoiExport(String fileDir, String sheetName) {
        this.fileDir = fileDir;
        this.sheetName = sheetName;
        workbook = new HSSFWorkbook();
    }

    public PoiExport(HttpServletResponse response, String fileName, String sheetName) {
        this.response = response;
        this.sheetName = sheetName;
        this.fileName = fileName;
        this.sheetName = sheetName;
        workbook = new HSSFWorkbook();
    }

    /**
     * 设置表头字体.
     *
     * @param titleFontType
     */
    public void setTitleFontType(String titleFontType) {
        this.titleFontType = titleFontType;
    }

    /**
     * 设置表头背景色.
     *
     * @param titleBackColor 十六进制
     */
    public void setTitleBackColor(String titleBackColor) {
        this.titleBackColor = titleBackColor;
    }

    /**
     * 设置表头字体大小.
     *
     * @param titleFontSize
     */
    public void setTitleFontSize(short titleFontSize) {
        this.titleFontSize = titleFontSize;
    }

    /**
     * 设置表头自动筛选栏位,如A:AC.
     *
     * @param address
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * 设置正文字体.
     *
     * @param contentFontType
     */
    public void setContentFontType(String contentFontType) {
        this.contentFontType = contentFontType;
    }

    /**
     * 设置正文字号.
     *
     * @param contentFontSize
     */
    public void setContentFontSize(short contentFontSize) {
        this.contentFontSize = contentFontSize;
    }

    /**
     * 设置float类型数据小数位 默认.00
     *
     * @param doubleDecimal 如 ".00"
     */
    public void setDoubleDecimal(String doubleDecimal) {
        this.doubleDecimal = doubleDecimal;
    }

    /**
     * 设置doubel类型数据小数位 默认.00
     *
     * @param floatDecimalFormat 如 ".00
     */
    public void setFloatDecimalFormat(DecimalFormat floatDecimalFormat) {
        this.floatDecimalFormat = floatDecimalFormat;
    }

    /**
     * 设置列的公式
     *
     * @param colFormula 存储i-1列的公式 涉及到的行号使用@替换 如A@+B@
     */
    public void setColFormula(String[] colFormula) {
        this.colFormula = colFormula;
    }

    /**
     * 写excel.
     *
     * @param titleColumn 对应bean的属性名
     * @param titleName excel要导出的表名,和titleColumn每列对应
     * @param titleSize 列宽(可为空，每列默认为13)
     * @param dataList 数据
     */
    public void wirteExcel(String titleColumn[], String titleName[], int titleSize[], List<?> dataList) {
        //新建文件  
        OutputStream out = null;
        try {
            if (CheckUtil.isEmpty(titleColumn) || CheckUtil.isEmpty(titleName) && CheckUtil.isEmpty(dataList)) {
                return;
            }
            //添加Worksheet（不添加sheet时生成的xls文件打开时会报错)  
            Sheet sheet = workbook.createSheet("sheet1");
            if (null != fileDir) {
                //有文件路径  
                out = new FileOutputStream(fileDir);
            } else {
                //否则，直接写到输出流中  
                out = response.getOutputStream();
                fileName = fileName + defSuffix;
                response.setContentType("application/x-msdownload");
                response.setHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode(fileName, "UTF-8"));
            }

            //写入excel的表头  
            Row titleNameRow = workbook.getSheet(sheetName).createRow(0);
            //设置样式  
            HSSFCellStyle titleStyle = workbook.createCellStyle();
            titleStyle = (HSSFCellStyle) setFontAndBorder(titleStyle, titleFontType, (short) titleFontSize);
            titleStyle = (HSSFCellStyle) setColor(titleStyle, titleBackColor, (short) 10);
            if (null == titleSize) {
                for (int i = 0; i < titleName.length; i++) {
                    titleSize[i] = defTitleSize;
                }
            }
            for (int i = 0; i < titleName.length; i++) {
                sheet.setColumnWidth(i, titleSize[i] * 256);    //设置宽度          
                Cell cell = titleNameRow.createCell(i);
                cell.setCellStyle(titleStyle);
                cell.setCellValue(titleName[i]);
            }
            //为表头添加自动筛选  
            if (!"".equals(address)) {
                CellRangeAddress c = (CellRangeAddress) CellRangeAddress.valueOf(address);
                sheet.setAutoFilter(c);
            }
            //通过反射获取数据并写入到excel中  
            //设置样式  
            HSSFCellStyle dataStyle = workbook.createCellStyle();
            titleStyle = (HSSFCellStyle) setFontAndBorder(titleStyle, contentFontType, (short) contentFontSize);
            for (int rowIndex = 1; rowIndex <= dataList.size(); rowIndex++) {
                Object obj = dataList.get(rowIndex - 1);     //获得该对象  
                Class clsss = obj.getClass();     //获得该对对象的class实例  
                Row dataRow = workbook.getSheet(sheetName).createRow(rowIndex);
                for (int columnIndex = 0; columnIndex < titleColumn.length; columnIndex++) {
                    String title = titleColumn[columnIndex].trim();
                    if (!CheckUtil.isEmpty(title)) {  //字段不为空  
                        //使首字母大写  
                        String UTitle = Character.toUpperCase(title.charAt(0)) + title.substring(1, title.length()); // 使其首字母大写;  
                        String methodName = "get" + UTitle;

                        // 设置要执行的方法  
                        Method method = clsss.getDeclaredMethod(methodName);

                        //获取返回类型  
                        String returnType = method.getReturnType().getName();

                        String data = method.invoke(obj) == null ? "" : method.invoke(obj).toString();
                        Cell cell = dataRow.createCell(columnIndex);
                        if (data != null && !"".equals(data)) {
                            if ("int".equals(returnType)) {
                                cell.setCellValue(Integer.parseInt(data));
                            } else if ("long".equals(returnType)) {
                                cell.setCellValue(Long.parseLong(data));
                            } else if ("float".equals(returnType)) {
                                cell.setCellValue(floatDecimalFormat.format(Float.parseFloat(data)));
                            } else if ("double".equals(returnType)) {
                                cell.setCellValue(doubleDecimalFormat.format(Double.parseDouble(data)));
                            } else {
                                cell.setCellValue(data);
                            }
                        }
                    } else {   //字段为空 检查该列是否是公式  
                        if (colFormula != null) {
                            String sixBuf = colFormula[columnIndex].replace("@", (rowIndex + 1) + "");
                            Cell cell = dataRow.createCell(columnIndex);
                            cell.setCellFormula(sixBuf);
                        }
                    }
                }
            }
            if (null != out) {
                workbook.write(out);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (null != out) {
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 写excel.
     *
     * @param response
     * @param fileName
     * @param titleMap 标题字段(key为bean中的字段名，value为excel中对应的中文标题)
     * @param titleSize 列宽(可为空，每列默认为13)
     * @param dataList 数据
     */
    public void wirteExcel(HttpServletResponse response, String fileName, Map<String, String> titleMap, int titleSize[], List<?> dataList) {
        //添加Worksheet（不添加sheet时生成的xls文件打开时会报错)  
        workbook = new HSSFWorkbook();
        Sheet sheet = workbook.createSheet("sheet1");
        //新建文件  
        OutputStream out = null;
        try {
            if (fileDir != null) {
                //有文件路径  
                out = new FileOutputStream(fileDir);
            } else {
                //否则，直接写到输出流中  
                out = response.getOutputStream();
                fileName = fileName + defSuffix;
                response.setContentType("application/x-msdownload");
                response.setHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode(fileName, "UTF-8"));
            }

            //写入excel的表头  
            Row titleNameRow = sheet.createRow(0);
            //设置样式  
            HSSFCellStyle titleStyle = workbook.createCellStyle();
            titleStyle = (HSSFCellStyle) setFontAndBorder(titleStyle, titleFontType, (short) titleFontSize);
            titleStyle = (HSSFCellStyle) setColor(titleStyle, titleBackColor, (short) 10);

            if (titleSize == null || titleSize.length <= 0) {
                titleSize = new int[titleMap.size()];
                for (int i = 0; i < titleMap.size(); i++) {
                    titleSize[i] = 13;
                }
            }

            //设置表头的值
            int index = 0;
            for (Map.Entry<String, String> entry : titleMap.entrySet()) {
                String value = entry.getValue();
                sheet.setColumnWidth(index, titleSize[index] * 256);    //设置宽度          
                Cell cell = titleNameRow.createCell(index);
                cell.setCellStyle(titleStyle);
                cell.setCellValue(value);
                index++;
            }

            //为表头添加自动筛选  
            if (!"".equals(address)) {
                CellRangeAddress c = (CellRangeAddress) CellRangeAddress.valueOf(address);
                sheet.setAutoFilter(c);
            }

            //通过反射获取数据并写入到excel中  
            if (dataList != null && dataList.size() > 0) {
                //设置样式  
                HSSFCellStyle dataStyle = workbook.createCellStyle();
                dataStyle = (HSSFCellStyle) setFontAndBorder(dataStyle, contentFontType, (short) contentFontSize);

                if (titleMap.size() > 0) {
                    for (int rowIndex = 1; rowIndex <= dataList.size(); rowIndex++) {
                        Object obj = dataList.get(rowIndex - 1);     //获得该对象  
                        Class clsss = obj.getClass();     //获得该对对象的class实例  
                        Row dataRow = sheet.createRow(rowIndex);
                        int columnIndex = 0;
                        for (Map.Entry<String, String> entry : titleMap.entrySet()) {
                            String title = entry.getKey().trim();
                            if (!"".equals(title)) {  //字段不为空  
                                //使首字母大写  
                                String UTitle = Character.toUpperCase(title.charAt(0)) + title.substring(1, title.length()); // 使其首字母大写;  
                                String methodName = "get" + UTitle;

                                // 设置要执行的方法  
                                Method method = clsss.getDeclaredMethod(methodName);

                                //获取返回类型  
                                String returnType = method.getReturnType().getName();

                                String data = (null == method.invoke(obj) ? "" : method.invoke(obj).toString());
                                Cell cell = dataRow.createCell(columnIndex);
                                cell.setCellStyle(dataStyle);
                                if (!CheckUtil.isEmpty(data)) {
                                    if ("int".equals(returnType)) {
                                        cell.setCellValue(Integer.parseInt(data));
                                    } else if ("long".equals(returnType)) {
                                        cell.setCellValue(Long.parseLong(data));
                                    } else if ("float".equals(returnType)) {
                                        cell.setCellValue(floatDecimalFormat.format(Float.parseFloat(data)));
                                    } else if ("double".equals(returnType)) {
                                        cell.setCellValue(doubleDecimalFormat.format(Double.parseDouble(data)));
                                    } else {
                                        cell.setCellValue(data);
                                    }
                                }
                            } else {   //字段为空 检查该列是否是公式  
                                if (colFormula != null) {
                                    String sixBuf = title.replace("@", (rowIndex + 1) + "");
                                    Cell cell = dataRow.createCell(columnIndex);
                                    cell.setCellFormula(sixBuf);
                                }
                            }
                            columnIndex++;
                        }
                    }

                }
            }

            workbook.write(out);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 写excel.
     *
     * @param response
     * @param fileName
     * @param titleMap 标题字段(key为bean中的字段名，value为excel中对应的中文标题)
     * @param titleSize 列宽(可为空，每列默认为13)
     * @param dataList 数据
     */
    public void wirteExcelData(HttpServletResponse response, String fileName, Map<String, String> titleMap, int titleSize[], List<Map<String, Object>> dataList) {
        //添加Worksheet（不添加sheet时生成的xls文件打开时会报错)  
        workbook = new HSSFWorkbook();
        Sheet sheet = workbook.createSheet("sheet1");
        //新建文件  
        OutputStream out = null;
        try {
            if (fileDir != null) {
                //有文件路径  
                out = new FileOutputStream(fileDir);
            } else {
                //否则，直接写到输出流中  
                out = response.getOutputStream();
                fileName = fileName + defSuffix;
                response.setContentType("application/x-msdownload");
                response.setHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode(fileName, "UTF-8"));
            }

            //写入excel的表头  
            Row titleNameRow = sheet.createRow(0);
            //设置样式  
            HSSFCellStyle titleStyle = workbook.createCellStyle();
            titleStyle = (HSSFCellStyle) setFontAndBorder(titleStyle, titleFontType, (short) titleFontSize);
            titleStyle = (HSSFCellStyle) setColor(titleStyle, titleBackColor, (short) 10);

            if (titleSize == null || titleSize.length <= 0) {
                titleSize = new int[titleMap.size()];
                for (int i = 0; i < titleMap.size(); i++) {
                    titleSize[i] = 13;
                }
            }

            //设置表头的值
            int index = 0;
            for (Map.Entry<String, String> entry : titleMap.entrySet()) {
                String value = entry.getValue();
                sheet.setColumnWidth(index, titleSize[index] * 256);    //设置宽度          
                Cell cell = titleNameRow.createCell(index);
                cell.setCellStyle(titleStyle);
                cell.setCellValue(value);
                index++;
            }

            //为表头添加自动筛选  
            if (!"".equals(address)) {
                CellRangeAddress c = (CellRangeAddress) CellRangeAddress.valueOf(address);
                sheet.setAutoFilter(c);
            }

            //通过反射获取数据并写入到excel中  
            if (dataList != null && dataList.size() > 0) {
                //设置样式  
                HSSFCellStyle dataStyle = workbook.createCellStyle();
                dataStyle = (HSSFCellStyle) setFontAndBorder(dataStyle, contentFontType, (short) contentFontSize);

                if (titleMap.size() > 0) {
                    for (int rowIndex = 1; rowIndex <= dataList.size(); rowIndex++) {
                        Map<String, Object> map = dataList.get(rowIndex - 1);     //获得该对象  
                        Row dataRow = sheet.createRow(rowIndex);
                        int columnIndex = 0;
                        for (Map.Entry<String, String> entry : titleMap.entrySet()) {
                            String title = entry.getKey().trim();
                            if (!CheckUtil.isEmpty(title)) {  //字段不为空  
                                Cell cell = dataRow.createCell(columnIndex);
                                Object value = map.get(title);
                                if (!CheckUtil.isEmpty(value)) {
                                    String returnType = CheckUtil.typeOf(value);
                                    if (Integer.class.getSimpleName().equalsIgnoreCase(returnType)) {
                                        cell.setCellValue(Integer.parseInt(value + ""));
                                    } else if (Long.class.getSimpleName().equals(returnType)) {
                                        cell.setCellValue(Long.parseLong(value + ""));
                                    } else if (Float.class.getSimpleName().equals(returnType)) {
                                        cell.setCellValue(floatDecimalFormat.format(Float.parseFloat(value + "")));
                                    } else if (Double.class.getSimpleName().equals(returnType)) {
                                        cell.setCellValue(doubleDecimalFormat.format(Double.parseDouble(value + "")));
                                    } else {
                                        cell.setCellValue(value+"");
                                    }
                                }
                            } else {   //字段为空 检查该列是否是公式  
                                if (colFormula != null) {
                                    String sixBuf = title.replace("@", (rowIndex + 1) + "");
                                    Cell cell = dataRow.createCell(columnIndex);
                                    cell.setCellFormula(sixBuf);
                                }
                            }
                            columnIndex++;
                        }
                    }

                }
            }

            workbook.write(out);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 将16进制的颜色代码写入样式中来设置颜色
     *
     * @param style 保证style统一
     * @param color 颜色：66FFDD
     * @param index 索引 8-64 使用时不可重复
     * @return
     */
    public CellStyle setColor(CellStyle style, String color, short index) {
        if (!"".equals(color) && color != null) {
            //转为RGB码  
            int r = Integer.parseInt((color.substring(0, 2)), 16);   //转为16进制  
            int g = Integer.parseInt((color.substring(2, 4)), 16);
            int b = Integer.parseInt((color.substring(4, 6)), 16);
            //自定义cell颜色  
            HSSFPalette palette = workbook.getCustomPalette();
            palette.setColorAtIndex((short) index, (byte) r, (byte) g, (byte) b);

            style.setFillPattern(CellStyle.SOLID_FOREGROUND);
            style.setFillForegroundColor(index);
        }
        return style;
    }

    /**
     * 设置字体并加外边框
     *
     * @param style 样式
     * @param fontName 字体名
     * @param size 大小
     * @return
     */
    public CellStyle setFontAndBorder(CellStyle style, String fontName, short size) {
        try {
            HSSFFont font = workbook.createFont();
            font.setFontHeightInPoints(size);
            font.setFontName(fontName);
            font.setBoldweight(size);
            style.setFont(font);
            style.setWrapText(true);//自动换行
            style.setBorderBottom(CellStyle.BORDER_THIN); //下边框      
            style.setBorderLeft(CellStyle.BORDER_THIN);//左边框      
            style.setBorderTop(CellStyle.BORDER_THIN);//上边框      
            style.setBorderRight(CellStyle.BORDER_THIN);//右边框  
        } catch (Exception e) {
            e.printStackTrace();
        }
        return style;
    }

    /**
     * 删除文件
     *
     * @return
     */
    public boolean deleteExcel() {
        boolean flag = false;
        try {
            File file = new File(this.fileDir);
            // 判断目录或文件是否存在    
            if (!file.exists()) {  // 不存在返回 false    
                return flag;
            } else {
                // 判断是否为文件    
                if (file.isFile()) {  // 为文件时调用删除文件方法    
                    file.delete();
                    flag = true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return flag;
    }

    /**
     * 删除文件
     *
     * @param path
     * @return
     */
    public boolean deleteExcel(String path) {
        boolean flag = false;
        try {
            File file = new File(path);
            // 判断目录或文件是否存在    
            if (!file.exists()) {  // 不存在返回 false    
                return flag;
            } else {
                // 判断是否为文件    
                if (file.isFile()) {  // 为文件时调用删除文件方法    
                    file.delete();
                    flag = true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return flag;
    }

}
