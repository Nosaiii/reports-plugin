package com.orangecheese.reports.utility;

public final class PlayerUtility {
    public static String uuidToDashedUuid(String uuid) {
        return uuid.replaceAll(
                "(\\w{8})(\\w{4})(\\w{4})(\\w{4})(\\w{12})",
                "$1-$2-$3-$4-$5");
    }
}