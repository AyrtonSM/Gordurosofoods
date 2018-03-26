package com.example.gilberto.gordurosofoods.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Parcelable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.gilberto.gordurosofoods.DetailActivity;
import com.example.gilberto.gordurosofoods.R;
import com.example.gilberto.gordurosofoods.model.Produto;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Serializable;
import java.util.List;

/**
 * Created by Gilberto on 11/03/2018.
 */

public class ProdutoAdapter extends RecyclerView.Adapter<ProdutoAdapter.ViewHolder>{

    private List<Produto> produtos;


    public static class ViewHolder extends RecyclerView.ViewHolder{

        private CardView cardView;
        private TextView nomeProduto;
        private TextView precoProduto;
        private ImageView imageProduto;
        private ImageButton detailsProduto;
        private int id;
        public ViewHolder(View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.cardView);
            nomeProduto = itemView.findViewById(R.id.nomeProduto);
            precoProduto = itemView.findViewById(R.id.precoProduto);
            imageProduto = itemView.findViewById(R.id.imagem);
            detailsProduto = itemView.findViewById(R.id.detailsButton);
        }
    }

    public ProdutoAdapter(List<Produto> produtos){
        this.produtos = produtos;
    }


    @Override
    public ProdutoAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.produto,parent,false);
        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(final ProdutoAdapter.ViewHolder holder, final int position) {
        Produto produto = produtos.get(position);

        final Context context = holder.cardView.getContext();



        holder.nomeProduto.setText(produto.getNome());
        holder.precoProduto.setText("R$ "+String.valueOf(produto.getPreco()));
        holder.imageProduto = produto.getImageView();
        holder.detailsProduto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, DetailActivity.class);
                /**
                 * O código abaixo converte um drawable em uma imagem bitmap e consequentemente em um array
                 * de bytes para ser enviado entre as activities para que do outro lado pegue a getIntent().getByteExtra()
                 */
                Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(),R.drawable.enroladinho_de_salsicha);
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

                bitmap.compress(Bitmap.CompressFormat.PNG,100,byteArrayOutputStream);
                byte[] bytes = byteArrayOutputStream.toByteArray();

                intent.putExtra("imagem",bytes);
                intent.putExtra("nome",holder.nomeProduto.getText());
                intent.putExtra("preco",holder.precoProduto.getText());
                holder.id = produtos.get(position).getId();
                intent.putExtra("id",holder.id);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return produtos.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }
}
