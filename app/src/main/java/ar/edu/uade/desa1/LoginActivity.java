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

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login); // conecta con el layout

        //Redireccion a pantalla de registro
        TextView registerLink = findViewById(R.id.textRegisterLink);
        registerLink.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });
        Button loginButton = findViewById(R.id.buttonLogin);
        EditText emailInput = findViewById(R.id.editTextEmail);
        EditText passwordInput = findViewById(R.id.editTextPassword);

        //Boton de login conectado al metodo de hacerLogin
        loginButton.setOnClickListener(v -> {
            String email = emailInput.getText().toString().trim();
            String password = passwordInput.getText().toString().trim();

            // Validacion campos vacios
            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(LoginActivity.this, "Por favor completar ambos campos", Toast.LENGTH_SHORT).show();
                return;
            }

            // Llamar al método que hace la solicitud HTTP
            hacerLogin(email, password);
        });
    }

    //Funcionalidad de login
    private void hacerLogin(String email, String password) {
        new Thread(() -> {
            try {
                URL url = new URL("http://10.0.2.2:8080/api/v1/login");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setDoOutput(true);

                String jsonInput = "{\"email\":\"" + email + "\",\"password\":\"" + password + "\"}";

                try (OutputStream os = conn.getOutputStream()) {
                    byte[] input = jsonInput.getBytes("utf-8");
                    os.write(input, 0, input.length);
                }

                int code = conn.getResponseCode();

                if (code == 200) {
                    // Leer el cuerpo de la respuesta
                    InputStream is = conn.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(is));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }
                    reader.close();

                    //Parsear el JSON y extraer el token
                    String json = response.toString();
                    JSONObject jsonObject = new JSONObject(json);
                    String token = jsonObject.getString("token");

                    //Guardar el token en SharedPreferences. Se guarda localmente en el celular o emulador.
                    SharedPreferences prefs = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
                    prefs.edit().putString("jwt_token", token).apply();

                    //Notificar al usuario y redireccion al Main activity
                    runOnUiThread(() -> {
                        Toast.makeText(LoginActivity.this, "¡Login exitoso!", Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);//Intent prepara la transicion al Main Activity
                        startActivity(intent);// Inicia la nueva pantalla
                        finish(); // evita que vuelva al login con el botón atrás
                    });

                }

                else {
                    runOnUiThread(() ->
                            Toast.makeText(LoginActivity.this, "Login fallido. Código: " + code, Toast.LENGTH_SHORT).show()
                    );
                }

            } catch (Exception e) {
                runOnUiThread(() ->
                        Toast.makeText(LoginActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show()
                );
                e.printStackTrace();
            }
        }).start();
    }
}
