package com.shaunhossain.marma;

import android.app.Dialog;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class add_product extends AppCompatActivity {

    DatabaseReference databaseReference;
    FirebaseAuth mAuth;
    FirebaseUser user;

    Button mAddproduct;
    EditText mCode,mName,mBrand,mPrice,mQuantity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ;
        mAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();

        mCode = (EditText) findViewById(R.id.item_code);
        mName = (EditText) findViewById(R.id.item_name);
        mBrand = (EditText) findViewById(R.id.item_brand);
        mPrice = (EditText) findViewById(R.id.item_price);
        mQuantity = (EditText) findViewById(R.id.item_quantity);
        mAddproduct = (Button) findViewById(R.id.add_button);
        mAddproduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addproduct();
            }
        });


    }





    private void addproduct() {

        String code = mCode.getText().toString().trim();
        String name = mName.getText().toString().trim();
        String brand = mBrand.getText().toString().trim();
        String price = mPrice.getText().toString().trim();
        String quantity = mQuantity.getText().toString().trim();

        String  key = databaseReference.push().getKey();
        user  = FirebaseAuth.getInstance().getCurrentUser();
        Information information = new Information(code,name,brand,price,quantity);


        databaseReference.child(user.getUid()).child(key).setValue(information);

        Toast.makeText(add_product.this,"data successfully saved",Toast.LENGTH_SHORT ).show();;
        mCode.setText("");
        mName.setText("");
        mBrand.setText("");
        mPrice.setText("");
        mQuantity.setText("");
    }

}
