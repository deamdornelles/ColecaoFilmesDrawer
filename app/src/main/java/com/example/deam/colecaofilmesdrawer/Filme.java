package com.example.deam.colecaofilmesdrawer;

/**
 * Created by Deam on 02/09/2017.
 */

public class Filme {

    String nome;
    int ano;

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

    @Override
    public String toString() {
        return nome + " - " + ano;
    }
}