### PAYable SDK - Android Integration

<img width=200 src="https://i.imgur.com/QCiiqMU.png" /> <br>

Android SDK - https://github.com/payable/payable-sdk-dev <br/>
<a target="_blank" href="https://github.com/payable/payable-sdk-dev/issues/new">Create Issue</a>

[![](https://jitpack.io/v/payable/payable-sdk-dev.svg)](https://jitpack.io/#payable/payable-sdk-dev) [![](https://gitlab.com/payable-team/payable-sdk/badges/master/pipeline.svg)](https://gitlab.com/payable-team/payable-sdk/pipelines)

<hr>

### Initialization 

* For PAX Terminal, Download <a href="https://gitlab.com/payable/payable_android_itegration/raw/master/PAYable/payable-pax.apk" target="_blank">payable-pax.apk</a> file 
* For Android Mobile, Download <a href="https://gitlab.com/payable/payable_android_itegration/raw/master/PAYable/payable.apk" target="_blank">payable.apk</a> file

<b>Step 1.</b> Add it in your root build.gradle at the end of repositories:
```gradle
allprojects {
    repositories {
        ...
        maven { url 'https://jitpack.io' }
    }
}
```

<b>Step 2.</b> Add the dependency in your app level gradle file:
```gradle
dependencies {
    implementation 'com.github.payable:payable-sdk-dev:2.0.0'
}
```

> Sync the project before implementation <br>
 <a target="_blank" href="https://stackoverflow.com/questions/19932793/syncing-android-studio-project-with-gradle-files">How to Sync Android Studio Project</a>

<hr>

### Implementation

<b>Step 1.</b> Extend the class from your activity class
```java
import com.payable.sdk.Payable;
import com.payable.sdk.PayableListener;

public class MainActivity extends Payable {}
```

* On click listener call the method

```java
private void payableSale() {

    // Set your EditText value
    saleAmount = Double.parseDouble(edtAmount.getText().toString());
    setClientId("YOUR_ID");
    setClientName("YOUR_NAME");
    
    startPayment(saleAmount, new PayableListener() {
        @Override
        public void onPaymentSuccess(Payable payable) {
            updateMyUI(payable);
        }
        @Override
        public void onPaymentFailure(Payable payable) {
            if(payable.getStatusCode() == PAYABLE_APP_NOT_INSTALLED) {
                Toast.makeText(getApplicationContext(), "PAYABLE_APP_NOT_INSTALLED", Toast.LENGTH_LONG).show();
            }
            else {
                //updateMyUI(payable);
            }
        }
    });
}
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

##### * If you want to use it in your class without extending from Payable class
```java
public class InsideActivity extends AppCompatActivity implements PayableListener {

    EditText edtAmount;
    Button btnPay;
    TextView txtResponse;

    double saleAmount = 0;

    // 1. Declare Payable Client
    Payable payableClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        edtAmount = findViewById(R.id.edtAmount);
        btnPay = findViewById(R.id.btnPay);
        txtResponse = findViewById(R.id.txtResponse);

        // 2. Set Payable Client
        payableClient = new Payable();

        btnPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // 3. Call your method
                payableSale();
            }
        });

    }

    private void payableSale() {

        // 4. Convert sale amount to double from EditText
        saleAmount = Double.parseDouble(edtAmount.getText().toString());

        // 5. Set client id, name and amount to the Payable object
        payableClient.setClientName("NAME");
        payableClient.setClientId("456");
        payableClient.setSaleAmount(saleAmount);

        try {

            // 6. Start the activity
            startActivityForResult(payableClient.getPaymentIntent(), Payable.PAYABLE_REQUEST_CODE);

        } catch (ActivityNotFoundException ex) {

            // If App is not installed handle here
            Toast.makeText(getApplicationContext(), "PAYABLE_APP_NOT_INSTALLED", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // 7. onActivityResult set the callback listener to handle the response
        if (requestCode == Payable.PAYABLE_REQUEST_CODE) {
            if (payableClient != null) {
                payableClient.setResponseCallback(data, this);
            }
        }
    }

    // 8. onPaymentSuccess method
    @Override
    public void onPaymentSuccess(Payable payable) {
        updateMyUI(payable);
    }

    // 9. onPaymentFailure method
    @Override
    public void onPaymentFailure(Payable payable) {
        updateMyUI(payable);
    }

    // 10. Update..
    private void updateMyUI(Payable payable) {

        String responseText = "statusCode: " + payable.getStatusCode() + "\n";
        responseText += "responseAmount: " + payable.getSaleAmount() + "\n";
        responseText += "ccLast4: " + payable.getCcLast4() + "\n";
        responseText += "cardType: " + payable.getCardType() + "\n";
        responseText += "txId: " + payable.getTxId() + "\n";
        responseText += "terminalId: " + payable.getTerminalId() + "\n";
        responseText += "mid: " + payable.getMid() + "\n";
        responseText += "isEmv: " + payable.getIsEmv() + "\n";
        responseText += "txnStatus: " + payable.getTxnStatus() + "\n";
        responseText += "receiptSMS: " + payable.getReceiptSMS() + "\n";
        responseText += "receiptEmail: " + payable.getReceiptEmail() + "\n";

        txtResponse.setText(responseText);

    }
}
```

PAYable SDK Android Integration
