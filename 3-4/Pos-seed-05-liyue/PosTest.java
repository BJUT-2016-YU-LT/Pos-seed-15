package com.thoughtworks.pos.domains;
import com.thoughtworks.pos.common.EmptyShoppingCartException;
import com.thoughtworks.pos.domains.Item;
import com.thoughtworks.pos.domains.Pos;
import com.thoughtworks.pos.domains.ShoppingChart;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * Created by Liyue on 2016/6/24.
 */
public class PosTest {
    @Test
    public void testGetCorrectShoppingListForSingleItem() throws Exception {
        // given
        Item cokeCola = new Item("ITEM000000", "可口可乐", "瓶", 3.00);
        ShoppingChart shoppingChart = new ShoppingChart();
        shoppingChart.add(cokeCola);

        // when
        Pos pos = new Pos();
        String actualShoppingList = pos.getShoppingList(shoppingChart);

        // then
        String expectedShoppingList =
                          "***商店购物清单***\n"
                        + "名称：可口可乐，数量：1瓶，单价：3.00(元)，小计：3.00(元)\n"
                        + "----------------------\n"
                        + "总计：3.00(元)\n"
                        + "**********************\n";
        assertThat(actualShoppingList, is(expectedShoppingList));
    }

    @Test
    public void testGetCorrectShoppingListForTwoSameItems() throws Exception {
        // given
        ShoppingChart shoppingChart = new ShoppingChart();
        shoppingChart.add(new Item("ITEM000000", "可口可乐", "瓶", 3.00));
        shoppingChart.add(new Item("ITEM000000", "可口可乐", "瓶", 3.00));

        // when
        Pos pos = new Pos();
        String actualShoppingList = pos.getShoppingList(shoppingChart);

        // then
        String expectedShoppingList =
                          "***商店购物清单***\n"
                        + "名称：可口可乐，数量：2瓶，单价：3.00(元)，小计：6.00(元)\n"
                        + "----------------------\n"
                        + "总计：6.00(元)\n"
                        + "**********************\n";
        assertThat(actualShoppingList, is(expectedShoppingList));
    }

    @Test
    public void testGetCorrectShoppingListForMultipleItemsWithMultipleTypes() throws Exception{
        // given
        ShoppingChart shoppingChart = new ShoppingChart();
        shoppingChart.add(new Item("ITEM000000", "雪碧", "瓶", 2.00));
        shoppingChart.add(new Item("ITEM000001", "可口可乐", "瓶", 3.00));

        // when
        Pos pos = new Pos();
        String actualShoppingList = pos.getShoppingList(shoppingChart);

        // then
        String expectedShoppingList =
                "***商店购物清单***\n"
                        + "名称：雪碧，数量：1瓶，单价：2.00(元)，小计：2.00(元)\n"
                        + "名称：可口可乐，数量：1瓶，单价：3.00(元)，小计：3.00(元)\n"
                        + "----------------------\n"
                        + "总计：5.00(元)\n"
                        + "**********************\n";
        assertThat(actualShoppingList, is(expectedShoppingList));
    }

    @Test
    public void testGetCorrectShoppingListWhenDifferentItemHaveSameItemName() throws  Exception{
        // given
        ShoppingChart shoppingChart = new ShoppingChart();
        shoppingChart.add(new Item("ITEM000000", "雪碧", "瓶", 2.00));
        shoppingChart.add(new Item("ITEM000002", "雪碧", "瓶", 3.00));

        // when
        Pos pos = new Pos();
        String actualShoppingList = pos.getShoppingList(shoppingChart);

        // then
        String expectedShoppingList =
                "***商店购物清单***\n"
                        + "名称：雪碧，数量：1瓶，单价：2.00(元)，小计：2.00(元)\n"
                        + "名称：雪碧，数量：1瓶，单价：3.00(元)，小计：3.00(元)\n"
                        + "----------------------\n"
                        + "总计：5.00(元)\n"
                        + "**********************\n";
        assertThat(actualShoppingList, is(expectedShoppingList));
    }

