package com.lab.myattendance.utilies;

import android.util.Base64;

import java.security.MessageDigest;
import java.util.Arrays;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * This class is used by QR scanner to Encrypt,Decrypt the data
 */
public class EncryptionHelper {

    private static EncryptionHelper encryptionHelper = null;
    private String encryptionKey;

    private EncryptionHelper() {

    }

    public static EncryptionHelper getInstance() {
        if (encryptionHelper == null) {
            encryptionHelper = new EncryptionHelper();
        }
        return encryptionHelper;
    }

    public String encryptMsg() {
        return Base64.encodeToString(encryptionKey.getBytes(), Base64.DEFAULT);
    }

    public EncryptionHelper encryptionString(String encryptionKey) {
        this.encryptionKey = encryptionKey;
        return encryptionHelper;
    }

    public String getDecryptionString(String encryptedText) {
        return new String(Base64.decode(encryptedText.getBytes(), Base64.DEFAULT));
    }
}
