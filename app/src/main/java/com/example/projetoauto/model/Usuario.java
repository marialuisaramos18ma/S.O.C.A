package com.example.projetoauto.model;

import com.example.projetoauto.helper.FirebaseHelper;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;

import java.io.Serializable;

public class Usuario implements Serializable {

    //Atributos de usuarios
    private String Id;
    private String nome;
    private String email;
    private String telefone;
    private String senha;
    private String imagemPerfil;

    public Usuario() {

    }

    public void salvar() {
        DatabaseReference usuariosRef = FirebaseHelper.getDatabaseReference()
                .child("usuarios")
                .child(getId());
        usuariosRef.setValue(this);

        FirebaseUser user = FirebaseHelper.getAuth().getCurrentUser();
        UserProfileChangeRequest perfil = new UserProfileChangeRequest.Builder()
                .setDisplayName(getNome())
                .build();

        if (user != null) user.updateProfile(perfil);
    }

    public String getId() {
        return Id;
    }

    public void setId(String Id) {
        this.Id = Id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getImagemPerfil() {
        return imagemPerfil;
    }

    public void setImagemPerfil(String imagemPerfil) {
        this.imagemPerfil = imagemPerfil;
    }

    @Exclude
    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

}
