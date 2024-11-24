package com.example.projetoauto.model;

import java.io.Serializable;

public class Horario implements Serializable {

    private String hora;

    public Horario(String hora) {
        this.hora = hora;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }
}

