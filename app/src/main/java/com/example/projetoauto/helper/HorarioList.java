package com.example.projetoauto.helper;

import com.example.projetoauto.model.Horario;

import java.util.ArrayList;
import java.util.List;

public class HorarioList {

    public static List<Horario> getHorarios(boolean todas){

        List<Horario> horarioList = new ArrayList<>();

        if (todas) horarioList.add(new Horario("Todos os Horarios"));

        horarioList.add(new Horario("8:00"));
        horarioList.add(new Horario("9:00"));
        horarioList.add(new Horario("10:00"));
        horarioList.add(new Horario("11:00"));
        horarioList.add(new Horario("14:00"));
        horarioList.add(new Horario("15:00"));
        horarioList.add(new Horario("16:00"));
        horarioList.add(new Horario("17:00"));


        return horarioList;
    }
}
