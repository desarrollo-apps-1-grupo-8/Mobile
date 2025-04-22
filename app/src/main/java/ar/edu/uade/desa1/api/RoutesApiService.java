package ar.edu.uade.desa1.api;

import java.util.List;

import ar.edu.uade.desa1.domain.request.CreateRouteRequest;
import ar.edu.uade.desa1.domain.request.UpdateRouteStatusRequest; // Asegúrate de que esta línea esté presente
import ar.edu.uade.desa1.domain.response.DeliveryRouteResponse;
import ar.edu.uade.desa1.domain.response.DeliveryRouteResponseWithUserInfo;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface RoutesApiService {

    @GET("/api/v1/routes/history/{userId}")
<<<<<<< Updated upstream
    Call<List<DeliveryRouteResponse>> getCompletedRoutesByUserId(@Path("userId") long userId);

    @POST("/api/v1/routes/update-status")
    Call<DeliveryRouteResponse> updateRouteStatus(@Body UpdateRouteStatusRequest request);

    @POST("/api/v1/routes")
    Call<DeliveryRouteResponse> createRoute(@Body CreateRouteRequest request);

    @GET("/api/v1/routes")
    Call<List<DeliveryRouteResponse>> getAllRoutes();

    @GET("/api/v1/routes/{id}")
    Call<DeliveryRouteResponse> getRouteById(@Path("id") long routeId);
=======
    Call<List<DeliveryRouteResponseWithUserInfo>> getCompletedRoutesByUserId(@Path("userId") long userId);

    @GET("/api/v1/routes/user/{userId}")
    Call<List<DeliveryRouteResponseWithUserInfo>> getRoutesByUserId(@Path("userId") long userId);

    @GET("/api/v1/routes")
    Call<List<DeliveryRouteResponseWithUserInfo>> getAllRoutes();
>>>>>>> Stashed changes
}
