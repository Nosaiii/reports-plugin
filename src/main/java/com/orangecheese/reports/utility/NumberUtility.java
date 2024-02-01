package com.orangecheese.reports.utility;

public final class NumberUtility {
    public static int clamp(int value, int min, int max) {
        return Math.max(min, Math.min(max, value));
    }
}