### PAYable SDK - Android Integration

![](https://i.imgur.com/ERpCDa7.png)

Android SDK - [android-sdk.payable.lk](https://android-sdk.payable.lk) | [Create Issue](https://github.com/payable/payable-sdk-dev/issues/new)

[![](https://jitpack.io/v/payable/payable-sdk-dev.svg)](https://jitpack.io/#payable/payable-sdk-dev) 

<hr>

### Initialization 

* Request and install **Sandbox** PAYable APP - Testing purpose

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
implementation 'com.github.payable:payable-sdk-dev:3.6.1'
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

<b>2.</b> Implement `PayableListener` and declare PAYable client in your class.

```java
public class MainActivity extends AppCompatActivity implements PayableListener {
    
    Payable payableClient;
    
    @Override
    boolean onPaymentStart(PayableSale payableSale){
        return true;
    }
    
    @Override
    void onPaymentSuccess(PayableSale payableSale){
        
    }
    
    @Override
    void onPaymentFailure(PayableSale payableSale){
        
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
ReadMe
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

##### * Return Payable Object

```java
payable.getStatusCode();
payable.getSaleAmount();
payable.getCcLast4();
payable.getCardType();
payable.getTxId();
payable.getTerminalId();
payable.getMid();
payable.getIsEmv();
payable.getTxnStatus();
payable.getReceiptSMS();
payable.getReceiptEmail();
payable.getOrderTracking();
```

##### * Return Status Codes

```javaReadMe
Payable.PAYABLE_REQUEST_CODE : 3569;
Payable.PAYABLE_STATUS_SUCCESS : 222;
Payable.PAYABLE_STATUS_NOT_LOGIN : 555;
Payable.PAYABLE_STATUS_FAILED : 0;
Payable.PAYABLE_INVALID_AMOUNT : 999;
Payable.PAYABLE_APP_NOT_INSTALLED : 888;
```

##### * Card Actions

```java
Payable.TXN_SWIPE : 0;
Payable.TXN_EMV : 1;
Payable.TXN_MANUAL : 2;
Payable.TXN_NFC : 3;
```

##### * Card Types

```java
VISA = 1;
AMEX = 2;
MASTER = 3;
DINERS = 4;
MAESTRO = 5;
CUP = 6;
JCB = 7;
```

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

* This method will be called in the background when the terminal listens to any card interactions such as ENV, SWIPE, and NFC, this will respond with your sale values and interacted action as `Payable.EMV, Payable.SWIPE, Payable.NFC` and -1 for any error on card interaction. You can get the error description using `payableSale.getMessage()` method.

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
});
```

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
| `boolean requestVoid(String txId, int cardType);` | `onVoid(PayableResponse payableResponse)`
| `boolean requestTransactionStatus(String txId, int cardType)` | `onTransactionStatus(PayableTxStatusResponse payableResponse)`
| `boolean requestTransactionStatusV2(String orderId, int cardType)` | `onTransactionStatus(PayableTxStatusResponseV2 payableResponse)`

<br/>

* `PayableProfile`

```java
String tid;
String name;
String currency;
Integer installment;
```

* `PAYableResponse`

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

<hr/>

##### Example Usage

```java
public class MainActivity extends AppCompatActivity implements PayableListener {

    EditText edtAmount, edtTracking, edtEmail, edtSMS, edtTxnId;
    Button btnPayCard, btnPayWallet, btnPay, btnProfile, btnVoid, btnStatus;
    TextView txtResponse, actTitle;

    double saleAmount = 0;
    String selectedProfile;

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
        btnPayCard = findViewById(R.id.btnPayCard);
        btnPayWallet = findViewById(R.id.btnPayWallet);
        btnPay = findViewById(R.id.btnPay);
        btnProfile = findViewById(R.id.btnProfile);
        btnVoid = findViewById(R.id.btnVoid);
        btnStatus = findViewById(R.id.btnStatus);
        txtResponse = findViewById(R.id.txtResponse);
        actTitle = findViewById(R.id.actTitle);
        actTitle.setText("Main Activity");

        edtAmount.setFilters(AmountInputFilter.getFilter(this, 100000));

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
