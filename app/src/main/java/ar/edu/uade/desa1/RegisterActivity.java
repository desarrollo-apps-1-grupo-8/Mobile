package ar.edu.uade.desa1;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import javax.inject.Inject;

import ar.edu.uade.desa1.domain.request.AuthRegisterRequest;
import ar.edu.uade.desa1.domain.response.AuthRegisterResponse;
import ar.edu.uade.desa1.repository.AuthRepository;

public class RegisterActivity extends BaseActivity {

    private EditText edtFirstName, edtLastName, edtDni, edtPhone, edtEmail, edtPassword;
    private Button btnRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        setTitle("Registro de Usuario");
        
        // No necesitamos verificar la sesión en la pantalla de registro
        setSkipSessionCheck(true);

        initViews();
        setupListeners();
    }
    
    @Override
    protected boolean requiresAuth() {
        return false;
    }

    private void initViews() {
        edtFirstName = findViewById(R.id.edtFirstName);
        edtLastName = findViewById(R.id.edtLastName);
        edtDni = findViewById(R.id.edtDni);
        edtPhone = findViewById(R.id.edtPhone);
        edtEmail = findViewById(R.id.edtEmail);
        edtPassword = findViewById(R.id.edtPassword);
        btnRegister = findViewById(R.id.btnRegister);

        btnRegister.setOnClickListener(v -> {
            String firstName = edtFirstName.getText().toString();
            String lastName = edtLastName.getText().toString();
            String dni = edtDni.getText().toString();
            String phone = edtPhone.getText().toString();
            String email = edtEmail.getText().toString();
            String password = edtPassword.getText().toString();

            String mensaje = "Nombre: " + firstName + "\nEmail: " + email;
            Toast.makeText(this, mensaje, Toast.LENGTH_LONG).show();
        });
    }
}
