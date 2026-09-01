### PAYable SDK - Android Integration

![](https://i.imgur.com/ERpCDa7.png)

Android SDK - [android-sdk.payable.lk](https://android-sdk.payable.lk) | [Create Issue](https://github.com/payable/payable-sdk-dev/issues/new)

[![Build Status](https://travis-ci.com/payable/payable-sdk-dev.svg?branch=master)](https://travis-ci.com/payable/payable-sdk-dev)
[![](https://jitpack.io/v/payable/payable-sdk-dev.svg)](https://jitpack.io/#payable/payable-sdk-dev) 

<hr>

### Initialization 

* Request and install **Sandbox** PAYable APP - Testing purpose

> Payments (`startPayment`) work against any PAYable POS app, but every `request*` method and the event
> callbacks below talk to **Payable Pro** (`com.cba.payable.wpos`) - it is the only app that declares the
> SDK's broadcast receiver. With any other PAYable app installed, those requests return `false` and the
> callback fires with `APP_NOT_INSTALLED (888)`.

### Initialization

1. Add the below repository into your project level `settings.gradle` or `build.gradle` file.

```gradle
allprojects {
    repositories {
        ...
        maven { url 'https://jitpack.io' }
    }
}
```

2. Add the below dependency into your module level `build.gradle` file.

```gradle
implementation 'com.github.payable:payable-sdk-dev:LATEST_VERSION'
```

<hr>

### Implementation

<b>1.</b> Import PAYable SDK packages.

```java
import com.payable.sdk.Payable;
import com.payable.sdk.PayableListener;
import com.payable.sdk.PayableProgressListener;
import com.payable.sdk.PayableSale;
```

* The advanced usage below additionally needs

```java
import com.payable.sdk.AmountInputFilter;
import com.payable.sdk.PayableEventListener;
import com.payable.sdk.PayableForceReversalResponse;
import com.payable.sdk.PayableProfile;
import com.payable.sdk.PayableResponse;
import com.payable.sdk.PayableReversalRecordResponse;
import com.payable.sdk.PayableSettlement;
import com.payable.sdk.PayableSettlementHistoryResponse;
import com.payable.sdk.PayableTxStatusResponse;
import com.payable.sdk.PayableTxStatusResponseV2;
import com.payable.sdk.Picker;
```

<b>2.</b> Implement `PayableListener` and declare PAYable client in your class.

```java
public class MainActivity extends AppCompatActivity implements PayableListener {

    Payable payableClient;

    // Return false to abort the payment before the PAYable app is launched.
    @Override
    public boolean onPaymentStart(PayableSale payableSale) {
        return true;
    }

    @Override
    public void onPaymentSuccess(PayableSale payableSale) {

    }

    @Override
    public void onPaymentFailure(PayableSale payableSale) {

    }
}
```

<b>3.</b> Create PAYable client with 

```java 
Payable.createPayableClient(activity: Activity, client_id: String, client_name: String, api_key: String);
```

> It should be declared inside `onCreate` method like below.

```java
@Override
protected void onCreate(Bundle savedInstanceState) {
    ...
    payableClient = Payable.createPayableClient(this, "1452", "FOOD_COURT", "C6DFA0B215B2CF24EF04794F718A3FC8");
}
```

<b>4.</b> Override `onActivityResult` method and set the callback listener to handle the response.

```java
@Override
protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    ...
    payableClient.handleResponse(requestCode, data);
}
```

<b>5.</b> On click listener call the method to start payment.

```java
PayableSale payableSale = new PayableSale(sale_amount: Double, payment_method: Integer);
```

* Optional parameters

```java
payableSale.setReceiptEmail("test@payable.lk");
payableSale.setReceiptSMS("0110000000");
payableSale.setOrderTracking("invoice56");
payableSale.setTerminalId("1254");
```

* Start the payment intent

```java
payableClient.startPayment(payableSale, this);
```

<!--
```java 
payableClient.startPayment(sale_amount: Double, payment_method: Integer, payable_listener: PayableListener);
```

If you want to track the sale or need to pass custom data and receive it on payment completion, use this method.

```java 
payableClient.startPayment(sale_amount: Double, payment_method: Integer, json_data: String, payable_listener: PayableListener);
```
-->

* Payment methods

```java
Payable.METHOD_ANY
Payable.METHOD_CARD
Payable.METHOD_WALLET
```

Example:

<!--
```java
payableClient.startPayment(500.50, Payable.METHOD_ANY, this);
```
-->

* For the order tracking you need to pass the tracking number in json data as below.

```java
PayableSale payableSale = new PayableSale( /* saleAmount */ 500, Payable.METHOD_CARD);
payableSale.setReceiptEmail("test@payable.lk");
payableSale.setReceiptSMS("0110000000");
payableSale.setOrderTracking("invoice56");

payableClient.startPayment(payableSale, /* PayableListener */ this);
```

* In order to choose a TID for the transaction, you can pass the terminal ID.

```java
payableSale.setTerminalId("terminalId");
```

<!--
```java
payableClient.startPayment(500.50, Payable.METHOD_ANY, "{ \"ORDER_TRACKING\" : \"123455\" }", this);
```
-->

##### * Return PayableSale Object

The `PayableSale` passed to `onPaymentSuccess` / `onPaymentFailure` carries the result.

```java
payableSale.getStatusCode();
payableSale.getSaleAmount();
payableSale.getCcLast4();
payableSale.getCardNo();
payableSale.getCardType();
payableSale.getTxId();
payableSale.getTerminalId();
payableSale.getMid();
payableSale.getTxnType();      // Payable.TXN_SWIPE / TXN_EMV / TXN_MANUAL / TXN_NFC
payableSale.getTxnTypeName();  // the above as a readable string
payableSale.getTxnStatus();
payableSale.getPaymentMethod();
payableSale.getReceiptSMS();
payableSale.getReceiptEmail();
payableSale.getOrderTracking();
payableSale.getMessage();      // error description on failure
```

##### * Return Status Codes

```java
Payable.REQUEST_CODE : 3569;
Payable.STATUS_SUCCESS : 222;
Payable.STATUS_NOT_LOGIN : 555;
Payable.STATUS_FAILED : 0;
Payable.INVALID_AMOUNT : 999;
Payable.APP_NOT_INSTALLED : 888;
Payable.INVALID_ORDER_ID : 777;
```

> The sale amount must be **1 or more**. `startPayment` rejects anything below that without launching
> the PAYable app: `onPaymentFailure` fires with `INVALID_AMOUNT (999)`. Returning `false` from
> `onPaymentStart` aborts the sale the same way, without a callback.

##### * Card Actions

```java
Payable.TXN_SWIPE : 0;
Payable.TXN_EMV : 1;
Payable.TXN_MANUAL : 2;
Payable.TXN_NFC : 3;
```

##### * Card Types

The `cardType` parameter of `requestVoid` and `requestTransactionStatus[V2]` takes one of these.

```java
Payable.CARD_TYPE_OTHER : 0;
Payable.CARD_TYPE_VISA : 1;
Payable.CARD_TYPE_AMEX : 2;
Payable.CARD_TYPE_MASTER : 3;
Payable.CARD_TYPE_DINERS : 4;
Payable.CARD_TYPE_MAESTRO : 5;
Payable.CARD_TYPE_CUP : 6;
Payable.CARD_TYPE_JCB : 7;
Payable.WALLET_QPLUS : 8;
```

* `Picker.cardTypePicker(context, cardType -> { ... })` shows a ready-made dialog for the common ones
  (VISA / MASTER / AMEX / CUP / JCB), and `Picker.profilePicker(context, payableProfiles, listener)`
  does the same for the profiles returned by `requestProfileList()`.

<hr/>

### Advanced Usage

##### Background Progress Listener

* If you want to receive the progress updates of the ongoing payment in background, you need to register progress listener using `registerProgressListener(listener)` and make sure you unregister the listener using `unregisterProgressListener()` method on activity `onDestroy()` method to avoid memory leakage.

```java
payableClient.registerProgressListener(new PayableProgressListener() {

    @Override
    public void onCardInteraction(int action, PayableSale payableSale) {
        
    }

    @Override
    public void onPaymentAccepted(PayableSale payableSale) {
        
    }

    @Override
    public void onPaymentRejected(PayableSale payableSale) {

    }
});
```

Explanation for `PayableProgressListener` interface.

```java
onCardInteraction(int action, PayableSale payableSale)
```

* This method will be called in the background when the terminal listens to any card interactions such as ENV, SWIPE, and NFC, this will respond with your sale values and interacted action as `Payable.TXN_EMV, Payable.TXN_SWIPE, Payable.TXN_NFC` and -1 for any error on card interaction. You can get the error description using `payableSale.getMessage()` method.

```java
onPaymentAccepted(PayableSale payableSale)
```

* This method will be called in the background when the terminal accepts the card and proceed further.

```java
onPaymentRejected(PayableSale payableSale)
```

* This method will be called in the background when the terminal rejects the card or throws any errors from servers.

##### Unregister progress listener

```java
@Override
protected void onDestroy() {
    super.onDestroy();
    payableClient.unregisterProgressListener();
}
```

##### Register event listener

When you need to request any event from PAYable you will have to register the event listener and unregister it on `onDestroy` method when you are done.

```java
payableClient.registerEventListener(new PayableEventListener() {

    @Override
    public void onProfileList(List<PayableProfile> payableProfiles) {

    }

    @Override
    public void onVoid(PayableResponse payableResponse) {

    }

    @Override
    public void onTransactionStatus(PayableTxStatusResponse payableResponse) {

    }

    @Override
    public void onTransactionStatusV2(PayableTxStatusResponseV2 payableResponse) {

    }

    @Override
    public void onSettlementHistory(PayableSettlementHistoryResponse payableResponse) {

    }

    @Override
    public void onLatestReversalRecord(PayableReversalRecordResponse payableResponse) {

    }

    @Override
    public void onForceReversal(PayableForceReversalResponse payableResponse) {

    }
});
```

> `onSettlementHistory`, `onLatestReversalRecord` and `onForceReversal` are optional - they have
> default no-op implementations, so you only override the ones you use.

##### Unregister event listener

```java
@Override
protected void onDestroy() {
    super.onDestroy();
    payableClient.unregisterEventListener();
}
```

##### PAYable events

| Method | Callback
|--|--|
| `boolean requestProfileList()` | `onProfileList(List<PayableProfile> payableProfiles)`
| `boolean requestVoid(String txId, int cardType)` | `onVoid(PayableResponse payableResponse)`
| `boolean requestTransactionStatus(String txId, int cardType)` | `onTransactionStatus(PayableTxStatusResponse payableResponse)`
| `boolean requestTransactionStatusV2(String orderId, int cardType)` | `onTransactionStatusV2(PayableTxStatusResponseV2 payableResponse)`
| `boolean requestSettlementHistory(PayableSettlementFilter filter)`<br/>`void requestSettlementHistory(int pageId, int pageSize)` | `onSettlementHistory(PayableSettlementHistoryResponse payableResponse)`
| `boolean requestLatestReversalRecord()` | `onLatestReversalRecord(PayableReversalRecordResponse payableResponse)`
| `boolean requestForceReversal(String reversalId)` | `onForceReversal(PayableForceReversalResponse payableResponse)`

<br/>

* `PayableProfile`

```java
String tid;
String name;
String currency;
Integer installment;
```

* `PayableResponse`

```java
int status;
String txId;
String error;
```

* `PayableTxStatusResponse`

```java
String cardName;
String ccLast4;
double amount;
int cardType;
String time;
String orderTracking;
int txType;
int currencyType;
int installment;
String tid;
String mid;
String cardNo;
```

* `PayableTxStatusResponseV2`

```java
String txKeyId
String cardHolder
String ccLast4
BigDecimal amount
int cardType
Date serverTime
String approvalCode
int transactionStatus
```

> Unlike the other models, these fields are `private` and have no getters - read them with
> `toFormattedString()` or `toString()`. The inherited `status`, `txId` and `error` are public as usual.

* `PayableSettlementFilter`

Every field is optional. `pageId` is zero based; `pageSize` defaults to 20 on the POS app when left at 0.

```java
int pageId;
int pageSize;
Long profileId;
String startDate;
String endDate;
String tid;
Integer currency;
Integer installment;
Integer isDcc;
Integer batchNo;
```

* `PayableSettlementHistoryResponse`

```java
List<PayableSettlement> settlements;
int pageId;
boolean hasMore;          // true when there is at least one more page to request
```

* `PayableSettlement`

```java
String id;
long profileId;
int batchNo;
String tid;
String mid;
int currency;
int installment;
int isDcc;
int txCount;
double totalAmount;
String startDate;
String endDate;
String settledDate;
String printedDate;
```

* `PayableReversalRecordResponse`

```java
String reversalId;        // pass this to requestForceReversal()
String amount;
int currency;
String dateTime;
String merchantId;
String terminalId;
int txnType;
String payStatus;
String last4;
String cardType;
String txType;
String batchNo;
String invoiceNo;
String bin;
String appName;
String aid;
String rrn;
String traceNo;
String refNum;
```

* `PayableForceReversalResponse`

```java
String reversalHistoryId;
String reversalStatus;    // isCompleted() is true when this is REVERSAL_COMPLETED
String nacResponseCode;
String retrievalRefNo;
String authIdResponseCode;
String ccLast4;
String amount;
int currency;
String invoiceNo;
```

> **Force reversal is a two call flow.** Call `requestLatestReversalRecord()` first - it returns the
> most recent timed out reversal that has not been retried yet (`reversalId` is null when there is
> nothing pending) - then pass its `reversalId` to `requestForceReversal(String)`. The reversal only
> succeeded when `isCompleted()` returns true; a completed API call with any other `reversalStatus`
> comes back with `status` failed and `error` set to that status.

> Every response type extends `PayableResponse`, so they all carry `status`, `txId` and `error`. On a
> failed request (`APP_NOT_INSTALLED`, `INVALID_AMOUNT`, `INVALID_ORDER_ID`) the request method returns
> `false` and the callback still fires with `status` and `error` set. For `requestTransactionStatusV2`,
> `orderId` must be 1-40 characters and may contain only letters, digits, `_`, `-` and `/`;
> `requestForceReversal` applies the same rule to `reversalId` with a 64 character limit.

<hr/>

##### Example Usage

```java
public class MainActivity extends AppCompatActivity implements PayableListener {

    EditText edtAmount, edtTracking, edtEmail, edtSMS, edtTxnId, edtOrderId;
    Button btnPayCard, btnPayWallet, btnPay, btnProfile, btnVoid, btnStatus, btnStatusV2,
            btnSettlementHistory, btnLatestReversal, btnForceReversal;
    TextView txtResponse, actTitle;

    double saleAmount = 0;
    String selectedProfile;
    String pendingReversalId;

    // 1. Declare Payable Client
    Payable payableClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        edtAmount = findViewById(R.id.edtAmount);
        edtTracking = findViewById(R.id.edtTracking);
        edtEmail = findViewById(R.id.edtEmail);
        edtSMS = findViewById(R.id.edtSMS);
        edtTxnId = findViewById(R.id.edtTxnId);
        edtOrderId = findViewById(R.id.edtOrderId);
        btnPayCard = findViewById(R.id.btnPayCard);
        btnPayWallet = findViewById(R.id.btnPayWallet);
        btnPay = findViewById(R.id.btnPay);
        btnProfile = findViewById(R.id.btnProfile);
        btnVoid = findViewById(R.id.btnVoid);
        btnStatus = findViewById(R.id.btnStatus);
        btnStatusV2 = findViewById(R.id.btnStatusV2);
        btnSettlementHistory = findViewById(R.id.btnSettlementHistory);
        btnLatestReversal = findViewById(R.id.btnLatestReversal);
        btnForceReversal = findViewById(R.id.btnForceReversal);
        txtResponse = findViewById(R.id.txtResponse);
        actTitle = findViewById(R.id.actTitle);
        actTitle.setText("Main Activity");

        edtAmount.setFilters(AmountInputFilter.getFilter(100000));

        // 2. Set Payable Client
        payableClient = Payable.createPayableClient(this, "1452", "FOOD_COURT", "C6DFA0B215B2CF24EF04794F718A3FC8");

        btnPayCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                hideSoftKeyboard(edtAmount);

                // 3. Call your method
                payableSale(Payable.METHOD_CARD);
            }
        });

        btnPayWallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                hideSoftKeyboard(edtAmount);

                // 3. Call your method
                payableSale(Payable.METHOD_WALLET);
            }
        });

        btnPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                payableSale(Payable.METHOD_ANY);
            }
        });

        btnProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                payableClient.requestProfileList();
            }
        });

        /**
         * Advanced Usage (Optional):
         * If you want to receive the progress updates of the ongoing payment, you need to register a progress listener
         * and make sure you unregister the listener using unregisterProgressListener() method on activity onDestroy() method
         *
         */
        payableClient.registerProgressListener(new PayableProgressListener() {

            @Override
            public void onCardInteraction(int action, PayableSale payableSale) {
                Log.e("TEST_IMPL", "background: onCardInteraction: " + action + " => " + payableSale.toString());
                updateTxtResponse("background: onCardInteraction => " + action);
                Toast.makeText(getApplicationContext(), "background: onCardInteraction", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPaymentAccepted(PayableSale payableSale) {
                Log.e("TEST_IMPL", "background: onPaymentAccepted: " + payableSale.toString());
                updateTxtResponse("background: onPaymentAccepted => " + payableSale.getTxnTypeName());
                Toast.makeText(getApplicationContext(), "background: onPaymentAccepted", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPaymentRejected(PayableSale payableSale) {
                Log.e("TEST_IMPL", "background: onPaymentRejected => " + payableSale.toString());
                updateTxtResponse("background: onPaymentRejected: " + payableSale.getMessage());
                Toast.makeText(getApplicationContext(), "background: onPaymentRejected", Toast.LENGTH_SHORT).show();
            }
        });

        /**
         * Advanced Usage (Optional):
         * If you want to make any requests to PAYable and get responses, register the event listener
         * and make sure you unregister the listener using unregisterEventListener() method on activity onDestroy() method
         *
         */
        payableClient.registerEventListener(new PayableEventListener() {
            @Override
            public void onProfileList(final List<PayableProfile> payableProfiles) {

                for (PayableProfile payableProfile : payableProfiles) {
                    updateFreshTxtResponse("tid: " + payableProfile.tid + " " + payableProfile.currency + " name: " + payableProfile.name + " inst: " + payableProfile.installment);
                }

                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
                builder.setTitle("Select Profile");

                String[] profileNames = new String[payableProfiles.size()];
                for (int i = 0; i < payableProfiles.size(); i++) {
                    profileNames[i] = "tid: " + payableProfiles.get(i).tid + " " + payableProfiles.get(i).currency + " : name: " + payableProfiles.get(i).name + " inst: " + payableProfiles.get(i).installment;
                }

                builder.setItems(profileNames, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        selectedProfile = payableProfiles.get(which).tid;
                        btnProfile.setText("Selected Profile: " + selectedProfile);
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();
            }

            @Override
            public void onVoid(PayableResponse payableResponse) {
                updateFreshTxtResponse("onVoid: " + payableResponse.status + " txId: " + payableResponse.txId + " error: " + payableResponse.error);
            }

            @Override
            public void onTransactionStatus(PayableTxStatusResponse payableResponse) {
                if (payableResponse.error != null) {
                    updateFreshTxtResponse("onTransactionStatus: " + payableResponse.status + " txId: " + payableResponse.txId + " error: " + payableResponse.error);
                } else {
                    updateFreshTxtResponse("onTransactionStatus: " + payableResponse.toString());
                }
            }

            @Override
            public void onTransactionStatusV2(PayableTxStatusResponseV2 payableResponse) {
                if (payableResponse.error != null) {
                    updateFreshTxtResponse("onTransactionStatus: " + payableResponse.status + " txId: " + " error: " + payableResponse.error);
                } else {
                    updateFreshTxtResponse("onTransactionStatus: " + payableResponse.toFormattedString());
                }
            }

            @Override
            public void onSettlementHistory(PayableSettlementHistoryResponse payableResponse) {

                if (payableResponse.error != null) {
                    updateFreshTxtResponse("onSettlementHistory: " + payableResponse.status + " error: " + payableResponse.error);
                    return;
                }

                List<PayableSettlement> settlements = payableResponse.settlements;

                if (settlements == null || settlements.isEmpty()) {
                    updateFreshTxtResponse("onSettlementHistory: no settlements");
                    return;
                }

                updateFreshTxtResponse("onSettlementHistory: " + settlements.size() + " settlement(s), has more: " + payableResponse.hasMore);

                for (PayableSettlement settlement : settlements) {
                    updateTxtResponse(settlement.toFormattedString());
                }
            }

            @Override
            public void onLatestReversalRecord(PayableReversalRecordResponse payableResponse) {

                pendingReversalId = payableResponse.reversalId;
                btnForceReversal.setEnabled(pendingReversalId != null && !pendingReversalId.isEmpty());

                if (payableResponse.error != null) {
                    updateFreshTxtResponse("onLatestReversalRecord: " + payableResponse.status + " error: " + payableResponse.error);
                } else if (pendingReversalId == null) {
                    updateFreshTxtResponse("onLatestReversalRecord: no pending reversal");
                } else {
                    updateFreshTxtResponse("onLatestReversalRecord: " + payableResponse.toFormattedString());
                }
            }

            @Override
            public void onForceReversal(PayableForceReversalResponse payableResponse) {

                pendingReversalId = null;
                btnForceReversal.setEnabled(false);

                if (payableResponse.error != null) {
                    updateFreshTxtResponse("onForceReversal: " + payableResponse.status + " error: " + payableResponse.error);
                } else {
                    updateFreshTxtResponse("onForceReversal: " + payableResponse.toFormattedString());
                }
            }
        });

        btnVoid.setOnClickListener(v -> {
            if (!edtTxnId.getText().toString().isEmpty()) {
                Picker.cardTypePicker(MainActivity.this, cardType -> payableClient.requestVoid(edtTxnId.getText().toString(), cardType));
            }
        });

        btnStatus.setOnClickListener(v -> {
            if (!edtTxnId.getText().toString().isEmpty()) {
                Picker.cardTypePicker(MainActivity.this, cardType -> payableClient.requestTransactionStatus(edtTxnId.getText().toString(), cardType));
            }
        });

        btnStatusV2.setOnClickListener(v -> {
            if (!edtOrderId.getText().toString().isEmpty()) {
                Picker.cardTypePicker(MainActivity.this, cardType -> payableClient.requestTransactionStatusV2(edtOrderId.getText().toString(), cardType));
            }
        });

        btnSettlementHistory.setOnClickListener(v -> payableClient.requestSettlementHistory(0, 20));

        btnLatestReversal.setOnClickListener(v -> payableClient.requestLatestReversalRecord());

        btnForceReversal.setOnClickListener(v -> {

            if (pendingReversalId == null || pendingReversalId.isEmpty()) {
                Toast.makeText(this, "Fetch the latest reversal record first", Toast.LENGTH_SHORT).show();
                return;
            }

            new AlertDialog.Builder(MainActivity.this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT)
                    .setTitle("Force Reversal")
                    .setMessage("Retry the reversal of record " + pendingReversalId + "?")
                    .setNegativeButton("Cancel", null)
                    .setPositiveButton("Reverse", (dialog, which) -> payableClient.requestForceReversal(pendingReversalId))
                    .show();
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        payableClient.unregisterProgressListener();
        payableClient.unregisterEventListener();
    }

    private void payableSale(int paymentMethod) {

        if (edtAmount.getText().toString().isEmpty()) {
            Toast.makeText(this, "Amount is empty", Toast.LENGTH_SHORT).show();
            return;
        }

        // 4. Convert sale amount to double from EditText
        saleAmount = Double.parseDouble(edtAmount.getText().toString());

        // 5. start the payment request to PAYable app with the callback listener
        PayableSale payableSale = new PayableSale(saleAmount, paymentMethod);

        if (!edtEmail.getText().toString().isEmpty()) {
            payableSale.setReceiptEmail(edtEmail.getText().toString());
        }

        if (!edtSMS.getText().toString().isEmpty()) {
            payableSale.setReceiptSMS(edtSMS.getText().toString());
        }

        if (!edtTracking.getText().toString().isEmpty()) {
            payableSale.setOrderTracking(edtTracking.getText().toString());
        }

        if (selectedProfile != null) {
            payableSale.setTerminalId(selectedProfile);
        }

        payableClient.startPayment(payableSale, this);

        // Deprecated implementations
        // payableClient.startPayment(saleAmount, paymentMethod, "{ \"ORDER_TRACKING\" : \"SDK-TEST\" }", this);
        // payableClient.startPayment(saleAmount, paymentMethod, this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // 7. onActivityResult set the callback listener to handle the response
        payableClient.handleResponse(requestCode, data);
    }

    // 8. onPaymentSuccess method
    @Override
    public boolean onPaymentStart(PayableSale payableSale) {
        txtResponse.setText("foreground: onPaymentStart => " + payableSale.getSaleAmount());
        return true;
    }

    // 8. onPaymentSuccess method
    @Override
    public void onPaymentSuccess(PayableSale payableSale) {
        updateTxtResponse("foreground: onPaymentSuccess => " + payableSale.getTxId());
        updateTxtResponse(payableSale);

        edtTxnId.setText(payableSale.getTxId());
    }

    // 9. onPaymentFailure method
    @Override
    public void onPaymentFailure(PayableSale payableSale) {
        updateTxtResponse("foreground: onPaymentFailure => " + payableSale.getMessage());
        updateTxtResponse(payableSale);
    }

    // 10. Update..
    private void updateTxtResponse(PayableSale payableSale) {

        String responseText = "\nstatusCode: " + payableSale.getStatusCode() + "\n";
        responseText += "responseAmount: " + payableSale.getSaleAmount() + "\n";
        responseText += "ccLast4: " + payableSale.getCcLast4() + "\n";
        responseText += "cardNo: " + payableSale.getCardNo() + "\n";
        responseText += "cardType: " + payableSale.getCardType() + "\n";
        responseText += "txId: " + payableSale.getTxId() + "\n";
        responseText += "terminalId: " + payableSale.getTerminalId() + "\n";
        responseText += "mid: " + payableSale.getMid() + "\n";
        responseText += "txnType: " + payableSale.getTxnType() + "\n";
        responseText += "txnStatus: " + payableSale.getTxnStatus() + "\n";
        responseText += "receiptSMS: " + payableSale.getReceiptSMS() + "\n";
        responseText += "receiptEmail: " + payableSale.getReceiptEmail() + "\n";
        responseText += "paymentMethod: " + payableSale.getPaymentMethod() + "\n";
        responseText += "message: " + payableSale.getMessage() + "\n";
        responseText += "orderTracking: " + payableSale.getOrderTracking() + "\n";

        updateTxtResponse(responseText);
    }

    private void updateTxtResponse(String message) {
        txtResponse.setText(txtResponse.getText().toString() + "\n" + message);
    }

    private void updateFreshTxtResponse(String message) {
        txtResponse.setText("");
        updateTxtResponse(message);
    }

    protected void hideSoftKeyboard(EditText input) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(input.getWindowToken(), 0);
    }
}
```

PAYable SDK Android Integration
