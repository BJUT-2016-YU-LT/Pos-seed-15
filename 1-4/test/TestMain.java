import com.thoughtworks.pos.domains.Item;
import com.thoughtworks.pos.domains.Pos;
import com.thoughtworks.pos.domains.ShoppingChart;
import com.thoughtworks.pos.common.EmptyShoppingCartException;
import java.io.IOException;
import java.util.Scanner;

/**
 * Created by liyue on 2016/6/21
 */
public class TestMain
{
    public static void main(String[] args)throws Exception
    {
        int x;
        Scanner input=new Scanner(System.in);
        TestMain test=new TestMain();
        Pos pos = new Pos();
        ShoppingChart shoppingChart = new ShoppingChart();
        test.displayInformation();
        System.out.println("输入你的选择：");
        x=input.nextInt();
        while(1<=x&&x<=3)
        {
            if(x==1)
            {
                Item cokeCola = new Item("ITEM000000", "可口可乐", "瓶", 3.00);
                shoppingChart.add(cokeCola);
            }
            if(x==2)
            {
                Item sprite = new Item("ITEM000001", "雪碧", "瓶", 3.00);
                shoppingChart.add(sprite);
            }
            if(x==3)
            {
                //Item battery = new Item("ITEM000004", "电池", "个", 2.00);
                Item battery = new Item("ITEM000004", "电池", "个", 2.00,0.8);
                shoppingChart.add(battery);
            }
            System.out.println("\n \n \n");
            test.displayInformation();
            System.out.println("购物车中已有" + shoppingChart.getItems().size()+"件商品");
            System.out.println();
            System.out.print("输入你的选择：");
            x=input.nextInt();
        }
        if(x==4)
        {
            System.out.println("\n \n \n");
            String actualShoppingList = pos.getShoppingList(shoppingChart);
            System.out.println(actualShoppingList);
        }
    }
    public void displayInformation(){
        System.out.println("1.ITEM000000 可口可乐  3.00/瓶");
        System.out.println("2.ITEM000001 雪碧      3.00/瓶");
       //System.out.println("3.ITEM000004 电池      2.00/个");
        System.out.println("3.ITEM000004 电池      2.00元/个  折扣:0.8");
        System.out.println("4.打印账单");
    }
}