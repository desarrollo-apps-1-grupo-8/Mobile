package ar.edu.uade.desa1;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import ar.edu.uade.desa1.api.RoutesApiService;
import ar.edu.uade.desa1.domain.response.DeliveryRouteResponseWithUserInfo;
import ar.edu.uade.desa1.fragment.RouteCardFragment;
import ar.edu.uade.desa1.util.AuthRouteHandler;
import ar.edu.uade.desa1.util.RoleEnum;
import ar.edu.uade.desa1.util.TokenManager;
import dagger.hilt.android.AndroidEntryPoint;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@AndroidEntryPoint
public class RoutesActivity extends AppCompatActivity {

    private LinearLayout routesContainer;

    @Inject
    RoutesApiService routesApiService;

    @Inject
    AuthRouteHandler authRouteHandler;

    @Inject
    TokenManager tokenManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (!authRouteHandler.checkAuthentication(this, LoginActivity.class)) { //
            return;
        }

        setContentView(R.layout.activity_routes);

        routesContainer = findViewById(R.id.routesContainer);

        long userId = tokenManager.getUserIdFromToken();
        RoleEnum role = RoleEnum.valueOf(tokenManager.getUserRoleFromToken().toUpperCase(Locale.ROOT));
        if (role == RoleEnum.USUARIO) {
            routesApiService.getRoutesByUserId(userId)
                    .enqueue(new Callback<List<DeliveryRouteResponseWithUserInfo>>() {
                        @Override
                        public void onResponse(Call<List<DeliveryRouteResponseWithUserInfo>> call, Response<List<DeliveryRouteResponseWithUserInfo>> response) {
                            if (response.isSuccessful() && response.body() != null) {
                                TextView noRoutesText = findViewById(R.id.noRoutesText);
                                List<DeliveryRouteResponseWithUserInfo> rutas = response.body();

                                if (rutas.isEmpty()) {
                                    noRoutesText.setVisibility(View.VISIBLE);
                                } else {
                                    noRoutesText.setVisibility(View.GONE);
                                    for (DeliveryRouteResponseWithUserInfo route : rutas) {
                                        addRouteCard(route);
                                    }
                                }

                            }
                        }

                        @Override
                        public void onFailure(Call<List<DeliveryRouteResponseWithUserInfo>> call, Throwable t) {
                            Log.e("RoutesActivity", "Error en Retrofit", t);
                            Toast.makeText(RoutesActivity.this, "Fallo al conectar con el servidor", Toast.LENGTH_SHORT).show();
                        }
                    });
        }



        if (role == RoleEnum.REPARTIDOR) {
        routesApiService.getAllRoutes()
                .enqueue(new Callback<List<DeliveryRouteResponseWithUserInfo>>() {
                    @Override
                    public void onResponse(Call<List<DeliveryRouteResponseWithUserInfo>> call, Response<List<DeliveryRouteResponseWithUserInfo>> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            TextView noRoutesText = findViewById(R.id.noRoutesText);
                            List<DeliveryRouteResponseWithUserInfo> rutas = response.body();

                            if (rutas.isEmpty()) {
                                noRoutesText.setVisibility(View.VISIBLE);
                            } else {
                                noRoutesText.setVisibility(View.GONE);
                                for (DeliveryRouteResponseWithUserInfo route : rutas) {
                                    addRouteCard(route);
                                }
                            }

                        }
                    }

                    @Override
                    public void onFailure(Call<List<DeliveryRouteResponseWithUserInfo>> call, Throwable t) {
                        Log.e("RoutesActivity", "Error en Retrofit", t);
                        Toast.makeText(RoutesActivity.this, "Fallo al conectar con el servidor", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
    private void addRouteCard(DeliveryRouteResponseWithUserInfo route) {
        Fragment fragment = RouteCardFragment.newInstance(route);
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.routesContainer, fragment)
                .commit();
    }

}