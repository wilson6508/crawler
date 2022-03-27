package com.controller;

import com.pojo.dto.CrawlerApiRequestBean;
import com.tool.EnumTool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/crawlerApi")
public class CrawlerApiManager {

    @Autowired
    private EnumTool enumTool;

    @CrossOrigin("http://localhost:8080")
    @RequestMapping(value = "/normal", method = {RequestMethod.POST, RequestMethod.GET},
            produces = {"application/json;charset=UTF-8"}, consumes = "application/json")
    public String normal(@RequestBody CrawlerApiRequestBean requestDTO, HttpServletRequest request) {
        long beginTime = System.currentTimeMillis();
        EnumTool.ModuleType moduleType = enumTool.findModuleType(requestDTO.getModuleName());
        if (moduleType != null) {
            request.setAttribute("beginTime", beginTime);
            request.setAttribute("parameter", requestDTO.getParameter());
            return "forward:" + moduleType.getModulePath();
        }
        return "forward:/crawlerApiResponse";
    }

}
