package com.example.deam.colecaofilmesdrawer;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ListView;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Deam on 02/09/2017.
 */

public class MeusFilmes extends Fragment {

    private final String NAMESPACE = "http://ws/";
    private final String URL = "http://192.168.25.211:8080/Banco/BuscaFilme";
    private final String SOAP_ACTION = "http://192.168.25.211:8080/Banco/BuscaFilme/buscaFilme";
    private final String METHOD_NAME = "buscaFilme";

    ListView filmes;
    List<Filme> listaFilmes = new ArrayList<Filme>();
    AutoCompleteTextView procura;

    SharedPreferences shared;
    String usuario;

    Fragment fragment = null;

    ProgressDialog pd;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Meus Filmes");

        fragment = new FilmeDetalhes();

        procura = (AutoCompleteTextView) getView().findViewById(R.id.procura);


        filmes = (ListView) getView().findViewById(R.id.filmes);
        filmes.setEmptyView(getView().findViewById(R.id.empty_list_item));

        filmes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {

                Filme filme = (Filme) parent.getItemAtPosition(position);

                Bundle args = new Bundle();
                args.clear();
                args.putString("nome", filme.getNome());
                args.putString("nomeOriginal", filme.getNomeOriginal());
                args.putString("atores", filme.getAtores().substring(0, filme.getAtores().lastIndexOf(",")));
                args.putString("diretores", filme.getDiretores().substring(0, filme.getDiretores().lastIndexOf(",")));
                args.putString("generos", filme.getGeneros().substring(0, filme.getGeneros().lastIndexOf(",")));
                args.putInt("ano", filme.getAno());
                args.putString("id", filme.getId());
                args.putBoolean("anunciado", filme.isAnunciado());
                //args.putByteArray("imagem", filme.getImagem());
                fragment.setArguments(args);

                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.content_main, fragment).addToBackStack(null);
                ft.commit();
            }
        });

        AsyncCallWSBuscaFilme task = new AsyncCallWSBuscaFilme();
        task.execute();

        shared = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
        usuario = shared.getString("usuario", null);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.meus_filmes, container, false);
    }

    private class AsyncCallWSBuscaFilme extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... params) {

            getResposta();
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            pd.dismiss();
            ArrayAdapter<Filme> arrayAdapter = new ArrayAdapter<Filme>(getContext(), android.R.layout.simple_list_item_1, listaFilmes);
            procura.setText("");
            procura.setAdapter(arrayAdapter);
            filmes.setAdapter(arrayAdapter);


            //filmes.setAdapter(adapter);

        }

        @Override
        protected void onPreExecute() {
            pd = new ProgressDialog(getActivity());
            pd.setMessage("Carregando filmes");
            pd.show();
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
                    f.setAno(Integer.parseInt(filme.getProperty("ano").toString()));
                    f.setAtores(filme.getProperty("atores").toString());
                    f.setDiretores(filme.getProperty("diretores").toString());
                    f.setGeneros(filme.getProperty("generos").toString());
                    f.setId(filme.getProperty("id").toString());
                    //f.setImagem(Base64.decode(filme.getProperty("imagem").toString().getBytes(), Base64.DEFAULT));
                    f.setNome(filme.getProperty("nome").toString());
                    f.setNomeOriginal(filme.getProperty("nomeOriginal").toString());
                    f.setAnunciado(Boolean.valueOf(filme.getProperty("anunciado").toString()));
                    listaFilmes.add(f);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}