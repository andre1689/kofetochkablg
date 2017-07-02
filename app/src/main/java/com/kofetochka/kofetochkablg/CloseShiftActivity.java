package com.kofetochka.kofetochkablg;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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

    Date dateNow;

    EditText et_DiceBox_150, et_DiceBox_200, et_DiceBox_300, et_DiceBox_400, et_DiceBox_Sum, et_Coffee_1kg, et_Coffee_250g, et_DripCoffee, et_Exchenge;
    TextView tv_Name_CH;

    InquiryGetShiftID_ID_CH inquiryGetShiftID_id_ch;
    InquiryGetNameCH inquiryGetNameCH;
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

        dateNow = new Date();
        SimpleDateFormat formatForDateNow = new SimpleDateFormat("yyyy-M-dd");
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
}
