package fileManager;

import org.json.simple.JSONObject;
import org.springframework.web.bind.annotation.*;
import java.sql.SQLException;

@RestController
class FileController {
    @PostMapping("/imports")
    void importNewNodes(@RequestBody String nodes) throws SQLException {
        nodes = nodes.replaceAll(System.getProperty("line.separator"), "");
        nodes = nodes.replaceAll(" ", "");
        nodes = nodes.replaceAll("None", "\"None\"");

        JSONObject json1 = Request.parse(nodes);
        Request.importR(json1);
        DBActions.exportInDB();
    }

    @GetMapping("/Nodes/{id}")
    JSONObject getNodes(@PathVariable String id) {
        return Request.nodesR(id);
    }

    @DeleteMapping("/delete/{id}")
    void deleteNodes(@PathVariable String id) throws SQLException{
        Request.deleteR(id);
        DBActions.exportInDB();
    }
}