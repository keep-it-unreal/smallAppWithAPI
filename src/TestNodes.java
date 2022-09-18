import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import java.io.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;


public class TestNodes {
    public static void testNodes() throws IOException, SQLException {
        TestImport.testImport();
        JSONObject x = Request.nodesR("069cb8d7-bbdd-47d3-ad8f-82ef4c269df1");
        Map dict_x = (Map) x;
        System.out.println(((ArrayList)x.get("children")).size() == 1);

    }
}
