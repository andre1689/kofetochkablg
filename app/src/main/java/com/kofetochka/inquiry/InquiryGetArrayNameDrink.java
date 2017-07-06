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
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

public class InquiryGetArrayNameDrink extends Thread{
    private String Table;
    private String[] arrayName_Drink;
    private int l;

    JSONArray names_drink= null;

    private String Result = null;
    private String Line = null;
    InputStream InpStr = null;

    public void run(){
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        nameValuePairs.add(new BasicNameValuePair("Table",Table));
        try {
            HttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost("http://146143.simplecloud.club/kofetochkablg/get_array_name_drink.php");
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
        } catch (IOException e) {Log.e("Pass 2","Connection succes" + Result);
            e.printStackTrace();
        }

        try {
            JSONObject json_data  = new JSONObject(Result);
            names_drink = json_data.getJSONArray("names_drink");
            l = names_drink.length();
            arrayName_Drink = new String[l];
            for (int i = 0; i < l; i++) {
                JSONObject c = names_drink.getJSONObject(i);
                arrayName_Drink[i]= c.getString("Name_Drink");
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public void start (String table){
        this.Table = table;
        this.start();
    }

    public String[] resName_Drink(){
        return arrayName_Drink;
    }

    public int resLenght(){
        return l;
    }
}