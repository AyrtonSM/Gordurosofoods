package com.example.gilberto.gordurosofoods;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.gilberto.gordurosofoods.adapter.ProdutoAdapter;
import com.example.gilberto.gordurosofoods.adapter.ProfileProductsAdapter;
import com.example.gilberto.gordurosofoods.model.Carrinho2;
import com.example.gilberto.gordurosofoods.model.ItemPedido;
import com.example.gilberto.gordurosofoods.model.Pedido;
import com.example.gilberto.gordurosofoods.model.Produto;
import com.example.gilberto.gordurosofoods.utils.CartUtils;
import com.example.gilberto.gordurosofoods.utils.UrlUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProfileActivity extends AppCompatActivity {
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter adapter;
    private RecyclerView recyclerView;
    private Button finishButton;
    private static boolean orderMade = false;
    private CardView cardView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        cardView = findViewById(   R.id.finishCardView);
        TextView user = findViewById(R.id.userName);
        TextView email = findViewById(R.id.emailUser);

        user.setText(getIntent().getStringExtra("user"));
        email.setText(getIntent().getStringExtra("email"));


        TextView total = findViewById(R.id.totalText);
        total.setText("Total a Pagar : R$" + CartUtils.getCarrinho().getTotal());




        if(CartUtils.getCarrinho() != null)
            buildRecycler(CartUtils.getCarrinho().getPedido().getItens());
            finishButton = findViewById(R.id.finishOrder);

        if(orderMade == true){
            finishButton.setVisibility(View.GONE);
            createStatusButton(cardView);
        }else{

            finishButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        finishOrder();

                    } catch (URISyntaxException e) {
                        e.printStackTrace();
                    }
                }
            });


        }






    }

    public void buildRecycler(List<ItemPedido> produtos){
        recyclerView = findViewById(R.id.orders);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setHasFixedSize(true);

        recyclerView.setLayoutManager(layoutManager);

        adapter = new ProfileProductsAdapter(produtos,recyclerView);
        recyclerView.setAdapter(adapter);

    }

    public JSONObject buildJSON(Carrinho2 carrinho2){

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("pedido_id",carrinho2.getPedido().getId());
            jsonObject.put("user_id",0);

            JSONArray pedidos = new JSONArray();
            JSONObject innerJSON;
            for(ItemPedido itemPedido : carrinho2.getPedido().getItens()){
                innerJSON = new JSONObject();
                innerJSON.put("quantidade",itemPedido.getQuantidade());
                innerJSON.put("id_produto",itemPedido.getProduto().getId());
                pedidos.put(innerJSON);
            }

            jsonObject.put("pedido",pedidos.toString());
            jsonObject.put("total",carrinho2.getTotal());

            return jsonObject;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }



    };


    public void finishOrder() throws URISyntaxException {
        RequestQueue queue = Volley.newRequestQueue(this);
        URI uri = UrlUtils.orders().toURI();


        if(CartUtils.getCarrinho()!=null){

            final JSONObject jsonObject = buildJSON(CartUtils.getCarrinho());

           StringRequest stringRequest = new StringRequest(Request.Method.POST, uri.toString(), new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                        Toast.makeText(getApplicationContext(),response,Toast.LENGTH_LONG).show();
                        recyclerView.setVisibility(View.GONE);
                        finishButton.setVisibility(View.GONE);
                        createStatusButton(cardView);


                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getApplicationContext(),error.toString(),Toast.LENGTH_LONG).show();
                }
            }){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    HashMap<String,String> mapping = new HashMap<>();
                    mapping.put("order",jsonObject.toString());
                    return mapping;
                }
            };

            queue.add(stringRequest);
            queue.start();


        }else{
            Toast.makeText(this,"Não pode finalizar pedido, não existem produtos na sua sacola",Toast.LENGTH_LONG).show();
        }





    }


    public CardView createStatusButton(CardView cardView){
        Button status = new Button(getApplicationContext());
        status.setText("Ver Pedido");

        status.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),StatusActivity.class);
                intent.putExtra("name",getIntent().getStringExtra("name"));
                orderMade = true;
                startActivity(intent);

            }
        });

        cardView.addView(status);
        return cardView;
    }



}
