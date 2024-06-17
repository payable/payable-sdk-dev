package com.payable.sdk;

import static com.payable.sdk.PayableStringUtils.cardTypeToString;
import static com.payable.sdk.PayableStringUtils.statusToString;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.math.BigDecimal;
import java.util.Date;

public class PayableTxStatusResponseV2 extends PayableResponse {

    @SerializedName("txKeyId")
    @Expose
    private String txKeyId;

    @SerializedName("ch")
    @Expose
    private String cardHolder;

    @SerializedName("cc")
    @Expose
    private String ccLast4;

    @SerializedName("amount")
    @Expose
    private BigDecimal amount;

    @SerializedName("cardType")
    @Expose
    private int cardType;

    @SerializedName("time")
    @Expose
    private Date serverTime;

    @SerializedName("approvalCode")
    @Expose
    private String approvalCode;

    @SerializedName("txStatus")
    @Expose
    private int transactionStatus;

    @Override
    public String toString() {
        return "TxStatusV2Res{" +
                "txId=" + txId +
                ", txKeyId='" + txKeyId + '\'' +
                ", cardHolder='" + cardHolder + '\'' +
                ", ccLast4='" + ccLast4 + '\'' +
                ", amount=" + amount +
                ", cardType=" + cardType +
                ", serverTime=" + serverTime +
                ", approvalCode='" + approvalCode + '\'' +
                ", transactionStatus=" + transactionStatus +
                '}';
    }

    public String toFormattedString() {
        return "\n\n"
                + "Transaction ID: " + txId + "\n\n"
                + "Transaction key ID: " + txKeyId + "\n\n"
                + "Card holder: " + cardHolder + "\n\n"
                + "Card last 4 digits: " + ccLast4 + "\n\n"
                + "Amount: " + amount + "\n\n"
                + "Card type: " + cardTypeToString(cardType) + "\n\n"
                + "Server time: " + serverTime.toLocaleString() + "\n\n"
                + "Approval code: " + approvalCode + "\n\n"
                + "Transaction status: " + statusToString(transactionStatus) + "\n\n";
    }


}
