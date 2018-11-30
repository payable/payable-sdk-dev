# PAYable Android Integration
Android Application

### Initialization

1. Import the payablesdk.aar file
    - File > New > New Module > Import .JAR/.AAR Package
    - Select your payablesdk.aar file
    - Click finish
    - File > Project Structure > app > Dependencies > Add Button > Module dependency > Select payablesdk > Click Ok and Complete
    - Sync the project

2. import the payablesdk library and extend the class from your activity class

```java
import com.payable.sdk.Payable;

public class MainActivity extends Payable {}
```

On click listener call the method
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

Return Payable Object
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

Return Status Codes
```java
Payable.PAYABLE_REQUEST_CODE : 3569;
Payable.PAYABLE_STATUS_SUCCESS : 222;
Payable.PAYABLE_STATUS_NOT_LOGIN : 555;
Payable.PAYABLE_STATUS_FAILED : 0;
Payable.PAYABLE_INVALID_AMOUNT : 999;
Payable.PAYABLE_APP_NOT_INSTALLED : 888;
```
