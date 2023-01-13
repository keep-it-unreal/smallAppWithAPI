package my.app.repository;

import my.app.entity.Node;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NodeRepository extends JpaRepository<Node, String> {
}
