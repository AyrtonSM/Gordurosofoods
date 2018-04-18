package com.example.gilberto.gordurosofoods.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gilberto.gordurosofoods.R;
import com.example.gilberto.gordurosofoods.model.Pedido;

import org.w3c.dom.Text;

import java.util.List;

/**
 * Created by Gilberto on 29/03/2018.
 */

public class OrdersMadeAdapter extends RecyclerView.Adapter<OrdersMadeAdapter.ViewHolder> {

    public List<Pedido> pedidoList;

    public static class ViewHolder extends RecyclerView.ViewHolder{

        public int id;
        public CardView cardView;
        public TextView codigo;
        public Button detalhes;

        public ViewHolder(View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.pedidos);
            detalhes = itemView.findViewById(R.id.takeALook);
            codigo = itemView.findViewById(R.id.codigo);

        }
    }

    public OrdersMadeAdapter(List<Pedido> pedidoList){
        this.pedidoList = pedidoList;
    }


    @Override
    public OrdersMadeAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_orders,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;

    }

    @Override
    public void onBindViewHolder(OrdersMadeAdapter.ViewHolder holder, int position) {
        final Context context = holder.cardView.getContext();
        holder.codigo.append(String.valueOf(pedidoList.get(position).getId()));
        final int p = position;
        holder.detalhes.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Toast.makeText(context,pedidoList.get(p).getItens().toString(),Toast.LENGTH_SHORT).show();
            }

        });

    }

    @Override
    public int getItemCount() {
        Log.e("erro na construcao",""+this.pedidoList.size());
        return this.pedidoList.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }
}
