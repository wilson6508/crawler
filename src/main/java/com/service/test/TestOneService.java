package com.service.test;

import com.pojo.dto.CrawlerApiResponseBean;
import com.tool.ObjectTool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TestOneService {

    @Autowired
    private ObjectTool objectTool;

    public CrawlerApiResponseBean testOne(Object parameter) {
        CrawlerApiResponseBean responseBean = objectTool.getErrorRep();
        try {
            System.out.println(parameter.toString());
            responseBean = objectTool.getSuccessRep();
            responseBean.setResult("KKK");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return responseBean;
    }

}