    @Test(expected = EmptyShoppingCartException.class)
    public void testThrowExceptionWhenNoItemsInShoppingCart() throws EmptyShoppingCartException{
        // given
        ShoppingChart shoppingChart = new ShoppingChart();

        // when
        Pos pos = new Pos();
        pos.getShoppingList(shoppingChart);
    }

    @Test
    public void testShouldSupportDiscountWhenHavingOneFavourableItem() throws EmptyShoppingCartException {
        // given
        ShoppingChart shoppingChart = new ShoppingChart();
        shoppingChart.add(new Item("ITEM000000", "雪碧", "瓶", 2.00, 0.8));

        // when
        Pos pos = new Pos();
        String actualShoppingList = pos.getShoppingList(shoppingChart);

        // then
        String expectedShoppingList =
                "***商店购物清单***\n"
                        + "名称：雪碧，数量：1瓶，单价：2.00(元)，小计：1.60(元)\n"
                        + "----------------------\n"
                        + "总计：1.60(元)\n"
                        + "节省：0.40(元)\n"
                        + "**********************\n";
        assertThat(actualShoppingList, is(expectedShoppingList));
    }
    @Test
    public void testGetCorrectShoppingListForReq2_battery() throws EmptyShoppingCartException {
        //given
        ShoppingChart shoppingChart = new ShoppingChart();
        shoppingChart.add(new Item("ITEM000004","电池","个",2.00,0.8));

        //when
        Pos pos = new Pos();
        String actualShoppingList = pos.getShoppingList(shoppingChart);

        //then
        String expectedShoppingList =
                "***商店购物清单***\n"
                        + "名称：电池，数量：1个，单价：2.00(元)，小计：1.60(元)\n"
                        + "----------------------\n"
                        + "总计：1.60(元)\n"
                        + "节省：0.40(元)\n"
                        + "**********************\n";
        assertThat(actualShoppingList, is(expectedShoppingList));
    }
    @Test
         public void testGetCorrectShoppingListForThreeItemsForReq4() throws Exception {
        // given
        ShoppingChart shoppingChart = new ShoppingChart();
        shoppingChart.add(new Item("ITEM000000", "可口可乐", "瓶", 3.00, 1,true));
        shoppingChart.add(new Item("ITEM000000", "可口可乐", "瓶", 3.00, 1, true));
        shoppingChart.add(new Item("ITEM000000", "可口可乐", "瓶", 3.00, 1, true));

        // when
        Pos pos = new Pos();
        String actualShoppingList = pos.getShoppingList(shoppingChart);

        // then
        String expectedShoppingList =
                "***商店购物清单***\n"
                        + "名称：可口可乐，数量：3瓶，单价：3.00(元)，小计：9.00(元)\n"
                        + "----------------------\n"
                        + "买二赠一商品:\n"
                        + "名称：可口可乐，数量：1瓶\n"
                        + "----------------------\n"
                        + "总计：9.00(元)\n"
                        + "节省：3.00(元)\n"
                        + "**********************\n";
        assertThat(actualShoppingList, is(expectedShoppingList));
    }


    @Test
    public void testGetCorrectShoppingListForTwoDiffer1ItemForReq4() throws Exception {
        // given
        ShoppingChart shoppingChart = new ShoppingChart();
        shoppingChart.add(new Item("ITEM000000", "可口可乐", "瓶", 3.00, 1, true));
        shoppingChart.add(new Item("ITEM000001", "雪碧", "瓶", 3.00, 1, true));

        // when
        Pos pos = new Pos();
        String actualShoppingList = pos.getShoppingList(shoppingChart);

        // then
        String expectedShoppingList =
                "***商店购物清单***\n"
                        + "名称：可口可乐，数量：1瓶，单价：3.00(元)，小计：3.00(元)\n"
                        + "名称：雪碧，数量：1瓶，单价：3.00(元)，小计：3.00(元)\n"
                        + "----------------------\n"
                        + "总计：6.00(元)\n"
                        + "**********************\n";
        assertThat(actualShoppingList, is(expectedShoppingList));
    }


