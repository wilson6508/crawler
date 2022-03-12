package com.pojo.prop;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class PropertiesBean {

    @Value("${timeOut}")
    private int timeOut;

    @Value("${databaseApi}")
    private String databaseApi;

    @Value("${readExcelApi}")
    private String readExcelApi;

    @Value("${firstExcel}")
    private String firstExcel;

    @Value("${secondExcel}")
    private String secondExcel;

    @Value("${chartsUrl}")
    private String chartsUrl;

    @Value("${playSportUrl}")
    private String playSportUrl;

    public int getTimeOut() {
        return timeOut;
    }

    public void setTimeOut(int timeOut) {
        this.timeOut = timeOut;
    }

    public String getReadExcelApi() {
        return readExcelApi;
    }

    public String getDatabaseApi() {
        return databaseApi;
    }

    public void setDatabaseApi(String databaseApi) {
        this.databaseApi = databaseApi;
    }

    public void setReadExcelApi(String readExcelApi) {
        this.readExcelApi = readExcelApi;
    }

    public String getFirstExcel() {
        return firstExcel;
    }

    public void setFirstExcel(String firstExcel) {
        this.firstExcel = firstExcel;
    }

    public String getSecondExcel() {
        return secondExcel;
    }

    public void setSecondExcel(String secondExcel) {
        this.secondExcel = secondExcel;
    }

    public String getChartsUrl() {
        return chartsUrl;
    }

    public void setChartsUrl(String chartsUrl) {
        this.chartsUrl = chartsUrl;
    }

    public String getPlaySportUrl() {
        return playSportUrl;
    }

    public void setPlaySportUrl(String playSportUrl) {
        this.playSportUrl = playSportUrl;
    }

}
