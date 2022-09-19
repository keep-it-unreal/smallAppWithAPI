import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Map;

public class Test {

    public static void testImport() throws IOException, SQLException {
        String s = File.separator;
        String file = "C:" + s + "Users" + s + "79022" + s + "Desktop" + s + "Java" +
                s + "smallApp2" + s + "Text" + s + "ForTest.txt";
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
        System.out.println(parent_1 == parent);
        System.out.println(parent.size == 384);
        System.out.println(parent.parent.size == 384);
    }

    public static void testNodes() {
        JSONObject x = Request.nodesR("069cb8d7-bbdd-47d3-ad8f-82ef4c269df1");
        Map dict_x = (Map) x;
        System.out.println(((ArrayList) x.get("children")).size() == 1);
    }

    public static void testUpdates() throws IOException, SQLException {
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

    public static void testDelete() {

        Request.deleteR("069cb8d7-bbdd-47d3-ad8f-82ef4c269df1");
        try {
            JSONObject y = Request.nodesR("863e1a7a-1304-42ae-943b-179184c077e3");
            System.out.println(false);
        } catch (NullPointerException e) {
            System.out.println(true);
        }
    }

    public static void testExportInDB() throws IOException, SQLException {
        Test.testImport();
        DBActions.exportInDB();
        String sql;
        String size = "";
        try (var connection = ConnectManager.get();
             var statement = connection.createStatement()) {
            sql = "SELECT size FROM treeStr WHERE id = '069cb8d7-bbdd-47d3-ad8f-82ef4c269df1'";
            var executeResult = statement.executeQuery(sql);
            while (executeResult.next()) {
                size = executeResult.getString("size");
            }
        }
        System.out.println(size.equals("384"));
        Test.testUpdates();
        DBActions.exportInDB();
        try (var connection = ConnectManager.get();
             var statement = connection.createStatement()) {
            sql = "SELECT size FROM treeStr WHERE id = '069cb8d7-bbdd-47d3-ad8f-82ef4c269df1'";
            var executeResult = statement.executeQuery(sql);
            while (executeResult.next()) {
                size = executeResult.getString("size");
            }
        }
        System.out.println(size.equals("128"));
    }

    public static void testImportFromDB() throws SQLException{
        DBActions.importFromDB();
        System.out.println(TreeStr.mainFolder.get("069cb8d7-bbdd-47d3-ad8f-82ef4c269fobo").children.size() == 2);
        System.out.println(TreeStr.mainFolder.get("863e1a7a-1304-42ae-943b-179184c077e3").parent.id.equals("d515e43f-f3f6-4471-bb77-6b455017a2d2"));
    }
}
