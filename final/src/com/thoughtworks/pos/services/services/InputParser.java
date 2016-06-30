package com.thoughtworks.pos.services.services;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thoughtworks.pos.common.EmptyIndex;
import com.thoughtworks.pos.common.EmptyShoppingCartException;
import com.thoughtworks.pos.common.PromoteAndTwo;
import com.thoughtworks.pos.domains.Item;
import com.thoughtworks.pos.domains.ShoppingChart;
import com.thoughtworks.pos.domains.User;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;

/**
 * Created by Administrator on 2015/1/2.
 */
public class InputParser {
    private File file_index;
    private File file_list;
    private File file_vip;
    private final ObjectMapper objectMapper;

    public InputParser(File file_index) {
        this.file_index = file_index;
        objectMapper = new ObjectMapper(new JsonFactory());
        objectMapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
    }
    public InputParser(File file_index, File file_list) {
        this.file_index = file_index;
        this.file_list = file_list;
        objectMapper = new ObjectMapper(new JsonFactory());
        objectMapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
    }
    public InputParser(File file_index, File file_list,File file_vip) {
        this.file_index = file_index;
        this.file_list = file_list;
        this.file_vip=file_vip;
        objectMapper = new ObjectMapper(new JsonFactory());
        objectMapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
    }
    public ShoppingChart parser() throws IOException {
        String textInput = FileUtils.readFileToString(file_index);
        Item[] items = objectMapper.readValue(textInput, Item[].class);
        return BuildShoppingChart(items);
    }
    private ShoppingChart BuildShoppingChart(Item[] items,User user) {
        ShoppingChart shoppingChart = new ShoppingChart();
        for (Item item : items) {
            shoppingChart.add(item);
        }
        shoppingChart.setUser(user);
        return shoppingChart;
    }
    private ShoppingChart BuildShoppingChart(Item[] items) {
        ShoppingChart shoppingChart = new ShoppingChart();
        for (Item item : items) {
            shoppingChart.add(item);
        }
        return shoppingChart;
    }
  //需求3新增
    public ShoppingChart parsertwofile() throws IOException, EmptyIndex, EmptyShoppingCartException, PromoteAndTwo {
        int num_product=0;
        String textInput_index = FileUtils.readFileToString(file_index);
        String textInput_list = FileUtils.readFileToString(file_list);
        JsonNode rootNode_list = objectMapper.readTree(textInput_list);
        JsonNode rootNode = objectMapper.readTree(textInput_index);
        Iterator<JsonNode> iterator_list = rootNode_list.elements();
        while (iterator_list.hasNext()) {
            num_product++;
            iterator_list.next();
        }
        String [] Barcode=new String[num_product];
        iterator_list = rootNode_list.elements();
        int i=0;
        while (iterator_list.hasNext()) {
            JsonNode jsonNode =iterator_list.next();
            Barcode[i]=jsonNode.textValue();
            i++;
        }
        rootNode=rootNode.path(0);
        System.out.println(rootNode.asText());
        Iterator<JsonNode> iterator_index = rootNode.elements();
        String textInput_sync;
        textInput_sync="[";
        while (iterator_index.hasNext()) {
            JsonNode jsonNode =iterator_index.next();
            textInput_sync+=jsonNode;

            if(!iterator_index.hasNext());
                else textInput_sync+=",";
        }
        textInput_sync+="]";
        Item[] items;
        if(textInput_sync.toString().equals("[]"))
            throw new EmptyIndex();
        Item[]items_index = objectMapper.readValue(textInput_sync, Item[].class);
        items=new Item[num_product];
        if(num_product>0) {
            items[0] = items_index[0];
            items[0].setBarcode(Barcode[0]);
            if(items[0].getDiscount()!=1&&items[0].isPromotion()==true)
                throw new PromoteAndTwo();
        }
        int j=1;
        for( i=1;i<num_product;i++)
        {
            if(Barcode[i].equals(Barcode[i-1]))
                items[i]=items[i-1];
            else {
                items[i]=items_index[j];
                j++;
            }
            items[i].setBarcode(Barcode[i]);
            if(items[i].getDiscount()!=1&&items[i].isPromotion()==true)
            throw new PromoteAndTwo();
        }

        return BuildShoppingChart(items);
    }

