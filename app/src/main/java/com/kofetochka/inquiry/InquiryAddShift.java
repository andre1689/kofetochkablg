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

public class InquiryAddShift extends Thread{

    private String Date_Shift;
    private String Time_Shift;
    private String ID_CH;
    private String Res;

    private String Result = null;
    private String Line = null;
    InputStream InpStr = null;

    public void run(){
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        nameValuePairs.add(new BasicNameValuePair("Date_Shift",Date_Shift));
        nameValuePairs.add(new BasicNameValuePair("Time_Shift",Time_Shift));
        nameValuePairs.add(new BasicNameValuePair("ID_CH",ID_CH));
        try {
            HttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost("http://146143.simplecloud.club/kofetochkablg/add_shift.php");
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

    public void start (String date_Shift, String time_Shift, String id_CH){
        this.Date_Shift = date_Shift;
        this.Time_Shift = time_Shift;
        this.ID_CH = id_CH;
        this.start();
    }

    public String resSuccess (){
        return Res;
    }
}
