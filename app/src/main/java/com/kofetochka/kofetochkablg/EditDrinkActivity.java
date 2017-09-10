package com.kofetochka.kofetochkablg;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.kofetochka.adapter.AdditivesAdapter;
import com.kofetochka.dto.AddEntryDTO;
import com.kofetochka.dto.AdditivesDTO;
import com.kofetochka.dto.DeleteEntryDTO;
import com.kofetochka.dto.GetArrayOneColumnDTO;
import com.kofetochka.dto.GetOneResDTO;
import com.kofetochka.dto.UpdateEntryDTO;
import com.kofetochka.inquiry.InquiryGetOneRes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EditDrinkActivity extends AppCompatActivity {
    int length_namedrink, length_volumedrink, length_syrupdrink;
    String Season;
    String[] arrayName_Drink, arrayOneName_Drink, arrayVolume_Drink, arrayOneVolume_Drink, arrayOneSyrup, arrayNameAdditives, arrayNameSyrup, arrayCheckedNameAdditives;
    String  ID_AP, ID_Application, Login;
    private String SelectNameDrink;
    private String SelectVolumeDrink;
    private String SelectSyrup = null;
    ListView lv_NameDrink, lv_VolumeDrink, lv_Syrup;
    Switch switch_additives, switch_Syrup;
    TextView tv_PriceDrink2, tv_PriceAdditives2, tv_PriceSyrup2, tv_PriceSumm2;
    Button btn_Save, btn_Cancel;
    List<AdditivesDTO> DataSet;
    private RecyclerView.LayoutManager layoutManager;
    AdditivesAdapter recyclerAdapter;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_drink_layout);
        InitializationToolbar();
        InitializationElementsActivity();
        getSeason();
        ID_AP = getIntent().getStringExtra("ID_AP");
        ID_Application = getIntent().getStringExtra("ID_Application");
        Login = getIntent().getStringExtra("Login");
        final GetOneResDTO getOneResDTO = new GetOneResDTO();

        String ID_Drink = getOneResDTO.getOneResDTO("SELECT ID_Drink FROM Application_Part WHERE ID_AP='"+ID_AP+"'","ID_Drink");
        String Name_Drink_select = getOneResDTO.getOneResDTO("SELECT Name_Drink FROM Drink WHERE ID_Drink='"+ID_Drink+"'","Name_Drink");
        getArrayNameDrink();
        arrayOneName_Drink = new String[]{Name_Drink_select};
        SelectNameDrink = arrayOneName_Drink[0];
        FillingListViewNameDrink(arrayOneName_Drink);

        final String Volume_Drink_select = getOneResDTO.getOneResDTO("SELECT Volume_Drink FROM Drink WHERE ID_Drink='"+ID_Drink+"'","Volume_Drink");
        getArrayVolumeDrink();
        arrayOneVolume_Drink = new String[]{Volume_Drink_select};
        SelectVolumeDrink = arrayOneVolume_Drink[0];
        FillingListViewVolumeDrink(arrayOneVolume_Drink);
        String Price_Drink_select = getOneResDTO.getOneResDTO("SELECT Price_Drink FROM Drink WHERE ID_Drink='"+ID_Drink+"'","Price_Drink");
        tv_PriceDrink2.setText(Price_Drink_select);

        String ID_Syrup_select = getOneResDTO.getOneResDTO("SELECT ID_Syrup FROM ApplicationPart_Syrup WHERE ID_AP='"+ID_AP+"'","ID_Syrup");
        if(ID_Syrup_select!=null){
            getArrayNameSyrup();
            length_syrupdrink=1;
            switch_Syrup.setChecked(true);
            String Name_Syrup_select = getOneResDTO.getOneResDTO("SELECT Name_Syrup FROM Syrup WHERE ID_Syrup='"+ID_Syrup_select+"'","Name_Syrup");
            arrayOneSyrup = new String[]{Name_Syrup_select};
            SelectSyrup = arrayOneSyrup[0];
            FillingListViewNameSyrup(arrayOneSyrup);
            String Price_Syrup = getOneResDTO.getOneResDTO("SELECT Price_Syrup FROM Syrup WHERE ID_Syrup='"+ID_Syrup_select+"'","Price_Syrup");
            tv_PriceSyrup2.setText(Price_Syrup);
        }

        getArrayNameAdditives();
        FillingListViewNameAdditives(arrayNameAdditives);
        lv_NameDrink.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //если больше 1 напитка, выбираем нажатый
                if (length_namedrink>1) {
                    SelectNameDrink = ((TextView) view).getText().toString();
                    arrayOneName_Drink = new String[]{SelectNameDrink};
                    FillingListViewNameDrink(arrayOneName_Drink);
                    getArrayVolumeDrink();
                    FillingListViewVolumeDrink(arrayVolume_Drink);
                    //если объем только один
                    if(arrayVolume_Drink.length==1){
                        SelectVolumeDrink = arrayVolume_Drink[0].toString();
                        tv_PriceDrink2.setText(getPriceDrink(SelectNameDrink,SelectVolumeDrink));//устнавливаем цену в TextView
                        setSumm(); //Суммирум в общую сумму
                        btn_Save.setVisibility(View.VISIBLE);
                        btn_Cancel.setVisibility(View.VISIBLE);
                    }
                    //если напиток выбран и его нужно изменить
                } else {
                    SelectNameDrink = null;
                    FillingListViewNameDrink(arrayName_Drink);
                    tv_PriceDrink2.setText("0");
                    setSumm();
                    btn_Save.setVisibility(View.INVISIBLE);
                    btn_Cancel.setVisibility(View.INVISIBLE);
                }
            }
        });

        lv_VolumeDrink.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(arrayVolume_Drink.length==1){
                    SelectVolumeDrink = arrayVolume_Drink[0].toString();
                    tv_PriceDrink2.setText(getPriceDrink(SelectNameDrink,SelectVolumeDrink));//устнавливаем цену в TextView
                    setSumm(); //Суммирум в общую сумму
                }else {
                    if (length_volumedrink > 1) {
                        SelectVolumeDrink = ((TextView) view).getText().toString();
                        arrayOneVolume_Drink = new String[]{SelectVolumeDrink};
                        FillingListViewVolumeDrink(arrayOneVolume_Drink);
                        tv_PriceDrink2.setText(getPriceDrink(SelectNameDrink, SelectVolumeDrink));
                        setSumm();
                        btn_Save.setVisibility(View.VISIBLE);
                        btn_Cancel.setVisibility(View.VISIBLE);
                    } else {
                        SelectVolumeDrink = null;
                        FillingListViewVolumeDrink(arrayVolume_Drink);
                        tv_PriceDrink2.setText("0");
                        setSumm();
                        btn_Save.setVisibility(View.INVISIBLE);
                        btn_Cancel.setVisibility(View.INVISIBLE);
                    }
                }
            }
        });

        switch_additives.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    getArrayNameAdditives();
                    FillingListViewNameAdditives(arrayNameAdditives);
                } else {
                    arrayNameAdditives = new String[0];
                    FillingListViewNameAdditives(arrayNameAdditives);
                    tv_PriceAdditives2.setText("0");
                    setSumm();
                }
            }
        });

        switch_Syrup.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    getArrayNameSyrup();
                    FillingListViewNameSyrup(arrayNameSyrup);
                }else {
                    arrayNameSyrup = new String[0];
                    FillingListViewNameSyrup(arrayNameSyrup);
                    tv_PriceSyrup2.setText("0");
                    setSumm();
                }
            }
        });

        lv_Syrup.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (length_syrupdrink>1){
                    SelectSyrup = ((TextView)view).getText().toString();
                    arrayOneSyrup = new String[]{SelectSyrup};
                    FillingListViewNameSyrup(arrayOneSyrup);
                    tv_PriceSyrup2.setText(getOneResDTO.getOneResDTO("SELECT Price_Syrup FROM Syrup WHERE Name_Syrup='"+SelectSyrup+"'", "Price_Syrup"));
                    setSumm();
                } else {
                    SelectSyrup = null;
                    FillingListViewNameSyrup(arrayNameSyrup);
                    tv_PriceSyrup2.setText("0");
                    setSumm();
                }
            }
        });

        btn_Save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UpdateEntryDTO updateEntryDTO = new UpdateEntryDTO();
                DeleteEntryDTO deleteEntryDTO = new DeleteEntryDTO();
                AddEntryDTO addEntryDTO = new AddEntryDTO();
                String ID_Drink = getOneResDTO.getOneResDTO("SELECT ID_Drink FROM Drink WHERE (Name_Drink='"+SelectNameDrink+"') AND (Volume_Drink='"+SelectVolumeDrink+"')","ID_Drink");
                Toast.makeText(EditDrinkActivity.this, updateEntryDTO.Update("UPDATE Application_Part SET ID_Drink='"+ID_Drink+"', Sum_AP = '"+tv_PriceSumm2.getText().toString()+"' WHERE ID_AP='"+ID_AP+"'"), Toast.LENGTH_SHORT).show();
                //Проверяем есть ли в напитке добавки
                deleteEntryDTO.DeleteEntry("DELETE FROM ApplicationPart_Additives WHERE ID_AP='"+ID_AP+"'");
                if(switch_additives.isChecked()) {//если добавки есть
                    for(int i=0;i<arrayNameAdditives.length;i++){
                        if(DataSet.get(i).getCheckBox_Additives()){
                            addEntryDTO.AddEntry("ApplicationPart_Additives", "(`ID_AP`, `ID_Additives`)", "('" + ID_AP + "', '" +DataSet.get(i).getID_Additives().toString()+"')");
                        }
                    }
                }
                deleteEntryDTO.DeleteEntry("DELETE FROM ApplicationPart_Syrup WHERE ID_AP='"+ID_AP+"'");
                if(switch_Syrup.isChecked()){
                    //Получаем значение ID_Syrup
                    String ID_Syrup = getOneResDTO.getOneResDTO("SELECT ID_Syrup FROM Syrup WHERE Name_Syrup='"+SelectSyrup+"'","ID_Syrup");
                    //Заносим запись в таблицу ApplicationPart_Syrup
                    addEntryDTO.AddEntry("ApplicationPart_Syrup", "(`ID_AP`, `ID_Syrup`)", "('" + ID_AP + "', '" +ID_Syrup+"')");
                }
                StartEditDrinkActivity();
            }
        });

        btn_Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StartEditDrinkActivity();
            }
        });

    }

    private void StartEditDrinkActivity() {
        Intent intent = new Intent(EditDrinkActivity.this,ApplicationPartActivity.class);
        intent.putExtra("Login",Login);
        intent.putExtra("ID_Application",ID_Application);
        startActivity(intent);
        finish();
    }

    //Инициализация Toolbar
    private void InitializationToolbar() {
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        if (toolbar != null){
            setSupportActionBar (toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }
    //Инициализация элементов Activity
    private void InitializationElementsActivity() {
        lv_NameDrink = (ListView) findViewById(R.id.listView_Name);
        lv_VolumeDrink = (ListView) findViewById(R.id.listView_Volume);
        lv_Syrup = (ListView) findViewById(R.id.listView_Syrup);
        switch_additives = (Switch) findViewById(R.id.switch_Additives);
        switch_Syrup = (Switch) findViewById(R.id.switch_Syrup);
        tv_PriceDrink2 = (TextView) findViewById(R.id.textView_PriceDrink2);
        tv_PriceAdditives2 = (TextView) findViewById(R.id.textView_PriceAdditives2);
        tv_PriceSyrup2 = (TextView) findViewById(R.id.textView_PriceSyrup2);
        tv_PriceSumm2 = (TextView) findViewById(R.id.textView_PriceSumm2);
        btn_Save = (Button) findViewById(R.id.button_Save);
        btn_Cancel = (Button) findViewById(R.id.button_Cancel);
    }
    //Получение значения сезона
    private void getSeason() {
        GetOneResDTO getOneResDTO = new GetOneResDTO();
        Season = getOneResDTO.getOneResDTO("SELECT Season FROM Settings","Season");
    }
    //Получение массива названий напитка
    private void getArrayNameDrink() {
        GetArrayOneColumnDTO getArrayOneColumnDTO = new GetArrayOneColumnDTO();
        arrayName_Drink = getArrayOneColumnDTO.getArrayOneColumn("SELECT Name_Drink FROM Drink WHERE Season LIKE '%"+Season+"%' GROUP BY Name_Drink","Name_Drink");
    }
    //Получение массива объема напитка
    private void getArrayVolumeDrink() {
        GetArrayOneColumnDTO getArrayOneColumnDTO = new GetArrayOneColumnDTO();
        arrayVolume_Drink = getArrayOneColumnDTO.getArrayOneColumn("SELECT Volume_Drink FROM Drink WHERE Name_Drink='"+SelectNameDrink+"' GROUP BY Volume_Drink","Volume_Drink");
    }
    //Получение массива названий добавок
    private void getArrayNameAdditives() {
        GetArrayOneColumnDTO getArrayOneColumnDTO = new GetArrayOneColumnDTO();
        arrayNameAdditives = getArrayOneColumnDTO.getArrayOneColumn("SELECT Name_Additives FROM Additives","Name_Additives");
    }
    //Получение массива названий сиропа
    private void getArrayNameSyrup() {
        GetArrayOneColumnDTO getArrayOneColumnDTO = new GetArrayOneColumnDTO();
        arrayNameSyrup = getArrayOneColumnDTO.getArrayOneColumn("SELECT Name_Syrup FROM Syrup","Name_Syrup");
    }
    //Заполнение ListView названиями напитков
    private void FillingListViewNameDrink(String[] arrayname_drink) {
        length_namedrink = arrayname_drink.length;
        ArrayAdapter<String> adapter_name = new ArrayAdapter<>(this, R.layout.list_item, arrayname_drink);
        lv_NameDrink.setAdapter(adapter_name);
        Utility.setListViewHeightBasedOnChildren(lv_NameDrink);
    }
    //Заполнение ListView объема напитка
    private void FillingListViewVolumeDrink(String[] arrayvolume_drink) {
        length_volumedrink = arrayvolume_drink.length;
        ArrayAdapter<String> adapter_volume = new ArrayAdapter<>(this, R.layout.list_item, arrayvolume_drink);
        lv_VolumeDrink.setAdapter(adapter_volume);
        Utility.setListViewHeightBasedOnChildren(lv_VolumeDrink);
    }
    //Заполнение ListView названиями сиропов
    private void FillingListViewNameSyrup(String[] arrayname_syrup) {
      length_syrupdrink = arrayname_syrup.length;
      ArrayAdapter<String> adapter_syrup = new ArrayAdapter<>(this, R.layout.list_item, arrayname_syrup);
      lv_Syrup.setAdapter(adapter_syrup);
      Utility.setListViewHeightBasedOnChildren(lv_Syrup);
    }
    //Заполнение RecyclerView добавок
    private void FillingListViewNameAdditives(String[] arrayname_additives) {
      if (arrayname_additives.length>0) {
          final List<AdditivesDTO> myDataset = getDataSet();
          RecyclerView rv = (RecyclerView) findViewById(R.id.recyclerView_Additives);
          rv.setHasFixedSize(true);
          layoutManager = new LinearLayoutManager(this);
          rv.setLayoutManager(layoutManager);
          recyclerAdapter = new AdditivesAdapter(myDataset);
          rv.setAdapter(recyclerAdapter);
          recyclerAdapter.setOnItemClickListener(new AdditivesAdapter.OnItemClickListener() {
              @Override
              public void onItemClick(View itemView, int position, View view) {
                  boolean CheckBoxClick = DataSet.get(position).getCheckBox_Additives();
                  if (CheckBoxClick) {
                      DataSet.get(position).setCheckBox_Additives(false);
                      recyclerAdapter.notifyDataSetChanged();
                      tv_PriceAdditives2.setText(Integer.toString(Integer.parseInt(tv_PriceAdditives2.getText().toString()) - Integer.parseInt(DataSet.get(position).getPrice_Additives())));
                      setSumm();
                  } else {
                      DataSet.get(position).setCheckBox_Additives(true);
                      recyclerAdapter.notifyDataSetChanged();
                      tv_PriceAdditives2.setText(Integer.toString(Integer.parseInt(tv_PriceAdditives2.getText().toString()) + Integer.parseInt(DataSet.get(position).getPrice_Additives())));
                      setSumm();
                  }
              }
          });
      } else {
          DataSet.clear();
          final List<AdditivesDTO> myDataset = DataSet;
          RecyclerView rv = (RecyclerView) findViewById(R.id.recyclerView_Additives);
          rv.setHasFixedSize(true);
          layoutManager = new LinearLayoutManager(this);
          rv.setLayoutManager(layoutManager);
          recyclerAdapter = new AdditivesAdapter(myDataset);
          rv.setAdapter(recyclerAdapter);
      }
    }
    //Определение добавок для добавления в RecycleView
    public List<AdditivesDTO> getDataSet() {
        GetArrayOneColumnDTO getArrayOneColumnDTO = new GetArrayOneColumnDTO();
        DataSet = new ArrayList();
        DataSet.clear();
        String[] array_ID_Additives = getArrayOneColumnDTO.getArrayOneColumn("SELECT ID_Additives FROM Additives","ID_Additives");
        String[] array_Name_Additives = getArrayOneColumnDTO.getArrayOneColumn("SELECT Name_Additives FROM Additives","Name_Additives");
        String[] array_Price_Additives = getArrayOneColumnDTO.getArrayOneColumn("SELECT Price_Additives FROM Additives","Price_Additives");
        String[] array_Cheked_Additives = getArrayOneColumnDTO.getArrayOneColumn("SELECT ID_Additives FROM ApplicationPart_Additives WHERE ID_AP='"+ID_AP+"'","ID_Additives");
        for (int i = 0; i < array_ID_Additives.length; i++){
            int error = 0;
            if(array_Cheked_Additives!=null) {
                for (int j=0; j<array_Cheked_Additives.length;j++){
                if (array_ID_Additives[i].equals(array_Cheked_Additives[j])){
                    switch_additives.setChecked(true);
                    DataSet.add(new AdditivesDTO(array_ID_Additives[i], array_Name_Additives[i], array_Price_Additives[i], true));
                    tv_PriceAdditives2.setText(Integer.toString(Integer.parseInt(tv_PriceAdditives2.getText().toString()) + Integer.parseInt(DataSet.get(i).getPrice_Additives())));
                    break;
                } else  {
                    error++;
                    if (error==array_Cheked_Additives.length) {
                        DataSet.add(new AdditivesDTO(array_ID_Additives[i], array_Name_Additives[i], array_Price_Additives[i], false));
                    }
                }
            }
            } else {
                if (switch_additives.isChecked()) {
                    DataSet.add(new AdditivesDTO(array_ID_Additives[i], array_Name_Additives[i], array_Price_Additives[i], false));
                }
            }
        }
        setSumm();
        return DataSet;
    }
    //Сумма всего заказа
    private void setSumm() {
        tv_PriceSumm2.setText(Integer.toString(Integer.parseInt(tv_PriceDrink2.getText().toString())+Integer.parseInt(tv_PriceAdditives2.getText().toString())+Integer.parseInt(tv_PriceSyrup2.getText().toString())));
    }

    private String getPriceDrink(String selectNameDrink, String selectVolumeDrink) {
        GetOneResDTO getOneResDTO = new GetOneResDTO();
        return getOneResDTO.getOneResDTO("SELECT Price_Drink FROM Drink WHERE (Name_Drink='" + selectNameDrink + "') AND (Volume_Drink='" + selectVolumeDrink + "')","Price_Drink");
    }

}