    //需求5新增
    public ShoppingChart parserThreefile() throws IOException, EmptyIndex, EmptyShoppingCartException, PromoteAndTwo {
        int num_product=0,num_vip=0;
        String textInput_index = FileUtils.readFileToString(file_index);
        String textInput_list = FileUtils.readFileToString(file_list);
        String textInput_vip = FileUtils.readFileToString(file_vip);
        JsonNode rootNode_vip = objectMapper.readTree(textInput_vip);
        JsonNode rootNode_list = objectMapper.readTree(textInput_list);
        JsonNode rootNode = objectMapper.readTree(textInput_index);

        Iterator<JsonNode> iterator_list = rootNode_list.elements();
        iterator_list=iterator_list.next().elements();
        String userName =iterator_list.next().textValue();
        iterator_list=iterator_list.next().elements();
        while (iterator_list.hasNext()) {
            num_product++;
            iterator_list.next();
        }
        String [] Barcode=new String[num_product];
        iterator_list = rootNode_list.elements().next().elements();
        iterator_list.next();
        iterator_list=iterator_list.next().elements();
        int i=0;
        while (iterator_list.hasNext()) {
            JsonNode jsonNode =iterator_list.next();
            Barcode[i]=jsonNode.textValue();
            i++;
        }//读取list文件


        Iterator<JsonNode> iterator_vip = rootNode_vip.elements();
        while (iterator_vip.hasNext()){
            num_vip++;
            iterator_vip.next();
        }
        String[]Username=new String[num_vip];
        Boolean[] IsVip=new Boolean[num_vip];
        int[]Score=new int[num_vip];
        iterator_vip=rootNode_vip.elements();
        i=0;
        while (iterator_vip.hasNext()) {
            Iterator<JsonNode> it =iterator_vip.next().elements();
            Username[i]=it.next().textValue();
            IsVip[i]=(it.next().asText().equals("true"));
            if(it.hasNext())
            Score[i]=(it.next().intValue());
            i++;
        }


        rootNode=rootNode.path(0);
        Iterator<JsonNode> iterator_index = rootNode.elements();
        String textInput_sync;
        textInput_sync="[";
        while (iterator_index.hasNext()) {
            JsonNode jsonNode =iterator_index.next();
            textInput_sync+=jsonNode;
            if(!iterator_index.hasNext());
            else textInput_sync+=",";
        }
        textInput_sync+="]";
        Item[] items;
        if(textInput_sync.toString().equals("[]"))
            throw new EmptyIndex();
        Item[]items_index = objectMapper.readValue(textInput_sync, Item[].class);
        items=new Item[num_product];


        if(num_product>0) {
            items[0] = items_index[0];
            items[0].setBarcode(Barcode[0]);
            if((items[0].getVipDiscount()!=1||items[0].getDiscount()!=1)&&items[0].isPromotion()==true)
                throw new PromoteAndTwo();
        }
        int j=1;
        for( i=1;i<num_product;i++)
        {
            if(Barcode[i].equals(Barcode[i-1]))
                items[i]=items[i-1];
            else {
                items[i]=items_index[j];
                j++;
            }
            items[i].setBarcode(Barcode[i]);
            if((items[0].getVipDiscount()!=1||items[0].getDiscount()!=1)&&items[0].isPromotion()==true)
                throw new PromoteAndTwo();
        }
        User user=new User();
        for(i=0;i<num_vip;i++)
            if(userName.equals(Username[i])) {
                user.setName(userName);
                user.setScore(Score[i]);
                user.setVip(IsVip[i]);
            }
        if(!user.getVip())
        for(i=0;i<num_product;i++)
        {
            items[i].setVipDiscount(0);
        }
        return BuildShoppingChart(items,user);
    }

}