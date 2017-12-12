package com.first.myapp.com.myapplication.util;

import java.io.IOException;
import java.math.BigDecimal;

/**
 * Created by lynnliu on 1/14/16.
 */
public class ByteUtil {

    public static String bytes2HexString(byte[] src) {
        StringBuilder stringBuilder = new StringBuilder("");
        if (src == null || src.length <= 0) {
            return null;
        }
        for (int i = 0; i < src.length; i++) {
            int v = src[i] & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
            stringBuilder.append(" ");
        }
        return stringBuilder.toString().toUpperCase();
    }

    public static String bytes2NumberString(byte[] src) {
        return bytes2NumberString(src, ".", false);
    }

    public static String bytes2NumberString(byte[] src, String separateSign, boolean ifAddZero) {
        StringBuilder stringBuilder = new StringBuilder("");
        if (src == null || src.length <= 0) {
            return null;
        }
        for (int i = 0; i < src.length; i++) {
            int v = src[i] & 0xFF;
            String hv = String.valueOf(v);
            if (ifAddZero && hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
            if (i != src.length - 1) {
                stringBuilder.append(separateSign);
            }
        }
        return stringBuilder.toString().toUpperCase();
    }

    public static byte[] short2Bytes(short s) {
        byte[] bytes = new byte[2];
        for (int i = 1; i >= 0; i--) {
            bytes[i] = (byte) (s % 256);
            s >>= 8;
        }
        return bytes;
    }

    public static byte[] int2Bytes(int s) {
        byte[] bytes = new byte[4];
        for (int i = 3; i >= 0; i--) {
            bytes[i] = (byte) (s % 256);
            s >>= 8;
        }
        return bytes;
    }

    public static byte[] int2Bytes(int value, boolean isLittleEndian) {
        byte[] bytes = new byte[4];
        if (isLittleEndian) {
            bytes[3] = (byte) ((value >> 24) & 0xFF);
            bytes[2] = (byte) ((value >> 16) & 0xFF);
            bytes[1] = (byte) ((value >> 8) & 0xFF);
            bytes[0] = (byte) (value & 0xFF);
        } else {
            bytes[0] = (byte) ((value >> 24) & 0xFF);
            bytes[1] = (byte) ((value >> 16) & 0xFF);
            bytes[2] = (byte) ((value >> 8) & 0xFF);
            bytes[3] = (byte) (value & 0xFF);
        }

        return bytes;
    }


    public static int bytes2Int(byte[] b, int start, boolean isLittleEndian) {
        return (int) transform(b, start, 4, isLittleEndian);
    }

    public static long bytes2UInt(byte[] b, int start, boolean isLittleEndian) {
        return transform(b, start, 4, isLittleEndian);
    }

    public static long bytes2Long(byte[] b, int start, boolean isLittleEndian) {
        return transform(b, start, 8, isLittleEndian);
    }

    public static long bytes2LongEX(byte[] b, int start, int len, boolean isLittleEndian) {
        return transform(b, start, len, isLittleEndian);
    }

    public static short bytes2Short(byte[] b, int start, boolean isLittleEndian) {
        return (short) (transform(b, start, 2, isLittleEndian));
    }

    public static int bytes2UShort(byte[] b, int start, boolean isLittleEndian) {
        return (int) (transform(b, start, 2, isLittleEndian));
    }

    public static byte bytes2Byte(byte[] b, int start) {
        return (byte) transform(b, start, 1, false);
    }

    public static int bytes2UByte(byte[] b, int start) {
        return (int) transform(b, start, 1, false);
    }

    public static byte[] subByteArray(byte[] bytes, int start, int len) {
        byte[] returnBytes = new byte[len];
        System.arraycopy(bytes, start, returnBytes, 0, len);
        return returnBytes;
    }

    public static long transform(byte[] bytes, int offset, int length, boolean isLittleEndian) {
        long value = 0;
        if (isLittleEndian) {
            for (int i = 0; i < length; i++) {
                int shift = i * 8;
                value = value | ((bytes[i + offset] & 0x00000000000000FF) << shift);
            }
        } else {
            for (int i = 0; i < length; i++) {
                int shift = (length - 1 - i) * 8;
                value = value | ((bytes[i + offset] & 0x00000000000000FF) << shift);
            }
        }

        return value;
    }

    public static String byteArrayToCharString(byte[] b) {
        if (b == null) return "";
        StringBuilder retStr = new StringBuilder();
        for (int i = 0; i < b.length && b[i] != 0; i++) {
            retStr.append((char) b[i]);
        }

        return retStr.toString();
    }

    public static String byteArrayToString(byte[] b) {
        if (b == null) return "";
        StringBuffer retStr = new StringBuffer();
        for (int i = 0; i < b.length; i++) {
            if (b[i] != 0) {
                retStr.append((char) (b[i]));
            } else {
                break;
            }
        }
        return retStr.toString();
    }


    public static BigDecimal long2ULong(long value) throws IOException {
        if (value >= 0)
            return new BigDecimal(value);
        long lowValue = value & 0x7fffffffffffffffL;
        return BigDecimal.valueOf(lowValue).add(BigDecimal.valueOf(Long.MAX_VALUE)).add(BigDecimal.valueOf(1));
    }

    public static byte[] subByteArrayByChar(byte[] b, char c) {
        byte[] des = null;
        for (int i = 0; i < b.length; i++) {
            if (b[i] == c) {
                des = new byte[i];
                System.arraycopy(b, 0, des, 0, i);
                break;
            }
        }
        if (des == null) {
            return b;
        }
        return des;
    }

    public static float byte2Float(byte[] b, int offset) {
        int l;
        l = b[offset + 0];
        l &= 0xff;
        l |= ((long) b[offset + 1] << 8);
        l &= 0xffff;
        l |= ((long) b[offset + 2] << 16);
        l &= 0xffffff;
        l |= ((long) b[offset + 3] << 24);
        return Float.intBitsToFloat(l);
    }

    public static byte[] float2Bytes(float f) {
        int fbit = Float.floatToIntBits(f);

        byte[] b = new byte[4];
        for (int i = 0; i < 4; i++) {
            b[i] = (byte) (fbit >> (24 - i * 8));
        }
        int len = b.length;
        byte[] dest = new byte[len];
        System.arraycopy(b, 0, dest, 0, len);
        byte temp;
        for (int i = 0; i < len / 2; ++i) {
            temp = dest[i];
            dest[i] = dest[len - i - 1];
            dest[len - i - 1] = temp;
        }

        return dest;
    }

    public static int firstIndexOf(byte[] data, byte b) {
        for (int i = 0; i < data.length; i++) {
            if (data[i] == b) {
                return i;
            }
        }
        return -1;
    }

    public static byte[] string2UnicodeBytes(String str, boolean isLittleEndian) {
        str = (str == null ? "" : str);
        byte[] bytes = new byte[str.length() * 2];
        char c;
        for (int i = 0; i < str.length(); i++) {
            c = str.charAt(i);
            if (isLittleEndian) {
                bytes[2 * i + 1] = (byte) (c >>> 8);
                bytes[2 * i] = (byte) (c & 0xFF);
            } else {
                bytes[2 * i] = (byte) (c >>> 8);
                bytes[2 * i + 1] = (byte) (c & 0xFF);
            }

        }
        return bytes;
    }

    public static String unicodeBytes2String(byte[] bytes, boolean isLittleEndian) {
        StringBuffer sb = new StringBuffer("");
        char c;
        for (int i = 0; i < bytes.length / 2; i++) {
            if (isLittleEndian) {
                c = (char) (bytes[2 * i + 1] << 8 | (bytes[2 * i] & 0x00ff));
            } else {
                c = (char) (bytes[2 * i] << 8 | (bytes[2 * i + 1] & 0x00ff));
            }
            sb.append(c);
        }
        return sb.toString();
    }
}
