package ar.edu.uade.desa1;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import ar.edu.uade.desa1.domain.request.AuthRegisterRequest;
import ar.edu.uade.desa1.domain.response.AuthRegisterResponse;
import ar.edu.uade.desa1.repository.AuthRepository;
import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class RegisterActivity extends AppCompatActivity {

    private EditText edtFirstName, edtLastName, edtDni, edtPhone, edtEmail, edtPassword, edtConfirmPassword;
    private Button btnRegister;
    private ProgressBar progressBar;
    private Spinner spinnerRole;

    // Mapeo de roles a sus IDs
    private final Map<String, Long> roleMap = new HashMap<>();

    @Inject
    AuthRepository authRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        setTitle("Registro de Usuario");

        roleMap.put(getString(R.string.role_deliveryman), 1L);
        roleMap.put(getString(R.string.role_user), 2L);

        initViews();
        setupRoleSpinner();
        setupListeners();
    }

    private void initViews() {
        edtFirstName = findViewById(R.id.edtFirstName);
        edtLastName = findViewById(R.id.edtLastName);
        edtDni = findViewById(R.id.edtDni);
        edtPhone = findViewById(R.id.edtPhone);
        edtEmail = findViewById(R.id.edtEmail);
        edtPassword = findViewById(R.id.edtPassword);
        edtConfirmPassword = findViewById(R.id.edtConfirmPassword);
        btnRegister = findViewById(R.id.btnRegister);
        progressBar = findViewById(R.id.progressBar);
        spinnerRole = findViewById(R.id.spinnerRole);
    }

    private void setupRoleSpinner() {
        String[] roles = {
            getString(R.string.role_deliveryman),
            getString(R.string.role_user)
        };

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
            this,
            android.R.layout.simple_spinner_item,
            roles
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerRole.setAdapter(adapter);
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
        String confirmPassword = edtConfirmPassword.getText().toString();

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

        if (!password.equals(confirmPassword)) {
            edtConfirmPassword.setError("Las contraseñas no coinciden");
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

        // Obtener el rol seleccionado del spinner
        String selectedRole = spinnerRole.getSelectedItem().toString();
        long roleId = roleMap.get(selectedRole);

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
                    intent.putExtra("recover", false);
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
