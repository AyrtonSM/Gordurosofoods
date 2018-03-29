package com.example.gilberto.gordurosofoods;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.text.LoginFilter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.gilberto.gordurosofoods.adapter.ProdutoAdapter;
import com.example.gilberto.gordurosofoods.model.ItemPedido;
import com.example.gilberto.gordurosofoods.model.Pedido;
import com.example.gilberto.gordurosofoods.model.Produto;
import com.example.gilberto.gordurosofoods.utils.CartUtils;
import com.example.gilberto.gordurosofoods.utils.UrlUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class NavigationActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter adapter;
    private RecyclerView recyclerView;
    public static List<Produto> produtos = new ArrayList<>();
    public static List<ItemPedido> itemPedidos = new ArrayList<>();
    public static Pedido pedido = new Pedido();

    public ProgressBar progressBar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);



        try {
            getProdutosFromDatabase();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        SearchView searchView = findViewById(R.id.searchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                try {
                    Log.e("tamanho",String.valueOf(s.length()));
                    if(s.isEmpty() || s.length() == 0)
                        getProdutosFromDatabase();
                    else
                        getProdutosByQuery(s);
                    return true;

                } catch (URISyntaxException e) {
                    e.printStackTrace();
                    return false;
                }
            }
        });

        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                try {
                    getProdutosFromDatabase();
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }
                return true;
            }
        });



    }

    private void getProdutosByQuery(final String query) throws URISyntaxException {
        progressBar = findViewById(R.id.progressBar);
        progressBar.setIndeterminate(true);
        progressBar.setVisibility(View.VISIBLE);

            RequestQueue requestQueue = Volley.newRequestQueue(this);
            URI uri = UrlUtils.makeSearch().toURI();

            StringRequest stringRequest = new StringRequest(Request.Method.POST, uri.toString(), new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    System.out.println("resposta curta: "+response);

                    progressBar.setVisibility(View.GONE);
                    produtos = new ArrayList<>();
                    buildProductsData(response);
                    buildRecycler(produtos);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_LONG).show();
            }
        }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    HashMap<String, String> params = new HashMap<>();
                    params.put("QUERY", query);
                    return params;
                }
            };
            requestQueue.add(stringRequest);
            requestQueue.start();
        }


    private void getProdutosFromDatabase() throws URISyntaxException {
        progressBar = findViewById(R.id.progressBar);
        progressBar.setIndeterminate(true);
        if(!produtos.isEmpty()){
            buildRecycler(produtos);
            progressBar.setVisibility(View.GONE);
        }else{

            RequestQueue requestQueue = Volley.newRequestQueue(this);
            URI uri = UrlUtils.getProdutos().toURI();
            StringRequest produtosRequest = new StringRequest(Request.Method.GET, uri.toString(), new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    System.out.println("resposta grande: "+response);
                    produtos = new ArrayList<>();
                    buildProductsData(response);
                    buildRecycler(produtos);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getApplicationContext(),error.toString(),Toast.LENGTH_LONG).show();
                }
            });

            requestQueue.add(produtosRequest);
            requestQueue.start();

        }

    }



    public void buildRecycler(List<Produto> produtos){
        recyclerView = findViewById(R.id.recyclerView);


        layoutManager = new LinearLayoutManager(this);
        recyclerView.setHasFixedSize(true);

        recyclerView.setLayoutManager(layoutManager);

        adapter = new ProdutoAdapter(produtos);
        recyclerView.setAdapter(adapter);
        progressBar.setVisibility(View.GONE);

    }

    private void buildProductsData(String response) {


        JSONArray jsonArray = null;
        try {
            jsonArray = new JSONArray(response);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject product = (JSONObject) jsonArray.get(i);
                Produto produto = new Produto();

                produto.setId(product.getInt("id"));
                produto.setNome(product.getString("nome"));
                produto.setPreco(product.getDouble("preco"));
                produto.setImageView((ImageView) findViewById(R.id.imagem));
                produtos.add(produto);

            }
        } catch (JSONException e) {
            Toast.makeText(getApplicationContext(),"Nenhum produto foi encontrado para sua busca",Toast.LENGTH_SHORT).show();

        }
    }

        @Override
        public void onBackPressed () {
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            if (drawer.isDrawerOpen(GravityCompat.START)) {
                drawer.closeDrawer(GravityCompat.START);
            } else {
                super.onBackPressed();
            }
        }

        @Override
        public boolean onCreateOptionsMenu (Menu menu){
            // Inflate the menu; this adds items to the action bar if it is present.
            getMenuInflater().inflate(R.menu.navigation, menu);
            return true;
        }

        @Override
        public boolean onOptionsItemSelected (MenuItem item){
            // Handle action bar item clicks here. The action bar will
            // automatically handle clicks on the Home/Up button, so long
            // as you specify a parent activity in AndroidManifest.xml.
            int id = item.getItemId();

            //noinspection SimplifiableIfStatement
            if (id == R.id.action_settings) {
                return true;
            }

            return super.onOptionsItemSelected(item);
        }

        @SuppressWarnings("StatementWithEmptyBody")
        @Override
        public boolean onNavigationItemSelected (MenuItem item){
            // Handle navigation view item clicks here.
            int id = item.getItemId();

            if (id == R.id.nav_camera) {
                Intent intent = new Intent(this,ProfileActivity.class);
                    intent.putExtra("user", "Ayrton Sousa");
                    intent.putExtra("email", "Ayrton@hotmail.com.br");

                    startActivity(intent);

            } else if (id == R.id.nav_gallery) {



            } else if (id == R.id.nav_share) {

            }

            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
            return true;
        }
    }
