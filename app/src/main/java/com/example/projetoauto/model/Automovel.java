package com.example.projetoauto.model;

import android.app.Activity;
import android.content.Intent;

import com.example.projetoauto.activity.empresa.EmpresaHomeActivity;
import com.example.projetoauto.helper.FirebaseHelper;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ServerValue;
import com.google.firebase.storage.StorageReference;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Automovel implements Serializable {

    private String id;
    private String titulo;
    private String descricao;
    private String placa;
    private String modelo;
    private String ano;
    private String quilometragem;
    private Double valorDeVenda;
    private Double valorComprado;

    private String idEmpresa;
    private String idTipo;
    private String idEndereco;

    private long dataPublicacao;

    private final List<Imagem> urlImagens = new ArrayList<>();

    private Endereco endereco;

    private Empresa empresa;


    public Automovel() {
        DatabaseReference automovelRef = FirebaseHelper.getDatabaseReference();
        this.setId(automovelRef.push().getKey());
    }


    public void salvar(Activity activity, boolean novoAutomovel) {
        DatabaseReference automoveisPublicosRef = FirebaseHelper.getDatabaseReference()
                .child("automoveis_publicos")
                .child(this.getId());
        automoveisPublicosRef.setValue(this);

        DatabaseReference meusAutomoveisRef = FirebaseHelper.getDatabaseReference()
                .child("meus_automoveis")
                .child(FirebaseHelper.getIdFirebase())
                .child(this.getId());
        meusAutomoveisRef.setValue(this);

        if (novoAutomovel) {
            DatabaseReference dataAutomovelPublico = automoveisPublicosRef
                    .child("dataPublicacao");
            dataAutomovelPublico.setValue(ServerValue.TIMESTAMP);

            DatabaseReference dataAutomovel = meusAutomoveisRef
                    .child("dataPublicacao");
            dataAutomovel.setValue(ServerValue.TIMESTAMP).addOnCompleteListener(task -> {
                activity.finish();
                Intent intent = new Intent(activity, EmpresaHomeActivity.class);
                activity.startActivity(intent);
            });
        } else {
            activity.finish();
        }

    }

    public void remover() {
        DatabaseReference automoveisPublicosRef = FirebaseHelper.getDatabaseReference()
                .child("automoveis_publicos")
                .child(this.getId());
        automoveisPublicosRef.removeValue();

        DatabaseReference meusAutomoveisRef = FirebaseHelper.getDatabaseReference()
                .child("meus_automoveis")
                .child(FirebaseHelper.getIdFirebase())
                .child(this.getId());
        meusAutomoveisRef.removeValue();

        for (int i = 0; i < getUrlImagens().size(); i++) {
            StorageReference storageReference = FirebaseHelper.getStorageReference()
                    .child("imagens")
                    .child("automoveis")
                    .child(getId())
                    .child("imagem" + i + ".jpeg");
            storageReference.delete();
        }
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getPlaca() {
        return placa;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public String getAno() {
        return ano;
    }

    public void setAno(String ano) {
        this.ano = ano;
    }

    public String getQuilometragem() {
        return quilometragem;
    }

    public void setQuilometragem(String quilometragem) {
        this.quilometragem = quilometragem;
    }

    public Double getValorDeVenda() {
        return valorDeVenda;
    }

    public void setValorDeVenda(Double valorDeVenda) {
        this.valorDeVenda = valorDeVenda;
    }

    public Double getValorComprado() {
        return valorComprado;
    }

    public void setValorComprado(Double valorComprado) {
        this.valorComprado = valorComprado;
    }

    public String getIdEmpresa() {
        return idEmpresa;
    }

    public void setIdEmpresa(String idEmpresa) {
        this.idEmpresa = idEmpresa;
    }

    public String getIdEndereco() {
        return idEndereco;
    }

    public String getIdTipo() {
        return idTipo;
    }

    public void setIdTipo(String idTipo) {
        this.idTipo = idTipo;
    }

    public List<Imagem> getUrlImagens() {
        return urlImagens;
    }

    public void setIdEndereco(String idEndereco) {
        this.idEndereco = idEndereco;
    }

    public Endereco getEndereco() {
        return endereco;
    }

    public void setEndereco(Endereco endereco) {
        this.endereco = endereco;
    }

    public Empresa getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Empresa empresa) {
        this.empresa = empresa;
    }

    public long getDataPublicacao() {
        return dataPublicacao;
    }

    public void setDataPublicacao(long dataPublicacao) {
        this.dataPublicacao = dataPublicacao;
    }
}
