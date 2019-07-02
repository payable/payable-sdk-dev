package com.payable.sdk;

public class PayableSale {

    // PAYable
    private String ccLast4;
    private int cardType;
    private String txId;
    private String terminalId;
    private String mid;
    private int txtType;
    private int txnStatus;
    private String receiptSMS;
    private String receiptEmail;

    // Client
    private int statusCode;
    private double saleAmount;
    private String clientId;
    private String clientName;
    private int paymentMethod;
    private String apiKey;
    private String message;

    public String getCcLast4() {
        return ccLast4;
    }

    protected void setCcLast4(String ccLast4) {
        this.ccLast4 = ccLast4;
    }

    public int getCardType() {
        return cardType;
    }

    protected void setCardType(int cardType) {
        this.cardType = cardType;
    }

    public String getTxId() {
        return txId;
    }

    protected void setTxId(String txId) {
        this.txId = txId;
    }

    public String getTerminalId() {
        return terminalId;
    }

    protected void setTerminalId(String terminalId) {
        this.terminalId = terminalId;
    }

    public String getMid() {
        return mid;
    }

    protected void setMid(String mid) {
        this.mid = mid;
    }

    public int getTxnType() {
        return txtType;
    }

    protected void setTxnType(int txtType) {
        this.txtType = txtType;
    }

    public int getTxnStatus() {
        return txnStatus;
    }

    protected void setTxnStatus(int txnStatus) {
        this.txnStatus = txnStatus;
    }

    public String getReceiptSMS() {
        return receiptSMS;
    }

    protected void setReceiptSMS(String receiptSMS) {
        this.receiptSMS = receiptSMS;
    }

    public String getReceiptEmail() {
        return receiptEmail;
    }

    protected void setReceiptEmail(String receiptEmail) {
        this.receiptEmail = receiptEmail;
    }

    public int getStatusCode() {
        return statusCode;
    }

    protected void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public double getSaleAmount() {
        return saleAmount;
    }

    protected void setSaleAmount(double saleAmount) {
        this.saleAmount = saleAmount;
    }

    public String getClientId() {
        return clientId;
    }

    protected void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getClientName() {
        return clientName;
    }

    protected void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public int getPaymentMethod() {
        return paymentMethod;
    }

    protected void setPaymentMethod(int paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getApiKey() {
        return apiKey;
    }

    protected void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public String getMessage() {
        return message;
    }

    protected void setMessage(String message) {
        this.message = message;
    }

    public String getTxnTypeName() {
        if (txtType == Payable.TXN_EMV) return "EMV";
        if (txtType == Payable.TXN_SWIPE) return "SWIPE";
        if (txtType == Payable.TXN_NFC) return "NFC";
        if (txtType == Payable.TXN_MANUAL) return "MANUAL";
        return "SWIPE";
    }
}
