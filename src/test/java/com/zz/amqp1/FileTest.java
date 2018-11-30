package com.zz.amqp1;

import com.itextpdf.text.pdf.AcroFields;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Description:
 * User: zhouzhou
 * Date: 2018-09-28
 * Time: 15:54
 */
public class FileTest {

    public static final String PDF_1_FILEPATH = "pdf/银行开户单1.pdf";
    public static final String PDF_2_FILEPATH = "pdf/备案网首页Model_v1.0.1.pdf";

    @Test
    public void test01() throws Exception {
        Map<String,Object> map = new HashMap<>();
        map.put("1","123456");
        ByteArrayOutputStream os = null;
        PdfReader reader = null;
        PdfStamper ps = null;
        try {
            os = new ByteArrayOutputStream();
            reader = new PdfReader(PDF_2_FILEPATH);
            ps = new PdfStamper(reader, os);
            AcroFields acroFields = ps.getAcroFields();
            acroFields.addSubstitutionFont(getBaseFont());
            acroFields.setField("1","123456");
            ps.setFormFlattening(true);
        } catch (Exception e) {
           e.printStackTrace();
        } finally {
            try {
                ps.close();
                reader.close();
                os.close();
            } catch (Exception e){

            }
        }
        File file = new File("test.pdf");
        FileOutputStream fos = new FileOutputStream(file);
        fos.write(os.toByteArray());
        fos.flush();
        fos.close();
    }


    //添加中文支持
    public  BaseFont getBaseFont() throws Exception {
        BaseFont bf = BaseFont.createFont("C:/WINDOWS/Fonts/SIMSUN.TTC,1",
                BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
        return bf;
    }

}
