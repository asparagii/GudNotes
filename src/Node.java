import java.util.ArrayList;
import java.util.List;


// Basic node class

public class Node {
    private List<Node> parents;
    private List<Node> children;

    Node(){
        parents = new ArrayList<>();
        children = new ArrayList<>();
    }

    void addChild(Node n){
        children.add(n);
    }

    void addParent(Node n){
        parents.add(n);
    }

    List getChildren(){
        return this.children;
    }

    List getParents(){
        return this.parents;
    }
}
