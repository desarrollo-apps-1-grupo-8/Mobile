package ar.edu.uade.desa1.domain.response;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeliveryRouteResponse {
    private Long id;
    private String packageInfo;
    private String origin;
    private String destination;
    private String status;
    private Long userId;
    private Long deliveryUserId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String userInfo;
}

