package com.kofetochka.kofetochkablg;

import android.graphics.Color;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.kofetochka.inquiry.InquiryAddShift;
import com.kofetochka.inquiry.InquiryAddStorage;
import com.kofetochka.inquiry.InquiryCoffeeHouse;
import com.kofetochka.inquiry.InquiryCoffeeHouseID;
import com.kofetochka.inquiry.InquiryGetShiftID;

import org.json.JSONArray;

import java.util.Date;

public class OpenShift extends AppCompatActivity{

    InquiryCoffeeHouse inquiryCoffeeHouse;
    private String Table = "CoffeeHouse";
    private String Name_CH = null;
    private String ID_CH = null;
    private String Date;
    private String Time;
    private String ResAddShift;
    private String ResAddStorage;
    private String ID_Shift;
    private int l;
    String[] arrayName_CH;
    EditText et_DiceBox_150, et_DiceBox_200, et_DiceBox_300, et_DiceBox_400, et_DiceBox_Sum, et_Coffee_1kg, et_Coffee_250g, et_DripCoffee, et_Exchenge;
    Spinner sp_CH_Name;
    Date dateNow;

    InquiryCoffeeHouseID inquiryCoffeeHouseID;
    InquiryAddShift inquiryAddShift;
    InquiryGetShiftID inquiryGetShiftID;
    InquiryAddStorage inquiryAddStorage;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.openshift_layout);

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        if (toolbar != null){
            setSupportActionBar (toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        InitializationSpinnerCoffeeHouse();

    }

    private void InitializationSpinnerCoffeeHouse() {
        inquiryCoffeeHouse = new InquiryCoffeeHouse();
        inquiryCoffeeHouse.start(Table);

        try {
            inquiryCoffeeHouse.join();
        } catch (InterruptedException e) {
            Log.e("Pass 0", e.getMessage());
        }

        l=inquiryCoffeeHouse.resLenght();
        arrayName_CH = new String[l];
        arrayName_CH = inquiryCoffeeHouse.resName_CH();

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, arrayName_CH);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Spinner spinner = (Spinner) findViewById(R.id.spinner_CoffeeHouse);
        spinner.setAdapter(adapter);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void openShift(View view){
        et_DiceBox_150 = (EditText) findViewById(R.id.editText_DiceBox_150);
        et_DiceBox_200 = (EditText) findViewById(R.id.editText_DiceBox_200);
        et_DiceBox_300 = (EditText) findViewById(R.id.editText_DiceBox_300);
        et_DiceBox_400 = (EditText) findViewById(R.id.editText_DiceBox_400);
        et_DiceBox_Sum = (EditText) findViewById(R.id.editText_DiceBox_Sum);
        et_Coffee_1kg = (EditText) findViewById(R.id.editText_Coffee_1kg);
        et_Coffee_250g = (EditText) findViewById(R.id.editText_Coffee_250g);
        et_DripCoffee = (EditText) findViewById(R.id.editText_DripCoffee);
        et_Exchenge = (EditText) findViewById(R.id.editText_Exchange);
        sp_CH_Name = (Spinner) findViewById(R.id.spinner_CoffeeHouse);

        //Toast.makeText(this, et_DiceBox_150.getText().toString(), Toast.LENGTH_LONG).show();
        String S = "";
        if (et_DiceBox_150.getText().toString().equals(S) ||
                et_DiceBox_200.getText().toString().equals(S) ||
                et_DiceBox_300.getText().toString().equals(S) ||
                et_DiceBox_400.getText().toString().equals(S) ||
                et_DiceBox_Sum.getText().toString().equals(S) ||
                et_Coffee_1kg.getText().toString().equals(S) ||
                et_Coffee_250g.getText().toString().equals(S) ||
                et_DripCoffee.getText().toString().equals(S) ||
                et_Exchenge.getText().toString().equals(S))
        {
            Toast.makeText(this, "Для открытия смены заполните все поля", Toast.LENGTH_LONG).show();
            //et_DiceBox_150.setBackgroundColor(Color.parseColor("#81ff4081"));
        } else {

            Name_CH = sp_CH_Name.getSelectedItem().toString();
            inquiryCoffeeHouseID = new InquiryCoffeeHouseID();
            inquiryCoffeeHouseID.start(Name_CH);

            try {
                inquiryCoffeeHouseID.join();
            } catch (InterruptedException e) {
                Log.e("Pass 0", e.getMessage());
            }

            ID_CH = inquiryCoffeeHouseID.resID_CH();

            dateNow = new Date();
            SimpleDateFormat formatForDateNow = new SimpleDateFormat("yyyy-MM-dd");
            Date = formatForDateNow.format(dateNow);
            SimpleDateFormat formatForTimeNow = new SimpleDateFormat("hh:mm");
            Time = formatForTimeNow.format(dateNow);

            inquiryAddShift = new InquiryAddShift();
            inquiryAddShift.start(Date,Time,ID_CH);

            try {
                inquiryAddShift.join();
            } catch (InterruptedException e) {
                Log.e("Pass 0", e.getMessage());
            }

            ResAddShift = inquiryAddShift.resSuccess();
            if(ResAddShift.equals("1")){
                Toast.makeText(this, "Смена открыта.", Toast.LENGTH_SHORT).show();
            }
            else Toast.makeText(this, "Не удалось открыть смену", Toast.LENGTH_SHORT).show();

            inquiryGetShiftID = new InquiryGetShiftID();
            inquiryGetShiftID.start(Date,Time,ID_CH);

            try {
                inquiryGetShiftID.join();
            } catch (InterruptedException e) {
                Log.e("Pass 0", e.getMessage());
            }

            ID_Shift = inquiryGetShiftID.resID_Shift();

            inquiryAddStorage = new InquiryAddStorage();
            inquiryAddStorage.start("1", et_DiceBox_150.getText().toString(), et_DiceBox_200.getText().toString(), et_DiceBox_300.getText().toString(), et_DiceBox_400.getText().toString(), et_DiceBox_Sum.getText().toString(), et_Coffee_1kg.getText().toString(), et_Coffee_250g.getText().toString(), et_DripCoffee.getText().toString(), et_Exchenge.getText().toString(), ID_Shift);

            try {
                inquiryAddStorage.join();
            } catch (InterruptedException e) {
                Log.e("Pass 0", e.getMessage());
            }

            ResAddStorage = inquiryAddStorage.resSuccess();
            if(ResAddShift.equals("1")){
                Toast.makeText(this, "Данные внесены в базу данных", Toast.LENGTH_SHORT).show();
            }
            else Toast.makeText(this, "Не удалось внести данные в базу данных", Toast.LENGTH_SHORT).show();
        }
    }
}
