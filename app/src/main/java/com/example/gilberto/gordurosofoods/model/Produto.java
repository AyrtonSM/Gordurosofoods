package com.example.gilberto.gordurosofoods.model;

import android.widget.ImageButton;
import android.widget.ImageView;

/**
 * Created by Gilberto on 11/03/2018.
 */

public class Produto {

    private int id;
    private ImageView imageView;
    private String nome;
    private double preco;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ImageView getImageView() {
        return imageView;
    }

    public void setImageView(ImageView imageView) {
        this.imageView = imageView;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public double getPreco() {
        return preco;
    }

    public void setPreco(double preco) {
        this.preco = preco;
    }
}
