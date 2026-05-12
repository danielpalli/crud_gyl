package com.gyl.CrudGyL.util.adapters;

public interface UrlCryptoAdapter {
    String getAlgorithm();

    String encrypt(String value);

    String decrypt(String encryptedValue);
}
