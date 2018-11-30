package com.zz.amqp1.controller;

import com.zz.amqp1.bean.BankDto;
import com.zz.amqp1.utils.ExcelExportUtil;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Description:
 * User: zhouzhou
 * Date: 2018-10-25
 * Time: 19:44
 */
@RestController
public class ExcelController {
    /**
     * 将通用报表导出接口Demo
     *
     * @param response
     *            response
     * @param request
     *            request
     */
    @RequestMapping(

            produces = MediaType.APPLICATION_OCTET_STREAM_VALUE, path = "/excelDemo")
    public void excelExportDemo(HttpServletResponse response,
                                HttpServletRequest request) {
        ExcelExportUtil excelUtil = new ExcelExportUtil(response, "demo表",
                "demo表");

        Map<Integer, List<Object>> map = new HashMap<>();
        String[] titleName = { "第一列", "第二列", "第三列", "第四列" };
        String[][] dataarray = { { "第一1列", "第二1列", "第三1列", "第四1列" },
                { "第一2列", "第二2列", "第三2列", "第四2列" },
                { "第一3列", "第二3列", "第三3列", "第四3" } };
        int[] cloumnsSize = { 30, 30, 30, 30 };
        List<Object> list = new ArrayList<>();
        list.add(titleName);
        list.add(dataarray);
        list.add(cloumnsSize);
        String[] titleName1 = { "第一列", "第二列", "第三列" };
        String[][] dataarray1 = { { "第一1列", "第二1列", "第三1列" },
                { "第一2列", "第二2列", "第三2列" }, { "第一3列", "第二3列", "第三3列" } };
        int[] cloumnsSize1 = { 30, 30, 30 };
        List<Object> list1 = new ArrayList<>();
        list1.add(titleName1);
        list1.add(dataarray1);
        list1.add(cloumnsSize1);
        String[] titleName2 = { "第一列", "第二列", "第三列", "第四列", "第五列" };
        String[][] dataarray2 = { { "第一3列", "第二3列", "第三3列", "第四31列", "第五5列" },
                { "第一32列", "第二32列", "第三32列", "第四32列", "第五5列" },
                { "第一33列", "第二33列", "第三33列", "第四33", "第五5列" } };
        int[] cloumnsSize2 = { 30, 30, 30, 30, 30 };
        List<Object> list2 = new ArrayList<>();
        list2.add(titleName2);
        list2.add(dataarray2);
        list2.add(cloumnsSize2);
        String[] pojoCloumns = { "acctId", "nickName", "mobilePhone" };
        String[] cloumnsName = { "用户编号", "昵称", "电话号码" };
        List<BankDto> pojoList = new ArrayList<>();
        BankDto dto1 = new BankDto();
        dto1.setAcctId("123");
        dto1.setNickName("方鸿渐");
        dto1.setMobilePhone("123456");
        BankDto dto2 = new BankDto();
        dto2.setAcctId("124");
        dto2.setNickName("唐晓芙");
        dto2.setMobilePhone("123457");
        BankDto dto3 = new BankDto();
        dto3.setAcctId("125");
        dto3.setNickName("李梅亭");
        dto3.setMobilePhone("123458");
        pojoList.add(dto1);
        pojoList.add(dto2);
        pojoList.add(dto3);
        int[] sizeList = { 30, 30, 30 };
        List<Object> list3 = new ArrayList<>();
        list3.add(pojoCloumns);
        list3.add(cloumnsName);
        list3.add(pojoList);
        list3.add(sizeList);
        map.put(1, list3);
        map.put(6, list2);
        map.put(2, list);
        map.put(4, list1);
        excelUtil.wirteExportExcelCommon(map);
    }
}
