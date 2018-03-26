package com.example.gilberto.gordurosofoods.model;

import java.util.List;

/**
 * Created by Gilberto on 19/03/2018.
 */

public class Pedido {

    private int id;
    private List<ItemPedido> itens;
    private int quantidadeItens;
    private double precoTotal;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    public List<ItemPedido> getItens() {
        return itens;
    }

    public void setItens(List<ItemPedido> itens) {
        this.itens = itens;
    }

    public int getQuantidadeItens() {
        return quantidadeItens;
    }

    public void setQuantidadeItens(int quantidadeItens) {
        this.quantidadeItens = quantidadeItens;
    }

    public double getPrecoTotal() {
        return precoTotal;
    }

    public void setPrecoTotal(double precoTotal) {
        this.precoTotal = precoTotal;
    }

}
