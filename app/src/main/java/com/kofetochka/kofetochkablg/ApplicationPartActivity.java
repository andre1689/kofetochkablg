package com.kofetochka.kofetochkablg;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.kofetochka.adapter.ApplicationPartListAdapter;
import com.kofetochka.dto.AddEntryDTO;
import com.kofetochka.dto.ApplicationPartDTO;
import com.kofetochka.dto.DeleteEntryDTO;
import com.kofetochka.dto.GetArrayOneColumnDTO;
import com.kofetochka.dto.GetOneResDTO;
import com.kofetochka.dto.UpdateEntryDTO;
import com.kofetochka.inquiry.InquiryGetArrayOneColumn;
import com.kofetochka.inquiry.InquiryGetOneRes;

import java.util.ArrayList;
import java.util.List;

public class ApplicationPartActivity extends AppCompatActivity{

    String ID_Application;
    String[] array_additives;
    String[] array_ID_AP;
    String Login;
    int lenghtAdditives=0;
    int lenghtAP=0;
    FloatingActionButton floatingActionButtonDrink, floatingActionButtonCoffee;
    List<ApplicationPartDTO> DataSet;
    ApplicationPartListAdapter recyclerAdapter;

    InquiryGetOneRes inquiryGetOneRes;
    InquiryGetArrayOneColumn inquiryGetArrayOneColumn;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.applictionpart_layout);

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        if (toolbar != null){
            setSupportActionBar (toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        ID_Application = getIntent().getStringExtra("ID_Application");
        Login = getIntent().getStringExtra("Login");
        array_ID_AP = getArrayOneColumn("SELECT ID_AP FROM Application_Part WHERE ID_Application='"+ID_Application+"'","ID_AP");
        lenghtAP = array_ID_AP.length;

        floatingActionButtonDrink = (FloatingActionButton) findViewById(R.id.action_drink);
        floatingActionButtonDrink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ApplicationPartActivity.this, NewDrinkActivity.class);
                intent.putExtra("Login",Login);
                intent.putExtra("ID_Application",ID_Application);
                startActivity(intent);
            }
        });

        final List<ApplicationPartDTO> myDataset = getDataSet();
        RecyclerView rv = (RecyclerView) findViewById(R.id.recyclerView);
        rv.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        rv.setLayoutManager(layoutManager);
        recyclerAdapter = new ApplicationPartListAdapter(myDataset);
        rv.setAdapter(recyclerAdapter);
        recyclerAdapter.setOnItemClickListener(new ApplicationPartListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View itemView, int position, View view) {
                ShowPopupMenuMain(view,position);
            }
        });
    }
    //Копирование item
    private void CopyItem(int position){
        GetOneResDTO getOneResDTO = new GetOneResDTO();
        AddEntryDTO addEntryDTO = new AddEntryDTO();
        //Узнаем ID_AP копируемого item
        String ID_AP_position = DataSet.get(position).getID_AP();
        //По ID_AP копируемого item узнаем все остальные данные необходимые для копирования
        String ID_Application_position = getOneResDTO.getOneResDTO("SELECT ID_Application FROM Application_Part WHERE ID_AP='"+ID_AP_position+"'","ID_Application");
        String ID_Drink_position = getOneResDTO.getOneResDTO("SELECT ID_Drink FROM Application_Part WHERE ID_AP='"+ID_AP_position+"'","ID_Drink");
        String Sum_AP_position = getOneResDTO.getOneResDTO("SELECT Sum_AP FROM Application_Part WHERE ID_AP='"+ID_AP_position+"'","Sum_AP");
        //если стаканчик бесплатный
        if (Integer.parseInt(Sum_AP_position)==0)
        {
            //Выводим сообщение о невозможности его копирования
            new MaterialDialog.Builder(ApplicationPartActivity.this)
                    .title("Сообщение")
                    .positiveColorRes(R.color.colorPrimary)
                    .content("Неозможно скопировать бесплатный стаканчик")
                    .positiveText("Ok")
                    .cancelable(false)
                    .show();
        } else {
            //Получение максимального значения ID_AP
            String Max_AP = getOneResDTO.getOneResDTO("SELECT MAX(ID_AP) AS ID_AP FROM Application_Part", "ID_AP");
            //Добавляем запись в таблицу Application_Part
            String ID_AP; //Переменная для хранения нового ID_AP
            if (Max_AP == "null") { //если таблица Application_Part пустая
                ID_AP = "1";
                addEntryDTO.AddEntry("Application_Part", "(`ID_AP`, `ID_Application`, `ID_Drink`, `Sum_AP`)", "('1', '" + ID_Application_position + "', '" + ID_Drink_position + "', '" + Sum_AP_position + "')");
            } else { //если в таблице Application_Part есть записи
                int AP = Integer.parseInt(Max_AP) + 1;
                ID_AP = Integer.toString(AP);
                addEntryDTO.AddEntry("Application_Part", "(`ID_AP`, `ID_Application`, `ID_Drink`, `Sum_AP`)", "('" + ID_AP + "', '" + ID_Application_position + "', '" + ID_Drink_position + "', '" + Sum_AP_position + "')");
            }
            //Проверяем есть ли в копируемом item сиропы
            String ID_Syrup_position = getOneResDTO.getOneResDTO("SELECT ID_Syrup FROM ApplicationPart_Syrup WHERE ID_AP='" + ID_AP_position + "'", "ID_Syrup");
            //Если сироп есть, то добавляем запись в таблицу ApplicationPart_Syrup с новым ID_AP
            if (ID_Syrup_position != null) {
                addEntryDTO.AddEntry("ApplicationPart_Syrup", "(`ID_AP`, `ID_Syrup`)", "('" + ID_AP + "', '" + ID_Syrup_position + "')");
            }
            //Проверяем есть ли в копируемом item добавки
            GetArrayOneColumnDTO getArrayOneColumnDTO = new GetArrayOneColumnDTO();
            String[] ArrayAdditives = getArrayOneColumnDTO.getArrayOneColumn("SELECT ID_Additives FROM ApplicationPart_Additives WHERE ID_AP='" + ID_AP_position + "'", "ID_Additives");
            //Количество добавок
            int LengthAdditives = getArrayOneColumnDTO.getLenght();
            //Если добавка одна
            if (LengthAdditives == 1) {
                //кипруем добавку в новым ID_AP
                addEntryDTO.AddEntry("ApplicationPart_Additives", "(`ID_AP`, `ID_Additives`)", "('" + ID_AP + "', '" + ArrayAdditives[0] + "')");
                //Если добавок больше 1
            } else if (LengthAdditives > 1) {
                //С помощью цыкла записываем все добавки в БД с новым ID_AP
                for (int i = 0; i < ArrayAdditives.length; i++) {
                    addEntryDTO.AddEntry("ApplicationPart_Additives", "(`ID_AP`, `ID_Additives`)", "('" + ID_AP + "', '" + ArrayAdditives[i] + "')");
                }
            }
            //Добавляем копию item в RecycleeView
            ApplicationPartDTO newAPDTO = new ApplicationPartDTO(DataSet.get(position).getTitle(), DataSet.get(position).getSyrup(), DataSet.get(position).getAdditives(), DataSet.get(position).getPrice(), ID_AP, "");
            recyclerAdapter.addItem(position + 1, newAPDTO);
            //Известим адаптер о добавлении элемента
            recyclerAdapter.notifyItemInserted(position + 1);
        }
    }
    //Редактирование item
    private void EditItem(int position){
        String ID_AP_position = DataSet.get(position).getID_AP();
        Intent intent = new Intent(this,EditDrinkActivity.class);
        intent.putExtra("Login", Login);
        intent.putExtra("ID_AP",ID_AP_position);
        intent.putExtra("ID_Application", ID_Application);
        startActivity(intent);
    }
    //Удаление item
    private void DeleteItem(final int position){
        final DeleteEntryDTO deleteEntryDTO = new DeleteEntryDTO();
        new MaterialDialog.Builder(this)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        String res = deleteEntryDTO.DeleteEntry("DELETE FROM `Application_Part` WHERE `ID_AP` ="+DataSet.get(position).getID_AP());
                        //Удалим элемент из набора данных адаптера
                        recyclerAdapter.deleteItem(position);
                        //И уведомим об этом адаптер
                        recyclerAdapter.notifyItemRemoved(position);
                    }
                })
                /*.onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {

                    }
                })*/
                .title("Сообщение")
                .positiveColorRes(R.color.colorPrimary)
                .negativeColorRes(R.color.colorPrimary)
                .content("Вы действительно хотите удалить выбранный пункт?")
                .positiveText("ДА")
                .negativeText("НЕТ")
                .cancelable(false)
                .show();
        //String res = deleteEntryDTO.DeleteEntry("DELETE FROM `Application_Part` WHERE `ID_AP` ="+DataSet.get(position).getID_AP());
        //Toast.makeText(this, res, Toast.LENGTH_SHORT).show();

    }
    //Всплывающее меню item
    private void ShowPopupMenuMain (final View v, final int position){
        PopupMenu popupMenu = new PopupMenu(this, v);
        popupMenu.inflate(R.menu.popupmenu_ap);
        //Обработчик нажатия на пункт меню
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    //Если выбрано Копировать
                    case R.id.menu_copy:
                        CopyItem(position);
                        return true;
                    //Если выбрано Редактировать
                    case R.id.menu_edit:
                        EditItem(position);
                        return true;
                    //Если выбрано Удалить
                    case R.id.menu_delete:
                        DeleteItem(position);
                        return true;
                    //Если выбрано Бемплатно
                    case R.id.menu_free:
                        ShowPopupMenu(v,position);
                        return true;
                    default:
                        return false;
                }
            }
        });
        /*popupMenu.setOnDismissListener(new PopupMenu.OnDismissListener() {
            @Override
            public void onDismiss(PopupMenu menu) {
                Toast.makeText(ApplicationPartActivity.this, "onDismiss", Toast.LENGTH_SHORT).show();
            }
        });*/
        popupMenu.show();
    }
    //Всплывающее подменю меню "Бесплатно"
    private void ShowPopupMenu(View v, final int position){
        PopupMenu popupMenu = new PopupMenu(this, v);
        popupMenu.inflate(R.menu.popupmenu_free);
        //Обрабочик нажатия на пункт меню
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                //Узнаем ID_AP выбранного item
                String ID_AP_position = DataSet.get(position).getID_AP();
                //Переменная для записи результата обновления данных в БД
                String res;
                //Переменная для сравнения при удачной записи в БД
                String succes = "Запись обновлена";
                switch (item.getItemId()){
                    case R.id.menu2:
                        res = SetFreeDrink(ID_AP_position,getString(R.string.item2));
                        if (res.equals(succes))
                        {
                            DataSet.get(position).setFree(getString(R.string.item2));
                            DataSet.get(position).setPrice("0 руб.");
                            recyclerAdapter.notifyDataSetChanged();
                        } else {
                            new MaterialDialog.Builder(ApplicationPartActivity.this)
                                    .title("Сообщение")
                                    .positiveColorRes(R.color.colorPrimary)
                                    .content(res)
                                    .positiveText("Ok")
                                    .cancelable(false)
                                    .show();
                        }
                        return true;
                    case R.id.menu3:
                        res = SetFreeDrink(ID_AP_position,getString(R.string.item3));
                        if (res.equals(succes))
                        {
                            DataSet.get(position).setFree(getString(R.string.item3));
                            DataSet.get(position).setPrice("0 руб.");
                            recyclerAdapter.notifyDataSetChanged();
                        } else {
                            new MaterialDialog.Builder(ApplicationPartActivity.this)
                                    .title("Сообщение")
                                    .positiveColorRes(R.color.colorPrimary)
                                    .content(res)
                                    .positiveText("Ok")
                                    .cancelable(false)
                                    .show();
                        }
                        return true;
                    case R.id.menu4:
                        res = SetFreeDrink(ID_AP_position,getString(R.string.item4));
                        if (res.equals(succes))
                        {
                            DataSet.get(position).setFree(getString(R.string.item4));
                            DataSet.get(position).setPrice("0 руб.");
                            recyclerAdapter.notifyDataSetChanged();
                        } else {
                            new MaterialDialog.Builder(ApplicationPartActivity.this)
                                    .title("Сообщение")
                                    .positiveColorRes(R.color.colorPrimary)
                                    .content(res)
                                    .positiveText("Ok")
                                    .cancelable(false)
                                    .show();
                        }
                        return true;
                    case R.id.menu5:
                        res = SetFreeDrink(ID_AP_position,getString(R.string.item5));
                        if (res.equals(succes))
                        {
                            DataSet.get(position).setFree(getString(R.string.item5));
                            DataSet.get(position).setPrice("0 руб.");
                            recyclerAdapter.notifyDataSetChanged();
                        } else {
                            new MaterialDialog.Builder(ApplicationPartActivity.this)
                                    .title("Сообщение")
                                    .positiveColorRes(R.color.colorPrimary)
                                    .content(res)
                                    .positiveText("Ok")
                                    .cancelable(false)
                                    .show();
                        }
                        return true;
                    default:
                        return false;
                }
            }
        });

        /*popupMenu.setOnDismissListener(new PopupMenu.OnDismissListener() {
            @Override
            public void onDismiss(PopupMenu menu) {
                Toast.makeText(ApplicationPartActivity.this, "onDismiss", Toast.LENGTH_SHORT).show();
            }
        });*/
        popupMenu.show();
    }
    //Добовляем в БД информацию о бесплатном стаканчике согласно выбранному в меню пункту и устанавливаем сумму равной 0
    private String SetFreeDrink(String ID_AP, String Free){
        UpdateEntryDTO updateEntryDTO = new UpdateEntryDTO();
        return updateEntryDTO.Update("UPDATE `Application_Part` SET `Free`='"+Free+"', `Sum_AP`='0' WHERE `ID_AP`='"+ID_AP+"'");
    }

    private String[] getArrayOneColumn(String inquiry, String column){
        inquiryGetArrayOneColumn = new InquiryGetArrayOneColumn();
        inquiryGetArrayOneColumn.start(inquiry, column);
        try {
            inquiryGetArrayOneColumn.join();
        } catch (InterruptedException e) {
            Log.e("ArrayAP",e.getMessage());
        }
        lenghtAdditives = inquiryGetArrayOneColumn.resLenght();
        return inquiryGetArrayOneColumn.resColumn();
    }

    private String getOneRes(String inquiry, String column) {
        inquiryGetOneRes = new InquiryGetOneRes();
        inquiryGetOneRes.start(inquiry, column);
        try {
            inquiryGetOneRes.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return inquiryGetOneRes.res();
    }

    public List<ApplicationPartDTO> getDataSet() {
        DataSet = new ArrayList();
        DataSet.clear();
        String[] array_ID_AP_list = getArrayOneColumn("SELECT ID_AP FROM Application_Part WHERE ID_Application='"+ID_Application+"'","ID_AP");
        int lenghtAP_list = array_ID_AP_list.length;
        for (int i = 0; i < lenghtAP_list; i++){
            String ID_Drink = getOneRes("SELECT ID_Drink FROM Application_Part WHERE ID_AP='" + array_ID_AP_list[i] + "'", "ID_Drink");
            String Sum_AP = getOneRes("SELECT Sum_AP FROM Application_Part WHERE ID_AP='" + array_ID_AP_list[i] + "'", "Sum_AP");
            String Name_Drink = getOneRes("SELECT Name_Drink FROM Drink WHERE ID_Drink='" + ID_Drink + "'", "Name_Drink");
            String Volume_Drink = getOneRes("SELECT Volume_Drink FROM Drink WHERE ID_Drink='" + ID_Drink + "'", "Volume_Drink");

            String ID_Syrup = getOneRes("SELECT ID_Syrup FROM ApplicationPart_Syrup WHERE ID_AP='" + array_ID_AP_list[i] + "'", "ID_Syrup");
            String Syrup = getOneRes("SELECT Name_Syrup FROM Syrup WHERE ID_Syrup='" + ID_Syrup + "'", "Name_Syrup");
            if (Syrup != null) {
                Syrup = "Сироп:" + Syrup;
            } else {
                Syrup="";
            }

            String Additives = "";
            array_additives = getArrayOneColumn("SELECT ID_Additives FROM ApplicationPart_Additives WHERE ID_AP='" + array_ID_AP_list[i] + "'", "ID_Additives");
            if (lenghtAdditives > 0) {
                Additives = "Добавки:";
                for (int j = 0; j < lenghtAdditives; j++) {
                    Additives += getOneRes("SELECT Name_Additives FROM Additives WHERE ID_Additives='" + array_additives[j] + "'", "Name_Additives");
                    Additives += " ";
                }
            }
            DataSet.add(new ApplicationPartDTO(Name_Drink + "  " + Volume_Drink + " мл.", Syrup, Additives, Sum_AP + " руб.", array_ID_AP_list[i],""));

        }
        return DataSet;
    }
}
