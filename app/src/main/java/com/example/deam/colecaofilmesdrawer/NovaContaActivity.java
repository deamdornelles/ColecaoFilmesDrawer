package com.example.deam.colecaofilmesdrawer;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

public class NovaContaActivity extends AppCompatActivity {

    EditText loginCadastro;
    EditText senhaCadastro;
    String retorno;

    ProgressDialog pd;

    private final String NAMESPACE = "http://ws/";
    private final String URL = "http://192.168.25.211:8080/Banco/CadastraUsuario";
    private final String SOAP_ACTION = "http://192.168.25.211:8080/Banco/CadastraUsuario/cadastraUsuario";
    private final String METHOD_NAME = "cadastraUsuario";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nova_conta);

        loginCadastro = (EditText) findViewById(R.id.loginCadastro);
        senhaCadastro = (EditText) findViewById(R.id.senhaCadastro);
    }

    public void cadastrarUsuario(View v) {
        if (TextUtils.isEmpty(loginCadastro.getText().toString())) {
            loginCadastro.setError("Informe o login");
            return;
        } else if (TextUtils.isEmpty(senhaCadastro.getText().toString())) {
            senhaCadastro.setError("Informe a senha");
            return;
        } else {
            NovaContaActivity.AsyncCallWSCadastraUsuario task = new NovaContaActivity.AsyncCallWSCadastraUsuario();
            task.execute();
        }
    }

    private class AsyncCallWSCadastraUsuario extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... params) {
            getResposta();
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            pd.dismiss();
            Toast.makeText(NovaContaActivity.this, retorno, Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(NovaContaActivity.this, LoginActivity.class);
            NovaContaActivity.this.startActivity(intent);
        }

        @Override
        protected void onPreExecute() {
            pd = new ProgressDialog(NovaContaActivity.this);
            pd.setMessage("Cadastrando conta");
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
            login.setValue(loginCadastro.getText().toString());
            login.setName("login");
            login.setType((String.class));
            request.addProperty(login);

            PropertyInfo senha = new PropertyInfo();
            senha.type = PropertyInfo.STRING_CLASS;
            senha.setValue(senhaCadastro.getText().toString());
            senha.setName("senha");
            senha.setType((String.class));
            request.addProperty(senha);

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
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