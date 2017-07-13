package com.kofetochka.kofetochkablg;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.kofetochka.inquiry.InquiryIdentification;
import com.kofetochka.inquiry.InquiryRole;
import com.mikepenz.iconics.utils.Utils;

public class LoginActivity extends Activity {

    private EditText etLogin, etPassword;
    private Button btn_Enter;

    private String Login = null;
    private String Password = null;
    private String Surname = null;
    private String Name = null;
    private String Middlename = null;
    private String Block = null;
    private String Role = null;
    private String NameRole = null;private

    InquiryIdentification inquiryIdentification;
    InquiryRole inquiryRole;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);

        etLogin = (EditText)findViewById(R.id.editText_Login);
        etPassword = (EditText)findViewById(R.id.editText_Password);
        btn_Enter = (Button) findViewById(R.id.button_Login);

    }

    public void ViewDB (View view){
        Login = etLogin.getText().toString();
        inquiryIdentification = new InquiryIdentification();
        inquiryIdentification.start(Login);

        try {
            inquiryIdentification.join();
        } catch (InterruptedException e) {
            Log.e("Pass 0", e.getMessage());
        }

        Password = inquiryIdentification.resPassword();
        Surname = inquiryIdentification.resSurname();
        Name = inquiryIdentification.resName();
        Middlename = inquiryIdentification.resMiddlename();
        Block = inquiryIdentification.resBlock();
        Role = inquiryIdentification.resRole();

        inquiryRole = new InquiryRole();
        inquiryRole.start(Role);

        try {
            inquiryRole.join();
        } catch (InterruptedException e) {
            Log.e("Pass 0", e.getMessage());
        }

        NameRole = inquiryRole.resNameRole();

        if (Login!=null){
            if (etPassword.getText().toString().equals(Password)){
                if(Block.equals("0")){
                    Intent intent = new Intent(this,WorkActivity.class);
                    intent.putExtra("Login", Login);
                    intent.putExtra("NameRole",NameRole);
                    intent.putExtra("Surname", Surname);
                    intent.putExtra("Name", Name);
                    startActivity(intent);
                }
                else Toast.makeText(this, "Ваша учетная запись заблокирована. Обратитесь к администратору.", Toast.LENGTH_LONG).show();
            }
            else Toast.makeText(this, "Пара логин/пароль введены неверно", Toast.LENGTH_LONG).show();
        }
        else Toast.makeText(this, "Пара логин/пароль введены неверно", Toast.LENGTH_LONG).show();

    }
}
