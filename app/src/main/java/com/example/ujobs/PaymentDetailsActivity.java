package com.example.ujobs;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

public class PaymentDetailsActivity extends AppCompatActivity {

    TextView txtId, txtEstatus, txtAmount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_details);

        txtId = findViewById(R.id.txtId);
        txtAmount = findViewById(R.id.txtAmount);
        txtEstatus = findViewById(R.id.txtEstatus);

        // Data recive
        Intent intent = getIntent();
        try {
            JSONObject jsonObject = new JSONObject(intent.getStringExtra("PaymentDetails"));
            seeDetails(jsonObject.getJSONObject("response"), intent.getStringExtra("PaymentAmount"));
        }catch (JSONException e){
            e.printStackTrace();
        }
    }

    private void seeDetails(JSONObject response, String amount) {
        try {
            txtId.setText(response.getString("id"));
            txtEstatus.setText(response.getString("state"));
            txtAmount.setText("$ " + amount);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}