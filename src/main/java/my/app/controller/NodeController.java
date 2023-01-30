package my.app.controller;

import lombok.RequiredArgsConstructor;
import my.app.Application;
import my.app.entity.Node;
import my.app.repository.NodeRepository;
import my.app.service.NodeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.MediaType;

import java.util.List;


@RestController
@RequestMapping(value = "/smallApp", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class NodeController {

    private final NodeService service;

    @GetMapping("/getNode/{id}")
    public ResponseEntity<?> findById(@PathVariable String id){
        try {
            Node node = service.findById(id);
            return new ResponseEntity<>(node, HttpStatus.OK);
        } catch (RuntimeException e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e){
            return new ResponseEntity<>(e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/getAllNodes")
    public ResponseEntity<?> getAllNodes(){
        try {
            return new ResponseEntity<>(service.findAll(), HttpStatus.OK);
        } catch (Exception e){
            return new ResponseEntity<>(e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping(value = "/createNode", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createNode(@RequestBody Node node){
        try {
            String id = node.getId();
            if (!Application.mainFolder.containsKey(id)) {
                Application.mainFolder.put(id, node);
            } else {
                updateNode(node);
            }
            node = service.save(node);
            return new ResponseEntity<>(node, HttpStatus.CREATED);
        } catch (Exception e){
            return new ResponseEntity<>(e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping(value = "/createSomeNodes", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createSomeNode(@RequestBody List<Node> list){
        try {
            String id;
            for (Node n : list) {
                id = n.getId();
                if (!Application.mainFolder.containsKey(id)) {
                    Application.mainFolder.put(id, n);
                    createNode(n);
                } else {
                    updateNode(n);
                }
            }
            for (Node updatedNode : Application.updatesItems) {
                service.updateNode(updatedNode);
            }
            Application.updatesItems.clear();
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (Exception ex){
            return new ResponseEntity<>(ex, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping(value = "/updateNode", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateNode(@RequestBody Node node){
        try {
            node = service.updateNode(node);
            for (Node updatedNode : Application.updatesItems) {
                service.updateNode(updatedNode);
            }
            Application.updatesItems.clear();
            return new ResponseEntity<>(node, HttpStatus.OK);
        }  catch (RuntimeException e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e){
            return new ResponseEntity<>(e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/deleteNode/{id}")
    public ResponseEntity<?> deleteNode(@PathVariable String id){
        try {
            Node node = Application.mainFolder.get(id);
            node.deleting();
            for (Node deletedNode : Application.updatesItems) {
                service.deleteNode(deletedNode.getId());
            }
            Application.updatesItems.clear();
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (RuntimeException e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e){
            return new ResponseEntity<>(e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
