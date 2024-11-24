package com.example.projetoauto.helper;


import com.example.projetoauto.R;
import com.example.projetoauto.model.Tipo;

import java.util.ArrayList;
import java.util.List;

public class TipoList {

    public static List<Tipo> getList(boolean todos) {

        List<Tipo> categoriaList = new ArrayList<>();
        if (todos) categoriaList.add(new Tipo(R.drawable.ic_todos_tipos, "Todos os Tipos"));
        categoriaList.add(new Tipo(R.drawable.ic_moto, "A - Motores de Pistão"));
        categoriaList.add(new Tipo(R.drawable.ic_carro, "B - Motores a Jato"));
        categoriaList.add(new Tipo(R.drawable.ic_caminhao, "C - Motores a Hélice"));
        categoriaList.add(new Tipo(R.drawable.ic_van, "D - Motores Estatoreadores."));
        categoriaList.add(new Tipo(R.drawable.ic_caminhao_grande, "E - Motores a Turbina"));


        return categoriaList;

    }

}
