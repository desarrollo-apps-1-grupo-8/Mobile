package ar.edu.uade.desa1.repository;

import javax.inject.Inject;
import javax.inject.Singleton;

import ar.edu.uade.desa1.api.AuthApiService;
import ar.edu.uade.desa1.domain.request.AuthLoginRequest;
import ar.edu.uade.desa1.domain.request.AuthRegisterRequest;
import ar.edu.uade.desa1.domain.request.VerifyEmailRequest;
import ar.edu.uade.desa1.domain.response.AuthRegisterResponse;
import ar.edu.uade.desa1.domain.response.VerifyEmailResponse;
import ar.edu.uade.desa1.service.AuthService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ar.edu.uade.desa1.api.AuthApiService;
import ar.edu.uade.desa1.domain.response.AuthResponse;

@Singleton
public class AuthRepository {
    
    private final AuthApiService authApiService;
    
    @Inject
    public AuthRepository(AuthApiService authApiService) {
        this.authApiService = authApiService;
    }
    
    public void register(AuthRegisterRequest request, OnRegisterCallback callback) {
        authApiService.register(request).enqueue(new Callback<AuthRegisterResponse>() {
            @Override
            public void onResponse(Call<AuthRegisterResponse> call, Response<AuthRegisterResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onError("Error en el registro: " + response.message());
                }
            }
            
            @Override
            public void onFailure(Call<AuthRegisterResponse> call, Throwable t) {
                callback.onError("Error de conexión: " + t.getMessage());
            }
        });
    }

    public void verifyEmail(VerifyEmailRequest request, OnVerifyEmailCallback callback) {
        authApiService.verifyEmail(request).enqueue(new Callback<VerifyEmailResponse>() {
            @Override
            public void onResponse(Call<VerifyEmailResponse> call, Response<VerifyEmailResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onError("Error en la verificación: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<VerifyEmailResponse> call, Throwable t) {
                callback.onError("Error de conexión: " + t.getMessage());
            }
        });
    };
    
    public interface OnRegisterCallback {
        void onSuccess(AuthRegisterResponse response);
        void onError(String errorMessage);
    }

    public interface OnVerifyEmailCallback {
        void onSuccess(VerifyEmailResponse response);
        void onError(String errorMessage);
    }

    //Login
    @Inject
    AuthApiService authService;
    public void login(AuthLoginRequest request, AuthResultCallback callback) {

        authService.login(request).enqueue(new Callback<AuthResponse>() {
            @Override
            public void onResponse(Call<AuthResponse> call, Response<AuthResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onResult(response.body());
                } else {
                    callback.onResult(new AuthResponse(false, null));
                }
            }

            @Override
            public void onFailure(Call<AuthResponse> call, Throwable t) {
                callback.onResult(new AuthResponse(false, null));
            }
        });
    }

} 