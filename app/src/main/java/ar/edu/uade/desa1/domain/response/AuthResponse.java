package ar.edu.uade.desa1.domain.response;


//Representa la respuesta del login en Retrofit
public class AuthResponse {
    private boolean success;
    private String token;

    private Boolean active;

    private String status;

    public AuthResponse(boolean success, String token, Boolean active, String status) {
        this.success = success;
        this.token = token;
        this.active = active;
        this.status = status;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getToken() {
        return token;
    }

    public Boolean getActive() {
        return active;
    }

    public String getStatus() {
        return status;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
