import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.json.simple.JSONObject;

import java.io.*;

public class TestImport {
    public static void testImport() throws IOException {
        String s = File.separator;
        String file = "C:" + s + "Users" + s + "User" + s + "Desktop" + s + "Java" + s + "smallApp" + s + "ForTest.txt";
        String x = new String(Files.readAllBytes(Paths.get(file)), StandardCharsets.UTF_8);
        x = x.replaceAll(System.getProperty("line.separator"), "");
        x = x.replaceAll(" ", "");
        x = x.replaceAll("None", "\"None\"");


        JSONObject json1 = Request.parse(x);
        Request.importR(json1);

        Node parent = TreeStr.mainFolder.get("d515e43f-f3f6-4471-bb77-6b455017a2d2");
        System.out.println(parent.children.size() == 2);
        String a = "069cb8d7-bbdd-47d3-ad8f-82ef4c269df1";
        System.out.println(parent.parent.id.equals(a));

        Node parent_1 = TreeStr.mainFolder.get("b1d8fd7d-2ae3-47d5-b2f9-0f094af800d4").parent;
        System.out.println(parent_1==parent);
        System.out.println(parent.sumSize);
    }
}
