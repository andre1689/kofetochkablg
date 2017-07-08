package com.kofetochka.kofetochkablg;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.kofetochka.inquiry.InquiryGetArrayNameAdditives;
import com.kofetochka.inquiry.InquiryGetArrayNameDrink;
import com.kofetochka.inquiry.InquiryGetArrayNameSyrup;
import com.kofetochka.inquiry.InquiryGetArrayVolumeDrink;
import com.kofetochka.inquiry.InquiryGetPriceAdditives;
import com.kofetochka.inquiry.InquiryGetPriceDrink;

public class NewOrderActivity extends AppCompatActivity{

    private final String TABLE_DRINK = "Drink";
    private final String TABLE_ADDITIVES = "Additives";
    private final String TABLE_SYRUP = "Syrup";
    private int l, L;
    private int v, V;
    private int a, A;
    private int s, S;
    private int choise;
    private int Summ = 0;
    private String SelectNameDrink;
    private String SelectVolumeDrink;
    private String SelectSyrup;
    private String PriceDrink;

    String[] arrayName_Drink;
    String[] arrayOneName_Drink;
    String[] arrayVolume_Drink;
    String[] arrayOneVolume_Drink;
    String[] arrayOneSyrup;
    String[] arrayNameAdditives;
    String[] arrayNameSyrup;
    String[] arrayCheckedNameAdditives;
    ListView lv_NameDrink, lv_VolumeDrink, lv_Additives, lv_Syrup;
    Switch switch_additives, switch_Syrup;
    TextView tv_PriceDrink2, tv_PriceAdditives2;

