package com.example.loginpctoandroid;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
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
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.chip.Chip;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.Collator;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity
{

    private Collator VolleyApplication;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //come prima cosa creo dei riferimenti agli elementi nell'interfaccia
        TextView passworddimenticata  = findViewById(R.id.txtLink);
        EditText userName = findViewById(R.id.UserName);
        EditText password = findViewById(R.id.pwd);
        Button btnInvia = findViewById(R.id.Invia);
        //creo una variabile contenente l'url della pagina
        String url_sito = "https://api-staging.ediliziapp.it/login/users";
        //imposto la chip per la visualizzazione in modo chiaro della password
        Chip chipShow = findViewById(R.id.chipShow);

        passworddimenticata.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, passwordDimenticata.class);
                startActivity(i);
            }
        });

        //faccio in modo che con il click della CHIP la password venga mostrata a caratteri chiari
        chipShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //cambio il tipo della seconda EditText, facedola assare da passwrod a testo semplice
                if(chipShow.isChecked())
                {
                    password.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                }
                else
                    password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            }
        });
        //creo l'evento del CLICK del bottone
        btnInvia.setOnClickListener(new View.OnClickListener()
        {
            final RequestQueue MyRequestQueue = Volley.newRequestQueue(getApplicationContext());
            @Override
            //imposto cosa deve succedere se clicco il bottone
            public void onClick(View v)
            {
                 StringRequest MyStringRequest = new StringRequest(Request.Method.POST, url_sito, new Response.Listener<String>()
                {
                    @Override
                    //imposto la risposta del click
                    //salvo tutto ciò che mi viane mandato in una stringa, altrimenti da errore e lavoro con le condizioni
                    //agendo sulla singola stringa controllando il suo contenuto
                    public void onResponse(String response) {
                        //imposto le condizioni delle varie risposte, filtrandole
                        JSONObject oggettoJson = null;
                        try {
                            oggettoJson = new JSONObject(response);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if (response.contains("error"))
                            try
                            {
                                String error = oggettoJson.getString("error");
                                Toast.makeText(MainActivity.this, error, Toast.LENGTH_LONG).show();
                            }
                        catch (JSONException e)
                            {
                                e.printStackTrace();
                            }
                        else
                        {
                            Intent i = new Intent(MainActivity.this, secondaPagina.class);
                            String token = null;
                            try {
                                token = oggettoJson.getString("token");

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            Toast.makeText(MainActivity.this, "login eseguito con successo", Toast.LENGTH_LONG).show();
                            i.putExtra("token", token);
                            startActivity(i);
                        }
                    }
                }, new Response.ErrorListener()
                {
                    @Override
                    //imposto cosa deve succedere in caso di errore
                    public void onErrorResponse(VolleyError error)
                    {
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                })
                {
                    //creo la funzione che definisce il tipo di dati da inoltrare
                    protected Map<String, String> getParams() throws AuthFailureError
                    {
                        Map<String, String> MyData = new HashMap<>();
                        MyData.put("email",userName.getText().toString());
                        MyData.put("password", password.getText().toString());
                        return MyData;
                    }
                };
                //aggiungo la richiesta che faccio al server alla lista di richieste, così da avere delle richieste asincrone
                //per non rallentare l'applicazzione e per non bloccarla
                MyRequestQueue.add(MyStringRequest);
            }
        });
    }
}