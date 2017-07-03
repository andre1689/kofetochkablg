package com.kofetochka.kofetochkablg;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.kofetochka.inquiry.InquiryAddStorage;
import com.kofetochka.inquiry.InquiryGetID_St_OpenShift;
import com.kofetochka.inquiry.InquiryGetNameCH;
import com.kofetochka.inquiry.InquiryGetShiftID_ID_CH;

import java.text.SimpleDateFormat;
import java.util.Date;

public class CloseShiftActivity extends AppCompatActivity{
    private String Surname;
    private String Name;
    private String NameRole;
    private String ID_Shift;
    private String ID_CH;
    private String Name_CH;
    private String Login;
    private String Date;
    private String ID_St;
    private String S = "";
    private String ResAddStorage;

    Date dateNow;

    EditText et_DiceBox_150, et_DiceBox_200, et_DiceBox_300, et_DiceBox_400, et_DiceBox_Sum, et_Coffee_1kg, et_Coffee_250g, et_DripCoffee, et_Exchenge;
    TextView tv_Name_CH;

    InquiryGetShiftID_ID_CH inquiryGetShiftID_id_ch;
    InquiryGetNameCH inquiryGetNameCH;
    InquiryGetID_St_OpenShift inquiryGetID_st_openShift;
    InquiryAddStorage inquiryAddStorage;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.closeshift_layout);

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        if (toolbar != null){
            setSupportActionBar (toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        Surname = getIntent().getStringExtra("Surname");
        Name = getIntent().getStringExtra("Name");
        NameRole = getIntent().getStringExtra("NameRole");
        Login = getIntent().getStringExtra("Login");

        initializationEditText();

        dateNow = new Date();
        SimpleDateFormat formatForDateNow = new SimpleDateFormat("yyyy-M-d");
        Date = formatForDateNow.format(dateNow);

        tv_Name_CH = (TextView)findViewById(R.id.textView_CoffeeHouse);

        getID_Shift_ID_CH();

        if (ID_Shift==null){
            Toast.makeText(this, "Смена не открыта", Toast.LENGTH_LONG).show();
            startWorkActivity();
        } else {
            getNameCH();
            tv_Name_CH.setText("Кофеточка: " + Name_CH);
        }
    }

    private void initializationEditText() {
        et_DiceBox_150 = (EditText) findViewById(R.id.editText_DiceBox_150);
        et_DiceBox_200 = (EditText) findViewById(R.id.editText_DiceBox_200);
        et_DiceBox_300 = (EditText) findViewById(R.id.editText_DiceBox_300);
        et_DiceBox_400 = (EditText) findViewById(R.id.editText_DiceBox_400);
        et_DiceBox_Sum = (EditText) findViewById(R.id.editText_DiceBox_Sum);
        et_Coffee_1kg = (EditText) findViewById(R.id.editText_Coffee_1kg);
        et_Coffee_250g = (EditText) findViewById(R.id.editText_Coffee_250g);
        et_DripCoffee = (EditText) findViewById(R.id.editText_DripCoffee);
        et_Exchenge = (EditText) findViewById(R.id.editText_Exchange);
    }

    public void CloseShift (View view){

        getID_St_OpenShift();

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
            Toast.makeText(this, "Для закрытия смены заполните все поля", Toast.LENGTH_LONG).show();
            //et_DiceBox_150.setBackgroundColor(Color.parseColor("#81ff4081"));
        } else if (ID_St!=null){
            Toast.makeText(this, "Смена уже была закрыта", Toast.LENGTH_SHORT).show();
        }
        else {
            addStorage();
        }
    }

    private void getID_St_OpenShift() {
        inquiryGetID_st_openShift = new InquiryGetID_St_OpenShift();
        inquiryGetID_st_openShift.start(ID_Shift);
        try {
            inquiryGetID_st_openShift.join();
        } catch (InterruptedException e) {
            Log.e("GetID_St_OpenShift",e.getMessage());
        }
        ID_St = inquiryGetID_st_openShift.resID_St();
    }

    private void getNameCH() {
        inquiryGetNameCH = new InquiryGetNameCH();
        inquiryGetNameCH.start(ID_CH);
        try {
            inquiryGetNameCH.join();
        } catch (InterruptedException e) {
            Log.e("GetNameCH",e.getMessage());
        }
        Name_CH = inquiryGetNameCH.resName_CH();
    }

    private void getID_Shift_ID_CH() {
        inquiryGetShiftID_id_ch = new InquiryGetShiftID_ID_CH();
        inquiryGetShiftID_id_ch.start(Date,Login);
        try {
            inquiryGetShiftID_id_ch.join();
        } catch (InterruptedException e) {
            Log.e("Log CloseShift:", e.getMessage());
        }

        ID_Shift = inquiryGetShiftID_id_ch.resID_Shift();
        ID_CH = inquiryGetShiftID_id_ch.resID_CH();
    }

    private void startWorkActivity() {
        Intent intent = new Intent(this, WorkActivity.class);
        intent.putExtra("Login",Login);
        intent.putExtra("NameRole",NameRole);
        intent.putExtra("Surname", Surname);
        intent.putExtra("Name", Name);
        startActivity(intent);
        finish();
    }

    private void addStorage() {
        inquiryAddStorage = new InquiryAddStorage();
        inquiryAddStorage.start("0", et_DiceBox_150.getText().toString(), et_DiceBox_200.getText().toString(), et_DiceBox_300.getText().toString(), et_DiceBox_400.getText().toString(), et_DiceBox_Sum.getText().toString(), et_Coffee_1kg.getText().toString(), et_Coffee_250g.getText().toString(), et_DripCoffee.getText().toString(), et_Exchenge.getText().toString(), ID_Shift);

        try {
            inquiryAddStorage.join();
        } catch (InterruptedException e) {
            Log.e("Pass 0", e.getMessage());
        }

        ResAddStorage = inquiryAddStorage.resSuccess();
        if(ResAddStorage.equals("1")){
            Toast.makeText(this, "Смена закрыта", Toast.LENGTH_SHORT).show();
            startWorkActivity();
        }
        else Toast.makeText(this, "Не удалось закрыть смену", Toast.LENGTH_SHORT).show();
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
