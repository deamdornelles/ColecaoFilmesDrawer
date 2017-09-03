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

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        nomeFilme = (TextView) getView().findViewById(R.id.nomeFilme);
        nomeOriginal = (TextView) getView().findViewById(R.id.nomeOriginal);

        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Detalhes");

        Bundle b = getArguments();
        nomeFilme.setText(b.getString("nome"));
        nomeOriginal.setText(b.getString("nomeOriginal"));
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.filme_detalhes, container, false);
    }
}
