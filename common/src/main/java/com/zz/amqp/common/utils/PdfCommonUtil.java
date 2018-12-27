package com.zz.amqp.common.utils;

import com.itextpdf.text.Document;
import com.itextpdf.text.pdf.AcroFields;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfImportedPage;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfSmartCopy;
import com.itextpdf.text.pdf.PdfStamper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;

/**
 * 由于pdf通用工具v1.0.1
 * <p>
 * <p>
 * Created by zhouzhou on 2018-10-30 11:31:56.
 */
public class PdfCommonUtil {
    private static Logger logger = LoggerFactory.getLogger(PdfCommonUtil.class);


    public static ByteArrayOutputStream getPdfOS(String pdfTemplate, Map<String, Object> pararms) {
        ByteArrayOutputStream os = null;
        PdfReader reader = null;
        PdfStamper ps = null;
        try {
            os = new ByteArrayOutputStream();
            reader = new PdfReader(pdfTemplate);
            ps = new PdfStamper(reader, os);
            AcroFields acroFields = ps.getAcroFields();
            wirteText(acroFields, pararms);
            ps.setFormFlattening(true);
        } catch (Exception e) {
            logger.error("pdf生成发生异常", e);
        } finally {
            try {
                os.close();
                reader.close();
                ps.close();
            } catch (Exception e){

            }
        }
        return os;
    }

    /**
     * 向字段里写入数据
     *
     * @param acroFields 字段
     * @param param      map集合
     * @throws Exception IOexception
     */
    public static void wirteText(AcroFields acroFields, Map<String, Object> param) throws Exception {
        // 添加中文支持
        acroFields.addSubstitutionFont(getBaseFont());
        for (Map.Entry entry : param.entrySet()) {
            acroFields.setField((String)entry.getKey(), entry.getValue().toString());
        }
    }

    public static void wirteText(AcroFields s, String name, String value,
                                 BaseFont bf) throws Exception {
        // 开始写入文本
        if (StringUtils.isEmpty(name)) {
            return;
        }
        s.setFieldProperty(name, "textfont", bf, null);
        s.setField(name, value);
    }

    public static void wirteTextNoFont(AcroFields s, String name, String value)
            throws Exception {
        if (StringUtils.isEmpty(name)) {
            return;
        }
        // 开始写入文本
        s.setField(name, value);
    }

    public static String getStringWithOutNull(String value1, String value2) {
        String valu = "";
        if (!StringUtils.isEmpty(value1)) {
            valu = value1;
        }
        if (!StringUtils.isEmpty(value2)) {
            valu = valu + "" + value2;
        }
        return valu;
    }

    public static String formattingNubber(BigDecimal value, DecimalFormat df) {
        if (value == null) {
            return null;
        }
        return df.format(value.doubleValue());
    }

    /**
     * 将多张pdf合并一张
     * 确保每个pdf只有1页，否则我也没试过
     */
    public static void mergePdfFiles(List<ByteArrayOutputStream> osList, OutputStream os) {
        try {
            Document document = new Document(new PdfReader(osList.get(0)
                    .toByteArray()).getPageSize(1));
            PdfSmartCopy copy = new PdfSmartCopy(document, os);//PdfSmartCopy 占用内存，且运行慢，但是合并的pdf会优化大小 PdfCopy 运行快但是生成的pdf大，臃肿
            document.open();
            for (int i = 0; i < osList.size(); i++) {
                PdfReader reader = new PdfReader(osList.get(i).toByteArray());
                int n = reader.getNumberOfPages();
                for (int j = 1; j <= n; j++) {
                    document.newPage();
                    PdfImportedPage page = copy.getImportedPage(reader, j);
                    copy.addPage(page);
                }
            }
            document.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //添加中文支持
    public static BaseFont getBaseFont() throws Exception {
        BaseFont bf = BaseFont.createFont("C:/WINDOWS/Fonts/SIMSUN.TTC,1",
                BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
        return bf;
    }
}
