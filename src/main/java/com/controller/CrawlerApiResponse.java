package com.controller;

import com.pojo.dto.CrawlerApiResponseBean;
import com.tool.ObjectTool;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

@RestController
public class CrawlerApiResponse {

    @Autowired
    private ObjectTool objectTool;

    @RequestMapping(value = "/crawlerApiResponse", method = {RequestMethod.POST, RequestMethod.GET})
    public ResponseEntity<Object> crawlerApiResponse(HttpServletRequest request) {
        CrawlerApiResponseBean responseDTO = (CrawlerApiResponseBean) request.getAttribute("response");
        if (responseDTO == null) {
            responseDTO = objectTool.getErrorRep();
        }
        long beginTime = (long) request.getAttribute("beginTime");
        float queryTime = (float) (System.currentTimeMillis() - beginTime) / 1000;
        responseDTO.setQueryTime(queryTime);
        return ResponseEntity.ok(responseDTO);
    }

    @RequestMapping(value = "/crawlerApiDownload", method = {RequestMethod.POST, RequestMethod.GET})
    public void crawlerApiDownload(HttpServletRequest request, HttpServletResponse response) {
        try {
            XSSFWorkbook xssfWorkbook = new XSSFWorkbook();
            InputStream inputStream = getClass().getResourceAsStream("/test0406.xlsx");
            if (inputStream != null) {
                xssfWorkbook = new XSSFWorkbook(inputStream);
            }
//            response.setCharacterEncoding("UTF-8");
//            response.setContentType("application/octet-stream;charset=UTF-8");
//            response.setHeader("Test-Apple", "nothing");
//            String fileName = URLEncoder.encode("test1111.xlsx", "utf-8");
//            response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
//            response.flushBuffer();
            OutputStream output = response.getOutputStream();
            xssfWorkbook.write(output);
            xssfWorkbook.close();
            output.flush();
            output.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
