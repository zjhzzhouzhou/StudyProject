package com.zz.amqp.common.utils;

import org.springframework.util.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Description: 通用验证工具类
 * User: zhouzhou
 * Date: 2018-08-15
 * Time: 14:29
 */


public class ValidateUtil {

    //手机号
    public static String mobile = "^((13[0-9])|(14[0-9])|(15[0-9])|(16[0-9])|(17[0-9])|(18[0-9])|(19[0-9]))\\d{8}$";

    //不允许为空
    public static String blank = ".*[^ ].*";

    //邮件
    public static String email = "^([a-z0-9A-Z]+[-|_|\\.]?)+[a-z0-9A-Z]@([0-9a-z_\\-]*)(\\.(com|cn|inc|org|cc|edu|de)*){1,2}([a-z]{2})?$";

    //QQ，允许为空
    public static String tencentQQAllowNull = "((^$)|([1-9][0-9]{4,11}))";

    //QQ
    public static String tencentQQ = "[1-9][0-9]{4,11}";

    //网址，允许为空
    public static String urlAllowNull = "((^$)|(http|https)+://([\\w-]+\\.)+[\\w-]+(/[\\w- ./?%&=]*)?)";

    //网址
    public static String url = "(http|https)+://([\\w-]+\\.)+[\\w-]+(/[\\w- ./?%&=]*)?";

    //微信，允许为空
    public static String weixinAllowNull = "((^$)|(^[A-Za-z0-9]\\w{3,60}+$))";

    //微信
    public static String weixin = "^[A-Za-z0-9]\\w{3,60}+$";

    //正整数
    public static String PositiveInteger = "^[0-9]*[1-9][0-9]*$";

    //年份正则表达式
    public static String YearReg = "([0-9]{3}[1-9]|[0-9]{2}[1-9][0-9]{1}|[0-9]{1}[1-9][0-9]{2}|[1-9][0-9]{3})";

    //正整数或非负数
    public static String NonnegativeNumber = "^(\\+?[1-9][0-9]*$)|(([0-9]+\\.[0-9]*[0-9][0-9]*))";

    //不允许有任何空白
    public static String NoAnyEmpty = "^[\\S]{5,30}$";

    //日期
    public static String DateReg = "^$|^((((1[6-9]|[2-9]\\d)\\d{2})-(0?[13578]|1[02])-(0?[1-9]|[12]\\d|3[01]))|(((1[6-9]|[2-9]\\d)\\d{2})-(0?[13456789]|1[012])-(0?[1-9]|[12]\\d|30))|(((1[6-9]|[2-9]\\d)\\d{2})-0?2-(0?[1-9]|1\\d|2[0-8]))|(((1[6-9]|[2-9]\\d)(0[48]|[2468][048]|[13579][26])|((16|[2468][048]|[3579][26])00))-0?2-29-))$";

    // 数字和英文组成的字符串
    public static String numOrEng = "^[A-Za-z0-9]+$";

    //是否是Double数字类型
    public static boolean isDouble(String value) {
        if (value == null) {
            return false;
        }
        Pattern pattern = Pattern.compile("[0-9]*(\\.?)[0-9]*");
        return pattern.matcher(value).matches();
    }

    //是否是Long类型
    public static boolean isValidLong(String str) {
        try {
            Long.parseLong(str);
            return true;
        } catch (NumberFormatException e) {
        }
        return false;
    }

    //是否是Int类型
    public static boolean isValidInt(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e) {
        }
        return false;
    }

    public static boolean isMobile(String str) {
        return validata(str, mobile);
    }

    public static boolean blank(String str) {
        return validata(str, blank);
    }

    /****
     * 不为空，限制长度范围
     * @param str
     * @param start
     * @param end
     * @return
     */
    public static boolean blankforlenth(String str, int start, int end) {
        String reg = "\\S{" + start + "," + end + "}";
        return validata(str, reg);
    }

    /***
     * 非负数
     * @param str
     * @return
     */
    public static boolean nonnegativeNumber(String str) {
        return validata(str, NonnegativeNumber);
    }

    /****
     * 不允许有任何空白
     * @param str
     * @return
     */
    public static boolean noAnyEmpty(String str) {
        return validata(str, NoAnyEmpty);
    }

    /***
     * 日期判断
     * @param str
     * @return
     */
    public static boolean isDateReg(String str) {
        return validata(str, DateReg);
    }

    public static boolean isEmail(String str) {
        return validata(str, email);
    }

    public static boolean tencentQQAllNull(String str) {
        return validata(str, tencentQQAllowNull);
    }

    public static boolean tencentQQ(String str) {
        return validata(str, tencentQQ);
    }

    public static boolean webURLAllowNull(String str) {
        return validata(str, urlAllowNull);
    }

    public static boolean webURL(String str) {
        return validata(str, url);
    }

    public static boolean weixinAllowNull(String str) {
        return validata(str, weixinAllowNull);
    }

    public static boolean weixin(String str) {
        return validata(str, weixin);
    }

    public static boolean positiveInteger(String str) {
        return validata(str, PositiveInteger);
    }

    public static boolean isyear(String str) {
        return validata(str, YearReg);
    }

    public static boolean isEnglishOrNum(String str) {
        return validata(str,numOrEng);
    }

    public static boolean validata(String str, String type) {
        if (StringUtils.isEmpty(str)) {
            return false;
        }
        Pattern p = Pattern.compile(type);

        Matcher m = p.matcher(str);
        return m.matches();
    }

    public static void main(String[] args) {
        String xxx = "xxxxx";
        System.out.println(blankforlenth(xxx, 5, 10));
    }

}
