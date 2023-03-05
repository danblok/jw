package com.example.jewelry;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.jewelry.databinding.ActivitySignInBinding;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;

public class SignInActivity extends AppCompatActivity {

    private ActivitySignInBinding binding;
    private SingInViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignInBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        viewModel = new ViewModelProvider(this).get(SingInViewModel.class);
        observeViewModel();
        setupListeners();
    }

    private void setupListeners() {
        binding.textViewForgotPassword.setOnClickListener(view -> {
            startActivity(SignUpActivity.newIntent(this));
        });
        binding.buttonSignIn.setOnClickListener(view -> {

            String email = Utils.getTrimmedValue(binding.editTextEmail);
            String password = Utils.getTrimmedValue(binding.editTextPassword);

            if (Validator.isEmailValid(email) && password.length() > 4) {
                viewModel.signInWithEmailAndPassword(email, password);
            } else {
                Utils.showAlertDialog(this,
                        getString(R.string.invalid_data),
                        getString(R.string.invalid_login_or_password));
            }
        });
    }

    private void observeViewModel() {
        viewModel.getError().observe(this, err -> {
            Toast.makeText(this, err, Toast.LENGTH_SHORT).show();
        });
        viewModel.getToken().observe(this, token -> {
            try {
                Jws<Claims> jwt = Jwts.parserBuilder()
                        .setSigningKey(Constants.SECRET_KEY.getBytes())
                        .build()
                        .parseClaimsJws(token);
                startActivity(MainScreenActivity.newIntent(this));
            } catch (Exception err) {
                Log.d("token", err.getMessage());
                Toast.makeText(this, R.string.invalid_token, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public static Intent newIntent(Context context) {
        return new Intent(context, SignInActivity.class);
    }
}