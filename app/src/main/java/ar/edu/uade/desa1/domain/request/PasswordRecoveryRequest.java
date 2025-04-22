package ar.edu.uade.desa1.domain.request;

public class PasswordRecoveryRequest {
    private String email;

    public PasswordRecoveryRequest(String email) {
        this.email = email;
    }

    public String getEmail() { return email; }

    public void setEmail(String email) { this.email = email; }
}