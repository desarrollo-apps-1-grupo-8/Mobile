package ar.edu.uade.desa1;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.android.material.textview.MaterialTextView;

import javax.inject.Inject;
import ar.edu.uade.desa1.repository.AuthRepository;
import ar.edu.uade.desa1.domain.request.VerifyEmailRequest;
import ar.edu.uade.desa1.domain.response.VerifyEmailResponse;
import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class OtpActivity extends AppCompatActivity {

    @Inject
    AuthRepository authRepository;

    private TextInputEditText edtOtp;
    private MaterialButton btnVerifyOtp;
    private ProgressBar progressBarOtp;
    private TextInputLayout otpInputLayout;
    private MaterialTextView tvOtpInstruction;
    private View cardView;
    
    private String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);
        setTitle("Verificación OTP");

        edtOtp = findViewById(R.id.edtOtp);
        btnVerifyOtp = findViewById(R.id.btnVerifyOtp);
        progressBarOtp = findViewById(R.id.progressBarOtp);
        otpInputLayout = findViewById(R.id.otpInputLayout);
        tvOtpInstruction = findViewById(R.id.tvOtpInstruction);
        cardView = (View) otpInputLayout.getParent().getParent(); // CardView

        email = getIntent().getStringExtra("email");

        btnVerifyOtp.setOnClickListener(v -> {
            String otp = edtOtp.getText() != null ? edtOtp.getText().toString().trim() : "";
            if (otp.isEmpty() || otp.length() < 6) {
                showOtpError("Ingrese el código OTP de 6 dígitos");
                return;
            }
            verifyOtp(otp);
        });
    }

    private void verifyOtp(String otp) {
        showLoading(true);
        clearOtpError();

        VerifyEmailRequest request = new VerifyEmailRequest(email, otp);

        authRepository.verifyEmail(request, new AuthRepository.OnVerifyEmailCallback() {
            @Override
            public void onSuccess(VerifyEmailResponse response) {
                showLoading(false);
                if (response.isSuccess()) {
                    otpInputLayout.setBoxStrokeColor(getColor(R.color.purple_500));
                    otpInputLayout.setError(null);
                    tvOtpInstruction.setText("¡OTP verificado correctamente!");
                    tvOtpInstruction.setTextColor(getColor(R.color.purple_500));

                    // Redirigir a la pantalla principal
                    new Handler(Looper.getMainLooper()).postDelayed(() -> {
                        Intent intent = new Intent(OtpActivity.this, RoutesActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();
                    }, 1500); // Delay opcional para que vea el mensaje
            } else {

                    showOtpError(response.getMessage());
                }
            }
            @Override
            public void onError(String errorMessage) {
                showLoading(false);
                showOtpError(errorMessage);
            }
        });
    }

    private void showOtpError(String errorMsg) {
        otpInputLayout.setError(errorMsg);
        otpInputLayout.setBoxStrokeColor(getColor(android.R.color.holo_red_dark));
        shakeView(cardView);
        tvOtpInstruction.setText("Hubo un error, intente nuevamente");
        tvOtpInstruction.setTextColor(getColor(android.R.color.holo_red_dark));
    }

    private void clearOtpError() {
        otpInputLayout.setError(null);
        otpInputLayout.setBoxStrokeColor(getColor(R.color.purple_500));
        tvOtpInstruction.setText("Verificá tu identidad");
        tvOtpInstruction.setTextColor(getColor(R.color.purple_500));
    }

    private void showLoading(boolean show) {
        progressBarOtp.setVisibility(show ? View.VISIBLE : View.GONE);
        btnVerifyOtp.setEnabled(!show);
        edtOtp.setEnabled(!show);
    }

    private void shakeView(View view) {
        Animation shake = new TranslateAnimation(0, 24, 0, 0);
        shake.setDuration(300);
        shake.setRepeatMode(Animation.REVERSE);
        shake.setRepeatCount(2);
        view.startAnimation(shake);
    }
} 