package com.kofetochka.kofetochkablg;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.Toast;

import com.kofetochka.adapter.ApplicationPartListAdapter;
import com.kofetochka.dto.ApplicationPartDTO;
import com.kofetochka.inquiry.InquiryGetArrayOneColumn;
import com.kofetochka.inquiry.InquiryGetOneRes;

import java.util.ArrayList;
import java.util.List;

public class ApplicationPartActivity extends AppCompatActivity{

    String ID_AP;
    String ID_Drink;
    String Sum_AP;
    String Name_Drink;
    String Volume_Drink;
    String ID_Syrup;
    String Syrup;
    String Additives;
    String[] array_additives;
    int lenghtAdditives=0;

    InquiryGetOneRes inquiryGetOneRes;
    InquiryGetArrayOneColumn inquiryGetArrayOneColumn;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.applictionpart_layout);

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        if (toolbar != null){
            setSupportActionBar (toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        List<ApplicationPartDTO> myDataset = getDataSet();

        RecyclerView rv = (RecyclerView) findViewById(R.id.recyclerView);
        rv.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        rv.setLayoutManager(layoutManager);
        ApplicationPartListAdapter recyclerAdapter = new ApplicationPartListAdapter(myDataset);
        rv.setAdapter(recyclerAdapter);
    }

    private String[] getArrayOneColumn(String inquiry, String column){
        inquiryGetArrayOneColumn = new InquiryGetArrayOneColumn();
        inquiryGetArrayOneColumn.start(inquiry, column);
        try {
            inquiryGetArrayOneColumn.join();
        } catch (InterruptedException e) {
            Log.e("ArrayAP",e.getMessage());
        }
        lenghtAdditives = inquiryGetArrayOneColumn.resLenght();
        return inquiryGetArrayOneColumn.resColumn();
    }

    private String getOneRes(String inquiry, String column) {
        inquiryGetOneRes = new InquiryGetOneRes();
        inquiryGetOneRes.start(inquiry, column);
        try {
            inquiryGetOneRes.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return inquiryGetOneRes.res();
    }

    public List<ApplicationPartDTO> getDataSet() {
        ID_AP = getIntent().getStringExtra("ID_AP");
        ID_Drink = getOneRes("SELECT ID_Drink FROM Application_Part WHERE ID_AP='"+ID_AP+"'","ID_Drink");
        Sum_AP = getOneRes("SELECT Sum_AP FROM Application_Part WHERE ID_AP='"+ID_AP+"'","Sum_AP");
        Name_Drink = getOneRes("SELECT Name_Drink FROM Drink WHERE ID_Drink='"+ID_Drink+"'","Name_Drink");
        Volume_Drink = getOneRes("SELECT Volume_Drink FROM Drink WHERE ID_Drink='"+ID_Drink+"'","Volume_Drink");

        ID_Syrup = getOneRes("SELECT ID_Syrup FROM ApplicationPart_Syrup WHERE ID_AP='"+ID_AP+"'","ID_Syrup");
        Syrup = getOneRes("SELECT Name_Syrup FROM Syrup WHERE ID_Syrup='"+ID_Syrup+"'","Name_Syrup");
        if (Syrup!=null) {
            Syrup="Сироп:"+Syrup;
        }

        Additives = "Добавки:";
        array_additives = getArrayOneColumn("SELECT ID_Additives FROM ApplicationPart_Additives WHERE ID_AP='"+ID_AP+"'","ID_Additives");
        Toast.makeText(this, Integer.toString(lenghtAdditives), Toast.LENGTH_SHORT).show();
        if (lenghtAdditives>0){

            for(int i=0;i<lenghtAdditives;i++){
                Additives += getOneRes("SELECT Name_Additives FROM Additives WHERE ID_Additives='"+array_additives[i]+"'","Name_Additives");
                Additives += " ";
            }
        }
        List<ApplicationPartDTO> DataSet = new ArrayList();
        DataSet.add(new ApplicationPartDTO(Name_Drink+"  "+Volume_Drink+" мл.",Additives+Syrup,Sum_AP+" руб."));
        return DataSet;
    }
}
