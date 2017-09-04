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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.kofetochka.adapter.AdditivesAdapter;
import com.kofetochka.dto.AdditivesDTO;
import com.kofetochka.dto.GetArrayOneColumnDTO;
import com.kofetochka.dto.GetOneResDTO;
import com.kofetochka.inquiry.InquiryAdd;

import com.kofetochka.inquiry.InquiryGetOneRes;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class NewDrinkActivity extends AppCompatActivity{

    private int  L;
    private int  V;
    private int a, A;
    private int s, S;
    private int volume_drink_lenght;
    private int choise;
    private int Summ = 0;
    private String SelectNameDrink;
    private String SelectVolumeDrink;
    private String SelectSyrup = null;
    private String PriceDrink;
    private String Date;
    private String Login;
    private String Season;
    List<AdditivesDTO> DataSet;
    private RecyclerView.LayoutManager layoutManager;
    AdditivesAdapter recyclerAdapter;

    String[] arrayName_Drink, arrayOneName_Drink, arrayVolume_Drink, arrayOneVolume_Drink, arrayOneSyrup, arrayNameAdditives, arrayNameSyrup, arrayCheckedNameAdditives;
    ListView lv_NameDrink, lv_VolumeDrink, lv_Additives, lv_Syrup;
    Switch switch_additives, switch_Syrup;
    TextView tv_PriceDrink2, tv_PriceAdditives2, tv_PriceSyrup2, tv_PriceSumm2;
    Button btn_Add;
    Date dateNow;

    InquiryAdd inquiryAdd;
    InquiryGetOneRes inquiryGetOneRes;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_drink_layout);
        //Инициализацию Toolbar
        InitializationToolbar();
        //Узнаем текуюую дату
        InitializationDate();
        //Получаем занение логина из предыдущего Activity
        Login = getIntent().getStringExtra("Login");
        //Инициализация элементов Activity
        InitializationElementsActivity();

        btn_Add.setVisibility(View.INVISIBLE);

        arrayCheckedNameAdditives = new String[0];
        //Узнаем какой сезон устанвлен в настройках
        getSeason();
        //Получаем массив напитков
        getArrayNameDrink();
        //Заполняем ListView полученным массивом напитков
        FillingListViewNameDrink(arrayName_Drink);
        //Обрабочик выбора напитка
        lv_NameDrink.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //если больше 1 напитка, выбираем нажатый
                if (L>1) {
                    SelectNameDrink = ((TextView) view).getText().toString();
                    arrayOneName_Drink = new String[]{SelectNameDrink};
                    FillingListViewNameDrink(arrayOneName_Drink);
                    getArrayVolumeDrink();
                    //если объем только один
                    volume_drink_lenght = arrayVolume_Drink.length;
                    if(volume_drink_lenght==1){
                        SelectVolumeDrink = arrayVolume_Drink[0].toString();
                        getPriceDrink(SelectNameDrink,SelectVolumeDrink); //получаем цену выбранного напитка
                        tv_PriceDrink2.setText(PriceDrink);//устнавливаем цену в TextView
                        setSumm(); //Суммирум в общую сумму
                        btn_Add.setVisibility(View.VISIBLE);
                    }
                    //если напиток выбран и его нужно изменить
                } else {
                    SelectNameDrink = null;
                    FillingListViewNameDrink(arrayName_Drink);
                    tv_PriceDrink2.setText("0");
                    setSumm();
                    btn_Add.setVisibility(View.INVISIBLE);
                }
            }
        });

        lv_VolumeDrink.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(volume_drink_lenght==1){
                    SelectVolumeDrink = arrayVolume_Drink[0].toString();
                    getPriceDrink(SelectNameDrink,SelectVolumeDrink); //получаем цену выбранного напитка
                    tv_PriceDrink2.setText(PriceDrink);//устнавливаем цену в TextView
                    setSumm(); //Суммирум в общую сумму
                }else {
                    if (V > 1) {
                        SelectVolumeDrink = ((TextView) view).getText().toString();
                        arrayOneVolume_Drink = new String[]{SelectVolumeDrink};
                        FillingListViewVolumeDrink(arrayOneVolume_Drink);
                        getPriceDrink(SelectNameDrink, SelectVolumeDrink);
                        tv_PriceDrink2.setText(PriceDrink);
                        setSumm();
                        btn_Add.setVisibility(View.VISIBLE);
                    } else {
                        SelectVolumeDrink = null;
                        FillingListViewVolumeDrink(arrayVolume_Drink);
                        tv_PriceDrink2.setText("0");
                        setSumm();
                        btn_Add.setVisibility(View.INVISIBLE);
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
                if (S>1){
                    SelectSyrup = ((TextView)view).getText().toString();
                    arrayOneSyrup = new String[]{SelectSyrup};
                    FillingListViewNameSyrup(arrayOneSyrup);
                    String ColmunSyrup = "Price_Syrup";
                    String QuirySyrup = "SELECT "+ColmunSyrup+" FROM Syrup WHERE Name_Syrup='"+SelectSyrup+"'";
                    inquiryGetOneRes = new InquiryGetOneRes();
                    inquiryGetOneRes.start(QuirySyrup,ColmunSyrup);
                    try {
                        inquiryGetOneRes.join();
                    } catch (InterruptedException e) {
                        Log.e("GetPriseSyrup",e.getMessage());
                    }
                    tv_PriceSyrup2.setText(inquiryGetOneRes.res());
                    setSumm();
                } else {
                    SelectSyrup = null;
                    FillingListViewNameSyrup(arrayNameSyrup);
                    tv_PriceSyrup2.setText("0");
                    setSumm();
                }
            }
        });

    }
    //Метод инициализации элементов Activity
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
        btn_Add = (Button) findViewById(R.id.button_Add);
    }
    //Метод инициализации Tollbar
    private void InitializationToolbar() {
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        if (toolbar != null){
            setSupportActionBar (toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }
    //Метод получения данных по текущему сезону
    private void getSeason() {
        GetOneResDTO getOneResDTO = new GetOneResDTO();
        Season = getOneResDTO.getOneResDTO("SELECT Season FROM Settings","Season");
    }
    //Метод инициализации текущей даты
    private void InitializationDate() {
        dateNow = new Date();
        SimpleDateFormat formatForDateNow = new SimpleDateFormat("yyyy-M-d");
        Date = formatForDateNow.format(dateNow);
    }
    //Метод отображения конечной суммы заказа
    private void setSumm() {
        tv_PriceSumm2.setText(Integer.toString(Integer.parseInt(tv_PriceDrink2.getText().toString())+Integer.parseInt(tv_PriceAdditives2.getText().toString())+Integer.parseInt(tv_PriceSyrup2.getText().toString())));
    }

    private void getPriceDrink(String selectNameDrink, String selectVolumeDrink) {
        GetOneResDTO getOneResDTO = new GetOneResDTO();
        PriceDrink = getOneResDTO.getOneResDTO("SELECT Price_Drink FROM Drink WHERE (Name_Drink='" + selectNameDrink + "') AND (Volume_Drink='" + selectVolumeDrink + "')","Price_Drink");
    }

    private void getArrayNameSyrup() {
        GetArrayOneColumnDTO getArrayOneColumnDTO = new GetArrayOneColumnDTO();
        arrayNameSyrup = getArrayOneColumnDTO.getArrayOneColumn("SELECT Name_Syrup FROM Syrup","Name_Syrup");
    }
    private void getArrayNameAdditives() {
        GetArrayOneColumnDTO getArrayOneColumnDTO = new GetArrayOneColumnDTO();
        arrayNameAdditives = getArrayOneColumnDTO.getArrayOneColumn("SELECT Name_Additives FROM Additives","Name_Additives");
    }
    private void getArrayNameDrink() {
        GetArrayOneColumnDTO getArrayOneColumnDTO = new GetArrayOneColumnDTO();
        arrayName_Drink = getArrayOneColumnDTO.getArrayOneColumn("SELECT Name_Drink FROM Drink WHERE Season LIKE '%"+Season+"%' GROUP BY Name_Drink","Name_Drink");
    }
    private void getArrayVolumeDrink() {
        GetArrayOneColumnDTO getArrayOneColumnDTO = new GetArrayOneColumnDTO();
        arrayVolume_Drink = getArrayOneColumnDTO.getArrayOneColumn("SELECT Volume_Drink FROM Drink WHERE Name_Drink='"+SelectNameDrink+"' GROUP BY Volume_Drink","Volume_Drink");
        FillingListViewVolumeDrink(arrayVolume_Drink);
    }
    //Метод заполнения ListView значениями наименований напитков
    private void FillingListViewNameDrink(String[] arrayname_drink) {
        L = arrayname_drink.length;
        ArrayAdapter<String> adapter_name = new ArrayAdapter<>(this, R.layout.list_item, arrayname_drink);
        lv_NameDrink.setAdapter(adapter_name);
        Utility.setListViewHeightBasedOnChildren(lv_NameDrink);
    }
    //Метод заполнения ListView значениями объемов напитка
    private void FillingListViewVolumeDrink(String[] arrayvolume_drink) {
        V = arrayvolume_drink.length;
        ArrayAdapter<String> adapter_volume = new ArrayAdapter<>(this, R.layout.list_item, arrayvolume_drink);
        lv_VolumeDrink.setAdapter(adapter_volume);
        Utility.setListViewHeightBasedOnChildren(lv_VolumeDrink);
    }
    //Метод заполнения ListView значениями наименований добавок
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
    //Метод заполнения ListView значениями наименований сиропов
    private void FillingListViewNameSyrup(String[] arrayname_syrup) {
        S = arrayname_syrup.length;
        ArrayAdapter<String> adapter_syrup = new ArrayAdapter<>(this, R.layout.list_item, arrayname_syrup);
        lv_Syrup.setAdapter(adapter_syrup);
        Utility.setListViewHeightBasedOnChildren(lv_Syrup);
    }

    public void addApplication (View view){
        GetOneResDTO getOneResDTO = new GetOneResDTO();
        String ID_Shift = getOneResDTO.getOneResDTO("SELECT ID_Shift FROM Shift WHERE (Date_Shift='"+Date+"') AND (Login = '"+Login+"')","ID_Shift");
        //getID_Shift();
        String ID_AP = null;
        //Полчаем значение ID_Application из предыдущего Activity
        String ID_Application = getIntent().getStringExtra("ID_Application");
        if (ID_Application.equals("0")) {//если ID_Application = 0 (т.е. не был до этого создан)
            //Получение максимального значения ID_Application
            String Max_ID_Application = getOneResDTO.getOneResDTO("SELECT MAX(ID_Application) AS ID_Application FROM Application", "ID_Application");
            //Добавляем запись в таблицу Application
            if (Max_ID_Application == "null") { //если таблица Application пустая
                ID_Application = "1";
                AddEntry("Application", "(`ID_Application`, `ID_Shift`)", "('1', '" + ID_Shift + "')");
            } else { //если в таблице Application есть записи
                int Apllication = Integer.parseInt(Max_ID_Application) + 1;
                ID_Application = Integer.toString(Apllication);
                AddEntry("Application", "(`ID_Application`, `ID_Shift`)", "('" + ID_Application + "', '" + ID_Shift + "')");
            }
        }
        //Получение значения ID_Drink
        String ID_Drink = getOneResDTO.getOneResDTO("SELECT ID_Drink FROM Drink WHERE (Name_Drink='"+SelectNameDrink+"') AND (Volume_Drink='"+SelectVolumeDrink+"')","ID_Drink");
        //Получение максимального значения ID_AP
        String Max_AP = getOneResDTO.getOneResDTO("SELECT MAX(ID_AP) AS ID_AP FROM Application_Part","ID_AP");
        //Добавляем запись в таблицу Application_Part
        if (Max_AP=="null"){ //если таблица Application_Part пустая
            AddEntry("Application_Part", "(`ID_AP`, `ID_Application`, `ID_Drink`, `Sum_AP`)", "('1', '" + ID_Application +"', '"+ID_Drink+"', '"+tv_PriceSumm2.getText().toString()+"')");
        } else { //если в таблице Application_Part есть записи
            int AP = Integer.parseInt(Max_AP) + 1;
            ID_AP = Integer.toString(AP);
            AddEntry("Application_Part", "(`ID_AP`, `ID_Application`, `ID_Drink`, `Sum_AP`)", "('" + ID_AP + "', '" + ID_Application +"', '"+ID_Drink+"', '"+tv_PriceSumm2.getText().toString()+"')");
        }

        //Проверяем есть ли в напитке добавки
        if(arrayNameAdditives.length>0) {//если добавки есть
            for(int i=0;i<arrayNameAdditives.length;i++){
                if(DataSet.get(i).getCheckBox_Additives()){
                    AddEntry("ApplicationPart_Additives", "(`ID_AP`, `ID_Additives`)", "('" + ID_AP + "', '" +DataSet.get(i).getID_Additives().toString()+"')");
                }
            }
        }

        if(SelectSyrup!=null){
            //Получаем значение ID_Syrup
            String ID_Syrup = getOneResDTO.getOneResDTO("SELECT ID_Syrup FROM Syrup WHERE Name_Syrup='"+SelectSyrup+"'","ID_Syrup");
            //Заносим запись в таблицу ApplicationPart_Syrup
            AddEntry("ApplicationPart_Syrup", "(`ID_AP`, `ID_Syrup`)", "('" + ID_AP + "', '" +ID_Syrup+"')");
        }

        Intent intent = new Intent(this,ApplicationPartActivity.class);
        intent.putExtra("ID_AP",ID_AP);
        intent.putExtra("Login", Login);
        intent.putExtra("ID_Application", ID_Application);
        startActivity(intent);
    }

    private void AddEntry(String tableApplication, String columnApplication, String valuesApplication) {
        inquiryAdd = new InquiryAdd();
        inquiryAdd.start(tableApplication, columnApplication, valuesApplication);
        try {
            inquiryAdd.join();
        } catch (InterruptedException e) {
            Log.e("Add", e.getMessage());
        }
    }

    public List<AdditivesDTO> getDataSet() {
        GetArrayOneColumnDTO getArrayOneColumnDTO = new GetArrayOneColumnDTO();
        DataSet = new ArrayList();
        DataSet.clear();
        String[] array_ID_Additives = getArrayOneColumnDTO.getArrayOneColumn("SELECT ID_Additives FROM Additives","ID_Additives");
        String[] array_Name_Additives = getArrayOneColumnDTO.getArrayOneColumn("SELECT Name_Additives FROM Additives","Name_Additives");
        String[] array_Price_Additives = getArrayOneColumnDTO.getArrayOneColumn("SELECT Price_Additives FROM Additives","Price_Additives");
        int lenght_list = array_ID_Additives.length;
        for (int i = 0; i < lenght_list; i++){
            DataSet.add(new AdditivesDTO(array_ID_Additives[i],array_Name_Additives[i],array_Price_Additives[i],false));
        }
        return DataSet;
    }
}
