package com.controller;

import com.pojo.dto.CrawlerApiResponseBean;
import com.service.stock.TwStockService;
import com.service.stock.UsaStockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/stock")
public class StockController {

    @Autowired
    private TwStockService twStockService;
    @Autowired
    private UsaStockService usaStockService;

    @RequestMapping(value = "/crawlTwNowPrice", method = {RequestMethod.POST, RequestMethod.GET})
    public String crawlTwNowPrice(HttpServletRequest request) {
        CrawlerApiResponseBean responseDTO = twStockService.crawlTwNowPrice();
        request.setAttribute("response", responseDTO);
        return "forward:/crawlerApiResponse";
    }

    @RequestMapping(value = "/crawlTwTradeLog", method = {RequestMethod.POST, RequestMethod.GET})
    public String crawlTwTradeLog(HttpServletRequest request) {
        CrawlerApiResponseBean responseDTO = twStockService.crawlTwTradeLog();
        request.setAttribute("response", responseDTO);
        return "forward:/crawlerApiResponse";
    }

    @RequestMapping(value = "/crawlUsaTradeLog", method = {RequestMethod.POST, RequestMethod.GET})
    public String crawlUsaTradeLog(HttpServletRequest request) {
        CrawlerApiResponseBean responseDTO = usaStockService.crawlUsaTradeLog();
        request.setAttribute("response", responseDTO);
        return "forward:/crawlerApiResponse";
    }

}
