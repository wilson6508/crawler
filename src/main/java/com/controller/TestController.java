package com.controller;

import com.pojo.dto.CrawlerApiResponseBean;
import com.service.test.TestOneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
public class TestController {

    @Autowired
    private TestOneService testOneService;

    @RequestMapping("/test/{action}")
    public String test(@PathVariable("action") String action, HttpServletRequest request) {
        Object parameter = request.getAttribute("parameter");
        CrawlerApiResponseBean responseDTO = null;
        switch (action) {
            case "testOne": {
                responseDTO = testOneService.testOne(parameter);
                break;
            }
        }
        request.setAttribute("response", responseDTO);
        return "forward:/crawlerApiResponse";
    }

}
