package ar.edu.uade.desa1.api;

import ar.edu.uade.desa1.domain.request.AuthRegisterRequest;
import ar.edu.uade.desa1.domain.request.VerifyEmailRequest;
import ar.edu.uade.desa1.domain.response.AuthRegisterResponse;
import ar.edu.uade.desa1.domain.response.VerifyEmailResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface AuthApiService {
    
    @POST("/api/v1/register")
    Call<AuthRegisterResponse> register(@Body AuthRegisterRequest request);

    @POST("/api/v1/verify-email")
    Call<VerifyEmailResponse> verifyEmail(@Body VerifyEmailRequest request);
} 