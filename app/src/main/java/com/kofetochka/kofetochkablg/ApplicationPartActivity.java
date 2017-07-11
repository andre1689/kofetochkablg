package com.kofetochka.kofetochkablg;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.kofetochka.inquiry.InquiryAdd;
import com.kofetochka.inquiry.InquiryGetOneRes;

import java.util.ArrayList;
import java.util.HashMap;

public class ApplicationPartActivity extends AppCompatActivity{

    String ID_AP;
    ListView lv_applicationpart;
    String[] array_applicationpart;

    InquiryGetOneRes inquiryGetOneRes;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.applictionpart_layout);

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        if (toolbar != null){
            setSupportActionBar (toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        lv_applicationpart = (ListView) findViewById(R.id.listView_ApplicationPart);

        ID_AP = getIntent().getStringExtra("ID_AP");
        String ID_Drink = getOneRes("SELECT ID_Drink FROM Application_Part WHERE ID_AP='"+ID_AP+"'","ID_Drink");
        String Sum_AP = getOneRes("SELECT Sum_AP FROM Application_Part WHERE ID_AP='"+ID_AP+"'","Sum_AP");
        String Name_Drink = getOneRes("SELECT Name_Drink FROM Drink WHERE ID_Drink='"+ID_Drink+"'","Name_Drink");
        String Volume_Drink = getOneRes("SELECT Volume_Drink FROM Drink WHERE ID_Drink='"+ID_Drink+"'","Volume_Drink");
        Toast.makeText(this, Name_Drink+ " " + Volume_Drink, Toast.LENGTH_SHORT).show();
        ArrayList<HashMap<String, String>> arrayApplicationPart = new ArrayList<HashMap<String, String>>();
        HashMap<String, String> map;

        map = new HashMap <String, String>();
        map.put("NameDrink", Name_Drink+" "+Volume_Drink+" мл.");
        map.put("Additives", "Нет");
        map.put("Price", Sum_AP+" руб.");
        arrayApplicationPart.add(map);

        SimpleAdapter adapter = new SimpleAdapter(this, arrayApplicationPart,
                R.layout.list_applicationpart, new String[] { "NameDrink", "Additives",
                "Price" }, new int[] { R.id.NameDrink, R.id.Additives,
                R.id.Price});
        lv_applicationpart.setAdapter(adapter);
    }

    private String getOneRes(String quiry, String column) {
        String Column = column;
        String Quiry = quiry;
        String Res;
        inquiryGetOneRes = new InquiryGetOneRes();
        inquiryGetOneRes.start(Quiry,Column);
        try {
            inquiryGetOneRes.join();
        } catch (InterruptedException e) {
            Log.e("GetOneRes",e.getMessage());
        }
        return Res = inquiryGetOneRes.res();
    }
}
