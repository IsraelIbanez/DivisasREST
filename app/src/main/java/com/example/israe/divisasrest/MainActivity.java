package com.example.israe.divisasrest;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.w3c.dom.Text;

import java.io.IOException;

public class MainActivity extends Activity {
    Spinner spDivisas;
    EditText cantidad;
    TextView resultado;
    String esc = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.spDivisas= (Spinner) findViewById(R.id.spDivisas);
        this.cantidad = (EditText) findViewById(R.id.txtCantidad);
        this.resultado = (TextView) findViewById(R.id.lblResultado);
        loadSpinnerDivisas();
    }

    public void loadSpinnerDivisas()
    {
        try {
            spDivisas.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l){
                    if(i!= 0) {
                        esc = "" + i;
                    }else
                        esc = "";
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {
                    esc = "";
                }
            });
        }catch(Exception e)
        {
            Log.i("Error",e.getMessage());
        }
    }

    private class ConvertirWSREST extends AsyncTask<String,Void,String>{


        @Override

        protected  String doInBackground(String... strings) {
            String result="";
            HttpClient cliente=new DefaultHttpClient();
            HttpGet peticion=new HttpGet("http://192.168.1.6:8080/WSREST/app/conversor/cantidad="+strings[0]+"&tipo="+strings[1]);
            peticion.setHeader("content-type","text/plain");
            try {
                HttpResponse res=cliente.execute(peticion);
                result= EntityUtils.toString(res.getEntity());

            } catch (IOException e) {
                Log.i("Error",e.getMessage());
            }
            return result;
        }

        @Override
        protected void onPostExecute(String s) {

            resultado.setText("Resultado "+ s.toString());
        }
    }

    public void convertir(View v){
        if(cantidad.getText().toString().equals("")){
            Toast.makeText(getApplicationContext(),"Elige la cantidad.",Toast.LENGTH_LONG).show();
        }else{
            if(esc.equals("")){
                Toast.makeText(getApplicationContext(),"Elige una divisa.",Toast.LENGTH_LONG).show();
            }else{
                new ConvertirWSREST().execute(cantidad.getText().toString(),esc);
            }
        }
    }
}
