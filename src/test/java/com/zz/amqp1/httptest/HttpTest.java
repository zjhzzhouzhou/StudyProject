package com.zz.amqp1.httptest;

import com.zz.amqp1.utils.ThreadPoolUtil;
import org.junit.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.TimeUnit;

/**
 * Description:
 * User: zhouzhou
 * Date: 2019-04-16
 * Time: 3:07 PM
 */

public class HttpTest {

    @Test
    public void testGet() throws Exception {
        for (int i = 0; i < 100; i++) {
            ThreadPoolUtil.execute(()-> System.out.println(sendGetRequest()));
            if (i % 10 == 0){
                TimeUnit.SECONDS.sleep(1);
            }
        }

        System.in.read();
    }

    /**
     * 发送 get 请求
     * @return
     */
    private String sendGetRequest(){

        String url = "http://localhost:8080/getToken";


        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
        headers.setContentType(type);
        headers.add("Accept", MediaType.APPLICATION_JSON.toString());

       // JSONObject jsonObj = JSONObject.parseObject(params);

        // HttpEntity<String> formEntity = new HttpEntity<String>(jsonObj.toString(), headers);

        String result = restTemplate.getForObject(url, String.class);

        return result;
    }
}


