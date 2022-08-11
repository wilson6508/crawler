package com.pojo.prop;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "crawler")
public class PropertiesBean {
    private int timeOut;
    private String databaseApi;
    private String readExcelApi;
    private String firstExcel;
    private String secondExcel;
    private String chartsUrl;
    private String playSportUrl;
}
