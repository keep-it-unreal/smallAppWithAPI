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

    public Node updateNode(Node node){
        findById(node.getId());
        Node nodeBeforeChange = Application.mainFolder.get(node.getId());
        if(node.getSize() != nodeBeforeChange.getSize()){
            int changingSize = node.getSize() - nodeBeforeChange.getSize();
            Application.mainFolder.get(node.getParentId()).addingSumSize(changingSize);
        }
        return save(node);
    }

    public void deleteNode(String id){
        findById(id);
        repository.deleteById(id);
    }
}
