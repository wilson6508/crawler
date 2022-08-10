package com.pojo.bean.stock;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UsaPrice {
    private String date;
    private String stockId;
    private double open;
    private double high;
    private double low;
    private double close;
    private String volume;
}
