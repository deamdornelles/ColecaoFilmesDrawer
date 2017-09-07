package com.example.deam.colecaofilmesdrawer;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by Deam on 03/09/2017.
 */

public class FilmeDetalhes extends Fragment {

    TextView nomeFilme;
    TextView nomeOriginal;
    TextView elenco;
    TextView diretor;
    TextView genero;


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        nomeFilme = (TextView) getView().findViewById(R.id.nomeFilme);
        nomeOriginal = (TextView) getView().findViewById(R.id.nomeOriginal);
        elenco = (TextView) getView().findViewById(R.id.elenco);
        diretor = (TextView) getView().findViewById(R.id.diretor);
        genero = (TextView) getView().findViewById(R.id.genero);


        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Detalhes");

        Bundle b = getArguments();
        nomeFilme.setText(b.getString("nome"));
        nomeOriginal.setText(b.getString("nomeOriginal"));
        elenco.setText(b.getString("atores"));
        diretor.setText(b.getString("diretores"));
        genero.setText(b.getString("generos"));

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.filme_detalhes, container, false);
    }
}
