package ar.edu.uade.desa1.domain.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VerifyCodeResponse {
    private boolean success;
    private String message;
    private String token;
} 