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

    // 星期二到星期六20:55執行
    @Scheduled(cron = "0 10 21 * * MON-FRI")
    public void dailyTask() {
        List<String> stockIdList = Arrays.asList("AMAT", "QQQM", "VTI", "SPY");
        CrawlerApiResponseBean crawl = usaStockService.crawlUsaPriceLog(stockIdList);
        if (crawl.getSuccess()) {
            DatabaseApiResponseBean insert = postService.databaseApi("stock_create_usa_price_log", crawl.getResult());
            if (insert.getSuccess()) {
                System.out.println("Crawl stock price of USA successfully. " + localDateTool.getCurrentTime("dayTw"));
                return;
            }
        }
        System.out.println("crawlUsaPrice failed " + localDateTool.getCurrentTime("dayTw"));
    }

//    每個月1號20:30執行
//    @Scheduled(cron = "0 30 20 1 * *")
//    public void monthlyTask() {
//        System.out.println("QQQ");
//    }

}
