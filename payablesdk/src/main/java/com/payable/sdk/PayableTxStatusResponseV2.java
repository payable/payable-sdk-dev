package com.payable.sdk;

import static com.payable.sdk.PayableStringUtils.cardTypeToString;
import static com.payable.sdk.PayableStringUtils.statusToString;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Result of {@link Payable#requestTransactionStatusV2(String, int)}.
 * <p>
 * The V2 lookup does not return the batch, terminal or card number, so the SDK follows it up with
 * a V1 lookup by txId and merges that in before the callback fires. The getters mirror
 * {@link PayableSale} so the result reads the same as a payment success.
 */
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

    // Filled from the V1 lookup, see merge()

    @SerializedName("batchNo")
    @Expose
    private int batchNo;

    @Expose
    private String terminalId;

    @Expose
    private String mid;

    @Expose
    private String cardNo;

    @Expose
    private int txnType = -1;

    @Expose
    private String orderTracking;

    void merge(PayableTxStatusResponse v1) {
        batchNo = v1.batchNo;
        terminalId = v1.tid;
        mid = v1.mid;
        cardNo = v1.cardNo;
        txnType = v1.txType;
        orderTracking = v1.orderTracking;
        if (cardHolder == null) cardHolder = v1.cardName;
        if (ccLast4 == null) ccLast4 = v1.ccLast4;
    }

    public int getStatusCode() {
        return status;
    }

    public String getTxId() {
        return txId;
    }

    public String getMessage() {
        return error;
    }

    public String getTxKeyId() {
        return txKeyId;
    }

    public String getCardHolder() {
        return cardHolder;
    }

    public String getCcLast4() {
        return ccLast4;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public double getSaleAmount() {
        return amount == null ? 0 : amount.doubleValue();
    }

    public int getPaymentMethod() {
        return cardType == Payable.WALLET_QPLUS ? Payable.METHOD_WALLET : Payable.METHOD_CARD;
    }

    public int getCardType() {
        return cardType;
    }

    public Date getServerTime() {
        return serverTime;
    }

    public String getApprovalCode() {
        return approvalCode;
    }

    public int getTxnStatus() {
        return transactionStatus;
    }

    public int getBatchNo() {
        return batchNo;
    }

    public String getTerminalId() {
        return terminalId;
    }

    public String getMid() {
        return mid;
    }

    public String getCardNo() {
        return cardNo;
    }

    public int getTxnType() {
        return txnType;
    }

    public String getTxnTypeName() {
        if (txnType == Payable.TXN_EMV) return "EMV";
        if (txnType == Payable.TXN_SWIPE) return "SWIPE";
        if (txnType == Payable.TXN_NFC) return "NFC";
        if (txnType == Payable.TXN_MANUAL) return "MANUAL";
        return "SWIPE";
    }

    public String getOrderTracking() {
        return orderTracking;
    }

    @Override
    public String toString() {
        return "TxStatusV2Res{" +
                "status=" + status +
                ", error='" + error + '\'' +
                ", txId=" + txId +
                ", txKeyId='" + txKeyId + '\'' +
                ", cardHolder='" + cardHolder + '\'' +
                ", ccLast4='" + ccLast4 + '\'' +
                ", cardNo='" + cardNo + '\'' +
                ", amount=" + amount +
                ", cardType=" + cardType +
                ", serverTime=" + serverTime +
                ", approvalCode='" + approvalCode + '\'' +
                ", transactionStatus=" + transactionStatus +
                ", batchNo=" + batchNo +
                ", terminalId='" + terminalId + '\'' +
                ", mid='" + mid + '\'' +
                ", txnType=" + txnType +
                ", orderTracking='" + orderTracking + '\'' +
                '}';
    }

    public String toFormattedString() {
        return "\n\n"
                + "Transaction ID: " + txId + "\n\n"
                + "Transaction key ID: " + txKeyId + "\n\n"
                + "Card holder: " + cardHolder + "\n\n"
                + "Card last 4 digits: " + ccLast4 + "\n\n"
                + "Card number: " + cardNo + "\n\n"
                + "Amount: " + amount + "\n\n"
                + "Card type: " + cardTypeToString(cardType) + "\n\n"
                + "Server time: " + (serverTime == null ? null : serverTime.toLocaleString()) + "\n\n"
                + "Approval code: " + approvalCode + "\n\n"
                + "Transaction status: " + statusToString(transactionStatus) + "\n\n"
                + "Transaction type: " + getTxnTypeName() + "\n\n"
                + "Batch no: " + batchNo + "\n\n"
                + "TID: " + terminalId + "\n\n"
                + "MID: " + mid + "\n\n"
                + "Order tracking: " + orderTracking + "\n\n";
    }
}
