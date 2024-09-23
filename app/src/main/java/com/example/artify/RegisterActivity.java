package com.example.artify;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        EditText etRegisterUsername = findViewById(R.id.et_register_username);
        EditText etRegisterEmail = findViewById(R.id.et_register_email);
        EditText etRegisterPassword = findViewById(R.id.et_register_password);
        EditText etConfirmPassword = findViewById(R.id.et_confirm_password);
        EditText etRegisterPhone = findViewById(R.id.et_register_phone);

        Button btnSignupLogin = findViewById(R.id.btn_sign_up_login);
        Button btnSignupRegister = findViewById(R.id.btn_sign_up_register);

        btnSignupRegister.setOnClickListener(v -> {
            String username = etRegisterUsername.getText().toString();
            String email = etRegisterEmail.getText().toString();
            String password = etRegisterPassword.getText().toString();
            String confirmPassword = etConfirmPassword.getText().toString();
            String phone = etRegisterPhone.getText().toString();

            if (!isEmailValid(email)) {
                Toast.makeText(RegisterActivity.this, "Invalid email format!", Toast.LENGTH_SHORT).show();
            } else if (!isPhoneNumberValid(phone)) {
                Toast.makeText(RegisterActivity.this, "Invalid phone number format!", Toast.LENGTH_SHORT).show();
            } else if (!password.equals(confirmPassword) || password.isEmpty() || username.isEmpty()) {
                Toast.makeText(RegisterActivity.this, "Passwords do not match or fields are empty!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(RegisterActivity.this, "Well done!", Toast.LENGTH_SHORT).show();

                // Connection with DB
                DatabaseHelper dbHelper = new DatabaseHelper(RegisterActivity.this);
                boolean isInserted = dbHelper.insertUser(username, email, password, phone);

                if (isInserted) {
                    Toast.makeText(RegisterActivity.this, "Registration Successful!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(RegisterActivity.this, "Registration Failed!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnSignupLogin.setOnClickListener(v -> {
            Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
            startActivity(intent);
        });
    }

    private boolean isEmailValid(String email) {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private boolean isPhoneNumberValid(String phone) {
        String phonePattern = "^(\\+8801|01)[0-9]{9}$";
        return Pattern.compile(phonePattern).matcher(phone).matches();
    }
}
