package com.kream.root.order;

import lombok.Data;
import java.util.List;

@Data
public class PaymentInfo {
    private String applyNum;
    private String bankName;
    private String buyerAddr;
    private String buyerEmail;
    private String buyerName;
    private String buyerPostcode;
    private String buyerTel;
    private String cardName;
    private String cardNumber;
    private int cardQuota;
    private String currency;
    private String customData;
    private String impUid;
    private String merchantUid;
    private String productName;
    private double paidAmount;
    private long paidAt;
    private String payMethod;
    private String pgProvider;
    private String pgTid;
    private String pgType;
    private String receiptUrl;
    private String status;
    private boolean success;
    private int userId;
    private List<Long> productIds;  // 추가된 필드
    private List<Integer> quantities;  // 추가된 필드
}
