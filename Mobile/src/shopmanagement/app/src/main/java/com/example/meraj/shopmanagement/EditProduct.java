package com.example.meraj.shopmanagement;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
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

public class EditProduct extends AppCompatActivity {

    EditText ETpname,ETquantity,ETprice,Etsell;
    Button save;
    String pname,quantity,price,sell,email,pname2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_product_layout);

        email=getIntent().getExtras().getString("email");
        pname=getIntent().getExtras().getString("name");
        quantity=getIntent().getExtras().getString("quantity");
        price=getIntent().getExtras().getString("price");

        ETpname=(EditText)findViewById(R.id.pname);
        ETquantity=(EditText)findViewById(R.id.quantity);
        ETprice=(EditText)findViewById(R.id.price);
        save=(Button)findViewById(R.id.save);

        ETpname.setText(pname);
        ETquantity.setText(quantity);
        ETprice.setText(price);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pname2=ETpname.getText().toString();
                quantity=ETquantity.getText().toString();
                price=ETprice.getText().toString();
                new Backgroundtask().execute();
            }
        });
    }
    public class Backgroundtask extends AsyncTask<Void,Void,String>
    {
        String reg_url;
        protected void onPreExecute() {
            reg_url="http://"+getString(R.string.ip)+"/project/product.php";
        }

        @Override
        protected String doInBackground(Void... params) {
            try
            {
                URL url=new URL(reg_url);
                HttpURLConnection httpURLConnection=(HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                OutputStream os=httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter=new BufferedWriter(new OutputStreamWriter(os,"UTF-8"));
                String data= URLEncoder.encode("pnamed","UTF-8")+"="+URLEncoder.encode(pname,"UTF-8")
                        +"&"+ URLEncoder.encode("quantity","UTF-8")+"="+URLEncoder.encode(quantity,"UTF-8")
                        +"&"+ URLEncoder.encode("price","UTF-8")+"="+URLEncoder.encode(price,"UTF-8")
                        +"&"+ URLEncoder.encode("email","UTF-8")+"="+URLEncoder.encode(email,"UTF-8")
                        +"&"+ URLEncoder.encode("type","UTF-8")+"="+URLEncoder.encode("E","UTF-8")
                        +"&"+ URLEncoder.encode("prev_name","UTF-8")+"="+URLEncoder.encode(pname2,"UTF-8");
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
            return null;
        }
        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }


        @Override
        protected void onPostExecute(String result) {
            Toast.makeText(EditProduct.this,result,Toast.LENGTH_LONG).show();
            finish();
        }
    }
}
