package com.service.stock;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.pojo.dto.CrawlerApiResponseBean;
import com.pojo.dto.DatabaseApiResponseBean;
import com.pojo.info.UsaStockPrice;
import com.pojo.info.UsaTradeLog;
import com.service.fetch.IoService;
import com.service.fetch.PostService;
import com.tool.ListObjectTool;
import com.tool.ObjectTool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

@Service
public class UsaStockService {

    private final Gson gson = new Gson();
    @Autowired
    private ObjectTool objectTool;
    @Autowired
    private ListObjectTool listObjectTool;
    @Autowired
    private IoService ioService;
    @Autowired
    private PostService postService;

    public CrawlerApiResponseBean crawlUsaPrice(Object parameter) {
        CrawlerApiResponseBean responseBean = objectTool.getErrorRep();
        try {
            Type type = new TypeToken<ArrayList<String>>() {}.getType();
            List<String> stockIdList = gson.fromJson(gson.toJson(parameter), type);
            List<UsaStockPrice> list = new ArrayList<>();
            for (String stockId : stockIdList) {
                list.addAll(listObjectTool.getUsaStockPriceList(stockId));
            }
            DatabaseApiResponseBean databaseApiResponseBean = postService.databaseApi("create_usa_stock_price_log", list);
            if (databaseApiResponseBean.getSuccess()) {
                responseBean = objectTool.getSuccessRep();
                responseBean.setResult("success");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return responseBean;
    }

    public CrawlerApiResponseBean crawlUsaTradeLog() {
        CrawlerApiResponseBean responseBean = objectTool.getErrorRep();
        try {
//            List<String> list = ioService.getDataFromFile("C:\\Users\\hcw\\Desktop\\tempStock\\temp.txt");
//            List<UsaTradeLog> reqList = listObjectTool.getUsaTradeLogList(list);
//            DatabaseApiResponseBean repBean = postService.databaseApi("create_usa_stock_trade_log", reqList);
//            if (repBean.getSuccess()) {
//                responseBean = objectTool.getSuccessRep();
//                responseBean.setResult("success");
//            }
            List<UsaTradeLog> reqList = listObjectTool.getUsaTradeLogList("C:\\Users\\hcw\\Desktop\\證券\\TD\\trade.xlsx", 0);
            DatabaseApiResponseBean repBean = postService.databaseApi("create_usa_stock_trade_log", reqList);
            if (repBean.getSuccess()) {
                responseBean = objectTool.getSuccessRep();
                responseBean.setResult("success");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return responseBean;
    }

}


