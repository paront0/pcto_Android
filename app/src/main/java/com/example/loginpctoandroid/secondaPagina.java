package com.example.loginpctoandroid;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class secondaPagina extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seconda_pagina);

        Button btn1 = findViewById(R.id.btnTorna);
        //codice della sencoda pagina da creare
        Button btnCrea = findViewById(R.id.btnAggiungi);
        Button btnArchivio = findViewById(R.id.btnArchivio);
        //imposto il click del pulsante che porta all'activity per la creazione dell'utente
        btnCrea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(secondaPagina.this, CreaUtente.class);
                startActivity(i);
            }
        });
        //imposto il click del pulsante che porta all'archivio degli utenti ai quali è stata applicata una SOFT-delete
        btnArchivio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(secondaPagina.this, paginaArchivio.class);
                startActivity(i);
            }
        });
        //imosto il bottone che riporta alla pagina di login
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(secondaPagina.this, MainActivity.class);
                startActivity(i);
            }
        });
        //carico tutti i vari utenti, richiamando il metodo di carica
        CaricaDati();
    }//metodo che carica tutti gli utenti con la pressione di un pulsante
    public void CaricaDati()
    {
        //creo una richiesta di un array di oggetti JSON con il metodo GET, poichè devo prendere i dati dei vari utenti
        RequestQueue MyRequestQueue = Volley.newRequestQueue(getApplicationContext());
        LinearLayout linearLayout = findViewById(R.id.layouLineare);
        linearLayout.removeAllViews();
        String url_sito = "https://api-staging.ediliziapp.it/users";
        JsonArrayRequest Utente = new JsonArrayRequest(Request.Method.GET, url_sito, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                JSONObject oggetto = null;
                String nome = null;
                //con unf ciclo per la lunghezza dell'array di JSON, carico i vari utenti nella lista scorrevole
                for (int i = 0; i < response.length(); i++) {
                    TextView utente = new TextView(linearLayout.getContext());

                    try {
                        //controllo che il campo per l'eliminazione(delted_at) sia nullo, così so che utenti mostrare
                        oggetto = response.getJSONObject(i);
                        int idUtente = oggetto.getInt("id");
                        if (oggetto.getString("deleted_at").equals("null")) {

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
                            String[] nomiDatiUtente = {"codice fiscale:", "email:", "mobile:", "phone:", "codice postale:", "creazione:", "pec:", "utimo accesso:", "versione:", "professione:", "numero fatture:"};
                            String[] datiUtente = {oggetto.getString("fiscal_code"), oggetto.getString("email"), oggetto.getString("mobile"), oggetto.getString("phone"), oggetto.getString("zip"), oggetto.getString("created_at"), oggetto.getString("pec"), oggetto.getString("last_access_at"), "0.1", oggetto.getString("profession"), oggetto.getString("estimates")};
                            String finalNome = nome;
                            linearLayout.addView(utente);
                            //passo tutti i dati del singolo utente cliccato
                            JSONObject finalOggetto = oggetto;
                            utente.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    //passo i dati all'activity per mostrare le informazioni dei vari utenti
                                    Intent i = new Intent(secondaPagina.this, ActivityUtente.class);
                                    String tok = getIntent().getStringExtra("token");
                                    i.putExtra("nomeUtente", finalNome);
                                    i.putExtra("idSingoloUtente", idUtente);
                                    i.putExtra("nomiDatiUtente", nomiDatiUtente);
                                    i.putExtra("datiUtente", datiUtente);
                                    i.putExtra("token", tok);
                                    Intent h = new Intent(secondaPagina.this, ModificaContatto.class);
                                    h.putExtra("datiUtentePrincipale", datiUtente);
                                    h.putExtra("token", tok);
                                    try {
                                        //ora passo i dati più specifici dei vari utenti
                                        i.putExtra("fiscal_code", finalOggetto.getString("fiscall_code"));
                                        i.putExtra("email", finalOggetto.getString("email"));
                                        i.putExtra("mobile", finalOggetto.getString("mobile"));
                                        i.putExtra("phone", finalOggetto.getString("phone"));
                                        i.putExtra("postal_code", finalOggetto.getString("zip"));
                                        i.putExtra("pec", finalOggetto.getString("pec"));
                                        i.putExtra("profession", finalOggetto.getString("profession"));
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    startActivity(i);
                                    Intent j = new Intent(secondaPagina.this, ActivityUtente.class);
                                    j.putExtra("token", tok);
                                }
                            });
                            //imposto cosa succede se faccio un click prolungato
                            utente.setOnLongClickListener(new View.OnLongClickListener() {
                                @Override
                                public boolean onLongClick(View v) {
                                    //quando applico una pressione prolungata viene fatta una richesta di tipo get sugli abbonamenti dei singoli utenti.
                                    //questa operazione poteva essere fatta in modo automatico con una operazione asincrona, quindi senza rallentare il lavoro della visualizzazione degli utenti
                                    //facendo fare molte richieste al dataBase. Ma così facendo si ha una risposta che in ogni caso è quasi istantanea, poichè con la richiesta asincrona
                                    //si poteva premere su un utente senza che le sue informazioni venissero caricate, riscontrando così un malfunzionamento
                                    String urlAbbonamenti = "https://api-staging.ediliziapp.it/users/" + idUtente + "/subscriptions/history";
                                    //creo l'array di stringhe che andrà a contenere i dati dei vari utenti
                                    String [] dati = new String[7];

                                    final RequestQueue coda = Volley.newRequestQueue(getApplicationContext());
                                    StringRequest richiesta = new StringRequest(Request.Method.GET, urlAbbonamenti, new Response.Listener<String>() {
                                        @Override
                                        public void onResponse(String response) {
                                            try {
                                                //gli abbonamenti si dividono in EXPIRED, NOT_EXPIRED e non esistenti
                                                //EXPIRED=scaduti
                                                //NOT_EXPIRED=non scaduti, quindi validi
                                                JSONObject object = new JSONObject(response);
                                                JSONArray expired = object.getJSONArray("expired");
                                                JSONArray not_expired = object.getJSONArray("not_expired");

                                                //controllo il contenuto dei campi degli array EXPIRED e NOT_EXPIRED, così da filtrare le risposte da dare all'utente
                                                //in base al cliente premuto
                                                if(expired.toString().equals("[]"))
                                                {
                                                    if(not_expired.toString().equals("[]"))
                                                    {
                                                        Toast.makeText(secondaPagina.this, "Questo utente non si è mai registrato a nessun abbonamento", Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                                if(!expired.toString().equals("[]"))
                                                {
                                                    //ora che so che il campo EXPIRED non è vuoto mostro nel popup i dati del campo
                                                    JSONObject Expired = expired.getJSONObject(0);
                                                    JSONObject Expired_subscription = Expired.getJSONObject("subscription");
                                                    dati[0] = "Nome abbonamento: "+Expired_subscription.getString("name") + "\n";
                                                    dati[1] ="Prezzo abbonamento: "+Expired_subscription.getString("price") + "€"+ "\n";
                                                    dati[2] ="Tipo di abbonamento: "+Expired_subscription.getString("type")+ "\n";
                                                    dati[3] ="Descrizione: "+Expired_subscription.getString("description")+ "\n";
                                                    dati[4] ="Giorni alla scadenza: " + Expired.getString("missing_days")+ "\n";
                                                    dati[5] ="Durata abbonamento: " + Expired_subscription.getString("duration") + " giorni"+ "\n";
                                                    dati[6] ="Abbonamento scaduto";
                                                    //AletrDialog è ciò che mi permette di creare la finestra di popup in primo piano
                                                    AlertDialog.Builder popup = new AlertDialog.Builder(secondaPagina.this);
                                                    popup.setTitle(finalNome);
                                                    popup.setMessage(Arrays.toString(dati).substring(1, Arrays.toString(dati).lastIndexOf("]")).replace(",", ""));
                                                    //creo un bottone nella ginestra di popup che mi permette di uscire, se viene lasciato vuoto il metodo del click
                                                    //viene semplicemente indicata la chiusura del popup
                                                    popup.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                                        public void onClick(DialogInterface dialog, int id) {
                                                            // Do something
                                                        }
                                                    });
                                                    //ora mostro il popup
                                                    popup.show();

                                                }
                                                if(!not_expired.toString().equals("[]"))
                                                {
                                                    //faccio le stesse operazioni ma con il campo NOT_EXPIRED
                                                    JSONObject Not_Expired = not_expired.getJSONObject(0);
                                                    JSONObject NOT_Expired_subscription = Not_Expired.getJSONObject("subscription");
                                                    dati[0] ="Nome abbonamento: "+NOT_Expired_subscription.getString("name")+ "\n";
                                                    dati[1] ="Prezzo abbonamento: "+NOT_Expired_subscription.getString("price") + "€"+ "\n";
                                                    dati[2] ="Tipo di abbonamento: "+NOT_Expired_subscription.getString("type")+ "\n";
                                                    dati[3] ="Descrizione: "+NOT_Expired_subscription.getString("description")+ "\n";
                                                    dati[4] ="Giorni alla scadenza: " + Not_Expired.getString("missing_days")+ "\n";
                                                    dati[5] ="Durata abbonamento: " + NOT_Expired_subscription.getString("duration") + " giorni"+ "\n";
                                                    dati[6] ="Abbonamento valido";

                                                    AlertDialog.Builder popup = new AlertDialog.Builder(secondaPagina.this);
                                                    popup.setTitle(finalNome);
                                                    popup.setMessage(Arrays.toString(dati).substring(1, Arrays.toString(dati).lastIndexOf("]")).replace(",", ""));

                                                    popup.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                                        public void onClick(DialogInterface dialog, int id) {
                                                            // Do something
                                                        }
                                                    });
                                                    popup.show();


                                                }
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }

                                        }
                                    }, new Response.ErrorListener() {
                                        @Override
                                        public void onErrorResponse(VolleyError error) {
                                            Toast.makeText(secondaPagina.this, error.toString(), Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                    ){
                                        public Map<String, String> getHeaders() throws AuthFailureError {
                                            String tok = getIntent().getStringExtra("token");
                                            Map<String, String> params = new HashMap<>();
                                            params.put("X-AUTH-TOKEN", tok);
                                            params.put("X-ROLE", "ROLE_ADMIN");
                                            params.put("X-API-KEY", "7c12813d-3eae-4dc3-a91a-4533ac0d1789");
                                            return params;
                                        }
                                    };
                                    coda.add(richiesta);
                                return true;
                                }

                                });
                            }

                        }catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            };
        }, new Response.ErrorListener() {
            @Override
            //imposto cosa deve succedere in caso di errore
            public void onErrorResponse(VolleyError error) { }
        }) {
            //passo il token e altri parametri nell'header
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