    @Test
    public void testGetCorrectShoppingListForTwoDiffer2ItemForReq4() throws Exception {
        // given
        ShoppingChart shoppingChart = new ShoppingChart();
        shoppingChart.add(new Item("ITEM000000", "可口可乐", "瓶", 3.00, 1, true));
        shoppingChart.add(new Item("ITEM000004", "电池", "个", 2.00, 0.8, false));

        // when
        Pos pos = new Pos();
        String actualShoppingList = pos.getShoppingList(shoppingChart);

        // then
        String expectedShoppingList =
                "***商店购物清单***\n"
                        + "名称：可口可乐，数量：1瓶，单价：3.00(元)，小计：3.00(元)\n"
                        + "名称：电池，数量：1个，单价：2.00(元)，小计：1.60(元)\n"
                        + "----------------------\n"
                        + "总计：4.60(元)\n"
                        + "节省：0.40(元)\n"
                        + "**********************\n";
        assertThat(actualShoppingList, is(expectedShoppingList));
    }


    @Test
    public void testGetCorrectShoppingListForThreeItemsTwoForSaleForReq4() throws Exception {
        // given
        ShoppingChart shoppingChart = new ShoppingChart();
        shoppingChart.add(new Item("ITEM000000", "可口可乐", "瓶", 3.00, 1, true));
        shoppingChart.add(new Item("ITEM000000", "可口可乐", "瓶", 3.00, 1, true));
        shoppingChart.add(new Item("ITEM000004", "电池", "个", 2.00, 0.8, false));

        // when
        Pos pos = new Pos();
        String actualShoppingList = pos.getShoppingList(shoppingChart);

        // then
        String expectedShoppingList =
                "***商店购物清单***\n"
                        + "名称：可口可乐，数量：2瓶，单价：3.00(元)，小计：6.00(元)\n"
                        + "名称：电池，数量：1个，单价：2.00(元)，小计：1.60(元)\n"
                        + "----------------------\n"
                        + "买二赠一商品:\n"
                        + "名称：可口可乐，数量：1瓶\n"
                        + "----------------------\n"
                        + "总计：7.60(元)\n"
                        + "节省：3.40(元)\n"
                        + "**********************\n";
        assertThat(actualShoppingList, is(expectedShoppingList));
    }


    @Test
    public void testGetCorrectShoppingItemForReq4() throws Exception {
        // given
        ShoppingChart shoppingChart = new ShoppingChart();
        shoppingChart.add(new Item("ITEM000000", "可口可乐", "瓶", 3.00, 1, true));
        shoppingChart.add(new Item("ITEM000004", "电池", "个", 2.00, 0.8, false));
        shoppingChart.add(new Item("ITEM000004", "电池", "个", 2.00, 0.8, false));

        // when
        Pos pos = new Pos();
        String actualShoppingList = pos.getShoppingList(shoppingChart);

        // then
        String expectedShoppingList =
                "***商店购物清单***\n"
                        + "名称：可口可乐，数量：1瓶，单价：3.00(元)，小计：3.00(元)\n"
                        + "名称：电池，数量：2个，单价：2.00(元)，小计：3.20(元)\n"
                        + "----------------------\n"
                        + "总计：6.20(元)\n"
                        + "节省：0.80(元)\n"
                        + "**********************\n";
        assertThat(actualShoppingList, is(expectedShoppingList));
    }
    @Test
    public void testGetCorrectShoppingListForNoDiscountWithVIPInformationForReq6() throws Exception {
        // given
        ShoppingChart shoppingChart = new ShoppingChart();
        shoppingChart.setUser(new User("USER001",true,19));
        shoppingChart.add(new Item("ITEM000000", "可口可乐", "瓶", 3.00, 1, 1,false));
        shoppingChart.add(new Item("ITEM000002", "电池", "个", 1.00, 1,1,true));


        // when
        Pos pos = new Pos();
        String actualShoppingList = pos.getShoppingList(shoppingChart);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");


        // then
        String expectedShoppingList = new String();
        expectedShoppingList =
                "***商店购物清单***\n"
                        + "会员编号：USER001    会员积分：19分\n"
                        + "----------------------\n"
                        +"打印时间："
                        +new String(dateFormat.format(new Date()))+"\n"
                        +"----------------------\n"
                        + "名称：可口可乐，数量：1瓶，单价：3.00(元)，小计：3.00(元)\n"
                        + "名称：电池，数量：1个，单价：1.00(元)，小计：1.00(元)\n"
                        + "----------------------\n"
                        + "总计：4.00(元)\n"
                        + "**********************\n";
        assertThat(actualShoppingList, is(expectedShoppingList));
    }


