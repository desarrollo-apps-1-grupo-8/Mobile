package ar.edu.uade.desa1;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import javax.inject.Inject;

import ar.edu.uade.desa1.util.TokenManager;
import dagger.hilt.android.AndroidEntryPoint;

/**
 * Actividad base que todas las actividades deben extender.
 * Maneja la verificación de la sesión del usuario.
 */
@AndroidEntryPoint
public abstract class BaseActivity extends AppCompatActivity {

    @Inject
    TokenManager tokenManager;
    
    private boolean skipSessionCheck = false;
    
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        if (!skipSessionCheck && requiresAuth()) {
            checkUserSession();
        }
    }
    
    protected void checkUserSession() {
        String token = tokenManager.getAccessToken();
        
        if (token == null || token.isEmpty()) {
            Intent intent = new Intent(this, RegisterActivity.class); // CAMBIARLO A LOGIN
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        }
    }
    
    /**
     * Este metodo nos sirve para omitir la verificacion de sesion en activities que no requieren autenticacion.
     * Ejemplo: Login y Register.
     */
    protected void setSkipSessionCheck(boolean skip) {
        this.skipSessionCheck = skip;
    }
    
    /**
     * Este metodo nos sirve para indicar si la actividad requiere autenticacion.
     */
    protected boolean requiresAuth() {
        return true;
    }
} 