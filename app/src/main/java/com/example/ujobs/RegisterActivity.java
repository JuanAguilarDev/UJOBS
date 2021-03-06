package com.example.ujobs;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.util.PatternsCompat;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.ujobs.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.UUID;
import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {

    Button btnYes;
    Button btnNo;
    Spinner sp;
    TextInputLayout name;
    TextInputLayout email;
    TextInputLayout password;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        firebaseAuth = FirebaseAuth.getInstance();


        // Variables association
        sp = findViewById(R.id.sp);
        name = findViewById(R.id.name);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        btnYes = findViewById(R.id.btnYes);
        btnNo = findViewById(R.id.btnNo);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.states, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp.setAdapter(adapter);

        //  database
        firebaseStart();

        // User isn??t and tutor
        btnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createUser();
            }
        });

        // User is a tutor
        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createUserTutor();
            }
        });

    }

    private void firebaseStart(){
        FirebaseApp.initializeApp(this);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
    }

    public void createUser(){
        if(!validate()){
            Toast.makeText(this, "Los datos no se han especificado de una correcta manera.", Toast.LENGTH_SHORT).show();
            return;
        }else{
            firebaseAuth.createUserWithEmailAndPassword(email.getEditText().getText().toString().trim(), password.getEditText().getText().toString().trim()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        String id = task.getResult().getUser().getUid().trim();
                        User user = new User();
                        user.setUid(id);
                        user.setName(name.getEditText().getText().toString().trim());
                        user.setEmail(email.getEditText().getText().toString().trim());
                        user.setState(sp.getSelectedItem().toString().trim());
                        user.setPassword(password.getEditText().getText().toString().trim());
                        user.setIsTutor(false);
                        databaseReference.child("User").child(user.getUid()).setValue(user);
                        Toast.makeText(RegisterActivity.this, "Usuario creado", Toast.LENGTH_SHORT).show();
                        Intent main = new Intent(RegisterActivity.this, MainActivity.class);
                        startActivity(main);
                        finish();
                    }else{
                        String errorCode = ((FirebaseAuthException) task.getException()).getErrorCode();
                        toastError(errorCode);
                    }
                }
            });


        }
    }

    public void createUserTutor(){
        if(!validate()){
            Toast.makeText(this, "Los datos no se han especificado de una correcta manera.", Toast.LENGTH_SHORT).show();
            return;
        }else{
            firebaseAuth.createUserWithEmailAndPassword(email.getEditText().getText().toString().trim(), password.getEditText().getText().toString().trim()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        String id = task.getResult().getUser().getUid().trim();
                        User user = new User();
                        user.setUid(id);
                        user.setName(name.getEditText().getText().toString().trim());
                        user.setEmail(email.getEditText().getText().toString().trim());
                        user.setState(sp.getSelectedItem().toString().trim());
                        user.setPassword(password.getEditText().getText().toString().trim());
                        user.setIsTutor(true);
                        databaseReference.child("User").child(user.getUid()).setValue(user);
                        Toast.makeText(RegisterActivity.this, "Usuario creado", Toast.LENGTH_SHORT).show();
                        Intent main = new Intent(RegisterActivity.this, MainActivity.class);
                        startActivity(main);
                        finish();
                    }else{
                        String errorCode = ((FirebaseAuthException) task.getException()).getErrorCode();
                        toastError(errorCode);
                    }
                }
            });


        }
    }

    public boolean validate(){
        Boolean [] result = {validateEmail(), validatePassword()};
        for(int i = 0; i< result.length;i++){
            if(!result[i]){
                return false;
            }
        }
        return true;
    };

    public boolean validateEmail(){
        String em = email.getEditText().getText().toString().trim();
        if(em.isEmpty()){
            email.setError("El campo no puede quedar vac??o");
            return false;
        }else if(!PatternsCompat.EMAIL_ADDRESS.matcher(em).matches()){
            email.setError("Ingrese un email valido.");
            return false;
        }else{
            email.setError(null);
            return true;
        }
    };

    public boolean validatePassword(){

        /*
        *           ^                                 # start of line
                    (?=.*[0-9])                       # digit [0-9]
                    (?=.*[a-z])                       # one lowercase character [a-z]
                    (?=.*[A-Z])                       # one uppercase character [A-Z]
                    (?=.*[!@#&()???[{}]:;',?/*~$^+=<>]) # one of the special character in this [..]
                    .                                 # matches anything
                    {8,20}                            # length at least 8 characters and maximum of 20 characters
                    $                                 # end of line
        *
        */

        String pass = password.getEditText().getText().toString().trim();
        Pattern passwordRegex = Pattern.compile(
                "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#&()???[{}]:;',?/*~$^+=<>]).{8,20}$"
        );

        if(pass.isEmpty()){
            password.setError("Este campo no puede quedar vac??o ");
            return false;
        }else if(!passwordRegex.matcher(pass).matches()){
            password.setError("Esta contrase??a es muy debil.");
            return false;
        } else{
            password.setError(null);
            return true;
        }
    };

    private void toastError(String error) {

        switch (error) {

            case "ERROR_INVALID_CUSTOM_TOKEN":
                Toast.makeText(RegisterActivity.this, "El formato del token personalizado es incorrecto. Por favor revise la documentaci??n", Toast.LENGTH_LONG).show();
                break;

            case "ERROR_CUSTOM_TOKEN_MISMATCH":
                Toast.makeText(RegisterActivity.this, "El token personalizado corresponde a una audiencia diferente.", Toast.LENGTH_LONG).show();
                break;

            case "ERROR_INVALID_CREDENTIAL":
                Toast.makeText(RegisterActivity.this, "La credencial de autenticaci??n proporcionada tiene un formato incorrecto o ha caducado.", Toast.LENGTH_LONG).show();
                break;

            case "ERROR_INVALID_EMAIL":
                Toast.makeText(RegisterActivity.this, "La direcci??n de correo electr??nico est?? mal formateada.", Toast.LENGTH_LONG).show();
                email.setError("La direcci??n de correo electr??nico est?? mal formateada.");
                email.requestFocus();
                break;

            case "ERROR_WRONG_PASSWORD":
                Toast.makeText(RegisterActivity.this, "La contrase??a no es v??lida o el usuario no tiene contrase??a.", Toast.LENGTH_LONG).show();
                password.setError("la contrase??a es incorrecta ");
                password.requestFocus();
                break;

            case "ERROR_USER_MISMATCH":
                Toast.makeText(RegisterActivity.this, "Las credenciales proporcionadas no corresponden al usuario que inici?? sesi??n anteriormente..", Toast.LENGTH_LONG).show();
                break;

            case "ERROR_REQUIRES_RECENT_LOGIN":
                Toast.makeText(RegisterActivity.this, "Esta operaci??n es sensible y requiere autenticaci??n reciente. Inicie sesi??n nuevamente antes de volver a intentar esta solicitud.", Toast.LENGTH_LONG).show();
                break;

            case "ERROR_ACCOUNT_EXISTS_WITH_DIFFERENT_CREDENTIAL":
                Toast.makeText(RegisterActivity.this, "Ya existe una cuenta con la misma direcci??n de correo electr??nico pero diferentes credenciales de inicio de sesi??n. Inicie sesi??n con un proveedor asociado a esta direcci??n de correo electr??nico.", Toast.LENGTH_LONG).show();
                break;

            case "ERROR_EMAIL_ALREADY_IN_USE":
                Toast.makeText(RegisterActivity.this, "La direcci??n de correo electr??nico ya est?? siendo utilizada por otra cuenta..   ", Toast.LENGTH_LONG).show();
                email.setError("La direcci??n de correo electr??nico ya est?? siendo utilizada por otra cuenta.");
                email.requestFocus();
                break;

            case "ERROR_CREDENTIAL_ALREADY_IN_USE":
                Toast.makeText(RegisterActivity.this, "Esta credencial ya est?? asociada con una cuenta de usuario diferente.", Toast.LENGTH_LONG).show();
                break;

            case "ERROR_USER_DISABLED":
                Toast.makeText(RegisterActivity.this, "La cuenta de usuario ha sido inhabilitada por un administrador..", Toast.LENGTH_LONG).show();
                break;

            case "ERROR_USER_TOKEN_EXPIRED":
                Toast.makeText(RegisterActivity.this, "La credencial del usuario ya no es v??lida. El usuario debe iniciar sesi??n nuevamente.", Toast.LENGTH_LONG).show();
                break;

            case "ERROR_USER_NOT_FOUND":
                Toast.makeText(RegisterActivity.this, "No hay ning??n registro de usuario que corresponda a este identificador. Es posible que se haya eliminado al usuario.", Toast.LENGTH_LONG).show();
                break;

            case "ERROR_INVALID_USER_TOKEN":
                Toast.makeText(RegisterActivity.this, "La credencial del usuario ya no es v??lida. El usuario debe iniciar sesi??n nuevamente.", Toast.LENGTH_LONG).show();
                break;

            case "ERROR_OPERATION_NOT_ALLOWED":
                Toast.makeText(RegisterActivity.this, "Esta operaci??n no est?? permitida. Debes habilitar este servicio en la consola.", Toast.LENGTH_LONG).show();
                break;

            case "ERROR_WEAK_PASSWORD":
                Toast.makeText(RegisterActivity.this, "La contrase??a proporcionada no es v??lida..", Toast.LENGTH_LONG).show();
                password.setError("La contrase??a no es v??lida, debe tener al menos 6 caracteres");
                password.requestFocus();
                break;

        }
    };
}