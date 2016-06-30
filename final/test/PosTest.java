import com.thoughtworks.pos.common.EmptyShoppingCartException;
import com.thoughtworks.pos.domains.Item;
import com.thoughtworks.pos.domains.Pos;
import com.thoughtworks.pos.domains.ShoppingChart;
import com.thoughtworks.pos.domains.User;
import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.Date;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * Created by zyl on 2016/06/26.
 */
public class PosTest {
    @Test(expected = EmptyShoppingCartException.class)//没有商品
    public void testThrowExceptionWhenNoItemsInShoppingCart() throws EmptyShoppingCartException{
        // given
        ShoppingChart shoppingChart = new ShoppingChart();

        // when
        Pos pos = new Pos();
        pos.getShoppingList(shoppingChart);
    }
    @Test//只有一个商品
    public void testGetCorrectShoppingListForSingleItem() throws Exception {
        // given
        Item cokeCola = new Item("ITEM000000", "可口可乐", "瓶", 3.00);
        ShoppingChart shoppingChart = new ShoppingChart();
        shoppingChart.add(cokeCola);

        // when
        Pos pos = new Pos();
        String actualShoppingList = pos.getShoppingList(shoppingChart);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
        // then
        String expectedShoppingList =
                "***商店购物清单***\n"
                        +"打印时间："
                        +new String(dateFormat.format(new Date()))+"\n"
                        +"----------------------\n"
                        + "名称：可口可乐，数量：1瓶，单价：3.00(元)，小计：3.00(元)\n"
                        + "----------------------\n"
                        + "总计：3.00(元)\n"
                        + "**********************\n";
        assertThat(actualShoppingList, is(expectedShoppingList));
    }
    @Test//有两个相同的商品
    public void testGetCorrectShoppingListForTwoSameItems() throws Exception {
        // given
        ShoppingChart shoppingChart = new ShoppingChart();
        shoppingChart.add(new Item("ITEM000000", "可口可乐", "瓶", 3.00));
        shoppingChart.add(new Item("ITEM000000", "可口可乐", "瓶", 3.00));

        // when
        Pos pos = new Pos();
        String actualShoppingList = pos.getShoppingList(shoppingChart);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
        // then
        String expectedShoppingList =
                "***商店购物清单***\n"
                        +"打印时间："
                        +new String(dateFormat.format(new Date()))+"\n"
                        +"----------------------\n"
                        + "名称：可口可乐，数量：2瓶，单价：3.00(元)，小计：6.00(元)\n"
                        + "----------------------\n"
                        + "总计：6.00(元)\n"
                        + "**********************\n";
        assertThat(actualShoppingList, is(expectedShoppingList));
    }
    @Test//两个单位相同的商品
    public void testGetCorrectShoppingListForMultipleItemsWithMultipleTypes() throws Exception{
        // given
        ShoppingChart shoppingChart = new ShoppingChart();
        shoppingChart.add(new Item("ITEM000000", "雪碧", "瓶", 2.00));
        shoppingChart.add(new Item("ITEM000001", "可口可乐", "瓶", 3.00));

        // when
        Pos pos = new Pos();
        String actualShoppingList = pos.getShoppingList(shoppingChart);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
        // then
        String expectedShoppingList =
                "***商店购物清单***\n"
                        +"打印时间："
                        +new String(dateFormat.format(new Date()))+"\n"
                        +"----------------------\n"
                        + "名称：雪碧，数量：1瓶，单价：2.00(元)，小计：2.00(元)\n"
                        + "名称：可口可乐，数量：1瓶，单价：3.00(元)，小计：3.00(元)\n"
                        + "----------------------\n"
                        + "总计：5.00(元)\n"
                        + "**********************\n";
        assertThat(actualShoppingList, is(expectedShoppingList));
    }
    @Test//两个编号不同其余相同的商品
    public void testGetCorrectShoppingListWhenDifferentItemHaveSameItemName() throws  Exception{
        // given
        ShoppingChart shoppingChart = new ShoppingChart();
        shoppingChart.add(new Item("ITEM000000", "雪碧", "瓶", 2.00));
        shoppingChart.add(new Item("ITEM000002", "雪碧", "瓶", 3.00));

        // when
        Pos pos = new Pos();
        String actualShoppingList = pos.getShoppingList(shoppingChart);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
        // then
        String expectedShoppingList =
                "***商店购物清单***\n"
                        +"打印时间："
                        +new String(dateFormat.format(new Date()))+"\n"
                        +"----------------------\n"
                        + "名称：雪碧，数量：1瓶，单价：2.00(元)，小计：2.00(元)\n"
                        + "名称：雪碧，数量：1瓶，单价：3.00(元)，小计：3.00(元)\n"
                        + "----------------------\n"
                        + "总计：5.00(元)\n"
                        + "**********************\n";
        assertThat(actualShoppingList, is(expectedShoppingList));
    }
    @Test//一个打折的商品
    public void testShouldSupportDiscountWhenHavingOneFavourableItem() throws EmptyShoppingCartException {
        // given
        ShoppingChart shoppingChart = new ShoppingChart();
        shoppingChart.add(new Item("ITEM000000", "雪碧", "瓶", 2.00, 0.8));

        // when
        Pos pos = new Pos();
        String actualShoppingList = pos.getShoppingList(shoppingChart);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
        // then
        String expectedShoppingList =
                "***商店购物清单***\n"
                        +"打印时间："
                        +new String(dateFormat.format(new Date()))+"\n"
                        +"----------------------\n"
                        + "名称：雪碧，数量：1瓶，单价：2.00(元)，小计：1.60(元)\n"
                        + "----------------------\n"
                        + "总计：1.60(元)\n"
                        + "节省：0.40(元)\n"
                        + "**********************\n";
        assertThat(actualShoppingList, is(expectedShoppingList));
    }
    @Test//测试文档中正确的数据（三个促销商品）
    public void testGetCorrectShoppingListForThreeItemsForReq4() throws Exception {
        // given
        ShoppingChart shoppingChart = new ShoppingChart();
        shoppingChart.add(new Item("ITEM000000", "可口可乐", "瓶", 3.00, 1,true));
        shoppingChart.add(new Item("ITEM000000", "可口可乐", "瓶", 3.00, 1, true));
        shoppingChart.add(new Item("ITEM000000", "可口可乐", "瓶", 3.00, 1, true));

        // when
        Pos pos = new Pos();
        String actualShoppingList = pos.getShoppingList(shoppingChart);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
        // then
        String expectedShoppingList =
                "***商店购物清单***\n"
                        +"打印时间："
                        +new String(dateFormat.format(new Date()))+"\n"
                        +"----------------------\n"
                        + "名称：可口可乐，数量：3瓶，单价：3.00(元)，小计：6.00(元)\n"
                        + "----------------------\n"
                        + "挥泪赠送商品:\n"
                        + "名称：可口可乐，数量：1瓶\n"
                        + "----------------------\n"
                        + "总计：6.00(元)\n"
                        + "节省：3.00(元)\n"
                        + "**********************\n";
        assertThat(actualShoppingList, is(expectedShoppingList));
    }
    @Test//测试文档中正确的数据（二个促销商品）
    public void testGetCorrectShoppingListForTwoItemsForReq4() throws Exception {
        // given
        ShoppingChart shoppingChart = new ShoppingChart();
        shoppingChart.add(new Item("ITEM000000", "可口可乐", "瓶", 3.00, 1,true));
        shoppingChart.add(new Item("ITEM000000", "可口可乐", "瓶", 3.00, 1, true));

        // when
        Pos pos = new Pos();
        String actualShoppingList = pos.getShoppingList(shoppingChart);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
        // then
        String expectedShoppingList =
                "***商店购物清单***\n"
                        +"打印时间："
                        +new String(dateFormat.format(new Date()))+"\n"
                        +"----------------------\n"
                        + "名称：可口可乐，数量：2瓶，单价：3.00(元)，小计：6.00(元)\n"
                        + "----------------------\n"
                        + "总计：6.00(元)\n"
                        + "**********************\n";
        assertThat(actualShoppingList, is(expectedShoppingList));
    }
    @Test//测试购买四个促销商品
    public void testGetCorrectShoppingListForFourItemsForReq4() throws Exception {
        // given
        ShoppingChart shoppingChart = new ShoppingChart();
        shoppingChart.add(new Item("ITEM000000", "可口可乐", "瓶", 3.00, 1,true));
        shoppingChart.add(new Item("ITEM000000", "可口可乐", "瓶", 3.00, 1, true));
        shoppingChart.add(new Item("ITEM000000", "可口可乐", "瓶", 3.00, 1, true));
        shoppingChart.add(new Item("ITEM000000", "可口可乐", "瓶", 3.00, 1, true));

        // when
        Pos pos = new Pos();
        String actualShoppingList = pos.getShoppingList(shoppingChart);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
        // then
        String expectedShoppingList =
                "***商店购物清单***\n"
                        +"打印时间："
                        +new String(dateFormat.format(new Date()))+"\n"
                        +"----------------------\n"
                        + "名称：可口可乐，数量：4瓶，单价：3.00(元)，小计：9.00(元)\n"
                        + "----------------------\n"
                        + "挥泪赠送商品:\n"
                        + "名称：可口可乐，数量：1瓶\n"
                        + "----------------------\n"
                        + "总计：9.00(元)\n"
                        + "节省：3.00(元)\n"
                        + "**********************\n";
        assertThat(actualShoppingList, is(expectedShoppingList));
    }
    @Test//测试购买五个促销商品（顾客不要第二个赠品的情况）
    public void testGetCorrectShoppingListForFiveItemsForReq4() throws Exception {
        // given
        ShoppingChart shoppingChart = new ShoppingChart();
        shoppingChart.add(new Item("ITEM000000", "可口可乐", "瓶", 3.00, 1,true));
        shoppingChart.add(new Item("ITEM000000", "可口可乐", "瓶", 3.00, 1, true));
        shoppingChart.add(new Item("ITEM000000", "可口可乐", "瓶", 3.00, 1, true));
        shoppingChart.add(new Item("ITEM000000", "可口可乐", "瓶", 3.00, 1, true));
        shoppingChart.add(new Item("ITEM000000", "可口可乐", "瓶", 3.00, 1, true));

        // when
        Pos pos = new Pos();
        String actualShoppingList = pos.getShoppingList(shoppingChart);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
        // then
        String expectedShoppingList =
                "***商店购物清单***\n"
                        +"打印时间："
                        +new String(dateFormat.format(new Date()))+"\n"
                        +"----------------------\n"
                        + "名称：可口可乐，数量：5瓶，单价：3.00(元)，小计：12.00(元)\n"
                        + "----------------------\n"
                        + "挥泪赠送商品:\n"
                        + "名称：可口可乐，数量：1瓶\n"
                        + "----------------------\n"
                        + "总计：12.00(元)\n"
                        + "节省：3.00(元)\n"
                        + "**********************\n";
        assertThat(actualShoppingList, is(expectedShoppingList));
    }
    @Test//测试购买六个促销商品
    public void testGetCorrectShoppingListForSixItemsForReq4() throws Exception {
        // given
        ShoppingChart shoppingChart = new ShoppingChart();
        shoppingChart.add(new Item("ITEM000000", "可口可乐", "瓶", 3.00, 1,true));
        shoppingChart.add(new Item("ITEM000000", "可口可乐", "瓶", 3.00, 1, true));
        shoppingChart.add(new Item("ITEM000000", "可口可乐", "瓶", 3.00, 1, true));
        shoppingChart.add(new Item("ITEM000000", "可口可乐", "瓶", 3.00, 1, true));
        shoppingChart.add(new Item("ITEM000000", "可口可乐", "瓶", 3.00, 1, true));
        shoppingChart.add(new Item("ITEM000000", "可口可乐", "瓶", 3.00, 1, true));

        // when
        Pos pos = new Pos();
        String actualShoppingList = pos.getShoppingList(shoppingChart);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
        // then
        String expectedShoppingList =
                "***商店购物清单***\n"
                        +"打印时间："
                        +new String(dateFormat.format(new Date()))+"\n"
                        +"----------------------\n"
                        + "名称：可口可乐，数量：6瓶，单价：3.00(元)，小计：12.00(元)\n"
                        + "----------------------\n"
                        + "挥泪赠送商品:\n"
                        + "名称：可口可乐，数量：2瓶\n"
                        + "----------------------\n"
                        + "总计：12.00(元)\n"
                        + "节省：6.00(元)\n"
                        + "**********************\n";
        assertThat(actualShoppingList, is(expectedShoppingList));
    }
    @Test//测试两个不同的促销商品只买一个的结果
    public void testGetCorrectShoppingListForTwoDiffer1ItemForReq4() throws Exception {
        // given
        ShoppingChart shoppingChart = new ShoppingChart();
        shoppingChart.add(new Item("ITEM000000", "可口可乐", "瓶", 3.00, 1, true));
        shoppingChart.add(new Item("ITEM000001", "雪碧", "瓶", 3.00, 1, true));

        // when
        Pos pos = new Pos();
        String actualShoppingList = pos.getShoppingList(shoppingChart);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
        // then
        String expectedShoppingList =
                "***商店购物清单***\n"
                        +"打印时间："
                        +new String(dateFormat.format(new Date()))+"\n"
                        +"----------------------\n"
                        + "名称：可口可乐，数量：1瓶，单价：3.00(元)，小计：3.00(元)\n"
                        + "名称：雪碧，数量：1瓶，单价：3.00(元)，小计：3.00(元)\n"
                        + "----------------------\n"
                        + "总计：6.00(元)\n"
                        + "**********************\n";
        assertThat(actualShoppingList, is(expectedShoppingList));
    }
    @Test//测试一个打折一个促销的结果
    public void testGetCorrectShoppingListForTwoDiffer2ItemForReq4() throws Exception {
        // given
        ShoppingChart shoppingChart = new ShoppingChart();
        shoppingChart.add(new Item("ITEM000000", "可口可乐", "瓶", 3.00, 1, true));
        shoppingChart.add(new Item("ITEM000004", "电池", "个", 2.00, 0.8, false));

        // when
        Pos pos = new Pos();
        String actualShoppingList = pos.getShoppingList(shoppingChart);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
        // then
        String expectedShoppingList =
                "***商店购物清单***\n"
                        +"打印时间："
                        +new String(dateFormat.format(new Date()))+"\n"
                        +"----------------------\n"
                        + "名称：可口可乐，数量：1瓶，单价：3.00(元)，小计：3.00(元)\n"
                        + "名称：电池，数量：1个，单价：2.00(元)，小计：1.60(元)\n"
                        + "----------------------\n"
                        + "总计：4.60(元)\n"
                        + "节省：0.40(元)\n"
                        + "**********************\n";
        assertThat(actualShoppingList, is(expectedShoppingList));
    }
    @Test//测试只买两瓶赠送商品（规定如果清单里只有两瓶表示客户并不需要赠品）和一个打折商品
    public void testGetCorrectShoppingListForThreeItemsTwoForSaleForReq4() throws Exception {
        // given
        ShoppingChart shoppingChart = new ShoppingChart();
        shoppingChart.add(new Item("ITEM000000", "可口可乐", "瓶", 3.00, 1, true));
        shoppingChart.add(new Item("ITEM000000", "可口可乐", "瓶", 3.00, 1, true));
        shoppingChart.add(new Item("ITEM000004", "电池", "个", 2.00, 0.8, false));

        // when
        Pos pos = new Pos();
        String actualShoppingList = pos.getShoppingList(shoppingChart);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
        // then
        String expectedShoppingList =
                "***商店购物清单***\n"
                        +"打印时间："
                        +new String(dateFormat.format(new Date()))+"\n"
                        +"----------------------\n"
                        + "名称：可口可乐，数量：2瓶，单价：3.00(元)，小计：6.00(元)\n"
                        + "名称：电池，数量：1个，单价：2.00(元)，小计：1.60(元)\n"
                        + "----------------------\n"
                        + "总计：7.60(元)\n"
                        + "节省：0.40(元)\n"
                        + "**********************\n";
        assertThat(actualShoppingList, is(expectedShoppingList));
    }
    @Test//测试两个打折商品和一个促销商品
    public void testGetCorrectShoppingItemForReq4() throws Exception {
        // given
        ShoppingChart shoppingChart = new ShoppingChart();
        shoppingChart.add(new Item("ITEM000000", "可口可乐", "瓶", 3.00, 1, true));
        shoppingChart.add(new Item("ITEM000004", "电池", "个", 2.00, 0.8, false));
        shoppingChart.add(new Item("ITEM000004", "电池", "个", 2.00, 0.8, false));

        // when
        Pos pos = new Pos();
        String actualShoppingList = pos.getShoppingList(shoppingChart);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
        // then
        String expectedShoppingList =
                "***商店购物清单***\n"
                        +"打印时间："
                        +new String(dateFormat.format(new Date()))+"\n"
                        +"----------------------\n"
                        + "名称：可口可乐，数量：1瓶，单价：3.00(元)，小计：3.00(元)\n"
                        + "名称：电池，数量：2个，单价：2.00(元)，小计：3.20(元)\n"
                        + "----------------------\n"
                        + "总计：6.20(元)\n"
                        + "节省：0.80(元)\n"
                        + "**********************\n";
        assertThat(actualShoppingList, is(expectedShoppingList));
    }
    @Test//测试两个打折商品和三个促销商品
    public void testGetCorrectShoppingItemForReq41() throws Exception {
        // given
        ShoppingChart shoppingChart = new ShoppingChart();
        shoppingChart.add(new Item("ITEM000000", "可口可乐", "瓶", 3.00, 1, true));
        shoppingChart.add(new Item("ITEM000000", "可口可乐", "瓶", 3.00, 1, true));
        shoppingChart.add(new Item("ITEM000000", "可口可乐", "瓶", 3.00, 1, true));
        shoppingChart.add(new Item("ITEM000004", "电池", "个", 2.00, 0.8, false));
        shoppingChart.add(new Item("ITEM000004", "电池", "个", 2.00, 0.8, false));

        // when
        Pos pos = new Pos();
        String actualShoppingList = pos.getShoppingList(shoppingChart);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
        // then
        String expectedShoppingList =
                "***商店购物清单***\n"
                        +"打印时间："
                        +new String(dateFormat.format(new Date()))+"\n"
                        +"----------------------\n"
                        + "名称：可口可乐，数量：3瓶，单价：3.00(元)，小计：6.00(元)\n"
                        + "名称：电池，数量：2个，单价：2.00(元)，小计：3.20(元)\n"
                        + "----------------------\n"
                        + "挥泪赠送商品:\n"
                        + "名称：可口可乐，数量：1瓶\n"
                        + "----------------------\n"
                        + "总计：9.20(元)\n"
                        + "节省：3.80(元)\n"
                        + "**********************\n";
        assertThat(actualShoppingList, is(expectedShoppingList));
    }