    @Test
    public void testGetCorrectShoppingListFor0to200withVIPDiscountForReq6() throws Exception {
        // given
        ShoppingChart shoppingChart = new ShoppingChart();
        shoppingChart.setUser(new User("USER002",true,19));
        shoppingChart.add(new Item("ITEM000000", "可口可乐", "瓶", 3.00, 1,0.9,false));
        shoppingChart.add(new Item("ITEM000000", "可口可乐", "瓶", 3.00, 1,0.9,false));
        shoppingChart.add(new Item("ITEM000002", "电池", "个", 1.00, 1,1,true));


        // when

        Pos pos = new Pos();
        String actualShoppingList = pos.getShoppingList(shoppingChart);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");

        // then
        String expectedShoppingList = new String();
        expectedShoppingList =
                "***商店购物清单***\n"
                        + "会员编号：USER001    会员积分：20分\n"
                        + "----------------------\n"
                        +"打印时间："
                        +new String(dateFormat.format(new Date()))+"\n"
                        +"----------------------\n"
                        + "名称：可口可乐，数量：2瓶，单价：3.00(元)，小计：5.40(元)\n"
                        + "名称：电池，数量：1个，单价：1.00(元)，小计：1.00(元)\n"
                        + "----------------------\n"
                        + "总计：6.40(元)\n"
                        + "节省：0.60(元)\n"
                        + "**********************\n";
        assertThat(actualShoppingList, is(expectedShoppingList));
    }


    @Test
    public void testGetCorrectShoppingListFor0to200withVIPDiscount2ForReq6() throws Exception {
        // given
        ShoppingChart shoppingChart = new ShoppingChart();
        shoppingChart.setUser(new User("USER001",true,19));
        shoppingChart.add(new Item("ITEM000000", "可口可乐", "瓶", 3.00, 1,0.9,false));
        shoppingChart.add(new Item("ITEM000000", "可口可乐", "瓶", 3.00, 1,0.9,false));
        shoppingChart.add(new Item("ITEM000001", "雪碧", "瓶", 3.00, 0.8,0.95,false));
        shoppingChart.add(new Item("ITEM000001", "雪碧", "瓶", 3.00, 0.8, 0.95,false));
        shoppingChart.add(new Item("ITEM000002", "电池", "个", 1.00, 1, 1,true));


        // when
        Pos pos = new Pos();
        String actualShoppingList = pos.getShoppingList(shoppingChart);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");


        // then
        String expectedShoppingList = new String();
        expectedShoppingList =
                "***商店购物清单***\n"
                        + "会员编号：USER001    会员积分：21分 \n"
                        + "----------------------\n"
                        +"打印时间："
                        +new String(dateFormat.format(new Date()))+"\n"
                        +"----------------------\n"
                        + "名称：可口可乐，数量：2瓶，单价：3.00(元)，小计：5.40(元)\n"
                        + "名称：雪碧，数量：2瓶，单价：3.00(元)，小计：4.56(元)\n"
                        + "名称：电池，数量：1个，单价：1.00(元)，小计：1.00(元)\n"
                        + "----------------------\n"
                        + "总计：10.96(元)\n"
                        + "节省：2.04(元)\n"
                        + "**********************\n";
        assertThat(actualShoppingList, is(expectedShoppingList));
    }

