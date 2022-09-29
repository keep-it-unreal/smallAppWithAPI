package fileManager;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class ConnectManager {

    private static final String USERNAME = "postgres";
    private static final String PASSWORD = "Zil131";
    private static final String URL = "jdbc:postgresql://localhost:5432/TreeStr";

    private ConnectManager(){

    }
    public static Connection get(){
        try {
            return DriverManager.getConnection(URL, USERNAME, PASSWORD);
        }
        catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

}
