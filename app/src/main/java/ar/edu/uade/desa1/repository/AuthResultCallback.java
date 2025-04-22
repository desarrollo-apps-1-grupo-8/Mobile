package ar.edu.uade.desa1.repository;
import ar.edu.uade.desa1.domain.response.AuthResponse;

public interface AuthResultCallback {
    void onResult(AuthResponse result);
}