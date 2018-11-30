package com.payabledemo.com.payabledemo;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

import java.text.DecimalFormat;

public class Payable extends AppCompatActivity {

    // Status Codes
    public static final int PAYABLE_REQUEST_CODE = 3569;
    public static final int PAYABLE_STATUS_SUCCESS = 222;
    public static final int PAYABLE_STATUS_NOT_LOGIN = 555;
    public static final int PAYABLE_STATUS_FAILED = 0;
    public static final int PAYABLE_INVALID_AMOUNT = 999;
    public static final int PAYABLE_APP_NOT_INSTALLED = 888;

    // PAYable
    private String ccLast4;
    private int cardType;
    private String txId;
    private String terminalId;
    private String mid;
    private int isEmv;
    private int txnStatus;

    // Client
    private int statusCode;
    private double saleAmount;
    private String clientId;
    private String clientName;

    PayableListener payableListener;

    public void Payable() {
    }

    public String getCcLast4() {
        return ccLast4;
    }

    private void setCcLast4(String ccLast4) {
        this.ccLast4 = ccLast4;
    }

    public int getCardType() {
        return cardType;
    }

    private void setCardType(int cardType) {
        this.cardType = cardType;
    }

    public String getTxId() {
        return txId;
    }

    private void setTxId(String txId) {
        this.txId = txId;
    }

    public String getTerminalId() {
        return terminalId;
    }

    private void setTerminalId(String terminalId) {
        this.terminalId = terminalId;
    }

    public String getMid() {
        return mid;
    }

    private void setMid(String mid) {
        this.mid = mid;
    }

    public int getIsEmv() {
        return isEmv;
    }

    private void setIsEmv(int isEmv) {
        this.isEmv = isEmv;
    }

    public int getTxnStatus() {
        return txnStatus;
    }

    private void setTxnStatus(int txnStatus) {
        this.txnStatus = txnStatus;
    }

    public int getStatusCode() {
        return statusCode;
    }

    private void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public double getSaleAmount() {
        return saleAmount;
    }

    public void setSaleAmount(double saleAmount) {
        this.saleAmount = saleAmount;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public void setIntentResponse(Intent data) {

        if (data != null) {
            this.statusCode = data.getIntExtra("STATUS_CODE", 0);
            this.saleAmount = data.getDoubleExtra("PAY_AMOUNT", 0);
            this.ccLast4 = data.getStringExtra("ccLast4");
            this.cardType = data.getIntExtra("cardType", 0);
            this.txId = data.getStringExtra("txId");
            this.terminalId = data.getStringExtra("terminalId");
            this.mid = data.getStringExtra("mid");
            this.isEmv = data.getIntExtra("isEmv", 0);
            this.txnStatus = data.getIntExtra("txnStatus", 0);
        }

    }

    public Intent getPaymentIntent() {

        Intent i = new Intent("com.payable.CARD_PAY");
        i.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        i.putExtra("PAY_AMOUNT", getSaleAmount());
        i.putExtra("CLIENT_ID", getClientId());
        i.putExtra("CLIENT_NAME", getClientName());

        return i;
    }

    public void startPayment(double saleAmount, PayableListener payableListenerLocal) {

        this.payableListener = payableListenerLocal;

        if (saleAmount > 0) {

            setSaleAmount(Double.parseDouble(new DecimalFormat("0.00").format(saleAmount)));

            try {
                Intent i = new Intent("com.payable.CARD_PAY");
                i.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                i.putExtra("PAY_AMOUNT", getSaleAmount());
                i.putExtra("CLIENT_ID", getClientId());
                i.putExtra("CLIENT_NAME", getClientName());
                startActivityForResult(i, PAYABLE_REQUEST_CODE);

            } catch (ActivityNotFoundException ex) {
                setStatusCode(PAYABLE_APP_NOT_INSTALLED);
                payableListener.onPaymentFailure(this);
            }

        } else {
            setStatusCode(PAYABLE_INVALID_AMOUNT);
            payableListener.onPaymentFailure(this);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PAYABLE_REQUEST_CODE) {
            setResponseCallback(data, payableListener);
        }

    }

    public void setResponseCallback(Intent data, PayableListener payableListenerLocal) {

        setIntentResponse(data);
        this.payableListener = payableListenerLocal;

        switch (getStatusCode()) {
            case PAYABLE_STATUS_SUCCESS:
                setStatusCode(PAYABLE_STATUS_SUCCESS);
                payableListener.onPaymentSuccess(this);
                break;
            case PAYABLE_STATUS_NOT_LOGIN:
                setStatusCode(PAYABLE_STATUS_NOT_LOGIN);
                payableListener.onPaymentFailure(this);
                break;
            case PAYABLE_STATUS_FAILED:
                setStatusCode(PAYABLE_STATUS_FAILED);
                payableListener.onPaymentFailure(this);
                break;
            case PAYABLE_INVALID_AMOUNT:
                setStatusCode(PAYABLE_INVALID_AMOUNT);
                payableListener.onPaymentFailure(this);
                break;
            default:
                break;
        }
    }

}