package com.kofetochka.kofetochkablg;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.SparseBooleanArray;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.kofetochka.dto.GetArrayOneColumnDTO;
import com.kofetochka.dto.GetOneResDTO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class EditDrinkActivity extends AppCompatActivity {
    int length_namedrink, length_volumedrink, length_syrupdrink, length_additivesdrink;
    String Season;
    String[] arrayName_Drink, arrayOneName_Drink, arrayVolume_Drink, arrayOneVolume_Drink, arrayOneSyrup, arrayNameAdditives, arrayNameSyrup, arrayCheckedNameAdditives;
    String  ID_AP;
    ListView lv_NameDrink, lv_VolumeDrink, lv_Additives, lv_Syrup;
    Switch switch_additives, switch_Syrup;
    TextView tv_PriceDrink2, tv_PriceAdditives2, tv_PriceSyrup2, tv_PriceSumm2;
    Button btn_Save;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_drink_layout);
        InitializationToolbar();
        InitializationElementsActivity();
        getSeason();
        ID_AP = getIntent().getStringExtra("ID_AP");
        GetOneResDTO getOneResDTO = new GetOneResDTO();
        String ID_Drink = getOneResDTO.getOneResDTO("SELECT ID_Drink FROM Application_Part WHERE ID_AP='"+ID_AP+"'","ID_Drink");
        String Name_Drink_select = getOneResDTO.getOneResDTO("SELECT Name_Drink FROM Drink WHERE ID_Drink='"+ID_Drink+"'","Name_Drink");
        arrayName_Drink = new String[]{Name_Drink_select};
        FillingListViewNameDrink(arrayName_Drink);
        String Volume_Drink_select = getOneResDTO.getOneResDTO("SELECT Volume_Drink FROM Drink WHERE ID_Drink='"+ID_Drink+"'","Volume_Drink");
        arrayVolume_Drink = new String[]{Volume_Drink_select};
        FillingListViewVolumeDrink(arrayVolume_Drink);
        String Price_Drink_select = getOneResDTO.getOneResDTO("SELECT Price_Drink FROM Drink WHERE ID_Drink='"+ID_Drink+"'","Price_Drink");
        tv_PriceDrink2.setText(Price_Drink_select);

        String ID_Syrup_select = getOneResDTO.getOneResDTO("SELECT ID_Syrup FROM ApplicationPart_Syrup WHERE ID_AP='"+ID_AP+"'","ID_Syrup");
        if(ID_Syrup_select!=null){
            switch_Syrup.setChecked(true);
            String Name_Syrup_select = getOneResDTO.getOneResDTO("SELECT Name_Syrup FROM Syrup WHERE ID_Syrup='"+ID_Syrup_select+"'","Name_Syrup");
            arrayNameSyrup = new String[]{Name_Syrup_select};
            FillingListViewNameSyrup(arrayNameSyrup);
            String Price_Syrup = getOneResDTO.getOneResDTO("SELECT Price_Syrup FROM Syrup WHERE ID_Syrup='"+ID_Syrup_select+"'","Price_Syrup");
            tv_PriceSyrup2.setText(Price_Syrup);
        }

        GetArrayOneColumnDTO getArrayOneColumnDTO = new GetArrayOneColumnDTO();
        String[] array_ID_Additives_select = getArrayOneColumnDTO.getArrayOneColumn("SELECT ID_Additives FROM ApplicationPart_Additives WHERE ID_AP='"+ID_AP+"'","ID_Additives");
        if (array_ID_Additives_select.length>0){
            switch_additives.setChecked(true);
            getArrayNameAdditives();
            FillingListViewNameAdditives(arrayNameAdditives);
            SparseBooleanArray sparseBooleanArray = lv_Additives.getCheckedItemPositions();

        }
    }

    private void InitializationToolbar() {
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        if (toolbar != null){
            setSupportActionBar (toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    private void InitializationElementsActivity() {
        lv_NameDrink = (ListView) findViewById(R.id.listView_Name);
        lv_VolumeDrink = (ListView) findViewById(R.id.listView_Volume);
        lv_Additives = (ListView) findViewById(R.id.listView_Additives);
        lv_Syrup = (ListView) findViewById(R.id.listView_Syrup);
        switch_additives = (Switch) findViewById(R.id.switch_Additives);
        switch_Syrup = (Switch) findViewById(R.id.switch_Syrup);
        tv_PriceDrink2 = (TextView) findViewById(R.id.textView_PriceDrink2);
        tv_PriceAdditives2 = (TextView) findViewById(R.id.textView_PriceAdditives2);
        tv_PriceSyrup2 = (TextView) findViewById(R.id.textView_PriceSyrup2);
        tv_PriceSumm2 = (TextView) findViewById(R.id.textView_PriceSumm2);
        btn_Save = (Button) findViewById(R.id.button_Save);
    }

    private void getSeason() {
        GetOneResDTO getOneResDTO = new GetOneResDTO();
        Season = getOneResDTO.getOneResDTO("SELECT Season FROM Settings","Season");
    }

    private void getArrayNameDrink() {
        GetArrayOneColumnDTO getArrayOneColumnDTO = new GetArrayOneColumnDTO();
        arrayName_Drink = getArrayOneColumnDTO.getArrayOneColumn("SELECT Name_Drink FROM Drink WHERE Season LIKE '%"+Season+"%' GROUP BY Name_Drink","Name_Drink");
    }

    private void getArrayNameAdditives() {
        GetArrayOneColumnDTO getArrayOneColumnDTO = new GetArrayOneColumnDTO();
        arrayNameAdditives = getArrayOneColumnDTO.getArrayOneColumn("SELECT Name_Additives FROM Additives","Name_Additives");
    }

    private void FillingListViewNameDrink(String[] arrayname_drink) {
        length_namedrink = arrayname_drink.length;
        ArrayAdapter<String> adapter_name = new ArrayAdapter<>(this, R.layout.list_item, arrayname_drink);
        lv_NameDrink.setAdapter(adapter_name);
        Utility.setListViewHeightBasedOnChildren(lv_NameDrink);
    }

    private void FillingListViewVolumeDrink(String[] arrayvolume_drink) {
        length_volumedrink = arrayvolume_drink.length;
        ArrayAdapter<String> adapter_volume = new ArrayAdapter<>(this, R.layout.list_item, arrayvolume_drink);
        lv_VolumeDrink.setAdapter(adapter_volume);
        Utility.setListViewHeightBasedOnChildren(lv_VolumeDrink);
    }

  private void FillingListViewNameSyrup(String[] arrayname_syrup) {
      length_syrupdrink = arrayname_syrup.length;
      ArrayAdapter<String> adapter_syrup = new ArrayAdapter<>(this, R.layout.list_item, arrayname_syrup);
      lv_Syrup.setAdapter(adapter_syrup);
      Utility.setListViewHeightBasedOnChildren(lv_Syrup);
  }

  private void FillingListViewNameAdditives(String[] arrayname_additives) {
      length_additivesdrink = arrayname_additives.length;
      //ArrayAdapter<String> adapter_additives = new ArrayAdapter<>(this, R.layout.list_item_check, arrayname_additives);
      ArrayList<Map<String,Object>> data_additives = new ArrayList<Map<String,Object>>(length_additivesdrink);
      Map<String,Object> item;
      for (int i=0;i<length_additivesdrink;i++){
          item = new HashMap<String, Object>();
          item.put("Text",arrayname_additives[i]);
          item.put("Checked",true);
          data_additives.add(item);
      }
      String[] from = {"Text","Checked"};
      //int[] to = {R.id.};
      //SimpleAdapter simpleAdapter = new SimpleAdapter(this, R.layout.list_item_check, data_additives);
      //lv_Additives.setAdapter(adapter_additives);
      Utility.setListViewHeightBasedOnChildren(lv_Additives);

    }
}
