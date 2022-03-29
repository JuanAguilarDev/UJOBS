package com.example.ujobs;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.util.PatternsCompat;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.regex.Pattern;

public class AuthActivity extends AppCompatActivity {

    Button btnCreate;
    Button btnRecovery;
    TextInputLayout email;
    TextInputLayout password;
    FirebaseAuth firebaseAuth;
    DatabaseReference databaseReference;
    Boolean flag = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference();


        btnRecovery = findViewById(R.id.forgotPassword);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        btnCreate = findViewById(R.id.btnYes);

        if(user != null){
            String id = user.getUid().trim(); 
            databaseReference.child("User").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.exists()){
                        String isTutor = snapshot.child(id).child("isTutor").getValue().toString().trim();
                        if(isTutor == "true"){
                            Intent homeTutor = new Intent(AuthActivity.this, HomeTutorActivity.class);
                            startActivity(homeTutor);
                            finish();
                        }else{
                            Intent homeTutor = new Intent(AuthActivity.this, HomeActivity.class);
                            startActivity(homeTutor);
                            finish();
                        }
                    }else {
                        Toast.makeText(AuthActivity.this, "No se han podido recuperar los datos.", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }


        btnRecovery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                forgetPassword(view);
            }
        });

        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                start(view);
            }
        });
    }

    public void forgetPassword(View view){
        Intent recovery = new Intent(this, ResetPasswordActivity.class);
        startActivity(recovery);
    }

    public void start(View view){
        if(!validate()){
            Toast.makeText(this, "Asegurece de que los datos sean correctos. ", Toast.LENGTH_SHORT).show();
        }else{
            String mail = email.getEditText().getText().toString().trim();
            String pass = password.getEditText().getText().toString().trim();

            firebaseAuth.signInWithEmailAndPassword(mail, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(AuthActivity.this, "Sesion iniciada. ", Toast.LENGTH_SHORT).show();
                        goToHome();
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
    }

    public void goToHome(){

        String id = firebaseAuth.getCurrentUser().getUid().trim();

        databaseReference.child("User").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    String isTutor = snapshot.child(id).child("isTutor").getValue().toString().trim();
                    if(isTutor == "true"){
                        Intent homeTutor = new Intent(AuthActivity.this, HomeTutorActivity.class);
                        startActivity(homeTutor);
                        finish();
                    }else{
                        Intent homeTutor = new Intent(AuthActivity.this, HomeActivity.class);
                        startActivity(homeTutor);
                        finish();
                    }

                }else{
                    Toast.makeText(AuthActivity.this, "Parece que estamos teniedo problemas", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    public boolean validateEmail(){
        String em = email.getEditText().getText().toString().trim();
        if(em.isEmpty()){
            email.setError("El campo no puede quedar vacío");
            return false;
        }else if(!PatternsCompat.EMAIL_ADDRESS.matcher(em).matches()){
            email.setError("Ingrese un email valido.");
            return false;
        }else{
            email.setError(null);
            return true;
        }
    }

    public boolean validatePassword(){

        /*
        *           ^                                 # start of line
                    (?=.*[0-9])                       # digit [0-9]
                    (?=.*[a-z])                       # one lowercase character [a-z]
                    (?=.*[A-Z])                       # one uppercase character [A-Z]
                    (?=.*[!@#&()–[{}]:;',?/*~$^+=<>]) # one of the special character in this [..]
                    .                                 # matches anything
                    {8,20}                            # length at least 8 characters and maximum of 20 characters
                    $                                 # end of line
        *
        */

        String pass = password.getEditText().getText().toString().trim();
        Pattern passwordRegex = Pattern.compile(
                "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#&()–[{}]:;',?/*~$^+=<>]).{8,20}$"
        );

        if(pass.isEmpty()){
            password.setError("Este campo no puede quedar vacío ");
            return false;
        }else if(!passwordRegex.matcher(pass).matches()){
            password.setError("Esta contraseña es muy debil.");
            return false;
        } else{
            password.setError(null);
            return true;
        }
    }

    private void toastError(String error) {

        switch (error) {

            case "ERROR_INVALID_CUSTOM_TOKEN":
                Toast.makeText(AuthActivity.this, "El formato del token personalizado es incorrecto. Por favor revise la documentación", Toast.LENGTH_LONG).show();
                break;

            case "ERROR_CUSTOM_TOKEN_MISMATCH":
                Toast.makeText(AuthActivity.this, "El token personalizado corresponde a una audiencia diferente.", Toast.LENGTH_LONG).show();
                break;

            case "ERROR_INVALID_CREDENTIAL":
                Toast.makeText(AuthActivity.this, "La credencial de autenticación proporcionada tiene un formato incorrecto o ha caducado.", Toast.LENGTH_LONG).show();
                break;

            case "ERROR_INVALID_EMAIL":
                Toast.makeText(AuthActivity.this, "La dirección de correo electrónico está mal formateada.", Toast.LENGTH_LONG).show();
                email.setError("La dirección de correo electrónico está mal formateada.");
                email.requestFocus();
                break;

            case "ERROR_WRONG_PASSWORD":
                Toast.makeText(AuthActivity.this, "La contraseña no es válida o el usuario no tiene contraseña.", Toast.LENGTH_LONG).show();
                password.setError("la contraseña es incorrecta ");
                password.requestFocus();
                break;

            case "ERROR_USER_MISMATCH":
                Toast.makeText(AuthActivity.this, "Las credenciales proporcionadas no corresponden al usuario que inició sesión anteriormente..", Toast.LENGTH_LONG).show();
                break;

            case "ERROR_REQUIRES_RECENT_LOGIN":
                Toast.makeText(AuthActivity.this, "Esta operación es sensible y requiere autenticación reciente. Inicie sesión nuevamente antes de volver a intentar esta solicitud.", Toast.LENGTH_LONG).show();
                break;

            case "ERROR_ACCOUNT_EXISTS_WITH_DIFFERENT_CREDENTIAL":
                Toast.makeText(AuthActivity.this, "Ya existe una cuenta con la misma dirección de correo electrónico pero diferentes credenciales de inicio de sesión. Inicie sesión con un proveedor asociado a esta dirección de correo electrónico.", Toast.LENGTH_LONG).show();
                break;

            case "ERROR_EMAIL_ALREADY_IN_USE":
                Toast.makeText(AuthActivity.this, "La dirección de correo electrónico ya está siendo utilizada por otra cuenta..   ", Toast.LENGTH_LONG).show();
                email.setError("La dirección de correo electrónico ya está siendo utilizada por otra cuenta.");
                email.requestFocus();
                break;

            case "ERROR_CREDENTIAL_ALREADY_IN_USE":
                Toast.makeText(AuthActivity.this, "Esta credencial ya está asociada con una cuenta de usuario diferente.", Toast.LENGTH_LONG).show();
                break;

            case "ERROR_USER_DISABLED":
                Toast.makeText(AuthActivity.this, "La cuenta de usuario ha sido inhabilitada por un administrador..", Toast.LENGTH_LONG).show();
                break;

            case "ERROR_USER_TOKEN_EXPIRED":
                Toast.makeText(AuthActivity.this, "La credencial del usuario ya no es válida. El usuario debe iniciar sesión nuevamente.", Toast.LENGTH_LONG).show();
                break;

            case "ERROR_USER_NOT_FOUND":
                Toast.makeText(AuthActivity.this, "No hay ningún registro de usuario que corresponda a este identificador. Es posible que se haya eliminado al usuario.", Toast.LENGTH_LONG).show();
                break;

            case "ERROR_INVALID_USER_TOKEN":
                Toast.makeText(AuthActivity.this, "La credencial del usuario ya no es válida. El usuario debe iniciar sesión nuevamente.", Toast.LENGTH_LONG).show();
                break;

            case "ERROR_OPERATION_NOT_ALLOWED":
                Toast.makeText(AuthActivity.this, "Esta operación no está permitida. Debes habilitar este servicio en la consola.", Toast.LENGTH_LONG).show();
                break;

            case "ERROR_WEAK_PASSWORD":
                Toast.makeText(AuthActivity.this, "La contraseña proporcionada no es válida..", Toast.LENGTH_LONG).show();
                password.setError("La contraseña no es válida, debe tener al menos 6 caracteres");
                password.requestFocus();
                break;

        }
    }
}