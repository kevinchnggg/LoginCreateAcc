package com.example.testscanner;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.vishnusivadas.advanced_httpurlconnection.PutData;

import java.util.regex.Pattern;

public class Signup extends AppCompatActivity {

    private static final Pattern ALPHABET_PATTERN = Pattern.compile("^[a-zA-Z]+$");
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9._%+-]+@(?:[A-Za-z0-9-]+\\.)+[A-Za-z]{2,}$");

    TextInputEditText textInputEditTextFullname, textInputEditTextUsername, textInputEditTextPassword, textInputEditTextEmail;
    Button buttonCreateAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        textInputEditTextFullname = findViewById(R.id.etFullname);
        textInputEditTextUsername = findViewById(R.id.etUsername);
        textInputEditTextPassword = findViewById(R.id.etPassword);
        textInputEditTextEmail = findViewById(R.id.etEmail);
        buttonCreateAccount = findViewById(R.id.buttonCreateAccount);

        buttonCreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fullname, username, password, email;
                fullname = String.valueOf(textInputEditTextFullname.getText());
                username = String.valueOf(textInputEditTextUsername.getText());
                password = String.valueOf(textInputEditTextPassword.getText());
                email = String.valueOf(textInputEditTextEmail.getText());

                if (!fullname.equals("") && !username.equals("") && !password.equals("") && !email.equals("")) {
                    if (!isValidFullName(fullname)) {
                        // Display error message for invalid fullname
                        Toast.makeText(getApplicationContext(), "Invalid fullname! Only alphabets/letters are allowed.", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (!isValidEmail(email)) {
                        // Display error message for invalid email
                        Toast.makeText(getApplicationContext(), "Invalid email address!", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    Handler handler = new Handler(Looper.getMainLooper());
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            String[] field = new String[4];
                            field[0] = "fullname";
                            field[1] = "username";
                            field[2] = "password";
                            field[3] = "email";
                            //Creating array for data
                            String[] data = new String[4];
                            data[0] = fullname;
                            data[1] = username;
                            data[2] = password;
                            data[3] = email;

                            PutData putData = new PutData("http://192.168.68.105/LoginRegister/signup.php", "POST", field, data);
                            if (putData.startPut()) {
                                if (putData.onComplete()) {
                                    String result = putData.getResult();
                                    if(result.equals("Sign Up Success")){
                                        Intent intent= new Intent(getApplicationContext(),Login.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                    else{
                                        Toast.makeText(getApplicationContext(),result,Toast.LENGTH_SHORT).show();
                                    }

                                }
                            }

                        }
                    });
                } else {
                    Toast.makeText(getApplicationContext(), "All fields are required!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    private boolean isValidFullName(String fullName) {
        return ALPHABET_PATTERN.matcher(fullName).matches();
    }

    private boolean isValidEmail(String email) {
        return EMAIL_PATTERN.matcher(email).matches();
    }
}
