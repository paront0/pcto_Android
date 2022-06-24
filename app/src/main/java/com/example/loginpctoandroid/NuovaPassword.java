package com.example.loginpctoandroid;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.chip.Chip;

import java.util.HashMap;
import java.util.Map;

public class NuovaPassword extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nuova_password);
        EditText txtPassword = findViewById(R.id.txtNewPassword);
        EditText txtSecondaPassword = findViewById(R.id.txtNewPassword2);

        String reset_token = getIntent().getStringExtra("reset_token");
        String url_sito = "https://api-staging.ediliziapp.it/users/password/reset/change";
        Button btnBack = findViewById(R.id.btnBackChangePassword);
        Button btnInvia = findViewById(R.id.btnChangePassword);

        Chip CShowPassword1 = findViewById(R.id.chipShowPassword1);
        Chip CShowPassword2 = findViewById(R.id.chipShowPassword2);
        //imposto la funzionalità delle due chip
        CShowPassword1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(CShowPassword1.isChecked())
                {
                    txtPassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                }
                else
                    txtPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            }
        });

        CShowPassword2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(CShowPassword2.isChecked())
                {
                    txtSecondaPassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                }
                else
                    txtSecondaPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            }
        });
        //imposto il bottone per tornare indietro
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(NuovaPassword.this, MainActivity.class);
                startActivity(i);
            }
        });
        //imposto il bottone per inviare i dati,
        btnInvia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //controllo che i testi non siano nulli o con solo spazi, successivamente controllo che le due password siano uguali per inviarle
                if(!txtPassword.getText().toString().contains(" ") && !txtSecondaPassword.getText().toString().contains(" ") &&!txtPassword.getText().toString().trim().equals("") && !txtSecondaPassword.getText().toString().trim().equals(""))
                    if(txtPassword.getText().toString().trim().equals(txtSecondaPassword.getText().toString().trim()))
                    {
                        //faccio la richiesta POST per cambiare la password del singolo utente e ne gestisco le risposte
                        final RequestQueue MyRequestQueue = Volley.newRequestQueue(getApplicationContext());
                        StringRequest richiesta = new StringRequest(Request.Method.POST, url_sito, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Toast.makeText(NuovaPassword.this, "Password modificata con successo. ", Toast.LENGTH_LONG).show();
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(NuovaPassword.this, "Errore nella modifica della password. ", Toast.LENGTH_LONG).show();
                            }
                        }) {
                            protected Map<String, String> getParams() throws AuthFailureError
                            {
                                Map<String, String> MyData = new HashMap<>();
                                MyData.put("new_password",txtPassword.getText().toString());
                                MyData.put("reset_token", reset_token);
                                return MyData;
                            }
                        };
                        MyRequestQueue.add(richiesta);

                    }else{
                        Toast.makeText(NuovaPassword.this, "Le due password non corrispondono", Toast.LENGTH_SHORT).show();
                    }
                else
                    {
                        Toast.makeText(NuovaPassword.this, "La password non deve contenere spazi", Toast.LENGTH_SHORT).show();
                    }
            }
        });
    }
}