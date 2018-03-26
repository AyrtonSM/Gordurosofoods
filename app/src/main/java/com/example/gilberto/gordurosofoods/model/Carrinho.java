package com.example.gilberto.gordurosofoods.model;

import java.util.List;

/**
 * Created by Gilberto on 17/03/2018.
 */

public class Carrinho {

    private List<Produto> produtos;
    private User user;
    private double total;


    public List<Produto> getProdutos() {
        return produtos;
    }

    public void setProdutos(List<Produto> produtos) {
        this.produtos = produtos;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }
}
