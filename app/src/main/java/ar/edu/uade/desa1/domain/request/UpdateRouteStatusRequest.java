package ar.edu.uade.desa1.domain.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateRouteStatusRequest {
    private Long deliveryRouteId;
    private String status;
    private Long deliveryUserId;
}