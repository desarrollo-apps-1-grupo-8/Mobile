package ar.edu.uade.desa1.repository;

import javax.inject.Inject;
import javax.inject.Singleton;

import ar.edu.uade.desa1.api.AuthApiService;
import ar.edu.uade.desa1.domain.request.AuthLoginRequest;
import ar.edu.uade.desa1.domain.request.AuthRegisterRequest;
import ar.edu.uade.desa1.domain.request.SendVerificationCodeRequest;
import ar.edu.uade.desa1.domain.request.VerifyCodeRequest;
import ar.edu.uade.desa1.domain.request.VerifyCodeRequest;
import ar.edu.uade.desa1.domain.response.AuthRegisterResponse;
import ar.edu.uade.desa1.domain.response.SendVerificationCodeResponse;
import ar.edu.uade.desa1.domain.response.VerifyCodeResponse;
import ar.edu.uade.desa1.domain.response.VerifyCodeResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
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

    public void verifyCode(VerifyCodeRequest request, OnVerifyCodeCallback callback) {
        authApiService.verifyCode(request).enqueue(new Callback<VerifyCodeResponse>() {
            @Override
            public void onResponse(Call<VerifyCodeResponse> call, Response<VerifyCodeResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onError("Error en la verificación: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<VerifyCodeResponse> call, Throwable t) {
                callback.onError("Error de conexión: " + t.getMessage());
            }
        });
    };
    
    public interface OnRegisterCallback {
        void onSuccess(AuthRegisterResponse response);
        void onError(String errorMessage);
    }

    public interface OnVerifyCodeCallback {
        void onSuccess(VerifyCodeResponse response);
        void onError(String errorMessage);
    }

    public interface OnSendVerificationCodeCallback {
        void onSuccess(SendVerificationCodeResponse response);
        void onError(String errorMessage);
    }

    public void sendVerificationCode(SendVerificationCodeRequest request, OnSendVerificationCodeCallback callback) {
        authApiService.sendVerificationCode(request).enqueue(new Callback<SendVerificationCodeResponse>() {
            @Override
            public void onResponse(Call<SendVerificationCodeResponse> call, Response<SendVerificationCodeResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onError("Error al enviar el código: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<SendVerificationCodeResponse> call, Throwable t) {
                callback.onError("Error de conexión: " + t.getMessage());
            }
        });
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
                    callback.onResult(new AuthResponse(false, null, false, ""));
                }
            }

            @Override
            public void onFailure(Call<AuthResponse> call, Throwable t) {
                callback.onResult(new AuthResponse(false, null, false, ""));
            }
        });
    }

} 