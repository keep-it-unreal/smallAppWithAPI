package fileManager;

import java.io.IOException;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) throws IOException, SQLException {
        DBActions.importFromDB();
        Test.testImport();
        //Test.testNodes();
        Test.testUpdates();
        //DBActions.createDB();
        DBActions.exportInDB();
        //Test.testDelete();
        //Test.testExportInDB();
        //Test.testImportFromDB();
    }
}
