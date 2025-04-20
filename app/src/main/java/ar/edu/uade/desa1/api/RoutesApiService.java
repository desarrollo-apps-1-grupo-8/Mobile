package ar.edu.uade.desa1.api;

import java.util.List;

import ar.edu.uade.desa1.domain.response.DeliveryRouteResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface RoutesApiService {

    @GET("/api/v1/routes/history/{userId}")
    Call<List<DeliveryRouteResponse>> getCompletedRoutesByUserId(@Path("userId") long userId);
}
