package com.example.projetoauto.helper;

import android.app.Activity;
import android.content.SharedPreferences;

import com.example.projetoauto.model.Filtro;


public class SPFiltro {

    private static final String ARQUIVO_PREFERENCIA = "ArquivoPreferencia";

    public static void setFiltro(Activity activity, String chave, String valor) {
        SharedPreferences preferences = activity.getSharedPreferences(ARQUIVO_PREFERENCIA, 0);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(chave, valor);
        editor.apply();
    }

    public static Filtro getFiltro(Activity activity) {
        SharedPreferences preferences = activity.getSharedPreferences(ARQUIVO_PREFERENCIA, 0);

        String pesquisa = preferences.getString("pesquisa", "");
        String tipo = preferences.getString("tipo", "");

        String valorMin = preferences.getString("valorMin", "");
        String valorMax = preferences.getString("valorMax", "");


        Filtro filtro = new Filtro();
        filtro.setPesquisa(pesquisa);
        filtro.setTipo(tipo);

        if (!valorMin.isEmpty()) filtro.setValorMin(Integer.parseInt(valorMin));
        if (!valorMax.isEmpty()) filtro.setValorMax(Integer.parseInt(valorMax));

        return filtro;
    }

    public static void LimparFiltros(Activity activity) {
        setFiltro(activity, "pesquisa", "");
        setFiltro(activity, "tipo", "");
        setFiltro(activity, "valorMin", "");
        setFiltro(activity, "valorMax", "");
    }

}
