package ar.edu.uade.desa1;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import ar.edu.uade.desa1.util.NetworkUtils;

public class ForgotPasswordActivity extends AppCompatActivity {

    private EditText emailInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (!NetworkUtils.isConnected(this)) {
            startActivity(new Intent(this, NoInternetActivity.class));
            finish();
            return;
        }

        setContentView(R.layout.activity_forgot_password);

        emailInput = findViewById(R.id.email_input);
        Button recoverButton = findViewById(R.id.recover_button);

        recoverButton.setOnClickListener(view -> {
            String email = emailInput.getText().toString().trim();
            if (email.isEmpty()) {
                Toast.makeText(this, "Ingresá un email", Toast.LENGTH_SHORT).show();
                return;
            }
            
            Toast.makeText(ForgotPasswordActivity.this, "Si tu Email existe, te enviaremos un código", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(ForgotPasswordActivity.this, OtpActivity.class);
            intent.putExtra("recover", true);
            intent.putExtra("email", email);
            startActivity(intent);
            finish();
        });
    }
}