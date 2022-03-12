package com.tool;

import com.pojo.dto.CrawlerApiResponseBean;
import org.springframework.stereotype.Service;

@Service
public class ObjectTool {

    public CrawlerApiResponseBean getErrorRep() {
        CrawlerApiResponseBean responseBean = new CrawlerApiResponseBean();
        responseBean.setSuccess(false);
        responseBean.setErrorCode(7.0);
        responseBean.setErrorMessage("error");
        return responseBean;
    }

    public CrawlerApiResponseBean getSuccessRep() {
        CrawlerApiResponseBean responseBean = new CrawlerApiResponseBean();
        responseBean.setSuccess(true);
        responseBean.setErrorCode(0.0);
        responseBean.setErrorMessage("success");
        return responseBean;
    }

}
