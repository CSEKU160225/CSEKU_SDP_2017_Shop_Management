package com.example.meraj.shopmanagement;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
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
import java.util.ArrayList;
import java.util.Locale;

public class Showing_All_Product extends AppCompatActivity {

    String email;

    ListView listview;
    ProductAdapter productadapter;

    ArrayList<Product> addproduct;
    ArrayList<Product> addproduct2;
    EditText search;

    String name,quantity,price;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.showing_all_product_layout);
        email=getIntent().getExtras().getString("email");

        addproduct=new ArrayList<>();
        new Backgroundtask().execute(email);

        listview=(ListView)findViewById(R.id.listproduct);
        productadapter=new ProductAdapter(this,R.layout.showing_all_product_list,addproduct);
        listview.setAdapter(productadapter);

        search=(EditText) findViewById(R.id.searchView);
        search.addTextChangedListener(new TextWatcher() {
            @Override
               public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String newText = search.getText().toString().toLowerCase(Locale.getDefault());
                if (newText != null && !newText.isEmpty()) {

                    addproduct2 = new ArrayList<Product>();
                    for (Product item : addproduct) {
                        Log.d(item.toString(), newText);
                        if (item.getPname().toLowerCase().contains(newText.toString().toLowerCase())) {
                            addproduct2.add(item);
                        }
                    }
                    productadapter = new ProductAdapter(Showing_All_Product.this, R.layout.showing_all_product_list, addproduct2);
                    //seeting adapter in the listview
                    listview.setAdapter(productadapter);
                } else if (newText == null || newText.isEmpty()) {
                    productadapter = new ProductAdapter(Showing_All_Product.this, R.layout.showing_all_product_list, addproduct);
                    //seeting adapter in the listview
                    listview.setAdapter(productadapter);
                }
            }
        });

}

    public class ProductAdapter extends ArrayAdapter{

        ArrayList<Product> list=new ArrayList<>();

        public ProductAdapter(Context context, int resource,ArrayList<Product> lis) {
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
                productView=layoutInflater.inflate(R.layout.showing_all_product_list,parent,false);

                productHolder=new productHolder();

                productHolder.name=(TextView)productView.findViewById(R.id.name);
                productHolder.quantity=(TextView)productView.findViewById(R.id.quantity);
                productHolder.price=(TextView)productView.findViewById(R.id.price);

                productView.setTag(productHolder);
            }
            else
                productHolder=(productHolder)productView.getTag();

            final Product product=(Product)this.getItem(position);

            productHolder.name.setText(product.getPname());
            productHolder.quantity.setText("Quantity: "+product.getPquantity());
            productHolder.price.setText("Price: "+product.getPprice());

            productView.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
                @Override
                public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
                    name=product.getPname();
                    quantity=product.getPquantity();
                    price=product.getPprice();

                    MenuInflater menuInflater=getMenuInflater();
                    menuInflater.inflate(R.menu.product_edit_delete,menu);
                }
            });

//            productView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    AlertDialog.Builder alert=new AlertDialog.Builder(Showing_All_Product.this);
//                    alert.setTitle("Info");
//                    alert.setCancelable(false);
//                    price=product.getPprice();
//                    sell=product.getPsell();
//                    int value=Integer.parseInt(sell)-Integer.parseInt(price);
//                    String value1;
//                    if(value>=0) {
//                        value1 = Integer.toString(value);
//                        alert.setMessage("Profit = " + value1 + " Taka");
//                    }
//                    else
//                    {
//                        value1 = Integer.toString(value*-1);
//                        alert.setMessage("Loss = " + value1 + " Taka");
//                    }
//                    alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            dialog.cancel();
//                        }
//                    });
//                    AlertDialog alertDialog=alert.create();
//                    alertDialog.show();
//                }
//            });

            return productView;
        }

        class productHolder{
            TextView name,quantity,price;
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch(item.getItemId())
        {
            case R.id.edit:
                Intent intent=new Intent(Showing_All_Product.this,EditProduct.class );
                intent.putExtra("email",email);
                intent.putExtra("name",name);
                intent.putExtra("quantity",quantity);
                intent.putExtra("price",price);
                startActivity(intent);
            default:
                return super.onContextItemSelected(item);
        }
    }

    public class Backgroundtask extends AsyncTask<String,Void,String> {
        String reg_url;

        @Override
        protected void onPreExecute() {
            reg_url= "http://"+getString(R.string.ip)+"/project/ProductList.php";
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
                    String name,quantity,price;
                    while(count<jsonArray.length())
                    {
                        JSONObject jo=jsonArray.getJSONObject(count);
                        name=jo.getString("product");
                        quantity=jo.getString("quantity");
                        price=jo.getString("price");

                        Product product=new Product(name,quantity,price);
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

    public class Product{
        String pname,pquantity,pprice;

        public Product(String pname, String pquantity, String pprice) {
            this.pname = pname;
            this.pquantity = pquantity;
            this.pprice = pprice;
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
}
