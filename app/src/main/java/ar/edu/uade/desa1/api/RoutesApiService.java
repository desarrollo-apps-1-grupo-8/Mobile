package ar.edu.uade.desa1.api;

import java.util.List;

import ar.edu.uade.desa1.domain.request.CreateRouteRequest;
import ar.edu.uade.desa1.domain.request.UpdateRouteStatusRequest;
import ar.edu.uade.desa1.domain.response.DeliveryRouteResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface RoutesApiService {

    @GET("/api/v1/routes/history/{userId}")
    Call<List<DeliveryRouteResponse>> getCompletedRoutesByUserId(@Path("userId") long userId);

    @POST("/api/v1/routes")
    Call<DeliveryRouteResponse> createRoute(@Body CreateRouteRequest request);

    @POST("/api/v1/routes/update-status")
    Call<DeliveryRouteResponse> updateRouteStatus(@Body UpdateRouteStatusRequest request);
}

