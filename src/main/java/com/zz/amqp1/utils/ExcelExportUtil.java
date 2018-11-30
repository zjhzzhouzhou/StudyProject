/**************************************************************************
 * Copyright (c) 2006-2017 ZheJiang Electronic Port, Inc.
 * All rights reserved.
 * <p>
 * 项目名称：货车非现金支付平台
 * 版权说明：本软件属浙江电子口岸有限公司所有，在未获得浙江电子口岸有限公司正式授权
 * 情况下，任何企业和个人，不能获取、阅读、安装、传播本软件涉及的任何受知
 * 识产权保护的内容。
 ***************************************************************************/
package com.zz.amqp1.utils;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFPalette;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;

/**
 * 类描述
 *
 * @author <a href="mailto:lvjx@zjport.gov.cn">lvjx</a>
 * @version $Id: ExcelExportUtil.java 5921 2018-10-08 09:10:53Z zhaozy $
 * @since 1.0
 */
public  class ExcelExportUtil {
    HttpServletResponse response;
    // 文件名
    private String fileName ;
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



    DecimalFormat floatDecimalFormat=new DecimalFormat(floatDecimal);
    DecimalFormat doubleDecimalFormat=new DecimalFormat(doubleDecimal);

    private HSSFWorkbook workbook = null;
    SXSSFWorkbook xssfbook = null;

    public ExcelExportUtil(String fileDir, String sheetName){
        this.fileDir = fileDir;
        this.sheetName = sheetName;
        workbook = new HSSFWorkbook();
        xssfbook = new SXSSFWorkbook(1000);
    }

