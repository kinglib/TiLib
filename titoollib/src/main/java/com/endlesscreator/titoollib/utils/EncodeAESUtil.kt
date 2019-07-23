package com.endlesscreator.titoollib.utils

import android.util.Base64
import com.endlesscreator.titoollib.utils.EncodeAESUtil.AES_IV
import com.endlesscreator.titoollib.utils.EncodeAESUtil.AES_KEY

import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

object EncodeAESUtil {

    private const val CHARSET = "UTF-8"


    /**
     * 密钥（默认值）
     */
    var AES_KEY = "1234567890123456"

    /**
     * 密钥偏移量（默认值）
     */
    var AES_IV = "1234567890123456"
    /**
     * 算法模式 CBC 加密块链、ECB 电子密码本、CFB 加密反馈、OFB 输出反馈
     * 补码方式 PKCS5Padding、PKCS7Padding、zeropadding、iso10126、ansix923
     */
    private const val CIPHER_TRANSFORMATION = "AES/CBC/PKCS5Padding"
    /**
     * 解密串编码方式：Base64
     */
    private const val BASE64_FLAGS = Base64.DEFAULT
    /**
     * 加密方式
     */
    private const val CIPHER = "AES"
//    /**
//     * 密钥长度
//     */
//    private val AES_KEY_LENGTH_BITS = 128
//    /**
//     * 偏移量长度
//     */
//    private val IV_LENGTH_BYTES = 16

    /**
     * 使用前先初始化
     */
    fun initParams(aesKey: String = AES_KEY, aesIv: String = AES_IV) {
        AES_KEY = aesKey
        AES_IV = aesIv
    }

    /**
     * 加密
     */
    fun encrypt(content: String?, key: String? = AES_KEY, iv: String? = AES_IV, handleResult: Boolean = true): String? {
        if (!content.isNullOrEmpty()) try {
            val encrypt = encrypt(content.toByteArray(charset(CHARSET)), getCharsetBytes(key), getCharsetBytes(iv))
            val result = Base64.encodeToString(encrypt, BASE64_FLAGS)
            return if (handleResult) result.replace("[\\s*\t\n\r]".toRegex(), "")
                    .replace("=".toRegex(), "")
                    .replace("[+]".toRegex(), "-")
                    .replace("/".toRegex(), "_") else result
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    /**
     * 解密
     */
    fun decrypt(content: String?, key: String? = AES_KEY, iv: String? = AES_IV, handleSource: Boolean = true): String? {
        if (!content.isNullOrEmpty()) try {
            val lSource = if (handleSource) content.replace("-".toRegex(), "+")
                    .replace("_".toRegex(), "/") else content
            decrypt(Base64.decode(lSource, BASE64_FLAGS), getCharsetBytes(key), getCharsetBytes(iv))?.let {
                return String(it)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return null
    }

    private fun getCharsetBytes(str: String?): ByteArray? {
        if (!str.isNullOrEmpty())
            try {
                return str.toByteArray(charset(CHARSET))
            } catch (e: Exception) {
                e.printStackTrace()
            }
        return null
    }

    private fun decrypt(content: ByteArray?, key: ByteArray?, iv: ByteArray?): ByteArray? {
        return doFinal(Cipher.DECRYPT_MODE, content, key, iv)
    }

    private fun encrypt(content: ByteArray?, key: ByteArray?, iv: ByteArray?): ByteArray? {
        return doFinal(Cipher.ENCRYPT_MODE, content, key, iv)
    }

    private fun doFinal(opmode: Int, content: ByteArray?, key: ByteArray?, iv: ByteArray?): ByteArray? {
        content?.let {
            try {
                val secretKey = SecretKeySpec(key ?: getCharsetBytes(AES_KEY), CIPHER)
                val cipher = Cipher.getInstance(CIPHER_TRANSFORMATION)
                val ivSpec = IvParameterSpec(iv ?: getCharsetBytes(AES_IV))
                cipher.init(opmode, secretKey, ivSpec)
                return cipher.doFinal(it)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        return null
    }


}
