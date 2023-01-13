package my.app.controller;

import lombok.RequiredArgsConstructor;
import my.app.entity.Node;
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
    public Node findById(@PathVariable String id){
        return service.findById(id);
    }

    @GetMapping("/getAllNodes")
    public List<Node> getAllNodes(){
        return service.findAll();
    }

    @PostMapping(value = "/createNode", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Node> createNode(@RequestBody Node node){
        service.save(node);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping(value = "/createSomeNodes", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Node> createSomeNode(@RequestBody List<Node> list){
        service.saveAll(list);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping(value = "/updateNode", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Node> updateNode(@RequestBody Node node){
        service.updateNode(node);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/deleteNode/{id}")
    public ResponseEntity<Node> deleteNode(@PathVariable String id){
        service.deleteNode(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
