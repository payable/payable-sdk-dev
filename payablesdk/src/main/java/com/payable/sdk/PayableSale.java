package com.payable.sdk;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

public class PayableSale {

    // PAYable
    private String ccLast4;
    private int cardType;
    private String txId;
    private String terminalId;
    private String mid;
    private int txtType;
    private int txnStatus;
    private String out_trade_no;
    private String receiptSMS;
    private String receiptEmail;
    private String orderTracking;

    // Client
    private int statusCode;
    private double saleAmount;
    private String clientId;
    private String clientName;
    private int paymentMethod;
    private String apiKey;
    private String message;
    private String jsonData;

    public PayableSale() {

    }

    public PayableSale(double saleAmount, int paymentMethod) {
        this.saleAmount = saleAmount;
        this.paymentMethod = paymentMethod;
    }

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

    public void setReceiptSMS(String receiptSMS) {
        this.receiptSMS = receiptSMS;
    }

    public String getReceiptEmail() {
        return receiptEmail;
    }

    public void setReceiptEmail(String receiptEmail) {
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

    public String getJsonData() {
        return jsonData;
    }

    public void setJsonData(String jsonData) {
        this.jsonData = jsonData;
    }

    public String getTxnTypeName() {
        if (txtType == Payable.TXN_EMV) return "EMV";
        if (txtType == Payable.TXN_SWIPE) return "SWIPE";
        if (txtType == Payable.TXN_NFC) return "NFC";
        if (txtType == Payable.TXN_MANUAL) return "MANUAL";
        return "SWIPE";
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }

    public String getOut_trade_no() {
        return out_trade_no;
    }

    public void setOut_trade_no(String out_trade_no) {
        this.out_trade_no = out_trade_no;
    }

    public String getOrderTracking() {

        if (orderTracking == null) {
            try {
                JSONObject jsonObject = new JSONObject(getJsonData());
                if (jsonObject.has("ORDER_TRACKING")) {
                    orderTracking = jsonObject.getString("ORDER_TRACKING");
                } else if (jsonObject.has("orderTracking")) {
                    orderTracking = jsonObject.getString("orderTracking");
                }
            } catch (JSONException e) {
                orderTracking = null;
            }
        }

        return orderTracking;
    }

    public void setOrderTracking(String orderTracking) {
        this.orderTracking = orderTracking;
    }
}
