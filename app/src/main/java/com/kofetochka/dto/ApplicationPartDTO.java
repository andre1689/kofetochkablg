package com.kofetochka.dto;

public class ApplicationPartDTO {
    private String title;
    private String subtitle;
    private String price;
    private String ID_AP;

    public ApplicationPartDTO(String title, String subtitle, String price, String id_ap) {

        this.title = title;
        this.subtitle = subtitle;
        this.price = price;
        this.ID_AP = id_ap;
    }

    public String getSubtitle(){
        return subtitle;
    }

    public String getPrice(){
        return price;
    }

    public String getTitle() { return title; }

    public String getID_AP() { return ID_AP; }

}
