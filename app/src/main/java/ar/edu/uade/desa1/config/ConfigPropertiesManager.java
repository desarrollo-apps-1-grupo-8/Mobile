package ar.edu.uade.desa1.config;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Gestor de configuración a través de un archivo de propiedades.
 */
@Singleton
public class ConfigPropertiesManager {
    
    private static final String TAG = "ConfigProperties";
    private static final String CONFIG_FILE = "config.properties";
    private static final String KEY_BASE_URL = "base_url";
    private static final String DEFAULT_BASE_URL = "http://10.0.2.2:8080/";
    
    private final Properties properties;
    
    @Inject
    public ConfigPropertiesManager(Context context) {
        properties = new Properties();
        loadProperties(context);
    }
    
    private void loadProperties(Context context) {
        AssetManager assetManager = context.getAssets();
        try (InputStream inputStream = assetManager.open(CONFIG_FILE)) {
            properties.load(inputStream);
        } catch (IOException e) {
            Log.e(TAG, "Error loading config properties", e);
        }
    }
    
    /**
     * Obtiene la URL base desde el archivo de propiedades
     */
    public String getBaseUrl() {
        return properties.getProperty(KEY_BASE_URL, DEFAULT_BASE_URL);
    }
    
    /**
     * Obtiene un valor de configuración por su clave
     */
    public String getProperty(String key, String defaultValue) {
        return properties.getProperty(key, defaultValue);
    }
} 