package com.gyl.CrudGyL.util.adapters;

import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;

@Component
public class Md5UrlCryptoAdapter implements UrlCryptoAdapter {

    @Override
    public String getAlgorithm() {
        return "md5";
    }

    @Override
    public String encrypt(String value) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            byte[] hash = messageDigest.digest(value.getBytes(StandardCharsets.UTF_8));
            return HexFormat.of().formatHex(hash);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("No se pudo generar el hash MD5.", e);
        }
    }

    @Override
    public String decrypt(String encryptedValue) {
        throw new UnsupportedOperationException("MD5 no se puede descifrar porque es un hash, no un cifrado reversible.");
    }
}
