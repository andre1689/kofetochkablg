package com.kofetochka.dto;

import android.util.Log;

import com.kofetochka.inquiry.InquiryGetArrayOneColumn;

public class GetArrayOneColumnDTO {
    InquiryGetArrayOneColumn inquiryGetArrayOneColumn;
    public String[] getArrayOneColumn(String Inquiry,String Column){
        inquiryGetArrayOneColumn = new InquiryGetArrayOneColumn();
        inquiryGetArrayOneColumn.start(Inquiry, Column);
        try {
            inquiryGetArrayOneColumn.join();
        } catch (InterruptedException e) {
            Log.e("GetArrayOneColumnDTO",e.getMessage());
        }
        return inquiryGetArrayOneColumn.resColumn();
    }

    public int getLenght (){
        return inquiryGetArrayOneColumn.resLenght();
    }
}
