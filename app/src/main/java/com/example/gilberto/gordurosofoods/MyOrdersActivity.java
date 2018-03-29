package com.example.gilberto.gordurosofoods;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.gilberto.gordurosofoods.adapter.OrdersMadeAdapter;
import com.example.gilberto.gordurosofoods.model.ItemPedido;
import com.example.gilberto.gordurosofoods.model.Pedido;
import com.example.gilberto.gordurosofoods.model.Produto;
import com.example.gilberto.gordurosofoods.utils.CartUtils;
import com.example.gilberto.gordurosofoods.utils.UrlUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyOrdersActivity extends AppCompatActivity {

    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView recyclerView;
    private List<Pedido> pedidoList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_orders);



        RequestQueue queue = Volley.newRequestQueue(this);

        try {
            URI uri = UrlUtils.orders().toURI();

            StringRequest request = new StringRequest(Request.Method.GET, uri.toString().concat("?user_id=1"), new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Toast.makeText(getApplicationContext(),response,Toast.LENGTH_SHORT).show();

                    try {
                        List<Pedido> pedidoList = new ArrayList<>();
                        JSONObject jsonObject = new JSONObject(response);
                        Pedido pedido = new Pedido();
                        pedido.setPrecoTotal(jsonObject.getDouble("total"));
                        pedido.setQuantidadeItens(jsonObject.getInt("quantidade"));
                        pedido.setId(jsonObject.getInt("id"));
                        ItemPedido itemPedido;
                        List<ItemPedido> items = new ArrayList<>();
                        String[] produtos = (String[])jsonObject.get("produtos");
                        for(int i = 0 ; i < produtos.length ; i++){
                            itemPedido = new ItemPedido();
                            Produto produto = new Produto();
                            produto.setNome(produtos[i]);
                            itemPedido.setProduto(produto);

                            items.add(itemPedido);

                        }


                        pedido.setItens(items);
                        pedidoList.add(pedido);

                        Toast.makeText(getApplicationContext(),pedidoList.toString(),Toast.LENGTH_SHORT).show();

                        recyclerView = findViewById(R.id.ordersDoneRecycler);
                        mLayoutManager = new LinearLayoutManager(getApplicationContext());
                        recyclerView.setLayoutManager(mLayoutManager);


                        mAdapter = new OrdersMadeAdapter(pedidoList);
                        recyclerView.setAdapter(mAdapter);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getApplicationContext(),error.toString(),Toast.LENGTH_SHORT).show();

                }
            });


            queue.add(request);
            queue.start();

        } catch (URISyntaxException e) {
            e.printStackTrace();
        }


    }


    public void buildRecycler(List<Pedido> pedidos){


    }



}
