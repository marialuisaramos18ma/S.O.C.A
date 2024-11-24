package com.example.projetoauto.model;

import java.io.Serializable;

public class Imagem implements Serializable {

    private String caminhoimagem;
    private int index;

    public Imagem() {
    }


    public Imagem(String caminhoimagem, int index) {
        this.caminhoimagem = caminhoimagem;
        this.index = index;
    }

    public String getCaminhoimagem() {
        return caminhoimagem;
    }

    public void setCaminhoimagem(String caminhoimagem) {
        this.caminhoimagem = caminhoimagem;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
}
