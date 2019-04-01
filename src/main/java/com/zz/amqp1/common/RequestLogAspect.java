package com.zz.amqp1.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Date;

/**
 * Description: 请求日志切面
 * User: zhouzhou
 * Date: 2019-03-28
 * Time: 9:00 PM
 */
@Aspect
@Component
public class RequestLogAspect {

    private static final Logger logger = LoggerFactory.getLogger(RequestLogAspect.class);

    private ThreadLocal<OperatorLog> threadLocal = new ThreadLocal<OperatorLog>();

    private static final int maxLength = 5000;

//    @Autowired
//    private OptLogService optLogService;

    @Pointcut("execution(public * com.zz.amqp1.controller..*.*(..))")
    public void webRequestLog() {
    }

    // @Order(5)
    @Before("webRequestLog()")
    public void doBefore(JoinPoint joinPoint) {
        try {

            long beginTime = System.currentTimeMillis();

            //获取操作系统等信息
            StringBuffer sb = new StringBuffer();
            sb.append("操作系统名称:" + System.getProperty("os.name"));//操作系统名称
            sb.append("操作系统构架" + System.getProperty("os.arch"));//操作系统构架
            sb.append("操作系统版本" + System.getProperty("os.version"));//操作系统版本

            // 接收到请求，记录请求内容
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            HttpServletRequest request = attributes.getRequest();
            String beanName = joinPoint.getSignature().getDeclaringTypeName();
            String methodName = joinPoint.getSignature().getName();
            String uri = request.getRequestURI();
            String remoteAddr = getIpAddr(request);
            String sessionId = request.getSession().getId();
            String user = (String) request.getSession().getAttribute("user");
            String httpMethod = request.getMethod();
            String params = "";
            String paramsAll = "";
//            if ("POST".equals(httpMethod)) {

            try {
                Object[] paramsArray = joinPoint.getArgs();
                ObjectMapper mapper = new ObjectMapper();
                params = mapper.writeValueAsString(paramsArray);
                paramsAll = params;
                if (!StringUtils.isEmpty(params) && params.length() > maxLength) {
                    params = params.substring(0, maxLength);
                    params = "[参数太长截取 " + maxLength + " 显示]" + params;
                }
            } catch (Exception e) {
                logger.warn("params转换失败，程序不需要处理: {}", joinPoint.getArgs());
            }

            logger.info("uri=" + uri + "; method=" + httpMethod + "; beanName=" + beanName + "; remoteAddr=" + remoteAddr + "; user=" + user
                    + "; methodName=" + methodName + "; params=" + params);

            OperatorLog optLog = new OperatorLog();
            optLog.setBeanName(beanName);
            optLog.setCurUser(user);
            optLog.setHttpMethod(httpMethod);
            optLog.setMethod(methodName);
            optLog.setParams("" + paramsAll);
            optLog.setRemoteAddr(remoteAddr);
            optLog.setSessionId(sessionId);
            optLog.setRequestUrl(uri);
            optLog.setRequestStartTime(beginTime);
            optLog.setCreateDate(new Date());
            optLog.setUserAgent(sb.toString());

            // 目标方法不为空
            if (!StringUtils.isEmpty(methodName)) {
                // set与get方法除外
                if (!(methodName.startsWith("set") || methodName.startsWith("get"))) {
                    Class targetClass = joinPoint.getTarget().getClass();
//                    Method method = targetClass.getMethod(methodName);
                    Method method = null;
                    Method[] methods = targetClass.getMethods();
                    if (methods != null) {
                        for (Method ele : methods) {
                            if (methodName.equals(ele.getName())) {
                                method = ele;
                                break;
                            }
                        }
                    }

                }
            }

            threadLocal.set(optLog);
        } catch (Exception e) {
            logger.error("***操作请求日志记录失败doBefore()***", e);
        }
    }


    // @Order(5)
    @AfterReturning(returning = "result", pointcut = "webRequestLog()")
    public void doAfterReturning(Object result) {
        try {
            // 处理完请求，返回内容
            OperatorLog optLog = threadLocal.get();
            optLog.setResult(result == null ? null : result.toString());
            long beginTime = optLog.getRequestStartTime();
            long endTime = System.currentTimeMillis();
            long requestTime = (endTime - beginTime);
            optLog.setRequestEndTime(endTime);
            optLog.setRequestTime(requestTime);

            String strResult = String.valueOf(optLog.getResult());
            if (!StringUtils.isEmpty(strResult) && strResult.length() > maxLength) {
                strResult = strResult.substring(0, maxLength);
                strResult = "[参数太长截取 " + maxLength + " 显示]" + strResult;
            }

            String params = optLog.getParams();
            if (!StringUtils.isEmpty(params) && params.length() > maxLength) {
                params = params.substring(0, maxLength);
                params = "[参数太长截取 " + maxLength + " 显示]" + params;
            }

            String log = "\n请求耗时：" + optLog.getRequestTime() + "ms  " + optLog.getRequestUrl() + " " + optLog.getHttpMethod() + "  **  " + optLog.getMethod() + "  **  " + params
                    + "\nRESPONSE : " + strResult;

            logger.info(log);
            logger.warn("TODO  还未保存API日志: {}", "★★★★★★★★★★★★★★★★★★★★★★★★");
        } catch (Exception e) {
            logger.error("***操作请求日志记录失败doAfterReturning()***", e);
        }
    }

    /**
     * 获取登录用户远程主机ip地址
     *
     * @param request
     * @return
     */
    private String getIpAddr(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }


    // 创建目录
    private static boolean createDir(String filePath) {
        File file = new File(filePath);
        if (file.exists()) {// 判断文件是否存在
            System.out.println("目标文件已存在" + filePath);
            return false;
        }
        if (filePath.endsWith(File.separator)) {// 判断文件是否为目录
            System.out.println("目标文件不能为目录！");
            return false;
        }
        if (!file.getParentFile().exists()) {// 判断目标文件所在的目录是否存在
            // 如果目标文件所在的文件夹不存在，则创建父文件夹
            System.out.println("目标文件所在目录不存在，准备创建它！");
            if (!file.getParentFile().mkdirs()) {// 判断创建目录是否成功
                System.out.println("创建目标文件所在的目录失败！");
                return false;
            }
        }
        try {
            if (file.createNewFile()) {// 创建目标文件
                System.out.println("创建文件成功:" + filePath);
                return true;
            } else {
                System.out.println("创建文件失败！");
                return false;
            }
        } catch (IOException e) {// 捕获异常
            e.printStackTrace();
            return false;
        }
    }
}