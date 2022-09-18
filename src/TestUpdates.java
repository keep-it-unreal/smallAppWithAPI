import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import java.io.File;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

public class TestUpdates {
    public static void testUpdates() throws Exception {
        String s = File.separator;
        String file = "C:" + s + "Users" + s + "79022" + s + "Desktop" + s + "Java" +
                s + "smallApp2" + s + "Text" + s + "ForTest_update.txt";
        String x = new String(Files.readAllBytes(Paths.get(file)), StandardCharsets.UTF_8);
        x = x.replaceAll(System.getProperty("line.separator"), "");
        x = x.replaceAll(" ", "");
        x = x.replaceAll("None", "\"None\"");


        Object obj = JSONValue.parse(x);
        JSONArray json = (JSONArray) obj;

        Request.importR((JSONObject) json.get(0));
        Node parent = TreeStr.mainFolder.get("069cb8d7-bbdd-47d3-ad8f-82ef4c269df1");
        System.out.println(parent.children.get(0).size == 384);
        Request.importR((JSONObject) json.get(1));
        System.out.println(parent.size == 128);
        System.out.println(parent.parent.size == 384);
        System.out.println(parent.parent.children.size() == 2);
    }
}
