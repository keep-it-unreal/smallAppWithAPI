import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.postgresql.Driver;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TestDB {

    public static void exportInDB() throws SQLException{
        Map newFolder;
        String sql, id, url, size, type, parentID, updateDate;
        StringBuilder children;
        PreparedStatement preparedStatement;
        try (var connection = ConnectManager.get();
             var statement = connection.createStatement()){

            for(Node i: Request.importNewItems) {
                newFolder = i.getInfo();
                sql = """
                        INSERT INTO treeStr (id, url, size, type, parent_id, children_id)
                        VALUES (?, ?, ?, ?, ?, ?);
                        """;
                preparedStatement = connection.prepareStatement(sql);
                id = newFolder.get("id").toString();
                url = newFolder.get("url").toString();
                size = newFolder.get("size").toString();
                type = newFolder.get("type").toString();
                parentID = newFolder.get("parentID").toString();
                children = new StringBuilder();
                for(Node child: TreeStr.mainFolder.get(id).children){
                    children.append(", " + child.id);
                }
                preparedStatement.setString(1, id);
                preparedStatement.setString(2, url);
                preparedStatement.setString(3, size);
                preparedStatement.setString(4, type);
                preparedStatement.setString(5, parentID);
                preparedStatement.setString(6, children.toString());
                preparedStatement.execute();

            }
            for(Node i: Request.importUpdateItems) {
                newFolder = i.getInfo();
                sql = "UPDATE treeStr SET url = ?, size = ?, parent_id = ?, children_id = ? where id = ?;";
                preparedStatement = connection.prepareStatement(sql);
                id = newFolder.get("id").toString();
                url = newFolder.get("url").toString();
                size = newFolder.get("size").toString();
                parentID = newFolder.get("parentID").toString();
                children = new StringBuilder();
                for(Node child: TreeStr.mainFolder.get(id).children){
                    children.append(", " + child.id);
                }
                preparedStatement.setString(1, url);
                preparedStatement.setString(2, size);
                preparedStatement.setString(3, parentID);
                preparedStatement.setString(4, children.toString());
                preparedStatement.setString(5, id);
                preparedStatement.execute();

            }
        }
    }

    public static void importFromDB() throws SQLException {
        String id, url, type, parentID,updateDate, size;
        JSONArray itemsList = new JSONArray();
        Map item;
        String sql = """
        SELECT * FROM treeStr;
""";

        try (var connection = ConnectManager.get();
        var statement = connection.createStatement()){
            var executeResult = statement.executeQuery(sql);
            while(executeResult.next()){
                id = executeResult.getString("id");
                url = executeResult.getString("url");
                size = executeResult.getString("size");
                type = executeResult.getString("type");
                parentID = executeResult.getString("parent_id");
                //updateDate = executeResult.getString("update_date"); - пока не используем
                item = new HashMap();
                item.put("id", id);
                item.put("url", url);
                item.put("size", size);
                item.put("type", type);
                item.put("parentID", parentID);
                itemsList.add(new JSONObject(item));
            }
        }
        JSONObject items = new JSONObject();
        items.put("items", itemsList);
        Request.importR(items);

    }

    public static void createDB() throws SQLException {
        String sql = """
                CREATE TABLE treeStr(
                    id VARCHAR,
                    url VARCHAR,
                    size INT,
                    type VARCHAR,
                    parent_id VARCHAR,
                    children_id VARCHAR,
                    update_date VARCHAR
                );
                """;

        try (var connection = ConnectManager.get();
             var statement = connection.createStatement()) {
            var executeResult = statement.execute(sql);
        }
    }
}
