package com.example.gilberto.gordurosofoods.model;

/**
 * Created by Gilberto on 19/03/2018.
 */

public class ItemPedido {

    private Produto produto;
    private int quantidade;
    private double total;

    public Produto getProduto() {
        return produto;
    }

    public void setProduto(Produto produto) {
        this.produto = produto;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }
}
