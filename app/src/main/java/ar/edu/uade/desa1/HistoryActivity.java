package ar.edu.uade.desa1;

import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.card.MaterialCardView;

import java.util.List;

import javax.inject.Inject;

import ar.edu.uade.desa1.api.RoutesApiService;
import ar.edu.uade.desa1.domain.RouteStatusEnum;
import ar.edu.uade.desa1.domain.response.DeliveryRouteResponse;
import ar.edu.uade.desa1.fragment.RouteCardFragment;
import ar.edu.uade.desa1.util.AuthRouteHandler;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (!authRouteHandler.checkAuthentication(this, LoginActivity.class)) { //
            return;
        }

        setContentView(R.layout.activity_history);

        routesContainer = findViewById(R.id.routesContainer);

        long userId = 1L; // reemplacelo con el real cuando lo tengas

        routesApiService.getCompletedRoutesByUserId(userId)
                .enqueue(new Callback<List<DeliveryRouteResponse>>() {
                    @Override
                    public void onResponse(Call<List<DeliveryRouteResponse>> call, Response<List<DeliveryRouteResponse>> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            TextView noRoutesText = findViewById(R.id.noRoutesText);
                            List<DeliveryRouteResponse> rutas = response.body();

                            if (rutas.isEmpty()) {
                                noRoutesText.setVisibility(View.VISIBLE);
                            } else {
                                noRoutesText.setVisibility(View.GONE);
                                for (DeliveryRouteResponse route : rutas) {
                                    addRouteCard(route);
                                }
                            }

                        } else {
                            Toast.makeText(HistoryActivity.this, "Error al obtener rutas", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<List<DeliveryRouteResponse>> call, Throwable t) {
                        Log.e("HistoryActivity", "Error en Retrofit", t);
                        Toast.makeText(HistoryActivity.this, "Fallo al conectar con el servidor", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void addRouteCard(DeliveryRouteResponse route) {
        Fragment fragment = RouteCardFragment.newInstance(route);
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.routesContainer, fragment)
                .commit();
    }

}