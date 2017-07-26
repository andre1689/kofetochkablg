package com.kofetochka.dto;

public class ApplicationPartDTO {
    private String title;
    private String subtitle;
    private String price;

    public ApplicationPartDTO(String title, String subtitle, String price) {

        this.title = title;
        this.subtitle = subtitle;
        this.price = price;
    }

    public String getSubtitle(){
        return subtitle;
    }

    public String getPrice(){
        return price;
    }

    public String getTitle() {
        return title;
    }

}
