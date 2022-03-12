package com.service.stock;

import com.google.gson.Gson;
import com.pojo.dto.CrawlerApiResponseBean;
import com.pojo.dto.DatabaseApiResponseBean;
import com.pojo.info.TwTradeLog;
import com.pojo.prop.PropertiesBean;
import com.service.fetch.IoService;
import com.service.fetch.PostService;
import com.tool.ListObjectTool;
import com.tool.ObjectTool;
import com.tool.StringTool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TwStockService {

    @Autowired
    private Gson gson = new Gson();
    @Autowired
    private PropertiesBean propertiesBean;
    @Autowired
    private ListObjectTool listObjectTool;
    @Autowired
    private ObjectTool objectTool;
    @Autowired
    private StringTool stringTool;
    @Autowired
    private IoService ioService;
    @Autowired
    private PostService postService;

    public CrawlerApiResponseBean crawlTwNowPrice() {
        CrawlerApiResponseBean responseBean = objectTool.getErrorRep();
        try {
            String str = ioService.getDataFromExcel(8, propertiesBean.getFirstExcel());
            DatabaseApiResponseBean repBean = postService.databaseApi("update_tw_name_mapping", gson.fromJson(str, ArrayList.class));
            if (repBean.getSuccess()) {
                responseBean = objectTool.getSuccessRep();
                responseBean.setResult(true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return responseBean;
    }

    public CrawlerApiResponseBean crawlTwTradeLog() {
        CrawlerApiResponseBean responseBean = objectTool.getErrorRep();
        try {
            String skFile = String.format("C:\\Users\\hcw\\Desktop\\證券\\新光證券\\對帳單\\sk-%s.txt", stringTool.getLastMonth());
            List<String> skData =ioService.getDataFromFile(skFile);
            List<TwTradeLog> skList = listObjectTool.skStockLog(skData);
            List<TwTradeLog> reqList = new ArrayList<>(skList);

            String tsFile = String.format( "C:\\Users\\hcw\\Desktop\\證券\\台新證券\\對帳單\\ts-%s.txt", stringTool.getLastMonth());
            List<String> tsData =ioService.getDataFromFile(tsFile);
            List<TwTradeLog> tsList = listObjectTool.tsStockLog(tsData);
            reqList.addAll(tsList);

            DatabaseApiResponseBean repBean = postService.databaseApi("create_tw_stock_trade_log", reqList);
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
