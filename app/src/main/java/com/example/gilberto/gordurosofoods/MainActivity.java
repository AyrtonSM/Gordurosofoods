package com.example.gilberto.gordurosofoods;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.gilberto.gordurosofoods.utils.UrlUtils;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final EditText user = findViewById(R.id.editText);
        final EditText passs = findViewById(R.id.editText2);

        CardView cardView = findViewById(R.id.cardViewButton);
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    login(user,passs);

                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }
            }
        });


    }

    private void login(final EditText user,final EditText pass) throws URISyntaxException {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        URI uri = UrlUtils.getWebSiteURL().toURI();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, uri.toString(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response){

                if(!response.equals(null)){

                    Toast.makeText(getApplicationContext(),response,Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(),NavigationActivity.class);
                    intent.putExtra("user","Ayrton");
                    startActivity(intent);

                }else{
                    Toast.makeText(getApplicationContext(),"Usuario ou senha incorretos",Toast.LENGTH_LONG).show();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(),error.toString(),Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> params = new HashMap<>();
                params.put("USER",user.getText().toString());
                params.put("PASS",pass.getText().toString());
                return params;
            }
        };

        requestQueue.add(stringRequest);
        requestQueue.start();

    }




}
