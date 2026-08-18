package com.trialinteractive.utils;

public final class TestConfig {

    private TestConfig() {
    }

    public static String getRequired(String key) {
        String value = System.getenv(key);
        if (value == null || value.isBlank()) {
            throw new IllegalStateException(
                    "Required environment variable is not set: " + key);
        }
        return value;
    }

    public static String getOptional(String key) {
        String value = System.getenv(key);
        return value == null ? "" : value.trim();
    }

    public static String getUrl() {
        return "https://stg-signin.trialinteractive.com/cas/login"
                + "?service=https%3A%2F%2Fsandbox-lms.trialinteractive.com%2Flogin%2Findex.php";
    }

    public static String getDashboardUrl() {
        return "https://sandbox-lms.trialinteractive.com/my/";
    }
}
