package com.kofetochka.kofetochkablg;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.kofetochka.inquiry.InquiryAddShift;
import com.kofetochka.inquiry.InquiryAddStorage;
import com.kofetochka.inquiry.InquiryCoffeeHouse;
import com.kofetochka.inquiry.InquiryCoffeeHouseID;
import com.kofetochka.inquiry.InquiryGetIDShiftByDateID_CH;
import com.kofetochka.inquiry.InquiryGetShiftID;
import com.kofetochka.inquiry.InquiryGetStroageByDateCloseShift;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class OpenShift extends AppCompatActivity{

    InquiryCoffeeHouse inquiryCoffeeHouse;
    private String Table = "CoffeeHouse";
    private String Name_CH = null;
    private String ID_CH = null;
    private String Date;
    private String DateBack1day;
    private String Time;
    private String ResAddShift;
    private String ResAddStorage;
    private String ID_Shift=null;
    private String ID_Shift_BackDate;
    private String Login;
    private String Surname;
    private String Name;
    private String NameRole;
    private String DiceBox_150, DiceBox_200, DiceBox_300, DiceBox_400, DiceBox_Sum, Coffee_1kg, Coffee_250g, DripCoffee, Exchenge;
    private int l;
    private String S = "";

    String[] arrayName_CH;
    EditText et_DiceBox_150, et_DiceBox_200, et_DiceBox_300, et_DiceBox_400, et_DiceBox_Sum, et_Coffee_1kg, et_Coffee_250g, et_DripCoffee, et_Exchenge;
    ImageView iv_DiceBox_150, iv_DiceBox_200, iv_DiceBox_300, iv_DiceBox_400, iv_DiceBox_Sum, iv_Coffee_1kg, iv_Coffee_250g, iv_DripCoffee, iv_Exchenge;
    Spinner sp_CH_Name;
    Date dateNow;
    Calendar calendar;

    InquiryCoffeeHouseID inquiryCoffeeHouseID;
    InquiryAddShift inquiryAddShift;
    InquiryGetShiftID inquiryGetShiftID;
    InquiryAddStorage inquiryAddStorage;
    InquiryGetStroageByDateCloseShift inquiryGetStroageByDateCloseShift;
    InquiryGetIDShiftByDateID_CH inquiryGetIDShiftByDateID_ch;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.openshift_layout);

        Surname = getIntent().getStringExtra("Surname");
        Name = getIntent().getStringExtra("Name");
        NameRole = getIntent().getStringExtra("NameRole");

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

        iv_DiceBox_150 = (ImageView) findViewById(R.id.imageView_DiceBox_150);
        iv_DiceBox_200 = (ImageView) findViewById(R.id.imageView_DiceBox_200);
        iv_DiceBox_300 = (ImageView) findViewById(R.id.imageView_DiceBox_300);
        iv_DiceBox_400 = (ImageView) findViewById(R.id.imageView_DiceBox_400);
        iv_DiceBox_Sum = (ImageView) findViewById(R.id.imageView_DiceBox_Sum);
        iv_Coffee_1kg = (ImageView) findViewById(R.id.imageView_Coffee_1kg);
        iv_Coffee_250g = (ImageView) findViewById(R.id.imageView_Coffee_250g);
        iv_DripCoffee = (ImageView) findViewById(R.id.imageView_DripCoffee);
        iv_Exchenge = (ImageView) findViewById(R.id.imageView_Exchange);

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        if (toolbar != null){
            setSupportActionBar (toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        }

        InitializationSpinnerCoffeeHouse();
        InitializationDateAndTime();

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
        Login = getIntent().getStringExtra("Login");

        getID_CH();
        getShiftID();

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
        } else if (ID_Shift!=null){
            Toast.makeText(this, "Смена уже была создана", Toast.LENGTH_SHORT).show();
        }
        else {
            addShift();

            getShiftID();

            addStorage();

            startWorkActivity();
        }
    }

    public void reviseStorage(View view) {

        if (et_DiceBox_150.getText().toString().equals(S) ||
                et_DiceBox_200.getText().toString().equals(S) ||
                et_DiceBox_300.getText().toString().equals(S) ||
                et_DiceBox_400.getText().toString().equals(S) ||
                et_DiceBox_Sum.getText().toString().equals(S) ||
                et_Coffee_1kg.getText().toString().equals(S) ||
                et_Coffee_250g.getText().toString().equals(S) ||
                et_DripCoffee.getText().toString().equals(S) ||
                et_Exchenge.getText().toString().equals(S)) {
            Toast.makeText(this, "Для возможности сверки заполните все поля", Toast.LENGTH_LONG).show();
        } else {

            getID_CH();

            get_ID_Shift_BackDate();

            inquiryGetStroageByDateCloseShift = new InquiryGetStroageByDateCloseShift();
            inquiryGetStroageByDateCloseShift.start(ID_Shift_BackDate);
            try {
                inquiryGetStroageByDateCloseShift.join();
            } catch (InterruptedException e) {
                Log.e ("GetStroage", e.getMessage());
            }
            DiceBox_150 = inquiryGetStroageByDateCloseShift.resDiceBox_150_St();
            DiceBox_200 = inquiryGetStroageByDateCloseShift.resDiceBox_200_St();
            DiceBox_300 = inquiryGetStroageByDateCloseShift.resDiceBox_300_St();
            DiceBox_400 = inquiryGetStroageByDateCloseShift.resDiceBox_400_St();
            DiceBox_Sum = inquiryGetStroageByDateCloseShift.resDiceBox_Summer_St();
            Coffee_1kg = inquiryGetStroageByDateCloseShift.resCoffee_1kg_St();
            Coffee_250g = inquiryGetStroageByDateCloseShift.resCoffee_250g_St();
            DripCoffee = inquiryGetStroageByDateCloseShift.resDripCoffee_St();
            Exchenge = inquiryGetStroageByDateCloseShift.resExchange_St();

            if (et_DiceBox_150.getText().toString().equals(DiceBox_150)) {
                iv_DiceBox_150.setImageResource(R.mipmap.ic_check_black_18dp);

            } else {
                iv_DiceBox_150.setImageResource(R.mipmap.ic_close_black_18dp);
            }

            if (et_DiceBox_200.getText().toString().equals(DiceBox_200)){
                iv_DiceBox_200.setImageResource(R.mipmap.ic_check_black_18dp);

            } else {
                iv_DiceBox_200.setImageResource(R.mipmap.ic_close_black_18dp);
            }

            if (et_DiceBox_300.getText().toString().equals(DiceBox_300)){
                iv_DiceBox_300.setImageResource(R.mipmap.ic_check_black_18dp);

            } else {
                iv_DiceBox_300.setImageResource(R.mipmap.ic_close_black_18dp);
            }

            if (et_DiceBox_400.getText().toString().equals(DiceBox_400)){
                iv_DiceBox_400.setImageResource(R.mipmap.ic_check_black_18dp);

            } else {
                iv_DiceBox_400.setImageResource(R.mipmap.ic_close_black_18dp);
            }

            if (et_DiceBox_Sum.getText().toString().equals(DiceBox_Sum)){
                iv_DiceBox_Sum.setImageResource(R.mipmap.ic_check_black_18dp);

            } else {
                iv_DiceBox_Sum.setImageResource(R.mipmap.ic_close_black_18dp);
            }

            if (et_Coffee_1kg.getText().toString().equals(Coffee_1kg)){
                iv_Coffee_1kg.setImageResource(R.mipmap.ic_check_black_18dp);

            } else {
                iv_Coffee_1kg.setImageResource(R.mipmap.ic_close_black_18dp);
            }

            if (et_Coffee_250g.getText().toString().equals(Coffee_250g)){
                iv_Coffee_250g.setImageResource(R.mipmap.ic_check_black_18dp);

            } else {
                iv_Coffee_250g.setImageResource(R.mipmap.ic_close_black_18dp);
            }

            if (et_DripCoffee.getText().toString().equals(DripCoffee)){
                iv_DripCoffee.setImageResource(R.mipmap.ic_check_black_18dp);

            } else {
                iv_DripCoffee.setImageResource(R.mipmap.ic_close_black_18dp);
            }

            if (et_Exchenge.getText().toString().equals(Exchenge)){
                iv_Exchenge.setImageResource(R.mipmap.ic_check_black_18dp);

            } else {
                iv_Exchenge.setImageResource(R.mipmap.ic_close_black_18dp);
            }
        }
    }

    private void get_ID_Shift_BackDate() {
        inquiryGetIDShiftByDateID_ch = new InquiryGetIDShiftByDateID_CH();
        inquiryGetIDShiftByDateID_ch.start(DateBack1day,ID_CH);
        try {
            inquiryGetIDShiftByDateID_ch.join();
        } catch (InterruptedException e) {
            Log.e("GetIDShiftByDateID_CH", e.getMessage());
        }
        ID_Shift_BackDate = inquiryGetIDShiftByDateID_ch.resID_Shift();
    }

    private void startWorkActivity() {
        Intent intent = new Intent(this,WorkActivity.class);
        intent.putExtra("Login",Login);
        intent.putExtra("NameRole",NameRole);
        intent.putExtra("Surname", Surname);
        intent.putExtra("Name", Name);
        startActivity(intent);
        finish();
    }

    private void InitializationDateAndTime() {
        dateNow = new Date();
        SimpleDateFormat formatForDateNow = new SimpleDateFormat("yyyy-M-d");
        SimpleDateFormat formatForTimeNow = new SimpleDateFormat("hh:mm");
        Date = formatForDateNow.format(dateNow);
        Time = formatForTimeNow.format(dateNow);

        calendar = new GregorianCalendar();
        calendar.add(Calendar.DAY_OF_YEAR,-1);
        DateBack1day = Integer.toString(calendar.get(Calendar.YEAR))+"-"+Integer.toString(calendar.get(Calendar.MONTH)+1)+"-"+Integer.toString(calendar.get(Calendar.DAY_OF_MONTH));
    }

    private void addStorage() {
        inquiryAddStorage = new InquiryAddStorage();
        inquiryAddStorage.start("1", et_DiceBox_150.getText().toString(), et_DiceBox_200.getText().toString(), et_DiceBox_300.getText().toString(), et_DiceBox_400.getText().toString(), et_DiceBox_Sum.getText().toString(), et_Coffee_1kg.getText().toString(), et_Coffee_250g.getText().toString(), et_DripCoffee.getText().toString(), et_Exchenge.getText().toString(), ID_Shift);

        try {
            inquiryAddStorage.join();
        } catch (InterruptedException e) {
            Log.e("Pass 0", e.getMessage());
        }

        ResAddStorage = inquiryAddStorage.resSuccess();
        if(ResAddStorage.equals("1")){
            Toast.makeText(this, "Данные внесены в базу данных", Toast.LENGTH_SHORT).show();
        }
        else Toast.makeText(this, "Не удалось внести данные в базу данных", Toast.LENGTH_SHORT).show();
    }

    private void getShiftID() {
        inquiryGetShiftID = new InquiryGetShiftID();
        inquiryGetShiftID.start(Date,ID_CH,Login);

        try {
            inquiryGetShiftID.join();
        } catch (InterruptedException e) {
            Log.e("Pass 0", e.getMessage());
        }

        ID_Shift = inquiryGetShiftID.resID_Shift();
    }

    private void addShift() {
        inquiryAddShift = new InquiryAddShift();
        inquiryAddShift.start(Date,Time,ID_CH,Login);

        try {
            inquiryAddShift.join();
        } catch (InterruptedException e) {
            Log.e("Pass 0", e.getMessage());
        }

        ResAddShift = inquiryAddShift.resSuccess();
        if(ResAddShift.equals("1")){
            Toast.makeText(this, "Смена открыта", Toast.LENGTH_SHORT).show();
        }
        else Toast.makeText(this, "Не удалось открыть смену", Toast.LENGTH_SHORT).show();
    }

    private void getID_CH() {
        Name_CH = sp_CH_Name.getSelectedItem().toString();
        inquiryCoffeeHouseID = new InquiryCoffeeHouseID();
        inquiryCoffeeHouseID.start(Name_CH);

        try {
            inquiryCoffeeHouseID.join();
        } catch (InterruptedException e) {
            Log.e("Pass 0", e.getMessage());
        }

        ID_CH = inquiryCoffeeHouseID.resID_CH();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id==16908332)
        {
            startWorkActivity();
        }
        return super.onOptionsItemSelected(item);
    }

}
