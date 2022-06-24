package com.example.loginpctoandroid;

import android.annotation.SuppressLint;
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

import java.util.HashMap;
import java.util.Map;

public class ActivityUtente extends AppCompatActivity {
    int volte = 0;
    @SuppressLint("SetTextI18n")
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_utente);
        TextView Nome = findViewById(R.id.TxtNomeUtente);
        TextView id = findViewById(R.id.txtIdUtente);



        LinearLayout linearLayoutNomiDatiUtente = findViewById(R.id.LlDefinizioneDatiUtente);
        LinearLayout linearLayoutDatiUtente = findViewById(R.id.LlDatiUtente);
        Button btnBack = findViewById(R.id.btnIndietro);
        Button btnElimina = findViewById(R.id.btnEliminaContatto);

        //ottengo dalle altre acriviy i dati che mi servono
        Nome.setText("Nome utente: " + getIntent().getStringExtra("nomeUtente"));
        id.setText("ID Utente: " + getIntent().getIntExtra("idSingoloUtente", 0));
        String[] datiUtente = getIntent().getStringArrayExtra("datiUtente");
        String[] nomiDatiUtente = {"codice fiscale:", "email:", "mobile:", "phone:", "codice postale:", "creazione:", "pec:", "utimo accesso:", "versione:", "professione:", "numero fatture:"};
        String token = getIntent().getStringExtra("token");
        //passo alle altre activity i dati che potrebbero servire
        Intent u = new Intent(ActivityUtente.this, RipristinaUtente.class);
        u.putExtra("datiUtente", datiUtente);
        u.putExtra("nomiDatiUtente", nomiDatiUtente);
        u.putExtra("token", token);
        Intent r = new Intent(ActivityUtente.this, ModificaContatto.class);
        r.putExtra("datiUtente", datiUtente);
        r.putExtra("nomiDatiUtente", nomiDatiUtente);
        r.putExtra("token", token);
        String nomeEliminato = Nome.getText().toString();
        //creo il link che contatterò per le informazioni che mi servono
        String urlPOST = "https://api-staging.ediliziapp.it/admin/users/";


        Button btnModifica = findViewById(R.id.btnModificaContatto);
        //creo l'evento per il click del bottone della modifica del contatto
        btnModifica.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //passo all'activity della modifica i dati necessari alla modifica dell'utente
                Intent i = new Intent(ActivityUtente.this, ModificaContatto.class);
                i.putExtra("nomeUtente", getIntent().getStringExtra("nomeUtente"));
                i.putExtra("idUtente", +getIntent().getIntExtra("idSingoloUtente", 0));
                startActivity(i);
            }
        });
        btnElimina.setOnClickListener(new View.OnClickListener() {
            //creo la lista delle richieste che inoltrerò al sito per ottenere i dati che mi servono
            final RequestQueue MyRequestQueue = Volley.newRequestQueue(getApplicationContext());

            @Override
            public void onClick(View v) {
                int volteTot = volte++;
                //creo un INTENT che servirà per mandare quale utente è stato eliminato
                Intent i = new Intent(ActivityUtente.this, secondaPagina.class);
                i.putExtra("nomeEliminato", nomeEliminato);
                StringRequest richiesta = new StringRequest(Request.Method.DELETE, urlPOST + getIntent().getIntExtra("idSingoloUtente", 0), new Response.Listener<String>() {
                    @Override
                    //gestisco la risposta postiva del sito
                    public void onResponse(String response) {
                        //come conseguenza alla POST ottengo e stampo a video l'oggetto JSON che ottengo
                        Toast.makeText(ActivityUtente.this, "Utente eliminato con successo", Toast.LENGTH_LONG).show();
                    }
                }, new Response.ErrorListener() {
                    @Override
                    //gestisco l'errore che il sito potrebbe generare
                    public void onErrorResponse(VolleyError error) {
                        //come conseguenza all'errore nella post stampo sempre l'oggetto JSON che ottengo contenente l'errore
                        Toast.makeText(ActivityUtente.this, "Errore nell'eliminazione", Toast.LENGTH_LONG).show();
                    }
                }) {
                    //passo tutti i parametri che ottengo OBBLIGATORIAMENTE dall'utente al database così che il contatto possa essere salvato

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

        //ora gestisco il click del bottone per tornare indietro, ricaricando l'activity precedente per poter avere le informazioni sempre aggiornate
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Creo il bottone per tornare INDIETRO
                Intent i = new Intent(ActivityUtente.this, secondaPagina.class);
                startActivity(i);
            }
        });
        //carico tutti i nomi dei dati dell'utente
        for (int i = 0; i < nomiDatiUtente.length; i++) {
            //ora carico tutti i dati corrispondenti al nome che ho caricato precedentemente
            TextView datoUtente = new TextView(linearLayoutDatiUtente.getContext());
            datoUtente.isClickable();
            if (!datiUtente[i].contains("null") && !datiUtente[i].trim().equals("")) {

                datoUtente.setText(datiUtente[i]);
                linearLayoutDatiUtente.addView(datoUtente);
            } else {
                //imposto che messaggio mandare nel caso in cui il dato sia vuoto o NULL
                datoUtente.setText("DATO NON PRESENTE");
                datoUtente.setTextColor(getResources().getColor(R.color.rossoERRORE));
                linearLayoutDatiUtente.addView(datoUtente);
            }
            //imposto le TextView per vedere nome e id dell'utente selezionato
            TextView nomeDatoUtente = new TextView(linearLayoutNomiDatiUtente.getContext());
            nomeDatoUtente.setText(nomiDatiUtente[i]);
            linearLayoutNomiDatiUtente.addView(nomeDatoUtente);
            int finalI = i;

            //ora creo l'evento per il click sul singolo dato dell'utente
            datoUtente.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    //restringo i dati cliccabili, rendendo cliccabili solo i dati non registrati
                    if(datoUtente.getText().toString().equals("DATO NON PRESENTE"))
                    {
                        if(finalI == 5 || finalI == 7 || finalI == 8 || finalI == 10)
                        {
                            //nel caso in cui un utente abbia cliccato su un dato non modificabile faccio uscire un messaggio di errore
                            Toast.makeText(ActivityUtente.this, "Dato non modificabile", Toast.LENGTH_SHORT).show();
                        }
                        //quando un untente clicca su un dato non registrto modificabile, cambio il colore del dato nella modifica dall'utente e lo mando ad inserirlo
                        else if(finalI == 3)
                        {
                            Intent I = new Intent(ActivityUtente.this, ModificaContatto.class);
                            I.putExtra("nomeUtente", getIntent().getStringExtra("nomeUtente"));
                            I.putExtra("idUtente", +getIntent().getIntExtra("idSingoloUtente", 0));
                            I.putExtra("indice", finalI);
                            startActivity(I);
                        }
                        else if(finalI == 4)
                        {
                            Intent I = new Intent(ActivityUtente.this, ModificaContatto.class);
                            I.putExtra("nomeUtente", getIntent().getStringExtra("nomeUtente"));
                            I.putExtra("idUtente", +getIntent().getIntExtra("idSingoloUtente", 0));
                            I.putExtra("indice", finalI);
                            startActivity(I);
                        }
                        else if(finalI == 6)
                        {
                            Intent I = new Intent(ActivityUtente.this, ModificaContatto.class);
                            I.putExtra("nomeUtente", getIntent().getStringExtra("nomeUtente"));
                            I.putExtra("idUtente", +getIntent().getIntExtra("idSingoloUtente", 0));
                            I.putExtra("indice", finalI);
                            startActivity(I);
                        }
                        else if(finalI == 9)
                        {
                            Intent I = new Intent(ActivityUtente.this, ModificaContatto.class);
                            I.putExtra("nomeUtente", getIntent().getStringExtra("nomeUtente"));
                            I.putExtra("idUtente", +getIntent().getIntExtra("idSingoloUtente", 0));
                            I.putExtra("indice", finalI);
                            startActivity(I);
                        }
                    }
                }
            });
        }
    }

}