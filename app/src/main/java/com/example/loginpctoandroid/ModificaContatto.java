package com.example.loginpctoandroid;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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

import java.util.HashMap;
import java.util.Map;

public class ModificaContatto extends AppCompatActivity {

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int Indice = getIntent().getIntExtra("indice", 0);
        setContentView(R.layout.activity_modifica_contatto);
        String nome = getIntent().getStringExtra("nomeUtente");
        int id = getIntent().getIntExtra("idUtente", 0);
        TextView txtNome = findViewById(R.id.txtNomeModifica);
        TextView txtId = findViewById(R.id.txtIdModifca);
        Button btnApplica = findViewById(R.id.btnApplica);
        txtNome.setText("Nome utente: " + nome);
        txtId.setText("id utente: " + id);

        //creo tutte le EditText che mi servono
        Button btnBack = findViewById(R.id.btnBackMod);
        EditText FiscalCodeMod = findViewById(R.id.txtCodiceFiscale);
        EditText EMailMod = findViewById(R.id.txtEmail);
        EditText MobileMod = findViewById(R.id.txtMobile);
        EditText ProfessioneMod = findViewById(R.id.txtProfessione);
        EditText PhoneMod = findViewById(R.id.txtPhone);
        EditText PostalMod = findViewById(R.id.txtCodicePostale);
        EditText PECMod = findViewById(R.id.txtPec);
        //prendo il primo url del sito he mi serve
        String url_sito = "https://api-staging.ediliziapp.it/users/";
        String token = getIntent().getStringExtra("token");
        RequestQueue MyRequestQueue = Volley.newRequestQueue(getApplicationContext());
        String url = "https://api-staging.ediliziapp.it/users";
        //creo la prima richiesta GET che mi farà avere un array di oggetti JSON
        JsonArrayRequest Utente = new JsonArrayRequest(Request.Method.GET, url_sito, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                for (int i = 0; i < response.length(); i++) {
                    try {
                        //faccio sì che vengano mostrati solo i campi non NULL
                        JSONObject singolo = response.getJSONObject(i);
                        if (singolo.getString("name").trim().equals(nome)) {
                            FiscalCodeMod.setText(singolo.getString("fiscal_code"));
                            EMailMod.setText(singolo.getString("email"));
                            MobileMod.setText(singolo.getString("mobile"));

                            if(!singolo.getString("phone").equals("null"))
                                PhoneMod.setText(singolo.getString("phone"));

                            if(!singolo.getString("zip").equals("null"))
                                PostalMod.setText(singolo.getString("zip"));

                            if(!singolo.getString("pec").equals("null"))
                                PECMod.setText(singolo.getString("pec"));

                            if(!singolo.getString("profession").equals("null"))
                                ProfessioneMod.setText(singolo.getString("profession"));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            //passo gli header necessari
            public Map<String, String> getHeaders() throws AuthFailureError {
                String tok = getIntent().getStringExtra("token");
                Map<String, String> params = new HashMap<>();
                params.put("X-AUTH-TOKEN", tok);
                params.put("X-ROLE", "ROLE_ADMIN");
                params.put("X-API-KEY", "7c12813d-3eae-4dc3-a91a-4533ac0d1789");
                return params;
            }
        };
        //aggiungo la richiesta alla ccode della richieste
        MyRequestQueue.add(Utente);

            //imposto il click del bottone per tornare indietro
            btnBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i =  new Intent(ModificaContatto.this, secondaPagina.class);
                    startActivity(i);
                }
            });
            //imposto la condizione seconda la quale il campo da compilare viene evidenziato in rosso
                if(Indice != 0)
                {
                    if(Indice == 3)
                    {
                        PhoneMod.setHintTextColor(getResources().getColor(R.color.evidenzio));
                    }
                    else if (Indice == 4)
                    {
                        PostalMod.setHintTextColor(getResources().getColor(R.color.evidenzio));
                    }
                    else if(Indice == 6)
                    {
                        PECMod.setHintTextColor(getResources().getColor(R.color.evidenzio));
                    }
                    else if(Indice == 9)
                    {
                        ProfessioneMod.setHintTextColor(getResources().getColor(R.color.evidenzio));
                    }
                }

            btnApplica.setOnClickListener(new View.OnClickListener() {
                //applico le mie modifica facendo una ulteriore richiesta, PUT in questo caso, poichè vado a modificare campi già compilati

                final RequestQueue MyRequestQueue = Volley.newRequestQueue(getApplicationContext());

                @Override

                public void onClick(View v) {
                    //creo l'ggetto JSON da inoltrare al server con i dati da me compilati
                    JSONObject oggettoTOT = new JSONObject();
                    {
                        try {
                            oggettoTOT.put("pec", PECMod.getText());
                            oggettoTOT.put("profession", ProfessioneMod.getText());
                            oggettoTOT.put("mobile", MobileMod.getText());
                            oggettoTOT.put("email", EMailMod.getText());
                            oggettoTOT.put("fiscal_code", FiscalCodeMod.getText());
                            oggettoTOT.put("phone", PhoneMod.getText());
                            oggettoTOT.put("zip", PostalMod.getText());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }//volgio che almeno i pimi tre campi siano compilato, poichè rappresentano i dati minimi che un utente deve avere
                    if (FiscalCodeMod.getText().toString().equals("") || EMailMod.getText().toString().equals("") || MobileMod.getText().toString().equals("")) {
                        Toast.makeText(ModificaContatto.this, "Compilare tutti i campi obbligatori", Toast.LENGTH_LONG).show();
                    } else {
                        StringRequest richiesta = new StringRequest(Request.Method.PUT, url_sito + id, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Toast.makeText(ModificaContatto.this, "modifiche apportate con successo", Toast.LENGTH_LONG).show();
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(ModificaContatto.this, "Errore nell'apportare le modifiche", Toast.LENGTH_LONG).show();
                            }
                        }) {
                            //imposto il corpo della mia richiesta e lo mando
                            @Override
                            public byte[] getBody() throws AuthFailureError {
                                String str = oggettoTOT.toString();
                                return str.getBytes();
                            }
                            //dichiaro il tipo del contenuto del corpo
                            public String getBodyContentType() {
                                return "application/json; charset=utf-8";
                            }

                            public Map<String, String> getHeaders() throws AuthFailureError {
                                Map<String, String> params = new HashMap<>();
                                params.put("X-AUTH-TOKEN", token);
                                params.put("X-ROLE", "ROLE_ADMIN");
                                params.put("X-API-KEY", "7c12813d-3eae-4dc3-a91a-4533ac0d1789");
                                return params;
                            }
                        };
                        MyRequestQueue.add(richiesta);
                    }
                }
            });
        }
    }

