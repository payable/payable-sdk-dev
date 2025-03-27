package com.payable.sdk;


import android.text.InputFilter;
import android.text.Spanned;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AmountInputFilter implements InputFilter {

    private final Pattern pattern;
    private final double maxAmount;
    private final Listener eventListener;

    public interface Listener {
        void onMaxReached(double amount);
    }

    public AmountInputFilter(int digitsBeforeDecimal, int digitsAfterDecimal, double maxAmount, Listener eventListener) {
        this.maxAmount = maxAmount;
        this.eventListener = eventListener;
        this.pattern = Pattern.compile("^\\d{0," + digitsBeforeDecimal + "}(\\.\\d{0," + digitsAfterDecimal + "})?$");
    }

    @Override
    public CharSequence filter(CharSequence source, int sourceStart, int sourceEnd, Spanned destination, int destinationStart, int destinationEnd) {
        // Construct the new string
        String newString = destination.subSequence(0, destinationStart) + source.toString() + destination.subSequence(destinationEnd, destination.length());

        // Allow input if it's just a single dot at start
        if (newString.equals(".")) {
            return "0."; // Auto-prepend 0 for single dot
        }

        // Validate against regex
        Matcher matcher = pattern.matcher(newString);
        if (!matcher.matches()) {
            return "";
        }

        // Max amount check
        try {
            double value = Double.parseDouble(newString);
            if (value > maxAmount) {
                if (eventListener != null) {
                    eventListener.onMaxReached(maxAmount);
                }
                return "";
            }
        } catch (NumberFormatException ignored) {
        }

        return null;
    }

    public static InputFilter[] getFilter(double maxAmount, Listener eventListener) {
        return new InputFilter[]{
                new AmountInputFilter(10, 2, maxAmount, eventListener)
        };
    }

    public static InputFilter[] getFilter(double maxAmount) {
        return AmountInputFilter.getFilter(maxAmount, null);
    }
}
