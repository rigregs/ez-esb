package com.opnitech.esb.processor.utils;

import java.security.MessageDigest;

import org.apache.commons.lang3.StringUtils;

/**
 * @author Rigre Gregorio Garciandia Sonora
 */
public final class CheckSumUtil {

    private CheckSumUtil() {
        // Default constructor
    }

    public static String checkSum(String value) {

        try {
            if (StringUtils.isBlank(value)) {
                return null;
            }

            byte[] valueBytes = value.getBytes();

            MessageDigest md = MessageDigest.getInstance("SHA1");
            md.update(valueBytes, 0, valueBytes.length);

            byte[] mdbytes = md.digest();

            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < mdbytes.length; i++) {
                sb.append(Integer.toString((mdbytes[i] & 0xff) + 0x100, 16).substring(1));
            }

            return sb.toString();
        }
        catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }
}
