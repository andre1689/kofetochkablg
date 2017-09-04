package com.kofetochka.dto;

import android.util.Log;

import com.kofetochka.inquiry.InquiryUpdateEntry;

public class UpdateEntryDTO {
    InquiryUpdateEntry inquiryUpdateEntry;

    public String Update(String Query){
        inquiryUpdateEntry=new InquiryUpdateEntry();
        inquiryUpdateEntry.start(Query);
        try {
            inquiryUpdateEntry.join();
        } catch (InterruptedException e) {
            Log.e("UpdateEntryDTO",e.getMessage());
        }
        return inquiryUpdateEntry.resSuccess();
    }
}
