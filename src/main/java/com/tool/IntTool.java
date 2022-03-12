package com.tool;

import org.springframework.stereotype.Service;

@Service
public class IntTool {

    // payment = 15,492(付) , return -15942
    // payment = 15,492(收) , return 15942
    public int transPayment(String payment) {
        String[] stringArray = payment.split("\\(");
        int absPayment = Integer.parseInt(stringArray[0].replace(",", ""));
        return stringArray[1].startsWith("付") ? -absPayment : absPayment;
    }

}
