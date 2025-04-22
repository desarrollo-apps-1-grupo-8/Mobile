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

import javax.inject.Inject;

import ar.edu.uade.desa1.api.RoutesApiService;
import ar.edu.uade.desa1.domain.response.DeliveryRouteResponse;
import ar.edu.uade.desa1.fragment.HistoryRouteCardFragment;
import ar.edu.uade.desa1.util.AuthRouteHandler;
import ar.edu.uade.desa1.util.TokenManager;
import dagger.hilt.android.AndroidEntryPoint;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@AndroidEntryPoint
public class HistoryActivity extends AppCompatActivity {

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

        if (!authRouteHandler.checkAuthentication(this, LoginActivity.class)) {
            return;
        }

        setContentView(R.layout.activity_history);

        routesContainer = findViewById(R.id.routesContainer);

        long userId =  tokenManager.getUserIdFromToken();

        routesApiService.getCompletedRoutesByUserId(userId)
                .enqueue(new Callback<List<DeliveryRouteResponse>>() {
                    @Override
                    public void onResponse(Call<List<DeliveryRouteResponse>> call, Response<List<DeliveryRouteResponse>> response) {
                        if (isFinishing() || isDestroyed())
                            return; // Verifica si la actividad sigue activa

                        Log.d("DEBUG", "Código de respuesta: " + response.code());
                        Log.d("DEBUG", "Body recibido: " + response.body());
                        if (response.isSuccessful() && response.body() != null) {
                            TextView noRoutesText = findViewById(R.id.noRoutesText);
                            List<DeliveryRouteResponse> rutas = response.body();

                            if (rutas.isEmpty()) {
                                noRoutesText.setVisibility(View.VISIBLE);
                            } else {
                                noRoutesText.setVisibility(View.GONE);
                                for (DeliveryRouteResponse route : rutas) {
                                    addRouteCard(route, userRole);
                                }
                            }
                        } else {
                            Toast.makeText(HistoryActivity.this, "Error al obtener rutas", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<List<DeliveryRouteResponse>> call, Throwable t) {
                        if (isFinishing() || isDestroyed())
                            return; // Verifica si la actividad sigue activa

                        Log.e("DEBUG", "Error en la llamada Retrofit: " + t.getMessage(), t);
                        Log.e("HistoryActivity", "Error en Retrofit", t);
                        Toast.makeText(HistoryActivity.this, "Fallo al conectar con el servidor", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void addRouteCard(DeliveryRouteResponse route, String userRole) {
        Fragment fragment = HistoryRouteCardFragment.newInstance(route, userRole);
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.routesContainer, fragment)
                .commit();
    }

}