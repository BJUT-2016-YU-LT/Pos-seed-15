package com.thoughtworks.pos.domains;
import java.util.ArrayList;
import com.thoughtworks.pos.common.EmptyShoppingCartException;
import com.thoughtworks.pos.services.services.ReportDataGenerator;

/**
 * Created by Liyue on 2016/6/24
 */
public class Pos {
    public String getShoppingList(ShoppingChart shoppingChart) {
        ArrayList<Item> items = shoppingChart.getItems();
        ArrayList<ShoppingListItem> shoplist = new ArrayList<ShoppingListItem>();
        double total=0;
        double saved=0;

        int i,j;
        for (i = 0; i <items.size() ; i++) {
            if(shoplist.size()==0){
                ShoppingListItem listItem = new ShoppingListItem(items.get(i));
                shoplist.add(listItem);
                total += items.get(i).getPrice()*items.get(i).getDiscount();
                saved += items.get(i).getPrice()*(1-items.get(i).getDiscount());
                continue;
            }

            for (j = 0; j < shoplist.size(); j++) {
                if (shoplist.get(j).getBarCode().equals(items.get(i).getBarCode())){
                    j++;
                    break;
                }
            }
            j--;

            if (shoplist.get(j).getBarCode().equals(items.get(i).getBarCode()))
            {
                shoplist.get(j).setAmount(shoplist.get(j).getAmount()+1);
                shoplist.get(j).setSubTotal(shoplist.get(j).getAmount() * shoplist.get(j).getPrice() * shoplist.get(j).getDiscount());
            }else
            {
                ShoppingListItem listItem = new ShoppingListItem(items.get(i));
                shoplist.add(listItem);
            }

            total += items.get(i).getPrice()*items.get(i).getDiscount();
            saved += items.get(i).getPrice()*(1-items.get(i).getDiscount());
        }
        items = null;

        StringBuilder stringBuilder = new StringBuilder();
        StringBuilder gifts = new StringBuilder();
        stringBuilder.append("***商店购物清单***\n");
        for (int k = 0; k < shoplist.size(); k++) {
            stringBuilder
                    .append("名称：").append(shoplist.get(k).getName()).append("，")
                    .append("数量：").append(shoplist.get(k).getAmount()).append(shoplist.get(k).getUnit()).append("，")
                    .append("单价：").append(String.format("%.2f", shoplist.get(k).getPrice()))
                    .append("(元)").append("，")
                    .append("小计：").append(String.format("%.2f", shoplist.get(k).getSubTotal()))
                    .append("(元)").append("\n");
            if(shoplist.get(k).getAmount() >= 2&&shoplist.get(k).getDiscount() ==1)
            {
                gifts
                        .append("----------------------\n买二赠一商品:\n")
                        .append("名称：").append(shoplist.get(k).getName()).append("，")
                        .append("数量：").append("1").append(shoplist.get(k).getUnit()).append("\n");
                   saved+= shoplist.get(k).getPrice();
            }
        }
        stringBuilder.append(gifts);
        stringBuilder
                .append("----------------------\n")
                .append("总计：").append(String.format("%.2f", total)).append("(元)").append("\n");
        if(saved!=0) {
            stringBuilder.append("节省：").append(String.format("%.2f", saved)).append("(元)").append("\n");
        }
        stringBuilder.append("**********************\n");


        return stringBuilder.toString();
    }
}
