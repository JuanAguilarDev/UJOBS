package com.example.ujobs;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class HomeTutorActivity extends AppCompatActivity {

    TextView userName;
    Button btnOut, btnPro, btnMath, btnWhere, btnConver;
    Button btnCreate;
    DatabaseReference databaseReference;
    String userNameText;
    String userIdText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_home_tutor);

        btnOut = findViewById(R.id.btnOut);
        btnCreate = findViewById(R.id.btnCreate);
        userName = findViewById(R.id.userName);
        btnMath = findViewById(R.id.btnMath);
        btnPro = findViewById(R.id.btnPro);
        btnWhere = findViewById(R.id.btnWhere);
        btnConver = findViewById(R.id.btnConversations);

        // Firebase
        databaseReference = FirebaseDatabase.getInstance().getReference();
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();

        databaseReference.child("User").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // Get data
                if(snapshot.exists()){
                    userNameText = snapshot.child(user.getUid()).child("name").getValue().toString();
                    userIdText = user.getUid().trim();
                    userName.setText(userNameText);
                }else{
                    Toast.makeText(HomeTutorActivity.this, "Parece que estamos teniedo problemas", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        btnOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logOut();
            }
        });

        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createMentoring();
            }
        });

        btnPro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(HomeTutorActivity.this, "Por el momento no se puede realizar esta acción", Toast.LENGTH_SHORT).show();
            }
        });

        btnMath.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(HomeTutorActivity.this, "Por el momento no se puede realizar esta acción", Toast.LENGTH_SHORT).show();
            }
        });

        btnWhere.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goWhere();
            }
        });
        
        btnConver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(HomeTutorActivity.this, "Por el momento esta opcion no esta disponible.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void logOut(){
        FirebaseAuth.getInstance().signOut();
        Toast.makeText(this, "Sesión cerrada con exito.", Toast.LENGTH_SHORT).show();
        goStart();
    }

    public void goStart(){
        Intent start = new Intent(this, MainActivity.class);
        start.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(start);
        finish();
    }

    public void createMentoring(){
        Intent create = new Intent(this, RegisterMentoringActivity.class);
        create.putExtra("name", userNameText);
        create.putExtra("id", userIdText);
        startActivity(create);
    }

    public void goWhere(){
        Intent where = new Intent(this, WhereActivity.class);
        startActivity(where);
    }
}