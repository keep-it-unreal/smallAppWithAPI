package my.app.entity;

import jakarta.persistence.*;
import lombok.Data;
import my.app.Application;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Nodes")
@Data
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
        if (parentId == null)
            return;

        Node parentEntity = Application.mainFolder.get(parentId);
        if (parentEntity != null && !this.parentId.equalsIgnoreCase(Application.mainFolder.get(this.Id).parentId)) {
            changingParent();
        }

        if(!parentEntity.children.contains(this.Id)) {
            parentEntity.children.add(this.Id);
            parentEntity.addingSumSize(this.size);
        }
    }
    public void changingParent() {
        Node parentEntity = Application.mainFolder.get(parentId);
        parentEntity.addingSumSize(-this.size);
        parentEntity.children.remove(this.Id);
    }

    public void deleteChildren()  {
        for (String childId : children) {
            Application.mainFolder.get(childId).deleteChildren();
        }
        Application.mainFolder.remove(this.Id);
        Application.updatesItems.add(this);
    }
    public void deleting(){
        this.deleteChildren();
        Node parentEntity = Application.mainFolder.get(this.parentId);
        if (parentEntity != null)
            parentEntity.getChildren().remove(this);
        if (this.size != 0){
            addingSumSize(-this.size);
        }
    }
}
