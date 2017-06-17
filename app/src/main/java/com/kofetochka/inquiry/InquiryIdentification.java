package com.kofetochka.inquiry;

import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

public class InquiryIdentification extends Thread {

    private String Login;
    private String Password;
    private String Surname;
    private String Name;
    private String Middlename;
    private String Block;
    private String Role;

    private String Result = null;
    private String Line = null;
    InputStream InpStr = null;

    public void run(){
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        nameValuePairs.add(new BasicNameValuePair("Login",Login));
        try {
            HttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost("http://146143.simplecloud.club/kofetochkablg/get_login.php");
            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs,"UTF-8"));
            HttpResponse httpResponse = httpClient.execute(httpPost);
            HttpEntity httpEntity = httpResponse.getEntity();
            InpStr = httpEntity.getContent();
            Log.e("Pass 1","Connect succes");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(InpStr,"UTF-8"),8);
            StringBuilder stringBuilder = new StringBuilder();
            while ((Line=reader.readLine())!= null){
                stringBuilder.append(Line + "\n");
            }
            InpStr.close();
            Result = stringBuilder.toString();
            Log.e("Pass 2","Connection succes" + Result);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            JSONObject json_data  = new JSONObject(Result);
            Password = (json_data.getString("Password"));
            Surname = (json_data.getString("Surname"));
            Name = (json_data.getString("Name"));
            Middlename = (json_data.getString("Middlename"));
            Block = (json_data.getString("Block"));
            Role = (json_data.getString("ID_role"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public void start (String login){
        this.Login = login;
        this.start();
    }

    public String resPassword (){
        return Password;
    }

    public String resSurname (){
        return Surname;
    }

    public String resName (){
        return Name;
    }

    public String resMiddlename (){
        return Middlename;
    }

    public String resBlock (){
        return Block;
    }

    public String resRole (){
        return Role;
    }
}
