package com.example.loginpctoandroid;

import android.content.Intent;
import android.os.Bundle;
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

import java.util.HashMap;
import java.util.Map;

public class CreaUtente extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //creo tutti i riferimenti che mi servono
        setContentView(R.layout.activity_crea_utente);
        Button btnAggiungiContatto = findViewById(R.id.btnCreaUtente);
        EditText txtNome = findViewById(R.id.nomeAzienda);
        EditText txtMail = findViewById(R.id.email);
        Button btnIndietro = findViewById(R.id.btnIndietro2);
        EditText pwd = findViewById(R.id.password);
        EditText mobile = findViewById(R.id.mobileNumber);
        EditText pIVA = findViewById(R.id.partitaIvaNumber);
        EditText fiscalCode = findViewById(R.id.codiceFiscale);



        //creo le variabili che conterranno i dati che mi servono
        String token = getIntent().getStringExtra("tok");
        String urlPOST = "https://api-staging.ediliziapp.it/admin/users/";


        //imposto l'evento di click del pulsante INDIETRO
        btnIndietro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(CreaUtente.this, secondaPagina.class);
                startActivity(i);
            }
        });
        //permetto di aggiungere il contatto con l'apposito pulsante
        btnAggiungiContatto.setOnClickListener(new View.OnClickListener() {
            //creo la mia coda di richieste
            final RequestQueue MyRequestQueue = Volley.newRequestQueue(getApplicationContext());

            @Override
            public void onClick(View v) {
                //codice per salvare il contatto
                //inizio inoltrando la richiesta ed il dato che voglio in cambio di questa richiesta
                StringRequest richiesta = new StringRequest(Request.Method.POST, urlPOST, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //come conseguenza alla POST ottengo e stampo a video l'oggetto JSON che ottengo
                        Toast.makeText(CreaUtente.this, "utente creato con successo, parte POST. RISPOSTA: ", Toast.LENGTH_LONG).show();
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    //come conseguenza all'errore nella post stampo sempre l'oggetto JSON che ottengo contenente l'errore
                        Toast.makeText(CreaUtente.this, "errore nella creazione dell'utente, parte POST. ERRORE: "+ "TOKEN", Toast.LENGTH_LONG).show();
                    }
                }) {
                    //passo tutti i parametri che ottengo OBBLIGATORIAMENTE dall'utente al database così che il contatto possa essere salvato
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> MyData = new HashMap<>();
                        MyData.put("email", txtMail.getText().toString());
                        MyData.put("name", txtNome.getText().toString());
                        MyData.put("password", pwd.getText().toString());
                        MyData.put("fiscal_code", fiscalCode.getText().toString());
                        MyData.put("vat", pIVA.getText().toString());
                        MyData.put("mobile", mobile.getText().toString());
                        return MyData;
                    }

                    //passo gli headers per poter accedere all'area di crezione del contatto
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        Map<String, String> params = new HashMap<>();
                        params.put("X-AUTH-TOKEN", token);
                        params.put("X-ROLE", "ROLE_ADMIN");
                        params.put("X-API-KEY", "7c12813d-3eae-4dc3-a91a-4533ac0d1789");
                        return params;
                    }

                };
                //aggiungo la richiesta alla coda di richieste per inviarla
                MyRequestQueue.add(richiesta);
            }

        });
    }


}
