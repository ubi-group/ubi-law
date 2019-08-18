package com.itcag.util;

import java.security.Key;

import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

/**
 * Encrypts/decrypts strings. Primarily designed for encrypting/decrypting authorization data for storage in files and databases.
 * @author IT Consulting Alicja Grużdź (Nahum)
 */
public final class Encryptor {

    private static final byte[] KEY = "3c526c65e94049ea".getBytes();
    private static final String ALGORITHM = "AES";

    public final static String encrypt(String input) throws Exception {
        
        Key key = generateKey();
        
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, key);
        
        byte[] encValue = cipher.doFinal(input.getBytes());

        Base64.Encoder encoder = Base64.getEncoder();
        String retVal = encoder.encodeToString(encValue);
        
        return retVal;
    
    }

    public final static String decrypt(String input) throws Exception {
    
        Key key = generateKey();
        
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, key);
        
        Base64.Decoder decoder = Base64.getDecoder();
        byte[] decodedValue = decoder.decode(input);

        byte[] decValue = cipher.doFinal(decodedValue);

        String retVal = new String(decValue);
        return retVal;
    
    }

    private static Key generateKey() throws Exception {
        
        Key key = new SecretKeySpec(KEY, ALGORITHM);
        return key;
    
    }

}
