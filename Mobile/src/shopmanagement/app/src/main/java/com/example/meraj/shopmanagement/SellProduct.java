package com.example.meraj.shopmanagement;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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
import java.util.ArrayList;


public class SellProduct extends Activity {

    Spinner spinner_item, spinne_quantity;

    EditText et_price;
    ArrayList<String> item;
    ArrayList<String> quantity;
    ArrayAdapter<String> arrayAdapter;
    ArrayAdapter arrayAdapter2;
    Button save;

    ProgressDialog progressDialog;
    String itemname,getquantity,getprice="0",setprice,email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sell_product);

        email=getIntent().getExtras().getString("email");

        progressDialog=new ProgressDialog(this);
        progressDialog.setCancelable(false);

        spinner_item = (Spinner) findViewById(R.id.spinneritem);
        spinne_quantity = (Spinner) findViewById(R.id.quantity);
        et_price = (EditText) findViewById(R.id.price);
        save=(Button)findViewById(R.id.save);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setprice=et_price.getText().toString();
                Intent CreditIntent=new Intent(SellProduct.this,CreditDetails.class);
                Bundle bundle=new Bundle();
                bundle.putString("PName",itemname);
                bundle.putDouble("Quantity", Double.parseDouble(getquantity));
                bundle.putDouble("Price", Double.parseDouble(setprice));
                bundle.putDouble("Profit", Double.parseDouble(getprice));
                bundle.putString("Source","Sell");
                CreditIntent.putExtras(bundle);
                startActivity(CreditIntent);
                new sell().execute();
            }
        });

        item = new ArrayList<>();
        quantity = new ArrayList<>();

        progressDialog.setMessage("Please Wait.....");
        progressDialog.show();
        new Backgroundtask().execute();

        arrayAdapter=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,item);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_item.setAdapter(arrayAdapter);

        spinner_item.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //progressDialog.show();
                itemname=spinner_item.getItemAtPosition(position).toString();
                Toast.makeText(SellProduct.this,"item name"+itemname, Toast.LENGTH_SHORT).show();
               new Quan_list().execute();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        //new Quan_list().execute();

        arrayAdapter2=new ArrayAdapter(this,android.R.layout.simple_spinner_item,quantity);
        arrayAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinne_quantity.setAdapter(arrayAdapter2);

        spinne_quantity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
               // progressDialog.show();
                TextView view2=(TextView)view;
                getquantity =(String)view2.getText();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    public class Backgroundtask extends AsyncTask<Void,Void,String> {
        String reg_url;

        @Override
        protected void onPreExecute() {
            reg_url= "http://"+getString(R.string.ip)+"/project/Item_Quan_List.php";
        }

        @Override
        protected String doInBackground(Void... params) {
            try {
                URL url=new URL(reg_url);
                HttpURLConnection httpURLConnection=(HttpURLConnection) url.openConnection();
                InputStream input=httpURLConnection.getInputStream();
                BufferedReader buffer=new BufferedReader(new InputStreamReader(input));
                String line="";
                String result="";
                while((line=buffer.readLine())!=null)
                {
                    result+=line;
                }
                buffer.close();
                input.close();
                httpURLConnection.disconnect();

                JSONObject jsonObject = new JSONObject(result);
                JSONArray jsonArray=jsonObject.getJSONArray("Server_response");
                int count=0;
                String name;
                while(count<jsonArray.length())
                {
                    JSONObject jo=jsonArray.getJSONObject(count);
                    name=jo.getString("product_name");
                    item.add(name);
                    count++;
                }
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
            progressDialog.cancel();
            arrayAdapter.notifyDataSetChanged();
          //Toast.makeText(SellProduct.this,result, Toast.LENGTH_SHORT).show();

        }
    }
    public class Quan_list extends AsyncTask<Void, Void, String> {
        String reg_url;

        @Override
        protected void onPreExecute() {
            reg_url = "http://" + getString(R.string.ip) + "/project/Item_reserve.php";
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
                String data= URLEncoder.encode("pnamed","UTF-8")+"="+URLEncoder.encode(itemname,"UTF-8");
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

                quantity.clear();

                JSONObject jsonObject = new JSONObject(result);
                JSONArray jsonArray = jsonObject.getJSONArray("Server_response");
                int count = 0;
                JSONObject jo = jsonArray.getJSONObject(count);
                String quan= jo.getString("quantity");
                int quant=Integer.parseInt(quan);
                for(int i=1;i<=quant;i++)
                {
                    quantity.add(Integer.toString(i));
                }
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
            arrayAdapter2.notifyDataSetChanged();
            //Toast.makeText(SellProduct.this, result, Toast.LENGTH_SHORT).show();
            progressDialog.cancel();
        }
    }

    public class Price extends AsyncTask<Void, Void, String> {
        String reg_url;

        @Override
        protected void onPreExecute() {
            reg_url = "http://" + getString(R.string.ip) + "/project/BuyItemPrice.php";
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
                String data= URLEncoder.encode("pnamed","UTF-8")+"="+URLEncoder.encode(itemname,"UTF-8");
                bufferedWriter.write(data);
                bufferedWriter.flush();
                bufferedWriter.close();
                os.close();
                InputStream input = httpURLConnection.getInputStream();
                BufferedReader buffer = new BufferedReader(new InputStreamReader(input, "iso-8859-1"));
                String line = "";
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
                getprice= jo.getString("price");

                return result;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
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
            progressDialog.cancel();
            //Toast.makeText(SellProduct.this,"buy price"+result, Toast.LENGTH_SHORT).show();
        }
    }
    //sell

    public class sell extends AsyncTask<Void, Void, String> {
        String reg_url;

        int quan=Integer.parseInt(getquantity);
        int price=Integer.parseInt(setprice);

        int price2=quan*price;

        String finalprice=Integer.toString(price2);

        @Override
        protected void onPreExecute() {
            reg_url = "http://" + getString(R.string.ip) + "/project/SellProduct.php";
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
                String data= URLEncoder.encode("pnamed","UTF-8")+"="+URLEncoder.encode(itemname,"UTF-8")
                        +"&"+ URLEncoder.encode("quantity","UTF-8")+"="+URLEncoder.encode(getquantity,"UTF-8")
                        +"&"+ URLEncoder.encode("price","UTF-8")+"="+URLEncoder.encode(finalprice,"UTF-8")
                        +"&"+ URLEncoder.encode("email","UTF-8")+"="+URLEncoder.encode(email,"UTF-8");
                bufferedWriter.write(data);
                bufferedWriter.flush();
                bufferedWriter.close();
                os.close();
                InputStream input = httpURLConnection.getInputStream();
                BufferedReader buffer = new BufferedReader(new InputStreamReader(input, "iso-8859-1"));
                String line = "";
                String result = "";
                while ((line = buffer.readLine()) != null) {
                    result += line;
                }
                buffer.close();
                input.close();
                httpURLConnection.disconnect();
                return result;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
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
            progressDialog.cancel();
            //Toast.makeText(SellProduct.this,"buy price"+result, Toast.LENGTH_SHORT).show();
        }
    }


}