package com.kofetochka.kofetochkablg;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.Layout;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.kofetochka.inquiry.InquiryAdd;
import com.kofetochka.inquiry.InquiryAddShift;
import com.kofetochka.inquiry.InquiryAddStorage;
import com.kofetochka.inquiry.InquiryCoffeeHouse;
import com.kofetochka.inquiry.InquiryCoffeeHouseID;
import com.kofetochka.inquiry.InquiryGetArrayOneColumn;
import com.kofetochka.inquiry.InquiryGetIDShiftByDateID_CH;
import com.kofetochka.inquiry.InquiryGetOneRes;
import com.kofetochka.inquiry.InquiryGetShiftID;
import com.kofetochka.inquiry.InquiryGetStroageByDateCloseShift;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.concurrent.ExecutionException;

public class OpenShift extends AppCompatActivity{

    private String SelectName_CH = null;
    private String ID_CH = null;
    private String Date;
    private String DateBack1day;
    private String Time;
    private String ID_Shift=null;
    private String ID_Shift_BackDate;
    private String Login;
    private String Surname;
    private String Name;
    private String NameRole;
    private String MessageOpenShift;
    private String DiceBox_150, DiceBox_200, DiceBox_300, DiceBox_400, DiceBox_Sum, Coffee_1kg, Coffee_250g, DripCoffee, Exchenge;
    private TextInputLayout til_DiceBox_150, til_DiceBox_200, til_DiceBox_300, til_DiceBox_400, til_DiceBox_Sum, til_Coffee_1kg, til_Coffee_250g, til_DripCoffee, til_Exchenge;
    int sum_error;
    int sum_click=0;
    private String S = "";
    ProgressBar pb_OpenShift;

    String[] arrayName_CH;
    int lenght_NameCH;

    EditText et_DiceBox_150, et_DiceBox_200, et_DiceBox_300, et_DiceBox_400, et_DiceBox_Sum, et_Coffee_1kg, et_Coffee_250g, et_DripCoffee, et_Exchenge;
    EditText et_Comment;
    TextView tv_meter;
    ImageView iv_DiceBox_150, iv_DiceBox_200, iv_DiceBox_300, iv_DiceBox_400, iv_DiceBox_Sum, iv_Coffee_1kg, iv_Coffee_250g, iv_DripCoffee, iv_Exchenge;
    Date dateNow;
    Calendar calendar;
    ListView lv_NameCH;
    View content;
    LinearLayout ll_openshift;

