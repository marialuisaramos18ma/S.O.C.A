package com.example.projetoauto.model;

import android.net.Uri;

import com.example.projetoauto.helper.FirebaseHelper;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;

import java.io.Serializable;

public class Empresa implements Serializable {

    private String Id;
    private String nome;
    private String email;
    private String telefone;
    private String senha;
    private String imagemLogo;
    private Boolean acesso;

    private String endereco;

    public Empresa() {

    }

    public void salvar() {
        DatabaseReference empresaRef = FirebaseHelper.getDatabaseReference()
                .child("empresas")
                .child(getId());
        empresaRef.setValue(this);

        FirebaseUser user = FirebaseHelper.getAuth().getCurrentUser();
        UserProfileChangeRequest perfil;
        if (getImagemLogo() == null) {
            perfil = new UserProfileChangeRequest.Builder()
                    .setDisplayName(getNome())
                    .build();
        } else {
            perfil = new UserProfileChangeRequest.Builder()
                    .setDisplayName(getNome())
                    .setPhotoUri(Uri.parse(getImagemLogo()))
                    .build();
        }

        if (user != null) user.updateProfile(perfil);

    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
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

    @Exclude
    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getImagemLogo() {
        return imagemLogo;
    }

    public void setImagemLogo(String imagemPerfil) {
        this.imagemLogo = imagemPerfil;
    }

    public Boolean getAcesso() {
        return acesso;
    }

    public void setAcesso(Boolean acesso) {
        this.acesso = acesso;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }
}
