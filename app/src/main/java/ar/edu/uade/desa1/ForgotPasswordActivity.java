package ar.edu.uade.desa1;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import ar.edu.uade.desa1.api.AuthApiService;

import ar.edu.uade.desa1.api.RetrofitClient;
import ar.edu.uade.desa1.domain.request.PasswordRecoveryRequest;

import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ForgotPasswordActivity extends AppCompatActivity {

    private EditText emailInput;
    private AuthApiService authService;  // tu interfaz de Retrofit


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        emailInput = findViewById(R.id.email_input);
        Button recoverButton = findViewById(R.id.recover_button);

        authService = RetrofitClient.getInstance().create(AuthApiService.class);

        recoverButton.setOnClickListener(view -> {
            String email = emailInput.getText().toString().trim();
            if (email.isEmpty()) {
                Toast.makeText(this, "Ingresá un email", Toast.LENGTH_SHORT).show();
                return;
            }

            PasswordRecoveryRequest request = new PasswordRecoveryRequest(email);
            authService.recoverPassword(request).enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(ForgotPasswordActivity.this, "Revisá tu email (o consola)", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(ForgotPasswordActivity.this, "Error al recuperar contraseña", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    Toast.makeText(ForgotPasswordActivity.this, "Falló la conexión", Toast.LENGTH_SHORT).show();
                }
            });
        });

    }




}