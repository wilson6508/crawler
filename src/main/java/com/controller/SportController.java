package com.controller;

import com.pojo.dto.CrawlerApiResponseBean;
import com.service.sport.SportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/sport")
public class SportController {

    @Autowired
    private SportService sportService;

    @RequestMapping(value = "/crawlMainSpreads", method = {RequestMethod.POST, RequestMethod.GET})
    public String crawlMainSpreads(HttpServletRequest request) {
        Object parameter = request.getAttribute("request");
        CrawlerApiResponseBean responseDTO = sportService.crawlMainSpreads(parameter);
        request.setAttribute("response", responseDTO);
        return "forward:/crawlerApiResponse";
    }

    @RequestMapping(value = "/crawlVueData", method = {RequestMethod.POST, RequestMethod.GET})
    public String crawlVueData(HttpServletRequest request) {
        CrawlerApiResponseBean responseDTO = sportService.crawlVueData();
        request.setAttribute("response", responseDTO);
        return "forward:/crawlerApiResponse";
    }

}
