package com.example.projetoauto.util;


import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class Util {



    public static boolean statusInternet_MoWi(Context context) {
        boolean status = false;

        ConnectivityManager conexao = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo informacao = conexao.getActiveNetworkInfo();

        if (informacao != null && informacao.isConnected()) {
            status = true;

        } else
            status = false;

        return status;
    }






}
