package com.kofetochka.kofetochkablg;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.kofetochka.inquiry.InquiryGetArrayOneColumn;
import com.kofetochka.inquiry.InquiryGetOneRes;

import java.text.SimpleDateFormat;
import java.util.Date;

public class NewCoffeeActivity extends AppCompatActivity{
    private String Login;
    private String Date;
    private String SelectNameCoffee;
    private String SelectVolumeCoffee;
    private String ID_CH;
    private String[] Name_Coffee;
    private String[] Volume_Coffee;
    int lenght_NameCoffee;
    int lenght_VolumeCoffee;
    int LENGHT_VOLUMECOFFEE;
    InquiryGetOneRes inquiryGetOneRes;
    InquiryGetArrayOneColumn inquiryGetArrayOneColumn;
    Date dateNow;
    ListView lv_NameCoffee, lv_VolumeCoffee;
    Spinner spinner_AmountCoffee;
    TextView tv_PriceCoffee;
    Button btn_Application, btn_ApplicationPart;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_coffee_layout);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar!=null){
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        lv_NameCoffee = (ListView) findViewById(R.id.listView_NameCoffee);
        lv_VolumeCoffee = (ListView) findViewById(R.id.listView_VolumeCoffee);
        spinner_AmountCoffee = (Spinner)findViewById(R.id.spinner_AmountCoffee);
        tv_PriceCoffee = (TextView) findViewById(R.id.textView_PriceCoffee2);
        btn_Application = (Button) findViewById(R.id.button_Application);
        btn_ApplicationPart = (Button) findViewById(R.id.button_ApplicationPart);

        Login = getIntent().getStringExtra("Login");
        InitializationDate();
        ID_CH = getOneRes("SELECT ID_CH FROM Shift WHERE (Login='"+Login+"') AND (Date_Shift='"+Date+"')","ID_CH");
        Name_Coffee = getArrayOneColumn("SELECT Name_Coffee FROM Coffee WHERE (ID_CH='"+ID_CH+"') AND (Amount_Coffee>0) GROUP BY Name_Coffee","Name_Coffee");

        fillNameCoffee(Name_Coffee);

        lv_NameCoffee.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (lenght_NameCoffee>1){
                    SelectNameCoffee = ((TextView)view).getText().toString();
                    String[] arrayOneNameCoffe = new String[]{SelectNameCoffee};
                    fillNameCoffee(arrayOneNameCoffe);
                    Volume_Coffee = getArrayOneColumn("SELECT Volume_Coffee FROM Coffee WHERE (ID_CH='"+ID_CH+"') AND (Name_Coffee='"+SelectNameCoffee+"') AND (Amount_Coffee>0)","Volume_Coffee");
                    fillVolumeCoffee(Volume_Coffee);
                    LENGHT_VOLUMECOFFEE = Volume_Coffee.length;
                    if (LENGHT_VOLUMECOFFEE==1){
                        SelectVolumeCoffee = Volume_Coffee[0].toString();
                        int length_AmountCoffee = Integer.parseInt(getOneRes("SELECT Amount_Coffee FROM Coffee WHERE (Name_Coffee='"+SelectNameCoffee+"') AND (Volume_Coffee='"+SelectVolumeCoffee+"')","Amount_Coffee"));
                        fillAmountCoffee(length_AmountCoffee);
                        setPriceCoffee();
                        alpha1();
                    }
                } else {
                    SelectNameCoffee = null;
                    fillNameCoffee(Name_Coffee);
                    String[] NullArray = new String[0];
                    fillVolumeCoffee(NullArray);
                    fillAmountCoffee(0);
                    tv_PriceCoffee.setText("0");
                    alpha0();
                }
            }
        });
        lv_VolumeCoffee.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(LENGHT_VOLUMECOFFEE>1) {
                    if (lenght_VolumeCoffee>1) {
                        SelectVolumeCoffee = ((TextView) view).getText().toString();
                        String[] arrayOneVolumeCoffee = new String[]{SelectVolumeCoffee};
                        fillVolumeCoffee(arrayOneVolumeCoffee);
                        int length_AmountCoffee = Integer.parseInt(getOneRes("SELECT Amount_Coffee FROM Coffee WHERE (Name_Coffee='"+SelectNameCoffee+"') AND (Volume_Coffee='"+SelectVolumeCoffee+"') AND (ID_CH='"+ID_CH+"')","Amount_Coffee"));
                        fillAmountCoffee(length_AmountCoffee);
                        setPriceCoffee();
                        alpha1();
                    } else {
                        SelectVolumeCoffee = null;
                        fillVolumeCoffee(Volume_Coffee);
                        tv_PriceCoffee.setText("0");
                        alpha0();
                    }
                } else {
                    SelectVolumeCoffee = ((TextView) view).getText().toString();
                    setPriceCoffee();
                }
            }
        });

        spinner_AmountCoffee.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                setPriceCoffee();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private void fillNameCoffee(String[] name_Coffee) {
        lenght_NameCoffee = name_Coffee.length;
        ArrayAdapter<String> adapter_NameCoffee = new ArrayAdapter<>(this, R.layout.list_item, name_Coffee);
        lv_NameCoffee.setAdapter(adapter_NameCoffee);
        Utility.setListViewHeightBasedOnChildren(lv_NameCoffee);
    }

    private void fillVolumeCoffee(String[] volume_Coffee) {
        lenght_VolumeCoffee = volume_Coffee.length;
        ArrayAdapter<String> adapter_VolumeCoffee = new ArrayAdapter<>(this, R.layout.list_item, volume_Coffee);
        lv_VolumeCoffee.setAdapter(adapter_VolumeCoffee);
        Utility.setListViewHeightBasedOnChildren(lv_VolumeCoffee);
    }

    private void fillAmountCoffee(int length_amountcoffee) {
        String[] array_AmountCoffee = new String[length_amountcoffee];
        for (int i=0;i<length_amountcoffee;i++){
            array_AmountCoffee[i]=Integer.toString(i+1);
        }
        ArrayAdapter<String> adapter_AmountCoffee = new ArrayAdapter<>(this, R.layout.list_item, array_AmountCoffee);
        spinner_AmountCoffee.setAdapter(adapter_AmountCoffee);
    }

    private String[] getArrayOneColumn(String inquiry, String column) {
        String Column = column;
        String Inquiry = inquiry;
        inquiryGetArrayOneColumn = new InquiryGetArrayOneColumn();
        inquiryGetArrayOneColumn.start(inquiry,column);
        try {
            inquiryGetArrayOneColumn.join();
        } catch (InterruptedException e) {
            Log.e("GetArrayOneColumn",e.getMessage());
        }
        int l = inquiryGetArrayOneColumn.resLenght();
        String[] ArrayColumn = new String[l];
        return ArrayColumn = inquiryGetArrayOneColumn.resName_Drink();
    }

    private void InitializationDate() {
        dateNow = new Date();
        SimpleDateFormat formatForDateNow = new SimpleDateFormat("yyyy-M-d");
        Date = formatForDateNow.format(dateNow);
    }

    private String getOneRes(String inquiry, String column) {
        String Column = column;
        String Inquiry = inquiry;
        inquiryGetOneRes = new InquiryGetOneRes();
        inquiryGetOneRes.start(Inquiry,Column);
        try {
            inquiryGetOneRes.join();
        } catch (InterruptedException e) {
            Log.e("GetOneRes NCA",e.getMessage());
        }
        return inquiryGetOneRes.res();
    }

    private void setPriceCoffee(){
        int amountCoffee = Integer.parseInt(spinner_AmountCoffee.getSelectedItem().toString());
        int priceCoffee = Integer.parseInt(getOneRes("SELECT Price_Coffee FROM Coffee WHERE (Name_Coffee='"+SelectNameCoffee+"') AND (Volume_Coffee='"+SelectVolumeCoffee+"') AND (ID_CH='"+ID_CH+"')","Price_Coffee"));
        tv_PriceCoffee.setText(Integer.toString(amountCoffee*priceCoffee));
    }

    private void alpha1(){
        Animation animation = null;
        animation = AnimationUtils.loadAnimation(this,R.anim.alpha1);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                btn_ApplicationPart.setVisibility(View.INVISIBLE);
                btn_Application.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                btn_ApplicationPart.setVisibility(View.VISIBLE);
                btn_Application.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                btn_ApplicationPart.setVisibility(View.VISIBLE);
                btn_Application.setVisibility(View.VISIBLE);
            }
        });
        btn_Application.startAnimation(animation);
        btn_ApplicationPart.startAnimation(animation);
    }

    private void alpha0(){
        Animation animation = null;
        animation = AnimationUtils.loadAnimation(this,R.anim.alpha0);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                btn_ApplicationPart.setVisibility(View.VISIBLE);
                btn_Application.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                btn_ApplicationPart.setVisibility(View.INVISIBLE);
                btn_Application.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                btn_ApplicationPart.setVisibility(View.VISIBLE);
                btn_Application.setVisibility(View.VISIBLE);
            }
        });
        btn_Application.startAnimation(animation);
        btn_ApplicationPart.startAnimation(animation);
    }
}
