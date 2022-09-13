import org.json.simple.JSONObject;

import java.io.IOException;

public class TestDelete {
    public static void main(String[] args) throws IOException {
        TestImport.testImport();
        TestNodes.testNodes();
        Request.deleteR("069cb8d7-bbdd-47d3-ad8f-82ef4c269df1");
        try{
            JSONObject y = Request.nodesR("863e1a7a-1304-42ae-943b-179184c077e3");
            System.out.println(false);
        }
        catch(NullPointerException e){
            System.out.println(true);
        }
    }
}
