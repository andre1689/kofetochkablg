package com.kofetochka.kofetochkablg;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.kofetochka.inquiry.InquiryAdd;
import com.kofetochka.inquiry.InquiryAddStorage;
import com.kofetochka.inquiry.InquiryGetID_St_OpenShift;
import com.kofetochka.inquiry.InquiryGetNameCH;
import com.kofetochka.inquiry.InquiryGetOneRes;
import com.kofetochka.inquiry.InquiryGetShiftID_ID_CH;

import java.text.SimpleDateFormat;
import java.util.Date;

public class CloseShiftActivity extends AppCompatActivity{

    private String ID_Shift;
    private String ID_CH;
    private String Name_CH;
    private String Login;
    private String Date;
    private String ID_St=null;
    private String S = "";
    private String MessageOpenShift;

    Date dateNow;

    private TextInputLayout til_DiceBox_150, til_DiceBox_200, til_DiceBox_300, til_DiceBox_400, til_DiceBox_Sum, til_Coffee_1kg, til_Coffee_250g, til_DripCoffee, til_Exchenge;
    EditText et_DiceBox_150, et_DiceBox_200, et_DiceBox_300, et_DiceBox_400, et_DiceBox_Sum, et_Coffee_1kg, et_Coffee_250g, et_DripCoffee, et_Exchenge, et_Comment;
    TextView tv_Name_CH;

