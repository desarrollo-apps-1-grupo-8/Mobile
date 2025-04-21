package ar.edu.uade.desa1.domain.response;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DeliveryRouteResponseWithUserInfo {

    private Long id;
    private String packageInfo;
    private String origin;
    private String destination;
    private String status;

    private String userInfo;
    private String deliveryUserInfo;

    private String createdAt;
    private String updatedAt;
}