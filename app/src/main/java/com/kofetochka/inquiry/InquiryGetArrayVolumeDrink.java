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

public class InquiryGetArrayVolumeDrink extends Thread{
    private String Name_Drink;
    private String[] arrayVolume_Drink;
    private int l;

    JSONArray volumes_drink= null;

    private String Result = null;
    private String Line = null;
    InputStream InpStr = null;

    public void run(){
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        nameValuePairs.add(new BasicNameValuePair("Name_Drink",Name_Drink));
        try {
            HttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost("http://146143.simplecloud.club/kofetochkablg/get_array_volume_drink.php");
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
            volumes_drink = json_data.getJSONArray("volumes_drink");
            l = volumes_drink.length();
            arrayVolume_Drink = new String[l];
            for (int i = 0; i < l; i++) {
                JSONObject c = volumes_drink.getJSONObject(i);
                arrayVolume_Drink[i]= c.getString("Volume_Drink");
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public void start (String name_drink){
        this.Name_Drink = name_drink;
        this.start();
    }

    public String[] resVolume_Drink(){
        return arrayVolume_Drink;
    }

    public int resLenght(){
        return l;
    }
}
