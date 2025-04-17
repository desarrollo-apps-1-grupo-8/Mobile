package ar.edu.uade.desa1;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import javax.inject.Inject;

import ar.edu.uade.desa1.domain.request.AuthRegisterRequest;
import ar.edu.uade.desa1.domain.response.AuthRegisterResponse;
import ar.edu.uade.desa1.repository.AuthRepository;
import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class RegisterActivity extends AppCompatActivity {

    private EditText edtFirstName, edtLastName, edtDni, edtPhone, edtEmail, edtPassword;
    private Button btnRegister;
    private ProgressBar progressBar;

    @Inject
    AuthRepository authRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        setTitle("Registro de Usuario");

        initViews();
        setupListeners();
    }

    private void initViews() {
        edtFirstName = findViewById(R.id.edtFirstName);
        edtLastName = findViewById(R.id.edtLastName);
        edtDni = findViewById(R.id.edtDni);
        edtPhone = findViewById(R.id.edtPhone);
        edtEmail = findViewById(R.id.edtEmail);
        edtPassword = findViewById(R.id.edtPassword);
        btnRegister = findViewById(R.id.btnRegister);
        progressBar = findViewById(R.id.progressBar);
    }

    private void setupListeners() {
        btnRegister.setOnClickListener(v -> {
            if (validateInputs()) {
                registerUser();
            }
        });
    }

    private boolean validateInputs() {
        String firstName = edtFirstName.getText().toString().trim();
        String lastName = edtLastName.getText().toString().trim();
        String dni = edtDni.getText().toString().trim();
        String phone = edtPhone.getText().toString().trim();
        String email = edtEmail.getText().toString().trim();
        String password = edtPassword.getText().toString();

        if (firstName.isEmpty()) {
            edtFirstName.setError("Ingrese su nombre");
            return false;
        }
        if (lastName.isEmpty()) {
            edtLastName.setError("Ingrese su apellido");
            return false;
        }
        if (dni.isEmpty()) {
            edtDni.setError("Ingrese su DNI");
            return false;
        }
        if (phone.isEmpty()) {
            edtPhone.setError("Ingrese su teléfono");
            return false;
        }
        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            edtEmail.setError("Ingrese un email válido");
            return false;
        }
        if (password.isEmpty() || password.length() < 6) {
            edtPassword.setError("La contraseña debe tener al menos 6 caracteres");
            return false;
        }

        return true;
    }

    private void registerUser() {
        showLoading(true);

        String firstName = edtFirstName.getText().toString().trim();
        String lastName = edtLastName.getText().toString().trim();
        String dni = edtDni.getText().toString().trim();
        String phone = edtPhone.getText().toString().trim();
        String email = edtEmail.getText().toString().trim();
        String password = edtPassword.getText().toString();

        long roleId = 1;

        AuthRegisterRequest request = new AuthRegisterRequest(
                email, password, roleId, firstName, lastName, dni, phone
        );

        authRepository.register(request, new AuthRepository.OnRegisterCallback() {
            @Override
            public void onSuccess(AuthRegisterResponse response) {
                showLoading(false);

                Toast.makeText(RegisterActivity.this,
                    "¡Registro exitoso! Se ha enviado un correo a tu casilla para verificar la cuenta.",
                    Toast.LENGTH_LONG).show();

                new Handler(Looper.getMainLooper()).postDelayed(() -> {
                    Intent intent = new Intent(RegisterActivity.this, OtpActivity.class);
                    intent.putExtra("email", email);
                    startActivity(intent);
                    finish();
                }, 2000); // Espera 2 segundos para que el usuario vea el Toast
            }

            @Override
            public void onError(String errorMessage) {
                showLoading(false);
                Toast.makeText(RegisterActivity.this, errorMessage, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void showLoading(boolean show) {
        if (progressBar != null) {
            progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
            btnRegister.setEnabled(!show);
        }
    }
}
