package com.controller;

import com.pojo.dto.CrawlerApiResponseBean;
import com.service.stock.TwStockService;
import com.service.stock.UsaStockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/stock")
public class StockController {

    @Autowired
    private UsaStockService usaStockService;
    @Autowired
    private TwStockService twStockService;

    @RequestMapping("/{action}")
    public String stock(@PathVariable("action") String action, HttpServletRequest request) {
        Object parameter = request.getAttribute("parameter");
        CrawlerApiResponseBean responseDTO = null;
        switch (action) {
            case "crawlUsaTradeLog": {
                responseDTO = usaStockService.crawlUsaTradeLog();
                break;
            }
            case "crawlUsaPrice": {
                responseDTO = usaStockService.crawlUsaPrice(parameter);
                break;
            }
            case "crawlTwTradeLog": {
                responseDTO = twStockService.crawlTwTradeLog();
                break;
            }
            case "crawlTwPrice": {
                responseDTO = twStockService.crawlTwPrice();
                break;
            }
        }
        request.setAttribute("response", responseDTO);
        return "forward:/crawlerApiResponse";
    }

}
