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
    List<String> children = new ArrayList<>();

    public void addingSumSize(int size) {
        if (size == 0)
            return;

        this.size += size;
        Application.updatesItems.add(this);

        Node parentEntity = Application.mainFolder.get(this.parentId);
        if (parentEntity != null) {
            parentEntity.addingSumSize(size);
        }
    }

    public void addingParent(String parentId) {
        if (parentId == null || parentId.equalsIgnoreCase("None"))
            return;

        if(!Application.mainFolder.containsKey(parentId)){
            Node parentEntity = new Node();
            parentEntity.setId(parentId);
            parentEntity.setType("FOLDER");
            Application.mainFolder.put(parentId, parentEntity);
        }

        Node parentEntity = Application.mainFolder.get(parentId);
        if (parentEntity != null && !this.parentId.equalsIgnoreCase(parentId)) {
            changingParent();
        }

        if(!parentEntity.children.contains(this.Id)) {
            parentEntity.children.add(this.Id);
            parentEntity.addingSumSize(this.size);
        }
//        Application.updatesItems.add(parentEntity);
    }
    public void changingParent() {
        Node parentEntity = Application.mainFolder.get(parentId);
        parentEntity.addingSumSize(-this.size);
        parentEntity.children.remove(this.Id);
    }

    public void deleting()  {
        for (String childId : children) {
            Application.mainFolder.get(childId).deleting();
        }
        Application.mainFolder.remove(this.Id);
        Application.updatesItems.add(this);
    }
}