    @Test
    public void testGetCorrectShoppingListFor200to500SingleItemForReq6() throws Exception {
        // given
        ShoppingChart shoppingChart = new ShoppingChart();
        shoppingChart.setUser(new User("USER001",true,19));
        shoppingChart.add(new Item("ITEM000000", "可口可乐", "瓶", 3.00, 1, 0.9,false));


        // when
        Pos pos = new Pos();
        String actualShoppingList = pos.getShoppingList(shoppingChart);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");


        // then
        String expectedShoppingList = new String();
        expectedShoppingList =
                "***商店购物清单***\n"
                        + "会员编号：USER001    会员积分：19分\n"
                        + "----------------------\n"
                        +"打印时间："
                        +new String(dateFormat.format(new Date()))+"\n"
                        +"----------------------\n"
                        + "名称：可口可乐，数量：1瓶，单价：3.00(元)，小计：2.70(元)\n"
                        + "----------------------\n"
                        + "总计：2.70(元)\n"
                        + "节省：2.04(元)\n"
                        + "**********************\n";
        assertThat(actualShoppingList, is(expectedShoppingList));
    }


    @Test
    public void testGetCorrectShoppingListFor200to500TwoItemsWithVIPDiscountForReq6() throws Exception {
        // given
        ShoppingChart shoppingChart = new ShoppingChart();
        shoppingChart.setUser(new User("USER001",true,19));
        shoppingChart.add(new Item("ITEM000000", "可口可乐", "瓶", 3.00, 1, 0.9,false));
        shoppingChart.add(new Item("ITEM000000", "可口可乐", "瓶", 3.00, 1, 0.9,false));


        // when
        Pos pos = new Pos();
        String actualShoppingList = pos.getShoppingList(shoppingChart);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");


        // then
        String expectedShoppingList = new String();
        expectedShoppingList =
                "***商店购物清单***\n"
                        + "会员编号：USER001    会员积分：22分\n"
                        + "----------------------\n"
                        +"打印时间："
                        +new String(dateFormat.format(new Date()))+"\n"
                        +"----------------------\n"
                        + "名称：可口可乐，数量：2瓶，单价：3.00(元)，小计：5.40(元)\n"
                        + "----------------------\n"
                        + "总计：5.40(元)\n"
                        + "节省：0.60(元)\n"
                        + "**********************\n";
        assertThat(actualShoppingList, is(expectedShoppingList));
    }


    @Test
    public void testGetCorrectShoppingListFor200to500ForItemsWithVIPDiscountForReq6() throws Exception {
        // given
        ShoppingChart shoppingChart = new ShoppingChart();
        shoppingChart.setUser(new User("USER001",true,19));
        shoppingChart.add(new Item("ITEM000000", "可口可乐", "瓶", 3.00, 1, 0.9,false));
        shoppingChart.add(new Item("ITEM000000", "可口可乐", "瓶", 3.00, 1, 0.9,false));
        shoppingChart.add(new Item("ITEM000000", "可口可乐", "瓶", 3.00, 1,0.9,false));
        shoppingChart.add(new Item("ITEM000000", "可口可乐", "瓶", 3.00, 1,0.9,false));


        // when
        Pos pos = new Pos();
        String actualShoppingList = pos.getShoppingList(shoppingChart);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");


        // then
        String expectedShoppingList = new String();
        expectedShoppingList =
                "***商店购物清单***\n"
                        + "会员编号：USER001    会员积分：25分\n"
                        + "----------------------\n"
                        +"打印时间："
                        +new String(dateFormat.format(new Date()))+"\n"
                        +"----------------------\n"
                        + "名称：可口可乐，数量：4瓶，单价：3.00(元)，小计：10.80(元)\n"
                        + "----------------------\n"
                        + "总计：10.80(元)\n"
                        + "节省：1.20(元)\n"
                        + "**********************\n";
        assertThat(actualShoppingList, is(expectedShoppingList));
    }
    @Test
    public void testGetCorrectShoppingListForMoreThan500SingleItemForReq6() throws Exception {
        // given
        ShoppingChart shoppingChart = new ShoppingChart();
        shoppingChart.setUser(new User("USER001",true,19));
        shoppingChart.add(new Item("ITEM000000", "可口可乐", "瓶", 3.00, 1,0.9,false));


        // when
        Pos pos = new Pos();
        String actualShoppingList = pos.getShoppingList(shoppingChart);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");


        // then
        String expectedShoppingList = new String();
        expectedShoppingList =
                "***商店购物清单***\n"
                        + "会员编号：USER001    会员积分：19分\n"
                        + "----------------------\n"
                        +"打印时间："
                        +new String(dateFormat.format(new Date()))+"\n"
                        +"----------------------\n"
                        + "名称：可口可乐，数量：1瓶，单价：3.00(元)，小计：3.00(元)\n"
                        + "----------------------\n"
                        + "总计：2.70(元)\n"
                        + "节省：0.30(元)\n"
                        + "**********************\n";
        assertThat(actualShoppingList, is(expectedShoppingList));
    }


