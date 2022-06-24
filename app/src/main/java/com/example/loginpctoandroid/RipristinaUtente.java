package com.example.loginpctoandroid;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RipristinaUtente extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ripristina_utente);

        //creo i riferimenti agli oggetti presenti nell'acctivity
        TextView nome = findViewById(R.id.txtNomeRipristino);
        TextView id = findViewById(R.id.txtIdRipristino);
        Button btnBack = findViewById(R.id.btnIndietroRipristino);
        String url_sito = "https://api-staging.ediliziapp.it/users/";
        LinearLayout LlDatiUtente = findViewById(R.id.LlDatiUtente);
        LinearLayout LlNomiDatiUtente = findViewById(R.id.LlDefinizioneDatiUtente);
        //metto nell'oggetto JSON il campo deleted_at a null, così l'utente potrà essere rimesso nella lista degli utenti normali
        JSONObject deleted_at = new JSONObject();
        try {
            deleted_at.put("deleted_at", "null");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //ottengo un array con i dati e i nomi dei dati che mi servono
        String tok = getIntent().getStringExtra("token");
        String [] datiUtente = getIntent().getStringArrayExtra("datiUtente");
        String [] nomiDatiUtente = getIntent().getStringArrayExtra("nomiDatiUtente");
        //creo l'evento per il bottone INDIETRO
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new  Intent(RipristinaUtente.this, paginaArchivio.class);
                startActivity(i);
            }
        });
        Button btnRipristina = findViewById(R.id.btnRipristinoP);

        //imposto le TextView del nome e dell'ID dell'utente
        nome.setText("Nome utente da ripristinare: " + getIntent().getStringExtra("nomeUtente"));
        id.setText("Id utente: " + getIntent().getIntExtra("idSingoloUtente", 0));
        //imposto il bottone per ripristinare l'utente
        btnRipristina.setOnClickListener(new View.OnClickListener() {
            final RequestQueue MyRequestQueue = Volley.newRequestQueue(getApplicationContext());
            @Override
            public void onClick(View v) {
                //mando una PUT al database per poter impostare il campo come lo voglio io
                StringRequest richiesta = new StringRequest(Request.Method.PUT, url_sito + getIntent().getIntExtra("idSingoloUtente", 0), new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(RipristinaUtente.this, "utente ripristinato con successo", Toast.LENGTH_LONG).show();
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(RipristinaUtente.this, "errore nel ripristino dell'utente", Toast.LENGTH_LONG).show();
                    }
                }){
                    //imposto il campo deleted_at a null, così da ripristinare l'utente, seguendo la sintassi del campo accettata dal database
                    @Override
                    public byte[] getBody() throws AuthFailureError {
                        String str = "{\"deleted_at\":null}";
                        return str.getBytes();
                    }
                    //passo sempre tutti i parametri che mi permettono di accedere ai campi più ristretti dell'utente
                    public String getBodyContentType()
                    {
                        return "application/json; charset=utf-8";
                    }
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        Map<String, String> params = new HashMap<>();
                        params.put("X-AUTH-TOKEN", tok);
                        params.put("X-ROLE", "ROLE_ADMIN");
                        params.put("X-API-KEY", "7c12813d-3eae-4dc3-a91a-4533ac0d1789");
                        return params;

                    }
                };MyRequestQueue.add(richiesta);
            }
        });
        for(int i = 0; i < nomiDatiUtente.length; i++)
        {
            //ora carico tutti i dati corrispondenti al nome che ho caricato precedentemente
            TextView datoUtente = new TextView(LlDatiUtente.getContext());
            if(!datiUtente[i].contains("null") && !datiUtente[i].trim().equals("")) {
                datoUtente.setText(datiUtente[i]);
                LlDatiUtente.addView(datoUtente);
            }
            else
            {
                //imposto che messaggio mandare nel caso in cui il dato sia vuoto o NULL
                datoUtente.setText("DATO NON PRESENTE");
                datoUtente.setTextColor(getResources().getColor(R.color.rossoERRORE));
                LlDatiUtente.addView(datoUtente);
            }
            //imposto le TextView per vedere nome e id dell'utente selezionato
            TextView nomeDatoUtente = new TextView(LlNomiDatiUtente.getContext());
            nomeDatoUtente.setText(nomiDatiUtente[i]);
            LlNomiDatiUtente.addView(nomeDatoUtente);

        }
    }
}