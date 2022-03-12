package com.scheduler;

import com.pojo.dto.CrawlerApiResponseBean;
import com.service.stock.UsaStockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class InsertTask {

    @Autowired
    private UsaStockService usaStockService;

    // 星期二到星期六20:55執行
    @Scheduled(cron = "30 22 17 * * TUE-SAT")
    public void dailyTask() {
        List<String> stockIdList = Arrays.asList("SPY", "QQQ");
        CrawlerApiResponseBean responseBean = usaStockService.crawlUsaPrice(stockIdList);
        if (responseBean.getSuccess()) {
            System.out.println("dailyTask success");
        } else {
            System.out.println("dailyTask error");
        }
    }

    // 每個月1號20:30執行
    @Scheduled(cron = "0 30 20 1 * *")
    public void monthlyTask() {
        System.out.println("QQQ");
    }

}
