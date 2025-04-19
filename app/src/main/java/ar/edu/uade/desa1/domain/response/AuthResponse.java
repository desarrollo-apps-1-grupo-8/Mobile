package ar.edu.uade.desa1.domain.response;


//Representa la respuesta del login en Retrofit
public class AuthResponse {
    private boolean success;
    private String token;

    public AuthResponse(boolean success, String token) {
        this.success = success;
        this.token = token;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getToken() {
        return token;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
