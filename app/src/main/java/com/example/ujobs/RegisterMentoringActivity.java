package com.example.ujobs;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.ujobs.model.Course;
import com.example.ujobs.model.User;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.Random;
import java.util.UUID;

public class RegisterMentoringActivity extends AppCompatActivity {

    Button btnCreate;
    TextInputLayout name;
    TextInputLayout description;
    Spinner sp;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    Bundle extras;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_mentoring);

        // Asociate variables
        btnCreate = findViewById(R.id.btnCreate);
        name = findViewById(R.id.name);
        description = findViewById(R.id.description);
        sp = findViewById(R.id.sp);

        // Start firebase
        firebaseStart();

        // Spinner topics
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.topics, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp.setAdapter(adapter);

        // Extras
        extras = getIntent().getExtras();


        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createMentoring();
            }
        });
    }

    private void firebaseStart(){
        FirebaseApp.initializeApp(this);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
    }


    public void createMentoring(){
        String idAuthor = extras.getString("id");
        String nameAuthor = extras.getString("name");
        Course topic = new Course();
        topic.setTopic(name.getEditText().getText().toString().trim());
        topic.setAuthor(nameAuthor);
        topic.setImage(sp.getSelectedItem().toString().trim());
        topic.setUid(UUID.randomUUID().toString());
        topic.setAuthorId(idAuthor);
        topic.setDescription(description.getEditText().getText().toString());
        databaseReference.child("Mentoring").child(topic.getUid()).setValue(topic);
        Toast.makeText(RegisterMentoringActivity.this, "Asesoria creada", Toast.LENGTH_SHORT).show();
        Intent HomeT = new Intent(RegisterMentoringActivity.this, HomeTutorActivity.class);
        startActivity(HomeT);
        finish();
    }
}