package com.first.myapp.com.myapplication.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by lynnliu on 9/25/16.
 */
public class SHA256Util {

    /**
     * encrypt string, default algorithm is SHA-256
     *
     * @param strSrc  the string to be encrypt
     * @param encName encrypt algorithm name
     * @return
     */
    public static String Encrypt(String strSrc, String encName) {
        String strDes = null;

        try {
            if (StringUtil.isEmpty(encName)) {
                encName = "SHA-256";
            }
            MessageDigest md = MessageDigest.getInstance(encName);
            md.update(strSrc.getBytes());
            strDes = ByteUtil.bytes2HexString(md.digest()); // to HexString
        } catch (NoSuchAlgorithmException e) {
//            e.printStackTrace();
            return null;
        }
        return strDes;
    }
}
