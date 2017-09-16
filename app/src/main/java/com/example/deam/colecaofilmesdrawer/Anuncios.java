package com.example.deam.colecaofilmesdrawer;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ListView;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Deam on 07/09/2017.
 */

public class Anuncios extends Fragment {

    private final String NAMESPACE = "http://ws/";
    private final String URL = "http://192.168.25.211:8080/Banco/BuscaFilme";
    private final String SOAP_ACTION = "http://192.168.25.211:8080/Banco/BuscaFilme/buscaTodosAnuncios";
    private final String METHOD_NAME = "buscaTodosAnuncios";

    ProgressDialog pd;

    List<Anuncio> listaAnuncios = new ArrayList<Anuncio>();
    ListView anuncios;

    SharedPreferences shared;
    String usuario;

    AutoCompleteTextView procura;

    Fragment fragment = null;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Anúncios");

        fragment = new AnunciosDetalhes();

        procura = (AutoCompleteTextView) getView().findViewById(R.id.procura);

        anuncios = (ListView) getView().findViewById(R.id.anuncios);
        anuncios.setEmptyView(getView().findViewById(R.id.empty_list_item));

        anuncios.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {

                Anuncio anuncio = (Anuncio) parent.getItemAtPosition(position);

                Bundle args = new Bundle();
                args.clear();
                args.putString("descricao", anuncio.getDescricao());
                args.putString("nome", anuncio.getNomeFilme());
                args.putString("id", anuncio.getId());
                args.putString("id_filme", anuncio.getId_filme());
                args.putString("nome_usuario", anuncio.getNome_usuario());
                fragment.setArguments(args);

                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.content_main, fragment).addToBackStack(null);
                ft.commit();
            }
        });

        shared = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
        usuario = shared.getString("usuario", null);

        AsyncCallWSBuscaAnuncio task = new AsyncCallWSBuscaAnuncio();
        task.execute();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.anuncios, container, false);
    }

    private class AsyncCallWSBuscaAnuncio extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... params) {

            getResposta();
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            pd.dismiss();
            ArrayAdapter<Anuncio> arrayAdapter = new ArrayAdapter<Anuncio>(getContext(), android.R.layout.simple_list_item_1, listaAnuncios);
            procura.setAdapter(arrayAdapter);
            anuncios.setAdapter(arrayAdapter);


            //filmes.setAdapter(adapter);

        }

        @Override
        protected void onPreExecute() {
            pd = new ProgressDialog(getActivity());
            pd.setMessage("Carregando anúncios");
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

            listaAnuncios.clear();

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.setOutputSoapObject(request);
            HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
            try {
                androidHttpTransport.call(SOAP_ACTION, envelope);
                SoapObject response = (SoapObject)envelope.bodyIn;

                for (int i = 0; i < response.getPropertyCount(); i++) {
                    SoapObject anuncio = (SoapObject) response.getProperty(i);
                    Anuncio a = new Anuncio();
                    a.setId(anuncio.getProperty("id").toString());
                    a.setDescricao(anuncio.getProperty("descricao").toString());
                    a.setNomeFilme(anuncio.getProperty("nomeFilme").toString());
                    a.setId_filme(anuncio.getProperty("id_filme").toString());
                    a.setNome_usuario(anuncio.getProperty("nome_usuario").toString());
                    listaAnuncios.add(a);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}