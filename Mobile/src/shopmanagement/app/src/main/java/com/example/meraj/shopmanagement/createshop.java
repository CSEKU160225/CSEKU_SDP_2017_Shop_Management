package com.example.meraj.shopmanagement;

import android.app.Activity;
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

public class createshop extends Activity {

    EditText ETshopname,ETcategory;
    Button shop;
    String shopname,category,Email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shop);

        Email=getIntent().getExtras().getString("email");

        ETshopname=(EditText)findViewById(R.id.shopname);
        ETcategory=(EditText)findViewById(R.id.category);
        shop=(Button)findViewById(R.id.shop);
        shop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shopname=ETshopname.getText().toString();
                category=ETcategory.getText().toString();
                String method="createshop";
              new Backgroundtask().execute(method,shopname,category);
            }
        });
        }


           public class Backgroundtask extends AsyncTask<String,Void,String>
           {
               String reg_url;
               protected void onPreExecute() {
                   super.onPreExecute();
               }

               @Override
               protected String doInBackground(String... params) {
                   reg_url="http://"+getString(R.string.ip)+"/project/shop.php";
                   String method=params[0];
                   if(method.equals("createshop"))
                   {
                       String shopname=params[1];
                       String category=params[2];
                       try
                       {
                           URL url=new URL(reg_url);
                           HttpURLConnection httpURLConnection=(HttpURLConnection) url.openConnection();
                           httpURLConnection.setRequestMethod("POST");
                           httpURLConnection.setDoOutput(true);

                           OutputStream os=httpURLConnection.getOutputStream();

                           BufferedWriter bufferedWriter=new BufferedWriter(new OutputStreamWriter(os,"UTF-8"));

                           String data= URLEncoder.encode("Email","UTF-8")+"="+URLEncoder.encode(shopname,"UTF-8")+
                                   "&" + URLEncoder.encode("Password","UTF-8")+"="+URLEncoder.encode(category,"UTF-8")+
                                   "&" + URLEncoder.encode("email","UTF-8")+"="+URLEncoder.encode(Email,"UTF-8");
                           bufferedWriter.write(data);
                            bufferedWriter.flush();
                           bufferedWriter.close();
                           os.close();
                           InputStream is=httpURLConnection.getInputStream();
                           is.close();
                           return "shop creation success...";


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
                   Toast.makeText(createshop.this,result,Toast.LENGTH_LONG).show();
               }

           }
        }
