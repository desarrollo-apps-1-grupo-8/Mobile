package ar.edu.uade.desa1;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.android.material.textview.MaterialTextView;

import javax.inject.Inject;

import ar.edu.uade.desa1.domain.request.SendVerificationCodeRequest;
import ar.edu.uade.desa1.domain.request.VerifyCodeRequest;
import ar.edu.uade.desa1.domain.response.SendVerificationCodeResponse;
import ar.edu.uade.desa1.domain.response.VerifyCodeResponse;
import ar.edu.uade.desa1.repository.AuthRepository;
import ar.edu.uade.desa1.domain.request.VerifyCodeRequest;
import ar.edu.uade.desa1.domain.response.VerifyCodeResponse;
import ar.edu.uade.desa1.util.NetworkUtils;
import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class OtpActivity extends AppCompatActivity {

    @Inject
    AuthRepository authRepository;

    private TextInputEditText edtOtp;
    private MaterialButton btnVerifyOtp;
    private MaterialButton btnResendCode;
    private ProgressBar progressBarOtp;
    private TextInputLayout otpInputLayout;
    private MaterialTextView tvOtpInstruction;
    private View cardView;
    private Boolean isRecover;
    private String email;
    private CountDownTimer resendTimer;
    private static final long RESEND_DELAY = 60000; // 60 segundos en milisegundos

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        if (!NetworkUtils.isConnected(this)) {
            startActivity(new Intent(this, NoInternetActivity.class));
            finish();
            return;
        }

        setContentView(R.layout.activity_otp);
        setTitle("Verificación OTP");

        edtOtp = findViewById(R.id.edtOtp);
        btnVerifyOtp = findViewById(R.id.btnVerifyOtp);
        btnResendCode = findViewById(R.id.btnResendCode);
        progressBarOtp = findViewById(R.id.progressBarOtp);
        otpInputLayout = findViewById(R.id.otpInputLayout);
        tvOtpInstruction = findViewById(R.id.tvOtpInstruction);
        cardView = (View) otpInputLayout.getParent().getParent();

        email = getIntent().getStringExtra("email");
        isRecover = getIntent().getBooleanExtra("recover", false);

        sendInitialVerificationCode();

        btnVerifyOtp.setOnClickListener(v -> {
            String otp = edtOtp.getText() != null ? edtOtp.getText().toString().trim() : "";
            if (otp.isEmpty() || otp.length() < 6) {
                showOtpError("Ingrese el código OTP de 6 dígitos");
                return;
            }
            verifyOtp(otp);
        });

        btnResendCode.setOnClickListener(v -> resendVerificationCode());
    }

    private void startResendTimer() {
        btnResendCode.setEnabled(false);
        resendTimer = new CountDownTimer(RESEND_DELAY, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                long seconds = millisUntilFinished / 1000;
                btnResendCode.setText(String.format("Reenviar código (%d)", seconds));
            }

            @Override
            public void onFinish() {
                btnResendCode.setEnabled(true);
                btnResendCode.setText("Reenviar código");
            }
        }.start();
    }

    private void sendInitialVerificationCode() {
        showLoading(true);
        clearOtpError();
        tvOtpInstruction.setText("Enviando código de verificación...");
        tvOtpInstruction.setTextColor(getColor(R.color.text_primary));
        System.out.println("EL TYPE ESTA VINIENDO CON: " + isRecover);
        SendVerificationCodeRequest request = new SendVerificationCodeRequest(email, isRecover);

        authRepository.sendVerificationCode(request, new AuthRepository.OnSendVerificationCodeCallback() {
            @Override
            public void onSuccess(SendVerificationCodeResponse response) {
                showLoading(false);
                if (response.isSuccess()) {
                    tvOtpInstruction.setText("Verificá tu identidad");
                    tvOtpInstruction.setTextColor(getColor(R.color.text_primary));
                    Toast.makeText(OtpActivity.this, 
                        "Se ha enviado un código de verificación a tu email", 
                        Toast.LENGTH_LONG).show();
                    startResendTimer();
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

    private void resendVerificationCode() {
        showLoading(true);
        clearOtpError();

        SendVerificationCodeRequest request = new SendVerificationCodeRequest(email, isRecover);

        authRepository.sendVerificationCode(request, new AuthRepository.OnSendVerificationCodeCallback() {
            @Override
            public void onSuccess(SendVerificationCodeResponse response) {
                showLoading(false);
                if (response.isSuccess()) {
                    Toast.makeText(OtpActivity.this, 
                        "Se ha enviado un nuevo código de verificación a tu email", 
                        Toast.LENGTH_LONG).show();
                    startResendTimer();
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (resendTimer != null) {
            resendTimer.cancel();
        }
    }

    private void verifyOtp(String otp) {
        showLoading(true);
        clearOtpError();

        VerifyCodeRequest request = new VerifyCodeRequest(email, otp, isRecover);

        authRepository.verifyCode(request, new AuthRepository.OnVerifyCodeCallback() {
            @Override
            public void onSuccess(VerifyCodeResponse response) {
                showLoading(false);
                if (response.isSuccess()) {
                    otpInputLayout.setBoxStrokeColor(getColor(R.color.purple_500));
                    otpInputLayout.setError(null);
                    tvOtpInstruction.setText("¡OTP verificado correctamente!");
                    tvOtpInstruction.setTextColor(getColor(R.color.purple_500));

                    new Handler(Looper.getMainLooper()).postDelayed(() -> {
                        Intent intent = new Intent(OtpActivity.this, isRecover ? ResetPasswordActivity.class : LoginActivity.class);
                        if (isRecover) {
                            intent.putExtra("email", email);
                        }
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();
                    }, 1500);
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