package com.example.ujobs;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;

public class ResetPasswordActivity extends AppCompatActivity {

    TextInputLayout email;
    Button btnSend;
    FirebaseAuth mAuth;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_reset_password);

        email = findViewById(R.id.email);
        btnSend = findViewById(R.id.btnSend);
        progressDialog = new ProgressDialog(this);

        mAuth = FirebaseAuth.getInstance();


        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.setMessage("Espere un momento...");
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.show();
                resetPassword();
            }
        });
    }

    public void resetPassword(){
        String mail = email.getEditText().getText().toString().trim();


        if(!mail.isEmpty()){
            mAuth.setLanguageCode("es");
            mAuth.sendPasswordResetEmail(mail).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(ResetPasswordActivity.this, "Se ha enviado el correo para reestablecer la contraseña. ", Toast.LENGTH_SHORT).show();
                        Intent main = new Intent(ResetPasswordActivity.this, MainActivity.class);
                        startActivity(main);
                        finish();

                    }else{
                        Toast.makeText(ResetPasswordActivity.this, "No se ha podido encontrar esta dirección de correo.", Toast.LENGTH_SHORT).show();
                    }

                    progressDialog.dismiss();
                }
            });
        }else{
            Toast.makeText(this, "Ingrese un correo valido. ", Toast.LENGTH_SHORT).show();
        }
    }
}