package com.example.projetoauto.model;

import com.example.projetoauto.helper.FirebaseHelper;
import com.google.firebase.database.DatabaseReference;

import java.io.Serializable;

public class Login implements Serializable {
    private String id;
    private String tipo;
    private boolean acesso;

    public Login() {
    }

    public Login(String id, String tipo, boolean acesso) {
        this.id = id;
        this.tipo = tipo;
        this.acesso = acesso;
    }

    public void salvar() {
        DatabaseReference loginRef = FirebaseHelper.getDatabaseReference()
                .child("login")
                .child(getId());
        loginRef.setValue(this);

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public boolean getAcesso() {
        return acesso;
    }

    public void setAcesso(boolean acesso) {
        this.acesso = acesso;
    }

}
