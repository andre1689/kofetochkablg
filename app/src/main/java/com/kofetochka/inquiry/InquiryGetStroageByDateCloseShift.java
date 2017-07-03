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

public class InquiryGetStroageByDateCloseShift extends Thread{

    private String ID_Shift;
    private String DiceBox_150_St;
    private String DiceBox_200_St;
    private String DiceBox_300_St;
    private String DiceBox_400_St;
    private String DiceBox_Summer_St;
    private String Coffee_1kg_St;
    private String Coffee_250g_St;
    private String DripCoffee_St;
    private String Exchange_St;

    private String Result = null;
    private String Line = null;
    InputStream InpStr = null;

    public void run(){
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        nameValuePairs.add(new BasicNameValuePair("ID_Shift",ID_Shift));
        try {
            HttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost("http://146143.simplecloud.club/kofetochkablg/get_storage_by_date_closeshift.php");
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
            DiceBox_150_St = (json_data.getString("DiceBox_150_St"));
            DiceBox_200_St = (json_data.getString("DiceBox_200_St"));
            DiceBox_300_St = (json_data.getString("DiceBox_300_St"));;
            DiceBox_400_St = (json_data.getString("DiceBox_400_St"));;
            DiceBox_Summer_St = (json_data.getString("DiceBox_Summer_St"));;
            Coffee_1kg_St = (json_data.getString("Coffee_1kg_St"));;
            Coffee_250g_St = (json_data.getString("Coffee_250g_St"));;
            DripCoffee_St = (json_data.getString("DripCoffee_St"));;
            Exchange_St = (json_data.getString("Exchange_St"));;
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public void start (String id_Shift){
        this.ID_Shift = id_Shift;
        this.start();
    }

    public String resDiceBox_150_St (){
        return DiceBox_150_St;
    }
    public String resDiceBox_200_St (){
        return DiceBox_200_St;
    }
    public String resDiceBox_300_St (){
        return DiceBox_300_St;
    }
    public String resDiceBox_400_St (){
        return DiceBox_400_St;
    }
    public String resDiceBox_Summer_St (){
        return DiceBox_Summer_St;
    }
    public String resCoffee_1kg_St (){
        return Coffee_1kg_St;
    }
    public String resCoffee_250g_St (){
        return Coffee_250g_St;
    }
    public String resDripCoffee_St (){
        return DripCoffee_St;
    }
    public String resExchange_St (){
        return Exchange_St;
    }

}
