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

public class InquiryGetArrayOneColumn extends Thread{

    private String Inquiry;
    private String Column;
    private String[] arrayRes_Column;
    private int l;

    JSONArray res_column= null;

    private String Result = null;
    private String Line = null;
    InputStream InpStr = null;

    public void run(){
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        nameValuePairs.add(new BasicNameValuePair("Column",Column));
        nameValuePairs.add(new BasicNameValuePair("Inquiry",Inquiry));
        try {
            HttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost("http://146143.simplecloud.club/kofetochkablg/get_array_one_column.php");
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
            res_column = json_data.getJSONArray("array_"+Column);
            l = res_column.length();
            arrayRes_Column = new String[l];
            for (int i = 0; i < l; i++) {
                JSONObject c = res_column.getJSONObject(i);
                arrayRes_Column[i]= c.getString(Column);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public void start (String inquiry, String column){
        this.Inquiry = inquiry;
        this.Column = column;
        this.start();
    }

    public String[] resColumn(){
        return arrayRes_Column;
    }

    public int resLenght(){
        return l;
    }
}
