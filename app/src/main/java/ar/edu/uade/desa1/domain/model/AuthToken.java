package ar.edu.uade.desa1.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Modelo para representar la información de autenticación
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthToken {
    private String accessToken;
    private long expiresIn;
    
    /**
     * Calcula el tiempo de expiración en milisegundos
     * @return tiempo de expiración en milisegundos desde la época Unix
     */
    public long getExpiryTimeMillis() {
        return System.currentTimeMillis() + (expiresIn * 1000);
    }
} 