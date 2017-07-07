package com.kofetochka.kofetochkablg;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.kofetochka.inquiry.InquiryGetArrayNameDrink;
import com.kofetochka.inquiry.InquiryGetArrayVolumeDrink;

public class NewOrderActivity extends AppCompatActivity{

    private final String TABLE = "Drink";
    private int l, L;
    private int v, V;
    private String SelectNameDrink;
    private String SelectVolumeDrink;

    String[] arrayName_Drink;
    String[] arrayOneName_Drink;
    String[] arrayVolume_Drink;
    String[] arrayOneVolume_Drink;
    ListView lv_NameDrink, lv_VolumeDrink;

    InquiryGetArrayNameDrink inquiryGetArrayNameDrink;
    InquiryGetArrayVolumeDrink inquiryGetArrayVolumeDrink;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_order_layout);

        lv_NameDrink = (ListView) findViewById(R.id.listView_Name);
        lv_VolumeDrink = (ListView) findViewById(R.id.listView_Volume);

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        if (toolbar != null){
            setSupportActionBar (toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        getArrayNameDrink();
        FillingListViewNameDrink(arrayName_Drink);

        lv_NameDrink.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (L>1) {
                    SelectNameDrink = ((TextView) view).getText().toString();
                    arrayOneName_Drink = new String[]{SelectNameDrink};
                    FillingListViewNameDrink(arrayOneName_Drink);
                    getArrayVolumeDrink();
                } else {
                    SelectNameDrink = null;
                    FillingListViewNameDrink(arrayName_Drink);
                }
            }
        });

        lv_VolumeDrink.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (V>1){
                    SelectVolumeDrink = ((TextView)view).getText().toString();
                    arrayOneVolume_Drink = new String[]{SelectVolumeDrink};
                    FillingListViewVolumeDrink(arrayOneVolume_Drink);
                } else {
                    SelectVolumeDrink = null;
                    FillingListViewVolumeDrink(arrayVolume_Drink);
                }
            }
        });
    }

    private void getArrayNameDrink() {
        inquiryGetArrayNameDrink = new InquiryGetArrayNameDrink();
        inquiryGetArrayNameDrink.start(TABLE);
        try {
            inquiryGetArrayNameDrink.join();
        } catch (InterruptedException e) {
            Log.e("GetArrayNameDrink", e.getMessage());
        }
        l = inquiryGetArrayNameDrink.resLenght();
        arrayName_Drink = new String[l];
        arrayName_Drink = inquiryGetArrayNameDrink.resName_Drink();
    }
    private void getArrayVolumeDrink() {
        inquiryGetArrayVolumeDrink = new InquiryGetArrayVolumeDrink();
        inquiryGetArrayVolumeDrink.start(SelectNameDrink);
        try {
            inquiryGetArrayVolumeDrink.join();
        } catch (InterruptedException e) {
            Log.e("GetArrayVolumeDrink", e.getMessage());
        }
        v = inquiryGetArrayVolumeDrink.resLenght();
        arrayVolume_Drink = new String[v];
        arrayVolume_Drink = inquiryGetArrayVolumeDrink.resVolume_Drink();
        FillingListViewVolumeDrink(arrayVolume_Drink);
    }
    private void FillingListViewVolumeDrink(String[] arrayvolume_drink) {
        V = arrayvolume_drink.length;
        ArrayAdapter<String> adapter_volume = new ArrayAdapter<>(this, R.layout.list_item, arrayvolume_drink);
        lv_VolumeDrink.setAdapter(adapter_volume);
    }
    private void FillingListViewNameDrink(String[] arrayname_drink) {
        L = arrayname_drink.length;
        ArrayAdapter<String> adapter_name = new ArrayAdapter<>(this, R.layout.list_item, arrayname_drink);
        lv_NameDrink.setAdapter(adapter_name);
    }
}
