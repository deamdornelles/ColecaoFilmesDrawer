package com.example.deam.colecaofilmesdrawer;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Deam on 02/09/2017.
 */

public class AdicionarFilmes extends Fragment {

    private final String NAMESPACE = "http://ws/";
    private final String URL = "http://192.168.25.204:8080/Banco/BuscaFilme";
    private final String SOAP_ACTION = "http://192.168.25.204:8080/Banco/BuscaFilme/buscaTodosFilmes";
    private final String SOAP_ACTION2 = "http://192.168.25.204:8080/Banco/BuscaFilme/adicionaFilmes";
    private final String METHOD_NAME = "buscaTodosFilmes";
    private final String METHOD_NAME2 = "adicionaFilmes";

    ListView filmes;
    List<Filme> listaFilmes = new ArrayList<Filme>();

    SharedPreferences shared;
    String usuario;
    Fragment fragment = null;
    Button adicionar;
    ArrayList<Filme> lista;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Adicionar Filmes");

        fragment = new MeusFilmes();

        adicionar = (Button) getView().findViewById(R.id.adicionar);
        adicionar.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                    lista = ((MyCustomAdapter) filmes.getAdapter()).getFilmes();
                if (lista.size() > 0) {
                    Toast.makeText(getActivity(), "" + lista.size(), Toast.LENGTH_LONG).show();

                    AsyncCallWSAdicionaFilmes task = new AsyncCallWSAdicionaFilmes();
                    task.execute();
                } else {
                    Toast.makeText(getActivity(), "Você precisa marcar no mínimo um filme para adicionar", Toast.LENGTH_LONG).show();
                }



            }
        });

        filmes = (ListView) getView().findViewById(R.id.filmes);

        /*filmes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //view.setBackgroundColor(getResources().getColor(R.color.laranja));
            }
        });*/

        AsyncCallWSBuscaTodosFilmes task = new AsyncCallWSBuscaTodosFilmes();
        task.execute();

        shared = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());

        usuario = shared.getString("usuario", null);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.adicionar_filmes, container, false);
    }



    private class AsyncCallWSBuscaTodosFilmes extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... params) {

            getResposta();
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            //ArrayAdapter<Filme> arrayAdapter = new ArrayAdapter<Filme>(getContext(), android.R.layout.simple_list_item_1, listaFilmes);
            //filmes.setAdapter(arrayAdapter);

            MyCustomAdapter adapter = new MyCustomAdapter(listaFilmes, getActivity().getApplicationContext());
            filmes.setAdapter(adapter);

        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onProgressUpdate(Void... values) {
        }

        public void getResposta() {
            //Create request
            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);

            PropertyInfo login = new PropertyInfo();
            login.type = PropertyInfo.STRING_CLASS;
            login.setValue(usuario);
            login.setName("login");
            login.setType((String.class));
            request.addProperty(login);

            listaFilmes.clear();

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
                    f.setId(filme.getProperty(1).toString());
                    f.setNome(filme.getProperty(2).toString());
                    f.setNomeOriginal(filme.getProperty(3).toString());
                    listaFilmes.add(f);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private class AsyncCallWSAdicionaFilmes extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... params) {

            getResposta();
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            //ArrayAdapter<Filme> arrayAdapter = new ArrayAdapter<Filme>(getContext(), android.R.layout.simple_list_item_1, listaFilmes);
            //filmes.setAdapter(arrayAdapter);

            MyCustomAdapter adapter = new MyCustomAdapter(listaFilmes, getActivity().getApplicationContext());
            filmes.setAdapter(adapter);

        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onProgressUpdate(Void... values) {
        }

        public void getResposta() {
            //Create request
            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME2);
            String filmesArray = "";

            for (int i = 0; i < lista.size(); i++) {

                for (int j = 0; j < listaFilmes.size(); j++) {
                    if (lista.get(i).getNome().equals(listaFilmes.get(j).getNome())) {
                        listaFilmes.remove(i);
                    }
                }

                //filmesArray.add(lista.get(i).getNome());
                filmesArray = filmesArray + lista.get(i).getId() + ",";
            }


            PropertyInfo filmes = new PropertyInfo();
            filmes.type = PropertyInfo.STRING_CLASS;
            filmes.setValue(filmesArray);
            filmes.setName("filmes");
            filmes.setType((String.class));
            request.addProperty(filmes);

            PropertyInfo login = new PropertyInfo();
            login.type = PropertyInfo.STRING_CLASS;
            login.setValue(usuario);
            login.setName("login");
            login.setType((String.class));
            request.addProperty(login);

            //listaFilmes.clear();

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.setOutputSoapObject(request);
            HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
            try {
                androidHttpTransport.call(SOAP_ACTION2, envelope);

                //SoapObject response = (SoapObject)envelope.bodyIn;

                /*for (int i = 0; i < response.getPropertyCount(); i++) {
                    SoapObject filme = (SoapObject) response.getProperty(i);
                    Filme f = new Filme();
                    f.setAno(Integer.parseInt(filme.getProperty(0).toString()));
                    f.setNome(filme.getProperty(1).toString());
                    f.setNomeOriginal(filme.getProperty(2).toString());
                    listaFilmes.add(f);
                }*/

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}