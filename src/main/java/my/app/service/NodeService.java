package my.app.service;

import lombok.RequiredArgsConstructor;
import my.app.Application;
import my.app.entity.Node;
import my.app.repository.NodeRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NodeService {

    private final NodeRepository repository;

    public Node findById(String id){
        return repository.findById(id).orElseThrow(() -> new RuntimeException("Node not found, id = " + id));
    }

    public List<Node> findAll(){
        return repository.findAll();
    }

    public Node save(Node node){
        if(node.getParentId() != null && !Application.mainFolder.containsKey(node.getParentId())){
            Node parent = new Node();
            parent.setId(node.getParentId());
            parent.setType("FOLDER");
            Application.mainFolder.put(node.getParentId(), parent);
            save(parent);
        }
        node.addingParent(node.getParentId());
        return repository.save(node);
    }

    public void saveAll(List<Node> list) {
        String id;
        for (Node n : list) {
            id = n.getId();
            if (!Application.mainFolder.containsKey(id)) {
                Application.mainFolder.put(id, n);
                save(n);
            } else {
                updateNode(n);
            }
        }
    }

    public Node updateNode(Node node){
        findById(node.getId());
        node.changingParent();
        return save(node);
    }

    public void deleteNode(String id){
        findById(id);
        Node node = Application.mainFolder.get(id);
        node.deleting();
        if (node.getParentEntity() != null)
            node.getParentEntity().getChildren().remove(node);
        if (node.getSize() != 0){
            node.addingSumSize(-node.getSize());
        }
        for (Node n:Application.updatesItems) {
            updateNode(n);
        }
        repository.deleteById(id);
    }
}
