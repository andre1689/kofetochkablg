package com.kofetochka.dto;

import android.util.Log;
import com.kofetochka.inquiry.InquiryAdd;

public class AddEntryDTO {
    InquiryAdd inquiryAdd;

    public String AddEntry(String table, String column, String values) {
        inquiryAdd = new InquiryAdd();
        inquiryAdd.start(table, column, values);
        try {
            inquiryAdd.join();
        } catch (InterruptedException e) {
            Log.e("AddEntryDTO", e.getMessage());
        }
        return inquiryAdd.resSuccess();
    }
}
