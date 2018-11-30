package com.zz.amqp1.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

/**
 * Description: httpApiConfig, 用于restTemplate
 * User: zhouzhou
 * Date: 2018-09-14
 * Time: 13:25
 */
@Configuration
public class HttpApiConfig {
    @Bean
    public RestTemplate restTemplate(ClientHttpRequestFactory factory) {
        return new RestTemplate(factory);
    }

    @Bean
    public ClientHttpRequestFactory simpleClientHttpRequestFactory() {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        // 建立连接所用的时间
        factory.setReadTimeout(5000);//单位为ms
        // 建立连接后从服务器读取到可用资源所用的时间
        factory.setConnectTimeout(5000);//单位为ms
        return factory;
    }

}
