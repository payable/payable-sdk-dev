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

    /**
     * Maps a currency to its ISO alpha code. Accepts both the POS app's internal currency ids
     * (1 = LKR, 2 = USD, 3 = GBP, 4 = EUR) and ISO 4217 numeric codes (144, 840, 826, 978), which is
     * what the reversal APIs return.
     */
    public static String currencyToString(int currency) {
        switch (currency) {
            case 1:
            case 144:
                return "LKR";
            case 2:
            case 840:
                return "USD";
            case 3:
            case 826:
                return "GBP";
            case 4:
            case 978:
                return "EUR";
            default:
                return String.valueOf(currency);
        }
    }
}
