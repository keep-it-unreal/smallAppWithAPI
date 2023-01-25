package my.app;

import my.app.entity.Node;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

@SpringBootApplication
public class Application {
    public static HashMap<String, Node> mainFolder = new HashMap<>();
    public static Set<Node> updatesItems = new HashSet<>();

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);

    }
}
