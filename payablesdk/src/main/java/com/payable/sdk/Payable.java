package com.payable.sdk;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;

public class Payable {

    // Status Codes
    public static final int REQUEST_CODE = 3569;
    public static final int STATUS_SUCCESS = 222;
    public static final int STATUS_NOT_LOGIN = 555;
    public static final int STATUS_FAILED = 0;
    public static final int INVALID_AMOUNT = 999;
    public static final int APP_NOT_INSTALLED = 888;

    public static final int METHOD_CARD = 10;
    public static final int METHOD_WALLET = 20;
    public static final int METHOD_ANY = 0;

    public static final int TXN_SWIPE = 0;
    public static final int TXN_EMV = 1;
    public static final int TXN_MANUAL = 2;
    public static final int TXN_NFC = 3;

    private Activity activity;
    private static Payable payable;
    private PayableSale clientSale;
    private PayableListener payableListener;
    private WaitDialog waitDialog;

    protected Payable() {
        clientSale = new PayableSale();
    }

    public static Payable createPayableClient(Activity activity, String id, String name, String apiKey) {

        payable = new Payable();
        payable.activity = activity;
        payable.createPayableClient(id, name, apiKey);

        return payable;
    }

    private void createPayableClient(String id, String name, String apiKey) {

        clientSale.setApiKey(apiKey);
        clientSale.setClientId(id);
        clientSale.setClientName(name);
    }

    private void setIntentResponse(Intent data) {

        if (data != null) {
            clientSale.setStatusCode(data.getIntExtra("STATUS_CODE", 0));
            clientSale.setSaleAmount(data.getDoubleExtra("PAY_AMOUNT", 0));
            clientSale.setPaymentMethod(data.getIntExtra("PAY_METHOD", 0));
            clientSale.setCcLast4(data.getStringExtra("ccLast4"));
            clientSale.setCardType(data.getIntExtra("cardType", 0));
            clientSale.setTxId(data.getStringExtra("txId"));
            clientSale.setTerminalId(data.getStringExtra("terminalId"));
            clientSale.setMid(data.getStringExtra("mid"));
            clientSale.setTxnType(data.getIntExtra("txnType", -1));
            clientSale.setTxnStatus(data.getIntExtra("txnStatus", 0));
            clientSale.setReceiptSMS(data.getStringExtra("receiptSMS"));
            clientSale.setReceiptEmail(data.getStringExtra("receiptEmail"));
            clientSale.setMessage(data.getStringExtra("message"));
        }

    }

    private Intent getPaymentIntent() {

        Intent i = new Intent("com.payable.CARD_PAY");
        i.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        i.putExtra("PAY_AMOUNT", clientSale.getSaleAmount());
        i.putExtra("PAY_METHOD", clientSale.getPaymentMethod());
        i.putExtra("CLIENT_ID", clientSale.getClientId());
        i.putExtra("CLIENT_NAME", clientSale.getClientName());
        i.putExtra("CLIENT_API_KEY", clientSale.getApiKey());
        return i;
    }

    public void startPayment(double saleAmount, int paymentMethod, PayableListener payableListenerLocal) {

        waitDialog = waitDialog == null ? new WaitDialog() : waitDialog;
        waitDialog.showDialog(activity);

        this.payableListener = payableListenerLocal;

        clientSale.setSaleAmount(saleAmount);
        clientSale.setPaymentMethod(paymentMethod);

        if (payable.payableListener.onPaymentStart(payable.clientSale)) {

            if (saleAmount > 0) {

                try {
                    payable.activity.startActivityForResult(getPaymentIntent(), REQUEST_CODE);

                } catch (ActivityNotFoundException ex) {
                    clientSale.setStatusCode(APP_NOT_INSTALLED);
                    payableListener.onPaymentFailure(clientSale);
                }

            } else {
                clientSale.setStatusCode(INVALID_AMOUNT);
                payableListener.onPaymentFailure(clientSale);
            }

        }
    }

    private void setResponseCallback(Intent data, PayableListener payableListenerLocal) {

        setIntentResponse(data);
        this.payableListener = payableListenerLocal;

        switch (clientSale.getStatusCode()) {
            case STATUS_SUCCESS:
                clientSale.setStatusCode(STATUS_SUCCESS);
                payableListener.onPaymentSuccess(clientSale);
                break;
            case STATUS_NOT_LOGIN:
                clientSale.setStatusCode(STATUS_NOT_LOGIN);
                payableListener.onPaymentFailure(clientSale);
                break;
            case STATUS_FAILED:
                clientSale.setStatusCode(STATUS_FAILED);
                payableListener.onPaymentFailure(clientSale);
                break;
            case INVALID_AMOUNT:
                clientSale.setStatusCode(INVALID_AMOUNT);
                payableListener.onPaymentFailure(clientSale);
                break;
            default:
                break;
        }
    }

    public void handleResponse(int requestCode, Intent data) {

        if (requestCode == REQUEST_CODE) {

            waitDialog.dismiss();

            if (payable != null && payable.payableListener != null) {
                payable.setResponseCallback(data, payable.payableListener);
            }
        }
    }
}
