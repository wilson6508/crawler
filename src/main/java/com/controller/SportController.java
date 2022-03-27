package com.controller;

import com.pojo.dto.CrawlerApiResponseBean;
import com.service.sport.SportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
public class SportController {

    @Autowired
    private SportService sportService;

    @RequestMapping("/sport/{action}")
    public String sport(@PathVariable("action") String action, HttpServletRequest request) {
        Object parameter = request.getAttribute("parameter");
        CrawlerApiResponseBean responseDTO = null;
        switch (action) {
            case "crawlSpreads": {
                responseDTO = sportService.crawlSpreads(parameter);
                break;
            }
            case "crawlVueData": {
                responseDTO = sportService.crawlVueData();
                break;
            }
        }
        request.setAttribute("response", responseDTO);
        return "forward:/crawlerApiResponse";
    }

}
