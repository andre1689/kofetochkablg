package com.kofetochka.dto;

import android.util.Log;

import com.kofetochka.inquiry.InquiryDeleteEntry;

public class DeleteEntryDTO {
    InquiryDeleteEntry inquiryDeleteEntry;

    public String DeleteEntry(String Query){
        inquiryDeleteEntry = new InquiryDeleteEntry();
        inquiryDeleteEntry.start(Query);
        try {
            inquiryDeleteEntry.join();
        } catch (InterruptedException e) {
            Log.e("DeleteEntry",e.getMessage());
        }
        return inquiryDeleteEntry.resSuccess();
    }
}
