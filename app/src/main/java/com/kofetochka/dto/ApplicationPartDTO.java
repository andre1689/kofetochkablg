package com.kofetochka.dto;

public class ApplicationPartDTO {
    private String title;
    private String syrup;
    private String additives;
    private String price;
    private String ID_AP;
    private String free;

    public ApplicationPartDTO(String title, String syrup, String additives, String price, String id_ap, String free) {
        this.title = title;
        this.syrup = syrup;
        this.additives = additives;
        this.price = price;
        this.ID_AP = id_ap;
        this.free = free;
    }

    public void setTitle (String title)
    {
     this.title = title;
    }
    public void setID_AP (String id_ap)
    {
        this.ID_AP = id_ap;
    }
    public void setFree (String free) {this.free = free;}
    public void setPrice (String price){this.price=price;}
    public String getTitle() { return title; }
    public String getSyrup(){
        return syrup;
    }
    public String getAdditives(){return additives;}
    public String getPrice(){
        return price;
    }
    public String getID_AP() { return ID_AP; }
    public String getFree() {return free;}

}
