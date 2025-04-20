package ar.edu.uade.desa1;

import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.card.MaterialCardView;

import java.util.List;

import javax.inject.Inject;

import ar.edu.uade.desa1.api.RoutesApiService;
import ar.edu.uade.desa1.domain.RouteStatusEnum;
import ar.edu.uade.desa1.domain.response.DeliveryRouteResponse;
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
                            for (DeliveryRouteResponse route : response.body()) {
                                addRouteCard(route);
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
        MaterialCardView card = new MaterialCardView(this);
        card.setCardElevation(8f);
        card.setRadius(16f);
        card.setUseCompatPadding(true);

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(24, 24, 24, 24);

        // Título
        TextView title = new TextView(this);
        title.setText(route.getPackageInfo());
        title.setTypeface(null, Typeface.BOLD);
        title.setTextSize(16);

        // Subtítulo
        TextView subtitle = new TextView(this);
        subtitle.setText(route.getOrigin() + " ➝ " + route.getDestination());
        subtitle.setTextSize(14);

        // Estado
        TextView status = new TextView(this);
        status.setText(RouteStatusEnum.valueOf(route.getStatus()).getSpanishStatus());
        status.setGravity(Gravity.END);
        status.setTypeface(null, Typeface.ITALIC);

        // Footer
        TextView footer = new TextView(this);
        footer.setText("Cliente: (sin datos)   Fecha: (sin datos)");
        footer.setTextSize(12);

        layout.addView(title);
        layout.addView(subtitle);
        layout.addView(status);
        layout.addView(footer);

        card.addView(layout, new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        ));

        // Agregar al contenedor principal
        routesContainer.addView(card);
    }
}