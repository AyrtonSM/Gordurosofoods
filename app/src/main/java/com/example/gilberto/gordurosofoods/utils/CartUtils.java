package com.example.gilberto.gordurosofoods.utils;

import com.example.gilberto.gordurosofoods.model.Carrinho;
import com.example.gilberto.gordurosofoods.model.Carrinho2;

/**
 * Created by Gilberto on 17/03/2018.
 */

public class CartUtils {

    private static Carrinho2 carrinho;

    public static Carrinho2 getCarrinho(){
        return carrinho;
    }

    public static void createCart(Carrinho2 cart){
       carrinho = cart;
    }

    public static int getQuantityOfProdutosFromCart(){
        return carrinho != null ? carrinho.getPedido().getItens().size() : 0;
    }


}
