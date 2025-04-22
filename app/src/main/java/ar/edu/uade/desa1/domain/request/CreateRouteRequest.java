package ar.edu.uade.desa1.domain.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateRouteRequest {
    private String packageInfo;
    private String origin;
    private String destination;
    private Long userId; // ID del usuario que crea la ruta
    private Long deliveryUserId; // ID del usuario asignado para la entrega
    private String status; // Estado inicial de la ruta, por ejemplo, "AVAILABLE"
}

