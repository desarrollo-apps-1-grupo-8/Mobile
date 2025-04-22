package ar.edu.uade.desa1.api;

import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;

import java.io.IOException;

import javax.inject.Inject;
import javax.inject.Singleton;

import ar.edu.uade.desa1.RegisterActivity;
import ar.edu.uade.desa1.util.TokenManager;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Interceptor para agregar el token de autenticación a las peticiones HTTP
 * y manejar respuestas 401 (No autorizado) cuando el token expira o es inválido.
 */
@Singleton
public class AuthInterceptor implements Interceptor {

    private final TokenManager tokenManager;
    private final Context context;

    @Inject
    public AuthInterceptor(TokenManager tokenManager, Context context) {
        this.tokenManager = tokenManager;
        this.context = context;
    }

    @NonNull
    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
        Request originalRequest = chain.request();
        
        String token = tokenManager.getAccessToken();
        Request.Builder requestBuilder = originalRequest.newBuilder();
        
        if (token != null && !token.isEmpty()) {
            requestBuilder.header("Authorization", "Bearer " + token);
        }
        
        Request request = requestBuilder.build();
        Response response = chain.proceed(request);
        
        // Si el servidor responde con 401, es porque el token expiró o es inválido
        if (response.code() == 401) {
            tokenManager.clearToken();
            
            Intent intent = new Intent(context, RegisterActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            context.startActivity(intent);
        }
        
        return response;
    }
} 