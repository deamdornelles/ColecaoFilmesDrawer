package com.example.deam.colecaofilmesdrawer;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by Deam on 07/09/2017.
 */

public class AnunciosDetalhes extends Fragment {

    TextView descricao;
    TextView nome_usuario;
    TextView nomeFilme;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Detalhes");

        descricao = (TextView) getView().findViewById(R.id.descricao);
        nomeFilme = (TextView) getView().findViewById(R.id.nomeFilme);
        nome_usuario = (TextView) getView().findViewById(R.id.usuario);

        Bundle b = getArguments();
        descricao.setText(b.getString("descricao"));
        nomeFilme.setText(b.getString("nome"));
        nome_usuario.setText(b.getString("nome_usuario"));

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.anuncios_detalhes, container, false);
    }
}