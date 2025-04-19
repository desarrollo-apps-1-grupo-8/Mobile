package ar.edu.uade.desa1.service;

import ar.edu.uade.desa1.domain.request.AuthLoginRequest;
import ar.edu.uade.desa1.domain.response.AuthResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface AuthService {
    @POST("/api/v1/login")
    Call<AuthResponse> login(@Body AuthLoginRequest request);
}
