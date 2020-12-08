### PAYable SDK - Android Integration

![](https://i.imgur.com/ERpCDa7.png)

Android SDK - [android-sdk.payable.lk](https://android-sdk.payable.lk) | [Create Issue](https://github.com/payable/payable-sdk-dev/issues/new)

[![Build Status](https://travis-ci.com/payable/payable-sdk-dev.svg?branch=master)](https://travis-ci.com/payable/payable-sdk-dev)
[ ![Download](https://api.bintray.com/packages/payable/android/pay/images/download.svg) ](https://bintray.com/payable/android/pay/link)
[![](https://jitpack.io/v/payable/payable-sdk-dev.svg)](https://jitpack.io/#payable/payable-sdk-dev) 

<hr>

### Initialization 

* Request and install **Sandbox** PAYable APP - Testing purpose

<b>Step 1.</b> Add the dependency in your app level gradle file:
```gradle
dependencies {
    implementation 'com.payable:pay:2.0.4'
}
```

> <b>Deprecated</b> : The `com.github.payable:payable-sdk-dev'` module is deprecated and we recommend to implement our latest `com.payable:pay` module and remove the deprecated implementation from your `build.gradle` if there is already.

<hr>

### Implementation

<b>1.</b> Import PAYable SDK package.

```java
import com.payable.sdk.Payable;
import com.payable.sdk.PayableListener;
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
payableClient.startPayment(sale_amount: Double, payment_method: Integer, payable_listener: PayableListener);
```

If you want to track the sale or need to pass custom data and receive it on payment completion, use this method.

```java 
payableClient.startPayment(sale_amount: Double, payment_method: Integer, json_data: String, payable_listener: PayableListener);
```

* Payment methods

```java
Payable.METHOD_ANY
Payable.METHOD_CARD
Payable.METHOD_WALLET
```

Example:

```java
payableClient.startPayment(500.50, Payable.METHOD_ANY, this);
```

* For the order tracking you need to pass the tracking number in json data as below.

```java
payableClient.startPayment(500.50, Payable.METHOD_ANY, "{ \"ORDER_TRACKING\" : \"123455\" }", this);
```

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
```

##### * Return Status Codes
```java
Payable.PAYABLE_REQUEST_CODE : 3569;
Payable.PAYABLE_STATUS_SUCCESS : 222;
Payable.PAYABLE_STATUS_NOT_LOGIN : 555;
Payable.PAYABLE_STATUS_FAILED : 0;
Payable.PAYABLE_INVALID_AMOUNT : 999;
Payable.PAYABLE_APP_NOT_INSTALLED : 888;
```

##### * Example Activity
```java
public class MainActivity extends AppCompatActivity implements PayableListener {

    EditText edtAmount;
    Button btnPayCard, btnPayWallet, btnPay;
    TextView txtResponse, actTitle;

    double saleAmount = 0;

    // 1. Declare Payable Client
    Payable payableClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        edtAmount = findViewById(R.id.edtAmount);
        btnPayCard = findViewById(R.id.btnPayCard);
        btnPayWallet = findViewById(R.id.btnPayWallet);
        btnPay = findViewById(R.id.btnPay);
        txtResponse = findViewById(R.id.txtResponse);
        actTitle = findViewById(R.id.actTitle);
        actTitle.setText("Main Activity");

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
    }

    private void payableSale(int paymentMethod) {

        // 4. Convert sale amount to double from EditText
        saleAmount = Double.parseDouble(edtAmount.getText().toString());

        // 5. start the payment request to PAYable app with the callback listener { "ORDER_TRACKING" : "123455" }
        // payableClient.startPayment(saleAmount, paymentMethod,  json\" : \"123455\" }", this);
        payableClient.startPayment(saleAmount, paymentMethod, this);
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
        return true;
    }

    // 8. onPaymentSuccess method
    @Override
    public void onPaymentSuccess(PayableSale payableSale) {
        updateMyUI(payableSale);
    }

    // 9. onPaymentFailure method
    @Override
    public void onPaymentFailure(PayableSale payableSale) {
        updateMyUI(payableSale);
    }

    // 10. Update..
    private void updateMyUI(PayableSale payableSale) {

        String responseText = "statusCode: " + payableSale.getStatusCode() + "\n";
        responseText += "responseAmount: " + payableSale.getSaleAmount() + "\n";
        responseText += "ccLast4: " + payableSale.getCcLast4() + "\n";
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

        txtResponse.setText(responseText);
    }
}
```

PAYable SDK Android Integration
