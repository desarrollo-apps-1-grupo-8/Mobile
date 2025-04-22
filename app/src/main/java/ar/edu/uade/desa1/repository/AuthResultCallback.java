package ar.edu.uade.desa1.repository;
import ar.edu.uade.desa1.domain.response.AuthResponse;



// interfaz simple para manejar la respuesta del login
//
public interface AuthResultCallback {
    void onResult(AuthResponse result);
}