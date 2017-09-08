package com.example.deam.colecaofilmesdrawer;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Deam on 02/09/2017.
 */

public class Filme {

    String nome;
    int ano;
    String nomeOriginal;
    boolean selected;
    String id;
    String atores;
    String diretores;
    String generos;
    byte[] imagem;
    boolean anunciado;

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getAno() {
        return ano;
    }

    public void setAno(int ano) {
        this.ano = ano;
    }

    public String getNomeOriginal() {
        return nomeOriginal;
    }

    public void setNomeOriginal(String nomeOriginal) {
        this.nomeOriginal = nomeOriginal;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAtores() {
        return atores;
    }

    public void setAtores(String atores) {
        this.atores = atores;
    }

    public String getDiretores() {
        return diretores;
    }

    public void setDiretores(String diretores) {
        this.diretores = diretores;
    }

    public String getGeneros() {
        return generos;
    }

    public void setGeneros(String generos) {
        this.generos = generos;
    }

    public byte[] getImagem() {
        return imagem;
    }

    public void setImagem(byte[] imagem) {
        this.imagem = imagem;
    }

    public boolean isAnunciado() {
        return anunciado;
    }

    public void setAnunciado(boolean anunciado) {
        this.anunciado = anunciado;
    }

    @Override
    public String toString() {
        return nome + " - " + ano;
    }
}