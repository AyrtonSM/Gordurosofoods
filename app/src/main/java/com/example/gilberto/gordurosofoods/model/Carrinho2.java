package com.example.gilberto.gordurosofoods.model;

/**
 * Created by Gilberto on 19/03/2018.
 */

public class Carrinho2 {

    private Pedido pedido;
    private double total;
    private User usuario;

    public Pedido getPedido() {
        return pedido;
    }

    public void setPedido(Pedido pedido) {
        this.pedido = pedido;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public User getUsuario() {
        return usuario;
    }

    public void setUsuario(User usuario) {
        this.usuario = usuario;
    }
}
