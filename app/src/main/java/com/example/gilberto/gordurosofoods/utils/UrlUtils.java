package com.example.gilberto.gordurosofoods.utils;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;

/**
 * Created by Gilberto on 15/03/2018.
 */

public class UrlUtils {

    private static final String PROTOCOL = "http";
    private static final int PORT  = 80;
    private static final String SERVER_IP = "ayrtonmarinho.esy.es";
    private static final String WEBSITE = "gorduroso_foods";
    private static final String ALL_PRODUCTS = WEBSITE+"/produto.php";
    private static final String SEARCH = WEBSITE+"/search.php";
    private static final String ORDERS = WEBSITE+"/orders.php";

    /*
    * Esse método conecta-se ao site, usando o ip do servidor e o nome da pasta principal do site
    *
    * */
    public static URL getWebSiteURL(){
        try {
            return  new URL(PROTOCOL, SERVER_IP, PORT, WEBSITE, new URLStreamHandler() {
                @Override
                protected URLConnection openConnection(URL url) throws IOException {
                   return new URLConnection(url) {
                       @Override
                       public void connect() throws IOException {}
                   };
                }
            });
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        }

    }

    /**
     * Pega todos os produtos no banco de dados.
     * Usa uma instancia de URL, recebendo como parametro o protocolo a ser utilizado, o ip do servidor, a porta a ser utilizada
     * e a pagina web em que se encontram os dados.
     *
     * O quinto parametro é um objecto da classe abstrata URLStreamHandler que basicamente é necessário que ocorra uma
     * implementação do método openConnection(). O mesmo deve retornar uma instancia de URLConnection passado como parâmentro
     * a url que foi usada a partir dos dados de protocolo,porta,ip e arquivo. D
     * @return
     */
    public static URL getProdutos(){
        try{
            return new URL(PROTOCOL, SERVER_IP, PORT, ALL_PRODUCTS, new URLStreamHandler() {
                @Override
                protected URLConnection openConnection(URL url) throws IOException {
                    return new URLConnection(url) {
                        @Override
                        public void connect() throws IOException {}
                    };
                }
            });
        }catch(MalformedURLException e){
            e.printStackTrace();
            return null;
        }
    }
    public static URL makeSearch(){
        try{
            return new URL(PROTOCOL, SERVER_IP, PORT, SEARCH, new URLStreamHandler() {
                @Override
                protected URLConnection openConnection(URL url) throws IOException {
                    return new URLConnection(url) {
                        @Override
                        public void connect() throws IOException {}
                    };
                }
            });
        }catch(MalformedURLException e){
            e.printStackTrace();
            return null;
        }
    }

    public static URL orders(){
        try {
            return new URL(PROTOCOL, SERVER_IP, PORT,ORDERS, new URLStreamHandler() {
                @Override
                protected URLConnection openConnection(URL url) throws IOException {
                    return new URLConnection(url) {
                        @Override
                        public void connect() throws IOException {}
                    };
                }
            });
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        }

    }


}
