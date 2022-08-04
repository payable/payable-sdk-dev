package com.payable.sdk;

import java.util.List;

public interface PayableEventListener {

    void onProfileList(List<PayableProfile> payableProfiles);

    void onVoid(PayableResponse payableResponse);

    void onTransactionStatus(PayableTxStatusResponse payableResponse);
}
