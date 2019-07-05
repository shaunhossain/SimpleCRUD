package com.shaunhossain.marma;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.security.PrivateKey;
import java.util.List;

public class CustomAdapter extends ArrayAdapter {

    private Activity context ;
    private List<Information> list;

    public CustomAdapter( Activity context,List<Information> list) {
        super(context,R.layout.sample_layout, list);
        this.context = context;
        this.list = list;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater layoutInflater = context.getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.sample_layout,null,true);
        Information  information = list.get(position);
        TextView code = view.findViewById(R.id.code);
        TextView brand = view.findViewById(R.id.barnd);
        TextView name = view.findViewById(R.id.name);
        TextView price = view.findViewById(R.id.price);
        TextView quantity = view.findViewById(R.id.quantity);



        code.setText("code :" +information.getCode());
        brand.setText("brand :" +information.getBrand());
        name.setText("name :" +information.getName());
        price.setText("price :" +information.getPrice());
        quantity.setText("quantity :" +information.getQuantity());

        return view;


    }
}
