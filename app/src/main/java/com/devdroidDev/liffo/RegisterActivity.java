package com.devdroidDev.liffo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.security.PrivateKey;


public class RegisterActivity extends AppCompatActivity {

    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    EditText regUserName, regEmail, regPhone, regPassword, regConfirmPassword;
    Button registerButton;
    ProgressDialog progressDialog;
    FirebaseAuth auth;
    FirebaseUser firebaseUser;
    FirebaseFirestore firestore;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        regUserName = findViewById(R.id.regUserName);
        regEmail = findViewById(R.id.regEmail);
        regPhone = findViewById(R.id.regPhone);
        regPassword = findViewById(R.id.regPassword);
        registerButton = findViewById(R.id.registerUser_Button);
        regConfirmPassword = findViewById(R.id.regConfirmPassword);
        progressDialog = new ProgressDialog(this);
        auth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();


        // Register the user through Register Button
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                performAuthentication();
            }
        });
    }

    private void performAuthentication() {
        // Take the userData that inputs from Fields e.g get the data form EditText
        String userName = regUserName.getText().toString();
        String email = regEmail.getText().toString().trim();
        String phoneNo = regPhone.getText().toString();
        String password = regPassword.getText().toString();
        String confirmPassword = regConfirmPassword.getText().toString();

        // Perform Validation on Data that enters From user side in EditText Fields
        if (!email.matches(emailPattern))
        {
            regEmail.setError("Enter Correct Email");
        }
        if (password.isEmpty() || password.length() < 6)
        {
            regPassword.setError("Enter Proper Password");
        }
        if (!password.equals(confirmPassword))
        {
            regConfirmPassword.setError("Password Not Matched");
        }
        else
        {
            progressDialog.setTitle("Registration");
            progressDialog.setIcon(R.drawable.verifieduser);
            progressDialog.setMessage("Please wait while registration");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();

            auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful())
                    {
                        progressDialog.dismiss();
                        // If registration is done successfully we move the user another Activity
                        sendUserToAnotherActivity();
                        Toast.makeText(getApplicationContext(),"Registration Successfully", Toast.LENGTH_SHORT).show();

                        // To store the user data in fireStore such as name, email, number, password
                        firestore.collection("liffo User")
                                .document(FirebaseAuth.getInstance().getUid())
                                // Here in set() method we pass the object of userModel class
                                .set(new UserModel(userName, email, phoneNo, password));
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

    private void sendUserToAnotherActivity() {
        Intent iLogout = new Intent(RegisterActivity.this,MainActivity.class);
        iLogout.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(iLogout);
    }
}