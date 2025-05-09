package ar.edu.uade.desa1;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import javax.inject.Inject;

import ar.edu.uade.desa1.domain.request.PasswordResetRequest;
import ar.edu.uade.desa1.repository.AuthRepository;
import ar.edu.uade.desa1.util.NetworkUtils;
import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class ResetPasswordActivity extends AppCompatActivity {

    private TextInputEditText passwordInput;
    private TextInputEditText confirmPasswordInput;
    private TextInputLayout passwordLayout;
    private TextInputLayout confirmPasswordLayout;
    private Button resetButton;
    private ProgressBar progressBar;
    private String email;

    @Inject
    AuthRepository authRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (!NetworkUtils.isConnected(this)) {
            startActivity(new Intent(this, NoInternetActivity.class));
            finish();
            return;
        }

        setContentView(R.layout.activity_reset_password);
        
        passwordInput = findViewById(R.id.passwordInput);
        confirmPasswordInput = findViewById(R.id.confirmPasswordInput);
        passwordLayout = findViewById(R.id.passwordLayout);
        confirmPasswordLayout = findViewById(R.id.confirmPasswordLayout);
        resetButton = findViewById(R.id.resetButton);
        progressBar = findViewById(R.id.progressBar);
        
        if (getIntent().hasExtra("email")) {
            email = getIntent().getStringExtra("email");
            
            if (email == null) {
                Toast.makeText(this, "Error: No se pudo obtener el email", Toast.LENGTH_LONG).show();
                navigateToLogin();
                return;
            }
        } else {
            Toast.makeText(this, "Error: No se pudo identificar el usuario", Toast.LENGTH_LONG).show();
            navigateToLogin();
            return;
        }
        
        resetButton.setOnClickListener(v -> resetPassword());
    }
    
    private void resetPassword() {
        passwordLayout.setError(null);
        confirmPasswordLayout.setError(null);
        
        String password = passwordInput.getText().toString().trim();
        String confirmPassword = confirmPasswordInput.getText().toString().trim();
        
        if (TextUtils.isEmpty(password)) {
            passwordLayout.setError("La contraseña no puede estar vacía");
            return;
        }
        
        if (password.length() < 6) {
            passwordLayout.setError("La contraseña debe tener al menos 6 caracteres");
            return;
        }
        
        if (!password.equals(confirmPassword)) {
            confirmPasswordLayout.setError("Las contraseñas no coinciden");
            return;
        }
        
        showLoading(true);
        
        PasswordResetRequest request = new PasswordResetRequest(email, password);
        
        authRepository.resetPassword(request, new AuthRepository.OnResetPasswordCallback() {
            @Override
            public void onSuccess() {
                showLoading(false);
                Toast.makeText(ResetPasswordActivity.this, 
                    "¡Tu contraseña ha sido actualizada correctamente!", 
                    Toast.LENGTH_LONG).show();
                
                navigateToLogin();
            }
            
            @Override
            public void onError(String errorMessage) {
                showLoading(false);
                Toast.makeText(ResetPasswordActivity.this, 
                    errorMessage, 
                    Toast.LENGTH_LONG).show();
            }
        });
    }
    
    private void navigateToLogin() {
        Intent intent = new Intent(ResetPasswordActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
    
    private void showLoading(boolean isLoading) {
        if (isLoading) {
            progressBar.setVisibility(View.VISIBLE);
            resetButton.setEnabled(false);
        } else {
            progressBar.setVisibility(View.GONE);
            resetButton.setEnabled(true);
        }
    }
} 