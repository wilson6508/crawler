package com.service.fetch;

import com.pojo.prop.PropertiesBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
public class GetService {

    @Autowired
    private PropertiesBean propertiesBean;

    // 網址不代參數
    public String withoutParameters(String url){
        SimpleClientHttpRequestFactory clientHttpRequestFactory = new SimpleClientHttpRequestFactory();
        clientHttpRequestFactory.setConnectTimeout(propertiesBean.getTimeOut());
        clientHttpRequestFactory.setReadTimeout(propertiesBean.getTimeOut());
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setRequestFactory(clientHttpRequestFactory);
        String result = null;
        try {
            result = restTemplate.getForObject(url, String.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    // 網址代參數
    public String withParameters(String url, Map<String, Object> params) {
        SimpleClientHttpRequestFactory clientHttpRequestFactory = new SimpleClientHttpRequestFactory();
        clientHttpRequestFactory.setConnectTimeout(propertiesBean.getTimeOut());
        clientHttpRequestFactory.setReadTimeout(propertiesBean.getTimeOut());
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setRequestFactory(clientHttpRequestFactory);
        String result = null;
        try {
            result = restTemplate.getForObject(url, String.class, params);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    // 偽裝成瀏覽器
    public String fakeExplorer(String url) {
        SimpleClientHttpRequestFactory clientHttpRequestFactory = new SimpleClientHttpRequestFactory();
        clientHttpRequestFactory.setConnectTimeout(propertiesBean.getTimeOut());
        clientHttpRequestFactory.setReadTimeout(propertiesBean.getTimeOut());
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setRequestFactory(clientHttpRequestFactory);
        HttpHeaders headers = new HttpHeaders();
        headers.add("User-Agent", "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/74.0.3729.169 Safari/537.36");
        ResponseEntity<String> result = null;
        try {
            HttpEntity<String> entity = new HttpEntity<>(headers);
            result = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
        } catch (ResourceAccessException e) {
            e.printStackTrace();
        }
        return result != null ? result.getBody() : null;
    }

}
