package com.example.meraj.shopmanagement;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

public class MainActivity extends AppCompatActivity {

    EditText email,password;
    String emai;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        email=(EditText)findViewById(R.id.Email);
        password=(EditText)findViewById(R.id.Password);
        //email.setText("Mehadi");
        //password.setText("88");
    }
    public  void Reg(View view)
    {
        Intent intent=new Intent(MainActivity.this,Register.class);
        startActivity(intent);
    }

    public void userLogin(View view)
    {
        emai=email.getText().toString();
        String pass=password.getText().toString();
        new Backgroundtask().execute("Login",emai,pass);


        //Toast.makeText(this, emai+pass, Toast.LENGTH_SHORT).show();
    }

    public class Backgroundtask extends AsyncTask<String,Void,String> {
        String reg_url;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {

            reg_url= "http://"+getString(R.string.ip)+"/project/login.php";

                    String method=params[0];
            if(method.equals("Login"))
            {
                String Email=params[1];
                 String Password=params[2];
                try {
                    URL url=new URL(reg_url);
                    HttpURLConnection httpURLConnection=(HttpURLConnection) url.openConnection();
                    httpURLConnection.setRequestMethod("POST");
                    httpURLConnection.setDoOutput(true);
                    OutputStream os=httpURLConnection.getOutputStream();
                    BufferedWriter bufferedWriter=new BufferedWriter(new OutputStreamWriter(os,"UTF-8"));
                    String data= URLEncoder.encode("Email","UTF-8")+"="+URLEncoder.encode(Email,"UTF-8")+"&"
                            + URLEncoder.encode("Password","UTF-8")+"="+URLEncoder.encode(Password,"UTF-8");

                    bufferedWriter.write(data);
                    bufferedWriter.flush();
                    bufferedWriter.close();
                    os.close();
                    InputStream input=httpURLConnection.getInputStream();
                    BufferedReader buffer=new BufferedReader(new InputStreamReader(input,"iso-8859-1"));
                    String line="";
                    String result="";
                    while((line=buffer.readLine())!=null)
                    {
                        result+=line;
                    }
                    buffer.close();
                    input.close();
                    httpURLConnection.disconnect();
                    return result;
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(String result) {
            Intent intent = new Intent(MainActivity.this, product1.class);
            intent.putExtra("email",emai);
            startActivity(intent);
                Toast.makeText(MainActivity.this, result, Toast.LENGTH_LONG).show();
        }
    }
}

