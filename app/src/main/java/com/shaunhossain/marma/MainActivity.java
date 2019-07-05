package com.shaunhossain.marma;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private ListView listViewId;
    DatabaseReference databaseReference;
    List<Information> list;
    CustomAdapter customAdapter;

    //initial firebase user to call current user.
    FirebaseUser user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        listViewId = findViewById(R.id.listViewId);
        list = new ArrayList<>();
        customAdapter = new CustomAdapter(this, list);
        user = FirebaseAuth.getInstance().getCurrentUser();

        // call each current UserID to retrieve all note of it.
        databaseReference = FirebaseDatabase.getInstance().getReference().child(user.getUid());

        listViewId.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                Information  information = list.get(i);
                showUpdateDeleteDialog(information.getCode(), information.getName(),information.getBrand(),information.getPrice(),information.getQuantity());
                return true;
            }
        });



        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(view.getContext(), add_product.class));
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }


    private void showUpdateDeleteDialog(final String code, final String name , final String brand , final String price , final String quantity) {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.modify_product, null);
        dialogBuilder.setView(dialogView);

        final EditText edit_Code = (EditText) dialogView.findViewById(R.id.edit_code);
        final EditText edit_Name = (EditText) dialogView.findViewById(R.id.edit_name);
        final EditText edit_Brand = (EditText) dialogView.findViewById(R.id.edit_brand);
        final EditText edit_Price = (EditText) dialogView.findViewById(R.id.edit_price);
        final EditText edit_Quantity = (EditText) dialogView.findViewById(R.id.edit_quantity);
        final Button Update_button = (Button) dialogView.findViewById(R.id.update_button);
        final Button Delete_button = (Button) dialogView.findViewById(R.id.delete_button);

        dialogBuilder.setTitle(code);
        final AlertDialog b = dialogBuilder.create();
        b.show();


        Update_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String code = edit_Code.getText().toString().trim();
                String name = edit_Name.getText().toString().trim();
                String brand = edit_Brand.getText().toString().trim();
                String price = edit_Price.getText().toString().trim();
                String quantity = edit_Quantity.getText().toString().trim();
                if (!TextUtils.isEmpty(name)) {
                    UpdateProduct(code, name ,brand ,price ,quantity);
                    b.dismiss();
                }
            }
        });


        Delete_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                deleteProduct(code, name ,brand ,price ,quantity);
                b.dismiss();
            }
        });
    }

    private boolean UpdateProduct(String code, String name , String brand , String price , String quantity) {
        //getting the specified artist reference
        DatabaseReference dR = FirebaseDatabase.getInstance().getReference().child(code);

        //updating artist
        Information information = new Information(code, name ,brand ,price ,quantity);
        dR.setValue(information);
        Toast.makeText(getApplicationContext(), "Product Updated", Toast.LENGTH_LONG).show();
        return true;
    }

    private boolean deleteProduct(String code, String name , String brand , String price , String quantity) {
        //getting the specified artist reference
        DatabaseReference dR = FirebaseDatabase.getInstance().getReference().child(code);

        //removing artist
        Information information = new Information(code, name ,brand ,price ,quantity);
        dR.removeValue((DatabaseReference.CompletionListener) information);

        //getting the tracks reference for the specified artist
        //DatabaseReference drTracks = FirebaseDatabase.getInstance().getReference("tracks").child(id);

        //removing all tracks
        //drTracks.removeValue();
        Toast.makeText(getApplicationContext(), "product Deleted", Toast.LENGTH_LONG).show();

        return true;
    }





    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onStart() {

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list.clear();
                for (DataSnapshot d : dataSnapshot.getChildren()) {
                    Information information = d.getValue(Information.class);

                    list.add(information);

                }
                listViewId.setAdapter(customAdapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        super.onStart();
    }

}