    InquiryAdd inquiryAdd;
    InquiryGetOneRes inquiryGetOneRes;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.closeshift_layout);
        //Получаем значение login из прошлого Activity
        Login = getIntent().getStringExtra("Login");
        tv_Name_CH = (TextView)findViewById(R.id.textView_CoffeeHouse);
        //Инициализирует ToolBar
        initializationToolbar();
        initializationDetailsLayout();
        //Получаем значение текущей даты
        initializationDate();
        //Получаем значение ID CoffeeHouse
        ID_CH = getOneRes("SELECT ID_CH FROM Shift WHERE (Date_Shift='"+Date+"') AND (Login='"+Login+"')","ID_CH");
        //Заполнение EditText (кофе 1 кг, кофе 250 г., дрип кофе)
        fillingEditText();
        //Получаем значение ID Shift
        ID_Shift = getOneRes("SELECT ID_Shift FROM Shift WHERE (Date_Shift='"+Date+"') AND (ID_CH='"+ID_CH+"')","ID_Shift");
        //Проверяем была ли открыта смена, прежде чем ее закрывать
        if (ID_Shift==null){ //если смена не была найдена, то выводим диалоговое окно
            new MaterialDialog.Builder(this)
                    .onPositive(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            startOpenShiftActivity();
                        }
                    })
                    .onNegative(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            startWorkActivity();
                        }
                    })
                    .title("Сообщение")
                    .positiveColorRes(R.color.colorPrimary)
                    .negativeColorRes(R.color.colorPrimary)
                    .content("Смена не была Вами открыта")
                    .positiveText("Открыть смену")
                    .negativeText("Отмена")
                    .cancelable(false)
                    .show();
        } else {
            //Получаем название кофейни, где была открыта смена и заполняем TextView
            Name_CH = getOneRes("SELECT Name_CH FROM CoffeeHouse WHERE ID_CH='"+ID_CH+"'","Name_CH");
            tv_Name_CH.setText("Кофеточка: " + Name_CH);
        }
        et_DiceBox_150.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                til_DiceBox_150.setErrorEnabled(false);
            }
        });
        et_DiceBox_200.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                til_DiceBox_200.setErrorEnabled(false);
            }
        });
        et_DiceBox_300.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                til_DiceBox_300.setErrorEnabled(false);
            }
        });
        et_DiceBox_400.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                til_DiceBox_400.setErrorEnabled(false);
            }
        });
        et_DiceBox_Sum.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                til_DiceBox_Sum.setErrorEnabled(false);
            }
        });
        et_Coffee_1kg.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                til_Coffee_1kg.setErrorEnabled(false);
            }
        });
        et_Coffee_250g.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                til_Coffee_250g.setErrorEnabled(false);
            }
        });
        et_DripCoffee.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                til_DripCoffee.setErrorEnabled(false);
            }
        });
        et_Exchenge.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                til_Exchenge.setErrorEnabled(false);
            }
        });
    }
    //Заполнение EditText (кофе 1 кг, кофе 250 г., дрип кофе)
    private void fillingEditText() {
        //Заполнение поля Кофе в зернах 1 кг
        String SumCoffee1kg = getOneRes("SELECT SUM(Amount_Coffee) FROM `Coffee` WHERE (Name_Coffee LIKE '%Кофе в зернах%') AND (Volume_Coffee='1000 г.') AND (ID_CH='"+ID_CH+"')","SUM(Amount_Coffee)");
        et_Coffee_1kg.setText(SumCoffee1kg);
        //Заполнение поля Кофе в зернах 250 г
        String SumCoffee250g = getOneRes("SELECT SUM(Amount_Coffee) FROM `Coffee` WHERE (Name_Coffee LIKE '%Кофе в зернах%') AND (Volume_Coffee='250 г.') AND (ID_CH='"+ID_CH+"')","SUM(Amount_Coffee)");
        et_Coffee_250g.setText(SumCoffee250g);
        //Заполнение поля Дрип кофе
        String SumDripCoffee = getOneRes("SELECT SUM(Amount_Coffee) FROM `Coffee` WHERE (Name_Coffee LIKE '%Кофе дрип натуральный%') AND (ID_CH='"+ID_CH+"')","SUM(Amount_Coffee)");
        et_DripCoffee.setText(SumDripCoffee);
    }
    //Открыть OpenShiftActivity
    private void startOpenShiftActivity() {
        Intent intent = new Intent(CloseShiftActivity.this,OpenShiftActivity.class);
        intent.putExtra("Login",Login);
        startActivity(intent);
        finish();
    }
    //Получение записи из БД
    private String getOneRes(String inquiry, String column) {
        inquiryGetOneRes = new InquiryGetOneRes();
        inquiryGetOneRes.start(inquiry,column);
        try {
            inquiryGetOneRes.join();
        } catch (InterruptedException e) {
            Log.e("GetOneResWork",e.getMessage());
        }
        return inquiryGetOneRes.res();
    }
    //Добавление записи в таблицу БД
    private void addEntry(String table,String column, String values) {
        inquiryAdd = new InquiryAdd();
        inquiryAdd.start(table,column,values);
        try {
            inquiryAdd.join();
        } catch (InterruptedException e) {
            Log.e("AddOpenShift",e.getMessage());
        }
        //Toast.makeText(this, inquiryAdd.resSuccess()+" в таблицу "+table, Toast.LENGTH_SHORT).show();
    }
    //Инициализация ToolBar
    private void initializationToolbar() {
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        if (toolbar != null){
            setSupportActionBar (toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }
    //получение текущей даты
    private void initializationDate() {
        dateNow = new Date();
        SimpleDateFormat formatForDateNow = new SimpleDateFormat("yyyy-M-d");
        Date = formatForDateNow.format(dateNow);
    }
    //Инициализация элементов Layout
    private void initializationDetailsLayout() {
        et_DiceBox_150 = (EditText) findViewById(R.id.editText_DiceBox_150);
        et_DiceBox_200 = (EditText) findViewById(R.id.editText_DiceBox_200);
        et_DiceBox_300 = (EditText) findViewById(R.id.editText_DiceBox_300);
        et_DiceBox_400 = (EditText) findViewById(R.id.editText_DiceBox_400);
        et_DiceBox_Sum = (EditText) findViewById(R.id.editText_DiceBox_Sum);
        et_Coffee_1kg = (EditText) findViewById(R.id.editText_Coffee_1kg);
        et_Coffee_250g = (EditText) findViewById(R.id.editText_Coffee_250g);
        et_DripCoffee = (EditText) findViewById(R.id.editText_DripCoffee);
        et_Exchenge = (EditText) findViewById(R.id.editText_Exchange);
        et_Comment = (EditText) findViewById(R.id.editText_Comment);


        til_DiceBox_150 = (TextInputLayout) findViewById(R.id.DiceBox_150_layout);
        til_DiceBox_200 = (TextInputLayout) findViewById(R.id.DiceBox_200_layout);
        til_DiceBox_300 = (TextInputLayout) findViewById(R.id.DiceBox_300_layout);
        til_DiceBox_400 = (TextInputLayout) findViewById(R.id.DiceBox_400_layout);
        til_DiceBox_Sum = (TextInputLayout) findViewById(R.id.DiceBox_Sum_layout);
        til_Coffee_1kg = (TextInputLayout) findViewById(R.id.Coffee_1kg_layout);
        til_Coffee_250g = (TextInputLayout) findViewById(R.id.Coffee_250g_layout);
        til_DripCoffee = (TextInputLayout) findViewById(R.id.DripCoffee_layout);
        til_Exchenge = (TextInputLayout) findViewById(R.id.Exchange_layout);
    }
    //Нажатие на кнопку "Закрыть смену"
    public void CloseShift (View view){
        ID_St = getOneRes("SELECT ID_St FROM Storage WHERE (ID_Shift='"+ID_Shift+"') AND (OpenShift_St='0')","ID_St");
        int error = checkingFilling();
        if (ID_St!=null) {
            new MaterialDialog.Builder(this)
                    .onPositive(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            startWorkActivity();
                        }
                    })
                    .title("Сообщение")
                    .positiveColorRes(R.color.colorPrimary)
                    .content("Смена уже была Вами закрыта")
                    .positiveText("Ok")
                    .cancelable(false)
                    .show();
        } else {
            if (error == 0) {
                addEntry("Storage", "(`OpenShift_St`, `DiceBox_150_St`, `DiceBox_200_St`, `DiceBox_300_St`, `DiceBox_400_St`, `DiceBox_Summer_St`, `Coffee_1kg_St`, `Coffee_250g_St`, `DripCoffee_St`, `Exchange_St`, `Comment_St`, `ID_Shift`)",
                        "('0', '"
                                + et_DiceBox_150.getText().toString() + "', '"
                                + et_DiceBox_200.getText().toString() + "', '"
                                + et_DiceBox_300.getText().toString() + "', '"
                                + et_DiceBox_400.getText().toString() + "', '"
                                + et_DiceBox_Sum.getText().toString() + "', '"
                                + et_Coffee_1kg.getText().toString() + "', '"
                                + et_Coffee_250g.getText().toString() + "', '"
                                + et_DripCoffee.getText().toString() + "', '"
                                + et_Exchenge.getText().toString() + "', '"
                                + et_Comment.getText().toString() + "', '"
                                + ID_Shift + "')"
                );
                if (inquiryAdd.resSuccess().equals("Запись добавлена")) {
                    MessageOpenShift = "Смена закрыта";
                } else {
                    MessageOpenShift = "При закрытии смены произошла ошибка. Смена не закрыта.";
                }
                new MaterialDialog.Builder(this)
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                startWorkActivity();
                            }
                        })
                        .title("Сообщение")
                        .positiveColorRes(R.color.colorPrimary)
                        .content(MessageOpenShift)
                        .positiveText("Ok")
                        .cancelable(false)
                        .show();
            }
        }
    }
    //Переход на WorkActivity
    private void startWorkActivity() {
        Intent intent = new Intent(this, WorkActivity.class);
        intent.putExtra("Login",Login);
        startActivity(intent);
        finish();
    }
    //Проверка на заполнение обязательных полей
    private int checkingFilling() {
        int error=0;
        if (et_DiceBox_150.getText().toString().equals(S)) {
            til_DiceBox_150.setErrorEnabled(true);
            til_DiceBox_150.setError(getResources().getString(R.string.error_OpenShift));
            error++;
        }
        if (et_DiceBox_200.getText().toString().equals(S)){
            til_DiceBox_200.setErrorEnabled(true);
            til_DiceBox_200.setError(getResources().getString(R.string.error_OpenShift));
            error++;
        }
        if (et_DiceBox_300.getText().toString().equals(S)){
            til_DiceBox_300.setErrorEnabled(true);
            til_DiceBox_300.setError(getResources().getString(R.string.error_OpenShift));
            error++;
        }
        if (et_DiceBox_400.getText().toString().equals(S)){
            til_DiceBox_400.setErrorEnabled(true);
            til_DiceBox_400.setError(getResources().getString(R.string.error_OpenShift));
            error++;
        }
        if (et_DiceBox_Sum.getText().toString().equals(S)){
            til_DiceBox_Sum.setErrorEnabled(true);
            til_DiceBox_Sum.setError(getResources().getString(R.string.error_OpenShift));
            error++;
        }
        if (et_Coffee_1kg.getText().toString().equals(S)){
            til_Coffee_1kg.setErrorEnabled(true);
            til_Coffee_1kg.setError(getResources().getString(R.string.error_OpenShift));
            error++;
        }
        if (et_Coffee_250g.getText().toString().equals(S)){
            til_Coffee_250g.setErrorEnabled(true);
            til_Coffee_250g.setError(getResources().getString(R.string.error_OpenShift));
            error++;
        }
        if (et_DripCoffee.getText().toString().equals(S)){
            til_DripCoffee.setErrorEnabled(true);
            til_DripCoffee.setError(getResources().getString(R.string.error_OpenShift));
            error++;
        }
        if (et_Exchenge.getText().toString().equals(S)){
            til_Exchenge.setErrorEnabled(true);
            til_Exchenge.setError(getResources().getString(R.string.error_OpenShift));
            error++;
        }
        return error;
    }
    //При нажатиии на кнопку Back в TollBar
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
