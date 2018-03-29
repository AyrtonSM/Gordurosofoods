package com.example.gilberto.gordurosofoods.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.gilberto.gordurosofoods.ProfileActivity;
import com.example.gilberto.gordurosofoods.R;
import com.example.gilberto.gordurosofoods.model.ItemPedido;
import com.example.gilberto.gordurosofoods.model.Pedido;
import com.example.gilberto.gordurosofoods.model.Produto;
import com.example.gilberto.gordurosofoods.utils.CartUtils;

import java.util.List;

/**
 * Created by Gilberto on 19/03/2018.
 */

public class ProfileProductsAdapter extends RecyclerView.Adapter<ProfileProductsAdapter.ViewHolder>{

    private List<ItemPedido> produtos;
    private RecyclerView recyclerView;
    private Activity activity;

    public static class ViewHolder extends RecyclerView.ViewHolder{

        private CardView card;
        private ImageView imageProduct;
        private TextView nomeProduct;
        private EditText quantidade;
        private TextView preco;
        private ImageButton lessButton;
        private ImageButton moreButton;
        private ImageButton removeButton;

        public ViewHolder(View itemView) {
            super(itemView);

            card = itemView.findViewById(R.id.productCardView);
            imageProduct = itemView.findViewById(R.id.finalImageProduct);
            nomeProduct = itemView.findViewById(R.id.productName);
            quantidade = itemView.findViewById(R.id.quantidadePedidas);
            preco = itemView.findViewById(R.id.precoSomado);
            lessButton = itemView.findViewById(R.id.lessButton2);
            moreButton = itemView.findViewById(R.id.moreButton2);
            removeButton = itemView.findViewById(R.id.removeButton);
        }
    }

    public ProfileProductsAdapter(List<ItemPedido> produtos,RecyclerView recyclerView,Activity activity){
        this.produtos = produtos;
        this.recyclerView = recyclerView;
        this.activity = activity;
    }



    @Override
    public ProfileProductsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.orders_layout,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ProfileProductsAdapter.ViewHolder holder, final int position) {

        if(CartUtils.getCarrinho() != null) {


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
            holder.quantidade.setText("" + produtos.get(position).getQuantidade());
            holder.quantidade.setEnabled(false);

            holder.moreButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int quantity = Integer.parseInt(String.valueOf(holder.quantidade.getText()));

                    holder.quantidade.setText(String.valueOf(++quantity));
                    holder.preco.setText("" + quantity * produtos.get(position).getProduto().getPreco());

                    quantity = Integer.parseInt(String.valueOf(holder.quantidade.getText()));

                    double preco = quantity * produtos.get(position).getProduto().getPreco();
                    holder.preco.setText("" + preco);

                    CartUtils.getCarrinho().getPedido().getItens().get(position).setQuantidade(quantity);
                    CartUtils.getCarrinho().getPedido().getItens().get(position).setTotal(preco);

                    double total = 0;
                    for (ItemPedido itemPedido : CartUtils.getCarrinho().getPedido().getItens()) {
                        total += itemPedido.getTotal();
                    }

                    CartUtils.getCarrinho().getPedido().setPrecoTotal(total);

                    CartUtils.getCarrinho().setTotal(total);

                    ProfileActivity.total.setText("Total a Pagar : R$" + CartUtils.getCarrinho().getTotal());
                }
            });

            holder.lessButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int quantity = Integer.parseInt(String.valueOf(holder.quantidade.getText()));
                    if (quantity == 0) {
                        holder.quantidade.setText(String.valueOf(0));

                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setMessage("Deseja realmente apagar esse produto da sua sacola?");
                        builder.setPositiveButton(R.string.lable_ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                double quantity = Double.parseDouble(holder.quantidade.getText().toString());


                                CartUtils.getCarrinho().getPedido().setPrecoTotal(CartUtils.getCarrinho().getPedido().getPrecoTotal() - quantity * produtos.get(position).getProduto().getPreco());
                                CartUtils.getCarrinho().setTotal(CartUtils.getCarrinho().getPedido().getPrecoTotal());
                                produtos.remove(position);

                                ProfileActivity.total.setText("Total a Pagar : R$" + CartUtils.getCarrinho().getTotal());

                                activity.finish();
                                Intent intent = new Intent(context, ProfileActivity.class);
                                activity.startActivity(intent);
                            }
                        });

                        builder.show();

                    } else if (quantity >= 0) {

                        holder.quantidade.setText(String.valueOf(--quantity));
                        quantity = Integer.parseInt(String.valueOf(holder.quantidade.getText()));

                        double preco = quantity * produtos.get(position).getProduto().getPreco();
                        holder.preco.setText("" + preco);

                        CartUtils.getCarrinho().getPedido().getItens().get(position).setQuantidade(quantity);
                        CartUtils.getCarrinho().getPedido().getItens().get(position).setTotal(preco);

                        double total = 0;
                        for (ItemPedido itemPedido : CartUtils.getCarrinho().getPedido().getItens()) {
                            total += itemPedido.getTotal();
                        }

                        CartUtils.getCarrinho().getPedido().setPrecoTotal(total);

                        CartUtils.getCarrinho().setTotal(total);
                        ProfileActivity.total.setText("Total a Pagar : R$" + CartUtils.getCarrinho().getTotal());


                    }


                }
            });

            holder.removeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setMessage("Deseja realmente apagar esse produto da sua sacola?");
                    builder.setPositiveButton(R.string.lable_ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            double quantity = Double.parseDouble(holder.quantidade.getText().toString());


                            CartUtils.getCarrinho().getPedido().setPrecoTotal(CartUtils.getCarrinho().getPedido().getPrecoTotal() - quantity * produtos.get(position).getProduto().getPreco());
                            CartUtils.getCarrinho().setTotal(CartUtils.getCarrinho().getPedido().getPrecoTotal());
                            produtos.remove(position);

                            ProfileActivity.total.setText("Total a Pagar : R$" + CartUtils.getCarrinho().getTotal());

                            activity.finish();
                            Intent intent = new Intent(context, ProfileActivity.class);
                            activity.startActivity(intent);
                        }
                    });

                    builder.show();


                }
            });


        }
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
