package ar.edu.uade.desa1.api.interceptor;

import androidx.annotation.NonNull;

import java.io.IOException;

import javax.inject.Inject;
import javax.inject.Singleton;

import ar.edu.uade.desa1.util.TokenManager;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;


@Singleton
public class AuthInterceptor implements Interceptor {

    private final TokenManager tokenManager;

    @Inject
    public AuthInterceptor(TokenManager tokenManager) {
        this.tokenManager = tokenManager;
    }

    @NonNull
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request originalRequest = chain.request();
        
        if (!tokenManager.isLoggedIn() || originalRequest.header("Authorization") != null) {
            return chain.proceed(originalRequest);
        }
        
        String token = tokenManager.getFormattedAccessToken();
        if (token != null) {
            Request authorizedRequest = originalRequest.newBuilder()
                    .header("Authorization", token)
                    .build();
            return chain.proceed(authorizedRequest);
        }
        
        return chain.proceed(originalRequest);
    }
} 