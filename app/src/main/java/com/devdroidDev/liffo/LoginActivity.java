package com.devdroidDev.liffo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    TextView registerActivity;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    EditText  loginEmail, loginPassword;
    Button loginButton;
    ProgressDialog progressDialog;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

       registerActivity = findViewById(R.id.registerActivity);
       loginEmail = findViewById(R.id.emailLogin);
       loginPassword = findViewById(R.id.passwordLogin);
       loginButton = findViewById(R.id.LoginButton);
       auth = FirebaseAuth.getInstance();
       progressDialog = new ProgressDialog(this);

        registerActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this,RegisterActivity.class));
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                performLogin();
            }
        });

    }

    private void performLogin() {
        String email = loginEmail.getText().toString().trim();
        String password = loginPassword.getText().toString();

        // perform validation on userData
        if (!email.matches(emailPattern))
        {
            loginEmail.setError("Enter Correct Email");
        }
        if (password.isEmpty() || password.length() < 6)
        {
            loginPassword.setError("Enter Proper Password");
        }
        else {
            progressDialog.setTitle("Login");
            progressDialog.setIcon(R.drawable.login_icon);
            progressDialog.setMessage("Please wait while login");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();

            auth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful())
                    {
                        progressDialog.dismiss();
                        // If login is done successfully we move the user another Activity
                        sendUserToHomeActivity();
                        Toast.makeText(getApplicationContext(),"Login Successfully", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(),""+ task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    private void sendUserToHomeActivity() {
        Intent iLogout = new Intent(LoginActivity.this,MainActivity.class);
        iLogout.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(iLogout);
    }
}