package com.example.projetoauto.model;


public class Filtro{


    private String tipo;
    private String pesquisa;
    private int valorMin;
    private int valorMax;

    public Filtro() {
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getPesquisa() {
        return pesquisa;
    }

    public void setPesquisa(String pesquisa) {
        this.pesquisa = pesquisa;
    }

    public int getValorMin() {
        return valorMin;
    }

    public void setValorMin(int valorMin) {
        this.valorMin = valorMin;
    }

    public int getValorMax() {
        return valorMax;
    }

    public void setValorMax(int valorMax) {
        this.valorMax = valorMax;
    }
}
