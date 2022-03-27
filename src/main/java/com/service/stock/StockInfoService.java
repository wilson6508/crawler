//package com.service.stock;
//
//import com.google.gson.Gson;
//import com.pojo.bean.StockInfoReqBean;
//import com.pojo.dto.CrawlerApiResponseBean;
//import com.pojo.info.UsaStockPrice;
//import com.pojo.prop.PropertiesBean;
//import com.service.fetch.IoService;
//import com.tool.ListObjectTool;
//import com.tool.ObjectTool;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//
//@Service
//public class StockInfoService {
//
//    private final Gson gson = new Gson();
//    @Autowired
//    private ObjectTool objectTool;
//    @Autowired
//    private ListObjectTool listObjectTool;
//    @Autowired
//    private IoService ioService;
//    @Autowired
//    private PropertiesBean propertiesBean;
//
//    public CrawlerApiResponseBean createInfo(Object parameter) {
//        CrawlerApiResponseBean responseBean = objectTool.getErrorRep();
//        try {
//            responseBean = objectTool.getSuccessRep();
//            String temp = ioService.getDataFromExcel(5, propertiesBean.getFirstExcel());
//            responseBean.setResult(temp);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return responseBean;
//    }
//
//    public CrawlerApiResponseBean readInfo(Object parameter) {
//        CrawlerApiResponseBean responseBean = objectTool.getErrorRep();
//        try {
//            StockInfoReqBean reqBean = gson.fromJson(gson.toJson(parameter), StockInfoReqBean.class);
//            String date = reqBean.getDate();
//            String stockId = reqBean.getStockId();
//            List<UsaStockPrice> usaStockPriceList = listObjectTool.getUsaStockPriceList(stockId);
//            responseBean = objectTool.getSuccessRep();
//            responseBean.setResult(usaStockPriceList);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return responseBean;
//    }
//
//}
