package com.example.meraj.shopmanagement;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class product1 extends AppCompatActivity {

    String email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product1);

        email=getIntent().getExtras().getString("email");
    }
    public void buyproduct(View view)
    {
        Intent intent=new Intent(product1.this,BuyProduct.class);
        intent.putExtra("email",email);
        startActivity(intent);
    }
    public void product(View view)
    {
        Intent intent=new Intent(product1.this,Showing_All_Product.class);
        intent.putExtra("email",email);
        startActivity(intent);
    }
    public void sellproduct(View view)
    {
        Intent intent=new Intent(product1.this,SellProduct.class);
        intent.putExtra("email",email);
        startActivity(intent);
    }
    public void history(View view)
    {
        Intent intent=new Intent(product1.this,History.class);
        intent.putExtra("email",email);
        startActivity(intent);
    }
}

