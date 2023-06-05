package com.luv2code.springbootlibrary.requestmodels;

import lombok.Data;

@Data
public class PaymentInfoRequest {
    private int amount;
    private String currency;
    private String receiptEmail;
    private String description;
    private String name;
    private String address;

}
