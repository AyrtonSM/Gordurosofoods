package com.example.gilberto.gordurosofoods;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

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

        RequestQueue requestQueue = Volley.newRequestQueue(this);
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



    }
}
