package com.example.gilberto.gordurosofoods;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.gilberto.gordurosofoods.utils.CartUtils;
import com.example.gilberto.gordurosofoods.utils.UrlUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;
import java.util.TimerTask;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class StatusActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status);

        final TextView userStatus = findViewById(R.id.userStatus);

        final TextView orderStatus =  findViewById(R.id.orderStatus);

        final RequestQueue requestQueue = Volley.newRequestQueue(this);
        try {
            URI uri = UrlUtils.orders().toURI();

            StringRequest statusRequest =  new StringRequest(Request.Method.POST, uri.toString(), new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        userStatus.append(String.valueOf(jsonObject.get("pedido_id")));
                        if(Integer.parseInt(jsonObject.get("status").toString())==0){
                            orderStatus.append("Ainda não visualizado");
                        }else if(Integer.parseInt(jsonObject.get("status").toString())==1){
                            orderStatus.append("Confirmado, Preparando...");
                        }else if(Integer.parseInt(jsonObject.get("status").toString())==2){
                            orderStatus.append("Pedido Pronto! A caminho da entrega");
                        }else if(Integer.parseInt(jsonObject.get("status").toString())==3){
                            orderStatus.append("Pedido entregue. Muito Obrigado");
                            CartUtils.createCart(null);
                            ProfileActivity.orderMade = false;
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            }){

                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    HashMap<String,String> params = new HashMap<>();
                    params.put("order_id", String.valueOf(CartUtils.getCarrinho().getPedido().getId()));

                    return params;
                }
            };

            requestQueue.add(statusRequest);
            requestQueue.start();





        } catch (URISyntaxException e) {
            e.printStackTrace();
        }


        Button cancel = findViewById(R.id.cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
                try {
                    URI uri = UrlUtils.orders().toURI();
                    StringRequest request = new StringRequest(Request.Method.PUT, uri.toString(), new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            Toast.makeText(getApplicationContext(),response,Toast.LENGTH_SHORT).show();
                            CartUtils.createCart(null);
                            ProfileActivity.orderMade = false;
                            ProfileActivity.finishButton.setVisibility(View.VISIBLE);
                            ProfileActivity.details.setVisibility(View.GONE);
                            finish();


                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                        }
                    }){

                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            HashMap<String,String> params = new HashMap<>();
                            params.put("cancel",String.valueOf(200));
                            params.put("order_id",String.valueOf(CartUtils.getCarrinho().getPedido().getId()));
                            return params;
                        }
                    };


                    requestQueue.add(request);
                    requestQueue.start();

                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }











            }
        });



    }
}
