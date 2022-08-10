package com.pojo.bean.stock;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UsaTradeLog {
    private String tradeDate;
    private String stockId;
    private int quantity;
    private double amount;
}
