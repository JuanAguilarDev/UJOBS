package com.example.ujobs;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Person;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ujobs.model.Course;
import com.example.ujobs.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    Button btnOut, btnPro, btnMath, btnAbout, btnWhere, btnAll;
    TextView tvName;
    DatabaseReference databaseReference;

    private List<User> users = new ArrayList<User>();
    ArrayAdapter<User> userArrayAdapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_home);

        btnOut = findViewById(R.id.btnOut);
        btnPro = findViewById(R.id.btnPro);
        btnMath = findViewById(R.id.btnMath);
        btnAbout = findViewById(R.id.btnAbout);
        btnWhere = findViewById(R.id.btnWhere);
        btnAll = findViewById(R.id.btnAll);

        databaseReference = FirebaseDatabase.getInstance().getReference();
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();

        tvName = findViewById(R.id.userName);
        String id = user.getUid();

        createCourses();


        databaseReference.child("User").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // Get data
                if(snapshot.exists()){
                    String name = snapshot.child(id).child("name").getValue().toString();
                    tvName.setText(name);
                }else{
                    Toast.makeText(HomeActivity.this, "Parece que estamos teniedo problemas", Toast.LENGTH_SHORT).show();
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

        btnWhere.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goWhere();
            }
        });

        btnAbout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goAbout();
            }
        });

        btnAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(HomeActivity.this, "Por el momento esta opcion no esta disponible", Toast.LENGTH_SHORT).show();
            }
        });

        btnMath.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent pay = new Intent(HomeActivity.this, PaypalActivity.class);
                startActivity(pay);
            }
        });
    }

    public void createCourses(){
        databaseReference.child("Mentoring").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                users.clear();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

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

    public void goWhere(){
        Intent where = new Intent(this, WhereActivity.class);
        startActivity(where);
    }

    public void goAbout(){
        Intent about = new Intent(this, AboutUsActivity.class);
        startActivity(about);
    }
}
