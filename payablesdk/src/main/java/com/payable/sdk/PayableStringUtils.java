package com.payable.sdk;

import static com.payable.sdk.Payable.STATUS_CLOSE;
import static com.payable.sdk.Payable.STATUS_CLOSE_VOID;
import static com.payable.sdk.Payable.STATUS_OPEN;
import static com.payable.sdk.Payable.STATUS_OPEN_VOID;

public class PayableStringUtils {

    public static String cardTypeToString(int cardType) {

        if (cardType == Payable.CARD_TYPE_VISA) {
            return "VISA";
        } else if (cardType == Payable.CARD_TYPE_MASTER) {
            return "MASTER";
        } else if (cardType == Payable.CARD_TYPE_AMEX) {
            return "AMEX";
        } else if (cardType == Payable.CARD_TYPE_CUP) {
            return "CUP";
        } else if (cardType == Payable.CARD_TYPE_JCB) {
            return "JCB";
        } else {
            return String.valueOf(cardType);
        }
    }

    public static int cardTypeInt(String cardType) {

        if (cardType == null) return 0;

        if (cardType.equals(cardTypeToString(Payable.CARD_TYPE_VISA))) {
            return Payable.CARD_TYPE_VISA;
        } else if (cardType.equals(cardTypeToString(Payable.CARD_TYPE_MASTER))) {
            return Payable.CARD_TYPE_MASTER;
        } else if (cardType.equals(cardTypeToString(Payable.CARD_TYPE_AMEX))) {
            return Payable.CARD_TYPE_AMEX;
        } else if (cardType.equals(cardTypeToString(Payable.CARD_TYPE_CUP))) {
            return Payable.CARD_TYPE_CUP;
        } else if (cardType.equals(cardTypeToString(Payable.CARD_TYPE_JCB))) {
            return Payable.CARD_TYPE_JCB;
        } else {
            return 0;
        }
    }

    public static String statusToString(int status) {
        switch (status) {
            case STATUS_OPEN:
                return "Open";
            case STATUS_CLOSE:
                return "Close";
            case STATUS_OPEN_VOID:
                return "Open Void";
            case STATUS_CLOSE_VOID:
                return "Close Void";
            default:
                return String.valueOf(status);
        }
    }
}
