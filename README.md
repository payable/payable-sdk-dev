# PAYable Android Integration
Android Application

### Initialization
Copy this code to string.xml file in resources directory and change your information
:
```xml
<string name="payable_client_id">Kangaroo</string>
<string name="payable_client_name">155636</string>
```

Extend your Activity class from Payable class
```java
public class MainActivity extends Payable {}
```

On click listener call the method 
```java
private void payableSale() {
    // Set your EditText value
    saleAmount = Double.parseDouble(edtAmount.getText().toString());
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

Return Status codes
```java
PAYABLE_REQUEST_CODE = 3569;
PAYABLE_STATUS_SUCCESS = 222;
PAYABLE_STATUS_NOT_LOGIN = 555;
PAYABLE_STATUS_FAILED = 0;
PAYABLE_INVALID_AMOUNT = 999;
PAYABLE_APP_NOT_INSTALLED = 888;
```
