package com.kofetochka.kofetochkablg;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.kofetochka.inquiry.InquiryGetOneRes;

public class LoginActivity extends Activity {

    private EditText etLogin, etPassword;
    private Button btn_Enter;

    private InquiryGetOneRes inquiryGetOneRes;

    TextInputLayout til_password, til_login;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);

        til_login = (TextInputLayout) findViewById(R.id.login_layout);
        etLogin = (EditText)findViewById(R.id.EditText_Login);
        til_password = (TextInputLayout) findViewById(R.id.password_layout);
        etPassword = (EditText)findViewById(R.id.EditText_Password);
        btn_Enter = (Button) findViewById(R.id.button_Login);

    }

    public void ViewDB (View view){
        String Login = etLogin.getText().toString();

        String Password = getOneRes("SELECT Password FROM Identification WHERE Login='"+Login+"'","Password");
        String Surname = getOneRes("SELECT Surname FROM Identification WHERE Login='"+Login+"'","Surname");
        String Name = getOneRes("SELECT Name FROM Identification WHERE Login='"+Login+"'","Name");
        String Block = getOneRes("SELECT Block FROM Identification WHERE Login='"+Login+"'","Block");
        String Role = getOneRes("SELECT ID_role FROM Identification WHERE Login='"+Login+"'","ID_role");
        String NameRole = getOneRes("SELECT Name_role FROM Role WHERE ID_role='"+Role+"'","Name_role");

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
                else {
                    til_login.setErrorEnabled(true);
                    til_login.setError(getResources().getString(R.string.error_block));
                }
            }
            else {
                til_login.setErrorEnabled(true);
                til_login.setError(getResources().getString(R.string.error_login_password));
                til_password.setErrorEnabled(true);
                til_password.setError(getResources().getString(R.string.error_login_password));
            }
        }
        else {
            til_login.setErrorEnabled(true);
            til_login.setError(getResources().getString(R.string.error_login_password));
            til_password.setErrorEnabled(true);
            til_password.setError(getResources().getString(R.string.error_login_password));
        }

        etLogin.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                til_login.setErrorEnabled(false);
                til_password.setErrorEnabled(false);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        etPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                til_login.setErrorEnabled(false);
                til_password.setErrorEnabled(false);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    private String getOneRes(String inquiry, String column){
        String Inquiry = inquiry;
        String Column = column;
        inquiryGetOneRes = new InquiryGetOneRes();
        inquiryGetOneRes.start(Inquiry,Column);
        try {
            inquiryGetOneRes.join();
        } catch (InterruptedException e) {
            Log.e("GetLogin",e.getMessage());
        }
        return inquiryGetOneRes.res();
    }
}
