package com.pojo.dto;

public class CrawlerApiResponseBean {

    private Boolean success;
    private Double errorCode;
    private String errorMessage;
    private Float queryTime;
    private Object result;
    private Object extraInfo;

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public Double getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(Double errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public Float getQueryTime() {
        return queryTime;
    }

    public void setQueryTime(Float queryTime) {
        this.queryTime = queryTime;
    }

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }

    public Object getExtraInfo() {
        return extraInfo;
    }

    public void setExtraInfo(Object extraInfo) {
        this.extraInfo = extraInfo;
    }
}
