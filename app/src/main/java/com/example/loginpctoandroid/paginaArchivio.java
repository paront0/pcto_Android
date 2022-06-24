package com.example.loginpctoandroid;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class paginaArchivio extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pagina_archivio);
        CaricaDati();
        Button btnBack2 = findViewById(R.id.btnBack2);
        //imposto subito l'evento al click sul bottone indietro
        btnBack2.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(paginaArchivio.this, secondaPagina.class);
                        startActivity(i);
                    }
                }
        );
    }//creo il metodo che andrò poi a richiamare per caricare i dati dell'utente
    public void CaricaDati()
    {
        LinearLayout linearLayout =  findViewById(R.id.LLArchivio);
        RequestQueue MyRequestQueue = Volley.newRequestQueue(getApplicationContext());
        linearLayout.removeAllViews();
        String url_sito = "https://api-staging.ediliziapp.it/users";
        //faccio una richesta per un array di oggetti JSON tramite il metodo GET, quindi prendo una cosa dal database
        JsonArrayRequest Utente = new JsonArrayRequest(Request.Method.GET, url_sito, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                JSONObject oggetto = null;
                String nome = null;
                for (int i = 0; i < response.length(); i++) {
                    TextView utente = new TextView(linearLayout.getContext());
                    try {
                        oggetto = response.getJSONObject(i);
                        int idUtente = oggetto.getInt("id");
                        if(!oggetto.getString("deleted_at").equals("null")) {
                            //mostro solo uno dei due nomi registrati nel database, quello compilato
                            //prima prova del salvataggio del nome degli utenti per poterli distinguere da quelli precedentemente presenti
                            oggetto.getString("company_name");
                            if (!oggetto.getString("company_name").equals("null") && !oggetto.getString("company_name").trim().equals("")) {
                                nome = oggetto.getString("company_name");
                            } else {
                                nome = oggetto.getString("name");
                            }
                            //imposto il layout per il singolo utente che deve essere applicato poi a tutti gli utenti mostrati
                            utente.setText(nome);
                            utente.setPadding(12, 12, 12, 12);
                            utente.setTextSize(20);
                            utente.setElegantTextHeight(true);
                            utente.setClickable(true);
                            String[] nomiDatiUtente = {"codice fiscale:", "email:", "mobile:", "phone:", "codice postale:", "creazione:", "pec:", "utimo accesso:", "versione:", "professione:", "numero fatture:", "deleted at"};
                            String[] datiUtente = {oggetto.getString("fiscal_code"), oggetto.getString("email"), oggetto.getString("mobile"), oggetto.getString("phone"), oggetto.getString("zip"), oggetto.getString("created_at"), oggetto.getString("pec"), oggetto.getString("last_access_at"), "0.1", oggetto.getString("profession"), oggetto.getString("estimates"), oggetto.getString("deleted_at")};
                            String finalNome = nome;

                            //passo tutti i dati del singolo utente cliccato
                            utente.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent i = new Intent(paginaArchivio.this, RipristinaUtente.class);
                                    String tok = getIntent().getStringExtra("token");
                                    i.putExtra("nomeUtente", finalNome);
                                    i.putExtra("idSingoloUtente", idUtente);
                                    i.putExtra("nomiDatiUtente", nomiDatiUtente);
                                    i.putExtra("datiUtente", datiUtente);
                                    i.putExtra("token", tok);
                                    startActivity(i);
                                    Intent j = new Intent(paginaArchivio.this, RipristinaUtente.class);
                                    j.putExtra("token", tok);

                                }
                            });
                            linearLayout.addView(utente);
                        }
                    } catch(JSONException e){
                        e.printStackTrace();
                    }
                }

            }
        }, new Response.ErrorListener() {
            @Override
            //imposto cosa deve succedere in caso di errore
            public void onErrorResponse(VolleyError error) {
            }
        }) {
            //passo il token come parametro nell'header
            public Map<String, String> getHeaders() throws AuthFailureError {
                String tok = getIntent().getStringExtra("token");
                Map<String, String> params = new HashMap<>();
                params.put("X-AUTH-TOKEN", tok);
                params.put("X-ROLE", "ROLE_ADMIN");
                params.put("X-API-KEY", "7c12813d-3eae-4dc3-a91a-4533ac0d1789");
                return params;
            }
        };
        MyRequestQueue.add(Utente);
    }
}
