package com.example.goodfood;

public class History_Food {
    private String Name_Food;
    private String Last_Day;
    private int img;

    public History_Food(String name_Food, String last_Day, int img) {
        Name_Food = name_Food;
        Last_Day = last_Day;
        this.img = img;
    }

    public String getName_Food() {
        return Name_Food;
    }

    public void setName_Food(String name_Food) {
        Name_Food = name_Food;
    }

    public String getLast_Day() {
        return Last_Day;
    }

    public void setLast_Day(String last_Day) {
        Last_Day = last_Day;
    }

    public int getImg() {
        return img;
    }

    public void setImg(int img) {
        this.img = img;
    }
}
