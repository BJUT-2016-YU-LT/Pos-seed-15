import com.thoughtworks.pos.common.EmptyShoppingCartException;
import com.thoughtworks.pos.domains.Item;
import com.thoughtworks.pos.domains.ShoppingChart;
import com.thoughtworks.pos.services.services.InputParser;
import javafx.geometry.Pos;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * Created by ZXR on 2016/6/24.
 */
//需求1测试。
public class TestFileInputS01 {
    public static void main(String[] args) throws IOException, EmptyShoppingCartException {
        TestFileInputS01 testFileInputS01=new TestFileInputS01();
        testFileInputS01.inputandoutput();
    }
    public void inputandoutput() throws IOException, EmptyShoppingCartException {
        InputParser inputParser = new InputParser(new File("src/filesrc/data01.json"));
        ArrayList<Item> items = inputParser.parser().getItems();
        ShoppingChart shoppingChart=new ShoppingChart();
        for(Item item:items)
        shoppingChart.add(item);
        com.thoughtworks.pos.domains.Pos pos = new com.thoughtworks.pos.domains.Pos();
        System.out.println(pos.getShoppingList(shoppingChart));
    }

}