    @Test//需求五例子 会员+打折+会员打折
    public void testGetCorrectShoppingListForDiscountWithVIPInformationForReq5() throws Exception {
        // given
        ShoppingChart shoppingChart = new ShoppingChart();
        shoppingChart.setUser(new User("USER001",true,0));
        shoppingChart.add(new Item("ITEM000000", "可口可乐", "瓶", 3.00, 0.8, 0.5,false));
        shoppingChart.add(new Item("ITEM000002", "电池", "个", 1.00, 1,1,true));


        // when
        Pos pos = new Pos();
        String actualShoppingList = pos.getShoppingList(shoppingChart);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");


        // then
        String expectedShoppingList = new String();
        expectedShoppingList =
                "***商店购物清单***\n"
                        + "会员编号：  USER001    会员积分：  0分\n"
                        + "----------------------\n"
                        +"打印时间："
                        +new String(dateFormat.format(new Date()))+"\n"
                        +"----------------------\n"
                        + "名称：可口可乐，数量：1瓶，单价：3.00(元)，小计：1.20(元)\n"
                        + "名称：电池，数量：1个，单价：1.00(元)，小计：1.00(元)\n"
                        + "----------------------\n"
                        + "总计：2.20(元)\n"
                        + "节省：1.80(元)\n"
                        + "**********************\n";
        assertThat(actualShoppingList, is(expectedShoppingList));
    }
    @Test//需求五例子 会员+不打折+会员打折
    public void testGetCorrectShoppingListForNoDiscountWithVIPInformationForReq5() throws Exception {
        // given
        ShoppingChart shoppingChart = new ShoppingChart();
        shoppingChart.setUser(new User("USER001",true,0));
        shoppingChart.add(new Item("ITEM000000", "可口可乐", "瓶", 3.00, 1, 0.5,false));
        shoppingChart.add(new Item("ITEM000002", "电池", "个", 1.00, 1,1,false));


        // when
        Pos pos = new Pos();
        String actualShoppingList = pos.getShoppingList(shoppingChart);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");


        // then
        String expectedShoppingList = new String();
        expectedShoppingList =
                "***商店购物清单***\n"
                        + "会员编号：  USER001    会员积分：  0分\n"
                        + "----------------------\n"
                        +"打印时间："
                        +new String(dateFormat.format(new Date()))+"\n"
                        +"----------------------\n"
                        + "名称：可口可乐，数量：1瓶，单价：3.00(元)，小计：1.50(元)\n"
                        + "名称：电池，数量：1个，单价：1.00(元)，小计：1.00(元)\n"
                        + "----------------------\n"
                        + "总计：2.50(元)\n"
                        + "节省：1.50(元)\n"
                        + "**********************\n";
        assertThat(actualShoppingList, is(expectedShoppingList));
    }
    @Test//需求五例子 会员+打折+会员不打折
    public void testGetCorrectShoppingListForDiscountWithVIPNoInformationForReq5() throws Exception {
        // given
        ShoppingChart shoppingChart = new ShoppingChart();
        shoppingChart.setUser(new User("USER001",true,0));
        shoppingChart.add(new Item("ITEM000000", "可口可乐", "瓶", 3.00, 0.5, 1,false));
        shoppingChart.add(new Item("ITEM000002", "电池", "个", 1.00, 1,1,false));


        // when
        Pos pos = new Pos();
        String actualShoppingList = pos.getShoppingList(shoppingChart);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");


        // then
        String expectedShoppingList = new String();
        expectedShoppingList =
                "***商店购物清单***\n"
                        + "会员编号：  USER001    会员积分：  0分\n"
                        + "----------------------\n"
                        +"打印时间："
                        +new String(dateFormat.format(new Date()))+"\n"
                        +"----------------------\n"
                        + "名称：可口可乐，数量：1瓶，单价：3.00(元)，小计：1.50(元)\n"
                        + "名称：电池，数量：1个，单价：1.00(元)，小计：1.00(元)\n"
                        + "----------------------\n"
                        + "总计：2.50(元)\n"
                        + "节省：1.50(元)\n"
                        + "**********************\n";
        assertThat(actualShoppingList, is(expectedShoppingList));
    }
    @Test//需求五例子 非会员+打折+会员不打折
    public void testGetCorrectShoppingListForDiscountWithNoVIPInformationForReq5() throws Exception {
        // given
        ShoppingChart shoppingChart = new ShoppingChart();
        shoppingChart.setUser(new User("USER001",false,0));
        shoppingChart.add(new Item("ITEM000000", "可口可乐", "瓶", 3.00, 0.5, 1,false));
        shoppingChart.add(new Item("ITEM000002", "电池", "个", 1.00, 1,1,false));


        // when
        Pos pos = new Pos();
        String actualShoppingList = pos.getShoppingList(shoppingChart);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");


        // then
        String expectedShoppingList = new String();
        expectedShoppingList =
                "***商店购物清单***\n"
                        +"打印时间："
                        +new String(dateFormat.format(new Date()))+"\n"
                        +"----------------------\n"
                        + "名称：可口可乐，数量：1瓶，单价：3.00(元)，小计：1.50(元)\n"
                        + "名称：电池，数量：1个，单价：1.00(元)，小计：1.00(元)\n"
                        + "----------------------\n"
                        + "总计：2.50(元)\n"
                        + "节省：1.50(元)\n"
                        + "**********************\n";
        assertThat(actualShoppingList, is(expectedShoppingList));
    }
    @Test//需求五例子 非会员+不打折+会员不打折
    public void testGetCorrectShoppingListForNoDiscountWithNoVIPInformationForReq5() throws Exception {
        // given
        ShoppingChart shoppingChart = new ShoppingChart();
        shoppingChart.setUser(new User("USER001",false,0));
        shoppingChart.add(new Item("ITEM000000", "可口可乐", "瓶", 3.00, 1, 1,false));
        shoppingChart.add(new Item("ITEM000002", "电池", "个", 1.00, 1,1,false));


        // when
        Pos pos = new Pos();
        String actualShoppingList = pos.getShoppingList(shoppingChart);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");


        // then
        String expectedShoppingList = new String();
        expectedShoppingList =
                "***商店购物清单***\n"
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

    @Test//会员不打折
    public void testGetCorrectShoppingListForNoDiscountWithVIPInformationForReq6() throws Exception {
    // given
    ShoppingChart shoppingChart = new ShoppingChart();
    shoppingChart.setUser(new User("USER001",true,19));
    shoppingChart.add(new Item("ITEM000000", "可口可乐", "瓶", 3.00, 1, 1,false));
    shoppingChart.add(new Item("ITEM000002", "电池", "个", 1.00, 1,1,false));


    // when
    Pos pos = new Pos();
    String actualShoppingList = pos.getShoppingList(shoppingChart);
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");


    // then
    String expectedShoppingList = new String();
    expectedShoppingList =
            "***商店购物清单***\n"
                    + "会员编号：  USER001    会员积分：  19分\n"
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
    @Test//会员积分0-200，5元区
    public void testGetCorrectShoppingListFor0to200withVIPDiscountForReq6() throws Exception {
        // given
        ShoppingChart shoppingChart = new ShoppingChart();
        shoppingChart.setUser(new User("USER001",true,19));
        shoppingChart.add(new Item("ITEM000000", "可口可乐", "瓶", 3.00, 1,0.9,false));
        shoppingChart.add(new Item("ITEM000000", "可口可乐", "瓶", 3.00, 1,0.9,false));
        shoppingChart.add(new Item("ITEM000002", "电池", "个", 1.00, 1,1,false));


        // when

        Pos pos = new Pos();
        String actualShoppingList = pos.getShoppingList(shoppingChart);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");

        // then
        String expectedShoppingList = new String();
        expectedShoppingList =
                "***商店购物清单***\n"
                        + "会员编号：  USER001    会员积分：  20分\n"
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
    @Test//会员积分0-200，10元区
    public void testGetCorrectShoppingListFor0to200withVIPDiscount2ForReq6() throws Exception {
        // given
        ShoppingChart shoppingChart = new ShoppingChart();
        shoppingChart.setUser(new User("USER001",true,19));
        shoppingChart.add(new Item("ITEM000000", "可口可乐", "瓶", 3.00, 1,0.9,false));
        shoppingChart.add(new Item("ITEM000000", "可口可乐", "瓶", 3.00, 1,0.9,false));
        shoppingChart.add(new Item("ITEM000001", "雪碧", "瓶", 3.00, 0.8,0.95,false));
        shoppingChart.add(new Item("ITEM000001", "雪碧", "瓶", 3.00, 0.8, 0.95,false));
        shoppingChart.add(new Item("ITEM000002", "电池", "个", 1.00, 1, 1,false));


        // when
        Pos pos = new Pos();
        String actualShoppingList = pos.getShoppingList(shoppingChart);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");


        // then
        String expectedShoppingList = new String();
        expectedShoppingList =
                "***商店购物清单***\n"
                        + "会员编号：  USER001    会员积分：  21分\n"
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
    @Test//会员积分200-500，0元区
    public void testGetCorrectShoppingListFor200to500SingleItemForReq6() throws Exception {
        // given
        ShoppingChart shoppingChart = new ShoppingChart();
        shoppingChart.setUser(new User("USER001",true,201));
        shoppingChart.add(new Item("ITEM000000", "可口可乐", "瓶", 3.00, 1, 0.9,false));


        // when
        Pos pos = new Pos();
        String actualShoppingList = pos.getShoppingList(shoppingChart);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");


        // then
        String expectedShoppingList = new String();
        expectedShoppingList =
                "***商店购物清单***\n"
                        + "会员编号：  USER001    会员积分：  201分\n"
                        + "----------------------\n"
                        +"打印时间："
                        +new String(dateFormat.format(new Date()))+"\n"
                        +"----------------------\n"
                        + "名称：可口可乐，数量：1瓶，单价：3.00(元)，小计：2.70(元)\n"
                        + "----------------------\n"
                        + "总计：2.70(元)\n"
                        + "节省：0.30(元)\n"
                        + "**********************\n";
        assertThat(actualShoppingList, is(expectedShoppingList));
    }
    @Test//会员积分200-500，5元区
    public void testGetCorrectShoppingListFor200to500TwoItemsWithVIPDiscountForReq6() throws Exception {
        // given
        ShoppingChart shoppingChart = new ShoppingChart();
        shoppingChart.setUser(new User("USER001",true,201));
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
                        + "会员编号：  USER001    会员积分：  204分\n"
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
    @Test//会员积分200-500，10元区
    public void testGetCorrectShoppingListFor200to500ForItemsWithVIPDiscountForReq6() throws Exception {
        // given
        ShoppingChart shoppingChart = new ShoppingChart();
        shoppingChart.setUser(new User("USER001",true,201));
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
                        + "会员编号：  USER001    会员积分：  207分\n"
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
    @Test//会员积分大于500，0元区
    public void testGetCorrectShoppingListForMoreThan500SingleItemForReq6() throws Exception {
        // given
        ShoppingChart shoppingChart = new ShoppingChart();
        shoppingChart.setUser(new User("USER001",true,501));
        shoppingChart.add(new Item("ITEM000000", "可口可乐", "瓶", 3.00, 1,0.9,false));


        // when
        Pos pos = new Pos();
        String actualShoppingList = pos.getShoppingList(shoppingChart);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");


        // then
        String expectedShoppingList = new String();
        expectedShoppingList =
                "***商店购物清单***\n"
                        + "会员编号：  USER001    会员积分：  501分\n"
                        + "----------------------\n"
                        +"打印时间："
                        +new String(dateFormat.format(new Date()))+"\n"
                        +"----------------------\n"
                        + "名称：可口可乐，数量：1瓶，单价：3.00(元)，小计：2.70(元)\n"
                        + "----------------------\n"
                        + "总计：2.70(元)\n"
                        + "节省：0.30(元)\n"
                        + "**********************\n";
        assertThat(actualShoppingList, is(expectedShoppingList));
    }
    @Test//会员积分大于500，5元区
    public void testGetCorrectShoppingListForMoreThan500SingleMultiItem1ForReq6() throws Exception {
        // given
        ShoppingChart shoppingChart = new ShoppingChart();
        shoppingChart.setUser(new User("USER001",true,501));
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
                        + "会员编号：  USER001    会员积分：  506分\n"
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
    @Test//会员积分大于500，10元区
    public void testGetCorrectShoppingListForMoreThan500SingleMultiItem2ForReq6() throws Exception {
        // given
        ShoppingChart shoppingChart = new ShoppingChart();
        shoppingChart.setUser(new User("USER001",true,501));
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
                        + "会员编号：  USER001    会员积分：  511分\n"
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