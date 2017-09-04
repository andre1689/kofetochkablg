package com.kofetochka.dto;

public class AdditivesDTO {
    public String ID_Additives;
    public String Name_Additives;
    public String Price_Additives;
    public boolean CheckBox_Additives;

    public AdditivesDTO(String id_additives, String name_additives, String price_additives, boolean checkbox_additives){
        this.ID_Additives = id_additives;
        this.Name_Additives = name_additives;
        this.Price_Additives = price_additives;
        this.CheckBox_Additives = checkbox_additives;
    }

    public String getID_Additives (){
        return ID_Additives;
    }

    public String getName_Additives(){
        return Name_Additives;
    }

    public String getPrice_Additives(){
        return Price_Additives;
    }

    public void setCheckBox_Additives(boolean checkBox_additives){this.CheckBox_Additives=checkBox_additives;}

    public boolean getCheckBox_Additives(){
        return CheckBox_Additives;
    }
}
