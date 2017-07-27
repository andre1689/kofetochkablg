package com.kofetochka.kofetochkablg;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.kofetochka.inquiry.InquiryGetOneRes;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

public class WorkActivity extends AppCompatActivity {

    private Drawer result = null;
    private AccountHeader accountHeaderResult = null;
    private String Surname;
    private String Name;
    private String NameRole;
    private String Login;
    private String ID_Shift = null;
    private String Name_CH = null;
    FloatingActionButton floatingActionButtonDrink, floatingActionButtonCoffee;

    InquiryGetOneRes inquiryGetOneRes;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        Login = getIntent().getStringExtra("Login");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.work_layout);

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        if (toolbar != null){
            setSupportActionBar (toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        initializeNavigationDrawer(toolbar);
        floatingActionButtonDrink = (FloatingActionButton) findViewById(R.id.action_drink);
        floatingActionButtonDrink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WorkActivity.this, NewOrderActivity.class);
                intent.putExtra("Login",Login);
                intent.putExtra("ID_Application","0");
                startActivity(intent);
            }
        });
        floatingActionButtonCoffee = (FloatingActionButton) findViewById(R.id.action_coffee);
        floatingActionButtonCoffee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WorkActivity.this,NewCoffeeActivity.class);
                intent.putExtra("Login",Login);
                startActivity(intent);
            }
        });

    }

    private void initializeNavigationDrawer(Toolbar toolbar) {

        String ID_Role = getOneRes("SELECT ID_role FROM Identification WHERE Login='"+Login+"'","ID_role");
        NameRole = getOneRes("SELECT Name_role FROM Role WHERE ID_role='"+ID_Role+"'","Name_role");
        Surname = getOneRes("SELECT Surname FROM Identification WHERE Login='"+Login+"'","Surname");
        Name = getOneRes("SELECT Name FROM Identification WHERE Login='"+Login+"'","Name");
        ID_Shift = getIntent().getStringExtra("ID_Shift");
        Name_CH = getIntent().getStringExtra("Name_CH");

        accountHeaderResult = new AccountHeaderBuilder()
                .withActivity(this)
                .withHeaderBackground(R.color.colorPrimaryDark)
                .addProfiles(
                        new ProfileDrawerItem().withName(NameRole+": "+Surname+" "+Name)
                )
                .build();

        result = new DrawerBuilder(this)
                .withActivity(this)
                .withToolbar(toolbar)
                .withAccountHeader(accountHeaderResult)
                .withDisplayBelowStatusBar(false)
                .withActionBarDrawerToggleAnimated(true)
                .addDrawerItems(
                        new PrimaryDrawerItem()
                                .withName(R.string.item_home)
                                .withDescription("Переход домой")
                                .withIdentifier(1)
                                .withSelectable(false),
                        new DividerDrawerItem(),
                        new PrimaryDrawerItem()
                                .withName(R.string.item_openshift)
                                .withIdentifier(2),
                        new PrimaryDrawerItem()
                                .withName("Закрыть смену")
                                .withIdentifier(3)
                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        if (drawerItem!=null){
                            if (drawerItem.getIdentifier()==2){
                                Intent intent = new Intent(com.kofetochka.kofetochkablg.WorkActivity.this, OpenShiftActivity.class);
                                intent.putExtra("Login",Login);
                                intent.putExtra("NameRole",NameRole);
                                intent.putExtra("Surname", Surname);
                                intent.putExtra("Name", Name);
                                startActivity(intent);
                                finish();
                            }
                            if (drawerItem.getIdentifier()==3){
                                Intent intent = new Intent(com.kofetochka.kofetochkablg.WorkActivity.this, CloseShiftActivity.class);
                                intent.putExtra("Login",Login);
                                intent.putExtra("NameRole",NameRole);
                                intent.putExtra("Surname", Surname);
                                intent.putExtra("Name", Name);
                                startActivity(intent);
                                finish();
                            }
                        }

                        return false;
                    }
                })
                .build();

    }

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

    public void IntentOpenShift (View view){
        Intent intent = new Intent(this, OpenShiftActivity.class);
        startActivity(intent);
    }

    public void openNewOrderActivity (View view){
        Intent intent = new Intent(this, NewOrderActivity.class);
        intent.putExtra("Login",Login);
        startActivity(intent);
    }

}
