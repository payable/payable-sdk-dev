package com.payable.sdk;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * The latest timed out reversal that has not been retried yet, as returned by
 * {@link Payable#requestLatestReversalRecord()}. Pass {@link #reversalId} to
 * {@link Payable#requestForceReversal(String)} to retry it.
 */
public class PayableReversalRecordResponse extends PayableResponse {

    @Expose
    @SerializedName("id")
    public String reversalId;

    @Expose
    public String amount;

    @Expose
    public int currency;

    @Expose
    public String dateTime;

    @Expose
    public String merchantId;

    @Expose
    public String terminalId;

    @Expose
    public int txnType;

    @Expose
    public String payStatus;

    @Expose
    public String last4;

    @Expose
    public String cardType;

    @Expose
    @SerializedName("transType")
    public String txType;

    @Expose
    public int batchNo;

    @Expose
    public String invoiceNo;

    @Expose
    public String bin;

    @Expose
    public String appName;

    @Expose
    public String aid;

    @Expose
    public String rrn;

    @Expose
    public String traceNo;

    @Expose
    public String refNum;

    @Override
    public String toString() {
        return "PayableReversalRecordResponse{" +
                "status=" + status +
                ", error='" + error + '\'' +
                ", reversalId='" + reversalId + '\'' +
                ", amount='" + amount + '\'' +
                ", currency=" + currency +
                ", dateTime='" + dateTime + '\'' +
                ", merchantId='" + merchantId + '\'' +
                ", terminalId='" + terminalId + '\'' +
                ", txnType=" + txnType +
                ", payStatus='" + payStatus + '\'' +
                ", last4='" + last4 + '\'' +
                ", cardType='" + cardType + '\'' +
                ", txType='" + txType + '\'' +
                ", batchNo=" + batchNo +
                ", invoiceNo='" + invoiceNo + '\'' +
                ", bin='" + bin + '\'' +
                ", appName='" + appName + '\'' +
                ", aid='" + aid + '\'' +
                ", rrn='" + rrn + '\'' +
                ", traceNo='" + traceNo + '\'' +
                ", refNum='" + refNum + '\'' +
                '}';
    }

    public String toFormattedString() {
        return "\n\n"
                + "Status: " + status + "\n"
                + "Error: " + error + "\n"
                + "Reversal ID: " + reversalId + "\n"
                + "Amount: " + PayableStringUtils.currencyToString(currency) + " " + amount + "\n"
                + "Card number: " + maskedCardNo(last4, bin) + "\n"
                + "Card type: " + cardType + "\n"
                + "Transaction type: " + txType + "\n"
                + "Date time: " + dateTime + "\n"
                + "Pay status: " + payStatus + "\n"
                + "MID: " + merchantId + "\n"
                + "TID: " + terminalId + "\n"
                + "Batch no: " + batchNo + "\n"
                + "Invoice no: " + invoiceNo + "\n"
                + "RRN: " + rrn + "\n"
                + "Trace no: " + traceNo + "\n";
    }
}
