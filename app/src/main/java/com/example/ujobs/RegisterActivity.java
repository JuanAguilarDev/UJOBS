package com.example.ujobs;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.util.PatternsCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ujobs.model.User;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

import java.util.UUID;
import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {

    Button btnCreate;
    Spinner sp;
    TextInputLayout name;
    TextInputLayout email;
    TextInputLayout password;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Variables association
        sp = findViewById(R.id.sp);
        name = findViewById(R.id.name);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        btnCreate = findViewById(R.id.btnCrear);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.states, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp.setAdapter(adapter);

        //  database
        firebaseStart();

        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createUser();
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
            User user = new User();
            user.setUid(UUID.randomUUID().toString());
            user.setName(name.getEditText().getText().toString().trim());
            user.setEmail(email.getEditText().getText().toString().trim());
            user.setState(sp.getSelectedItem().toString().trim());
            user.setPassword(password.getEditText().getText().toString().trim());
            databaseReference.child("User").child(user.getUid()).setValue(user);
            Toast.makeText(this, "Usuario creado", Toast.LENGTH_SHORT).show();
            Intent main = new Intent(this, MainActivity.class);
            startActivity(main);
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





}