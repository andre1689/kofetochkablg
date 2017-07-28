package com.kofetochka.dto;

import android.util.Log;

import com.kofetochka.inquiry.InquiryGetOneRes;

public class GetOneResDTO {
    InquiryGetOneRes inquiryGetOneRes;

    public String getOneResDTO (String inquiry, String column){
        inquiryGetOneRes = new InquiryGetOneRes();
        inquiryGetOneRes.start(inquiry, column);
        try {
            inquiryGetOneRes.join();
        } catch (InterruptedException e) {
            Log.e("InquiryGetOneRes",e.getMessage());
        }
        return inquiryGetOneRes.res();
    }

}
