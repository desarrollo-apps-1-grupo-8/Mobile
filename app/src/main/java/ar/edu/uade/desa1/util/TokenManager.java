package ar.edu.uade.desa1.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;
import android.util.Log;

import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKey;

import java.io.IOException;
import java.security.GeneralSecurityException;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Clase encargada del manejo seguro de tokens de autenticación utilizando EncryptedSharedPreferences
 */
@Singleton
public class TokenManager {
    
    private static final String TAG = "TokenManager";
    private static final String PREF_FILE_NAME = "secure_token_prefs";
    private static final String KEY_ACCESS_TOKEN = "access_token";
    private static final String KEY_EXPIRY_TIME = "expiry_time";
    
    private final SharedPreferences encryptedPrefs;
    
    @Inject
    public TokenManager(Context context) {
        try {
            MasterKey masterKey = new MasterKey.Builder(context)
                    .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
                    .setKeyGenParameterSpec(
                            new KeyGenParameterSpec.Builder(
                                    "_token_manager_master_key_",
                                    KeyProperties.PURPOSE_ENCRYPT | KeyProperties.PURPOSE_DECRYPT)
                                    .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
                                    .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
                                    .setKeySize(256)
                                    .build())
                    .build();
            
            this.encryptedPrefs = EncryptedSharedPreferences.create(
                    context,
                    PREF_FILE_NAME,
                    masterKey,
                    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            );
        } catch (GeneralSecurityException | IOException e) {
            Log.e(TAG, "Error al inicializar EncryptedSharedPreferences: " + e.getMessage());
            throw new RuntimeException("Error al inicializar el TokenManager", e);
        }
    }

    public void saveToken(String accessToken, long expiryTimeInMillis) {
        encryptedPrefs.edit()
                .putString(KEY_ACCESS_TOKEN, accessToken)
                .putLong(KEY_EXPIRY_TIME, expiryTimeInMillis)
                .apply();
    }

    public void saveToken(String accessToken) {
        // Establecemos un tiempo de expiración de 1 hora desde ahora
        saveToken(accessToken, System.currentTimeMillis() + (3600 * 1000));
    }

    public String getAccessToken() {
        return encryptedPrefs.getString(KEY_ACCESS_TOKEN, null);
    }

    public boolean isLoggedIn() {
        return getAccessToken() != null;
    }

    public boolean isTokenExpired() {
        long expiryTime = encryptedPrefs.getLong(KEY_EXPIRY_TIME, 0);
        return expiryTime > 0 && System.currentTimeMillis() >= expiryTime;
    }

    public void clearToken() {
        encryptedPrefs.edit().clear().apply();
    }

    public String getFormattedAccessToken() {
        String accessToken = getAccessToken();
        return accessToken != null ? "Bearer " + accessToken : null;
    }
} 