package com.example.meraj.shopmanagement;

import android.content.Intent;
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

public class BuyProduct extends AppCompatActivity {

    EditText ETpname,ETquantity,ETprice;
    Button save;
    String pname,quantity,price,email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.buy_product);

        email=getIntent().getExtras().getString("email");

        ETpname=(EditText)findViewById(R.id.pname);
        ETquantity=(EditText)findViewById(R.id.quantity);
        ETprice=(EditText)findViewById(R.id.price);
        save=(Button)findViewById(R.id.save);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pname=ETpname.getText().toString();
                quantity=ETquantity.getText().toString();
                price=ETprice.getText().toString();
                new BuyProduct.Backgroundtask().execute();
                Intent CreditIntent=new Intent(BuyProduct.this,CreditDetails.class);
                Bundle bundle=new Bundle();
                bundle.putString("PName",pname);
                bundle.putDouble("Quantity", Double.parseDouble(quantity));
                bundle.putDouble("Price", Double.parseDouble(price));
                bundle.putDouble("Profit",0.0);
                bundle.putString("Source","Buy");
                CreditIntent.putExtras(bundle);
                startActivity(CreditIntent);

                //Toast.makeText(SellProduct.this, pname+quantity+price+sell, Toast.LENGTH_SHORT).show();
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
                        +"&"+ URLEncoder.encode("type","UTF-8")+"="+URLEncoder.encode("A","UTF-8")
                        +"&"+ URLEncoder.encode("prev_name","UTF-8")+"="+URLEncoder.encode(pname,"UTF-8");
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
            Toast.makeText(BuyProduct.this,result,Toast.LENGTH_LONG).show();
            finish();
        }
    }
}
