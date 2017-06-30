package com.kofetochka.kofetochkablg;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.work_layout);

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        if (toolbar != null){
            setSupportActionBar (toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        initializeNavigationDrawer(toolbar);

    }

    private void initializeNavigationDrawer(Toolbar toolbar) {

        Surname = getIntent().getStringExtra("Surname");
        Name = getIntent().getStringExtra("Name");
        NameRole = getIntent().getStringExtra("NameRole");
        Login = getIntent().getStringExtra("Login");

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
                                Intent intent = new Intent(com.kofetochka.kofetochkablg.WorkActivity.this, OpenShift.class);
                                intent.putExtra("Login",Login);
                                startActivity(intent);
                            }
                            if (drawerItem.getIdentifier()==3){
                                Intent intent = new Intent(com.kofetochka.kofetochkablg.WorkActivity.this, CloseShiftActivity.class);
                                //intent.putExtra("Login",Login);
                                startActivity(intent);
                            }
                        }

                        return false;
                    }
                })
                .build();

    }

    public void IntentOpenShift (View view){
        Intent intent = new Intent(this, OpenShift.class);
        startActivity(intent);
    }

}
