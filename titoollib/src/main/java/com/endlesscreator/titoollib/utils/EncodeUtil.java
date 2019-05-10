package com.endlesscreator.titoollib.utils;

import android.util.Base64;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.security.MessageDigest;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class EncodeUtil {

    private static final String TAG = EncodeUtil.class.getName();

    /**
     * MD5 加密
     */
    public static String encodeMD5(String aStr) {
        return aStr == null ? null : encodeMD5(aStr.getBytes());
    }

    /**
     * MD5 加密
     */
    public static String encodeMD5(byte[] aInput) {
        char lHexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
        try {
            //获得MD5摘要算法的 MessageDigest 对象
            MessageDigest lMessageDigest = MessageDigest.getInstance("MD5");
            //使用指定的字节更新摘要
            lMessageDigest.update(aInput);
            //获得密文
            byte[] lDigest = lMessageDigest.digest();
            //把密文转换成十六进制的字符串形式
            int llDigestLength = lDigest.length;
            char lOut[] = new char[llDigestLength * 2];
            int k = 0;
            for (int i = 0; i < llDigestLength; i++) {
                byte lData = lDigest[i];
                lOut[k++] = lHexDigits[lData >>> 4 & 0xf];
                lOut[k++] = lHexDigits[lData & 0xf];
            }
            return new String(lOut);
        } catch (Exception e) {
            LogUtil.e(TAG, e);
        }
        return "";
    }

    /**
     * 加密数据
     *
     * @param aInput
     * @param aKeyRule
     * @return
     */
    public static byte[] encodeDES(byte[] aInput, String aKeyRule) {
        try {
            //数据长度只能为8的倍数
            aInput = normalizationDESInput(aInput);
            SecretKeySpec key = new SecretKeySpec(getKey(aKeyRule), "DES");
            Cipher cipher = Cipher.getInstance("DES/ECB/NoPadding");
            cipher.init(Cipher.ENCRYPT_MODE, key);
            return cipher.doFinal(aInput);
        } catch (Exception e) {
            LogUtil.e(TAG, e);
            return null;
        }
    }

    private static byte[] normalizationDESInput(byte[] aInput) {
        String[] lBB = {"00", "01", "02", "03", "04", "05", "06", "07", "08"};
        int lAddLen = 8 - aInput.length % 8;
        String lAddHexString = "";
        for (int i = 0; i < lAddLen; i++) {
            lAddHexString += lBB[lAddLen];
        }
        byte[] lAddBytes = EncodeUtil.hexStringToByte(lAddHexString);
        byte[] lResult = new byte[aInput.length + lAddBytes.length];
        System.arraycopy(aInput, 0, lResult, 0, aInput.length);
        System.arraycopy(lAddBytes, 0, lResult, aInput.length, lAddBytes.length);
        return lResult;
    }

    /**
     * 自定义一个key
     *
     * @param keyRule
     */
    private static byte[] getKey(String keyRule) {
        byte[] keyByte = keyRule.getBytes();
        // 创建一个空的八位数组,默认情况下为0
        byte[] byteTemp = new byte[8];
        // 将用户指定的规则转换成八位数组
        for (int i = 0; i < byteTemp.length && i < keyByte.length; i++) {
            byteTemp[i] = keyByte[i];
        }
        return new SecretKeySpec(byteTemp, "DES").getEncoded();
    }

    /**
     * 把字节数组转换成16进制字符串
     */
    private static String bytesToHexString(byte[] bArray) {
        if (bArray == null) {
            return "";
        }
        StringBuffer sb = new StringBuffer(bArray.length);
        String sTemp;
        for (int i = 0; i < bArray.length; i++) {
            sTemp = Integer.toHexString(0xFF & bArray[i]);
            if (sTemp.length() < 2)
                sb.append(0);
            sb.append(sTemp.toUpperCase());
        }
        return sb.toString();
    }

    /**
     * 把16进制字符串转换成字节数组
     */
    private static byte[] hexStringToByte(String hex) {
        int len = (hex.length() / 2);
        byte[] result = new byte[len];
        char[] achar = hex.toCharArray();
        for (int i = 0; i < len; i++) {
            int pos = i * 2;
            result[i] = (byte) (toByte(achar[pos]) << 4 | toByte(achar[pos + 1]));
        }
        return result;
    }

    /***
     * 解密数据
     */
    public static byte[] decodeDES(byte[] aInput, String aKeyRule) {
        try {
            SecretKeySpec key = new SecretKeySpec(getKey(aKeyRule), "DES");
            Cipher cipher = Cipher.getInstance("DES/ECB/NoPadding");
            cipher.init(Cipher.DECRYPT_MODE, key);
            byte[] lOutputBytes = cipher.doFinal(aInput);
            return normalizationDESOutput(lOutputBytes);
        } catch (Exception e) {
            LogUtil.e(TAG, e);
            return null;
        }
    }

    private static byte[] normalizationDESOutput(byte[] aOutput) {
        try {
            String lOutputHex = bytesToHexString(aOutput);
            int lEndRemoveCount = Integer.parseInt(lOutputHex.substring(lOutputHex.length() - 2, lOutputHex.length())) * 2;
            return hexStringToByte(lOutputHex.substring(0, lOutputHex.length() - lEndRemoveCount));
        } catch (Exception e) {
            LogUtil.e(TAG, e);
        }
        return aOutput;
    }

    private static byte toByte(char c) {
        return (byte) "0123456789ABCDEF".indexOf(c);
    }

    public static byte[] encodeBase64(byte[] input) {
        return Base64.encode(input, Base64.DEFAULT);
    }

    public static byte[] decodeBase64(byte[] input) {
        return Base64.decode(input, Base64.DEFAULT);
    }

    /**
     * 方法说明：解压ZIP的方法
     * @param aTargetDir 解压缩的目标目录
     */
    public static void unzip(String aZipFile, String aTargetDir) {
        try {
            BufferedOutputStream lOutputStream; // 缓冲输出流
            ZipInputStream lInputStream = new ZipInputStream(new BufferedInputStream(new FileInputStream(aZipFile)));

            int lBufferSize = 4 * 1024; // 这里缓冲区我们使用4KB，
            ZipEntry lItemEntry; // 每个zip条目的实例
            String lItemName; // 保存每个zip的条目名称
            while ((lItemEntry = lInputStream.getNextEntry()) != null) {
                try {
                    LogUtil.i(TAG, "unzip lItemEntry = " + lItemEntry);
                    lItemName = lItemEntry.getName();

                    File lEntryFile = new File(aTargetDir + "/" + lItemName);
                    File lEntryDir = new File(lEntryFile.getParent());
                    if (!lEntryDir.exists()) {
                        lEntryDir.mkdirs();
                    }

                    lOutputStream = new BufferedOutputStream(new FileOutputStream(lEntryFile), lBufferSize);
                    int lCount;
                    byte lData[] = new byte[lBufferSize];
                    while ((lCount = lInputStream.read(lData, 0, lBufferSize)) != -1) {
                        lOutputStream.write(lData, 0, lCount);
                    }
                    lOutputStream.flush();
                    lOutputStream.close();
                } catch (Exception e) {
                    LogUtil.e(TAG, e);
                }
            }
            lInputStream.close();
        } catch (Exception e) {
            LogUtil.e(TAG, e);
        }
    }

    /**
     * 读取文本文件中的内容
     */
    public static String getFileText(String aFilePath) {
        String lResult = "";
        try {
            FileInputStream lInputStream = new FileInputStream(aFilePath);
            byte[] lBuffer = new byte[lInputStream.available()];
            lInputStream.read(lBuffer);
            // lResult = EncodingUtils.getString(buffer, "UTF-8");// 依Y.txt的编码类型选择合适的编码，如果不调整会乱码
            lResult = new String(lBuffer);
            lInputStream.close();
        } catch (Exception e) {
            LogUtil.e(TAG, e);
        }
        return lResult;
    }

}
