package my.app.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import my.app.Application;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Nodes")
@Getter @Setter
@RequiredArgsConstructor
public class Node {
    @Id
    String Id;
    String url;
    String type;
    String parentId;
    int size = 0;
    @ManyToOne
    Node parentEntity;
    @OneToMany
    List<Node> children = new ArrayList<>();

    public void addingSumSize(int size) {
        if (size == 0)
            return;

        this.size += size;
        Application.updatesItems.add(this);
        if (parentEntity != null) {
            parentEntity.addingSumSize(size);
        }
    }

    public void addingParent(String parentId) {
        if (parentId == null || parentId.equalsIgnoreCase("None"))
            return;

//        if(!Application.mainFolder.containsKey(parentId)){
//            Node parent = new Node();
//            parent.setId(parentId);
//            parent.setType("FOLDER");
//            Application.mainFolder.put(parentId, parent);
//        }

        if (this.parentEntity != null && !this.parentEntity.getId().equalsIgnoreCase(parentId)) {
            changingParent();
        }

        if(!Application.mainFolder.get(parentId).children.contains(this)) {
            Application.mainFolder.get(parentId).children.add(this);
            parentEntity = Application.mainFolder.get(parentId);
            parentEntity.addingSumSize(this.size);
        }
    }
    public void changingParent() {
        parentEntity.addingSumSize(-this.size);
        this.parentEntity.children.remove(this);
    }

    public void deleting()  {
        for (Node child : children) {
            child.deleting();
        }
        Application.mainFolder.remove(Id);
    }
}
