package ar.edu.uade.desa1;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import android.content.SharedPreferences;
import org.json.JSONObject;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import ar.edu.uade.desa1.RoutesActivity;

import javax.inject.Inject;

import ar.edu.uade.desa1.domain.request.AuthLoginRequest;
import ar.edu.uade.desa1.repository.AuthRepository;
import ar.edu.uade.desa1.util.TokenManager;
import dagger.hilt.android.AndroidEntryPoint;


@AndroidEntryPoint
public class LoginActivity extends AppCompatActivity {
    @Inject
    AuthRepository authRepository;

    @Inject
    TokenManager tokenManager;

    //login con Retrofit
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        EditText emailInput = findViewById(R.id.editTextEmail);
        EditText passwordInput = findViewById(R.id.editTextPassword);
        Button loginButton = findViewById(R.id.buttonLogin);

        loginButton.setOnClickListener(v -> {
            String email = emailInput.getText().toString().trim();
            String password = passwordInput.getText().toString().trim();

            AuthLoginRequest request = new AuthLoginRequest(email, password); //Se usa para enviar el JSON al back. Modela el cuerpo del JSON.

            authRepository.login(request, result -> {
                if (result.isSuccess()) {
                    String token = result.getToken();

                    tokenManager.saveToken(token);

                    Toast.makeText(this, "¡Login exitoso!", Toast.LENGTH_SHORT).show();

                    startActivity(new Intent(LoginActivity.this, RoutesActivity.class)); //Redireccion a Routes Activity
                    finish();
                } else {
                    Toast.makeText(this, "Login fallido", Toast.LENGTH_SHORT).show();
                }
            });
        });
    }


}
