package ar.edu.uade.desa1.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;
import android.util.Log;

import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKey;

import org.json.JSONObject;

import java.io.IOException;
import java.security.GeneralSecurityException;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * autenticación utilizando EncryptedSharedPreferences
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
            String alias = "_token_manager_master_key_";

            KeyGenParameterSpec spec = new KeyGenParameterSpec.Builder(
                    alias,
                    KeyProperties.PURPOSE_ENCRYPT | KeyProperties.PURPOSE_DECRYPT)
                    .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
                    .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
                    .setKeySize(256)
                    .build();

            MasterKey masterKey = new MasterKey.Builder(context, alias)
                    .setKeyGenParameterSpec(spec)
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

    public Long getUserIdFromToken() {
        try {
            String token = getAccessToken();
            if (token == null) return null;

            if (token.startsWith("Bearer ")) {
                token = token.substring(7);
            }

            String[] parts = token.split("\\.");
            if (parts.length != 3) return null;

            String payload = new String(android.util.Base64.decode(parts[1], android.util.Base64.URL_SAFE));
            JSONObject json = new JSONObject(payload);

            return json.getLong("id");
        } catch (Exception e) {
            Log.e(TAG, "Error al obtener ID del token: " + e.getMessage());
            return null;
        }
    }

    public String getUserRoleFromToken() {
        try {
            String token = getAccessToken();
            if (token == null) return null;
<<<<<<< Updated upstream

            if (token.startsWith("Bearer ")) {
                token = token.substring(7);
            }

            String[] parts = token.split("\\.");
            if (parts.length != 3) return null;

            String payload = new String(android.util.Base64.decode(parts[1], android.util.Base64.URL_SAFE));
            JSONObject json = new JSONObject(payload);

            return json.getString("role");
        } catch (Exception e) {
            Log.e(TAG, "Error al obtener rol del token: " + e.getMessage());
            return null;
        }
    }
=======
>>>>>>> Stashed changes

            if (token.startsWith("Bearer ")) {
                token = token.substring(7);
            }

            String[] parts = token.split("\\.");
            if (parts.length != 3) return null;

            String payload = new String(android.util.Base64.decode(parts[1], android.util.Base64.URL_SAFE));
            JSONObject json = new JSONObject(payload);

            return json.getString("role");
        } catch (Exception e) {
            Log.e(TAG, "Error al obtener role del token: " + e.getMessage());
            return null;
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
