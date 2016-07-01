import com.thoughtworks.pos.common.EmptyIndex;
import com.thoughtworks.pos.common.EmptyShoppingCartException;
import com.thoughtworks.pos.common.PromoteAndTwo;
import com.thoughtworks.pos.domains.Item;
import com.thoughtworks.pos.domains.ShoppingChart;
import com.thoughtworks.pos.domains.User;
import com.thoughtworks.pos.services.services.InputParser;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by ZXR on 2016/6/24.
 */
public class TestFileInputS05 {
    public static void main(String[] args) throws IOException, EmptyShoppingCartException, EmptyIndex, PromoteAndTwo {
        TestFileInputS05 testFileInputS05=new TestFileInputS05();
        testFileInputS05.inputandoutput();
    }
    public void inputandoutput() throws IOException, EmptyShoppingCartException, EmptyIndex, PromoteAndTwo {
        InputParser inputParser = new InputParser(new File("src/filesrc/data_index05.json"),new File("src/filesrc/data_list05.json"),new File("src/filesrc/data_user05.json"));
        ArrayList<Item> items = inputParser.parserThreefile().getItems();
        User user=inputParser.parserThreefile().getUser();
        ShoppingChart shoppingChart=new ShoppingChart();
        for(Item item:items)
            shoppingChart.add(item);
        shoppingChart.setUser(user);
        com.thoughtworks.pos.domains.Pos pos = new com.thoughtworks.pos.domains.Pos();
        System.out.println(pos.getShoppingList(shoppingChart));
    }

}