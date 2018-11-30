# PAYable Android Integration
Android Application

### Initialization
1. Copy this code to your string.xml file in resources directory and change it with your information

```xml
<string name="payable_client_id">Kangaroo</string>
<string name="payable_client_name">155636</string>
```

2. Copy Payable.java & PayableListener.java and extend your activity class from Payable.java, makesure Payable.java can access your R.java file in order to access your string.xml
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

Return Status Codes
```java
Payable.PAYABLE_REQUEST_CODE : 3569;
Payable.PAYABLE_STATUS_SUCCESS : 222;
Payable.PAYABLE_STATUS_NOT_LOGIN : 555;
Payable.PAYABLE_STATUS_FAILED : 0;
Payable.PAYABLE_INVALID_AMOUNT : 999;
Payable.PAYABLE_APP_NOT_INSTALLED : 888;
```
