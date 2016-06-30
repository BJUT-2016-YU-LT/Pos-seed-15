import com.thoughtworks.pos.common.EmptyIndex;
import com.thoughtworks.pos.common.EmptyShoppingCartException;
import com.thoughtworks.pos.common.PromoteAndTwo;
import com.thoughtworks.pos.domains.Item;
import com.thoughtworks.pos.domains.ShoppingChart;
import com.thoughtworks.pos.services.services.InputParser;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by ZXR on 2016/6/24.
 */
public class TestFileInputS04 {
    public static void main(String[] args) throws IOException, EmptyShoppingCartException, EmptyIndex, PromoteAndTwo {
        TestFileInputS04 testFileInputS04=new TestFileInputS04();
        testFileInputS04.inputandoutput();
    }
    public void inputandoutput() throws IOException, EmptyShoppingCartException, EmptyIndex, PromoteAndTwo {
        InputParser inputParser = new InputParser(new File("src/filesrc/data_index04.json"),new File("src/filesrc/data_list04.json"));
        ArrayList<Item> items = inputParser.parsertwofile().getItems();
        ShoppingChart shoppingChart=new ShoppingChart();
        for(Item item:items)
            shoppingChart.add(item);
        com.thoughtworks.pos.domains.Pos pos = new com.thoughtworks.pos.domains.Pos();
        System.out.println(pos.getShoppingList(shoppingChart));
    }

}