package br.com.dextra.dexboard.utils;

public class Config {
    public static String getProperty(String key, String defaultValue) {
        String value = System.getProperty(key);
        if (StringUtils.isNullOrEmpty(value)) {
            return defaultValue;
        }
        return value;
    }
}
