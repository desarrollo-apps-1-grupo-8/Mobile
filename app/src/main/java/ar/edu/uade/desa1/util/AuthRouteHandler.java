package ar.edu.uade.desa1.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * encargada de gestionar las rutas autenticadas y redireccionar
 * al login cuando sea necesario
 */
@Singleton
public class AuthRouteHandler {
    
    private final TokenManager tokenManager;
    private final Context context;
    
    @Inject
    public AuthRouteHandler(TokenManager tokenManager, Context context) {
        this.tokenManager = tokenManager;
        this.context = context;
    }
    
    public boolean checkAuthentication(Activity activity, Class<?> loginActivityClass) {
        if (!tokenManager.isLoggedIn()) {
            // El usuario no está autenticado, redirigir al login
            Toast.makeText(context, "Debes iniciar sesión para acceder", Toast.LENGTH_SHORT).show();
            
            Intent intent = new Intent(activity, loginActivityClass);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            activity.startActivity(intent);
            activity.finish();
            return false;
        }
        
        if (tokenManager.isTokenExpired()) {
            tokenManager.clearToken();
            
            Toast.makeText(context, "Tu sesión ha expirado, inicia sesión nuevamente", Toast.LENGTH_SHORT).show();
            
            Intent intent = new Intent(activity, loginActivityClass);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            activity.startActivity(intent);
            activity.finish();
            return false;
        }
        
        return true;
    }
} 