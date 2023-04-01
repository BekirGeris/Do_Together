package com.example.dotogether.util.helper

import android.util.Base64
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

object EncryptionHelper {

    private val SECRET_KEY = byteArrayOf(
        0x74, 0x68, 0x69, 0x73, 0x2d, 0x69, 0x73, 0x2d,
        0x61, 0x2d, 0x73, 0x65, 0x63, 0x72, 0x65, 0x74
    )
    private val ivBytes = ByteArray(16) // 16 bytes for AES

    fun encrypt(input: String): String {
        val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
        val ivSpec = IvParameterSpec(ivBytes)
        cipher.init(Cipher.ENCRYPT_MODE, SecretKeySpec(SECRET_KEY, "AES"), ivSpec)
        val cipherText = cipher.doFinal(input.toByteArray())
        return Base64.encodeToString(cipherText, Base64.DEFAULT)
    }

    fun decrypt(encrypted: String): String {
        val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
        val ivSpec = IvParameterSpec(ivBytes)
        cipher.init(Cipher.DECRYPT_MODE, SecretKeySpec(SECRET_KEY, "AES"), ivSpec)
        val decodedBytes = Base64.decode(encrypted, Base64.DEFAULT)
        val decryptedBytes = cipher.doFinal(decodedBytes)
        return String(decryptedBytes)
    }
}