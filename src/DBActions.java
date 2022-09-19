import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class DBActions {

    public static void exportInDB() throws SQLException{
        HashMap<String, String> newFolder;
        String sql, id, url, size, type, parentID, updateDate;
        StringBuilder children;
        PreparedStatement preparedStatement;
        try (var connection = ConnectManager.get()){

            for(Node i: Request.importNewItems) {
                newFolder = i.getAllAttributes();
                sql = """
                        INSERT INTO treeStr (id, url, size, type, parent_id, children_id)
                        VALUES (?, ?, ?, ?, ?, ?);
                        """;
                preparedStatement = connection.prepareStatement(sql);

                preparedStatement.setString(1, newFolder.get("id"));
                preparedStatement.setString(2, newFolder.get("url"));
                preparedStatement.setString(3, newFolder.get("size"));
                preparedStatement.setString(4, newFolder.get("type"));
                preparedStatement.setString(5, newFolder.get("parentId"));
                preparedStatement.setString(6, newFolder.get("children"));
                preparedStatement.execute();

            }
            Request.importNewItems.clear();
            for(Node i: Request.importUpdateItems) {
                newFolder = i.getAllAttributes();
                sql = "UPDATE treeStr SET url = ?, size = ?, parent_id = ?, children_id = ? where id = ?;";
                preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setString(1, newFolder.get("url"));
                preparedStatement.setString(2, newFolder.get("size"));
                preparedStatement.setString(3, newFolder.get("parentId"));
                preparedStatement.setString(4, newFolder.get("children"));
                preparedStatement.setString(5, newFolder.get("id"));
                preparedStatement.execute();
            }
            Request.importUpdateItems.clear();
        }
    }

    public static void importFromDB() throws SQLException {
        String id, url, type, parentId,updateDate, size;
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
                parentId = executeResult.getString("parent_id");
                //updateDate = executeResult.getString("update_date"); - пока не используем
                item = new HashMap();
                item.put("id", id);
                item.put("url", url);
                item.put("size", size);
                item.put("type", type);
                item.put("parentId", parentId);
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
