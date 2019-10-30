package com.zz.amqp1.utils;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.InputStream;

/**
 * Description: excel工具类
 * User: zhouzhou
 * Date: 2018-10-24
 * Time: 15:26
 */
public class ExcelImportUtil {

    /**
     * 验证EXCEL文件
     * @param filePath 文件名
     * @return 是/否
     */
    public static boolean validateExcel(String filePath){
        if (filePath == null || !(isExcel2003(filePath) || isExcel2007(filePath))){
            return false;
        }
        return true;
    }

    /**
     * 根据文件后缀，自适应上传文件的版本
     */
    public static Workbook getWorkbook(InputStream inStr, String filename) {
        Workbook workbook ;
        try {
            if (isExcel2003(filename)) {
                workbook = new HSSFWorkbook(inStr);//2003-
            } else if (isExcel2007(filename)) {
                workbook = new XSSFWorkbook(inStr);//2007+
            } else {
                throw new Exception(String.format("解析的文件格式有误,文件名为{%s}", filename));
            }

        } catch (Exception e) {
            throw new RuntimeException("解析的文件发生异常:" + e.getMessage());
        }
        return workbook;
    }
























    // @描述：是否是2003的excel，返回true是2003
    public static boolean isExcel2003(String filePath) {
        return filePath.matches("^.+\\.(?i)(xls)$");
    }

    //@描述：是否是2007的excel，返回true是2007
    public static boolean isExcel2007(String filePath) {
        return filePath.matches("^.+\\.(?i)(xlsx)$");
    }

    public static void main(String[] args) {
        System.out.println(validateExcel("fasdasdf.xlsx"));
    }
}
