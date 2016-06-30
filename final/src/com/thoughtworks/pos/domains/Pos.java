package com.thoughtworks.pos.domains;

import com.thoughtworks.pos.common.EmptyShoppingCartException;
import com.thoughtworks.pos.services.services.ReportDataGenerator;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.function.BooleanSupplier;

/**
 * Created by Administrator on 2014/12/28.
 */
public class Pos {
    public String getShoppingList(ShoppingChart shoppingChart) throws EmptyShoppingCartException {

        Report report = new ReportDataGenerator(shoppingChart).generate();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
        StringBuilder shoppingListBuilder = new StringBuilder()
                .append("***商店购物清单***\n");

        if(report.getUser().getVip())
            shoppingListBuilder.append("会员编号：  ")
                    .append(report.getUserName())
                    .append("    ")
                    .append("会员积分：  ")
                    .append(report.getScore())
                    .append("分\n")
                    .append("----------------------\n");
        shoppingListBuilder .append("打印时间：")
                .append(new String(dateFormat.format(new Date())))
                .append("\n")
                .append("----------------------\n")
                ;
        Boolean hasremote=false;
        for (ItemGroup itemGroup : report.getItemGroupies()) {
            shoppingListBuilder.append(
                    new StringBuilder()
                            .append("名称：").append(itemGroup.groupName()).append("，")
                            .append("数量：").append(itemGroup.groupSize()).append(itemGroup.groupUnit()).append("，")
                            .append("单价：").append(String.format("%.2f", itemGroup.groupPrice())).append("(元)").append("，")
                            .append("小计：").append(String.format("%.2f", itemGroup.subTotal())).append("(元)").append("\n")
                            .toString());
            if (itemGroup.Ispromote()==true)
                hasremote=true;
        }
        StringBuilder TempBuilder=new StringBuilder(shoppingListBuilder);
        StringBuilder giftStringBuilder=shoppingListBuilder;
        double saving = report.getSaving();
        Boolean title=false;
        int numOfPor=0;
        if(hasremote==true)
        {
            if(title==false) {
                giftStringBuilder = shoppingListBuilder
                        .append("----------------------\n")
                        .append("挥泪赠送商品:\n");
            }
            title=true;
        for (ItemGroup itemGroup : report.getItemGroupies()) {
            if(itemGroup.groupSize()-itemGroup.getRealbuy()!=0)
            shoppingListBuilder.append(
                    new StringBuilder()
                            .append("名称：").append(itemGroup.groupName()).append("，")
                            .append("数量：").append(itemGroup.groupSize()-itemGroup.getRealbuy()).append(itemGroup.groupUnit()).append("\n")
                            .toString());
            numOfPor+=itemGroup.groupSize()-itemGroup.getRealbuy();
        }
        }
        if(numOfPor==0)
            title=false;
        StringBuilder subStringBuilder;
        if(title)
        subStringBuilder= giftStringBuilder;
        else
        subStringBuilder=TempBuilder;
        subStringBuilder
                .append("----------------------\n")
                .append("总计：").append(String.format("%.2f", report.getTotal())).append("(元)").append("\n");


        if (saving == 0) {
            return subStringBuilder
                    .append("**********************\n")
                    .toString();
        }
        return subStringBuilder
                .append("节省：").append(String.format("%.2f", saving)).append("(元)").append("\n")
                .append("**********************\n")
                .toString();
    }
}
