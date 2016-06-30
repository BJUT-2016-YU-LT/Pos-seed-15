package com.thoughtworks.pos.domains;

/**
 * Created by Administrator on 2014/12/28.
 */
public class Item {
    private String barcode;
    private String name;
    private String unit;
    private double price;
    private double discount;
    protected boolean promotion;
    private double vipDiscount;

    public Item() {
    }

    public Item(String barcode, String name, String unit, double price) {

        this.setBarcode(barcode);
        this.setName(name);
        this.setUnit(unit);
        this.setPrice(price);
    }
    public Item(String name, String unit, double price){
        this.setName(name);
        this.setUnit(unit);
        this.setPrice(price);
    }


    public Item(String barcode, String name, String unit, double price, double discount) {
        this(barcode, name, unit, price);
        this.setDiscount(discount);
    }
    public Item(String barcode, String name, String unit, double price, double discount,double vipDiscount,boolean promotion) {
        this(barcode, name, unit, price,discount,promotion);
        this.setVipDiscount(vipDiscount);
    }

    public Item(String barCode, String name, String unit, double price,double discount,boolean promotion) {

        this.barcode = barCode;
        this.name = name;
        this.unit = unit;
        this.price = price;
        this.discount=discount;
        this.promotion=promotion;

    }
    public boolean isPromotion() {
        return promotion;
    }
    public String getName() {
        return name;
    }
    public String getUnit() {
        return unit;
    }

    public double getPrice() {
        return price;
    }

    public String getBarcode() { return barcode; }

    public double getDiscount() {
        if (discount == 0.00)
            return 1.00;
        return discount;
    }

    public double getVipDiscount() {
        if (vipDiscount == 0.00)
            return 1.00;
        return vipDiscount;
    }
    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public void setVipDiscount(double vipDiscount) {
        this.vipDiscount = vipDiscount;
    }
}
