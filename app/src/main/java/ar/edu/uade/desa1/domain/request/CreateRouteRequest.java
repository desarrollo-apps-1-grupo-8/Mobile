package ar.edu.uade.desa1.domain.request;

import ar.edu.uade.desa1.domain.RouteStatusEnum;
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
    private RouteStatusEnum status;
    private Long userId;
}
