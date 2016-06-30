package com.thoughtworks.pos.domains;

/**
 * Created by Liyue on 2016/6/24
*/
public class ShoppingListItem extends Item{
    private int amount;
    private double subTotal;
    public ShoppingListItem(Item item) {
        this.barCode = item.barCode;
        this.name = item.name;
        this.unit = item.unit;
        this.price = item.price;
        this.discount = item.discount;
        this.amount = 1;
        this.subTotal = item.price*item.discount;
    }

    public int getAmount() {
        return amount;
    }

    public double getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(double subTotal) {
        this.subTotal = subTotal;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
}