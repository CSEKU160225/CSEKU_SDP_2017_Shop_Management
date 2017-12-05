package com.example.meraj.shopmanagement;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;


public class Register extends Activity {
    EditText ETEmail,ETPassword;
    Button register;
    String Email,Password;
    protected void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);
        ETEmail=(EditText)findViewById(R.id.New_Email);
        ETPassword=(EditText)findViewById(R.id.New_Password);
        register=(Button)findViewById(R.id.Register);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Email=ETEmail.getText().toString();
                Password=ETPassword.getText().toString();
                String method="Register";

                new Backgroundtask().execute(method);
            }
        });

    }
   /* public  void userReg(View view)
        {
            Intent intent=new Intent(Register.this,createshop.class);
            startActivity(intent);
            Email=ETEmail.getText().toString();
            Password=ETPassword.getText().toString();
            new Backgroundtask().execute("");
        }*/

    public class Backgroundtask extends AsyncTask<String,Void,String> {
        String reg_url;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {

            reg_url="http://"+getString(R.string.ip)+"/project/register.php";

            String method=params[0];
            if(method.equals("Register"))
            {
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
                    InputStream is=httpURLConnection.getInputStream();
                    is.close();
                    return "true";
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
            if(result.equals("true")) {
                Intent intent = new Intent(Register.this, createshop.class);
                intent.putExtra("email",Email);
                startActivity(intent);
            }
            else
                Toast.makeText(Register.this, "Cant connect to the database", Toast.LENGTH_SHORT).show();
        }


    }
}

