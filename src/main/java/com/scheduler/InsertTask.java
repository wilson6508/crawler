package com.scheduler;

import com.pojo.dto.CrawlerApiResponseBean;
import com.pojo.dto.DatabaseApiResponseBean;
import com.service.fetch.PostService;
import com.service.stock.UsaStockService;
import com.tool.LocalDateTool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class InsertTask {

    @Autowired
    private UsaStockService usaStockService;
    @Autowired
    private LocalDateTool localDateTool;
    @Autowired
    private PostService postService;

    // 星期一到星期五21:10執行
    @Scheduled(cron = "0 10 21 ? * MON-FRI")
    public void dailyTask() {
        List<String> stockIdList = Arrays.asList("AMAT", "QQQM", "VTI", "SPY");
        // 抓取股價資訊
        CrawlerApiResponseBean crawl = usaStockService.crawlUsaPriceLog(stockIdList);
        if (crawl.getSuccess()) {
            // 打databaseApi
            DatabaseApiResponseBean insert = postService.databaseApi("stock_create_usa_price_log", crawl.getResult());
            if (insert.getSuccess()) {
                System.out.println("Crawl stock price of USA successfully. " + localDateTool.getCurrentTime("dayTw"));
                return;
            }
        }
        System.out.println("crawlUsaPrice failed " + localDateTool.getCurrentTime("dayTw"));
    }

}
