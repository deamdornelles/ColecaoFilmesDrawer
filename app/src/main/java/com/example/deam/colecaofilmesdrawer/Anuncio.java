package com.example.deam.colecaofilmesdrawer;

/**
 * Created by Deam on 07/09/2017.
 */

public class Anuncio {

    String id;
    String descricao;
    String nomeFilme;
    String id_filme;
    String nome_usuario;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getNomeFilme() {
        return nomeFilme;
    }

    public void setNomeFilme(String nomeFilme) {
        this.nomeFilme = nomeFilme;
    }

    public String getId_filme() {
        return id_filme;
    }

    public void setId_filme(String id_filme) {
        this.id_filme = id_filme;
    }

    public String getNome_usuario() {
        return nome_usuario;
    }

    public void setNome_usuario(String nome_usuario) {
        this.nome_usuario = nome_usuario;
    }

    @Override
    public String toString() {
        return nomeFilme + " - " + descricao;
    }
}
