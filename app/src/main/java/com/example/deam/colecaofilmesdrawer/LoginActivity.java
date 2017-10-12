package com.example.deam.colecaofilmesdrawer;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

public class LoginActivity extends AppCompatActivity {

    EditText loginCadastro;
    EditText senhaCadastro;

    ProgressDialog pd;

    int retorno = 0;

    private final String NAMESPACE = "http://ws/";
    private final String URL = "http://10.0.2.2:8080/Banco/CadastraUsuario";
    private final String SOAP_ACTION = "http://10.0.2.2:8080/Banco/CadastraUsuario/verificaUsuario";
    private final String METHOD_NAME = "verificaUsuario";

    SharedPreferences shared;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginCadastro = (EditText) findViewById(R.id.loginCadastro);
        senhaCadastro = (EditText) findViewById(R.id.senhaCadastro);

        shared = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        try {
            Boolean teste = shared.getBoolean("primeiraVez", false);
            if (teste == false) {

            } else {
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                LoginActivity.this.startActivity(intent);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void novaConta(View v) {
        Intent intent = new Intent(LoginActivity.this, NovaContaActivity.class);
        LoginActivity.this.startActivity(intent);
    }

    public void entrar(View v) {

        try {
            LoginActivity.AsyncCallWSVerificaUsuario task = new LoginActivity.AsyncCallWSVerificaUsuario();
            task.execute();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private class AsyncCallWSVerificaUsuario extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... params) {
            getResposta();
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            if (retorno > 0) {
                pd.dismiss();
                shared.edit().putBoolean("primeiraVez", true).apply();
                shared.edit().putString("usuario", loginCadastro.getText().toString()).apply();
                Toast.makeText(LoginActivity.this, "Olá " + loginCadastro.getText().toString() + "!!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                LoginActivity.this.startActivity(intent);
            } else {
                pd.dismiss();
                Toast.makeText(LoginActivity.this, "Erro no login, tente novamente!!", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected void onPreExecute() {
            pd = new ProgressDialog(LoginActivity.this);
            pd.setMessage("Autenticando");
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
                retorno = Integer.parseInt(response.toString());

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}