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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ReimpostaPassword extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reimposta_password);
        EditText Codice = findViewById(R.id.txtCodice);
        Button BtnBack = findViewById(R.id.btnBackVerifica);
        Button BtnIvia = findViewById(R.id.btnInviaVerifica);
        String url_sito = "https://api-staging.ediliziapp.it/users/password/reset/verify";
        BtnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        BtnIvia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    //verifica che il codice non sia vuoto o riutilizzato
                    if (!Codice.getText().toString().trim().equals("")) {
                        //faccio una POST per ottenere un token di reset per poter cambiare la password
                        final RequestQueue MyRequestQueue = Volley.newRequestQueue(getApplicationContext());
                            StringRequest richiesta = new StringRequest(Request.Method.POST, url_sito, new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    try {
                                        if(response.contains("ok")) {
                                            //verifico la risposta del token, ok=bene , ko=male
                                            String sempliceJSON = response.substring(1, response.lastIndexOf(']'));
                                            //tolgo le [] per ottenere un oggetto JSON e lavorare con quello
                                            JSONObject oggetto = new JSONObject(sempliceJSON);
                                            //dall'oggetto estraggo il token e lo memorizzo
                                            String reset_token = oggetto.getString("reset_token");
                                            Intent i = new Intent(ReimpostaPassword.this, NuovaPassword.class);
                                            //invio il token alla pagina successiva
                                            i.putExtra("reset_token", reset_token);
                                            startActivity(i);
                                        }
                                        else
                                            //mando il messaggio di errore
                                            Toast.makeText(ReimpostaPassword.this, "token scaduto o già utilizzato", Toast.LENGTH_SHORT).show();
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Toast.makeText(ReimpostaPassword.this, "token scaduto o già utilizzato", Toast.LENGTH_LONG).show();
                                }
                            }) {
                                //mando come parametro il codice di verifica
                                public Map<String, String> getParams() throws AuthFailureError {
                                    Map<String, String> MyData = new HashMap<String, String>();
                                    MyData.put("verification_code", Codice.getText().toString().toLowerCase());
                                    return MyData;
                                }
                            };
                            MyRequestQueue.add(richiesta);
                    }


            }
        });

    }
}