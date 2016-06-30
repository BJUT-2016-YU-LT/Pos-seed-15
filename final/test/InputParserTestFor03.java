/**
 * Created by ZXR on 2016/6/26.
 */
//用于测试需求三


import com.thoughtworks.pos.common.EmptyIndex;
import com.thoughtworks.pos.common.EmptyShoppingCartException;
import com.thoughtworks.pos.common.PromoteAndTwo;
import com.thoughtworks.pos.domains.Item;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import com.thoughtworks.pos.services.services.InputParser;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class InputParserTestFor03 {

    private File file_index;
    private File file_list;
    @Before
    public void setUp() throws Exception {
        file_index = new File("src/filesrc/readfile_index.json");
        file_list = new File("src/filesrc/readfile_list.json");
    }

    @After
    public void tearDown() throws Exception {
        if(file_index.exists()){
            file_index.delete();
        }
        if(file_list.exists()){
            file_list.delete();
        }
    }

    @Test//输入输出文件里都只有一个商品，不打折
    public void testParseJsonFileToItems() throws Exception {
        PrintWriter printWriter_index = new PrintWriter(file_index);
        String sampleInput_index = new StringBuilder()
                .append("[\n")
                .append("{\n")
                .append("\"ITEM000000\":")
                .append("{\n")
                .append("\"name\": '电池',\n")
                .append("\"unit\": '个',\n")
                .append("\"price\": 2.00,\n")
                .append("\"discount\": 0.8\n")
                .append("}\n")
                .append("}\n")
                .append("]")
                .toString();

        printWriter_index.write(sampleInput_index);
        printWriter_index.close();
        PrintWriter printWriter_list = new PrintWriter(file_list);
        String sampleInput_list = new StringBuilder()
                .append("[\n")
                .append("\"ITEM000000\"")
                .append("]")
                .toString();

        printWriter_list.write(sampleInput_list);
        printWriter_list.close();

        InputParser inputParser = new InputParser(file_index,file_list);
        ArrayList<Item> items = inputParser.parsertwofile().getItems();

        assertThat(items.size(), is(1));
        Item item = items.get(0);
        assertThat(item.getName(), is("电池"));
        assertThat(item.getBarcode(), is("ITEM000000"));
        assertThat(item.getUnit(), is("个"));
        assertThat(item.getPrice(), is(2.00));
        assertThat(item.getDiscount(), is(0.8));
    }

    @Test//输入输出文件里都只有一个商品，且打折
    public void testParseJsonWhenHasNoDiscount() throws Exception {
        PrintWriter printWriter_index = new PrintWriter(file_index);
        String sampleInput_index = new StringBuilder()
                .append("[\n")
                .append("{\n")
                .append("\"ITEM000000\":")
                .append("{\n")
                .append("\"name\": '电池',\n")
                .append("\"unit\": '个',\n")
                .append("\"price\": 2.00\n")
                .append("}\n")
                .append("}\n")
                .append("]")
                .toString();

        printWriter_index.write(sampleInput_index);
        printWriter_index.close();
        PrintWriter printWriter_list = new PrintWriter(file_list);
        String sampleInput_list = new StringBuilder()
                .append("[\n")
                .append("\"ITEM000000\"")
                .append("]")
                .toString();

        printWriter_list.write(sampleInput_list);
        printWriter_list.close();

        InputParser inputParser = new InputParser(file_index,file_list);
        ArrayList<Item> items = inputParser.parsertwofile().getItems();

        assertThat(items.size(), is(1));
        Item item = items.get(0);
        assertThat(item.getName(), is("电池"));
        assertThat(item.getBarcode(), is("ITEM000000"));
        assertThat(item.getUnit(), is("个"));
        assertThat(item.getPrice(), is(2.00));
    }

    @Test(expected = PromoteAndTwo.class)//打折又赠送
    public void testParseJsonWhenPromoteAndGift() throws Exception {
        PrintWriter printWriter_index = new PrintWriter(file_index);
        String sampleInput_index = new StringBuilder()
                .append("[\n")
                .append("{\n")
                .append("\"ITEM000000\":")
                .append("{\n")
                .append("\"name\": '电池',\n")
                .append("\"unit\": '个',\n")
                .append("\"price\": 2.00,\n")
                .append("\"discount\": 0.8,\n")
                .append("\"promotion\":true\n")
                .append("}\n")
                .append("}\n")
                .append("]")
                .toString();

        printWriter_index.write(sampleInput_index);
        printWriter_index.close();
        PrintWriter printWriter_list = new PrintWriter(file_list);
        String sampleInput_list = new StringBuilder()
                .append("[\n")
                .append("\"ITEM000000\"")
                .append("]")
                .toString();

        printWriter_list.write(sampleInput_list);
        printWriter_list.close();

        InputParser inputParser = new InputParser(file_index,file_list);
        ArrayList<Item> items = inputParser.parsertwofile().getItems();


    }


}