    @Test
    public void testGetCorrectShoppingListForMoreThan500SingleMultiItem1ForReq6() throws Exception {
        // given
        ShoppingChart shoppingChart = new ShoppingChart();
        shoppingChart.setUser(new User("USER001",true,19));
        shoppingChart.add(new Item("ITEM000000", "可口可乐", "瓶", 3.00, 1, 0.9,false));
        shoppingChart.add(new Item("ITEM000000", "可口可乐", "瓶", 3.00, 1, 0.9,false));


        // when
        Pos pos = new Pos();
        String actualShoppingList = pos.getShoppingList(shoppingChart);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");


        // then
        String expectedShoppingList = new String();
        expectedShoppingList =
                "***商店购物清单***\n"
                        + "会员编号：USER001    会员积分：24分\n"
                        + "----------------------\n"
                        +"打印时间："
                        +new String(dateFormat.format(new Date()))+"\n"
                        +"----------------------\n"
                        + "名称：可口可乐，数量：2瓶，单价：3.00(元)，小计：5.40(元)\n"
                        + "----------------------\n"
                        + "总计：5.40(元)\n"
                        + "节省：0.60(元)\n"
                        + "**********************\n";
        assertThat(actualShoppingList, is(expectedShoppingList));
    }


    @Test
    public void testGetCorrectShoppingListForMoreThan500SingleMultiItem2ForReq6() throws Exception {
        // given
        ShoppingChart shoppingChart = new ShoppingChart();
        shoppingChart.setUser(new User("USER001",true,19));
        shoppingChart.add(new Item("ITEM000000", "可口可乐", "瓶", 3.00, 1, 0.9,false));
        shoppingChart.add(new Item("ITEM000000", "可口可乐", "瓶", 3.00, 1, 0.9,false));
        shoppingChart.add(new Item("ITEM000000", "可口可乐", "瓶", 3.00, 1, 0.9,false));
        shoppingChart.add(new Item("ITEM000000", "可口可乐", "瓶", 3.00, 1,0.9,false));


        // when
        Pos pos = new Pos();
        String actualShoppingList = pos.getShoppingList(shoppingChart);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");


        // then
        String expectedShoppingList = new String();
        expectedShoppingList =
                "***商店购物清单***\n"
                        + "会员编号：USER001    会员积分：29分\n"
                        + "----------------------\n"
                        +"打印时间："
                        +new String(dateFormat.format(new Date()))+"\n"
                        +"----------------------\n"
                        + "名称：可口可乐，数量：4瓶，单价：3.00(元)，小计：10.80(元)\n"
                        + "----------------------\n"
                        + "总计：10.80(元)\n"
                        + "节省：1.20(元)\n"
                        + "**********************\n";
        assertThat(actualShoppingList, is(expectedShoppingList));
    }




}