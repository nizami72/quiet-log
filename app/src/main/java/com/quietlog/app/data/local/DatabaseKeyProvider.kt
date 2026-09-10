package com.quietlog.app.data.local

import android.content.Context
import android.util.Base64
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import java.security.SecureRandom

/**
 * Generates a random passphrase for the Room/SQLCipher database on first run and keeps it in
 * [EncryptedSharedPreferences], whose own key is Keystore-backed — the passphrase itself never
 * sits in plaintext at rest.
 */
object DatabaseKeyProvider {
    private const val PREFS_FILE_NAME = "quietlog_db_key_prefs"
    private const val KEY_PASSPHRASE = "db_passphrase"
    private const val PASSPHRASE_LENGTH_BYTES = 32

    fun getOrCreatePassphrase(context: Context): ByteArray {
        val prefs = EncryptedSharedPreferences.create(
            context,
            PREFS_FILE_NAME,
            MasterKey.Builder(context).setKeyScheme(MasterKey.KeyScheme.AES256_GCM).build(),
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM,
        )

        prefs.getString(KEY_PASSPHRASE, null)?.let { return Base64.decode(it, Base64.NO_WRAP) }

        val passphrase = ByteArray(PASSPHRASE_LENGTH_BYTES).also { SecureRandom().nextBytes(it) }
        prefs.edit().putString(KEY_PASSPHRASE, Base64.encodeToString(passphrase, Base64.NO_WRAP)).apply()
        return passphrase
    }
}
