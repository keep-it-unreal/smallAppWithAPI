package my.app.controller;

import my.app.entity.Node;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
class NodeControllerTest {
    @Autowired
    NodeController controller;
    ResponseEntity<?> response;

    /**
     In this test we check method .createNode().
     **/
    @Test
    void createNode(){
        Node node = new Node();
        node.setId("d515e43f-f3f6-4471-bb77-6b455017a2d2");
        node.setType("FOLDER");
        node.setParentId("069cb8d7-bbdd-47d3-ad8f-82ef4c269df1");
        response = controller.createNode(node);
        int status = response.getStatusCode().value();
        assertThat(status).isEqualTo(201);
    }

    /**
     In this test we check method .findById().
     **/
    @Test
    void findById() {
        Node node = new Node();
        node.setId("d515e43f-f3f6-4471-bb77-6b455017a2d2");
        node.setType("FOLDER");
        node.setParentId("069cb8d7-bbdd-47d3-ad8f-82ef4c269df1");
        response = controller.createNode(node);
        response = controller.findById("d515e43f-f3f6-4471-bb77-6b455017a2d2");
        assertThat(((Node)response.getBody()).getParentId()).isEqualTo("069cb8d7-bbdd-47d3-ad8f-82ef4c269df1");
    }

    @Test
    void getAllNodes() {
        Node node = new Node();
        node.setId("d515e43f-f3f6-4471-bb77-6b455017a2d2");
        node.setType("FOLDER");
        node.setParentId("069cb8d7-bbdd-47d3-ad8f-82ef4c269df1");
        response = controller.createNode(node);
        node = new Node();
        node.setId("863e1a7a-1304-42ae-943b-179184c077e3");
        node.setType("FILE");
        node.setSize(128);
        node.setParentId("d515e43f-f3f6-4471-bb77-6b455017a2d2");
        response = controller.createNode(node);
        response = controller.getAllNodes();
        assertThat(((List<Node>)response.getBody()).size()).isEqualTo(3);
    }

    @Test
    void createSomeNode() {
        List<Node> list = new ArrayList<>();
        Node node = new Node();
        node.setId("d515e43f-f3f6-4471-bb77-6b455017a2d2");
        node.setType("FOLDER");
        node.setParentId("069cb8d7-bbdd-47d3-ad8f-82ef4c269df1");
        list.add(node);
        node = new Node();
        node.setId("863e1a7a-1304-42ae-943b-179184c077e3");
        node.setType("FILE");
        node.setSize(128);
        node.setParentId("d515e43f-f3f6-4471-bb77-6b455017a2d2");
        list.add(node);
        assertThat(controller.createSomeNode(list).getStatusCode().value()).isEqualTo(201);
    }

    @Test
    void updateNode() {
        Node node = new Node();
        node.setId("863e1a7a-1304-42ae-943b-179184c077e3");
        node.setType("FILE");
        node.setSize(128);
        node.setParentId("d515e43f-f3f6-4471-bb77-6b455017a2d2");
        controller.createNode(node);
        node.setSize(257);
        node = (Node) controller.updateNode(node).getBody();
        assertThat(node.getSize()).isEqualTo(257);
    }

    @Test
    void deleteNode() {
        Node node = new Node();
        node.setId("863e1a7a-1304-42ae-943b-179184c077e3");
        node.setType("FILE");
        node.setSize(128);
        node.setParentId("d515e43f-f3f6-4471-bb77-6b455017a2d2");
        controller.createNode(node);
        controller.deleteNode("863e1a7a-1304-42ae-943b-179184c077e3");
        assertThat(controller.findById("863e1a7a-1304-42ae-943b-179184c077e3").getStatusCode().value()).isEqualTo(404);
    }
}