package com.example.meraj.shopmanagement;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
import java.text.DecimalFormat;

public class CreditDetails extends AppCompatActivity {

    TextView textView,SellIn;
    private String pname,source;
    private double Dquantity,DPrice;
    private double quantity,price,profit,buyrate=0;
    ProgressDialog progressDialog;
    String s2="total profit = ";
    String s="Profit : ";
    double taotalprofit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.credits_details);

        progressDialog=new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Please Wait.....");


        final Intent i=getIntent();
        Bundle b=i.getExtras();
        pname=b.getString("PName");
        source=b.getString("Source");
        quantity=b.getDouble("Quantity");
        price=b.getDouble("Price");
        buyrate=b.getDouble("Profit");

        textView=(TextView) findViewById(R.id.textView);
        SellIn=(TextView) findViewById(R.id.SellInform);
        DecimalFormat df = new DecimalFormat("0.00");

        textView.setText(source+" Information\n\n"+"Product Name : "+pname+"\nQuantity :"+df.format(quantity)+"\nPrice : "+df.format(price)+"\nTotal Price: "+df.format(price*quantity));

        //buyrate wil get from database
        profit=quantity*(price-buyrate);
        if(buyrate>price)
        {
            s="Loss : ";
            profit*=-1;
        }
        new database().execute();


    }

    public class database extends AsyncTask<Void, Void, String> {
        String reg_url;
        @Override
        protected void onPreExecute() {
            reg_url = "http://" + getString(R.string.ip) + "/project/sellDetails.php";
        }
        @Override
        protected String doInBackground(Void... params) {
            try {
                URL url=new URL(reg_url);
                HttpURLConnection httpURLConnection=(HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                OutputStream os=httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter=new BufferedWriter(new OutputStreamWriter(os,"UTF-8"));
                String data= URLEncoder.encode("pnamed","UTF-8")+"="+URLEncoder.encode(pname,"UTF-8");
                bufferedWriter.write(data);
                bufferedWriter.flush();
                bufferedWriter.close();
                os.close();
                InputStream input = httpURLConnection.getInputStream();
                BufferedReader buffer = new BufferedReader(new InputStreamReader(input, "iso-8859-1"));
                String line;
                String result = "";
                while ((line = buffer.readLine()) != null) {
                    result += line;
                }
                buffer.close();
                input.close();
                httpURLConnection.disconnect();

                JSONObject jsonObject = new JSONObject(result);
                JSONArray jsonArray = jsonObject.getJSONArray("Server_response");
                int count = 0;
                JSONObject jo = jsonArray.getJSONObject(count);
                Dquantity= jo.getDouble("quantity");
                DPrice= jo.getDouble("price");
                return result;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            catch (Exception e)
            {

            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(String result) {
            //Toast.makeText(SellProduct.this, result, Toast.LENGTH_SHORT).show();
            if(Dquantity*buyrate>DPrice)
            {

                taotalprofit=Dquantity*buyrate-DPrice;
            }
            else
            {
                s2="total loss = ";
                taotalprofit=DPrice-Dquantity*buyrate;
            }
            if(source.equals("Sell"))
            {
                //progressDialog.show();
                SellIn.setVisibility(View.VISIBLE);

                DecimalFormat df = new DecimalFormat("0.00");
                SellIn.setText(s+df.format(profit)+"\nToday this product Sold\n"+"Quantity : "+Dquantity+"\nTotal_sell_price= "+DPrice
                        +"\n"+s2+taotalprofit);
            }
            progressDialog.cancel();
        }
    }
}