    InquiryGetArrayOneColumn inquiryGetArrayOneColumn;
    InquiryGetOneRes inquiryGetOneRes;
    InquiryAdd inquiryAdd;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.openshift_layout);
        //Делаем LinerLayout невидимым при загрузке Activity
        ll_openshift = (LinearLayout) findViewById(R.id.OpenShift_layout);
        ll_openshift.setVisibility(View.INVISIBLE);
        //Получаем переменные из другого Activity
        Login = getIntent().getStringExtra("Login");
        Surname = getIntent().getStringExtra("Surname");
        Name = getIntent().getStringExtra("Name");
        NameRole = getIntent().getStringExtra("NameRole");
        //Инициализация элеметов Layout
        InitializationDetailsLayout();
        //Инициализация Toolbar
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        if (toolbar != null){
            setSupportActionBar (toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        //Инициализация текущеого времени и даты, а также даты вчерашенго дня
        InitializationDateAndTime();
        //Инициализация массива содержащего названия кофейнь
        InitializationArrayNameCH();
        //Обработчик событий при выборе кафейни
        lv_NameCH.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (lenght_NameCH>1){ //если отображается более, чем 1 кофейня
                    SelectName_CH = ((TextView)view).getText().toString();
                    String[] arrayOneNameCH = new String[] {SelectName_CH};
                    fillNameCoffeeHouse(arrayOneNameCH);
                    ll_openshift.setVisibility(View.VISIBLE);
                    ID_CH = getOneRes("SELECT ID_CH FROM CoffeeHouse WHERE Name_CH='"+SelectName_CH+"'","ID_CH");
                    //Заполнение поля Кофе в зернах 1 кг
                    String SumCoffee1kg = getOneRes("SELECT SUM(Amount_Coffee) FROM `Coffee` WHERE (Name_Coffee LIKE '%Кофе в зернах%') AND (Volume_Coffee='1000 г.') AND (ID_CH='"+ID_CH+"')","SUM(Amount_Coffee)");
                    et_Coffee_1kg.setText(SumCoffee1kg);
                    //Заполнение поля Кофе в зернах 250 г
                    String SumCoffee250g = getOneRes("SELECT SUM(Amount_Coffee) FROM `Coffee` WHERE (Name_Coffee LIKE '%Кофе в зернах%') AND (Volume_Coffee='250 г.') AND (ID_CH='"+ID_CH+"')","SUM(Amount_Coffee)");
                    et_Coffee_250g.setText(SumCoffee250g);
                    //Заполнение поля Дрип кофе
                    String SumDripCoffee = getOneRes("SELECT SUM(Amount_Coffee) FROM `Coffee` WHERE (Name_Coffee LIKE '%Кофе дрип натуральный%') AND (ID_CH='"+ID_CH+"')","SUM(Amount_Coffee)");
                    et_DripCoffee.setText(SumDripCoffee);
                } else { //если отображается одна кофейня
                    fillNameCoffeeHouse(arrayName_CH);
                    ll_openshift.setVisibility(View.INVISIBLE);
                    ID_CH=null;
                }
            }
        });
        //Обрабоччик ввода текста в EditText (Коментарии)
        et_Comment.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                tv_meter.setText(et_Comment.getText().length()+"/100");
            }
        });
        //Убираем сообщение об ошибке ввода, если вводят символ
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
    //Получения записи из БД
    private String getOneRes(String inquiry, String column) {
        inquiryGetOneRes = new InquiryGetOneRes();
        inquiryGetOneRes.start(inquiry,column);
        try {
            inquiryGetOneRes.join();
        } catch (InterruptedException e) {
            Log.e("GetOneRes.OpenSh",e.getMessage());
        }
        return inquiryGetOneRes.res();

    }
    //Инициализация элементов Layout
    private void InitializationDetailsLayout() {
        pb_OpenShift = (ProgressBar) findViewById(R.id.progressBar_OpenShift);
        lv_NameCH = (ListView) findViewById(R.id.listView_NameCoffeeHouse);

        content = (View) findViewById(R.id.coordinator);

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

        tv_meter = (TextView) findViewById(R.id.textView_Meter);

        iv_DiceBox_150 = (ImageView) findViewById(R.id.imageView_DiceBox_150);
        iv_DiceBox_200 = (ImageView) findViewById(R.id.imageView_DiceBox_200);
        iv_DiceBox_300 = (ImageView) findViewById(R.id.imageView_DiceBox_300);
        iv_DiceBox_400 = (ImageView) findViewById(R.id.imageView_DiceBox_400);
        iv_DiceBox_Sum = (ImageView) findViewById(R.id.imageView_DiceBox_Sum);
        iv_Coffee_1kg = (ImageView) findViewById(R.id.imageView_Coffee_1kg);
        iv_Coffee_250g = (ImageView) findViewById(R.id.imageView_Coffee_250g);
        iv_DripCoffee = (ImageView) findViewById(R.id.imageView_DripCoffee);
        iv_Exchenge = (ImageView) findViewById(R.id.imageView_Exchange);

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
    //Получение массива наименований кофейнь
    private void InitializationArrayNameCH() {
        String Inquiry = "SELECT Name_CH FROM CoffeeHouse";
        String Column = "Name_CH";
        arrayName_CH = getArrayOneColumn(Inquiry,Column);
        fillNameCoffeeHouse(arrayName_CH);
    }
    //Заполнение ListView массивом наименований кофейнь
    private void fillNameCoffeeHouse(String[] name_CH) {
        lenght_NameCH = name_CH.length;
        ArrayAdapter<String> adapter_NameCH = new ArrayAdapter<>(this, R.layout.list_item, name_CH);
        lv_NameCH.setAdapter(adapter_NameCH);
        Utility.setListViewHeightBasedOnChildren(lv_NameCH);
    }
    //Полчуние массива из БД
    private String[] getArrayOneColumn(String inquiry, String column) {
        inquiryGetArrayOneColumn = new InquiryGetArrayOneColumn();
        inquiryGetArrayOneColumn.start(inquiry, column);
        try {
            inquiryGetArrayOneColumn.join();
        } catch (InterruptedException e) {
            Log.e("GetArrayNameCH",e.getMessage());
        }
        return inquiryGetArrayOneColumn.resColumn();
    }
    //Нажатие на кнопку "Открыть смену"
    public void openShift(View view){
        int error = 0;
        error = checkingFilling();
        //Если все поля заполнены, то проверяем, не была ли смена уже открыта данным пользователем
        if(error==0){
            ID_Shift = getOneRes("SELECT ID_Shift FROM Shift WHERE (Date_Shift='"+Date+"') AND (ID_CH='"+ID_CH+"') AND (Login='"+Login+"')","ID_Shift");
            if(ID_Shift!=null){
                Snackbar.make(content, "Смена уже была Вами открыта", Snackbar.LENGTH_LONG).show();
            } else {
                //Добовляет смену
                addEntry("Shift","(`Date_Shift`,`Time_Shift`,`ID_CH`,`Login`)","('"+Date+"', '"+Time+"', '"+ID_CH+"', '"+Login+"')");
                //Получаем ID добавленной нами смены
                ID_Shift = getOneRes("SELECT ID_Shift FROM Shift WHERE (Date_Shift='"+Date+"') AND (ID_CH='"+ID_CH+"') AND (Login='"+Login+"')","ID_Shift");
                //Добавляем запись в таблицу Склад
                CheckingReviseStorageOpenShift();
                addEntry("Storage","(`OpenShift_St`, `DiceBox_150_St`, `DiceBox_200_St`, `DiceBox_300_St`, `DiceBox_400_St`, `DiceBox_Summer_St`, `Coffee_1kg_St`, `Coffee_250g_St`, `DripCoffee_St`, `Exchange_St`, `Comment_St`, `ID_Shift`, `SumError_St`, `SumClick_St`)",
                        "('1', '"
                        +et_DiceBox_150.getText().toString()+"', '"
                        +et_DiceBox_200.getText().toString()+"', '"
                        +et_DiceBox_300.getText().toString()+"', '"
                        +et_DiceBox_400.getText().toString()+"', '"
                        +et_DiceBox_Sum.getText().toString()+"', '"
                        +et_Coffee_1kg.getText().toString()+"', '"
                        +et_Coffee_250g.getText().toString()+"', '"
                        +et_DripCoffee.getText().toString()+"', '"
                        +et_Exchenge.getText().toString()+"', '"
                        +et_Comment.getText().toString()+"', '"
                        +ID_Shift+"', '"
                        +sum_error+"', '"
                        +sum_click+"')"
                        );
                if (inquiryAdd.resSuccess().equals("Запись добавлена")){
                    MessageOpenShift = "Смена открыта";
                } else {
                    MessageOpenShift = "При открытии смены произошла ошибка. Смена не открыта.";
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
                        .show();
                //Закрываем наше Activity и преходим в WorkActivity
                //startWorkActivity();
            }
        }
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
    //Нажатие на кнопку "Сравнить с прошлой сменой"
    public void reviseStorage(View view) {
        sum_click++;
        int error = 0;
        error = checkingFilling();
        if (error==0){
            ID_Shift_BackDate = getOneRes("SELECT ID_Shift FROM Shift WHERE (Date_Shift='"+DateBack1day+"') AND (ID_CH='"+ID_CH+"')","ID_Shift");
            DiceBox_150 = getOneRes("SELECT DiceBox_150_St FROM Storage WHERE (ID_Shift='"+ID_Shift_BackDate+"') AND (OpenShift_St='0')","DiceBox_150_St");
            DiceBox_200 = getOneRes("SELECT DiceBox_200_St FROM Storage WHERE (ID_Shift='"+ID_Shift_BackDate+"') AND (OpenShift_St='0')","DiceBox_200_St");
            DiceBox_300 = getOneRes("SELECT DiceBox_300_St FROM Storage WHERE (ID_Shift='"+ID_Shift_BackDate+"') AND (OpenShift_St='0')","DiceBox_300_St");
            DiceBox_400 = getOneRes("SELECT DiceBox_400_St FROM Storage WHERE (ID_Shift='"+ID_Shift_BackDate+"') AND (OpenShift_St='0')","DiceBox_400_St");
            DiceBox_Sum = getOneRes("SELECT DiceBox_Summer_St FROM Storage WHERE (ID_Shift='"+ID_Shift_BackDate+"') AND (OpenShift_St='0')","DiceBox_Summer_St");
            Coffee_1kg = getOneRes("SELECT Coffee_1kg_St FROM Storage WHERE (ID_Shift='"+ID_Shift_BackDate+"') AND (OpenShift_St='0')","Coffee_1kg_St");
            Coffee_250g = getOneRes("SELECT Coffee_250g_St FROM Storage WHERE (ID_Shift='"+ID_Shift_BackDate+"') AND (OpenShift_St='0')","Coffee_250g_St");
            DripCoffee = getOneRes("SELECT DripCoffee_St FROM Storage WHERE (ID_Shift='"+ID_Shift_BackDate+"') AND (OpenShift_St='0')","DripCoffee_St");
            Exchenge = getOneRes("SELECT Exchange_St FROM Storage WHERE (ID_Shift='"+ID_Shift_BackDate+"') AND (OpenShift_St='0')","Exchange_St");
            CheckingReviseStorage();
        }
    }
    //Переход на WorkActivity
    private void startWorkActivity() {
        Intent intent = new Intent(this,WorkActivity.class);
        intent.putExtra("Login",Login);
        intent.putExtra("NameRole",NameRole);
        intent.putExtra("Surname", Surname);
        intent.putExtra("Name", Name);
        startActivity(intent);
        finish();
    }
    //Получение текущей даты и времени, а также даты вчерашнего дня
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
    //Сравнение данных с прошлой сменой (с ImageView)
    private void CheckingReviseStorage(){
        sum_error=0;
        if (et_DiceBox_150.getText().toString().equals(DiceBox_150)) {
            iv_DiceBox_150.setImageResource(R.mipmap.ic_check_black_18dp);
        } else {
            iv_DiceBox_150.setImageResource(R.mipmap.ic_close_black_18dp);
            sum_error++;
        }

        if (et_DiceBox_200.getText().toString().equals(DiceBox_200)){
            iv_DiceBox_200.setImageResource(R.mipmap.ic_check_black_18dp);
        } else {
            iv_DiceBox_200.setImageResource(R.mipmap.ic_close_black_18dp);
            sum_error++;
        }

        if (et_DiceBox_300.getText().toString().equals(DiceBox_300)){
            iv_DiceBox_300.setImageResource(R.mipmap.ic_check_black_18dp);
        } else {
            iv_DiceBox_300.setImageResource(R.mipmap.ic_close_black_18dp);
            sum_error++;
        }

        if (et_DiceBox_400.getText().toString().equals(DiceBox_400)){
            iv_DiceBox_400.setImageResource(R.mipmap.ic_check_black_18dp);
        } else {
            iv_DiceBox_400.setImageResource(R.mipmap.ic_close_black_18dp);
            sum_error++;
        }

        if (et_DiceBox_Sum.getText().toString().equals(DiceBox_Sum)){
            iv_DiceBox_Sum.setImageResource(R.mipmap.ic_check_black_18dp);
        } else {
            iv_DiceBox_Sum.setImageResource(R.mipmap.ic_close_black_18dp);
            sum_error++;
        }

        if (et_Coffee_1kg.getText().toString().equals(Coffee_1kg)){
            iv_Coffee_1kg.setImageResource(R.mipmap.ic_check_black_18dp);
        } else {
            iv_Coffee_1kg.setImageResource(R.mipmap.ic_close_black_18dp);
            sum_error++;
        }

        if (et_Coffee_250g.getText().toString().equals(Coffee_250g)){
            iv_Coffee_250g.setImageResource(R.mipmap.ic_check_black_18dp);
        } else {
            iv_Coffee_250g.setImageResource(R.mipmap.ic_close_black_18dp);
            sum_error++;
        }

        if (et_DripCoffee.getText().toString().equals(DripCoffee)){
            iv_DripCoffee.setImageResource(R.mipmap.ic_check_black_18dp);
        } else {
            iv_DripCoffee.setImageResource(R.mipmap.ic_close_black_18dp);
            sum_error++;
        }

        if (et_Exchenge.getText().toString().equals(Exchenge)){
            iv_Exchenge.setImageResource(R.mipmap.ic_check_black_18dp);
        } else {
            iv_Exchenge.setImageResource(R.mipmap.ic_close_black_18dp);
            sum_error++;
        }
    }
    //Сравнение данных с прошлой сменой (без ImageView)
    private void CheckingReviseStorageOpenShift(){
        ID_Shift_BackDate = getOneRes("SELECT ID_Shift FROM Shift WHERE (Date_Shift='"+DateBack1day+"') AND (ID_CH='"+ID_CH+"')","ID_Shift");
        DiceBox_150 = getOneRes("SELECT DiceBox_150_St FROM Storage WHERE (ID_Shift='"+ID_Shift_BackDate+"') AND (OpenShift_St='0')","DiceBox_150_St");
        DiceBox_200 = getOneRes("SELECT DiceBox_200_St FROM Storage WHERE (ID_Shift='"+ID_Shift_BackDate+"') AND (OpenShift_St='0')","DiceBox_200_St");
        DiceBox_300 = getOneRes("SELECT DiceBox_300_St FROM Storage WHERE (ID_Shift='"+ID_Shift_BackDate+"') AND (OpenShift_St='0')","DiceBox_300_St");
        DiceBox_400 = getOneRes("SELECT DiceBox_400_St FROM Storage WHERE (ID_Shift='"+ID_Shift_BackDate+"') AND (OpenShift_St='0')","DiceBox_400_St");
        DiceBox_Sum = getOneRes("SELECT DiceBox_Summer_St FROM Storage WHERE (ID_Shift='"+ID_Shift_BackDate+"') AND (OpenShift_St='0')","DiceBox_Summer_St");
        Coffee_1kg = getOneRes("SELECT Coffee_1kg_St FROM Storage WHERE (ID_Shift='"+ID_Shift_BackDate+"') AND (OpenShift_St='0')","Coffee_1kg_St");
        Coffee_250g = getOneRes("SELECT Coffee_250g_St FROM Storage WHERE (ID_Shift='"+ID_Shift_BackDate+"') AND (OpenShift_St='0')","Coffee_250g_St");
        DripCoffee = getOneRes("SELECT DripCoffee_St FROM Storage WHERE (ID_Shift='"+ID_Shift_BackDate+"') AND (OpenShift_St='0')","DripCoffee_St");
        Exchenge = getOneRes("SELECT Exchange_St FROM Storage WHERE (ID_Shift='"+ID_Shift_BackDate+"') AND (OpenShift_St='0')","Exchange_St");
        sum_error=0;
        if (!et_DiceBox_150.getText().toString().equals(DiceBox_150)) {
            sum_error++;
        }
        if (!et_DiceBox_200.getText().toString().equals(DiceBox_200)){
            sum_error++;
        }
        if (!et_DiceBox_300.getText().toString().equals(DiceBox_300)){
            sum_error++;
        }
        if (!et_DiceBox_400.getText().toString().equals(DiceBox_400)){
            sum_error++;
        }
        if (!et_DiceBox_Sum.getText().toString().equals(DiceBox_Sum)){
            sum_error++;
        }
        if (!et_Coffee_1kg.getText().toString().equals(Coffee_1kg)){
            sum_error++;
        }
        if (!et_Coffee_250g.getText().toString().equals(Coffee_250g)){
            sum_error++;
        }
        if (!et_DripCoffee.getText().toString().equals(DripCoffee)){
            sum_error++;
        }
        if (!et_Exchenge.getText().toString().equals(Exchenge)){
            sum_error++;
        }
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