    public ExcelExportUtil(HttpServletResponse response, String fileName, String sheetName){
        this.response = response;
        this.sheetName = sheetName;
        this.fileName = fileName;
        workbook = new HSSFWorkbook();
        xssfbook = new SXSSFWorkbook(1000);
    }
    /**
     * 设置表头字体.
     * @param titleFontType
     */
    public void setTitleFontType(String titleFontType) {
        this.titleFontType = titleFontType;
    }
    /**
     * 设置表头背景色.
     * @param titleBackColor 十六进制
     */
    public void setTitleBackColor(String titleBackColor) {
        this.titleBackColor = titleBackColor;
    }
    /**
     * 设置表头字体大小.
     * @param titleFontSize
     */
    public void setTitleFontSize(short titleFontSize) {
        this.titleFontSize = titleFontSize;
    }
    /**
     * 设置表头自动筛选栏位,如A:AC.
     * @param address
     */
    public void setAddress(String address) {
        this.address = address;
    }
    /**
     * 设置正文字体.
     * @param contentFontType
     */
    public void setContentFontType(String contentFontType) {
        this.contentFontType = contentFontType;
    }
    /**
     * 设置正文字号.
     * @param contentFontSize
     */
    public void setContentFontSize(short contentFontSize) {
        this.contentFontSize = contentFontSize;
    }
    /**
     * 设置float类型数据小数位 默认.00
     * @param doubleDecimal 如 ".00"
     */
    public void setDoubleDecimal(String doubleDecimal) {
        this.doubleDecimal = doubleDecimal;
    }
    /**
     * 设置doubel类型数据小数位 默认.00
     * @param floatDecimalFormat 如 ".00
     */
    public void setFloatDecimalFormat(DecimalFormat floatDecimalFormat) {
        this.floatDecimalFormat = floatDecimalFormat;
    }
    /**
     * 设置列的公式
     * @param colFormula  存储i-1列的公式 涉及到的行号使用@替换 如A@+B@
     */
    public void setColFormula(String[] colFormula) {
        this.colFormula = colFormula;
    }
    /**
     * 写excel.
     * @param titleColumn  对应bean的属性名
     * @param titleName   excel要导出的表名
     * @param titleSize   列宽
     * @param dataList  数据
     */
    public void wirteExcel(String titleColumn[],String titleName[],int titleSize[],List<?> dataList){
        //添加Worksheet（不添加sheet时生成的xls文件打开时会报错)
        Sheet sheet = workbook.createSheet(this.sheetName);
        //新建文件
        OutputStream out = null;
        try {
            if(fileDir!=null){
                //有文件路径
                out = new FileOutputStream(fileDir);
            }else{
                //否则，直接写到输出流中
                out = response.getOutputStream();
                fileName = fileName+".xls";
                response.setContentType("application/x-msdownload");
                response.setHeader("Content-Disposition", "attachment; filename="
                        + URLEncoder.encode(fileName, "UTF-8"));
            }

            //写入excel的表头
            Row titleNameRow = workbook.getSheet(sheetName).createRow(0);
            //设置样式
            HSSFCellStyle titleStyle = workbook.createCellStyle();
            titleStyle = (HSSFCellStyle) setFontAndBorder(titleStyle, titleFontType, (short) titleFontSize);
            titleStyle = (HSSFCellStyle) setColor(titleStyle, titleBackColor, (short)10);

            for(int i = 0;i < titleName.length;i++){
                sheet.setColumnWidth(i, titleSize[i]*256);    //设置宽度
                Cell cell = titleNameRow.createCell(i);
                cell.setCellStyle(titleStyle);
                cell.setCellValue(titleName[i].toString());
            }

            //为表头添加自动筛选
            if(!"".equals(address)){
                CellRangeAddress c = (CellRangeAddress) CellRangeAddress.valueOf(address);
                sheet.setAutoFilter(c);
            }

            //通过反射获取数据并写入到excel中
            if(dataList!=null&&dataList.size()>0){
                //设置样式
                HSSFCellStyle dataStyle = workbook.createCellStyle();
                titleStyle = (HSSFCellStyle) setFontAndBorder(titleStyle, contentFontType, (short) contentFontSize);

                if(titleColumn.length>0){
                    for(int rowIndex = 1;rowIndex<=dataList.size();rowIndex++){
                        Object obj = dataList.get(rowIndex-1);     //获得该对象
                        Class clsss = obj.getClass();     //获得该对对象的class实例
                        Row dataRow = workbook.getSheet(sheetName).createRow(rowIndex);
                        for(int columnIndex = 0;columnIndex<titleColumn.length;columnIndex++){
                            //获取返回类型
                            String returnType = "string";
                            String data = null;
                            String title = titleColumn[columnIndex].toString().trim();
                            if(!"".equals(title)){  //字段不为空
                                if(obj instanceof Map){
                                    data = ((Map) obj).get(title)!=null?((Map) obj).get(title).toString():"";
                                }else{
                                    //使首字母大写
                                    String UTitle = Character.toUpperCase(title.charAt(0))+ title.substring(1, title.length()); // 使其首字母大写;
                                    String methodName  = "get"+UTitle;
                                    // 设置要执行的方法
                                    Method method = clsss.getDeclaredMethod(methodName);
                                    //获取返回类型
                                     returnType = method.getReturnType().getName();
                                     data = method.invoke(obj)==null?"":method.invoke(obj).toString();
                                }

                                Cell cell = dataRow.createCell(columnIndex);
                                if(data!=null&&!"".equals(data)){
                                    if("int".equals(returnType)){
                                        cell.setCellValue(Integer.parseInt(data));
                                    }else if("long".equals(returnType)){
                                        cell.setCellValue(Long.parseLong(data));
                                    }else if("float".equals(returnType)){
                                        cell.setCellValue(floatDecimalFormat.format(Float.parseFloat(data)));
                                    }else if("double".equals(returnType)){
                                        cell.setCellValue(doubleDecimalFormat.format(Double.parseDouble(data)));
                                    }else{
                                        cell.setCellValue(data);
                                    }
                                }
                            }else{   //字段为空 检查该列是否是公式
                                if(colFormula!=null){
                                    String sixBuf = colFormula[columnIndex].replace("@", (rowIndex+1)+"");
                                    Cell cell = dataRow.createCell(columnIndex);
                                    cell.setCellFormula(sixBuf.toString());
                                }
                            }
                        }
                    }

                }
            }

            workbook.write(out);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
            	if(out!=null){
            		out.close();
            	}
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 将16进制的颜色代码写入样式中来设置颜色
     * @param style  保证style统一
     * @param color 颜色：66FFDD
     * @param index 索引 8-64 使用时不可重复
     * @return
     */
    public CellStyle setColor(CellStyle style,String color,short index){
        if(StringUtils.isNotEmpty(color)){
            //转为RGB码
            int r = Integer.parseInt((color.substring(0,2)),16);   //转为16进制
            int g = Integer.parseInt((color.substring(2,4)),16);
            int b = Integer.parseInt((color.substring(4,6)),16);
            //自定义cell颜色
            HSSFPalette palette = workbook.getCustomPalette();
            palette.setColorAtIndex((short)index, (byte) r, (byte) g, (byte) b);

            style.setFillPattern(CellStyle.SOLID_FOREGROUND);
            style.setFillForegroundColor(index);
        }
        return style;
    }

    /**
     * 设置字体并加外边框
     * @param style  样式
     * @param style  字体名
     * @param style  大小
     * @return
     */
    public CellStyle setFontAndBorder(CellStyle style,String fontName,short size){
        HSSFFont font = workbook.createFont();
        font.setFontHeightInPoints(size);
        font.setFontName(fontName);
        font.setBold(true);
        style.setFont(font);
        style.setBorderBottom(CellStyle.BORDER_THIN); //下边框
        style.setBorderLeft(CellStyle.BORDER_THIN);//左边框
        style.setBorderTop(CellStyle.BORDER_THIN);//上边框
        style.setBorderRight(CellStyle.BORDER_THIN);//右边框
        return style;
    }
    /**
     * 删除文件
     * @param
     * @return
     */
    public boolean deleteExcel(){
        boolean flag = false;
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
        return flag;
    }
    /**
     * 删除文件
     * @param path
     * @return
     */
    public boolean deleteExcel(String path){
        boolean flag = false;
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
        return flag;
    }

    
    public void wirteExcelByMap(String[] titleColumn,Map<String, String[]>  titleNameMap,int[] titleSize,Map<String,List<Object>> map){
        //添加Worksheet（不添加sheet时生成的xls文件打开时会报错)
        Sheet sheet = workbook.createSheet(this.sheetName);
        //新建文件
        OutputStream out = null;
        try {
            if(fileDir!=null){
                //有文件路径
                out = new FileOutputStream(fileDir);
            }else{
                //否则，直接写到输出流中
                out = response.getOutputStream();
                fileName = fileName+".xls";
                response.setContentType("application/x-msdownload");
                response.setHeader("Content-Disposition", "attachment; filename="
                        + URLEncoder.encode(fileName, "UTF-8"));
            }
            int rowIndex=0;
            for(Map.Entry<String, List<Object>> mEntry:map.entrySet()){ 
            	//写入excel的表头
            	Row titleNameRow = workbook.getSheet(sheetName).createRow(rowIndex);
            	//设置样式
            	HSSFCellStyle titleStyle = workbook.createCellStyle();
            	titleStyle = (HSSFCellStyle) setFontAndBorder(titleStyle, titleFontType, (short) titleFontSize);
            	titleStyle = (HSSFCellStyle) setColor(titleStyle, titleBackColor, (short)10);
            	String[] titleName=titleNameMap.get(mEntry.getKey());
            	for(int i = 0;i < titleName.length;i++){
            		sheet.setColumnWidth(i, titleSize[i]*256);    //设置宽度
            		Cell cell = titleNameRow.createCell(i);
            		cell.setCellStyle(titleStyle);
            		cell.setCellValue(titleName[i].toString());
            	}
            	//为表头添加自动筛选
            	if(!"".equals(address)){
            		CellRangeAddress c = (CellRangeAddress) CellRangeAddress.valueOf(address);
            		sheet.setAutoFilter(c);
            	}
            	List<?> dataList=mEntry.getValue();
            	//通过反射获取数据并写入到excel中
            	if(dataList!=null&&dataList.size()>0){
            		//设置样式
            		HSSFCellStyle dataStyle = workbook.createCellStyle();
            		titleStyle = (HSSFCellStyle) setFontAndBorder(titleStyle, contentFontType, (short) contentFontSize);
            		
            		if(titleColumn.length>0){
            			for(int  i= 0;i<dataList.size();i++){
            				++rowIndex;
            				Object obj = dataList.get(i);     //获得该对象
            				Class clsss = obj.getClass();     //获得该对对象的class实例
            				Row dataRow = workbook.getSheet(sheetName).createRow(rowIndex);
            				for(int columnIndex = 0;columnIndex<titleColumn.length;columnIndex++){
            					//获取返回类型
            					String returnType = "string";
            					String data = null;
            					String title = titleColumn[columnIndex].toString().trim();
            					if(!"".equals(title)){  //字段不为空
            						if(obj instanceof Map){
            							data = ((Map) obj).get(title)!=null?((Map) obj).get(title).toString():"";
            						}else{
            							//使首字母大写
            							String UTitle = Character.toUpperCase(title.charAt(0))+ title.substring(1, title.length()); // 使其首字母大写;
            							String methodName  =""; 
            							if((i==dataList.size()-1)&&UTitle.endsWith("Num")&&("ccb".equals(mEntry.getKey())||"zjportDepositstate".equals(mEntry.getKey()))){
            								methodName="get"+UTitle+"Rate";
            							}else{
            								methodName="get"+UTitle;
            							}
            							// 设置要执行的方法
            							Method method = clsss.getDeclaredMethod(methodName);
            							//获取返回类型
            							returnType = method.getReturnType().getName();
            							data = method.invoke(obj)==null?"":method.invoke(obj).toString();
            						}
            						
            						Cell cell = dataRow.createCell(columnIndex);
            						if(data!=null&&!"".equals(data)){
            							cell.setCellValue(data);
            						}
            					}else{   //字段为空 检查该列是否是公式
            						if(colFormula!=null){
            							String sixBuf = colFormula[columnIndex].replace("@", (rowIndex+1)+"");
            							Cell cell = dataRow.createCell(columnIndex);
            							cell.setCellFormula(sixBuf.toString());
            						}
            					}
            				}
            			}
            			
            		}
            	}
            	workbook.getSheet(sheetName).createRow(++rowIndex);
            }
            workbook.write(out);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
            	if(out!=null){
            		out.close();
            	}
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    /**
     * 导出各余额及扣款信息的excel.
     * @param titleColumn  对应bean的属性名
     * @param titleName   excel要导出的表名
     * @param titleSize   列宽
     * @param dataList  数据
     */
    public void wirteExportExcel(String titleColumn[],String titleName[],int titleSize[],Map<String,Object> dataList){
        //添加Worksheet（不添加sheet时生成的xls文件打开时会报错)
        Sheet sheet = workbook.createSheet(this.sheetName);
        //新建文件
        OutputStream out = null;
        try {
            if(fileDir!=null){
                //有文件路径
                out = new FileOutputStream(fileDir);
            }else{
                //否则，直接写到输出流中
                out = response.getOutputStream();
                fileName = fileName+".xls";
                response.setContentType("application/x-msdownload");
                response.setHeader("Content-Disposition", "attachment; filename="
                        + URLEncoder.encode(fileName, "UTF-8"));
            }

            Float ccbDeductions = (Float)dataList.get("ccbDeductions");
            //写入excel的表头
            Row titleNameRow = workbook.getSheet(sheetName).createRow(0);
            //设置样式
            HSSFCellStyle titleStyle = workbook.createCellStyle();
            titleStyle = (HSSFCellStyle) setFontAndBorder(titleStyle, titleFontType, (short) titleFontSize);
            titleStyle = (HSSFCellStyle) setColor(titleStyle, titleBackColor, (short)10);
            sheet.setColumnWidth(0, titleSize[0]*256);    //设置宽度
            //第一行
            Cell cell0 = titleNameRow.createCell(0);
            cell0.setCellStyle(titleStyle);
            cell0.setCellValue("记账日期");
            sheet.setColumnWidth(1, titleSize[1]*256);    //设置宽度
            Cell cell1 = titleNameRow.createCell(1);
            cell1.setCellStyle(titleStyle);
            cell1.setCellValue((String)dataList.get("startAndEnd"));
            for(int i= 1;i<3;i++){
                sheet.setColumnWidth(2*i, titleSize[i]*256);    //设置宽度
                Cell cell2 = titleNameRow.createCell(2*i);
                cell2.setCellStyle(titleStyle);
                cell2.setCellValue(titleName[i].toString());
                sheet.setColumnWidth((2*i)+1, titleSize[i]*256);    //设置宽度
                Cell cell3 = titleNameRow.createCell((2*i)+1);
                cell3.setCellStyle(titleStyle);
                cell3.setCellValue(floatDecimalFormat.format(ccbDeductions));
            }
            //写入excel的表头
            Row titleCustomerRow = workbook.getSheet(sheetName).createRow(1);
            sheet.setColumnWidth(0, titleSize[0]*256);    //设置宽度
            Cell cellOne = titleCustomerRow.createCell(0);
            cellOne.setCellStyle(titleStyle);
            cellOne.setCellValue("记账日期");
            for(int i = 1;i < titleName.length-12;i++){
                sheet.setColumnWidth(i, titleSize[i]*256);    //设置宽度
                Cell cell = titleCustomerRow.createCell(i);
                cell.setCellStyle(titleStyle);
                cell.setCellValue(titleName[i+3].toString());
            }

            //获得可用余额和保证金的list
            Map<String,List<Double>> map=(Map<String,List<Double>>)dataList.get("customerAmount");
            //为表头添加自动筛选
            if(!"".equals(address)){
                CellRangeAddress c = (CellRangeAddress) CellRangeAddress.valueOf(address);
                sheet.setAutoFilter(c);
            }

            /**用户的日常账单集合*/
            List totaletcCustomerDailyBills = (List) dataList.get("totaletcCustomerDailyBills");

            /**签约机构的各项数据明细集合*/
            List contractinTotals = (List)  dataList.get("contractinTotal");
            //通过反射获取数据并写入到excel中
            if(totaletcCustomerDailyBills!=null&&totaletcCustomerDailyBills.size()>0){
                //设置样式
                HSSFCellStyle dataStyle = workbook.createCellStyle();
                titleStyle = (HSSFCellStyle) setFontAndBorder(titleStyle, contentFontType, (short) contentFontSize);

                if(titleColumn.length>0){
                    for(int rowIndex = 2;rowIndex<=totaletcCustomerDailyBills.size()+1;rowIndex++){
                        Object obj = totaletcCustomerDailyBills.get(rowIndex-2);     //获得该对象
                        Class clsss = obj.getClass();     //获得该对对象的class实例
                        String customerId="";
                        Row dataRow = workbook.getSheet(sheetName).createRow(rowIndex);
                        for(int columnIndex = 3;columnIndex<titleColumn.length-11;columnIndex++){

                            //获取返回类型
                            String returnType = "string";
                            String data = null;
                            String title = titleColumn[columnIndex].toString().trim();
                            if(columnIndex==3&&(title==null || "".equals(title))){
                                Cell cell = dataRow.createCell(columnIndex-3);
                                cell.setCellValue((String)dataList.get("startAndEnd"));
                            }else {

                                if ( !"".equals(title) ) {  //字段不为空
                                    if ( obj instanceof Map ) {
                                        data = ( (Map) obj ).get(title) != null ? ( (Map) obj ).get(title).toString() : "";
                                    } else {

                                        //使首字母大写
                                        String UTitle = Character.toUpperCase(title.charAt(0)) + title.substring(1 , title.length()); // 使其首字母大写;
                                        String methodName = "get" + UTitle;
                                        // 设置要执行的方法
                                        Method method = clsss.getDeclaredMethod(methodName);
                                        //获取返回类型
                                        returnType = method.getReturnType().getName();
                                        data = method.invoke(obj) == null ? "" : method.invoke(obj).toString();
                                        if("custacctid".equals(title)){
                                            customerId=data;
                                        }
                                    }

                                    Cell cell = dataRow.createCell(columnIndex-3);
                                    if ( data != null && !"".equals(data) ) {
                                        if ( "java.lang.int".equals(returnType) ) {
                                            cell.setCellValue(Integer.parseInt(data));
                                        } else if ( "java.lang.long".equals(returnType) ) {
                                            cell.setCellValue(Long.parseLong(data));
                                        } else if ( "java.lang.float".equals(returnType) ) {
                                            cell.setCellValue(floatDecimalFormat.format(Float.parseFloat(data)));
                                        } else if ( "java.lang.double".equals(returnType) ) {
                                            cell.setCellValue(doubleDecimalFormat.format(Double.parseDouble(data)));
                                        } else {
                                            cell.setCellValue(data);
                                        }
                                    }
                                } else {   //字段为空 检查该列是否是公式
                                    if ( colFormula != null ) {
                                        String sixBuf = colFormula[ columnIndex ].replace("@" , ( rowIndex + 1 ) + "");
                                        Cell cell = dataRow.createCell(columnIndex-3);
                                        cell.setCellFormula(sixBuf.toString());
                                    }
                                }
                            }
                        }
                        sheet.setColumnWidth(7, titleSize[0]*256);    //设置宽度
                        Cell cell = dataRow.createCell(7);
                        //cell.setCellStyle(titleStyle);
                        sheet.setColumnWidth(8 , titleSize[ 1 ] * 256);    //设置宽度
                        Cell cellTwo = dataRow.createCell(8);
                        //cellTwo.setCellStyle(titleStyle);
                        if(map!=null) {
                            if ( null != map.get(customerId) && map.get(customerId).size()>0 ) {
                                cell.setCellValue(map.get(customerId).get(0));
                            }
                            if ( null != map.get(customerId) && map.get(customerId).size()>1 ) {
                                cellTwo.setCellValue(map.get(customerId).get(1));
                            }
                        }
                    }

                }
            }
            int row=totaletcCustomerDailyBills.size()+2;
            //写入签约机构excel的表头
            Row titleContarctRow = workbook.getSheet(sheetName).createRow(row++);
            sheet.setColumnWidth(0, titleSize[0]*256);    //设置宽度
            Cell cellContarctOne = titleContarctRow.createCell(0);
            cellContarctOne.setCellStyle(titleStyle);
            cellContarctOne.setCellValue("记账日期");
            for(int i = 13;i < titleName.length;i++){
                sheet.setColumnWidth(i-12, titleSize[i]*256);    //设置宽度
                Cell cell = titleContarctRow.createCell(i-12);
                cell.setCellStyle(titleStyle);
                cell.setCellValue(titleName[i].toString());
            }
            //通过反射获取数据并写入到excel中
            if(contractinTotals!=null&&contractinTotals.size()>0) {
                //设置样式
                HSSFCellStyle dataStyle = workbook.createCellStyle();
                titleStyle = (HSSFCellStyle) setFontAndBorder(titleStyle , contentFontType , (short) contentFontSize);

                if ( titleColumn.length > 0 ) {
                    for (int rowIndex = 0; rowIndex < contractinTotals.size() ; rowIndex++) {
                        Object obj = contractinTotals.get(rowIndex);     //获得该对象
                        Class clsss = obj.getClass();     //获得该对对象的class实例
                        String customerId = "";
                        Row dataRow = workbook.getSheet(sheetName).createRow(row++);
                        for (int columnIndex = 12; columnIndex < titleColumn.length; columnIndex++) {

                            //获取返回类型
                            String returnType = "string";
                            String data = null;
                            String title = titleColumn[ columnIndex ].toString().trim();
                            if ( columnIndex == 12 && ( title == null || "".equals(title) ) ) {
                                Cell cell = dataRow.createCell(columnIndex - 12);
                                cell.setCellValue((String) dataList.get("startAndEnd"));
                            } else {

                                if ( !"".equals(title) ) {  //字段不为空
                                    if ( obj instanceof Map ) {
                                        data = ( (Map) obj ).get(title) != null ? ( (Map) obj ).get(title).toString() : "";
                                    } else {

                                        //使首字母大写
                                        String UTitle = Character.toUpperCase(title.charAt(0)) + title.substring(1 , title.length()); // 使其首字母大写;
                                        String methodName = "get" + UTitle;
                                        // 设置要执行的方法
                                        Method method = clsss.getDeclaredMethod(methodName);
                                        //获取返回类型
                                        returnType = method.getReturnType().getName();
                                        data = method.invoke(obj) == null ? "" : method.invoke(obj).toString();

                                    }

                                    Cell cell = dataRow.createCell(columnIndex - 12);
                                    if ( data != null && !"".equals(data) ) {
                                        if ( "java.lang.int".equals(returnType)|| "int".equals(returnType) ) {
                                            cell.setCellValue(Integer.parseInt(data));
                                        } else if ( "java.lang.long".equals(returnType) || "long".equals(returnType)) {
                                            cell.setCellValue(Long.parseLong(data));
                                        } else if ( "java.lang.float".equals(returnType) || "float".equals(returnType)) {
                                            cell.setCellValue(floatDecimalFormat.format(Float.parseFloat(data)));
                                        } else if ( "java.lang.double".equals(returnType)|| "double".equals(returnType) ) {
                                            cell.setCellValue(doubleDecimalFormat.format(Double.parseDouble(data)));
                                        } else {
                                            cell.setCellValue(data);
                                        }
                                    }
                                } else {   //字段为空 检查该列是否是公式
                                    if ( colFormula != null ) {
                                        String sixBuf = colFormula[ columnIndex ].replace("@" , ( rowIndex + 1 ) + "");
                                        Cell cell = dataRow.createCell(columnIndex - 3);
                                        cell.setCellFormula(sixBuf.toString());
                                    }
                                }
                            }
                        }
                    }
                }
            }

            /**写入签约机构的表头*/
//            Row titlethirdRow = workbook.getSheet(sheetName).createRow(totaletcCustomerDailyBills.size()+2);
//
//            int row=totaletcCustomerDailyBills.size()+3;
//            for(int i = 0;i < 2;i++){
//                sheet.setColumnWidth(i, titleSize[i]*256);    //设置宽度
//                Cell cell = titlethirdRow.createCell(i);
//                cell.setCellStyle(titleStyle);
//                cell.setCellValue(titleName[i+12].toString());
//            }
//
//            //为表头添加自动筛选
//            if(!"".equals(address)){
//                CellRangeAddress c = (CellRangeAddress) CellRangeAddress.valueOf(address);
//                sheet.setAutoFilter(c);
//            }
//            Map<String,Map<String,Float>> mapthird=(Map<String,Map<String,Float>>)dataList.get("contractingAgencyMap");
//            for(Map.Entry<String,Map<String,Float>> entry: mapthird.entrySet()){
//
//                if(entry.getKey()!=null) {
//                    if(entry.getValue()!=null){
//                        Row bodythirdRow = workbook.getSheet(sheetName).createRow(row);
//
//                        Map<String,Float>  mapinder=entry.getValue();
//                        if(entry.getValue()!=null) {
//                            for (Map.Entry<String, Float> entryinder : mapinder.entrySet()) {
//                                sheet.setColumnWidth(0, titleSize[0]*256);    //设置宽度
//                                Cell cell = bodythirdRow.createCell(0);
//                                cell.setCellStyle(titleStyle);
//                                sheet.setColumnWidth(1, titleSize[1]*256);    //设置宽度
//                                Cell cellTwo = bodythirdRow.createCell(1);
//                                cellTwo.setCellStyle(titleStyle);
//                                if(entryinder.getValue()!=null&& !"".equals(entryinder.getValue())&&!"0".equals(entryinder.getValue())&&0!=entryinder.getValue()) {
//                                    if ( entryinder.getKey() != null ) {
//                                        cell.setCellValue(entryinder.getKey());
//                                    } else {
//                                        cell.setCellValue(entry.getKey());
//                                    }
//                                    if ( entryinder.getValue() != null ) {
//                                        cellTwo.setCellValue(floatDecimalFormat.format(entryinder.getValue()));
//                                    }
//                                    row++;
//                                }
//                            }
//
//                        }
//                    }
//                }
//            }

            workbook.write(out);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if(out!=null){
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 自己做一个定制导出excel的报表工具方法
     * 参数为map集合map<Integer,List<Object>>
     *     当map中键值对:
     *          1.为奇数时认为是通过对象来创建报表
     *                  list的参数:
     *                      [0]:字段属性名称集合
     *                      [1]:excel中的列名集合
     *                      [2]:封装数据的对象集合
     *                      [3]:每一列的大小的数组
     *
     *
     *          2.为偶数时认为是通过数组来创建报表
     *                  list的参数:
     *                      [0]:列名数组
     *                      [1]:数据的二维数组
     *                      [2]:每一列的大小的数组
     *
     * @param map
     * return 向页面导出报表
     */

    public void wirteExportExcelCommonXssf(Map<Integer,List<Object>> map){
        //添加Worksheet（不添加sheet时生成的xls文件打开时会报错)
        Sheet sheet = xssfbook.createSheet(this.sheetName);
        //新建文件
        OutputStream out = null;
        try {
            if ( fileDir != null ) {
                //指定有文件路径
                out = new FileOutputStream(fileDir);
            } else {
                //否则，直接写到输出流中
                out = response.getOutputStream();
                fileName = fileName+ ".xlsx";
                response.setContentType("application/octet-streem");
                response.setHeader("Content-Disposition" , "attachment; filename="
                        + URLEncoder.encode(fileName , "UTF-8"));
            }
            int rowIndex = 0;//用来记录当前报表有多少行
            for(Map.Entry<Integer,List<Object>> entry:map.entrySet()) {


                    if ( entry.getKey() % 2 == 0 ) {
                        //该键值对为通过数组完成报表
                        //写入excel的表头
                        Row titleNameRow = xssfbook.getSheet(sheetName).createRow(rowIndex++);
                        //设置样式
                        CellStyle titleStyle =  xssfbook.createCellStyle();
                        titleStyle = (CellStyle) setFontAndBorder(titleStyle, titleFontType, (short) titleFontSize);
                        titleStyle = (CellStyle) setColor(titleStyle, titleBackColor, (short)10);
                        List<Object> listTemp = entry.getValue();
                        //获得列名数组
                        String[] titleName = (String[])listTemp.get(0);
                        //获得每一列的大小的数组
                        int[] titleSize=(int[])listTemp.get(2);
                        for(int i = 0;i < titleName.length;i++){
                            sheet.setColumnWidth(i, titleSize[i]*256);    //设置宽度
                            Cell cell = titleNameRow.createCell(i);
                            cell.setCellStyle(titleStyle);
                            cell.setCellValue(titleName[i].toString());

                        }
                        //为表头添加自动筛选
                        if(!"".equals(address)){
                            CellRangeAddress c = (CellRangeAddress) CellRangeAddress.valueOf(address);
                            sheet.setAutoFilter(c);
                        }
                        //获得封装数据的数组
                        Object[][] objects = (Object[][]) listTemp.get(1);
                        for(int i=0;i<objects.length;i++){
                            Row dataRow = xssfbook.getSheet(sheetName).createRow(rowIndex++);
                            for(int columnIndex=0;columnIndex<titleName.length;columnIndex++){
                                Cell cell = dataRow.createCell(columnIndex);
                                Object data =objects[i][columnIndex];
                                String returnType = data.getClass().getName();
                                if(data!=null&&!"".equals(data)){
                                    if("java.lang.int".equals(returnType)){
                                        cell.setCellValue((int)data);
                                    }else if("java.lang.long".equals(returnType)){
                                        cell.setCellValue((long)data);
                                    }else if("java.lang.float".equals(returnType)){
                                        cell.setCellValue(floatDecimalFormat.format((float)data));
                                    }else if("java.lang.double".equals(returnType)){
                                        cell.setCellValue(doubleDecimalFormat.format((double)data));
                                    }else{
                                        cell.setCellValue(data.toString());
                                    }
                                }

                            }
                        }
                    } else {
                        //该键值对为通过对象完成报表
                        //写入excel的表头
                        Row titleNameRow = xssfbook.getSheet(sheetName).createRow(rowIndex++);
                        //设置样式
                        CellStyle titleStyle = xssfbook.createCellStyle();
                        titleStyle = (CellStyle) setFontAndBorder(titleStyle, titleFontType, (short) titleFontSize);
                        titleStyle = (CellStyle) setColor(titleStyle, titleBackColor, (short)10);
                        List<Object> listTemp = (List<Object>)entry.getValue();
                        //获得列名数组
                        String[] titleName = (String[])listTemp.get(1);
                        //获得每一列的大小的数组
                        int[] titleSize =(int[])listTemp.get(3);
                        for(int i = 0;i < titleName.length;i++){
                            sheet.setColumnWidth(i, titleSize[i]*256);    //设置宽度
                            Cell cell = titleNameRow.createCell(i);
                            cell.setCellStyle(titleStyle);
                            cell.setCellValue(titleName[i].toString());
                        }
                        //为表头添加自动筛选
                        if(!"".equals(address)){
                            CellRangeAddress c = (CellRangeAddress) CellRangeAddress.valueOf(address);
                            sheet.setAutoFilter(c);
                        }
                        //获取对象那个集合
                        List<?> objectList = (List<?>)listTemp.get(2);
                        //获取对象的字段集合
                        String[] cloumnsList = (String[])listTemp.get(0);
                        if(cloumnsList.length>0) {
                            //设置样式
                        	CellStyle dataStyle = workbook.createCellStyle();
                            titleStyle = (CellStyle) setFontAndBorder(titleStyle, contentFontType, (short) contentFontSize);
                            //通过反射将对象的数据写入excel表中
                            for (Object obj : objectList) {
                                    Class clsss = obj.getClass();     //获得该对对象的class实例
                                    Row dataRow = xssfbook.getSheet(sheetName).createRow(rowIndex++);
                                    for(int columnIndex = 0;columnIndex<cloumnsList.length;columnIndex++){
                                        //获取返回类型
                                        String returnType = "string";
                                        String data = null;
                                        String title = cloumnsList[columnIndex].toString().trim();
                                        if(!"".equals(title)){  //字段不为空
                                            if(obj instanceof Map){
                                                data = ((Map) obj).get(title)!=null?((Map) obj).get(title).toString():"";
                                            }else{
                                                //使首字母大写
                                                String UTitle = Character.toUpperCase(title.charAt(0))+ title.substring(1, title.length()); // 使其首字母大写;
                                                String methodName  ="";
                                                methodName="get"+UTitle;
                                                // 设置要执行的方法
                                                Method method = clsss.getDeclaredMethod(methodName);
                                                //获取返回类型
                                                returnType = method.getReturnType().getName();
                                                data = method.invoke(obj)==null?"":method.invoke(obj).toString();
                                            }

                                            Cell cell = dataRow.createCell(columnIndex);
                                            if(data!=null&&!"".equals(data)){
                                                if("java.lang.int".equalsIgnoreCase(returnType)){
                                                    cell.setCellValue(Integer.parseInt(data));
                                                }else if("java.lang.long".equalsIgnoreCase(returnType)){
                                                    cell.setCellValue(Long.parseLong(data));
                                                }else if("java.lang.float".equalsIgnoreCase(returnType)){
                                                    cell.setCellValue(floatDecimalFormat.format(Float.parseFloat(data)));
                                                }else if("java.lang.double".equalsIgnoreCase(returnType)){
                                                    cell.setCellValue(doubleDecimalFormat.format(Double.parseDouble(data)));
                                                }else{
                                                    cell.setCellValue(data);
                                                }
                                            }
                                        }else{   //字段为空 检查该列是否是公式
                                            if(colFormula!=null){
                                                String sixBuf = colFormula[columnIndex].replace("@", (rowIndex+1)+"");
                                                Cell cell = dataRow.createCell(columnIndex);
                                                cell.setCellFormula(sixBuf.toString());
                                            }
                                        }
                                    }
                                }
                            }
                        }
                }
            xssfbook.write(out);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if ( out != null ) {
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
    
   

    
    /**
     * 自己做一个定制导出excel的报表工具方法
     * 参数为map集合map<Integer,List<Object>>
     *     当map中键值对:
     *          1.为奇数时认为是通过对象来创建报表
     *                  list的参数:
     *                      [0]:字段属性名称集合
     *                      [1]:excel中的列名集合
     *                      [2]:封装数据的对象集合
     *                      [3]:每一列的大小的数组
     *
     *
     *          2.为偶数时认为是通过数组来创建报表
     *                  list的参数:
     *                      [0]:列名数组
     *                      [1]:数据的二维数组
     *                      [2]:每一列的大小的数组
     *
     * @param map
     * return 向页面导出报表
     */

    public void wirteExportExcelCommon(Map<Integer,List<Object>> map){
        //添加Worksheet（不添加sheet时生成的xls文件打开时会报错)
        Sheet sheet = workbook.createSheet(this.sheetName);
        //新建文件
        OutputStream out = null;
        try {
            if ( fileDir != null ) {
                //指定有文件路径
                out = new FileOutputStream(fileDir);
            } else {
                //否则，直接写到输出流中
                out = response.getOutputStream();
                fileName = fileName+ ".xls";
                response.setContentType("application/x-msdownload");
                response.setHeader("Content-Disposition" , "attachment; filename="
                        + URLEncoder.encode(fileName , "UTF-8"));
            }
            int rowIndex = 0;//用来记录当前报表有多少行
            for(Map.Entry<Integer,List<Object>> entry:map.entrySet()) {


                    if ( entry.getKey() % 2 == 0 ) {
                        //该键值对为通过数组完成报表
                        //写入excel的表头
                        Row titleNameRow = workbook.getSheet(sheetName).createRow(rowIndex++);
                        //设置样式
                        HSSFCellStyle titleStyle = workbook.createCellStyle();
                        titleStyle = (HSSFCellStyle) setFontAndBorder(titleStyle, titleFontType, (short) titleFontSize);
                        titleStyle = (HSSFCellStyle) setColor(titleStyle, titleBackColor, (short)10);
                        List<Object> listTemp = entry.getValue();
                        //获得列名数组
                        String[] titleName = (String[])listTemp.get(0);
                        //获得每一列的大小的数组
                        int[] titleSize=(int[])listTemp.get(2);
                        for(int i = 0;i < titleName.length;i++){
                            sheet.setColumnWidth(i, titleSize[i]*256);    //设置宽度
                            Cell cell = titleNameRow.createCell(i);
                            cell.setCellStyle(titleStyle);
                            cell.setCellValue(titleName[i].toString());

                        }
                        //为表头添加自动筛选
                        if(!"".equals(address)){
                            CellRangeAddress c = (CellRangeAddress) CellRangeAddress.valueOf(address);
                            sheet.setAutoFilter(c);
                        }
                        //获得封装数据的数组
                        Object[][] objects = (Object[][]) listTemp.get(1);
                        for(int i=0;i<objects.length;i++){
                            Row dataRow = workbook.getSheet(sheetName).createRow(rowIndex++);
                            for(int columnIndex=0;columnIndex<titleName.length;columnIndex++){
                                Cell cell = dataRow.createCell(columnIndex);
                                Object data =objects[i][columnIndex];
                                String returnType = data.getClass().getName();
                                if(data!=null&&!"".equals(data)){
                                    if("java.lang.int".equals(returnType)){
                                        cell.setCellValue((int)data);
                                    }else if("java.lang.long".equals(returnType)){
                                        cell.setCellValue((long)data);
                                    }else if("java.lang.float".equals(returnType)){
                                        cell.setCellValue(floatDecimalFormat.format((float)data));
                                    }else if("java.lang.double".equals(returnType)){
                                        cell.setCellValue(doubleDecimalFormat.format((double)data));
                                    }else{
                                        cell.setCellValue(data.toString());
                                    }
                                }

                            }
                        }
                    } else {
                        //该键值对为通过对象完成报表
                        //写入excel的表头
                        Row titleNameRow = workbook.getSheet(sheetName).createRow(rowIndex++);
                        //设置样式
                        HSSFCellStyle titleStyle = workbook.createCellStyle();
                        titleStyle = (HSSFCellStyle) setFontAndBorder(titleStyle, titleFontType, (short) titleFontSize);
                        titleStyle = (HSSFCellStyle) setColor(titleStyle, titleBackColor, (short)10);
                        List<Object> listTemp = (List<Object>)entry.getValue();
                        //获得列名数组
                        String[] titleName = (String[])listTemp.get(1);
                        //获得每一列的大小的数组
                        int[] titleSize =(int[])listTemp.get(3);
                        for(int i = 0;i < titleName.length;i++){
                            sheet.setColumnWidth(i, titleSize[i]*256);    //设置宽度
                            Cell cell = titleNameRow.createCell(i);
                            cell.setCellStyle(titleStyle);
                            cell.setCellValue(titleName[i].toString());
                        }
                        //为表头添加自动筛选
                        if(!"".equals(address)){
                            CellRangeAddress c = (CellRangeAddress) CellRangeAddress.valueOf(address);
                            sheet.setAutoFilter(c);
                        }
                        //获取对象那个集合
                        List<?> objectList = (List<?>)listTemp.get(2);
                        //获取对象的字段集合
                        String[] cloumnsList = (String[])listTemp.get(0);
                        if(cloumnsList.length>0) {
                            //设置样式
                            HSSFCellStyle dataStyle = workbook.createCellStyle();
                            titleStyle = (HSSFCellStyle) setFontAndBorder(titleStyle, contentFontType, (short) contentFontSize);
                            //通过反射将对象的数据写入excel表中
                            for (Object obj : objectList) {
                                    Class clsss = obj.getClass();     //获得该对对象的class实例
                                    Row dataRow = workbook.getSheet(sheetName).createRow(rowIndex++);
                                    for(int columnIndex = 0;columnIndex<cloumnsList.length;columnIndex++){
                                        //获取返回类型
                                        String returnType = "string";
                                        String data = null;
                                        String title = cloumnsList[columnIndex].toString().trim();
                                        if(!"".equals(title)){  //字段不为空
                                            if(obj instanceof Map){
                                                data = ((Map) obj).get(title)!=null?((Map) obj).get(title).toString():"";
                                            }else{
                                                //使首字母大写
                                                String UTitle = Character.toUpperCase(title.charAt(0))+ title.substring(1, title.length()); // 使其首字母大写;
                                                String methodName  ="";
                                                methodName="get"+UTitle;
                                                // 设置要执行的方法
                                                Method method = clsss.getDeclaredMethod(methodName);
                                                //获取返回类型
                                                returnType = method.getReturnType().getName();
                                                data = method.invoke(obj)==null?"":method.invoke(obj).toString();
                                            }

                                            Cell cell = dataRow.createCell(columnIndex);
                                            if(data!=null&&!"".equals(data)){
                                                if("java.lang.int".equalsIgnoreCase(returnType)){
                                                    cell.setCellValue(Integer.parseInt(data));
                                                }else if("java.lang.long".equalsIgnoreCase(returnType)){
                                                    cell.setCellValue(Long.parseLong(data));
                                                }else if("java.lang.float".equalsIgnoreCase(returnType)){
                                                    cell.setCellValue(floatDecimalFormat.format(Float.parseFloat(data)));
                                                }else if("java.lang.double".equalsIgnoreCase(returnType)){
                                                    cell.setCellValue(doubleDecimalFormat.format(Double.parseDouble(data)));
                                                }else{
                                                    cell.setCellValue(data);
                                                }
                                            }
                                        }else{   //字段为空 检查该列是否是公式
                                            if(colFormula!=null){
                                                String sixBuf = colFormula[columnIndex].replace("@", (rowIndex+1)+"");
                                                Cell cell = dataRow.createCell(columnIndex);
                                                cell.setCellFormula(sixBuf.toString());
                                            }
                                        }
                                    }
                                }
                            }
                        }
                }
            workbook.write(out);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if ( out != null ) {
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}
