package com.example.gilberto.gordurosofoods;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gilberto.gordurosofoods.model.Carrinho;
import com.example.gilberto.gordurosofoods.model.Carrinho2;
import com.example.gilberto.gordurosofoods.model.ItemPedido;
import com.example.gilberto.gordurosofoods.model.Pedido;
import com.example.gilberto.gordurosofoods.model.Produto;
import com.example.gilberto.gordurosofoods.model.User;
import com.example.gilberto.gordurosofoods.utils.CartUtils;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

public class DetailActivity extends AppCompatActivity {

    public int quantity = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        final TextView nameDetails = findViewById(R.id.produtoDetail);
        final TextView precoDetails = findViewById(R.id.precoDetail);
        final ImageView imagemProduto = findViewById(R.id.produtoImage);
        TextView textView = findViewById(R.id.cartInfo);
        textView.setText(String.valueOf(CartUtils.getQuantityOfProdutosFromCart()));

        nameDetails.setText(getIntent().getStringExtra("nome"));
        precoDetails.setText(getIntent().getStringExtra("preco"));

        // Pegando a imagem passada pela activity anterior
       /* byte[] bytes = getIntent().getByteArrayExtra("imagem");
        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes,0,bytes.length);
        imagemProduto.setImageBitmap(bitmap);
        */

        ImageButton moreButton = findViewById(R.id.moreButton);
        ImageButton lessButton = findViewById(R.id.lessButton);
        final TextView quantidade = findViewById(R.id.quantidade);

        moreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                quantidade.setText(String.valueOf(++quantity));
            }
        });
        lessButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(quantity <= 0){
                    quantity = 0;
                    quantidade.setText(String.valueOf(quantity));
                }else
                    quantidade.setText(String.valueOf(--quantity));
            }
        });

        Button addToCart = findViewById(R.id.addToCart);
        addToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Produto produto = new Produto();

                produto.setId(getIntent().getIntExtra("id",0));

                produto.setNome(nameDetails.getText().toString());
                produto.setPreco(normalizePrice(precoDetails.getText().toString()));
                //produto.setImageView(imagemProduto);

                if(CartUtils.getCarrinho() == null){
                    if(!quantidade.getText().toString().equals("")) {

                        ItemPedido itemPedido = new ItemPedido();
                        itemPedido.setProduto(produto);
                        itemPedido.setQuantidade(Integer.parseInt(quantidade.getText().toString()));
                        itemPedido.setTotal(itemPedido.getQuantidade() * normalizePrice(precoDetails.getText().toString()));

                        Pedido pedido = new Pedido();
                        Random random = new Random();

                        pedido.setId(Math.abs(random.nextInt()));
                        pedido.setItens(new ArrayList<ItemPedido>());
                        pedido.getItens().add(itemPedido);
                        pedido.setQuantidadeItens(pedido.getQuantidadeItens() + itemPedido.getQuantidade());

                        Carrinho2 carrinho2 = new Carrinho2();
                        carrinho2.setPedido(pedido);
                        carrinho2.setTotal(carrinho2.getTotal() + itemPedido.getTotal());
                        carrinho2.setUsuario(new User());

                        CartUtils.createCart(carrinho2);
                    }else{
                        Toast.makeText(getApplicationContext(),"Informe a quantidade",Toast.LENGTH_SHORT).show();

                    }
                }
                else {
                    if(!quantidade.getText().toString().equals("")) {
                        boolean foundPreviouslyCreatedItem = false;
                        // Verifica se existe algum item com o mesmo ID para que altere a quantidade e o preco total

                        Iterator<ItemPedido> itemPedidoIterator = CartUtils.getCarrinho().getPedido().getItens().iterator();
                        while (itemPedidoIterator.hasNext()){
                            ItemPedido itemP = itemPedidoIterator.next();

                            if(itemP.getProduto().getId() == produto.getId()) {

                                int quant = Integer.parseInt(quantidade.getText().toString());

                                itemP.setQuantidade(itemP.getQuantidade() + quant);
                                itemP.setTotal(itemP.getQuantidade() * normalizePrice(precoDetails.getText().toString()));
                                Toast.makeText(getApplicationContext(), "Adicionado na sacola", Toast.LENGTH_SHORT).show();

                                double total = (CartUtils.getCarrinho().getTotal() - (itemP.getQuantidade() - quant) * itemP.getProduto().getPreco()) + itemP.getTotal();
                                CartUtils.getCarrinho().setTotal(total);

                                foundPreviouslyCreatedItem = true;

                                break;


                            }
                        }
                        // Verifica se o item não estava na lista
                        if(!foundPreviouslyCreatedItem){

                            ItemPedido itemPedido = new ItemPedido();

                            itemPedido.setProduto(produto);
                            itemPedido.setQuantidade(Integer.parseInt(quantidade.getText().toString()));
                            itemPedido.setTotal(itemPedido.getQuantidade() * normalizePrice(precoDetails.getText().toString()));


                            Carrinho2 carrinho2 = CartUtils.getCarrinho();
                            carrinho2.getPedido().getItens().add(itemPedido);
                            carrinho2.setTotal(carrinho2.getTotal() + itemPedido.getTotal());

                        }

                    }else{
                        Toast.makeText(getApplicationContext(),"Informe a quantidade",Toast.LENGTH_SHORT).show();
                    }
                }
                    Toast.makeText(getApplicationContext(),""+CartUtils.getQuantityOfProdutosFromCart(),Toast.LENGTH_LONG).show();

            }
        });

    }

    private double normalizePrice(String price){
        String tmp = price.replace("R$ ","");
        return Double.parseDouble(tmp);

    }

}
