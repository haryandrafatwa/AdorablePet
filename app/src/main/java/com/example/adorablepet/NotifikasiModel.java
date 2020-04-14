package com.example.adorablepet;

public class NotifikasiModel {

    private String id,additionalInformation, imageURL, date, duration, packages, payMethod, quantity;
    private Boolean read;
    private int totalPrice;

    public NotifikasiModel(String id,String additionalInformation, String imageURL, String date, String duration, String packages, String payMethod, String quantity, Boolean read, int totalPrice) {
        this.id = id;
        this.additionalInformation = additionalInformation;
        this.imageURL = imageURL;
        this.date = date;
        this.duration = duration;
        this.packages = packages;
        this.payMethod = payMethod;
        this.quantity = quantity;
        this.read = read;
        this.totalPrice = totalPrice;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAdditionalInformation() {
        return additionalInformation;
    }

    public void setAdditionalInformation(String additionalInformation) {
        this.additionalInformation = additionalInformation;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getPackages() {
        return packages;
    }

    public void setPackages(String packages) {
        this.packages = packages;
    }

    public String getPayMethod() {
        return payMethod;
    }

    public void setPayMethod(String payMethod) {
        this.payMethod = payMethod;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public Boolean getRead() {
        return read;
    }

    public void setRead(Boolean read) {
        this.read = read;
    }

    public int getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(int totalPrice) {
        this.totalPrice = totalPrice;
    }
}
