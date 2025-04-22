package ar.edu.uade.desa1.api;

import ar.edu.uade.desa1.domain.request.AuthLoginRequest;
import ar.edu.uade.desa1.domain.request.AuthRegisterRequest;
import ar.edu.uade.desa1.domain.request.SendVerificationCodeRequest;
import ar.edu.uade.desa1.domain.request.VerifyCodeRequest;
import ar.edu.uade.desa1.domain.request.VerifyCodeRequest;
import ar.edu.uade.desa1.domain.response.AuthRegisterResponse;
import ar.edu.uade.desa1.domain.response.AuthResponse;

import ar.edu.uade.desa1.domain.response.VerifyEmailResponse;
import ar.edu.uade.desa1.domain.request.PasswordRecoveryRequest;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface AuthApiService {
    
    @POST("/api/v1/register")
    Call<AuthRegisterResponse> register(@Body AuthRegisterRequest request);

    @POST("/api/v1/verify-code")
    Call<VerifyCodeResponse> verifyCode(@Body VerifyCodeRequest request);

    @POST("/api/v1/send-verification-code")
    Call<SendVerificationCodeResponse> sendVerificationCode(@Body SendVerificationCodeRequest request);

    @POST("/api/v1/login")
    Call<AuthResponse> login(@Body AuthLoginRequest request);

    @POST("/auth/recover")
    Call<Void> recoverPassword(@Body PasswordRecoveryRequest request);
} 