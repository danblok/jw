package com.example.jewelry;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.jewelry.databinding.ActivitySignUpBinding;

public class SignUpActivity extends AppCompatActivity {

    private static final String TAG = "SignUpActivity";
    private ActivitySignUpBinding binding;
    private SingUpViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setupListeners();
        viewModel = new ViewModelProvider(this).get(SingUpViewModel.class);
        observeViewModel();
    }

    private void setupListeners() {
        binding.buttonCancel.setOnClickListener(view -> {
            startActivity(SignInActivity.newIntent(this));
        });
        binding.buttonSignUp.setOnClickListener(view -> {
            String email = Utils.getTrimmedValue(binding.editTextEmail);
            String password = Utils.getTrimmedValue(binding.editTextPassword);
            String fullName = Utils.getTrimmedValue(binding.editTextFullName);
            String phone = Utils.getTrimmedValue(binding.editTextPhoneNumber);

            if (Validator.isEmailValid(email)
                    && password.length() > 4
                    && fullName.length() > 3
                    && Validator.isPhoneNumberValid(phone)) {
                viewModel.signUp(email, password, fullName, phone);
            } else {
                Utils.showAlertDialog(this,
                        getString(R.string.invalid_data),
                        getString(R.string.fill_the_data_correctly));
            }
        });
    }

    private void observeViewModel() {
        viewModel.getError().observe(this, err -> {
            Log.d(TAG, err);
        });
        viewModel.getIsSignedUp().observe(this, isSignedUp -> {
            if (isSignedUp) {
                startActivity(SignInActivity.newIntent(this));
            }
        });
    }

    public static Intent newIntent(Context context) {
        return new Intent(context, SignUpActivity.class);
    }
}