    InquiryGetArrayNameDrink inquiryGetArrayNameDrink;
    InquiryGetArrayVolumeDrink inquiryGetArrayVolumeDrink;
    InquiryGetArrayNameAdditives inquiryGetArrayNameAdditives;
    InquiryGetArrayNameSyrup inquiryGetArrayNameSyrup;
    InquiryGetPriceDrink inquiryGetPriceDrink;
    InquiryGetPriceAdditives inquiryGetPriceAdditives;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_order_layout);

        lv_NameDrink = (ListView) findViewById(R.id.listView_Name);
        lv_VolumeDrink = (ListView) findViewById(R.id.listView_Volume);
        lv_Additives = (ListView) findViewById(R.id.listView_Additives);
        lv_Syrup = (ListView) findViewById(R.id.listView_Syrup);
        switch_additives = (Switch) findViewById(R.id.switch_Additives);
        switch_Syrup = (Switch) findViewById(R.id.switch_Syrup);
        tv_PriceDrink2 = (TextView) findViewById(R.id.textView_PriceDrink2);
        tv_PriceAdditives2 = (TextView) findViewById(R.id.textView_PriceAdditives2);

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        if (toolbar != null){
            setSupportActionBar (toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        getArrayNameDrink();
        String[] Str = new String[0];
        FillingListViewNameDrink(arrayName_Drink);

        lv_NameDrink.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (L>1) {
                    SelectNameDrink = ((TextView) view).getText().toString();
                    arrayOneName_Drink = new String[]{SelectNameDrink};
                    FillingListViewNameDrink(arrayOneName_Drink);
                    getArrayVolumeDrink();

                    if(v==1){
                        SelectVolumeDrink = arrayVolume_Drink[0].toString();
                        getPriceDrink();
                        tv_PriceDrink2.setText(PriceDrink);
                    }
                } else {
                    SelectNameDrink = null;
                    FillingListViewNameDrink(arrayName_Drink);
                    tv_PriceDrink2.setText("0");
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
                    getPriceDrink();
                    tv_PriceDrink2.setText(PriceDrink);
                } else {
                    SelectVolumeDrink = null;
                    FillingListViewVolumeDrink(arrayVolume_Drink);
                    tv_PriceDrink2.setText("0");
                }
            }
        });

        lv_Additives.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                choise = lv_Additives.getCount();
                Summ = 0;
                arrayCheckedNameAdditives = new String[choise];
                SparseBooleanArray sparseBooleanArray = lv_Additives.getCheckedItemPositions();
                for (int i = 0; i < choise; i++) {
                    if (sparseBooleanArray.get(i) == true) {
                        inquiryGetPriceAdditives = new InquiryGetPriceAdditives();
                        inquiryGetPriceAdditives.start(lv_Additives.getItemAtPosition(i).toString());
                        try {
                            inquiryGetPriceAdditives.join();
                        } catch (InterruptedException e) {
                            Log.e("GetPriceAdditives",e.getMessage());
                        }
                        Summ += Integer.parseInt(inquiryGetPriceAdditives.resPrice_Additives());
                    }
                    tv_PriceAdditives2.setText(Integer.toString(Summ));
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
                } else {
                    SelectSyrup = null;
                    FillingListViewNameSyrup(arrayNameSyrup);
                }
            }
        });
    }

    private void getPriceDrink() {
        inquiryGetPriceDrink = new InquiryGetPriceDrink();
        inquiryGetPriceDrink.start(SelectNameDrink,SelectVolumeDrink);
        try {
            inquiryGetPriceDrink.join();
        } catch (InterruptedException e) {
            Log.e("GetPriceDrink",e.getMessage());
        }
        PriceDrink = inquiryGetPriceDrink.resPrice_Drink();
    }


    private void getArrayNameSyrup() {
        inquiryGetArrayNameSyrup = new InquiryGetArrayNameSyrup();
        inquiryGetArrayNameSyrup.start(TABLE_SYRUP);
        try {
            inquiryGetArrayNameSyrup.join();
        } catch (InterruptedException e) {
            Log.e("GetArrayNameSyrup",e.getMessage());
        }
        s = inquiryGetArrayNameSyrup.resLenght();
        arrayNameSyrup = new String[s];
        arrayNameSyrup = inquiryGetArrayNameSyrup.resName_Syrup();
    }
    private void getArrayNameAdditives() {
        inquiryGetArrayNameAdditives = new InquiryGetArrayNameAdditives();
        inquiryGetArrayNameAdditives.start(TABLE_ADDITIVES);
        try {
            inquiryGetArrayNameAdditives.join();
        } catch (InterruptedException e) {
            Log.e("GetArrayNameAdditives",e.getMessage());
        }
        a = inquiryGetArrayNameAdditives.resLenght();
        arrayNameAdditives = new String[a];
        arrayNameAdditives = inquiryGetArrayNameAdditives.resName_Additives();
    }
    private void getArrayNameDrink() {
        inquiryGetArrayNameDrink = new InquiryGetArrayNameDrink();
        inquiryGetArrayNameDrink.start(TABLE_DRINK);
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
        Utility.setListViewHeightBasedOnChildren(lv_VolumeDrink);
    }
    private void FillingListViewNameDrink(String[] arrayname_drink) {
        L = arrayname_drink.length;
        ArrayAdapter<String> adapter_name = new ArrayAdapter<>(this, R.layout.list_item, arrayname_drink);
        lv_NameDrink.setAdapter(adapter_name);
        Utility.setListViewHeightBasedOnChildren(lv_NameDrink);
    }
    private void FillingListViewNameAdditives(String[] arrayname_additives) {
        A = arrayname_additives.length;
        ArrayAdapter<String> adapter_additives = new ArrayAdapter<>(this, R.layout.list_item_2, arrayname_additives);
        lv_Additives.setAdapter(adapter_additives);
        Utility.setListViewHeightBasedOnChildren(lv_Additives);
    }
    private void FillingListViewNameSyrup(String[] arrayname_syrup) {
        S = arrayname_syrup.length;
        ArrayAdapter<String> adapter_syrup = new ArrayAdapter<>(this, R.layout.list_item, arrayname_syrup);
        lv_Syrup.setAdapter(adapter_syrup);
        Utility.setListViewHeightBasedOnChildren(lv_Syrup);
    }
}
