package com.example.gilberto.gordurosofoods.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.gilberto.gordurosofoods.R;
import com.example.gilberto.gordurosofoods.model.ItemPedido;
import com.example.gilberto.gordurosofoods.model.Produto;

import java.util.List;

/**
 * Created by Gilberto on 19/03/2018.
 */

public class ProfileProductsAdapter extends RecyclerView.Adapter<ProfileProductsAdapter.ViewHolder>{

    private List<ItemPedido> produtos;
    private RecyclerView recyclerView;

    public static class ViewHolder extends RecyclerView.ViewHolder{

        private CardView card;
        private ImageView imageProduct;
        private TextView nomeProduct;
        private TextView quantidade;
        private TextView preco;

        public ViewHolder(View itemView) {
            super(itemView);

            card = itemView.findViewById(R.id.productCardView);
            imageProduct = itemView.findViewById(R.id.finalImageProduct);
            nomeProduct = itemView.findViewById(R.id.productName);
            quantidade = itemView.findViewById(R.id.quantidadePedidas);
            preco = itemView.findViewById(R.id.precoSomado);

        }
    }

    public ProfileProductsAdapter(List<ItemPedido> produtos,RecyclerView recyclerView){
        this.produtos = produtos;
        this.recyclerView = recyclerView;
    }



    @Override
    public ProfileProductsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.orders_layout,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ProfileProductsAdapter.ViewHolder holder, int position) {
        final Context context = holder.card.getContext();

        final CardView cardView = holder.card;

        holder.card.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("Deseja apagar esse produto da sua sacola?");
                builder.setPositiveButton(R.string.lable_ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        recyclerView.removeView(cardView);

                    }
                });

                builder.show();

                return false;
            }
        });


        holder.imageProduct.setImageDrawable(produtos.get(position).getProduto().getImageView().getDrawable());
        holder.preco.setText("R$ " + String.valueOf(produtos.get(position).getProduto().getPreco() * produtos.get(position).getQuantidade()));
        holder.nomeProduct.setText(produtos.get(position).getProduto().getNome());
        holder.quantidade.setText(""+produtos.get(position).getQuantidade());

    }

    @Override
    public int getItemCount() {
        return this.produtos.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }
}
