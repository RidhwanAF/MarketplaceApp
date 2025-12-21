package com.raf.core.data.utility

import android.util.Base64
import android.util.Log
import java.nio.ByteBuffer
import java.security.SecureRandom
import javax.crypto.Cipher
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec
import javax.crypto.spec.SecretKeySpec

object EncryptionManager {
    /**
     * Encryption Simulation
     */
    private const val SECRET_KEY = "secret_key_for_encryption"

    private const val TAG = "SessionEncryptionManager"
    private const val ALGORITHM = "AES"
    private const val TRANSFORMATION = "AES/GCM/NoPadding"
    private const val IV_SIZE = 12
    private const val TAG_BIT_LENGTH = 128

    private fun getSecretKey(): SecretKey {
        val keyBytes = SECRET_KEY.toByteArray(Charsets.UTF_8).copyOf(32)
        return SecretKeySpec(keyBytes, ALGORITHM)
    }

    fun encrypt(data: String): String? {
        return try {
            val iv = ByteArray(IV_SIZE)
            SecureRandom().nextBytes(iv)

            val cipher = Cipher.getInstance(TRANSFORMATION)
            val parameterSpec = GCMParameterSpec(TAG_BIT_LENGTH, iv)
            cipher.init(Cipher.ENCRYPT_MODE, getSecretKey(), parameterSpec)

            val encryptedData = cipher.doFinal(data.toByteArray(Charsets.UTF_8))

            val byteBuffer = ByteBuffer.allocate(iv.size + encryptedData.size)
            byteBuffer.put(iv)
            byteBuffer.put(encryptedData)

            Base64.encodeToString(byteBuffer.array(), Base64.DEFAULT)
        } catch (e: Exception) {
            Log.e(TAG, "Failed to encrypt", e)
            null
        }
    }

    fun decrypt(encryptedString: String): String? {
        return try {
            val decodedBytes = Base64.decode(encryptedString, Base64.DEFAULT)
            val byteBuffer = ByteBuffer.wrap(decodedBytes)

            val iv = ByteArray(IV_SIZE)
            byteBuffer.get(iv)

            val encryptedData = ByteArray(byteBuffer.remaining())
            byteBuffer.get(encryptedData)

            val cipher = Cipher.getInstance(TRANSFORMATION)
            val parameterSpec = GCMParameterSpec(TAG_BIT_LENGTH, iv)
            cipher.init(Cipher.DECRYPT_MODE, getSecretKey(), parameterSpec)

            val decryptedData = cipher.doFinal(encryptedData)

            String(decryptedData, Charsets.UTF_8)
        } catch (e: Exception) {
            Log.e(TAG, "Failed to decrypt", e)
            null
        }
    }

    fun compareIsSame(string: String, encryptedString: String): Boolean {
        val decryptedData = decrypt(encryptedString)
        return decryptedData == string
    }
}