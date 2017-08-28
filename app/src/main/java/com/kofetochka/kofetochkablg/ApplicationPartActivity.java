package com.kofetochka.kofetochkablg;

import android.content.Intent;
import android.os.Bundle;
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
import com.kofetochka.dto.AddEntryDTO;
import com.kofetochka.dto.ApplicationPartDTO;
import com.kofetochka.dto.DeleteEntryDTO;
import com.kofetochka.dto.GetOneResDTO;
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
                Intent intent = new Intent(ApplicationPartActivity.this, NewDrinkActivity.class);
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
                    case R.id.imageView_Copy:
                        CopyItem(position);
                        break;
                    case R.id.imageView_Delete:
                        DeleteItem(position);
                        break;
                    case R.id.imageView_Edit:
                        break;
                    case R.id.imageView_Free:
                        break;
                }
            }
        });
    }

    private void CopyItem(int position){
        GetOneResDTO getOneResDTO = new GetOneResDTO();
        AddEntryDTO addEntryDTO = new AddEntryDTO();
        //Скопируем элемент с индексом position и вставим копию в следующую позицию
        ApplicationPartDTO currentPerson = DataSet.get(position);
        String ID_AP_position = DataSet.get(position).getID_AP();
        String ID_Application_position = getOneResDTO.getOneResDTO("SELECT ID_Application FROM Application_Part WHERE ID_AP='"+ID_AP_position+"'","ID_Application");
        String ID_Drink_position = getOneResDTO.getOneResDTO("SELECT ID_Drink FROM Application_Part WHERE ID_AP='"+ID_AP_position+"'","ID_Drink");
        //String ID_Free_position = getOneResDTO.getOneResDTO("SELECT ID_Free FROM Application_Part WHERE ID_AP='"+ID_AP_position+"'","ID_Free");
        String Sum_AP_position = getOneResDTO.getOneResDTO("SELECT Sum_AP FROM Application_Part WHERE ID_AP='"+ID_AP_position+"'","Sum_AP");
        //Получение максимального значения ID_AP
        String Max_AP = getOneResDTO.getOneResDTO("SELECT MAX(ID_AP) AS ID_AP FROM Application_Part","ID_AP");
        //Добавляем запись в таблицу Application_Part
        String ID_AP;
        if (Max_AP=="null"){ //если таблица Application_Part пустая
            ID_AP = "1";
            addEntryDTO.AddEntry("Application_Part", "(`ID_AP`, `ID_Application`, `ID_Drink`, `Sum_AP`)", "('1', '" + ID_Application_position +"', '"+ID_Drink_position+"', '"+Sum_AP_position+"')");
        } else { //если в таблице Application_Part есть записи
            int AP = Integer.parseInt(Max_AP) + 1;
            ID_AP = Integer.toString(AP);
            addEntryDTO.AddEntry("Application_Part", "(`ID_AP`, `ID_Application`, `ID_Drink`, `Sum_AP`)", "('" + ID_AP + "', '" + ID_Application_position +"', '"+ID_Drink_position+"', '"+Sum_AP_position+"')");
        }
        ApplicationPartDTO newAPDTO = new ApplicationPartDTO(DataSet.get(position).getTitle(),DataSet.get(position).getSubtitle(), DataSet.get(position).getPrice(), ID_AP);
        recyclerAdapter.addItem(position + 1,newAPDTO);
        //Известим адаптер о добавлении элемента
        recyclerAdapter.notifyItemInserted(position + 1);
    }

    private void DeleteItem(int position){
        DeleteEntryDTO deleteEntryDTO = new DeleteEntryDTO();
        String res = deleteEntryDTO.DeleteEntry("DELETE FROM `Application_Part` WHERE `ID_AP` ="+DataSet.get(position).getID_AP());
        Toast.makeText(this, res, Toast.LENGTH_SHORT).show();
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
        DataSet.clear();
        String[] array_ID_AP_list = getArrayOneColumn("SELECT ID_AP FROM Application_Part WHERE ID_Application='"+ID_Application+"'","ID_AP");
        int lenghtAP_list = array_ID_AP_list.length;
        for (int i = 0; i < lenghtAP_list; i++){
            String ID_Drink = getOneRes("SELECT ID_Drink FROM Application_Part WHERE ID_AP='" + array_ID_AP_list[i] + "'", "ID_Drink");
            String Sum_AP = getOneRes("SELECT Sum_AP FROM Application_Part WHERE ID_AP='" + array_ID_AP_list[i] + "'", "Sum_AP");
            String Name_Drink = getOneRes("SELECT Name_Drink FROM Drink WHERE ID_Drink='" + ID_Drink + "'", "Name_Drink");
            String Volume_Drink = getOneRes("SELECT Volume_Drink FROM Drink WHERE ID_Drink='" + ID_Drink + "'", "Volume_Drink");

            String ID_Syrup = getOneRes("SELECT ID_Syrup FROM ApplicationPart_Syrup WHERE ID_AP='" + array_ID_AP_list[i] + "'", "ID_Syrup");
            String Syrup = getOneRes("SELECT Name_Syrup FROM Syrup WHERE ID_Syrup='" + ID_Syrup + "'", "Name_Syrup");
            if (Syrup != null) {
                Syrup = "Сироп:" + Syrup;
            } else {
                Syrup="";
            }

            String Additives = "";
            array_additives = getArrayOneColumn("SELECT ID_Additives FROM ApplicationPart_Additives WHERE ID_AP='" + array_ID_AP_list[i] + "'", "ID_Additives");
            if (lenghtAdditives > 0) {
                Additives = "Добавки:";
                for (int j = 0; j < lenghtAdditives; j++) {
                    Additives += getOneRes("SELECT Name_Additives FROM Additives WHERE ID_Additives='" + array_additives[j] + "'", "Name_Additives");
                    Additives += " ";
                }
            }
            DataSet.add(new ApplicationPartDTO(Name_Drink + "  " + Volume_Drink + " мл.", Additives + Syrup, Sum_AP + " руб.", array_ID_AP_list[i]));

        }
        return DataSet;
    }
}
