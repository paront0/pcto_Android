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

public class passwordDimenticata extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_dimenticata);
        EditText mailDiRecupero = findViewById(R.id.txtMailRecupero);
        Button btnInviaMail = findViewById(R.id.btnInviaMail);
        Button btnBack = findViewById(R.id.btnIndietroPasswordDimenticata);
        btnInviaMail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final RequestQueue MyRequestQueue = Volley.newRequestQueue(getApplicationContext());
                //mando una richiesta al database che successivamente verrà processata e verrà inviata una mail all'indirizzo che è stato inserito
                if (!mailDiRecupero.getText().toString().trim().equals("")) {
                    StringRequest richiesta = new StringRequest(Request.Method.POST, "https://api-staging.ediliziapp.it/users/password/reset/", new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            //mostro il messaggio di successo e passo all'inserimento della nuova password
                            Toast.makeText(passwordDimenticata.this, "Codice inoltrato con successo alla Mail", Toast.LENGTH_LONG).show();
                            Intent i = new Intent(passwordDimenticata.this, ReimpostaPassword.class);
                            startActivity(i);
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            //mostro un messaggio nel caso in cui la mail inserita sia insesistente
                            Toast.makeText(passwordDimenticata.this, "Errore, mail inesistente", Toast.LENGTH_LONG).show();
                        }

                    }) {
                        public Map<String, String> getParams() throws AuthFailureError {
                            Map<String, String> MyData = new HashMap<>();
                            MyData.put("email", mailDiRecupero.getText().toString());
                            return MyData;
                        }

                    };
                    MyRequestQueue.add(richiesta);
                }

            }

        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
}