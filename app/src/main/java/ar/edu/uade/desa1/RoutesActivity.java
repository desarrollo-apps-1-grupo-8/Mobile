package ar.edu.uade.desa1;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

import javax.inject.Inject;

import ar.edu.uade.desa1.api.RoutesApiService;
import ar.edu.uade.desa1.domain.RouteStatusEnum;
import ar.edu.uade.desa1.domain.request.CreateRouteRequest;
import ar.edu.uade.desa1.domain.response.DeliveryRouteResponse;
import ar.edu.uade.desa1.util.AuthRouteHandler;
import dagger.hilt.android.AndroidEntryPoint;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


@AndroidEntryPoint
public class RoutesActivity extends AppCompatActivity {

    @Inject
    RoutesApiService routesApiService;
    @Inject
    AuthRouteHandler authRouteHandler;

    private EditText edtPackageInfo, edtOrigin, edtDestination, edtUserId;
    private Button btnCreateRoute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (!authRouteHandler.checkAuthentication(this, LoginActivity.class)) { //
            return;
        }

        ConstraintLayout layout = new ConstraintLayout(this);
        layout.setId(View.generateViewId());

        // Texto de bienvenida
        TextView textView = new TextView(this);
        textView.setText("¡Bienvenido a RoutesActivity!");
        textView.setTextSize(24);
        textView.setId(View.generateViewId());
        layout.addView(textView);

        // Botón para redirigir
        Button btnIrAHistorial = new Button(this);
        btnIrAHistorial.setText("Ir a Historial");
        btnIrAHistorial.setId(View.generateViewId());
        layout.addView(btnIrAHistorial);

        // Acción del botón
        btnIrAHistorial.setOnClickListener(v -> {
            Intent intent = new Intent(RoutesActivity.this, HistoryActivity.class);
            startActivity(intent);
        });

        // EditText para la información del paquete
        edtPackageInfo = new EditText(this);
        edtPackageInfo.setHint("Información del paquete");
        edtPackageInfo.setId(View.generateViewId());
        layout.addView(edtPackageInfo);

        // EditText para el origen
        edtOrigin = new EditText(this);
        edtOrigin.setHint("Origen");
        edtOrigin.setId(View.generateViewId());
        layout.addView(edtOrigin);

        // EditText para el destino
        edtDestination = new EditText(this);
        edtDestination.setHint("Destino");
        edtDestination.setId(View.generateViewId());
        layout.addView(edtDestination);

        // EditText para el ID del usuario
        edtUserId = new EditText(this);
        edtUserId.setHint("ID de usuario");
        edtUserId.setId(View.generateViewId());
        layout.addView(edtUserId);

        // Botón para crear la ruta
        btnCreateRoute = new Button(this);
        btnCreateRoute.setText("Crear Ruta");
        btnCreateRoute.setId(View.generateViewId());
        layout.addView(btnCreateRoute);

        // Acción del botón para crear la ruta
        btnCreateRoute.setOnClickListener(v -> {
            String packageInfo = edtPackageInfo.getText().toString().trim();
            String origin = edtOrigin.getText().toString().trim();
            String destination = edtDestination.getText().toString().trim();
            Long userId = Long.parseLong(edtUserId.getText().toString().trim());

            CreateRouteRequest request = new CreateRouteRequest(packageInfo, origin, destination, RouteStatusEnum.AVAILABLE, userId);

            routesApiService.createRoute(request).enqueue(new Callback<DeliveryRouteResponse>() {
                @Override
                public void onResponse(Call<DeliveryRouteResponse> call, Response<DeliveryRouteResponse> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        Toast.makeText(RoutesActivity.this, "Ruta creada exitosamente", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(RoutesActivity.this, "Error al crear ruta", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<DeliveryRouteResponse> call, Throwable t) {
                    Toast.makeText(RoutesActivity.this, "Fallo al conectar con el servidor", Toast.LENGTH_SHORT).show();
                }
            });
        });

        // Constraints
        ConstraintSet set = new ConstraintSet();
        set.clone(layout);

        // Centrar el texto
        set.connect(textView.getId(), ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP, 200);
        set.connect(textView.getId(), ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START, 0);
        set.connect(textView.getId(), ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END, 0);

        // Botón debajo del texto
        set.connect(btnIrAHistorial.getId(), ConstraintSet.TOP, textView.getId(), ConstraintSet.BOTTOM, 40);
        set.connect(btnIrAHistorial.getId(), ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START, 0);
        set.connect(btnIrAHistorial.getId(), ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END, 0);

        // Posicionar los EditText y el botón de creación de ruta
        set.connect(edtPackageInfo.getId(), ConstraintSet.TOP, btnIrAHistorial.getId(), ConstraintSet.BOTTOM, 40);
        set.connect(edtPackageInfo.getId(), ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START, 0);
        set.connect(edtPackageInfo.getId(), ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END, 0);

        set.connect(edtOrigin.getId(), ConstraintSet.TOP, edtPackageInfo.getId(), ConstraintSet.BOTTOM, 20);
        set.connect(edtOrigin.getId(), ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START, 0);
        set.connect(edtOrigin.getId(), ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END, 0);

        set.connect(edtDestination.getId(), ConstraintSet.TOP, edtOrigin.getId(), ConstraintSet.BOTTOM, 20);
        set.connect(edtDestination.getId(), ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START, 0);
        set.connect(edtDestination.getId(), ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END, 0);

        set.connect(edtUserId.getId(), ConstraintSet.TOP, edtDestination.getId(), ConstraintSet.BOTTOM, 20);
        set.connect(edtUserId.getId(), ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START, 0);
        set.connect(edtUserId.getId(), ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END, 0);

        set.connect(btnCreateRoute.getId(), ConstraintSet.TOP, edtUserId.getId(), ConstraintSet.BOTTOM, 40);
        set.connect(btnCreateRoute.getId(), ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START, 0);
        set.connect(btnCreateRoute.getId(), ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END, 0);

        set.applyTo(layout);

        setContentView(layout);
    }
}