package com.zz.amqp1.utils;

import freemarker.template.Configuration;
import freemarker.template.Template;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Description: FreeMarker的工具类
 * User: zhouzhou
 * Date: 2018-12-12
 * Time: 14:45
 */
public class FreeMarkerUtils {
    private Configuration cfg;

    public void init() throws Exception {
        // 初始化FreeMarker配置
        // 创建一个Configuration实例
        cfg = new Configuration();
        // 设置FreeMarker的模版文件位置
        cfg.setDirectoryForTemplateLoading(new File(
                "src\\main\\resources\\template"));
    }

    public void process(FreeMarkerUtils hf) throws Exception {

        Map root = new HashMap();

        String packageName = "com.zz.amqp1.dao";
        String basicClass = "Test";
        String modelName = "测试类";
        String basicOModel = "TestOModel";
        String basicIModel = "TestIModel";
        String author = "zhouzhou";
        Date date = new Date();

        root.put("packageName", packageName);
        root.put("basicClass", basicClass);
        root.put("modelName", modelName);
        root.put("basicOModel", basicOModel);
        root.put("basicIModel", basicIModel);
        root.put("author", author);
        root.put("date", date);

        String projectPath = "E:\\project\\StudyProject";

        String fileName = basicClass + "Dao.java";

        String savePath = "src\\main\\java\\com\\zz\\amqp1\\dao\\template\\";

        Template template = cfg.getTemplate("daotemplate.ftl");

        hf.buildTemplate(root, projectPath, savePath, fileName, template);

    }

    public void buildTemplate(Map root, String projectPath, String savePath,
                              String fileName, Template template) {
        String realFileName = savePath + fileName;

        String realSavePath = projectPath + "/" + savePath;

        File newsDir = new File(realSavePath);
        if (!newsDir.exists()) {
            newsDir.mkdirs();
        }

        try {
            // SYSTEM_ENCODING = "UTF-8";
            Writer out = new OutputStreamWriter(new FileOutputStream(realFileName), "utf-8");

            template.process(root, out);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) throws Exception {
        FreeMarkerUtils hf = new FreeMarkerUtils();
        hf.init();
        hf.process(hf);
    }

}
