package com.payable.sdk;

import com.google.gson.annotations.Expose;

/**
 * A single closed transaction returned by
 * {@link Payable#requestTransactionHistory(PayableTransactionFilter)}.
 */
public class PayableTransaction {

    @Expose
    public long txId;

    @Expose
    public String cardHolder;

    /**
     * The masked card number, e.g. {@code 462834******1234}.
     */
    @Expose
    public String cardNo;

    @Expose
    public String ccLast4;

    @Expose
    public double amount;

    /**
     * One of the {@code Payable.CARD_TYPE_*} constants.
     */
    @Expose
    public int cardType;

    /**
     * How the card was read - one of the {@code Payable.TXN_*} constants.
     */
    @Expose
    public int txnType;

    @Expose
    public int txType;

    /**
     * One of {@link Payable#STATUS_OPEN}, {@link Payable#STATUS_CLOSE},
     * {@link Payable#STATUS_OPEN_VOID}, {@link Payable#STATUS_CLOSE_VOID}.
     */
    @Expose
    public int status;

    @Expose
    public String time;

    @Expose
    public String approvalCode;

    @Expose
    public String rrn;

    @Expose
    public int batchNo;

    @Expose
    public String merchantInvoiceId;

    @Expose
    public int currencyType;

    @Expose
    public int installment;

    @Expose
    public String tid;

    @Expose
    public String mid;

    @Expose
    public String appName;

    @Expose
    public String aid;

    @Expose
    public int stn;

    @Expose
    public String invoice;

    @Override
    public String toString() {
        return "PayableTransaction{" +
                "txId=" + txId +
                ", cardHolder='" + cardHolder + '\'' +
                ", cardNo='" + cardNo + '\'' +
                ", ccLast4='" + ccLast4 + '\'' +
                ", amount=" + amount +
                ", cardType=" + cardType +
                ", txnType=" + txnType +
                ", txType=" + txType +
                ", status=" + status +
                ", time='" + time + '\'' +
                ", approvalCode='" + approvalCode + '\'' +
                ", rrn='" + rrn + '\'' +
                ", batchNo=" + batchNo +
                ", merchantInvoiceId='" + merchantInvoiceId + '\'' +
                ", currencyType=" + currencyType +
                ", installment=" + installment +
                ", tid='" + tid + '\'' +
                ", mid='" + mid + '\'' +
                ", appName='" + appName + '\'' +
                ", aid='" + aid + '\'' +
                ", stn=" + stn +
                ", invoice='" + invoice + '\'' +
                '}';
    }

    public String toFormattedString() {
        return "\n"
                + "Transaction ID: " + txId + "\n"
                + "Time: " + time + "\n"
                + "Amount: " + PayableStringUtils.currencyToString(currencyType) + " " + amount + "\n"
                + "Card holder: " + cardHolder + "\n"
                + "Card no: " + cardNo + "\n"
                + "Card type: " + PayableStringUtils.cardTypeToString(cardType) + "\n"
                + "Status: " + PayableStringUtils.statusToString(status) + "\n"
                + "Approval code: " + approvalCode + "\n"
                + "RRN: " + rrn + "\n"
                + "Batch no: " + batchNo + "\n"
                + "Invoice: " + merchantInvoiceId + "\n"
                + "TID: " + tid + "\n"
                + "MID: " + mid + "\n"
                + "Installment: " + installment + "\n";
    }
}
