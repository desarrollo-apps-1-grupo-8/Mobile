package ar.edu.uade.desa1;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

import javax.inject.Inject;

import ar.edu.uade.desa1.util.AuthRouteHandler;
import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class RoutesActivity extends AppCompatActivity {

    @Inject
    AuthRouteHandler authRouteHandler;

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

        set.applyTo(layout);

        setContentView(layout);
    }
}