# PAYable Android Integration
Android Application

### Initialization

1. Import the payablesdk.aar file
    - File > New > New Module > Import .JAR/.AAR Package > Select your payablesdk.aar file > Click finish
    - File > Project Structure > app > Dependencies > Add Button > Module dependency > Select payablesdk > Click Ok and Complete
    - Sync the project

2. Extend the class from your activity class
```java
import com.payable.sdk.Payable;
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

* Return Payable Object
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
```

* Return Status Codes
```java
Payable.PAYABLE_REQUEST_CODE : 3569;
Payable.PAYABLE_STATUS_SUCCESS : 222;
Payable.PAYABLE_STATUS_NOT_LOGIN : 555;
Payable.PAYABLE_STATUS_FAILED : 0;
Payable.PAYABLE_INVALID_AMOUNT : 999;
Payable.PAYABLE_APP_NOT_INSTALLED : 888;
```

* If you want to use it in your class without extending from Payable class
```java

public class InsideActivity extends AppCompatActivity implements PayableListener {

    EditText edtAmount;
    Button btnPay;
    TextView txtResponse;

    double saleAmount = 0;

    Payable payableClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        edtAmount = findViewById(R.id.edtAmount);
        btnPay = findViewById(R.id.btnPay);
        txtResponse = findViewById(R.id.txtResponse);

        payableClient = new Payable();

        btnPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                payableSale();
            }
        });

    }

    private void payableSale() {

        saleAmount = Double.parseDouble(edtAmount.getText().toString());

        payableClient.setClientName("NAME");
        payableClient.setClientId("456");
        payableClient.setSaleAmount(saleAmount);

        try {
            startActivityForResult(payableClient.getPaymentIntent(), Payable.PAYABLE_REQUEST_CODE);
        } catch (ActivityNotFoundException ex) {
            Toast.makeText(getApplicationContext(), "PAYABLE_APP_NOT_INSTALLED", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Payable.PAYABLE_REQUEST_CODE) {
            if (payableClient != null) {
                payableClient.setResponseCallback(data, this);
            }
        }
    }

    @Override
    public void onPaymentSuccess(Payable payable) {
        updateMyUI(payable);
    }

    @Override
    public void onPaymentFailure(Payable payable) {
        updateMyUI(payable);
    }

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

        txtResponse.setText(responseText);

    }
}
```





