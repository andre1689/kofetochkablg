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

public class InquiryAddStorage extends Thread{
    private String OpenShift_St;
    private String DiceBox_150_St;
    private String DiceBox_200_St;
    private String DiceBox_300_St;
    private String DiceBox_400_St;
    private String DiceBox_Summer_St;
    private String Coffee_1kg_St;
    private String Coffee_250g_St;
    private String DripCoffee_St;
    private String Exchange_St;
    private String ID_Shift;
    private String Res;

    private String Result = null;
    private String Line = null;
    InputStream InpStr = null;

    public void run(){
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        nameValuePairs.add(new BasicNameValuePair("OpenShift_St",OpenShift_St));
        nameValuePairs.add(new BasicNameValuePair("DiceBox_150_St",DiceBox_150_St));
        nameValuePairs.add(new BasicNameValuePair("DiceBox_200_St",DiceBox_200_St));
        nameValuePairs.add(new BasicNameValuePair("DiceBox_300_St",DiceBox_300_St));
        nameValuePairs.add(new BasicNameValuePair("DiceBox_400_St",DiceBox_400_St));
        nameValuePairs.add(new BasicNameValuePair("DiceBox_Summer_St",DiceBox_Summer_St));
        nameValuePairs.add(new BasicNameValuePair("Coffee_1kg_St",Coffee_1kg_St));
        nameValuePairs.add(new BasicNameValuePair("Coffee_250g_St",Coffee_250g_St));
        nameValuePairs.add(new BasicNameValuePair("DripCoffee_St",DripCoffee_St));
        nameValuePairs.add(new BasicNameValuePair("Exchange_St",Exchange_St));
        nameValuePairs.add(new BasicNameValuePair("ID_Shift",ID_Shift));
        try {
            HttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost("http://146143.simplecloud.club/kofetochkablg/add_storage.php");
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
            Res = (json_data.getString("Success"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public void start (String openShift_St, String diceBox_150_St, String diceBox_200_St, String diceBox_300_St, String diceBox_400_St, String diceBox_Summer_St, String coffee_1kg_St, String coffee_250g_St, String dripCoffee_St, String exchange_St, String id_Shift){
        this.OpenShift_St = openShift_St;
        this.DiceBox_150_St = diceBox_150_St;
        this.DiceBox_200_St = diceBox_200_St;
        this.DiceBox_300_St = diceBox_300_St;
        this.DiceBox_400_St = diceBox_400_St;
        this.DiceBox_Summer_St = diceBox_Summer_St;
        this.Coffee_1kg_St = coffee_1kg_St;
        this.Coffee_250g_St = coffee_250g_St;
        this.DripCoffee_St = dripCoffee_St;
        this.Exchange_St = exchange_St;
        this.ID_Shift = id_Shift;
        this.start();
    }

    public String resSuccess (){
        return Res;
    }
}
