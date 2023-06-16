package com.squarecross.photoalbum.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class Encrypt {
    public String genSalt() {
        SecureRandom r = new SecureRandom();
        byte[] salt = new byte[15];

        r.nextBytes(salt);

        return getString(salt);
    }

    public String getEncrypt(String plain, String salt) {
        String result = null;

        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update((plain + salt).getBytes());
            byte[] digest = md.digest();
            result = getString(digest);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return result;
    }

    private String getString(byte[] bytes) {
        StringBuilder sb = new StringBuilder();

        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }

        return sb.toString();
    }
}
