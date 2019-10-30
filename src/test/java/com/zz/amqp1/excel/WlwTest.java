package com.zz.amqp1.excel;

import com.zz.amqp1.bean.WLWCard;
import com.zz.amqp1.utils.ExcelExportUtil;
import com.zz.amqp1.utils.ExcelImportUtil;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.junit.Test;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Description: 物联网test
 * User: zhouzhou
 * Date: 2019-10-28
 * Time: 9:51
 */
public class WlwTest {

    /**
     * 获取脏数据
     * @throws Exception
     */
    @Test
    public void test() throws Exception {

        String WLWFileName = "C:\\Users\\zhouzhou\\Desktop\\test\\WLW.xlsx";
        String CMPFileName = "C:\\Users\\zhouzhou\\Desktop\\test\\CMP.xlsx";

        InputStream isWLW = new FileInputStream(WLWFileName);
        InputStream isCMP = new FileInputStream(CMPFileName);

        Workbook wlwBook = ExcelImportUtil.getWorkbook(isWLW, WLWFileName);
        Workbook cmpBook = ExcelImportUtil.getWorkbook(isCMP, CMPFileName);

        Set<String> wlwCardIds = getCardIds(wlwBook);
        Set<String> cmpCardIds = getCardIds(cmpBook);

        System.out.println("物联网导出数据个数为:" + wlwCardIds.size());
        System.out.println("CMP导出数据个数为:" + cmpCardIds.size());

        System.out.println("----------------- 开始搜索物联网有cmp没有的数据 -------------------");

        List<WLWCard> wlwOwnCards = new ArrayList<>();
        wlwCardIds.forEach(id -> {
            if (!cmpCardIds.contains(id)) {
                wlwOwnCards.add(new WLWCard(id));
            }
        });
        System.out.println(String.format("物联网有cmp没有的数据共:{%s}条", wlwOwnCards.size()));

        System.out.println("----------------- 开始搜索cmp有物联网没有的数据 -------------------");
        List<WLWCard> cmpOwnCards = new ArrayList<>();
        cmpCardIds.forEach(id -> {
            if (!wlwCardIds.contains(id)) {
                cmpOwnCards.add(new WLWCard(id));
            }
        });
        System.out.println(String.format("cmp有物联网没有的数据共:{%s}条", cmpOwnCards.size()));


        System.out.println("开始导出物联网有cmp没有的数据");
        writeExcel("物联网有cmp没有的数据.xlsx", wlwOwnCards);
        System.out.println("导出完毕");

        System.out.println("开始导出cmp有物联网没有的数据");
        writeExcel("cmp有物联网没有的数据.xlsx", cmpOwnCards);
        System.out.println("导出完毕");

    }


    /**
     * 获取已经卖掉的
     */
    @Test
    public void testGetSold() throws Exception{
        String basePath = "C:\\Users\\zhouzhou\\Desktop\\test\\sell\\";
        String storeFileName = basePath + "store.xlsx";
        String allFileName = basePath + "all.xlsx";

        InputStream storeIs = new FileInputStream(storeFileName);
        InputStream allIs = new FileInputStream(allFileName);

        Workbook storeWorkbook = ExcelImportUtil.getWorkbook(storeIs, storeFileName);
        Workbook allWorkbook = ExcelImportUtil.getWorkbook(allIs, allFileName);

        Set<String> storeCardIds = getCardIds(storeWorkbook);
        Set<String> allCardIds = getCardIds(allWorkbook);

        System.out.println("库存数量:" + storeCardIds.size());
        System.out.println("全部数量:" + allCardIds.size());

        allCardIds.removeAll(storeCardIds);

        System.out.println("算差集后剩余量为:" + allCardIds.size());

        System.out.println("------------------->导出excel");
        List<WLWCard> wlwCards = new ArrayList<>();
        allCardIds.forEach(id ->{
            wlwCards.add(new WLWCard(id));
        });

        // 由于数量超过65536个, 分为两个excel文件提出
        List<WLWCard> wlwCards1 = new ArrayList<>();
        List<WLWCard> wlwCards2 = new ArrayList<>();

        for (int i = 0; i < 60000; i++) {
            wlwCards1.add(wlwCards.get(i));
        }

        for (int i = 60000; i < wlwCards.size(); i++) {
            wlwCards2.add(wlwCards.get(i));
        }

        writeExcel("已经卖掉的卡号-1.xlsx", wlwCards1);
        writeExcel("已经卖掉的卡号-2.xlsx", wlwCards2);

        System.out.println("<-------------------导出excel完毕");

    }


    /**
     * 造数据
     */
    @Test
    public void testCreateCards() {
        String prefix = "898604391118804";
        List<WLWCard> wlwCards = new ArrayList<>();
        for (int i = 80000; i <=  99999; i++) {
            String str = String.format("%05d", i);
            wlwCards.add(new WLWCard(prefix + str));
        }
        writeExcel("老 80000-99999.xlsx", wlwCards);
        System.out.println("导出完毕");
    }

    public void writeExcel(String fileName, List<WLWCard> list) {
        String[] cloumnsName = {"卡号"};
        String[] pojoCloumns = {"cardId"};
        int[] sizeList = {30};
        String filePath = "C:\\Users\\zhouzhou\\Desktop\\test\\data\\";
        ExcelExportUtil exportUtil = new ExcelExportUtil(filePath + fileName, "不一样的表");

//        WLWCard wlwCard = new WLWCard("123456-1");
//        WLWCard wlwCard1 = new WLWCard("123456-2");
//        WLWCard wlwCard2 = new WLWCard("123456-3");
//
//        List<WLWCard> list = new ArrayList<>();
//        list.add(wlwCard);
//        list.add(wlwCard1);
//        list.add(wlwCard2);
        exportUtil.wirteExcel(pojoCloumns, cloumnsName, sizeList, list);
    }

    private Set<String> getCardIds(Workbook workbook) {
        Set<String> set = new HashSet<>();
        // 获取第一份工作簿
        Sheet sheet = workbook.getSheetAt(0);
        //得到Excel的行数
        int totalRows = sheet.getPhysicalNumberOfRows();
        //循环Excel行数,从第二行开始。标题不入库
        for (int r = 1; r < totalRows; r++) {
            Row row = sheet.getRow(r);
            // 因为导入只需要第1列所以只取第1列, 没有跳过
            if (row == null) {
                break; // 如果出现空行则直接停止遍历
            }
            Cell cell = row.getCell(0);
            if (null == cell) { // 如果出现空行则直接停止遍历
                break;
            }
            String cardNumber = cell.getStringCellValue();
            set.add(cardNumber);
        }
        return set;
    }
}
