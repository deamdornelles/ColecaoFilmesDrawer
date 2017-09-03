package com.example.deam.colecaofilmesdrawer;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Deam on 02/09/2017.
 */

public class MeusFilmes extends Fragment {

    private final String NAMESPACE = "http://ws/";

    private final String URL = "http://192.168.25.204:8080/Banco/BuscaFilme";
    private final String SOAP_ACTION = "http://192.168.25.204:8080/Banco/BuscaFilme/buscaFilme";
    private final String METHOD_NAME = "buscaFilme";
    private static String retorno;

    ListView filmes;
    List<Filme> listaFilmes = new ArrayList<Filme>();

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Meus Filmes");

        filmes = (ListView) getView().findViewById(R.id.filmes);
        filmes.setEmptyView(getView().findViewById(R.id.empty_list_item));

        AsyncCallWS task = new AsyncCallWS();
        task.execute();

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.meus_filmes, container, false);
    }


    private class AsyncCallWS extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... params) {
            getResposta();
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            ArrayAdapter<Filme> arrayAdapter = new ArrayAdapter<Filme>(getContext(), android.R.layout.simple_list_item_1, listaFilmes);
            filmes.setAdapter(arrayAdapter);
        }

        @Override
        protected void onPreExecute() {
            //tv.setText("Calculating...");
        }

        @Override
        protected void onProgressUpdate(Void... values) {
        }

        public boolean onCreateOptionsMenu(Menu menu) {
            return true;
        }

        public boolean onOptionsItemSelected(MenuItem item) {
            return true;
        }

        public void getResposta() {
            //Create request

            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
            //PropertyInfo celsiusPI = new PropertyInfo();
            //celsiusPI.type = PropertyInfo.STRING_CLASS;
            //celsiusPI.setName("Alo");
            //celsiusPI.setValue(celsius);
            //celsiusPI.setType(String.class);
            //request.addProperty(celsiusPI);
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.setOutputSoapObject(request);
            HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
            try {
                androidHttpTransport.call(SOAP_ACTION, envelope);
                SoapObject response = (SoapObject)envelope.bodyIn;

                for (int i = 0; i < response.getPropertyCount(); i++) {
                    SoapObject filme = (SoapObject) response.getProperty(i);
                    Filme f = new Filme();
                    f.setAno(Integer.parseInt(filme.getProperty(0).toString()));
                    f.setNome(filme.getProperty(1).toString());
                    listaFilmes.add(f);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}