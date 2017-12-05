package com.example.meraj.shopmanagement;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
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
import java.util.Calendar;

public class History extends AppCompatActivity {

    String email;
    ArrayList<Product> addproduct;

    ListView listview;
    ProductAdapter productadapter;

    String datetime;

    private TextView startDateDisplay;
    private Button startPickDate;
    private Calendar startDate;
    static final int DATE_DIALOG_ID = 0;

    private TextView activeDateDisplay;
    private Calendar activeDate;

    private ProgressDialog progressDialog;
    Boolean dateselect=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        progressDialog=new ProgressDialog(this);
        progressDialog.setCancelable(false);

        email=getIntent().getExtras().getString("email");

        addproduct=new ArrayList<>();
        new AllproductinHistory().execute(email);

        listview=(ListView)findViewById(R.id.listproduct);
        productadapter=new ProductAdapter(this,R.layout.activity_history,addproduct);
        listview.setAdapter(productadapter);

        startDateDisplay = (TextView) findViewById(R.id.date);
        startPickDate = (Button) findViewById(R.id.dateselect);

        startDate = Calendar.getInstance();

        /* add a click listener to the button   */
        startPickDate.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Toast.makeText(History.this, datetime, Toast.LENGTH_SHORT).show();
                showDateDialog(startDateDisplay, startDate);
            }
        });
        updateDisplay(startDateDisplay, startDate);

    }

    private void updateDisplay(TextView dateDisplay, Calendar date) {
        datetime=date.get(Calendar.YEAR)+"-"+(date.get(Calendar.MONTH)+1)+"-"+date.get(Calendar.DAY_OF_MONTH);
        dateDisplay.setText(datetime);
        if(dateselect) {
            progressDialog.setMessage("Loading.Please Wait....");
            progressDialog.show();
            new BackgroundtaskOrderlist().execute(datetime);
        }
        dateselect=true;
    }

    public void showDateDialog(TextView dateDisplay, Calendar date) {
        activeDateDisplay = dateDisplay;
        activeDate = date;
        showDialog(DATE_DIALOG_ID);
    }

    private DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
        @Override

        public void onDateSet(android.widget.DatePicker view, int year,int monthOfYear, int dayOfMonth)
        {
            activeDate.set(Calendar.YEAR, year);
            activeDate.set(Calendar.MONTH, monthOfYear);
            activeDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateDisplay(activeDateDisplay, activeDate);
            unregisterDateDisplay();
        }
    };

    private void unregisterDateDisplay() {
        activeDateDisplay = null;
        activeDate = null;
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DATE_DIALOG_ID:
                return new DatePickerDialog(this, dateSetListener, activeDate.get(Calendar.YEAR), activeDate.get(Calendar.MONTH), activeDate.get(Calendar.DAY_OF_MONTH));
        }
        return null;
    }

    @Override
    protected void onPrepareDialog(int id, Dialog dialog) {
        super.onPrepareDialog(id, dialog);
        switch (id) {
            case DATE_DIALOG_ID:
                ((DatePickerDialog) dialog).updateDate(activeDate.get(Calendar.YEAR), activeDate.get(Calendar.MONTH), activeDate.get(Calendar.DAY_OF_MONTH));
                break;
        }
    }

    public class AllproductinHistory extends AsyncTask<String,Void,String> {
        String reg_url;

        @Override
        protected void onPreExecute() {
            reg_url= "http://"+getString(R.string.ip)+"/project/selllist.php";
        }

        @Override
        protected String doInBackground(String... params) {
            String Email=params[0];
            try {
                URL url=new URL(reg_url);
                HttpURLConnection httpURLConnection=(HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                OutputStream os=httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter=new BufferedWriter(new OutputStreamWriter(os,"UTF-8"));
                String data= URLEncoder.encode("Email","UTF-8")+"="+URLEncoder.encode(Email,"UTF-8");
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

                JSONObject jsonObject=new JSONObject(result);
                JSONArray jsonArray=jsonObject.getJSONArray("Server_response");
                int count=0;
                String name,quantity,price,date;
                while(count<jsonArray.length())
                {
                    JSONObject jo=jsonArray.getJSONObject(count);
                    name=jo.getString("product");
                    quantity=jo.getString("quantity");
                    price=jo.getString("price");
                    date=jo.getString("date");

                    Product product=new Product(name,quantity,price,date);
                    addproduct.add(product);
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
            return null;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(String result) {
            //  Toast.makeText(Showing_All_Product.this,result+email, Toast.LENGTH_SHORT).show();
            productadapter.notifyDataSetChanged();
        }
    }

    public class ProductAdapter extends ArrayAdapter {

        ArrayList<Product> list=new ArrayList<>();

        public ProductAdapter(Context context, int resource, ArrayList<Product> lis) {
            super(context, resource);
            list=lis;
        }

        @Override
        public void add(Object object) {
            super.add(object);
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Nullable
        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @NonNull
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View productView;
            productView=convertView;
            productHolder productHolder;
            if(productView==null)
            {
                LayoutInflater layoutInflater=(LayoutInflater)this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                productView=layoutInflater.inflate(R.layout.history_layout,parent,false);

                productHolder=new productHolder();

                productHolder.name=(TextView)productView.findViewById(R.id.name);
                productHolder.quantity=(TextView)productView.findViewById(R.id.quantity);
                productHolder.price=(TextView)productView.findViewById(R.id.price);
                productHolder.date=(TextView)productView.findViewById(R.id.date);

                productView.setTag(productHolder);
            }
            else
                productHolder=(productHolder)productView.getTag();

            final Product product=(Product)this.getItem(position);

            productHolder.name.setText(product.getPname());
            productHolder.quantity.setText("Quantity: "+product.getPquantity());
            productHolder.price.setText("Price: "+product.getPprice());
            productHolder.date.setText("Date: "+product.getDate());

            return productView;
        }

        class productHolder{
            TextView name,quantity,price,date;
        }
    }

    public class Product{
        String pname,pquantity,pprice,date;

        public Product(String pname, String pquantity, String pprice, String date) {
            this.pname = pname;
            this.pquantity = pquantity;
            this.pprice = pprice;
            this.date = date;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String getPname() {
            return pname;
        }

        public void setPname(String pname) {
            this.pname = pname;
        }

        public String getPquantity() {
            return pquantity;
        }

        public void setPquantity(String pquantity) {
            this.pquantity = pquantity;
        }

        public String getPprice() {
            return pprice;
        }

        public void setPprice(String pprice) {
            this.pprice = pprice;
        }
    }

    public class BackgroundtaskOrderlist extends AsyncTask<String,Void,Boolean>
    {

        String json_url,resu;
        String JSON_STRING;

        @Override
        protected void onPreExecute() {
            json_url="http://"+getString(R.string.ip)+"/project/selllistdate.php";
        }

        @Override
        protected Boolean doInBackground(String... params) {
            try {
                String date1=params[0];
                URL url=new URL(json_url);
                HttpURLConnection httpURLConnection=(HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputstream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedwritter = new BufferedWriter(new OutputStreamWriter(outputstream, "UTF-8"));
                String postdata = URLEncoder.encode("date", "UTF-8") + "=" + URLEncoder.encode(date1, "UTF-8") + "&" +
                        URLEncoder.encode("Email", "UTF-8") + "=" + URLEncoder.encode(email, "UTF-8");
                bufferedwritter.write(postdata);
                bufferedwritter.flush();
                bufferedwritter.close();
                outputstream.close();
                InputStream inputStream=httpURLConnection.getInputStream();
                BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder stringBuilder=new StringBuilder();
                while((JSON_STRING=bufferedReader.readLine())!=null){
                    stringBuilder.append(JSON_STRING+"\n");
                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                resu=stringBuilder.toString().trim();
                return true;

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return false;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(Boolean result) {
            progressDialog.cancel();
            if (result) {
                addproduct.clear();
                // Toast.makeText(FullPaid.this, result, Toast.LENGTH_SHORT).show();
                listview=(ListView)findViewById(R.id.listproduct);
                productadapter=new ProductAdapter(History.this,R.layout.activity_history,addproduct);
                listview.setAdapter(productadapter);
                try {
                    JSONObject jsonObject=new JSONObject(resu);
                    JSONArray jsonArray=jsonObject.getJSONArray("Server_response");
                    int count=0;
                    String name,quantity,price,date;
                    while(count<jsonArray.length())
                    {
                        JSONObject jo=jsonArray.getJSONObject(count);
                        name=jo.getString("product");
                        quantity=jo.getString("quantity");
                        price=jo.getString("price");
                        date=jo.getString("date");

                        Product product=new Product(name,quantity,price,date);
                        addproduct.add(product);
                        count++;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            else
                Toast.makeText(History.this, "Failed", Toast.LENGTH_SHORT).show();
        }
    }
}
