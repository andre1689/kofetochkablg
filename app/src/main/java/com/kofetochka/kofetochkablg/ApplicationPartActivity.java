package com.kofetochka.kofetochkablg;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.kofetochka.adapter.ApplicationPartListAdapter;
import com.kofetochka.dto.ApplicationPartDTO;
import com.kofetochka.inquiry.InquiryGetArrayOneColumn;
import com.kofetochka.inquiry.InquiryGetOneRes;

import java.util.ArrayList;
import java.util.List;

public class ApplicationPartActivity extends AppCompatActivity{

    String ID_Application;
    String[] array_additives;
    String[] array_ID_AP;
    String Login;
    int lenghtAdditives=0;
    int lenghtAP=0;
    FloatingActionButton floatingActionButtonDrink, floatingActionButtonCoffee;
    List<ApplicationPartDTO> DataSet;
    ApplicationPartListAdapter recyclerAdapter;

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

        ID_Application = getIntent().getStringExtra("ID_Application");
        Login = getIntent().getStringExtra("Login");
        array_ID_AP = getArrayOneColumn("SELECT ID_AP FROM Application_Part WHERE ID_Application='"+ID_Application+"'","ID_AP");
        lenghtAP = array_ID_AP.length;

        floatingActionButtonDrink = (FloatingActionButton) findViewById(R.id.action_drink);
        floatingActionButtonDrink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ApplicationPartActivity.this, NewOrderActivity.class);
                intent.putExtra("Login",Login);
                intent.putExtra("ID_Application",ID_Application);
                startActivity(intent);
            }
        });

        final List<ApplicationPartDTO> myDataset = getDataSet();
        RecyclerView rv = (RecyclerView) findViewById(R.id.recyclerView);
        rv.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        rv.setLayoutManager(layoutManager);
        recyclerAdapter = new ApplicationPartListAdapter(myDataset);
        rv.setAdapter(recyclerAdapter);
        recyclerAdapter.setOnItemClickListener(new ApplicationPartListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View itemView, int position, View view) {
                switch (view.getId()) {
                    case R.id.button_Edit:
                        CopyItem(position);
                        break;
                    case R.id.button_Delete:
                        DeleteItem(position);
                        break;
                }
            }
        });
    }

    private void CopyItem(int position){
        //Скопируем элемент с индексом position и вставим копию в следующую позицию
        ApplicationPartDTO currentPerson = DataSet.get(position);
        recyclerAdapter.addItem(position + 1, currentPerson);
        //Известим адаптер о добавлении элемента
        recyclerAdapter.notifyItemInserted(position + 1);
    }

    private void DeleteItem(int position){
        //Удалим элемент из набора данных адаптера
        recyclerAdapter.deleteItem(position);
        //И уведомим об этом адаптер
        recyclerAdapter.notifyItemRemoved(position);
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
        DataSet = new ArrayList();
        for (int i = 0; i < lenghtAP; i++){
            String ID_Drink = getOneRes("SELECT ID_Drink FROM Application_Part WHERE ID_AP='" + array_ID_AP[i] + "'", "ID_Drink");
            String Sum_AP = getOneRes("SELECT Sum_AP FROM Application_Part WHERE ID_AP='" + array_ID_AP[i] + "'", "Sum_AP");
            String Name_Drink = getOneRes("SELECT Name_Drink FROM Drink WHERE ID_Drink='" + ID_Drink + "'", "Name_Drink");
            String Volume_Drink = getOneRes("SELECT Volume_Drink FROM Drink WHERE ID_Drink='" + ID_Drink + "'", "Volume_Drink");

            String ID_Syrup = getOneRes("SELECT ID_Syrup FROM ApplicationPart_Syrup WHERE ID_AP='" + array_ID_AP[i] + "'", "ID_Syrup");
            String Syrup = getOneRes("SELECT Name_Syrup FROM Syrup WHERE ID_Syrup='" + ID_Syrup + "'", "Name_Syrup");
            if (Syrup != null) {
                Syrup = "Сироп:" + Syrup;
            } else {
                Syrup="";
            }

            String Additives = "";
            array_additives = getArrayOneColumn("SELECT ID_Additives FROM ApplicationPart_Additives WHERE ID_AP='" + array_ID_AP[i] + "'", "ID_Additives");
            Toast.makeText(this, Integer.toString(lenghtAdditives), Toast.LENGTH_SHORT).show();
            if (lenghtAdditives > 0) {
                Additives = "Добавки:";
                for (int j = 0; j < lenghtAdditives; j++) {
                    Additives += getOneRes("SELECT Name_Additives FROM Additives WHERE ID_Additives='" + array_additives[j] + "'", "Name_Additives");
                    Additives += " ";
                }
            }
            DataSet.add(new ApplicationPartDTO(Name_Drink + "  " + Volume_Drink + " мл.", Additives + Syrup, Sum_AP + " руб."));
        }
        return DataSet;
    }
}
