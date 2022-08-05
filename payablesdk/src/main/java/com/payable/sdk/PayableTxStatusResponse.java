package com.payable.sdk;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PayableTxStatusResponse extends PayableResponse {

    @Expose
    @SerializedName("cardHolder")
    public String cardName;

    @Expose
    public String ccLast4;

    @Expose
    public double amount;

    @Expose
    public int cardType;

    @Expose
    public String time;

    @Expose
    @SerializedName("merchantInvoiceId")
    public String orderTracking;

    @Expose
    public int txType;

    @Expose
    public int currencyType;

    @Expose
    public int installment;

    @Expose
    public String tid;

    @Expose
    public String mid;

    @Expose
    public String cardNo;

    @Override
    public String toString() {
        return "PayableTxStatusResponse{" +
                "status=" + status +
                ", txId='" + txId + '\'' +
                ", error='" + error + '\'' +
                ", cardName='" + cardName + '\'' +
                ", ccLast4='" + ccLast4 + '\'' +
                ", amount=" + amount +
                ", cardType=" + cardType +
                ", time='" + time + '\'' +
                ", orderTracking='" + orderTracking + '\'' +
                ", txType=" + txType +
                ", currencyType=" + currencyType +
                ", installment=" + installment +
                ", tid='" + tid + '\'' +
                ", mid='" + mid + '\'' +
                ", cardNo='" + cardNo + '\'' +
                '}';
    }
}
