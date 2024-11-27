package com.example.projetoauto.helper;

import com.example.projetoauto.R;
import com.example.projetoauto.model.Tipo;

import java.util.ArrayList;
import java.util.List;

public class TipoList {

    public static List<Tipo> getList(boolean todos) {
        List<Tipo> categoriaList = new ArrayList<>();
        if (todos) {
            // Mantém o ícone para "Todos os Tipos"
            categoriaList.add(new Tipo(R.drawable.ic_todos_tipos, "Todos os Tipos"));
        }
        // Remove os ícones para outros tipos
        categoriaList.add(new Tipo(0, "Motores de Pistão"));
        categoriaList.add(new Tipo(0, "Motores a Jato"));
        categoriaList.add(new Tipo(0, "Motores a Hélice"));

        return categoriaList;
    }
}
