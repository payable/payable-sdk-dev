package com.payable.sdk;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

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

    public static final String TX_RECEIVER = "payable.intent.action.TX_RECEIVER";
    public static final String BROADCAST_ACTION = "sdk.intent.action.FROM_PAYMENT_CLIENT";

    private Activity activity;
    private static Payable payable;
    private PayableSale clientSale;
    private PayableListener payableListener;
    private WaitDialog waitDialog;

    List<PayableProgressListener> progressListeners = new ArrayList<>();
    BroadcastReceiver progressBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.hasExtra("onCardInteraction")) {
                int interaction = intent.getIntExtra("onCardInteraction", -1);
                PayableSale sale = setIntentResponse(intent, clientSale);
                for (PayableProgressListener progressListener : progressListeners) {
                    progressListener.onCardInteraction(interaction, sale);
                }
            } else if (intent.hasExtra("onPaymentAccepted")) {
                PayableSale sale = setIntentResponse(intent, clientSale);
                for (PayableProgressListener progressListener : progressListeners) {
                    progressListener.onPaymentAccepted(sale);
                }
            } else if (intent.hasExtra("onPaymentRejected")) {
                PayableSale sale = setIntentResponse(intent, clientSale);
                for (PayableProgressListener progressListener : progressListeners) {
                    progressListener.onPaymentRejected(sale);
                }
            }
        }
    };

    List<PayableEventListener> eventListeners = new ArrayList<>();
    BroadcastReceiver eventBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            if (intent.hasExtra("onProfileList")) {

                List<PayableProfile> payableProfiles = new Gson().fromJson(intent.getStringExtra("onProfileList"), new TypeToken<List<PayableProfile>>() {
                }.getType());
                for (PayableEventListener eventListener : eventListeners) {
                    eventListener.onProfileList(payableProfiles);
                }

            } else if (intent.hasExtra("onVoid")) {

                PayableResponse payableResponse = new Gson().fromJson(intent.getStringExtra("onVoid"), PayableResponse.class);
                for (PayableEventListener eventListener : eventListeners) {
                    eventListener.onVoid(payableResponse);
                }
            }
        }
    };

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

    private static PayableSale setIntentResponse(Intent data, PayableSale clientSale) {

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
            clientSale.setOut_trade_no(data.getStringExtra("out_trade_no"));
            clientSale.setReceiptSMS(data.getStringExtra("receiptSMS"));
            clientSale.setReceiptEmail(data.getStringExtra("receiptEmail"));
            clientSale.setOrderTracking(data.getStringExtra("orderTracking"));
            clientSale.setMessage(data.getStringExtra("message"));
            clientSale.setJsonData(data.getStringExtra("JSON_DATA"));
        }

        return clientSale;
    }

    public static PayableSale getIntentSale(Intent data) {
        return setIntentResponse(data, new PayableSale());
    }

    private Intent getPaymentIntent() {

        Intent i = new Intent("com.payable.CARD_PAY");
        i.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        i.putExtra("PAY_AMOUNT", clientSale.getSaleAmount());
        i.putExtra("PAY_METHOD", clientSale.getPaymentMethod());
        i.putExtra("CLIENT_ID", clientSale.getClientId());
        i.putExtra("CLIENT_NAME", clientSale.getClientName());
        i.putExtra("CLIENT_API_KEY", clientSale.getApiKey());
        i.putExtra("JSON_DATA", clientSale.getJsonData());
        return i;
    }

    /**
     * This method will not be available from next version
     *
     * @deprecated use {@link #startPayment(PayableSale, PayableListener)} instead.
     */
    @Deprecated
    public void startPayment(double saleAmount, int paymentMethod, PayableListener payableListenerLocal) {
        startPayment(saleAmount, paymentMethod, "{}", payableListenerLocal);
    }

    /**
     * This method will not be available from next version
     *
     * @deprecated use {@link #startPayment(PayableSale, PayableListener)} instead.
     */
    @Deprecated
    public void startPayment(double saleAmount, int paymentMethod, String jsonData, PayableListener payableListenerLocal) {

        waitDialog = waitDialog == null ? new WaitDialog() : waitDialog;
        waitDialog.showDialog(activity);

        this.payableListener = payableListenerLocal;

        clientSale.setSaleAmount(saleAmount);
        clientSale.setPaymentMethod(paymentMethod);
        clientSale.setJsonData(jsonData);

        if (payable.payableListener.onPaymentStart(payable.clientSale)) {

            if (saleAmount >= 1) {

                try {
                    payable.activity.startActivityForResult(getPaymentIntent(), REQUEST_CODE);
                    payable.activity.overridePendingTransition(R.anim.enter, R.anim.exit);
                } catch (ActivityNotFoundException ex) {
                    waitDialog.dismiss();
                    clientSale.setMessage("APP_NOT_INSTALLED");
                    clientSale.setStatusCode(APP_NOT_INSTALLED);
                    payableListener.onPaymentFailure(clientSale);
                }

            } else {
                waitDialog.dismiss();
                clientSale.setMessage("INVALID_AMOUNT");
                clientSale.setStatusCode(INVALID_AMOUNT);
                payableListener.onPaymentFailure(clientSale);
            }

        }
    }

    public void startPayment(PayableSale payableSale, PayableListener payableListenerLocal) {
        startPayment(payableSale.getSaleAmount(), payableSale.getPaymentMethod(), new Gson().toJson(payableSale), payableListenerLocal);
    }

    private void setResponseCallback(Intent data, PayableListener payableListenerLocal) {

        setIntentResponse(data, clientSale);
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

    public void registerProgressListener(PayableProgressListener progressListener) {
        progressListeners.add(progressListener);
        if (progressListeners.size() == 1) {
            IntentFilter intentFilter = new IntentFilter(Payable.TX_RECEIVER + "_" + clientSale.getClientId());
            activity.registerReceiver(progressBroadcastReceiver, intentFilter);
        }
    }

    public void unregisterProgressListener() {
        progressListeners.clear();
        try {
            activity.unregisterReceiver(progressBroadcastReceiver);
        } catch (IllegalArgumentException ex) {
            ex.printStackTrace();
        }
    }

    public void registerEventListener(PayableEventListener eventListener) {
        eventListeners.add(eventListener);
        if (eventListeners.size() == 1) {
            IntentFilter intentFilter = new IntentFilter(Payable.TX_RECEIVER + "_EVENT_" + clientSale.getClientId());
            activity.registerReceiver(eventBroadcastReceiver, intentFilter);
        }
    }

    public void unregisterEventListener() {
        eventListeners.clear();
        try {
            activity.unregisterReceiver(eventBroadcastReceiver);
        } catch (IllegalArgumentException ex) {
            ex.printStackTrace();
        }
    }

    public boolean requestProfileList() {
        sendBroadcast("requestProfileList");
        return true;
    }

    public void sendBroadcast(String sdkAction) {
        Intent intent = getBroadcastIntent(sdkAction);
        sendBroadcast(intent);
    }

    public void sendBroadcast(Intent intent) {
        activity.sendBroadcast(intent);
    }

    private Intent getBroadcastIntent(String sdkAction) {
        Intent intent = new Intent(BROADCAST_ACTION);
        intent.putExtra("sdkAction", sdkAction);
        intent.putExtra("clientId", clientSale.getClientId());
        return intent;
    }

    public boolean requestVoid(String txId) {
        Intent intent = getBroadcastIntent("requestVoid");
        intent.putExtra("txId", txId);
        sendBroadcast(intent);
        return true;
    }
}
