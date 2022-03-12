package com.service.fetch;

import com.pojo.dto.DatabaseApiRequestBean;
import com.pojo.dto.DatabaseApiResponseBean;
import com.pojo.prop.PropertiesBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

@Service
public class PostService {

    @Autowired
    private PropertiesBean propertiesBean;

    public DatabaseApiResponseBean databaseApi(String moduleName, Object parameter) {
        DatabaseApiRequestBean postBody = new DatabaseApiRequestBean();
        postBody.setModuleName(moduleName);
        postBody.setParameter(parameter);
        return postDatabaseApi(postBody);
    }

    private DatabaseApiResponseBean postDatabaseApi(DatabaseApiRequestBean postBody) {
        SimpleClientHttpRequestFactory clientHttpRequestFactory = new SimpleClientHttpRequestFactory();
        clientHttpRequestFactory.setConnectTimeout(propertiesBean.getTimeOut());
        clientHttpRequestFactory.setReadTimeout(propertiesBean.getTimeOut());
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setRequestFactory(clientHttpRequestFactory);
        ResponseEntity<DatabaseApiResponseBean> result = null;
        try {
            HttpEntity<DatabaseApiRequestBean> requestBody = new HttpEntity<>(postBody);
            result = restTemplate.postForEntity(propertiesBean.getDatabaseApi(), requestBody, DatabaseApiResponseBean.class);
        } catch (ResourceAccessException e1) {
            e1.printStackTrace();
        }
        return result != null ? result.getBody() : null;
    }

}
