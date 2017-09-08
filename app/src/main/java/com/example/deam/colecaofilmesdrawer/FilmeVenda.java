package com.example.deam.colecaofilmesdrawer;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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
 * Created by Deam on 07/09/2017.
 */

public class FilmeVenda extends Fragment {

    TextView nomeFilme;
    String idFilme;
    EditText descricao;
    Button salvar;

    String usuario;
    SharedPreferences shared;

    int retorno;

    Fragment fragment = null;

    private final String NAMESPACE = "http://ws/";
    private final String URL = "http://192.168.25.204:8080/Banco/BuscaFilme";
    private final String SOAP_ACTION = "http://192.168.25.204:8080/Banco/BuscaFilme/salvaAnuncio";
    private final String METHOD_NAME = "salvaAnuncio";

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        shared = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
        usuario = shared.getString("usuario", null);

        nomeFilme = (TextView) getView().findViewById(R.id.nomeFilme);
        descricao = (EditText) getView().findViewById(R.id.descricao);
        salvar = (Button) getView().findViewById(R.id.salvar);

        salvar.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (TextUtils.isEmpty(descricao.getText().toString())) {
                    descricao.setError("Informe a descricao");
                    return;
                } else {
                    AsyncCallWSSalvaAnuncio task = new AsyncCallWSSalvaAnuncio();
                    task.execute();
                }


            }
        });

        Bundle b = getArguments();
        nomeFilme.setText(b.getString("nome"));
        idFilme = b.getString("id");

        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Anúncio");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.filme_venda, container, false);
    }

    private class AsyncCallWSSalvaAnuncio extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... params) {

            getResposta();
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {

            if (retorno == 0) {
                Toast.makeText(getActivity(), "Anúncio cadastrado com sucesso", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getActivity(), "Ocorreu um erro ao cadastrar o anúncio, tente novamente", Toast.LENGTH_SHORT).show();
            }

            fragment = new MeusFilmes();
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.replace(R.id.content_main, fragment);
            ft.commit();
            //ArrayAdapter<Filme> arrayAdapter = new ArrayAdapter<Filme>(getContext(), android.R.layout.simple_list_item_1, listaFilmes);
            //filmes.setAdapter(arrayAdapter);

            //MyCustomAdapter adapter = new MyCustomAdapter(listaFilmes, getActivity().getApplicationContext());
            //filmes.setAdapter(adapter);

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

            PropertyInfo id = new PropertyInfo();
            id.type = PropertyInfo.STRING_CLASS;
            id.setValue(idFilme);
            id.setName("id_filme");
            id.setType((String.class));
            request.addProperty(id);

            PropertyInfo descricaoInfo = new PropertyInfo();
            descricaoInfo.type = PropertyInfo.STRING_CLASS;
            descricaoInfo.setValue(descricao.getText().toString());
            descricaoInfo.setName("descricao");
            descricaoInfo.setType((String.class));
            request.addProperty(descricaoInfo);

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            new MarshalBase64().register(envelope);
            envelope.setOutputSoapObject(request);
            HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);

            try {
                androidHttpTransport.call(SOAP_ACTION, envelope);
                SoapPrimitive response = (SoapPrimitive) envelope.getResponse();
                retorno = Integer.parseInt(response.toString());

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}