package com.example.projetoauto.model;

import android.app.Activity;

import com.example.projetoauto.helper.FirebaseHelper;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.StorageReference;

import java.io.Serializable;

public class Agendamento implements Serializable {

    String id;
    private long data;
    private String descricao;

    private String IdAutomovel;

    private Automovel automovel;

    private String horario;
    private Usuario usuario;



    public Agendamento() {
        DatabaseReference agendamentoRef = FirebaseHelper.getDatabaseReference();
        this.setId(agendamentoRef.push().getKey());
    }


    public void Salvar(Activity activity) {
        DatabaseReference agendamentoRef = FirebaseHelper.getDatabaseReference()
                .child("agendamentos")
                .child(FirebaseHelper.getIdFirebase())
                .child(this.getId());

        agendamentoRef.setValue(this);



        activity.finish();
    }

    public void remover() {
        DatabaseReference agendamentoRef = FirebaseHelper.getDatabaseReference()
                .child("agendamentos")
                .child(FirebaseHelper.getIdFirebase())
                .child(this.getId());
        agendamentoRef.removeValue();
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public long getData() {
        return data;
    }

    public void setData(long data) {
        this.data = data;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }


    public String getIdAutomovel() {
        return IdAutomovel;
    }

    public void setIdAutomovel(String idAutomovel) {
        IdAutomovel = idAutomovel;
    }

    public Automovel getAutomovel() {
        return automovel;
    }

    public void setAutomovel(Automovel automovel) {
        this.automovel = automovel;
    }

    public String getHorario() {
        return horario;
    }

    public void setHorario(String horario) {
        this.horario = horario;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
}
