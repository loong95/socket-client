package io.loong95.light.chat.socket;

/**
 * @author linyunlong
 */
public class ByteUtil {
    private static String HEX_STR = "0123456789ABCDEF";

    public static short toInt16(byte[] bytes, int offset) {
        short result = (short) ((int) bytes[offset] & 0xff);
        result |= ((int) bytes[offset + 1] & 0xff) << 8;
        return (short) (result & 0xffff);
    }

    public static int toUInt16(byte[] bytes, int offset) {
        int result = (int) bytes[offset + 1] & 0xff;
        result |= ((int) bytes[offset] & 0xff) << 8;
        return result & 0xffff;
    }

    public static int toInt32(byte[] bytes, int offset) {
        int result = (int) bytes[offset] & 0xff;
        result |= ((int) bytes[offset + 1] & 0xff) << 8;
        result |= ((int) bytes[offset + 2] & 0xff) << 16;
        result |= ((int) bytes[offset + 3] & 0xff) << 24;
        return result;
    }

    public static long toUInt32(byte[] bytes, int offset) {
        long result = (int) bytes[offset] & 0xff;
        result |= ((int) bytes[offset + 1] & 0xff) << 8;
        result |= ((int) bytes[offset + 2] & 0xff) << 16;
        result |= ((int) bytes[offset + 3] & 0xff) << 24;
        return result & 0xFFFFFFFFL;
    }

    public static long toInt64(byte[] buffer, int offset) {
        long values = 0;
        for (int i = 0; i < 8; i++) {
            values <<= 8;
            values |= (buffer[offset + i] & 0xFF);
        }
        return values;
    }

    public static long toUInt64(byte[] bytes, int offset) {
        long result = 0;
        for (int i = 0; i <= 56; i += 8) {
            result |= ((int) bytes[offset++] & 0xff) << i;
        }
        return result;
    }

    public static float toFloat(byte[] bs, int index) {
        return Float.intBitsToFloat(toInt32(bs, index));
    }

    public static double toDouble(byte[] arr, int offset) {
        return Double.longBitsToDouble(toUInt64(arr, offset));
    }

    public static boolean toBoolean(byte[] bytes, int offset) {
        return bytes[offset] != 0x00;
    }

    public static byte[] getBytes(short value) {
        byte[] bytes = new byte[2];
        bytes[0] = (byte) (value & 0xff);
        bytes[1] = (byte) ((value & 0xff00) >> 8);
        return bytes;
    }

    public static byte[] getBytes(int value) {
        byte[] bytes = new byte[4];
        bytes[0] = (byte) ((value) & 0xFF); //最低位
        bytes[1] = (byte) ((value >> 8) & 0xFF);
        bytes[2] = (byte) ((value >> 16) & 0xFF);
        bytes[3] = (byte) ((value >>> 24)); //最高位，无符号右移
        return bytes;
    }

    public static byte[] getBytes(long values) {
        byte[] buffer = new byte[8];
        for (int i = 0; i < 8; i++) {
            int offset = 64 - (i + 1) * 8;
            buffer[i] = (byte) ((values >> offset) & 0xff);
        }
        return buffer;
    }

    public static byte[] getBytes(float value) {
        return getBytes(Float.floatToIntBits(value));
    }

    public static byte[] getBytes(double val) {
        long value = Double.doubleToLongBits(val);
        return getBytes(value);
    }

    public static byte[] getBytes(boolean value) {
        return new byte[]{(byte) (value ? 1 : 0)};
    }

    public static byte intToByte(int x) {
        return (byte) x;
    }

    public static int byteToInt(byte b) {
        return b & 0xFF;
    }

    public static char toChar(byte[] bs, int offset) {
        return (char) (((bs[offset] & 0xFF) << 8) | (bs[offset + 1] & 0xFF));
    }

    public static byte[] getBytes(char value) {
        byte[] b = new byte[2];
        b[0] = (byte) ((value & 0xFF00) >> 8);
        b[1] = (byte) (value & 0xFF);
        return b;
    }

    public static byte[] concat(byte[]... bs) {
        int len = 0, idx = 0;
        for (byte[] b : bs) len += b.length;
        byte[] buffer = new byte[len];
        for (byte[] b : bs) {
            System.arraycopy(b, 0, buffer, idx, b.length);
            idx += b.length;
        }
        return buffer;
    }

    public static byte[] binaryStringToBytes(String binaryString) {
        if (binaryString == null || !binaryString.matches("^[0-9a-eA-E]+$")) {
            throw new IllegalArgumentException("输入参数不是十六进制字符串：" + binaryString);
        }
        return numberStringToBytes(binaryString, 2, 8);
    }

    public static byte[] hexStringToBytes(String hexString) {
        if (hexString == null || !hexString.matches("^[0-9a-fA-F]+$")) {
            throw new IllegalArgumentException("输入参数不是十六进制字符串：" + hexString);
        }
        return numberStringToBytes(hexString, 16, 2);
    }

    private static byte[] numberStringToBytes(String hexString, int radix, int byteStringLength) {
        int remainLength = hexString.length();
        byte[] bytes = new byte[(int) Math.ceil(remainLength / (float) byteStringLength)];
        int byteIndex = bytes.length;
        while (remainLength >= byteStringLength) {
            byte b = (byte) Integer.parseInt(hexString.substring(remainLength - byteStringLength, remainLength), radix);
            bytes[--byteIndex] = b;
            remainLength -= byteStringLength;
        }
        if (remainLength > 0) {
            bytes[--byteIndex] = (byte) Integer.parseInt(hexString.substring(0, remainLength), radix);
        }
        return bytes;
    }

    public static String bytesToHexString(byte[] bytes) {
        StringBuilder result = new StringBuilder();
        for (byte aByte : bytes) {
            result.append(HEX_STR.charAt((aByte & 0xF0) >> 4)).append(HEX_STR.charAt(aByte & 0x0F));
        }
        return result.toString();
    }
}
