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

public class AnuncioDetalhes extends Fragment {

    private final String NAMESPACE = "http://ws/";
    private final String URL = "http://10.0.2.2:8080/Banco/BuscaFilme";
    private final String SOAP_ACTION = "http://10.0.2.2:8080/Banco/BuscaFilme/atualizaAnuncio";
    private final String METHOD_NAME = "atualizaAnuncio";
    private final String SOAP_ACTION2 = "http://10.0.2.2:8080/Banco/BuscaFilme/removeAnuncio";
    private final String METHOD_NAME2 = "removeAnuncio";

    EditText descricao;
    Button atualizar;
    Button remover;
    TextView nomeFilme;
    String idAnuncio;
    String idFilme;
    int retorno;
    int retorno2;

    Fragment fragment = null;

    SharedPreferences shared;
    String usuario;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Detalhes");

        shared = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
        usuario = shared.getString("usuario", null);

        fragment = new Fragment();

        descricao = (EditText) getView().findViewById(R.id.descricao);
        nomeFilme = (TextView) getView().findViewById(R.id.nomeFilme);

        Bundle b = getArguments();
        descricao.setText(b.getString("descricao"));
        nomeFilme.setText(b.getString("nome"));
        idAnuncio = b.getString("id");
        idFilme = b.getString("id_filme");

        atualizar = (Button) getView().findViewById(R.id.atualizar);

        atualizar.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (TextUtils.isEmpty(descricao.getText().toString())) {
                    descricao.setError("Informe a descricao");
                    return;
                } else {
                    AsyncCallWSAtualizaAnuncio task = new AsyncCallWSAtualizaAnuncio();
                    task.execute();
                }
            }
        });

        remover = (Button) getView().findViewById(R.id.remover);

        remover.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
                alertDialog.setMessage("Deseja remover este anúncio?");
                alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancelar",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Sim",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                AsyncCallWSRemoveAnuncio task = new AsyncCallWSRemoveAnuncio();
                                task.execute();
                                dialog.dismiss();
                            }
                        });
                alertDialog.show();

            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.anuncio_detalhes, container, false);
    }

    private class AsyncCallWSAtualizaAnuncio extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... params) {

            getResposta();
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {

            if (retorno == 0) {
                Toast.makeText(getActivity(), "Anúncio atualizado com sucesso", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getActivity(), "Ocorreu um erro ao atualizar o anúncio, tente novamente", Toast.LENGTH_SHORT).show();
            }
            fragment = new MeusAnuncios();
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

            PropertyInfo id = new PropertyInfo();
            id.type = PropertyInfo.STRING_CLASS;
            id.setValue(idAnuncio);
            id.setName("id");
            id.setType((String.class));
            request.addProperty(id);

            PropertyInfo descricaoInfo = new PropertyInfo();
            descricaoInfo.type = PropertyInfo.STRING_CLASS;
            descricaoInfo.setValue(descricao.getText().toString());
            descricaoInfo.setName("descricao");
            descricaoInfo.setType((String.class));
            request.addProperty(descricaoInfo);

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
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

    private class AsyncCallWSRemoveAnuncio extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... params) {

            getResposta();
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            if (retorno2 == 0) {
                Toast.makeText(getActivity(), "Anúncio removido com sucesso", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getActivity(), "Ocorreu um erro ao remover o anúncio, tente novamente", Toast.LENGTH_SHORT).show();
            }
            fragment = new MeusAnuncios();
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

            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME2);

            PropertyInfo id = new PropertyInfo();
            id.type = PropertyInfo.STRING_CLASS;
            id.setValue(idAnuncio);
            id.setName("id_anuncio");
            id.setType((String.class));
            request.addProperty(id);

            PropertyInfo id_filme = new PropertyInfo();
            id_filme.type = PropertyInfo.STRING_CLASS;
            id_filme.setValue(idFilme);
            id_filme.setName("id_filme");
            id_filme.setType((String.class));
            request.addProperty(id_filme);

            PropertyInfo usuario_info = new PropertyInfo();
            usuario_info.type = PropertyInfo.STRING_CLASS;
            usuario_info.setValue(usuario);
            usuario_info.setName("nome_usuario");
            usuario_info.setType((String.class));
            request.addProperty(usuario_info);

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            new MarshalBase64().register(envelope);
            envelope.setOutputSoapObject(request);
            HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);

            try {
                androidHttpTransport.call(SOAP_ACTION2, envelope);
                SoapPrimitive response = (SoapPrimitive) envelope.getResponse();
                retorno2  = Integer.parseInt(response.toString());

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}