package com.example.deam.colecaofilmesdrawer;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.MarshalBase64;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

/**
 * Created by Deam on 03/09/2017.
 */

public class FilmeDetalhes extends Fragment {

    private final String NAMESPACE = "http://ws/";
    private final String URL = "http://192.168.25.204:8080/Banco/BuscaFilme";
    private final String SOAP_ACTION = "http://192.168.25.204:8080/Banco/BuscaFilme/removeFilme";
    private final String METHOD_NAME = "removeFilme";

    String usuario;
    SharedPreferences shared;
    String idFilme;
    String retorno;

    TextView nomeFilme;
    TextView nomeOriginal;
    TextView elenco;
    TextView diretor;
    TextView genero;
    TextView ano;
    Button vender;
    Button remover;

    Fragment fragment = null;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        shared = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
        usuario = shared.getString("usuario", null);

        nomeFilme = (TextView) getView().findViewById(R.id.nomeFilme);
        nomeOriginal = (TextView) getView().findViewById(R.id.nomeOriginal);
        elenco = (TextView) getView().findViewById(R.id.elenco);
        diretor = (TextView) getView().findViewById(R.id.diretor);
        genero = (TextView) getView().findViewById(R.id.genero);
        ano = (TextView) getView().findViewById(R.id.ano);

        vender = (Button) getView().findViewById(R.id.vender);
        vender.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Bundle args = new Bundle();
                args.clear();
                args.putString("nome", nomeFilme.getText().toString());
                args.putString("id", idFilme);

                fragment = new FilmeVenda();
                fragment.setArguments(args);
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.content_main, fragment).addToBackStack(null);
                ft.commit();
            }
        });

        remover = (Button) getView().findViewById(R.id.remover);
        remover.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
                alertDialog.setMessage("Deseja remover este filme?");
                alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancelar",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Sim",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                AsyncCallWSRemoveFilme task = new AsyncCallWSRemoveFilme();
                                task.execute();
                                dialog.dismiss();
                            }
                        });
                alertDialog.show();

            }
        });

        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Detalhes");

        Bundle b = getArguments();
        nomeFilme.setText(b.getString("nome"));
        nomeOriginal.setText(b.getString("nomeOriginal"));
        elenco.setText(b.getString("atores"));
        diretor.setText(b.getString("diretores"));
        genero.setText(b.getString("generos"));
        ano.setText(String.valueOf(b.getInt("ano")));
        idFilme = b.getString("id");

        if (b.getBoolean("anunciado")) {
            vender.setVisibility(View.INVISIBLE);
        }

    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.filme_detalhes, container, false);
    }

    private class AsyncCallWSRemoveFilme extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... params) {

            getResposta();
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            Toast.makeText(getActivity(), retorno, Toast.LENGTH_SHORT).show();
            fragment = new MeusFilmes();
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.replace(R.id.content_main, fragment).addToBackStack(null);
            ft.commit();

        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onProgressUpdate(Void... values) {
        }

        public void getResposta() {

            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);

            PropertyInfo login = new PropertyInfo();
            login.type = PropertyInfo.STRING_CLASS;
            login.setValue(usuario);
            login.setName("login");
            login.setType((String.class));
            request.addProperty(login);

            PropertyInfo id = new PropertyInfo();
            id.type = PropertyInfo.STRING_CLASS;
            id.setValue(idFilme);
            id.setName("id_filme");
            id.setType((String.class));
            request.addProperty(id);

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            new MarshalBase64().register(envelope);
            envelope.setOutputSoapObject(request);
            HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);

            try {
                androidHttpTransport.call(SOAP_ACTION, envelope);
                SoapPrimitive response = (SoapPrimitive) envelope.getResponse();
                retorno = response.toString();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}