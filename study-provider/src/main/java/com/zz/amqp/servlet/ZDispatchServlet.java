package com.zz.amqp.servlet;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.zz.amqp.annotation.ZAutowired;
import com.zz.amqp.annotation.ZController;
import com.zz.amqp.annotation.ZRequestMapping;
import com.zz.amqp.annotation.ZService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * Description:
 * User: zhouzhou
 * Date: 2018-12-24
 * Time: 17:14
 */

@WebServlet(urlPatterns = "/*", description = "这个是自定义的springmvc", loadOnStartup = 1,
        initParams = {@WebInitParam(name = "contextConfigLocation", value = "application.properties")})
public class ZDispatchServlet extends HttpServlet {
    private Logger logger = LoggerFactory.getLogger(ZDispatchServlet.class);

    private Properties contextConfig = new Properties();

    private List<String> classNames = Lists.newArrayList();

    private Map<String, Object> ioc = new HashMap<>();

    private Map<String, Method> handlerMapping = Maps.newHashMap();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        // 运行阶段执行逻辑
        try {
            doDispatch(req, resp);
        } catch (Exception e) {
            e.printStackTrace();
            resp.getWriter().write("500 :" + e.getMessage());
        }
    }

    private void doDispatch(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        if (CollectionUtils.isEmpty(handlerMapping)) {
            return;
        }

        String url = req.getRequestURI();
        String contextPath = req.getContextPath();
        url = url.replace(contextPath, "").replaceAll("/+", "/");

        if (handlerMapping.containsKey(url)) {
            resp.getWriter().write("404");
        }

        Method method = handlerMapping.get(url);
        Object o = this.ioc.get(toFirstCase(method.getDeclaringClass().getSimpleName()));
        method.invoke(o, req, resp, req.getParameter("name"));


    }

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);

        // 1. 加载配置文件
        doLocalConfig(config.getInitParameter("contextConfigLocation"));

        // 2. 初始化IOC容器, 扫描所有相关的类
        doScanner(contextConfig.getProperty("scanPackage"));

        // 3. 初始化所有的相关联的实例,  并且保存到IOC容器中去
        doInstance();

        // 4. 完成依赖注入,DI
        doAutowired();

        //5. 初始化handlerMapping
        initHandlerMapping();

        // 初始化完成
        logger.info("初始化MVC容器成功");
    }


    private void initHandlerMapping() {
        if (CollectionUtils.isEmpty(ioc)) return;

        ioc.forEach((key, value) -> {
            Class<?> clazz = value.getClass();
            if (clazz.isAnnotationPresent(ZController.class)) {
                // 开始进行handlermapping的构建
                String baseUrl = "";
                if (clazz.isAnnotationPresent(ZRequestMapping.class)) {
                    ZRequestMapping requestMapping = clazz.getAnnotation(ZRequestMapping.class);
                    String requestMappingBaseValue = requestMapping.value();
                    if (StringUtils.isNotEmpty(requestMappingBaseValue)) {
                        baseUrl += requestMappingBaseValue;
                    }

                    // 扫描下面的所有公共方法, 获取其requestMapping
                    Method[] methods = clazz.getMethods();
                    for (Method method : methods) {
                        if (method.isAnnotationPresent(ZRequestMapping.class)) {
                            ZRequestMapping annotation = method.getAnnotation(ZRequestMapping.class);
                            String value1 = annotation.value();
                            String url = baseUrl + "/" + value1;
                            String finalUrl = url.replaceAll("/+", "/");// 对url进行优化处理
                            handlerMapping.put(finalUrl, method);

                            System.out.println(String.format("mapped: {%s}, method:{%s}", finalUrl, method.getName()));
                        }
                    }
                }
            }
        });
    }

    private void doAutowired() {
        if (CollectionUtils.isEmpty(ioc)) return;
        ioc.forEach((key, value) -> {
            // 获取所有的字段
            Field[] declaredFields = value.getClass().getDeclaredFields();
            for (Field field : declaredFields) {
                // 如果字段用@Autowird修饰则进行注入
                if (field.isAnnotationPresent(ZAutowired.class)) {
                    ZAutowired autowired = field.getAnnotation(ZAutowired.class);
                    String beanName = autowired.value();
                    if (StringUtils.isEmpty(beanName)) {
                        beanName = field.getType().getSimpleName();
                        beanName = toFirstCase(beanName);
                    }
                    field.setAccessible(true); // 强制获取访问权
                    try {
                        field.set(value, ioc.get(beanName));
                    } catch (Exception e) {
                        throw new RuntimeException(String.format("设置值异常, 没有找到{%s}", beanName));
                    }

                }
            }
        });
    }

    private void doInstance() {
        if (CollectionUtils.isEmpty(classNames)) return;
        classNames.forEach(className -> {
                    try {
                        Class<?> clazz = Class.forName(className);
                        // 判断加了注解才实例化
                        if (clazz.isAnnotationPresent(ZController.class)) {
                            String beanName = toFirstCase(clazz.getSimpleName());
                            Object value = clazz.newInstance();
                            ioc.put(beanName, value);
                        } else if (clazz.isAnnotationPresent(ZService.class)) {
                            ZService zService = clazz.getAnnotation(ZService.class);
                            // 获取注解的值
                            String beanName = zService.value();
                            if (StringUtils.isEmpty(beanName)) {
                                beanName = toFirstCase(clazz.getSimpleName());
                            }
                            Object value = clazz.newInstance();
                            ioc.put(beanName, value);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
        );
    }

    private String toFirstCase(String simpleName) {
        char[] chars = simpleName.toCharArray();
        chars[0] += 32;
        return new String(chars);
    }

    private void doScanner(String scanPackage) {
        String s = scanPackage.replaceAll("\\.", "/");
        URL url = this.getClass().getClassLoader().getResource(s);
        File root = new File(url.getFile());
        File[] files = root.listFiles();
        for (File file : files) {
            if (file.isDirectory()) {
                // 递归扫描包
                doScanner(scanPackage + "." + file.getName());
            } else {
                if (!file.getName().endsWith(".class")) continue;
                String className = scanPackage + "." + file.getName().replace(".class", "");
                classNames.add(className);
            }
        }

    }

    private void doLocalConfig(String contextConfiguration) {
        InputStream resourceAsStream = this.getClass().getClassLoader().getResourceAsStream(contextConfiguration);
        try {
            contextConfig.load(resourceAsStream);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (null != resourceAsStream) {
                try {
                    resourceAsStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
